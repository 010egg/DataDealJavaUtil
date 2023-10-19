// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLArchiveTableStatement extends SQLStatementImpl
{
    private SQLName table;
    private SQLName type;
    private List<SQLIntegerExpr> spIdList;
    private List<SQLIntegerExpr> pIdList;
    
    public SQLArchiveTableStatement() {
        this.spIdList = new ArrayList<SQLIntegerExpr>();
        this.pIdList = new ArrayList<SQLIntegerExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.type);
            this.acceptChild(visitor, this.spIdList);
            this.acceptChild(visitor, this.pIdList);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName table) {
        if (table != null) {
            table.setParent(this);
        }
        this.table = table;
    }
    
    public SQLName getType() {
        return this.type;
    }
    
    public void setType(final SQLName type) {
        if (type != null) {
            type.setParent(this);
        }
        this.type = type;
    }
    
    public List<SQLIntegerExpr> getSpIdList() {
        return this.spIdList;
    }
    
    public List<SQLIntegerExpr> getpIdList() {
        return this.pIdList;
    }
}
