// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.transform;

import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLUnionQuery;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.Collections;
import java.util.Comparator;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.repository.SchemaResolveVisitor;
import com.alibaba.druid.sql.repository.SchemaRepository;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlASTVisitorAdapter;

public class SQLUnifiedVisitor extends MySqlASTVisitorAdapter
{
    @Override
    public boolean visit(final MySqlSelectQueryBlock x) {
        final SchemaRepository repository = new SchemaRepository(DbType.mysql);
        repository.resolve(x, new SchemaResolveVisitor.Option[0]);
        final List<SQLSelectItem> selectList = x.getSelectList();
        for (final SQLSelectItem item : selectList) {
            if (item.getExpr() instanceof SQLPropertyExpr) {
                final SQLPropertyExpr expr = (SQLPropertyExpr)item.getExpr();
                final SQLTableSource resolvedTableSource = expr.getResolvedTableSource();
                if (resolvedTableSource != null) {
                    final String alias = resolvedTableSource.getAlias();
                    if (alias != null) {
                        expr.setOwner(alias);
                    }
                    else {
                        expr.setOwner(resolvedTableSource.computeAlias());
                    }
                }
            }
            item.setAlias(null);
        }
        Collections.sort(selectList, new Comparator<SQLSelectItem>() {
            @Override
            public int compare(final SQLSelectItem o1, final SQLSelectItem o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
        if (x.getFrom() != null) {
            x.getFrom().accept(this);
        }
        if (x.getWhere() != null) {
            x.getWhere().accept(this);
        }
        if (x.getGroupBy() != null) {
            x.getGroupBy().accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        final SQLBinaryOperator operator = x.getOperator();
        if (operator == SQLBinaryOperator.BooleanOr || operator == SQLBinaryOperator.BooleanAnd) {
            final SQLExpr left = x.getLeft();
            left.accept(this);
            final SQLExpr right = x.getRight();
            right.accept(this);
            final int compareResult = left.toString().compareToIgnoreCase(right.toString());
            if (compareResult > 0) {
                x.setLeft(right);
                x.setRight(left);
            }
        }
        else {
            final SQLExpr left = x.getLeft();
            left.accept(this);
            final SQLExpr right = x.getRight();
            right.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLPropertyExpr x) {
        final SQLExpr owner = x.getOwner();
        final SQLTableSource resolvedTableSource = x.getResolvedTableSource();
        if (resolvedTableSource != null) {
            final String alias = resolvedTableSource.getAlias();
            if (alias != null) {
                x.setOwner(alias);
            }
            else {
                x.setOwner(resolvedTableSource.computeAlias());
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLBinaryOpExprGroup x) {
        Collections.sort(x.getItems(), new Comparator<SQLExpr>() {
            @Override
            public int compare(final SQLExpr o1, final SQLExpr o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
        return false;
    }
    
    @Override
    public boolean visit(final SQLUnionQuery x) {
        final SQLSelectQuery left = x.getLeft();
        final SQLSelectQuery right = x.getRight();
        left.accept(this);
        right.accept(this);
        final int compareResult = left.toString().compareToIgnoreCase(right.toString());
        if (compareResult > 0) {
            x.setLeft(right);
            x.setRight(left);
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        final String tablename = ((SQLName)x.getExpr()).getSimpleName();
        x.setAlias(tablename);
        return false;
    }
    
    @Override
    public boolean visit(final SQLSelectGroupByClause x) {
        Collections.sort(x.getItems(), new Comparator<SQLExpr>() {
            @Override
            public int compare(final SQLExpr o1, final SQLExpr o2) {
                return o1.toString().compareToIgnoreCase(o2.toString());
            }
        });
        final SQLExpr having = x.getHaving();
        if (having != null) {
            having.accept(this);
        }
        return false;
    }
}
