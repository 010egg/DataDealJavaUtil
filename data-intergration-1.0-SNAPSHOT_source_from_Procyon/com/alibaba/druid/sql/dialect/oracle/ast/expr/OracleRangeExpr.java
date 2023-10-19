// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Arrays;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleRangeExpr extends OracleSQLObjectImpl implements SQLExpr
{
    private SQLExpr lowBound;
    private SQLExpr upBound;
    
    public OracleRangeExpr() {
    }
    
    public OracleRangeExpr(final SQLExpr lowBound, final SQLExpr upBound) {
        this.setLowBound(lowBound);
        this.setUpBound(upBound);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.lowBound);
            this.acceptChild(visitor, this.upBound);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.lowBound, this.upBound);
    }
    
    public SQLExpr getLowBound() {
        return this.lowBound;
    }
    
    public void setLowBound(final SQLExpr lowBound) {
        if (lowBound != null) {
            lowBound.setParent(this);
        }
        this.lowBound = lowBound;
    }
    
    public SQLExpr getUpBound() {
        return this.upBound;
    }
    
    public void setUpBound(final SQLExpr upBound) {
        if (upBound != null) {
            upBound.setParent(this);
        }
        this.upBound = upBound;
    }
    
    @Override
    public OracleRangeExpr clone() {
        final OracleRangeExpr x = new OracleRangeExpr();
        if (this.lowBound != null) {
            x.setLowBound(this.lowBound.clone());
        }
        if (this.upBound != null) {
            x.setUpBound(this.upBound.clone());
        }
        return x;
    }
}
