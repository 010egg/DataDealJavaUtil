// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public abstract class OracleSelectRestriction extends OracleSQLObjectImpl
{
    protected SQLName constraint;
    
    public SQLName getConstraint() {
        return this.constraint;
    }
    
    public void setConstraint(final SQLName constraint) {
        if (constraint != null) {
            constraint.setParent(this);
        }
        this.constraint = constraint;
    }
    
    public static class CheckOption extends OracleSelectRestriction
    {
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.constraint);
            }
            visitor.endVisit(this);
        }
        
        @Override
        public CheckOption clone() {
            final CheckOption x = new CheckOption();
            if (this.constraint != null) {
                x.setConstraint(this.constraint.clone());
            }
            return x;
        }
    }
    
    public static class ReadOnly extends OracleSelectRestriction
    {
        @Override
        public void accept0(final OracleASTVisitor visitor) {
            visitor.visit(this);
            visitor.endVisit(this);
        }
        
        @Override
        public ReadOnly clone() {
            final ReadOnly x = new ReadOnly();
            return x;
        }
    }
}
