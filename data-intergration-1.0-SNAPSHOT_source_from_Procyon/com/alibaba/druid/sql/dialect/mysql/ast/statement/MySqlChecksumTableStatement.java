// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.ast.statement;

import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import java.util.List;

public class MySqlChecksumTableStatement extends MySqlStatementImpl
{
    private final List<SQLExprTableSource> tables;
    private boolean quick;
    private boolean extended;
    
    public MySqlChecksumTableStatement() {
        this.tables = new ArrayList<SQLExprTableSource>();
    }
    
    public void addTable(final SQLExprTableSource table) {
        if (table == null) {
            return;
        }
        table.setParent(this);
        this.tables.add(table);
    }
    
    public List<SQLExprTableSource> getTables() {
        return this.tables;
    }
    
    public boolean isQuick() {
        return this.quick;
    }
    
    public void setQuick(final boolean quick) {
        this.quick = quick;
    }
    
    public boolean isExtended() {
        return this.extended;
    }
    
    public void setExtended(final boolean extended) {
        this.extended = extended;
    }
    
    @Override
    public void accept0(final MySqlASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tables);
        }
        visitor.endVisit(this);
    }
}
