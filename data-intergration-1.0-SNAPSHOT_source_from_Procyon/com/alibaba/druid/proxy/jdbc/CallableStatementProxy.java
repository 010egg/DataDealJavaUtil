// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;

public interface CallableStatementProxy extends CallableStatement, PreparedStatementProxy
{
    CallableStatement getRawObject();
}
