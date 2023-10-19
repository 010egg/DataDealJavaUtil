// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.semantic;

import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.visitor.SQLASTVisitorAdapter;

public class SemanticCheck extends SQLASTVisitorAdapter
{
    @Override
    public boolean visit(final SQLCreateTableStatement stmt) {
        stmt.containsDuplicateColumnNames(true);
        return true;
    }
    
    public static boolean check(final String sql, final DbType dbType) {
        final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
        final SemanticCheck v = new SemanticCheck();
        for (final SQLStatement stmt : stmtList) {
            stmt.accept(v);
        }
        return false;
    }
}
