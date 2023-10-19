// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;

public interface SQLColumnConstraint extends SQLConstraint, SQLTableElement
{
    SQLColumnConstraint clone();
}
