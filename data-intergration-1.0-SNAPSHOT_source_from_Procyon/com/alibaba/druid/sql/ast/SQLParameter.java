// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import java.util.List;

public final class SQLParameter extends SQLObjectImpl implements SQLObjectWithDataType
{
    private SQLName name;
    private SQLDataType dataType;
    private SQLExpr defaultValue;
    private ParameterType paramType;
    private boolean noCopy;
    private boolean constant;
    private SQLName cursorName;
    private final List<SQLParameter> cursorParameters;
    private boolean order;
    private boolean map;
    private boolean member;
    
    public SQLParameter() {
        this.noCopy = false;
        this.constant = false;
        this.cursorParameters = new ArrayList<SQLParameter>();
    }
    
    public SQLExpr getDefaultValue() {
        return this.defaultValue;
    }
    
    public void setDefaultValue(final SQLExpr deaultValue) {
        if (deaultValue != null) {
            deaultValue.setParent(this);
        }
        this.defaultValue = deaultValue;
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
    
    public ParameterType getParamType() {
        return this.paramType;
    }
    
    public void setParamType(final ParameterType paramType) {
        this.paramType = paramType;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.dataType);
            this.acceptChild(visitor, this.defaultValue);
        }
        visitor.endVisit(this);
    }
    
    public boolean isNoCopy() {
        return this.noCopy;
    }
    
    public void setNoCopy(final boolean noCopy) {
        this.noCopy = noCopy;
    }
    
    public boolean isConstant() {
        return this.constant;
    }
    
    public void setConstant(final boolean constant) {
        this.constant = constant;
    }
    
    public List<SQLParameter> getCursorParameters() {
        return this.cursorParameters;
    }
    
    public SQLName getCursorName() {
        return this.cursorName;
    }
    
    public void setCursorName(final SQLName cursorName) {
        if (cursorName != null) {
            cursorName.setParent(this);
        }
        this.cursorName = cursorName;
    }
    
    @Override
    public SQLParameter clone() {
        final SQLParameter x = new SQLParameter();
        if (this.name != null) {
            x.setName(this.name.clone());
        }
        if (this.dataType != null) {
            x.setDataType(this.dataType.clone());
        }
        if (this.defaultValue != null) {
            x.setDefaultValue(this.defaultValue.clone());
        }
        x.paramType = this.paramType;
        x.noCopy = this.noCopy;
        x.constant = this.constant;
        x.order = this.order;
        x.map = this.map;
        if (this.cursorName != null) {
            x.setCursorName(this.cursorName.clone());
        }
        for (final SQLParameter p : this.cursorParameters) {
            final SQLParameter p2 = p.clone();
            p2.setParent(x);
            x.cursorParameters.add(p2);
        }
        return x;
    }
    
    public boolean isOrder() {
        return this.order;
    }
    
    public void setOrder(final boolean order) {
        this.order = order;
    }
    
    public boolean isMap() {
        return this.map;
    }
    
    public void setMap(final boolean map) {
        this.map = map;
    }
    
    public boolean isMember() {
        return this.member;
    }
    
    public void setMember(final boolean member) {
        this.member = member;
    }
    
    public enum ParameterType
    {
        DEFAULT, 
        IN, 
        OUT, 
        INOUT;
    }
}
