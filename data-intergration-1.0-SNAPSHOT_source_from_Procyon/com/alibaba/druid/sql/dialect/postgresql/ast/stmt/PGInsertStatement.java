// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;

public class PGInsertStatement extends SQLInsertStatement implements PGSQLStatement
{
    private List<ValuesClause> valuesList;
    private SQLExpr returning;
    private boolean defaultValues;
    private List<SQLExpr> onConflictTarget;
    private SQLName onConflictConstraint;
    private SQLExpr onConflictWhere;
    private SQLExpr onConflictUpdateWhere;
    private boolean onConflictDoNothing;
    private List<SQLUpdateSetItem> onConflictUpdateSetItems;
    
    public PGInsertStatement() {
        this.valuesList = new ArrayList<ValuesClause>();
        this.defaultValues = false;
        this.dbType = DbType.postgresql;
    }
    
    public void cloneTo(final PGInsertStatement x) {
        super.cloneTo(x);
        for (final ValuesClause v : this.valuesList) {
            final ValuesClause v2 = v.clone();
            v2.setParent(x);
            x.valuesList.add(v2);
        }
        if (this.returning != null) {
            x.setReturning(this.returning.clone());
        }
        x.defaultValues = this.defaultValues;
    }
    
    public SQLExpr getReturning() {
        return this.returning;
    }
    
    public void setReturning(final SQLExpr returning) {
        this.returning = returning;
    }
    
    @Override
    public ValuesClause getValues() {
        if (this.valuesList.size() == 0) {
            return null;
        }
        return this.valuesList.get(0);
    }
    
    @Override
    public void setValues(final ValuesClause values) {
        if (this.valuesList.size() == 0) {
            this.valuesList.add(values);
        }
        else {
            this.valuesList.set(0, values);
        }
    }
    
    @Override
    public List<ValuesClause> getValuesList() {
        return this.valuesList;
    }
    
    @Override
    public void addValueCause(final ValuesClause valueClause) {
        valueClause.setParent(this);
        this.valuesList.add(valueClause);
    }
    
    public boolean isDefaultValues() {
        return this.defaultValues;
    }
    
    public void setDefaultValues(final boolean defaultValues) {
        this.defaultValues = defaultValues;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof PGASTVisitor) {
            this.accept0((PGASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.with);
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
            this.acceptChild(visitor, this.returning);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public PGInsertStatement clone() {
        final PGInsertStatement x = new PGInsertStatement();
        this.cloneTo(x);
        return x;
    }
    
    public List<SQLExpr> getOnConflictTarget() {
        return this.onConflictTarget;
    }
    
    public void setOnConflictTarget(final List<SQLExpr> onConflictTarget) {
        this.onConflictTarget = onConflictTarget;
    }
    
    public boolean isOnConflictDoNothing() {
        return this.onConflictDoNothing;
    }
    
    public void setOnConflictDoNothing(final boolean onConflictDoNothing) {
        this.onConflictDoNothing = onConflictDoNothing;
    }
    
    public List<SQLUpdateSetItem> getOnConflictUpdateSetItems() {
        return this.onConflictUpdateSetItems;
    }
    
    public void addConflicUpdateItem(final SQLUpdateSetItem item) {
        if (this.onConflictUpdateSetItems == null) {
            this.onConflictUpdateSetItems = new ArrayList<SQLUpdateSetItem>();
        }
        item.setParent(this);
        this.onConflictUpdateSetItems.add(item);
    }
    
    public SQLName getOnConflictConstraint() {
        return this.onConflictConstraint;
    }
    
    public void setOnConflictConstraint(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.onConflictConstraint = x;
    }
    
    public SQLExpr getOnConflictWhere() {
        return this.onConflictWhere;
    }
    
    public void setOnConflictWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.onConflictWhere = x;
    }
    
    public SQLExpr getOnConflictUpdateWhere() {
        return this.onConflictUpdateWhere;
    }
    
    public void setOnConflictUpdateWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.onConflictUpdateWhere = x;
    }
    
    @Override
    public List<SQLCommentHint> getHeadHintsDirect() {
        return null;
    }
}
