// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.antspark.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.dialect.antspark.visitor.AntsparkVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;

public class AntsparkCreateTableStatement extends SQLCreateTableStatement
{
    protected List<SQLAssignItem> mappedBy;
    protected List<SQLExpr> skewedBy;
    protected List<SQLExpr> skewedByOn;
    protected Map<String, SQLObject> serdeProperties;
    protected SQLExpr metaLifeCycle;
    protected SQLExprTableSource datasource;
    
    public AntsparkCreateTableStatement() {
        super(DbType.antspark);
        this.mappedBy = new ArrayList<SQLAssignItem>(1);
        this.skewedBy = new ArrayList<SQLExpr>();
        this.skewedByOn = new ArrayList<SQLExpr>();
        this.serdeProperties = new LinkedHashMap<String, SQLObject>();
    }
    
    public List<SQLAssignItem> getMappedBy() {
        return this.mappedBy;
    }
    
    public List<SQLExpr> getSkewedBy() {
        return this.skewedBy;
    }
    
    public void addSkewedBy(final SQLExpr item) {
        item.setParent(this);
        this.skewedBy.add(item);
    }
    
    public List<SQLExpr> getSkewedByOn() {
        return this.skewedByOn;
    }
    
    public void addSkewedByOn(final SQLExpr item) {
        item.setParent(this);
        this.skewedByOn.add(item);
    }
    
    public Map<String, SQLObject> getSerdeProperties() {
        return this.serdeProperties;
    }
    
    public SQLExpr getMetaLifeCycle() {
        return this.metaLifeCycle;
    }
    
    public void setMetaLifeCycle(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.metaLifeCycle = x;
    }
    
    public void setDatasource(final SQLExpr datasource) {
        this.datasource = new SQLExprTableSource(datasource);
    }
    
    public SQLExprTableSource getDatasource() {
        return this.datasource;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v instanceof AntsparkVisitor) {
            this.accept0((AntsparkVisitor)v);
            return;
        }
        super.accept0(v);
    }
    
    protected void accept0(final AntsparkVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v);
        }
        v.endVisit(this);
    }
    
    @Override
    protected void acceptChild(final SQLASTVisitor v) {
        super.acceptChild(v);
        this.acceptChild(v, this.datasource);
        this.acceptChild(v, this.skewedBy);
        this.acceptChild(v, this.skewedByOn);
        for (final SQLObject item : this.serdeProperties.values()) {
            this.acceptChild(v, item);
        }
        this.acceptChild(v, this.metaLifeCycle);
    }
}
