// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.sql.SQLException;
import java.sql.ResultSet;

public interface ResultSetConsumer<T>
{
    T apply(final ResultSet p0) throws SQLException;
    
    void accept(final T p0);
}
