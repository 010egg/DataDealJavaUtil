// 
// Decompiled by Procyon v0.5.36
// 

package com.netease.hz.imp.trans.util;

import org.slf4j.LoggerFactory;
import java.sql.ResultSetMetaData;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.sql.PreparedStatement;
import java.util.List;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import org.slf4j.Logger;

public class GpManager
{
    private static final Logger logger;
    private Connection conn;
    
    public GpManager() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        this.conn = DriverManager.getConnection(GpUtil.URL, GpUtil.USERNAME, GpUtil.PASSWORD);
    }
    
    public int[] batchInsert(final String schema, final String table, final List<DataBean> dataBeans) throws SQLException {
        final String sql = String.format("INSERT INTO %s.%s (data,etl_time) VALUES (?,?)", schema, table);
        final PreparedStatement pstm = this.conn.prepareStatement(sql);
        pstm.setFetchSize(100);
        for (final DataBean dataBean : dataBeans) {
            pstm.setString(1, dataBean.getData());
            pstm.setString(2, dataBean.getEtl_time());
            pstm.addBatch();
        }
        final int[] result = pstm.executeBatch();
        return result;
    }
    
    public int[] eleBatchInsert(final String schema, final String table, final List<EleDataBean> eleDataBeans) throws SQLException {
        final String sql = String.format("INSERT INTO %s.%s (data,etl_time,type) VALUES (?,?,?)", schema, table);
        final PreparedStatement pstm = this.conn.prepareStatement(sql);
        pstm.setFetchSize(100);
        for (final EleDataBean dataBean : eleDataBeans) {
            pstm.setString(1, dataBean.getData());
            pstm.setString(2, dataBean.getEtl_time());
            pstm.setString(3, dataBean.getType());
            pstm.addBatch();
        }
        final int[] result = pstm.executeBatch();
        return result;
    }
    
    public List<Map<String, Object>> query(final String sql) throws SQLException {
        final List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
        final PreparedStatement pstm = this.conn.prepareStatement(sql);
        GpManager.logger.info("gp sql:" + sql);
        pstm.setFetchSize(100);
        final ResultSet rs = pstm.executeQuery();
        final ResultSetMetaData rsmd = rs.getMetaData();
        final int numCols = rsmd.getColumnCount();
        final String[] colNames = new String[numCols];
        for (int i = 0; i < numCols; ++i) {
            colNames[i] = rsmd.getColumnName(i + 1);
        }
        while (rs.next()) {
            final Map<String, Object> map = new HashMap<String, Object>();
            for (String colName : colNames) {
                colName = colName.replace("\"", "");
                final Object value = rs.getObject(colName);
                map.put(colName, value);
                maps.add(map);
            }
        }
        return maps;
    }
    
    static {
        logger = LoggerFactory.getLogger(GpManager.class);
    }
}
