// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.h2.visitor;

import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class H2OutputVisitor extends SQLASTOutputVisitor implements H2ASTVisitor
{
    public H2OutputVisitor(final Appendable appender) {
        super(appender, DbType.h2);
    }
    
    public H2OutputVisitor(final Appendable appender, final DbType dbType) {
        super(appender, dbType);
    }
    
    public H2OutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
        this.dbType = DbType.h2;
    }
    
    @Override
    public boolean visit(final SQLReplaceStatement x) {
        this.print0(this.ucase ? "MERGE INTO " : "merge into ");
        this.printTableSourceExpr(x.getTableName());
        final List<SQLExpr> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print0(this.ucase ? " KEY (" : " key (");
            for (int i = 0, size = columns.size(); i < size; ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                final SQLExpr columnn = columns.get(i);
                this.printExpr(columnn, this.parameterized);
            }
            this.print(')');
        }
        final List<SQLInsertStatement.ValuesClause> valuesClauseList = x.getValuesList();
        if (valuesClauseList.size() != 0) {
            this.println();
            this.print0(this.ucase ? "VALUES " : "values ");
            final int size = valuesClauseList.size();
            if (size == 0) {
                this.print0("()");
            }
            else {
                for (int j = 0; j < size; ++j) {
                    if (j != 0) {
                        this.print0(", ");
                    }
                    this.visit(valuesClauseList.get(j));
                }
            }
        }
        final SQLQueryExpr query = x.getQuery();
        if (query != null) {
            this.visit(query);
        }
        return false;
    }
}
