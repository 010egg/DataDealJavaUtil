// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Arrays;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleTreatExpr extends OracleSQLObjectImpl implements SQLExpr
{
    private SQLExpr expr;
    private SQLExpr type;
    private boolean ref;
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    public SQLExpr getType() {
        return this.type;
    }
    
    public void setType(final SQLExpr type) {
        if (type != null) {
            type.setParent(this);
        }
        this.type = type;
    }
    
    public boolean isRef() {
        return this.ref;
    }
    
    public void setRef(final boolean ref) {
        this.ref = ref;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.type);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.expr, this.type);
    }
    
    @Override
    public OracleTreatExpr clone() {
        final OracleTreatExpr x = new OracleTreatExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        if (this.type != null) {
            x.setType(this.type.clone());
        }
        x.ref = this.ref;
        return x;
    }
}
