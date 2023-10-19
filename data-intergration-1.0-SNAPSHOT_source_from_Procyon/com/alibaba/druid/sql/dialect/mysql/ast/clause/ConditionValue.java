// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

public class ConditionValue
{
    private ConditionType type;
    private String value;
    
    public ConditionType getType() {
        return this.type;
    }
    
    public void setType(final ConditionType type) {
        this.type = type;
    }
    
    public String getValue() {
        return this.value;
    }
    
    public void setValue(final String value) {
        this.value = value;
    }
    
    public enum ConditionType
    {
        SQLSTATE, 
        SELF, 
        SYSTEM, 
        MYSQL_ERROR_CODE;
    }
}
