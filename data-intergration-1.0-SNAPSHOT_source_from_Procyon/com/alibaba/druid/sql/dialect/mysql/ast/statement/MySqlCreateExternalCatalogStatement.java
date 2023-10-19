// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.HashMap;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class MySqlCreateExternalCatalogStatement extends MySqlStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private boolean ifNotExists;
    private Map<SQLName, SQLName> properties;
    private SQLName comment;
    
    public MySqlCreateExternalCatalogStatement() {
        this.properties = new HashMap<SQLName, SQLName>();
        this.setDbType(DbType.mysql);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public SQLName getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLName comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public Map<SQLName, SQLName> getProperties() {
        return this.properties;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.comment);
        }
        visitor.endVisit(this);
    }
}
