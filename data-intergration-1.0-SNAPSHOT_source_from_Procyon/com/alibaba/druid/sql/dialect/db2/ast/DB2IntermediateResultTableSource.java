// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.ast;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLTableSourceImpl;

public class DB2IntermediateResultTableSource extends SQLTableSourceImpl
{
    @Override
    protected void accept0(final SQLASTVisitor v) {
    }
    
    public enum Type
    {
        OldTable, 
        NewTable, 
        FinalTable;
    }
}
