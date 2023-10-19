// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

public enum SQLObjectType
{
    TABLE("TABLE"), 
    FUNCTION("FUNCTION"), 
    PROCEDURE("PROCEDURE"), 
    USER("USER"), 
    DATABASE("DATABASE"), 
    SCHEMA("SCHEMA"), 
    ROLE("ROLE"), 
    PROJECT("PROJECT"), 
    PACKAGE("PACKAGE"), 
    RESOURCE("RESOURCE"), 
    INSTANCE("INSTANCE"), 
    JOB("JOB"), 
    VOLUME("VOLUME"), 
    OfflineModel("OFFLINEMODEL"), 
    SYSTEM("SYSTEM"), 
    GLOBAL("GLOBAL"), 
    XFLOW("XFLOW");
    
    public final String name;
    public final String name_lcase;
    
    private SQLObjectType(final String name) {
        this.name = name;
        this.name_lcase = name.toLowerCase();
    }
}
