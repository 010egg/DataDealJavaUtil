// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLAlterIndexStatement extends SQLStatementImpl implements SQLAlterStatement
{
    private SQLName name;
    private SQLName renameTo;
    private SQLExprTableSource table;
    private boolean compile;
    private Boolean enable;
    protected boolean unusable;
    private Boolean monitoringUsage;
    private Rebuild rebuild;
    private SQLExpr parallel;
    private List<SQLAssignItem> partitions;
    protected SQLPartitionBy dbPartitionBy;
    
    public SQLAlterIndexStatement() {
        this.partitions = new ArrayList<SQLAssignItem>();
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.name);
            this.acceptChild(visitor, this.renameTo);
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.partitions);
            this.acceptChild(visitor, this.rebuild);
            this.acceptChild(visitor, this.parallel);
        }
        visitor.endVisit(this);
    }
    
    public SQLName getRenameTo() {
        return this.renameTo;
    }
    
    public void setRenameTo(final SQLName renameTo) {
        this.renameTo = renameTo;
    }
    
    public SQLExpr getParallel() {
        return this.parallel;
    }
    
    public void setParallel(final SQLExpr parallel) {
        this.parallel = parallel;
    }
    
    public Boolean getMonitoringUsage() {
        return this.monitoringUsage;
    }
    
    public void setMonitoringUsage(final Boolean monitoringUsage) {
        this.monitoringUsage = monitoringUsage;
    }
    
    public Rebuild getRebuild() {
        return this.rebuild;
    }
    
    public void setRebuild(final Rebuild rebuild) {
        this.rebuild = rebuild;
    }
    
    public SQLName getName() {
        return this.name;
    }
    
    public void setName(final SQLName name) {
        this.name = name;
    }
    
    public boolean isCompile() {
        return this.compile;
    }
    
    public void setCompile(final boolean compile) {
        this.compile = compile;
    }
    
    public Boolean getEnable() {
        return this.enable;
    }
    
    public void setEnable(final Boolean enable) {
        this.enable = enable;
    }
    
    public boolean isUnusable() {
        return this.unusable;
    }
    
    public void setUnusable(final boolean unusable) {
        this.unusable = unusable;
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName x) {
        this.setTable(new SQLExprTableSource(x));
    }
    
    public void setTable(final SQLExprTableSource x) {
        if (x != null) {
            x.setParent(this);
        }
        this.table = x;
    }
    
    public List<SQLAssignItem> getPartitions() {
        return this.partitions;
    }
    
    public SQLPartitionBy getDbPartitionBy() {
        return this.dbPartitionBy;
    }
    
    public void setDbPartitionBy(final SQLPartitionBy x) {
        if (x != null) {
            x.setParent(this);
        }
        this.dbPartitionBy = x;
    }
    
    public static class Rebuild extends SQLObjectImpl
    {
        private SQLObject option;
        
        public SQLObject getOption() {
            return this.option;
        }
        
        public void setOption(final SQLObject option) {
            this.option = option;
        }
        
        public void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                this.acceptChild(visitor, this.option);
            }
            visitor.endVisit(this);
        }
    }
}
