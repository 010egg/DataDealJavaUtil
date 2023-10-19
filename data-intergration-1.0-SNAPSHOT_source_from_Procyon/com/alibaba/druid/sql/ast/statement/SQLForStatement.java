// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLForStatement extends SQLStatementImpl
{
    protected SQLName index;
    protected SQLExpr range;
    protected List<SQLStatement> statements;
    
    public SQLForStatement() {
        this.statements = new ArrayList<SQLStatement>();
    }
    
    public SQLName getIndex() {
        return this.index;
    }
    
    public void setIndex(final SQLName index) {
        this.index = index;
    }
    
    public SQLExpr getRange() {
        return this.range;
    }
    
    public void setRange(final SQLExpr range) {
        if (range != null) {
            range.setParent(this);
        }
        this.range = range;
    }
    
    public List<SQLStatement> getStatements() {
        return this.statements;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.index);
            this.acceptChild(v, this.range);
            this.acceptChild(v, this.statements);
        }
        v.endVisit(this);
    }
}
