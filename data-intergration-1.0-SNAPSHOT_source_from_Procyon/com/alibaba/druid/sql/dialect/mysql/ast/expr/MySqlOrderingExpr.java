// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class MySqlOrderingExpr extends SQLExprImpl implements MySqlExpr, SQLReplaceable
{
    protected SQLExpr expr;
    protected SQLOrderingSpecification type;
    
    public MySqlOrderingExpr() {
    }
    
    public MySqlOrderingExpr(final SQLExpr expr, final SQLOrderingSpecification type) {
        this.setExpr(expr);
        this.type = type;
    }
    
    @Override
    public MySqlOrderingExpr clone() {
        final MySqlOrderingExpr x = new MySqlOrderingExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.type = this.type;
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        final MySqlASTVisitor mysqlVisitor = (MySqlASTVisitor)visitor;
        if (mysqlVisitor.visit(this) && this.expr != null) {
            this.expr.accept(visitor);
        }
        mysqlVisitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return Collections.singletonList(this.expr);
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    public SQLOrderingSpecification getType() {
        return this.type;
    }
    
    public void setType(final SQLOrderingSpecification type) {
        this.type = type;
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
        final MySqlOrderingExpr other = (MySqlOrderingExpr)obj;
        if (this.expr != other.expr) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
        }
        else if (!this.type.equals(other.type)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.expr == null) ? 0 : this.expr.hashCode());
        result = 31 * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }
}
