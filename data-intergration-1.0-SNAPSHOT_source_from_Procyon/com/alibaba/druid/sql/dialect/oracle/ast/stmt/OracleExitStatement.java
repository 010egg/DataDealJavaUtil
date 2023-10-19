// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.ArrayList;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;

public class OracleExitStatement extends OracleStatementImpl
{
    private String label;
    private SQLExpr when;
    
    public SQLExpr getWhen() {
        return this.when;
    }
    
    public void setWhen(final SQLExpr when) {
        if (when != null) {
            when.setParent(this);
        }
        this.when = when;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.when);
        }
        visitor.endVisit(this);
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(final String label) {
        this.label = label;
    }
    
    @Override
    public OracleExitStatement clone() {
        final OracleExitStatement x = new OracleExitStatement();
        x.setLabel(this.label);
        x.setAfterSemi(this.afterSemi);
        x.setDbType(this.dbType);
        if (this.when != null) {
            x.setWhen(this.when.clone());
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
