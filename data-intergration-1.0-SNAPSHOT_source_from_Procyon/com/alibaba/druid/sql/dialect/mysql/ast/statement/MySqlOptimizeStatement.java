// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.List;

public class MySqlOptimizeStatement extends MySqlStatementImpl
{
    private boolean noWriteToBinlog;
    private boolean local;
    protected final List<SQLExprTableSource> tableSources;
    
    public MySqlOptimizeStatement() {
        this.noWriteToBinlog = false;
        this.local = false;
        this.tableSources = new ArrayList<SQLExprTableSource>();
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSources);
        }
        visitor.endVisit(this);
    }
    
    public boolean isNoWriteToBinlog() {
        return this.noWriteToBinlog;
    }
    
    public void setNoWriteToBinlog(final boolean noWriteToBinlog) {
        this.noWriteToBinlog = noWriteToBinlog;
    }
    
    public boolean isLocal() {
        return this.local;
    }
    
    public void setLocal(final boolean local) {
        this.local = local;
    }
    
    public List<SQLExprTableSource> getTableSources() {
        return this.tableSources;
    }
    
    public void addTableSource(final SQLExprTableSource tableSource) {
        if (tableSource != null) {
            tableSource.setParent(this);
        }
        this.tableSources.add(tableSource);
    }
}
