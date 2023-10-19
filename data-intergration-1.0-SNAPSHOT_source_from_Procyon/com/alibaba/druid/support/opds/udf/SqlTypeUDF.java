// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.opds.udf;

import java.util.Set;
import com.alibaba.druid.sql.parser.Lexer;
import java.util.Iterator;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.parser.Token;
import java.util.TreeSet;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsDeclareVariableStatement;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsQueryAliasStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowUsersStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticListStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowStatisticStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowRecylebinStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowGrantsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowCatalogsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowStatement;
import com.alibaba.druid.sql.ast.statement.SQLPurgeTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropViewStatement;
import com.alibaba.druid.sql.ast.statement.SQLDropTableStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableChangeOwner;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableTouch;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenameColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetComment;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetOption;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddColumn;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRename;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDisableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableEnableLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableSetLifecycle;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableRenamePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableMergePartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableDropPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAddPartition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableItem;
import com.alibaba.druid.sql.parser.SQLType;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableStatement;
import com.aliyun.odps.udf.UDF;

public class SqlTypeUDF extends UDF
{
    public String evaluate(final String sql) {
        return this.evaluate(sql, null, false);
    }
    
    public String evaluate(final String sql, final String dbTypeName) {
        return this.evaluate(sql, dbTypeName, false);
    }
    
    static SQLType getSqlType(final SQLAlterTableStatement stmt) {
        final List<SQLAlterTableItem> items = stmt.getItems();
        SQLAlterTableItem item = null;
        if (items.size() == 1) {
            item = items.get(0);
        }
        else if (items.size() > 1) {
            item = items.get(0);
            for (int i = 1; i < items.size(); ++i) {
                final SQLAlterTableItem t = items.get(i);
                if (t != null && t.getClass() != item.getClass()) {
                    item = null;
                    break;
                }
            }
        }
        SQLType sqlType = SQLType.ALTER_TABLE;
        if (item instanceof SQLAlterTableAddPartition) {
            sqlType = SQLType.ALTER_TABLE_ADD_PARTITION;
        }
        else if (item instanceof SQLAlterTableDropPartition) {
            sqlType = SQLType.ALTER_TABLE_DROP_PARTITION;
        }
        else if (item instanceof SQLAlterTableMergePartition) {
            sqlType = SQLType.ALTER_TABLE_MERGE_PARTITION;
        }
        else if (item instanceof SQLAlterTableRenamePartition) {
            sqlType = SQLType.ALTER_TABLE_RENAME_PARTITION;
        }
        else if (item instanceof SQLAlterTableSetLifecycle) {
            sqlType = SQLType.ALTER_TABLE_SET_LIFECYCLE;
        }
        else if (item instanceof SQLAlterTableEnableLifecycle) {
            sqlType = SQLType.ALTER_TABLE_ENABLE_LIFECYCLE;
        }
        else if (item instanceof SQLAlterTableDisableLifecycle) {
            sqlType = SQLType.ALTER_TABLE_DISABLE_LIFECYCLE;
        }
        else if (item instanceof SQLAlterTableRename) {
            sqlType = SQLType.ALTER_TABLE_RENAME;
        }
        else if (item instanceof SQLAlterTableAddColumn) {
            sqlType = SQLType.ALTER_TABLE_ADD_COLUMN;
        }
        else if (item instanceof SQLAlterTableAlterColumn) {
            sqlType = SQLType.ALTER_TABLE_ALTER_COLUMN;
        }
        else if (item instanceof SQLAlterTableSetOption) {
            sqlType = SQLType.ALTER_TABLE_SET_TBLPROPERTIES;
        }
        else if (item instanceof SQLAlterTableSetComment) {
            sqlType = SQLType.ALTER_TABLE_SET_COMMENT;
        }
        else if (item instanceof SQLAlterTableRenameColumn) {
            sqlType = SQLType.ALTER_TABLE_RENAME_COLUMN;
        }
        else if (item instanceof SQLAlterTableTouch) {
            sqlType = SQLType.ALTER_TABLE_TOUCH;
        }
        else if (item instanceof SQLAlterTableChangeOwner) {
            sqlType = SQLType.ALTER_TABLE_CHANGE_OWNER;
        }
        return sqlType;
    }
    
    static SQLType getStmtSqlType(final SQLStatement stmt, SQLType sqlType) {
        if (stmt instanceof SQLCreateTableStatement) {
            if (((SQLCreateTableStatement)stmt).getSelect() != null) {
                sqlType = SQLType.CREATE_TABLE_AS_SELECT;
            }
            else {
                sqlType = SQLType.CREATE_TABLE;
            }
        }
        else if (stmt instanceof HiveInsertStatement) {
            final HiveInsertStatement hiveInsert = (HiveInsertStatement)stmt;
            sqlType = (hiveInsert.isOverwrite() ? SQLType.INSERT_OVERWRITE : SQLType.INSERT_INTO);
            if (hiveInsert.getQuery() != null) {
                sqlType = (hiveInsert.isOverwrite() ? SQLType.INSERT_OVERWRITE_SELECT : SQLType.INSERT_INTO_SELECT);
            }
            else if (hiveInsert.getValuesList().size() > 0) {
                sqlType = (hiveInsert.isOverwrite() ? SQLType.INSERT_OVERWRITE_VALUES : SQLType.INSERT_INTO_VALUES);
            }
        }
        else if (stmt instanceof SQLUpdateStatement) {
            sqlType = SQLType.UPDATE;
        }
        else if (stmt instanceof SQLDeleteStatement) {
            sqlType = SQLType.DELETE;
        }
        else if (stmt instanceof SQLSelectStatement) {
            sqlType = SQLType.SELECT;
        }
        else if (stmt instanceof HiveMultiInsertStatement) {
            sqlType = SQLType.INSERT_MULTI;
        }
        else if (stmt instanceof SQLDropTableStatement) {
            sqlType = SQLType.DROP_TABLE;
        }
        else if (stmt instanceof SQLDropViewStatement) {
            sqlType = SQLType.DROP_VIEW;
        }
        else if (stmt instanceof SQLPurgeTableStatement) {
            sqlType = SQLType.PURGE;
        }
        else if (stmt instanceof SQLShowStatement) {
            if (stmt instanceof SQLShowCatalogsStatement) {
                sqlType = SQLType.SHOW_CATALOGS;
            }
            else if (stmt instanceof SQLShowCreateTableStatement) {
                sqlType = SQLType.SHOW_CREATE_TABLE;
            }
            else if (stmt instanceof SQLShowGrantsStatement) {
                sqlType = SQLType.SHOW_GRANTS;
            }
            else if (stmt instanceof SQLShowRecylebinStatement) {
                sqlType = SQLType.SHOW_RECYCLEBIN;
            }
            else if (stmt instanceof SQLShowStatisticStmt) {
                sqlType = SQLType.SHOW_STATISTIC;
            }
            else if (stmt instanceof SQLShowStatisticListStmt) {
                sqlType = SQLType.SHOW_STATISTIC_LIST;
            }
            else if (stmt instanceof SQLShowTablesStatement) {
                sqlType = SQLType.SHOW_TABLES;
            }
            else if (stmt instanceof SQLShowUsersStatement) {
                sqlType = SQLType.SHOW_USERS;
            }
            else if (stmt instanceof SQLShowPartitionsStmt) {
                sqlType = SQLType.SHOW_PARTITIONS;
            }
            else if (sqlType == null) {
                sqlType = SQLType.SHOW;
            }
        }
        else if (stmt instanceof OdpsQueryAliasStatement || stmt instanceof OdpsDeclareVariableStatement) {
            sqlType = SQLType.SCRIPT;
        }
        else if (stmt instanceof SQLAlterTableStatement) {
            sqlType = getSqlType((SQLAlterTableStatement)stmt);
        }
        return sqlType;
    }
    
    public String evaluate(String sql, final String dbTypeName, final boolean throwError) {
        if (sql == null || sql.isEmpty()) {
            return null;
        }
        final DbType dbType = (dbTypeName == null) ? null : DbType.valueOf(dbTypeName);
        SQLType sqlType = null;
        try {
            final List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);
            if (stmtList.size() == 0) {
                return SQLType.EMPTY.name();
            }
            int setCnt = 0;
            int notSetCnt = 0;
            SQLStatement notSetStmt = null;
            for (final SQLStatement stmt : stmtList) {
                if (stmt instanceof SQLSetStatement) {
                    ++setCnt;
                }
                else {
                    notSetStmt = stmt;
                    ++notSetCnt;
                }
            }
            if (setCnt == stmtList.size()) {
                final Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
                sqlType = lexer.scanSQLTypeV2();
                if (sqlType != null) {
                    return sqlType.name();
                }
                return SQLType.SET.name();
            }
            else if (notSetCnt == 1) {
                final Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
                sqlType = lexer.scanSQLTypeV2();
                sqlType = getStmtSqlType(notSetStmt, sqlType);
            }
            else {
                for (final SQLStatement stmt : stmtList) {
                    if (stmt instanceof OdpsQueryAliasStatement || stmt instanceof OdpsDeclareVariableStatement) {
                        return SQLType.SCRIPT.name();
                    }
                }
                final Set<String> typeNameSet = new TreeSet<String>();
                for (final SQLStatement stmt2 : stmtList) {
                    if (stmt2 instanceof SQLSetStatement) {
                        continue;
                    }
                    final SQLType type = getStmtSqlType(stmt2, null);
                    if (type == null) {
                        continue;
                    }
                    typeNameSet.add(type.name());
                }
                if (typeNameSet.size() == 1) {
                    return typeNameSet.stream().findFirst().get();
                }
                if (typeNameSet.size() > 0) {
                    final StringBuffer buf = new StringBuffer();
                    for (final String s : typeNameSet) {
                        if (buf.length() != 0) {
                            buf.append(',');
                        }
                        buf.append(s);
                    }
                    return "MULTI:" + buf.toString();
                }
                sqlType = SQLType.MULTI;
            }
        }
        catch (ParserException ex2) {
            sql = sql.trim();
            final int semiIndex = sql.indexOf(59);
            if (semiIndex == sql.length() - 1 || (semiIndex == -1 && sql.indexOf(10) == -1)) {
                final String singleLineSqlType = this.getSqlTypeForSingleLineSql(sql, dbType);
                if (singleLineSqlType != null) {
                    return singleLineSqlType;
                }
            }
            else if (sql.lastIndexOf(10, semiIndex) == -1) {
                final String firstSql = sql.substring(0, semiIndex).trim().toLowerCase();
                if (firstSql.startsWith("set ")) {
                    final String restSql = sql.substring(semiIndex + 1);
                    return this.evaluate(restSql, dbTypeName, throwError);
                }
            }
            Label_0697: {
                try {
                    final Lexer lexer2 = SQLParserUtils.createLexer(sql, dbType);
                    int semiCnt = 0;
                    Token token = null;
                    while (true) {
                        lexer2.nextToken();
                        if (token == Token.VARIANT && lexer2.token() == Token.COLONEQ) {
                            return SQLType.SCRIPT.name();
                        }
                        token = lexer2.token();
                        switch (token) {
                            case EOF:
                            case ERROR: {
                                break Label_0697;
                            }
                            case SEMI: {
                                ++semiCnt;
                                continue;
                            }
                            default: {
                                continue;
                            }
                        }
                    }
                }
                catch (ParserException ex3) {}
            }
            sqlType = SQLType.ERROR;
        }
        catch (Throwable ex) {
            if (throwError) {
                throw new IllegalArgumentException("error sql : \n" + sql, ex);
            }
            sqlType = SQLType.ERROR;
        }
        if (sqlType != null) {
            return sqlType.name();
        }
        return SQLType.UNKNOWN.name();
    }
    
    private String getSqlTypeForSingleLineSql(final String sql, final DbType dbType) {
        try {
            Lexer lexer = SQLParserUtils.createLexer(sql, dbType);
            final SQLType sqlType = lexer.scanSQLTypeV2();
            if (sqlType == null) {
                return null;
            }
            Label_0194: {
                if (sqlType == SQLType.WITH) {
                    lexer = SQLParserUtils.createLexer(sql, dbType);
                    int updateCnt = 0;
                    int insertCnt = 0;
                    int deleteCnt = 0;
                    Token token = null;
                    while (true) {
                        final Token last = token;
                        lexer.nextToken();
                        token = lexer.token();
                        if (last == Token.INSERT) {
                            if (token == Token.OVERWRITE) {
                                return SQLType.INSERT_OVERWRITE_SELECT.name();
                            }
                            if (token == Token.INTO) {
                                return SQLType.INSERT_INTO_SELECT.name();
                            }
                        }
                        switch (token) {
                            case EOF:
                            case ERROR: {
                                if (updateCnt == 0 && insertCnt == 0 && deleteCnt == 0) {
                                    return SQLType.SELECT.name();
                                }
                                break Label_0194;
                            }
                            case INSERT: {
                                ++insertCnt;
                                continue;
                            }
                            case DELETE: {
                                ++deleteCnt;
                                continue;
                            }
                            case UPDATE: {
                                ++updateCnt;
                                continue;
                            }
                        }
                    }
                }
            }
            return sqlType.name();
        }
        catch (ParserException ex) {
            return null;
        }
    }
}
