// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;

public class OracleProcedureDataType extends SQLDataTypeImpl
{
    private boolean isStatic;
    private final List<SQLParameter> parameters;
    private SQLStatement block;
    
    public OracleProcedureDataType() {
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
    
    @Override
    public OracleProcedureDataType clone() {
        final OracleProcedureDataType x = new OracleProcedureDataType();
        this.cloneTo(x);
        x.isStatic = this.isStatic;
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
