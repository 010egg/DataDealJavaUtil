// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateTableGroupStatement extends SQLStatementImpl implements SQLCreateStatement
{
    protected SQLName name;
    protected boolean ifNotExists;
    protected SQLExpr partitionNum;
    
    public SQLCreateTableGroupStatement() {
        this.ifNotExists = false;
    }
    
    public SQLCreateTableGroupStatement(final DbType dbType) {
        super(dbType);
        this.ifNotExists = false;
    }
    
    public String getSchemaName() {
        if (this.name instanceof SQLPropertyExpr) {
            return SQLUtils.toMySqlString(((SQLPropertyExpr)this.name).getOwner());
        }
        return null;
    }
    
    public void setSchemaName(final String name) {
        if (name != null) {
            this.name = new SQLPropertyExpr(name, this.getTableGroupName());
        }
    }
    
    public String getTableGroupName() {
        if (this.name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)this.name).getName();
        }
        if (this.name instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr)this.name).getName();
        }
        if (this.name instanceof SQLCharExpr) {
            return ((SQLCharExpr)this.name).getText();
        }
        return null;
    }
    
    public SQLExpr getPartitionNum() {
        return this.partitionNum;
    }
    
    public void setPartitionNum(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.partitionNum = x;
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
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
