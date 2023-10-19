// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowCreateMaterializedViewStatement extends SQLStatementImpl implements SQLShowStatement
{
    private SQLExpr name;
    
    public SQLShowCreateMaterializedViewStatement() {
    }
    
    public SQLShowCreateMaterializedViewStatement(final DbType dbType) {
        super(dbType);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getName() {
        return this.name;
    }
    
    public void setName(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.name != null) {
            children.add(this.name);
        }
        return children;
    }
}
