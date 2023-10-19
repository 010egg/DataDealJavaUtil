// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock.handler;

import java.sql.SQLException;
import java.sql.ResultSet;
import com.alibaba.druid.mock.MockStatementBase;

public interface MockExecuteHandler
{
    ResultSet executeQuery(final MockStatementBase p0, final String p1) throws SQLException;
}
