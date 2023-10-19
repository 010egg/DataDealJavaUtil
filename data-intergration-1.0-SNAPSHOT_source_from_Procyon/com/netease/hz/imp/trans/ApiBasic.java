// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import java.util.Iterator;
import java.net.URI;
import java.net.URISyntaxException;
import com.alibaba.fastjson.JSONPath;
import com.alibaba.fastjson.JSONArray;
import org.apache.commons.lang3.StringUtils;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.HttpUtil;
import org.apache.http.client.utils.URIBuilder;
import com.netease.hz.imp.trans.util.DataBean;
import java.util.LinkedList;
import org.apache.http.NameValuePair;
import java.util.HashMap;
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

public class ApiBasic
{
    private static final Logger LOGGER;
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
    private Map<String, String> gpOption;
    private Integer isFirstArray;
    private String arrayJsonPath;
    private Map<String, String> schemas;
    private GpManager gpManager;
    private SparkSession spark;
    private Integer isSpark;
    
    public ApiBasic(final String[] args) throws IOException, ParseException, Exception {
        this.dynmcParameters = new ArrayList<Map<String, Object>>();
        this.initOption(args);
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
        this.schemas = JsonUtil.parseMap(commandLine.getOptionValue("schemas", "{}"));
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "0"));
        this.isFirstArray = Integer.parseInt(commandLine.getOptionValue("isFirstArray", "0"));
        this.url = commandLine.getOptionValue("url", "");
        this.arrayJsonPath = commandLine.getOptionValue("arrayJsonPath", "");
    }
    
    private Map<String, String> initHeaders() {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }
    
    private Map<String, Object> initParams() {
        final Map<String, Object> body = new HashMap<String, Object>();
        return body;
    }
    
    private List<NameValuePair> initParams(final Map<String, Object> inputParam) {
        final List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        return parameters;
    }
    
    private List<DataBean> getData() {
        final List<DataBean> dataBeans = new ArrayList<DataBean>();
        try {
            final URI uri = new URIBuilder(this.url).addParameters(this.initParams(this.parameters)).build();
            ApiBasic.LOGGER.info("URI: " + uri.toString());
            final String result = HttpUtil.getMethod(uri.toString(), this.initHeaders());
            if (this.isFirstArray == 1) {
                final JSONArray jsonArray = JSON.parseArray(result);
                for (int i = 0; i < jsonArray.size(); ++i) {
                    final DataBean dataBean = new DataBean();
                    dataBean.setData(jsonArray.get(i).toString());
                    dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                }
            }
            else if (StringUtils.isNotBlank(this.arrayJsonPath)) {
                final JSONArray jsonArray = (JSONArray)JSONPath.read(result, this.arrayJsonPath);
                for (int i = 0; i < jsonArray.size(); ++i) {
                    final DataBean dataBean = new DataBean();
                    dataBean.setData(jsonArray.get(i).toString());
                    dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                    dataBeans.add(dataBean);
                }
            }
            else {
                final Map<String, Object> data = new HashMap<String, Object>();
                if (this.schemas.size() > 0) {
                    for (final Map.Entry<String, String> param : this.schemas.entrySet()) {
                        data.put(param.getKey(), JSONPath.read(result, param.getValue()).toString());
                    }
                    final DataBean dataBean2 = new DataBean();
                    dataBean2.setData(JsonUtil.toJsonString(data));
                    dataBean2.setEtl_time(String.valueOf(System.currentTimeMillis()));
                    dataBeans.add(dataBean2);
                }
            }
        }
        catch (URISyntaxException e) {
            ApiBasic.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (Exception e2) {
            ApiBasic.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeans;
    }
    
    private void sinkData(final List<DataBean> dataBeans) throws Exception {
        ApiBasic.LOGGER.info("isSpark: " + this.isSpark);
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
        final ApiBasic apiBasic = new ApiBasic(args);
        if (apiBasic.isSpark.equals(1)) {
            apiBasic.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        final List<DataBean> dataBeanList = apiBasic.getData();
        apiBasic.sinkData(dataBeanList);
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiBasic.class);
    }
}
