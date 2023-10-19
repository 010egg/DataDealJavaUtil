// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util.jdbc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.List;

public class LocalResultSet extends ResultSetBase
{
    private int rowIndex;
    private List<Object[]> rows;
    
    public LocalResultSet(final Statement statement) {
        super(statement);
        this.rowIndex = -1;
        this.rows = new ArrayList<Object[]>();
    }
    
    public List<Object[]> getRows() {
        return this.rows;
    }
    
    @Override
    public synchronized boolean next() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        if (this.rowIndex < this.rows.size() - 1) {
            ++this.rowIndex;
            return true;
        }
        return false;
    }
    
    @Override
    public Object getObjectInternal(final int columnIndex) {
        final Object[] row = this.rows.get(this.rowIndex);
        return row[columnIndex - 1];
    }
    
    @Override
    public synchronized boolean previous() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        if (this.rowIndex > 0) {
            --this.rowIndex;
            return true;
        }
        return false;
    }
    
    @Override
    public void updateObject(final int columnIndex, final Object x) throws SQLException {
        final Object[] row = this.rows.get(this.rowIndex);
        row[columnIndex - 1] = x;
    }
}
