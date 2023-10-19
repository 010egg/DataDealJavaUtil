// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateProcedureStatement extends SQLStatementImpl implements SQLCreateStatement
{
    private SQLName definer;
    private boolean create;
    private boolean orReplace;
    private SQLName name;
    private SQLStatement block;
    private List<SQLParameter> parameters;
    private String javaCallSpec;
    private SQLName authid;
    private boolean deterministic;
    private boolean containsSql;
    private boolean noSql;
    private boolean readSqlData;
    private boolean modifiesSqlData;
    private boolean languageSql;
    private String wrappedSource;
    private SQLCharExpr comment;
    
    public SQLCreateProcedureStatement() {
        this.create = true;
        this.parameters = new ArrayList<SQLParameter>();
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.definer);
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.parameters);
            this.acceptChild(visitor, this.block);
            this.acceptChild(visitor, this.comment);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLParameter> getParameters() {
        return this.parameters;
    }
    
    public void setParameters(final List<SQLParameter> parameters) {
        this.parameters = parameters;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public SQLStatement getBlock() {
        return this.block;
    }
    
    public void setBlock(final SQLStatement block) {
        if (block != null) {
            block.setParent(this);
        }
        this.block = block;
    }
    
    public SQLName getAuthid() {
        return this.authid;
    }
    
    public void setAuthid(final SQLName authid) {
        if (authid != null) {
            authid.setParent(this);
        }
        this.authid = authid;
    }
    
    public boolean isOrReplace() {
        return this.orReplace;
    }
    
    public void setOrReplace(final boolean orReplace) {
        this.orReplace = orReplace;
    }
    
    public SQLName getDefiner() {
        return this.definer;
    }
    
    public void setDefiner(final SQLName definer) {
        this.definer = definer;
    }
    
    public boolean isCreate() {
        return this.create;
    }
    
    public void setCreate(final boolean create) {
        this.create = create;
    }
    
    public String getJavaCallSpec() {
        return this.javaCallSpec;
    }
    
    public void setJavaCallSpec(final String javaCallSpec) {
        this.javaCallSpec = javaCallSpec;
    }
    
    public boolean isDeterministic() {
        return this.deterministic;
    }
    
    public void setDeterministic(final boolean deterministic) {
        this.deterministic = deterministic;
    }
    
    public boolean isContainsSql() {
        return this.containsSql;
    }
    
    public void setContainsSql(final boolean containsSql) {
        this.containsSql = containsSql;
    }
    
    public boolean isNoSql() {
        return this.noSql;
    }
    
    public void setNoSql(final boolean noSql) {
        this.noSql = noSql;
    }
    
    public boolean isReadSqlData() {
        return this.readSqlData;
    }
    
    public void setReadSqlData(final boolean readSqlData) {
        this.readSqlData = readSqlData;
    }
    
    public boolean isModifiesSqlData() {
        return this.modifiesSqlData;
    }
    
    public void setModifiesSqlData(final boolean modifiesSqlData) {
        this.modifiesSqlData = modifiesSqlData;
    }
    
    public SQLParameter findParameter(final long hash) {
        for (final SQLParameter param : this.parameters) {
            if (param.getName().nameHashCode64() == hash) {
                return param;
            }
        }
        return null;
    }
    
    public boolean isLanguageSql() {
        return this.languageSql;
    }
    
    public void setLanguageSql(final boolean languageSql) {
        this.languageSql = languageSql;
    }
    
    public SQLCharExpr getComment() {
        return this.comment;
    }
    
    public void setComment(final SQLCharExpr comment) {
        if (comment != null) {
            comment.setParent(this);
        }
        this.comment = comment;
    }
    
    public String getWrappedSource() {
        return this.wrappedSource;
    }
    
    public void setWrappedSource(final String wrappedSource) {
        this.wrappedSource = wrappedSource;
    }
}
