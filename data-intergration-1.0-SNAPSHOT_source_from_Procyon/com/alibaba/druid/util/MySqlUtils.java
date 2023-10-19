// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.Arrays;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.text.DateFormat;
import java.util.TimeZone;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import java.util.Iterator;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.sql.SQLException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLFeatureNotSupportedException;
import javax.sql.XAConnection;
import java.sql.Connection;
import java.sql.Driver;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Set;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class MySqlUtils
{
    static Class<?> utilClass;
    static boolean utilClassError;
    static boolean utilClass_isJdbc4;
    static Class<?> class_5_connection;
    static Method method_5_getPinGlobalTxToPhysicalConnection;
    static Class<?> class_5_suspendableXAConnection;
    static Constructor<?> constructor_5_suspendableXAConnection;
    static Class<?> class_5_JDBC4SuspendableXAConnection;
    static Constructor<?> constructor_5_JDBC4SuspendableXAConnection;
    static Class<?> class_5_MysqlXAConnection;
    static Constructor<?> constructor_5_MysqlXAConnection;
    static Class<?> class_ConnectionImpl;
    static Method method_getId;
    static boolean method_getId_error;
    static Class<?> class_6_ConnectionImpl;
    static Method method_6_getId;
    static volatile Class<?> class_6_connection;
    static volatile Method method_6_getPropertySet;
    static volatile Method method_6_getBooleanReadableProperty;
    static volatile Method method_6_getValue;
    static volatile boolean method_6_getValue_error;
    static volatile Class<?> class_6_suspendableXAConnection;
    static volatile Method method_6_getInstance;
    static volatile boolean method_6_getInstance_error;
    static volatile Method method_6_getInstanceXA;
    static volatile boolean method_6_getInstanceXA_error;
    static volatile Class<?> class_6_JDBC4SuspendableXAConnection;
    private static Set<String> keywords;
    private static Set<String> builtinDataTypes;
    private static transient Class class_connectionImpl;
    private static transient boolean class_connectionImpl_Error;
    private static transient Method method_getIO;
    private static transient boolean method_getIO_error;
    private static transient Class class_MysqlIO;
    private static transient boolean class_MysqlIO_Error;
    private static transient Method method_getLastPacketReceivedTimeMs;
    private static transient boolean method_getLastPacketReceivedTimeMs_error;
    private static volatile boolean mysqlJdbcVersion6;
    private static transient Class classJdbc;
    private static transient Method getIdleFor;
    private static transient boolean getIdleForError;
    static Class<?> class_5_CommunicationsException;
    static Class<?> class_6_CommunicationsException;
    public static final Charset GBK;
    public static final Charset BIG5;
    public static final Charset UTF8;
    public static final Charset UTF16;
    public static final Charset UTF32;
    public static final Charset ASCII;
    private static BigInteger[] MAX_INT;
    private static BigInteger[] MIN_INT;
    private static BigDecimal[] MAX_DEC_1;
    private static BigDecimal[] MIN_DEC_1;
    private static BigDecimal[] MAX_DEC_2;
    private static BigDecimal[] MIN_DEC_2;
    private static final String[] parseFormats;
    private static final long[] parseFormatCodes;
    
    public static XAConnection createXAConnection(final Driver driver, final Connection physicalConn) throws SQLException {
        final int major = driver.getMajorVersion();
        if (major == 5) {
            if (MySqlUtils.utilClass == null && !MySqlUtils.utilClassError) {
                try {
                    MySqlUtils.utilClass = Class.forName("com.mysql.jdbc.Util");
                    final Method method = MySqlUtils.utilClass.getMethod("isJdbc4", (Class<?>[])new Class[0]);
                    MySqlUtils.utilClass_isJdbc4 = (boolean)method.invoke(null, new Object[0]);
                    MySqlUtils.class_5_connection = Class.forName("com.mysql.jdbc.Connection");
                    MySqlUtils.method_5_getPinGlobalTxToPhysicalConnection = MySqlUtils.class_5_connection.getMethod("getPinGlobalTxToPhysicalConnection", (Class<?>[])new Class[0]);
                    MySqlUtils.class_5_suspendableXAConnection = Class.forName("com.mysql.jdbc.jdbc2.optional.SuspendableXAConnection");
                    MySqlUtils.constructor_5_suspendableXAConnection = MySqlUtils.class_5_suspendableXAConnection.getConstructor(MySqlUtils.class_5_connection);
                    MySqlUtils.class_5_JDBC4SuspendableXAConnection = Class.forName("com.mysql.jdbc.jdbc2.optional.JDBC4SuspendableXAConnection");
                    MySqlUtils.constructor_5_JDBC4SuspendableXAConnection = MySqlUtils.class_5_JDBC4SuspendableXAConnection.getConstructor(MySqlUtils.class_5_connection);
                    MySqlUtils.class_5_MysqlXAConnection = Class.forName("com.mysql.jdbc.jdbc2.optional.MysqlXAConnection");
                    MySqlUtils.constructor_5_MysqlXAConnection = MySqlUtils.class_5_MysqlXAConnection.getConstructor(MySqlUtils.class_5_connection, Boolean.TYPE);
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    MySqlUtils.utilClassError = true;
                }
            }
            try {
                final boolean pinGlobTx = (boolean)MySqlUtils.method_5_getPinGlobalTxToPhysicalConnection.invoke(physicalConn, new Object[0]);
                if (!pinGlobTx) {
                    return (XAConnection)MySqlUtils.constructor_5_MysqlXAConnection.newInstance(physicalConn, Boolean.FALSE);
                }
                if (!MySqlUtils.utilClass_isJdbc4) {
                    return (XAConnection)MySqlUtils.constructor_5_suspendableXAConnection.newInstance(physicalConn);
                }
                return (XAConnection)MySqlUtils.constructor_5_JDBC4SuspendableXAConnection.newInstance(physicalConn);
            }
            catch (Exception e) {
                e.printStackTrace();
                throw new SQLFeatureNotSupportedException();
            }
        }
        if (major == 6 || major == 8) {
            if (MySqlUtils.method_6_getValue == null && !MySqlUtils.method_6_getValue_error) {
                try {
                    MySqlUtils.class_6_connection = Class.forName("com.mysql.cj.api.jdbc.JdbcConnection");
                }
                catch (Throwable t) {}
                try {
                    if (MySqlUtils.class_6_connection == null) {
                        MySqlUtils.class_6_connection = Class.forName("com.mysql.cj.jdbc.JdbcConnection");
                        MySqlUtils.method_6_getPropertySet = MySqlUtils.class_6_connection.getMethod("getPropertySet", (Class<?>[])new Class[0]);
                        final Class<?> propertySetClass = Class.forName("com.mysql.cj.conf.PropertySet");
                        NoSuchMethodException noSuchMethodException = null;
                        try {
                            MySqlUtils.method_6_getBooleanReadableProperty = propertySetClass.getMethod("getBooleanReadableProperty", String.class);
                            MySqlUtils.method_6_getValue = Class.forName("com.mysql.cj.conf.ReadableProperty").getMethod("getValue", (Class<?>[])new Class[0]);
                        }
                        catch (NoSuchMethodException error) {
                            noSuchMethodException = error;
                        }
                        if (MySqlUtils.method_6_getBooleanReadableProperty == null) {
                            MySqlUtils.method_6_getBooleanReadableProperty = propertySetClass.getMethod("getBooleanProperty", String.class);
                            MySqlUtils.method_6_getValue = Class.forName("com.mysql.cj.conf.RuntimeProperty").getMethod("getValue", (Class<?>[])new Class[0]);
                        }
                    }
                    else {
                        MySqlUtils.method_6_getPropertySet = MySqlUtils.class_6_connection.getMethod("getPropertySet", (Class<?>[])new Class[0]);
                        MySqlUtils.method_6_getBooleanReadableProperty = Class.forName("com.mysql.cj.api.conf.PropertySet").getMethod("getBooleanReadableProperty", String.class);
                        MySqlUtils.method_6_getValue = Class.forName("com.mysql.cj.api.conf.ReadableProperty").getMethod("getValue", (Class<?>[])new Class[0]);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                    MySqlUtils.method_6_getValue_error = true;
                }
            }
            try {
                final Boolean pinGlobTx2 = (Boolean)MySqlUtils.method_6_getValue.invoke(MySqlUtils.method_6_getBooleanReadableProperty.invoke(MySqlUtils.method_6_getPropertySet.invoke(physicalConn, new Object[0]), "pinGlobalTxToPhysicalConnection"), new Object[0]);
                if (pinGlobTx2 != null && pinGlobTx2) {
                    try {
                        if (MySqlUtils.method_6_getInstance == null && !MySqlUtils.method_6_getInstance_error) {
                            MySqlUtils.class_6_suspendableXAConnection = Class.forName("com.mysql.cj.jdbc.SuspendableXAConnection");
                            (MySqlUtils.method_6_getInstance = MySqlUtils.class_6_suspendableXAConnection.getDeclaredMethod("getInstance", MySqlUtils.class_6_connection)).setAccessible(true);
                        }
                    }
                    catch (Throwable ex2) {
                        ex2.printStackTrace();
                        MySqlUtils.method_6_getInstance_error = true;
                    }
                    return (XAConnection)MySqlUtils.method_6_getInstance.invoke(null, physicalConn);
                }
                try {
                    if (MySqlUtils.method_6_getInstanceXA == null && !MySqlUtils.method_6_getInstanceXA_error) {
                        MySqlUtils.class_6_JDBC4SuspendableXAConnection = Class.forName("com.mysql.cj.jdbc.MysqlXAConnection");
                        (MySqlUtils.method_6_getInstanceXA = MySqlUtils.class_6_JDBC4SuspendableXAConnection.getDeclaredMethod("getInstance", MySqlUtils.class_6_connection, Boolean.TYPE)).setAccessible(true);
                    }
                }
                catch (Throwable ex2) {
                    ex2.printStackTrace();
                    MySqlUtils.method_6_getInstanceXA_error = true;
                }
                return (XAConnection)MySqlUtils.method_6_getInstanceXA.invoke(null, physicalConn, Boolean.FALSE);
            }
            catch (InvocationTargetException e2) {
                final Throwable cause = e2.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException)cause;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                MySqlUtils.method_6_getInstance_error = true;
            }
        }
        throw new SQLFeatureNotSupportedException();
    }
    
    public static String buildKillQuerySql(final Connection connection, final SQLException error) throws SQLException {
        final Long threadId = getId(connection);
        if (threadId == null) {
            return null;
        }
        return "KILL QUERY " + threadId;
    }
    
    public static boolean isKeyword(final String name) {
        if (name == null) {
            return false;
        }
        final String name_lower = name.toLowerCase();
        Set<String> words = MySqlUtils.keywords;
        if (words == null) {
            words = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/mysql/keywords", words);
            MySqlUtils.keywords = words;
        }
        return words.contains(name_lower);
    }
    
    public static boolean isBuiltinDataType(final String dataType) {
        if (dataType == null) {
            return false;
        }
        final String table_lower = dataType.toLowerCase();
        Set<String> dataTypes = MySqlUtils.builtinDataTypes;
        if (dataTypes == null) {
            dataTypes = new HashSet<String>();
            Utils.loadFromFile("META-INF/druid/parser/mysql/builtin_datatypes", dataTypes);
            MySqlUtils.builtinDataTypes = dataTypes;
        }
        return dataTypes.contains(table_lower);
    }
    
    public static List<String> showTables(final Connection conn) throws SQLException {
        final List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("show tables");
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
    
    public static List<String> getTableDDL(final Connection conn, final List<String> tables) throws SQLException {
        final List<String> ddlList = new ArrayList<String>();
        Statement stmt = null;
        try {
            for (String table : tables) {
                if (stmt == null) {
                    stmt = conn.createStatement();
                }
                if (isKeyword(table)) {
                    table = "`" + table + "`";
                }
                ResultSet rs = null;
                try {
                    rs = stmt.executeQuery("show create table " + table);
                    if (!rs.next()) {
                        continue;
                    }
                    final String ddl = rs.getString(2);
                    ddlList.add(ddl);
                }
                finally {
                    JdbcUtils.close(rs);
                }
            }
        }
        finally {
            JdbcUtils.close(stmt);
        }
        return ddlList;
    }
    
    public static String getCreateTableScript(final Connection conn) throws SQLException {
        return getCreateTableScript(conn, true, true);
    }
    
    public static String getCreateTableScript(final Connection conn, final boolean sorted, final boolean simplify) throws SQLException {
        final List<String> tables = showTables(conn);
        final List<String> ddlList = getTableDDL(conn, tables);
        final StringBuilder buf = new StringBuilder();
        for (final String ddl : ddlList) {
            buf.append(ddl);
            buf.append(';');
        }
        final String ddlScript = buf.toString();
        if (!sorted && !simplify) {
            return ddlScript;
        }
        final List stmtList = SQLUtils.parseStatements(ddlScript, DbType.mysql);
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
        return SQLUtils.toSQLString(stmtList, DbType.mysql);
    }
    
    public static Long getId(final Object conn) {
        if (conn == null) {
            return null;
        }
        final Class<?> clazz = conn.getClass();
        if (MySqlUtils.class_ConnectionImpl == null) {
            if (clazz.getName().equals("com.mysql.jdbc.ConnectionImpl")) {
                MySqlUtils.class_ConnectionImpl = clazz;
            }
            else if (clazz.getName().equals("com.mysql.jdbc.Connection")) {
                MySqlUtils.class_ConnectionImpl = clazz;
            }
            else if (clazz.getName().equals("com.mysql.cj.jdbc.ConnectionImpl")) {
                MySqlUtils.class_ConnectionImpl = clazz;
            }
            else if (clazz.getSuperclass().getName().equals("com.mysql.jdbc.ConnectionImpl")) {
                MySqlUtils.class_ConnectionImpl = clazz.getSuperclass();
            }
        }
        if (MySqlUtils.class_ConnectionImpl != clazz) {
            if (MySqlUtils.class_ConnectionImpl != clazz.getSuperclass()) {
                return null;
            }
        }
        try {
            if (MySqlUtils.method_getId == null && !MySqlUtils.method_getId_error) {
                final Method method = MySqlUtils.class_ConnectionImpl.getDeclaredMethod("getId", (Class<?>[])new Class[0]);
                method.setAccessible(true);
                MySqlUtils.method_getId = method;
            }
            return (Long)MySqlUtils.method_getId.invoke(conn, new Object[0]);
        }
        catch (Throwable ex) {
            MySqlUtils.method_getId_error = true;
        }
        return null;
    }
    
    public static long getLastPacketReceivedTimeMs(final Connection conn) throws SQLException {
        if (MySqlUtils.class_connectionImpl == null && !MySqlUtils.class_connectionImpl_Error) {
            try {
                MySqlUtils.class_connectionImpl = Utils.loadClass("com.mysql.jdbc.MySQLConnection");
                if (MySqlUtils.class_connectionImpl == null) {
                    MySqlUtils.class_connectionImpl = Utils.loadClass("com.mysql.cj.MysqlConnection");
                    if (MySqlUtils.class_connectionImpl != null) {
                        MySqlUtils.mysqlJdbcVersion6 = true;
                    }
                }
            }
            catch (Throwable error) {
                MySqlUtils.class_connectionImpl_Error = true;
            }
        }
        if (MySqlUtils.class_connectionImpl == null) {
            return -1L;
        }
        if (MySqlUtils.mysqlJdbcVersion6) {
            if (MySqlUtils.classJdbc == null) {
                MySqlUtils.classJdbc = Utils.loadClass("com.mysql.cj.jdbc.JdbcConnection");
            }
            if (MySqlUtils.classJdbc == null) {
                return -1L;
            }
            if (MySqlUtils.getIdleFor == null && !MySqlUtils.getIdleForError) {
                try {
                    (MySqlUtils.getIdleFor = MySqlUtils.classJdbc.getMethod("getIdleFor", (Class[])new Class[0])).setAccessible(true);
                }
                catch (Throwable error) {
                    MySqlUtils.getIdleForError = true;
                }
            }
            if (MySqlUtils.getIdleFor == null) {
                return -1L;
            }
            try {
                final Object connImpl = conn.unwrap((Class<Object>)MySqlUtils.class_connectionImpl);
                if (connImpl == null) {
                    return -1L;
                }
                return System.currentTimeMillis() - (long)MySqlUtils.getIdleFor.invoke(connImpl, new Object[0]);
            }
            catch (Exception e) {
                throw new SQLException("getIdleFor error", e);
            }
        }
        if (MySqlUtils.method_getIO == null && !MySqlUtils.method_getIO_error) {
            try {
                MySqlUtils.method_getIO = MySqlUtils.class_connectionImpl.getMethod("getIO", (Class[])new Class[0]);
            }
            catch (Throwable error) {
                MySqlUtils.method_getIO_error = true;
            }
        }
        if (MySqlUtils.method_getIO == null) {
            return -1L;
        }
        if (MySqlUtils.class_MysqlIO == null && !MySqlUtils.class_MysqlIO_Error) {
            try {
                MySqlUtils.class_MysqlIO = Utils.loadClass("com.mysql.jdbc.MysqlIO");
            }
            catch (Throwable error) {
                MySqlUtils.class_MysqlIO_Error = true;
            }
        }
        if (MySqlUtils.class_MysqlIO == null) {
            return -1L;
        }
        if (MySqlUtils.method_getLastPacketReceivedTimeMs == null && !MySqlUtils.method_getLastPacketReceivedTimeMs_error) {
            try {
                final Method method = MySqlUtils.class_MysqlIO.getDeclaredMethod("getLastPacketReceivedTimeMs", (Class[])new Class[0]);
                method.setAccessible(true);
                MySqlUtils.method_getLastPacketReceivedTimeMs = method;
            }
            catch (Throwable error) {
                MySqlUtils.method_getLastPacketReceivedTimeMs_error = true;
            }
        }
        if (MySqlUtils.method_getLastPacketReceivedTimeMs == null) {
            return -1L;
        }
        try {
            final Object connImpl = conn.unwrap((Class<Object>)MySqlUtils.class_connectionImpl);
            if (connImpl == null) {
                return -1L;
            }
            final Object mysqlio = MySqlUtils.method_getIO.invoke(connImpl, new Object[0]);
            return (long)MySqlUtils.method_getLastPacketReceivedTimeMs.invoke(mysqlio, new Object[0]);
        }
        catch (Exception e) {
            throw new SQLException("getLastPacketReceivedTimeMs error", e);
        }
    }
    
    public static Class getCommunicationsExceptionClass() {
        if (MySqlUtils.class_5_CommunicationsException != null) {
            return MySqlUtils.class_5_CommunicationsException;
        }
        if (MySqlUtils.class_6_CommunicationsException != null) {
            return MySqlUtils.class_6_CommunicationsException;
        }
        MySqlUtils.class_5_CommunicationsException = Utils.loadClass("com.mysql.jdbc.CommunicationsException");
        if (MySqlUtils.class_5_CommunicationsException != null) {
            return MySqlUtils.class_5_CommunicationsException;
        }
        MySqlUtils.class_6_CommunicationsException = Utils.loadClass("com.mysql.cj.jdbc.exceptions.CommunicationsException");
        if (MySqlUtils.class_6_CommunicationsException != null) {
            return MySqlUtils.class_6_CommunicationsException;
        }
        return null;
    }
    
    public static void loadDataTypes(final Set<String> dataTypes) {
        Utils.loadFromFile("META-INF/druid/parser/mysql/builtin_datatypes", dataTypes);
    }
    
    public static BigDecimal decimal(BigDecimal value, final int precision, final int scale) {
        final int v_scale = value.scale();
        int v_precision;
        if (v_scale > scale) {
            value = value.setScale(scale, 4);
            v_precision = value.precision();
        }
        else {
            v_precision = value.precision();
        }
        final int v_ints = v_precision - v_scale;
        final int ints = precision - scale;
        if (v_precision <= precision && v_ints <= ints) {
            return value;
        }
        final boolean sign = value.signum() > 0;
        if (scale == 1) {
            return sign ? MySqlUtils.MAX_DEC_1[ints] : MySqlUtils.MIN_DEC_1[ints];
        }
        if (scale == 2) {
            return sign ? MySqlUtils.MAX_DEC_2[ints] : MySqlUtils.MIN_DEC_2[ints];
        }
        return new BigDecimal(sign ? MySqlUtils.MAX_INT[precision - 1] : MySqlUtils.MIN_INT[precision - 1], scale);
    }
    
    public static boolean isNumber(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        final char c0 = str.charAt(0);
        boolean dot = false;
        boolean expr = false;
        int i = 0;
        if (c0 == '+' || c0 == '-') {
            ++i;
        }
        while (i < str.length()) {
            final char ch = str.charAt(i);
            if (ch == '.') {
                if (dot || expr) {
                    return false;
                }
                dot = true;
            }
            else if (ch == 'e' || ch == 'E') {
                if (expr) {
                    return false;
                }
                expr = true;
                if (i >= str.length() - 1) {
                    return false;
                }
                final char next = str.charAt(i + 1);
                if (next == '+' || next == '-') {
                    ++i;
                }
            }
            else if (ch < '0' || ch > '9') {
                return false;
            }
            ++i;
        }
        return true;
    }
    
    public static DateFormat toJavaFormat(final String fmt, final TimeZone timeZone) {
        final DateFormat dateFormat = toJavaFormat(fmt);
        if (dateFormat == null) {
            return null;
        }
        if (timeZone != null) {
            dateFormat.setTimeZone(timeZone);
        }
        return dateFormat;
    }
    
    public static DateFormat toJavaFormat(final String fmt) {
        if (fmt == null) {
            return null;
        }
        final StringBuffer buf = new StringBuffer();
        for (int i = 0, len = fmt.length(); i < len; ++i) {
            final char ch = fmt.charAt(i);
            if (ch == '%') {
                if (i + 1 == len) {
                    return null;
                }
                final char next_ch = fmt.charAt(++i);
                switch (next_ch) {
                    case 'a': {
                        buf.append("EEE");
                        break;
                    }
                    case 'b': {
                        buf.append("MMM");
                        break;
                    }
                    case 'c': {
                        buf.append("M");
                        break;
                    }
                    case 'd': {
                        buf.append("dd");
                        break;
                    }
                    case 'e': {
                        buf.append("d");
                        break;
                    }
                    case 'f': {
                        buf.append("SSS000");
                        break;
                    }
                    case 'H':
                    case 'k': {
                        buf.append("HH");
                        break;
                    }
                    case 'I':
                    case 'h':
                    case 'l': {
                        buf.append("hh");
                        break;
                    }
                    case 'i': {
                        buf.append("mm");
                        break;
                    }
                    case 'M': {
                        buf.append("MMMMM");
                        break;
                    }
                    case 'm': {
                        buf.append("MM");
                        break;
                    }
                    case 'p': {
                        buf.append('a');
                        break;
                    }
                    case 'r': {
                        buf.append("hh:mm:ss a");
                        break;
                    }
                    case 'S':
                    case 's': {
                        buf.append("ss");
                        break;
                    }
                    case 'T': {
                        buf.append("HH:mm:ss");
                        break;
                    }
                    case 'W': {
                        buf.append("EEEEE");
                        break;
                    }
                    case 'w': {
                        buf.append("u");
                        break;
                    }
                    case 'Y': {
                        buf.append("yyyy");
                        break;
                    }
                    case 'y': {
                        buf.append("yy");
                        break;
                    }
                    default: {
                        return null;
                    }
                }
            }
            else {
                buf.append(ch);
            }
        }
        try {
            return new SimpleDateFormat(buf.toString(), Locale.ENGLISH);
        }
        catch (IllegalArgumentException ex) {
            return null;
        }
    }
    
    public static Date parseDate(final String str, final TimeZone timeZone) {
        if (str == null) {
            return null;
        }
        final int length = str.length();
        if (length < 8) {
            return null;
        }
        ZoneId zoneId = (timeZone == null) ? ZoneId.systemDefault() : timeZone.toZoneId();
        char y0 = str.charAt(0);
        char y2 = str.charAt(1);
        char y3 = str.charAt(2);
        char y4 = str.charAt(3);
        char M0 = '\0';
        char M2 = '\0';
        char d0 = '\0';
        char d2 = '\0';
        char h0 = '\0';
        char h2 = '\0';
        char m0 = '\0';
        char m2 = '\0';
        char s0 = '\0';
        char s2 = '\0';
        char S0 = '0';
        char S2 = '0';
        char S3 = '0';
        final char c4 = str.charAt(4);
        final char c5 = str.charAt(5);
        final char c6 = str.charAt(6);
        final char c7 = str.charAt(7);
        int nanos = 0;
        switch (length) {
            case 8: {
                if (c4 == '-' && c6 == '-') {
                    M0 = '0';
                    M2 = c5;
                    d0 = '0';
                    d2 = c7;
                    break;
                }
                if (y3 == ':' && c5 == ':') {
                    h0 = y0;
                    h2 = y2;
                    m0 = y4;
                    m2 = c4;
                    s0 = c6;
                    s2 = c7;
                    y0 = '1';
                    y2 = '9';
                    y3 = '7';
                    y4 = '0';
                    M0 = '0';
                    M2 = '1';
                    d0 = '0';
                    d2 = '1';
                    break;
                }
                M0 = c4;
                M2 = c5;
                d0 = c6;
                d2 = c7;
                break;
            }
            case 9: {
                final char c8 = str.charAt(8);
                if (c4 != '-') {
                    return null;
                }
                if (c6 == '-') {
                    M0 = '0';
                    M2 = c5;
                    d0 = c7;
                    d2 = c8;
                    break;
                }
                if (c7 == '-') {
                    M0 = c5;
                    M2 = c6;
                    d0 = '0';
                    d2 = c8;
                    break;
                }
                return null;
            }
            case 10: {
                final char c8 = str.charAt(8);
                final char c9 = str.charAt(9);
                if (c4 != '-' || c7 != '-') {
                    return null;
                }
                M0 = c5;
                M2 = c6;
                d0 = c8;
                d2 = c9;
                break;
            }
            case 14: {
                final char c8 = str.charAt(8);
                final char c9 = str.charAt(9);
                final char c10 = str.charAt(10);
                final char c11 = str.charAt(11);
                final char c12 = str.charAt(12);
                final char c13 = str.charAt(13);
                if (c8 != ' ') {
                    M0 = c4;
                    M2 = c5;
                    d0 = c6;
                    d2 = c7;
                    h0 = c8;
                    h2 = c9;
                    m0 = c10;
                    m2 = c11;
                    s0 = c12;
                    s2 = c13;
                    break;
                }
                if (c4 == '-' && (c6 == '-' & c10 == ':') && c12 == ':') {
                    M0 = '0';
                    M2 = c5;
                    d0 = '0';
                    d2 = c7;
                    h0 = '0';
                    h2 = c9;
                    m0 = '0';
                    m2 = c11;
                    s0 = '0';
                    s2 = c13;
                    break;
                }
                return null;
            }
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 29: {
                if (length == 19 || length == 23 || length == 29) {
                    final char c8 = str.charAt(8);
                    final char c9 = str.charAt(9);
                    final char c10 = str.charAt(10);
                    final char c11 = str.charAt(11);
                    final char c12 = str.charAt(12);
                    final char c13 = str.charAt(13);
                    final char c14 = str.charAt(14);
                    final char c15 = str.charAt(15);
                    final char c16 = str.charAt(16);
                    final char c17 = str.charAt(17);
                    final char c18 = str.charAt(18);
                    if (c4 == '-' && c7 == '-' && (c10 == ' ' || c10 == 'T') && ((c13 == ':' && c16 == ':') || (c13 == '.' && c16 == '.'))) {
                        M0 = c5;
                        M2 = c6;
                        d0 = c8;
                        d2 = c9;
                        h0 = c11;
                        h2 = c12;
                        m0 = c14;
                        m2 = c15;
                        s0 = c17;
                        s2 = c18;
                        if (length == 23) {
                            final char c19 = str.charAt(19);
                            final char c20 = str.charAt(20);
                            final char c21 = str.charAt(21);
                            final char c22 = str.charAt(22);
                            if (c19 == '.') {
                                S0 = c20;
                                S2 = c21;
                                S3 = c22;
                            }
                            else {
                                if (c19 != ' ' || c20 != 'U' || c21 != 'T' || c22 != 'C') {
                                    return null;
                                }
                                zoneId = ZoneOffset.UTC;
                            }
                            break;
                        }
                        if (length != 29) {
                            break;
                        }
                        final char c19 = str.charAt(19);
                        final char c20 = str.charAt(20);
                        final char c21 = str.charAt(21);
                        final char c22 = str.charAt(22);
                        if (c19 != '.') {
                            return null;
                        }
                        S0 = c20;
                        S2 = c21;
                        S3 = c22;
                        final char c23 = str.charAt(23);
                        final char c24 = str.charAt(24);
                        final char c25 = str.charAt(25);
                        final char c26 = str.charAt(26);
                        final char c27 = str.charAt(27);
                        final char c28 = str.charAt(28);
                        if (c23 < '0' || c23 > '9' || c24 < '0' || c24 > '9' || c25 < '0' || c25 > '9' || c26 < '0' || c26 > '9' || c27 < '0' || c27 > '9' || c28 < '0' || c28 > '9') {
                            return null;
                        }
                        nanos = (c23 - '0') * 100000 + (c24 - '0') * 10000 + (c25 - '0') * 1000 + (c26 - '0') * 100 + (c27 - '0') * 10 + (c28 - '0');
                        break;
                    }
                }
                if (c4 != '-') {
                    return null;
                }
                int offset;
                if (c6 == '-') {
                    M0 = '0';
                    M2 = c5;
                    offset = 7;
                }
                else {
                    if (c7 != '-') {
                        return null;
                    }
                    M0 = c5;
                    M2 = c6;
                    offset = 8;
                }
                char n0 = str.charAt(offset);
                char n2;
                if ((n2 = str.charAt(offset + 1)) == ' ' || n2 == 'T') {
                    d0 = '0';
                    d2 = n0;
                    offset += 2;
                }
                else {
                    final char n3;
                    if ((n3 = str.charAt(offset + 2)) != ' ' && n3 != 'T') {
                        return null;
                    }
                    d0 = n0;
                    d2 = n2;
                    offset += 3;
                }
                n0 = str.charAt(offset);
                if ((n2 = str.charAt(offset + 1)) == ':') {
                    h0 = '0';
                    h2 = n0;
                    offset += 2;
                }
                else {
                    final char n3;
                    if ((n3 = str.charAt(offset + 2)) != ':') {
                        return null;
                    }
                    h0 = n0;
                    h2 = n2;
                    offset += 3;
                }
                n0 = str.charAt(offset);
                if ((n2 = str.charAt(offset + 1)) == ':') {
                    m0 = '0';
                    m2 = n0;
                    offset += 2;
                }
                else {
                    final char n3;
                    if (offset + 2 >= length || (n3 = str.charAt(offset + 2)) != ':') {
                        return null;
                    }
                    m0 = n0;
                    m2 = n2;
                    offset += 3;
                }
                if (offset == length - 1) {
                    s0 = '0';
                    s2 = str.charAt(offset);
                    break;
                }
                if (offset == length - 2) {
                    n0 = str.charAt(offset);
                    n2 = str.charAt(offset + 1);
                    if (n2 == '.') {
                        s0 = '0';
                        s2 = n0;
                    }
                    else {
                        s0 = n0;
                        s2 = n2;
                    }
                    break;
                }
                final char x0 = str.charAt(length - 1);
                final char x2 = str.charAt(length - 2);
                final char x3 = str.charAt(length - 3);
                final char x4 = str.charAt(length - 4);
                int lastOff;
                if (x0 == '.') {
                    lastOff = length - 2;
                }
                else if (x2 == '.') {
                    S3 = x0;
                    lastOff = length - 3;
                }
                else if (x3 == '.') {
                    S2 = x2;
                    S3 = x0;
                    lastOff = length - 4;
                }
                else if (x4 == '.') {
                    S0 = x3;
                    S2 = x2;
                    S3 = x0;
                    lastOff = length - 5;
                }
                else {
                    if ((x3 != '+' && x3 != '-') || length != offset + 5) {
                        return null;
                    }
                    final String zoneIdStr = new String(new char[] { x3, x2, x0 });
                    zoneId = ZoneId.of(zoneIdStr);
                    lastOff = length - 4;
                }
                final char k0 = str.charAt(lastOff);
                final char k2 = str.charAt(lastOff - 1);
                final char k3 = str.charAt(lastOff - 2);
                if (k2 == ':') {
                    s0 = '0';
                    s2 = k0;
                }
                else {
                    if (k3 != ':') {
                        return null;
                    }
                    s2 = k0;
                    s0 = k2;
                }
                break;
            }
            default: {
                return null;
            }
        }
        if (y0 < '0' || y0 > '9' || y2 < '0' || y2 > '9' || y3 < '0' || y3 > '9' || y4 < '0' || y4 > '9') {
            return null;
        }
        final int year = (y0 - '0') * 1000 + (y2 - '0') * 100 + (y3 - '0') * 10 + (y4 - '0');
        if (year < 1970) {
            return null;
        }
        if (M0 < '0' || M0 > '1') {
            return null;
        }
        if (M2 < '0' || M2 > '9') {
            return null;
        }
        final int month = (M0 - '0') * 10 + (M2 - '0');
        if (month < 1 || month > 12) {
            return null;
        }
        if (d0 < '0' || d0 > '9') {
            return null;
        }
        if (d2 < '0' || d2 > '9') {
            return null;
        }
        final int dayOfMonth = (d0 - '0') * 10 + (d2 - '0');
        if (dayOfMonth < 1) {
            return null;
        }
        int maxDayOfMonth = 0;
        switch (month) {
            case 2: {
                maxDayOfMonth = 29;
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                maxDayOfMonth = 30;
                break;
            }
            default: {
                maxDayOfMonth = 31;
                break;
            }
        }
        if (dayOfMonth > maxDayOfMonth) {
            return null;
        }
        ZonedDateTime zdt = null;
        if (h0 == '\0') {
            zdt = LocalDate.of(year, month, dayOfMonth).atStartOfDay(zoneId);
        }
        else {
            final int hour = (h0 - '0') * 10 + (h2 - '0');
            final int minute = (m0 - '0') * 10 + (m2 - '0');
            final int second = (s0 - '0') * 10 + (s2 - '0');
            final int nanoSecond = ((S0 - '0') * 100 + (S2 - '0') * 10 + (S3 - '0')) * 1000000 + nanos;
            if (hour >= 24 || minute > 60 || second > 61) {
                return null;
            }
            zdt = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoSecond).atZone(zoneId);
        }
        return Date.from(zdt.toInstant());
    }
    
    public static long parseMillis(final byte[] str, final TimeZone timeZone) {
        if (str == null) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        return parseMillis(str, 0, str.length, timeZone);
    }
    
    public static long parseMillis(final byte[] str, final int off, final int len, final TimeZone timeZone) {
        final ZoneId zoneId = (timeZone == null) ? ZoneId.systemDefault() : timeZone.toZoneId();
        return parseDateTime(str, off, len, zoneId).toInstant().toEpochMilli();
    }
    
    public static ZonedDateTime parseDateTime(final byte[] str, final int off, final int len, ZoneId zoneId) {
        if (str == null) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        if (len < 8) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        byte y0 = str[off];
        byte y2 = str[off + 1];
        byte y3 = str[off + 2];
        byte y4 = str[off + 3];
        byte M0 = 0;
        byte M2 = 0;
        byte d0 = 0;
        byte d2 = 0;
        byte h0 = 0;
        byte h2 = 0;
        byte m0 = 0;
        byte m2 = 0;
        byte s0 = 0;
        byte s2 = 0;
        byte S0 = 48;
        byte S2 = 48;
        byte S3 = 48;
        final byte c4 = str[off + 4];
        final byte c5 = str[off + 5];
        final byte c6 = str[off + 6];
        final byte c7 = str[off + 7];
        int nanos = 0;
        switch (len) {
            case 8: {
                if (c4 == 45 && c6 == 45) {
                    M0 = 48;
                    M2 = c5;
                    d0 = 48;
                    d2 = c7;
                    break;
                }
                if (y3 == 58 && c5 == 58) {
                    h0 = y0;
                    h2 = y2;
                    m0 = y4;
                    m2 = c4;
                    s0 = c6;
                    s2 = c7;
                    y0 = 49;
                    y2 = 57;
                    y3 = 55;
                    y4 = 48;
                    M0 = 48;
                    M2 = 49;
                    d0 = 48;
                    d2 = 49;
                    break;
                }
                M0 = c4;
                M2 = c5;
                d0 = c6;
                d2 = c7;
                break;
            }
            case 9: {
                final byte c8 = str[off + 8];
                if (c4 != 45) {
                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                }
                if (c6 == 45) {
                    M0 = 48;
                    M2 = c5;
                    d0 = c7;
                    d2 = c8;
                    break;
                }
                if (c7 == 45) {
                    M0 = c5;
                    M2 = c6;
                    d0 = 48;
                    d2 = c8;
                    break;
                }
                throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
            }
            case 10: {
                final byte c8 = str[off + 8];
                final byte c9 = str[off + 9];
                if (c4 != 45 || c7 != 45) {
                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                }
                M0 = c5;
                M2 = c6;
                d0 = c8;
                d2 = c9;
                break;
            }
            case 14: {
                final byte c8 = str[off + 8];
                final byte c9 = str[off + 9];
                final byte c10 = str[off + 10];
                final byte c11 = str[off + 11];
                final byte c12 = str[off + 12];
                final byte c13 = str[off + 13];
                if (c8 != 32) {
                    M0 = c4;
                    M2 = c5;
                    d0 = c6;
                    d2 = c7;
                    h0 = c8;
                    h2 = c9;
                    m0 = c10;
                    m2 = c11;
                    s0 = c12;
                    s2 = c13;
                    break;
                }
                if (c4 == 45 && (c6 == 45 & c10 == 58) && c12 == 58) {
                    M0 = 48;
                    M2 = c5;
                    d0 = 48;
                    d2 = c7;
                    h0 = 48;
                    h2 = c9;
                    m0 = 48;
                    m2 = c11;
                    s0 = 48;
                    s2 = c13;
                    break;
                }
                throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
            }
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 26:
            case 27:
            case 28:
            case 29: {
                if (len == 19 || len >= 23) {
                    final byte c8 = str[off + 8];
                    final byte c9 = str[off + 9];
                    final byte c10 = str[off + 10];
                    final byte c11 = str[off + 11];
                    final byte c12 = str[off + 12];
                    final byte c13 = str[off + 13];
                    final byte c14 = str[off + 14];
                    final byte c15 = str[off + 15];
                    final byte c16 = str[off + 16];
                    final byte c17 = str[off + 17];
                    final byte c18 = str[off + 18];
                    if (c4 == 45 && c7 == 45 && (c10 == 32 || c10 == 84) && c13 == 58 && c16 == 58) {
                        M0 = c5;
                        M2 = c6;
                        d0 = c8;
                        d2 = c9;
                        h0 = c11;
                        h2 = c12;
                        m0 = c14;
                        m2 = c15;
                        s0 = c17;
                        s2 = c18;
                        if (len == 19) {
                            break;
                        }
                        final byte c19 = str[off + 19];
                        final byte c20 = str[off + 20];
                        final byte c21 = str[off + 21];
                        final byte c22 = str[off + 22];
                        if (len == 23) {
                            if (c19 == 46) {
                                S0 = c20;
                                S2 = c21;
                                S3 = c22;
                                break;
                            }
                            if (c19 == 32 && c20 == 85 && c21 == 84 && c22 == 67) {
                                zoneId = ZoneOffset.UTC;
                                break;
                            }
                            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                        }
                        else {
                            if (c19 != 46) {
                                throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                            }
                            S0 = c20;
                            S2 = c21;
                            S3 = c22;
                            if (len == 29) {
                                final byte c23 = str[off + 23];
                                final byte c24 = str[off + 24];
                                final byte c25 = str[off + 25];
                                final byte c26 = str[off + 26];
                                final byte c27 = str[off + 27];
                                final byte c28 = str[off + 28];
                                if (c23 < 48 || c23 > 57 || c24 < 48 || c24 > 57 || c25 < 48 || c25 > 57 || c26 < 48 || c26 > 57 || c27 < 48 || c27 > 57 || c28 < 48 || c28 > 57) {
                                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                                }
                                nanos = (c23 - 48) * 100000 + (c24 - 48) * 10000 + (c25 - 48) * 1000 + (c26 - 48) * 100 + (c27 - 48) * 10 + (c28 - 48);
                                break;
                            }
                            else if (len == 28) {
                                final byte c23 = str[off + 23];
                                final byte c24 = str[off + 24];
                                final byte c25 = str[off + 25];
                                final byte c26 = str[off + 26];
                                final byte c27 = str[off + 27];
                                if (c23 < 48 || c23 > 57 || c24 < 48 || c24 > 57 || c25 < 48 || c25 > 57 || c26 < 48 || c26 > 57 || c27 < 48 || c27 > 57) {
                                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                                }
                                nanos = (c23 - 48) * 100000 + (c24 - 48) * 10000 + (c25 - 48) * 1000 + (c26 - 48) * 100 + (c27 - 48) * 10;
                                break;
                            }
                            else if (len == 27) {
                                final byte c23 = str[off + 23];
                                final byte c24 = str[off + 24];
                                final byte c25 = str[off + 25];
                                final byte c26 = str[off + 26];
                                if (c23 < 48 || c23 > 57 || c24 < 48 || c24 > 57 || c25 < 48 || c25 > 57 || c26 < 48 || c26 > 57) {
                                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                                }
                                nanos = (c23 - 48) * 100000 + (c24 - 48) * 10000 + (c25 - 48) * 1000 + (c26 - 48) * 100;
                                break;
                            }
                            else {
                                if (len != 26) {
                                    break;
                                }
                                final byte c23 = str[off + 23];
                                final byte c24 = str[off + 24];
                                final byte c25 = str[off + 25];
                                if (c23 < 48 || c23 > 57 || c24 < 48 || c24 > 57 || c25 < 48 || c25 > 57) {
                                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                                }
                                nanos = (c23 - 48) * 100000 + (c24 - 48) * 10000 + (c25 - 48) * 1000;
                                break;
                            }
                        }
                    }
                }
                if (c4 != 45) {
                    throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                }
                int off2;
                if (c6 == 45) {
                    M0 = 48;
                    M2 = c5;
                    off2 = off + 7;
                }
                else {
                    if (c7 != 45) {
                        throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                    }
                    M0 = c5;
                    M2 = c6;
                    off2 = off + 8;
                }
                byte n0 = str[off2];
                byte n2;
                if ((n2 = str[off2 + 1]) == 32 || n2 == 84) {
                    d0 = 48;
                    d2 = n0;
                    off2 += 2;
                }
                else {
                    final byte n3;
                    if ((n3 = str[off2 + 2]) != 32 && n3 != 84) {
                        throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                    }
                    d0 = n0;
                    d2 = n2;
                    off2 += 3;
                }
                n0 = str[off2];
                if ((n2 = str[off2 + 1]) == 58) {
                    h0 = 48;
                    h2 = n0;
                    off2 += 2;
                }
                else {
                    final byte n3;
                    if ((n3 = str[off2 + 2]) != 58) {
                        throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                    }
                    h0 = n0;
                    h2 = n2;
                    off2 += 3;
                }
                n0 = str[off2];
                if ((n2 = str[off2 + 1]) == 58) {
                    m0 = 48;
                    m2 = n0;
                    off2 += 2;
                }
                else {
                    final byte n3;
                    if (off2 + 2 >= off + len || (n3 = str[off2 + 2]) != 58) {
                        throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                    }
                    m0 = n0;
                    m2 = n2;
                    off2 += 3;
                }
                if (off2 == off + len - 1) {
                    s0 = 48;
                    s2 = str[off2];
                    break;
                }
                if (off2 == off + len - 2) {
                    n0 = str[off2];
                    n2 = str[off2 + 1];
                    if (n2 == 46) {
                        s0 = 48;
                        s2 = n0;
                    }
                    else {
                        s0 = n0;
                        s2 = n2;
                    }
                    break;
                }
                final byte x0 = str[off + len - 1];
                final byte x2 = str[off + len - 2];
                final byte x3 = str[off + len - 3];
                final byte x4 = str[off + len - 4];
                int lastOff;
                if (x0 == 46) {
                    lastOff = off + len - 2;
                }
                else if (x2 == 46) {
                    S0 = x0;
                    lastOff = off + len - 3;
                }
                else if (x3 == 46) {
                    S0 = x2;
                    S2 = x0;
                    lastOff = off + len - 4;
                }
                else {
                    if (x4 != 46) {
                        throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                    }
                    S0 = x3;
                    S2 = x2;
                    S3 = x0;
                    lastOff = off + len - 5;
                }
                final byte k0 = str[lastOff];
                final byte k2 = str[lastOff - 1];
                final byte k3 = str[lastOff - 2];
                if (k2 == 58) {
                    s0 = 48;
                    s2 = k0;
                }
                else {
                    if (k3 != 58) {
                        throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
                    }
                    s2 = k0;
                    s0 = k2;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
            }
        }
        if (y0 < 48 || y0 > 57 || y2 < 48 || y2 > 57 || y3 < 48 || y3 > 57 || y4 < 48 || y4 > 57) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        final int year = (y0 - 48) * 1000 + (y2 - 48) * 100 + (y3 - 48) * 10 + (y4 - 48);
        if (M0 < 48 || M0 > 49) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        if (M2 < 48 || M2 > 57) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        final int month = (M0 - 48) * 10 + (M2 - 48);
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        if (d0 < 48 || d0 > 57) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        if (d2 < 48 || d2 > 57) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        final int dayOfMonth = (d0 - 48) * 10 + (d2 - 48);
        if (dayOfMonth < 1) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        int maxDayOfMonth = 0;
        switch (month) {
            case 2: {
                maxDayOfMonth = 29;
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                maxDayOfMonth = 30;
                break;
            }
            default: {
                maxDayOfMonth = 31;
                break;
            }
        }
        if (dayOfMonth > maxDayOfMonth) {
            throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
        }
        ZonedDateTime zdt;
        if (h0 == 0) {
            zdt = LocalDate.of(year, month, dayOfMonth).atStartOfDay(zoneId);
        }
        else {
            final int hour = (h0 - 48) * 10 + (h2 - 48);
            final int minute = (m0 - 48) * 10 + (m2 - 48);
            final int second = (s0 - 48) * 10 + (s2 - 48);
            final int nanoSecond = ((S0 - 48) * 100 + (S2 - 48) * 10 + (S3 - 48)) * 1000000 + nanos;
            if (hour > 24 || minute > 60 || second > 61) {
                throw new IllegalArgumentException(new String(str, MySqlUtils.UTF8));
            }
            zdt = LocalDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoSecond).atZone(zoneId);
        }
        return zdt;
    }
    
    public static boolean isSupportParseDateformat(final String str) {
        return str != null && Arrays.binarySearch(MySqlUtils.parseFormatCodes, FnvHash.fnv1a_64(str)) >= 0;
    }
    
    public static TimeZone parseTimeZone(final String str) {
        if ("SYSTEM".equalsIgnoreCase(str)) {
            return TimeZone.getDefault();
        }
        return TimeZone.getTimeZone(str);
    }
    
    public static String utf32(final String hex) {
        final byte[] bytes = HexBin.decode(hex);
        if (bytes.length == 2) {
            return new String(bytes, MySqlUtils.UTF16);
        }
        return new String(bytes, MySqlUtils.UTF32);
    }
    
    public static String utf16(String hex) {
        if (hex.length() % 2 == 1) {
            final char[] chars = new char[hex.length() + 1];
            chars[0] = '0';
            hex.getChars(0, hex.length(), chars, 1);
            hex = new String(chars);
        }
        final byte[] bytes = HexBin.decode(hex);
        if (bytes == null) {
            return null;
        }
        return new String(bytes, MySqlUtils.UTF16);
    }
    
    public static String utf8(final String hex) {
        final byte[] bytes = HexBin.decode(hex);
        return new String(bytes, MySqlUtils.UTF8);
    }
    
    public static String gbk(final String hex) {
        final byte[] bytes = HexBin.decode(hex);
        return new String(bytes, MySqlUtils.GBK);
    }
    
    public static String big5(final String hex) {
        final byte[] bytes = HexBin.decode(hex);
        return new String(bytes, MySqlUtils.BIG5);
    }
    
    static {
        MySqlUtils.utilClassError = false;
        MySqlUtils.utilClass_isJdbc4 = false;
        MySqlUtils.class_5_connection = null;
        MySqlUtils.method_5_getPinGlobalTxToPhysicalConnection = null;
        MySqlUtils.class_5_suspendableXAConnection = null;
        MySqlUtils.constructor_5_suspendableXAConnection = null;
        MySqlUtils.class_5_JDBC4SuspendableXAConnection = null;
        MySqlUtils.constructor_5_JDBC4SuspendableXAConnection = null;
        MySqlUtils.class_5_MysqlXAConnection = null;
        MySqlUtils.constructor_5_MysqlXAConnection = null;
        MySqlUtils.class_ConnectionImpl = null;
        MySqlUtils.method_getId = null;
        MySqlUtils.method_getId_error = false;
        MySqlUtils.class_6_ConnectionImpl = null;
        MySqlUtils.method_6_getId = null;
        MySqlUtils.class_6_connection = null;
        MySqlUtils.method_6_getPropertySet = null;
        MySqlUtils.method_6_getBooleanReadableProperty = null;
        MySqlUtils.method_6_getValue = null;
        MySqlUtils.method_6_getValue_error = false;
        MySqlUtils.class_6_suspendableXAConnection = null;
        MySqlUtils.method_6_getInstance = null;
        MySqlUtils.method_6_getInstance_error = false;
        MySqlUtils.method_6_getInstanceXA = null;
        MySqlUtils.method_6_getInstanceXA_error = false;
        MySqlUtils.class_6_JDBC4SuspendableXAConnection = null;
        MySqlUtils.class_connectionImpl = null;
        MySqlUtils.class_connectionImpl_Error = false;
        MySqlUtils.method_getIO = null;
        MySqlUtils.method_getIO_error = false;
        MySqlUtils.class_MysqlIO = null;
        MySqlUtils.class_MysqlIO_Error = false;
        MySqlUtils.method_getLastPacketReceivedTimeMs = null;
        MySqlUtils.method_getLastPacketReceivedTimeMs_error = false;
        MySqlUtils.mysqlJdbcVersion6 = false;
        MySqlUtils.classJdbc = null;
        MySqlUtils.getIdleFor = null;
        MySqlUtils.getIdleForError = false;
        MySqlUtils.class_5_CommunicationsException = null;
        MySqlUtils.class_6_CommunicationsException = null;
        GBK = Charset.forName("GBK");
        BIG5 = Charset.forName("BIG5");
        UTF8 = Charset.forName("UTF-8");
        UTF16 = Charset.forName("UTF-16");
        UTF32 = Charset.forName("UTF-32");
        ASCII = Charset.forName("ASCII");
        MySqlUtils.MAX_INT = new BigInteger[] { new BigInteger("9"), new BigInteger("99"), new BigInteger("999"), new BigInteger("9999"), new BigInteger("99999"), new BigInteger("999999"), new BigInteger("9999999"), new BigInteger("99999999"), new BigInteger("999999999"), new BigInteger("9999999999"), new BigInteger("99999999999"), new BigInteger("999999999999"), new BigInteger("9999999999999"), new BigInteger("99999999999999"), new BigInteger("999999999999999"), new BigInteger("9999999999999999"), new BigInteger("99999999999999999"), new BigInteger("999999999999999999"), new BigInteger("9999999999999999999"), new BigInteger("99999999999999999999"), new BigInteger("999999999999999999999"), new BigInteger("9999999999999999999999"), new BigInteger("99999999999999999999999"), new BigInteger("999999999999999999999999"), new BigInteger("9999999999999999999999999"), new BigInteger("99999999999999999999999999"), new BigInteger("999999999999999999999999999"), new BigInteger("9999999999999999999999999999"), new BigInteger("99999999999999999999999999999"), new BigInteger("999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999"), new BigInteger("99999999999999999999999999999999"), new BigInteger("999999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999"), new BigInteger("99999999999999999999999999999999999"), new BigInteger("999999999999999999999999999999999999"), new BigInteger("9999999999999999999999999999999999999"), new BigInteger("99999999999999999999999999999999999999") };
        MySqlUtils.MIN_INT = new BigInteger[] { new BigInteger("-9"), new BigInteger("-99"), new BigInteger("-999"), new BigInteger("-9999"), new BigInteger("-99999"), new BigInteger("-999999"), new BigInteger("-9999999"), new BigInteger("-99999999"), new BigInteger("-999999999"), new BigInteger("-9999999999"), new BigInteger("-99999999999"), new BigInteger("-999999999999"), new BigInteger("-9999999999999"), new BigInteger("-99999999999999"), new BigInteger("-999999999999999"), new BigInteger("-9999999999999999"), new BigInteger("-99999999999999999"), new BigInteger("-999999999999999999"), new BigInteger("-9999999999999999999"), new BigInteger("-99999999999999999999"), new BigInteger("-999999999999999999999"), new BigInteger("-9999999999999999999999"), new BigInteger("-99999999999999999999999"), new BigInteger("-999999999999999999999999"), new BigInteger("-9999999999999999999999999"), new BigInteger("-99999999999999999999999999"), new BigInteger("-999999999999999999999999999"), new BigInteger("-9999999999999999999999999999"), new BigInteger("-99999999999999999999999999999"), new BigInteger("-999999999999999999999999999999"), new BigInteger("-9999999999999999999999999999999"), new BigInteger("-99999999999999999999999999999999"), new BigInteger("-999999999999999999999999999999999"), new BigInteger("-9999999999999999999999999999999999"), new BigInteger("-99999999999999999999999999999999999"), new BigInteger("-999999999999999999999999999999999999"), new BigInteger("-9999999999999999999999999999999999999"), new BigInteger("-99999999999999999999999999999999999999") };
        MySqlUtils.MAX_DEC_1 = new BigDecimal[] { new BigDecimal("0.9"), new BigDecimal("9.9"), new BigDecimal("99.9"), new BigDecimal("999.9"), new BigDecimal("9999.9"), new BigDecimal("99999.9"), new BigDecimal("999999.9"), new BigDecimal("9999999.9"), new BigDecimal("99999999.9"), new BigDecimal("999999999.9"), new BigDecimal("9999999999.9"), new BigDecimal("99999999999.9"), new BigDecimal("999999999999.9"), new BigDecimal("9999999999999.9"), new BigDecimal("99999999999999.9"), new BigDecimal("999999999999999.9"), new BigDecimal("9999999999999999.9"), new BigDecimal("99999999999999999.9"), new BigDecimal("999999999999999999.9"), new BigDecimal("9999999999999999999.9"), new BigDecimal("99999999999999999999.9"), new BigDecimal("999999999999999999999.9"), new BigDecimal("9999999999999999999999.9"), new BigDecimal("99999999999999999999999.9"), new BigDecimal("999999999999999999999999.9"), new BigDecimal("9999999999999999999999999.9"), new BigDecimal("99999999999999999999999999.9"), new BigDecimal("999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999.9"), new BigDecimal("99999999999999999999999999999.9"), new BigDecimal("999999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999999.9"), new BigDecimal("99999999999999999999999999999999.9"), new BigDecimal("999999999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999999999.9"), new BigDecimal("99999999999999999999999999999999999.9"), new BigDecimal("999999999999999999999999999999999999.9"), new BigDecimal("9999999999999999999999999999999999999.9") };
        MySqlUtils.MIN_DEC_1 = new BigDecimal[] { new BigDecimal("-0.9"), new BigDecimal("-9.9"), new BigDecimal("-99.9"), new BigDecimal("-999.9"), new BigDecimal("-9999.9"), new BigDecimal("-99999.9"), new BigDecimal("-999999.9"), new BigDecimal("-9999999.9"), new BigDecimal("-99999999.9"), new BigDecimal("-999999999.9"), new BigDecimal("-9999999999.9"), new BigDecimal("-99999999999.9"), new BigDecimal("-999999999999.9"), new BigDecimal("-9999999999999.9"), new BigDecimal("-99999999999999.9"), new BigDecimal("-999999999999999.9"), new BigDecimal("-9999999999999999.9"), new BigDecimal("-99999999999999999.9"), new BigDecimal("-999999999999999999.9"), new BigDecimal("-9999999999999999999.9"), new BigDecimal("-99999999999999999999.9"), new BigDecimal("-999999999999999999999.9"), new BigDecimal("-9999999999999999999999.9"), new BigDecimal("-99999999999999999999999.9"), new BigDecimal("-999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999999999.9"), new BigDecimal("-99999999999999999999999999999999999.9"), new BigDecimal("-999999999999999999999999999999999999.9"), new BigDecimal("-9999999999999999999999999999999999999.9") };
        MySqlUtils.MAX_DEC_2 = new BigDecimal[] { new BigDecimal("0.99"), new BigDecimal("9.99"), new BigDecimal("99.99"), new BigDecimal("999.99"), new BigDecimal("9999.99"), new BigDecimal("99999.99"), new BigDecimal("999999.99"), new BigDecimal("9999999.99"), new BigDecimal("99999999.99"), new BigDecimal("999999999.99"), new BigDecimal("9999999999.99"), new BigDecimal("99999999999.99"), new BigDecimal("999999999999.99"), new BigDecimal("9999999999999.99"), new BigDecimal("99999999999999.99"), new BigDecimal("999999999999999.99"), new BigDecimal("9999999999999999.99"), new BigDecimal("99999999999999999.99"), new BigDecimal("999999999999999999.99"), new BigDecimal("9999999999999999999.99"), new BigDecimal("99999999999999999999.99"), new BigDecimal("999999999999999999999.99"), new BigDecimal("9999999999999999999999.99"), new BigDecimal("99999999999999999999999.99"), new BigDecimal("999999999999999999999999.99"), new BigDecimal("9999999999999999999999999.99"), new BigDecimal("99999999999999999999999999.99"), new BigDecimal("999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999.99"), new BigDecimal("99999999999999999999999999999.99"), new BigDecimal("999999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999999.99"), new BigDecimal("99999999999999999999999999999999.99"), new BigDecimal("999999999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999999999.99"), new BigDecimal("99999999999999999999999999999999999.99"), new BigDecimal("999999999999999999999999999999999999.99"), new BigDecimal("9999999999999999999999999999999999999.99") };
        MySqlUtils.MIN_DEC_2 = new BigDecimal[] { new BigDecimal("-0.99"), new BigDecimal("-9.99"), new BigDecimal("-99.99"), new BigDecimal("-999.99"), new BigDecimal("-9999.99"), new BigDecimal("-99999.99"), new BigDecimal("-999999.99"), new BigDecimal("-9999999.99"), new BigDecimal("-99999999.99"), new BigDecimal("-999999999.99"), new BigDecimal("-9999999999.99"), new BigDecimal("-99999999999.99"), new BigDecimal("-999999999999.99"), new BigDecimal("-9999999999999.99"), new BigDecimal("-99999999999999.99"), new BigDecimal("-999999999999999.99"), new BigDecimal("-9999999999999999.99"), new BigDecimal("-99999999999999999.99"), new BigDecimal("-999999999999999999.99"), new BigDecimal("-9999999999999999999.99"), new BigDecimal("-99999999999999999999.99"), new BigDecimal("-999999999999999999999.99"), new BigDecimal("-9999999999999999999999.99"), new BigDecimal("-99999999999999999999999.99"), new BigDecimal("-999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999999999.99"), new BigDecimal("-99999999999999999999999999999999999.99"), new BigDecimal("-999999999999999999999999999999999999.99"), new BigDecimal("-9999999999999999999999999999999999999.99") };
        parseFormats = new String[] { "HH:mm:ss", "yyyyMMdd", "yyyyMMddHHmmss", "yyyy-M-d", "yyyy-M-d H:m:s", "yyyy-M-d H:m:s.S", "yyyy-M-d'T'H:m:s", "yyyy-M-d'T'H:m:s.S", "yyyy-MM-d", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.SSS" };
        final long[] codes = new long[MySqlUtils.parseFormats.length];
        for (int i = 0; i < MySqlUtils.parseFormats.length; ++i) {
            codes[i] = FnvHash.fnv1a_64(MySqlUtils.parseFormats[i]);
        }
        Arrays.sort(codes);
        parseFormatCodes = codes;
    }
}
