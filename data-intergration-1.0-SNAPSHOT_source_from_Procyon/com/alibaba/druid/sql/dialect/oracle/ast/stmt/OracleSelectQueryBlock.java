// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBase;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.ModelClause;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;

public class OracleSelectQueryBlock extends SQLSelectQueryBlock implements OracleSQLObject
{
    private ModelClause modelClause;
    private boolean skipLocked;
    
    @Override
    public OracleSelectQueryBlock clone() {
        final OracleSelectQueryBlock x = new OracleSelectQueryBlock();
        super.cloneTo(x);
        if (this.modelClause != null) {
            x.setModelClause(this.modelClause.clone());
        }
        if (this.forUpdateOf != null) {
            for (final SQLExpr item : this.forUpdateOf) {
                final SQLExpr item2 = item.clone();
                item2.setParent(x);
                x.getForUpdateOf().add(item2);
            }
        }
        x.skipLocked = this.skipLocked;
        return x;
    }
    
    public OracleSelectQueryBlock() {
        this.skipLocked = false;
        this.dbType = DbType.oracle;
    }
    
    public ModelClause getModelClause() {
        return this.modelClause;
    }
    
    public void setModelClause(final ModelClause modelClause) {
        this.modelClause = modelClause;
    }
    
    @Override
    public boolean isSkipLocked() {
        return this.skipLocked;
    }
    
    @Override
    public void setSkipLocked(final boolean skipLocked) {
        this.skipLocked = skipLocked;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)visitor);
            return;
        }
        super.accept0(visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.hints);
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.startWith);
            this.acceptChild(visitor, this.connectBy);
            this.acceptChild(visitor, this.groupBy);
            this.acceptChild(visitor, this.orderBy);
            this.acceptChild(visitor, this.waitTime);
            this.acceptChild(visitor, this.limit);
            this.acceptChild(visitor, this.modelClause);
            this.acceptChild(visitor, this.forUpdateOf);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
    
    @Override
    public void limit(final int rowCount, final int offset) {
        if (offset <= 0) {
            final SQLExpr rowCountExpr = new SQLIntegerExpr(rowCount);
            final SQLExpr newCondition = SQLUtils.buildCondition(SQLBinaryOperator.BooleanAnd, rowCountExpr, false, this.where);
            this.setWhere(newCondition);
            return;
        }
        throw new UnsupportedOperationException("not support offset");
    }
    
    public void setFrom(final String tableName) {
        SQLExprTableSource from;
        if (tableName == null || tableName.length() == 0) {
            from = null;
        }
        else {
            from = new OracleSelectTableReference(new SQLIdentifierExpr(tableName));
        }
        this.setFrom(from);
    }
}
