// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.visitor;

import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.visitor.ExportParameterVisitorUtils;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.visitor.ExportParameterVisitor;

public class MSSQLServerExportParameterVisitor extends SQLServerOutputVisitor implements ExportParameterVisitor
{
    private final boolean requireParameterizedOutput;
    
    public MSSQLServerExportParameterVisitor(final List<Object> parameters, final Appendable appender, final boolean wantParameterizedOutput) {
        super(appender, true);
        this.parameters = parameters;
        this.requireParameterizedOutput = wantParameterizedOutput;
    }
    
    public MSSQLServerExportParameterVisitor() {
        this(new ArrayList<Object>());
    }
    
    public MSSQLServerExportParameterVisitor(final List<Object> parameters) {
        this(parameters, new StringBuilder(), false);
    }
    
    public MSSQLServerExportParameterVisitor(final Appendable appender) {
        this(new ArrayList<Object>(), appender, true);
    }
    
    @Override
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    @Override
    public boolean visit(final SQLSelectItem x) {
        return this.requireParameterizedOutput && super.visit(x);
    }
    
    @Override
    public boolean visit(final SQLOrderBy x) {
        return this.requireParameterizedOutput && super.visit(x);
    }
    
    @Override
    public boolean visit(final SQLSelectGroupByClause x) {
        return this.requireParameterizedOutput && super.visit(x);
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        }
        ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getArguments());
        return true;
    }
    
    @Override
    public boolean visit(final SQLInListExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        }
        ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getTargetList());
        return true;
    }
    
    @Override
    public boolean visit(final SQLBetweenExpr x) {
        if (this.requireParameterizedOutput) {
            return super.visit(x);
        }
        ExportParameterVisitorUtils.exportParameter(this.parameters, x);
        return true;
    }
}
