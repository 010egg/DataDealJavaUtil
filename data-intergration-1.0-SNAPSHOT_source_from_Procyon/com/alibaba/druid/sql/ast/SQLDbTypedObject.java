// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.DbType;

public interface SQLDbTypedObject extends SQLObject
{
    DbType getDbType();
}
