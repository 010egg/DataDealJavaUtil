// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.net.URISyntaxException;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.HttpUtil;
import com.netease.hz.imp.trans.util.DataBean;
import java.util.Iterator;
import com.netease.hz.imp.trans.util.RsaUtils;
import com.netease.hz.imp.trans.util.AesUtils;
import com.netease.hz.imp.trans.dto.SpsRequestDto;
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

public class ApiPartyBuilding
{
    private static final Logger LOGGER;
    public static String URL;
    public static String APP_KEY;
    public static String APP_SECRET;
    public static Integer PAGE_SIZE;
    public static Boolean IS_TEST;
    private String queryMode;
    private String url;
    private Map<String, String> headers;
    private Map<String, Object> parameters;
    private List<Map<String, Object>> dynmcParameters;
    private String sinkDb;
    private String sinkTable;
    private Integer batchSize;
    private Integer isPage;
    private Integer pageSize;
    private Integer totalSize;
    private Integer totalPage;
    private Map<String, String> gpOption;
    private GpManager gpManager;
    private SparkSession spark;
    private Integer isSpark;
    
    private void initTest() {
    }
    
    public ApiPartyBuilding(final String[] args) throws IOException, ParseException, Exception {
        this.headers = new HashMap<String, String>();
        this.dynmcParameters = new ArrayList<Map<String, Object>>();
        this.initOption(args);
        if (ApiPartyBuilding.IS_TEST) {
            this.initTest();
        }
        this.gpOption = new GpUtil(this.sinkDb, this.sinkTable).getGpOption();
    }
    
    private void initDynmcParameters(final String sql) throws Exception {
        this.gpManager = new GpManager();
        this.dynmcParameters = this.gpManager.query(sql);
        ApiPartyBuilding.LOGGER.info("input params: {}" + this.dynmcParameters.size());
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
        final Option isPage = new Option(null, "isPage", true, "set isPage");
        options.addOption(isPage);
        final Option isSpark = new Option(null, "isSpark", true, "set isSpark");
        options.addOption(isSpark);
        final GnuParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.sinkTable = commandLine.getOptionValue("sinkTable", "");
        this.sinkDb = commandLine.getOptionValue("sinkDb", "");
        this.parameters = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "0"));
    }
    
    private void initHeaders() {
        this.headers.put("Content-Type", "application/json");
    }
    
    private String getSign() throws Exception {
        final SpsRequestDto spsRequestDto = new SpsRequestDto();
        spsRequestDto.setAppKey(ApiPartyBuilding.APP_KEY);
        spsRequestDto.setAppSecret(ApiPartyBuilding.APP_SECRET);
        spsRequestDto.setUsername("api");
        final String aesEncrypt = AesUtils.encrypt(spsRequestDto.toString());
        final String sign = RsaUtils.encryptByPublicKey(RsaUtils.spsEsbPublicKey, aesEncrypt);
        return sign;
    }
    
    private String initBody(final Map<String, Object> dynmcParameter) throws Exception {
        final Map<String, Object> query = new HashMap<String, Object>();
        final Map<String, Object> params = new HashMap<String, Object>();
        String funno = "";
        for (final Map.Entry<String, Object> parameter : this.parameters.entrySet()) {
            if (parameter.getKey().equals("funno")) {
                funno = parameter.getValue().toString();
            }
            else {
                params.put(parameter.getKey(), parameter.getValue());
            }
        }
        if (dynmcParameter != null) {
            for (final Map.Entry<String, Object> dynmc : dynmcParameter.entrySet()) {
                params.put(dynmc.getKey(), dynmc.getValue());
            }
        }
        query.put("parms", params);
        final String sign = this.getSign();
        query.put("sign", sign);
        query.put("funno", funno);
        query.put("pageSize", ApiPartyBuilding.PAGE_SIZE);
        return JsonUtil.toJsonString(query);
    }
    
    private List<DataBean> postData(final Map<String, Object> dynmcParameter) {
        final List<DataBean> dataBeans = new ArrayList<DataBean>();
        try {
            final String body = this.initBody(dynmcParameter);
            final String result = HttpUtil.postMethod(ApiPartyBuilding.URL, body, this.headers);
            final JSONObject jsonObject = JSON.parseObject(result);
            this.totalSize = jsonObject.getJSONObject("data").getInteger("total");
            ApiPartyBuilding.LOGGER.info("totalSize: " + this.totalSize);
            final JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("rows");
            if (jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); ++i) {
                    final JSONObject dataObject = JSON.parseObject(jsonArray.get(i).toString());
                    if (dynmcParameter != null && dynmcParameter.containsKey("ct_branch_level")) {
                        dataObject.put("ct_branch_level", dynmcParameter.get("ct_branch_level"));
                    }
                    final DataBean dataBean = new DataBean();
                    dataBean.setData(dataObject.toString());
                    dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                    dataBeans.add(dataBean);
                }
            }
        }
        catch (URISyntaxException e) {
            ApiPartyBuilding.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (Exception e2) {
            ApiPartyBuilding.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeans;
    }
    
    private void sinkData(final List<DataBean> dataBeans) throws Exception {
        ApiPartyBuilding.LOGGER.info("isSpark: " + this.isSpark);
        if (this.isSpark.equals(1)) {
            final Dataset saleBillListDataset = SparkUtil.getDataset(dataBeans, this.spark);
            saleBillListDataset.write().format("greenplum").options((Map)this.gpOption).mode(SaveMode.Append).save();
        }
        else {
            final GpManager gpManager = new GpManager();
            gpManager.batchInsert(this.sinkDb, this.sinkTable, dataBeans);
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final ApiPartyBuilding apiPartyBuilding = new ApiPartyBuilding(args);
        if (apiPartyBuilding.isSpark.equals(1)) {
            apiPartyBuilding.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        if (!ApiPartyBuilding.IS_TEST) {
            try {
                final SparkConf conf = SparkUtil.getSparkConf("ApiPartyBuilding");
                final String gpSql = conf.get("spark.gp.sql");
                apiPartyBuilding.initDynmcParameters(gpSql);
            }
            catch (Exception e) {
                ApiPartyBuilding.LOGGER.info("initParamsInput error ", e);
            }
        }
        if (apiPartyBuilding.dynmcParameters.size() == 0) {
            final List<DataBean> dataBeanList = apiPartyBuilding.postData(null);
            apiPartyBuilding.sinkData(dataBeanList);
        }
        else {
            for (final Map<String, Object> dynmcParameter : apiPartyBuilding.dynmcParameters) {
                final List<DataBean> dataBeanList2 = apiPartyBuilding.postData(dynmcParameter);
                apiPartyBuilding.sinkData(dataBeanList2);
            }
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiPartyBuilding.class);
        ApiPartyBuilding.URL = "http://10.0.126.10/party-mis/spsEsb/getRowDataFromSpsNew";
        ApiPartyBuilding.APP_KEY = "11A80780CE814F4F86BCB998DA5CF0CB";
        ApiPartyBuilding.APP_SECRET = "AFABFF88FE47472B9912BF22B47A200C";
        ApiPartyBuilding.PAGE_SIZE = 10000;
        ApiPartyBuilding.IS_TEST = false;
    }
}
