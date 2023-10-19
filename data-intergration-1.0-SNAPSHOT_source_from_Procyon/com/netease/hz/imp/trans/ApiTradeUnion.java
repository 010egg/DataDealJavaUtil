// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.SparkConf;
import java.util.Collection;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.net.URI;
import java.util.Iterator;
import java.net.URISyntaxException;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.HttpUtil;
import org.apache.http.client.utils.URIBuilder;
import java.util.HashMap;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import java.util.LinkedList;
import com.netease.hz.imp.trans.util.DataBean;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
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

public class ApiTradeUnion
{
    private static final Logger LOGGER;
    public static String URL;
    public static String APP_KEY;
    public static Integer PAGE_SIZE;
    private String sinkDb;
    private String sinkTable;
    private String path;
    private Map<String, Object> params;
    private Integer isPage;
    private Map<String, String> gpOption;
    private Integer totalPage;
    private Integer batchSize;
    private List<Map<String, Object>> inputparams;
    private GpManager gpManager;
    private String gpSql;
    private SparkSession spark;
    private Integer isSpark;
    
    public ApiTradeUnion(final String[] args) throws IOException, ParseException, Exception {
        this.inputparams = new ArrayList<Map<String, Object>>();
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
        this.path = commandLine.getOptionValue("path", "");
        this.sinkTable = commandLine.getOptionValue("sinkTable", "");
        this.sinkDb = commandLine.getOptionValue("sinkDb", "");
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "0"));
        this.batchSize = Integer.parseInt(commandLine.getOptionValue("batchSize", "1000"));
        this.params = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.gpSql = commandLine.getOptionValue("gpSql", "");
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
    }
    
    private void initParamsInput(final String sql) throws Exception {
        this.gpManager = new GpManager();
        this.inputparams = this.gpManager.query(sql);
        ApiTradeUnion.LOGGER.info("input params: {}" + this.inputparams.size());
    }
    
    private String getAccessKey() {
        final long timestamp2 = System.currentTimeMillis();
        final String str = ApiTradeUnion.APP_KEY + ":" + timestamp2;
        final byte[] encode = Base64.getEncoder().encode(str.getBytes(StandardCharsets.UTF_8));
        final String accessKey = new String(encode);
        return accessKey;
    }
    
    private List<DataBean> getData(final Map<String, Object> inputParam) {
        final List<DataBean> dataBeanList = new ArrayList<DataBean>();
        final String accessKey = this.getAccessKey();
        final List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("accessKey", accessKey));
        for (final Map.Entry<String, Object> param : this.params.entrySet()) {
            parameters.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
        }
        if (this.inputparams.size() > 0) {
            for (final Map.Entry<String, Object> param : inputParam.entrySet()) {
                parameters.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
            }
        }
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        try {
            final URI uri = new URIBuilder(ApiTradeUnion.URL).setPath(this.path).addParameters(parameters).build();
            final String result = HttpUtil.getMethod(uri.toString(), headers);
            ApiTradeUnion.LOGGER.info("URI: " + uri.toString());
            final JSONObject jsonObject = JSON.parseObject(result);
            final JSONArray data = jsonObject.getJSONArray("data");
            if (data.size() > 0) {
                for (int i = 0; i < data.size(); ++i) {
                    final DataBean dataBean = new DataBean();
                    dataBean.setData(data.get(i).toString());
                    dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                    dataBeanList.add(dataBean);
                }
            }
        }
        catch (URISyntaxException e) {
            ApiTradeUnion.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (IOException e2) {
            ApiTradeUnion.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeanList;
    }
    
    private List<DataBean> getData(final Integer size, final Integer page, final Map<String, Object> inputParam) {
        final List<DataBean> dataBeanList = new ArrayList<DataBean>();
        final String accessKey = this.getAccessKey();
        final List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("accessKey", accessKey));
        parameters.add(new BasicNameValuePair("page", page.toString()));
        parameters.add(new BasicNameValuePair("size", size.toString()));
        for (final Map.Entry<String, Object> param : this.params.entrySet()) {
            parameters.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
        }
        if (this.inputparams.size() > 0) {
            for (final Map.Entry<String, Object> param : inputParam.entrySet()) {
                ApiTradeUnion.LOGGER.info(String.format("param:%s value:%s", param.getKey(), param.getValue()));
                parameters.add(new BasicNameValuePair(param.getKey(), param.getValue().toString()));
            }
        }
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        try {
            final URI uri = new URIBuilder(ApiTradeUnion.URL).setPath(this.path).addParameters(parameters).build();
            final String result = HttpUtil.getMethod(uri.toString(), headers);
            ApiTradeUnion.LOGGER.info("URI: " + uri.toString());
            final JSONObject jsonObject = JSON.parseObject(result);
            this.totalPage = jsonObject.getJSONObject("data").getInteger("pages");
            final JSONArray data = jsonObject.getJSONObject("data").getJSONArray("data");
            if (data.size() > 0) {
                for (int i = 0; i < data.size(); ++i) {
                    final DataBean dataBean = new DataBean();
                    dataBean.setData(data.get(i).toString());
                    dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                    dataBeanList.add(dataBean);
                }
            }
        }
        catch (URISyntaxException e) {
            ApiTradeUnion.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (IOException e2) {
            ApiTradeUnion.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeanList;
    }
    
    private void sinkData(final List<DataBean> dataBeans) throws Exception {
        ApiTradeUnion.LOGGER.info("isSpark: " + this.isSpark);
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
            ApiTradeUnion.LOGGER.info("sind data no data");
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final ApiTradeUnion apiTradeUnion = new ApiTradeUnion(args);
        if (apiTradeUnion.isSpark.equals(1)) {
            apiTradeUnion.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        try {
            final SparkConf conf = SparkUtil.getSparkConf("TradeUnion");
            final String gpSql = conf.get("spark.gp.sql");
            apiTradeUnion.initParamsInput(gpSql);
        }
        catch (Exception e) {
            ApiTradeUnion.LOGGER.info("initParamsInput error ", e);
        }
        if (apiTradeUnion.inputparams.size() == 0) {
            if (apiTradeUnion.isPage == 0) {
                final List<DataBean> dataBeanList = apiTradeUnion.getData(null);
                apiTradeUnion.sinkData(dataBeanList);
            }
            else {
                Integer page = 1;
                final List<DataBean> dataBeanList2 = new ArrayList<DataBean>();
                while (true) {
                    final List<DataBean> dataBeanList3 = apiTradeUnion.getData(ApiTradeUnion.PAGE_SIZE, page, null);
                    ApiTradeUnion.LOGGER.info(String.format("totalPage:%s, curPage:%s", apiTradeUnion.totalPage, page));
                    dataBeanList2.addAll(dataBeanList3);
                    if (page >= apiTradeUnion.totalPage) {
                        break;
                    }
                    ++page;
                }
                ApiTradeUnion.LOGGER.info("sink data to gp" + String.format("sink size:%s", dataBeanList2.size()));
                apiTradeUnion.sinkData(dataBeanList2);
            }
        }
        else {
            for (final Map<String, Object> paramInput : apiTradeUnion.inputparams) {
                if (apiTradeUnion.isPage == 0) {
                    final List<DataBean> dataBeanList4 = apiTradeUnion.getData(paramInput);
                    apiTradeUnion.sinkData(dataBeanList4);
                }
                else {
                    Integer page2 = 1;
                    final List<DataBean> dataBeanList5 = new ArrayList<DataBean>();
                    while (true) {
                        final List<DataBean> dataBeanList6 = apiTradeUnion.getData(ApiTradeUnion.PAGE_SIZE, page2, paramInput);
                        ApiTradeUnion.LOGGER.info(String.format("totalPage:%s, curPage:%s", apiTradeUnion.totalPage, page2));
                        dataBeanList5.addAll(dataBeanList6);
                        if (page2 >= apiTradeUnion.totalPage) {
                            break;
                        }
                        ++page2;
                    }
                    ApiTradeUnion.LOGGER.info("sink data to gp" + String.format("sink size:%s", dataBeanList5.size()));
                    apiTradeUnion.sinkData(dataBeanList5);
                }
            }
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiTradeUnion.class);
        ApiTradeUnion.URL = "https://gonghui.zibchina.com:9001";
        ApiTradeUnion.APP_KEY = "27821298239726099082710";
        ApiTradeUnion.PAGE_SIZE = 100000;
    }
}
