// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;

public interface SQLTableSource extends SQLObject
{
    String getAlias();
    
    long aliasHashCode64();
    
    void setAlias(final String p0);
    
    List<SQLHint> getHints();
    
    SQLTableSource clone();
    
    String computeAlias();
    
    boolean containsAlias(final String p0);
    
    SQLExpr getFlashback();
    
    void setFlashback(final SQLExpr p0);
    
    SQLColumnDefinition findColumn(final String p0);
    
    SQLColumnDefinition findColumn(final long p0);
    
    SQLObject resolveColum(final long p0);
    
    SQLTableSource findTableSourceWithColumn(final String p0);
    
    SQLTableSource findTableSourceWithColumn(final long p0);
    
    SQLTableSource findTableSourceWithColumn(final SQLName p0);
    
    SQLTableSource findTableSourceWithColumn(final long p0, final String p1, final int p2);
    
    SQLTableSource findTableSource(final String p0);
    
    SQLTableSource findTableSource(final long p0);
}
