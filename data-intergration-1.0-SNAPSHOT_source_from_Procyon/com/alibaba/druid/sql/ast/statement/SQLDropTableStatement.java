// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.DbType;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLDropTableStatement extends SQLStatementImpl implements SQLDropStatement
{
    private List<SQLCommentHint> hints;
    protected List<SQLExprTableSource> tableSources;
    protected boolean purge;
    protected boolean cascade;
    protected boolean restrict;
    protected boolean ifExists;
    private boolean temporary;
    private boolean external;
    private boolean isDropPartition;
    private SQLExpr where;
    
    public SQLDropTableStatement() {
        this.tableSources = new ArrayList<SQLExprTableSource>();
        this.cascade = false;
        this.restrict = false;
        this.ifExists = false;
        this.temporary = false;
        this.external = false;
    }
    
    public SQLDropTableStatement(final DbType dbType) {
        super(dbType);
        this.tableSources = new ArrayList<SQLExprTableSource>();
        this.cascade = false;
        this.restrict = false;
        this.ifExists = false;
        this.temporary = false;
        this.external = false;
    }
    
    public SQLDropTableStatement(final SQLName name, final DbType dbType) {
        this(new SQLExprTableSource(name), dbType);
    }
    
    public SQLDropTableStatement(final SQLName name) {
        this(name, null);
    }
    
    public SQLDropTableStatement(final SQLExprTableSource tableSource) {
        this(tableSource, null);
    }
    
    public SQLDropTableStatement(final SQLExprTableSource tableSource, final DbType dbType) {
        this(dbType);
        this.tableSources.add(tableSource);
    }
    
    public List<SQLExprTableSource> getTableSources() {
        return this.tableSources;
    }
    
    public void addPartition(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSources.add(tableSource);
    }
    
    public void setName(final SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }
    
    public void addTableSource(final SQLName name) {
        this.addTableSource(new SQLExprTableSource(name));
    }
    
    public void addTableSource(final SQLExprTableSource tableSource) {
        this.tableSources.add(tableSource);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSources);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List getChildren() {
        return this.tableSources;
    }
    
    public boolean isPurge() {
        return this.purge;
    }
    
    public void setPurge(final boolean purge) {
        this.purge = purge;
    }
    
    public boolean isIfExists() {
        return this.ifExists;
    }
    
    public void setIfExists(final boolean ifExists) {
        this.ifExists = ifExists;
    }
    
    public boolean isCascade() {
        return this.cascade;
    }
    
    public void setCascade(final boolean cascade) {
        this.cascade = cascade;
    }
    
    public boolean isRestrict() {
        return this.restrict;
    }
    
    public void setRestrict(final boolean restrict) {
        this.restrict = restrict;
    }
    
    public boolean isTemporary() {
        return this.temporary;
    }
    
    public void setTemporary(final boolean temporary) {
        this.temporary = temporary;
    }
    
    public boolean isExternal() {
        return this.external;
    }
    
    public void setExternal(final boolean external) {
        this.external = external;
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
    
    public boolean isDropPartition() {
        return this.isDropPartition;
    }
    
    public void setDropPartition(final boolean dropPartition) {
        this.isDropPartition = dropPartition;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.where = x;
    }
}
