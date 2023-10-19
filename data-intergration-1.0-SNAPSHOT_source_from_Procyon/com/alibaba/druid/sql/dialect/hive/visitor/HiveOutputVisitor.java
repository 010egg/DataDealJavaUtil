// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.visitor;

import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableExchangePartition;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveMsckRepairStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveLoadDataStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveCreateFunctionStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.statement.SQLMergeStatement;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;

public class HiveOutputVisitor extends SQLASTOutputVisitor implements HiveASTVisitor
{
    public HiveOutputVisitor(final Appendable appender) {
        super(appender, DbType.hive);
        super.quote = '`';
    }
    
    public HiveOutputVisitor(final Appendable appender, final DbType dbType) {
        super(appender, dbType);
        super.quote = '`';
    }
    
    public HiveOutputVisitor(final Appendable appender, final boolean parameterized) {
        super(appender, parameterized);
        super.quote = '`';
        this.dbType = DbType.hive;
    }
    
    @Override
    public boolean visit(final HiveInsert x) {
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        if (x.getInsertBeforeCommentsDirect() != null) {
            this.printlnComments(x.getInsertBeforeCommentsDirect());
        }
        final SQLExprTableSource tableSource = x.getTableSource();
        if (tableSource != null) {
            if (x.isOverwrite()) {
                this.print0(this.ucase ? "INSERT OVERWRITE TABLE " : "insert overwrite table ");
            }
            else {
                this.print0(this.ucase ? "INSERT INTO TABLE " : "insert into table ");
            }
            tableSource.accept(this);
        }
        final List<SQLAssignItem> partitions = x.getPartitions();
        if (partitions != null) {
            final int partitionsSize = partitions.size();
            if (partitionsSize > 0) {
                this.print0(this.ucase ? " PARTITION (" : " partition (");
                for (int i = 0; i < partitionsSize; ++i) {
                    if (i != 0) {
                        this.print0(", ");
                    }
                    final SQLAssignItem assign = partitions.get(i);
                    assign.getTarget().accept(this);
                    if (assign.getValue() != null) {
                        this.print('=');
                        assign.getValue().accept(this);
                    }
                }
                this.print(')');
            }
            this.println();
        }
        final SQLSelect select = x.getQuery();
        final List<SQLInsertStatement.ValuesClause> valuesList = x.getValuesList();
        if (select != null) {
            select.accept(this);
        }
        else if (!valuesList.isEmpty()) {
            this.print0(this.ucase ? "VALUES " : "values ");
            this.printAndAccept(valuesList, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLExternalRecordFormat x) {
        return this.hiveVisit(x);
    }
    
    @Override
    public boolean visit(final HiveMultiInsertStatement x) {
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            this.visit(with);
            this.println();
        }
        final SQLTableSource from = x.getFrom();
        if (x.getFrom() != null) {
            if (from instanceof SQLSubqueryTableSource) {
                final SQLSelect select = ((SQLSubqueryTableSource)from).getSelect();
                this.print0(this.ucase ? "FROM (" : "from (");
                ++this.indentCount;
                this.println();
                select.accept(this);
                --this.indentCount;
                this.println();
                this.print0(") ");
                final String alias = x.getFrom().getAlias();
                if (alias != null) {
                    this.print0(alias);
                }
            }
            else {
                this.print0(this.ucase ? "FROM " : "from ");
                from.accept(this);
            }
            this.println();
        }
        for (int i = 0; i < x.getItems().size(); ++i) {
            final HiveInsert insert = x.getItems().get(i);
            if (i != 0) {
                this.println();
            }
            insert.accept(this);
        }
        return false;
    }
    
    @Override
    public boolean visit(final HiveInsertStatement x) {
        if (x.hasBeforeComment()) {
            this.printlnComments(x.getBeforeCommentsDirect());
        }
        final SQLWithSubqueryClause with = x.getWith();
        if (with != null) {
            this.visit(with);
            this.println();
        }
        if (x.isOverwrite()) {
            this.print0(this.ucase ? "INSERT OVERWRITE TABLE " : "insert overwrite table ");
        }
        else {
            this.print0(this.ucase ? "INSERT INTO TABLE " : "insert into table ");
        }
        x.getTableSource().accept(this);
        final List<SQLAssignItem> partitions = x.getPartitions();
        final int partitionSize = partitions.size();
        if (partitionSize > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            for (int i = 0; i < partitionSize; ++i) {
                if (i != 0) {
                    this.print0(", ");
                }
                final SQLAssignItem assign = partitions.get(i);
                assign.getTarget().accept(this);
                if (assign.getValue() != null) {
                    this.print('=');
                    assign.getValue().accept(this);
                }
            }
            this.print(')');
        }
        final List<SQLExpr> columns = x.getColumns();
        if (columns.size() > 0) {
            this.print(" (");
            this.printAndAccept(columns, ", ");
            this.print(')');
        }
        if (x.isIfNotExists()) {
            this.print0(this.ucase ? " IF NOT EXISTS" : " if not exists");
        }
        this.println();
        final SQLSelect select = x.getQuery();
        final List<SQLInsertStatement.ValuesClause> valuesList = x.getValuesList();
        if (select != null) {
            select.accept(this);
        }
        else if (!valuesList.isEmpty()) {
            this.print0(this.ucase ? "VALUES " : "values ");
            this.printAndAccept(valuesList, ", ");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLMergeStatement.MergeUpdateClause x) {
        this.print0(this.ucase ? "WHEN MATCHED " : "when matched ");
        ++this.indentCount;
        final SQLExpr where = x.getWhere();
        if (where != null) {
            ++this.indentCount;
            if (SQLBinaryOpExpr.isAnd(where)) {
                this.println();
            }
            else {
                this.print(' ');
            }
            this.print0(this.ucase ? "AND " : "and ");
            this.printExpr(where, this.parameterized);
            --this.indentCount;
            this.println();
        }
        this.print0(this.ucase ? "UPDATE SET " : "update set ");
        this.printAndAccept(x.getItems(), ", ");
        --this.indentCount;
        final SQLExpr deleteWhere = x.getDeleteWhere();
        if (deleteWhere != null) {
            this.println();
            this.print0(this.ucase ? "WHEN MATCHED AND " : "when matched and ");
            this.printExpr(deleteWhere, this.parameterized);
            this.print0(this.ucase ? " DELETE" : " delete");
        }
        return false;
    }
    
    @Override
    public boolean visit(final HiveCreateFunctionStatement x) {
        if (x.isTemporary()) {
            this.print0(this.ucase ? "CREATE TEMPORARY FUNCTION " : "create temporary function ");
        }
        else {
            this.print0(this.ucase ? "CREATE FUNCTION " : "create function ");
        }
        x.getName().accept(this);
        final SQLExpr className = x.getClassName();
        if (className != null) {
            this.print0(this.ucase ? " AS " : " as ");
            className.accept(this);
        }
        ++this.indentCount;
        final SQLExpr location = x.getLocation();
        final HiveCreateFunctionStatement.ResourceType resourceType = x.getResourceType();
        if (location != null) {
            this.println();
            if (resourceType != null) {
                this.print0(this.ucase ? "USING " : "using ");
                this.print0(resourceType.name());
                this.print(' ');
            }
            else {
                this.print0(this.ucase ? "LOCATION " : "location ");
            }
            location.accept(this);
        }
        final String code = x.getCode();
        if (code != null) {
            this.println();
            this.print0(this.ucase ? "USING" : "using");
            this.print0(code);
        }
        final SQLExpr symbol = x.getSymbol();
        if (symbol != null) {
            this.println();
            this.print0(this.ucase ? "SYMBOL = " : "symbol = ");
            symbol.accept(this);
        }
        --this.indentCount;
        return false;
    }
    
    @Override
    public boolean visit(final HiveLoadDataStatement x) {
        this.print0(this.ucase ? "LOAD DATA " : "load data ");
        if (x.isLocal()) {
            this.print0(this.ucase ? "LOCAL " : "local ");
        }
        this.print0(this.ucase ? "INPATH " : "inpath ");
        x.getInpath().accept(this);
        if (x.isOverwrite()) {
            this.print0(this.ucase ? " OVERWRITE INTO TABLE " : " overwrite into table ");
        }
        else {
            this.print0(this.ucase ? " INTO TABLE " : " into table ");
        }
        x.getInto().accept(this);
        if (x.getPartition().size() > 0) {
            this.print0(this.ucase ? " PARTITION (" : " partition (");
            this.printAndAccept(x.getPartition(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final HiveMsckRepairStatement x) {
        final List<SQLCommentHint> headHints = x.getHeadHintsDirect();
        if (headHints != null) {
            for (final SQLCommentHint hint : headHints) {
                hint.accept(this);
                this.println();
            }
        }
        this.print0(this.ucase ? "MSCK REPAIR" : "msck repair");
        final SQLName database = x.getDatabase();
        if (database != null) {
            this.print0(this.ucase ? " DATABASE " : " database ");
            database.accept(this);
        }
        final SQLExprTableSource table = x.getTable();
        if (table != null) {
            this.print0(this.ucase ? " TABLE " : " table ");
            table.accept(this);
        }
        if (x.isAddPartitions()) {
            this.print0(this.ucase ? " ADD PARTITIONS" : " add partitions");
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLAlterTableExchangePartition x) {
        this.print0(this.ucase ? "EXCHANGE PARTITION (" : "exchange partition (");
        this.printAndAccept(x.getPartitions(), ", ");
        this.print0(this.ucase ? ") WITH TABLE " : ") with table ");
        x.getTable().accept(this);
        final Boolean validation = x.getValidation();
        if (validation != null) {
            if (validation) {
                this.print0(this.ucase ? " WITH VALIDATION" : " with validation");
            }
            else {
                this.print0(this.ucase ? " WITHOUT VALIDATION" : " without validation");
            }
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCreateIndexStatement x) {
        this.print0(this.ucase ? "CREATE " : "create ");
        this.print0(this.ucase ? "INDEX " : "index ");
        x.getName().accept(this);
        this.print0(this.ucase ? " ON TABLE " : " on table ");
        x.getTable().accept(this);
        this.print0(" (");
        this.printAndAccept(x.getItems(), ", ");
        this.print(')');
        final String type = x.getType();
        if (type != null) {
            this.print0(this.ucase ? " AS " : " as ");
            this.print0(type);
        }
        if (x.isDeferedRebuild()) {
            this.print0(this.ucase ? " WITH DEFERRED REBUILD" : " with deferred rebuild");
        }
        if (x.getProperties().size() > 0) {
            this.print0(this.ucase ? " IDXPROPERTIES (" : " idxproperties (");
            this.printAndAccept(x.getProperties(), ", ");
            this.print(')');
        }
        final String using = x.getUsing();
        if (using != null) {
            this.print0(this.ucase ? " USING " : " using ");
            this.print0(using);
        }
        final SQLExpr comment = x.getComment();
        if (comment != null) {
            this.print0(this.ucase ? " COMMENT " : " comment ");
            comment.accept(this);
        }
        final SQLTableSource in = x.getIn();
        if (in != null) {
            this.print0(this.ucase ? " IN TABLE " : " in table ");
            in.accept(this);
        }
        final SQLExternalRecordFormat format = x.getRowFormat();
        if (format != null) {
            this.println();
            this.print0(this.ucase ? "ROW FORMAT DELIMITED " : "row rowFormat delimited ");
            this.visit(format);
        }
        final SQLName storedAs = x.getStoredAs();
        if (storedAs != null) {
            this.print0(this.ucase ? " STORED BY " : " stored by ");
            storedAs.accept(this);
        }
        if (x.getTableProperties().size() > 0) {
            this.print0(this.ucase ? " TBLPROPERTIES (" : " tblproperties (");
            this.printAndAccept(x.getTableProperties(), ", ");
            this.print(')');
        }
        return false;
    }
    
    @Override
    public boolean visit(final SQLCharExpr x, final boolean parameterized) {
        final String text = x.getText();
        if (text == null) {
            this.print0(this.ucase ? "NULL" : "null");
        }
        else {
            final StringBuilder buf = new StringBuilder(text.length() + 2);
            buf.append('\'');
            for (int i = 0; i < text.length(); ++i) {
                final char ch = text.charAt(i);
                switch (ch) {
                    case '\\': {
                        buf.append("\\\\");
                        break;
                    }
                    case '\'': {
                        buf.append("\\'");
                        break;
                    }
                    case '\0': {
                        buf.append("\\0");
                        break;
                    }
                    case '\n': {
                        buf.append("\\n");
                        break;
                    }
                    case '\r': {
                        buf.append("\\r");
                        break;
                    }
                    case '\b': {
                        buf.append("\\b");
                        break;
                    }
                    case '\t': {
                        buf.append("\\t");
                        break;
                    }
                    default: {
                        if (ch == '\u2605') {
                            buf.append("\\u2605");
                            break;
                        }
                        if (ch == '\u25bc') {
                            buf.append("\\u25bc");
                            break;
                        }
                        buf.append(ch);
                        break;
                    }
                }
            }
            buf.append('\'');
            this.print0(buf.toString());
        }
        return false;
    }
}
