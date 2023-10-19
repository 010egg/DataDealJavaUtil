// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.SQLException;

public interface SQLInsertValueHandler
{
    Object newRow() throws SQLException;
    
    void processInteger(final Object p0, final int p1, final Number p2) throws SQLException;
    
    void processString(final Object p0, final int p1, final String p2) throws SQLException;
    
    void processDate(final Object p0, final int p1, final String p2) throws SQLException;
    
    void processDate(final Object p0, final int p1, final Date p2) throws SQLException;
    
    void processTimestamp(final Object p0, final int p1, final String p2) throws SQLException;
    
    void processTimestamp(final Object p0, final int p1, final Date p2) throws SQLException;
    
    void processTime(final Object p0, final int p1, final String p2) throws SQLException;
    
    void processDecimal(final Object p0, final int p1, final BigDecimal p2) throws SQLException;
    
    void processBoolean(final Object p0, final int p1, final boolean p2) throws SQLException;
    
    void processNull(final Object p0, final int p1) throws SQLException;
    
    void processFunction(final Object p0, final int p1, final String p2, final long p3, final Object... p4) throws SQLException;
    
    void processRow(final Object p0) throws SQLException;
    
    void processComplete() throws SQLException;
}
