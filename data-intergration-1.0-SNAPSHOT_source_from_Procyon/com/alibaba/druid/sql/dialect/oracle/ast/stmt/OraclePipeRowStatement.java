// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class OraclePipeRowStatement extends OracleStatementImpl
{
    private final List<SQLExpr> parameters;
    
    public OraclePipeRowStatement() {
        this.parameters = new ArrayList<SQLExpr>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLExpr> getParameters() {
        return this.parameters;
    }
    
    @Override
    public OraclePipeRowStatement clone() {
        final OraclePipeRowStatement x = new OraclePipeRowStatement();
        x.setAfterSemi(this.afterSemi);
        x.setDbType(this.dbType);
        for (final SQLExpr expr : this.parameters) {
            final SQLExpr expr2 = expr.clone();
            expr2.setParent(x);
            x.parameters.add(expr2);
        }
        if (this.headHints != null) {
            final List<SQLCommentHint> headHintsClone = new ArrayList<SQLCommentHint>(this.headHints.size());
            for (final SQLCommentHint hint : this.headHints) {
                final SQLCommentHint h2 = hint.clone();
                h2.setParent(x);
                headHintsClone.add(h2);
            }
            x.setHeadHints(headHintsClone);
        }
        return x;
    }
}
