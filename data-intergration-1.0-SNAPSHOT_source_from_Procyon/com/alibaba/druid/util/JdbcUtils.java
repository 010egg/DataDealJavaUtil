// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.io.InputStream;
import java.util.Enumeration;
import java.net.URL;
import com.alibaba.druid.support.logging.LogFactory;
import java.util.Iterator;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.Map;
import java.util.Collections;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Arrays;
import javax.sql.DataSource;
import java.sql.Driver;
import com.alibaba.druid.DbType;
import java.sql.Date;
import java.sql.ResultSetMetaData;
import java.io.PrintStream;
import java.sql.SQLException;
import java.sql.Clob;
import java.sql.Blob;
import java.io.Closeable;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLRecoverableException;
import java.sql.Connection;
import java.util.Properties;
import com.alibaba.druid.support.logging.Log;

public final class JdbcUtils implements JdbcConstants
{
    private static final Log LOG;
    private static final Properties DRIVER_URL_MAPPING;
    private static Boolean mysql_driver_version_6;
    
    public static void close(final Connection x) {
        if (x == null) {
            return;
        }
        try {
            if (x.isClosed()) {
                return;
            }
            x.close();
        }
        catch (SQLRecoverableException ex) {}
        catch (Exception e) {
            JdbcUtils.LOG.debug("close connection error", e);
        }
    }
    
    public static void close(final Statement x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        }
        catch (Exception e) {
            boolean printError = true;
            if (e instanceof SQLRecoverableException && "Closed Connection".equals(e.getMessage())) {
                printError = false;
            }
            if (printError) {
                JdbcUtils.LOG.debug("close statement error", e);
            }
        }
    }
    
    public static void close(final ResultSet x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        }
        catch (Exception e) {
            JdbcUtils.LOG.debug("close result set error", e);
        }
    }
    
    public static void close(final Closeable x) {
        if (x == null) {
            return;
        }
        try {
            x.close();
        }
        catch (Exception e) {
            JdbcUtils.LOG.debug("close error", e);
        }
    }
    
    public static void close(final Blob x) {
        if (x == null) {
            return;
        }
        try {
            x.free();
        }
        catch (Exception e) {
            JdbcUtils.LOG.debug("close error", e);
        }
    }
    
    public static void close(final Clob x) {
        if (x == null) {
            return;
        }
        try {
            x.free();
        }
        catch (Exception e) {
            JdbcUtils.LOG.debug("close error", e);
        }
    }
    
    public static void printResultSet(final ResultSet rs) throws SQLException {
        printResultSet(rs, System.out);
    }
    
    public static void printResultSet(final ResultSet rs, final PrintStream out) throws SQLException {
        printResultSet(rs, out, true, "\t");
    }
    
    public static void printResultSet(final ResultSet rs, final PrintStream out, final boolean printHeader, final String seperator) throws SQLException {
        final ResultSetMetaData metadata = rs.getMetaData();
        final int columnCount = metadata.getColumnCount();
        if (printHeader) {
            for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print(seperator);
                }
                out.print(metadata.getColumnName(columnIndex));
            }
        }
        out.println();
        while (rs.next()) {
            for (int columnIndex = 1; columnIndex <= columnCount; ++columnIndex) {
                if (columnIndex != 1) {
                    out.print(seperator);
                }
                final int type = metadata.getColumnType(columnIndex);
                if (type == 12 || type == 1 || type == -9 || type == -15) {
                    out.print(rs.getString(columnIndex));
                }
                else if (type == 91) {
                    final Date date = rs.getDate(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(date.toString());
                    }
                }
                else if (type == -7) {
                    final boolean value = rs.getBoolean(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(Boolean.toString(value));
                    }
                }
                else if (type == 16) {
                    final boolean value = rs.getBoolean(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(Boolean.toString(value));
                    }
                }
                else if (type == -6) {
                    final byte value2 = rs.getByte(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(Byte.toString(value2));
                    }
                }
                else if (type == 5) {
                    final short value3 = rs.getShort(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(Short.toString(value3));
                    }
                }
                else if (type == 4) {
                    final int value4 = rs.getInt(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(Integer.toString(value4));
                    }
                }
                else if (type == -5) {
                    final long value5 = rs.getLong(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(Long.toString(value5));
                    }
                }
                else if (type == 93 || type == 2014) {
                    out.print(String.valueOf(rs.getTimestamp(columnIndex)));
                }
                else if (type == 3) {
                    out.print(String.valueOf(rs.getBigDecimal(columnIndex)));
                }
                else if (type == 2005) {
                    out.print(String.valueOf(rs.getString(columnIndex)));
                }
                else if (type == 2000) {
                    final Object object = rs.getObject(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(String.valueOf(object));
                    }
                }
                else if (type == -1) {
                    final Object object = rs.getString(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else {
                        out.print(String.valueOf(object));
                    }
                }
                else if (type == 0) {
                    out.print("null");
                }
                else {
                    final Object object = rs.getObject(columnIndex);
                    if (rs.wasNull()) {
                        out.print("null");
                    }
                    else if (object instanceof byte[]) {
                        final byte[] bytes = (byte[])object;
                        final String text = HexBin.encode(bytes);
                        out.print(text);
                    }
                    else {
                        out.print(String.valueOf(object));
                    }
                }
            }
            out.println();
        }
    }
    
    public static String getTypeName(final int sqlType) {
        switch (sqlType) {
            case 2003: {
                return "ARRAY";
            }
            case -5: {
                return "BIGINT";
            }
            case -2: {
                return "BINARY";
            }
            case -7: {
                return "BIT";
            }
            case 2004: {
                return "BLOB";
            }
            case 16: {
                return "BOOLEAN";
            }
            case 1: {
                return "CHAR";
            }
            case 2005: {
                return "CLOB";
            }
            case 70: {
                return "DATALINK";
            }
            case 91: {
                return "DATE";
            }
            case 3: {
                return "DECIMAL";
            }
            case 2001: {
                return "DISTINCT";
            }
            case 8: {
                return "DOUBLE";
            }
            case 6: {
                return "FLOAT";
            }
            case 4: {
                return "INTEGER";
            }
            case 2000: {
                return "JAVA_OBJECT";
            }
            case -16: {
                return "LONGNVARCHAR";
            }
            case -4: {
                return "LONGVARBINARY";
            }
            case -15: {
                return "NCHAR";
            }
            case 2011: {
                return "NCLOB";
            }
            case 0: {
                return "NULL";
            }
            case 2: {
                return "NUMERIC";
            }
            case -9: {
                return "NVARCHAR";
            }
            case 7: {
                return "REAL";
            }
            case 2006: {
                return "REF";
            }
            case -8: {
                return "ROWID";
            }
            case 5: {
                return "SMALLINT";
            }
            case 2009: {
                return "SQLXML";
            }
            case 2002: {
                return "STRUCT";
            }
            case 92: {
                return "TIME";
            }
            case 93: {
                return "TIMESTAMP";
            }
            case 2014: {
                return "TIMESTAMP_WITH_TIMEZONE";
            }
            case -6: {
                return "TINYINT";
            }
            case -3: {
                return "VARBINARY";
            }
            case 12: {
                return "VARCHAR";
            }
            default: {
                return "OTHER";
            }
        }
    }
    
    public static String getDriverClassName(final String rawUrl) throws SQLException {
        if (rawUrl == null) {
            return null;
        }
        if (rawUrl.startsWith("jdbc:derby:")) {
            return "org.apache.derby.jdbc.EmbeddedDriver";
        }
        if (rawUrl.startsWith("jdbc:mysql:")) {
            if (JdbcUtils.mysql_driver_version_6 == null) {
                JdbcUtils.mysql_driver_version_6 = (Utils.loadClass("com.mysql.cj.jdbc.Driver") != null);
            }
            if (JdbcUtils.mysql_driver_version_6) {
                return "com.mysql.cj.jdbc.Driver";
            }
            return "com.mysql.jdbc.Driver";
        }
        else {
            if (rawUrl.startsWith("jdbc:log4jdbc:")) {
                return "net.sf.log4jdbc.DriverSpy";
            }
            if (rawUrl.startsWith("jdbc:mariadb:")) {
                return "org.mariadb.jdbc.Driver";
            }
            if (rawUrl.startsWith("jdbc:oracle:") || rawUrl.startsWith("JDBC:oracle:")) {
                return "oracle.jdbc.OracleDriver";
            }
            if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
                return "com.alibaba.jdbc.AlibabaDriver";
            }
            if (rawUrl.startsWith("jdbc:oceanbase:")) {
                return "com.alipay.oceanbase.jdbc.Driver";
            }
            if (rawUrl.startsWith("jdbc:microsoft:")) {
                return "com.microsoft.jdbc.sqlserver.SQLServerDriver";
            }
            if (rawUrl.startsWith("jdbc:sqlserver:")) {
                return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
            }
            if (rawUrl.startsWith("jdbc:sybase:Tds:")) {
                return "com.sybase.jdbc2.jdbc.SybDriver";
            }
            if (rawUrl.startsWith("jdbc:jtds:")) {
                return "net.sourceforge.jtds.jdbc.Driver";
            }
            if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
                return "com.alibaba.druid.mock.MockDriver";
            }
            if (rawUrl.startsWith("jdbc:postgresql:")) {
                return "org.postgresql.Driver";
            }
            if (rawUrl.startsWith("jdbc:edb:")) {
                return "com.edb.Driver";
            }
            if (rawUrl.startsWith("jdbc:odps:")) {
                return "com.aliyun.odps.jdbc.OdpsDriver";
            }
            if (rawUrl.startsWith("jdbc:hsqldb:")) {
                return "org.hsqldb.jdbcDriver";
            }
            if (rawUrl.startsWith("jdbc:db2:")) {
                final String prefix = "jdbc:db2:";
                if (rawUrl.startsWith(prefix + "//")) {
                    return "com.ibm.db2.jcc.DB2Driver";
                }
                final String suffix = rawUrl.substring(prefix.length());
                if (suffix.indexOf(58) > 0) {
                    return "COM.ibm.db2.jdbc.net.DB2Driver";
                }
                return "COM.ibm.db2.jdbc.app.DB2Driver";
            }
            else {
                if (rawUrl.startsWith("jdbc:sqlite:")) {
                    return "org.sqlite.JDBC";
                }
                if (rawUrl.startsWith("jdbc:ingres:")) {
                    return "com.ingres.jdbc.IngresDriver";
                }
                if (rawUrl.startsWith("jdbc:h2:")) {
                    return "org.h2.Driver";
                }
                if (rawUrl.startsWith("jdbc:mckoi:")) {
                    return "com.mckoi.JDBCDriver";
                }
                if (rawUrl.startsWith("jdbc:cloudscape:")) {
                    return "COM.cloudscape.core.JDBCDriver";
                }
                if (rawUrl.startsWith("jdbc:informix-sqli:")) {
                    return "com.informix.jdbc.IfxDriver";
                }
                if (rawUrl.startsWith("jdbc:timesten:")) {
                    return "com.timesten.jdbc.TimesTenDriver";
                }
                if (rawUrl.startsWith("jdbc:as400:")) {
                    return "com.ibm.as400.access.AS400JDBCDriver";
                }
                if (rawUrl.startsWith("jdbc:sapdb:")) {
                    return "com.sap.dbtech.jdbc.DriverSapDB";
                }
                if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
                    return "com.jnetdirect.jsql.JSQLDriver";
                }
                if (rawUrl.startsWith("jdbc:JTurbo:")) {
                    return "com.newatlanta.jturbo.driver.Driver";
                }
                if (rawUrl.startsWith("jdbc:firebirdsql:")) {
                    return "org.firebirdsql.jdbc.FBDriver";
                }
                if (rawUrl.startsWith("jdbc:interbase:")) {
                    return "interbase.interclient.Driver";
                }
                if (rawUrl.startsWith("jdbc:pointbase:")) {
                    return "com.pointbase.jdbc.jdbcUniversalDriver";
                }
                if (rawUrl.startsWith("jdbc:edbc:")) {
                    return "ca.edbc.jdbc.EdbcDriver";
                }
                if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
                    return "com.mimer.jdbc.Driver";
                }
                if (rawUrl.startsWith("jdbc:dm:")) {
                    return "dm.jdbc.driver.DmDriver";
                }
                if (rawUrl.startsWith("jdbc:kingbase:")) {
                    return "com.kingbase.Driver";
                }
                if (rawUrl.startsWith("jdbc:kingbase8:")) {
                    return "com.kingbase8.Driver";
                }
                if (rawUrl.startsWith("jdbc:gbase:")) {
                    return "com.gbase.jdbc.Driver";
                }
                if (rawUrl.startsWith("jdbc:xugu:")) {
                    return "com.xugu.cloudjdbc.Driver";
                }
                if (rawUrl.startsWith("jdbc:hive:")) {
                    return "org.apache.hive.jdbc.HiveDriver";
                }
                if (rawUrl.startsWith("jdbc:hive2:")) {
                    return "org.apache.hive.jdbc.HiveDriver";
                }
                if (rawUrl.startsWith("jdbc:phoenix:thin:")) {
                    return "org.apache.phoenix.queryserver.client.Driver";
                }
                if (rawUrl.startsWith("jdbc:phoenix://")) {
                    return "org.apache.phoenix.jdbc.PhoenixDriver";
                }
                if (rawUrl.startsWith("jdbc:kylin:")) {
                    return "org.apache.kylin.jdbc.Driver";
                }
                if (rawUrl.startsWith("jdbc:elastic:")) {
                    return "com.alibaba.xdriver.elastic.jdbc.ElasticDriver";
                }
                if (rawUrl.startsWith("jdbc:clickhouse:")) {
                    return "ru.yandex.clickhouse.ClickHouseDriver";
                }
                if (rawUrl.startsWith("jdbc:presto:")) {
                    return "com.facebook.presto.jdbc.PrestoDriver";
                }
                if (rawUrl.startsWith("jdbc:trino:")) {
                    return "io.trino.jdbc.TrinoDriver";
                }
                if (rawUrl.startsWith("jdbc:inspur:")) {
                    return "com.inspur.jdbc.KdDriver";
                }
                if (rawUrl.startsWith("jdbc:polardb")) {
                    return "com.aliyun.polardb.Driver";
                }
                if (rawUrl.startsWith("jdbc:highgo:")) {
                    return "com.highgo.jdbc.Driver";
                }
                throw new SQLException("unknown jdbc driver : " + rawUrl);
            }
        }
    }
    
    public static DbType getDbTypeRaw(final String rawUrl, final String driverClassName) {
        if (rawUrl == null) {
            return null;
        }
        if (rawUrl.startsWith("jdbc:derby:") || rawUrl.startsWith("jdbc:log4jdbc:derby:")) {
            return DbType.derby;
        }
        if (rawUrl.startsWith("jdbc:mysql:") || rawUrl.startsWith("jdbc:cobar:") || rawUrl.startsWith("jdbc:log4jdbc:mysql:")) {
            return DbType.mysql;
        }
        if (rawUrl.startsWith("jdbc:mariadb:")) {
            return DbType.mariadb;
        }
        if (rawUrl.startsWith("jdbc:oracle:") || rawUrl.startsWith("jdbc:log4jdbc:oracle:")) {
            return DbType.oracle;
        }
        if (rawUrl.startsWith("jdbc:alibaba:oracle:")) {
            return DbType.ali_oracle;
        }
        if (rawUrl.startsWith("jdbc:oceanbase:oracle:")) {
            return DbType.oceanbase_oracle;
        }
        if (rawUrl.startsWith("jdbc:oceanbase:")) {
            return DbType.oceanbase;
        }
        if (rawUrl.startsWith("jdbc:microsoft:") || rawUrl.startsWith("jdbc:log4jdbc:microsoft:")) {
            return DbType.sqlserver;
        }
        if (rawUrl.startsWith("jdbc:sqlserver:") || rawUrl.startsWith("jdbc:log4jdbc:sqlserver:")) {
            return DbType.sqlserver;
        }
        if (rawUrl.startsWith("jdbc:sybase:Tds:") || rawUrl.startsWith("jdbc:log4jdbc:sybase:")) {
            return DbType.sybase;
        }
        if (rawUrl.startsWith("jdbc:jtds:") || rawUrl.startsWith("jdbc:log4jdbc:jtds:")) {
            return DbType.jtds;
        }
        if (rawUrl.startsWith("jdbc:fake:") || rawUrl.startsWith("jdbc:mock:")) {
            return DbType.mock;
        }
        if (rawUrl.startsWith("jdbc:postgresql:") || rawUrl.startsWith("jdbc:log4jdbc:postgresql:")) {
            return DbType.postgresql;
        }
        if (rawUrl.startsWith("jdbc:edb:")) {
            return DbType.edb;
        }
        if (rawUrl.startsWith("jdbc:hsqldb:") || rawUrl.startsWith("jdbc:log4jdbc:hsqldb:")) {
            return DbType.hsql;
        }
        if (rawUrl.startsWith("jdbc:odps:")) {
            return DbType.odps;
        }
        if (rawUrl.startsWith("jdbc:db2:")) {
            return DbType.db2;
        }
        if (rawUrl.startsWith("jdbc:sqlite:")) {
            return DbType.sqlite;
        }
        if (rawUrl.startsWith("jdbc:ingres:")) {
            return DbType.ingres;
        }
        if (rawUrl.startsWith("jdbc:h2:") || rawUrl.startsWith("jdbc:log4jdbc:h2:")) {
            return DbType.h2;
        }
        if (rawUrl.startsWith("jdbc:mckoi:")) {
            return DbType.mock;
        }
        if (rawUrl.startsWith("jdbc:cloudscape:")) {
            return DbType.cloudscape;
        }
        if (rawUrl.startsWith("jdbc:informix-sqli:") || rawUrl.startsWith("jdbc:log4jdbc:informix-sqli:")) {
            return DbType.informix;
        }
        if (rawUrl.startsWith("jdbc:timesten:")) {
            return DbType.timesten;
        }
        if (rawUrl.startsWith("jdbc:as400:")) {
            return DbType.as400;
        }
        if (rawUrl.startsWith("jdbc:sapdb:")) {
            return DbType.sapdb;
        }
        if (rawUrl.startsWith("jdbc:JSQLConnect:")) {
            return DbType.JSQLConnect;
        }
        if (rawUrl.startsWith("jdbc:JTurbo:")) {
            return DbType.JTurbo;
        }
        if (rawUrl.startsWith("jdbc:firebirdsql:")) {
            return DbType.firebirdsql;
        }
        if (rawUrl.startsWith("jdbc:interbase:")) {
            return DbType.interbase;
        }
        if (rawUrl.startsWith("jdbc:pointbase:")) {
            return DbType.pointbase;
        }
        if (rawUrl.startsWith("jdbc:edbc:")) {
            return DbType.edbc;
        }
        if (rawUrl.startsWith("jdbc:mimer:multi1:")) {
            return DbType.mimer;
        }
        if (rawUrl.startsWith("jdbc:dm:")) {
            return JdbcConstants.DM;
        }
        if (rawUrl.startsWith("jdbc:kingbase:") || rawUrl.startsWith("jdbc:kingbase8:")) {
            return JdbcConstants.KINGBASE;
        }
        if (rawUrl.startsWith("jdbc:gbase:")) {
            return JdbcConstants.GBASE;
        }
        if (rawUrl.startsWith("jdbc:xugu:")) {
            return JdbcConstants.XUGU;
        }
        if (rawUrl.startsWith("jdbc:log4jdbc:")) {
            return DbType.log4jdbc;
        }
        if (rawUrl.startsWith("jdbc:hive:")) {
            return DbType.hive;
        }
        if (rawUrl.startsWith("jdbc:hive2:")) {
            return DbType.hive;
        }
        if (rawUrl.startsWith("jdbc:phoenix:")) {
            return DbType.phoenix;
        }
        if (rawUrl.startsWith("jdbc:kylin:")) {
            return DbType.kylin;
        }
        if (rawUrl.startsWith("jdbc:elastic:")) {
            return DbType.elastic_search;
        }
        if (rawUrl.startsWith("jdbc:clickhouse:")) {
            return DbType.clickhouse;
        }
        if (rawUrl.startsWith("jdbc:presto:")) {
            return DbType.presto;
        }
        if (rawUrl.startsWith("jdbc:trino:")) {
            return DbType.trino;
        }
        if (rawUrl.startsWith("jdbc:inspur:")) {
            return DbType.kdb;
        }
        if (rawUrl.startsWith("jdbc:polardb")) {
            return DbType.polardb;
        }
        if (rawUrl.startsWith("jdbc:highgo:")) {
            return DbType.highgo;
        }
        if (rawUrl.startsWith("jdbc:pivotal:greenplum:") || rawUrl.startsWith("jdbc:datadirect:greenplum:")) {
            return DbType.greenplum;
        }
        return null;
    }
    
    public static String getDbType(final String rawUrl, final String driverClassName) {
        final DbType dbType = getDbTypeRaw(rawUrl, driverClassName);
        if (dbType == null) {
            return null;
        }
        return dbType.name();
    }
    
    public static Driver createDriver(final String driverClassName) throws SQLException {
        return createDriver(null, driverClassName);
    }
    
    public static Driver createDriver(final ClassLoader classLoader, final String driverClassName) throws SQLException {
        Class<?> clazz = null;
        if (classLoader != null) {
            try {
                clazz = classLoader.loadClass(driverClassName);
            }
            catch (ClassNotFoundException ex) {}
        }
        if (clazz == null) {
            try {
                final ClassLoader contextLoader = Thread.currentThread().getContextClassLoader();
                if (contextLoader != null) {
                    clazz = contextLoader.loadClass(driverClassName);
                }
            }
            catch (ClassNotFoundException ex2) {}
        }
        if (clazz == null) {
            try {
                clazz = Class.forName(driverClassName);
            }
            catch (ClassNotFoundException e) {
                throw new SQLException(e.getMessage(), e);
            }
        }
        try {
            return (Driver)clazz.newInstance();
        }
        catch (IllegalAccessException e2) {
            throw new SQLException(e2.getMessage(), e2);
        }
        catch (InstantiationException e3) {
            throw new SQLException(e3.getMessage(), e3);
        }
    }
    
    public static int executeUpdate(final DataSource dataSource, final String sql, final Object... parameters) throws SQLException {
        return executeUpdate(dataSource, sql, Arrays.asList(parameters));
    }
    
    public static int executeUpdate(final DataSource dataSource, final String sql, final List<Object> parameters) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return executeUpdate(conn, sql, parameters);
        }
        finally {
            close(conn);
        }
    }
    
    public static int executeUpdate(final Connection conn, final String sql, final List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;
        int updateCount;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            updateCount = stmt.executeUpdate();
        }
        finally {
            close(stmt);
        }
        return updateCount;
    }
    
    public static void execute(final DataSource dataSource, final String sql, final Object... parameters) throws SQLException {
        execute(dataSource, sql, Arrays.asList(parameters));
    }
    
    public static void execute(final DataSource dataSource, final String sql, final List<Object> parameters) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            execute(conn, sql, parameters);
        }
        finally {
            close(conn);
        }
    }
    
    public static void execute(final Connection conn, final String sql) throws SQLException {
        execute(conn, sql, Collections.emptyList());
    }
    
    public static void execute(final Connection conn, final String sql, final List<Object> parameters) throws SQLException {
        PreparedStatement stmt = null;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            stmt.executeUpdate();
        }
        finally {
            close(stmt);
        }
    }
    
    public static List<Map<String, Object>> executeQuery(final DataSource dataSource, final String sql, final Object... parameters) throws SQLException {
        return executeQuery(dataSource, sql, Arrays.asList(parameters));
    }
    
    public static List<Map<String, Object>> executeQuery(final DataSource dataSource, final String sql, final List<Object> parameters) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            return executeQuery(conn, sql, parameters);
        }
        finally {
            close(conn);
        }
    }
    
    public static List<Map<String, Object>> executeQuery(final Connection conn, final String sql, final List<Object> parameters) throws SQLException {
        final List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.prepareStatement(sql);
            setParameters(stmt, parameters);
            rs = stmt.executeQuery();
            final ResultSetMetaData rsMeta = rs.getMetaData();
            while (rs.next()) {
                final Map<String, Object> row = new LinkedHashMap<String, Object>();
                for (int i = 0, size = rsMeta.getColumnCount(); i < size; ++i) {
                    final String columName = rsMeta.getColumnLabel(i + 1);
                    final Object value = rs.getObject(i + 1);
                    row.put(columName, value);
                }
                rows.add(row);
            }
        }
        finally {
            close(rs);
            close(stmt);
        }
        return rows;
    }
    
    private static void setParameters(final PreparedStatement stmt, final List<Object> parameters) throws SQLException {
        for (int i = 0, size = parameters.size(); i < size; ++i) {
            final Object param = parameters.get(i);
            stmt.setObject(i + 1, param);
        }
    }
    
    public static void insertToTable(final DataSource dataSource, final String tableName, final Map<String, Object> data) throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            insertToTable(conn, tableName, data);
        }
        finally {
            close(conn);
        }
    }
    
    public static void insertToTable(final Connection conn, final String tableName, final Map<String, Object> data) throws SQLException {
        final String sql = makeInsertToTableSql(tableName, data.keySet());
        final List<Object> parameters = new ArrayList<Object>(data.values());
        execute(conn, sql, parameters);
    }
    
    public static String makeInsertToTableSql(final String tableName, final Collection<String> names) {
        final StringBuilder sql = new StringBuilder().append("insert into ").append(tableName).append("(");
        int nameCount = 0;
        for (final String name : names) {
            if (nameCount > 0) {
                sql.append(",");
            }
            sql.append(name);
            ++nameCount;
        }
        sql.append(") values (");
        for (int i = 0; i < nameCount; ++i) {
            if (i != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }
    
    public static <T> void executeQuery(final DataSource dataSource, final ResultSetConsumer<T> consumer, final String sql, final Object... parameters) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            for (int i = 0; i < parameters.length; ++i) {
                stmt.setObject(i + 1, parameters[i]);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                if (consumer != null) {
                    final T object = consumer.apply(rs);
                    consumer.accept(object);
                }
            }
        }
        finally {
            close(rs);
            close(stmt);
            close(conn);
        }
    }
    
    public static List<String> showTables(final Connection conn, final DbType dbType) throws SQLException {
        if (DbType.mysql == dbType || DbType.oceanbase == dbType) {
            return MySqlUtils.showTables(conn);
        }
        if (dbType == DbType.oracle || dbType == DbType.oceanbase_oracle) {
            return OracleUtils.showTables(conn);
        }
        if (dbType == DbType.postgresql) {
            return PGUtils.showTables(conn);
        }
        throw new SQLException("show tables dbType not support for " + dbType);
    }
    
    public static String getCreateTableScript(final Connection conn, final DbType dbType) throws SQLException {
        return getCreateTableScript(conn, dbType, true, true);
    }
    
    public static String getCreateTableScript(final Connection conn, final DbType dbType, final boolean sorted, final boolean simplify) throws SQLException {
        if (DbType.mysql == dbType || DbType.oceanbase == dbType) {
            return MySqlUtils.getCreateTableScript(conn, sorted, simplify);
        }
        if (dbType == DbType.oracle || dbType == DbType.oceanbase_oracle) {
            return OracleUtils.getCreateTableScript(conn, sorted, simplify);
        }
        throw new SQLException("getCreateTableScript dbType not support for " + dbType);
    }
    
    public static boolean isMySqlDriver(final String driverClassName) {
        return driverClassName.equals("com.mysql.jdbc.Driver") || driverClassName.equals("com.mysql.cj.jdbc.Driver") || driverClassName.equals("com.mysql.jdbc.");
    }
    
    public static boolean isOracleDbType(final String dbType) {
        return DbType.oracle.name().equals(dbType) || DbType.oceanbase.name().equals(dbType) || DbType.ali_oracle.name().equalsIgnoreCase(dbType);
    }
    
    public static boolean isOracleDbType(final DbType dbType) {
        return DbType.oracle == dbType || DbType.oceanbase == dbType || DbType.ali_oracle == dbType;
    }
    
    public static boolean isMysqlDbType(final String dbTypeName) {
        return isMysqlDbType(DbType.of(dbTypeName));
    }
    
    public static boolean isMysqlDbType(final DbType dbType) {
        if (dbType == null) {
            return false;
        }
        switch (dbType) {
            case mysql:
            case oceanbase:
            case drds:
            case mariadb:
            case h2: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isPgsqlDbType(final String dbTypeName) {
        return isPgsqlDbType(DbType.of(dbTypeName));
    }
    
    public static boolean isPgsqlDbType(final DbType dbType) {
        if (dbType == null) {
            return false;
        }
        switch (dbType) {
            case postgresql:
            case edb:
            case polardb:
            case greenplum:
            case gaussdb: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    public static boolean isSqlserverDbType(final String dbTypeName) {
        return isSqlserverDbType(DbType.of(dbTypeName));
    }
    
    public static boolean isSqlserverDbType(final DbType dbType) {
        if (dbType == null) {
            return false;
        }
        switch (dbType) {
            case sqlserver:
            case jtds: {
                return true;
            }
            default: {
                return false;
            }
        }
    }
    
    static {
        LOG = LogFactory.getLog(JdbcUtils.class);
        DRIVER_URL_MAPPING = new Properties();
        JdbcUtils.mysql_driver_version_6 = null;
        try {
            final ClassLoader ctxClassLoader = Thread.currentThread().getContextClassLoader();
            if (ctxClassLoader != null) {
                final Enumeration<URL> e = ctxClassLoader.getResources("META-INF/druid-driver.properties");
                while (e.hasMoreElements()) {
                    final URL url = e.nextElement();
                    final Properties property = new Properties();
                    InputStream is = null;
                    try {
                        is = url.openStream();
                        property.load(is);
                    }
                    finally {
                        close(is);
                    }
                    JdbcUtils.DRIVER_URL_MAPPING.putAll(property);
                }
            }
        }
        catch (Exception e2) {
            JdbcUtils.LOG.error("load druid-driver.properties error", e2);
        }
    }
}
