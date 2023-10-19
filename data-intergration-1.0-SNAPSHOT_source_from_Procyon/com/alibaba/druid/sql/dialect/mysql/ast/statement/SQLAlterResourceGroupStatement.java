// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.HashMap;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterResourceGroupStatement extends SQLStatementImpl implements SQLCreateStatement
{
    private SQLName name;
    private Map<String, SQLExpr> properties;
    private Boolean enable;
    
    public SQLAlterResourceGroupStatement() {
        this.properties = new HashMap<String, SQLExpr>();
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
    }
    
    public void addProperty(final String name, final SQLExpr value) {
        if (value != null) {
            value.setParent(this);
        }
        this.properties.put(name, value);
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public Map<String, SQLExpr> getProperties() {
        return this.properties;
    }
    
    public void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.name);
            for (final SQLExpr value : this.properties.values()) {
                this.acceptChild(v, value);
            }
        }
        v.endVisit(this);
    }
}
