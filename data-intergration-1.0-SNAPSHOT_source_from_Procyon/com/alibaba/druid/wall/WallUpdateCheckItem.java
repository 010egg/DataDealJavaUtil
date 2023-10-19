// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.wall;

import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;

public class WallUpdateCheckItem
{
    public final String tableName;
    public final String columnName;
    public final SQLExpr value;
    public final List<SQLExpr> filterValues;
    
    public WallUpdateCheckItem(final String tableName, final String columnName, final SQLExpr value, final List<SQLExpr> filterValues) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.value = value;
        this.filterValues = filterValues;
    }
}
