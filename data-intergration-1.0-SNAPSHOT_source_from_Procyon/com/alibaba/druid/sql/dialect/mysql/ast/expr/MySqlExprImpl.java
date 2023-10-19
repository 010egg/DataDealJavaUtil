// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlObjectImpl;

public abstract class MySqlExprImpl extends MySqlObjectImpl implements SQLExpr
{
    protected SQLCommentHint hint;
    
    @Override
    public SQLExpr clone() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public SQLCommentHint getHint() {
        return this.hint;
    }
    
    @Override
    public void setHint(final SQLCommentHint hint) {
        this.hint = hint;
    }
}
