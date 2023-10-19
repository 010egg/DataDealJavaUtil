// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLParameter;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObjectWithDataType;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLCreateFunctionStatement extends SQLStatementImpl implements SQLCreateStatement, SQLObjectWithDataType
{
    protected SQLName definer;
    protected boolean create;
    protected boolean orReplace;
    protected SQLName name;
    protected SQLStatement block;
    protected List<SQLParameter> parameters;
    private String javaCallSpec;
    private SQLName authid;
    SQLDataType returnDataType;
    private String comment;
    private boolean deterministic;
    private boolean parallelEnable;
    private boolean aggregate;
    private SQLName using;
    private boolean pipelined;
    private boolean resultCache;
    private String wrappedSource;
    private String language;
    private boolean temporary;
    protected boolean ifNotExists;
    
    public SQLCreateFunctionStatement() {
        this.create = true;
        this.parameters = new ArrayList<SQLParameter>();
        this.deterministic = false;
        this.ifNotExists = false;
    }
    
    @Override
    public SQLCreateFunctionStatement clone() {
        final SQLCreateFunctionStatement x = new SQLCreateFunctionStatement();
        if (this.definer != null) {
            x.setDefiner(this.definer.clone());
        }
        x.create = this.create;
        x.orReplace = this.orReplace;
        if (this.name != null) {
            x.setName(this.name.clone());
        }
        if (this.block != null) {
            x.setBlock(this.block.clone());
        }
        for (final SQLParameter p : this.parameters) {
            final SQLParameter p2 = p.clone();
            p2.setParent(x);
            x.parameters.add(p2);
        }
        x.javaCallSpec = this.javaCallSpec;
        if (this.authid != null) {
            x.setAuthid(this.authid.clone());
        }
        if (this.returnDataType != null) {
            x.setReturnDataType(this.returnDataType.clone());
        }
        x.comment = this.comment;
        x.deterministic = this.deterministic;
        x.pipelined = this.pipelined;
        x.language = this.language;
        return x;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.definer);
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.parameters);
            this.acceptChild(visitor, this.returnDataType);
            this.acceptChild(visitor, this.block);
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
    
    public void setName(final SQLName x) {
        if (x != null) {
            x.setParent(this);
        }
        this.name = x;
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
    
    public String getLanguage() {
        return this.language;
    }
    
    public void setLanguage(final String language) {
        this.language = language;
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
    
    public SQLDataType getReturnDataType() {
        return this.returnDataType;
    }
    
    public void setReturnDataType(final SQLDataType returnDataType) {
        if (returnDataType != null) {
            returnDataType.setParent(this);
        }
        this.returnDataType = returnDataType;
    }
    
    public String getComment() {
        return this.comment;
    }
    
    public void setComment(final String comment) {
        this.comment = comment;
    }
    
    public boolean isDeterministic() {
        return this.deterministic;
    }
    
    public void setDeterministic(final boolean deterministic) {
        this.deterministic = deterministic;
    }
    
    public String getSchema() {
        final SQLName name = this.getName();
        if (name == null) {
            return null;
        }
        if (name instanceof SQLPropertyExpr) {
            return ((SQLPropertyExpr)name).getOwnernName();
        }
        return null;
    }
    
    @Override
    public SQLDataType getDataType() {
        return this.returnDataType;
    }
    
    @Override
    public void setDataType(final SQLDataType dataType) {
        this.setReturnDataType(dataType);
    }
    
    public boolean isParallelEnable() {
        return this.parallelEnable;
    }
    
    public void setParallelEnable(final boolean parallel_enable) {
        this.parallelEnable = parallel_enable;
    }
    
    public boolean isAggregate() {
        return this.aggregate;
    }
    
    public void setAggregate(final boolean aggregate) {
        this.aggregate = aggregate;
    }
    
    public SQLName getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLName using) {
        this.using = using;
    }
    
    public boolean isPipelined() {
        return this.pipelined;
    }
    
    public void setPipelined(final boolean pipelined) {
        this.pipelined = pipelined;
    }
    
    public boolean isResultCache() {
        return this.resultCache;
    }
    
    public void setResultCache(final boolean resultCache) {
        this.resultCache = resultCache;
    }
    
    public String getWrappedSource() {
        return this.wrappedSource;
    }
    
    public void setWrappedSource(final String wrappedSource) {
        this.wrappedSource = wrappedSource;
    }
    
    public boolean isTemporary() {
        return this.temporary;
    }
    
    public void setTemporary(final boolean temporary) {
        this.temporary = temporary;
    }
    
    public boolean isIfNotExists() {
        return this.ifNotExists;
    }
    
    public void setIfNotExists(final boolean ifNotExists) {
        this.ifNotExists = ifNotExists;
    }
}
