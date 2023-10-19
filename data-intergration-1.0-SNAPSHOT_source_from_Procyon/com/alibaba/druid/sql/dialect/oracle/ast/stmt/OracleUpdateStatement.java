// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;

public class OracleUpdateStatement extends SQLUpdateStatement implements OracleStatement, SQLReplaceable
{
    private List<SQLHint> hints;
    private boolean only;
    private String alias;
    private final List<SQLExpr> returningInto;
    
    public OracleUpdateStatement() {
        super(DbType.oracle);
        this.hints = new ArrayList<SQLHint>(1);
        this.only = false;
        this.returningInto = new ArrayList<SQLExpr>();
    }
    
    public List<SQLExpr> getReturningInto() {
        return this.returningInto;
    }
    
    public void addReturningInto(final SQLExpr returningInto) {
        if (returningInto == null) {
            return;
        }
        returningInto.setParent(this);
        this.returningInto.add(returningInto);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)visitor);
            return;
        }
        super.accept(visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.hints);
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.returning);
            this.acceptChild(visitor, this.returningInto);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        final boolean replace = super.replace(expr, target);
        if (replace) {
            return true;
        }
        for (int i = this.returningInto.size() - 1; i >= 0; --i) {
            if (this.returningInto.get(i) == expr) {
                target.setParent(this);
                this.returningInto.set(i, target);
                return true;
            }
        }
        return false;
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public void setAlias(final String alias) {
        this.alias = alias;
    }
    
    public boolean isOnly() {
        return this.only;
    }
    
    public void setOnly(final boolean only) {
        this.only = only;
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public List<SQLHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLHint>(2);
        }
        return this.hints;
    }
    
    public void setHints(final List<SQLHint> hints) {
        this.hints = hints;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final OracleUpdateStatement that = (OracleUpdateStatement)o;
        Label_0062: {
            if (this.with != null) {
                if (this.with.equals(that.getWith())) {
                    break Label_0062;
                }
            }
            else if (that.getWith() == null) {
                break Label_0062;
            }
            return false;
        }
        if (!this.items.equals(that.getItems())) {
            return false;
        }
        Label_0113: {
            if (this.where != null) {
                if (this.where.equals(that.getWhere())) {
                    break Label_0113;
                }
            }
            else if (that.getWhere() == null) {
                break Label_0113;
            }
            return false;
        }
        Label_0146: {
            if (this.from != null) {
                if (this.from.equals(that.getFrom())) {
                    break Label_0146;
                }
            }
            else if (that.getFrom() == null) {
                break Label_0146;
            }
            return false;
        }
        Label_0181: {
            if (this.hints != null) {
                if (this.hints.equals(that.hints)) {
                    break Label_0181;
                }
            }
            else if (that.hints == null) {
                break Label_0181;
            }
            return false;
        }
        Label_0214: {
            if (this.tableSource != null) {
                if (this.tableSource.equals(that.tableSource)) {
                    break Label_0214;
                }
            }
            else if (that.tableSource == null) {
                break Label_0214;
            }
            return false;
        }
        if (this.returning != null) {
            if (this.returning.equals(that.returning)) {
                return (this.orderBy != null) ? this.orderBy.equals(that.orderBy) : (that.orderBy == null);
            }
        }
        else if (that.returning == null) {
            return (this.orderBy != null) ? this.orderBy.equals(that.orderBy) : (that.orderBy == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.with != null) ? this.with.hashCode() : 0;
        result = 31 * result + this.items.hashCode();
        result = 31 * result + ((this.where != null) ? this.where.hashCode() : 0);
        result = 31 * result + ((this.from != null) ? this.from.hashCode() : 0);
        result = 31 * result + ((this.tableSource != null) ? this.tableSource.hashCode() : 0);
        result = 31 * result + ((this.returning != null) ? this.returning.hashCode() : 0);
        result = 31 * result + ((this.orderBy != null) ? this.orderBy.hashCode() : 0);
        result = 31 * result + ((this.hints != null) ? this.hints.hashCode() : 0);
        return result;
    }
}
