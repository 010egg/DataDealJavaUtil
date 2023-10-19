// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.transform;

import com.alibaba.druid.DbType;

public abstract class SQLTranformImpl implements SQLTranform
{
    protected DbType sourceDbType;
    protected DbType targetDbType;
    
    @Override
    public DbType getSourceDbType() {
        return this.sourceDbType;
    }
    
    @Override
    public DbType getTargetDbType() {
        return this.targetDbType;
    }
}
