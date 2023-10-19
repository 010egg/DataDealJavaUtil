// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;

public class MySqlExecuteStatement extends MySqlStatementImpl
{
    private SQLName statementName;
    private final List<SQLExpr> parameters;
    
    public MySqlExecuteStatement() {
        this.parameters = new ArrayList<SQLExpr>();
    }
    
    public SQLName getStatementName() {
        return this.statementName;
    }
    
    public void setStatementName(final SQLName statementName) {
        this.statementName = statementName;
    }
    
    public List<SQLExpr> getParameters() {
        return this.parameters;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.statementName);
            this.acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.statementName != null) {
            children.add(this.statementName);
        }
        children.addAll(this.parameters);
        return children;
    }
}
