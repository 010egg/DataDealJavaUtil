// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.phoenix.visitor;

import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class PhoenixOutputVisitor extends SQLASTOutputVisitor implements PhoenixASTVisitor
{
    public PhoenixOutputVisitor(final Appendable appender) {
        super(appender);
    }
    
    public PhoenixOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
    }
}
