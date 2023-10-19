// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.stmt;

import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.dialect.hive.visitor.HiveASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Map;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class HiveLoadDataStatement extends SQLStatementImpl
{
    private boolean local;
    private SQLExpr inpath;
    private boolean overwrite;
    private SQLExprTableSource into;
    private final List<SQLExpr> partition;
    private SQLExternalRecordFormat format;
    private SQLExpr storedBy;
    private SQLExpr storedAs;
    private SQLExpr rowFormat;
    protected Map<String, SQLObject> serdeProperties;
    protected SQLExpr using;
    
    public HiveLoadDataStatement() {
        super(DbType.hive);
        this.partition = new ArrayList<SQLExpr>(4);
        this.serdeProperties = new LinkedHashMap<String, SQLObject>();
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof HiveASTVisitor) {
            this.accept0((HiveASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
    }
    
    protected void accept0(final HiveASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.inpath);
            this.acceptChild(visitor, this.into);
            this.acceptChild(visitor, this.partition);
            this.acceptChild(visitor, this.storedAs);
            this.acceptChild(visitor, this.storedBy);
            this.acceptChild(visitor, this.rowFormat);
        }
        visitor.endVisit(this);
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public SQLExpr getInpath() {
        return this.inpath;
    }
    
    public void setInpath(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.inpath = x;
    }
    
    public boolean isOverwrite() {
        return this.overwrite;
    }
    
    public void setOverwrite(final boolean overwrite) {
        this.overwrite = overwrite;
    }
    
    public SQLExprTableSource getInto() {
        return this.into;
    }
    
    public void setInto(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.into = x;
    }
    
    public void setInto(final SQLExpr x) {
        if (x == null) {
            this.into = null;
            return;
        }
        this.setInto(new SQLExprTableSource(x));
    }
    
    public List<SQLExpr> getPartition() {
        return this.partition;
    }
    
    public void addPartion(final SQLAssignItem item) {
        if (item != null) {
            item.setParent(this);
        }
        this.partition.add(item);
    }
    
    public SQLExternalRecordFormat getFormat() {
        return this.format;
    }
    
    public void setFormat(final SQLExternalRecordFormat x) {
        if (x != null) {
            x.setParent(this);
        }
        this.format = x;
    }
    
    public SQLExpr getStoredBy() {
        return this.storedBy;
    }
    
    public void setStoredBy(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedBy = x;
    }
    
    public SQLExpr getStoredAs() {
        return this.storedAs;
    }
    
    public void setStoredAs(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.storedAs = x;
    }
    
    public SQLExpr getRowFormat() {
        return this.rowFormat;
    }
    
    public void setRowFormat(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.rowFormat = x;
    }
    
    public SQLExpr getUsing() {
        return this.using;
    }
    
    public void setUsing(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.using = x;
    }
    
    public Map<String, SQLObject> getSerdeProperties() {
        return this.serdeProperties;
    }
}
