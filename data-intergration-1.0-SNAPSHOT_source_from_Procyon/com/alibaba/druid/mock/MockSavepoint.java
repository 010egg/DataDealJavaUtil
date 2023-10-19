// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.Savepoint;

public class MockSavepoint implements Savepoint
{
    private int savepointId;
    private String savepointName;
    
    @Override
    public int getSavepointId() {
        return this.savepointId;
    }
    
    public void setSavepointId(final int savepointId) {
        this.savepointId = savepointId;
    }
    
    @Override
    public String getSavepointName() {
        return this.savepointName;
    }
    
    public void setSavepointName(final String savepointName) {
        this.savepointName = savepointName;
    }
}
