// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Arrays;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDescribeStatement extends SQLStatementImpl implements SQLReplaceable
{
    protected SQLName object;
    protected SQLName column;
    protected boolean extended;
    protected boolean formatted;
    protected SQLObjectType objectType;
    protected List<SQLExpr> partition;
    
    public SQLDescribeStatement() {
        this.partition = new ArrayList<SQLExpr>();
    }
    
    public SQLName getObject() {
        return this.object;
    }
    
    public void setObject(final SQLName object) {
        this.object = object;
    }
    
    public SQLName getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLName column) {
        if (column != null) {
            column.setParent(this);
        }
        this.column = column;
    }
    
    public List<SQLExpr> getPartition() {
        return this.partition;
    }
    
    public void setPartition(final List<SQLExpr> partition) {
        this.partition = partition;
    }
    
    public SQLObjectType getObjectType() {
        return this.objectType;
    }
    
    public void setObjectType(final SQLObjectType objectType) {
        this.objectType = objectType;
    }
    
    public boolean isExtended() {
        return this.extended;
    }
    
    public void setExtended(final boolean extended) {
        this.extended = extended;
    }
    
    public boolean isFormatted() {
        return this.formatted;
    }
    
    public void setFormatted(final boolean formatted) {
        this.formatted = formatted;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.object);
            this.acceptChild(visitor, this.column);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.object, this.column);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.object == expr) {
            this.setObject((SQLName)target);
            return true;
        }
        if (this.column == expr) {
            this.setColumn((SQLName)target);
            return true;
        }
        return false;
    }
}
