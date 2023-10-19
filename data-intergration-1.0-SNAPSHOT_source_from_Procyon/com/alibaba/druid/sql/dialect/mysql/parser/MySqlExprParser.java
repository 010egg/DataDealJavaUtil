// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import java.util.Arrays;
import com.alibaba.druid.sql.ast.statement.SQLDDLStatement;
import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.SQLSubPartition;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOrderingExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLAnnIndex;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.expr.SQLExtractExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlUserName;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.statement.SQLValuesQuery;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.ast.SQLCurrentUserExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlJSONTableExpr;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlOutFileExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class MySqlExprParser extends SQLExprParser
{
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    public static final String[] SINGLE_WORD_TABLE_OPTIONS;
    public static final long[] SINGLE_WORD_TABLE_OPTIONS_CODES;
    
    public MySqlExprParser(final Lexer lexer) {
        super(lexer, DbType.mysql);
        this.aggregateFunctions = MySqlExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = MySqlExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    public MySqlExprParser(final String sql) {
        this(new MySqlLexer(sql));
        this.lexer.nextToken();
    }
    
    public MySqlExprParser(final String sql, final SQLParserFeature... features) {
        super(new MySqlLexer(sql, features), DbType.mysql);
        this.aggregateFunctions = MySqlExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = MySqlExprParser.AGGREGATE_FUNCTIONS_CODES;
        if (sql.length() > 6) {
            final char c0 = sql.charAt(0);
            final char c2 = sql.charAt(1);
            final char c3 = sql.charAt(2);
            final char c4 = sql.charAt(3);
            final char c5 = sql.charAt(4);
            final char c6 = sql.charAt(5);
            final char c7 = sql.charAt(6);
            if (c0 == 'S' && c2 == 'E' && c3 == 'L' && c4 == 'E' && c5 == 'C' && c6 == 'T' && c7 == ' ') {
                this.lexer.reset(6, ' ', Token.SELECT);
                return;
            }
            if (c0 == 's' && c2 == 'e' && c3 == 'l' && c4 == 'e' && c5 == 'c' && c6 == 't' && c7 == ' ') {
                this.lexer.reset(6, ' ', Token.SELECT);
                return;
            }
            if (c0 == 'I' && c2 == 'N' && c3 == 'S' && c4 == 'E' && c5 == 'R' && c6 == 'T' && c7 == ' ') {
                this.lexer.reset(6, ' ', Token.INSERT);
                return;
            }
            if (c0 == 'i' && c2 == 'n' && c3 == 's' && c4 == 'e' && c5 == 'r' && c6 == 't' && c7 == ' ') {
                this.lexer.reset(6, ' ', Token.INSERT);
                return;
            }
            if (c0 == 'U' && c2 == 'P' && c3 == 'D' && c4 == 'A' && c5 == 'T' && c6 == 'E' && c7 == ' ') {
                this.lexer.reset(6, ' ', Token.UPDATE);
                return;
            }
            if (c0 == 'u' && c2 == 'p' && c3 == 'd' && c4 == 'a' && c5 == 't' && c6 == 'e' && c7 == ' ') {
                this.lexer.reset(6, ' ', Token.UPDATE);
                return;
            }
            if (c0 == '/' && c2 == '*' && this.isEnabled(SQLParserFeature.OptimizedForParameterized) && !this.isEnabled(SQLParserFeature.TDDLHint)) {
                final MySqlLexer mySqlLexer = (MySqlLexer)this.lexer;
                mySqlLexer.skipFirstHintsOrMultiCommentAndNextToken();
                return;
            }
        }
        this.lexer.nextToken();
    }
    
    public MySqlExprParser(final String sql, final boolean keepComments) {
        this(new MySqlLexer(sql, true, keepComments));
        this.lexer.nextToken();
    }
    
    public MySqlExprParser(final String sql, final boolean skipComment, final boolean keepComments) {
        this(new MySqlLexer(sql, skipComment, keepComments));
        this.lexer.nextToken();
    }
    
    @Override
    public SQLExpr primary() {
        final Token tok = this.lexer.token();
        switch (tok) {
            case IDENTIFIER: {
                final long hash_lower = this.lexer.hash_lower();
                final Lexer.SavePoint savePoint = this.lexer.mark();
                if (hash_lower == FnvHash.Constants.OUTLINE) {
                    this.lexer.nextToken();
                    try {
                        final SQLExpr file = this.primary();
                        final SQLExpr expr = new MySqlOutFileExpr(file);
                        return this.primaryRest(expr);
                    }
                    catch (ParserException e) {
                        this.lexer.reset(savePoint);
                    }
                }
                final String strVal = this.lexer.stringVal();
                final boolean quoteStart = strVal.length() > 0 && (strVal.charAt(0) == '`' || strVal.charAt(0) == '\"');
                if (!quoteStart) {
                    this.setAllowIdentifierMethod(true);
                }
                SQLCurrentTimeExpr currentTimeExpr = null;
                if (hash_lower == FnvHash.Constants.CURRENT_TIME && !quoteStart) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIME);
                }
                else if (hash_lower == FnvHash.Constants.CURRENT_TIMESTAMP && !quoteStart) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP);
                }
                else if (hash_lower == FnvHash.Constants.CURRENT_DATE && !quoteStart) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_DATE);
                }
                else if (hash_lower == FnvHash.Constants.CURDATE && !quoteStart) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURDATE);
                }
                else if (hash_lower == FnvHash.Constants.LOCALTIME && !quoteStart) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.LOCALTIME);
                }
                else if (hash_lower == FnvHash.Constants.LOCALTIMESTAMP && !quoteStart) {
                    currentTimeExpr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.LOCALTIMESTAMP);
                }
                else if (hash_lower == FnvHash.Constants.JSON_TABLE) {
                    if (this.lexer.identifierEquals("JSON_TABLE")) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        final MySqlJSONTableExpr jsonTable = new MySqlJSONTableExpr();
                        jsonTable.setExpr(this.expr());
                        this.accept(Token.COMMA);
                        jsonTable.setPath(this.expr());
                        this.acceptIdentifier("COLUMNS");
                        this.accept(Token.LPAREN);
                        while (this.lexer.token() != Token.RPAREN) {
                            jsonTable.addColumn(this.parseJsonTableColumn());
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                        this.accept(Token.RPAREN);
                        return jsonTable;
                    }
                }
                else {
                    if (hash_lower == FnvHash.Constants._LATIN1 && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLExpr charExpr;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            String collate = null;
                            if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                                this.lexer.nextToken();
                                collate = this.lexer.stringVal();
                                if (this.lexer.token() == Token.LITERAL_CHARS) {
                                    this.lexer.nextToken();
                                }
                                else {
                                    this.accept(Token.IDENTIFIER);
                                }
                            }
                            charExpr = new MySqlCharExpr(str, "_latin1", collate);
                        }
                        else {
                            charExpr = new MySqlCharExpr(hexString, "_latin1");
                        }
                        return this.primaryRest(charExpr);
                    }
                    if ((hash_lower == FnvHash.Constants._UTF8 || hash_lower == FnvHash.Constants._UTF8MB4) && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLExpr charExpr;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            String collate = null;
                            if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                                this.lexer.nextToken();
                                collate = this.lexer.stringVal();
                                if (this.lexer.token() == Token.LITERAL_CHARS) {
                                    this.lexer.nextToken();
                                }
                                else {
                                    this.accept(Token.IDENTIFIER);
                                }
                            }
                            charExpr = new MySqlCharExpr(str, "_utf8", collate);
                        }
                        else {
                            final String str = MySqlUtils.utf8(hexString);
                            charExpr = new SQLCharExpr(str);
                        }
                        return this.primaryRest(charExpr);
                    }
                    if ((hash_lower == FnvHash.Constants._UTF16 || hash_lower == FnvHash.Constants._UCS2) && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLCharExpr charExpr2;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            charExpr2 = new MySqlCharExpr(str, "_utf16");
                        }
                        else {
                            charExpr2 = new SQLCharExpr(MySqlUtils.utf16(hexString));
                        }
                        return this.primaryRest(charExpr2);
                    }
                    if (hash_lower == FnvHash.Constants._UTF16LE && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLCharExpr charExpr2;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            charExpr2 = new MySqlCharExpr(str, "_utf16le");
                        }
                        else {
                            charExpr2 = new MySqlCharExpr(hexString, "_utf16le");
                        }
                        return this.primaryRest(charExpr2);
                    }
                    if (hash_lower == FnvHash.Constants._UTF32 && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLCharExpr charExpr2;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            charExpr2 = new MySqlCharExpr(str, "_utf32");
                        }
                        else {
                            charExpr2 = new SQLCharExpr(MySqlUtils.utf32(hexString));
                        }
                        return this.primaryRest(charExpr2);
                    }
                    if (hash_lower == FnvHash.Constants._GBK && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLCharExpr charExpr2;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            charExpr2 = new MySqlCharExpr(str, "_gbk");
                        }
                        else {
                            charExpr2 = new SQLCharExpr(MySqlUtils.gbk(hexString));
                        }
                        return this.primaryRest(charExpr2);
                    }
                    if (hash_lower == FnvHash.Constants._UJIS && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLCharExpr charExpr2;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            charExpr2 = new MySqlCharExpr(str, "_ujis");
                        }
                        else {
                            charExpr2 = new MySqlCharExpr(hexString, "_ujis");
                        }
                        return this.primaryRest(charExpr2);
                    }
                    if (hash_lower == FnvHash.Constants._BIG5 && !quoteStart) {
                        this.lexer.nextToken();
                        String hexString;
                        if (this.lexer.identifierEquals(FnvHash.Constants.X)) {
                            this.lexer.nextToken();
                            hexString = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.token() == Token.LITERAL_CHARS) {
                            hexString = null;
                        }
                        else {
                            hexString = this.lexer.hexString();
                            this.lexer.nextToken();
                        }
                        SQLCharExpr charExpr2;
                        if (hexString == null) {
                            final String str = this.lexer.stringVal();
                            this.lexer.nextToken();
                            charExpr2 = new MySqlCharExpr(str, "_big5");
                        }
                        else {
                            charExpr2 = new SQLCharExpr(MySqlUtils.big5(hexString));
                        }
                        return this.primaryRest(charExpr2);
                    }
                    if (hash_lower == FnvHash.Constants.CURRENT_USER && this.isEnabled(SQLParserFeature.EnableCurrentUserExpr)) {
                        this.lexer.nextToken();
                        return this.primaryRest(new SQLCurrentUserExpr());
                    }
                    if (hash_lower == -5808529385363204345L && this.lexer.charAt(this.lexer.pos()) == '\'') {
                        this.lexer.nextToken();
                        final SQLHexExpr hex = new SQLHexExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        return this.primaryRest(hex);
                    }
                }
                if (currentTimeExpr != null) {
                    final String methodName = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        if (this.lexer.token() != Token.LPAREN) {
                            return this.primaryRest(this.methodRest(new SQLIdentifierExpr(methodName), false));
                        }
                        this.lexer.nextToken();
                    }
                    return this.primaryRest(currentTimeExpr);
                }
                return super.primary();
            }
            case VARIANT: {
                SQLVariantRefExpr varRefExpr = new SQLVariantRefExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                if (varRefExpr.getName().equalsIgnoreCase("@@global")) {
                    this.accept(Token.DOT);
                    varRefExpr = new SQLVariantRefExpr(this.lexer.stringVal(), true);
                    this.lexer.nextToken();
                }
                else if (varRefExpr.getName().equals("@") && this.lexer.token() == Token.LITERAL_CHARS) {
                    varRefExpr.setName("@'" + this.lexer.stringVal() + "'");
                    this.lexer.nextToken();
                }
                else if (varRefExpr.getName().equals("@@") && this.lexer.token() == Token.LITERAL_CHARS) {
                    varRefExpr.setName("@@'" + this.lexer.stringVal() + "'");
                    this.lexer.nextToken();
                }
                return this.primaryRest(varRefExpr);
            }
            case VALUES: {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.LPAREN) {
                    final SQLExpr expr2 = this.primary();
                    final SQLValuesQuery values = new SQLValuesQuery();
                    values.addValue(new SQLListExpr(new SQLExpr[] { expr2 }));
                    return new SQLQueryExpr(new SQLSelect(values));
                }
                return this.methodRest(new SQLIdentifierExpr("VALUES"), true);
            }
            case BINARY: {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.SEMI || this.lexer.token() == Token.EOF) {
                    return new SQLIdentifierExpr("BINARY");
                }
                final SQLUnaryExpr binaryExpr = new SQLUnaryExpr(SQLUnaryOperator.BINARY, this.primary());
                return this.primaryRest(binaryExpr);
            }
            default: {
                return super.primary();
            }
        }
    }
    
    protected MySqlJSONTableExpr.Column parseJsonTableColumn() {
        final MySqlJSONTableExpr.Column column = new MySqlJSONTableExpr.Column();
        final SQLName name = this.name();
        column.setName(name);
        if (this.lexer.token() == Token.FOR) {
            this.lexer.nextToken();
            this.acceptIdentifier("ORDINALITY");
        }
        else {
            final boolean nested = name instanceof SQLIdentifierExpr && name.nameHashCode64() == FnvHash.Constants.NESTED;
            if (!nested) {
                column.setDataType(this.parseDataType());
            }
            if (this.lexer.token() == Token.EXISTS) {
                this.lexer.nextToken();
                column.setExists(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.PATH)) {
                this.lexer.nextToken();
                column.setPath(this.primary());
            }
            if (name instanceof SQLIdentifierExpr && name.nameHashCode64() == FnvHash.Constants.NESTED) {
                this.acceptIdentifier("COLUMNS");
                this.accept(Token.LPAREN);
                while (this.lexer.token() != Token.RPAREN) {
                    final MySqlJSONTableExpr.Column nestedColumn = this.parseJsonTableColumn();
                    column.addNestedColumn(nestedColumn);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            for (int i = 0; i < 2; ++i) {
                if (this.lexer.identifierEquals("ERROR") || this.lexer.token() == Token.DEFAULT || this.lexer.token() == Token.NULL) {
                    if (this.lexer.token() == Token.DEFAULT) {
                        this.lexer.nextToken();
                    }
                    final SQLExpr expr = this.expr();
                    this.accept(Token.ON);
                    if (this.lexer.identifierEquals("ERROR")) {
                        this.lexer.nextToken();
                        column.setOnError(expr);
                    }
                    else {
                        this.acceptIdentifier("EMPTY");
                        column.setOnEmpty(expr);
                    }
                }
            }
        }
        return column;
    }
    
    @Override
    public final SQLExpr primaryRest(SQLExpr expr) {
        if (expr == null) {
            throw new IllegalArgumentException("expr");
        }
        if (this.lexer.token() == Token.LITERAL_CHARS) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                final String ident = identExpr.getName();
                if (ident.equalsIgnoreCase("x")) {
                    final char ch = this.lexer.charAt(this.lexer.pos());
                    if (ch == '\'') {
                        final String charValue = this.lexer.stringVal();
                        this.lexer.nextToken();
                        expr = new SQLHexExpr(charValue);
                        return this.primaryRest(expr);
                    }
                }
                else if (ident.startsWith("_")) {
                    final String charValue2 = this.lexer.stringVal();
                    this.lexer.nextToken();
                    final MySqlCharExpr mysqlCharExpr = new MySqlCharExpr(charValue2);
                    mysqlCharExpr.setCharset(identExpr.getName());
                    if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                        this.lexer.nextToken();
                        final String collate = this.lexer.stringVal();
                        mysqlCharExpr.setCollate(collate);
                        if (this.lexer.token() == Token.LITERAL_CHARS) {
                            this.lexer.nextToken();
                        }
                        else {
                            this.accept(Token.IDENTIFIER);
                        }
                    }
                    expr = mysqlCharExpr;
                    return this.primaryRest(expr);
                }
            }
            else if (expr instanceof SQLCharExpr) {
                String text2 = ((SQLCharExpr)expr).getText();
                do {
                    final String chars = this.lexer.stringVal();
                    text2 += chars;
                    this.lexer.nextToken();
                } while (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS);
                expr = new SQLCharExpr(text2);
            }
            else if (expr instanceof SQLVariantRefExpr) {
                final SQLMethodInvokeExpr concat = new SQLMethodInvokeExpr("CONCAT");
                concat.addArgument(expr);
                concat.addArgument(this.primary());
                expr = concat;
                return this.primaryRest(expr);
            }
        }
        else if (this.lexer.token() == Token.IDENTIFIER) {
            if (expr instanceof SQLHexExpr) {
                if ("USING".equalsIgnoreCase(this.lexer.stringVal())) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.IDENTIFIER) {
                        throw new ParserException("syntax error, illegal hex. " + this.lexer.info());
                    }
                    final String charSet = this.lexer.stringVal();
                    this.lexer.nextToken();
                    expr.getAttributes().put("USING", charSet);
                    return this.primaryRest(expr);
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                if (this.lexer.token() != Token.IDENTIFIER && this.lexer.token() != Token.LITERAL_CHARS) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                final String collate2 = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)(expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.COLLATE, new SQLIdentifierExpr(collate2), DbType.mysql));
                return this.primaryRest(expr);
            }
            else if (expr instanceof SQLVariantRefExpr && this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.IDENTIFIER && this.lexer.token() != Token.LITERAL_CHARS) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                final String collate2 = this.lexer.stringVal();
                this.lexer.nextToken();
                expr.putAttribute("COLLATE", collate2);
                return this.primaryRest(expr);
            }
        }
        else if (this.lexer.token() == Token.LBRACKET) {
            final SQLArrayExpr array = new SQLArrayExpr();
            array.setExpr(expr);
            this.lexer.nextToken();
            this.exprList(array.getValues(), array);
            this.accept(Token.RBRACKET);
            return this.primaryRest(array);
        }
        if (this.lexer.token() == Token.VARIANT) {
            final String variant = this.lexer.stringVal();
            if ("@".equals(variant)) {
                return this.userNameRest(expr);
            }
            if ("@localhost".equals(variant)) {
                return this.userNameRest(expr);
            }
            throw new ParserException("syntax error. " + this.lexer.info());
        }
        else {
            if (this.lexer.token() == Token.ERROR) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            return super.primaryRest(expr);
        }
    }
    
    public SQLName userName() {
        final SQLName name = this.name();
        if (this.lexer.token() == Token.LPAREN && name.hashCode64() == FnvHash.Constants.CURRENT_USER) {
            this.lexer.nextToken();
            this.accept(Token.RPAREN);
            return name;
        }
        return (SQLName)this.userNameRest(name);
    }
    
    private SQLExpr userNameRest(final SQLExpr expr) {
        if (this.lexer.token() != Token.VARIANT || !this.lexer.stringVal().startsWith("@")) {
            return expr;
        }
        final MySqlUserName userName = new MySqlUserName();
        if (expr instanceof SQLCharExpr) {
            userName.setUserName(((SQLCharExpr)expr).getText());
        }
        else {
            userName.setUserName(((SQLIdentifierExpr)expr).getName());
        }
        final String strVal = this.lexer.stringVal();
        this.lexer.nextToken();
        if (strVal.length() > 1) {
            userName.setHost(strVal.substring(1));
            return userName;
        }
        if (this.lexer.token() == Token.LITERAL_CHARS) {
            userName.setHost(this.lexer.stringVal());
        }
        else {
            if (this.lexer.token() == Token.PERCENT) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            userName.setHost(this.lexer.stringVal());
        }
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.IDENTIFIED)) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.BY) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.PASSWORD)) {
                    this.lexer.reset(mark);
                }
                else {
                    userName.setIdentifiedBy(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
            }
            else {
                this.lexer.reset(mark);
            }
        }
        return userName;
    }
    
    @Override
    protected SQLExpr parsePosition() {
        SQLExpr expr = this.primary();
        expr = this.primaryRest(expr);
        expr = this.bitXorRest(expr);
        expr = this.additiveRest(expr);
        expr = this.shiftRest(expr);
        expr = this.bitAndRest(expr);
        expr = this.bitOrRest(expr);
        if (this.lexer.token() == Token.IN) {
            this.accept(Token.IN);
        }
        else {
            if (this.lexer.token() != Token.COMMA) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            this.accept(Token.COMMA);
        }
        final SQLExpr str = this.expr();
        this.accept(Token.RPAREN);
        final SQLMethodInvokeExpr locate = new SQLMethodInvokeExpr("LOCATE");
        locate.addArgument(expr);
        locate.addArgument(str);
        return this.primaryRest(locate);
    }
    
    @Override
    protected SQLExpr parseExtract() {
        if (this.lexer.token() != Token.IDENTIFIER) {
            throw new ParserException("syntax error. " + this.lexer.info());
        }
        final String unitVal = this.lexer.stringVal();
        final SQLIntervalUnit unit = SQLIntervalUnit.valueOf(unitVal.toUpperCase());
        this.lexer.nextToken();
        this.accept(Token.FROM);
        final SQLExpr value = this.expr();
        final SQLExtractExpr extract = new SQLExtractExpr();
        extract.setValue(value);
        extract.setUnit(unit);
        this.accept(Token.RPAREN);
        final SQLExpr expr = extract;
        return this.primaryRest(expr);
    }
    
    @Override
    public SQLSelectParser createSelectParser() {
        return new MySqlSelectParser(this);
    }
    
    @Override
    protected SQLExpr parseInterval() {
        this.accept(Token.INTERVAL);
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr("INTERVAL");
            if (this.lexer.token() != Token.RPAREN) {
                this.exprList(methodInvokeExpr.getArguments(), methodInvokeExpr);
            }
            this.accept(Token.RPAREN);
            if (methodInvokeExpr.getArguments().size() == 1 && this.lexer.token() == Token.IDENTIFIER) {
                final SQLExpr value = methodInvokeExpr.getArguments().get(0);
                final String unit = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLIntervalExpr intervalExpr = new SQLIntervalExpr();
                intervalExpr.setValue(value);
                intervalExpr.setUnit(SQLIntervalUnit.valueOf(unit.toUpperCase()));
                return intervalExpr;
            }
            return this.primaryRest(methodInvokeExpr);
        }
        else {
            final SQLExpr value2 = this.expr();
            if (this.lexer.token() != Token.IDENTIFIER) {
                throw new ParserException("Syntax error. " + this.lexer.info());
            }
            SQLIntervalUnit intervalUnit = null;
            final String unit = this.lexer.stringVal();
            final long unitHash = this.lexer.hash_lower();
            this.lexer.nextToken();
            intervalUnit = SQLIntervalUnit.valueOf(unit.toUpperCase());
            if (this.lexer.token() == Token.TO) {
                this.lexer.nextToken();
                if (unitHash == FnvHash.Constants.YEAR) {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.MONTH)) {
                        throw new ParserException("Syntax error. " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    intervalUnit = SQLIntervalUnit.YEAR_MONTH;
                }
                else if (unitHash == FnvHash.Constants.DAY) {
                    if (this.lexer.identifierEquals(FnvHash.Constants.HOUR)) {
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.DAY_HOUR;
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.MINUTE)) {
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.DAY_MINUTE;
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.SECOND)) {
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.DAY_SECOND;
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.MICROSECOND)) {
                            throw new ParserException("Syntax error. " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.DAY_MICROSECOND;
                    }
                }
                else if (unitHash == FnvHash.Constants.HOUR) {
                    if (this.lexer.identifierEquals(FnvHash.Constants.MINUTE)) {
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.HOUR_MINUTE;
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.SECOND)) {
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.HOUR_SECOND;
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.MICROSECOND)) {
                            throw new ParserException("Syntax error. " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.HOUR_MICROSECOND;
                    }
                }
                else if (unitHash == FnvHash.Constants.MINUTE) {
                    if (this.lexer.identifierEquals(FnvHash.Constants.SECOND)) {
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.MINUTE_SECOND;
                    }
                    else {
                        if (!this.lexer.identifierEquals(FnvHash.Constants.MICROSECOND)) {
                            throw new ParserException("Syntax error. " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        intervalUnit = SQLIntervalUnit.MINUTE_MICROSECOND;
                    }
                }
                else {
                    if (unitHash != FnvHash.Constants.SECOND) {
                        throw new ParserException("Syntax error. " + this.lexer.info());
                    }
                    if (!this.lexer.identifierEquals(FnvHash.Constants.MICROSECOND)) {
                        throw new ParserException("Syntax error. " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    intervalUnit = SQLIntervalUnit.SECOND_MICROSECOND;
                }
            }
            final SQLIntervalExpr intervalExpr2 = new SQLIntervalExpr();
            intervalExpr2.setValue(value2);
            intervalExpr2.setUnit(intervalUnit);
            return intervalExpr2;
        }
    }
    
    @Override
    public SQLColumnDefinition parseColumn() {
        final SQLColumnDefinition column = new SQLColumnDefinition();
        column.setDbType(this.dbType);
        final SQLName name = this.name();
        column.setName(name);
        column.setDataType(this.parseDataType());
        if (column.getDataType() != null && column.getDataType().jdbcType() == 1 && this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() != Token.IDENTIFIER && this.lexer.token() != Token.LITERAL_CHARS) {
                throw new ParserException(this.lexer.info());
            }
            column.setCharsetExpr(this.primary());
        }
        while (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
            this.lexer.nextToken();
            SQLExpr collateExpr;
            if (this.lexer.token() == Token.IDENTIFIER) {
                collateExpr = new SQLIdentifierExpr(this.lexer.stringVal());
            }
            else {
                collateExpr = new SQLCharExpr(this.lexer.stringVal());
            }
            this.lexer.nextToken();
            column.setCollateExpr(collateExpr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.GENERATED)) {
            this.lexer.nextToken();
            this.acceptIdentifier("ALWAYS");
            this.accept(Token.AS);
            this.accept(Token.LPAREN);
            final SQLExpr expr = this.expr();
            this.accept(Token.RPAREN);
            column.setGeneratedAlawsAs(expr);
        }
        return this.parseColumnRest(column);
    }
    
    @Override
    public SQLColumnDefinition parseColumnRest(final SQLColumnDefinition column) {
        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            this.accept(Token.UPDATE);
            final SQLExpr expr = this.primary();
            column.setOnUpdate(expr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ENCODE)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            column.setEncode(this.charExpr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COMPRESSION)) {
            this.lexer.nextToken();
            this.accept(Token.EQ);
            column.setCompression(this.charExpr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER) || this.lexer.identifierEquals(FnvHash.Constants.CHARSET)) {
            if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
                this.lexer.nextToken();
                this.accept(Token.SET);
            }
            else {
                this.lexer.nextToken();
            }
            SQLExpr charSet;
            if (this.lexer.token() == Token.IDENTIFIER) {
                charSet = new SQLIdentifierExpr(this.lexer.stringVal());
            }
            else {
                charSet = new SQLCharExpr(this.lexer.stringVal());
            }
            this.lexer.nextToken();
            column.setCharsetExpr(charSet);
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals("disableindex")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.TRUE) {
                this.lexer.nextToken();
                column.setDisableIndex(true);
            }
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals("jsonIndexAttrs")) {
            this.lexer.nextToken();
            column.setJsonIndexAttrsExpr(new SQLIdentifierExpr(this.lexer.stringVal()));
            this.lexer.nextToken();
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals("precision")) {
            this.lexer.nextToken();
            final int precision = this.parseIntValue();
            this.acceptIdentifier("scale");
            final int scale = this.parseIntValue();
            final List<SQLExpr> arguments = column.getDataType().getArguments();
            arguments.add(new SQLIntegerExpr(precision));
            arguments.add(new SQLIntegerExpr(scale));
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
            this.lexer.nextToken();
            SQLExpr collateExpr;
            if (this.lexer.token() == Token.IDENTIFIER) {
                collateExpr = new SQLIdentifierExpr(this.lexer.stringVal());
            }
            else {
                collateExpr = new SQLCharExpr(this.lexer.stringVal());
            }
            this.lexer.nextToken();
            column.setCollateExpr(collateExpr);
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PRECISION) && column.getDataType().nameHashCode64() == FnvHash.Constants.DOUBLE) {
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals("COLUMN_FORMAT")) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            column.setFormat(expr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STORAGE)) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            column.setStorage(expr);
        }
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            final SQLExpr expr = this.expr();
            column.setAsExpr(expr);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.STORED) || this.lexer.identifierEquals("PERSISTENT")) {
            this.lexer.nextToken();
            column.setStored(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.VIRTUAL)) {
            this.lexer.nextToken();
            column.setVirtual(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DELIMITER)) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            column.setDelimiter(expr);
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals("delimiter_tokenizer")) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            column.setDelimiterTokenizer(expr);
            return this.parseColumnRest(column);
        }
        if (this.lexer.identifierEquals("nlp_tokenizer")) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            column.setNlpTokenizer(expr);
        }
        if (this.lexer.identifierEquals("value_type")) {
            this.lexer.nextToken();
            final SQLExpr expr = this.expr();
            column.setValueType(expr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLPROPERTIES)) {
            this.lexer.nextToken();
            this.parseAssignItem(column.getColProperties(), column);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ANNINDEX)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            final SQLAnnIndex annIndex = new SQLAnnIndex();
            while (true) {
                if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    final String type = this.lexer.stringVal();
                    annIndex.setIndexType(type);
                    this.accept(Token.LITERAL_CHARS);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.RTTYPE)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    final String type = this.lexer.stringVal();
                    annIndex.setRtIndexType(type);
                    this.accept(Token.LITERAL_CHARS);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.DISTANCE)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    final String type = this.lexer.stringVal();
                    annIndex.setDistance(type);
                    this.accept(Token.LITERAL_CHARS);
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
            column.setAnnIndex(annIndex);
            return this.parseColumnRest(column);
        }
        super.parseColumnRest(column);
        return column;
    }
    
    @Override
    protected SQLDataType parseDataTypeRest(SQLDataType dataType) {
        super.parseDataTypeRest(dataType);
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.UNSIGNED)) {
                this.lexer.nextToken();
                ((SQLDataTypeImpl)dataType).setUnsigned(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.SIGNED)) {
                this.lexer.nextToken();
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.ZEROFILL)) {
                    break;
                }
                this.lexer.nextToken();
                ((SQLDataTypeImpl)dataType).setZerofill(true);
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ARRAY)) {
            this.lexer.nextToken();
            dataType = new SQLArrayDataType(dataType);
        }
        return dataType;
    }
    
    @Override
    public SQLAssignItem parseAssignItem(final boolean variant, final SQLObject parent) {
        final SQLAssignItem item = new SQLAssignItem();
        SQLExpr var = this.primary();
        String ident = null;
        long identHash = 0L;
        if (variant && var instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)var;
            ident = identExpr.getName();
            identHash = identExpr.hashCode64();
            if (identHash == FnvHash.Constants.GLOBAL) {
                ident = this.lexer.stringVal();
                this.lexer.nextToken();
                var = new SQLVariantRefExpr(ident, true);
            }
            else if (identHash == FnvHash.Constants.SESSION) {
                ident = this.lexer.stringVal();
                this.lexer.nextToken();
                var = new SQLVariantRefExpr(ident, false, true);
            }
            else {
                var = new SQLVariantRefExpr(ident);
            }
        }
        if (identHash == FnvHash.Constants.NAMES) {
            String charset = this.lexer.stringVal();
            SQLExpr varExpr = null;
            boolean chars = false;
            final Token token = this.lexer.token();
            if (token == Token.IDENTIFIER) {
                this.lexer.nextToken();
            }
            else if (token == Token.DEFAULT) {
                charset = "DEFAULT";
                this.lexer.nextToken();
            }
            else if (token == Token.QUES) {
                varExpr = new SQLVariantRefExpr("?");
                this.lexer.nextToken();
            }
            else {
                chars = true;
                this.accept(Token.LITERAL_CHARS);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                final MySqlCharExpr charsetExpr = new MySqlCharExpr(charset);
                this.lexer.nextToken();
                final String collate = this.lexer.stringVal();
                this.lexer.nextToken();
                charsetExpr.setCollate(collate);
                item.setValue(charsetExpr);
            }
            else if (varExpr != null) {
                item.setValue(varExpr);
            }
            else {
                item.setValue(chars ? new SQLCharExpr(charset) : new SQLIdentifierExpr(charset));
            }
            item.setTarget(var);
            return item;
        }
        if (identHash == FnvHash.Constants.CHARACTER) {
            var = new SQLVariantRefExpr("CHARACTER SET");
            this.accept(Token.SET);
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
        }
        else if (identHash == FnvHash.Constants.CHARSET) {
            var = new SQLVariantRefExpr("CHARACTER SET");
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
        }
        else if (identHash == FnvHash.Constants.TRANSACTION) {
            var = new SQLVariantRefExpr("TRANSACTION");
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
        }
        else if (this.lexer.token() == Token.COLONEQ) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.EQ);
        }
        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            item.setValue(new SQLIdentifierExpr("ON"));
        }
        else {
            item.setValue(this.expr());
        }
        item.setTarget(var);
        return item;
    }
    
    @Override
    public SQLName nameRest(final SQLName name) {
        if (this.lexer.token() == Token.VARIANT && "@".equals(this.lexer.stringVal())) {
            this.lexer.nextToken();
            final MySqlUserName userName = new MySqlUserName();
            userName.setUserName(((SQLIdentifierExpr)name).getName());
            if (this.lexer.token() == Token.LITERAL_CHARS) {
                userName.setHost("'" + this.lexer.stringVal() + "'");
            }
            else {
                userName.setHost(this.lexer.stringVal());
            }
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.IDENTIFIED)) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                userName.setIdentifiedBy(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            return userName;
        }
        return super.nameRest(name);
    }
    
    @Override
    public MySqlPrimaryKey parsePrimaryKey() {
        final MySqlPrimaryKey primaryKey = new MySqlPrimaryKey();
        this.parseIndex(primaryKey.getIndexDefinition());
        return primaryKey;
    }
    
    @Override
    public MySqlUnique parseUnique() {
        final MySqlUnique unique = new MySqlUnique();
        this.parseIndex(unique.getIndexDefinition());
        return unique;
    }
    
    @Override
    public MysqlForeignKey parseForeignKey() {
        this.accept(Token.FOREIGN);
        this.accept(Token.KEY);
        final MysqlForeignKey fk = new MysqlForeignKey();
        if (this.lexer.token() != Token.LPAREN) {
            final SQLName indexName = this.name();
            fk.setIndexName(indexName);
        }
        this.accept(Token.LPAREN);
        this.names(fk.getReferencingColumns(), fk);
        this.accept(Token.RPAREN);
        this.accept(Token.REFERENCES);
        fk.setReferencedTableName(this.name());
        this.accept(Token.LPAREN);
        this.names(fk.getReferencedColumns());
        this.accept(Token.RPAREN);
        if (this.lexer.identifierEquals(FnvHash.Constants.MATCH)) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("FULL") || this.lexer.token() == Token.FULL) {
                fk.setReferenceMatch(SQLForeignKeyImpl.Match.FULL);
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PARTIAL)) {
                fk.setReferenceMatch(SQLForeignKeyImpl.Match.PARTIAL);
                this.lexer.nextToken();
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.SIMPLE)) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                fk.setReferenceMatch(SQLForeignKeyImpl.Match.SIMPLE);
                this.lexer.nextToken();
            }
        }
        while (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.DELETE) {
                this.lexer.nextToken();
                final SQLForeignKeyImpl.Option option = this.parseReferenceOption();
                fk.setOnDelete(option);
            }
            else {
                if (this.lexer.token() != Token.UPDATE) {
                    throw new ParserException("syntax error, expect DELETE or UPDATE, actual " + this.lexer.token() + " " + this.lexer.info());
                }
                this.lexer.nextToken();
                final SQLForeignKeyImpl.Option option = this.parseReferenceOption();
                fk.setOnUpdate(option);
            }
        }
        return fk;
    }
    
    @Override
    protected SQLAggregateExpr parseAggregateExprRest(final SQLAggregateExpr aggregateExpr) {
        if (this.lexer.token() == Token.ORDER) {
            final SQLOrderBy orderBy = this.parseOrderBy();
            aggregateExpr.setOrderBy(orderBy);
            aggregateExpr.putAttribute("ORDER BY", orderBy);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SEPARATOR)) {
            this.lexer.nextToken();
            final SQLExpr seperator = this.primary();
            seperator.setParent(aggregateExpr);
            aggregateExpr.putAttribute("SEPARATOR", seperator);
        }
        return aggregateExpr;
    }
    
    public MySqlOrderingExpr parseSelectGroupByItem() {
        final MySqlOrderingExpr item = new MySqlOrderingExpr();
        item.setExpr(this.expr());
        if (this.lexer.token() == Token.ASC) {
            this.lexer.nextToken();
            item.setType(SQLOrderingSpecification.ASC);
        }
        else if (this.lexer.token() == Token.DESC) {
            this.lexer.nextToken();
            item.setType(SQLOrderingSpecification.DESC);
        }
        return item;
    }
    
    public SQLSubPartition parseSubPartition() {
        final SQLSubPartition subPartition = new SQLSubPartition();
        subPartition.setName(this.name());
        while (true) {
            boolean storage = false;
            if (this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
                this.lexer.nextToken();
                this.acceptIdentifier("DIRECTORY");
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                subPartition.setDataDirectory(this.expr());
            }
            else if (this.lexer.token() == Token.TABLESPACE) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLName tableSpace = this.name();
                subPartition.setTablespace(tableSpace);
            }
            else if (this.lexer.token() == Token.INDEX) {
                this.lexer.nextToken();
                this.acceptIdentifier("DIRECTORY");
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                subPartition.setIndexDirectory(this.expr());
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.MAX_ROWS)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr maxRows = this.primary();
                subPartition.setMaxRows(maxRows);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.MIN_ROWS)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr minRows = this.primary();
                subPartition.setMinRows(minRows);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE) || (storage = (this.lexer.token() == Token.STORAGE || this.lexer.identifierEquals(FnvHash.Constants.STORAGE)))) {
                if (storage) {
                    this.lexer.nextToken();
                }
                this.acceptIdentifier("ENGINE");
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLName engine = this.name();
                subPartition.setEngine(engine);
            }
            else {
                if (this.lexer.token() != Token.COMMENT) {
                    break;
                }
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr comment = this.primary();
                subPartition.setComment(comment);
            }
        }
        return subPartition;
    }
    
    public SQLPartition parsePartition() {
        if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITION) || this.lexer.identifierEquals(FnvHash.Constants.TBPARTITION) || this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
            this.lexer.nextToken();
        }
        else {
            this.accept(Token.PARTITION);
        }
        final SQLPartition partitionDef = new SQLPartition();
        partitionDef.setName(this.name());
        final SQLPartitionValue values = this.parsePartitionValues();
        if (values != null) {
            partitionDef.setValues(values);
        }
        while (true) {
            boolean storage = false;
            if (this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
                this.lexer.nextToken();
                this.acceptIdentifier("DIRECTORY");
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                partitionDef.setDataDirectory(this.expr());
            }
            else if (this.lexer.token() == Token.TABLESPACE) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLName tableSpace = this.name();
                partitionDef.setTablespace(tableSpace);
            }
            else if (this.lexer.token() == Token.INDEX) {
                this.lexer.nextToken();
                this.acceptIdentifier("DIRECTORY");
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                partitionDef.setIndexDirectory(this.expr());
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.MAX_ROWS)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr maxRows = this.primary();
                partitionDef.setMaxRows(maxRows);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.MIN_ROWS)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr minRows = this.primary();
                partitionDef.setMaxRows(minRows);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE) || (storage = (this.lexer.token() == Token.STORAGE || this.lexer.identifierEquals(FnvHash.Constants.STORAGE)))) {
                if (storage) {
                    this.lexer.nextToken();
                }
                this.acceptIdentifier("ENGINE");
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLName engine = this.name();
                partitionDef.setEngine(engine);
            }
            else {
                if (this.lexer.token() != Token.COMMENT) {
                    break;
                }
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr comment = this.primary();
                partitionDef.setComment(comment);
            }
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                this.acceptIdentifier("SUBPARTITION");
                final SQLSubPartition subPartition = this.parseSubPartition();
                partitionDef.addSubPartition(subPartition);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        return partitionDef;
    }
    
    @Override
    protected SQLExpr parseAliasExpr(final String alias) {
        if (this.isEnabled(SQLParserFeature.KeepNameQuotes)) {
            return new SQLIdentifierExpr(alias);
        }
        final Lexer newLexer = new Lexer(alias);
        newLexer.nextTokenValue();
        return new SQLCharExpr(newLexer.stringVal());
    }
    
    public boolean parseTableOptions(final List<SQLAssignItem> assignItems, final SQLDDLStatement parent) {
        boolean succeed = false;
        while (this.lexer.token() != Token.EOF) {
            final long hash = this.lexer.hash_lower();
            final int idx = Arrays.binarySearch(MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS_CODES, hash);
            SQLAssignItem assignItem = null;
            Lexer.SavePoint mark = null;
            if (idx >= 0 && idx < MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS_CODES.length && MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS_CODES[idx] == hash && (this.lexer.token() == Token.IDENTIFIER || (this.lexer.token().name != null && this.lexer.token().name.length() == MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS[idx].length()))) {
                if (this.lexer.token() == Token.TABLESPACE) {
                    this.lexer.nextToken();
                    final MySqlCreateTableStatement.TableSpaceOption option = new MySqlCreateTableStatement.TableSpaceOption();
                    option.setName(this.name());
                    if (this.lexer.identifierEquals("STORAGE")) {
                        this.lexer.nextToken();
                        option.setStorage(this.name());
                    }
                    assignItem = new SQLAssignItem(new SQLIdentifierExpr("TABLESPACE"), option);
                }
                else if (this.lexer.token() == Token.UNION) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    this.accept(Token.LPAREN);
                    final SQLListExpr list = new SQLListExpr();
                    this.exprList(list.getItems(), list);
                    this.accept(Token.RPAREN);
                    assignItem = new SQLAssignItem(new SQLIdentifierExpr("UNION"), list);
                }
                else if (this.lexer.identifierEquals("PACK_KEYS")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    if (this.lexer.identifierEquals("PACK")) {
                        this.lexer.nextToken();
                        this.accept(Token.ALL);
                        assignItem = new SQLAssignItem(new SQLIdentifierExpr("PACK_KEYS"), new SQLIdentifierExpr("PACK ALL"));
                    }
                    else {
                        assignItem = new SQLAssignItem(new SQLIdentifierExpr("PACK_KEYS"), this.expr());
                    }
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    SQLExpr expr;
                    if (this.lexer.token() == Token.MERGE) {
                        expr = new SQLIdentifierExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                    }
                    else {
                        expr = this.expr();
                    }
                    assignItem = new SQLAssignItem(new SQLIdentifierExpr("ENGINE"), expr);
                }
                else {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    assignItem = new SQLAssignItem(new SQLIdentifierExpr(MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS[idx]), (idx == 9) ? this.charExpr() : this.expr());
                }
            }
            else {
                mark = this.lexer.mark();
                if (this.lexer.token() == Token.DEFAULT) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
                        this.lexer.nextToken();
                        this.accept(Token.SET);
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        assignItem = new SQLAssignItem(new SQLIdentifierExpr("CHARACTER SET"), this.expr());
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        assignItem = new SQLAssignItem(new SQLIdentifierExpr("COLLATE"), this.expr());
                    }
                }
                else if (hash == FnvHash.Constants.CHARACTER) {
                    this.lexer.nextToken();
                    this.accept(Token.SET);
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    assignItem = new SQLAssignItem(new SQLIdentifierExpr("CHARACTER SET"), this.expr());
                }
                else if (hash == FnvHash.Constants.DATA || this.lexer.token() == Token.INDEX) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("DIRECTORY")) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        assignItem = new SQLAssignItem(new SQLIdentifierExpr("COLLATE"), this.expr());
                    }
                }
            }
            if (assignItem == null) {
                if (mark != null) {
                    this.lexer.reset(mark);
                }
                return succeed;
            }
            assignItem.setParent(parent);
            assignItems.add(assignItem);
            succeed = true;
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token() == Token.EOF) {
                    break;
                }
                continue;
            }
        }
        return succeed;
    }
    
    static {
        final String[] strings = { "AVG", "ANY_VALUE", "BIT_AND", "BIT_OR", "BIT_XOR", "COUNT", "GROUP_CONCAT", "LISTAGG", "MAX", "MIN", "STD", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "SUM", "VAR_SAMP", "VARIANCE", "JSON_ARRAYAGG", "JSON_OBJECTAGG" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[MySqlExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(MySqlExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            MySqlExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
        final String[] options = { "AUTO_INCREMENT", "AVG_ROW_LENGTH", "CHECKSUM", "COLLATE", "COMMENT", "COMPRESSION", "CONNECTION", "DELAY_KEY_WRITE", "ENCRYPTION", "ENGINE", "INSERT_METHOD", "KEY_BLOCK_SIZE", "MAX_ROWS", "MIN_ROWS", "PACK_KEYS", "PASSWORD", "ROW_FORMAT", "STATS_AUTO_RECALC", "STATS_PERSISTENT", "STATS_SAMPLE_PAGES", "TABLESPACE", "UNION", "STORAGE_TYPE", "STORAGE_POLICY" };
        SINGLE_WORD_TABLE_OPTIONS_CODES = FnvHash.fnv1a_64_lower(options, true);
        SINGLE_WORD_TABLE_OPTIONS = new String[MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS_CODES.length];
        for (final String str2 : options) {
            final long hash2 = FnvHash.fnv1a_64_lower(str2);
            final int index2 = Arrays.binarySearch(MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS_CODES, hash2);
            MySqlExprParser.SINGLE_WORD_TABLE_OPTIONS[index2] = str2;
        }
    }
}
