// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.ArrayList;
import java.util.List;

public class TransactionInfo
{
    private final long id;
    private final List<String> sqlList;
    private final long startTimeMillis;
    private long endTimeMillis;
    
    public TransactionInfo(final long id) {
        this.sqlList = new ArrayList<String>(4);
        this.id = id;
        this.startTimeMillis = System.currentTimeMillis();
    }
    
    public long getId() {
        return this.id;
    }
    
    public List<String> getSqlList() {
        return this.sqlList;
    }
    
    public long getStartTimeMillis() {
        return this.startTimeMillis;
    }
    
    public long getEndTimeMillis() {
        return this.endTimeMillis;
    }
    
    public void setEndTimeMillis() {
        if (this.endTimeMillis == 0L) {
            this.endTimeMillis = System.currentTimeMillis();
        }
    }
    
    public void setEndTimeMillis(final long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }
}
