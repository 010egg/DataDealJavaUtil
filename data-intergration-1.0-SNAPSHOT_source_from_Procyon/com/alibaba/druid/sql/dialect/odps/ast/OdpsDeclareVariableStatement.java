// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;

public class OdpsDeclareVariableStatement extends OdpsStatementImpl
{
    private String variant;
    private SQLDataType dataType;
    private SQLExpr initValue;
    
    public OdpsDeclareVariableStatement() {
    }
    
    public OdpsDeclareVariableStatement(final String variant, final SQLExpr initValue) {
        this.variant = variant;
        this.initValue = initValue;
    }
    
    @Override
    protected void accept0(final OdpsASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.dataType);
            this.acceptChild(v, this.initValue);
        }
        v.endVisit(this);
    }
    
    public String getVariant() {
        return this.variant;
    }
    
    public void setVariant(final String variant) {
        this.variant = variant;
    }
    
    public SQLExpr getInitValue() {
        return this.initValue;
    }
    
    public void setInitValue(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.initValue = x;
    }
    
    public SQLDataType getDataType() {
        return this.dataType;
    }
    
    public void setDataType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dataType = x;
    }
}
