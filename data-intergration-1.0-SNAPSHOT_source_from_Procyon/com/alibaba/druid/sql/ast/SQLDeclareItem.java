// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import java.util.List;

public class SQLDeclareItem extends SQLObjectImpl implements SQLObjectWithDataType, SQLReplaceable
{
    protected Type type;
    protected SQLName name;
    protected SQLDataType dataType;
    protected SQLExpr value;
    protected List<SQLTableElement> tableElementList;
    protected transient SQLObject resolvedObject;
    
    public SQLDeclareItem() {
        this.tableElementList = new ArrayList<SQLTableElement>();
    }
    
    public SQLDeclareItem(final SQLName name, final SQLDataType dataType) {
        this.tableElementList = new ArrayList<SQLTableElement>();
        this.setName(name);
        this.setDataType(dataType);
    }
    
    public SQLDeclareItem(final SQLName name, final SQLDataType dataType, final SQLExpr value) {
        this.tableElementList = new ArrayList<SQLTableElement>();
        this.setName(name);
        this.setDataType(dataType);
        this.setValue(value);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.name == expr) {
            this.setName((SQLName)target);
            return true;
        }
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        return false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.dataType);
            this.acceptChild(visitor, this.value);
            this.acceptChild(visitor, this.tableElementList);
        }
        visitor.endVisit(this);
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
    public SQLDataType getDataType() {
        return this.dataType;
    }
    
    @Override
    public void setDataType(final SQLDataType dataType) {
        if (dataType != null) {
            dataType.setParent(this);
        }
        this.dataType = dataType;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }
    
    public List<SQLTableElement> getTableElementList() {
        return this.tableElementList;
    }
    
    public void setTableElementList(final List<SQLTableElement> tableElementList) {
        this.tableElementList = tableElementList;
    }
    
    public Type getType() {
        return this.type;
    }
    
    public void setType(final Type type) {
        this.type = type;
    }
    
    public SQLObject getResolvedObject() {
        return this.resolvedObject;
    }
    
    public void setResolvedObject(final SQLObject resolvedObject) {
        this.resolvedObject = resolvedObject;
    }
    
    public enum Type
    {
        TABLE, 
        LOCAL, 
        CURSOR;
    }
}
