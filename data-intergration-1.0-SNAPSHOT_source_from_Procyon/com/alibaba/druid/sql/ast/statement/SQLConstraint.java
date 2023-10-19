// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;

public interface SQLConstraint extends SQLObject
{
    SQLName getName();
    
    void setName(final SQLName p0);
    
    SQLExpr getComment();
    
    void setComment(final SQLExpr p0);
    
    void simplify();
}
