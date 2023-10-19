// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.List;

public abstract class SQLExprImpl extends SQLObjectImpl implements SQLExpr
{
    @Override
    public abstract boolean equals(final Object p0);
    
    @Override
    public abstract int hashCode();
    
    @Override
    public abstract SQLExpr clone();
    
    @Override
    public SQLDataType computeDataType() {
        return null;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return null;
    }
}
