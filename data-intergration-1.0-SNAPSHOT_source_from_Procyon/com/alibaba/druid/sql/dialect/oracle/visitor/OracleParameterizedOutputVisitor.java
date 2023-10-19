// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.visitor.VisitorFeature;
import com.alibaba.druid.sql.visitor.ParameterizedVisitor;

public class OracleParameterizedOutputVisitor extends OracleOutputVisitor implements ParameterizedVisitor
{
    public OracleParameterizedOutputVisitor() {
        this(new StringBuilder());
        this.config(VisitorFeature.OutputParameterized, true);
    }
    
    public OracleParameterizedOutputVisitor(final Appendable appender) {
        super(appender);
        this.config(VisitorFeature.OutputParameterized, true);
    }
    
    public OracleParameterizedOutputVisitor(final Appendable appender, final boolean printPostSemi) {
        super(appender, printPostSemi);
        this.config(VisitorFeature.OutputParameterized, true);
    }
    
    @Override
    public boolean visit(SQLBinaryOpExpr x) {
        x = SQLBinaryOpExpr.merge(this, x);
        return super.visit(x);
    }
}
