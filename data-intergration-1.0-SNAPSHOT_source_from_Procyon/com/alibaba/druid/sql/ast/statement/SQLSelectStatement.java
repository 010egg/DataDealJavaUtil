// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLSelectStatement extends SQLStatementImpl
{
    protected SQLSelect select;
    
    public SQLSelectStatement() {
    }
    
    public SQLSelectStatement(final DbType dbType) {
        super(dbType);
    }
    
    public SQLSelectStatement(final SQLSelect select) {
        this.setSelect(select);
    }
    
    public SQLSelectStatement(final SQLSelect select, final DbType dbType) {
        this(dbType);
        this.setSelect(select);
    }
    
    public SQLSelect getSelect() {
        return this.select;
    }
    
    public void setSelect(final SQLSelect select) {
        if (select != null) {
            select.setParent(this);
        }
        this.select = select;
    }
    
    @Override
    public void output(final Appendable buf) {
        this.select.output(buf);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.select != null) {
            this.select.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public SQLSelectStatement clone() {
        final SQLSelectStatement x = new SQLSelectStatement();
        x.dbType = this.dbType;
        x.afterSemi = this.afterSemi;
        if (this.select != null) {
            x.setSelect(this.select.clone());
        }
        if (this.headHints != null) {
            for (final SQLCommentHint h : this.headHints) {
                final SQLCommentHint h2 = h.clone();
                h2.setParent(x);
                if (x.headHints == null) {
                    x.headHints = new ArrayList<SQLCommentHint>(this.headHints.size());
                }
                x.headHints.add(h2);
            }
        }
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.select);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLSelectStatement that = (SQLSelectStatement)o;
        return (this.select != null) ? this.select.equals(that.select) : (that.select == null);
    }
    
    @Override
    public int hashCode() {
        return (this.select != null) ? this.select.hashCode() : 0;
    }
    
    public List<String> computeSelecteListAlias() {
        return this.select.computeSelecteListAlias();
    }
    
    public boolean addWhere(final SQLExpr where) {
        return this.select.addWhere(where);
    }
}
