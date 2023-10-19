// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.pool;

import java.util.Iterator;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import java.util.Map;
import java.util.ArrayList;
import com.alibaba.druid.util.JdbcSqlStatUtils;
import java.util.LinkedHashMap;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;

public class DruidDataSourceStatLoggerImpl extends DruidDataSourceStatLoggerAdapter
{
    private static Log LOG;
    private Log logger;
    
    public DruidDataSourceStatLoggerImpl() {
        this.logger = DruidDataSourceStatLoggerImpl.LOG;
        this.configFromProperties(System.getProperties());
    }
    
    @Override
    public void configFromProperties(final Properties properties) {
        if (properties == null) {
            return;
        }
        final String property = properties.getProperty("druid.stat.loggerName");
        if (property != null && property.length() > 0) {
            this.setLoggerName(property);
        }
    }
    
    public Log getLogger() {
        return this.logger;
    }
    
    @Override
    public void setLoggerName(final String loggerName) {
        this.logger = LogFactory.getLog(loggerName);
    }
    
    @Override
    public void setLogger(final Log logger) {
        if (logger == null) {
            throw new IllegalArgumentException("logger can not be null");
        }
        this.logger = logger;
    }
    
    public boolean isLogEnable() {
        return this.logger.isInfoEnabled();
    }
    
    public void log(final String value) {
        this.logger.info(value);
    }
    
    @Override
    public void log(final DruidDataSourceStatValue statValue) {
        if (!this.isLogEnable()) {
            return;
        }
        final Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("url", statValue.url);
        map.put("dbType", statValue.getDbType());
        map.put("name", statValue.getName());
        map.put("activeCount", statValue.getActiveCount());
        if (statValue.getActivePeak() > 0) {
            map.put("activePeak", statValue.getActivePeak());
            map.put("activePeakTime", statValue.getActivePeakTime());
        }
        map.put("poolingCount", statValue.getPoolingCount());
        if (statValue.getPoolingPeak() > 0) {
            map.put("poolingPeak", statValue.getPoolingPeak());
            map.put("poolingPeakTime", statValue.getPoolingPeakTime());
        }
        map.put("connectCount", statValue.getConnectCount());
        map.put("closeCount", statValue.getCloseCount());
        if (statValue.getWaitThreadCount() > 0L) {
            map.put("waitThreadCount", statValue.getWaitThreadCount());
        }
        if (statValue.getNotEmptyWaitCount() > 0L) {
            map.put("notEmptyWaitCount", statValue.getNotEmptyWaitCount());
        }
        if (statValue.getNotEmptyWaitMillis() > 0L) {
            map.put("notEmptyWaitMillis", statValue.getNotEmptyWaitMillis());
        }
        if (statValue.getLogicConnectErrorCount() > 0L) {
            map.put("logicConnectErrorCount", statValue.getLogicConnectErrorCount());
        }
        if (statValue.getPhysicalConnectCount() > 0L) {
            map.put("physicalConnectCount", statValue.getPhysicalConnectCount());
        }
        if (statValue.getPhysicalCloseCount() > 0L) {
            map.put("physicalCloseCount", statValue.getPhysicalCloseCount());
        }
        if (statValue.getPhysicalConnectErrorCount() > 0L) {
            map.put("physicalConnectErrorCount", statValue.getPhysicalConnectErrorCount());
        }
        if (statValue.getExecuteCount() > 0L) {
            map.put("executeCount", statValue.getExecuteCount());
        }
        if (statValue.getErrorCount() > 0L) {
            map.put("errorCount", statValue.getErrorCount());
        }
        if (statValue.getCommitCount() > 0L) {
            map.put("commitCount", statValue.getCommitCount());
        }
        if (statValue.getRollbackCount() > 0L) {
            map.put("rollbackCount", statValue.getRollbackCount());
        }
        if (statValue.getPstmtCacheHitCount() > 0L) {
            map.put("pstmtCacheHitCount", statValue.getPstmtCacheHitCount());
        }
        if (statValue.getPstmtCacheMissCount() > 0L) {
            map.put("pstmtCacheMissCount", statValue.getPstmtCacheMissCount());
        }
        if (statValue.getStartTransactionCount() > 0L) {
            map.put("startTransactionCount", statValue.getStartTransactionCount());
            map.put("transactionHistogram", JdbcSqlStatUtils.rtrim(statValue.getTransactionHistogram()));
        }
        if (statValue.getConnectCount() > 0L) {
            map.put("connectionHoldTimeHistogram", JdbcSqlStatUtils.rtrim(statValue.getConnectionHoldTimeHistogram()));
        }
        if (statValue.getClobOpenCount() > 0L) {
            map.put("clobOpenCount", statValue.getClobOpenCount());
        }
        if (statValue.getBlobOpenCount() > 0L) {
            map.put("blobOpenCount", statValue.getBlobOpenCount());
        }
        if (statValue.getSqlSkipCount() > 0L) {
            map.put("sqlSkipCount", statValue.getSqlSkipCount());
        }
        final ArrayList<Map<String, Object>> sqlList = new ArrayList<Map<String, Object>>();
        if (statValue.sqlList.size() > 0) {
            for (final JdbcSqlStatValue sqlStat : statValue.getSqlList()) {
                final Map<String, Object> sqlStatMap = new LinkedHashMap<String, Object>();
                sqlStatMap.put("sql", sqlStat.getSql());
                if (sqlStat.getExecuteCount() > 0L) {
                    sqlStatMap.put("executeCount", sqlStat.getExecuteCount());
                    sqlStatMap.put("executeMillisMax", sqlStat.getExecuteMillisMax());
                    sqlStatMap.put("executeMillisTotal", sqlStat.getExecuteMillisTotal());
                    sqlStatMap.put("executeHistogram", JdbcSqlStatUtils.rtrim(sqlStat.getExecuteHistogram()));
                    sqlStatMap.put("executeAndResultHoldHistogram", JdbcSqlStatUtils.rtrim(sqlStat.getExecuteAndResultHoldHistogram()));
                }
                final long executeErrorCount = sqlStat.getExecuteErrorCount();
                if (executeErrorCount > 0L) {
                    sqlStatMap.put("executeErrorCount", executeErrorCount);
                }
                final int runningCount = sqlStat.getRunningCount();
                if (runningCount > 0) {
                    sqlStatMap.put("runningCount", runningCount);
                }
                final int concurrentMax = sqlStat.getConcurrentMax();
                if (concurrentMax > 0) {
                    sqlStatMap.put("concurrentMax", concurrentMax);
                }
                if (sqlStat.getFetchRowCount() > 0L) {
                    sqlStatMap.put("fetchRowCount", sqlStat.getFetchRowCount());
                    sqlStatMap.put("fetchRowCountMax", sqlStat.getFetchRowCountMax());
                    sqlStatMap.put("fetchRowHistogram", JdbcSqlStatUtils.rtrim(sqlStat.getFetchRowHistogram()));
                }
                if (sqlStat.getUpdateCount() > 0L) {
                    sqlStatMap.put("updateCount", sqlStat.getUpdateCount());
                    sqlStatMap.put("updateCountMax", sqlStat.getUpdateCountMax());
                    sqlStatMap.put("updateHistogram", JdbcSqlStatUtils.rtrim(sqlStat.getUpdateHistogram()));
                }
                if (sqlStat.getInTransactionCount() > 0L) {
                    sqlStatMap.put("inTransactionCount", sqlStat.getInTransactionCount());
                }
                if (sqlStat.getClobOpenCount() > 0L) {
                    sqlStatMap.put("clobOpenCount", sqlStat.getClobOpenCount());
                }
                if (sqlStat.getBlobOpenCount() > 0L) {
                    sqlStatMap.put("blobOpenCount", sqlStat.getBlobOpenCount());
                }
                sqlList.add(sqlStatMap);
            }
            map.put("sqlList", sqlList);
        }
        if (statValue.getKeepAliveCheckCount() > 0L) {
            map.put("keepAliveCheckCount", statValue.getKeepAliveCheckCount());
        }
        final String text = JSONUtils.toJSONString(map);
        this.log(text);
    }
    
    static {
        DruidDataSourceStatLoggerImpl.LOG = LogFactory.getLog(DruidDataSourceStatLoggerImpl.class);
    }
}
