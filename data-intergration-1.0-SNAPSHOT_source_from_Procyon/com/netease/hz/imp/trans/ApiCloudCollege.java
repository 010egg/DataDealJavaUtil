// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import com.alibaba.fastjson.JSONArray;
import java.net.URI;
import java.util.Iterator;
import java.net.URISyntaxException;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.HttpUtil;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import java.util.LinkedList;
import java.util.ArrayList;
import com.netease.hz.imp.trans.util.DataBean;
import java.util.List;
import java.net.URLEncoder;
import java.util.HashMap;
import com.netease.hz.imp.trans.util.DateUtil;
import org.apache.commons.cli.CommandLine;
import com.netease.hz.imp.trans.util.JsonUtil;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import java.io.IOException;
import com.netease.hz.imp.trans.util.GpUtil;
import org.apache.spark.sql.SparkSession;
import com.netease.hz.imp.trans.util.GpManager;
import java.util.Map;
import org.slf4j.Logger;

public class ApiCloudCollege
{
    private static final Logger LOGGER;
    public static String URL;
    public static String APP_KEY;
    public static String APP_SECRET;
    public static Integer PAGE_SIZE;
    private String sinkDb;
    private String sinkTable;
    private String path;
    private Map<String, Object> params;
    private Integer isPage;
    private Map<String, String> gpOption;
    private Integer totalPage;
    private Integer batchSize;
    private GpManager gpManager;
    private SparkSession spark;
    private Integer isSpark;
    private String startDate;
    private String endDate;
    
    public ApiCloudCollege(final String[] args) throws IOException, ParseException, Exception {
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
        final Option isSpark = new Option(null, "isSpark", true, "set isSpark");
        options.addOption(isSpark);
        final Option startDate = new Option(null, "startDate", true, "set startDate");
        options.addOption(startDate);
        final Option endDate = new Option(null, "endDate", true, "set endDate");
        options.addOption(endDate);
        final GnuParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.path = commandLine.getOptionValue("path", "");
        this.sinkTable = commandLine.getOptionValue("sinkTable", "");
        this.sinkDb = commandLine.getOptionValue("sinkDb", "");
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "0"));
        this.batchSize = Integer.parseInt(commandLine.getOptionValue("batchSize", "1000"));
        this.params = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
        this.startDate = commandLine.getOptionValue("startDate", "");
        this.endDate = commandLine.getOptionValue("endDate", "");
    }
    
    private String getSign(String uriPath) {
        uriPath = uriPath.replace(".html", "").replace("/open", "");
        final String signText = ApiCloudCollege.APP_SECRET + "|" + uriPath + "|" + ApiCloudCollege.APP_SECRET;
        return DateUtil.getMD5(signText);
    }
    
    private Map<String, String> initHeaders() {
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        return headers;
    }
    
    private Map<String, Object> initParams(final Map<String, Object> inputParam) throws Exception {
        final Map<String, Object> courseReportQuery = new HashMap<String, Object>();
        final Long startTime = DateUtil.getTime(this.startDate, "00:00:00");
        final Long endTime = DateUtil.getTime(this.endDate, "23:59:59");
        final Long timeStamp = System.currentTimeMillis();
        courseReportQuery.put("startTime", startTime);
        courseReportQuery.put("endTime", endTime);
        final Map<String, Object> body = new HashMap<String, Object>();
        body.put("courseReportQuery", URLEncoder.encode(JsonUtil.toJsonString(courseReportQuery), "UTF-8"));
        body.put("appKey_", ApiCloudCollege.APP_KEY);
        body.put("timestamp_", timeStamp);
        final String sign = this.getSign(this.path);
        body.put("sign_", sign);
        return body;
    }
    
    private List<DataBean> getData(final Map<String, Object> inputParam) throws Exception {
        final List<DataBean> dataBeanList = new ArrayList<DataBean>();
        final Map<String, String> headers = this.initHeaders();
        final Map<String, Object> bodys = this.initParams(null);
        final List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        for (final Map.Entry<String, Object> body : bodys.entrySet()) {
            parameters.add(new BasicNameValuePair(body.getKey(), body.getValue().toString()));
        }
        try {
            final URI uri = new URIBuilder(ApiCloudCollege.URL).setPath(this.path).addParameters(parameters).build();
            ApiCloudCollege.LOGGER.info("URI: " + uri.toString());
            final String result = HttpUtil.getMethod(uri.toString(), headers);
            final JSONArray jsonArray = JSON.parseArray(result);
            if (jsonArray.size() > 0) {
                for (int i = 0; i < jsonArray.size(); ++i) {
                    final DataBean dataBean = new DataBean();
                    dataBean.setData(jsonArray.get(i).toString());
                    dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                    dataBeanList.add(dataBean);
                }
            }
        }
        catch (URISyntaxException e) {
            ApiCloudCollege.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (IOException e2) {
            ApiCloudCollege.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeanList;
    }
    
    private void sinkData(final List<DataBean> dataBeans) throws Exception {
        ApiCloudCollege.LOGGER.info("isSpark: " + this.isSpark);
        if (dataBeans != null && dataBeans.size() > 0) {
            if (this.isSpark == 1) {
                final Dataset saleBillListDataset = SparkUtil.getDataset(dataBeans, this.spark);
                saleBillListDataset.write().format("greenplum").options((Map)this.gpOption).mode(SaveMode.Append).save();
            }
            else {
                final GpManager gpManager = new GpManager();
                gpManager.batchInsert(this.sinkDb, this.sinkTable, dataBeans);
            }
        }
        else {
            ApiCloudCollege.LOGGER.info("sind data no data");
        }
    }
    
    public static void main(final String[] args) throws Exception {
        final ApiCloudCollege apiCloudCollege = new ApiCloudCollege(args);
        if (apiCloudCollege.isSpark == 1) {
            apiCloudCollege.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        final List<DataBean> dataBeanList = apiCloudCollege.getData(null);
        apiCloudCollege.sinkData(dataBeanList);
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiCloudCollege.class);
        ApiCloudCollege.URL = "http://v4.21tb.com";
        ApiCloudCollege.APP_KEY = "3196746D76404192A5D456FFDE750E01";
        ApiCloudCollege.APP_SECRET = "A9660C63561A4DDF971959BD17E9C010";
        ApiCloudCollege.PAGE_SIZE = 50;
    }
}
