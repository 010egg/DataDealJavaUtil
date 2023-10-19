// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import java.util.Iterator;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.druid.stat.TableStat;
import java.util.List;
import java.util.ArrayList;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.aliyun.odps.udf.UDF;

public class ExportConditions extends UDF
{
    public String evaluate(final String sql) {
        return this.evaluate(sql, null);
    }
    
    public String evaluate(final String sql, final String dbType) {
        return this.evaluate(sql, dbType, null);
    }
    
    public String evaluate(final String sql, final String dbTypeName, final Boolean compactValues) {
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        try {
            final List<SQLStatement> statementList = SQLUtils.parseStatements(sql, dbType);
            final SchemaStatVisitor visitor = SQLUtils.createSchemaStatVisitor(dbType);
            for (final SQLStatement stmt : statementList) {
                stmt.accept(visitor);
            }
            final List<List<Object>> rows = new ArrayList<List<Object>>();
            final List<TableStat.Condition> conditions = visitor.getConditions();
            for (int i = 0; i < conditions.size(); ++i) {
                final TableStat.Condition condition = conditions.get(i);
                final TableStat.Column column = condition.getColumn();
                final String operator = condition.getOperator();
                final List<Object> values = condition.getValues();
                final List<Object> row = new ArrayList<Object>();
                row.add(column.getTable());
                row.add(column.getName());
                row.add(operator);
                if (values.size() == 0) {
                    row.add(null);
                }
                else if (values.size() == 1) {
                    if (compactValues != null && compactValues) {
                        row.add(values);
                    }
                    else {
                        row.add(values.get(0));
                    }
                }
                else {
                    row.add(values);
                }
                rows.add(row);
            }
            return JSONUtils.toJSONString(rows);
        }
        catch (Exception ex) {
            System.err.println("error sql : " + sql);
            ex.printStackTrace();
            return null;
        }
    }
}
