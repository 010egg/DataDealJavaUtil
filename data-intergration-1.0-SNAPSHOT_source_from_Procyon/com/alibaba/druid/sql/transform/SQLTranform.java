// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.transform;

import com.alibaba.druid.DbType;

public interface SQLTranform
{
    DbType getSourceDbType();
    
    DbType getTargetDbType();
}
