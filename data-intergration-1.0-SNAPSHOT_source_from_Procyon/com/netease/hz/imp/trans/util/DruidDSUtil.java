// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import com.alibaba.druid.pool.DruidDataSource;

public class DruidDSUtil
{
    private static DruidDataSource druidGpDataSource;
    
    public static DruidDataSource createGpDataSource() {
        if (DruidDSUtil.druidGpDataSource == null) {
            synchronized (DruidDSUtil.class) {
                if (DruidDSUtil.druidGpDataSource == null) {
                    System.out.println("~~~\u83b7\u53d6Druid\u8fde\u63a5\u6c60\u5bf9\u8c61~~~");
                    (DruidDSUtil.druidGpDataSource = new DruidDataSource()).setDriverClassName("org.postgresql.Driver");
                    DruidDSUtil.druidGpDataSource.setUrl(GpUtil.URL);
                    DruidDSUtil.druidGpDataSource.setUsername(GpUtil.USERNAME);
                    DruidDSUtil.druidGpDataSource.setPassword(GpUtil.PASSWORD);
                    DruidDSUtil.druidGpDataSource.setInitialSize(5);
                    DruidDSUtil.druidGpDataSource.setMaxActive(20);
                    DruidDSUtil.druidGpDataSource.setMinIdle(5);
                    DruidDSUtil.druidGpDataSource.setMaxWait(-1L);
                    DruidDSUtil.druidGpDataSource.setValidationQuery("select 1");
                    DruidDSUtil.druidGpDataSource.setTestWhileIdle(true);
                    DruidDSUtil.druidGpDataSource.setTestOnBorrow(false);
                    DruidDSUtil.druidGpDataSource.setTestOnReturn(false);
                    DruidDSUtil.druidGpDataSource.setTimeBetweenEvictionRunsMillis(30000L);
                    DruidDSUtil.druidGpDataSource.setMinEvictableIdleTimeMillis(1800000L);
                }
            }
        }
        return DruidDSUtil.druidGpDataSource;
    }
}
