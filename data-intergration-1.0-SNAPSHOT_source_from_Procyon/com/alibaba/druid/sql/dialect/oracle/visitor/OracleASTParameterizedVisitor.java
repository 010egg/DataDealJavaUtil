// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import java.util.List;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTParameterizedVisitor;

public class OracleASTParameterizedVisitor extends SQLASTParameterizedVisitor implements OracleASTVisitor
{
    public OracleASTParameterizedVisitor() {
        super(DbType.oracle);
    }
    
    public OracleASTParameterizedVisitor(final List<Object> parameters) {
        super(DbType.oracle, parameters);
    }
}
