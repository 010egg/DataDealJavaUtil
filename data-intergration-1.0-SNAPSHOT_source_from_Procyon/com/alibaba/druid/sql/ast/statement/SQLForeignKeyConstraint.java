// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;

public interface SQLForeignKeyConstraint extends SQLConstraint, SQLTableElement, SQLTableConstraint
{
    List<SQLName> getReferencingColumns();
    
    SQLExprTableSource getReferencedTable();
    
    SQLName getReferencedTableName();
    
    void setReferencedTableName(final SQLName p0);
    
    List<SQLName> getReferencedColumns();
}
