// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.io.Writer;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.sql.Clob;

public class MockClob implements Clob
{
    private byte[] bytes;
    private ByteArrayOutputStream out;
    
    public MockClob() {
        this(new byte[0]);
    }
    
    public MockClob(final byte[] bytes) {
        this.out = new ByteArrayOutputStream();
        this.bytes = bytes;
    }
    
    @Override
    public long length() throws SQLException {
        return this.bytes.length;
    }
    
    @Override
    public String getSubString(final long pos, final int length) throws SQLException {
        return new String(this.bytes, (int)(pos - 1L), length);
    }
    
    @Override
    public Reader getCharacterStream() throws SQLException {
        return null;
    }
    
    @Override
    public InputStream getAsciiStream() throws SQLException {
        return new ByteArrayInputStream(this.bytes);
    }
    
    @Override
    public long position(final String searchstr, final long start) throws SQLException {
        if (this.bytes.length == 0) {
            return 0L;
        }
        return new String(this.bytes).indexOf(searchstr);
    }
    
    @Override
    public long position(final Clob searchstr, final long start) throws SQLException {
        return 0L;
    }
    
    @Override
    public int setString(final long pos, final String str) throws SQLException {
        return 0;
    }
    
    @Override
    public int setString(final long pos, final String str, final int offset, final int len) throws SQLException {
        return 0;
    }
    
    @Override
    public OutputStream setAsciiStream(final long pos) throws SQLException {
        return this.out;
    }
    
    @Override
    public Writer setCharacterStream(final long pos) throws SQLException {
        return null;
    }
    
    @Override
    public void truncate(final long len) throws SQLException {
    }
    
    @Override
    public void free() throws SQLException {
    }
    
    @Override
    public Reader getCharacterStream(final long pos, final long length) throws SQLException {
        return null;
    }
}
