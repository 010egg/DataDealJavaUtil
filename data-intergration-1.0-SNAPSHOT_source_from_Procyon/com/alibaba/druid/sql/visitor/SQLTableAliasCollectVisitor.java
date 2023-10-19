// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLValuesTableSource;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import java.util.LinkedHashMap;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import java.util.Map;

public class SQLTableAliasCollectVisitor extends SQLASTVisitorAdapter
{
    protected Map<Long, SQLTableSource> tableSourceMap;
    protected volatile int seed;
    
    public SQLTableAliasCollectVisitor() {
        this.tableSourceMap = new LinkedHashMap<Long, SQLTableSource>();
    }
    
    @Override
    public boolean visit(final SQLLateralViewTableSource x) {
        final String alias = x.getAlias();
        if (alias == null) {
            return false;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        this.tableSourceMap.put(hashCode64, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLValuesTableSource x) {
        final String alias = x.getAlias();
        if (alias == null) {
            return false;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        this.tableSourceMap.put(hashCode64, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLUnionQueryTableSource x) {
        final String alias = x.getAlias();
        if (alias == null) {
            x.getUnion().accept(this);
            return false;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        this.tableSourceMap.put(hashCode64, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        final String alias = x.getAlias();
        if (alias == null) {
            x.getSelect().accept(this);
            return false;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        this.tableSourceMap.put(hashCode64, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLJoinTableSource x) {
        final String alias = x.getAlias();
        if (alias == null) {
            return true;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        this.tableSourceMap.put(hashCode64, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLWithSubqueryClause.Entry x) {
        final String alias = x.getAlias();
        if (alias == null) {
            return true;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        this.tableSourceMap.put(hashCode64, x);
        return true;
    }
    
    @Override
    public boolean visit(final SQLExprTableSource x) {
        final String alias = x.getAlias();
        if (alias != null) {
            return true;
        }
        final SQLExpr expr = x.getExpr();
        if (expr instanceof SQLName) {
            final long hashCode64 = ((SQLName)expr).nameHashCode64();
            this.tableSourceMap.put(hashCode64, x);
            return false;
        }
        return true;
    }
    
    public Collection<SQLTableSource> getTableSources() {
        return this.tableSourceMap.values();
    }
    
    public SQLTableSource getTableSource(final long hashCode64) {
        return this.tableSourceMap.get(hashCode64);
    }
    
    public boolean containsTableSource(final String alias) {
        if (alias == null) {
            return false;
        }
        final long hashCode64 = FnvHash.hashCode64(alias);
        return this.tableSourceMap.containsKey(hashCode64);
    }
    
    public String genAlias(int seed) {
        String alias = null;
        while (seed < 100) {
            final String str = "G" + seed++;
            if (!this.containsTableSource(str)) {
                alias = str;
                this.seed = seed;
                break;
            }
        }
        return alias;
    }
    
    public int getSeed() {
        return this.seed;
    }
}
