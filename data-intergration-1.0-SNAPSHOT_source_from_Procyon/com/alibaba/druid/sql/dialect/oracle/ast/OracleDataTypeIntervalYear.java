// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast;

import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;

public class OracleDataTypeIntervalYear extends SQLDataTypeImpl implements OracleSQLObject
{
    public OracleDataTypeIntervalYear() {
        this.setName("INTERVAL YEAR");
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getArguments());
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleDataTypeIntervalYear clone() {
        final OracleDataTypeIntervalYear x = new OracleDataTypeIntervalYear();
        super.cloneTo(x);
        return x;
    }
}
