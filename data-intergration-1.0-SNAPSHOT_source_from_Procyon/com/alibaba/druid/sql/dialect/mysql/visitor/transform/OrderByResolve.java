// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor.transform;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitorAdapter;

public class OrderByResolve extends OracleASTVisitorAdapter
{
    static final long DBMS_RANDOM_VALUE;
    
    @Override
    public boolean visit(final SQLSelect x) {
        final SQLSelectQueryBlock queryBlock = x.getQueryBlock();
        if (queryBlock == null) {
            return super.visit(x);
        }
        if (x.getOrderBy() != null && queryBlock.isForUpdate() && queryBlock.getOrderBy() == null) {
            queryBlock.setOrderBy(x.getOrderBy());
            x.setOrderBy(null);
        }
        final SQLOrderBy orderBy = queryBlock.getOrderBy();
        if (orderBy == null) {
            return super.visit(x);
        }
        if (!queryBlock.selectItemHasAllColumn(false)) {
            final List<SQLSelectOrderByItem> notContainsOrderBy = new ArrayList<SQLSelectOrderByItem>();
            for (final SQLSelectOrderByItem orderByItem : orderBy.getItems()) {
                final SQLExpr orderByExpr = orderByItem.getExpr();
                if (orderByExpr instanceof SQLName) {
                    if (((SQLName)orderByExpr).hashCode64() == OrderByResolve.DBMS_RANDOM_VALUE) {
                        continue;
                    }
                    final long hashCode64 = ((SQLName)orderByExpr).nameHashCode64();
                    final SQLSelectItem selectItem = queryBlock.findSelectItem(hashCode64);
                    if (selectItem != null) {
                        continue;
                    }
                    queryBlock.addSelectItem(orderByExpr.clone());
                }
            }
            if (notContainsOrderBy.size() > 0) {
                for (final SQLSelectOrderByItem orderByItem : notContainsOrderBy) {
                    queryBlock.addSelectItem(orderByItem.getExpr());
                }
                final OracleSelectQueryBlock queryBlock2 = new OracleSelectQueryBlock();
                queryBlock2.setFrom(queryBlock, "x");
                x.setQuery(queryBlock2);
            }
        }
        return super.visit(x);
    }
    
    static {
        DBMS_RANDOM_VALUE = FnvHash.hashCode64("DBMS_RANDOM.value");
    }
}
