// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.SQLUtils;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;

public class OracleSelectJoin extends SQLJoinTableSource implements OracleSelectTableSource
{
    protected OracleSelectPivotBase pivot;
    
    public OracleSelectJoin(final String alias) {
        super(alias);
    }
    
    public OracleSelectJoin() {
    }
    
    public OracleSelectJoin(final SQLTableSource left, final JoinType joinType, final SQLTableSource right, final SQLExpr condition) {
        super(left, joinType, right, condition);
    }
    
    @Override
    public OracleSelectPivotBase getPivot() {
        return this.pivot;
    }
    
    @Override
    public void setPivot(final OracleSelectPivotBase pivot) {
        this.pivot = pivot;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.left);
            this.acceptChild(visitor, this.right);
            this.acceptChild(visitor, this.condition);
            this.acceptChild(visitor, this.using);
            this.acceptChild(visitor, this.flashback);
        }
        visitor.endVisit(this);
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
        final OracleSelectJoin that = (OracleSelectJoin)o;
        if (this.pivot != null) {
            if (this.pivot.equals(that.pivot)) {
                return (this.flashback != null) ? this.flashback.equals(that.flashback) : (that.flashback == null);
            }
        }
        else if (that.pivot == null) {
            return (this.flashback != null) ? this.flashback.equals(that.flashback) : (that.flashback == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.pivot != null) ? this.pivot.hashCode() : 0;
        result = 31 * result + ((this.flashback != null) ? this.flashback.hashCode() : 0);
        return result;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
    
    @Override
    public SQLJoinTableSource clone() {
        final OracleSelectJoin x = new OracleSelectJoin();
        this.cloneTo(x);
        if (this.pivot != null) {
            x.setPivot(this.pivot.clone());
        }
        if (this.flashback != null) {
            x.setFlashback(this.flashback.clone());
        }
        return x;
    }
    
    public void setLeft(final String tableName) {
        SQLExprTableSource tableSource;
        if (tableName == null || tableName.length() == 0) {
            tableSource = null;
        }
        else {
            tableSource = new OracleSelectTableReference(new SQLIdentifierExpr(tableName));
        }
        this.setLeft(tableSource);
    }
    
    public void setRight(final String tableName) {
        SQLExprTableSource tableSource;
        if (tableName == null || tableName.length() == 0) {
            tableSource = null;
        }
        else {
            tableSource = new OracleSelectTableReference(new SQLIdentifierExpr(tableName));
        }
        this.setRight(tableSource);
    }
    
    @Override
    public SQLJoinTableSource join(final SQLTableSource right, final JoinType joinType, final SQLExpr condition) {
        final SQLJoinTableSource joined = new OracleSelectJoin(this, joinType, right, condition);
        return joined;
    }
}
