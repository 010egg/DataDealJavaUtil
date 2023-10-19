// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class OdpsTransformExpr extends SQLExprImpl implements OdpsObject
{
    private SQLExternalRecordFormat inputRowFormat;
    private final List<SQLExpr> inputColumns;
    private final List<SQLColumnDefinition> outputColumns;
    private SQLExpr using;
    private final List<SQLExpr> resources;
    private SQLExternalRecordFormat outputRowFormat;
    
    public OdpsTransformExpr() {
        this.inputColumns = new ArrayList<SQLExpr>();
        this.outputColumns = new ArrayList<SQLColumnDefinition>();
        this.resources = new ArrayList<SQLExpr>();
    }
    
    @Override
    public boolean equals(final Object o) {
        return false;
    }
    
    @Override
    public int hashCode() {
        return 0;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        this.accept0((OdpsASTVisitor)v);
    }
    
    @Override
    public void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.inputColumns);
            this.acceptChild(v, this.outputColumns);
        }
        v.endVisit(this);
    }
    
    @Override
    public SQLExpr clone() {
        return null;
    }
    
    public SQLExternalRecordFormat getInputRowFormat() {
        return this.inputRowFormat;
    }
    
    public void setInputRowFormat(final SQLExternalRecordFormat inputRowFormat) {
        this.inputRowFormat = inputRowFormat;
    }
    
    public List<SQLExpr> getInputColumns() {
        return this.inputColumns;
    }
    
    public List<SQLColumnDefinition> getOutputColumns() {
        return this.outputColumns;
    }
    
    public SQLExpr getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLExpr using) {
        this.using = using;
    }
    
    public SQLExternalRecordFormat getOutputRowFormat() {
        return this.outputRowFormat;
    }
    
    public void setOutputRowFormat(final SQLExternalRecordFormat outputRowFormat) {
        this.outputRowFormat = outputRowFormat;
    }
    
    public List<SQLExpr> getResources() {
        return this.resources;
    }
}
