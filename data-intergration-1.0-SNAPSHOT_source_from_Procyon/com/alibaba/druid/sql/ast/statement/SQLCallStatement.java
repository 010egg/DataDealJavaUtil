// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCallStatement extends SQLStatementImpl
{
    private boolean brace;
    private SQLVariantRefExpr outParameter;
    private SQLName procedureName;
    private final List<SQLExpr> parameters;
    
    public SQLCallStatement() {
        this.brace = false;
        this.parameters = new ArrayList<SQLExpr>();
    }
    
    public SQLCallStatement(final DbType dbType) {
        super(dbType);
        this.brace = false;
        this.parameters = new ArrayList<SQLExpr>();
    }
    
    public SQLVariantRefExpr getOutParameter() {
        return this.outParameter;
    }
    
    public void setOutParameter(final SQLVariantRefExpr outParameter) {
        this.outParameter = outParameter;
    }
    
    public SQLName getProcedureName() {
        return this.procedureName;
    }
    
    public void setProcedureName(final SQLName procedureName) {
        this.procedureName = procedureName;
    }
    
    public List<SQLExpr> getParameters() {
        return this.parameters;
    }
    
    public boolean isBrace() {
        return this.brace;
    }
    
    public void setBrace(final boolean brace) {
        this.brace = brace;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.outParameter);
            this.acceptChild(visitor, this.procedureName);
            this.acceptChild(visitor, this.parameters);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.outParameter);
        children.add(this.procedureName);
        children.addAll(this.parameters);
        return null;
    }
}
