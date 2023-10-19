// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import java.util.List;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class ExportConditionColumns extends UDF
{
    public String evaluate(final String sql) {
        return this.evaluate(sql, null);
    }
    
    public String evaluate(final String sql, final String dbTypeName) {
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        try {
            final List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);
            final SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
            for (final SQLStatement stmt : statementList) {
                stmt.accept(visitor);
            }
            final StringBuffer buf = new StringBuffer();
            for (final TableStat.Column column : visitor.getColumns()) {
                if (!column.isWhere() && !column.isJoin()) {
                    continue;
                }
                if (buf.length() != 0) {
                    buf.append(',');
                }
                buf.append(column.toString());
            }
            return buf.toString();
        }
        catch (Exception ex) {
            System.err.println("error sql : " + sql);
            ex.printStackTrace();
            return null;
        }
    }
}
