// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import org.apache.spark.sql.Encoder;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Dataset;
import java.util.List;
import org.apache.spark.SparkConf;
import org.apache.spark.sql.SparkSession;

public class SparkUtil
{
    public static SparkSession getSparkSession(final String master, final String appName) {
        final SparkSession spark = SparkSession.builder().master(master).appName(appName).getOrCreate();
        return spark;
    }
    
    public static SparkConf getSparkConf(final String appName) {
        final SparkConf sparkConf = new SparkConf().setAppName(appName);
        return sparkConf;
    }
    
    public static Dataset getDataset(final List<DataBean> dataBeanList, final SparkSession spark) {
        final DataBean dataBean = new DataBean();
        final Encoder<DataBean> encoder = (Encoder<DataBean>)Encoders.bean((Class)DataBean.class);
        final Dataset<DataBean> dataBeanDS = (Dataset<DataBean>)spark.createDataset((List)dataBeanList, (Encoder)encoder);
        return dataBeanDS;
    }
    
    public static Dataset getEleDataset(final List<EleDataBean> eleDataBeanList, final SparkSession spark) {
        final EleDataBean eleDataBean = new EleDataBean();
        final Encoder<EleDataBean> encoder = (Encoder<EleDataBean>)Encoders.bean((Class)EleDataBean.class);
        final Dataset<EleDataBean> eleDataBeanDS = (Dataset<EleDataBean>)spark.createDataset((List)eleDataBeanList, (Encoder)encoder);
        return eleDataBeanDS;
    }
}
