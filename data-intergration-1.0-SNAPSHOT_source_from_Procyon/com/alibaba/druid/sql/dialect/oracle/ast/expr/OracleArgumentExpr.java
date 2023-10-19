// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.expr;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class OracleArgumentExpr extends OracleSQLObjectImpl implements SQLExpr, SQLReplaceable
{
    private String argumentName;
    private SQLExpr value;
    
    public OracleArgumentExpr() {
    }
    
    public OracleArgumentExpr(final String argumentName, final SQLExpr value) {
        this.argumentName = argumentName;
        this.setValue(value);
    }
    
    public String getArgumentName() {
        return this.argumentName;
    }
    
    public void setArgumentName(final String argumentName) {
        this.argumentName = argumentName;
    }
    
    public SQLExpr getValue() {
        return this.value;
    }
    
    public void setValue(final SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.value = value;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.value);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public OracleArgumentExpr clone() {
        final OracleArgumentExpr x = new OracleArgumentExpr();
        x.argumentName = this.argumentName;
        if (this.value != null) {
            x.setValue(this.value.clone());
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.value == expr) {
            this.setValue(target);
            return true;
        }
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.value);
    }
}
