// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLAlterStatement;

public class OdpsAddStatisticStatement extends OdpsStatementImpl implements SQLAlterStatement
{
    private SQLExprTableSource table;
    private OdpsStatisticClause statisticClause;
    
    @Override
    protected void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
        }
        visitor.endVisit(this);
    }
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLExprTableSource table) {
        if (table != null) {
            table.setParent(table);
        }
        this.table = table;
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
    }
    
    public OdpsStatisticClause getStatisticClause() {
        return this.statisticClause;
    }
    
    public void setStatisticClause(final OdpsStatisticClause statisticClause) {
        this.statisticClause = statisticClause;
    }
}
