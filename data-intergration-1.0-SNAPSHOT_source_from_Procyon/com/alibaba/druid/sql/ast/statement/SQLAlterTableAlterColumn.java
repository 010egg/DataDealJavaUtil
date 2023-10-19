// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLAlterTableAlterColumn extends SQLObjectImpl implements SQLAlterTableItem
{
    private SQLName originColumn;
    private SQLColumnDefinition column;
    private boolean setNotNull;
    private boolean dropNotNull;
    private SQLExpr setDefault;
    private boolean dropDefault;
    private boolean first;
    private SQLName after;
    private SQLDataType dataType;
    private boolean toFirst;
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.column);
            this.acceptChild(visitor, this.setDefault);
        }
        visitor.endVisit(this);
    }
    
    public SQLColumnDefinition getColumn() {
        return this.column;
    }
    
    public void setColumn(final SQLColumnDefinition column) {
        this.column = column;
    }
    
    public boolean isSetNotNull() {
        return this.setNotNull;
    }
    
    public void setSetNotNull(final boolean setNotNull) {
        this.setNotNull = setNotNull;
    }
    
    public boolean isDropNotNull() {
        return this.dropNotNull;
    }
    
    public void setDropNotNull(final boolean dropNotNull) {
        this.dropNotNull = dropNotNull;
    }
    
    public SQLExpr getSetDefault() {
        return this.setDefault;
    }
    
    public void setSetDefault(final SQLExpr setDefault) {
        this.setDefault = setDefault;
    }
    
    public boolean isDropDefault() {
        return this.dropDefault;
    }
    
    public void setDropDefault(final boolean dropDefault) {
        this.dropDefault = dropDefault;
    }
    
    public SQLName getOriginColumn() {
        return this.originColumn;
    }
    
    public void setOriginColumn(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.originColumn = x;
    }
    
    public boolean isFirst() {
        return this.first;
    }
    
    public void setFirst(final boolean x) {
        this.first = x;
    }
    
    public SQLName getAfter() {
        return this.after;
    }
    
    public void setAfter(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.after = x;
    }
    
    public SQLDataType getDataType() {
        return this.dataType;
    }
    
    public void setDataType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dataType = x;
    }
}
