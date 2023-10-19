// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.ArrayList;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLExplainStatement extends SQLStatementImpl
{
    protected String type;
    protected String format;
    protected boolean extended;
    protected boolean dependency;
    protected boolean authorization;
    protected boolean optimizer;
    protected SQLStatement statement;
    protected List<SQLCommentHint> hints;
    protected boolean parenthesis;
    
    public SQLExplainStatement() {
    }
    
    public SQLExplainStatement(final DbType dbType) {
        super(dbType);
    }
    
    public SQLStatement getStatement() {
        return this.statement;
    }
    
    public void setStatement(final SQLStatement statement) {
        if (statement != null) {
            statement.setParent(this);
        }
        this.statement = statement;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.statement);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        if (this.statement != null) {
            children.add(this.statement);
        }
        return children;
    }
    
    public boolean isExtended() {
        return this.extended;
    }
    
    public void setExtended(final boolean extended) {
        this.extended = extended;
    }
    
    public boolean isDependency() {
        return this.dependency;
    }
    
    public void setDependency(final boolean dependency) {
        this.dependency = dependency;
    }
    
    public boolean isAuthorization() {
        return this.authorization;
    }
    
    public void setAuthorization(final boolean authorization) {
        this.authorization = authorization;
    }
    
    public boolean isOptimizer() {
        return this.optimizer;
    }
    
    public void setOptimizer(final boolean optimizer) {
        this.optimizer = optimizer;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public void setFormat(final String format) {
        this.format = format;
    }
    
    public boolean isParenthesis() {
        return this.parenthesis;
    }
    
    public void setParenthesis(final boolean parenthesis) {
        this.parenthesis = parenthesis;
    }
}
