// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.visitor;

import java.util.Collection;
import java.util.ArrayList;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import java.util.LinkedHashMap;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import java.util.List;
import java.util.Map;
import com.alibaba.druid.DbType;

public class SQLSubQueryGroupVisitor extends SQLASTVisitorAdapter
{
    private final DbType dbType;
    protected Map<Long, List<SQLSubqueryTableSource>> tableSourceMap;
    
    public SQLSubQueryGroupVisitor(final DbType dbType) {
        this.tableSourceMap = new LinkedHashMap<Long, List<SQLSubqueryTableSource>>();
        this.dbType = dbType;
    }
    
    @Override
    public boolean visit(final SQLSubqueryTableSource x) {
        final String sql = SQLUtils.toSQLString(x.getSelect(), this.dbType);
        final long hashCode64 = FnvHash.fnv1a_64(sql);
        List<SQLSubqueryTableSource> list = this.tableSourceMap.get(hashCode64);
        if (list == null) {
            list = new ArrayList<SQLSubqueryTableSource>();
            this.tableSourceMap.put(hashCode64, list);
        }
        list.add(x);
        return true;
    }
    
    public Collection<List<SQLSubqueryTableSource>> getGroupedSubqueryTableSources() {
        return this.tableSourceMap.values();
    }
}
