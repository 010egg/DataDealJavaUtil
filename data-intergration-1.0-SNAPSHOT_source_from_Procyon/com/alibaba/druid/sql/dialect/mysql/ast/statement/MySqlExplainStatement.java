// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExplainStatement;

public class MySqlExplainStatement extends SQLExplainStatement implements MySqlStatement
{
    private boolean describe;
    private SQLName tableName;
    private SQLName columnName;
    private SQLExpr wild;
    private SQLExpr connectionId;
    private boolean distributeInfo;
    
    public MySqlExplainStatement() {
        super(DbType.mysql);
        this.distributeInfo = false;
    }
    
    public MySqlExplainStatement(final DbType dbType) {
        super(dbType);
        this.distributeInfo = false;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.tableName != null) {
                this.tableName.accept(visitor);
                if (this.columnName != null) {
                    this.columnName.accept(visitor);
                }
                else if (this.wild != null) {
                    this.wild.accept(visitor);
                }
            }
            else if (this.connectionId != null) {
                this.connectionId.accept(visitor);
            }
            else if (this.statement != null) {
                this.statement.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    @Override
    public String toString() {
        return SQLUtils.toMySqlString(this);
    }
    
    public boolean isDescribe() {
        return this.describe;
    }
    
    public void setDescribe(final boolean describe) {
        this.describe = describe;
    }
    
    public SQLName getTableName() {
        return this.tableName;
    }
    
    public void setTableName(final SQLName tableName) {
        this.tableName = tableName;
    }
    
    public SQLName getColumnName() {
        return this.columnName;
    }
    
    public void setColumnName(final SQLName columnName) {
        this.columnName = columnName;
    }
    
    public SQLExpr getWild() {
        return this.wild;
    }
    
    public void setWild(final SQLExpr wild) {
        this.wild = wild;
    }
    
    public SQLExpr getConnectionId() {
        return this.connectionId;
    }
    
    public void setConnectionId(final SQLExpr connectionId) {
        this.connectionId = connectionId;
    }
    
    public boolean isDistributeInfo() {
        return this.distributeInfo;
    }
    
    public void setDistributeInfo(final boolean distributeInfo) {
        this.distributeInfo = distributeInfo;
    }
}
