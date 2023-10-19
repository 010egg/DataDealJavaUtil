// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropViewStatement extends SQLStatementImpl implements SQLDropStatement
{
    protected List<SQLExprTableSource> tableSources;
    protected boolean cascade;
    protected boolean restrict;
    protected boolean ifExists;
    
    public SQLDropViewStatement() {
        this.tableSources = new ArrayList<SQLExprTableSource>();
        this.cascade = false;
        this.restrict = false;
        this.ifExists = false;
    }
    
    public SQLDropViewStatement(final DbType dbType) {
        super(dbType);
        this.tableSources = new ArrayList<SQLExprTableSource>();
        this.cascade = false;
        this.restrict = false;
        this.ifExists = false;
    }
    
    public SQLDropViewStatement(final SQLName name) {
        this(new SQLExprTableSource(name));
    }
    
    public SQLDropViewStatement(final SQLExprTableSource tableSource) {
        this.tableSources = new ArrayList<SQLExprTableSource>();
        this.cascade = false;
        this.restrict = false;
        this.ifExists = false;
        this.tableSources.add(tableSource);
    }
    
    public List<SQLExprTableSource> getTableSources() {
        return this.tableSources;
    }
    
    public void addPartition(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSources.add(tableSource);
    }
    
    public void setName(final SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }
    
    public void addTableSource(final SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }
    
    public void addTableSource(final SQLExprTableSource tableSource) {
        this.tableSources.add(tableSource);
    }
    
    public boolean isCascade() {
        return this.cascade;
    }
    
    public void setCascade(final boolean cascade) {
        this.cascade = cascade;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            for (int i = 0; i < this.tableSources.size(); ++i) {
                final SQLExprTableSource item = this.tableSources.get(i);
                if (item != null) {
                    item.accept(visitor);
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isRestrict() {
        return this.restrict;
    }
    
    public void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    @Override
    public List getChildren() {
        return this.tableSources;
    }
}
