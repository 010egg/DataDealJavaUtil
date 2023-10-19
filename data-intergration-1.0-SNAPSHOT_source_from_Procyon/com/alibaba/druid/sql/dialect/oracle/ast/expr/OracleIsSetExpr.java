// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class OracleIsSetExpr extends SQLExprImpl implements OracleExpr, SQLReplaceable
{
    private SQLExpr nestedTable;
    
    public OracleIsSetExpr() {
    }
    
    public OracleIsSetExpr(final SQLExpr nestedTable) {
        this.nestedTable = nestedTable;
    }
    
    @Override
    public OracleIsSetExpr clone() {
        final OracleIsSetExpr x = new OracleIsSetExpr();
        if (this.nestedTable != null) {
            x.setNestedTable(this.nestedTable.clone());
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.nestedTable == expr) {
            this.setNestedTable(target);
            return true;
        }
        return false;
    }
    
    public SQLExpr getNestedTable() {
        return this.nestedTable;
    }
    
    public void setNestedTable(final SQLExpr nestedTable) {
        if (nestedTable != null) {
            nestedTable.setParent(this);
        }
        this.nestedTable = nestedTable;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.nestedTable);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.nestedTable);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.nestedTable == null) ? 0 : this.nestedTable.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final OracleIsSetExpr other = (OracleIsSetExpr)obj;
        if (this.nestedTable == null) {
            if (other.nestedTable != null) {
                return false;
            }
        }
        else if (!this.nestedTable.equals(other.nestedTable)) {
            return false;
        }
        return true;
    }
}
