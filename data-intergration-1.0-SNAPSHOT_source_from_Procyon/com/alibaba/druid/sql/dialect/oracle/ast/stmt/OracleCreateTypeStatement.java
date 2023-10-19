// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLCreateStatement;

public class OracleCreateTypeStatement extends OracleStatementImpl implements SQLCreateStatement
{
    private boolean orReplace;
    private SQLName name;
    private SQLName authId;
    private boolean force;
    private SQLName oid;
    private boolean body;
    private boolean object;
    private boolean paren;
    private Boolean isFinal;
    private Boolean instantiable;
    private SQLName under;
    private List<SQLParameter> parameters;
    private SQLDataType tableOf;
    private SQLExpr varraySizeLimit;
    private SQLDataType varrayDataType;
    private String wrappedSource;
    
    public OracleCreateTypeStatement() {
        this.parameters = new ArrayList<SQLParameter>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.authId);
            this.acceptChild(visitor, this.oid);
            this.acceptChild(visitor, this.under);
            this.acceptChild(visitor, this.parameters);
            this.acceptChild(visitor, this.tableOf);
            this.acceptChild(visitor, this.varraySizeLimit);
            this.acceptChild(visitor, this.varrayDataType);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        if (name != null) {
            name.setParent(this);
        }
        this.name = name;
    }
    
    public boolean isOrReplace() {
        return this.orReplace;
    }
    
    public void setOrReplace(final boolean orReplace) {
        this.orReplace = orReplace;
    }
    
    public boolean isForce() {
        return this.force;
    }
    
    public void setForce(final boolean force) {
        this.force = force;
    }
    
    public SQLName getOid() {
        return this.oid;
    }
    
    public void setOid(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.oid = x;
    }
    
    public SQLName getAuthId() {
        return this.authId;
    }
    
    public void setAuthId(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.authId = x;
    }
    
    public List<SQLParameter> getParameters() {
        return this.parameters;
    }
    
    public boolean isBody() {
        return this.body;
    }
    
    public void setBody(final boolean body) {
        this.body = body;
    }
    
    public Boolean getFinal() {
        return this.isFinal;
    }
    
    public void setFinal(final boolean aFinal) {
        this.isFinal = aFinal;
    }
    
    public Boolean getInstantiable() {
        return this.instantiable;
    }
    
    public void setInstantiable(final boolean instantiable) {
        this.instantiable = instantiable;
    }
    
    public SQLDataType getTableOf() {
        return this.tableOf;
    }
    
    public void setTableOf(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.tableOf = x;
    }
    
    public SQLExpr getVarraySizeLimit() {
        return this.varraySizeLimit;
    }
    
    public void setVarraySizeLimit(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.varraySizeLimit = x;
    }
    
    public SQLDataType getVarrayDataType() {
        return this.varrayDataType;
    }
    
    public void setVarrayDataType(final SQLDataType x) {
        if (x != null) {
            x.setParent(this);
        }
        this.varrayDataType = x;
    }
    
    public SQLName getUnder() {
        return this.under;
    }
    
    public void setUnder(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.under = x;
    }
    
    public boolean isObject() {
        return this.object;
    }
    
    public void setObject(final boolean object) {
        this.object = object;
    }
    
    public boolean isParen() {
        return this.paren;
    }
    
    public void setParen(final boolean paren) {
        this.paren = paren;
    }
    
    public String getWrappedSource() {
        return this.wrappedSource;
    }
    
    public void setWrappedSource(final String wrappedSource) {
        this.wrappedSource = wrappedSource;
    }
}
