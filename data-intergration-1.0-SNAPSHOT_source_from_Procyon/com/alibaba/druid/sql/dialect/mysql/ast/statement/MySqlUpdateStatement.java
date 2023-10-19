// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;

public class MySqlUpdateStatement extends SQLUpdateStatement implements MySqlStatement
{
    private SQLLimit limit;
    private boolean lowPriority;
    private boolean ignore;
    private boolean commitOnSuccess;
    private boolean rollBackOnFail;
    private boolean queryOnPk;
    private SQLExpr targetAffectRow;
    private boolean forceAllPartitions;
    private SQLName forcePartition;
    protected List<SQLCommentHint> hints;
    
    public MySqlUpdateStatement() {
        super(DbType.mysql);
        this.lowPriority = false;
        this.ignore = false;
        this.commitOnSuccess = false;
        this.rollBackOnFail = false;
        this.queryOnPk = false;
        this.forceAllPartitions = false;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit limit) {
        if (limit != null) {
            limit.setParent(this);
        }
        this.limit = limit;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.tableSource != null) {
                this.tableSource.accept(visitor);
            }
            if (this.from != null) {
                this.from.accept(visitor);
            }
            if (this.items != null) {
                for (int i = 0; i < this.items.size(); ++i) {
                    final SQLUpdateSetItem item = this.items.get(i);
                    if (item != null) {
                        item.accept(visitor);
                    }
                }
            }
            if (this.where != null) {
                this.where.accept(visitor);
            }
            if (this.orderBy != null) {
                this.orderBy.accept(visitor);
            }
            if (this.limit != null) {
                this.limit.accept(visitor);
            }
            if (this.hints != null) {
                for (int i = 0; i < this.hints.size(); ++i) {
                    final SQLCommentHint hint = this.hints.get(i);
                    if (hint != null) {
                        hint.accept(visitor);
                    }
                }
            }
        }
        visitor.endVisit(this);
    }
    
    public boolean isLowPriority() {
        return this.lowPriority;
    }
    
    public void setLowPriority(final boolean lowPriority) {
        this.lowPriority = lowPriority;
    }
    
    public boolean isIgnore() {
        return this.ignore;
    }
    
    public void setIgnore(final boolean ignore) {
        this.ignore = ignore;
    }
    
    public boolean isCommitOnSuccess() {
        return this.commitOnSuccess;
    }
    
    public void setCommitOnSuccess(final boolean commitOnSuccess) {
        this.commitOnSuccess = commitOnSuccess;
    }
    
    public boolean isRollBackOnFail() {
        return this.rollBackOnFail;
    }
    
    public void setRollBackOnFail(final boolean rollBackOnFail) {
        this.rollBackOnFail = rollBackOnFail;
    }
    
    public boolean isQueryOnPk() {
        return this.queryOnPk;
    }
    
    public void setQueryOnPk(final boolean queryOnPk) {
        this.queryOnPk = queryOnPk;
    }
    
    public SQLExpr getTargetAffectRow() {
        return this.targetAffectRow;
    }
    
    public void setTargetAffectRow(final SQLExpr targetAffectRow) {
        if (targetAffectRow != null) {
            targetAffectRow.setParent(this);
        }
        this.targetAffectRow = targetAffectRow;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final MySqlUpdateStatement that = (MySqlUpdateStatement)o;
        if (this.lowPriority != that.lowPriority) {
            return false;
        }
        if (this.ignore != that.ignore) {
            return false;
        }
        if (this.commitOnSuccess != that.commitOnSuccess) {
            return false;
        }
        if (this.rollBackOnFail != that.rollBackOnFail) {
            return false;
        }
        if (this.queryOnPk != that.queryOnPk) {
            return false;
        }
        Label_0139: {
            if (this.hints != null) {
                if (!this.hints.equals(that.hints)) {
                    break Label_0139;
                }
            }
            else if (that.hints == null) {
                break Label_0139;
            }
            return false;
        }
        if (this.limit != null) {
            if (this.limit.equals(that.limit)) {
                return (this.targetAffectRow != null) ? this.targetAffectRow.equals(that.targetAffectRow) : (that.targetAffectRow == null);
            }
        }
        else if (that.limit == null) {
            return (this.targetAffectRow != null) ? this.targetAffectRow.equals(that.targetAffectRow) : (that.targetAffectRow == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.limit != null) ? this.limit.hashCode() : 0);
        result = 31 * result + (this.lowPriority ? 1 : 0);
        result = 31 * result + (this.ignore ? 1 : 0);
        result = 31 * result + (this.commitOnSuccess ? 1 : 0);
        result = 31 * result + (this.rollBackOnFail ? 1 : 0);
        result = 31 * result + (this.queryOnPk ? 1 : 0);
        result = 31 * result + ((this.targetAffectRow != null) ? this.targetAffectRow.hashCode() : 0);
        result = 31 * result + ((this.hints != null) ? this.hints.hashCode() : 0);
        return result;
    }
    
    public SQLName getForcePartition() {
        return this.forcePartition;
    }
    
    public void setForcePartition(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.forcePartition = x;
    }
    
    public boolean isForceAllPartitions() {
        return this.forceAllPartitions;
    }
    
    public void setForceAllPartitions(final boolean forceAllPartitions) {
        this.forceAllPartitions = forceAllPartitions;
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    public List<SQLCommentHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLCommentHint>(2);
        }
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
}
