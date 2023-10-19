// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.sql.Statement;
import java.util.Map;
import java.sql.PreparedStatement;

public interface PreparedStatementProxy extends PreparedStatement, StatementProxy
{
    String getSql();
    
    PreparedStatement getRawObject();
    
    Map<Integer, JdbcParameter> getParameters();
}
