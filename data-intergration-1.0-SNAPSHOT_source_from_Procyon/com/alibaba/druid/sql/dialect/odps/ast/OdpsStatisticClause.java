// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;

public abstract class OdpsStatisticClause extends OdpsObjectImpl
{
    public abstract static class ColumnStatisticClause extends OdpsStatisticClause
    {
        protected SQLName column;
        
        public SQLName getColumn() {
            return this.column;
        }
        
        public void setColumn(final SQLName column) {
            if (column != null) {
                column.setParent(this);
            }
            this.column = column;
        }
    }
    
    public static class NullValue extends ColumnStatisticClause
    {
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.column);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ColumnSum extends ColumnStatisticClause
    {
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.column);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ColumnMin extends ColumnStatisticClause
    {
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.column);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class DistinctValue extends ColumnStatisticClause
    {
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.column);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ColumnMax extends ColumnStatisticClause
    {
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.column);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class ExpressionCondition extends OdpsStatisticClause
    {
        private SQLExpr expr;
        
        public SQLExpr getExpr() {
            return this.expr;
        }
        
        public void setExpr(final SQLExpr expr) {
            if (expr != null) {
                expr.setParent(this);
            }
            this.expr = expr;
        }
        
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.expr);
            }
            visitor.endVisit(this);
        }
    }
    
    public static class TableCount extends OdpsStatisticClause
    {
        @Override
        public void accept0(final OdpsASTVisitor visitor) {
            if (visitor.visit(this)) {}
            visitor.endVisit(this);
        }
    }
}
