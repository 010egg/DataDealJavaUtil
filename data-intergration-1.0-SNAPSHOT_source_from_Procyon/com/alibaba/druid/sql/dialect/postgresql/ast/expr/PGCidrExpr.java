// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class PGCidrExpr extends PGExprImpl implements SQLReplaceable
{
    private SQLExpr value;
    
    @Override
    public PGCidrExpr clone() {
        final PGCidrExpr x = new PGCidrExpr();
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        return x;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr value) {
        this.value = value;
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.value);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.value);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.value == null) ? 0 : this.value.hashCode());
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
        final PGCidrExpr other = (PGCidrExpr)obj;
        if (this.value == null) {
            if (other.value != null) {
                return false;
            }
        }
        else if (!this.value.equals(other.value)) {
            return false;
        }
        return true;
    }
}
