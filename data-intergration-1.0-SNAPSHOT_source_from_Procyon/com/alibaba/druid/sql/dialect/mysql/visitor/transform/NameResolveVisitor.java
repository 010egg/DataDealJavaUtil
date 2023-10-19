// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor.transform;

import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;

public class NameResolveVisitor extends OracleASTVisitorAdapter
{
    @Override
    public boolean visit(final SQLIdentifierExpr x) {
        SQLObject parent = x.getParent();
        if (parent instanceof SQLBinaryOpExpr && x.getResolvedColumn() == null) {
            final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)parent;
            final boolean isJoinCondition = binaryOpExpr.getLeft() instanceof SQLName && binaryOpExpr.getRight() instanceof SQLName;
            if (isJoinCondition) {
                return false;
            }
        }
        final String name = x.getName();
        if ("ROWNUM".equalsIgnoreCase(name)) {
            return false;
        }
        final long hash = x.nameHashCode64();
        final SQLTableSource tableSource = null;
        if (hash == FnvHash.Constants.LEVEL || hash == FnvHash.Constants.CONNECT_BY_ISCYCLE || hash == FnvHash.Constants.SYSTIMESTAMP) {
            return false;
        }
        if (parent instanceof SQLPropertyExpr) {
            return false;
        }
        while (parent != null) {
            if (parent instanceof SQLTableSource) {
                return false;
            }
            if (parent instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
                if (queryBlock.getInto() != null) {
                    return false;
                }
                if (queryBlock.getParent() instanceof SQLSelect) {
                    final SQLObject pp = queryBlock.getParent().getParent();
                    if (pp instanceof SQLInSubQueryExpr || pp instanceof SQLExistsExpr) {
                        return false;
                    }
                }
                final SQLTableSource from = queryBlock.getFrom();
                if (from instanceof SQLExprTableSource || from instanceof SQLSubqueryTableSource) {
                    final String alias = from.getAlias();
                    if (alias != null) {
                        final boolean isRowNumColumn = this.isRowNumColumn(x, queryBlock);
                        final boolean isAliasColumn = this.isAliasColumn(x, queryBlock);
                        if (!isRowNumColumn && !isAliasColumn) {
                            SQLUtils.replaceInParent(x, new SQLPropertyExpr(alias, name));
                        }
                    }
                }
                return false;
            }
            else {
                parent = parent.getParent();
            }
        }
        return true;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        final String ownerName = x.getOwnernName();
        if (ownerName == null) {
            return super.visit(x);
        }
        for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock queryBlock = (SQLSelectQueryBlock)parent;
                final SQLTableSource tableSource = queryBlock.findTableSource(ownerName);
                if (tableSource != null) {
                    final String alias = tableSource.computeAlias();
                    if (ownerName.equalsIgnoreCase(alias) && !ownerName.equals(alias)) {
                        x.setOwner(alias);
                        break;
                    }
                    break;
                }
            }
        }
        return super.visit(x);
    }
    
    public boolean isRowNumColumn(final SQLExpr x, final SQLSelectQueryBlock source) {
        if (x instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)x;
            final long nameHashCode64 = identifierExpr.nameHashCode64();
            if (nameHashCode64 == FnvHash.Constants.ROWNUM) {
                return true;
            }
            final SQLSelectQueryBlock queryBlock = source;
            if (queryBlock.getFrom() instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)queryBlock.getFrom()).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock subQueryBlock = ((SQLSubqueryTableSource)queryBlock.getFrom()).getSelect().getQueryBlock();
                final SQLSelectItem selectItem = subQueryBlock.findSelectItem(nameHashCode64);
                if (selectItem != null && this.isRowNumColumn(selectItem.getExpr(), subQueryBlock)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean isAliasColumn(final SQLExpr x, final SQLSelectQueryBlock source) {
        if (x instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)x;
            final long nameHashCode64 = identifierExpr.nameHashCode64();
            final SQLSelectQueryBlock queryBlock = source;
            final SQLSelectItem selectItem = queryBlock.findSelectItem(nameHashCode64);
            if (selectItem != null) {
                return true;
            }
            if (queryBlock.getFrom() instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)queryBlock.getFrom()).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock subQueryBlock = ((SQLSubqueryTableSource)queryBlock.getFrom()).getSelect().getQueryBlock();
                if (this.isAliasColumn(x, subQueryBlock)) {
                    return true;
                }
            }
        }
        return false;
    }
}
