// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.visitor;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLUnionOperator;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.statement.SQLSelect;

public class OracleRowNumToLimit extends OracleASTVisitorAdapter
{
    private Context context;
    private boolean removeSelectListRownum;
    
    public OracleRowNumToLimit() {
        this.removeSelectListRownum = true;
    }
    
    @Override
    public boolean visit(final SQLSelect x) {
        if (x.getWithSubQuery() != null) {
            x.getWithSubQuery().accept(this);
        }
        if (x.getQuery() != null) {
            x.getQuery().accept(this);
        }
        final SQLSelectQueryBlock queryBlock = x.getQueryBlock();
        if (queryBlock != null && queryBlock.getLimit() != null) {
            final SQLExpr rowCount = queryBlock.getLimit().getRowCount();
            if (rowCount instanceof SQLIntegerExpr && SQLIntegerExpr.isZero(rowCount)) {
                x.setOrderBy(null);
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final OracleSelectQueryBlock x) {
        this.context = new Context(this.context);
        this.context.queryBlock = x;
        final SQLExpr where = x.getWhere();
        if (where != null) {
            where.accept(this);
        }
        final SQLTableSource from = x.getFrom();
        if (from != null) {
            from.accept(this);
        }
        this.removeSelectListRowNum(x);
        final List<SQLSelectItem> selectList = x.getSelectList();
        for (final SQLSelectItem selectItem : selectList) {
            selectItem.accept(this);
        }
        final SQLExpr startWith = x.getStartWith();
        if (startWith != null) {
            startWith.accept(this);
        }
        boolean allColumn = false;
        if (selectList.size() == 1) {
            final SQLExpr expr = selectList.get(0).getExpr();
            if (expr instanceof SQLAllColumnExpr) {
                allColumn = true;
            }
            else if (expr instanceof SQLPropertyExpr && ((SQLPropertyExpr)expr).getName().equals("*")) {
                allColumn = true;
            }
        }
        if (!allColumn && x.getFrom() instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)x.getFrom()).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
            final SQLSelectQueryBlock subQuery = ((SQLSubqueryTableSource)x.getFrom()).getSelect().getQueryBlock();
            final List<SQLSelectItem> subSelectList = subQuery.getSelectList();
            if (subSelectList.size() >= selectList.size()) {
                boolean match = true;
                for (int i = 0; i < selectList.size(); ++i) {
                    if (!selectList.get(i).equals(subSelectList.get(i))) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    allColumn = true;
                }
            }
        }
        if (x.getParent() instanceof SQLSelect && x.getWhere() == null && x.getOrderBy() == null && allColumn && x.getLimit() != null && x.getFrom() instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)x.getFrom()).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
            final SQLSelect select = (SQLSelect)x.getParent();
            final SQLSelectQueryBlock subQuery2 = ((SQLSubqueryTableSource)x.getFrom()).getSelect().getQueryBlock();
            subQuery2.mergeLimit(x.getLimit());
            x.setLimit(null);
            select.setQuery(subQuery2);
            this.context.queryBlock = subQuery2;
            this.context.fixLimit();
            subQuery2.accept(this);
        }
        if (x.getParent() instanceof SQLUnionQuery && x.getWhere() == null && x.getOrderBy() == null && allColumn && x.getLimit() != null && x.getFrom() instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)x.getFrom()).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
            final SQLUnionQuery union = (SQLUnionQuery)x.getParent();
            final SQLSelectQueryBlock subQuery2 = ((SQLSubqueryTableSource)x.getFrom()).getSelect().getQueryBlock();
            subQuery2.mergeLimit(x.getLimit());
            x.setLimit(null);
            if (union.getLeft() == x) {
                union.setLeft(subQuery2);
            }
            else {
                union.setRight(subQuery2);
            }
            this.context.queryBlock = subQuery2;
            this.context.fixLimit();
            subQuery2.accept(this);
        }
        this.context = this.context.parent;
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionQuery x) {
        if (x.getLeft() != null) {
            x.getLeft().accept(this);
        }
        if (x.getRight() != null) {
            x.getRight().accept(this);
        }
        if (x.getLeft() instanceof SQLSelectQueryBlock && x.getRight() instanceof SQLSelectQueryBlock) {
            if (x.getOperator() == SQLUnionOperator.MINUS) {
                final SQLSelectQueryBlock left = (SQLSelectQueryBlock)x.getLeft().clone();
                SQLSelectQueryBlock right = (SQLSelectQueryBlock)x.getRight().clone();
                left.setLimit(null);
                right.setLimit(null);
                final boolean eqNonLimit = left.toString().equals(right.toString());
                if (eqNonLimit) {
                    final SQLSelectQueryBlock merged = (SQLSelectQueryBlock)x.getLeft().clone();
                    right = (SQLSelectQueryBlock)x.getRight();
                    final SQLLimit leftLimit = merged.getLimit();
                    final SQLLimit rightLimit = right.getLimit();
                    if ((leftLimit == null && rightLimit == null) || (leftLimit != null && leftLimit.equals(rightLimit))) {
                        merged.setLimit(new SQLLimit(0));
                    }
                    else if (leftLimit == null) {
                        final SQLExpr rightOffset = rightLimit.getOffset();
                        if (rightOffset != null && !SQLIntegerExpr.isZero(rightOffset)) {
                            return false;
                        }
                        final SQLLimit limit = new SQLLimit();
                        limit.setOffset(rightLimit.getRowCount());
                        merged.setLimit(limit);
                    }
                    else {
                        final SQLExpr rightOffset = rightLimit.getOffset();
                        if (rightOffset != null && !SQLIntegerExpr.isZero(rightOffset)) {
                            return false;
                        }
                        final SQLExpr leftOffset = leftLimit.getOffset();
                        if (leftOffset != null && !SQLIntegerExpr.isZero(leftOffset)) {
                            return false;
                        }
                        final SQLExpr rightRowCount = rightLimit.getRowCount();
                        final SQLExpr leftRowCount = leftLimit.getRowCount();
                        final SQLLimit limit2 = new SQLLimit();
                        limit2.setOffset(rightRowCount);
                        limit2.setRowCount(substract(leftRowCount, rightRowCount));
                        if (SQLIntegerExpr.isZero(limit2.getRowCount())) {
                            limit2.setRowCount(0);
                            limit2.setOffset(null);
                            if (merged.getOrderBy() != null) {
                                merged.setOrderBy(null);
                            }
                        }
                        merged.setLimit(limit2);
                    }
                    final SQLObject parent = x.getParent();
                    if (parent instanceof SQLSelect) {
                        final SQLSelect select = (SQLSelect)parent;
                        select.setQuery(merged);
                    }
                    else if (parent instanceof SQLUnionQuery) {
                        final SQLUnionQuery union = (SQLUnionQuery)parent;
                        if (union.getLeft() == x) {
                            union.setLeft(merged);
                        }
                        else {
                            union.setRight(merged);
                        }
                    }
                }
            }
            else if (x.getOperator() == SQLUnionOperator.INTERSECT) {
                final SQLSelectQueryBlock left = (SQLSelectQueryBlock)x.getLeft().clone();
                SQLSelectQueryBlock right = (SQLSelectQueryBlock)x.getRight().clone();
                left.setLimit(null);
                right.setLimit(null);
                final boolean eqNonLimit = left.toString().equals(right.toString());
                if (eqNonLimit) {
                    final SQLSelectQueryBlock merged = (SQLSelectQueryBlock)x.getLeft().clone();
                    right = (SQLSelectQueryBlock)x.getRight();
                    final SQLLimit leftLimit = merged.getLimit();
                    final SQLLimit rightLimit = right.getLimit();
                    if (rightLimit != null) {
                        if (!rightLimit.equals(leftLimit)) {
                            if (leftLimit == null) {
                                merged.setLimit(rightLimit.clone());
                            }
                            else {
                                final SQLLimit limit3 = new SQLLimit();
                                final SQLExpr rightOffset2 = rightLimit.getOffset();
                                final SQLExpr leftOffset2 = leftLimit.getOffset();
                                if (leftOffset2 == null) {
                                    limit3.setOffset(rightOffset2);
                                }
                                else if (rightOffset2 == null) {
                                    limit3.setOffset(leftOffset2);
                                }
                                else if (rightOffset2.equals(leftOffset2)) {
                                    limit3.setOffset(leftOffset2);
                                }
                                else {
                                    if (!(leftOffset2 instanceof SQLIntegerExpr) || !(rightOffset2 instanceof SQLIntegerExpr)) {
                                        return false;
                                    }
                                    limit3.setOffset(SQLIntegerExpr.greatst((SQLIntegerExpr)leftOffset2, (SQLIntegerExpr)rightOffset2));
                                }
                                final SQLExpr rightRowCount2 = rightLimit.getRowCount();
                                final SQLExpr leftRowCount2 = leftLimit.getRowCount();
                                final SQLExpr leftEnd = (leftOffset2 == null) ? leftRowCount2 : substract(leftRowCount2, leftOffset2);
                                final SQLExpr rightEnd = (rightOffset2 == null) ? rightRowCount2 : substract(rightRowCount2, rightOffset2);
                                if ((leftEnd != null && !(leftEnd instanceof SQLIntegerExpr)) || (rightEnd != null && !(rightEnd instanceof SQLIntegerExpr))) {
                                    return false;
                                }
                                final SQLIntegerExpr end = SQLIntegerExpr.least((SQLIntegerExpr)leftEnd, (SQLIntegerExpr)rightEnd);
                                if (limit3.getOffset() == null) {
                                    limit3.setRowCount(end);
                                }
                                else {
                                    limit3.setRowCount(substract(end, limit3.getOffset()));
                                }
                                merged.setLimit(limit3);
                            }
                        }
                    }
                    final SQLObject parent = x.getParent();
                    if (parent instanceof SQLSelect) {
                        final SQLSelect select = (SQLSelect)parent;
                        select.setQuery(merged);
                    }
                    else if (parent instanceof SQLUnionQuery) {
                        final SQLUnionQuery union = (SQLUnionQuery)parent;
                        if (union.getLeft() == x) {
                            union.setLeft(merged);
                        }
                        else {
                            union.setRight(merged);
                        }
                    }
                }
            }
        }
        return false;
    }
    
    private void removeSelectListRowNum(final SQLSelectQueryBlock x) {
        final SQLTableSource from = x.getFrom();
        SQLLimit limit = x.getLimit();
        if (limit == null && from instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)from).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
            limit = ((SQLSubqueryTableSource)from).getSelect().getQueryBlock().getLimit();
        }
        if (!this.removeSelectListRownum) {
            return;
        }
        final List<SQLSelectItem> selectList = x.getSelectList();
        for (int i = selectList.size() - 1; i >= 0; --i) {
            final SQLSelectItem selectItem = selectList.get(i);
            final SQLExpr expr = selectItem.getExpr();
            if (this.isRowNum(expr) && limit != null) {
                selectList.remove(i);
            }
        }
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        final SQLExpr left = x.getLeft();
        final SQLExpr right = x.getRight();
        final SQLBinaryOperator op = x.getOperator();
        if (this.context == null || this.context.queryBlock == null) {
            return false;
        }
        final boolean isRowNum = this.isRowNum(left);
        if (isRowNum) {
            if (op == SQLBinaryOperator.LessThan) {
                if (SQLUtils.replaceInParent(x, null)) {
                    this.context.setLimit(decrement(right));
                    this.context.fixLimit();
                }
                return false;
            }
            if (op == SQLBinaryOperator.LessThanOrEqual) {
                if (SQLUtils.replaceInParent(x, null)) {
                    this.context.setLimit(right);
                    this.context.fixLimit();
                }
                return false;
            }
            if (op == SQLBinaryOperator.Equality) {
                if (SQLUtils.replaceInParent(x, null)) {
                    this.context.setLimit(right);
                    this.context.fixLimit();
                }
                return false;
            }
            if (op == SQLBinaryOperator.GreaterThanOrEqual) {
                if (SQLUtils.replaceInParent(x, null)) {
                    this.context.setOffset(decrement(right));
                    this.context.fixLimit();
                }
                return false;
            }
            if (op == SQLBinaryOperator.GreaterThan) {
                if (SQLUtils.replaceInParent(x, null)) {
                    this.context.setOffset(right);
                    this.context.fixLimit();
                }
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean visit(final SQLBetweenExpr x) {
        if (!this.isRowNum(x.getTestExpr())) {
            return true;
        }
        if (SQLUtils.replaceInParent(x, null)) {
            SQLExpr offset = decrement(x.getBeginExpr());
            this.context.setOffset(offset);
            if (offset instanceof SQLIntegerExpr) {
                final int val = ((SQLIntegerExpr)offset).getNumber().intValue();
                if (val < 0) {
                    offset = new SQLIntegerExpr(0);
                }
            }
            this.context.setLimit(substract(x.getEndExpr(), offset));
            final SQLLimit limit = this.context.queryBlock.getLimit();
            if (limit != null) {
                limit.putAttribute("oracle.isFixLimit", Boolean.TRUE);
            }
        }
        return false;
    }
    
    public boolean isRowNum(final SQLExpr x) {
        if (x instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)x;
            final long nameHashCode64 = identifierExpr.nameHashCode64();
            if (nameHashCode64 == FnvHash.Constants.ROWNUM) {
                return true;
            }
            if (this.context != null && this.context.queryBlock != null && this.context.queryBlock.getFrom() instanceof SQLSubqueryTableSource && ((SQLSubqueryTableSource)this.context.queryBlock.getFrom()).getSelect().getQuery() instanceof SQLSelectQueryBlock) {
                final SQLSelectQueryBlock subQueryBlock = ((SQLSubqueryTableSource)this.context.queryBlock.getFrom()).getSelect().getQueryBlock();
                final SQLSelectItem selectItem = subQueryBlock.findSelectItem(nameHashCode64);
                this.context = new Context(this.context);
                this.context.queryBlock = subQueryBlock;
                try {
                    if (selectItem != null && this.isRowNum(selectItem.getExpr())) {
                        return true;
                    }
                }
                finally {
                    this.context = this.context.parent;
                }
            }
        }
        return false;
    }
    
    public static SQLExpr decrement(final SQLExpr x) {
        if (x instanceof SQLIntegerExpr) {
            final int val = ((SQLIntegerExpr)x).getNumber().intValue() - 1;
            return new SQLIntegerExpr(val);
        }
        return new SQLBinaryOpExpr(x.clone(), SQLBinaryOperator.Subtract, new SQLIntegerExpr(1));
    }
    
    public static SQLExpr substract(final SQLExpr left, final SQLExpr right) {
        if (left == null && right == null) {
            return null;
        }
        if (left == null) {
            return null;
        }
        if (left instanceof SQLIntegerExpr && right instanceof SQLIntegerExpr) {
            final int rightVal = Math.max(0, ((SQLIntegerExpr)right).getNumber().intValue());
            final int leftVal = ((SQLIntegerExpr)left).getNumber().intValue();
            int val = leftVal - rightVal;
            if (val < 0) {
                val = 0;
            }
            return new SQLIntegerExpr(val);
        }
        return new SQLBinaryOpExpr(left, SQLBinaryOperator.Subtract, right);
    }
    
    public static SQLExpr increment(final SQLExpr x) {
        if (x instanceof SQLIntegerExpr) {
            final int val = ((SQLIntegerExpr)x).getNumber().intValue() + 1;
            return new SQLIntegerExpr(val);
        }
        return new SQLBinaryOpExpr(x.clone(), SQLBinaryOperator.Add, new SQLIntegerExpr(1));
    }
    
    public static class Context
    {
        public final Context parent;
        public SQLSelectQueryBlock queryBlock;
        
        public Context(final Context parent) {
            this.parent = parent;
        }
        
        void setLimit(SQLExpr x) {
            if (x instanceof SQLIntegerExpr) {
                final int val = ((SQLIntegerExpr)x).getNumber().intValue();
                if (val < 0) {
                    x = new SQLIntegerExpr(0);
                }
            }
            SQLLimit limit = this.queryBlock.getLimit();
            if (limit == null) {
                limit = new SQLLimit();
                this.queryBlock.setLimit(limit);
            }
            limit.setRowCount(x);
        }
        
        void fixLimit() {
            final SQLLimit limit = this.queryBlock.getLimit();
            if (limit == null) {
                return;
            }
            if (limit.getAttribute("oracle.isFixLimit") == Boolean.TRUE) {
                return;
            }
            if (limit.getRowCount() != null && limit.getOffset() != null) {
                if (limit.getRowCount() instanceof SQLIntegerExpr && limit.getOffset() instanceof SQLIntegerExpr) {
                    final SQLIntegerExpr rowCountExpr = SQLIntegerExpr.substract((SQLIntegerExpr)limit.getRowCount(), (SQLIntegerExpr)limit.getOffset());
                    limit.setRowCount(rowCountExpr);
                }
                else {
                    limit.setRowCount(OracleRowNumToLimit.substract(limit.getRowCount(), limit.getOffset()));
                }
                limit.putAttribute("oracle.isFixLimit", Boolean.TRUE);
            }
        }
        
        void setOffset(SQLExpr x) {
            if (x instanceof SQLIntegerExpr) {
                final int val = ((SQLIntegerExpr)x).getNumber().intValue();
                if (val < 0) {
                    x = new SQLIntegerExpr(0);
                }
            }
            SQLLimit limit = this.queryBlock.getLimit();
            if (limit == null) {
                limit = new SQLLimit();
                this.queryBlock.setLimit(limit);
            }
            limit.setOffset(x);
        }
    }
}
