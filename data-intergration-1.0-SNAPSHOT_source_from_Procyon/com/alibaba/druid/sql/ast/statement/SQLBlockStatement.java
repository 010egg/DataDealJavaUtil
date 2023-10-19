// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLBlockStatement extends SQLStatementImpl
{
    private String labelName;
    private String endLabel;
    private List<SQLParameter> parameters;
    private List<SQLStatement> statementList;
    public SQLStatement exception;
    private boolean endOfCommit;
    
    public SQLBlockStatement() {
        this.parameters = new ArrayList<SQLParameter>();
        this.statementList = new ArrayList<SQLStatement>();
    }
    
    public List<SQLStatement> getStatementList() {
        return this.statementList;
    }
    
    public void setStatementList(final List<SQLStatement> statementList) {
        this.statementList = statementList;
    }
    
    public String getLabelName() {
        return this.labelName;
    }
    
    public void setLabelName(final String labelName) {
        this.labelName = labelName;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.parameters);
            this.acceptChild(visitor, this.statementList);
            this.acceptChild(visitor, this.exception);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLParameter> getParameters() {
        return this.parameters;
    }
    
    public void setParameters(final List<SQLParameter> parameters) {
        this.parameters = parameters;
    }
    
    public SQLStatement getException() {
        return this.exception;
    }
    
    public void setException(final SQLStatement exception) {
        if (exception != null) {
            exception.setParent(this);
        }
        this.exception = exception;
    }
    
    public String getEndLabel() {
        return this.endLabel;
    }
    
    public void setEndLabel(final String endLabel) {
        this.endLabel = endLabel;
    }
    
    @Override
    public SQLBlockStatement clone() {
        final SQLBlockStatement x = new SQLBlockStatement();
        x.labelName = this.labelName;
        x.endLabel = this.endLabel;
        for (final SQLParameter p : this.parameters) {
            final SQLParameter p2 = p.clone();
            p2.setParent(x);
            x.parameters.add(p2);
        }
        for (final SQLStatement stmt : this.statementList) {
            final SQLStatement stmt2 = stmt.clone();
            stmt2.setParent(x);
            x.statementList.add(stmt2);
        }
        if (this.exception != null) {
            x.setException(this.exception.clone());
        }
        return x;
    }
    
    public SQLParameter findParameter(final long hash) {
        for (final SQLParameter param : this.parameters) {
            if (param.getName().nameHashCode64() == hash) {
                return param;
            }
        }
        return null;
    }
    
    public boolean isEndOfCommit() {
        return this.endOfCommit;
    }
    
    public void setEndOfCommit(final boolean value) {
        this.endOfCommit = value;
    }
}
