// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.jdbc2;

import java.sql.ResultSet;

public class ResultWrapper
{
    private final ResultSet rs;
    private final int updateCount;
    private final long insertOID;
    private ResultWrapper next;
    
    public ResultWrapper(final ResultSet rs) {
        this.rs = rs;
        this.updateCount = -1;
        this.insertOID = -1L;
    }
    
    public ResultWrapper(final int updateCount, final long insertOID) {
        this.rs = null;
        this.updateCount = updateCount;
        this.insertOID = insertOID;
    }
    
    public ResultSet getResultSet() {
        return this.rs;
    }
    
    public int getUpdateCount() {
        return this.updateCount;
    }
    
    public long getInsertOID() {
        return this.insertOID;
    }
    
    public ResultWrapper getNext() {
        return this.next;
    }
    
    public void append(final ResultWrapper newResult) {
        ResultWrapper tail;
        for (tail = this; tail.next != null; tail = tail.next) {}
        tail.next = newResult;
    }
}
