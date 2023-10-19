// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropSynonymStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    private SQLName name;
    private boolean ifExists;
    private boolean isPublic;
    private boolean force;
    
    public SQLDropSynonymStatement() {
    }
    
    public SQLDropSynonymStatement(final DbType dbType) {
        super(dbType);
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
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public String getSchema() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    public boolean isPublic() {
        return this.isPublic;
    }
    
    public void setPublic(final boolean aPublic) {
        this.isPublic = aPublic;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.name == expr) {
            this.setName((SQLName)target);
            return true;
        }
        return false;
    }
}
