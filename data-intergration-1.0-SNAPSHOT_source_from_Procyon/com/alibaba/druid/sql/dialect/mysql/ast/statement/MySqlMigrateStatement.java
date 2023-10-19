// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlMigrateStatement extends MySqlStatementImpl
{
    private SQLName schema;
    private SQLCharExpr shardNames;
    private SQLIntegerExpr migrateType;
    private SQLCharExpr fromInsId;
    private SQLCharExpr fromInsIp;
    private SQLIntegerExpr fromInsPort;
    private SQLCharExpr fromInsStatus;
    private SQLCharExpr toInsId;
    private SQLCharExpr toInsIp;
    private SQLIntegerExpr toInsPort;
    private SQLCharExpr toInsStatus;
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.schema);
            this.acceptChild(visitor, this.shardNames);
            this.acceptChild(visitor, this.migrateType);
            this.acceptChild(visitor, this.fromInsId);
            this.acceptChild(visitor, this.fromInsIp);
            this.acceptChild(visitor, this.fromInsPort);
            this.acceptChild(visitor, this.fromInsStatus);
            this.acceptChild(visitor, this.toInsId);
            this.acceptChild(visitor, this.toInsIp);
            this.acceptChild(visitor, this.toInsPort);
            this.acceptChild(visitor, this.toInsStatus);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getSchema() {
        return this.schema;
    }
    
    public void setSchema(final SQLName schema) {
        this.schema = schema;
    }
    
    public SQLCharExpr getShardNames() {
        return this.shardNames;
    }
    
    public void setShardNames(final SQLCharExpr shardNames) {
        this.shardNames = shardNames;
    }
    
    public SQLIntegerExpr getMigrateType() {
        return this.migrateType;
    }
    
    public void setMigrateType(final SQLIntegerExpr migrateType) {
        this.migrateType = migrateType;
    }
    
    public SQLCharExpr getFromInsId() {
        return this.fromInsId;
    }
    
    public void setFromInsId(final SQLCharExpr fromInsId) {
        this.fromInsId = fromInsId;
    }
    
    public SQLCharExpr getFromInsIp() {
        return this.fromInsIp;
    }
    
    public void setFromInsIp(final SQLCharExpr fromInsIp) {
        this.fromInsIp = fromInsIp;
    }
    
    public SQLIntegerExpr getFromInsPort() {
        return this.fromInsPort;
    }
    
    public void setFromInsPort(final SQLIntegerExpr fromInsPort) {
        this.fromInsPort = fromInsPort;
    }
    
    public SQLCharExpr getFromInsStatus() {
        return this.fromInsStatus;
    }
    
    public void setFromInsStatus(final SQLCharExpr fromInsStatus) {
        this.fromInsStatus = fromInsStatus;
    }
    
    public SQLCharExpr getToInsId() {
        return this.toInsId;
    }
    
    public void setToInsId(final SQLCharExpr toInsId) {
        this.toInsId = toInsId;
    }
    
    public SQLCharExpr getToInsIp() {
        return this.toInsIp;
    }
    
    public void setToInsIp(final SQLCharExpr toInsIp) {
        this.toInsIp = toInsIp;
    }
    
    public SQLIntegerExpr getToInsPort() {
        return this.toInsPort;
    }
    
    public void setToInsPort(final SQLIntegerExpr toInsPort) {
        this.toInsPort = toInsPort;
    }
    
    public SQLCharExpr getToInsStatus() {
        return this.toInsStatus;
    }
    
    public void setToInsStatus(final SQLCharExpr toInsStatus) {
        this.toInsStatus = toInsStatus;
    }
}
