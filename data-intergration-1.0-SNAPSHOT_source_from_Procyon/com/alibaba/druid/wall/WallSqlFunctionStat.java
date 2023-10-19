// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

public class WallSqlFunctionStat
{
    private int invokeCount;
    
    public int getInvokeCount() {
        return this.invokeCount;
    }
    
    public void incrementInvokeCount() {
        ++this.invokeCount;
    }
    
    public void addInvokeCount(final int value) {
        this.invokeCount += value;
    }
}
