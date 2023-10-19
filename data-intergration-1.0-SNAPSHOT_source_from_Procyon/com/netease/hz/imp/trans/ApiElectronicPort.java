// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.SparkConf;
import java.lang.reflect.Field;
import org.dom4j.Document;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Element;
import com.netease.hz.imp.trans.dto.ElectronicEntryBody;
import com.netease.hz.imp.trans.dto.ElectronicEntryHead;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.dom4j.io.SAXReader;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.HttpUtil;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import com.netease.hz.imp.trans.util.EleDataBean;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Iterator;
import com.netease.hz.imp.trans.util.DateUtil;
import org.apache.commons.cli.CommandLine;
import com.netease.hz.imp.trans.util.JsonUtil;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.io.IOException;
import com.netease.hz.imp.trans.util.GpUtil;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.spark.sql.SparkSession;
import com.netease.hz.imp.trans.util.GpManager;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

public class ApiElectronicPort
{
    private static final Logger LOGGER;
    public static String URL;
    public static String APP_KEY;
    public static Boolean IS_TEST;
    public static Integer PAGE_SIZE;
    public static String APP_ID;
    private Map<String, Object> parameters;
    private List<Map<String, Object>> dynmcParameters;
    private String sinkDb;
    private String sinkTable;
    private Integer batchSize;
    private Integer isPage;
    private Integer pageSize;
    private Map<String, String> gpOption;
    private String gpSql;
    private GpManager gpManager;
    private String startDate;
    private String endDate;
    private SparkSession spark;
    private Integer isSpark;
    
    public String getStartDate() {
        return this.startDate;
    }
    
    public void setStartDate(final String startDate) {
        this.startDate = startDate;
    }
    
    public String getEndDate() {
        return this.endDate;
    }
    
    public void setEndDate(final String endDate) {
        this.endDate = endDate;
    }
    
    private void initTest() {
        final Map<String, Object> tradeCode = new HashMap<String, Object>();
        tradeCode.put("tradeCode", "3301950086");
        this.dynmcParameters.add(tradeCode);
        this.startDate = "2023-09-01";
        this.endDate = "2023-09-01";
    }
    
    public ApiElectronicPort(final String[] args) throws IOException, ParseException, Exception {
        this.dynmcParameters = new ArrayList<Map<String, Object>>();
        this.initOption(args);
        if (ApiElectronicPort.IS_TEST) {
            this.initTest();
        }
        this.gpOption = new GpUtil(this.sinkDb, this.sinkTable).getGpOption();
    }
    
    private void initOption(final String[] args) throws ParseException {
        final Options options = new Options();
        final Option url = new Option(null, "url", true, "set url");
        options.addOption(url);
        final Option sinkDb = new Option(null, "sinkDb", true, "set db");
        options.addOption(sinkDb);
        final Option sinkTable = new Option(null, "sinkTable", true, "set table");
        options.addOption(sinkTable);
        final Option params = new Option(null, "params", true, "set params");
        options.addOption(params);
        final Option schemas = new Option(null, "schemas", true, "set schemas");
        options.addOption(schemas);
        final Option isPage = new Option(null, "isPage", true, "set isPage");
        options.addOption(isPage);
        final Option isSpark = new Option(null, "isSpark", true, "set isSpark");
        options.addOption(isSpark);
        final Option isFirstArray = new Option(null, "isFirstArray", true, "set isFirstArray");
        options.addOption(isFirstArray);
        final Option arrayJsonPath = new Option(null, "arrayJsonPath", true, "set arrayJsonPath");
        options.addOption(arrayJsonPath);
        final GnuParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.sinkTable = commandLine.getOptionValue("sinkTable", "");
        this.sinkDb = commandLine.getOptionValue("sinkDb", "");
        this.parameters = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "0"));
    }
    
    private void initDynmcParameters(final String sql) throws Exception {
        this.gpManager = new GpManager();
        this.dynmcParameters = this.gpManager.query(sql);
        ApiElectronicPort.LOGGER.info("dynmcParameters params: {}" + this.dynmcParameters.size());
    }
    
    private String getContent(final Map<String, Object> dynmcParameter, final String declareDate, final Integer curPage, final Integer pageSize) {
        final String businessType = "EDP00201";
        final String senderId = "MA27U092";
        final String receiverId = "ZJPORT";
        final String generateTime = DateUtil.time2DateStr(System.currentTimeMillis(), "yyyyMMddHHmmss");
        String tradeCode = "";
        if (dynmcParameter != null && dynmcParameter.containsKey("tradeCode")) {
            tradeCode = dynmcParameter.get("tradeCode").toString();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("<mo version=\"1.0.0\" encoding=\"UTF-8\">");
        sb.append("<head>");
        sb.append(String.format("<businessType>%s</businessType>", businessType));
        sb.append(String.format("<senderId>%s</senderId>", senderId));
        sb.append(String.format("<receiverId>%s</receiverId>", receiverId));
        sb.append(String.format("<generateTime>%s</generateTime>", generateTime));
        sb.append("</head>");
        sb.append("<body><entryInfoSo>");
        sb.append(String.format("<tradeCode>%s</tradeCode>", tradeCode));
        sb.append(String.format("<declareDate>%s</declareDate>", declareDate));
        sb.append(String.format("<curPage>%s</curPage>", curPage));
        sb.append(String.format("<pageSize>%s</pageSize>", pageSize));
        sb.append("</entryInfoSo></body>");
        sb.append("</mo>");
        return sb.toString();
    }
    
    private Map<String, Object> initParams(final Map<String, Object> dynmcParameter, final String declareDate, final Integer curPage, final Integer pageSize) {
        final Map<String, Object> body = new HashMap<String, Object>();
        final String content = this.getContent(dynmcParameter, declareDate, curPage, pageSize);
        final String timestamp = String.valueOf(System.currentTimeMillis());
        for (final Map.Entry<String, Object> param : this.parameters.entrySet()) {
            if (param.getKey().equals("bizCode") || param.getKey().equals("bizId")) {
                body.put(param.getKey(), param.getValue());
            }
        }
        body.put("appId", ApiElectronicPort.APP_ID);
        body.put("content", content);
        body.put("timestamp", timestamp);
        final String sign = this.getSign(body);
        body.put("sign", sign);
        return body;
    }
    
    private String getSign(final Map<String, Object> body) {
        final String bizCode = body.get("bizCode").toString();
        final String bizId = body.get("bizId").toString();
        final String timestamp = body.get("timestamp").toString();
        final String content = body.get("content").toString();
        final String appKey = ApiElectronicPort.APP_KEY;
        final StringBuffer buffer = new StringBuffer();
        buffer.append("bizCode");
        buffer.append("=");
        buffer.append(bizCode);
        buffer.append("&");
        buffer.append("bizId");
        buffer.append("=");
        buffer.append(bizId);
        buffer.append("&");
        buffer.append("content");
        buffer.append("=");
        buffer.append(content);
        buffer.append("&");
        buffer.append("timestamp");
        buffer.append("=");
        buffer.append(timestamp);
        buffer.append("&");
        buffer.append("appKey");
        buffer.append("=");
        buffer.append(appKey);
        return DigestUtils.shaHex(buffer.toString());
    }
    
    private void sinkData(final List<EleDataBean> eleDataBeans) throws Exception {
        Integer retry = 3;
        while (true) {
            try {
                ApiElectronicPort.LOGGER.info(String.format("isSpark:%s eleDataBeans size:%s", this.isSpark, eleDataBeans.size()));
                if (eleDataBeans != null && eleDataBeans.size() > 0) {
                    if (this.isSpark == 1) {
                        final Dataset saleBillListDataset = SparkUtil.getEleDataset(eleDataBeans, this.spark);
                        saleBillListDataset.write().format("greenplum").options((Map)this.gpOption).mode(SaveMode.Append).save();
                    }
                    else {
                        final GpManager gpManager = new GpManager();
                        gpManager.eleBatchInsert(this.sinkDb, this.sinkTable, eleDataBeans);
                    }
                }
                else {
                    ApiElectronicPort.LOGGER.info("sind data no data");
                }
            }
            catch (Exception e) {
                ApiElectronicPort.LOGGER.info("sink Data exception" + e.getMessage());
                ApiElectronicPort.LOGGER.info("sleep 5 seconds");
                --retry;
                Thread.sleep(5000L);
                if (retry > 0) {
                    continue;
                }
            }
            break;
        }
    }
    
    private List<EleDataBean> getData(final Map<String, Object> dynmcParameter, final String declareDate, final Integer curPage, final Integer pageSize) throws Exception {
        final List<EleDataBean> eleDataBeans = new ArrayList<EleDataBean>();
        final Map<String, Object> params = this.initParams(dynmcParameter, declareDate, curPage, pageSize);
        final String result = HttpUtil.postMethod(ApiElectronicPort.URL, params);
        final JSONObject jsonObject = JSON.parseObject(result);
        final String message = jsonObject.getString("message");
        try {
            final SAXReader reader = new SAXReader();
            final Document document = reader.read(new ByteArrayInputStream(message.getBytes()));
            final Element root = document.getRootElement();
            final Element body = root.element("body");
            final Element resultInfo = body.element("resultInfo");
            if (resultInfo != null) {
                final String processCode = resultInfo.elementText("processCode");
                final String processResult = resultInfo.elementText("processResult");
                ApiElectronicPort.LOGGER.info(String.format("processCode:%s processResult:%s", processCode, processResult));
                if (processCode.equals("2100")) {
                    ApiElectronicPort.LOGGER.info("resultInfo count: " + resultInfo.elementText("count"));
                    ApiElectronicPort.LOGGER.info("resultInfo curPage: " + resultInfo.elementText("curPage"));
                    ApiElectronicPort.LOGGER.info("resultInfo pageSize: " + resultInfo.elementText("pageSize"));
                    final List<Element> entryInfoList = (List<Element>)body.element("entryInfoList").elements("entryInfo");
                    final List<ElectronicEntryHead> electronicEntryHeads = new ArrayList<ElectronicEntryHead>();
                    final List<ElectronicEntryBody> electronicEntryBodies = new ArrayList<ElectronicEntryBody>();
                    for (final Element entryInfo : entryInfoList) {
                        final Element entryHead = entryInfo.element("entryHead");
                        final ElectronicEntryHead electronicEntryHead = new ElectronicEntryHead();
                        final Field[] declaredFields;
                        final Field[] fields = declaredFields = ElectronicEntryHead.class.getDeclaredFields();
                        for (final Field field : declaredFields) {
                            final String name = field.getName();
                            final String value = entryHead.elementText(name);
                            field.setAccessible(true);
                            if (!StringUtils.isEmpty(value)) {
                                final Class<?> fieldType = field.getType();
                                if (fieldType.getSimpleName().equals("Integer")) {
                                    field.set(electronicEntryHead, Integer.parseInt(value));
                                }
                                else if (fieldType.getSimpleName().equals("Double")) {
                                    field.set(electronicEntryHead, Double.parseDouble(value));
                                }
                                else {
                                    field.set(electronicEntryHead, value);
                                }
                            }
                            else {
                                field.set(electronicEntryHead, value);
                            }
                        }
                        electronicEntryHeads.add(electronicEntryHead);
                        final List<Element> entryBodyList = (List<Element>)entryInfo.element("entryBodyList").elements("entryBody");
                        for (final Element entryBody : entryBodyList) {
                            final ElectronicEntryBody electronicEntryBody = new ElectronicEntryBody();
                            final Field[] declaredFields2;
                            final Field[] fields2 = declaredFields2 = ElectronicEntryBody.class.getDeclaredFields();
                            for (final Field field2 : declaredFields2) {
                                final String name2 = field2.getName();
                                if (name2.equals("entryId")) {
                                    electronicEntryBody.setEntryId(electronicEntryHead.getEntryId());
                                }
                                else {
                                    final String value2 = entryBody.elementText(name2);
                                    field2.setAccessible(true);
                                    if (!StringUtils.isEmpty(value2)) {
                                        final Class<?> fieldType2 = field2.getType();
                                        if (fieldType2.getSimpleName().equals("Integer")) {
                                            field2.set(electronicEntryBody, Integer.parseInt(value2));
                                        }
                                        else if (fieldType2.getSimpleName().equals("Double")) {
                                            field2.set(electronicEntryBody, Double.parseDouble(value2));
                                        }
                                        else {
                                            field2.set(electronicEntryBody, value2);
                                        }
                                    }
                                    else {
                                        field2.set(electronicEntryBody, value2);
                                    }
                                }
                            }
                            electronicEntryBodies.add(electronicEntryBody);
                        }
                    }
                    for (final ElectronicEntryHead eleHead : electronicEntryHeads) {
                        final EleDataBean eleDataBean = new EleDataBean();
                        eleDataBean.setData(JsonUtil.toJsonString(eleHead));
                        eleDataBean.setType("head");
                        eleDataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                        eleDataBeans.add(eleDataBean);
                    }
                    for (final ElectronicEntryBody eleBody : electronicEntryBodies) {
                        final EleDataBean eleDataBean = new EleDataBean();
                        eleDataBean.setData(JsonUtil.toJsonString(eleBody));
                        eleDataBean.setType("body");
                        eleDataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                        eleDataBeans.add(eleDataBean);
                    }
                }
            }
            else {
                ApiElectronicPort.LOGGER.info("resultInfo is null ");
            }
        }
        catch (Exception e) {
            ApiElectronicPort.LOGGER.error("get data  Exception news error ", e);
        }
        return eleDataBeans;
    }
    
    public static void main(final String[] args) throws Exception {
        final ApiElectronicPort apiElectronicPort = new ApiElectronicPort(args);
        if (apiElectronicPort.isSpark.equals(1)) {
            apiElectronicPort.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        if (!ApiElectronicPort.IS_TEST) {
            try {
                final SparkConf conf = SparkUtil.getSparkConf("ApiElectronicPort");
                final String gpSql = conf.get("spark.gp.sql");
                apiElectronicPort.setStartDate(conf.get("spark.startDate"));
                apiElectronicPort.setEndDate(conf.get("spark.endDate"));
                apiElectronicPort.initDynmcParameters(gpSql);
            }
            catch (Exception e) {
                ApiElectronicPort.LOGGER.info("initDynmcParameters error ", e);
            }
        }
        if (apiElectronicPort.dynmcParameters.size() != 0) {
            final Integer days = DateUtil.betweenDays(apiElectronicPort.endDate, apiElectronicPort.startDate);
            for (final Map<String, Object> dynmcParameter : apiElectronicPort.dynmcParameters) {
                final String tradeCode = dynmcParameter.get("tradeCode").toString();
                Integer dateCount = 0;
                while (true) {
                    final String declareDate = DateUtil.addDay(apiElectronicPort.startDate, dateCount).replaceAll("-", "");
                    ApiElectronicPort.LOGGER.info(String.format("tradeCode:%s declareDate:%s dataCount:%s days:%s", tradeCode, declareDate, dateCount, days));
                    final List<EleDataBean> eleDataBeans = apiElectronicPort.getData(dynmcParameter, declareDate, 1, ApiElectronicPort.PAGE_SIZE);
                    apiElectronicPort.sinkData(eleDataBeans);
                    if (dateCount >= days) {
                        break;
                    }
                    ++dateCount;
                }
            }
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiElectronicPort.class);
        ApiElectronicPort.URL = "http://172.16.100.49:57162/gateway/receive";
        ApiElectronicPort.APP_KEY = "@Ow0hu@N7FKW0Eo79AEV9MKSu06vsRlZUMZ3JLNI";
        ApiElectronicPort.IS_TEST = false;
        ApiElectronicPort.PAGE_SIZE = 200;
        ApiElectronicPort.APP_ID = "MA27U092";
    }
}
