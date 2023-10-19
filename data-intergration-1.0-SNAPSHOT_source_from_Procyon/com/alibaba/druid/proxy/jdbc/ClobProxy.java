// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.sql.Clob;

public interface ClobProxy extends Clob
{
    ConnectionProxy getConnectionWrapper();
    
    Clob getRawClob();
}
