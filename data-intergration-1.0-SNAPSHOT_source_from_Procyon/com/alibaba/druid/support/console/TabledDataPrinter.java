// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.console;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.io.PrintStream;
import java.util.List;

public class TabledDataPrinter
{
    private static final int SQL_MAX_LEN = 32;
    private static final int MAX_COL = 4;
    private static final String[] sqlRowTitle;
    private static final String[] sqlRowField;
    private static final String[] sqlColField;
    private static final String[] dsRowTitle;
    private static final String[] dsRowField;
    private static final String[] dsColField;
    
    public static void printActiveConnStack(final List<List<String>> content, final Option opt) {
        final PrintStream out = opt.getPrintStream();
        for (final List<String> stack : content) {
            for (final String line : stack) {
                out.println(line);
            }
            out.println("===============================\n");
        }
    }
    
    public static void printDataSourceData(final List<Map<String, Object>> content, final Option opt) {
        while (true) {
            _printDataSourceData(content, opt);
            if (opt.getInterval() == -1) {
                break;
            }
            try {
                Thread.sleep(opt.getInterval() * 1000);
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
    }
    
    public static void _printDataSourceData(List<Map<String, Object>> content, final Option opt) {
        final PrintStream out = opt.getPrintStream();
        if (opt.getId() != -1) {
            final List<Map<String, Object>> matchedContent = new ArrayList<Map<String, Object>>();
            for (final Map<String, Object> dsStat : content) {
                final Integer idStr = dsStat.get("Identity");
                if (idStr == opt.getId()) {
                    matchedContent.add(dsStat);
                    break;
                }
            }
            content = matchedContent;
        }
        if (opt.isDetailPrint()) {
            out.println(getVerticalFormattedOutput(content, TabledDataPrinter.dsColField));
        }
        else {
            out.println(getFormattedOutput(content, TabledDataPrinter.dsRowTitle, TabledDataPrinter.dsRowField));
        }
    }
    
    public static void printSqlData(final List<Map<String, Object>> content, final Option opt) {
        while (true) {
            _printSqlData(content, opt);
            if (opt.getInterval() == -1) {
                break;
            }
            try {
                Thread.sleep(opt.getInterval() * 1000);
                continue;
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            break;
        }
    }
    
    public static void _printSqlData(List<Map<String, Object>> content, final Option opt) {
        final PrintStream out = opt.getPrintStream();
        if (opt.getId() != -1) {
            final List<Map<String, Object>> matchedContent = new ArrayList<Map<String, Object>>();
            for (final Map<String, Object> sqlStat : content) {
                final Integer idStr = sqlStat.get("ID");
                if (idStr == opt.getId()) {
                    matchedContent.add(sqlStat);
                    if (opt.isDetailPrint()) {
                        final DbType dbType = DbType.of(sqlStat.get("DbType"));
                        final String sql = sqlStat.get("SQL");
                        out.println("Formatted SQL:");
                        out.println(SQLUtils.format(sql, dbType));
                        out.println();
                        break;
                    }
                    break;
                }
            }
            content = matchedContent;
        }
        if (opt.isDetailPrint()) {
            out.println(getVerticalFormattedOutput(content, TabledDataPrinter.sqlColField));
        }
        else {
            out.println(getFormattedOutput(content, TabledDataPrinter.sqlRowTitle, TabledDataPrinter.sqlRowField));
        }
    }
    
    public static String getFormattedOutput(final List<Map<String, Object>> content, final String[] title, final String[] rowField) {
        final List<String[]> printContents = new ArrayList<String[]>();
        printContents.add(title);
        for (final Map<String, Object> sqlStat : content) {
            final String[] row = new String[rowField.length];
            for (int i = 0; i < rowField.length; ++i) {
                final Object value = sqlStat.get(rowField[i]);
                row[i] = handleAndConvert(value, rowField[i]);
            }
            printContents.add(row);
        }
        return TableFormatter.format(printContents);
    }
    
    public static String getVerticalFormattedOutput(final List<Map<String, Object>> content, final String[] titleFields) {
        final List<String[]> printContents = new ArrayList<String[]>();
        final int maxCol = (content.size() > 4) ? 4 : content.size();
        for (final String titleField : titleFields) {
            final String[] row = new String[maxCol + 1];
            row[0] = titleField;
            for (int j = 0; j < maxCol; ++j) {
                final Map<String, Object> sqlStat = content.get(j);
                final Object value = sqlStat.get(titleField);
                row[j + 1] = handleAndConvert(value, titleField);
            }
            printContents.add(row);
        }
        return TableFormatter.format(printContents);
    }
    
    public static String handleAndConvert(Object value, final String fieldName) {
        if (value == null) {
            value = "";
        }
        if (fieldName.equals("SQL")) {
            String sql = (String)value;
            sql = sql.replace("\n", " ");
            sql = sql.replace("\t", " ");
            if (sql.length() > 32) {
                sql = sql.substring(0, 29) + "...";
            }
            value = sql;
        }
        return value.toString();
    }
    
    static {
        sqlRowTitle = new String[] { "ID", "SQL", "ExecCount", "ExecTime", "ExecMax", "Txn", "Error", "Update", "FetchRow", "Running", "Concurrent", "ExecRsHisto" };
        sqlRowField = new String[] { "ID", "SQL", "ExecuteCount", "TotalTime", "MaxTimespan", "InTransactionCount", "ErrorCount", "EffectedRowCount", "FetchRowCount", "RunningCount", "ConcurrentMax", "ExecuteAndResultHoldTimeHistogram" };
        sqlColField = new String[] { "ID", "DataSource", "SQL", "ExecuteCount", "ErrorCount", "TotalTime", "LastTime", "MaxTimespan", "LastError", "EffectedRowCount", "FetchRowCount", "MaxTimespanOccurTime", "BatchSizeMax", "BatchSizeTotal", "ConcurrentMax", "RunningCount", "Name", "File", "LastErrorMessage", "LastErrorClass", "LastErrorStackTrace", "LastErrorTime", "DbType", "URL", "InTransactionCount", "Histogram", "LastSlowParameters", "ResultSetHoldTime", "ExecuteAndResultSetHoldTime", "FetchRowCountHistogram", "EffectedRowCountHistogram", "ExecuteAndResultHoldTimeHistogram", "EffectedRowCountMax", "FetchRowCountMax", "ClobOpenCount" };
        dsRowTitle = new String[] { "Identity", "DbType", "PoolingCount", "PoolingPeak", "PoolingPeakTime", "ActiveCount", "ActivePeak", "ActivePeakTime", "ExecuteCount", "ErrorCount" };
        dsRowField = new String[] { "Identity", "DbType", "PoolingCount", "PoolingPeak", "PoolingPeakTime", "ActiveCount", "ActivePeak", "ActivePeakTime", "ExecuteCount", "ErrorCount" };
        dsColField = new String[] { "Identity", "Name", "DbType", "DriverClassName", "URL", "UserName", "FilterClassNames", "WaitThreadCount", "NotEmptyWaitCount", "NotEmptyWaitMillis", "PoolingCount", "PoolingPeak", "PoolingPeakTime", "ActiveCount", "ActivePeak", "ActivePeakTime", "InitialSize", "MinIdle", "MaxActive", "QueryTimeout", "TransactionQueryTimeout", "LoginTimeout", "ValidConnectionCheckerClassName", "ExceptionSorterClassName", "TestOnBorrow", "TestOnReturn", "TestWhileIdle", "DefaultAutoCommit", "DefaultReadOnly", "DefaultTransactionIsolation", "LogicConnectCount", "LogicCloseCount", "LogicConnectErrorCount", "PhysicalConnectCount", "PhysicalCloseCount", "PhysicalConnectErrorCount", "ExecuteCount", "ErrorCount", "CommitCount", "RollbackCount", "PSCacheAccessCount", "PSCacheHitCount", "PSCacheMissCount", "StartTransactionCount", "TransactionHistogram", "ConnectionHoldTimeHistogram", "RemoveAbandoned", "ClobOpenCount" };
    }
}
