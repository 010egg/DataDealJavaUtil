// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLFetchStatement extends SQLStatementImpl implements SQLReplaceable
{
    private SQLName cursorName;
    private boolean bulkCollect;
    private List<SQLExpr> into;
    private SQLLimit limit;
    
    public SQLFetchStatement() {
        this.into = new ArrayList<SQLExpr>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.cursorName);
            this.acceptChild(visitor, this.into);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getCursorName() {
        return this.cursorName;
    }
    
    public void setCursorName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.cursorName = x;
    }
    
    public SQLLimit getLimit() {
        return this.limit;
    }
    
    public void setLimit(final SQLLimit x) {
        if (x != null) {
            x.setParent(this);
        }
        this.limit = x;
    }
    
    public List<SQLExpr> getInto() {
        return this.into;
    }
    
    public void setInto(final List<SQLExpr> into) {
        this.into = into;
    }
    
    public boolean isBulkCollect() {
        return this.bulkCollect;
    }
    
    public void setBulkCollect(final boolean bulkCollect) {
        this.bulkCollect = bulkCollect;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.cursorName == expr) {
            this.setCursorName((SQLName)target);
            return true;
        }
        return false;
    }
}
