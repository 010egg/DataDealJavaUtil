// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.HashMap;
import java.util.ArrayList;
import com.alibaba.druid.sql.visitor.functions.Function;
import java.util.Map;
import java.util.List;

public class SQLEvalVisitorImpl extends SQLASTVisitorAdapter implements SQLEvalVisitor
{
    private List<Object> parameters;
    private Map<String, Function> functions;
    private int variantIndex;
    private boolean markVariantIndex;
    
    public SQLEvalVisitorImpl() {
        this(new ArrayList<Object>(1));
    }
    
    public SQLEvalVisitorImpl(final List<Object> parameters) {
        this.parameters = new ArrayList<Object>();
        this.functions = new HashMap<String, Function>();
        this.variantIndex = -1;
        this.markVariantIndex = true;
        this.parameters = parameters;
    }
    
    @Override
    public List<Object> getParameters() {
        return this.parameters;
    }
    
    @Override
    public void setParameters(final List<Object> parameters) {
        this.parameters = parameters;
    }
    
    @Override
    public boolean visit(final SQLCharExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public int incrementAndGetVariantIndex() {
        return ++this.variantIndex;
    }
    
    public int getVariantIndex() {
        return this.variantIndex;
    }
    
    @Override
    public boolean visit(final SQLVariantRefExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLIntegerExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLNumberExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLHexExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLCaseExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLInListExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLNullExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLMethodInvokeExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean visit(final SQLQueryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public boolean isMarkVariantIndex() {
        return this.markVariantIndex;
    }
    
    @Override
    public void setMarkVariantIndex(final boolean markVariantIndex) {
        this.markVariantIndex = markVariantIndex;
    }
    
    @Override
    public Function getFunction(final String funcName) {
        return this.functions.get(funcName);
    }
    
    @Override
    public void registerFunction(final String funcName, final Function function) {
        this.functions.put(funcName, function);
    }
    
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
    
    @Override
    public void unregisterFunction(final String funcName) {
        this.functions.remove(funcName);
    }
    
    @Override
    public boolean visit(final SQLBooleanExpr x) {
        x.getAttributes().put("eval.value", x.getBooleanValue());
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryExpr x) {
        return SQLEvalVisitorUtils.visit(this, x);
    }
}
