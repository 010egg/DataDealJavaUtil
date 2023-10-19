// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public abstract class SQLNumericLiteralExpr extends SQLExprImpl implements SQLLiteralExpr
{
    public abstract Number getNumber();
    
    public abstract void setNumber(final Number p0);
    
    @Override
    public abstract SQLNumericLiteralExpr clone();
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
}
