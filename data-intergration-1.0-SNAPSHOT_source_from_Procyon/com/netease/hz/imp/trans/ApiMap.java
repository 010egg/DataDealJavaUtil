// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import com.alibaba.fastjson.JSONObject;
import java.net.URI;
import java.util.Iterator;
import java.net.URISyntaxException;
import com.netease.hz.imp.trans.dto.MapDto;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.HttpUtil;
import org.apache.http.client.utils.URIBuilder;
import java.util.HashMap;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import java.util.LinkedList;
import com.netease.hz.imp.trans.util.DataBean;
import org.apache.commons.cli.CommandLine;
import com.netease.hz.imp.trans.util.JsonUtil;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.io.IOException;
import com.netease.hz.imp.trans.util.GpUtil;
import java.util.ArrayList;
import org.apache.spark.sql.SparkSession;
import com.netease.hz.imp.trans.util.GpManager;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;

public class ApiMap
{
    private static final Logger LOGGER;
    public static String URL;
    public static String AK;
    public static Integer PAGE_SIZE;
    private String sinkDb;
    private String sinkTable;
    private Map<String, Object> params;
    private Integer isPage;
    private Map<String, String> gpOption;
    private Integer totalPage;
    private Integer batchSize;
    private List<Map<String, Object>> dynmcParameters;
    private GpManager gpManager;
    private String gpSql;
    private SparkSession spark;
    private Integer isSpark;
    
    public ApiMap(final String[] args) throws IOException, ParseException, Exception {
        this.dynmcParameters = new ArrayList<Map<String, Object>>();
        this.initOption(args);
        this.gpOption = new GpUtil(this.sinkDb, this.sinkTable).getGpOption();
    }
    
    private void initOption(final String[] args) throws ParseException {
        final Options options = new Options();
        final Option path = new Option(null, "path", true, "set path");
        options.addOption(path);
        final Option sinkDb = new Option(null, "sinkDb", true, "set db");
        options.addOption(sinkDb);
        final Option sinkTable = new Option(null, "sinkTable", true, "set table");
        options.addOption(sinkTable);
        final Option params = new Option(null, "params", true, "set params");
        options.addOption(params);
        final Option isPage = new Option(null, "isPage", true, "set isPage");
        options.addOption(isPage);
        final Option batchSize = new Option(null, "batchSize", true, "set batchSize");
        options.addOption(batchSize);
        final Option gpSql = new Option(null, "gpSql", true, "set gpSql");
        options.addOption(gpSql);
        final Option isSpark = new Option(null, "isSpark", true, "set isSpark");
        options.addOption(isSpark);
        final GnuParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.sinkTable = commandLine.getOptionValue("sinkTable", "");
        this.sinkDb = commandLine.getOptionValue("sinkDb", "");
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "0"));
        this.batchSize = Integer.parseInt(commandLine.getOptionValue("batchSize", "1000"));
        this.params = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.gpSql = commandLine.getOptionValue("gpSql", "");
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
    }
    
    private void initDynmcParameters(final String sql) throws Exception {
        this.gpManager = new GpManager();
        this.dynmcParameters = this.gpManager.query(sql);
        ApiMap.LOGGER.info("input params: {}" + this.dynmcParameters.size());
    }
    
    private List<DataBean> getData(final Map<String, Object> inputParam) {
        final List<DataBean> dataBeanList = new ArrayList<DataBean>();
        final List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("ak", ApiMap.AK));
        parameters.add(new BasicNameValuePair("output", "json"));
        for (final Map.Entry<String, Object> param : this.params.entrySet()) {
            parameters.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
        }
        if (this.dynmcParameters.size() > 0) {
            for (final Map.Entry<String, Object> param : inputParam.entrySet()) {
                parameters.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
            }
        }
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        try {
            final URI uri = new URIBuilder(ApiMap.URL).addParameters(parameters).build();
            final String result = HttpUtil.getMethod(uri.toString(), headers);
            ApiMap.LOGGER.info("URI: " + uri.toString());
            JSONObject jsonObject = JSON.parseObject(result);
            final MapDto mapDto = new MapDto();
            mapDto.setStatus(jsonObject.getInteger("status"));
            jsonObject = jsonObject.getJSONObject("result");
            if (mapDto.getStatus().equals(0)) {
                final JSONObject location = jsonObject.getJSONObject("location");
                mapDto.setLat(location.getDouble("lat"));
                mapDto.setLng(location.getDouble("lng"));
                if (this.dynmcParameters.size() > 0) {
                    for (final Map.Entry<String, Object> param2 : inputParam.entrySet()) {
                        parameters.add(new BasicNameValuePair(param2.getKey(), param2.getValue().toString()));
                    }
                }
                mapDto.setPrecise(jsonObject.getInteger("precise"));
                mapDto.setConfidence(jsonObject.getInteger("confidence"));
                mapDto.setComprehension(jsonObject.getInteger("comprehension"));
                mapDto.setLevel(jsonObject.getString("level"));
                mapDto.setAnalys_level(jsonObject.getString("analys_level"));
                mapDto.setAddress(inputParam.get("address").toString());
                final DataBean dataBean = new DataBean();
                dataBean.setData(JsonUtil.toJsonString(mapDto));
                dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                dataBeanList.add(dataBean);
            }
        }
        catch (URISyntaxException e) {
            ApiMap.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (IOException e2) {
            ApiMap.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeanList;
    }
    
    private void sinkData(final List<DataBean> dataBeans) throws Exception {
        ApiMap.LOGGER.info("isSpark: " + this.isSpark);
        if (dataBeans != null && dataBeans.size() > 0) {
            if (this.isSpark.equals(1)) {
                final Dataset saleBillListDataset = SparkUtil.getDataset(dataBeans, this.spark);
                saleBillListDataset.write().format("greenplum").options((Map)this.gpOption).mode(SaveMode.Append).save();
            }
            else {
                final GpManager gpManager = new GpManager();
                gpManager.batchInsert(this.sinkDb, this.sinkTable, dataBeans);
            }
        }
        else {
            ApiMap.LOGGER.info("sind data no data");
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final ApiMap apiMap = new ApiMap(args);
        if (apiMap.isSpark.equals(1)) {
            apiMap.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        try {
            final SparkConf conf = SparkUtil.getSparkConf("ApiMap");
            final String gpSql = conf.get("spark.gp.sql");
            apiMap.initDynmcParameters(gpSql);
        }
        catch (Exception e) {
            ApiMap.LOGGER.info("initParamsInput error ", e);
        }
        if (apiMap.dynmcParameters.size() == 0) {
            final List<DataBean> dataBeanList = apiMap.getData(null);
            apiMap.sinkData(dataBeanList);
        }
        else {
            Integer i = 0;
            for (final Map<String, Object> paramInput : apiMap.dynmcParameters) {
                if (paramInput != null && paramInput.containsKey("address")) {
                    ApiMap.LOGGER.info(String.format("address:%s i: %s", paramInput.get("address"), i));
                }
                final List<DataBean> dataBeanList2 = apiMap.getData(paramInput);
                try {
                    apiMap.sinkData(dataBeanList2);
                }
                catch (Exception e2) {
                    ApiMap.LOGGER.info("sinkData error ", e2);
                    Thread.sleep(5000L);
                }
                ++i;
            }
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiMap.class);
        ApiMap.URL = "http://api.map.baidu.com/geocoding/v3";
        ApiMap.AK = "nlLHwe7A6MxSzVeMoq0lI6PHh0rxG92n";
        ApiMap.PAGE_SIZE = 50;
    }
}
