// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropTableGroupStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    protected SQLName name;
    protected boolean ifExists;
    
    public SQLDropTableGroupStatement() {
        this.ifExists = false;
    }
    
    public SQLDropTableGroupStatement(final DbType dbType) {
        super(dbType);
        this.ifExists = false;
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
    
    public String getTableGroupName() {
        if (this.name == null) {
            return null;
        }
        if (this.name instanceof SQLName) {
            return this.name.getSimpleName();
        }
        return null;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifNotExists) {
        this.ifExists = ifNotExists;
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
