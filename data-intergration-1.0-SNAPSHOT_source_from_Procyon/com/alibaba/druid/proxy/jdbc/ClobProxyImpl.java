// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.io.Writer;
import java.io.OutputStream;
import java.io.Reader;
import java.io.InputStream;
import java.sql.SQLException;
import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.filter.FilterChain;
import java.sql.Clob;

public class ClobProxyImpl implements ClobProxy
{
    protected final Clob clob;
    protected final ConnectionProxy connection;
    protected final DataSourceProxy dataSource;
    
    public ClobProxyImpl(final DataSourceProxy dataSource, final ConnectionProxy connection, final Clob clob) {
        if (clob == null) {
            throw new IllegalArgumentException("clob is null");
        }
        this.dataSource = dataSource;
        this.connection = connection;
        this.clob = clob;
    }
    
    public FilterChain createChain() {
        return new FilterChainImpl(this.dataSource);
    }
    
    @Override
    public ConnectionProxy getConnectionWrapper() {
        return this.connection;
    }
    
    @Override
    public Clob getRawClob() {
        return this.clob;
    }
    
    @Override
    public void free() throws SQLException {
        this.createChain().clob_free(this);
    }
    
    @Override
    public InputStream getAsciiStream() throws SQLException {
        return this.createChain().clob_getAsciiStream(this);
    }
    
    @Override
    public Reader getCharacterStream() throws SQLException {
        return this.createChain().clob_getCharacterStream(this);
    }
    
    @Override
    public Reader getCharacterStream(final long pos, final long length) throws SQLException {
        return this.createChain().clob_getCharacterStream(this, pos, length);
    }
    
    @Override
    public String getSubString(final long pos, final int length) throws SQLException {
        return this.createChain().clob_getSubString(this, pos, length);
    }
    
    @Override
    public long length() throws SQLException {
        return this.createChain().clob_length(this);
    }
    
    @Override
    public long position(final String searchstr, final long start) throws SQLException {
        return this.createChain().clob_position(this, searchstr, start);
    }
    
    @Override
    public long position(final Clob searchstr, final long start) throws SQLException {
        return this.createChain().clob_position(this, searchstr, start);
    }
    
    @Override
    public OutputStream setAsciiStream(final long pos) throws SQLException {
        return this.createChain().clob_setAsciiStream(this, pos);
    }
    
    @Override
    public Writer setCharacterStream(final long pos) throws SQLException {
        return this.createChain().clob_setCharacterStream(this, pos);
    }
    
    @Override
    public int setString(final long pos, final String str) throws SQLException {
        return this.createChain().clob_setString(this, pos, str);
    }
    
    @Override
    public int setString(final long pos, final String str, final int offset, final int len) throws SQLException {
        return this.createChain().clob_setString(this, pos, str, offset, len);
    }
    
    @Override
    public void truncate(final long len) throws SQLException {
        this.createChain().clob_truncate(this, len);
    }
    
    @Override
    public String toString() {
        return this.clob.toString();
    }
}
