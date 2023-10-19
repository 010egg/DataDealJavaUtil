// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLAnalyzeTableStatement;

public class MySqlAnalyzeStatement extends SQLAnalyzeTableStatement implements MySqlStatement
{
    private boolean noWriteToBinlog;
    private boolean local;
    
    public MySqlAnalyzeStatement() {
        this.noWriteToBinlog = false;
        this.local = false;
        super.dbType = DbType.mysql;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor instanceof MySqlASTVisitor) {
            this.accept0((MySqlASTVisitor)visitor);
        }
        else {
            super.accept0(visitor);
        }
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
