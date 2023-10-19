// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;

public class HiveCreateFunctionStatement extends SQLCreateFunctionStatement implements SQLCreateStatement
{
    protected boolean declare;
    protected SQLExpr className;
    protected SQLExpr location;
    protected SQLExpr symbol;
    protected ResourceType resourceType;
    protected String code;
    
    public HiveCreateFunctionStatement() {
        this.declare = false;
    }
    
    @Override
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof HiveASTVisitor) {
            this.accept0((HiveASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final HiveASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.className);
            this.acceptChild(visitor, this.location);
            this.acceptChild(visitor, this.symbol);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getClassName() {
        return this.className;
    }
    
    public void setClassName(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.className = x;
    }
    
    public SQLExpr getLocation() {
        return this.location;
    }
    
    public void setLocation(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.location = x;
    }
    
    public SQLExpr getSymbol() {
        return this.symbol;
    }
    
    public void setSymbol(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.symbol = x;
    }
    
    public ResourceType getResourceType() {
        return this.resourceType;
    }
    
    public void setResourceType(final ResourceType resourceType) {
        this.resourceType = resourceType;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public void setCode(final String code) {
        this.code = code;
    }
    
    public boolean isDeclare() {
        return this.declare;
    }
    
    public void setDeclare(final boolean declare) {
        this.declare = declare;
    }
    
    public enum ResourceType
    {
        JAR, 
        FILE, 
        ARCHIVE, 
        CODE;
    }
}
