// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import java.util.Arrays;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import oracle.jdbc.OracleResultSet;
import oracle.sql.ROWID;
import java.sql.ResultSet;
import java.util.Properties;
import oracle.jdbc.internal.OracleConnection;
import com.alibaba.druid.pool.DruidPooledConnection;
import oracle.jdbc.internal.OraclePreparedStatement;
import java.sql.SQLException;
import oracle.jdbc.OracleStatement;
import java.sql.PreparedStatement;
import javax.transaction.xa.XAException;
import oracle.jdbc.xa.client.OracleXAConnection;
import oracle.jdbc.driver.T4CXAConnection;
import javax.sql.XAConnection;
import java.sql.Connection;
import java.util.Set;
import com.alibaba.druid.support.logging.Log;

public class OracleUtils
{
    private static final Log LOG;
    private static Set<String> builtinFunctions;
    private static Set<String> builtinTables;
    private static Set<String> keywords;
    
    public static XAConnection OracleXAConnection(final Connection oracleConnection) throws XAException {
        final String oracleConnectionClassName = oracleConnection.getClass().getName();
        if ("oracle.jdbc.driver.T4CConnection".equals(oracleConnectionClassName)) {
            return (XAConnection)new T4CXAConnection(oracleConnection);
        }
        return (XAConnection)new OracleXAConnection(oracleConnection);
    }
    
    public static int getRowPrefetch(final PreparedStatement stmt) throws SQLException {
        final OracleStatement oracleStmt = stmt.unwrap(OracleStatement.class);
        if (oracleStmt == null) {
            return -1;
        }
        return oracleStmt.getRowPrefetch();
    }
    
    public static void setRowPrefetch(final PreparedStatement stmt, final int value) throws SQLException {
        final OracleStatement oracleStmt = stmt.unwrap(OracleStatement.class);
        if (oracleStmt != null) {
            oracleStmt.setRowPrefetch(value);
        }
    }
    
    public static void enterImplicitCache(final PreparedStatement stmt) throws SQLException {
        final OraclePreparedStatement oracleStmt = unwrapInternal(stmt);
        if (oracleStmt != null) {
            oracleStmt.enterImplicitCache();
        }
    }
    
    public static void exitImplicitCacheToClose(final PreparedStatement stmt) throws SQLException {
        final OraclePreparedStatement oracleStmt = unwrapInternal(stmt);
        if (oracleStmt != null) {
            oracleStmt.exitImplicitCacheToClose();
        }
    }
    
    public static void exitImplicitCacheToActive(final PreparedStatement stmt) throws SQLException {
        final OraclePreparedStatement oracleStmt = unwrapInternal(stmt);
        if (oracleStmt != null) {
            oracleStmt.exitImplicitCacheToActive();
        }
    }
    
    public static OraclePreparedStatement unwrapInternal(final PreparedStatement stmt) throws SQLException {
        if (stmt instanceof OraclePreparedStatement) {
            return (OraclePreparedStatement)stmt;
        }
        final OraclePreparedStatement unwrapped = stmt.unwrap(OraclePreparedStatement.class);
        if (unwrapped == null) {
            OracleUtils.LOG.error("can not unwrap statement : " + stmt.getClass());
        }
        return unwrapped;
    }
    
    public static short getVersionNumber(final DruidPooledConnection conn) throws SQLException {
        final OracleConnection oracleConn = (OracleConnection)unwrap(conn);
        return oracleConn.getVersionNumber();
    }
    
    public static void setDefaultRowPrefetch(final Connection conn, final int value) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        oracleConn.setDefaultRowPrefetch(value);
    }
    
    public static int getDefaultRowPrefetch(final Connection conn, final int value) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        return oracleConn.getDefaultRowPrefetch();
    }
    
    public static boolean getImplicitCachingEnabled(final Connection conn) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        return oracleConn.getImplicitCachingEnabled();
    }
    
    public static int getStatementCacheSize(final Connection conn) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        return oracleConn.getStatementCacheSize();
    }
    
    public static void purgeImplicitCache(final Connection conn) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        oracleConn.purgeImplicitCache();
    }
    
    public static void setImplicitCachingEnabled(final Connection conn, final boolean cache) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        oracleConn.setImplicitCachingEnabled(cache);
    }
    
    public static void setStatementCacheSize(final Connection conn, final int size) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        oracleConn.setStatementCacheSize(size);
    }
    
    public static int pingDatabase(final Connection conn) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        return oracleConn.pingDatabase();
    }
    
    public static void openProxySession(final Connection conn, final int type, final Properties prop) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        oracleConn.openProxySession(type, prop);
    }
    
    public static int getDefaultExecuteBatch(final Connection conn) throws SQLException {
        final oracle.jdbc.OracleConnection oracleConn = unwrap(conn);
        return oracleConn.getDefaultExecuteBatch();
    }
    
    public static oracle.jdbc.OracleConnection unwrap(final Connection conn) throws SQLException {
        if (conn instanceof oracle.jdbc.OracleConnection) {
            return (oracle.jdbc.OracleConnection)conn;
        }
        return conn.unwrap(oracle.jdbc.OracleConnection.class);
    }
    
    public static ROWID getROWID(final ResultSet rs, final int columnIndex) throws SQLException {
        final OracleResultSet oracleResultSet = rs.unwrap(OracleResultSet.class);
        return oracleResultSet.getROWID(columnIndex);
    }
    
    public static boolean isBuiltinFunction(final String function) {
        if (function == null) {
            return false;
        }
        final String function_lower = function.toLowerCase();
        Set<String> functions = OracleUtils.builtinFunctions;
        if (functions == null) {
            functions = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/oracle/builtin_functions", functions);
            OracleUtils.builtinFunctions = functions;
        }
        return functions.contains(function_lower);
    }
    
    public static boolean isBuiltinTable(final String table) {
        if (table == null) {
            return false;
        }
        final String table_lower = table.toLowerCase();
        Set<String> tables = OracleUtils.builtinTables;
        if (tables == null) {
            tables = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/oracle/builtin_tables", tables);
            OracleUtils.builtinTables = tables;
        }
        return tables.contains(table_lower);
    }
    
    public static boolean isKeyword(final String name) {
        if (name == null) {
            return false;
        }
        final String name_lower = name.toLowerCase();
        Set<String> words = OracleUtils.keywords;
        if (words == null) {
            words = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/oracle/keywords", words);
            OracleUtils.keywords = words;
        }
        return words.contains(name_lower);
    }
    
    public static List<String> showTables(final Connection conn) throws SQLException {
        final List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select table_name from user_tables");
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
    
    public static List<String> getTableDDL(final Connection conn, final String... tables) throws SQLException {
        return getTableDDL(conn, Arrays.asList(tables));
    }
    
    public static List<String> getTableDDL(final Connection conn, final List<String> tables) throws SQLException {
        final List<String> ddlList = new ArrayList<String>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            String sql = "select DBMS_METADATA.GET_DDL('TABLE', TABLE_NAME) FROM user_tables";
            if (tables.size() > 0) {
                sql += "IN (";
                for (int i = 0; i < tables.size(); ++i) {
                    if (i != 0) {
                        sql += ", ?";
                    }
                    else {
                        sql += "?";
                    }
                }
                sql += ")";
            }
            pstmt = conn.prepareStatement(sql);
            for (int i = 0; i < tables.size(); ++i) {
                pstmt.setString(i + 1, tables.get(i));
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                final String ddl = rs.getString(1);
                ddlList.add(ddl);
            }
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(pstmt);
        }
        return ddlList;
    }
    
    public static String getCreateTableScript(final Connection conn) throws SQLException {
        return getCreateTableScript(conn, true, true);
    }
    
    public static String getCreateTableScript(final Connection conn, final boolean sorted, final boolean simplify) throws SQLException {
        final List<String> ddlList = getTableDDL(conn, new String[0]);
        final StringBuilder buf = new StringBuilder();
        for (final String ddl : ddlList) {
            buf.append(ddl);
            buf.append(';');
        }
        final String ddlScript = buf.toString();
        if (!sorted && !simplify) {
            return ddlScript;
        }
        final List stmtList = SQLUtils.parseStatements(ddlScript, DbType.oracle);
        if (simplify) {
            for (final Object o : stmtList) {
                if (o instanceof SQLCreateTableStatement) {
                    final SQLCreateTableStatement createTableStmt = (SQLCreateTableStatement)o;
                    createTableStmt.simplify();
                }
            }
        }
        if (sorted) {
            SQLCreateTableStatement.sort(stmtList);
        }
        return SQLUtils.toSQLString(stmtList, DbType.oracle);
    }
    
    static {
        LOG = LogFactory.getLog(OracleUtils.class);
    }
}
