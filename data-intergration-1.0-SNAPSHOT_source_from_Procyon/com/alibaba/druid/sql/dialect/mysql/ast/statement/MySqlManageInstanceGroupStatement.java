// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class MySqlManageInstanceGroupStatement extends MySqlStatementImpl
{
    private List<SQLExpr> groupNames;
    private SQLIntegerExpr replication;
    private SQLName operation;
    
    public MySqlManageInstanceGroupStatement() {
        this.groupNames = new ArrayList<SQLExpr>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.groupNames);
            this.acceptChild(visitor, this.replication);
            this.acceptChild(visitor, this.operation);
        }
        visitor.endVisit(this);
    }
    
    public List<String> getGroupNamesToString() {
        final List<String> names = new ArrayList<String>(this.groupNames.size());
        for (final SQLExpr groupName : this.groupNames) {
            names.add(groupName.toString());
        }
        return names;
    }
    
    public List<SQLExpr> getGroupNames() {
        return this.groupNames;
    }
    
    public SQLIntegerExpr getReplication() {
        return this.replication;
    }
    
    public void setReplication(final SQLIntegerExpr replication) {
        this.replication = replication;
    }
    
    public SQLName getOperation() {
        return this.operation;
    }
    
    public void setOperation(final SQLName operation) {
        this.operation = operation;
    }
}
