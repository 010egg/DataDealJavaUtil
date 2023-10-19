// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCopyFromStatement extends SQLStatementImpl
{
    private SQLExprTableSource table;
    private final List<SQLName> columns;
    private SQLExpr from;
    private SQLExpr accessKeyId;
    private SQLExpr accessKeySecret;
    private final List<SQLAssignItem> options;
    private final List<SQLAssignItem> partitions;
    
    public SQLCopyFromStatement() {
        this.columns = new ArrayList<SQLName>();
        this.options = new ArrayList<SQLAssignItem>();
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.table);
            this.acceptChild(v, this.columns);
            this.acceptChild(v, this.partitions);
            this.acceptChild(v, this.from);
            this.acceptChild(v, this.options);
        }
        v.endVisit(this);
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
    
    public List<SQLName> getColumns() {
        return this.columns;
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
    
    public SQLExpr getAccessKeyId() {
        return this.accessKeyId;
    }
    
    public void setAccessKeyId(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.accessKeyId = x;
    }
    
    public SQLExpr getAccessKeySecret() {
        return this.accessKeySecret;
    }
    
    public void setAccessKeySecret(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.accessKeySecret = x;
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.options;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
}
