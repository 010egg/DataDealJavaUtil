// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.SQLException;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;

public class MockBlob implements Blob
{
    private final ByteArrayOutputStream out;
    
    public MockBlob() {
        this.out = new ByteArrayOutputStream();
    }
    
    @Override
    public long length() throws SQLException {
        return this.out.size();
    }
    
    @Override
    public byte[] getBytes(final long pos, final int length) throws SQLException {
        final byte[] bytes = new byte[length];
        final byte[] outBytes = this.out.toByteArray();
        System.arraycopy(outBytes, (int)(pos - 1L), bytes, 0, length);
        return bytes;
    }
    
    @Override
    public InputStream getBinaryStream() throws SQLException {
        return new ByteArrayInputStream(this.out.toByteArray());
    }
    
    @Override
    public long position(final byte[] pattern, final long start) throws SQLException {
        return 0L;
    }
    
    @Override
    public long position(final Blob pattern, final long start) throws SQLException {
        return 0L;
    }
    
    @Override
    public int setBytes(final long pos, final byte[] bytes) throws SQLException {
        return 0;
    }
    
    @Override
    public int setBytes(final long pos, final byte[] bytes, final int offset, final int len) throws SQLException {
        return 0;
    }
    
    @Override
    public OutputStream setBinaryStream(final long pos) throws SQLException {
        return null;
    }
    
    @Override
    public void truncate(final long len) throws SQLException {
    }
    
    @Override
    public void free() throws SQLException {
    }
    
    @Override
    public InputStream getBinaryStream(final long pos, final long length) throws SQLException {
        return null;
    }
}
