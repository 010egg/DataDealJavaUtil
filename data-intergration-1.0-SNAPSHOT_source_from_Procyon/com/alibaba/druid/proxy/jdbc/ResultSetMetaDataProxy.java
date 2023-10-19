// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.sql.ResultSetMetaData;

public interface ResultSetMetaDataProxy extends ResultSetMetaData, WrapperProxy
{
    ResultSetMetaData getResultSetMetaDataRaw();
    
    ResultSetProxy getResultSetProxy();
}
