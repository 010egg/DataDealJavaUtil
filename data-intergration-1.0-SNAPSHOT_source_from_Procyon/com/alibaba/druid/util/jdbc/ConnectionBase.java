// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util.jdbc;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Properties;
import java.sql.SQLWarning;
import java.util.Map;
import java.sql.Connection;

public abstract class ConnectionBase implements Connection
{
    private boolean autoCommit;
    private String catalog;
    private int transactionIsolation;
    private int holdability;
    private Map<String, Class<?>> typeMap;
    private SQLWarning warnings;
    private boolean readOnly;
    private String url;
    private Properties info;
    
    public ConnectionBase(final String url, final Properties info) {
        this.autoCommit = true;
        this.typeMap = new HashMap<String, Class<?>>();
        this.url = url;
        this.info = info;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    public Properties getConnectProperties() {
        return this.info;
    }
    
    @Override
    public void setAutoCommit(final boolean autoCommit) throws SQLException {
        this.autoCommit = autoCommit;
    }
    
    @Override
    public boolean getAutoCommit() throws SQLException {
        return this.autoCommit;
    }
    
    @Override
    public void setCatalog(final String catalog) throws SQLException {
        this.checkState();
        this.catalog = catalog;
    }
    
    @Override
    public String getCatalog() throws SQLException {
        return this.catalog;
    }
    
    public void checkState() throws SQLException {
    }
    
    @Override
    public void setTransactionIsolation(final int level) throws SQLException {
        this.checkState();
        this.transactionIsolation = level;
    }
    
    @Override
    public int getTransactionIsolation() throws SQLException {
        return this.transactionIsolation;
    }
    
    @Override
    public SQLWarning getWarnings() throws SQLException {
        return this.warnings;
    }
    
    @Override
    public void clearWarnings() throws SQLException {
        this.warnings = null;
    }
    
    public void setWarnings(final SQLWarning warnings) {
        this.warnings = warnings;
    }
    
    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        return this.typeMap;
    }
    
    @Override
    public void setTypeMap(final Map<String, Class<?>> map) throws SQLException {
        this.typeMap = map;
    }
    
    @Override
    public void setHoldability(final int holdability) throws SQLException {
        this.holdability = holdability;
    }
    
    @Override
    public int getHoldability() {
        return this.holdability;
    }
    
    @Override
    public void setReadOnly(final boolean readOnly) throws SQLException {
        this.readOnly = readOnly;
    }
    
    @Override
    public boolean isReadOnly() throws SQLException {
        return this.readOnly;
    }
}
