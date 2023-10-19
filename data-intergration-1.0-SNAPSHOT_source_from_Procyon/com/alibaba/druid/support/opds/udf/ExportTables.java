// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import java.util.List;
import com.alibaba.druid.stat.TableStat;
import java.util.Map;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class ExportTables extends UDF
{
    public String evaluate(final String sql) throws Throwable {
        return this.evaluate(sql, null, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName) throws Throwable {
        return this.evaluate(sql, dbTypeName, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName, final boolean throwError) throws Throwable {
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        final Throwable error = null;
        try {
            final List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);
            final SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
            for (final SQLStatement stmt : statementList) {
                stmt.accept(visitor);
            }
            final StringBuffer buf = new StringBuffer();
            for (final Map.Entry<TableStat.Name, TableStat> entry : visitor.getTables().entrySet()) {
                final TableStat.Name name = entry.getKey();
                if (buf.length() != 0) {
                    buf.append(',');
                }
                buf.append(name.toString());
            }
            return buf.toString();
        }
        catch (Exception ex) {}
        catch (StackOverflowError stackOverflowError) {}
        if (throwError && error != null) {
            throw error;
        }
        return null;
    }
}
