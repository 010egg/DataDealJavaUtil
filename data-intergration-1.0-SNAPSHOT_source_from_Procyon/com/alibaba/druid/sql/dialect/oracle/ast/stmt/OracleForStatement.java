// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLForStatement;

public class OracleForStatement extends SQLForStatement implements OracleStatement
{
    private boolean all;
    private SQLName endLabel;
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v instanceof OracleASTVisitor) {
            this.accept0((OracleASTVisitor)v);
            return;
        }
        super.accept0(v);
    }
    
    @Override
    public void accept0(final OracleASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.index);
            this.acceptChild(v, this.range);
            this.acceptChild(v, this.statements);
        }
        v.endVisit(this);
    }
    
    public boolean isAll() {
        return this.all;
    }
    
    public void setAll(final boolean all) {
        this.all = all;
    }
    
    public SQLName getEndLabel() {
        return this.endLabel;
    }
    
    public void setEndLabel(final SQLName endLabel) {
        if (endLabel != null) {
            endLabel.setParent(this);
        }
        this.endLabel = endLabel;
    }
    
    @Override
    public OracleForStatement clone() {
        final OracleForStatement x = new OracleForStatement();
        if (this.index != null) {
            x.setIndex(this.index.clone());
        }
        if (this.range != null) {
            x.setRange(this.range.clone());
        }
        for (final SQLStatement stmt : this.statements) {
            final SQLStatement stmt2 = stmt.clone();
            stmt2.setParent(x);
            x.statements.add(stmt2);
        }
        x.all = this.all;
        if (this.endLabel != null) {
            x.setEndLabel(this.endLabel.clone());
        }
        return x;
    }
}
