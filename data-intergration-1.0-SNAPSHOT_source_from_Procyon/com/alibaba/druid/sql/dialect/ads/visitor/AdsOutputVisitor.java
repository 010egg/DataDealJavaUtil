// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.ads.visitor;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class AdsOutputVisitor extends SQLASTOutputVisitor implements AdsVisitor
{
    public AdsOutputVisitor(final Appendable appender) {
        super(appender);
    }
    
    public AdsOutputVisitor(final Appendable appender, final DbType dbType) {
        super(appender, dbType);
    }
    
    public AdsOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
    }
    
    @Override
    public boolean visit(final SQLCreateTableStatement x) {
        this.printCreateTable(x, true);
        final List<SQLAssignItem> options = x.getTableOptions();
        if (options.size() > 0) {
            this.println();
            this.print0(this.ucase ? "OPTIONS (" : "options (");
            this.printAndAccept(options, ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableAddColumn x) {
        this.print0(this.ucase ? "ADD COLUMN " : "add column ");
        this.printAndAccept(x.getColumns(), ", ");
        return false;
    }
    
    @Override
    public boolean visit(final SQLShowColumnsStatement x) {
        this.print0(this.ucase ? "SHOW COLUMNS" : "show columns");
        if (x.getTable() != null) {
            this.print0(this.ucase ? " IN " : " in ");
            x.getTable().accept(this);
        }
        return false;
    }
    
    @Override
    public void endVisit(final MySqlPrimaryKey x) {
    }
    
    @Override
    public void endVisit(final MySqlCreateTableStatement x) {
    }
}
