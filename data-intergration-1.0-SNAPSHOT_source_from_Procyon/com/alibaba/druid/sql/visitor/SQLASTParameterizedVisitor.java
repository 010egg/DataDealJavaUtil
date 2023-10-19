// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import java.util.List;
import com.alibaba.druid.DbType;

public class SQLASTParameterizedVisitor extends SQLASTVisitorAdapter
{
    protected DbType dbType;
    protected List<Object> parameters;
    private int replaceCount;
    
    public SQLASTParameterizedVisitor(final DbType dbType) {
        this.replaceCount = 0;
        this.dbType = dbType;
    }
    
    public int getReplaceCount() {
        return this.replaceCount;
    }
    
    public void incrementReplaceCunt() {
        ++this.replaceCount;
    }
    
    public DbType getDbType() {
        return this.dbType;
    }
    
    public SQLASTParameterizedVisitor(final DbType dbType, final List<Object> parameters) {
        this.replaceCount = 0;
        this.dbType = dbType;
        this.parameters = parameters;
    }
    
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    public void setParameters(final List<Object> parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public boolean visit(final SQLTimestampExpr x) {
        this.parameterizeAndExportPara(x);
        return false;
    }
    
    public void parameterizeAndExportPara(final SQLExpr x) {
        final SQLVariantRefExpr variantRefExpr = new SQLVariantRefExpr("?");
        variantRefExpr.setIndex(this.replaceCount);
        SQLUtils.replaceInParent(x, variantRefExpr);
        this.incrementReplaceCunt();
        ExportParameterVisitorUtils.exportParameter(this.parameters, x);
    }
    
    public void parameterize(final SQLExpr x) {
        final SQLVariantRefExpr variantRefExpr = new SQLVariantRefExpr("?");
        variantRefExpr.setIndex(this.replaceCount);
        SQLUtils.replaceInParent(x, variantRefExpr);
        this.incrementReplaceCunt();
    }
    
    @Override
    public boolean visit(final SQLCharExpr x) {
        this.parameterizeAndExportPara(x);
        return false;
    }
    
    @Override
    public boolean visit(final SQLIntegerExpr x) {
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLSelectGroupByClause || parent instanceof SQLSelectOrderByItem) {
            return false;
        }
        this.parameterizeAndExportPara(x);
        return false;
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        final List<SQLExpr> arguments = x.getArguments();
        if (x.methodNameHashCode64() == FnvHash.Constants.TRIM && arguments.size() == 1 && arguments.get(0) instanceof SQLCharExpr && x.getTrimOption() == null && x.getFrom() == null) {
            this.parameterizeAndExportPara(x);
            if (this.parameters != null) {
                final SQLCharExpr charExpr = arguments.get(0);
                this.parameters.add(charExpr.getText().trim());
            }
            ++this.replaceCount;
            return false;
        }
        return true;
    }
    
    @Override
    public boolean visit(final SQLNCharExpr x) {
        this.parameterizeAndExportPara(x);
        return false;
    }
    
    @Override
    public boolean visit(final SQLNullExpr x) {
        final SQLObject parent = x.getParent();
        if (parent instanceof SQLInsertStatement || parent instanceof SQLInsertStatement.ValuesClause || parent instanceof SQLInListExpr || (parent instanceof SQLBinaryOpExpr && ((SQLBinaryOpExpr)parent).getOperator() == SQLBinaryOperator.Equality)) {
            this.parameterize(x);
            if (this.parameters != null) {
                if (parent instanceof SQLBinaryOpExpr) {
                    ExportParameterVisitorUtils.exportParameter(this.parameters, x);
                }
                else {
                    this.parameters.add(null);
                }
            }
            return false;
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLNumberExpr x) {
        this.parameterizeAndExportPara(x);
        return false;
    }
    
    @Override
    public boolean visit(final SQLHexExpr x) {
        this.parameterizeAndExportPara(x);
        return false;
    }
}
