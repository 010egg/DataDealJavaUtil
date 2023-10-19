// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLPrivilegeItem;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class OdpsAddTableStatement extends OdpsStatementImpl implements SQLAlterStatement
{
    private SQLExprTableSource table;
    private final List<SQLAssignItem> partitions;
    protected SQLExpr comment;
    protected boolean force;
    protected final List<SQLPrivilegeItem> privileges;
    protected SQLName toPackage;
    
    public OdpsAddTableStatement() {
        this.partitions = new ArrayList<SQLAssignItem>();
        this.privileges = new ArrayList<SQLPrivilegeItem>();
        super.dbType = DbType.odps;
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
        }
        visitor.endVisit(this);
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource table) {
        if (table != null) {
            table.setParent(table);
        }
        this.table = table;
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
    }
    
    public SQLExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.comment = x;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public SQLName getToPackage() {
        return this.toPackage;
    }
    
    public void setToPackage(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.toPackage = x;
    }
    
    public List<SQLPrivilegeItem> getPrivileges() {
        return this.privileges;
    }
}
