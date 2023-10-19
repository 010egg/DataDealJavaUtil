// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateDatabaseStatement extends SQLStatementImpl implements SQLCreateStatement
{
    protected SQLName name;
    protected String characterSet;
    protected String collate;
    protected List<SQLCommentHint> hints;
    protected boolean ifNotExists;
    protected SQLExpr comment;
    protected SQLExpr location;
    protected final List<SQLAssignItem> dbProperties;
    protected Map<String, SQLExpr> options;
    protected String user;
    protected SQLExpr password;
    protected final List<SQLAssignItem> storedOn;
    protected final List<List<SQLAssignItem>> storedBy;
    protected SQLExpr storedAs;
    protected SQLExpr storedIn;
    protected boolean physical;
    
    public SQLCreateDatabaseStatement() {
        this.ifNotExists = false;
        this.dbProperties = new ArrayList<SQLAssignItem>();
        this.options = new HashMap<String, SQLExpr>();
        this.storedOn = new ArrayList<SQLAssignItem>();
        this.storedBy = new ArrayList<List<SQLAssignItem>>();
    }
    
    public SQLCreateDatabaseStatement(final DbType dbType) {
        super(dbType);
        this.ifNotExists = false;
        this.dbProperties = new ArrayList<SQLAssignItem>();
        this.options = new HashMap<String, SQLExpr>();
        this.storedOn = new ArrayList<SQLAssignItem>();
        this.storedBy = new ArrayList<List<SQLAssignItem>>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.name != null) {
            children.add(this.name);
        }
        return children;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public String getCharacterSet() {
        return this.characterSet;
    }
    
    public void setCharacterSet(final String characterSet) {
        this.characterSet = characterSet;
    }
    
    public String getCollate() {
        return this.collate;
    }
    
    public void setCollate(final String collate) {
        this.collate = collate;
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public String getUser() {
        return this.user;
    }
    
    public void setUser(final String user) {
        this.user = user;
    }
    
    public void setComment(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }
    
    public SQLExpr getLocation() {
        return this.location;
    }
    
    public Map<String, SQLExpr> getOptions() {
        return this.options;
    }
    
    public void setLocation(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.location = x;
    }
    
    public List<SQLAssignItem> getDbProperties() {
        return this.dbProperties;
    }
    
    public void setOptions(final Map<String, SQLExpr> options) {
        this.options = options;
    }
    
    public List<SQLAssignItem> getStoredOn() {
        return this.storedOn;
    }
    
    public List<List<SQLAssignItem>> getStoredBy() {
        return this.storedBy;
    }
    
    public SQLExpr getStoredAs() {
        return this.storedAs;
    }
    
    public void setStoredAs(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedAs = x;
    }
    
    public SQLExpr getStoredIn() {
        return this.storedIn;
    }
    
    public void setStoredIn(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedIn = x;
    }
    
    public SQLExpr getPassword() {
        return this.password;
    }
    
    public void setPassword(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.password = x;
    }
    
    public String getDatabaseName() {
        if (this.name == null) {
            return null;
        }
        if (this.name instanceof SQLName) {
            return this.name.getSimpleName();
        }
        return null;
    }
    
    public void setDatabase(final String database) {
        final SQLExpr expr = SQLUtils.toSQLExpr(database);
        if (expr instanceof SQLIdentifierExpr && this.name instanceof SQLPropertyExpr) {
            ((SQLPropertyExpr)this.name).setName(database);
            return;
        }
        expr.setParent(this);
        this.name = (SQLName)expr;
    }
    
    public String getServer() {
        if (this.name instanceof SQLPropertyExpr) {
            final SQLExpr owner = ((SQLPropertyExpr)this.name).getOwner();
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
        if (this.name == null) {
            return false;
        }
        if (this.name instanceof SQLIdentifierExpr) {
            final SQLPropertyExpr propertyExpr = new SQLPropertyExpr(new SQLIdentifierExpr(server), ((SQLIdentifierExpr)this.name).getName());
            propertyExpr.setParent(this);
            this.name = propertyExpr;
            return true;
        }
        if (this.name instanceof SQLPropertyExpr) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)this.name;
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
    
    public boolean isPhysical() {
        return this.physical;
    }
    
    public void setPhysical(final boolean physical) {
        this.physical = physical;
    }
}
