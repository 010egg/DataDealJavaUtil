// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLImportTableStatement extends SQLStatementImpl
{
    private boolean extenal;
    private SQLExprTableSource table;
    private List<SQLAssignItem> partition;
    private SQLExpr from;
    private SQLExpr location;
    private SQLIntegerExpr version;
    private boolean usingBuild;
    
    public SQLImportTableStatement() {
        this.partition = new ArrayList<SQLAssignItem>();
        this.usingBuild = false;
        this.dbType = DbType.hive;
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    public List<SQLAssignItem> getPartition() {
        return this.partition;
    }
    
    public SQLExpr getFrom() {
        return this.from;
    }
    
    public void setFrom(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.from = x;
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
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.table);
            this.acceptChild(v, this.partition);
            this.acceptChild(v, this.from);
            this.acceptChild(v, this.location);
            this.acceptChild(v, this.version);
        }
        v.endVisit(this);
    }
    
    public SQLIntegerExpr getVersion() {
        return this.version;
    }
    
    public void setVersion(final SQLIntegerExpr version) {
        this.version = version;
    }
    
    public boolean isUsingBuild() {
        return this.usingBuild;
    }
    
    public void setUsingBuild(final boolean usingBuild) {
        this.usingBuild = usingBuild;
    }
    
    public boolean isExtenal() {
        return this.extenal;
    }
    
    public void setExtenal(final boolean extenal) {
        this.extenal = extenal;
    }
}
