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

public class OracleDatetimeExpr extends OracleSQLObjectImpl implements SQLExpr
{
    private SQLExpr expr;
    private SQLExpr timeZone;
    
    public OracleDatetimeExpr() {
    }
    
    public OracleDatetimeExpr(final SQLExpr expr, final SQLExpr timeZone) {
        this.expr = expr;
        this.timeZone = timeZone;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
            this.acceptChild(visitor, this.timeZone);
        }
        visitor.endVisit(this);
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        this.expr = expr;
    }
    
    public SQLExpr getTimeZone() {
        return this.timeZone;
    }
    
    public void setTimeZone(final SQLExpr timeZone) {
        this.timeZone = timeZone;
    }
    
    @Override
    public OracleDatetimeExpr clone() {
        final OracleDatetimeExpr x = new OracleDatetimeExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        if (this.timeZone != null) {
            x.setTimeZone(this.timeZone.clone());
        }
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.expr, this.timeZone);
    }
}
