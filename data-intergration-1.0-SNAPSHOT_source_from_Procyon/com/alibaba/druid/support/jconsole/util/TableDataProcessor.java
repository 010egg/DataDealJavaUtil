// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.jconsole.util;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.support.json.JSONUtils;
import javax.management.ObjectName;
import javax.management.MBeanServerConnection;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.support.logging.Log;

public final class TableDataProcessor
{
    private static final String COLUMN_KEY_NAME = "\u540d\u79f0";
    private static final String COLUMN_VALUE_NAME = "\u503c";
    private static final String RESP_JSON_RESULT_KEY = "ResultCode";
    private static final String RESP_JSON_CONTENT_KEY = "Content";
    protected static final int RESP_SUCCESS_RESULT = 1;
    private static final Log LOG;
    
    private TableDataProcessor() {
    }
    
    public static ColumnData row2col(final ArrayList<LinkedHashMap<String, Object>> rowData, final String keyword) {
        final ColumnData data = new ColumnData();
        final ArrayList<LinkedHashMap<String, Object>> colData = new ArrayList<LinkedHashMap<String, Object>>();
        final ArrayList<String> colNames = new ArrayList<String>();
        int rowCount = 0;
        int colCount = 0;
        for (final LinkedHashMap<String, Object> row : rowData) {
            if (keyword != null) {
                final String keyNow = row.remove(keyword).toString();
                colNames.add(keyNow);
            }
            ++rowCount;
            for (final Map.Entry<String, Object> element : row.entrySet()) {
                final LinkedHashMap<String, Object> colDataItem = new LinkedHashMap<String, Object>();
                colDataItem.put("\u540d\u79f0", element.getKey());
                colDataItem.put("\u503c", element.getValue());
                colData.add(colDataItem);
                if (rowCount == 1) {
                    ++colCount;
                }
            }
        }
        data.setCount(colCount);
        data.setData(colData);
        data.setNames(colNames);
        return data;
    }
    
    public static ColumnData multiRow2Col(final ArrayList<LinkedHashMap<String, Object>> rowData, final String keyword) {
        final ColumnData data = new ColumnData();
        final ArrayList<ArrayList<LinkedHashMap<String, Object>>> tableData = new ArrayList<ArrayList<LinkedHashMap<String, Object>>>();
        final ArrayList<String> colNames = new ArrayList<String>();
        int rowCount = 0;
        for (final LinkedHashMap<String, Object> row : rowData) {
            if (keyword != null) {
                final String keyNow = row.remove(keyword).toString();
                colNames.add(keyNow);
            }
            ++rowCount;
            final ArrayList<LinkedHashMap<String, Object>> colData = new ArrayList<LinkedHashMap<String, Object>>();
            for (final Map.Entry<String, Object> element : row.entrySet()) {
                final LinkedHashMap<String, Object> colDataItem = new LinkedHashMap<String, Object>();
                colDataItem.put("\u540d\u79f0", element.getKey());
                colDataItem.put("\u503c", element.getValue());
                colData.add(colDataItem);
            }
            tableData.add(colData);
        }
        data.setCount(rowCount);
        data.setTableData(tableData);
        data.setNames(colNames);
        return data;
    }
    
    public static ColumnData row2col(final ArrayList<LinkedHashMap<String, Object>> rowData) {
        return row2col(rowData, null);
    }
    
    public static ArrayList<LinkedHashMap<String, Object>> parseData(final Object respData) {
        ArrayList<LinkedHashMap<String, Object>> data = null;
        if (respData instanceof Map) {
            final LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>)respData;
            final int rv = map.get("ResultCode");
            if (rv == 1) {
                final Object content = map.get("Content");
                if (content instanceof List) {
                    data = (ArrayList<LinkedHashMap<String, Object>>)content;
                }
                else if (content instanceof Map) {
                    final LinkedHashMap<String, Object> contentEle = (LinkedHashMap<String, Object>)content;
                    data = new ArrayList<LinkedHashMap<String, Object>>();
                    data.add(contentEle);
                }
            }
        }
        return data;
    }
    
    public static Object getData(final String url, final MBeanServerConnection conn) throws Exception {
        Object o = null;
        final ObjectName name = new ObjectName("com.alibaba.druid:type=DruidStatService");
        final String result = (String)conn.invoke(name, "service", new String[] { url }, new String[] { String.class.getName() });
        o = JSONUtils.parse(result);
        if (TableDataProcessor.LOG.isDebugEnabled()) {
            TableDataProcessor.LOG.debug(o.toString());
        }
        return o;
    }
    
    static {
        LOG = LogFactory.getLog(TableDataProcessor.class);
    }
    
    public static class ColumnData
    {
        private ArrayList<String> names;
        private ArrayList<LinkedHashMap<String, Object>> data;
        private ArrayList<ArrayList<LinkedHashMap<String, Object>>> tableData;
        private int count;
        
        public ArrayList<String> getNames() {
            return this.names;
        }
        
        public void setNames(final ArrayList<String> names) {
            this.names = names;
        }
        
        public ArrayList<LinkedHashMap<String, Object>> getData() {
            return this.data;
        }
        
        public void setData(final ArrayList<LinkedHashMap<String, Object>> data) {
            this.data = data;
        }
        
        public int getCount() {
            return this.count;
        }
        
        public void setCount(final int count) {
            this.count = count;
        }
        
        public ArrayList<ArrayList<LinkedHashMap<String, Object>>> getTableData() {
            return this.tableData;
        }
        
        public void setTableData(final ArrayList<ArrayList<LinkedHashMap<String, Object>>> tableData) {
            this.tableData = tableData;
        }
    }
}
