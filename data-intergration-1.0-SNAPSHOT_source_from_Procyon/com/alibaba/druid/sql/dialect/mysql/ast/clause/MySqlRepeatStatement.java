// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.clause;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlStatementImpl;

public class MySqlRepeatStatement extends MySqlStatementImpl
{
    private String labelName;
    private List<SQLStatement> statements;
    private SQLExpr condition;
    
    public MySqlRepeatStatement() {
        this.statements = new ArrayList<SQLStatement>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.statements);
            this.acceptChild(visitor, this.condition);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLStatement> getStatements() {
        return this.statements;
    }
    
    public void setStatements(final List<SQLStatement> statements) {
        this.statements = statements;
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    public void setLabelName(final String labelName) {
        this.labelName = labelName;
    }
    
    public SQLExpr getCondition() {
        return this.condition;
    }
    
    public void setCondition(final SQLExpr condition) {
        this.condition = condition;
    }
}
