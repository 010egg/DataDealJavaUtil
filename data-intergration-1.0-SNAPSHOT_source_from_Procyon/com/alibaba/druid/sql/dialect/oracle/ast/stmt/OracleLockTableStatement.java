// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLLockTableStatement;

public class OracleLockTableStatement extends OracleStatementImpl implements SQLLockTableStatement
{
    private SQLExprTableSource table;
    private LockMode lockMode;
    private boolean noWait;
    private SQLExpr wait;
    private SQLExpr partition;
    
    public OracleLockTableStatement() {
        this.noWait = false;
    }
    
    public boolean isNoWait() {
        return this.noWait;
    }
    
    public void setNoWait(final boolean noWait) {
        this.noWait = noWait;
    }
    
    public SQLExpr getWait() {
        return this.wait;
    }
    
    public void setWait(final SQLExpr wait) {
        this.wait = wait;
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource table) {
        if (table != null) {
            table.setParent(this);
        }
        this.table = table;
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
        this.table.setParent(this);
    }
    
    public LockMode getLockMode() {
        return this.lockMode;
    }
    
    public void setLockMode(final LockMode lockMode) {
        this.lockMode = lockMode;
    }
    
    public SQLExpr getPartition() {
        return this.partition;
    }
    
    public void setPartition(final SQLExpr partition) {
        this.partition = partition;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.wait);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.table != null) {
            children.add(this.table);
        }
        if (this.wait != null) {
            children.add(this.wait);
        }
        if (this.partition != null) {
            children.add(this.partition);
        }
        return children;
    }
    
    public enum LockMode
    {
        ROW_SHARE, 
        ROW_EXCLUSIVE, 
        SHARE_UPDATE, 
        SHARE, 
        SHARE_ROW_EXCLUSIVE, 
        EXCLUSIVE;
        
        @Override
        public String toString() {
            switch (this) {
                case ROW_SHARE: {
                    return "ROW SHARE";
                }
                case ROW_EXCLUSIVE: {
                    return "ROW EXCLUSIVE";
                }
                case SHARE_UPDATE: {
                    return "SHARE UPDATE";
                }
                case SHARE_ROW_EXCLUSIVE: {
                    return "SHARE ROW EXCLUSIVE";
                }
                default: {
                    return this.name();
                }
            }
        }
    }
}
