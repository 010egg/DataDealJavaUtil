// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLLoopStatement extends SQLStatementImpl
{
    private String labelName;
    private final List<SQLStatement> statements;
    
    public SQLLoopStatement() {
        this.statements = new ArrayList<SQLStatement>();
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.statements);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLStatement> getStatements() {
        return this.statements;
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    public void setLabelName(final String labelName) {
        this.labelName = labelName;
    }
    
    public void addStatement(final SQLStatement stmt) {
        if (stmt != null) {
            stmt.setParent(this);
        }
        this.statements.add(stmt);
    }
    
    @Override
    public List getChildren() {
        return this.statements;
    }
    
    @Override
    public SQLLoopStatement clone() {
        final SQLLoopStatement x = new SQLLoopStatement();
        x.setLabelName(this.labelName);
        x.setAfterSemi(this.afterSemi);
        x.setDbType(this.dbType);
        for (final SQLStatement item : this.statements) {
            final SQLStatement item2 = item.clone();
            item2.setParent(x);
            x.statements.add(item2);
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
