// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropIndexStatement extends SQLStatementImpl implements SQLDropStatement, SQLReplaceable
{
    private SQLName indexName;
    private SQLExprTableSource tableName;
    private SQLExpr algorithm;
    private SQLExpr lockOption;
    private boolean ifExists;
    
    public SQLDropIndexStatement() {
    }
    
    public SQLDropIndexStatement(final DbType dbType) {
        super(dbType);
    }
    
    public SQLName getIndexName() {
        return this.indexName;
    }
    
    public void setIndexName(final SQLName indexName) {
        this.indexName = indexName;
    }
    
    public SQLExprTableSource getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final SQLName tableName) {
        this.setTableName(new SQLExprTableSource(tableName));
    }
    
    public void setTableName(final SQLExprTableSource tableName) {
        this.tableName = tableName;
    }
    
    public SQLExpr getAlgorithm() {
        return this.algorithm;
    }
    
    public void setAlgorithm(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.algorithm = x;
    }
    
    public SQLExpr getLockOption() {
        return this.lockOption;
    }
    
    public void setLockOption(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.lockOption = x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.indexName);
            this.acceptChild(visitor, this.tableName);
            this.acceptChild(visitor, this.algorithm);
            this.acceptChild(visitor, this.lockOption);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.indexName != null) {
            children.add(this.indexName);
        }
        if (this.tableName != null) {
            children.add(this.tableName);
        }
        return children;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.indexName == expr) {
            this.setIndexName((SQLName)target);
            return true;
        }
        if (this.algorithm == expr) {
            this.setAlgorithm(target);
            return true;
        }
        if (this.lockOption == expr) {
            this.setLockOption(target);
            return true;
        }
        return false;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
}
