// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.ast;

import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class HiveMultiInsertStatement extends SQLStatementImpl
{
    protected SQLWithSubqueryClause with;
    private SQLTableSource from;
    private List<HiveInsert> items;
    
    public HiveMultiInsertStatement() {
        super(DbType.hive);
        this.items = new ArrayList<HiveInsert>();
    }
    
    public void setFrom(final SQLTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.from = x;
    }
    
    public SQLTableSource getFrom() {
        return this.from;
    }
    
    public List<HiveInsert> getItems() {
        return this.items;
    }
    
    public void addItem(final HiveInsert item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof HiveASTVisitor) {
            this.accept0((HiveASTVisitor)visitor);
        }
        else {
            this.acceptChild(visitor, this.with);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.items);
        }
    }
    
    public void accept0(final HiveASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.with);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.items);
        }
        visitor.endVisit(this);
    }
    
    public SQLWithSubqueryClause getWith() {
        return this.with;
    }
    
    public void setWith(final SQLWithSubqueryClause with) {
        if (with != null) {
            with.setParent(this);
        }
        this.with = with;
    }
}
