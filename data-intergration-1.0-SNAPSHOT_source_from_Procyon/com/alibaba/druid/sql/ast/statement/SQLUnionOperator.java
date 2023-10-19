// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

public enum SQLUnionOperator
{
    UNION("UNION"), 
    UNION_ALL("UNION ALL"), 
    MINUS("MINUS"), 
    MINUS_DISTINCT("MINUS DISTINCT"), 
    MINUS_ALL("MINUS ALL"), 
    EXCEPT("EXCEPT"), 
    EXCEPT_ALL("EXCEPT ALL"), 
    EXCEPT_DISTINCT("EXCEPT DISTINCT"), 
    INTERSECT("INTERSECT"), 
    INTERSECT_ALL("INTERSECT ALL"), 
    INTERSECT_DISTINCT("INTERSECT DISTINCT"), 
    DISTINCT("UNION DISTINCT");
    
    public final String name;
    public final String name_lcase;
    
    private SQLUnionOperator(final String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }
    
    @Override
    public String toString() {
        return this.name;
    }
}
