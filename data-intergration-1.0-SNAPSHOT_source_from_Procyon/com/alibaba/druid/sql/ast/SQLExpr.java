// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.List;

public interface SQLExpr extends SQLObject, Cloneable
{
    SQLExpr clone();
    
    SQLDataType computeDataType();
    
    List<SQLObject> getChildren();
    
    SQLCommentHint getHint();
}
