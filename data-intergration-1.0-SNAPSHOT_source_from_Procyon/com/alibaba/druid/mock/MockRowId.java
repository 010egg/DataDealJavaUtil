// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.RowId;

public class MockRowId implements RowId
{
    private byte[] bytes;
    
    public MockRowId() {
    }
    
    public MockRowId(final byte[] bytes) {
        this.bytes = bytes;
    }
    
    @Override
    public byte[] getBytes() {
        return this.bytes;
    }
    
    public void setBytes(final byte[] bytes) {
        this.bytes = bytes;
    }
}
