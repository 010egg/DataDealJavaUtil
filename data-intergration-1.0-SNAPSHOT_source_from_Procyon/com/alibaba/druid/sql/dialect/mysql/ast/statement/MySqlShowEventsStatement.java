// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;

public class MySqlShowEventsStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLExpr schema;
    private SQLExpr like;
    private SQLExpr where;
    
    public SQLExpr getSchema() {
        return this.schema;
    }
    
    public void setSchema(final SQLExpr schema) {
        this.schema = schema;
    }
    
    public SQLExpr getLike() {
        return this.like;
    }
    
    public void setLike(final SQLExpr like) {
        this.like = like;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.schema);
            this.acceptChild(visitor, this.like);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
}
