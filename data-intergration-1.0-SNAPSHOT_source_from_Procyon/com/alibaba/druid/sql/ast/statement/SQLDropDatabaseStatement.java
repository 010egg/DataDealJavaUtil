// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropDatabaseStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    private SQLExpr database;
    private boolean ifExists;
    private Boolean restrict;
    private boolean cascade;
    private boolean physical;
    
    public SQLDropDatabaseStatement() {
    }
    
    public SQLDropDatabaseStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.database);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return (SQLName)this.database;
    }
    
    public String getDatabaseName() {
        if (this.database == null) {
            return null;
        }
        if (this.database instanceof SQLName) {
            return ((SQLName)this.database).getSimpleName();
        }
        return null;
    }
    
    public SQLExpr getDatabase() {
        return this.database;
    }
    
    public void setDatabase(final SQLExpr database) {
        if (database != null) {
            database.setParent(this);
        }
        this.database = database;
    }
    
    public void setDatabase(final String database) {
        final SQLExpr expr = SQLUtils.toSQLExpr(database);
        if (expr instanceof SQLIdentifierExpr && this.database instanceof SQLPropertyExpr) {
            ((SQLPropertyExpr)this.database).setName(database);
            return;
        }
        expr.setParent(this);
        this.database = expr;
    }
    
    public String getServer() {
        if (this.database instanceof SQLPropertyExpr) {
            final SQLExpr owner = ((SQLPropertyExpr)this.database).getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                return ((SQLIdentifierExpr)owner).getName();
            }
            if (owner instanceof SQLPropertyExpr) {
                return ((SQLPropertyExpr)owner).getName();
            }
        }
        return null;
    }
    
    public boolean setServer(final String server) {
        if (this.database == null) {
            return false;
        }
        if (this.database instanceof SQLIdentifierExpr) {
            final SQLPropertyExpr propertyExpr = new SQLPropertyExpr(new SQLIdentifierExpr(server), ((SQLIdentifierExpr)this.database).getName());
            propertyExpr.setParent(this);
            this.database = propertyExpr;
            return true;
        }
        if (this.database instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.database;
            final SQLExpr owner = propertyExpr.getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                propertyExpr.setOwner(new SQLIdentifierExpr(server));
                return true;
            }
            if (owner instanceof SQLPropertyExpr) {
                ((SQLPropertyExpr)owner).setName(server);
                return true;
            }
        }
        return false;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.database != null) {
            children.add(this.database);
        }
        return children;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.database == expr) {
            this.setDatabase(target);
            return true;
        }
        return false;
    }
    
    public Boolean getRestrict() {
        return this.restrict;
    }
    
    public boolean isRestrict() {
        if (this.restrict == null) {
            return !this.cascade;
        }
        return this.restrict;
    }
    
    public void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    public boolean isCascade() {
        return this.cascade;
    }
    
    public void setCascade(final boolean cascade) {
        this.cascade = cascade;
    }
    
    public boolean isPhysical() {
        return this.physical;
    }
    
    public void setPhysical(final boolean physical) {
        this.physical = physical;
    }
}
