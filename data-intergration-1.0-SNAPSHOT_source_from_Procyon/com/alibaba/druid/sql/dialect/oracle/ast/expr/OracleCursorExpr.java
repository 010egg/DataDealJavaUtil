// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class OracleCursorExpr extends SQLExprImpl implements OracleExpr
{
    private SQLSelect query;
    
    public OracleCursorExpr() {
    }
    
    @Override
    public OracleCursorExpr clone() {
        final OracleCursorExpr x = new OracleCursorExpr();
        if (this.query != null) {
            x.setQuery(this.query.clone());
        }
        return x;
    }
    
    public OracleCursorExpr(final SQLSelect query) {
        this.setQuery(query);
    }
    
    public SQLSelect getQuery() {
        return this.query;
    }
    
    public void setQuery(final SQLSelect query) {
        this.query = query;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.query);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.query);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.query == null) ? 0 : this.query.hashCode());
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
        final OracleCursorExpr other = (OracleCursorExpr)obj;
        if (this.query == null) {
            if (other.query != null) {
                return false;
            }
        }
        else if (!this.query.equals(other.query)) {
            return false;
        }
        return true;
    }
}
