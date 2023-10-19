// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.List;
import java.util.Map;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;

public interface SQLObject
{
    void accept(final SQLASTVisitor p0);
    
    SQLObject clone();
    
    SQLObject getParent();
    
    void setParent(final SQLObject p0);
    
    Map<String, Object> getAttributes();
    
    boolean containsAttribute(final String p0);
    
    Object getAttribute(final String p0);
    
    void putAttribute(final String p0, final Object p1);
    
    Map<String, Object> getAttributesDirect();
    
    void output(final StringBuffer p0);
    
    void output(final Appendable p0);
    
    void addBeforeComment(final String p0);
    
    void addBeforeComment(final List<String> p0);
    
    List<String> getBeforeCommentsDirect();
    
    void addAfterComment(final String p0);
    
    void addAfterComment(final List<String> p0);
    
    List<String> getAfterCommentsDirect();
    
    boolean hasBeforeComment();
    
    boolean hasAfterComment();
}
