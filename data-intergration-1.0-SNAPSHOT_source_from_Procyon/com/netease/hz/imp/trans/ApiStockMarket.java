// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans;

import org.slf4j.LoggerFactory;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SaveMode;
import com.netease.hz.imp.trans.util.SparkUtil;
import java.net.URI;
import java.net.URISyntaxException;
import com.netease.hz.imp.trans.dto.StockDto;
import com.netease.hz.imp.trans.util.HttpUtil;
import org.apache.http.client.utils.URIBuilder;
import java.util.HashMap;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.NameValuePair;
import java.util.LinkedList;
import java.util.ArrayList;
import com.netease.hz.imp.trans.util.DataBean;
import java.util.List;
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

public class ApiStockMarket
{
    private static final Logger LOGGER;
    public static String URL;
    private String sinkDb;
    private String sinkTable;
    private Map<String, Object> params;
    private Map<String, String> gpOption;
    private GpManager gpManager;
    private SparkSession spark;
    private Integer isSpark;
    
    public ApiStockMarket(final String[] args) throws IOException, ParseException, Exception {
        this.initOption(args);
        this.gpOption = new GpUtil(this.sinkDb, this.sinkTable).getGpOption();
    }
    
    private void initOption(final String[] args) throws ParseException {
        final Options options = new Options();
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
        this.params = JsonUtil.parseMap(commandLine.getOptionValue("params", "{}"));
        this.isSpark = Integer.parseInt(commandLine.getOptionValue("isSpark", "0"));
    }
    
    private List<DataBean> getData(final Map<String, Object> inputParam) {
        final List<DataBean> dataBeans = new ArrayList<DataBean>();
        final List<NameValuePair> parameters = new LinkedList<NameValuePair>();
        parameters.add(new BasicNameValuePair("q", "sh600120,sz000411,sh600572,sz002686"));
        parameters.add(new BasicNameValuePair("offset", "2,3,4,31,32,33,37,38,45,46"));
        final Map<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        try {
            final URI uri = new URIBuilder(ApiStockMarket.URL).addParameters(parameters).build();
            final String result = HttpUtil.getMethod(uri.toString(), headers);
            final String[] datas = result.split("\n");
            final String etlTime = String.valueOf(System.currentTimeMillis());
            for (final String data : datas) {
                final String[] stockDatas = data.replace(";", "").split("=");
                final StockDto stockDto = new StockDto();
                final DataBean dataBean = new DataBean();
                final String[] resultDatas = stockDatas[1].replace("\"", "").split("~");
                stockDto.setStock_code(resultDatas[1]);
                stockDto.setStock_name(resultDatas[0]);
                stockDto.setLatest_qttn(Double.parseDouble(resultDatas[2]));
                stockDto.setUps_downs_the_frhd(Double.parseDouble(resultDatas[3]));
                stockDto.setPrice_limit(Double.parseDouble(resultDatas[4]));
                stockDto.setTrdng_vlm(Double.parseDouble(resultDatas[5]));
                stockDto.setTrdng_amt(Double.parseDouble(resultDatas[6]));
                stockDto.setCrcltn_mrkt_value(Double.parseDouble(resultDatas[7]));
                stockDto.setTtl_mrkt_value(Double.parseDouble(resultDatas[8]));
                dataBean.setEtl_time(etlTime);
                dataBean.setData(JsonUtil.toJsonString(stockDto));
                dataBeans.add(dataBean);
            }
        }
        catch (URISyntaxException e) {
            ApiStockMarket.LOGGER.error("get data  URISyntaxException news error ", e);
        }
        catch (IOException e2) {
            ApiStockMarket.LOGGER.error("get data  IOException news error", e2);
        }
        return dataBeans;
    }
    
    private void sinkData(final List<DataBean> dataBeans) throws Exception {
        ApiStockMarket.LOGGER.info("isSpark: " + this.isSpark);
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
        final ApiStockMarket apiStockMarket = new ApiStockMarket(args);
        if (apiStockMarket.isSpark.equals(1)) {
            apiStockMarket.spark = SparkUtil.getSparkSession("yarn", "Trans");
        }
        final List<DataBean> dataBeanList = apiStockMarket.getData(null);
        apiStockMarket.sinkData(dataBeanList);
    }
    
    static {
        LOGGER = LoggerFactory.getLogger(ApiStockMarket.class);
        ApiStockMarket.URL = "https://sqt.gtimg.cn/";
    }
}
