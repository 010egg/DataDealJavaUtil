// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.monitor.dao;

import java.lang.reflect.Field;
import com.alibaba.druid.support.monitor.annotation.MTable;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.support.monitor.entity.MonitorInstance;
import com.alibaba.druid.support.monitor.entity.MonitorCluster;
import com.alibaba.druid.support.monitor.entity.MonitorApp;
import com.alibaba.druid.DruidRuntimeException;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.Connection;
import com.alibaba.druid.support.monitor.annotation.AggregateType;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.Timestamp;
import com.alibaba.druid.support.monitor.annotation.MField;
import java.util.ArrayList;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.alibaba.druid.util.StringUtils;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.support.monitor.MonitorContext;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.util.Utils;
import java.util.concurrent.ConcurrentHashMap;
import com.alibaba.druid.wall.WallFunctionStatValue;
import com.alibaba.druid.wall.WallTableStatValue;
import com.alibaba.druid.wall.WallSqlStatValue;
import com.alibaba.druid.wall.WallProviderStatValue;
import com.alibaba.druid.support.http.stat.WebAppStatValue;
import com.alibaba.druid.support.http.stat.WebURIStatValue;
import com.alibaba.druid.support.spring.stat.SpringMethodStatValue;
import com.alibaba.druid.stat.JdbcSqlStatValue;
import com.alibaba.druid.pool.DruidDataSourceStatValue;
import java.util.concurrent.ConcurrentMap;
import javax.sql.DataSource;
import com.alibaba.druid.support.logging.Log;

public class MonitorDaoJdbcImpl implements MonitorDao
{
    private static final Log LOG;
    private DataSource dataSource;
    private BeanInfo dataSourceStatBeanInfo;
    private BeanInfo sqlStatBeanInfo;
    private BeanInfo springMethodStatBeanInfo;
    private BeanInfo webURIStatBeanInfo;
    private BeanInfo webAppStatBeanInfo;
    private BeanInfo wallProviderStatBeanInfo;
    private BeanInfo wallSqlStatBeanInfo;
    private BeanInfo wallTableStatBeanInfo;
    private BeanInfo wallFunctionStatBeanInfo;
    private ConcurrentMap<String, ConcurrentMap<Long, String>> cacheMap;
    
    public MonitorDaoJdbcImpl() {
        this.dataSourceStatBeanInfo = new BeanInfo(DruidDataSourceStatValue.class);
        this.sqlStatBeanInfo = new BeanInfo(JdbcSqlStatValue.class);
        this.springMethodStatBeanInfo = new BeanInfo(SpringMethodStatValue.class);
        this.webURIStatBeanInfo = new BeanInfo(WebURIStatValue.class);
        this.webAppStatBeanInfo = new BeanInfo(WebAppStatValue.class);
        this.wallProviderStatBeanInfo = new BeanInfo(WallProviderStatValue.class);
        this.wallSqlStatBeanInfo = new BeanInfo(WallSqlStatValue.class);
        this.wallTableStatBeanInfo = new BeanInfo(WallTableStatValue.class);
        this.wallFunctionStatBeanInfo = new BeanInfo(WallFunctionStatValue.class);
        this.cacheMap = new ConcurrentHashMap<String, ConcurrentMap<Long, String>>();
    }
    
    public void createTables(final String dbType) {
        final String[] array;
        final String[] resources = array = new String[] { "basic.sql", "const.sql", "datasource.sql", "springmethod.sql", "sql.sql", "webapp.sql", "weburi.sql", "wall.sql" };
        for (final String item : array) {
            final String path = "/support/monitor/" + dbType + "/" + item;
            try {
                final String text = Utils.readFromResource(path);
                final String[] split;
                final String[] sqls = split = text.split(";");
                for (final String sql : split) {
                    JdbcUtils.execute(this.dataSource, sql, new Object[0]);
                }
            }
            catch (Exception ex) {
                MonitorDaoJdbcImpl.LOG.error("create table error", ex);
            }
        }
    }
    
    public DataSource getDataSource() {
        return this.dataSource;
    }
    
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }
    
    @Override
    public void saveSql(final MonitorContext ctx, final List<DruidDataSourceStatValue> dataSourceList) {
        this.save(this.dataSourceStatBeanInfo, ctx, dataSourceList);
        for (final DruidDataSourceStatValue dataSourceStatValue : dataSourceList) {
            final List<JdbcSqlStatValue> sqlList = dataSourceStatValue.getSqlList();
            this.save(this.sqlStatBeanInfo, ctx, sqlList);
        }
    }
    
    @Override
    public void saveSpringMethod(final MonitorContext ctx, final List<SpringMethodStatValue> list) {
        this.save(this.springMethodStatBeanInfo, ctx, list);
    }
    
    @Override
    public void saveWebURI(final MonitorContext ctx, final List<WebURIStatValue> list) {
        this.save(this.webURIStatBeanInfo, ctx, list);
    }
    
    @Override
    public void saveSqlWall(final MonitorContext ctx, final List<WallProviderStatValue> statList) {
        this.save(this.wallProviderStatBeanInfo, ctx, statList);
        for (final WallProviderStatValue providerStat : statList) {
            this.save(this.wallSqlStatBeanInfo, ctx, providerStat.getWhiteList());
            this.save(this.wallSqlStatBeanInfo, ctx, providerStat.getBlackList());
            this.save(this.wallTableStatBeanInfo, ctx, providerStat.getTables());
            this.save(this.wallFunctionStatBeanInfo, ctx, providerStat.getFunctions());
        }
    }
    
    @Override
    public void saveWebApp(final MonitorContext ctx, final List<WebAppStatValue> list) {
        this.save(this.webAppStatBeanInfo, ctx, list);
    }
    
    @Override
    public List<JdbcSqlStatValue> loadSqlList(final Map<String, Object> filters) {
        return (List<JdbcSqlStatValue>)this.load(this.sqlStatBeanInfo, filters);
    }
    
    static Integer getInteger(final Map<String, Object> filters, final String key) {
        final Object value = filters.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Integer) {
            return (Integer)value;
        }
        if (value instanceof Number) {
            return ((Number)value).intValue();
        }
        if (!(value instanceof String)) {
            return null;
        }
        final String text = (String)value;
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        return Integer.parseInt(text);
    }
    
    static Date getDate(final Map<String, Object> filters, final String key) {
        final Object value = filters.get(key);
        if (value == null) {
            return null;
        }
        if (value instanceof Number) {
            final long millis = ((Number)value).longValue();
            return new Date(millis);
        }
        if (value instanceof String) {
            final String text = (String)value;
            if (StringUtils.isEmpty(text)) {
                return null;
            }
            try {
                return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(text);
            }
            catch (ParseException e) {
                MonitorDaoJdbcImpl.LOG.error("parse filter error", e);
                return null;
            }
        }
        return null;
    }
    
    private List<?> load(final BeanInfo beanInfo, final Map<String, Object> filters) {
        final List<Object> list = new ArrayList<Object>();
        final StringBuilder buf = new StringBuilder();
        buf.append("SELECT ");
        final List<FieldInfo> fields = beanInfo.getFields();
        for (int i = 0; i < fields.size(); ++i) {
            final FieldInfo fieldInfo = fields.get(i);
            if (i != 0) {
                buf.append(", ");
            }
            final AggregateType aggregateType = fieldInfo.getField().getAnnotation(MField.class).aggregate();
            switch (aggregateType) {
                case Sum: {
                    buf.append("SUM(");
                    buf.append(fieldInfo.getColumnName());
                    buf.append(")");
                    break;
                }
                case Max: {
                    buf.append("MAX(");
                    buf.append(fieldInfo.getColumnName());
                    buf.append(")");
                    break;
                }
                default: {
                    buf.append(fieldInfo.getColumnName());
                    break;
                }
            }
        }
        buf.append("\nFROM ");
        buf.append(this.getTableName(beanInfo));
        buf.append("\nWHERE collectTime >= ? AND collectTime <= ? AND domain = ? AND app = ? AND cluster = ?");
        Date startTime = getDate(filters, "startTime");
        if (startTime == null) {
            final long now = System.currentTimeMillis();
            startTime = new Date(now - 1800000L);
        }
        Date endTime = getDate(filters, "endTime");
        if (endTime == null) {
            endTime = new Date();
        }
        String domain = filters.get("domain");
        if (StringUtils.isEmpty(domain)) {
            domain = "default";
        }
        String app = filters.get("app");
        if (StringUtils.isEmpty(app)) {
            app = "default";
        }
        String cluster = filters.get("cluster");
        if (StringUtils.isEmpty(cluster)) {
            cluster = "default";
        }
        final String host = filters.get("host");
        if (!StringUtils.isEmpty(host)) {
            buf.append("\nAND host = ?");
        }
        final Integer pid = getInteger(filters, "pid");
        if (pid != null) {
            buf.append("\nAND pid = ?");
        }
        final List<FieldInfo> groupByFields = beanInfo.getGroupByFields();
        for (int j = 0; j < groupByFields.size(); ++j) {
            if (j == 0) {
                buf.append("\nGROUP BY ");
            }
            else {
                buf.append(", ");
            }
            final FieldInfo fieldInfo2 = groupByFields.get(j);
            buf.append(fieldInfo2.getColumnName());
        }
        final Integer offset = filters.get("offset");
        Integer limit = filters.get("limit");
        if (limit == null) {
            limit = 1000;
        }
        buf.append("\nLIMIT ");
        if (offset != null) {
            buf.append(offset);
            buf.append(", ");
        }
        buf.append(limit);
        final String sql = buf.toString();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            stmt.setTimestamp(paramIndex++, new Timestamp(startTime.getTime()));
            stmt.setTimestamp(paramIndex++, new Timestamp(endTime.getTime()));
            stmt.setString(paramIndex++, domain);
            stmt.setString(paramIndex++, app);
            stmt.setString(paramIndex++, cluster);
            if (!StringUtils.isEmpty(host)) {
                stmt.setString(paramIndex++, host);
            }
            if (pid != null) {
                stmt.setInt(paramIndex++, pid);
            }
            rs = stmt.executeQuery();
            while (rs.next()) {
                final Object object = this.createInstance(beanInfo);
                for (int k = 0; k < fields.size(); ++k) {
                    final FieldInfo field = fields.get(k);
                    this.readFieldValue(object, field, rs, k + 1);
                }
                list.add(object);
            }
            stmt.close();
        }
        catch (SQLException ex) {
            MonitorDaoJdbcImpl.LOG.error("save sql error", ex);
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
        for (final FieldInfo hashField : beanInfo.getHashFields()) {
            this.loadHashValue(hashField, list, filters);
        }
        return list;
    }
    
    protected void readFieldValue(final Object object, final FieldInfo field, final ResultSet rs, final int paramIndex) throws SQLException {
        final Class<?> fieldType = field.getFieldType();
        Object fieldValue = null;
        if (fieldType.equals(Integer.TYPE) || fieldType.equals(Integer.class)) {
            fieldValue = rs.getInt(paramIndex);
        }
        else if (fieldType.equals(Long.TYPE) || fieldType.equals(Long.class)) {
            fieldValue = rs.getLong(paramIndex);
        }
        else if (fieldType.equals(String.class)) {
            fieldValue = rs.getString(paramIndex);
        }
        else {
            if (!fieldType.equals(Date.class)) {
                throw new UnsupportedOperationException();
            }
            final Timestamp timestamp = rs.getTimestamp(paramIndex);
            if (timestamp != null) {
                fieldValue = new Date(timestamp.getTime());
            }
        }
        try {
            field.getField().set(object, fieldValue);
        }
        catch (IllegalArgumentException e) {
            throw new DruidRuntimeException("set field error" + field.getField(), e);
        }
        catch (IllegalAccessException e2) {
            throw new DruidRuntimeException("set field error" + field.getField(), e2);
        }
    }
    
    private void loadHashValue(final FieldInfo hashField, final List<?> list, final Map<String, Object> filters) {
        String domain = filters.get("domain");
        if (StringUtils.isEmpty(domain)) {
            domain = "default";
        }
        String app = filters.get("app");
        if (StringUtils.isEmpty(app)) {
            app = "default";
        }
        for (final Object statValue : list) {
            try {
                final Long hash = (Long)hashField.field.get(statValue);
                String value = this.cacheGet(hashField.getHashForType(), hash);
                if (value == null) {
                    value = this.getConstValueFromDb(domain, app, hashField.getHashForType(), hash);
                }
                hashField.getHashFor().set(statValue, value);
            }
            catch (IllegalArgumentException e) {
                throw new DruidRuntimeException("set field error" + hashField.getField(), e);
            }
            catch (IllegalAccessException e2) {
                throw new DruidRuntimeException("set field error" + hashField.getField(), e2);
            }
        }
    }
    
    protected String getConstValueFromDb(final String domain, final String app, final String type, final Long hash) {
        final String sql = "select value from druid_const where domain = ? AND app = ? and type = ? and hash = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, domain);
            stmt.setString(2, app);
            stmt.setString(3, type);
            stmt.setLong(4, hash);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(1);
            }
        }
        catch (SQLException ex) {
            MonitorDaoJdbcImpl.LOG.error("save const error error", ex);
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
        return null;
    }
    
    private void saveHash(final FieldInfo hashField, final MonitorContext ctx, final List<?> list) {
        final String hashType = hashField.getHashForType();
        for (final Object statValue : list) {
            try {
                final Long hash = (Long)hashField.field.get(statValue);
                if (this.cacheContains(hashField.getHashForType(), hash)) {
                    continue;
                }
                final String value = (String)hashField.getHashFor().get(statValue);
                final String sql = "insert into druid_const (domain, app, type, hash, value) values (?, ?, ?, ?, ?)";
                Connection conn = null;
                PreparedStatement stmt = null;
                try {
                    conn = this.dataSource.getConnection();
                    stmt = conn.prepareStatement("insert into druid_const (domain, app, type, hash, value) values (?, ?, ?, ?, ?)");
                    stmt.setString(1, ctx.getDomain());
                    stmt.setString(2, ctx.getApp());
                    stmt.setString(3, hashType);
                    stmt.setLong(4, hash);
                    stmt.setString(5, value);
                    stmt.execute();
                    stmt.close();
                }
                catch (SQLException ex) {}
                finally {
                    JdbcUtils.close(stmt);
                    JdbcUtils.close(conn);
                }
                this.cachePut(hashField.getHashForType(), hash, value);
            }
            catch (IllegalArgumentException e) {
                throw new DruidRuntimeException("set field error" + hashField.getField(), e);
            }
            catch (IllegalAccessException e2) {
                throw new DruidRuntimeException("set field error" + hashField.getField(), e2);
            }
        }
    }
    
    private void save(final BeanInfo beanInfo, final MonitorContext ctx, final List<?> list) {
        if (list.size() == 0) {
            return;
        }
        for (final FieldInfo hashField : beanInfo.getHashFields()) {
            this.saveHash(hashField, ctx, list);
        }
        final String sql = this.buildInsertSql(beanInfo);
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            for (final Object statValue : list) {
                this.setParameterForSqlStat(beanInfo, ctx, stmt, statValue);
                stmt.addBatch();
            }
            stmt.executeBatch();
            stmt.close();
        }
        catch (SQLException ex) {
            MonitorDaoJdbcImpl.LOG.error("save sql error", ex);
        }
        finally {
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    protected void setParameterForSqlStat(final BeanInfo beanInfo, final MonitorContext ctx, final PreparedStatement stmt, final Object sqlStat) throws SQLException {
        int paramIndex = 1;
        setParam(stmt, paramIndex++, ctx.getDomain());
        setParam(stmt, paramIndex++, ctx.getApp());
        setParam(stmt, paramIndex++, ctx.getCluster());
        setParam(stmt, paramIndex++, ctx.getHost());
        setParam(stmt, paramIndex++, ctx.getPID());
        setParam(stmt, paramIndex++, ctx.getCollectTime());
        try {
            final List<FieldInfo> fields = beanInfo.getFields();
            for (final FieldInfo field : fields) {
                final Class<?> fieldType = field.getFieldType();
                final Object value = field.getField().get(sqlStat);
                if (fieldType.equals(Integer.TYPE) || fieldType.equals(Integer.class)) {
                    setParam(stmt, paramIndex, (Integer)value);
                }
                else if (fieldType.equals(Long.TYPE) || fieldType.equals(Long.class)) {
                    setParam(stmt, paramIndex, (Long)value);
                }
                else if (fieldType.equals(String.class)) {
                    setParam(stmt, paramIndex, (String)value);
                }
                else if (fieldType.equals(Date.class)) {
                    setParam(stmt, paramIndex, (Date)value);
                }
                else {
                    if (!fieldType.equals(Boolean.TYPE) && !fieldType.equals(Boolean.class)) {
                        throw new UnsupportedOperationException("not support type : " + fieldType);
                    }
                    setParam(stmt, paramIndex, (Boolean)value);
                }
                ++paramIndex;
            }
        }
        catch (RuntimeException ex) {
            throw ex;
        }
        catch (Exception ex2) {
            throw new DruidRuntimeException("setParam error", ex2);
        }
    }
    
    public Object createInstance(final BeanInfo beanInfo) {
        try {
            return beanInfo.getClazz().newInstance();
        }
        catch (InstantiationException ex) {
            throw new DruidRuntimeException("create instance error", ex);
        }
        catch (IllegalAccessException ex2) {
            throw new DruidRuntimeException("create instance error", ex2);
        }
    }
    
    public String buildInsertSql(final BeanInfo beanInfo) {
        String sql = beanInfo.insertSql;
        if (sql != null) {
            return sql;
        }
        final StringBuilder buf = new StringBuilder();
        buf.append("INSERT INTO ").append(this.getTableName(beanInfo));
        buf.append(" (domain, app, cluster, host, pid, collectTime");
        final List<FieldInfo> fields = beanInfo.getFields();
        for (final FieldInfo field : fields) {
            buf.append(", ");
            buf.append(field.getColumnName());
        }
        buf.append(")\nVALUES (?, ?, ?, ?, ?, ?");
        for (int i = 0; i < fields.size(); ++i) {
            buf.append(", ?");
        }
        buf.append(")");
        sql = buf.toString();
        beanInfo.setInsertSql(sql);
        return sql;
    }
    
    public String getTableName(final BeanInfo beanInfo) {
        return beanInfo.getTableName();
    }
    
    protected long getSqlHash(final String sql) {
        return Utils.fnv_64(sql);
    }
    
    static void setParam(final PreparedStatement stmt, final int paramIndex, final String value) throws SQLException {
        if (value == null) {
            stmt.setNull(paramIndex, 12);
        }
        else {
            stmt.setString(paramIndex, value);
        }
    }
    
    static void setParam(final PreparedStatement stmt, final int paramIndex, final Boolean value) throws SQLException {
        if (value == null) {
            stmt.setNull(paramIndex, 16);
        }
        else {
            stmt.setBoolean(paramIndex, value);
        }
    }
    
    static void setParam(final PreparedStatement stmt, final int paramIndex, final Long value) throws SQLException {
        if (value == null || value == 0L) {
            stmt.setNull(paramIndex, -5);
        }
        else {
            stmt.setLong(paramIndex, value);
        }
    }
    
    static void setParam(final PreparedStatement stmt, final int paramIndex, final Integer value) throws SQLException {
        if (value == null || value == 0) {
            stmt.setNull(paramIndex, 4);
        }
        else {
            stmt.setInt(paramIndex, value);
        }
    }
    
    static void setParam(final PreparedStatement stmt, final int paramIndex, final Date value) throws SQLException {
        if (value == null) {
            stmt.setNull(paramIndex, 93);
        }
        else {
            stmt.setTimestamp(paramIndex, new Timestamp(value.getTime()));
        }
    }
    
    public boolean cacheContains(final String type, final Long hash) {
        final Map<Long, String> cache = this.cacheMap.get(type);
        return cache != null && cache.containsKey(hash);
    }
    
    public String cacheGet(final String type, final Long hash) {
        final Map<Long, String> cache = this.cacheMap.get(type);
        if (cache == null) {
            return null;
        }
        return cache.get(hash);
    }
    
    public void cachePut(final String type, final Long hash, final String value) {
        ConcurrentMap<Long, String> cache = this.cacheMap.get(type);
        if (cache == null) {
            this.cacheMap.putIfAbsent(type, new ConcurrentHashMap<Long, String>(16, 0.75f, 1));
            cache = this.cacheMap.get(type);
        }
        cache.putIfAbsent(hash, value);
    }
    
    @Override
    public void insertAppIfNotExits(final String domain, final String app) throws SQLException {
        final MonitorApp monitorApp = this.findApp(domain, app);
        if (monitorApp != null) {
            return;
        }
        final String sql = "insert druid_app (domain, app) values (?, ?)";
        JdbcUtils.execute(this.dataSource, sql, domain, app);
    }
    
    public List<MonitorApp> listApp(final String domain) throws SQLException {
        final List<MonitorApp> list = new ArrayList<MonitorApp>();
        final String sql = "select id, domain, app from druid_app  where domain = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, domain);
            rs = stmt.executeQuery();
            if (rs.next()) {
                list.add(this.readApp(rs));
            }
            return list;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    public MonitorApp findApp(final String domain, final String app) throws SQLException {
        final String sql = "select id, domain, app from druid_app  where domain = ? and app = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, domain);
            stmt.setString(2, app);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return this.readApp(rs);
            }
            return null;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    private MonitorApp readApp(final ResultSet rs) throws SQLException {
        final MonitorApp app = new MonitorApp();
        app.setId(rs.getLong(1));
        app.setDomain(rs.getString(2));
        app.setApp(rs.getString(3));
        return app;
    }
    
    public List<MonitorCluster> listCluster(final String domain, final String app) throws SQLException {
        final List<MonitorCluster> list = new ArrayList<MonitorCluster>();
        String sql = "select id, domain, app, cluster from druid_cluster  where domain = ?";
        if (app != null) {
            sql += " and app = ?";
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, domain);
            if (app != null) {
                stmt.setString(2, app);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                list.add(this.readCluster(rs));
            }
            return list;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    @Override
    public void insertClusterIfNotExits(final String domain, final String app, final String cluster) throws SQLException {
        final MonitorCluster monitorApp = this.findCluster(domain, app, cluster);
        if (monitorApp != null) {
            return;
        }
        final String sql = "insert druid_cluster (domain, app, cluster) values (?, ?, ?)";
        JdbcUtils.execute(this.dataSource, sql, domain, app, cluster);
    }
    
    public MonitorCluster findCluster(final String domain, final String app, final String cluster) throws SQLException {
        final String sql = "select id, domain, app, cluster from druid_cluster  where domain = ? and app = ? and cluster = ?";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, domain);
            stmt.setString(2, app);
            stmt.setString(3, cluster);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return this.readCluster(rs);
            }
            return null;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    private MonitorCluster readCluster(final ResultSet rs) throws SQLException {
        final MonitorCluster app = new MonitorCluster();
        app.setId(rs.getLong(1));
        app.setDomain(rs.getString(2));
        app.setApp(rs.getString(3));
        app.setCluster(rs.getString(4));
        return app;
    }
    
    @Override
    public void insertOrUpdateInstance(final String domain, final String app, final String cluster, final String host, final String ip, final Date startTime, final long pid) throws SQLException {
        final MonitorInstance monitorInst = this.findInst(domain, app, cluster, host);
        if (monitorInst == null) {
            final String sql = "insert into druid_inst (domain, app, cluster, host, ip, lastActiveTime, lastPID)  values (?, ?, ?, ?, ?, ?, ?)";
            JdbcUtils.execute(this.dataSource, sql, domain, app, cluster, host, ip, startTime, pid);
        }
        else {
            final String sql = "update druid_inst set ip = ?, lastActiveTime = ?, lastPID = ?  where domain = ? and app = ? and cluster = ? and host = ? ";
            JdbcUtils.execute(this.dataSource, sql, ip, startTime, pid, domain, app, cluster, host);
        }
    }
    
    public MonitorInstance findInst(final String domain, final String app, final String cluster, final String host) throws SQLException {
        final String sql = "select id, domain, app, cluster, host, ip, lastActiveTime, lastPID from druid_inst  where domain = ? and app = ? and cluster = ? and host = ?  limit 1";
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, domain);
            stmt.setString(2, app);
            stmt.setString(3, cluster);
            stmt.setString(4, host);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return this.readInst(rs);
            }
            return null;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    public List<MonitorInstance> listInst(final String domain, final String app, final String cluster) throws SQLException {
        final List<MonitorInstance> list = new ArrayList<MonitorInstance>();
        String sql = "select id, domain, app, cluster, host, ip, lastActiveTime, lastPID from druid_inst where domain = ?";
        if (app != null) {
            sql += " and app = ?";
        }
        if (cluster != null) {
            sql += " and cluster = ?";
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = this.dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            int paramIndex = 1;
            stmt.setString(paramIndex++, domain);
            if (app != null) {
                stmt.setString(paramIndex++, app);
            }
            if (cluster != null) {
                stmt.setString(paramIndex++, cluster);
            }
            rs = stmt.executeQuery();
            if (rs.next()) {
                list.add(this.readInst(rs));
            }
            return list;
        }
        finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
            JdbcUtils.close(conn);
        }
    }
    
    private MonitorInstance readInst(final ResultSet rs) throws SQLException {
        final MonitorInstance inst = new MonitorInstance();
        inst.setId(rs.getLong(1));
        inst.setDomain(rs.getString(2));
        inst.setApp(rs.getString(3));
        inst.setCluster(rs.getString(4));
        inst.setHost(rs.getString(5));
        inst.setIp(rs.getString(6));
        inst.setLastActiveTime(rs.getTimestamp(7));
        inst.setLastPID(rs.getLong(8));
        return inst;
    }
    
    static {
        LOG = LogFactory.getLog(MonitorDaoJdbcImpl.class);
    }
    
    public static class BeanInfo
    {
        private final Class<?> clazz;
        private final List<FieldInfo> fields;
        private final List<FieldInfo> groupByFields;
        private final List<FieldInfo> hashFields;
        private final String tableName;
        private String insertSql;
        
        public BeanInfo(final Class<?> clazz) {
            this.fields = new ArrayList<FieldInfo>();
            this.groupByFields = new ArrayList<FieldInfo>();
            this.hashFields = new ArrayList<FieldInfo>();
            this.clazz = clazz;
            final MTable annotation = clazz.getAnnotation(MTable.class);
            if (annotation == null) {
                throw new IllegalArgumentException(clazz.getName() + " not contains @MTable");
            }
            this.tableName = annotation.name();
            for (final Field field : clazz.getDeclaredFields()) {
                final MField annotation2 = field.getAnnotation(MField.class);
                if (annotation2 != null) {
                    String columnName = annotation2.name();
                    if (StringUtils.isEmpty(columnName)) {
                        columnName = field.getName();
                    }
                    Field hashFor = null;
                    String hashForType = null;
                    if (!StringUtils.isEmpty(annotation2.hashFor())) {
                        try {
                            hashFor = clazz.getDeclaredField(annotation2.hashFor());
                            hashForType = annotation2.hashForType();
                        }
                        catch (Exception e) {
                            throw new IllegalStateException("hashFor error", e);
                        }
                    }
                    final FieldInfo fieldInfo = new FieldInfo(field, columnName, hashFor, hashForType);
                    this.fields.add(fieldInfo);
                    if (annotation2.groupBy()) {
                        this.groupByFields.add(fieldInfo);
                    }
                    if (hashFor != null) {
                        this.hashFields.add(fieldInfo);
                    }
                }
            }
        }
        
        public String getTableName() {
            return this.tableName;
        }
        
        public Class<?> getClazz() {
            return this.clazz;
        }
        
        public String getInsertSql() {
            return this.insertSql;
        }
        
        public void setInsertSql(final String insertSql) {
            this.insertSql = insertSql;
        }
        
        public List<FieldInfo> getFields() {
            return this.fields;
        }
        
        public List<FieldInfo> getGroupByFields() {
            return this.groupByFields;
        }
        
        public List<FieldInfo> getHashFields() {
            return this.hashFields;
        }
    }
    
    public static class FieldInfo
    {
        private final Field field;
        private final String columnName;
        private final Field hashFor;
        private final String hashForType;
        
        public FieldInfo(final Field field, final String columnName, final Field hashFor, final String hashForType) {
            this.field = field;
            this.columnName = columnName;
            this.hashFor = hashFor;
            this.hashForType = hashForType;
            field.setAccessible(true);
            if (hashFor != null) {
                hashFor.setAccessible(true);
            }
        }
        
        public String getHashForType() {
            return this.hashForType;
        }
        
        public Field getField() {
            return this.field;
        }
        
        public Field getHashFor() {
            return this.hashFor;
        }
        
        public String getColumnName() {
            return this.columnName;
        }
        
        public Class<?> getFieldType() {
            return this.field.getType();
        }
    }
}
