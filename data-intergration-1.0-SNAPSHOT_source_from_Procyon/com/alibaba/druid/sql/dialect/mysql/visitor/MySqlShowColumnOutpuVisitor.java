// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.visitor;

import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import java.util.Iterator;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.ArrayList;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;

public class MySqlShowColumnOutpuVisitor extends MySqlOutputVisitor
{
    public MySqlShowColumnOutpuVisitor(final Appendable appender) {
        super(appender);
    }
    
    @Override
    public boolean visit(final MySqlCreateTableStatement x) {
        final List<SQLColumnDefinition> columns = new ArrayList<SQLColumnDefinition>();
        final List<String> dataTypes = new ArrayList<String>();
        final List<String> defaultValues = new ArrayList<String>();
        int name_len = -1;
        int dataType_len = -1;
        int defaultVal_len = 7;
        int extra_len = 5;
        for (final SQLTableElement element : x.getTableElementList()) {
            if (element instanceof SQLColumnDefinition) {
                final SQLColumnDefinition column = (SQLColumnDefinition)element;
                columns.add(column);
                final String name = SQLUtils.normalize(column.getName().getSimpleName());
                if (name_len < name.length()) {
                    name_len = name.length();
                }
                String dataType = column.getDataType().getName();
                if (column.getDataType().getArguments().size() > 0) {
                    dataType += "(";
                    for (int i = 0; i < column.getDataType().getArguments().size(); ++i) {
                        if (i != 0) {
                            dataType += ",";
                        }
                        final SQLExpr arg = column.getDataType().getArguments().get(i);
                        dataType += arg.toString();
                    }
                    dataType += ")";
                }
                if (dataType_len < dataType.length()) {
                    dataType_len = dataType.length();
                }
                dataTypes.add(dataType);
                if (column.getDefaultExpr() == null) {
                    defaultValues.add(null);
                }
                else {
                    String defaultVal = SQLUtils.toMySqlString(column.getDefaultExpr());
                    if (defaultVal.length() > 2 && defaultVal.charAt(0) == '\'' && defaultVal.charAt(defaultVal.length() - 1) == '\'') {
                        defaultVal = defaultVal.substring(1, defaultVal.length() - 1);
                    }
                    defaultValues.add(defaultVal);
                    if (defaultVal_len < defaultVal.length()) {
                        defaultVal_len = defaultVal.length();
                    }
                }
                if (column.isAutoIncrement()) {
                    extra_len = "auto_increment".length();
                }
                else {
                    if (column.getOnUpdate() == null) {
                        continue;
                    }
                    extra_len = "on update CURRENT_TIMESTAMP".length();
                }
            }
        }
        this.print("+-");
        this.print('-', name_len);
        this.print("-+-");
        this.print('-', dataType_len);
        this.print("-+------+-----+-");
        this.print('-', defaultVal_len);
        this.print("-+-");
        this.print('-', extra_len);
        this.print("-+\n");
        this.print("| ");
        this.print("Field", name_len, ' ');
        this.print(" | ");
        this.print("Type", dataType_len, ' ');
        this.print(" | Null | Key | ");
        this.print("Default", defaultVal_len, ' ');
        this.print(" | ");
        this.print("Extra", extra_len, ' ');
        this.print(" |\n");
        this.print("+-");
        this.print('-', name_len);
        this.print("-+-");
        this.print('-', dataType_len);
        this.print("-+------+-----+-");
        this.print('-', defaultVal_len);
        this.print("-+-");
        this.print('-', extra_len);
        this.print("-+\n");
        for (int j = 0; j < columns.size(); ++j) {
            final SQLColumnDefinition column2 = columns.get(j);
            final String name2 = SQLUtils.normalize(column2.getName().getSimpleName());
            this.print("| ");
            this.print(name2, name_len, ' ');
            this.print(" | ");
            this.print(dataTypes.get(j), dataType_len, ' ');
            this.print(" | ");
            if (column2.containsNotNullConstaint()) {
                this.print("NO ");
            }
            else {
                this.print("YES");
            }
            this.print("  | ");
            final MySqlUnique unique = null;
            if (x.isPrimaryColumn(name2)) {
                this.print("PRI");
            }
            else if (x.isUNI(name2)) {
                this.print("UNI");
            }
            else if (x.isMUL(name2)) {
                this.print("MUL");
            }
            else {
                this.print("   ");
            }
            this.print(" | ");
            final String defaultVal2 = defaultValues.get(j);
            if (defaultVal2 == null) {
                this.print("NULL", defaultVal_len, ' ');
            }
            else {
                this.print(defaultVal2, defaultVal_len, ' ');
            }
            this.print(" | ");
            if (column2.isAutoIncrement()) {
                this.print("auto_increment", extra_len, ' ');
            }
            else if (column2.getOnUpdate() != null) {
                this.print("on update CURRENT_TIMESTAMP", extra_len, ' ');
            }
            else {
                this.print(' ', extra_len);
            }
            this.print(" |");
            this.print("\n");
        }
        this.print("+-");
        this.print('-', name_len);
        this.print("-+-");
        this.print('-', dataType_len);
        this.print("-+------+-----+-");
        this.print('-', defaultVal_len);
        this.print("-+-");
        this.print('-', extra_len);
        this.print("-+\n");
        return false;
    }
    
    void print(final char ch, final int count) {
        for (int i = 0; i < count; ++i) {
            this.print(ch);
        }
    }
    
    void print(final String text, final int columnSize, final char ch) {
        this.print(text);
        this.print(' ', columnSize - text.length());
    }
}
