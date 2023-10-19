// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class OracleSQLObjectImpl extends SQLObjectImpl implements OracleSQLObject
{
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)v);
            return;
        }
        if (v instanceof SQLASTOutputVisitor) {
            ((SQLASTOutputVisitor)v).print(this.toString());
        }
    }
    
    @Override
    public abstract void accept0(final OracleASTVisitor p0);
    
    @Override
    public OracleSQLObject clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    @Override
    public SQLDataType computeDataType() {
        return null;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toOracleString(this);
    }
}
