// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.SQLLimit;
import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.ast.SQLPartitionByValue;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLPartitionSpec;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.util.HexBin;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.sql.ast.SQLCurrentUserExpr;
import com.alibaba.druid.sql.ast.SQLCurrentTimeExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.alibaba.druid.sql.ast.statement.SQLDefault;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.SQLIndex;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.SQLIndexDefinition;
import com.alibaba.druid.sql.ast.TDDLHint;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.statement.SQLSetStatement;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.statement.SQLAssignItem;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLColumnCheck;
import com.alibaba.druid.sql.ast.statement.SQLColumnReference;
import com.alibaba.druid.sql.ast.AutoIncrementType;
import com.alibaba.druid.sql.ast.statement.SQLColumnUniqueKey;
import com.alibaba.druid.sql.ast.statement.SQLColumnPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLNullConstraint;
import com.alibaba.druid.sql.ast.statement.SQLColumnConstraint;
import com.alibaba.druid.sql.ast.statement.SQLNotNullConstraint;
import com.alibaba.druid.sql.ast.SQLRowDataType;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLUnionDataType;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLTableDataType;
import com.alibaba.druid.sql.ast.SQLStructDataType;
import com.alibaba.druid.sql.ast.SQLMapDataType;
import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleArgumentExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExprGroup;
import com.alibaba.druid.sql.ast.SQLExprImpl;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.ast.expr.SQLContainsExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.expr.SQLInSubQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.util.StringUtils;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLZOrderBy;
import com.alibaba.druid.sql.ast.SQLOver;
import java.util.Arrays;
import com.alibaba.druid.sql.ast.statement.SQLLateralViewTableSource;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.expr.SQLAggregateOption;
import com.alibaba.druid.sql.ast.expr.SQLDbLinkExpr;
import com.alibaba.druid.sql.ast.SQLDataTypeRefExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;
import com.alibaba.druid.sql.ast.expr.SQLMatchAgainstExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLGroupingSetExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.expr.SQLCurrentOfCursorExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.expr.SQLAnyExpr;
import com.alibaba.druid.sql.ast.expr.SQLSomeExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.expr.SQLAllExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLInListExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLNotExpr;
import com.alibaba.druid.sql.ast.expr.SQLExistsExpr;
import com.alibaba.druid.sql.ast.expr.SQLCaseExpr;
import com.alibaba.druid.sql.ast.expr.SQLDefaultExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLNCharExpr;
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.expr.SQLRealExpr;
import com.alibaba.druid.sql.ast.expr.SQLBigIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLTinyIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLSmallIntExpr;
import com.alibaba.druid.sql.ast.expr.SQLFloatExpr;
import com.alibaba.druid.sql.ast.expr.SQLDoubleExpr;
import com.alibaba.druid.sql.ast.expr.SQLDecimalExpr;
import com.alibaba.druid.sql.ast.expr.SQLJSONExpr;
import com.alibaba.druid.sql.ast.expr.SQLHexExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLBooleanExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateTimeExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLTimeExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLCastExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.math.BigInteger;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.DbType;

public class SQLExprParser extends SQLParser
{
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    protected String[] aggregateFunctions;
    protected long[] aggregateFunctionHashCodes;
    protected boolean allowIdentifierMethod;
    
    public SQLExprParser(final String sql) {
        super(sql);
        this.aggregateFunctions = SQLExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = SQLExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.allowIdentifierMethod = true;
    }
    
    public SQLExprParser(final String sql, final DbType dbType, final SQLParserFeature... features) {
        super(sql, dbType, features);
        this.aggregateFunctions = SQLExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = SQLExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.allowIdentifierMethod = true;
    }
    
    public SQLExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = SQLExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = SQLExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.allowIdentifierMethod = true;
    }
    
    public SQLExprParser(final Lexer lexer, final DbType dbType) {
        super(lexer, dbType);
        this.aggregateFunctions = SQLExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = SQLExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.allowIdentifierMethod = true;
    }
    
    public void setAllowIdentifierMethod(final boolean allowIdentifierMethod) {
        this.allowIdentifierMethod = allowIdentifierMethod;
    }
    
    public SQLExpr expr() {
        if (this.lexer.token == Token.STAR) {
            this.lexer.nextToken();
            final SQLExpr expr = new SQLAllColumnExpr();
            if (this.lexer.token == Token.DOT) {
                this.lexer.nextToken();
                this.accept(Token.STAR);
                return new SQLPropertyExpr(expr, "*");
            }
            return expr;
        }
        else {
            SQLExpr expr = this.primary();
            final Token token = this.lexer.token;
            if (token == Token.COMMA) {
                return expr;
            }
            if (token == Token.EQ || token == Token.EQEQ) {
                expr = this.relationalRest(expr);
                expr = this.andRest(expr);
                expr = this.xorRest(expr);
                expr = this.orRest(expr);
                return expr;
            }
            return this.exprRest(expr);
        }
    }
    
    public SQLExpr exprRest(SQLExpr expr) {
        expr = this.bitXorRest(expr);
        expr = this.multiplicativeRest(expr);
        expr = this.additiveRest(expr);
        expr = this.shiftRest(expr);
        expr = this.bitAndRest(expr);
        expr = this.bitOrRest(expr);
        expr = this.inRest(expr);
        expr = this.relationalRest(expr);
        expr = this.andRest(expr);
        expr = this.xorRest(expr);
        expr = this.orRest(expr);
        return expr;
    }
    
    public final SQLExpr bitXor() {
        final SQLExpr expr = this.primary();
        return this.bitXorRest(expr);
    }
    
    public SQLExpr bitXorRest(SQLExpr expr) {
        final Token token = this.lexer.token;
        switch (token) {
            case CARET: {
                this.lexer.nextToken();
                SQLBinaryOperator op;
                if (this.lexer.token == Token.EQ) {
                    this.lexer.nextToken();
                    op = SQLBinaryOperator.BitwiseXorEQ;
                }
                else {
                    op = SQLBinaryOperator.BitwiseXor;
                }
                final SQLExpr rightExp = this.primary();
                expr = new SQLBinaryOpExpr(expr, op, rightExp, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case SUBGT: {
                this.lexer.nextToken();
                SQLExpr rightExp2;
                if (this.dbType == DbType.mysql) {
                    if (this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.LITERAL_ALIAS) {
                        rightExp2 = this.primary();
                    }
                    else {
                        rightExp2 = this.expr();
                    }
                }
                else {
                    rightExp2 = this.primary();
                }
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.SubGt, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case LT_SUB_GT: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.PG_ST_DISTANCE, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case SUBGTGT: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.SubGtGt, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case POUNDGT: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.PoundGt, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case POUNDGTGT: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.PoundGtGt, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case QUESQUES: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.QuesQues, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case QUESBAR: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.QuesBar, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
            case QUESAMP: {
                this.lexer.nextToken();
                final SQLExpr rightExp2 = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.QuesAmp, rightExp2, this.dbType);
                expr = this.bitXorRest(expr);
                break;
            }
        }
        return expr;
    }
    
    public final SQLExpr multiplicative() {
        final SQLExpr expr = this.bitXor();
        return this.multiplicativeRest(expr);
    }
    
    public SQLExpr multiplicativeRest(SQLExpr expr) {
        final Token token = this.lexer.token;
        if (token == Token.STAR) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.bitXor();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Multiply, rightExp, this.getDbType());
            expr = this.multiplicativeRest(expr);
        }
        else if (token == Token.SLASH) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.bitXor();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Divide, rightExp, this.getDbType());
            expr = this.multiplicativeRest(expr);
        }
        else if (token == Token.PERCENT) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.bitXor();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Modulus, rightExp, this.getDbType());
            expr = this.multiplicativeRest(expr);
        }
        else if (token == Token.DIV) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.bitXor();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.DIV, rightExp, this.getDbType());
            expr = this.multiplicativeRest(expr);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MOD) || this.lexer.token == Token.MOD) {
            final Lexer.SavePoint savePoint = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token == Token.COMMA || this.lexer.token == Token.EOF || this.lexer.token == Token.ON || this.lexer.token == Token.WHERE || this.lexer.token == Token.RPAREN) {
                this.lexer.reset(savePoint);
                return expr;
            }
            final SQLExpr rightExp2 = this.bitXor();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Modulus, rightExp2, this.dbType);
            expr = this.multiplicativeRest(expr);
        }
        else if (token == Token.LITERAL_INT && this.lexer.isNegativeIntegerValue()) {
            Number number = this.lexer.integerValue();
            if (number instanceof Integer) {
                number = -number.intValue();
            }
            else if (number instanceof Long) {
                number = -number.longValue();
            }
            else {
                if (!(number instanceof BigInteger)) {
                    throw new ParserException("not support value : " + number + ", " + this.lexer.info());
                }
                number = ((BigInteger)number).abs();
            }
            final SQLIntegerExpr rightExp3 = new SQLIntegerExpr(number);
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Subtract, rightExp3, this.dbType);
            this.lexer.nextToken();
            expr = this.multiplicativeRest(expr);
        }
        return expr;
    }
    
    public SQLIntegerExpr integerExpr() {
        SQLIntegerExpr intExpr = null;
        if (this.lexer.token() == Token.SUB) {
            this.lexer.nextToken();
            intExpr = new SQLIntegerExpr(this.lexer.integerValue().longValue() * -1L);
        }
        else {
            intExpr = new SQLIntegerExpr(this.lexer.integerValue());
        }
        this.accept(Token.LITERAL_INT);
        return intExpr;
    }
    
    public SQLCharExpr charExpr() {
        final SQLCharExpr charExpr = new SQLCharExpr(this.lexer.stringVal());
        this.accept(Token.LITERAL_CHARS);
        return charExpr;
    }
    
    public int parseIntValue() {
        if (this.lexer.token == Token.LITERAL_INT) {
            final Number number = this.lexer.integerValue();
            final int intVal = (int)number;
            this.lexer.nextToken();
            return intVal;
        }
        throw new ParserException("not int. " + this.lexer.info());
    }
    
    public SQLExpr primary() {
        List<String> beforeComments = null;
        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
            beforeComments = this.lexer.readAndResetComments();
        }
        SQLExpr sqlExpr = null;
        final Token tok = this.lexer.token;
        Label_7259: {
            switch (tok) {
                case LPAREN: {
                    final int paranCount = 0;
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.RPAREN) {
                        this.lexer.nextToken();
                        sqlExpr = new SQLMethodInvokeExpr();
                        break;
                    }
                    sqlExpr = this.expr();
                    if (this.lexer.token == Token.COMMA) {
                        final SQLListExpr listExpr = new SQLListExpr();
                        listExpr.addItem(sqlExpr);
                        do {
                            this.lexer.nextToken();
                            listExpr.addItem(this.expr());
                        } while (this.lexer.token == Token.COMMA);
                        sqlExpr = listExpr;
                    }
                    if (sqlExpr instanceof SQLBinaryOpExpr) {
                        ((SQLBinaryOpExpr)sqlExpr).setParenthesized(true);
                    }
                    if ((this.lexer.token == Token.UNION || this.lexer.token == Token.MINUS || this.lexer.token == Token.EXCEPT) && sqlExpr instanceof SQLQueryExpr) {
                        final SQLQueryExpr queryExpr = (SQLQueryExpr)sqlExpr;
                        final SQLSelectQuery query = this.createSelectParser().queryRest(queryExpr.getSubQuery().getQuery(), true);
                        queryExpr.getSubQuery().setQuery(query);
                    }
                    this.accept(Token.RPAREN);
                    break;
                }
                case INSERT: {
                    this.lexer.nextToken();
                    if (this.lexer.token != Token.LPAREN) {
                        throw new ParserException("syntax error. " + this.lexer.info());
                    }
                    sqlExpr = new SQLIdentifierExpr("INSERT");
                    break;
                }
                case IDENTIFIER: {
                    String ident = this.lexer.stringVal();
                    long hash_lower = this.lexer.hash_lower();
                    int sourceLine = -1;
                    int sourceColumn = -1;
                    if (this.lexer.keepSourceLocation) {
                        this.lexer.computeRowAndColumn();
                        sourceLine = this.lexer.posLine;
                        sourceColumn = this.lexer.posColumn;
                    }
                    this.lexer.nextToken();
                    if (hash_lower == FnvHash.Constants.TRY_CAST) {
                        this.accept(Token.LPAREN);
                        final SQLCastExpr cast = new SQLCastExpr();
                        cast.setTry(true);
                        cast.setExpr(this.expr());
                        this.accept(Token.AS);
                        cast.setDataType(this.parseDataType(false));
                        this.accept(Token.RPAREN);
                        sqlExpr = cast;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.DATE && (this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.VARIANT) && SQLDateExpr.isSupport(this.dbType)) {
                        final String literal = (this.lexer.token == Token.LITERAL_CHARS) ? this.lexer.stringVal() : "?";
                        this.lexer.nextToken();
                        final SQLDateExpr dateExpr = new SQLDateExpr();
                        dateExpr.setLiteral(literal);
                        sqlExpr = dateExpr;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.TIMESTAMP && (this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.VARIANT) && DbType.oracle != this.dbType) {
                        final SQLTimestampExpr dateExpr2 = new SQLTimestampExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        sqlExpr = dateExpr2;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.TIME && (this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.VARIANT)) {
                        final SQLTimeExpr dateExpr3 = new SQLTimeExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        sqlExpr = dateExpr3;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.TIME && this.lexer.token == Token.LITERAL_ALIAS) {
                        final SQLTimeExpr dateExpr3 = new SQLTimeExpr(SQLUtils.normalize(this.lexer.stringVal()));
                        this.lexer.nextToken();
                        sqlExpr = dateExpr3;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.DATETIME && (this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.VARIANT)) {
                        final SQLDateTimeExpr dateExpr4 = new SQLDateTimeExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        sqlExpr = dateExpr4;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.DATETIME && this.lexer.token == Token.LITERAL_ALIAS) {
                        final SQLDateTimeExpr dateExpr4 = new SQLDateTimeExpr(SQLUtils.normalize(this.lexer.stringVal()));
                        this.lexer.nextToken();
                        sqlExpr = dateExpr4;
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.BOOLEAN && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLBooleanExpr(Boolean.valueOf(this.lexer.stringVal()));
                        this.lexer.nextToken();
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.VARCHAR && this.lexer.token == Token.LITERAL_CHARS) {
                        if (this.dbType == DbType.mysql) {
                            final MySqlCharExpr mysqlChar = new MySqlCharExpr(this.lexer.stringVal());
                            mysqlChar.setType("VARCHAR");
                            sqlExpr = mysqlChar;
                        }
                        else {
                            sqlExpr = new SQLCharExpr(this.lexer.stringVal());
                        }
                        this.lexer.nextToken();
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.CHAR && this.lexer.token == Token.LITERAL_CHARS) {
                        if (this.dbType == DbType.mysql) {
                            final MySqlCharExpr mysqlChar = new MySqlCharExpr(this.lexer.stringVal());
                            mysqlChar.setType("CHAR");
                            sqlExpr = mysqlChar;
                        }
                        else {
                            sqlExpr = new SQLCharExpr(this.lexer.stringVal());
                        }
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && ident.startsWith("0x") && ident.length() % 2 == 0) {
                        sqlExpr = new SQLHexExpr(ident.substring(2));
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.JSON && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLJSONExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.DECIMAL && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLDecimalExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.DOUBLE && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLDoubleExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.FLOAT && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLFloatExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.SMALLINT && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLSmallIntExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.TINYINT && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLTinyIntExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.BIGINT && this.lexer.token == Token.LITERAL_CHARS) {
                        String strVal = this.lexer.stringVal();
                        if (strVal.startsWith("--")) {
                            strVal = strVal.substring(2);
                        }
                        sqlExpr = new SQLBigIntExpr(strVal);
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.INTEGER && this.lexer.token == Token.LITERAL_CHARS) {
                        String strVal = this.lexer.stringVal();
                        if (strVal.startsWith("--")) {
                            strVal = strVal.substring(2);
                        }
                        final SQLIntegerExpr integerExpr = SQLIntegerExpr.ofIntOrLong(Long.parseLong(strVal));
                        integerExpr.setType("INTEGER");
                        sqlExpr = integerExpr;
                        this.lexer.nextToken();
                        break;
                    }
                    if (DbType.mysql == this.dbType && hash_lower == FnvHash.Constants.REAL && this.lexer.token == Token.LITERAL_CHARS) {
                        sqlExpr = new SQLRealExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.DATE && this.lexer.token == Token.LITERAL_ALIAS) {
                        sqlExpr = new SQLDateExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.DATETIME && this.lexer.token == Token.LITERAL_ALIAS) {
                        sqlExpr = new SQLDateTimeExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    if (hash_lower == FnvHash.Constants.TIMESTAMP && this.lexer.token == Token.LITERAL_ALIAS) {
                        sqlExpr = new SQLTimestampExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    final char c0 = ident.charAt(0);
                    if (c0 == '`' || c0 == '[' || c0 == '\"') {
                        if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                            ident = ident.substring(1, ident.length() - 1);
                        }
                        hash_lower = FnvHash.hashCode64(ident);
                    }
                    final SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(ident, hash_lower);
                    if (sourceLine != -1) {
                        identifierExpr.setSourceLine(sourceLine);
                        identifierExpr.setSourceColumn(sourceColumn);
                    }
                    sqlExpr = identifierExpr;
                    break;
                }
                case NEW: {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                case LITERAL_INT: {
                    final Number number = this.lexer.integerValue();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.BD)) {
                        final SQLDecimalExpr decimalExpr = new SQLDecimalExpr();
                        decimalExpr.setValue(new BigDecimal(number.intValue()));
                        this.lexer.nextToken();
                        sqlExpr = decimalExpr;
                        break;
                    }
                    sqlExpr = new SQLIntegerExpr(number);
                    break;
                }
                case LITERAL_FLOAT: {
                    sqlExpr = this.lexer.numberExpr();
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.BD)) {
                        final SQLDecimalExpr decimalExpr = new SQLDecimalExpr();
                        decimalExpr.setValue(new BigDecimal(sqlExpr.toString()));
                        this.lexer.nextToken();
                        sqlExpr = decimalExpr;
                        break;
                    }
                    break;
                }
                case LITERAL_CHARS: {
                    sqlExpr = new SQLCharExpr(this.lexer.stringVal());
                    if (DbType.mysql != this.dbType) {
                        this.lexer.nextToken();
                        break;
                    }
                    this.lexer.nextTokenValue();
                    while (true) {
                        if (this.lexer.token == Token.LITERAL_ALIAS) {
                            String concat = ((SQLCharExpr)sqlExpr).getText();
                            concat += this.lexer.stringVal();
                            this.lexer.nextTokenValue();
                            sqlExpr = new SQLCharExpr(concat);
                        }
                        else {
                            if (this.lexer.token != Token.LITERAL_CHARS && this.lexer.token != Token.LITERAL_NCHARS) {
                                break Label_7259;
                            }
                            String concat = ((SQLCharExpr)sqlExpr).getText();
                            concat += this.lexer.stringVal();
                            this.lexer.nextTokenValue();
                            sqlExpr = new SQLCharExpr(concat);
                        }
                    }
                    break;
                }
                case LITERAL_NCHARS: {
                    sqlExpr = new SQLNCharExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    if (DbType.mysql == this.dbType) {
                        SQLMethodInvokeExpr concat2 = null;
                        while (true) {
                            if (this.lexer.token == Token.LITERAL_ALIAS) {
                                if (concat2 == null) {
                                    concat2 = new SQLMethodInvokeExpr("CONCAT");
                                    concat2.addArgument(sqlExpr);
                                    sqlExpr = concat2;
                                }
                                final String alias = this.lexer.stringVal();
                                this.lexer.nextToken();
                                final SQLCharExpr concat_right = new SQLCharExpr(alias.substring(1, alias.length() - 1));
                                concat2.addArgument(concat_right);
                            }
                            else {
                                if (this.lexer.token != Token.LITERAL_CHARS && this.lexer.token != Token.LITERAL_NCHARS) {
                                    break;
                                }
                                if (concat2 == null) {
                                    concat2 = new SQLMethodInvokeExpr("CONCAT");
                                    concat2.addArgument(sqlExpr);
                                    sqlExpr = concat2;
                                }
                                final String chars = this.lexer.stringVal();
                                this.lexer.nextToken();
                                final SQLCharExpr concat_right = new SQLCharExpr(chars);
                                concat2.addArgument(concat_right);
                            }
                        }
                        break;
                    }
                    break;
                }
                case VARIANT: {
                    String varName = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (varName.equals(":") && this.lexer.token == Token.IDENTIFIER && DbType.oracle == this.dbType) {
                        final String part2 = this.lexer.stringVal();
                        this.lexer.nextToken();
                        varName += part2;
                    }
                    final SQLVariantRefExpr varRefExpr = new SQLVariantRefExpr(varName);
                    if (varName.startsWith(":")) {
                        varRefExpr.setIndex(this.lexer.nextVarIndex());
                    }
                    if (varRefExpr.getName().equals("@") && (this.lexer.token == Token.LITERAL_CHARS || (this.lexer.token == Token.VARIANT && this.lexer.stringVal().startsWith("@")))) {
                        varRefExpr.setName("@'" + this.lexer.stringVal() + "'");
                        this.lexer.nextToken();
                    }
                    else if (varRefExpr.getName().equals("@@") && this.lexer.token == Token.LITERAL_CHARS) {
                        varRefExpr.setName("@@'" + this.lexer.stringVal() + "'");
                        this.lexer.nextToken();
                    }
                    sqlExpr = varRefExpr;
                    break;
                }
                case DEFAULT: {
                    if (this.dbType == DbType.clickhouse) {
                        sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                    }
                    else {
                        sqlExpr = new SQLDefaultExpr();
                    }
                    this.lexer.nextToken();
                    break;
                }
                case IF:
                case INDEX:
                case PRIMARY:
                case KEY:
                case REPLACE:
                case DUAL:
                case LIMIT:
                case SCHEMA:
                case COLUMN:
                case END:
                case COMMENT:
                case COMPUTE:
                case ENABLE:
                case DISABLE:
                case INITIALLY:
                case SEQUENCE:
                case USER:
                case EXPLAIN:
                case WITH:
                case GRANT:
                case PCTFREE:
                case INITRANS:
                case MAXTRANS:
                case SEGMENT:
                case CREATION:
                case IMMEDIATE:
                case DEFERRED:
                case STORAGE:
                case NEXT:
                case MINEXTENTS:
                case MAXEXTENTS:
                case MAXSIZE:
                case PCTINCREASE:
                case FLASH_CACHE:
                case CELL_FLASH_CACHE:
                case NONE:
                case LOB:
                case STORE:
                case ROW:
                case CHUNK:
                case CACHE:
                case NOCACHE:
                case LOGGING:
                case NOCOMPRESS:
                case KEEP_DUPLICATES:
                case EXCEPTIONS:
                case PURGE:
                case FULL:
                case TO:
                case IDENTIFIED:
                case PASSWORD:
                case BINARY:
                case WINDOW:
                case OFFSET:
                case SHARE:
                case START:
                case CONNECT:
                case MATCHED:
                case ERRORS:
                case REJECT:
                case UNLIMITED:
                case BEGIN:
                case EXCLUSIVE:
                case MODE:
                case ADVISE:
                case VIEW:
                case ESCAPE:
                case OVER:
                case ORDER:
                case CONSTRAINT:
                case TYPE:
                case OPEN:
                case REPEAT:
                case TABLE:
                case TRUNCATE:
                case EXCEPTION:
                case FUNCTION:
                case IDENTITY:
                case EXTRACT:
                case DESC:
                case DO:
                case GROUP:
                case MOD:
                case CONCAT:
                case PARTITION:
                case LEAVE:
                case CLOSE:
                case CONDITION:
                case OUT:
                case USE:
                case EXCEPT:
                case INTERSECT:
                case MERGE:
                case MINUS:
                case UNTIL:
                case TOP:
                case SHOW:
                case INOUT:
                case OUTER: {
                    sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    break;
                }
                case CASE: {
                    final SQLCaseExpr caseExpr = new SQLCaseExpr();
                    this.lexer.nextToken();
                    if (this.lexer.token != Token.WHEN) {
                        caseExpr.setValueExpr(this.expr());
                    }
                    this.accept(Token.WHEN);
                    SQLExpr testExpr = this.expr();
                    this.accept(Token.THEN);
                    SQLExpr valueExpr = this.expr();
                    SQLCaseExpr.Item caseItem = new SQLCaseExpr.Item(testExpr, valueExpr);
                    caseExpr.addItem(caseItem);
                    while (this.lexer.token == Token.WHEN) {
                        this.lexer.nextToken();
                        testExpr = this.expr();
                        this.accept(Token.THEN);
                        valueExpr = this.expr();
                        caseItem = new SQLCaseExpr.Item(testExpr, valueExpr);
                        caseExpr.addItem(caseItem);
                    }
                    if (this.lexer.token == Token.ELSE) {
                        this.lexer.nextToken();
                        caseExpr.setElseExpr(this.expr());
                    }
                    this.accept(Token.END);
                    sqlExpr = caseExpr;
                    break;
                }
                case EXISTS: {
                    final String strVal2 = this.lexer.stringVal();
                    this.lexer.nextToken();
                    switch (this.lexer.token) {
                        case COMMA:
                        case DOT: {
                            sqlExpr = new SQLIdentifierExpr(strVal2);
                            break Label_7259;
                        }
                        default: {
                            this.accept(Token.LPAREN);
                            sqlExpr = new SQLExistsExpr(this.createSelectParser().select());
                            this.accept(Token.RPAREN);
                            this.parseQueryPlanHint(sqlExpr);
                            break Label_7259;
                        }
                    }
                    break;
                }
                case NOT: {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.EXISTS) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        final SQLExistsExpr exists = new SQLExistsExpr(this.createSelectParser().select(), true);
                        this.accept(Token.RPAREN);
                        this.parseQueryPlanHint(exists);
                        if (this.lexer.token == Token.EQ) {
                            exists.setNot(false);
                            final SQLExpr relational = this.relationalRest(exists);
                            sqlExpr = new SQLNotExpr(relational);
                        }
                        else {
                            sqlExpr = exists;
                        }
                        break;
                    }
                    if (this.lexer.token == Token.LPAREN) {
                        this.lexer.nextToken();
                        SQLExpr notTarget = this.expr();
                        this.accept(Token.RPAREN);
                        notTarget = this.bitXorRest(notTarget);
                        notTarget = this.multiplicativeRest(notTarget);
                        notTarget = this.additiveRest(notTarget);
                        notTarget = this.shiftRest(notTarget);
                        notTarget = this.bitAndRest(notTarget);
                        notTarget = this.bitOrRest(notTarget);
                        notTarget = this.inRest(notTarget);
                        notTarget = this.relationalRest(notTarget);
                        sqlExpr = new SQLNotExpr(notTarget);
                        this.parseQueryPlanHint(sqlExpr);
                        return this.primaryRest(sqlExpr);
                    }
                    final SQLExpr restExpr = this.relational();
                    sqlExpr = new SQLNotExpr(restExpr);
                    this.parseQueryPlanHint(sqlExpr);
                    break;
                }
                case FROM:
                case SELECT: {
                    final SQLQueryExpr queryExpr2 = (SQLQueryExpr)(sqlExpr = new SQLQueryExpr(this.createSelectParser().select()));
                    break;
                }
                case CAST: {
                    final String castStr = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.lexer.token != Token.LPAREN) {
                        sqlExpr = new SQLIdentifierExpr(castStr);
                        break;
                    }
                    this.lexer.nextToken();
                    final SQLCastExpr cast2 = new SQLCastExpr();
                    cast2.setExpr(this.expr());
                    this.accept(Token.AS);
                    cast2.setDataType(this.parseDataType(false));
                    this.accept(Token.RPAREN);
                    sqlExpr = cast2;
                    break;
                }
                case SUB: {
                    this.lexer.nextToken();
                    switch (this.lexer.token) {
                        case LITERAL_INT: {
                            Number integerValue = this.lexer.integerValue();
                            if (integerValue instanceof Integer) {
                                final int intVal = integerValue.intValue();
                                if (intVal == Integer.MIN_VALUE) {
                                    integerValue = intVal * -1L;
                                }
                                else {
                                    integerValue = intVal * -1;
                                }
                            }
                            else if (integerValue instanceof Long) {
                                final long longVal = (long)integerValue;
                                if (longVal == 2147483648L) {
                                    integerValue = (int)(longVal * -1L);
                                }
                                else {
                                    integerValue = longVal * -1L;
                                }
                            }
                            else {
                                integerValue = ((BigInteger)integerValue).negate();
                            }
                            sqlExpr = new SQLIntegerExpr(integerValue);
                            this.lexer.nextToken();
                            break Label_7259;
                        }
                        case LITERAL_FLOAT: {
                            sqlExpr = this.lexer.numberExpr(true);
                            this.lexer.nextToken();
                            break Label_7259;
                        }
                        case LITERAL_CHARS:
                        case LITERAL_ALIAS: {
                            if (this.dbType == DbType.mysql) {
                                sqlExpr = new SQLCharExpr(this.lexer.stringVal());
                            }
                            else {
                                sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                            }
                            this.lexer.nextToken();
                            if (this.lexer.token == Token.LPAREN || this.lexer.token == Token.LBRACKET || this.lexer.token == Token.DOT) {
                                sqlExpr = this.primaryRest(sqlExpr);
                            }
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, sqlExpr);
                            break Label_7259;
                        }
                        case QUES: {
                            final SQLVariantRefExpr variantRefExpr = new SQLVariantRefExpr("?");
                            variantRefExpr.setIndex(this.lexer.nextVarIndex());
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, variantRefExpr);
                            this.lexer.nextToken();
                            break Label_7259;
                        }
                        case PLUS:
                        case SUB:
                        case LPAREN:
                        case IDENTIFIER:
                        case BANG:
                        case CASE:
                        case CAST:
                        case NULL:
                        case INTERVAL:
                        case LBRACE:
                        case IF:
                        case CHECK:
                        case INDEX:
                        case PRIMARY:
                        case KEY:
                        case REPLACE: {
                            sqlExpr = this.primary();
                            while (this.lexer.token == Token.HINT) {
                                this.lexer.nextToken();
                            }
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, sqlExpr);
                            break Label_7259;
                        }
                        case VARIANT: {
                            sqlExpr = this.primary();
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, sqlExpr);
                            break Label_7259;
                        }
                        default: {
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                    }
                    break;
                }
                case PLUS: {
                    this.lexer.nextToken();
                    switch (this.lexer.token) {
                        case LITERAL_CHARS:
                        case LITERAL_ALIAS: {
                            sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, sqlExpr);
                            this.lexer.nextToken();
                            break Label_7259;
                        }
                        case QUES: {
                            final SQLVariantRefExpr variantRefExpr2 = new SQLVariantRefExpr("?");
                            variantRefExpr2.setIndex(this.lexer.nextVarIndex());
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, variantRefExpr2);
                            this.lexer.nextToken();
                            break Label_7259;
                        }
                        case LITERAL_INT:
                        case LITERAL_FLOAT:
                        case PLUS:
                        case SUB:
                        case LPAREN:
                        case IDENTIFIER:
                        case BANG:
                        case CASE:
                        case CAST:
                        case NULL:
                        case INTERVAL:
                        case LBRACE:
                        case IF:
                        case CHECK:
                        case REPLACE: {
                            sqlExpr = this.primary();
                            while (this.lexer.token == Token.HINT) {
                                this.lexer.nextToken();
                            }
                            sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, sqlExpr);
                            break Label_7259;
                        }
                        default: {
                            throw new ParserException("TODO " + this.lexer.info());
                        }
                    }
                    break;
                }
                case TILDE: {
                    this.lexer.nextToken();
                    final SQLExpr unaryValueExpr = this.primary();
                    final SQLUnaryExpr unary = (SQLUnaryExpr)(sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Compl, unaryValueExpr));
                    break;
                }
                case QUES: {
                    if (this.dbType == DbType.mysql) {
                        this.lexer.nextTokenValue();
                    }
                    else {
                        this.lexer.nextToken();
                    }
                    final SQLVariantRefExpr quesVarRefExpr = new SQLVariantRefExpr("?");
                    quesVarRefExpr.setIndex(this.lexer.nextVarIndex());
                    sqlExpr = quesVarRefExpr;
                    break;
                }
                case LEFT: {
                    sqlExpr = new SQLIdentifierExpr("LEFT");
                    this.lexer.nextToken();
                    break;
                }
                case RIGHT: {
                    sqlExpr = new SQLIdentifierExpr("RIGHT");
                    this.lexer.nextToken();
                    break;
                }
                case INNER: {
                    sqlExpr = new SQLIdentifierExpr("INNER");
                    this.lexer.nextToken();
                    break;
                }
                case DATABASE: {
                    sqlExpr = new SQLIdentifierExpr("DATABASE");
                    this.lexer.nextToken();
                    break;
                }
                case LOCK: {
                    sqlExpr = new SQLIdentifierExpr("LOCK");
                    this.lexer.nextToken();
                    break;
                }
                case NULL: {
                    sqlExpr = new SQLNullExpr();
                    this.lexer.nextToken();
                    break;
                }
                case BANG: {
                    this.lexer.nextToken();
                    sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Not, this.primary());
                    break;
                }
                case BANGBANG: {
                    if (this.dbType == DbType.hive) {
                        throw new ParserException(this.lexer.info());
                    }
                    this.lexer.nextToken();
                    sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Not, this.primary());
                    break;
                }
                case BANG_TILDE: {
                    this.lexer.nextToken();
                    final SQLExpr bangExpr = this.primary();
                    sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Not, new SQLUnaryExpr(SQLUnaryOperator.Compl, bangExpr));
                    break;
                }
                case LITERAL_HEX: {
                    final String hex = this.lexer.hexString();
                    sqlExpr = new SQLHexExpr(hex);
                    this.lexer.nextToken();
                    break;
                }
                case INTERVAL: {
                    sqlExpr = this.parseInterval();
                    break;
                }
                case COLON: {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.LITERAL_ALIAS) {
                        sqlExpr = new SQLVariantRefExpr(":\"" + this.lexer.stringVal() + "\"");
                        this.lexer.nextToken();
                        break;
                    }
                    break;
                }
                case ANY: {
                    sqlExpr = this.parseAny();
                    break;
                }
                case SOME: {
                    sqlExpr = this.parseSome();
                    break;
                }
                case ALL: {
                    sqlExpr = this.parseAll();
                    break;
                }
                case LITERAL_ALIAS: {
                    sqlExpr = this.parseAliasExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    break;
                }
                case EOF: {
                    throw new EOFParserException();
                }
                case TRUE: {
                    this.lexer.nextToken();
                    sqlExpr = new SQLBooleanExpr(true);
                    break;
                }
                case FALSE: {
                    this.lexer.nextToken();
                    sqlExpr = new SQLBooleanExpr(false);
                    break;
                }
                case BITS: {
                    final String strVal3 = this.lexer.stringVal();
                    this.lexer.nextToken();
                    sqlExpr = new SQLBinaryExpr(strVal3);
                    break;
                }
                case GLOBAL:
                case CONTAINS: {
                    sqlExpr = this.inRest(null);
                    break;
                }
                case SET: {
                    final Lexer.SavePoint savePoint = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.SET && this.dbType == DbType.odps) {
                        this.lexer.nextToken();
                    }
                    if (this.lexer.token() == Token.LPAREN) {
                        sqlExpr = new SQLIdentifierExpr("SET");
                        break;
                    }
                    if (this.lexer.token == Token.DOT) {
                        sqlExpr = new SQLIdentifierExpr("SET");
                        sqlExpr = this.primaryRest(sqlExpr);
                        break;
                    }
                    this.lexer.reset(savePoint);
                    throw new ParserException("ERROR. " + this.lexer.info());
                }
                case LBRACE: {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.TS)) {
                        this.lexer.nextToken();
                        final String literal2 = this.lexer.stringVal();
                        this.lexer.nextToken();
                        sqlExpr = new SQLTimestampExpr(literal2);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.D) || this.lexer.identifierEquals(FnvHash.Constants.DATE)) {
                        this.lexer.nextToken();
                        String literal2 = this.lexer.stringVal();
                        if (literal2.length() > 2 && literal2.charAt(0) == '\"' && literal2.charAt(literal2.length() - 1) == '\"') {
                            literal2 = literal2.substring(1, literal2.length() - 1);
                        }
                        this.lexer.nextToken();
                        sqlExpr = new SQLDateExpr(literal2);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.T)) {
                        this.lexer.nextToken();
                        final String literal2 = this.lexer.stringVal();
                        this.lexer.nextToken();
                        sqlExpr = new SQLTimeExpr(literal2);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.FN)) {
                        this.lexer.nextToken();
                        sqlExpr = this.expr();
                    }
                    else {
                        if (DbType.mysql != this.dbType) {
                            throw new ParserException("ERROR. " + this.lexer.info());
                        }
                        sqlExpr = this.expr();
                    }
                    this.accept(Token.RBRACE);
                    break;
                }
                case COMMA:
                case CHECK:
                case IS:
                case VALUES:
                case TRIGGER:
                case FOR:
                case DELETE:
                case BY:
                case UPDATE:
                case LOOP:
                case LIKE:
                case UNION:
                case CREATE:
                case STAR:
                case DIV:
                case DISTRIBUTE:
                case UNIQUE:
                case PROCEDURE:
                case REFERENCES:
                case REVOKE:
                case DECLARE:
                case DROP:
                case RLIKE:
                case FOREIGN:
                case FETCH:
                case ASC:
                case CURSOR:
                case ALTER: {
                    if (this.dbType == DbType.odps || this.dbType == DbType.hive) {
                        sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                        break;
                    }
                    throw new ParserException("ERROR. " + this.lexer.info());
                }
                case AS: {
                    if (this.dbType == DbType.odps) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        final String str = this.lexer.stringVal();
                        this.lexer.nextToken();
                        switch (this.lexer.token) {
                            case COMMA:
                            case RPAREN:
                            case AS:
                            case EQ:
                            case EQEQ:
                            case LT:
                            case LTEQ:
                            case GT:
                            case GTEQ:
                            case LTGT:
                            case SEMI: {
                                sqlExpr = new SQLIdentifierExpr(str);
                                break;
                            }
                            case DOT: {
                                sqlExpr = this.primaryRest(new SQLIdentifierExpr(str));
                                break;
                            }
                            default: {
                                this.lexer.reset(mark);
                                break;
                            }
                        }
                        break;
                    }
                    break;
                }
                case DISTINCT: {
                    if (this.dbType != DbType.elastic_search && this.dbType != DbType.mysql) {
                        throw new ParserException("ERROR. " + this.lexer.info());
                    }
                    final Lexer.SavePoint mark = this.lexer.mark();
                    sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    if (this.lexer.token != Token.LPAREN) {
                        this.lexer.reset(mark);
                        throw new ParserException("ERROR. " + this.lexer.info());
                    }
                    break;
                }
                case BETWEEN:
                case IN: {
                    if (this.dbType != DbType.odps) {
                        throw new ParserException("ERROR. " + this.lexer.info());
                    }
                    final String str2 = this.lexer.stringVal();
                    this.lexer.nextToken();
                    switch (this.lexer.token) {
                        case COMMA:
                        case DOT:
                        case RPAREN:
                        case AS:
                        case EQ:
                        case LT:
                        case GT:
                        case IS: {
                            sqlExpr = new SQLIdentifierExpr(str2);
                            break;
                        }
                    }
                    if (sqlExpr != null) {
                        break;
                    }
                    this.accept(Token.LPAREN);
                    final SQLInListExpr in = new SQLInListExpr();
                    in.setExpr(this.expr());
                    if (this.lexer.token == Token.COMMA) {
                        this.lexer.nextToken();
                        this.exprList(in.getTargetList(), in);
                    }
                    this.accept(Token.RPAREN);
                    sqlExpr = in;
                    break;
                }
                case LBRACKET: {
                    if (this.dbType == DbType.odps || this.dbType == DbType.clickhouse) {
                        final SQLArrayExpr array = new SQLArrayExpr();
                        this.lexer.nextToken();
                        this.exprList(array.getValues(), array);
                        this.accept(Token.RBRACKET);
                        sqlExpr = array;
                        break;
                    }
                    throw new ParserException("ERROR. " + this.lexer.info());
                }
                case ON: {
                    if (this.dbType == DbType.postgresql) {
                        final String methodName = this.lexer.stringVal();
                        this.lexer.nextToken();
                        if (this.lexer.token == Token.LPAREN) {
                            sqlExpr = this.methodRest(new SQLIdentifierExpr(methodName), true);
                            break;
                        }
                    }
                    throw new ParserException("ERROR. " + this.lexer.info());
                }
                case COLONCOLON: {
                    if (this.dbType != DbType.odps) {
                        throw new ParserException("ERROR. " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    final SQLExpr temp = this.primary();
                    if (temp instanceof SQLArrayExpr) {
                        sqlExpr = temp;
                        break;
                    }
                    final SQLMethodInvokeExpr method = (SQLMethodInvokeExpr)temp;
                    method.setOwner(new SQLIdentifierExpr(""));
                    sqlExpr = method;
                    break;
                }
                case ARRAY: {
                    final SQLArrayExpr array = new SQLArrayExpr();
                    array.setExpr(new SQLIdentifierExpr("ARRAY"));
                    this.lexer.nextToken();
                    this.accept(Token.LBRACKET);
                    this.exprList(array.getValues(), array);
                    this.accept(Token.RBRACKET);
                    sqlExpr = array;
                    break;
                }
                default: {
                    throw new ParserException("ERROR. " + this.lexer.info());
                }
            }
        }
        final SQLExpr expr = this.primaryRest(sqlExpr);
        if (beforeComments != null) {
            expr.addBeforeComment(beforeComments);
        }
        return expr;
    }
    
    protected SQLExpr parseAll() {
        final String str = this.lexer.stringVal();
        this.lexer.nextToken();
        switch (this.lexer.token) {
            case DOT:
            case EQ:
            case LT:
            case LTEQ:
            case GT:
            case GTEQ:
            case STAR:
            case DIV:
            case SLASH: {
                return this.primaryRest(new SQLIdentifierExpr(str));
            }
            case COMMA:
            case PLUS:
            case SUB:
            case RPAREN:
            case AS:
            case SEMI:
            case GROUP:
            case FROM:
            case WHERE: {
                return new SQLIdentifierExpr(str);
            }
            case IDENTIFIER: {
                if (this.dbType == DbType.odps) {
                    return new SQLIdentifierExpr(str);
                }
                break;
            }
        }
        final SQLAllExpr allExpr = new SQLAllExpr();
        this.accept(Token.LPAREN);
        final SQLSelect allSubQuery = this.createSelectParser().select();
        allExpr.setSubQuery(allSubQuery);
        this.accept(Token.RPAREN);
        allSubQuery.setParent(allExpr);
        final SQLExpr sqlExpr = allExpr;
        return sqlExpr;
    }
    
    protected SQLExpr parseSome() {
        final String str = this.lexer.stringVal();
        this.lexer.nextToken();
        if (this.lexer.token != Token.LPAREN) {
            return new SQLIdentifierExpr(str);
        }
        this.lexer.nextToken();
        final SQLSomeExpr someExpr = new SQLSomeExpr();
        final SQLSelect someSubQuery = this.createSelectParser().select();
        someExpr.setSubQuery(someSubQuery);
        this.accept(Token.RPAREN);
        someSubQuery.setParent(someExpr);
        final SQLExpr sqlExpr = someExpr;
        return sqlExpr;
    }
    
    protected SQLExpr parseAny() {
        this.lexer.nextToken();
        SQLExpr sqlExpr;
        if (this.lexer.token == Token.LPAREN) {
            this.accept(Token.LPAREN);
            if (this.lexer.token == Token.ARRAY || this.lexer.token == Token.IDENTIFIER) {
                final SQLExpr expr = this.expr();
                final SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr("ANY");
                methodInvokeExpr.addArgument(expr);
                this.accept(Token.RPAREN);
                return methodInvokeExpr;
            }
            final SQLSelect anySubQuery = this.createSelectParser().select();
            final SQLAnyExpr anyExpr = new SQLAnyExpr(anySubQuery);
            this.accept(Token.RPAREN);
            sqlExpr = anyExpr;
        }
        else {
            sqlExpr = new SQLIdentifierExpr("ANY");
        }
        return sqlExpr;
    }
    
    protected SQLExpr parseAliasExpr(final String alias) {
        return new SQLIdentifierExpr(alias);
    }
    
    protected SQLExpr parseInterval() {
        final String str = this.lexer.stringVal();
        this.accept(Token.INTERVAL);
        switch (this.lexer.token) {
            case COMMA:
            case DOT:
            case RPAREN:
            case AS:
            case EQ:
            case LT:
            case LTEQ:
            case GT:
            case GTEQ:
            case LTGT:
            case SEMI:
            case IS:
            case END:
            case ORDER:
            case DESC:
            case FROM:
            case UNION:
            case STAR:
            case DIV:
            case ASC:
            case BETWEEN:
            case IN:
            case SLASH:
            case BANGEQ:
            case THEN:
            case ELSE: {
                return new SQLIdentifierExpr(str);
            }
            case PLUS:
            case SUB: {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.token == Token.LITERAL_INT) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.IDENTIFIER) {
                        this.lexer.reset(mark);
                        break;
                    }
                }
                else {
                    this.lexer.reset(mark);
                }
                return new SQLIdentifierExpr(str);
            }
            default: {
                if (this.lexer.identifierEquals(FnvHash.Constants.GROUPING)) {
                    return new SQLIdentifierExpr(str);
                }
                break;
            }
        }
        final Lexer.SavePoint mark = this.lexer.mark();
        final SQLExpr value = this.expr();
        switch (this.lexer.token) {
            case COMMA:
            case RPAREN:
            case AS:
            case ORDER:
            case FROM:
            case WHERE: {
                this.lexer.reset(mark);
                return new SQLIdentifierExpr(str);
            }
            default: {
                if (this.lexer.token() != Token.IDENTIFIER) {
                    throw new ParserException("Syntax error. " + this.lexer.info());
                }
                String unit = this.lexer.stringVal().toUpperCase();
                this.lexer.nextToken();
                if (unit.equals("DAYS")) {
                    unit = "DAY";
                }
                final SQLIntervalExpr intervalExpr = new SQLIntervalExpr();
                intervalExpr.setValue(value);
                intervalExpr.setUnit(SQLIntervalUnit.valueOf(unit));
                return intervalExpr;
            }
        }
    }
    
    public SQLSelectParser createSelectParser() {
        return new SQLSelectParser(this);
    }
    
    public SQLExpr primaryRest(SQLExpr expr) {
        if (expr == null) {
            throw new IllegalArgumentException("expr");
        }
        final Token token = this.lexer.token;
        if (token == Token.OF) {
            if (expr instanceof SQLIdentifierExpr) {
                final long hashCode64 = ((SQLIdentifierExpr)expr).hashCode64();
                if (hashCode64 == FnvHash.Constants.CURRENT) {
                    this.lexer.nextToken();
                    final SQLName cursorName = this.name();
                    return new SQLCurrentOfCursorExpr(cursorName);
                }
            }
        }
        else if (token == Token.FOR && expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr idenExpr = (SQLIdentifierExpr)expr;
            if (idenExpr.hashCode64() == FnvHash.Constants.NEXTVAL) {
                this.lexer.nextToken();
                final SQLName seqName = this.name();
                final SQLSequenceExpr seqExpr = new SQLSequenceExpr(seqName, SQLSequenceExpr.Function.NextVal);
                return seqExpr;
            }
            if (idenExpr.hashCode64() == FnvHash.Constants.CURRVAL) {
                this.lexer.nextToken();
                final SQLName seqName = this.name();
                final SQLSequenceExpr seqExpr = new SQLSequenceExpr(seqName, SQLSequenceExpr.Function.CurrVal);
                return seqExpr;
            }
            if (idenExpr.hashCode64() == FnvHash.Constants.PREVVAL) {
                this.lexer.nextToken();
                final SQLName seqName = this.name();
                final SQLSequenceExpr seqExpr = new SQLSequenceExpr(seqName, SQLSequenceExpr.Function.PrevVal);
                return seqExpr;
            }
        }
        if (token == Token.DOT) {
            this.lexer.nextToken();
            if (expr instanceof SQLCharExpr) {
                final String text = ((SQLCharExpr)expr).getText();
                expr = new SQLIdentifierExpr(text);
            }
            expr = this.dotRest(expr);
            return this.primaryRest(expr);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SETS) && expr.getClass() == SQLIdentifierExpr.class && "GROUPING".equalsIgnoreCase(((SQLIdentifierExpr)expr).getName())) {
            final SQLGroupingSetExpr groupingSets = new SQLGroupingSetExpr();
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            while (true) {
                SQLExpr item;
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.COMMA && this.dbType == DbType.odps) {
                        this.lexer.nextToken();
                    }
                    final SQLListExpr listExpr = new SQLListExpr();
                    this.exprList(listExpr.getItems(), listExpr);
                    item = listExpr;
                    this.accept(Token.RPAREN);
                }
                else {
                    item = this.expr();
                }
                item.setParent(groupingSets);
                groupingSets.addParameter(item);
                if (this.lexer.token == Token.RPAREN) {
                    break;
                }
                this.accept(Token.COMMA);
            }
            this.exprList(groupingSets.getParameters(), groupingSets);
            this.accept(Token.RPAREN);
            return groupingSets;
        }
        if (this.lexer.token == Token.LPAREN && !(expr instanceof SQLIntegerExpr) && !(expr instanceof SQLHexExpr)) {
            SQLExpr method = this.methodRest(expr, true);
            if (this.lexer.token == Token.LBRACKET || this.lexer.token == Token.DOT) {
                method = this.primaryRest(method);
            }
            return method;
        }
        return expr;
    }
    
    protected SQLExpr parseExtract() {
        throw new ParserException("not supported.");
    }
    
    protected SQLExpr parsePosition() {
        throw new ParserException("not supported.");
    }
    
    protected SQLExpr parseMatch() {
        final SQLMatchAgainstExpr matchAgainstExpr = new SQLMatchAgainstExpr();
        if (this.lexer.token() == Token.RPAREN) {
            this.lexer.nextToken();
        }
        else {
            this.exprList(matchAgainstExpr.getColumns(), matchAgainstExpr);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.AGAINST)) {
            this.lexer.nextToken();
        }
        this.accept(Token.LPAREN);
        final SQLExpr against = this.primary();
        matchAgainstExpr.setAgainst(against);
        if (this.lexer.token() == Token.IN) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.NATURAL)) {
                this.lexer.nextToken();
                this.acceptIdentifier("LANGUAGE");
                this.acceptIdentifier("MODE");
                if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("QUERY");
                    this.acceptIdentifier("EXPANSION");
                    matchAgainstExpr.setSearchModifier(SQLMatchAgainstExpr.SearchModifier.IN_NATURAL_LANGUAGE_MODE_WITH_QUERY_EXPANSION);
                }
                else {
                    matchAgainstExpr.setSearchModifier(SQLMatchAgainstExpr.SearchModifier.IN_NATURAL_LANGUAGE_MODE);
                }
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.BOOLEAN)) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                this.lexer.nextToken();
                this.acceptIdentifier("MODE");
                matchAgainstExpr.setSearchModifier(SQLMatchAgainstExpr.SearchModifier.IN_BOOLEAN_MODE);
            }
        }
        else if (this.lexer.token() == Token.WITH) {
            this.lexer.nextToken();
            this.acceptIdentifier("QUERY");
            this.acceptIdentifier("EXPANSION");
            matchAgainstExpr.setSearchModifier(SQLMatchAgainstExpr.SearchModifier.WITH_QUERY_EXPANSION);
        }
        this.accept(Token.RPAREN);
        return this.primaryRest(matchAgainstExpr);
    }
    
    protected SQLExpr methodRest(final SQLExpr expr, final boolean acceptLPAREN) {
        if (acceptLPAREN) {
            this.accept(Token.LPAREN);
        }
        boolean distinct = false;
        if (this.lexer.token == Token.DISTINCT) {
            this.lexer.nextToken();
            distinct = true;
            if (this.lexer.token == Token.RPAREN || this.lexer.token == Token.COMMA) {
                throw new ParserException(this.lexer.info());
            }
        }
        String methodName = null;
        String aggMethodName = null;
        SQLExpr owner = null;
        String trimOption = null;
        long hash_lower = 0L;
        if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
            methodName = identifierExpr.getName();
            hash_lower = identifierExpr.nameHashCode64();
            if (this.allowIdentifierMethod) {
                if (hash_lower == FnvHash.Constants.TRIM) {
                    if (this.lexer.identifierEquals(FnvHash.Constants.LEADING)) {
                        trimOption = this.lexer.stringVal();
                        this.lexer.nextToken();
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.BOTH)) {
                        trimOption = this.lexer.stringVal();
                        this.lexer.nextToken();
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.TRAILING)) {
                        trimOption = this.lexer.stringVal();
                        this.lexer.nextToken();
                    }
                }
                else {
                    if (hash_lower == FnvHash.Constants.MATCH && (DbType.mysql == this.dbType || DbType.ads == this.dbType)) {
                        return this.parseMatch();
                    }
                    if (hash_lower == FnvHash.Constants.EXTRACT && DbType.mysql == this.dbType) {
                        return this.parseExtract();
                    }
                    if (hash_lower == FnvHash.Constants.POSITION && DbType.mysql == this.dbType) {
                        return this.parsePosition();
                    }
                    if (hash_lower == FnvHash.Constants.TRY_CAST) {
                        final SQLCastExpr cast = new SQLCastExpr();
                        cast.setTry(true);
                        cast.setExpr(this.expr());
                        this.accept(Token.AS);
                        cast.setDataType(this.parseDataType(false));
                        this.accept(Token.RPAREN);
                        return cast;
                    }
                    if (hash_lower == FnvHash.Constants.INT4 && DbType.postgresql == this.dbType) {
                        final PGTypeCastExpr castExpr = new PGTypeCastExpr();
                        castExpr.setExpr(this.expr());
                        castExpr.setDataType(new SQLDataTypeImpl(methodName));
                        this.accept(Token.RPAREN);
                        return castExpr;
                    }
                    if (hash_lower == FnvHash.Constants.VARBIT && DbType.postgresql == this.dbType) {
                        final PGTypeCastExpr castExpr = new PGTypeCastExpr();
                        final SQLExpr len = this.primary();
                        castExpr.setDataType(new SQLDataTypeImpl(methodName, len));
                        this.accept(Token.RPAREN);
                        castExpr.setExpr(this.expr());
                        return castExpr;
                    }
                    if (hash_lower == FnvHash.Constants.CONVERT && DbType.mysql == this.dbType) {
                        final SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr(methodName, hash_lower);
                        final SQLExpr arg0 = this.expr();
                        final Object exprUsing = arg0.getAttributes().get("USING");
                        if (exprUsing instanceof String) {
                            final String charset = (String)exprUsing;
                            methodInvokeExpr.setUsing(new SQLIdentifierExpr(charset));
                            arg0.getAttributes().remove("USING");
                        }
                        methodInvokeExpr.addArgument(arg0);
                        if (this.lexer.token == Token.COMMA) {
                            this.lexer.nextToken();
                            final SQLDataType dataType = this.parseDataType();
                            final SQLDataTypeRefExpr dataTypeRefExpr = new SQLDataTypeRefExpr(dataType);
                            methodInvokeExpr.addArgument(dataTypeRefExpr);
                        }
                        if (this.lexer.token == Token.USING || this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                            this.lexer.nextToken();
                            SQLExpr using;
                            if (this.lexer.token == Token.STAR) {
                                this.lexer.nextToken();
                                using = new SQLAllColumnExpr();
                            }
                            else if (this.lexer.token == Token.BINARY) {
                                using = new SQLIdentifierExpr(this.lexer.stringVal());
                                this.lexer.nextToken();
                            }
                            else {
                                using = this.primary();
                            }
                            methodInvokeExpr.setUsing(using);
                        }
                        this.accept(Token.RPAREN);
                        return this.primaryRest(methodInvokeExpr);
                    }
                }
            }
            if (distinct) {
                aggMethodName = methodName;
            }
            else {
                aggMethodName = this.getAggregateFunction(hash_lower);
            }
        }
        else if (expr instanceof SQLPropertyExpr) {
            methodName = ((SQLPropertyExpr)expr).getSimpleName();
            aggMethodName = SQLUtils.normalize(methodName);
            hash_lower = FnvHash.fnv1a_64_lower(aggMethodName);
            aggMethodName = this.getAggregateFunction(hash_lower);
            owner = ((SQLPropertyExpr)expr).getOwner();
        }
        else if (expr instanceof SQLDefaultExpr) {
            methodName = "DEFAULT";
        }
        else if (expr instanceof SQLCharExpr) {
            methodName = ((SQLCharExpr)expr).getText();
            if (this.isAggregateFunction(methodName)) {
                aggMethodName = methodName;
            }
        }
        else if (expr instanceof SQLDbLinkExpr) {
            final SQLDbLinkExpr dbLinkExpr = (SQLDbLinkExpr)expr;
            methodName = dbLinkExpr.toString();
        }
        if (aggMethodName != null) {
            final SQLAggregateExpr aggregateExpr = this.parseAggregateExpr(methodName);
            if (distinct) {
                aggregateExpr.setOption(SQLAggregateOption.DISTINCT);
            }
            if (this.lexer.token == Token.COLONCOLON) {
                return this.primaryRest(aggregateExpr);
            }
            return aggregateExpr;
        }
        else {
            final SQLMethodInvokeExpr methodInvokeExpr = new SQLMethodInvokeExpr(methodName, hash_lower);
            if (owner != null) {
                methodInvokeExpr.setOwner(owner);
            }
            if (trimOption != null) {
                methodInvokeExpr.setTrimOption(trimOption);
            }
            final Token token = this.lexer.token;
            if (token != Token.RPAREN && token != Token.FROM) {
                this.exprList(methodInvokeExpr.getArguments(), methodInvokeExpr);
            }
            if (hash_lower == FnvHash.Constants.EXIST && methodInvokeExpr.getArguments().size() == 1 && methodInvokeExpr.getArguments().get(0) instanceof SQLQueryExpr) {
                throw new ParserException("exists syntax error.");
            }
            if (this.lexer.token == Token.FROM) {
                this.lexer.nextToken();
                final SQLExpr from = this.expr();
                methodInvokeExpr.setFrom(from);
                if (this.lexer.token == Token.FOR) {
                    this.lexer.nextToken();
                    final SQLExpr forExpr = this.expr();
                    methodInvokeExpr.setFor(forExpr);
                }
            }
            if (this.lexer.token == Token.USING || this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                this.lexer.nextToken();
                SQLExpr using2;
                if (this.lexer.token == Token.STAR) {
                    this.lexer.nextToken();
                    using2 = new SQLAllColumnExpr();
                }
                else if (this.lexer.token == Token.BINARY) {
                    using2 = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
                else {
                    using2 = this.primary();
                }
                methodInvokeExpr.setUsing(using2);
            }
            if (hash_lower == FnvHash.Constants.WEIGHT_STRING) {
                if (this.lexer.token == Token.AS) {
                    this.lexer.nextToken();
                    final SQLDataType as = this.parseDataType();
                    methodInvokeExpr.putAttribute("as", as);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.LEVEL)) {
                    this.lexer.nextToken();
                    final List<SQLSelectOrderByItem> levels = new ArrayList<SQLSelectOrderByItem>();
                    while (true) {
                        final SQLSelectOrderByItem level = this.parseSelectOrderByItem();
                        levels.add(level);
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    methodInvokeExpr.putAttribute("levels", levels);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.REVERSE)) {
                    this.lexer.nextToken();
                    methodInvokeExpr.putAttribute("reverse", true);
                }
            }
            SQLAggregateExpr aggregateExpr2 = null;
            if (this.lexer.token == Token.ORDER) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                aggregateExpr2 = new SQLAggregateExpr(methodName);
                aggregateExpr2.getArguments().addAll(methodInvokeExpr.getArguments());
                final SQLOrderBy orderBy = new SQLOrderBy();
                this.orderBy(orderBy.getItems(), orderBy);
                aggregateExpr2.setOrderBy(orderBy);
            }
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals(FnvHash.Constants.USING) && this.dbType == DbType.odps) {
                this.lexer.nextToken();
                final SQLExpr using3 = this.primary();
                methodInvokeExpr.setUsing(using3);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.FILTER)) {
                if (aggregateExpr2 == null) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    final Token nextToken = this.lexer.token;
                    this.lexer.reset(mark);
                    if (nextToken == Token.LPAREN) {
                        aggregateExpr2 = new SQLAggregateExpr(methodName);
                        aggregateExpr2.getArguments().addAll(methodInvokeExpr.getArguments());
                        this.filter(aggregateExpr2);
                    }
                }
                else {
                    this.filter(aggregateExpr2);
                }
            }
            if (this.lexer.token == Token.OVER) {
                if (aggregateExpr2 == null) {
                    aggregateExpr2 = new SQLAggregateExpr(methodName);
                    aggregateExpr2.getArguments().addAll(methodInvokeExpr.getArguments());
                }
                this.over(aggregateExpr2);
            }
            if (aggregateExpr2 != null) {
                return this.primaryRest(aggregateExpr2);
            }
            if (this.lexer.token == Token.LPAREN) {
                return methodInvokeExpr;
            }
            return this.primaryRest(methodInvokeExpr);
        }
    }
    
    protected SQLExpr dotRest(SQLExpr expr) {
        if (this.lexer.token == Token.STAR) {
            this.lexer.nextToken();
            expr = new SQLPropertyExpr(expr, "*");
        }
        else {
            long hash_lower = 0L;
            String name;
            if (this.lexer.token == Token.IDENTIFIER) {
                name = this.lexer.stringVal();
                hash_lower = this.lexer.hash_lower;
                this.lexer.nextToken();
                if (hash_lower == FnvHash.Constants.NEXTVAL) {
                    expr = new SQLSequenceExpr((SQLName)expr, SQLSequenceExpr.Function.NextVal);
                    return this.primaryRest(expr);
                }
                if (hash_lower == FnvHash.Constants.CURRVAL) {
                    expr = new SQLSequenceExpr((SQLName)expr, SQLSequenceExpr.Function.CurrVal);
                    return this.primaryRest(expr);
                }
                if (hash_lower == FnvHash.Constants.PREVVAL) {
                    expr = new SQLSequenceExpr((SQLName)expr, SQLSequenceExpr.Function.PrevVal);
                    return this.primaryRest(expr);
                }
            }
            else if (this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.LITERAL_ALIAS) {
                name = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            else if (this.lexer.getKeywords().containsValue(this.lexer.token)) {
                name = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            else if (this.lexer.token == Token.VARIANT && this.lexer.stringVal().startsWith("$")) {
                name = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            else if ((this.lexer.token == Token.LITERAL_INT || this.lexer.token == Token.LITERAL_FLOAT) && (this.dbType == DbType.hive || this.dbType == DbType.odps)) {
                name = this.lexer.numberString();
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token != Token.DOT || this.dbType != DbType.odps || !expr.toString().equals("odps.sql.mapper")) {
                    throw new ParserException("error : " + this.lexer.info());
                }
                this.lexer.nextToken();
                name = this.lexer.stringVal();
                this.lexer.nextToken();
            }
            if (this.lexer.token == Token.LPAREN) {
                final boolean aggregate = hash_lower == FnvHash.Constants.WM_CONCAT && expr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)expr).nameHashCode64() == FnvHash.Constants.WMSYS;
                expr = this.methodRest(expr, name, aggregate);
            }
            else {
                if (name.charAt(0) == '`') {
                    if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                        name = name.substring(1, name.length() - 1);
                    }
                    hash_lower = FnvHash.hashCode64(name);
                }
                expr = new SQLPropertyExpr(expr, name, hash_lower);
            }
        }
        expr = this.primaryRest(expr);
        return expr;
    }
    
    private SQLExpr methodRest(SQLExpr expr, final String name, final boolean aggregate) {
        this.lexer.nextToken();
        if (this.lexer.token == Token.DISTINCT) {
            this.lexer.nextToken();
            final SQLAggregateExpr aggregateExpr = new SQLAggregateExpr(name, SQLAggregateOption.DISTINCT);
            aggregateExpr.setOwner(expr);
            if (this.lexer.token == Token.RPAREN) {
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token == Token.PLUS) {
                    aggregateExpr.getArguments().add(new SQLIdentifierExpr("+"));
                    this.lexer.nextToken();
                }
                else {
                    this.exprList(aggregateExpr.getArguments(), aggregateExpr);
                }
                this.accept(Token.RPAREN);
            }
            expr = aggregateExpr;
        }
        else if (aggregate) {
            final SQLAggregateExpr methodInvokeExpr = new SQLAggregateExpr(name);
            methodInvokeExpr.setMethodName(expr.toString() + "." + name);
            if (this.lexer.token == Token.RPAREN) {
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token == Token.PLUS) {
                    methodInvokeExpr.addArgument(new SQLIdentifierExpr("+"));
                    this.lexer.nextToken();
                }
                else {
                    this.exprList(methodInvokeExpr.getArguments(), methodInvokeExpr);
                }
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token == Token.OVER) {
                this.over(methodInvokeExpr);
            }
            expr = methodInvokeExpr;
        }
        else {
            SQLMethodInvokeExpr methodInvokeExpr2 = new SQLMethodInvokeExpr(name);
            methodInvokeExpr2.setOwner(expr);
            if (this.lexer.token == Token.RPAREN) {
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token == Token.PLUS) {
                    methodInvokeExpr2.addArgument(new SQLIdentifierExpr("+"));
                    this.lexer.nextToken();
                }
                else {
                    this.exprList(methodInvokeExpr2.getArguments(), methodInvokeExpr2);
                }
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token == Token.OVER) {
                final SQLAggregateExpr aggregateExpr2 = new SQLAggregateExpr(methodInvokeExpr2.getMethodName());
                aggregateExpr2.setOwner(methodInvokeExpr2.getOwner());
                aggregateExpr2.getArguments().addAll(methodInvokeExpr2.getArguments());
                this.over(aggregateExpr2);
                methodInvokeExpr2 = aggregateExpr2;
            }
            expr = methodInvokeExpr2;
        }
        return expr;
    }
    
    public final SQLExpr groupComparisionRest(final SQLExpr expr) {
        return expr;
    }
    
    public final void names(final Collection<SQLName> exprCol) {
        this.names(exprCol, null);
    }
    
    public final void names(final Collection<SQLName> exprCol, final SQLObject parent) {
        if (this.lexer.token == Token.RBRACE) {
            return;
        }
        if (this.lexer.token == Token.EOF) {
            return;
        }
        SQLName name = this.name();
        name.setParent(parent);
        exprCol.add(name);
        while (this.lexer.token == Token.COMMA) {
            this.lexer.nextToken();
            if (parent instanceof SQLLateralViewTableSource && this.lexer.token == Token.NULL) {
                name = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else {
                name = this.name();
            }
            name.setParent(parent);
            exprCol.add(name);
        }
    }
    
    @Deprecated
    public final void exprList(final Collection<SQLExpr> exprCol) {
        this.exprList(exprCol, null);
    }
    
    public final void exprList(final Collection<SQLExpr> exprCol, final SQLObject parent) {
        if (this.lexer.token == Token.RPAREN || this.lexer.token == Token.RBRACKET || this.lexer.token == Token.SEMI) {
            return;
        }
        if (this.lexer.token == Token.EOF) {
            return;
        }
        while (true) {
            SQLExpr expr;
            if (this.lexer.token == Token.ROW && parent instanceof SQLDataType) {
                final SQLDataType dataType = this.parseDataType();
                expr = new SQLDataTypeRefExpr(dataType);
            }
            else {
                expr = this.expr();
            }
            if (expr != null) {
                expr.setParent(parent);
                exprCol.add(expr);
            }
            if (this.lexer.token != Token.COMMA) {
                break;
            }
            if (this.dbType == DbType.mysql) {
                this.lexer.nextTokenValue();
            }
            else {
                this.lexer.nextToken();
            }
        }
    }
    
    public SQLIdentifierExpr identifier() {
        final SQLName name = this.name();
        if (name instanceof SQLIdentifierExpr) {
            return (SQLIdentifierExpr)name;
        }
        throw new ParserException("identifier excepted, " + this.lexer.info());
    }
    
    public SQLName name() {
        long hash = 0L;
        String identName;
        if (this.lexer.token == Token.LITERAL_ALIAS) {
            identName = this.lexer.stringVal();
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.IDENTIFIER) {
            identName = this.lexer.stringVal();
            final char c0 = identName.charAt(0);
            if (c0 != '[') {
                hash = this.lexer.hash_lower();
            }
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.LITERAL_CHARS) {
            identName = '\'' + this.lexer.stringVal() + '\'';
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.VARIANT) {
            identName = this.lexer.stringVal();
            this.lexer.nextToken();
        }
        else {
            switch (this.lexer.token) {
                case CAST:
                case INTERVAL:
                case INDEX:
                case KEY:
                case IS:
                case DEFAULT:
                case SCHEMA:
                case END:
                case COMMENT:
                case COMPUTE:
                case ENABLE:
                case DISABLE:
                case INITIALLY:
                case SEQUENCE:
                case USER:
                case EXPLAIN:
                case WITH:
                case GRANT:
                case PCTFREE:
                case INITRANS:
                case MAXTRANS:
                case SEGMENT:
                case CREATION:
                case IMMEDIATE:
                case DEFERRED:
                case STORAGE:
                case NEXT:
                case MINEXTENTS:
                case MAXEXTENTS:
                case MAXSIZE:
                case PCTINCREASE:
                case FLASH_CACHE:
                case CELL_FLASH_CACHE:
                case NONE:
                case LOB:
                case STORE:
                case ROW:
                case CHUNK:
                case CACHE:
                case NOCACHE:
                case LOGGING:
                case NOCOMPRESS:
                case KEEP_DUPLICATES:
                case EXCEPTIONS:
                case PURGE:
                case FULL:
                case BINARY:
                case ERRORS:
                case BEGIN:
                case MODE:
                case VIEW:
                case ESCAPE:
                case OVER:
                case ORDER:
                case TYPE:
                case OPEN:
                case REPEAT:
                case TRUNCATE:
                case FUNCTION:
                case DESC:
                case DO:
                case GROUP:
                case PARTITION:
                case LEAVE:
                case CLOSE:
                case EXCEPT:
                case INTERSECT:
                case MERGE:
                case MINUS:
                case UNTIL:
                case SHOW:
                case INOUT:
                case INNER:
                case LOCK:
                case ANY:
                case SOME:
                case ALL:
                case SET:
                case FOR:
                case LOOP:
                case UNIQUE:
                case REFERENCES:
                case REVOKE:
                case NOWAIT:
                case COMMIT:
                case ANALYZE:
                case OPTIMIZE:
                case JOIN:
                case TABLESPACE:
                case GOTO:
                case FULLTEXT: {
                    identName = this.lexer.stringVal();
                    this.lexer.nextToken();
                    break;
                }
                case NULL:
                case CHECK:
                case PRIMARY:
                case AS:
                case LIMIT:
                case TO:
                case CONSTRAINT:
                case TABLE:
                case OUT:
                case USE:
                case EXISTS:
                case LEFT:
                case RIGHT:
                case DATABASE:
                case VALUES:
                case TRIGGER:
                case DELETE:
                case BY:
                case UPDATE:
                case LIKE:
                case UNION:
                case CREATE:
                case DISTRIBUTE:
                case PROCEDURE:
                case DECLARE:
                case RLIKE:
                case FOREIGN:
                case FETCH:
                case ASC:
                case CURSOR:
                case ALTER:
                case IN:
                case PARTITIONED: {
                    if (this.dbType == DbType.odps) {
                        identName = this.lexer.stringVal();
                        this.lexer.nextToken();
                        break;
                    }
                    throw new ParserException("illegal name, " + this.lexer.info());
                }
                default: {
                    throw new ParserException("illegal name, " + this.lexer.info());
                }
            }
        }
        if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
            identName = SQLUtils.forcedNormalize(identName, this.dbType);
        }
        final SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(identName, hash);
        if (this.lexer.keepSourceLocation) {
            this.lexer.computeRowAndColumn();
            identifierExpr.setSourceLine(this.lexer.posLine);
            identifierExpr.setSourceColumn(this.lexer.posColumn);
        }
        SQLName name = identifierExpr;
        name = this.nameRest(name);
        return name;
    }
    
    public SQLName nameRest(SQLName name) {
        if (this.lexer.token == Token.DOT) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.KEY) {
                name = new SQLPropertyExpr(name, "KEY");
                this.lexer.nextToken();
                return name;
            }
            if (this.lexer.token != Token.LITERAL_ALIAS && this.lexer.token != Token.IDENTIFIER && !this.lexer.getKeywords().containsValue(this.lexer.token)) {
                throw new ParserException("error, " + this.lexer.info());
            }
            String propertyName = this.lexer.stringVal();
            if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                propertyName = SQLUtils.forcedNormalize(propertyName, this.dbType);
            }
            name = new SQLPropertyExpr(name, propertyName);
            this.lexer.nextToken();
            name = this.nameRest(name);
        }
        return name;
    }
    
    public boolean isAggregateFunction(final String word) {
        final long hash_lower = FnvHash.fnv1a_64_lower(word);
        return this.isAggregateFunction(hash_lower);
    }
    
    protected boolean isAggregateFunction(final long hash_lower) {
        return Arrays.binarySearch(this.aggregateFunctionHashCodes, hash_lower) >= 0;
    }
    
    protected String getAggregateFunction(final long hash_lower) {
        final int index = Arrays.binarySearch(this.aggregateFunctionHashCodes, hash_lower);
        if (index < 0) {
            return null;
        }
        return this.aggregateFunctions[index];
    }
    
    protected SQLAggregateExpr parseAggregateExpr(final String methodName) {
        SQLAggregateExpr aggregateExpr;
        if (this.lexer.token == Token.ALL) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token == Token.DOT) {
                aggregateExpr = new SQLAggregateExpr(methodName);
                this.lexer.reset(mark);
            }
            else {
                aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.ALL);
            }
        }
        else if (this.lexer.token == Token.DISTINCT) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.DISTINCT);
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.DEDUPLICATION)) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.DEDUPLICATION);
            this.lexer.nextToken();
        }
        else {
            aggregateExpr = new SQLAggregateExpr(methodName);
        }
        this.exprList(aggregateExpr.getArguments(), aggregateExpr);
        if (this.lexer.token != Token.RPAREN) {
            this.parseAggregateExprRest(aggregateExpr);
        }
        if (this.lexer.token == Token.AS) {
            this.lexer.nextToken();
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        if (this.lexer.identifierEquals(FnvHash.Constants.WITHIN)) {
            this.lexer.nextToken();
            this.accept(Token.GROUP);
            this.accept(Token.LPAREN);
            final SQLOrderBy orderBy = this.parseOrderBy();
            aggregateExpr.setWithinGroup(true);
            aggregateExpr.setOrderBy(orderBy);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FILTER)) {
            this.filter(aggregateExpr);
        }
        if (this.lexer.token == Token.OVER) {
            this.over(aggregateExpr);
        }
        return aggregateExpr;
    }
    
    protected void filter(final SQLAggregateExpr x) {
        final Lexer.SavePoint mark = this.lexer.mark();
        this.lexer.nextToken();
        switch (this.lexer.token) {
            case COMMA:
            case FROM: {
                this.lexer.reset(mark);
            }
            default: {
                this.accept(Token.LPAREN);
                this.accept(Token.WHERE);
                final SQLExpr filter = this.expr();
                this.accept(Token.RPAREN);
                x.setFilter(filter);
            }
        }
    }
    
    protected void over(final SQLAggregateExpr aggregateExpr) {
        this.lexer.nextToken();
        if (this.lexer.token != Token.LPAREN) {
            final SQLName overRef = this.name();
            aggregateExpr.setOverRef(overRef);
            return;
        }
        final SQLOver over = new SQLOver();
        this.over(over);
        aggregateExpr.setOver(over);
    }
    
    protected void over(final SQLOver over) {
        this.lexer.nextToken();
        if (this.lexer.token == Token.PARTITION || this.lexer.identifierEquals("PARTITION")) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprList(over.getPartitionBy(), over);
                this.accept(Token.RPAREN);
                if (over.getPartitionBy().size() == 1) {
                    switch (this.lexer.token) {
                        case PLUS:
                        case SUB:
                        case STAR:
                        case DIV:
                        case SLASH: {
                            final SQLExpr first = this.exprRest(over.getPartitionBy().get(0));
                            first.setParent(over);
                            over.getPartitionBy().set(0, first);
                            break;
                        }
                    }
                    if (this.lexer.token == Token.COMMA) {
                        this.lexer.nextToken();
                        this.exprList(over.getPartitionBy(), over);
                    }
                }
            }
            else if (this.lexer.token == Token.ALL) {
                final SQLName name = this.name();
                name.setParent(over);
                over.getPartitionBy().add(name);
                if (this.lexer.token == Token.COMMA) {
                    this.lexer.nextToken();
                    this.exprList(over.getPartitionBy(), over);
                }
            }
            else {
                this.exprList(over.getPartitionBy(), over);
            }
        }
        over.setOrderBy(this.parseOrderBy());
        over.setDistributeBy(this.parseDistributeBy());
        over.setSortBy(this.parseSortBy());
        over.setClusterBy(this.parseClusterBy());
        if (this.lexer.token == Token.OF) {
            this.lexer.nextToken();
            final SQLName of = this.name();
            over.setOf(of);
        }
        SQLOver.WindowingType windowingType = null;
        if (this.lexer.identifierEquals(FnvHash.Constants.ROWS) || this.lexer.token == Token.ROWS) {
            windowingType = SQLOver.WindowingType.ROWS;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.RANGE)) {
            windowingType = SQLOver.WindowingType.RANGE;
        }
        if (windowingType != null) {
            over.setWindowingType(windowingType);
            this.lexer.nextToken();
            if (this.lexer.token == Token.BETWEEN) {
                this.lexer.nextToken();
                if (this.lexer.token == Token.LITERAL_INT || this.lexer.token == Token.LITERAL_FLOAT || this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.CAST) {
                    final SQLExpr betweenBegin = this.additive();
                    over.setWindowingBetweenBegin(betweenBegin);
                }
                else if (this.lexer.token == Token.IDENTIFIER) {
                    final long hash = this.lexer.hash_lower();
                    if (hash != FnvHash.Constants.PRECEDING && hash != FnvHash.Constants.FOLLOWING && hash != FnvHash.Constants.CURRENT && hash != FnvHash.Constants.UNBOUNDED) {
                        final SQLExpr betweenBegin2 = this.primary();
                        over.setWindowingBetweenBegin(betweenBegin2);
                    }
                }
                else if (this.lexer.token == Token.INTERVAL) {
                    final SQLExpr betweenBegin = this.primary();
                    over.setWindowingBetweenBegin(betweenBegin);
                }
                final SQLOver.WindowingBound beginBound = this.parseWindowingBound();
                if (beginBound != null) {
                    over.setWindowingBetweenBeginBound(beginBound);
                }
                this.accept(Token.AND);
                if (this.lexer.token == Token.LITERAL_INT || this.lexer.token == Token.LITERAL_FLOAT || this.lexer.token == Token.LITERAL_CHARS) {
                    final SQLExpr betweenEnd = this.additive();
                    over.setWindowingBetweenEnd(betweenEnd);
                }
                else if (this.lexer.token == Token.INTERVAL) {
                    final SQLExpr betweenBegin3 = this.additive();
                    over.setWindowingBetweenEnd(betweenBegin3);
                }
                else if (this.lexer.token == Token.IDENTIFIER) {
                    final long hash2 = this.lexer.hash_lower();
                    if (hash2 != FnvHash.Constants.PRECEDING && hash2 != FnvHash.Constants.FOLLOWING && hash2 != FnvHash.Constants.CURRENT && hash2 != FnvHash.Constants.UNBOUNDED) {
                        final SQLExpr betweenBegin4 = this.additive();
                        over.setWindowingBetweenEnd(betweenBegin4);
                    }
                }
                final SQLOver.WindowingBound endBound = this.parseWindowingBound();
                if (endBound != null) {
                    over.setWindowingBetweenEndBound(endBound);
                }
            }
            else {
                if (this.lexer.token == Token.LITERAL_INT || this.lexer.token == Token.LITERAL_FLOAT || this.lexer.token == Token.LITERAL_CHARS || this.lexer.token == Token.INTERVAL) {
                    final SQLExpr betweenBegin = this.additive();
                    over.setWindowingBetweenBegin(betweenBegin);
                }
                else if (this.lexer.token == Token.IDENTIFIER) {
                    final long hash = this.lexer.hash_lower();
                    if (hash != FnvHash.Constants.PRECEDING && hash != FnvHash.Constants.FOLLOWING && hash != FnvHash.Constants.CURRENT && hash != FnvHash.Constants.UNBOUNDED) {
                        final SQLExpr betweenBegin2 = this.additive();
                        over.setWindowingBetweenBegin(betweenBegin2);
                    }
                }
                final SQLOver.WindowingBound beginBound = this.parseWindowingBound();
                if (beginBound != null) {
                    over.setWindowingBetweenBeginBound(beginBound);
                }
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXCLUDE)) {
            this.lexer.nextToken();
            this.acceptIdentifier("CURRENT");
            this.acceptIdentifier("ROW");
            over.setExcludeCurrentRow(true);
        }
        this.accept(Token.RPAREN);
    }
    
    protected SQLOver.WindowingBound parseWindowingBound() {
        if (this.lexer.identifierEquals(FnvHash.Constants.PRECEDING)) {
            this.lexer.nextToken();
            return SQLOver.WindowingBound.PRECEDING;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.FOLLOWING)) {
            this.lexer.nextToken();
            return SQLOver.WindowingBound.FOLLOWING;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CURRENT) || this.lexer.token == Token.CURRENT) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
                this.lexer.nextToken();
            }
            else {
                this.accept(Token.ROW);
            }
            return SQLOver.WindowingBound.CURRENT_ROW;
        }
        if (!this.lexer.identifierEquals(FnvHash.Constants.UNBOUNDED)) {
            return null;
        }
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.PRECEDING)) {
            this.lexer.nextToken();
            return SQLOver.WindowingBound.UNBOUNDED_PRECEDING;
        }
        this.acceptIdentifier("FOLLOWING");
        return SQLOver.WindowingBound.UNBOUNDED_FOLLOWING;
    }
    
    protected SQLAggregateExpr parseAggregateExprRest(final SQLAggregateExpr aggregateExpr) {
        return aggregateExpr;
    }
    
    public SQLOrderBy parseOrderBy() {
        if (this.lexer.token != Token.ORDER) {
            return null;
        }
        final SQLOrderBy orderBy = new SQLOrderBy();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.SIBLINGS)) {
            this.lexer.nextToken();
            orderBy.setSibings(true);
        }
        this.accept(Token.BY);
        this.orderBy(orderBy.getItems(), orderBy);
        if (this.lexer.token == Token.ORDER) {
            throw new ParserException(this.lexer.info());
        }
        return orderBy;
    }
    
    public SQLZOrderBy parseZOrderBy() {
        if (!this.lexer.identifierEquals("ZORDER")) {
            return null;
        }
        final SQLZOrderBy orderBy = new SQLZOrderBy();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.SIBLINGS)) {
            this.lexer.nextToken();
            orderBy.setSibings(true);
        }
        this.accept(Token.BY);
        this.orderBy(orderBy.getItems(), orderBy);
        if (this.lexer.token == Token.ORDER) {
            throw new ParserException(this.lexer.info());
        }
        return orderBy;
    }
    
    public SQLOrderBy parseDistributeBy() {
        if (this.lexer.token != Token.DISTRIBUTE && !this.lexer.identifierEquals("DISTRIBUTE")) {
            return null;
        }
        final SQLOrderBy orderBy = new SQLOrderBy();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.SIBLINGS)) {
            this.lexer.nextToken();
            orderBy.setSibings(true);
        }
        this.accept(Token.BY);
        this.orderBy(orderBy.getItems(), orderBy);
        if (this.lexer.token == Token.ORDER) {
            throw new ParserException(this.lexer.info());
        }
        return orderBy;
    }
    
    public SQLOrderBy parseSortBy() {
        if (this.lexer.token != Token.SORT && !this.lexer.identifierEquals(FnvHash.Constants.SORT)) {
            return null;
        }
        final SQLOrderBy orderBy = new SQLOrderBy();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.SIBLINGS)) {
            this.lexer.nextToken();
            orderBy.setSibings(true);
        }
        this.accept(Token.BY);
        this.orderBy(orderBy.getItems(), orderBy);
        if (this.lexer.token == Token.ORDER) {
            throw new ParserException(this.lexer.info());
        }
        return orderBy;
    }
    
    public SQLOrderBy parseClusterBy() {
        if (!this.lexer.identifierEquals(FnvHash.Constants.CLUSTER)) {
            return null;
        }
        final SQLOrderBy orderBy = new SQLOrderBy();
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.SIBLINGS)) {
            this.lexer.nextToken();
            orderBy.setSibings(true);
        }
        this.accept(Token.BY);
        this.orderBy(orderBy.getItems(), orderBy);
        if (this.lexer.token == Token.ORDER) {
            throw new ParserException(this.lexer.info());
        }
        return orderBy;
    }
    
    public void orderBy(final List<SQLSelectOrderByItem> items, final SQLObject parent) {
        SQLSelectOrderByItem item = this.parseSelectOrderByItem();
        item.setParent(parent);
        items.add(item);
        while (this.lexer.token == Token.COMMA) {
            this.lexer.nextToken();
            item = this.parseSelectOrderByItem();
            item.setParent(parent);
            items.add(item);
        }
    }
    
    public SQLSelectOrderByItem parseSelectOrderByItem() {
        final SQLSelectOrderByItem item = new SQLSelectOrderByItem();
        this.setAllowIdentifierMethod(false);
        try {
            SQLExpr expr;
            if (this.lexer.token() == Token.LITERAL_ALIAS) {
                expr = this.name();
                expr = this.primaryRest(expr);
                expr = this.exprRest(expr);
            }
            else if (this.lexer.token == Token.LPAREN) {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                int i = 0;
                while (true) {
                    expr = this.expr();
                    if (this.lexer.token == Token.ASC) {
                        this.lexer.nextToken();
                        item.setType(SQLOrderingSpecification.ASC);
                        if (this.lexer.token != Token.COMMA) {
                            this.accept(Token.RPAREN);
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    else if (this.lexer.token == Token.DESC) {
                        this.lexer.nextToken();
                        item.setType(SQLOrderingSpecification.DESC);
                        if (this.lexer.token != Token.COMMA) {
                            this.accept(Token.RPAREN);
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    else {
                        if (i > 0 && this.lexer.token == Token.RPAREN) {
                            this.lexer.nextToken();
                            break;
                        }
                        this.lexer.reset(mark);
                        expr = this.expr();
                        break;
                    }
                    ++i;
                }
            }
            else {
                expr = this.expr();
            }
            if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
                final SQLExpr owner = propertyExpr.getOwner();
                if (owner != null) {
                    String ownerStr = SQLUtils.toSQLString(owner);
                    if (ownerStr.length() > 1) {
                        ownerStr = StringUtils.removeNameQuotes(ownerStr);
                    }
                    propertyExpr.setOwner(ownerStr);
                }
                String name = propertyExpr.getName();
                if (name.length() > 1) {
                    name = StringUtils.removeNameQuotes(name);
                    propertyExpr.setName(name);
                }
                expr = propertyExpr;
            }
            item.setExpr(expr);
        }
        finally {
            this.setAllowIdentifierMethod(true);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
            this.lexer.nextToken();
            final String collate = this.lexer.stringVal();
            item.setCollate(collate);
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.ASC) {
            this.lexer.nextToken();
            item.setType(SQLOrderingSpecification.ASC);
        }
        else if (this.lexer.token == Token.DESC) {
            this.lexer.nextToken();
            item.setType(SQLOrderingSpecification.DESC);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.NULLS)) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.FIRST) || this.lexer.token == Token.FIRST) {
                this.lexer.nextToken();
                item.setNullsOrderType(SQLSelectOrderByItem.NullsOrderType.NullsFirst);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.LAST)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                item.setNullsOrderType(SQLSelectOrderByItem.NullsOrderType.NullsLast);
            }
        }
        if (this.lexer.token == Token.HINT) {
            item.setHint(this.parseHint());
        }
        return item;
    }
    
    public SQLUpdateSetItem parseUpdateSetItem() {
        final SQLUpdateSetItem item = new SQLUpdateSetItem();
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            final SQLListExpr list = new SQLListExpr();
            this.exprList(list.getItems(), list);
            this.accept(Token.RPAREN);
            item.setColumn(list);
        }
        else {
            final Token token = this.lexer.token();
            String identName;
            long hash;
            if (token == Token.IDENTIFIER) {
                identName = this.lexer.stringVal();
                hash = this.lexer.hash_lower();
            }
            else if (token == Token.LITERAL_CHARS) {
                identName = '\'' + this.lexer.stringVal() + '\'';
                hash = 0L;
            }
            else {
                identName = this.lexer.stringVal();
                hash = 0L;
            }
            this.lexer.nextTokenEq();
            SQLExpr expr = new SQLIdentifierExpr(identName, hash);
            while (this.lexer.token() == Token.DOT) {
                this.lexer.nextToken();
                final String propertyName = this.lexer.stringVal();
                this.lexer.nextTokenEq();
                expr = new SQLPropertyExpr(expr, propertyName);
            }
            item.setColumn(expr);
        }
        if (this.lexer.token == Token.LBRACKET && this.dbType == DbType.postgresql) {
            SQLExpr column = item.getColumn();
            column = this.primaryRest(column);
            item.setColumn(column);
        }
        if (this.lexer.token == Token.COLONEQ) {
            this.lexer.nextTokenValue();
        }
        else {
            if (this.lexer.token != Token.EQ) {
                throw new ParserException("syntax error, expect EQ, actual " + this.lexer.token + " " + this.lexer.info());
            }
            this.lexer.nextTokenValue();
        }
        item.setValue(this.expr());
        return item;
    }
    
    public final SQLExpr bitAnd() {
        SQLExpr expr = this.shift();
        if (this.lexer.token == Token.AMP) {
            expr = this.bitAndRest(expr);
        }
        return expr;
    }
    
    public final SQLExpr bitAndRest(SQLExpr expr) {
        while (this.lexer.token == Token.AMP) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.shift();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BitwiseAnd, rightExp, this.getDbType());
        }
        return expr;
    }
    
    public final SQLExpr bitOr() {
        SQLExpr expr = this.bitAnd();
        if (this.lexer.token == Token.BAR) {
            expr = this.bitOrRest(expr);
        }
        return expr;
    }
    
    public final SQLExpr bitOrRest(SQLExpr expr) {
        while (this.lexer.token == Token.BAR) {
            this.lexer.nextToken();
            SQLBinaryOperator op = SQLBinaryOperator.BitwiseOr;
            if (this.lexer.token == Token.BAR) {
                this.lexer.nextToken();
                op = SQLBinaryOperator.Concat;
            }
            final SQLExpr rightExp = this.bitAnd();
            expr = new SQLBinaryOpExpr(expr, op, rightExp, this.getDbType());
            expr = this.bitAndRest(expr);
        }
        return expr;
    }
    
    public final SQLExpr inRest(SQLExpr expr) {
        boolean global = false;
        if (this.lexer.token == Token.GLOBAL) {
            global = true;
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.IN) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextTokenLParen();
            if (this.lexer.token == Token.COMMA) {
                this.lexer.reset(mark);
                return expr;
            }
            final SQLInListExpr inListExpr = new SQLInListExpr(expr);
            final List<SQLExpr> targetList = inListExpr.getTargetList();
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextTokenValue();
                List<SQLCommentHint> hints = null;
                if (this.lexer.token == Token.HINT) {
                    hints = this.parseHints();
                }
                if (this.lexer.token == Token.WITH) {
                    final SQLSelect select = this.createSelectParser().select();
                    final SQLInSubQueryExpr queryExpr = new SQLInSubQueryExpr(select);
                    queryExpr.setExpr(expr);
                    this.accept(Token.RPAREN);
                    return queryExpr;
                }
                if (this.lexer.token != Token.RPAREN) {
                    while (true) {
                        SQLExpr item;
                        if (this.lexer.token == Token.LITERAL_INT) {
                            item = new SQLIntegerExpr(this.lexer.integerValue());
                            this.lexer.nextToken();
                            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                                item = this.primaryRest(item);
                                item = this.exprRest(item);
                            }
                        }
                        else {
                            item = this.expr();
                        }
                        item.setParent(inListExpr);
                        targetList.add(item);
                        if (item instanceof SQLCharExpr && this.lexer.token == Token.LITERAL_CHARS && this.dbType == DbType.odps) {
                            continue;
                        }
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextTokenValue();
                        if (this.lexer.token == Token.RPAREN && this.dbType == DbType.odps) {
                            break;
                        }
                    }
                    switch (this.lexer.token) {
                        case EXCEPT:
                        case MINUS:
                        case UNION: {
                            if (targetList.size() == 1 && targetList.get(0) instanceof SQLQueryExpr) {
                                final SQLQueryExpr queryExpr2 = targetList.get(0);
                                final SQLSelectQuery query = this.createSelectParser().queryRest(queryExpr2.getSubQuery().getQuery(), true);
                                if (query != queryExpr2.getSubQuery()) {
                                    queryExpr2.getSubQuery().setQuery(query);
                                }
                                if (hints != null && hints.size() > 0) {
                                    queryExpr2.getSubQuery().setHeadHint(hints.get(0));
                                }
                                break;
                            }
                            break;
                        }
                    }
                }
                final int line = this.lexer.line;
                this.accept(Token.RPAREN);
                if (line + 1 == this.lexer.line && this.lexer.hasComment() && this.lexer.getComments().get(0).startsWith("--")) {
                    inListExpr.addAfterComment(this.lexer.readAndResetComments());
                }
            }
            else {
                final SQLExpr itemExpr = this.primary();
                itemExpr.setParent(inListExpr);
                targetList.add(itemExpr);
            }
            this.parseQueryPlanHint(inListExpr);
            expr = inListExpr;
            if (targetList.size() == 1) {
                final SQLExpr targetExpr = targetList.get(0);
                if (targetExpr instanceof SQLQueryExpr) {
                    final SQLInSubQueryExpr inSubQueryExpr = new SQLInSubQueryExpr();
                    inSubQueryExpr.setExpr(inListExpr.getExpr());
                    inSubQueryExpr.setSubQuery(((SQLQueryExpr)targetExpr).getSubQuery());
                    inSubQueryExpr.setHint(inListExpr.getHint());
                    if (global) {
                        inSubQueryExpr.setGlobal(true);
                    }
                    expr = inSubQueryExpr;
                }
            }
        }
        else if (this.lexer.token == Token.CONTAINS) {
            this.lexer.nextTokenLParen();
            final SQLContainsExpr containsExpr = new SQLContainsExpr(expr);
            final List<SQLExpr> targetList2 = containsExpr.getTargetList();
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextTokenValue();
                if (this.lexer.token == Token.WITH) {
                    final SQLSelect select2 = this.createSelectParser().select();
                    final SQLInSubQueryExpr queryExpr3 = new SQLInSubQueryExpr(select2);
                    queryExpr3.setExpr(expr);
                    this.accept(Token.RPAREN);
                    return queryExpr3;
                }
                while (true) {
                    SQLExpr item2;
                    if (this.lexer.token == Token.LITERAL_INT) {
                        item2 = new SQLIntegerExpr(this.lexer.integerValue());
                        this.lexer.nextToken();
                        if (this.lexer.token != Token.COMMA && this.lexer.token != Token.RPAREN) {
                            item2 = this.primaryRest(item2);
                            item2 = this.exprRest(item2);
                        }
                    }
                    else {
                        item2 = this.expr();
                    }
                    item2.setParent(containsExpr);
                    targetList2.add(item2);
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextTokenValue();
                }
                this.accept(Token.RPAREN);
            }
            else {
                final SQLExpr itemExpr2 = this.primary();
                itemExpr2.setParent(containsExpr);
                targetList2.add(itemExpr2);
            }
            expr = containsExpr;
        }
        return expr;
    }
    
    public final SQLExpr additive() {
        SQLExpr expr = this.multiplicative();
        if (this.lexer.token == Token.PLUS || this.lexer.token == Token.BARBAR || this.lexer.token == Token.CONCAT || this.lexer.token == Token.SUB) {
            expr = this.additiveRest(expr);
        }
        return expr;
    }
    
    public SQLExpr additiveRest(SQLExpr expr) {
        final Token token = this.lexer.token;
        if (token == Token.PLUS) {
            this.lexer.nextToken();
            while (this.lexer.token == Token.HINT) {
                final SQLCommentHint hint = this.parseHint();
                if (expr instanceof SQLObjectImpl) {
                    ((SQLObjectImpl)expr).setHint(hint);
                }
            }
            final SQLExpr rightExp = this.multiplicative();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Add, rightExp, this.dbType);
            expr = this.additiveRest(expr);
        }
        else if ((token == Token.BARBAR || token == Token.CONCAT) && (this.isEnabled(SQLParserFeature.PipesAsConcat) || DbType.mysql != this.dbType)) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.multiplicative();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Concat, rightExp, this.dbType);
            expr = this.additiveRest(expr);
        }
        else if (token == Token.SUB) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.multiplicative();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Subtract, rightExp, this.dbType);
            expr = this.additiveRest(expr);
        }
        return expr;
    }
    
    public final SQLExpr shift() {
        SQLExpr expr = this.additive();
        if (this.lexer.token == Token.LTLT || this.lexer.token == Token.GTGT) {
            expr = this.shiftRest(expr);
        }
        return expr;
    }
    
    public SQLExpr shiftRest(SQLExpr expr) {
        if (this.lexer.token == Token.LTLT) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.additive();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.LeftShift, rightExp, this.dbType);
            expr = this.shiftRest(expr);
        }
        else if (this.lexer.token == Token.GTGT) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.additive();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.RightShift, rightExp, this.dbType);
            expr = this.shiftRest(expr);
        }
        return expr;
    }
    
    public SQLExpr and() {
        SQLExpr expr = this.relational();
        if (this.lexer.token == Token.AND || this.lexer.token == Token.AMPAMP) {
            expr = this.andRest(expr);
        }
        return expr;
    }
    
    public void parseQueryPlanHint(final SQLExpr expr) {
        if (this.lexer.token == Token.HINT && (expr instanceof SQLInListExpr || expr instanceof SQLBinaryOpExpr || expr instanceof SQLInSubQueryExpr || expr instanceof SQLExistsExpr || expr instanceof SQLNotExpr || expr instanceof SQLBetweenExpr)) {
            final String text = this.lexer.stringVal().trim();
            final Lexer hintLex = SQLParserUtils.createLexer(text, this.dbType);
            hintLex.nextToken();
            if (hintLex.token == Token.PLUS) {
                if (expr instanceof SQLBinaryOpExpr) {
                    final SQLBinaryOpExpr binaryOpExpr = (SQLBinaryOpExpr)expr;
                    final SQLBinaryOperator operator = binaryOpExpr.getOperator();
                    if (operator == SQLBinaryOperator.BooleanAnd || operator == SQLBinaryOperator.BooleanOr) {
                        if (binaryOpExpr.isParenthesized()) {
                            binaryOpExpr.setHint(new SQLCommentHint(text));
                        }
                        else {
                            final SQLExpr right = binaryOpExpr.getRight();
                            if (right instanceof SQLBinaryOpExpr || right instanceof SQLBetweenExpr) {
                                ((SQLExprImpl)right).setHint(new SQLCommentHint(text));
                            }
                        }
                    }
                    else {
                        binaryOpExpr.setHint(new SQLCommentHint(text));
                    }
                }
                else {
                    if (!(expr instanceof SQLObjectImpl)) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    ((SQLExprImpl)expr).setHint(new SQLCommentHint(text));
                }
                this.lexer.nextToken();
            }
        }
    }
    
    public SQLExpr andRest(SQLExpr expr) {
        while (true) {
            if (expr instanceof SQLBinaryOpExpr) {
                this.parseQueryPlanHint(expr);
            }
            final Token token = this.lexer.token;
            if (token == Token.AND) {
                if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                    expr.addAfterComment(this.lexer.readAndResetComments());
                }
                this.lexer.nextToken();
                final SQLExpr rightExp = this.relational();
                if (expr instanceof SQLBinaryOpExpr) {
                    this.parseQueryPlanHint(rightExp);
                }
                if (this.lexer.token == Token.AND && this.lexer.isEnabled(SQLParserFeature.EnableSQLBinaryOpExprGroup)) {
                    final SQLBinaryOpExprGroup group = new SQLBinaryOpExprGroup(SQLBinaryOperator.BooleanAnd, this.dbType);
                    group.add(expr);
                    group.add(rightExp);
                    if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                        rightExp.addAfterComment(this.lexer.readAndResetComments());
                    }
                    while (true) {
                        this.lexer.nextToken();
                        final SQLExpr more = this.relational();
                        if (more instanceof SQLBinaryOpExpr) {
                            this.parseQueryPlanHint(more);
                        }
                        group.add(more);
                        if (this.lexer.token != Token.AND) {
                            break;
                        }
                        if (!this.lexer.isKeepComments() || !this.lexer.hasComment()) {
                            continue;
                        }
                        more.addAfterComment(this.lexer.readAndResetComments());
                    }
                    expr = group;
                }
                else {
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BooleanAnd, rightExp, this.dbType);
                }
            }
            else {
                if (token != Token.AMPAMP) {
                    break;
                }
                if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                    expr.addAfterComment(this.lexer.readAndResetComments());
                }
                this.lexer.nextToken();
                final SQLExpr rightExp = this.relational();
                final SQLBinaryOperator operator = (DbType.postgresql == this.dbType) ? SQLBinaryOperator.PG_And : SQLBinaryOperator.BooleanAnd;
                expr = new SQLBinaryOpExpr(expr, operator, rightExp, this.dbType);
            }
        }
        return expr;
    }
    
    public SQLExpr xor() {
        SQLExpr expr = this.and();
        if (this.lexer.token == Token.XOR) {
            expr = this.xorRest(expr);
        }
        return expr;
    }
    
    public SQLExpr xorRest(SQLExpr expr) {
        while (this.lexer.token == Token.XOR) {
            this.lexer.nextToken();
            final SQLExpr rightExp = this.and();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BooleanXor, rightExp, this.dbType);
        }
        return expr;
    }
    
    public SQLExpr or() {
        SQLExpr expr = this.xor();
        if (this.lexer.token == Token.OR || this.lexer.token == Token.BARBAR) {
            expr = this.orRest(expr);
        }
        return expr;
    }
    
    public SQLExpr orRest(SQLExpr expr) {
        while (true) {
            if (this.lexer.token == Token.OR) {
                if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                    expr.addAfterComment(this.lexer.readAndResetComments());
                }
                this.lexer.nextToken();
                final SQLExpr rightExp = this.xor();
                if (this.lexer.token == Token.OR && this.lexer.isEnabled(SQLParserFeature.EnableSQLBinaryOpExprGroup)) {
                    final SQLBinaryOpExprGroup group = new SQLBinaryOpExprGroup(SQLBinaryOperator.BooleanOr, this.dbType);
                    group.add(expr);
                    group.add(rightExp);
                    if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                        rightExp.addAfterComment(this.lexer.readAndResetComments());
                    }
                    while (true) {
                        this.lexer.nextToken();
                        final SQLExpr more = this.xor();
                        group.add(more);
                        if (this.lexer.token != Token.OR) {
                            break;
                        }
                        if (!this.lexer.isKeepComments() || !this.lexer.hasComment()) {
                            continue;
                        }
                        more.addAfterComment(this.lexer.readAndResetComments());
                    }
                    expr = group;
                }
                else {
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.BooleanOr, rightExp, this.dbType);
                }
            }
            else {
                if (this.lexer.token != Token.BARBAR) {
                    break;
                }
                this.lexer.nextToken();
                final SQLExpr rightExp = this.xor();
                final SQLBinaryOperator op = (DbType.mysql == this.dbType && !this.isEnabled(SQLParserFeature.PipesAsConcat)) ? SQLBinaryOperator.BooleanOr : SQLBinaryOperator.Concat;
                expr = new SQLBinaryOpExpr(expr, op, rightExp, this.dbType);
            }
        }
        return expr;
    }
    
    public SQLExpr relational() {
        final SQLExpr expr = this.bitOr();
        return this.relationalRest(expr);
    }
    
    public SQLExpr relationalRest(SQLExpr expr) {
        final SQLExpr initExpr = expr;
        final Token token = this.lexer.token;
        Label_2223: {
            switch (token) {
                case EQ: {
                    this.lexer.nextToken();
                    SQLExpr rightExp;
                    try {
                        rightExp = this.bitOr();
                    }
                    catch (EOFParserException e) {
                        throw new ParserException("EOF, " + expr + "=", e);
                    }
                    if (this.lexer.token == Token.COLONEQ) {
                        this.lexer.nextToken();
                        final SQLExpr colonExpr = this.expr();
                        rightExp = new SQLBinaryOpExpr(rightExp, SQLBinaryOperator.Assignment, colonExpr, this.dbType);
                    }
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Equality, rightExp, this.dbType);
                    break;
                }
                case IS: {
                    this.lexer.nextTokenNotOrNull();
                    SQLBinaryOperator op;
                    if (this.lexer.token == Token.NOT) {
                        op = SQLBinaryOperator.IsNot;
                        this.lexer.nextTokenNotOrNull();
                    }
                    else {
                        op = SQLBinaryOperator.Is;
                    }
                    SQLExpr rightExp;
                    if (this.lexer.identifierEquals(FnvHash.Constants.JSON)) {
                        this.lexer.nextToken();
                        String name = "JSON";
                        if (this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
                            this.lexer.nextToken();
                            name = "JSON VALUE";
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.OBJECT)) {
                            this.lexer.nextToken();
                            name = "JSON OBJECT";
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.ARRAY)) {
                            this.lexer.nextToken();
                            name = "JSON ARRAY";
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.SCALAR)) {
                            this.lexer.nextToken();
                            name = "JSON SCALAR";
                        }
                        rightExp = new SQLIdentifierExpr(name);
                    }
                    else if (this.lexer.token == Token.DISTINCT) {
                        this.lexer.nextToken();
                        this.accept(Token.FROM);
                        if (op == SQLBinaryOperator.Is) {
                            op = SQLBinaryOperator.IsDistinctFrom;
                        }
                        else {
                            op = SQLBinaryOperator.IsNotDistinctFrom;
                        }
                        rightExp = this.bitOr();
                    }
                    else {
                        rightExp = this.primary();
                    }
                    expr = new SQLBinaryOpExpr(expr, op, rightExp, this.dbType);
                    break;
                }
                case EQGT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.expr();
                    final String argumentName = ((SQLIdentifierExpr)expr).getName();
                    expr = new OracleArgumentExpr(argumentName, rightExp);
                    break;
                }
                case BANGEQ:
                case CARETEQ: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotEqual, rightExp, this.dbType);
                    break;
                }
                case COLONEQ: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.expr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Assignment, rightExp, this.dbType);
                    break;
                }
                case LT: {
                    SQLBinaryOperator op = SQLBinaryOperator.LessThan;
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.EQ) {
                        this.lexer.nextToken();
                        op = SQLBinaryOperator.LessThanOrEqual;
                    }
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, op, rightExp, this.getDbType());
                    break;
                }
                case LTEQ: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.LessThanOrEqual, rightExp, this.getDbType());
                    break;
                }
                case LTEQGT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.LessThanOrEqualOrGreaterThan, rightExp, this.getDbType());
                    break;
                }
                case GT: {
                    SQLBinaryOperator op = SQLBinaryOperator.GreaterThan;
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.EQ) {
                        this.lexer.nextToken();
                        op = SQLBinaryOperator.GreaterThanOrEqual;
                    }
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, op, rightExp, this.getDbType());
                    break;
                }
                case GTEQ: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.GreaterThanOrEqual, rightExp, this.getDbType());
                    break;
                }
                case BANGLT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotLessThan, rightExp, this.getDbType());
                    break;
                }
                case BANGGT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotGreaterThan, rightExp, this.getDbType());
                    break;
                }
                case LTGT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.LessThanOrGreater, rightExp, this.getDbType());
                    break;
                }
                case LIKE: {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextTokenValue();
                    if (this.lexer.token == Token.COMMA) {
                        this.lexer.reset(mark);
                        return expr;
                    }
                    SQLExpr rightExp = this.bitOr();
                    if (rightExp.getClass() == SQLIdentifierExpr.class) {
                        final String name = ((SQLIdentifierExpr)rightExp).getName();
                        final int length = name.length();
                        if (length > 1 && name.charAt(0) == name.charAt(length - 1) && name.charAt(0) != '`') {
                            rightExp = new SQLCharExpr(name.substring(1, length - 1));
                        }
                    }
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Like, rightExp, this.getDbType());
                    if (this.lexer.token == Token.ESCAPE) {
                        this.lexer.nextToken();
                        rightExp = this.primary();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Escape, rightExp, this.getDbType());
                        break;
                    }
                    break;
                }
                case ILIKE: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.ILike, rightExp, this.getDbType());
                    break;
                }
                case MONKEYS_AT_AT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.AT_AT, rightExp, this.getDbType());
                    break;
                }
                case MONKEYS_AT_GT: {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Array_Contains, rightExp, this.getDbType());
                    break;
                }
                case LT_MONKEYS_AT: {
                    this.lexer.nextToken();
                    SQLExpr rightExp = this.bitOr();
                    rightExp = this.relationalRest(rightExp);
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Array_ContainedBy, rightExp, this.getDbType());
                    break;
                }
                case QUES: {
                    if (this.dbType == DbType.postgresql) {
                        this.lexer.nextToken();
                        SQLExpr rightExp = this.bitOr();
                        rightExp = this.relationalRest(rightExp);
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.JSONContains, rightExp, this.getDbType());
                        break;
                    }
                    break;
                }
                case NOT: {
                    this.lexer.nextToken();
                    expr = this.notRationalRest(expr);
                    break;
                }
                case BANG: {
                    if (this.dbType == DbType.odps) {
                        this.lexer.nextToken();
                        expr = this.notRationalRest(expr);
                        break;
                    }
                    break;
                }
                case BETWEEN: {
                    this.lexer.nextToken();
                    final SQLExpr beginExpr = this.relational();
                    this.accept(Token.AND);
                    final SQLExpr endExpr = this.relational();
                    expr = new SQLBetweenExpr(expr, beginExpr, endExpr);
                    this.parseQueryPlanHint(expr);
                    break;
                }
                case GLOBAL:
                case CONTAINS:
                case IN: {
                    expr = this.inRest(expr);
                    break;
                }
                case EQEQ: {
                    if (this.dbType == DbType.odps || this.dbType == DbType.hive) {
                        final Lexer.SavePoint mark2 = this.lexer.mark();
                        this.lexer.nextToken();
                        SQLExpr rightExp;
                        try {
                            if (this.lexer.token == Token.SEMI) {
                                this.lexer.reset(mark2);
                                break;
                            }
                            rightExp = this.bitOr();
                        }
                        catch (EOFParserException e2) {
                            throw new ParserException("EOF, " + expr + "=", e2);
                        }
                        if (this.lexer.token == Token.COLONEQ) {
                            this.lexer.nextToken();
                            final SQLExpr colonExpr2 = this.expr();
                            rightExp = new SQLBinaryOpExpr(rightExp, SQLBinaryOperator.Assignment, colonExpr2, this.dbType);
                        }
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Equality, rightExp, this.dbType);
                        break;
                    }
                    break;
                }
                case TILDE: {
                    if (DbType.postgresql == this.lexer.dbType) {
                        this.lexer.nextToken();
                        SQLExpr rightExp = this.relational();
                        rightExp = this.relationalRest(rightExp);
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.POSIX_Regular_Match, rightExp, this.getDbType());
                        break;
                    }
                    break;
                }
                case TILDE_STAR: {
                    if (DbType.postgresql == this.lexer.dbType) {
                        this.lexer.nextToken();
                        final SQLExpr rightExp = this.relational();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.POSIX_Regular_Match_Insensitive, rightExp, this.getDbType());
                        break;
                    }
                    return expr;
                }
                case BANG_TILDE: {
                    if (DbType.postgresql == this.lexer.dbType) {
                        this.lexer.nextToken();
                        final SQLExpr rightExp = this.relational();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.POSIX_Regular_Not_Match, rightExp, this.getDbType());
                        break;
                    }
                    return expr;
                }
                case BANG_TILDE_STAR: {
                    if (DbType.postgresql == this.lexer.dbType) {
                        this.lexer.nextToken();
                        final SQLExpr rightExp = this.relational();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.POSIX_Regular_Not_Match_POSIX_Regular_Match_Insensitive, rightExp, this.getDbType());
                        break;
                    }
                    return expr;
                }
                case TILDE_EQ: {
                    if (DbType.postgresql == this.lexer.dbType) {
                        this.lexer.nextToken();
                        final SQLExpr rightExp = this.relational();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.SAME_AS, rightExp, this.getDbType());
                        break;
                    }
                    return expr;
                }
                case RLIKE: {
                    final Lexer.SavePoint mark2 = this.lexer.mark();
                    this.lexer.nextToken();
                    switch (this.lexer.token) {
                        case COMMA: {
                            this.lexer.reset(mark2);
                            break Label_2223;
                        }
                        default: {
                            final SQLExpr rightExp = this.bitOr();
                            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.RLike, rightExp, this.getDbType());
                            break Label_2223;
                        }
                    }
                    break;
                }
                case IDENTIFIER: {
                    final long hash = this.lexer.hash_lower;
                    if (hash == FnvHash.Constants.SOUNDS) {
                        this.lexer.nextToken();
                        this.accept(Token.LIKE);
                        final SQLExpr rightExp = this.bitOr();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.SoudsLike, rightExp, this.getDbType());
                        break;
                    }
                    if (hash == FnvHash.Constants.REGEXP) {
                        this.lexer.nextToken();
                        final SQLExpr rightExp = this.bitOr();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.RegExp, rightExp, DbType.mysql);
                        break;
                    }
                    if (hash == FnvHash.Constants.SIMILAR && DbType.postgresql == this.lexer.dbType) {
                        this.lexer.nextToken();
                        this.accept(Token.TO);
                        final SQLExpr rightExp = this.bitOr();
                        expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.SIMILAR_TO, rightExp, this.getDbType());
                        break;
                    }
                    return expr;
                }
                default: {
                    return expr;
                }
            }
        }
        if (expr == initExpr) {
            return expr;
        }
        switch (this.lexer.token) {
            case EQ:
            case EQEQ:
            case LT:
            case LTEQ:
            case GT:
            case GTEQ:
            case LTGT:
            case IS:
            case NOT:
            case CONTAINS:
            case LIKE:
            case BETWEEN:
            case IN:
            case BANGEQ:
            case LTEQGT:
            case BANG_TILDE_STAR:
            case TILDE_EQ: {
                expr = this.relationalRest(expr);
                break;
            }
        }
        return expr;
    }
    
    public SQLExpr notRationalRest(SQLExpr expr) {
        switch (this.lexer.token) {
            case LIKE: {
                this.lexer.nextTokenValue();
                SQLExpr rightExp = this.bitOr();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotLike, rightExp, this.getDbType());
                if (this.lexer.token == Token.ESCAPE) {
                    this.lexer.nextToken();
                    rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Escape, rightExp, this.getDbType());
                    break;
                }
                break;
            }
            case IN: {
                this.lexer.nextToken();
                final SQLInListExpr inListExpr = new SQLInListExpr(expr, true);
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprList(inListExpr.getTargetList(), inListExpr);
                    expr = inListExpr;
                    switch (this.lexer.token) {
                        case MINUS:
                        case UNION: {
                            final List<SQLExpr> targetList = inListExpr.getTargetList();
                            if (targetList.size() == 1 && targetList.get(0) instanceof SQLQueryExpr) {
                                final SQLQueryExpr queryExpr = targetList.get(0);
                                final SQLSelectQuery query = this.createSelectParser().queryRest(queryExpr.getSubQuery().getQuery(), true);
                                if (query != queryExpr.getSubQuery()) {
                                    queryExpr.getSubQuery().setQuery(query);
                                }
                                break;
                            }
                            break;
                        }
                    }
                    this.accept(Token.RPAREN);
                }
                else {
                    final SQLExpr valueExpr = this.primary();
                    valueExpr.setParent(inListExpr);
                    inListExpr.getTargetList().add(valueExpr);
                    expr = inListExpr;
                }
                this.parseQueryPlanHint(inListExpr);
                if (inListExpr.getTargetList().size() == 1) {
                    final SQLExpr targetExpr = inListExpr.getTargetList().get(0);
                    if (targetExpr instanceof SQLQueryExpr) {
                        final SQLInSubQueryExpr inSubQueryExpr = new SQLInSubQueryExpr();
                        inSubQueryExpr.setNot(true);
                        inSubQueryExpr.setExpr(inListExpr.getExpr());
                        inSubQueryExpr.setSubQuery(((SQLQueryExpr)targetExpr).getSubQuery());
                        expr = inSubQueryExpr;
                    }
                    break;
                }
                break;
            }
            case CONTAINS: {
                this.lexer.nextToken();
                final SQLContainsExpr containsExpr = new SQLContainsExpr(expr, true);
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprList(containsExpr.getTargetList(), containsExpr);
                    expr = containsExpr;
                    switch (this.lexer.token) {
                        case MINUS:
                        case UNION: {
                            final List<SQLExpr> targetList2 = containsExpr.getTargetList();
                            if (targetList2.size() == 1 && targetList2.get(0) instanceof SQLQueryExpr) {
                                final SQLQueryExpr queryExpr2 = targetList2.get(0);
                                final SQLSelectQuery query2 = this.createSelectParser().queryRest(queryExpr2.getSubQuery().getQuery(), true);
                                if (query2 != queryExpr2.getSubQuery()) {
                                    queryExpr2.getSubQuery().setQuery(query2);
                                }
                                break;
                            }
                            break;
                        }
                    }
                    this.accept(Token.RPAREN);
                }
                else {
                    final SQLExpr valueExpr2 = this.primary();
                    valueExpr2.setParent(containsExpr);
                    containsExpr.getTargetList().add(valueExpr2);
                    expr = containsExpr;
                }
                if (containsExpr.getTargetList().size() == 1) {
                    final SQLExpr targetExpr2 = containsExpr.getTargetList().get(0);
                    if (targetExpr2 instanceof SQLQueryExpr) {
                        final SQLInSubQueryExpr inSubQueryExpr2 = new SQLInSubQueryExpr();
                        inSubQueryExpr2.setNot(true);
                        inSubQueryExpr2.setExpr(containsExpr.getExpr());
                        inSubQueryExpr2.setSubQuery(((SQLQueryExpr)targetExpr2).getSubQuery());
                        expr = inSubQueryExpr2;
                    }
                    break;
                }
                break;
            }
            case BETWEEN: {
                this.lexer.nextToken();
                final SQLExpr beginExpr = this.relational();
                this.accept(Token.AND);
                final SQLExpr endExpr = this.relational();
                expr = new SQLBetweenExpr(expr, true, beginExpr, endExpr);
                break;
            }
            case ILIKE: {
                this.lexer.nextToken();
                final SQLExpr rightExp = this.bitOr();
                return new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotILike, rightExp, this.getDbType());
            }
            case LPAREN: {
                expr = this.primary();
                break;
            }
            case RLIKE: {
                this.lexer.nextToken();
                final SQLExpr rightExp = this.bitOr();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotRLike, rightExp, this.getDbType());
                expr = this.relationalRest(expr);
                break;
            }
            case IDENTIFIER: {
                final long hash = this.lexer.hash_lower();
                if (hash == FnvHash.Constants.REGEXP) {
                    this.lexer.nextToken();
                    final SQLExpr rightExp = this.bitOr();
                    expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.NotRegExp, rightExp, this.getDbType());
                    expr = this.relationalRest(expr);
                    break;
                }
                break;
            }
            default: {
                throw new ParserException("TODO " + this.lexer.info());
            }
        }
        return expr;
    }
    
    public SQLDataType parseDataType() {
        return this.parseDataType(true);
    }
    
    public SQLDataType parseDataType(final boolean restrict) {
        final Token token = this.lexer.token;
        if (token == Token.DEFAULT || token == Token.NOT || token == Token.NULL) {
            return null;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.ARRAY)) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLArrayDataType array = new SQLArrayDataType(null, this.dbType);
                this.exprList(array.getArguments(), array);
                this.accept(Token.RPAREN);
                return array;
            }
            this.accept(Token.LT);
            final SQLDataType itemType = this.parseDataType();
            if (this.lexer.token == Token.GTGT) {
                this.lexer.token = Token.GT;
            }
            else {
                this.accept(Token.GT);
            }
            final SQLArrayDataType array2 = new SQLArrayDataType(itemType, this.dbType);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                this.exprList(array2.getArguments(), array2);
                this.accept(Token.RPAREN);
            }
            return array2;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MAP)) {
            this.lexer.nextToken();
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLDataType keyType = this.parseDataType();
                this.accept(Token.COMMA);
                final SQLDataType valueType = this.parseDataType();
                this.accept(Token.RPAREN);
                return new SQLMapDataType(keyType, valueType, this.dbType);
            }
            this.accept(Token.LT);
            final SQLDataType keyType = this.parseDataType();
            this.accept(Token.COMMA);
            final SQLDataType valueType = this.parseDataType();
            if (this.lexer.token == Token.GTGT) {
                this.lexer.token = Token.GT;
            }
            else {
                this.accept(Token.GT);
            }
            return new SQLMapDataType(keyType, valueType, this.dbType);
        }
        else {
            SQLName name = null;
            if (this.lexer.identifierEquals(FnvHash.Constants.STRUCT)) {
                this.lexer.nextToken();
                final SQLStructDataType struct = new SQLStructDataType(this.dbType);
                this.accept(Token.LT);
                while (true) {
                    switch (this.lexer.token) {
                        case TO:
                        case ORDER:
                        case GROUP:
                        case FROM: {
                            name = new SQLIdentifierExpr(this.lexer.stringVal());
                            this.lexer.nextToken();
                            break;
                        }
                        default: {
                            name = this.name();
                            break;
                        }
                    }
                    this.accept(Token.COLON);
                    final SQLDataType dataType = this.parseDataType();
                    final SQLStructDataType.Field field = struct.addField(name, dataType);
                    if (this.lexer.token == Token.COMMENT) {
                        this.lexer.nextToken();
                        final SQLCharExpr chars = (SQLCharExpr)this.primary();
                        field.setComment(chars.getText());
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                if (this.lexer.token == Token.GTGT) {
                    this.lexer.token = Token.GT;
                }
                else {
                    this.accept(Token.GT);
                }
                return struct;
            }
            if (this.lexer.token == Token.TABLE) {
                this.lexer.nextToken();
                final SQLTableDataType table = new SQLTableDataType();
                this.accept(Token.LPAREN);
                while (true) {
                    SQLColumnDefinition column;
                    if (this.lexer.token == Token.STAR) {
                        this.lexer.nextToken();
                        column = new SQLColumnDefinition();
                        column.setName("*");
                        this.accept(Token.ANY);
                    }
                    else {
                        column = this.parseColumn();
                    }
                    column.setParent(table);
                    table.getColumns().add(column);
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                return table;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.ROW) || this.lexer.token == Token.ROW) {
                this.lexer.nextToken();
                return this.parseSqlRowDataType();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.NESTED) && this.dbType == DbType.clickhouse) {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLStructDataType struct = new SQLStructDataType(this.dbType);
                while (true) {
                    switch (this.lexer.token) {
                        case TO:
                        case ORDER:
                        case GROUP:
                        case FROM: {
                            name = new SQLIdentifierExpr(this.lexer.stringVal());
                            this.lexer.nextToken();
                            break;
                        }
                        default: {
                            name = this.name();
                            break;
                        }
                    }
                    final SQLDataType dataType = this.parseDataType();
                    final SQLStructDataType.Field field = struct.addField(name, dataType);
                    if (this.lexer.token == Token.COMMENT) {
                        this.lexer.nextToken();
                        final SQLCharExpr chars = (SQLCharExpr)this.primary();
                        field.setComment(chars.getText());
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                return struct;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.UNIONTYPE)) {
                this.lexer.nextToken();
                this.accept(Token.LT);
                final SQLUnionDataType unionType = new SQLUnionDataType();
                while (true) {
                    final SQLDataType item = this.parseDataType();
                    unionType.add(item);
                    if (this.lexer.token != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.GT);
                return unionType;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.GENERATED) || this.lexer.identifierEquals(FnvHash.Constants.RENAME)) {
                return null;
            }
            final SQLName typeExpr = this.name();
            final long typeNameHashCode = typeExpr.nameHashCode64();
            String typeName = typeExpr.toString();
            if (typeNameHashCode == FnvHash.Constants.LONG && this.lexer.identifierEquals(FnvHash.Constants.BYTE) && DbType.mysql == this.dbType) {
                typeName = typeName + ' ' + this.lexer.stringVal();
                this.lexer.nextToken();
            }
            else if (typeNameHashCode == FnvHash.Constants.DOUBLE) {
                if (DbType.postgresql == this.dbType) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
                else if (DbType.mysql == this.dbType && this.lexer.identifierEquals(FnvHash.Constants.PRECISION)) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
            }
            if (typeNameHashCode == FnvHash.Constants.UNSIGNED) {
                if (this.lexer.token == Token.IDENTIFIER) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
            }
            else if (typeNameHashCode == FnvHash.Constants.SIGNED) {
                if (this.lexer.token == Token.IDENTIFIER) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
            }
            else if (this.isCharType(typeNameHashCode)) {
                SQLCharacterDataType charType = new SQLCharacterDataType(typeName);
                if (this.lexer.token == Token.LBRACKET) {
                    final SQLArrayDataType arrayDataType = new SQLArrayDataType(charType, this.dbType);
                    this.lexer.nextToken();
                    this.accept(Token.RBRACKET);
                    arrayDataType.putAttribute("ads.arrayDataType", Boolean.TRUE);
                    return arrayDataType;
                }
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    if (typeNameHashCode == FnvHash.Constants.ENUM) {
                        this.exprList(charType.getArguments(), charType);
                    }
                    else {
                        final SQLExpr arg = this.expr();
                        arg.setParent(charType);
                        charType.addArgument(arg);
                    }
                    this.accept(Token.RPAREN);
                }
                charType = (SQLCharacterDataType)this.parseCharTypeRest(charType);
                if (this.lexer.token == Token.HINT) {
                    final List<SQLCommentHint> hints = this.parseHints();
                    charType.setHints(hints);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.ARRAY)) {
                    return this.parseDataTypeRest(charType);
                }
                if (this.lexer.token == Token.LBRACKET) {
                    return this.parseDataTypeRest(charType);
                }
                return charType;
            }
            if (!"national".equalsIgnoreCase(typeName) || (!this.lexer.identifierEquals(FnvHash.Constants.CHAR) && !this.lexer.identifierEquals(FnvHash.Constants.VARCHAR))) {
                if ("character".equalsIgnoreCase(typeName) && "varying".equalsIgnoreCase(this.lexer.stringVal())) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
                if (this.lexer.token == Token.LT && this.dbType == DbType.odps) {
                    this.lexer.nextToken();
                    typeName += '<';
                    while (true) {
                        final SQLDataType itemType2 = this.parseDataType();
                        typeName += itemType2.toString();
                        if (this.lexer.token != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                        typeName += ", ";
                    }
                    this.accept(Token.GT);
                    typeName += '>';
                }
                SQLDataType dataType2 = new SQLDataTypeImpl(typeName);
                dataType2.setDbType(this.dbType);
                if (this.lexer.token == Token.LBRACKET) {
                    dataType2 = new SQLArrayDataType(dataType2, this.dbType);
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.LITERAL_INT) {
                        final SQLExpr arg = this.expr();
                        arg.setParent(dataType2);
                        dataType2.getArguments().add(arg);
                    }
                    this.accept(Token.RBRACKET);
                    dataType2.putAttribute("ads.arrayDataType", Boolean.TRUE);
                }
                return this.parseDataTypeRest(dataType2);
            }
            typeName = typeName + ' ' + this.lexer.stringVal();
            this.lexer.nextToken();
            SQLCharacterDataType charType = new SQLCharacterDataType(typeName);
            if (this.lexer.token == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLExpr arg = this.expr();
                arg.setParent(charType);
                charType.addArgument(arg);
                this.accept(Token.RPAREN);
            }
            charType = (SQLCharacterDataType)this.parseCharTypeRest(charType);
            if (this.lexer.token == Token.HINT) {
                final List<SQLCommentHint> hints = this.parseHints();
                charType.setHints(hints);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.ARRAY)) {
                return this.parseDataTypeRest(charType);
            }
            return charType;
        }
    }
    
    private SQLRowDataType parseSqlRowDataType() {
        final SQLRowDataType struct = new SQLRowDataType(this.dbType);
        this.accept(Token.LPAREN);
        while (true) {
            SQLDataType dataType = null;
            final Lexer.SavePoint mark = this.lexer.mark();
            SQLName name = null;
            switch (this.lexer.token) {
                case TO:
                case ORDER:
                case GROUP:
                case FROM: {
                    name = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    break;
                }
                case ROW: {
                    this.lexer.nextToken();
                    name = null;
                    dataType = this.parseSqlRowDataType();
                    break;
                }
                default: {
                    name = this.name();
                    break;
                }
            }
            if (this.lexer.token == Token.COMMA) {
                this.lexer.reset(mark);
                dataType = this.parseDataType();
                struct.addField(null, dataType);
                this.lexer.nextToken();
            }
            else {
                if (this.lexer.token != Token.RPAREN) {
                    dataType = this.parseDataType();
                }
                final SQLStructDataType.Field field = struct.addField(name, dataType);
                if (this.lexer.token == Token.COMMENT) {
                    this.lexer.nextToken();
                    final SQLCharExpr chars = (SQLCharExpr)this.primary();
                    field.setComment(chars.getText());
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        this.accept(Token.RPAREN);
        return struct;
    }
    
    protected SQLDataType parseDataTypeRest(final SQLDataType dataType) {
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            this.exprList(dataType.getArguments(), dataType);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PRECISION) && dataType.nameHashCode64() == FnvHash.Constants.DOUBLE) {
            this.lexer.nextToken();
            dataType.setName("DOUBLE PRECISION");
        }
        if (FnvHash.Constants.TIMESTAMP == dataType.nameHashCode64() || FnvHash.Constants.TIME == dataType.nameHashCode64()) {
            if (this.lexer.identifierEquals(FnvHash.Constants.WITHOUT)) {
                this.lexer.nextToken();
                this.acceptIdentifier("TIME");
                this.acceptIdentifier("ZONE");
                dataType.setWithTimeZone(false);
            }
            else if (this.lexer.token == Token.WITH) {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.TIME)) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("ZONE");
                    dataType.setWithTimeZone(true);
                }
                else {
                    this.lexer.reset(mark);
                }
            }
        }
        return dataType;
    }
    
    protected boolean isCharType(final String dataTypeName) {
        final long hash = FnvHash.hashCode64(dataTypeName);
        return this.isCharType(hash);
    }
    
    protected boolean isCharType(final long hash) {
        return hash == FnvHash.Constants.CHAR || hash == FnvHash.Constants.VARCHAR || hash == FnvHash.Constants.NCHAR || hash == FnvHash.Constants.NVARCHAR || hash == FnvHash.Constants.TINYTEXT || hash == FnvHash.Constants.TEXT || hash == FnvHash.Constants.MEDIUMTEXT || hash == FnvHash.Constants.LONGTEXT || hash == FnvHash.Constants.ENUM;
    }
    
    protected SQLDataType parseCharTypeRest(final SQLCharacterDataType charType) {
        if (this.lexer.token == Token.BINARY) {
            charType.setHasBinary(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.CHARACTER)) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token != Token.IDENTIFIER && this.lexer.token != Token.LITERAL_CHARS && this.lexer.token != Token.BINARY) {
                throw new ParserException(this.lexer.info());
            }
            charType.setCharSetName(this.lexer.stringVal());
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.CHARSET)) {
            this.lexer.nextToken();
            if (this.lexer.token != Token.IDENTIFIER && this.lexer.token != Token.LITERAL_CHARS && this.lexer.token != Token.BINARY) {
                throw new ParserException(this.lexer.info());
            }
            charType.setCharSetName(this.lexer.stringVal());
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.BINARY) {
            charType.setHasBinary(true);
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
            this.lexer.nextToken();
            if (this.lexer.token != Token.LITERAL_ALIAS && this.lexer.token != Token.IDENTIFIER && this.lexer.token != Token.LITERAL_CHARS) {
                throw new ParserException(this.lexer.info());
            }
            charType.setCollate(this.lexer.stringVal());
            this.lexer.nextToken();
        }
        return charType;
    }
    
    @Override
    public void accept(final Token token) {
        if (this.lexer.token == token) {
            this.lexer.nextToken();
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("syntax error, expect ");
        sb.append((token.name != null) ? token.name : token.toString());
        sb.append(", actual ");
        sb.append((this.lexer.token.name != null) ? this.lexer.token.name : this.lexer.token.toString());
        sb.append(" ");
        sb.append(this.lexer.info());
        throw new ParserException(sb.toString());
    }
    
    public SQLColumnDefinition parseColumn() {
        return this.parseColumn(null);
    }
    
    public SQLColumnDefinition parseColumn(final SQLObject parent) {
        final SQLColumnDefinition column = this.createColumnDefinition();
        column.setName(this.name());
        final Token token = this.lexer.token;
        if (token != Token.SET && token != Token.DROP && token != Token.PRIMARY && token != Token.RPAREN && token != Token.COMMA) {
            column.setDataType(this.parseDataType());
        }
        return this.parseColumnRest(column);
    }
    
    public SQLColumnDefinition createColumnDefinition() {
        final SQLColumnDefinition column = new SQLColumnDefinition();
        column.setDbType(this.dbType);
        return column;
    }
    
    public SQLColumnDefinition parseColumnRest(final SQLColumnDefinition column) {
        switch (this.lexer.token) {
            case DEFAULT: {
                this.lexer.nextToken();
                SQLExpr defaultExpr = null;
                if (this.lexer.token == Token.LITERAL_CHARS && this.dbType == DbType.mysql) {
                    defaultExpr = new SQLCharExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
                else {
                    defaultExpr = this.bitOr();
                }
                column.setDefaultExpr(defaultExpr);
                return this.parseColumnRest(column);
            }
            case NOT: {
                this.lexer.nextToken();
                this.accept(Token.NULL);
                final SQLNotNullConstraint notNull = new SQLNotNullConstraint();
                if (this.lexer.token == Token.HINT) {
                    final List<SQLCommentHint> hints = this.parseHints();
                    notNull.setHints(hints);
                }
                column.addConstraint(notNull);
                return this.parseColumnRest(column);
            }
            case NULL: {
                this.lexer.nextToken();
                column.getConstraints().add(new SQLNullConstraint());
                return this.parseColumnRest(column);
            }
            case PRIMARY: {
                this.lexer.nextToken();
                this.accept(Token.KEY);
                column.addConstraint(new SQLColumnPrimaryKey());
                return this.parseColumnRest(column);
            }
            case UNIQUE: {
                this.lexer.nextToken();
                if (this.lexer.token == Token.KEY) {
                    this.lexer.nextToken();
                }
                column.addConstraint(new SQLColumnUniqueKey());
                return this.parseColumnRest(column);
            }
            case KEY: {
                this.lexer.nextToken();
                column.addConstraint(new SQLColumnPrimaryKey());
                return this.parseColumnRest(column);
            }
            case REFERENCES: {
                final SQLColumnReference ref = this.parseReference();
                column.addConstraint(ref);
                return this.parseColumnRest(column);
            }
            case CONSTRAINT: {
                this.lexer.nextToken();
                final SQLName name = this.name();
                if (this.lexer.token == Token.PRIMARY) {
                    this.lexer.nextToken();
                    this.accept(Token.KEY);
                    final SQLColumnPrimaryKey pk = new SQLColumnPrimaryKey();
                    pk.setName(name);
                    column.addConstraint(pk);
                    return this.parseColumnRest(column);
                }
                if (this.lexer.token == Token.UNIQUE) {
                    this.lexer.nextToken();
                    final SQLColumnUniqueKey uk = new SQLColumnUniqueKey();
                    uk.setName(name);
                    column.addConstraint(uk);
                    return this.parseColumnRest(column);
                }
                if (this.lexer.token == Token.REFERENCES) {
                    final SQLColumnReference ref2 = this.parseReference();
                    ref2.setName(name);
                    column.addConstraint(ref2);
                    return this.parseColumnRest(column);
                }
                if (this.lexer.token == Token.NOT) {
                    this.lexer.nextToken();
                    this.accept(Token.NULL);
                    final SQLNotNullConstraint notNull2 = new SQLNotNullConstraint();
                    notNull2.setName(name);
                    column.addConstraint(notNull2);
                    return this.parseColumnRest(column);
                }
                if (this.lexer.token == Token.CHECK) {
                    final SQLColumnCheck check = this.parseColumnCheck();
                    check.setName(name);
                    check.setParent(column);
                    column.addConstraint(check);
                    return this.parseColumnRest(column);
                }
                if (this.lexer.token == Token.DEFAULT) {
                    this.lexer.nextToken();
                    final SQLExpr expr = this.expr();
                    column.setDefaultExpr(expr);
                    return this.parseColumnRest(column);
                }
                throw new ParserException("TODO : " + this.lexer.info());
            }
            case CHECK: {
                final SQLColumnCheck check = this.parseColumnCheck();
                column.addConstraint(check);
                return this.parseColumnRest(column);
            }
            case IDENTIFIER: {
                final long hash = this.lexer.hash_lower();
                if (hash != FnvHash.Constants.AUTO_INCREMENT) {
                    break;
                }
                this.lexer.nextToken();
                column.setAutoIncrement(true);
                if (this.lexer.token == Token.BY) {
                    this.lexer.nextToken();
                    if (this.lexer.hash_lower() == FnvHash.Constants.GROUP) {
                        this.lexer.nextToken();
                        column.setSequenceType(AutoIncrementType.GROUP);
                        if (!this.lexer.identifierEquals(FnvHash.Constants.UNIT)) {
                            return this.parseColumnRest(column);
                        }
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals(FnvHash.Constants.COUNT)) {
                            this.lexer.nextToken();
                            final SQLExpr unitCount = this.primary();
                            column.setUnitCount(unitCount);
                        }
                        if (this.lexer.token == Token.INDEX) {
                            this.lexer.nextToken();
                            final SQLExpr unitIndex = this.primary();
                            column.setUnitIndex(unitIndex);
                        }
                        if (this.lexer.hash_lower() == FnvHash.Constants.STEP) {
                            this.lexer.nextToken();
                            final SQLExpr step = this.primary();
                            column.setStep(step);
                        }
                    }
                    else {
                        if (this.lexer.hash_lower() == FnvHash.Constants.TIME) {
                            this.lexer.nextToken();
                            column.setSequenceType(AutoIncrementType.TIME);
                            return this.parseColumnRest(column);
                        }
                        if (this.lexer.hash_lower() == FnvHash.Constants.SIMPLE) {
                            this.lexer.nextToken();
                            if (this.lexer.hash_lower() != FnvHash.Constants.WITH) {
                                column.setSequenceType(AutoIncrementType.SIMPLE);
                                return this.parseColumnRest(column);
                            }
                            this.lexer.nextToken();
                            if (this.lexer.hash_lower() == FnvHash.Constants.CACHE) {
                                column.setSequenceType(AutoIncrementType.SIMPLE_CACHE);
                                this.lexer.nextToken();
                                return this.parseColumnRest(column);
                            }
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                    }
                    return this.parseColumnRest(column);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.UNIT)) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals(FnvHash.Constants.COUNT)) {
                        this.lexer.nextToken();
                        final SQLExpr unitCount = this.primary();
                        column.setUnitCount(unitCount);
                    }
                    if (this.lexer.token == Token.INDEX) {
                        this.lexer.nextToken();
                        final SQLExpr unitIndex = this.primary();
                        column.setUnitIndex(unitIndex);
                    }
                    if (this.lexer.hash_lower() == FnvHash.Constants.STEP) {
                        this.lexer.nextToken();
                        final SQLExpr unitIndex = this.primary();
                        column.setStep(unitIndex);
                    }
                }
                else if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLColumnDefinition.Identity ident = new SQLColumnDefinition.Identity();
                    if (this.lexer.token != Token.LITERAL_INT) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    ident.setSeed(this.lexer.integerValue().intValue());
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.COMMA) {
                        this.lexer.nextToken();
                        if (this.lexer.token != Token.LITERAL_INT) {
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                        ident.setIncrement(this.lexer.integerValue().intValue());
                        this.lexer.nextToken();
                    }
                    column.setIdentity(ident);
                    this.accept(Token.RPAREN);
                }
                return this.parseColumnRest(column);
            }
            case COMMENT: {
                this.lexer.nextToken();
                if (this.lexer.token == Token.LITERAL_ALIAS) {
                    String alias = this.lexer.stringVal();
                    if (alias.length() > 2 && alias.charAt(0) == '\"' && alias.charAt(alias.length() - 1) == '\"') {
                        alias = alias.substring(1, alias.length() - 1);
                    }
                    column.setComment(alias);
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    String stringVal = this.lexer.stringVal();
                    this.lexer.nextToken();
                    if (this.dbType == DbType.odps) {
                        while (true) {
                            if (this.lexer.token == Token.LITERAL_ALIAS) {
                                String tmp = this.lexer.stringVal();
                                if (tmp.length() > 2 && tmp.charAt(0) == '\"' && tmp.charAt(tmp.length() - 1) == '\"') {
                                    tmp = tmp.substring(1, tmp.length() - 1);
                                }
                                stringVal += tmp;
                                this.lexer.nextToken();
                            }
                            else {
                                if (this.lexer.token != Token.LITERAL_CHARS) {
                                    break;
                                }
                                stringVal += this.lexer.stringVal();
                                this.lexer.nextToken();
                            }
                        }
                    }
                    column.setComment(stringVal);
                }
                else {
                    column.setComment(this.primary());
                }
                return this.parseColumnRest(column);
            }
        }
        return column;
    }
    
    private SQLColumnReference parseReference() {
        final SQLColumnReference fk = new SQLColumnReference();
        this.lexer.nextToken();
        fk.setTable(this.name());
        this.accept(Token.LPAREN);
        this.names(fk.getColumns(), fk);
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
    
    protected SQLForeignKeyImpl.Option parseReferenceOption() {
        SQLForeignKeyImpl.Option option;
        if (this.lexer.token() == Token.RESTRICT || this.lexer.identifierEquals(FnvHash.Constants.RESTRICT)) {
            option = SQLForeignKeyImpl.Option.RESTRICT;
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.CASCADE)) {
            option = SQLForeignKeyImpl.Option.CASCADE;
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.NULL) {
                this.accept(Token.NULL);
                option = SQLForeignKeyImpl.Option.SET_NULL;
            }
            else {
                if (this.lexer.token != Token.DEFAULT) {
                    throw new ParserException("syntax error," + this.lexer.info());
                }
                this.accept(Token.DEFAULT);
                option = SQLForeignKeyImpl.Option.SET_DEFAULT;
            }
        }
        else {
            if (!this.lexer.identifierEquals(FnvHash.Constants.NO)) {
                throw new ParserException("syntax error, expect ACTION, actual " + this.lexer.token() + " " + this.lexer.info());
            }
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals(FnvHash.Constants.ACTION)) {
                throw new ParserException("syntax error, expect ACTION, actual " + this.lexer.token() + " " + this.lexer.info());
            }
            option = SQLForeignKeyImpl.Option.NO_ACTION;
            this.lexer.nextToken();
        }
        return option;
    }
    
    protected SQLColumnCheck parseColumnCheck() {
        this.lexer.nextToken();
        final SQLExpr expr = this.primary();
        final SQLColumnCheck check = new SQLColumnCheck(expr);
        if (this.lexer.token == Token.DISABLE) {
            this.lexer.nextToken();
            check.setEnable(false);
        }
        else if (this.lexer.token == Token.ENABLE) {
            this.lexer.nextToken();
            check.setEnable(true);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.VALIDATE)) {
            this.lexer.nextToken();
            check.setValidate(Boolean.TRUE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.NOVALIDATE)) {
            this.lexer.nextToken();
            check.setValidate(Boolean.FALSE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.RELY)) {
            this.lexer.nextToken();
            check.setRely(Boolean.TRUE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.NORELY)) {
            this.lexer.nextToken();
            check.setRely(Boolean.FALSE);
        }
        else if (this.lexer.identifierEquals("ENFORCED")) {
            this.lexer.nextToken();
            check.setEnforced(true);
        }
        else if (this.lexer.token == Token.NOT) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("ENFORCED")) {
                this.lexer.nextToken();
                check.setEnforced(false);
            }
            else {
                this.lexer.reset(mark);
            }
        }
        return check;
    }
    
    public SQLPrimaryKey parsePrimaryKey() {
        this.accept(Token.PRIMARY);
        this.accept(Token.KEY);
        final SQLPrimaryKeyImpl pk = new SQLPrimaryKeyImpl();
        if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
            this.lexer.nextToken();
            pk.setClustered(true);
        }
        this.accept(Token.LPAREN);
        this.orderBy(pk.getColumns(), pk);
        this.accept(Token.RPAREN);
        if (this.lexer.token == Token.DISABLE) {
            this.lexer.nextToken();
            this.acceptIdentifier("NOVALIDATE");
            pk.setDisableNovalidate(true);
        }
        return pk;
    }
    
    public SQLUnique parseUnique() {
        this.accept(Token.UNIQUE);
        final SQLUnique unique = new SQLUnique();
        this.accept(Token.LPAREN);
        this.orderBy(unique.getColumns(), unique);
        this.accept(Token.RPAREN);
        if (this.lexer.token == Token.DISABLE) {
            this.lexer.nextToken();
            unique.setEnable(false);
        }
        else if (this.lexer.token == Token.ENABLE) {
            this.lexer.nextToken();
            unique.setEnable(true);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.VALIDATE)) {
            this.lexer.nextToken();
            unique.setValidate(Boolean.TRUE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.NOVALIDATE)) {
            this.lexer.nextToken();
            unique.setValidate(Boolean.FALSE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.RELY)) {
            this.lexer.nextToken();
            unique.setRely(Boolean.TRUE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.NORELY)) {
            this.lexer.nextToken();
            unique.setRely(Boolean.FALSE);
        }
        return unique;
    }
    
    public void parseAssignItem(final List<SQLAssignItem> outList, final SQLObject parent) {
        this.accept(Token.LPAREN);
        while (true) {
            final SQLAssignItem item = this.parseAssignItem(true, parent);
            item.setParent(parent);
            outList.add(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
    }
    
    public SQLAssignItem parseAssignItem() {
        return this.parseAssignItem(true, null);
    }
    
    public SQLAssignItem parseAssignItem(final boolean variant) {
        return this.parseAssignItem(variant, null);
    }
    
    public SQLAssignItem parseAssignItem(final boolean variant, final SQLObject parent) {
        final SQLAssignItem item = new SQLAssignItem();
        if (this.lexer.token == Token.DOT && this.dbType == DbType.odps) {
            this.lexer.nextToken();
        }
        if (this.dbType == DbType.odps && this.lexer.identifierEquals("NC_TO_BE_EXECUTED")) {
            this.lexer.nextToken();
        }
        SQLExpr var;
        if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
            var = new SQLIdentifierExpr(this.lexer.stringVal());
            this.lexer.nextToken();
            if (this.lexer.token == Token.LPAREN && this.dbType == DbType.odps) {
                final SQLListExpr list = new SQLListExpr();
                this.exprList(list.getItems(), list);
                item.setTarget(new SQLIdentifierExpr("tblproperties"));
                item.setValue(list);
                return item;
            }
        }
        else {
            var = this.primary();
        }
        if (var instanceof SQLPropertyExpr && this.lexer.token == Token.SUB && this.dbType == DbType.odps) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)var;
            String name = propertyExpr.getName() + '-';
            this.lexer.nextToken();
            if (this.lexer.token == Token.IDENTIFIER) {
                name += this.lexer.stringVal();
                this.lexer.nextToken();
            }
            propertyExpr.setName(name);
            var = this.primaryRest(propertyExpr);
        }
        if (var instanceof SQLIdentifierExpr && this.dbType == DbType.odps) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)var;
            if ((identExpr.getName().equalsIgnoreCase("et") || identExpr.getName().equalsIgnoreCase("odps")) && this.lexer.token == Token.IDENTIFIER) {
                final SQLExpr expr = this.primary();
                identExpr.setName(identExpr.getName() + ' ' + expr.toString());
            }
        }
        if (var instanceof SQLPropertyExpr && this.dbType == DbType.odps) {
            final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)var;
            if (this.identifierEquals("DATEADD")) {
                String func = this.lexer.stringVal();
                this.lexer.nextToken();
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.accept(Token.RPAREN);
                    func += "()";
                }
                final String name2 = propertyExpr.getName() + func;
                propertyExpr.setName(name2);
            }
            else if (propertyExpr.getName().equalsIgnoreCase("enab") && this.identifierEquals("le")) {
                final String name = propertyExpr.getName() + this.lexer.stringVal();
                this.lexer.nextToken();
                propertyExpr.setName(name);
            }
            else if (propertyExpr.getName().equalsIgnoreCase("sq") && this.identifierEquals("l")) {
                final String name = propertyExpr.getName() + this.lexer.stringVal();
                this.lexer.nextToken();
                propertyExpr.setName(name);
            }
            else if (propertyExpr.getName().equalsIgnoreCase("s") && this.identifierEquals("ql")) {
                final String name = propertyExpr.getName() + this.lexer.stringVal();
                this.lexer.nextToken();
                propertyExpr.setName(name);
                var = this.primaryRest(propertyExpr);
            }
            else if (this.lexer.token == Token.BY) {
                final String name = propertyExpr.getName() + ' ' + this.lexer.stringVal();
                this.lexer.nextToken();
                propertyExpr.setName(name);
                var = this.primaryRest(propertyExpr);
            }
        }
        if (variant && var instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr ident = (SQLIdentifierExpr)var;
            if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTER) && ident.nameHashCode64() == FnvHash.Constants.RUNNING && this.dbType == DbType.odps) {
                final String str = ident.getName() + " " + this.lexer.stringVal();
                this.lexer.nextToken();
                ident.setName(str);
            }
            else if (this.lexer.token == Token.IDENTIFIER && this.dbType == DbType.odps) {
                ident.setName(ident.getName() + ' ' + this.lexer.stringVal());
                this.lexer.nextToken();
                while (this.lexer.token == Token.IDENTIFIER) {
                    ident.setName(ident.getName() + ' ' + this.lexer.stringVal());
                    this.lexer.nextToken();
                }
            }
            final String identName = ident.getName();
            if (identName.indexOf(64) != -1 || identName.indexOf(35) != -1 || identName.indexOf(58) != -1 || identName.indexOf(32) != -1) {
                var = new SQLVariantRefExpr(identName);
            }
        }
        if (var instanceof SQLMethodInvokeExpr && this.dbType == DbType.odps) {
            final SQLMethodInvokeExpr func2 = (SQLMethodInvokeExpr)var;
            final SQLExpr owner = func2.getOwner();
            if (owner != null) {
                item.setTarget(new SQLPropertyExpr(owner, func2.getMethodName()));
            }
            else {
                item.setTarget(new SQLIdentifierExpr(func2.getMethodName()));
            }
            final SQLListExpr properties = new SQLListExpr();
            for (final SQLExpr argument : func2.getArguments()) {
                properties.addItem(argument);
            }
            item.setValue(properties);
            return item;
        }
        item.setTarget(var);
        if (this.lexer.token == Token.COLONEQ) {
            this.lexer.nextToken();
        }
        else {
            if (this.lexer.token == Token.TRUE || this.lexer.identifierEquals(FnvHash.Constants.TRUE)) {
                this.lexer.nextToken();
                item.setValue(new SQLBooleanExpr(true));
                return item;
            }
            if (this.lexer.token == Token.ON) {
                this.lexer.nextToken();
                item.setValue(new SQLIdentifierExpr("ON"));
                return item;
            }
            if (this.lexer.token == Token.RPAREN || this.lexer.token == Token.COMMA || this.lexer.token == Token.SET) {
                if (this.dbType == DbType.odps || this.dbType == DbType.hive) {
                    return item;
                }
            }
            else if (this.lexer.token == Token.EQ) {
                if (this.dbType == DbType.odps) {
                    this.lexer.nextTokenForSet();
                }
                else {
                    this.lexer.nextToken();
                }
                if (this.lexer.token == Token.SEMI && this.dbType == DbType.odps) {
                    return item;
                }
            }
            else if (this.dbType != DbType.db2) {
                if (this.lexer.token != Token.QUES && this.lexer.token != Token.LITERAL_CHARS && this.lexer.token != Token.LITERAL_ALIAS) {
                    if (!this.lexer.identifierEquals("utf8mb4")) {
                        if (this.lexer.token == Token.EQEQ && this.dbType == DbType.odps) {
                            this.lexer.nextToken();
                        }
                        else {
                            this.accept(Token.EQ);
                        }
                    }
                }
            }
        }
        if (this.lexer.token == Token.ON) {
            item.setValue(new SQLIdentifierExpr(this.lexer.stringVal()));
            this.lexer.nextToken();
        }
        else if (this.lexer.token == Token.ALL) {
            item.setValue(new SQLIdentifierExpr(this.lexer.stringVal()));
            this.lexer.nextToken();
        }
        else {
            SQLExpr expr2 = this.expr();
            if (this.dbType == DbType.odps) {
                while (this.lexer.token == Token.LITERAL_FLOAT && this.lexer.numberString().startsWith(".")) {
                    if (expr2 instanceof SQLNumberExpr) {
                        String numStr = ((SQLNumberExpr)expr2).getLiteral();
                        numStr += this.lexer.numberString();
                        expr2 = new SQLIdentifierExpr(numStr);
                        this.lexer.nextToken();
                    }
                    else {
                        if (!(expr2 instanceof SQLIdentifierExpr)) {
                            break;
                        }
                        String ident2 = ((SQLIdentifierExpr)expr2).getName();
                        ident2 += this.lexer.numberString();
                        expr2 = new SQLIdentifierExpr(ident2);
                        this.lexer.nextToken();
                    }
                }
            }
            if (this.lexer.token == Token.COLON && (this.dbType == DbType.hive || this.dbType == DbType.odps)) {
                final Lexer.SavePoint mark = this.lexer.mark();
                this.lexer.nextToken();
                String str2 = expr2.toString() + ':';
                str2 += this.lexer.numberString();
                this.lexer.nextToken();
                expr2 = new SQLIdentifierExpr(str2);
            }
            if (this.lexer.token == Token.COMMA && (DbType.postgresql == this.dbType || (DbType.odps == this.dbType && parent instanceof SQLSetStatement))) {
                final SQLListExpr listExpr = new SQLListExpr();
                listExpr.addItem(expr2);
                expr2.setParent(listExpr);
                do {
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.SET && this.dbType == DbType.odps) {
                        break;
                    }
                    final SQLExpr listItem = this.expr();
                    listItem.setParent(listExpr);
                    listExpr.addItem(listItem);
                } while (this.lexer.token == Token.COMMA);
                item.setValue(listExpr);
            }
            else {
                item.setValue(expr2);
            }
        }
        return item;
    }
    
    public List<SQLCommentHint> parseHints() {
        final List<SQLCommentHint> hints = new ArrayList<SQLCommentHint>();
        this.parseHints(hints);
        return hints;
    }
    
    public void parseHints(final List hints) {
        while (this.lexer.token == Token.HINT) {
            final String text = this.lexer.stringVal();
            SQLCommentHint hint;
            if (this.lexer.isEnabled(SQLParserFeature.TDDLHint) && (text.startsWith("+ TDDL") || text.startsWith("+TDDL") || text.startsWith("!TDDL") || text.startsWith("TDDL"))) {
                hint = new TDDLHint(text);
            }
            else {
                hint = new SQLCommentHint(text);
            }
            if (this.lexer.commentCount > 0) {
                hint.addBeforeComment(this.lexer.comments);
            }
            hints.add(hint);
            this.lexer.nextToken();
        }
    }
    
    public SQLCommentHint parseHint() {
        final String text = this.lexer.stringVal();
        SQLCommentHint hint;
        if (this.lexer.isEnabled(SQLParserFeature.TDDLHint) && (text.startsWith("+ TDDL") || text.startsWith("+TDDL") || text.startsWith("!TDDL") || text.startsWith("TDDL"))) {
            hint = new TDDLHint(text);
        }
        else {
            hint = new SQLCommentHint(text);
        }
        if (this.lexer.commentCount > 0) {
            hint.addBeforeComment(this.lexer.comments);
        }
        this.lexer.nextToken();
        return hint;
    }
    
    public void parseIndex(final SQLIndexDefinition indexDefinition) {
        if (this.lexer.token() == Token.CONSTRAINT) {
            indexDefinition.setHasConstraint(true);
            this.lexer.nextToken();
            if (this.lexer.token() == Token.IDENTIFIER && !this.lexer.identifierEquals(FnvHash.Constants.GLOBAL) && !this.lexer.identifierEquals(FnvHash.Constants.LOCAL) && !this.lexer.identifierEquals(FnvHash.Constants.SPATIAL)) {
                indexDefinition.setSymbol(this.name());
            }
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.GLOBAL)) {
            indexDefinition.setGlobal(true);
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
            indexDefinition.setLocal(true);
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.FULLTEXT || this.lexer.token() == Token.UNIQUE || this.lexer.token() == Token.PRIMARY || this.lexer.identifierEquals(FnvHash.Constants.SPATIAL) || this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED) || this.lexer.identifierEquals(FnvHash.Constants.CLUSTERING) || this.lexer.identifierEquals(FnvHash.Constants.ANN)) {
            indexDefinition.setType(this.lexer.stringVal());
            this.lexer.nextToken();
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.GLOBAL)) {
            indexDefinition.setGlobal(true);
            this.lexer.nextToken();
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.LOCAL)) {
            indexDefinition.setLocal(true);
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.INDEX) {
            indexDefinition.setIndex(true);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.KEY) {
            indexDefinition.setKey(true);
            this.lexer.nextToken();
        }
        while (this.lexer.token() != Token.LPAREN && this.lexer.token() != Token.ON) {
            if (DbType.mysql == this.dbType && this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                this.lexer.nextToken();
                indexDefinition.getOptions().setIndexType(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else if ((DbType.mysql == this.dbType || DbType.ads == this.dbType) && this.lexer.identifierEquals("HASHMAP")) {
                this.lexer.nextToken();
                indexDefinition.setHashMapType(true);
                indexDefinition.getParent().putAttribute("ads.index", Boolean.TRUE);
            }
            else if ((DbType.mysql == this.dbType || DbType.ads == this.dbType) && this.lexer.identifierEquals(FnvHash.Constants.HASH)) {
                this.lexer.nextToken();
                indexDefinition.setHashType(true);
                indexDefinition.getParent().putAttribute("ads.index", Boolean.TRUE);
            }
            else {
                indexDefinition.setName(this.name());
            }
        }
        if (this.lexer.token() == Token.ON) {
            this.lexer.nextToken();
            indexDefinition.setTable(new SQLExprTableSource(this.name()));
        }
        this.parseIndexRest(indexDefinition, indexDefinition.getParent());
    Label_1589:
        while (true) {
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                indexDefinition.getOptions().setComment(this.primary());
            }
            else if (DbType.mysql == this.dbType && this.lexer.identifierEquals("INVISIBLE")) {
                this.lexer.nextToken();
                indexDefinition.getOptions().setInvisible(true);
            }
            else if (DbType.mysql == this.dbType && this.lexer.identifierEquals("VISIBLE")) {
                this.lexer.nextToken();
                indexDefinition.getOptions().setVisible(true);
            }
            else if (DbType.mysql == this.dbType && this.lexer.token == Token.HINT && this.lexer.stringVal().trim().equals("!80000 INVISIBLE")) {
                this.lexer.nextToken();
                indexDefinition.getOptions().setInvisible(true);
            }
            else {
                if (DbType.mysql != this.dbType) {
                    break;
                }
                switch (this.lexer.token()) {
                    case WITH: {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals("PARSER")) {
                            this.lexer.nextToken();
                            indexDefinition.getOptions().setParserName(this.lexer.stringVal());
                            this.lexer.nextToken();
                            continue;
                        }
                        this.lexer.reset(mark);
                        while (this.lexer.token == Token.WITH) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.INDEX) {
                                this.lexer.nextToken();
                                this.acceptIdentifier("ANALYZER");
                                indexDefinition.setIndexAnalyzerName(this.name());
                            }
                            else if (this.lexer.identifierEquals(FnvHash.Constants.QUERY)) {
                                this.lexer.nextToken();
                                this.acceptIdentifier("ANALYZER");
                                indexDefinition.setQueryAnalyzerName(this.name());
                            }
                            else {
                                if (this.lexer.identifierEquals(FnvHash.Constants.ANALYZER)) {
                                    this.lexer.nextToken();
                                    final SQLName name = this.name();
                                    indexDefinition.setAnalyzerName(name);
                                    break;
                                }
                                if (!this.lexer.identifierEquals("DICT")) {
                                    break;
                                }
                                this.lexer.nextToken();
                                indexDefinition.setWithDicName(this.name());
                            }
                        }
                        continue;
                    }
                    case LOCK: {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        indexDefinition.getOptions().setLock(this.lexer.stringVal());
                        this.lexer.nextToken();
                        continue;
                    }
                    case IDENTIFIER: {
                        if (this.lexer.identifierEquals(FnvHash.Constants.KEY_BLOCK_SIZE) || this.lexer.identifierEquals(FnvHash.Constants.BLOCK_SIZE)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.EQ) {
                                this.lexer.nextToken();
                            }
                            indexDefinition.getOptions().setKeyBlockSize(this.expr());
                            continue;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                            this.lexer.nextToken();
                            indexDefinition.getOptions().setIndexType(this.lexer.stringVal());
                            this.lexer.nextToken();
                            continue;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.EQ) {
                                this.lexer.nextToken();
                            }
                            indexDefinition.getOptions().setAlgorithm(this.lexer.stringVal());
                            this.lexer.nextToken();
                            continue;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.DISTANCEMEASURE)) {
                            final SQLExpr key = new SQLIdentifierExpr(this.lexer.stringVal());
                            this.lexer.nextToken();
                            if (this.lexer.token() == Token.EQ) {
                                this.lexer.nextToken();
                            }
                            final SQLAssignItem item = new SQLAssignItem(key, this.primary());
                            if (indexDefinition.getParent() != null) {
                                item.setParent(indexDefinition.getParent());
                            }
                            else {
                                item.setParent(indexDefinition);
                            }
                            indexDefinition.getOptions().getOtherOptions().add(item);
                            indexDefinition.getCompatibleOptions().add(item);
                            continue;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITION)) {
                            this.lexer.nextToken();
                            this.accept(Token.BY);
                            indexDefinition.setDbPartitionBy(this.primary());
                            continue;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.TBPARTITION)) {
                            this.lexer.nextToken();
                            this.accept(Token.BY);
                            SQLExpr expr = this.expr();
                            if (this.lexer.identifierEquals(FnvHash.Constants.STARTWITH)) {
                                this.lexer.nextToken();
                                final SQLExpr start = this.primary();
                                this.acceptIdentifier("ENDWITH");
                                final SQLExpr end = this.primary();
                                expr = new SQLBetweenExpr(expr, start, end);
                            }
                            indexDefinition.setTbPartitionBy(expr);
                            continue;
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.TBPARTITIONS)) {
                            this.lexer.nextToken();
                            indexDefinition.setTbPartitions(this.primary());
                            continue;
                        }
                        break Label_1589;
                    }
                    default: {
                        break Label_1589;
                    }
                }
            }
        }
    }
    
    public SQLConstraint parseConstaint() {
        SQLName name = null;
        if (this.lexer.token == Token.CONSTRAINT) {
            this.lexer.nextToken();
            name = this.name();
        }
        SQLConstraint constraint = null;
        switch (this.lexer.token) {
            case PRIMARY: {
                constraint = this.parsePrimaryKey();
                break;
            }
            case UNIQUE: {
                constraint = this.parseUnique();
                break;
            }
            case KEY: {
                constraint = this.parseUnique();
                break;
            }
            case FOREIGN: {
                constraint = this.parseForeignKey();
                break;
            }
            case CHECK: {
                constraint = this.parseCheck();
                break;
            }
            case DEFAULT: {
                constraint = this.parseDefault();
                break;
            }
            default: {
                throw new ParserException("TODO : " + this.lexer.info());
            }
        }
        constraint.setName(name);
        return constraint;
    }
    
    public SQLCheck parseCheck() {
        this.accept(Token.CHECK);
        final SQLCheck check = this.createCheck();
        this.accept(Token.LPAREN);
        check.setExpr(this.expr());
        this.accept(Token.RPAREN);
        return check;
    }
    
    public SQLDefault parseDefault() {
        this.accept(Token.DEFAULT);
        final SQLDefault sqlDefault = new SQLDefault();
        if (this.lexer.token == Token.LPAREN) {
            while (this.lexer.token == Token.LPAREN) {
                this.accept(Token.LPAREN);
            }
            sqlDefault.setExpr(this.expr());
            while (this.lexer.token == Token.RPAREN) {
                this.accept(Token.RPAREN);
            }
        }
        else {
            sqlDefault.setExpr(this.expr());
        }
        this.accept(Token.FOR);
        sqlDefault.setColumn(this.expr());
        if (this.lexer.token == Token.WITH) {
            this.lexer.nextToken();
            this.accept(Token.VALUES);
            sqlDefault.setWithValues(true);
        }
        return sqlDefault;
    }
    
    protected SQLCheck createCheck() {
        return new SQLCheck();
    }
    
    public SQLForeignKeyConstraint parseForeignKey() {
        this.accept(Token.FOREIGN);
        this.accept(Token.KEY);
        final SQLForeignKeyImpl fk = this.createForeignKey();
        this.accept(Token.LPAREN);
        this.names(fk.getReferencingColumns(), fk);
        this.accept(Token.RPAREN);
        this.accept(Token.REFERENCES);
        fk.setReferencedTableName(this.name());
        if (this.lexer.token == Token.LPAREN) {
            this.lexer.nextToken();
            this.names(fk.getReferencedColumns(), fk);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token == Token.ON) {
            this.lexer.nextToken();
            this.accept(Token.DELETE);
            if (this.lexer.identifierEquals(FnvHash.Constants.CASCADE) || this.lexer.token == Token.CASCADE) {
                this.lexer.nextToken();
                fk.setOnDeleteCascade(true);
            }
            else {
                this.accept(Token.SET);
                this.accept(Token.NULL);
                fk.setOnDeleteSetNull(true);
            }
        }
        if (this.lexer.token == Token.DISABLE) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.identifierEquals(FnvHash.Constants.NOVALIDATE)) {
                this.lexer.nextToken();
                fk.setDisableNovalidate(true);
            }
            else {
                this.lexer.reset(mark);
            }
        }
        return fk;
    }
    
    protected SQLForeignKeyImpl createForeignKey() {
        return new SQLForeignKeyImpl();
    }
    
    public SQLSelectItem parseSelectItem() {
        boolean connectByRoot = false;
        Token token = this.lexer.token;
        final int startPos = this.lexer.startPos;
        SQLExpr expr;
        if (token == Token.IDENTIFIER && (this.lexer.hash_lower() != -5808529385363204345L || this.lexer.charAt(this.lexer.pos) != '\'' || this.dbType != DbType.mysql)) {
            String ident = this.lexer.stringVal();
            long hash_lower = this.lexer.hash_lower();
            this.lexer.nextTokenComma();
            if (hash_lower == FnvHash.Constants.CONNECT_BY_ROOT) {
                connectByRoot = (this.lexer.token != Token.LPAREN);
                if (connectByRoot) {
                    expr = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
                else {
                    expr = new SQLIdentifierExpr(ident);
                }
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE) && this.dbType == DbType.mysql && this.lexer.stringVal().charAt(0) != '`') {
                this.lexer.nextToken();
                final String collate = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)(expr = new SQLBinaryOpExpr(new SQLIdentifierExpr(ident), SQLBinaryOperator.COLLATE, new SQLIdentifierExpr(collate), DbType.mysql));
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.REGEXP) && this.lexer.stringVal().charAt(0) != '`' && this.dbType == DbType.mysql) {
                this.lexer.nextToken();
                final SQLExpr rightExp = this.bitOr();
                final SQLBinaryOpExpr binaryExpr = (SQLBinaryOpExpr)(expr = new SQLBinaryOpExpr(new SQLIdentifierExpr(ident), SQLBinaryOperator.RegExp, rightExp, DbType.mysql));
                expr = this.relationalRest(expr);
            }
            else if (FnvHash.Constants.DATE == hash_lower && this.lexer.stringVal().charAt(0) != '`' && this.lexer.token == Token.LITERAL_CHARS && SQLDateExpr.isSupport(this.dbType)) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLDateExpr dateExpr = new SQLDateExpr();
                dateExpr.setLiteral(literal);
                expr = dateExpr;
            }
            else if (FnvHash.Constants.TIMESTAMP == hash_lower && this.lexer.stringVal().charAt(0) != '`' && this.lexer.token == Token.LITERAL_CHARS && this.dbType != DbType.oracle) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLTimestampExpr ts = (SQLTimestampExpr)(expr = new SQLTimestampExpr(literal));
                if (this.lexer.identifierEquals(FnvHash.Constants.AT)) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    this.lexer.nextToken();
                    String timeZone = null;
                    if (this.lexer.identifierEquals(FnvHash.Constants.TIME)) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals(FnvHash.Constants.ZONE)) {
                            this.lexer.nextToken();
                            timeZone = this.lexer.stringVal();
                            this.lexer.nextToken();
                        }
                    }
                    if (timeZone == null) {
                        this.lexer.reset(mark);
                    }
                    else {
                        ts.setTimeZone(timeZone);
                    }
                }
            }
            else if (FnvHash.Constants.DATETIME == hash_lower && this.lexer.stringVal().charAt(0) != '`' && this.lexer.token == Token.LITERAL_CHARS && this.dbType != DbType.oracle) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLDateTimeExpr ts2 = (SQLDateTimeExpr)(expr = new SQLDateTimeExpr(literal));
            }
            else if (FnvHash.Constants.BOOLEAN == hash_lower && this.lexer.stringVal().charAt(0) != '`' && this.lexer.token == Token.LITERAL_CHARS && this.dbType == DbType.mysql) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLBooleanExpr ts3 = (SQLBooleanExpr)(expr = new SQLBooleanExpr(Boolean.valueOf(literal)));
            }
            else if ((FnvHash.Constants.CHAR == hash_lower || FnvHash.Constants.VARCHAR == hash_lower) && this.lexer.token == Token.LITERAL_CHARS && this.dbType == DbType.mysql) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLCharExpr charExpr = (SQLCharExpr)(expr = new SQLCharExpr(literal));
            }
            else if (FnvHash.Constants.TIME == hash_lower && this.lexer.token == Token.LITERAL_CHARS) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                expr = new SQLTimeExpr(literal);
            }
            else if (hash_lower == FnvHash.Constants.DECIMAL && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLDecimalExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.REAL && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLRealExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.DOUBLE && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLDoubleExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.FLOAT && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLFloatExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.BIGINT && this.lexer.token == Token.LITERAL_CHARS) {
                String strVal = this.lexer.stringVal();
                if (strVal.startsWith("--")) {
                    strVal = strVal.substring(2);
                }
                expr = new SQLBigIntExpr(strVal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.INTEGER && this.lexer.token == Token.LITERAL_CHARS) {
                String strVal = this.lexer.stringVal();
                if (strVal.startsWith("--")) {
                    strVal = strVal.substring(2);
                }
                final SQLIntegerExpr integerExpr = SQLIntegerExpr.ofIntOrLong(Long.parseLong(strVal));
                integerExpr.setType("INTEGER");
                expr = integerExpr;
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.SMALLINT && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLSmallIntExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.TINYINT && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLTinyIntExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.JSON && this.lexer.token == Token.LITERAL_CHARS) {
                final String decimal = this.lexer.stringVal();
                expr = new SQLJSONExpr(decimal);
                this.lexer.nextToken();
            }
            else if (hash_lower == FnvHash.Constants.TRY_CAST) {
                this.accept(Token.LPAREN);
                final SQLCastExpr cast = new SQLCastExpr();
                cast.setTry(true);
                cast.setExpr(this.expr());
                this.accept(Token.AS);
                cast.setDataType(this.parseDataType(false));
                this.accept(Token.RPAREN);
                expr = cast;
            }
            else if (FnvHash.Constants.CURRENT_DATE == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && (this.dbType == DbType.mysql || this.dbType == DbType.hive)) {
                expr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_DATE);
            }
            else if (FnvHash.Constants.CURRENT_TIMESTAMP == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && (this.dbType == DbType.mysql || this.dbType == DbType.hive)) {
                expr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIMESTAMP);
            }
            else if (FnvHash.Constants.CURRENT_TIME == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && (this.dbType == DbType.mysql || this.dbType == DbType.hive)) {
                expr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURRENT_TIME);
            }
            else if (FnvHash.Constants.CURDATE == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && (this.dbType == DbType.mysql || this.dbType == DbType.hive)) {
                expr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.CURDATE);
            }
            else if (FnvHash.Constants.LOCALTIME == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && (this.dbType == DbType.mysql || this.dbType == DbType.hive)) {
                expr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.LOCALTIME);
            }
            else if (FnvHash.Constants.LOCALTIMESTAMP == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && (this.dbType == DbType.mysql || this.dbType == DbType.hive)) {
                expr = new SQLCurrentTimeExpr(SQLCurrentTimeExpr.Type.LOCALTIMESTAMP);
            }
            else if (FnvHash.Constants.CURRENT_USER == hash_lower && ident.charAt(0) != '`' && this.lexer.token != Token.LPAREN && this.isEnabled(SQLParserFeature.EnableCurrentUserExpr)) {
                expr = new SQLCurrentUserExpr();
            }
            else if (FnvHash.Constants._LATIN1 == hash_lower && ident.charAt(0) != '`' && this.dbType == DbType.mysql) {
                String hexString;
                if (this.lexer.token == Token.LITERAL_HEX) {
                    hexString = this.lexer.hexString();
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    hexString = null;
                }
                else {
                    this.acceptIdentifier("X");
                    hexString = this.lexer.stringVal();
                    this.accept(Token.LITERAL_CHARS);
                }
                if (hexString == null) {
                    final String str = this.lexer.stringVal();
                    this.lexer.nextToken();
                    String collate2 = null;
                    if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                        this.lexer.nextToken();
                        collate2 = this.lexer.stringVal();
                        if (this.lexer.token() == Token.LITERAL_CHARS) {
                            this.lexer.nextToken();
                        }
                        else {
                            this.accept(Token.IDENTIFIER);
                        }
                    }
                    expr = new MySqlCharExpr(str, "_latin1", collate2);
                }
                else {
                    expr = new MySqlCharExpr(hexString, "_latin1");
                }
            }
            else if ((FnvHash.Constants._UTF8 == hash_lower || FnvHash.Constants._UTF8MB4 == hash_lower) && ident.charAt(0) != '`' && this.dbType == DbType.mysql) {
                String hexString;
                if (this.lexer.token == Token.LITERAL_HEX) {
                    hexString = this.lexer.hexString();
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    hexString = null;
                }
                else {
                    this.acceptIdentifier("X");
                    hexString = this.lexer.stringVal();
                    this.accept(Token.LITERAL_CHARS);
                }
                if (hexString == null) {
                    final String str = this.lexer.stringVal();
                    this.lexer.nextToken();
                    String collate2 = null;
                    if (this.lexer.identifierEquals(FnvHash.Constants.COLLATE)) {
                        this.lexer.nextToken();
                        collate2 = this.lexer.stringVal();
                        if (this.lexer.token() == Token.LITERAL_CHARS) {
                            this.lexer.nextToken();
                        }
                        else {
                            this.accept(Token.IDENTIFIER);
                        }
                    }
                    expr = new MySqlCharExpr(str, "_utf8", collate2);
                }
                else {
                    expr = new SQLCharExpr(MySqlUtils.utf8(hexString));
                }
            }
            else if ((FnvHash.Constants._UTF16 == hash_lower || FnvHash.Constants._UCS2 == hash_lower) && ident.charAt(0) != '`' && this.dbType == DbType.mysql) {
                String hexString;
                if (this.lexer.token == Token.LITERAL_HEX) {
                    hexString = this.lexer.hexString();
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    hexString = null;
                }
                else {
                    this.acceptIdentifier("X");
                    hexString = this.lexer.stringVal();
                    this.accept(Token.LITERAL_CHARS);
                }
                if (hexString == null) {
                    final String str = this.lexer.stringVal();
                    hexString = HexBin.encode(str.getBytes(MySqlUtils.ASCII));
                    this.lexer.nextToken();
                }
                expr = new MySqlCharExpr(hexString, "_utf16");
            }
            else if (FnvHash.Constants._UTF32 == hash_lower && ident.charAt(0) != '`' && this.dbType == DbType.mysql) {
                String hexString;
                if (this.lexer.token == Token.LITERAL_HEX) {
                    hexString = this.lexer.hexString();
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    hexString = null;
                }
                else {
                    this.acceptIdentifier("X");
                    hexString = this.lexer.stringVal();
                    this.accept(Token.LITERAL_CHARS);
                }
                if (hexString == null) {
                    final String str = this.lexer.stringVal();
                    this.lexer.nextToken();
                    expr = new MySqlCharExpr(str, "_utf32");
                }
                else {
                    expr = new SQLCharExpr(MySqlUtils.utf32(hexString));
                }
            }
            else if (FnvHash.Constants._GBK == hash_lower && ident.charAt(0) != '`' && this.dbType == DbType.mysql) {
                String hexString;
                if (this.lexer.token == Token.LITERAL_HEX) {
                    hexString = this.lexer.hexString();
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    hexString = null;
                }
                else {
                    this.acceptIdentifier("X");
                    hexString = this.lexer.stringVal();
                    this.accept(Token.LITERAL_CHARS);
                }
                if (hexString == null) {
                    final String str = this.lexer.stringVal();
                    this.lexer.nextToken();
                    expr = new MySqlCharExpr(str, "_gbk");
                }
                else {
                    expr = new SQLCharExpr(MySqlUtils.gbk(hexString));
                }
            }
            else if (FnvHash.Constants._BIG5 == hash_lower && ident.charAt(0) != '`' && this.dbType == DbType.mysql) {
                String hexString;
                if (this.lexer.token == Token.LITERAL_HEX) {
                    hexString = this.lexer.hexString();
                    this.lexer.nextToken();
                }
                else if (this.lexer.token == Token.LITERAL_CHARS) {
                    hexString = null;
                }
                else {
                    this.acceptIdentifier("X");
                    hexString = this.lexer.stringVal();
                    this.accept(Token.LITERAL_CHARS);
                }
                if (hexString == null) {
                    final String str = this.lexer.stringVal();
                    this.lexer.nextToken();
                    expr = new MySqlCharExpr(str, "_big5");
                }
                else {
                    expr = new SQLCharExpr(MySqlUtils.big5(hexString));
                }
            }
            else {
                if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                    ident = SQLUtils.normalize(ident, this.dbType);
                }
                if (ident.charAt(0) == '\"' && ident.charAt(ident.length() - 1) == '\"') {
                    hash_lower = FnvHash.hashCode64(ident);
                }
                final SQLIdentifierExpr identifierExpr = new SQLIdentifierExpr(ident, hash_lower);
                if (this.lexer.keepSourceLocation) {
                    this.lexer.computeRowAndColumn();
                    identifierExpr.setSourceLine(this.lexer.posLine);
                    identifierExpr.setSourceColumn(this.lexer.posColumn);
                }
                expr = identifierExpr;
            }
            token = this.lexer.token;
            if (token == Token.DOT) {
                this.lexer.nextTokenIdent();
                String name;
                long name_hash_lower;
                if (this.lexer.token == Token.STAR) {
                    name = "*";
                    name_hash_lower = FnvHash.Constants.STAR;
                }
                else {
                    name = this.lexer.stringVal();
                    name_hash_lower = this.lexer.hash_lower();
                }
                this.lexer.nextTokenComma();
                token = this.lexer.token;
                if (token == Token.LPAREN) {
                    final boolean aggregate = hash_lower == FnvHash.Constants.WMSYS && name_hash_lower == FnvHash.Constants.WM_CONCAT;
                    expr = this.methodRest(expr, name, aggregate);
                    token = this.lexer.token;
                }
                else if (name_hash_lower == FnvHash.Constants.NEXTVAL) {
                    expr = new SQLSequenceExpr((SQLName)expr, SQLSequenceExpr.Function.NextVal);
                }
                else if (name_hash_lower == FnvHash.Constants.CURRVAL) {
                    expr = new SQLSequenceExpr((SQLName)expr, SQLSequenceExpr.Function.CurrVal);
                }
                else if (name_hash_lower == FnvHash.Constants.PREVVAL) {
                    expr = new SQLSequenceExpr((SQLName)expr, SQLSequenceExpr.Function.PrevVal);
                }
                else {
                    if (this.lexer.isEnabled(SQLParserFeature.IgnoreNameQuotes)) {
                        name = SQLUtils.normalize(name, this.dbType);
                    }
                    if (name.charAt(0) == '\"') {
                        name_hash_lower = FnvHash.hashCode64(name);
                    }
                    expr = new SQLPropertyExpr(expr, name, name_hash_lower);
                }
            }
            if (token == Token.COMMA) {
                return new SQLSelectItem(expr, (String)null, connectByRoot);
            }
            if (token == Token.AS) {
                this.lexer.nextTokenAlias();
                String as = null;
                if (this.lexer.token != Token.COMMA && this.lexer.token != Token.FROM) {
                    as = this.lexer.stringVal();
                    if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && as.length() > 1) {
                        as = StringUtils.removeNameQuotes(as);
                    }
                    this.lexer.nextTokenComma();
                    if (this.lexer.token == Token.DOT) {
                        this.lexer.nextToken();
                        as = as + '.' + this.lexer.stringVal();
                        this.lexer.nextToken();
                    }
                }
                return new SQLSelectItem(expr, as, connectByRoot);
            }
            if (token == Token.LITERAL_ALIAS) {
                String as = this.lexer.stringVal();
                if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && as.length() > 1) {
                    as = StringUtils.removeNameQuotes(as);
                }
                this.lexer.nextTokenComma();
                return new SQLSelectItem(expr, as, connectByRoot);
            }
            if (token == Token.IDENTIFIER && hash_lower != FnvHash.Constants.CURRENT) {
                String as;
                if (this.lexer.hash_lower == FnvHash.Constants.FORCE && DbType.mysql == this.dbType) {
                    final String force = this.lexer.stringVal();
                    final Lexer.SavePoint savePoint = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.PARTITION) {
                        this.lexer.reset(savePoint);
                        as = null;
                    }
                    else {
                        as = force;
                        if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && as.length() > 1) {
                            as = StringUtils.removeNameQuotes(as);
                        }
                        this.lexer.nextTokenComma();
                    }
                }
                else if (this.lexer.hash_lower == FnvHash.Constants.SOUNDS && DbType.mysql == this.dbType) {
                    final String sounds = this.lexer.stringVal();
                    final Lexer.SavePoint savePoint = this.lexer.mark();
                    this.lexer.nextToken();
                    if (this.lexer.token == Token.LIKE) {
                        this.lexer.reset(savePoint);
                        expr = this.exprRest(expr);
                        as = this.as();
                    }
                    else {
                        as = sounds;
                        if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && as.length() > 1) {
                            as = StringUtils.removeNameQuotes(as);
                        }
                        this.lexer.nextTokenComma();
                    }
                }
                else if (this.lexer.hash_lower == FnvHash.Constants.COLLATE && this.lexer.stringVal().charAt(0) != '`' && DbType.mysql == this.dbType) {
                    expr = this.primaryRest(expr);
                    as = this.as();
                }
                else if (this.lexer.hash_lower == FnvHash.Constants.REGEXP && this.lexer.stringVal().charAt(0) != '`' && DbType.mysql == this.dbType) {
                    expr = this.exprRest(expr);
                    as = this.as();
                }
                else {
                    as = this.lexer.stringVal();
                    if (this.isEnabled(SQLParserFeature.IgnoreNameQuotes) && as.length() > 1) {
                        as = StringUtils.removeNameQuotes(as);
                    }
                    this.lexer.nextTokenComma();
                }
                return new SQLSelectItem(expr, as, connectByRoot);
            }
            if (token == Token.LPAREN) {
                if (this.dbType == DbType.mysql) {
                    this.lexer.nextTokenValue();
                }
                else {
                    this.lexer.nextToken();
                }
                expr = this.methodRest(expr, false);
            }
            else {
                expr = this.primaryRest(expr);
            }
            expr = this.exprRest(expr);
        }
        else {
            if (token == Token.STAR) {
                expr = new SQLAllColumnExpr();
                this.lexer.nextToken();
                return new SQLSelectItem(expr, (String)null, connectByRoot);
            }
            if (token == Token.DO || token == Token.JOIN || token == Token.TABLESPACE) {
                expr = this.name();
                expr = this.exprRest(expr);
            }
            else {
                if (this.lexer.token == Token.DISTINCT && this.dbType == DbType.elastic_search) {
                    this.lexer.nextToken();
                }
                while (this.lexer.token == Token.HINT) {
                    this.lexer.nextToken();
                }
                expr = this.expr();
            }
        }
        List<String> aliasList = null;
        String alias = null;
        switch (this.lexer.token) {
            case FULL:
            case TABLESPACE: {
                alias = this.lexer.stringVal();
                this.lexer.nextToken();
                break;
            }
            case AS: {
                this.lexer.nextTokenAlias();
                if (this.lexer.token == Token.LITERAL_INT) {
                    alias = '\"' + this.lexer.stringVal() + '\"';
                    this.lexer.nextToken();
                    break;
                }
                if (this.lexer.token == Token.LPAREN) {
                    this.lexer.nextToken();
                    aliasList = new ArrayList<String>();
                    while (true) {
                        final String stringVal = this.lexer.stringVal();
                        this.lexer.nextToken();
                        aliasList.add(stringVal);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                    alias = null;
                    break;
                }
                alias = this.alias();
                break;
            }
            case EOF: {
                alias = null;
                break;
            }
            default: {
                alias = this.as();
                break;
            }
        }
        if (alias == null && this.isEnabled(SQLParserFeature.SelectItemGenerateAlias) && !(expr instanceof SQLName) && !(expr instanceof SQLNumericLiteralExpr) && !(expr instanceof SQLCharExpr) && !(expr instanceof SQLNullExpr) && !(expr instanceof SQLBooleanExpr)) {
            alias = this.lexer.text.substring(startPos, this.lexer.startPos);
            if (this.lexer.comments != null) {
                for (int i = this.lexer.comments.size() - 1; i >= 0; --i) {
                    final String comment = this.lexer.comments.get(i);
                    final int p = alias.lastIndexOf(comment);
                    if (p >= 0) {
                        alias = alias.substring(0, p - 1);
                    }
                }
            }
            alias = CharTypes.trim(alias);
            if (alias.length() > 0) {
                boolean specialChar = false;
                for (int j = 0; j < alias.length(); ++j) {
                    final char ch = alias.charAt(j);
                    if (this.dbType == DbType.mysql) {
                        if (ch == '`') {
                            specialChar = true;
                            break;
                        }
                    }
                    else if (!CharTypes.isIdentifierChar(ch)) {
                        specialChar = true;
                        break;
                    }
                }
                if (specialChar) {
                    if (this.dbType == DbType.mysql) {
                        alias = alias.replaceAll("`", "``");
                        alias = '`' + alias + '`';
                    }
                    else {
                        alias = alias.replaceAll("\"", "\\\"");
                    }
                }
            }
        }
        SQLSelectItem selectItem;
        if (aliasList != null) {
            selectItem = new SQLSelectItem(expr, aliasList, connectByRoot);
        }
        else {
            selectItem = new SQLSelectItem(expr, alias, connectByRoot);
        }
        if (this.lexer.token == Token.HINT && !this.lexer.isEnabled(SQLParserFeature.StrictForWall)) {
            final String comment = "/*" + this.lexer.stringVal() + "*/";
            selectItem.addAfterComment(comment);
            this.lexer.nextToken();
        }
        return selectItem;
    }
    
    protected SQLPartition parsePartition() {
        throw new ParserException("TODO");
    }
    
    public SQLPartitionSpec parsePartitionSpec() {
        final SQLPartitionSpec spec = new SQLPartitionSpec();
        this.accept(Token.PARTITION);
        this.accept(Token.LPAREN);
        while (true) {
            final SQLPartitionSpec.Item item = new SQLPartitionSpec.Item();
            item.setColumn(this.name());
            this.accept(Token.EQ);
            item.setValue(this.expr());
            spec.addItem(item);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        return spec;
    }
    
    protected SQLPartitionBy parsePartitionBy() {
        this.lexer.nextToken();
        this.accept(Token.BY);
        SQLPartitionBy partitionClause = null;
        if (this.lexer.identifierEquals("VALUE")) {
            partitionClause = new SQLPartitionByValue();
            if (this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    partitionClause.addColumn(this.expr());
                    this.accept(Token.RPAREN);
                }
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                this.lexer.nextToken();
                partitionClause.setLifecycle((SQLIntegerExpr)this.expr());
            }
        }
        return partitionClause;
    }
    
    public SQLExpr parseGroupingSet() {
        final String tmp = this.lexer.stringVal();
        this.acceptIdentifier("GROUPING");
        final SQLGroupingSetExpr expr = new SQLGroupingSetExpr();
        if (this.lexer.token == Token.SET || this.lexer.identifierEquals(FnvHash.Constants.SET)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprList(expr.getParameters(), expr);
            this.accept(Token.RPAREN);
            return expr;
        }
        return new SQLIdentifierExpr(tmp);
    }
    
    public SQLPartitionValue parsePartitionValues() {
        if (this.lexer.token != Token.VALUES) {
            return null;
        }
        this.lexer.nextToken();
        SQLPartitionValue values = null;
        if (this.lexer.token == Token.IN) {
            this.lexer.nextToken();
            values = new SQLPartitionValue(SQLPartitionValue.Operator.In);
            this.accept(Token.LPAREN);
            this.exprList(values.getItems(), values);
            this.accept(Token.RPAREN);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.LESS)) {
            this.lexer.nextToken();
            this.acceptIdentifier("THAN");
            values = new SQLPartitionValue(SQLPartitionValue.Operator.LessThan);
            if (this.lexer.identifierEquals(FnvHash.Constants.MAXVALUE)) {
                final SQLIdentifierExpr maxValue = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                maxValue.setParent(values);
                values.addItem(maxValue);
            }
            else {
                this.accept(Token.LPAREN);
                this.exprList(values.getItems(), values);
                this.accept(Token.RPAREN);
            }
        }
        else if (this.lexer.token == Token.LPAREN) {
            values = new SQLPartitionValue(SQLPartitionValue.Operator.List);
            this.lexer.nextToken();
            this.exprList(values.getItems(), values);
            this.accept(Token.RPAREN);
        }
        return values;
    }
    
    protected static boolean isIdent(final SQLExpr expr, final String name) {
        if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
            return identExpr.getName().equalsIgnoreCase(name);
        }
        return false;
    }
    
    public SQLLimit parseLimit() {
        if (this.lexer.token != Token.LIMIT) {
            return null;
        }
        final SQLLimit limit = new SQLLimit();
        this.lexer.nextTokenValue();
        SQLExpr temp;
        if (this.lexer.token == Token.LITERAL_INT) {
            temp = new SQLIntegerExpr(this.lexer.integerValue());
            this.lexer.nextTokenComma();
            if (this.lexer.token != Token.COMMA && this.lexer.token != Token.EOF && this.lexer.token != Token.IDENTIFIER) {
                temp = this.primaryRest(temp);
                temp = this.exprRest(temp);
            }
        }
        else {
            temp = this.expr();
        }
        if (this.lexer.token == Token.COMMA) {
            limit.setOffset(temp);
            this.lexer.nextTokenValue();
            SQLExpr rowCount;
            if (this.lexer.token == Token.LITERAL_INT) {
                rowCount = new SQLIntegerExpr(this.lexer.integerValue());
                this.lexer.nextToken();
                if (this.lexer.token != Token.EOF && this.lexer.token != Token.IDENTIFIER) {
                    rowCount = this.primaryRest(rowCount);
                    rowCount = this.exprRest(rowCount);
                }
            }
            else {
                rowCount = this.expr();
            }
            limit.setRowCount(rowCount);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.OFFSET)) {
            limit.setRowCount(temp);
            this.lexer.nextToken();
            limit.setOffset(this.expr());
        }
        else {
            limit.setRowCount(temp);
        }
        if (this.lexer.token == Token.BY && this.dbType == DbType.clickhouse) {
            this.lexer.nextToken();
            while (true) {
                final SQLExpr item = this.expr();
                limit.addBy(item);
                if (this.lexer.token != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
        return limit;
    }
    
    public void parseIndexRest(final SQLIndex idx) {
        this.parseIndexRest(idx, idx);
    }
    
    public void parseIndexRest(final SQLIndex idx, final SQLObject parent) {
        this.accept(Token.LPAREN);
        while (true) {
            final SQLSelectOrderByItem selectOrderByItem = this.parseSelectOrderByItem();
            selectOrderByItem.setParent(parent);
            idx.getColumns().add(selectOrderByItem);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        if (this.lexer.identifierEquals(FnvHash.Constants.COVERING)) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.token != Token.LPAREN) {
                this.lexer.reset(mark);
                return;
            }
            this.lexer.nextToken();
            while (true) {
                final SQLName name = this.name();
                name.setParent(parent);
                idx.getCovering().add(name);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
    }
    
    public SQLExternalRecordFormat parseRowFormat() {
        this.lexer.nextToken();
        this.acceptIdentifier("FORMAT");
        if (this.lexer.identifierEquals(FnvHash.Constants.DELIMITED)) {
            this.lexer.nextToken();
        }
        final SQLExternalRecordFormat format = new SQLExternalRecordFormat();
        if (this.lexer.identifierEquals(FnvHash.Constants.FIELDS)) {
            this.lexer.nextToken();
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setTerminatedBy(this.expr());
        }
        else if (this.lexer.identifierEquals("FIELD")) {
            throw new ParserException("syntax error, expect FIELDS, " + this.lexer.info());
        }
        if (this.lexer.token() == Token.ESCAPE || this.lexer.identifierEquals(FnvHash.Constants.ESCAPED)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            format.setEscapedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LINES)) {
            this.lexer.nextToken();
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setLinesTerminatedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLLECTION)) {
            this.lexer.nextToken();
            this.acceptIdentifier("ITEMS");
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setCollectionItemsTerminatedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.MAP)) {
            this.lexer.nextToken();
            this.acceptIdentifier("KEYS");
            this.acceptIdentifier("TERMINATED");
            this.accept(Token.BY);
            format.setMapKeysTerminatedBy(this.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SERDE)) {
            this.lexer.nextToken();
            format.setSerde(this.expr());
        }
        return format;
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[SQLExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(SQLExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            SQLExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
