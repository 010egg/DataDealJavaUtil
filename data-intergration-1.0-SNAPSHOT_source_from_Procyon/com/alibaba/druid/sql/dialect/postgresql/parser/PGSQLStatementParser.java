// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.parser;

import com.alibaba.druid.sql.ast.statement.SQLDropStatement;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.statement.SQLCommitStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGShowStatement;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGSelectStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGConnectToStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGStartTransactionStatement;
import com.alibaba.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDeleteStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGDropSchemaStatement;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGAlterSchemaStatement;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGCreateSchemaStatement;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import java.util.ArrayList;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collection;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.dialect.postgresql.ast.stmt.PGUpdateStatement;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class PGSQLStatementParser extends SQLStatementParser
{
    public static final String TIME_ZONE = "TIME ZONE";
    public static final String TIME = "TIME";
    public static final String LOCAL = "LOCAL";
    
    public PGSQLStatementParser(final PGExprParser parser) {
        super(parser);
    }
    
    public PGSQLStatementParser(final String sql) {
        super(new PGExprParser(sql));
    }
    
    public PGSQLStatementParser(final String sql, final SQLParserFeature... features) {
        super(new PGExprParser(sql, features));
    }
    
    public PGSQLStatementParser(final Lexer lexer) {
        super(new PGExprParser(lexer));
    }
    
    @Override
    public PGSelectParser createSQLSelectParser() {
        return new PGSelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public SQLUpdateStatement parseUpdateStatement() {
        this.accept(Token.UPDATE);
        final PGUpdateStatement udpateStatement = new PGUpdateStatement();
        final SQLSelectParser selectParser = this.exprParser.createSelectParser();
        final SQLTableSource tableSource = selectParser.parseTableSource();
        udpateStatement.setTableSource(tableSource);
        this.parseUpdateSet(udpateStatement);
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
            final SQLTableSource from = selectParser.parseTableSource();
            udpateStatement.setFrom(from);
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            udpateStatement.setWhere(this.exprParser.expr());
        }
        if (this.lexer.token() == Token.RETURNING) {
            this.lexer.nextToken();
            while (true) {
                udpateStatement.getReturning().add(this.exprParser.expr());
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return udpateStatement;
    }
    
    @Override
    public PGInsertStatement parseInsert() {
        final PGInsertStatement stmt = new PGInsertStatement();
        if (this.lexer.token() == Token.INSERT) {
            this.lexer.nextToken();
            this.accept(Token.INTO);
            final SQLName tableName = this.exprParser.name();
            stmt.setTableName(tableName);
            if (this.lexer.token() == Token.AS) {
                this.lexer.nextToken();
                stmt.setAlias(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.IDENTIFIER) {
                stmt.setAlias(this.lexer.stringVal());
                this.lexer.nextToken();
            }
        }
        if (this.lexer.token() == Token.DEFAULT) {
            this.lexer.nextToken();
            this.accept(Token.VALUES);
            stmt.setDefaultValues(true);
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprParser.exprList(stmt.getColumns(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.VALUES) {
            this.lexer.nextToken();
            while (true) {
                this.accept(Token.LPAREN);
                final SQLInsertStatement.ValuesClause valuesCaluse = new SQLInsertStatement.ValuesClause();
                this.exprParser.exprList(valuesCaluse.getValues(), valuesCaluse);
                stmt.addValueCause(valuesCaluse);
                this.accept(Token.RPAREN);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.token() == Token.SELECT) {
            final SQLQueryExpr queryExpr = (SQLQueryExpr)this.exprParser.expr();
            stmt.setQuery(queryExpr.getSubQuery());
        }
        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.CONFLICT)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    final List<SQLExpr> onConflictTarget = new ArrayList<SQLExpr>();
                    this.exprParser.exprList(onConflictTarget, stmt);
                    stmt.setOnConflictTarget(onConflictTarget);
                    this.accept(Token.RPAREN);
                }
                if (this.lexer.token() == Token.ON) {
                    this.lexer.nextToken();
                    this.accept(Token.CONSTRAINT);
                    final SQLName constraintName = this.exprParser.name();
                    stmt.setOnConflictConstraint(constraintName);
                }
                if (this.lexer.token() == Token.WHERE) {
                    this.lexer.nextToken();
                    final SQLExpr where = this.exprParser.expr();
                    stmt.setOnConflictWhere(where);
                }
                if (this.lexer.token() == Token.DO) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.NOTHING)) {
                        this.lexer.nextToken();
                        stmt.setOnConflictDoNothing(true);
                    }
                    else {
                        this.accept(Token.UPDATE);
                        this.accept(Token.SET);
                        while (true) {
                            final SQLUpdateSetItem item = this.exprParser.parseUpdateSetItem();
                            stmt.addConflicUpdateItem(item);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        if (this.lexer.token() == Token.WHERE) {
                            this.lexer.nextToken();
                            final SQLExpr where = this.exprParser.expr();
                            stmt.setOnConflictUpdateWhere(where);
                        }
                    }
                }
            }
        }
        if (this.lexer.token() == Token.RETURNING) {
            this.lexer.nextToken();
            SQLExpr returning = this.exprParser.expr();
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
                final SQLListExpr list = new SQLListExpr();
                list.addItem(returning);
                this.exprParser.exprList(list.getItems(), list);
                returning = list;
            }
            stmt.setReturning(returning);
        }
        return stmt;
    }
    
    @Override
    public PGCreateSchemaStatement parseCreateSchema() {
        this.accept(Token.CREATE);
        this.accept(Token.SCHEMA);
        final PGCreateSchemaStatement stmt = new PGCreateSchemaStatement();
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        if (this.lexer.token() == Token.IDENTIFIER) {
            if (this.lexer.identifierEquals("AUTHORIZATION")) {
                this.lexer.nextToken();
                stmt.setAuthorization(true);
                final SQLIdentifierExpr userName = (SQLIdentifierExpr)this.exprParser.expr();
                stmt.setUserName(userName);
            }
            else {
                final SQLIdentifierExpr schemaName = (SQLIdentifierExpr)this.exprParser.expr();
                stmt.setSchemaName(schemaName);
                if (this.lexer.identifierEquals("AUTHORIZATION")) {
                    this.lexer.nextToken();
                    stmt.setAuthorization(true);
                    final SQLIdentifierExpr userName2 = (SQLIdentifierExpr)this.exprParser.expr();
                    stmt.setUserName(userName2);
                }
            }
            return stmt;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
    
    @Override
    protected SQLStatement parseAlterSchema() {
        this.accept(Token.ALTER);
        this.accept(Token.SCHEMA);
        final PGAlterSchemaStatement stmt = new PGAlterSchemaStatement();
        stmt.setSchemaName(this.exprParser.identifier());
        if (this.lexer.identifierEquals(FnvHash.Constants.RENAME)) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            stmt.setNewName(this.exprParser.identifier());
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.OWNER)) {
            this.lexer.nextToken();
            this.accept(Token.TO);
            stmt.setNewOwner(this.exprParser.identifier());
        }
        return stmt;
    }
    
    public PGDropSchemaStatement parseDropSchema() {
        final PGDropSchemaStatement stmt = new PGDropSchemaStatement();
        if (this.lexer.token() == Token.SCHEMA) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.DATABASE);
        }
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.EXISTS);
            stmt.setIfExists(true);
        }
        final SQLIdentifierExpr name = this.exprParser.identifier();
        stmt.setSchemaName(name);
        if (this.lexer.identifierEquals(FnvHash.Constants.RESTRICT)) {
            this.lexer.nextToken();
            stmt.setRestrict(true);
        }
        else if (this.lexer.token() == Token.CASCADE || this.lexer.identifierEquals(FnvHash.Constants.CASCADE)) {
            this.lexer.nextToken();
            stmt.setCascade(true);
        }
        else {
            stmt.setCascade(false);
        }
        return stmt;
    }
    
    @Override
    public PGDeleteStatement parseDeleteStatement() {
        this.lexer.nextToken();
        final PGDeleteStatement deleteStatement = new PGDeleteStatement();
        if (this.lexer.token() == Token.FROM) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.ONLY) {
            this.lexer.nextToken();
            deleteStatement.setOnly(true);
        }
        final SQLName tableName = this.exprParser.name();
        deleteStatement.setTableName(tableName);
        if (this.lexer.token() == Token.AS) {
            this.accept(Token.AS);
        }
        if (this.lexer.token() == Token.IDENTIFIER) {
            deleteStatement.setAlias(this.lexer.stringVal());
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.USING) {
            this.lexer.nextToken();
            final SQLTableSource tableSource = this.createSQLSelectParser().parseTableSource();
            deleteStatement.setUsing(tableSource);
        }
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.CURRENT) {
                this.lexer.nextToken();
                this.accept(Token.OF);
                final SQLName cursorName = this.exprParser.name();
                final SQLExpr where = new SQLCurrentOfCursorExpr(cursorName);
                deleteStatement.setWhere(where);
            }
            else {
                final SQLExpr where2 = this.exprParser.expr();
                deleteStatement.setWhere(where2);
            }
        }
        if (this.lexer.token() == Token.RETURNING) {
            this.lexer.nextToken();
            this.accept(Token.STAR);
            deleteStatement.setReturning(true);
        }
        return deleteStatement;
    }
    
    @Override
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        switch (this.lexer.token()) {
            case BEGIN:
            case START: {
                final PGStartTransactionStatement stmt = this.parseBegin();
                statementList.add(stmt);
                return true;
            }
            case WITH: {
                statementList.add(this.parseWith());
                return true;
            }
            default: {
                if (this.lexer.identifierEquals(FnvHash.Constants.CONNECT)) {
                    final SQLStatement stmt2 = this.parseConnectTo();
                    statementList.add(stmt2);
                    return true;
                }
                return false;
            }
        }
    }
    
    protected PGStartTransactionStatement parseBegin() {
        final PGStartTransactionStatement stmt = new PGStartTransactionStatement();
        if (this.lexer.token() == Token.START) {
            this.lexer.nextToken();
            this.acceptIdentifier("TRANSACTION");
        }
        else {
            this.accept(Token.BEGIN);
        }
        return stmt;
    }
    
    public SQLStatement parseConnectTo() {
        this.acceptIdentifier("CONNECT");
        this.accept(Token.TO);
        final PGConnectToStatement stmt = new PGConnectToStatement();
        final SQLName target = this.exprParser.name();
        stmt.setTarget(target);
        return stmt;
    }
    
    @Override
    public PGSelectStatement parseSelect() {
        final PGSelectParser selectParser = this.createSQLSelectParser();
        final SQLSelect select = selectParser.select();
        return new PGSelectStatement(select);
    }
    
    @Override
    public SQLStatement parseWith() {
        final SQLWithSubqueryClause with = this.parseWithQuery();
        if (this.lexer.token() == Token.INSERT) {
            final PGInsertStatement stmt = this.parseInsert();
            stmt.setWith(with);
            return stmt;
        }
        if (this.lexer.token() == Token.SELECT) {
            final PGSelectStatement stmt2 = this.parseSelect();
            stmt2.getSelect().setWithSubQuery(with);
            return stmt2;
        }
        if (this.lexer.token() == Token.DELETE) {
            final PGDeleteStatement stmt3 = this.parseDeleteStatement();
            stmt3.setWith(with);
            return stmt3;
        }
        if (this.lexer.token() == Token.UPDATE) {
            final PGUpdateStatement stmt4 = (PGUpdateStatement)this.parseUpdateStatement();
            stmt4.setWith(with);
            return stmt4;
        }
        throw new ParserException("TODO. " + this.lexer.info());
    }
    
    @Override
    protected SQLAlterTableAlterColumn parseAlterColumn() {
        if (this.lexer.token() == Token.COLUMN) {
            this.lexer.nextToken();
        }
        final SQLColumnDefinition column = this.exprParser.parseColumn();
        final SQLAlterTableAlterColumn alterColumn = new SQLAlterTableAlterColumn();
        alterColumn.setColumn(column);
        if (column.getDataType() == null && column.getConstraints().size() == 0) {
            if (this.lexer.token() == Token.SET) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    this.accept(Token.NULL);
                    alterColumn.setSetNotNull(true);
                }
                else {
                    this.accept(Token.DEFAULT);
                    final SQLExpr defaultValue = this.exprParser.expr();
                    alterColumn.setSetDefault(defaultValue);
                }
            }
            else if (this.lexer.token() == Token.DROP) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    this.accept(Token.NULL);
                    alterColumn.setDropNotNull(true);
                }
                else {
                    this.accept(Token.DEFAULT);
                    alterColumn.setDropDefault(true);
                }
            }
        }
        return alterColumn;
    }
    
    @Override
    public SQLStatement parseShow() {
        this.accept(Token.SHOW);
        final PGShowStatement stmt = new PGShowStatement();
        switch (this.lexer.token()) {
            case ALL: {
                stmt.setExpr(new SQLIdentifierExpr(Token.ALL.name()));
                this.lexer.nextToken();
                break;
            }
            default: {
                stmt.setExpr(this.exprParser.expr());
                break;
            }
        }
        return stmt;
    }
    
    @Override
    public SQLStatement parseCommit() {
        final SQLCommitStatement stmt = new SQLCommitStatement();
        stmt.setDbType(this.dbType);
        this.lexer.nextToken();
        return stmt;
    }
    
    @Override
    public SQLStatement parseSet() {
        this.accept(Token.SET);
        final Token token = this.lexer.token();
        String range = "";
        SQLSetStatement.Option option = null;
        if (token == Token.SESSION) {
            this.lexer.nextToken();
            range = Token.SESSION.name();
            option = SQLSetStatement.Option.SESSION;
        }
        else if (token == Token.IDENTIFIER && "LOCAL".equalsIgnoreCase(this.lexer.stringVal())) {
            range = "LOCAL";
            option = SQLSetStatement.Option.LOCAL;
            this.lexer.nextToken();
        }
        final long hash = this.lexer.hash_lower();
        final String parameter = this.lexer.stringVal();
        final List<SQLExpr> values = new ArrayList<SQLExpr>();
        SQLExpr paramExpr;
        if (hash == FnvHash.Constants.TIME) {
            this.lexer.nextToken();
            this.acceptIdentifier("ZONE");
            paramExpr = new SQLIdentifierExpr("TIME ZONE");
            final String value = this.lexer.stringVal();
            if (this.lexer.token() == Token.IDENTIFIER) {
                values.add(new SQLIdentifierExpr(value.toUpperCase()));
            }
            else {
                values.add(new SQLCharExpr(value));
            }
            this.lexer.nextToken();
        }
        else if (hash == FnvHash.Constants.ROLE) {
            paramExpr = new SQLIdentifierExpr(parameter);
            this.lexer.nextToken();
            values.add(this.exprParser.primary());
            this.lexer.nextToken();
        }
        else {
            paramExpr = new SQLIdentifierExpr(parameter);
            this.lexer.nextToken();
            while (!this.lexer.isEOF()) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.LITERAL_CHARS) {
                    values.add(new SQLCharExpr(this.lexer.stringVal()));
                }
                else if (this.lexer.token() == Token.LITERAL_INT) {
                    values.add(new SQLIdentifierExpr(this.lexer.numberString()));
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.JSON_SET) || this.lexer.identifierEquals(FnvHash.Constants.JSONB_SET)) {
                    final SQLExpr json_set = this.exprParser.expr();
                    values.add(json_set);
                }
                else {
                    values.add(new SQLIdentifierExpr(this.lexer.stringVal()));
                }
                this.lexer.nextToken();
            }
        }
        SQLExpr valueExpr;
        if (values.size() == 1) {
            valueExpr = values.get(0);
        }
        else {
            final SQLListExpr listExpr = new SQLListExpr();
            for (final SQLExpr value2 : values) {
                listExpr.addItem(value2);
            }
            valueExpr = listExpr;
        }
        final SQLSetStatement stmt = new SQLSetStatement(paramExpr, valueExpr, this.dbType);
        stmt.setOption(option);
        return stmt;
    }
    
    @Override
    public SQLCreateIndexStatement parseCreateIndex(final boolean acceptCreate) {
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        final SQLCreateIndexStatement stmt = new SQLCreateIndexStatement(this.getDbType());
        if (this.lexer.token() == Token.UNIQUE) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("CLUSTERED")) {
                this.lexer.nextToken();
                stmt.setType("UNIQUE CLUSTERED");
            }
            else {
                stmt.setType("UNIQUE");
            }
        }
        else if (this.lexer.identifierEquals("FULLTEXT")) {
            stmt.setType("FULLTEXT");
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals("NONCLUSTERED")) {
            stmt.setType("NONCLUSTERED");
            this.lexer.nextToken();
        }
        this.accept(Token.INDEX);
        if (this.lexer.token() == Token.IF) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExists(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CONCURRENTLY)) {
            this.lexer.nextToken();
            stmt.setConcurrently(true);
        }
        if (this.lexer.token() != Token.ON) {
            stmt.setName(this.exprParser.name());
        }
        this.accept(Token.ON);
        stmt.setTable(this.exprParser.name());
        if (this.lexer.token() == Token.USING) {
            this.lexer.nextToken();
            final String using = this.lexer.stringVal();
            this.accept(Token.IDENTIFIER);
            stmt.setUsing(using);
        }
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
        if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                final String optionName = this.lexer.stringVal();
                this.accept(Token.IDENTIFIER);
                this.accept(Token.EQ);
                final SQLExpr option = this.exprParser.expr();
                option.setParent(stmt);
                stmt.addOption(optionName, option);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.TABLESPACE) {
            this.lexer.nextToken();
            final SQLName tablespace = this.exprParser.name();
            stmt.setTablespace(tablespace);
        }
        return stmt;
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new PGCreateTableParser(this.exprParser);
    }
}
