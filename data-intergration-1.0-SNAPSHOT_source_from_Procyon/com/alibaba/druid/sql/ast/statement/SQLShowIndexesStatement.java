// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLStatementImpl;

public class SQLShowIndexesStatement extends SQLStatementImpl implements SQLShowStatement
{
    private SQLExprTableSource table;
    private List<SQLCommentHint> hints;
    private SQLExpr where;
    private String type;
    
    public SQLExprTableSource getTable() {
        return this.table;
    }
    
    public void setTable(final SQLName table) {
        this.setTable(new SQLExprTableSource(table));
    }
    
    public void setTable(final SQLExprTableSource table) {
        this.table = table;
    }
    
    public SQLName getDatabase() {
        final SQLExpr expr = this.table.getExpr();
        if (expr instanceof SQLPropertyExpr) {
            return (SQLName)((SQLPropertyExpr)expr).getOwner();
        }
        return null;
    }
    
    public SQLExpr getWhere() {
        return this.where;
    }
    
    public void setWhere(final SQLExpr where) {
        this.where = where;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setDatabase(final String database) {
        this.table.setSchema(database);
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.table);
            this.acceptChild(visitor, this.where);
        }
        visitor.endVisit(this);
    }
    
    public List<SQLCommentHint> getHints() {
        return this.hints;
    }
    
    public void setHints(final List<SQLCommentHint> hints) {
        this.hints = hints;
    }
}
