// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public abstract class OracleSelectPivotBase extends OracleSQLObjectImpl
{
    protected final List<SQLExpr> pivotFor;
    
    public OracleSelectPivotBase() {
        this.pivotFor = new ArrayList<SQLExpr>();
    }
    
    public List<SQLExpr> getPivotFor() {
        return this.pivotFor;
    }
    
    @Override
    public OracleSelectPivotBase clone() {
        throw new UnsupportedOperationException();
    }
}
