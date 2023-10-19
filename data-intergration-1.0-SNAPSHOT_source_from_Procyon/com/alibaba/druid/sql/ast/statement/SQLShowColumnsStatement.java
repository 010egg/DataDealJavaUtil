// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowColumnsStatement extends SQLStatementImpl implements SQLShowStatement, SQLReplaceable
{
    private boolean full;
    private SQLName table;
    private SQLName database;
    private SQLExpr like;
    private SQLExpr where;
    
    public boolean isFull() {
        return this.full;
    }
    
    public void setFull(final boolean full) {
        this.full = full;
    }
    
    public SQLName getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName table) {
        if (table instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propExpr = (SQLPropertyExpr)table;
            this.setDatabase((SQLName)propExpr.getOwner());
            this.table = new SQLIdentifierExpr(propExpr.getName());
            return;
        }
        this.table = table;
    }
    
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
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.database);
            this.acceptChild(visitor, this.like);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.table != null) {
            children.add(this.table);
        }
        if (this.database != null) {
            children.add(this.database);
        }
        if (this.like != null) {
            children.add(this.like);
        }
        if (this.where != null) {
            children.add(this.where);
        }
        return children;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.table == expr) {
            this.setTable((SQLName)target);
            return true;
        }
        if (this.database == expr) {
            this.setDatabase((SQLName)target);
            return true;
        }
        if (this.like == expr) {
            this.setLike(target);
            return true;
        }
        if (this.where == expr) {
            this.setWhere(target);
            return true;
        }
        return false;
    }
}
