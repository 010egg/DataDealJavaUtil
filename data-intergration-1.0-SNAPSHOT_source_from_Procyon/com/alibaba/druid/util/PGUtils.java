// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.Arrays;
import java.util.HashSet;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;
import org.postgresql.xa.PGXAConnection;
import org.postgresql.core.BaseConnection;
import javax.sql.XAConnection;
import java.sql.Connection;
import java.util.Set;

public class PGUtils
{
    private static Set<String> keywords;
    private static final long[] pseudoColumnHashCodes;
    
    public static XAConnection createXAConnection(final Connection physicalConn) throws SQLException {
        return new PGXAConnection((BaseConnection)physicalConn);
    }
    
    public static List<String> showTables(final Connection conn) throws SQLException {
        final List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT tablename FROM pg_catalog.pg_tables where schemaname not in ('pg_catalog', 'information_schema', 'sys')");
            while (rs.next()) {
                final String tableName = rs.getString(1);
                tables.add(tableName);
            }
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }
    
    public static boolean isKeyword(final String name) {
        if (name == null) {
            return false;
        }
        final String name_lower = name.toLowerCase();
        Set<String> words = PGUtils.keywords;
        if (words == null) {
            words = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/postgresql/keywords", words);
            PGUtils.keywords = words;
        }
        return words.contains(name_lower);
    }
    
    public static boolean isPseudoColumn(final long hash) {
        return Arrays.binarySearch(PGUtils.pseudoColumnHashCodes, hash) >= 0;
    }
    
    static {
        final long[] array = { FnvHash.Constants.CURRENT_TIMESTAMP };
        Arrays.sort(array);
        pseudoColumnHashCodes = array;
    }
}
