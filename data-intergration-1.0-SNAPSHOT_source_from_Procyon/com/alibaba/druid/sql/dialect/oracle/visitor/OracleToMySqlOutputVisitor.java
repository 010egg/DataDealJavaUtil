// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;

public class OracleToMySqlOutputVisitor extends OracleOutputVisitor
{
    public OracleToMySqlOutputVisitor(final Appendable appender, final boolean printPostSemi) {
        super(appender, printPostSemi);
    }
    
    public OracleToMySqlOutputVisitor(final Appendable appender) {
        super(appender);
    }
    
    @Override
    public boolean visit(final OracleSelectQueryBlock x) {
        boolean parentIsSelectStatment = false;
        if (x.getParent() instanceof SQLSelect) {
            final SQLSelect select = (SQLSelect)x.getParent();
            if (select.getParent() instanceof SQLSelectStatement || select.getParent() instanceof SQLSubqueryTableSource) {
                parentIsSelectStatment = true;
            }
        }
        if (!parentIsSelectStatment) {
            return super.visit(x);
        }
        if (x.getWhere() instanceof SQLBinaryOpExpr && x.getFrom() instanceof SQLSubqueryTableSource) {
            final SQLBinaryOpExpr where = (SQLBinaryOpExpr)x.getWhere();
            if (!(where.getRight() instanceof SQLIntegerExpr) || !(where.getLeft() instanceof SQLIdentifierExpr)) {
                return super.visit(x);
            }
            final int rownum = ((SQLIntegerExpr)where.getRight()).getNumber().intValue();
            final String ident = ((SQLIdentifierExpr)where.getLeft()).getName();
            final SQLSelect select2 = ((SQLSubqueryTableSource)x.getFrom()).getSelect();
            SQLSelectQueryBlock queryBlock = null;
            SQLSelect subSelect = null;
            SQLBinaryOpExpr subWhere = null;
            boolean isSubQueryRowNumMapping = false;
            if (select2.getQuery() instanceof SQLSelectQueryBlock) {
                queryBlock = (SQLSelectQueryBlock)select2.getQuery();
                if (queryBlock.getWhere() instanceof SQLBinaryOpExpr) {
                    subWhere = (SQLBinaryOpExpr)queryBlock.getWhere();
                }
                for (final SQLSelectItem selectItem : queryBlock.getSelectList()) {
                    if (isRowNumber(selectItem.getExpr()) && where.getLeft() instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)where.getLeft()).getName().equals(selectItem.getAlias())) {
                        isSubQueryRowNumMapping = true;
                    }
                }
                final SQLTableSource subTableSource = queryBlock.getFrom();
                if (subTableSource instanceof SQLSubqueryTableSource) {
                    subSelect = ((SQLSubqueryTableSource)subTableSource).getSelect();
                }
            }
            if ("ROWNUM".equalsIgnoreCase(ident)) {
                final SQLBinaryOperator op = where.getOperator();
                Integer limit = null;
                if (op == SQLBinaryOperator.LessThanOrEqual) {
                    limit = rownum;
                }
                else if (op == SQLBinaryOperator.LessThan) {
                    limit = rownum - 1;
                }
                if (limit != null) {
                    select2.accept(this);
                    this.println();
                    this.print0(this.ucase ? "LIMIT " : "limit ");
                    this.print(limit);
                    return false;
                }
            }
            else if (isSubQueryRowNumMapping) {
                final SQLBinaryOperator op = where.getOperator();
                final SQLBinaryOperator subOp = subWhere.getOperator();
                if (isRowNumber(subWhere.getLeft()) && subWhere.getRight() instanceof SQLIntegerExpr) {
                    final int subRownum = ((SQLIntegerExpr)subWhere.getRight()).getNumber().intValue();
                    Integer offset = null;
                    if (op == SQLBinaryOperator.GreaterThanOrEqual) {
                        offset = rownum + 1;
                    }
                    else if (op == SQLBinaryOperator.GreaterThan) {
                        offset = rownum;
                    }
                    if (offset != null) {
                        Integer limit2 = null;
                        if (subOp == SQLBinaryOperator.LessThanOrEqual) {
                            limit2 = subRownum - offset;
                        }
                        else if (subOp == SQLBinaryOperator.LessThan) {
                            limit2 = subRownum - 1 - offset;
                        }
                        if (limit2 != null) {
                            subSelect.accept(this);
                            this.println();
                            this.print0(this.ucase ? "LIMIT " : "limit ");
                            this.print(offset);
                            this.print0(", ");
                            this.print(limit2);
                            return false;
                        }
                    }
                }
            }
        }
        return super.visit(x);
    }
    
    static boolean isRowNumber(final SQLExpr expr) {
        return expr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)expr).hashCode64() == FnvHash.Constants.ROWNUM;
    }
}
