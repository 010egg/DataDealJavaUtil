// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public class SQLCurrentTimeExpr extends SQLExprImpl
{
    private final Type type;
    
    public SQLCurrentTimeExpr(final Type type) {
        if (type == null) {
            throw new NullPointerException();
        }
        this.type = type;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        v.visit(this);
        v.endVisit(this);
    }
    
    public Type getType() {
        return this.type;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final SQLCurrentTimeExpr that = (SQLCurrentTimeExpr)o;
        return this.type == that.type;
    }
    
    @Override
    public int hashCode() {
        return this.type.hashCode();
    }
    
    @Override
    public SQLCurrentTimeExpr clone() {
        return new SQLCurrentTimeExpr(this.type);
    }
    
    public enum Type
    {
        CURRENT_TIME("CURRENT_TIME"), 
        CURRENT_DATE("CURRENT_DATE"), 
        CURDATE("CURDATE"), 
        CURTIME("CURTIME"), 
        CURRENT_TIMESTAMP("CURRENT_TIMESTAMP"), 
        LOCALTIME("LOCALTIME"), 
        LOCALTIMESTAMP("LOCALTIMESTAMP"), 
        SYSDATE("SYSDATE");
        
        public final String name;
        public final String name_lower;
        
        private Type(final String name) {
            this.name = name;
            this.name_lower = name.toLowerCase();
        }
    }
}
