// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.ast;

import java.util.List;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;

public class SQLServerSelectQueryBlock extends SQLSelectQueryBlock
{
    private SQLServerTop top;
    
    public SQLServerSelectQueryBlock() {
        this.dbType = DbType.sqlserver;
    }
    
    public SQLServerTop getTop() {
        return this.top;
    }
    
    public void setTop(final SQLServerTop top) {
        if (top != null) {
            top.setParent(this);
        }
        this.top = top;
    }
    
    public void setTop(final int rowCount) {
        this.setTop(new SQLServerTop(new SQLIntegerExpr(rowCount)));
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof SQLServerASTVisitor) {
            this.accept0((SQLServerASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final SQLServerASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.top);
            this.acceptChild(visitor, this.selectList);
            this.acceptChild(visitor, this.from);
            this.acceptChild(visitor, this.where);
            this.acceptChild(visitor, this.groupBy);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public void limit(final int rowCount, final int offset) {
        if (offset <= 0) {
            this.setTop(rowCount);
            return;
        }
        throw new UnsupportedOperationException("not support offset");
    }
}
