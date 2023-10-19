// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.clause;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObjectImpl;

public class CycleClause extends OracleSQLObjectImpl
{
    private final List<SQLExpr> aliases;
    private SQLExpr mark;
    private SQLExpr value;
    private SQLExpr defaultValue;
    
    public CycleClause() {
        this.aliases = new ArrayList<SQLExpr>();
    }
    
    public SQLExpr getMark() {
        return this.mark;
    }
    
    public void setMark(final SQLExpr mark) {
        if (mark != null) {
            mark.setParent(this);
        }
        this.mark = mark;
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
    
    public SQLExpr getDefaultValue() {
        return this.defaultValue;
    }
    
    public void setDefaultValue(final SQLExpr defaultValue) {
        if (defaultValue != null) {
            defaultValue.setParent(this);
        }
        this.defaultValue = defaultValue;
    }
    
    public List<SQLExpr> getAliases() {
        return this.aliases;
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.aliases);
            this.acceptChild(visitor, this.mark);
            this.acceptChild(visitor, this.value);
            this.acceptChild(visitor, this.defaultValue);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public CycleClause clone() {
        final CycleClause x = new CycleClause();
        for (final SQLExpr alias : this.aliases) {
            final SQLExpr alias2 = alias.clone();
            alias2.setParent(x);
            x.aliases.add(alias2);
        }
        if (this.mark != null) {
            this.setMark(this.mark.clone());
        }
        if (this.value != null) {
            this.setValue(this.value.clone());
        }
        if (this.defaultValue != null) {
            this.setDefaultValue(this.defaultValue.clone());
        }
        return x;
    }
}
