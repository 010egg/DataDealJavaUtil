// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.statement.SQLTableSource;

public interface OracleSelectTableSource extends SQLTableSource
{
    OracleSelectPivotBase getPivot();
    
    void setPivot(final OracleSelectPivotBase p0);
}
