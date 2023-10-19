// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterTableGroupStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private List<SQLAssignItem> options;
    
    public SQLAlterTableGroupStatement() {
        this.options = new ArrayList<SQLAssignItem>();
    }
    
    public SQLAlterTableGroupStatement(final DbType dbType) {
        this.options = new ArrayList<SQLAssignItem>();
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
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLAssignItem> getOptions() {
        return this.options;
    }
}
