// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.pool;

import java.sql.PreparedStatement;
import java.sql.CallableStatement;

public abstract class ProxyCallableStatement extends ProxyPreparedStatement implements CallableStatement
{
    protected ProxyCallableStatement(final ProxyConnection connection, final CallableStatement statement) {
        super(connection, statement);
    }
}
