// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.transform;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLType;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.visitor.ParameterizedOutputVisitorUtils;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.DbType;

public class SQLUnifiedUtils
{
    public static long unifyHash(final String sql, final DbType type) {
        final String unifySQL = unifySQL(sql, DbType.mysql);
        return FnvHash.fnv1a_64_lower(unifySQL);
    }
    
    public static String unifySQL(final String sql, final DbType type) {
        if (StringUtils.isEmpty(sql)) {
            throw new IllegalArgumentException("sql is empty.");
        }
        final SQLType sqlType = SQLParserUtils.getSQLType(sql, DbType.mysql);
        String parameterizeSQL = null;
        switch (sqlType) {
            case INSERT:
            case UPDATE:
            case SELECT:
            case DELETE: {
                parameterizeSQL = ParameterizedOutputVisitorUtils.parameterize(sql, DbType.mysql);
                final SQLStatement stmt = SQLUtils.parseSingleStatement(parameterizeSQL, DbType.mysql, SQLParserFeature.EnableSQLBinaryOpExprGroup);
                stmt.accept(new SQLUnifiedVisitor());
                return SQLUtils.toMySqlString(stmt);
            }
            default: {
                return ParameterizedOutputVisitorUtils.parameterize(sql, DbType.mysql);
            }
        }
    }
}
