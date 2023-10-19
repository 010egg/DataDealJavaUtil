// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface MockStatementBase extends Statement
{
    MockConnection getConnection() throws SQLException;
}
