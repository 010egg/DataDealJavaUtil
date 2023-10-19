// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast;

import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;

public class OracleDataTypeIntervalDay extends SQLDataTypeImpl implements OracleSQLObject
{
    private boolean toSecond;
    protected final List<SQLExpr> fractionalSeconds;
    
    public OracleDataTypeIntervalDay() {
        this.toSecond = false;
        this.fractionalSeconds = new ArrayList<SQLExpr>();
        this.setName("INTERVAL DAY");
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        this.accept0((OracleASTVisitor)visitor);
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.getArguments());
        }
        visitor.endVisit(this);
    }
    
    public boolean isToSecond() {
        return this.toSecond;
    }
    
    public void setToSecond(final boolean toSecond) {
        this.toSecond = toSecond;
    }
    
    public List<SQLExpr> getFractionalSeconds() {
        return this.fractionalSeconds;
    }
    
    @Override
    public OracleDataTypeIntervalDay clone() {
        final OracleDataTypeIntervalDay x = new OracleDataTypeIntervalDay();
        super.cloneTo(x);
        for (final SQLExpr arg : this.fractionalSeconds) {
            arg.setParent(x);
            x.fractionalSeconds.add(arg);
        }
        return x;
    }
}
