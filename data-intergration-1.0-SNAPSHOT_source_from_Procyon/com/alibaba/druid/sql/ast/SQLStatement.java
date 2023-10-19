// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import com.alibaba.druid.sql.visitor.VisitorFeature;
import java.util.List;
import com.alibaba.druid.DbType;

public interface SQLStatement extends SQLObject, SQLDbTypedObject
{
    DbType getDbType();
    
    boolean isAfterSemi();
    
    void setAfterSemi(final boolean p0);
    
    SQLStatement clone();
    
    List<SQLObject> getChildren();
    
    List<SQLCommentHint> getHeadHintsDirect();
    
    void setHeadHints(final List<SQLCommentHint> p0);
    
    String toString();
    
    String toString(final VisitorFeature... p0);
    
    String toLowerCaseString();
    
    String toParameterizedString();
}
