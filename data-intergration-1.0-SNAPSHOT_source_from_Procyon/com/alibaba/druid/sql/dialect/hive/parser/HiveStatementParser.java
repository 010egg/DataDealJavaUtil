// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.parser;

import com.alibaba.druid.sql.ast.statement.SQLAlterDatabaseStatement;
import com.alibaba.druid.sql.ast.statement.SQLImportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExportTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateFunctionStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import java.util.Collection;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowDatabasesStatement;
import com.alibaba.druid.sql.dialect.hive.stmt.HiveLoadDataStatement;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLShowPartitionsStmt;
import com.alibaba.druid.sql.ast.statement.SQLShowIndexesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowViewsStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLStatementImpl;
import java.util.List;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInsert;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.hive.ast.HiveMultiInsertStatement;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class HiveStatementParser extends SQLStatementParser
{
    public HiveStatementParser(final String sql) {
        super(new HiveExprParser(sql));
        this.dbType = DbType.hive;
    }
    
    public HiveStatementParser(final String sql, final SQLParserFeature... features) {
        super(new HiveExprParser(sql, features));
        this.dbType = DbType.hive;
    }
    
    public HiveStatementParser(final Lexer lexer) {
        super(new HiveExprParser(lexer));
        this.dbType = DbType.hive;
    }
    
    @Override
    public HiveSelectParser createSQLSelectParser() {
        return new HiveSelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new HiveCreateTableParser(this.exprParser);
    }
    
    @Override
    public SQLStatement parseInsert() {
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            final HiveMultiInsertStatement stmt = new HiveMultiInsertStatement();
            if (this.lexer.token() == Token.IDENTIFIER) {
                final SQLName tableName = this.exprParser.name();
                final SQLExprTableSource from = new SQLExprTableSource(tableName);
                final SQLTableSource tableSource = this.createSQLSelectParser().parseTableSourceRest(from);
                stmt.setFrom(tableSource);
                if (this.lexer.token() == Token.IDENTIFIER) {
                    from.setAlias(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
            }
            else {
                this.accept(Token.LPAREN);
                final SQLSelectParser selectParser = this.createSQLSelectParser();
                final SQLSelect select = selectParser.select();
                this.accept(Token.RPAREN);
                final String alias = this.lexer.stringVal();
                this.accept(Token.IDENTIFIER);
                SQLTableSource from2 = new SQLSubqueryTableSource(select, alias);
                switch (this.lexer.token()) {
                    case LEFT:
                    case RIGHT:
                    case FULL:
                    case JOIN: {
                        from2 = selectParser.parseTableSourceRest(from2);
                        break;
                    }
                }
                stmt.setFrom(from2);
            }
            do {
                final HiveInsert insert = this.parseHiveInsert();
                stmt.addItem(insert);
            } while (this.lexer.token() == Token.INSERT);
            return stmt;
        }
        return this.parseHiveInsertStmt();
    }
    
    @Override
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        if (this.lexer.token() == Token.HINT) {
            final List<SQLCommentHint> hints = this.exprParser.parseHints();
            final boolean tddlHints = false;
            final boolean accept = false;
            boolean acceptHint = false;
            switch (this.lexer.token()) {
                case SELECT:
                case WITH:
                case DELETE:
                case UPDATE:
                case INSERT:
                case SHOW:
                case REPLACE:
                case TRUNCATE:
                case DROP:
                case ALTER:
                case CREATE:
                case CHECK:
                case SET:
                case DESC:
                case OPTIMIZE:
                case ANALYZE:
                case KILL:
                case EXPLAIN:
                case LPAREN: {
                    acceptHint = true;
                    break;
                }
            }
            if (this.lexer.identifierEquals("MSCK")) {
                acceptHint = true;
            }
            if (acceptHint) {
                final SQLStatementImpl stmt = (SQLStatementImpl)this.parseStatement();
                stmt.setHeadHints(hints);
                statementList.add(stmt);
                return true;
            }
        }
        if (this.lexer.token() == Token.FROM) {
            final SQLStatement stmt2 = this.parseInsert();
            statementList.add(stmt2);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LOAD)) {
            final HiveLoadDataStatement stmt3 = this.parseLoad();
            statementList.add(stmt3);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ANALYZE)) {
            final SQLStatement stmt2 = this.parseAnalyze();
            statementList.add(stmt2);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXPORT)) {
            final SQLStatement stmt2 = this.parseExport();
            statementList.add(stmt2);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IMPORT)) {
            final SQLStatement stmt2 = this.parseImport();
            statementList.add(stmt2);
            return true;
        }
        if (this.lexer.identifierEquals("MSCK")) {
            final SQLStatement stmt2 = this.parseMsck();
            statementList.add(stmt2);
            return true;
        }
        if (!this.lexer.identifierEquals(FnvHash.Constants.SHOW)) {
            return false;
        }
        final Lexer.SavePoint savePoint = this.lexer.mark();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.VIEWS)) {
            this.lexer.nextToken();
            final SQLShowViewsStatement stmt4 = new SQLShowViewsStatement();
            if (this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                final SQLName db = this.exprParser.name();
                stmt4.setDatabase(db);
            }
            if (this.lexer.token() == Token.LIKE) {
                this.lexer.nextToken();
                final SQLExpr pattern = this.exprParser.expr();
                stmt4.setLike(pattern);
            }
            statementList.add(stmt4);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
            this.lexer.reset(savePoint);
            final SQLStatement stmt5 = this.parseShowTables();
            statementList.add(stmt5);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DATABASES)) {
            this.lexer.nextToken();
            final SQLShowDatabasesStatement stmt6 = this.parseShowDatabases(false);
            statementList.add(stmt6);
            return true;
        }
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.nextToken();
            final SQLShowIndexesStatement stmt7 = new SQLShowIndexesStatement();
            stmt7.setType("INDEX");
            if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                final SQLName table = this.exprParser.name();
                stmt7.setTable(table);
            }
            if (this.lexer.token() == Token.HINT) {
                stmt7.setHints(this.exprParser.parseHints());
            }
            statementList.add(stmt7);
            return true;
        }
        if (this.lexer.token() == Token.CREATE) {
            final SQLShowCreateTableStatement stmt8 = this.parseShowCreateTable();
            statementList.add(stmt8);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
            this.lexer.nextToken();
            final SQLShowPartitionsStmt stmt9 = new SQLShowPartitionsStmt();
            if (this.lexer.token() == Token.FROM) {
                this.lexer.nextToken();
            }
            final SQLExpr expr = this.exprParser.expr();
            stmt9.setTableSource(new SQLExprTableSource(expr));
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                this.parseAssignItems(stmt9.getPartition(), stmt9, false);
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token() == Token.WHERE) {
                this.lexer.nextToken();
                stmt9.setWhere(this.exprParser.expr());
            }
            statementList.add(stmt9);
            return true;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLUMNS)) {
            this.lexer.nextToken();
            final SQLShowColumnsStatement stmt10 = new SQLShowColumnsStatement();
            if (this.lexer.token() == Token.FROM || this.lexer.token() == Token.IN) {
                this.lexer.nextToken();
                SQLName table = this.exprParser.name();
                if (this.lexer.token() == Token.SUB && table instanceof SQLIdentifierExpr) {
                    this.lexer.mark();
                    this.lexer.nextToken();
                    final String strVal = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (table instanceof SQLIdentifierExpr) {
                        final SQLIdentifierExpr ident = (SQLIdentifierExpr)table;
                        table = new SQLIdentifierExpr(ident.getName() + "-" + strVal);
                    }
                }
                stmt10.setTable(table);
            }
            if (this.lexer.token() == Token.LIKE) {
                this.lexer.nextToken();
                final SQLExpr like = this.exprParser.expr();
                stmt10.setLike(like);
            }
            if (this.lexer.token() == Token.WHERE) {
                this.lexer.nextToken();
                final SQLExpr where = this.exprParser.expr();
                stmt10.setWhere(where);
            }
            statementList.add(stmt10);
            return true;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    protected HiveLoadDataStatement parseLoad() {
        this.acceptIdentifier("LOAD");
        this.acceptIdentifier("DATA");
        final HiveLoadDataStatement stmt = new HiveLoadDataStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
            this.lexer.nextToken();
            stmt.setLocal(true);
        }
        this.acceptIdentifier("INPATH");
        final SQLExpr inpath = this.exprParser.expr();
        stmt.setInpath(inpath);
        if (this.lexer.token() == Token.OVERWRITE) {
            this.lexer.nextToken();
            stmt.setOverwrite(true);
        }
        this.accept(Token.INTO);
        this.accept(Token.TABLE);
        final SQLExpr table = this.exprParser.expr();
        stmt.setInto(table);
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprParser.exprList(stmt.getPartition(), stmt);
            this.accept(Token.RPAREN);
        }
        return stmt;
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable() {
        final SQLCreateTableParser parser = new HiveCreateTableParser(this.exprParser);
        return parser.parseCreateTable();
    }
    
    @Override
    public SQLCreateFunctionStatement parseCreateFunction() {
        return this.parseHiveCreateFunction();
    }
    
    @Override
    public SQLCreateIndexStatement parseCreateIndex(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        this.accept(Token.INDEX);
        final SQLCreateIndexStatement stmt = new SQLCreateIndexStatement(this.dbType);
        stmt.setName(this.exprParser.name());
        this.accept(Token.ON);
        if (this.lexer.token() == Token.TABLE) {
            this.lexer.nextToken();
        }
        stmt.setTable(this.exprParser.name());
        this.accept(Token.LPAREN);
        while (true) {
            final SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
            item.setParent(stmt);
            stmt.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            final String indexType = this.lexer.stringVal();
            this.accept(Token.LITERAL_CHARS);
            stmt.setType(indexType);
        }
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("DEFERRED");
            this.acceptIdentifier("REBUILD");
            stmt.setDeferedRebuild(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.IDXPROPERTIES)) {
            this.lexer.nextToken();
            this.exprParser.parseAssignItem(stmt.getProperties(), stmt);
        }
        if (this.lexer.token() == Token.IN) {
            this.lexer.nextToken();
            this.accept(Token.TABLE);
            final SQLName inTable = this.exprParser.name();
            stmt.setIn(inTable);
        }
        if (this.lexer.token() == Token.ROW || this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
            final SQLExternalRecordFormat format = this.getExprParser().parseRowFormat();
            stmt.setRowFormat(format);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
            this.lexer.nextToken();
            this.accept(Token.AS);
            final SQLName name = this.exprParser.name();
            stmt.setStoredAs(name);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
            this.lexer.nextToken();
            this.exprParser.parseAssignItem(stmt.getTableProperties(), stmt);
        }
        return stmt;
    }
    
    protected SQLStatement parseExport() {
        this.lexer.nextToken();
        this.accept(Token.TABLE);
        final SQLExportTableStatement stmt = new SQLExportTableStatement();
        stmt.setTable(new SQLExprTableSource(this.exprParser.name()));
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.parseAssignItems(stmt.getPartition(), stmt, false);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.TO) {
            this.lexer.nextToken();
            final SQLExpr to = this.exprParser.primary();
            stmt.setTo(to);
        }
        return stmt;
    }
    
    protected SQLStatement parseImport() {
        this.lexer.nextToken();
        final SQLImportTableStatement stmt = new SQLImportTableStatement();
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            this.lexer.nextToken();
            stmt.setExtenal(true);
        }
        if (this.lexer.token() == Token.TABLE) {
            this.lexer.nextToken();
            stmt.setTable(new SQLExprTableSource(this.exprParser.name()));
        }
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.parseAssignItems(stmt.getPartition(), stmt, false);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            final SQLExpr to = this.exprParser.primary();
            stmt.setFrom(to);
        }
        return stmt;
    }
    
    @Override
    protected SQLStatement parseAlterDatabase() {
        this.accept(Token.ALTER);
        if (this.lexer.token() == Token.SCHEMA) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.DATABASE);
        }
        final SQLAlterDatabaseStatement stmt = new SQLAlterDatabaseStatement();
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals(FnvHash.Constants.DBPROPERTIES)) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.exprParser.parseAssignItem(stmt.getProperties(), stmt);
        }
        return stmt;
    }
    
    @Override
    protected SQLStatement parseAlterSchema() {
        return this.parseAlterDatabase();
    }
    
    @Override
    public SQLStatement parseCreateSchema() {
        return this.parseCreateDatabase();
    }
    
    @Override
    public HiveExprParser getExprParser() {
        return (HiveExprParser)this.exprParser;
    }
}
