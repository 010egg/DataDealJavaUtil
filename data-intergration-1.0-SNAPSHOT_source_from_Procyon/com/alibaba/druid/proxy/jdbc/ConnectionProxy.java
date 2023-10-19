// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.proxy.jdbc;

import java.util.Date;
import java.util.Properties;
import java.sql.Connection;

public interface ConnectionProxy extends Connection, WrapperProxy
{
    Connection getRawObject();
    
    Properties getProperties();
    
    DataSourceProxy getDirectDataSource();
    
    Date getConnectedTime();
    
    TransactionInfo getTransactionInfo();
    
    int getCloseCount();
}
