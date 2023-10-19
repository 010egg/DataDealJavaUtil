// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

public interface SQLObjectWithDataType extends SQLObject
{
    SQLDataType getDataType();
    
    void setDataType(final SQLDataType p0);
}
