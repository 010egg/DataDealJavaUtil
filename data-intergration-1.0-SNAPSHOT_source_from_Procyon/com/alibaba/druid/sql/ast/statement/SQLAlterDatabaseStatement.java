// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collections;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterDatabaseStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private boolean upgradeDataDirectoryName;
    private SQLAlterCharacter character;
    private SQLAlterDatabaseItem item;
    private List<SQLAssignItem> properties;
    
    public SQLAlterDatabaseStatement() {
        this.properties = new ArrayList<SQLAssignItem>();
    }
    
    public SQLAlterDatabaseStatement(final DbType dbType) {
        this.properties = new ArrayList<SQLAssignItem>();
        this.setDbType(dbType);
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
    
    public SQLAlterCharacter getCharacter() {
        return this.character;
    }
    
    public void setCharacter(final SQLAlterCharacter character) {
        if (character != null) {
            character.setParent(this);
        }
        this.character = character;
    }
    
    public boolean isUpgradeDataDirectoryName() {
        return this.upgradeDataDirectoryName;
    }
    
    public void setUpgradeDataDirectoryName(final boolean upgradeDataDirectoryName) {
        this.upgradeDataDirectoryName = upgradeDataDirectoryName;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLAlterDatabaseItem getItem() {
        return this.item;
    }
    
    public void setItem(final SQLAlterDatabaseItem item) {
        this.item = item;
    }
    
    public List<SQLAssignItem> getProperties() {
        return this.properties;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.name);
    }
}
