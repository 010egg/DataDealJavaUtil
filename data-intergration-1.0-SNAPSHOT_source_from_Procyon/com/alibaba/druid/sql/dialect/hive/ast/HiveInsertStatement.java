// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.ast;

import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;

public class HiveInsertStatement extends SQLInsertStatement implements SQLStatement
{
    private boolean ifNotExists;
    
    public HiveInsertStatement() {
        this.ifNotExists = false;
        this.dbType = DbType.hive;
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    @Override
    public HiveInsertStatement clone() {
        final HiveInsertStatement x = new HiveInsertStatement();
        super.cloneTo(x);
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof OdpsASTVisitor) {
            this.accept0((OdpsASTVisitor)visitor);
        }
        else if (visitor instanceof HiveASTVisitor) {
            this.accept0((HiveASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final HiveASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
        }
        visitor.endVisit(this);
    }
    
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLCommentHint> getHeadHintsDirect() {
        return null;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
