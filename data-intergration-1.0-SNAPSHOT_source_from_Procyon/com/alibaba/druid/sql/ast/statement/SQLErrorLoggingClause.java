// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLErrorLoggingClause extends SQLObjectImpl
{
    private SQLName into;
    private SQLExpr simpleExpression;
    private SQLExpr limit;
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.simpleExpression);
            this.acceptChild(visitor, this.limit);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getInto() {
        return this.into;
    }
    
    public void setInto(final SQLName into) {
        this.into = into;
    }
    
    public SQLExpr getSimpleExpression() {
        return this.simpleExpression;
    }
    
    public void setSimpleExpression(final SQLExpr simpleExpression) {
        this.simpleExpression = simpleExpression;
    }
    
    public SQLExpr getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLExpr limit) {
        this.limit = limit;
    }
    
    @Override
    public SQLErrorLoggingClause clone() {
        final SQLErrorLoggingClause x = new SQLErrorLoggingClause();
        if (this.into != null) {
            x.setInto(this.into.clone());
        }
        if (this.simpleExpression != null) {
            x.setSimpleExpression(this.simpleExpression.clone());
        }
        if (this.limit != null) {
            x.setLimit(this.limit.clone());
        }
        return x;
    }
}
