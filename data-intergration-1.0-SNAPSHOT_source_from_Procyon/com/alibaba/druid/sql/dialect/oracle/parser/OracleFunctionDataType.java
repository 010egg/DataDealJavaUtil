// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;

public class OracleFunctionDataType extends SQLDataTypeImpl
{
    private boolean isStatic;
    private final List<SQLParameter> parameters;
    private SQLDataType returnDataType;
    private SQLStatement block;
    
    public OracleFunctionDataType() {
        this.isStatic = false;
        this.parameters = new ArrayList<SQLParameter>();
    }
    
    public boolean isStatic() {
        return this.isStatic;
    }
    
    public void setStatic(final boolean aStatic) {
        this.isStatic = aStatic;
    }
    
    public List<SQLParameter> getParameters() {
        return this.parameters;
    }
    
    public SQLDataType getReturnDataType() {
        return this.returnDataType;
    }
    
    public void setReturnDataType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.returnDataType = x;
    }
    
    @Override
    public OracleFunctionDataType clone() {
        final OracleFunctionDataType x = new OracleFunctionDataType();
        this.cloneTo(x);
        x.isStatic = this.isStatic;
        if (this.returnDataType != null) {
            x.setReturnDataType(this.returnDataType.clone());
        }
        for (final SQLParameter parameter : this.parameters) {
            final SQLParameter p2 = parameter.clone();
            p2.setParent(x);
            x.parameters.add(p2);
        }
        return x;
    }
    
    public SQLStatement getBlock() {
        return this.block;
    }
    
    public void setBlock(final SQLStatement block) {
        if (block != null) {
            block.setParent(this);
        }
        this.block = block;
    }
}
