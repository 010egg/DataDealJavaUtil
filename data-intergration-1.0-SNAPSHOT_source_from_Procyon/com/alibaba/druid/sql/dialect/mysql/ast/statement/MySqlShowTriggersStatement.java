// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlShowTriggersStatement extends MySqlStatementImpl implements MySqlShowStatement
{
    private SQLName database;
    private SQLExpr like;
    private SQLExpr where;
    
    public SQLName getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLName database) {
        this.database = database;
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
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.like);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
}
