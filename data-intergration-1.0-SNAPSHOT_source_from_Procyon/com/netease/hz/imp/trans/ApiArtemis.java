// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import com.netease.hz.imp.trans.util.GpManager;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import org.apache.spark.sql.SaveMode;
import java.util.Collection;
import com.netease.hz.imp.trans.util.SparkUtil;
import com.alibaba.fastjson.JSONArray;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.fastjson.JSON;
import com.netease.hz.imp.trans.util.ArtemisHttpUtil;
import com.alibaba.fastjson.JSONObject;
import java.util.HashMap;
import com.netease.hz.imp.trans.util.DataBean;
import java.util.List;
import org.apache.commons.cli.CommandLine;
import com.netease.hz.imp.trans.util.JsonUtil;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import com.netease.hz.imp.trans.util.GpUtil;
import java.util.Map;
import org.apache.spark.sql.SparkSession;
import org.slf4j.Logger;

public class ApiArtemis
{
    private static final Logger LOGGER;
    public static String URL;
    public static String APP_KEY;
    public static Integer PAGE_SIZE;
    private Integer isSpark;
    private static SparkSession spark;
    private String sinkDb;
    private String sinkTable;
    private String path;
    private Map<String, Object> params;
    private Integer isPage;
    private Map<String, String> gpOption;
    private Integer totalPage;
    private String startTime;
    private String endTime;
    
    public void setStartDate(final String startTime) {
        this.startTime = startTime;
    }
    
    public void setEndDate(final String endTime) {
        this.endTime = endTime;
    }
    
    public ApiArtemis(final String[] args) throws ParseException {
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
        final Option startTime = new Option(null, "startTime", true, "set startTime");
        options.addOption(startTime);
        final Option endTime = new Option(null, "startTime", true, "set endTime");
        options.addOption(endTime);
        final GnuParser parser = new GnuParser();
        final CommandLine commandLine = parser.parse(options, args);
        this.path = commandLine.getOptionValue("path", "");
        this.sinkTable = commandLine.getOptionValue("sinkTable", "");
        this.sinkDb = commandLine.getOptionValue("sinkDb", "");
        this.isPage = Integer.parseInt(commandLine.getOptionValue("isPage", "1"));
        this.startTime = commandLine.getOptionValue("startTime", "1000");
        this.endTime = commandLine.getOptionValue("endTime", "1000");
        this.params = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
    }
    
    private List<DataBean> getData(final Integer size, final Integer page, final Integer isSpark) {
        final String ARTEMIS_PATH = "/artemis";
        final String getRootApi = "/artemis" + this.path;
        final Map<String, String> path = new HashMap<String, String>(2) {
            {
                this.put("https://", getRootApi);
            }
        };
        final String contentType = "application/json";
        final JSONObject jsonBody = new JSONObject();
        jsonBody.put("pageNo", page);
        jsonBody.put("pageSize", size);
        if (!isSpark.equals(1)) {
            jsonBody.put("startTime", this.startTime + "T00:00:00+08:00");
            jsonBody.put("endTime", this.endTime + "T23:59:59+08:00");
        }
        for (final Map.Entry<String, Object> param : this.params.entrySet()) {
            jsonBody.put(param.getKey(), param.getValue().toString());
        }
        final String body = jsonBody.toJSONString();
        List<DataBean> dataBeanList = null;
        final String result = ArtemisHttpUtil.doPostStringArtemis(path, body, null, null, contentType);
        final JSONObject jsonObject = JSON.parseObject(result);
        this.totalPage = jsonObject.getJSONObject("data").getInteger("total");
        final JSONArray data = jsonObject.getJSONObject("data").getJSONArray("list");
        if (data.size() > 0) {
            dataBeanList = new ArrayList<DataBean>();
            for (int i = 0; i < data.size(); ++i) {
                final DataBean dataBean = new DataBean();
                dataBean.setData(data.get(i).toString());
                dataBean.setEtl_time(String.valueOf(System.currentTimeMillis()));
                dataBeanList.add(dataBean);
            }
        }
        return dataBeanList;
    }
    
    public static void main(final String[] args) throws ParseException {
        final ApiArtemis apiArtemis = new ApiArtemis(args);
        if (apiArtemis.isSpark.equals(1)) {
            ApiArtemis.spark = SparkUtil.getSparkSession("yarn", "Artemis");
            Integer page = 1;
            final List<DataBean> dataBeanList = new ArrayList<DataBean>();
            while (true) {
                final List<DataBean> dataBeanList2 = apiArtemis.getData(ApiArtemis.PAGE_SIZE, page, apiArtemis.isSpark);
                ApiArtemis.LOGGER.info(String.format("totalPage:%s, curPage:%s", apiArtemis.totalPage, page));
                dataBeanList.addAll(dataBeanList2);
                if (page == Math.ceil(apiArtemis.totalPage / (double)ApiArtemis.PAGE_SIZE)) {
                    break;
                }
                ++page;
            }
            ApiArtemis.LOGGER.info("sink data to gp" + String.format("sink size:%s", dataBeanList.size()));
            final Dataset saleBillListDataset = SparkUtil.getDataset(dataBeanList, ApiArtemis.spark);
            saleBillListDataset.write().format("greenplum").options((Map)apiArtemis.gpOption).mode(SaveMode.Append).save();
        }
        else {
            final SparkConf conf = SparkUtil.getSparkConf("ApiArtemis");
            apiArtemis.setStartDate(conf.get("spark.startTime"));
            apiArtemis.setEndDate(conf.get("spark.endTime"));
            Integer page2 = 1;
            final List<DataBean> dataBeanList3 = new ArrayList<DataBean>();
            while (true) {
                final List<DataBean> dataBeanList4 = apiArtemis.getData(ApiArtemis.PAGE_SIZE, page2, apiArtemis.isSpark);
                System.out.println(String.format(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "totalPage:%s, curPage:%s", apiArtemis.totalPage, page2));
                dataBeanList3.addAll(dataBeanList4);
                if (page2 == Math.ceil(apiArtemis.totalPage / (double)ApiArtemis.PAGE_SIZE)) {
                    break;
                }
                ++page2;
            }
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "sink data to gp" + String.format("sink size:%s", dataBeanList3.size()));
            try {
                apiArtemis.sinkData(dataBeanList3);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    private void sinkData(final List<DataBean> dataBeanList) throws Exception {
        if (dataBeanList != null && dataBeanList.size() > 0) {
            final GpManager gpManager = new GpManager();
            gpManager.batchInsert(this.sinkDb, this.sinkTable, dataBeanList);
        }
        else {
            System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + "sind data no data");
        }
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiArtemis.class);
        ApiArtemis.URL = "https://192.168.21.250:443";
        ApiArtemis.APP_KEY = "20656519";
        ApiArtemis.PAGE_SIZE = 1000;
    }
}
