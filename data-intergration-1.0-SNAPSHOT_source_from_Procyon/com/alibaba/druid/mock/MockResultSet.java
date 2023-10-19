// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;
import java.util.List;
import java.sql.ResultSet;
import com.alibaba.druid.util.jdbc.ResultSetBase;

public class MockResultSet extends ResultSetBase implements ResultSet
{
    private int rowIndex;
    private List<Object[]> rows;
    
    public MockResultSet(final Statement statement) {
        this(statement, new ArrayList<Object[]>());
    }
    
    public MockResultSet(final Statement statement, final List<Object[]> rows) {
        super(statement);
        this.rowIndex = -1;
        this.rows = rows;
        super.metaData = new MockResultSetMetaData();
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
    public ResultSetMetaData getMetaData() throws SQLException {
        if (this.closed) {
            throw new SQLException("resultSet closed");
        }
        return this.metaData;
    }
    
    public MockResultSetMetaData getMockMetaData() throws SQLException {
        return (MockResultSetMetaData)this.metaData;
    }
    
    @Override
    public Object getObjectInternal(final int columnIndex) {
        final Object[] row = this.rows.get(this.rowIndex);
        final Object obj = row[columnIndex - 1];
        return obj;
    }
    
    @Override
    public synchronized boolean previous() throws SQLException {
        if (this.closed) {
            throw new SQLException();
        }
        if (this.rowIndex >= 0) {
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
