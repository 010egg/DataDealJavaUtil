// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKey;
import com.alibaba.druid.sql.ast.statement.SQLConstraint;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyImpl;
import com.alibaba.druid.sql.ast.SQLSubPartitionByList;
import com.alibaba.druid.sql.ast.SQLSubPartitionByHash;
import com.alibaba.druid.sql.ast.SQLSubPartitionBy;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLSubPartition;
import com.alibaba.druid.sql.ast.SQLPartitionValue;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCheck;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleForeignKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleConstraint;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUnique;
import com.alibaba.druid.sql.ast.statement.SQLUnique;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.ast.statement.SQLCreateIndexStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OraclePrimaryKey;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsOfTypeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIsSetExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.SQLOver;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleAnalytic;
import com.alibaba.druid.sql.ast.SQLKeep;
import com.alibaba.druid.sql.ast.expr.SQLAggregateOption;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleDatetimeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIntervalType;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleIntervalExpr;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLDbLinkExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleRangeExpr;
import com.alibaba.druid.sql.ast.expr.SQLSizeExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleTreatExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleOuterExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleCursorExpr;
import java.math.BigInteger;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import com.alibaba.druid.sql.dialect.oracle.ast.expr.OracleSysdateExpr;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLCharacterDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalDay;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleDataTypeIntervalYear;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class OracleExprParser extends SQLExprParser
{
    public boolean allowStringAdditive;
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public OracleExprParser(final Lexer lexer) {
        super(lexer);
        this.allowStringAdditive = false;
        this.aggregateFunctions = OracleExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = OracleExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.dbType = DbType.oracle;
    }
    
    public OracleExprParser(final String text) {
        this(new OracleLexer(text));
        this.lexer.nextToken();
        this.dbType = DbType.oracle;
    }
    
    public OracleExprParser(final String text, final SQLParserFeature... features) {
        this(new OracleLexer(text, features));
        this.lexer.nextToken();
        this.dbType = DbType.oracle;
    }
    
    @Override
    protected boolean isCharType(final long hash) {
        return hash == FnvHash.Constants.CHAR || hash == FnvHash.Constants.CHARACTER || hash == FnvHash.Constants.NCHAR || hash == FnvHash.Constants.VARCHAR || hash == FnvHash.Constants.VARCHAR2 || hash == FnvHash.Constants.NVARCHAR || hash == FnvHash.Constants.NVARCHAR2 || hash == FnvHash.Constants.CHARACTER;
    }
    
    @Override
    public SQLDataType parseDataType(final boolean restrict) {
        if (this.lexer.token() == Token.CONSTRAINT || this.lexer.token() == Token.COMMA) {
            return null;
        }
        if (this.lexer.token() == Token.DEFAULT || this.lexer.token() == Token.NOT || this.lexer.token() == Token.NULL) {
            return null;
        }
        if (this.lexer.token() == Token.INTERVAL) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("YEAR")) {
                this.lexer.nextToken();
                final OracleDataTypeIntervalYear interval = new OracleDataTypeIntervalYear();
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    interval.addArgument(this.expr());
                    this.accept(Token.RPAREN);
                }
                this.accept(Token.TO);
                this.acceptIdentifier("MONTH");
                return interval;
            }
            this.acceptIdentifier("DAY");
            final OracleDataTypeIntervalDay interval2 = new OracleDataTypeIntervalDay();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                interval2.addArgument(this.expr());
                this.accept(Token.RPAREN);
            }
            this.accept(Token.TO);
            this.acceptIdentifier("SECOND");
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                interval2.getFractionalSeconds().add(this.expr());
                this.accept(Token.RPAREN);
            }
            return interval2;
        }
        else {
            String typeName;
            if (this.lexer.token() == Token.EXCEPTION) {
                typeName = "EXCEPTION";
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.LONG)) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.RAW)) {
                    this.lexer.nextToken();
                    typeName = "LONG RAW";
                }
                else {
                    typeName = "LONG";
                }
            }
            else {
                final SQLName typeExpr = this.name();
                typeName = typeExpr.toString();
            }
            if ("TIMESTAMP".equalsIgnoreCase(typeName)) {
                final SQLDataTypeImpl timestamp = new SQLDataTypeImpl(typeName);
                timestamp.setDbType(this.dbType);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    timestamp.addArgument(this.expr());
                    this.accept(Token.RPAREN);
                }
                if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    if (this.lexer.identifierEquals("LOCAL")) {
                        this.lexer.nextToken();
                        timestamp.setWithLocalTimeZone(true);
                    }
                    timestamp.setWithTimeZone(true);
                    this.acceptIdentifier("TIME");
                    this.acceptIdentifier("ZONE");
                }
                return timestamp;
            }
            if ("national".equalsIgnoreCase(typeName) && this.isCharType(this.lexer.hash_lower())) {
                typeName = typeName + ' ' + this.lexer.stringVal();
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("VARYING")) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
                SQLCharacterDataType charType = new SQLCharacterDataType(typeName);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLExpr arg = this.expr();
                    arg.setParent(charType);
                    charType.addArgument(arg);
                    this.accept(Token.RPAREN);
                }
                charType = (SQLCharacterDataType)this.parseCharTypeRest(charType);
                if (this.lexer.token() == Token.HINT) {
                    final List<SQLCommentHint> hints = this.parseHints();
                    charType.setHints(hints);
                }
                return charType;
            }
            if (this.isCharType(typeName)) {
                if (this.lexer.identifierEquals("VARYING")) {
                    typeName = typeName + ' ' + this.lexer.stringVal();
                    this.lexer.nextToken();
                }
                final SQLCharacterDataType charType = new SQLCharacterDataType(typeName);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    charType.addArgument(this.expr());
                    if (this.lexer.identifierEquals("CHAR")) {
                        this.lexer.nextToken();
                        charType.setCharType("CHAR");
                    }
                    else if (this.lexer.identifierEquals("BYTE")) {
                        this.lexer.nextToken();
                        charType.setCharType("BYTE");
                    }
                    this.accept(Token.RPAREN);
                }
                else {
                    if (this.lexer.token() == Token.COMMA) {
                        return this.parseCharTypeRest(charType);
                    }
                    if (restrict) {
                        this.accept(Token.LPAREN);
                    }
                }
                return this.parseCharTypeRest(charType);
            }
            if (this.lexer.token() == Token.PERCENT) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals("TYPE")) {
                    this.lexer.nextToken();
                    typeName += "%TYPE";
                }
                else {
                    if (!this.lexer.identifierEquals("ROWTYPE")) {
                        throw new ParserException("syntax error : " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    typeName += "%ROWTYPE";
                }
            }
            final SQLDataTypeImpl dataType = new SQLDataTypeImpl(typeName);
            dataType.setDbType(this.dbType);
            return this.parseDataTypeRest(dataType);
        }
    }
    
    @Override
    public SQLExpr primary() {
        final Token tok = this.lexer.token();
        SQLExpr sqlExpr = null;
        switch (tok) {
            case SYSDATE: {
                this.lexer.nextToken();
                final OracleSysdateExpr sysdate = new OracleSysdateExpr();
                if (this.lexer.token() == Token.MONKEYS_AT) {
                    this.lexer.nextToken();
                    this.accept(Token.BANG);
                    sysdate.setOption("!");
                }
                sqlExpr = sysdate;
                return this.primaryRest(sqlExpr);
            }
            case PRIOR: {
                this.lexer.nextToken();
                sqlExpr = this.expr();
                sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Prior, sqlExpr);
                return this.primaryRest(sqlExpr);
            }
            case COLON: {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.LITERAL_INT) {
                    final String name = ":" + this.lexer.numberString();
                    this.lexer.nextToken();
                    return new SQLVariantRefExpr(name);
                }
                if (this.lexer.token() != Token.IDENTIFIER) {
                    throw new ParserException("syntax error : " + this.lexer.info());
                }
                final String name = this.lexer.stringVal();
                if (name.charAt(0) == 'B' || name.charAt(0) == 'b') {
                    this.lexer.nextToken();
                    return new SQLVariantRefExpr(":" + name);
                }
                throw new ParserException("syntax error : " + this.lexer.info());
            }
            case LITERAL_ALIAS: {
                final String alias = this.lexer.stringVal();
                this.lexer.nextToken();
                return this.primaryRest(new SQLIdentifierExpr(alias));
            }
            case BINARY_FLOAT: {
                final OracleBinaryFloatExpr floatExpr = new OracleBinaryFloatExpr();
                floatExpr.setValue(Float.parseFloat(this.lexer.numberString()));
                this.lexer.nextToken();
                return this.primaryRest(floatExpr);
            }
            case BINARY_DOUBLE: {
                final OracleBinaryDoubleExpr doubleExpr = new OracleBinaryDoubleExpr();
                doubleExpr.setValue(Double.parseDouble(this.lexer.numberString()));
                this.lexer.nextToken();
                return this.primaryRest(doubleExpr);
            }
            case TABLE: {
                this.lexer.nextToken();
                return this.primaryRest(new SQLIdentifierExpr("TABLE"));
            }
            case PLUS: {
                this.lexer.nextToken();
                switch (this.lexer.token()) {
                    case LITERAL_INT: {
                        sqlExpr = new SQLIntegerExpr(this.lexer.integerValue());
                        this.lexer.nextToken();
                        break;
                    }
                    case LITERAL_FLOAT: {
                        sqlExpr = this.lexer.numberExpr();
                        this.lexer.nextToken();
                        break;
                    }
                    case BINARY_FLOAT: {
                        sqlExpr = new OracleBinaryFloatExpr(Float.parseFloat(this.lexer.numberString()));
                        this.lexer.nextToken();
                        break;
                    }
                    case BINARY_DOUBLE: {
                        sqlExpr = new OracleBinaryDoubleExpr(Double.parseDouble(this.lexer.numberString()));
                        this.lexer.nextToken();
                        break;
                    }
                    case LPAREN: {
                        this.lexer.nextToken();
                        sqlExpr = this.expr();
                        this.accept(Token.RPAREN);
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, sqlExpr);
                        break;
                    }
                    case IDENTIFIER: {
                        sqlExpr = this.expr();
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Plus, sqlExpr);
                        break;
                    }
                    default: {
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                }
                return this.primaryRest(sqlExpr);
            }
            case SUB: {
                this.lexer.nextToken();
                switch (this.lexer.token()) {
                    case LITERAL_INT: {
                        Number integerValue = this.lexer.integerValue();
                        if (integerValue instanceof Integer) {
                            final int intVal = (int)integerValue;
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
                        break;
                    }
                    case LITERAL_FLOAT: {
                        sqlExpr = this.lexer.numberExpr(true);
                        this.lexer.nextToken();
                        break;
                    }
                    case BINARY_FLOAT: {
                        sqlExpr = new OracleBinaryFloatExpr(Float.parseFloat(this.lexer.numberString()) * -1.0f);
                        this.lexer.nextToken();
                        break;
                    }
                    case BINARY_DOUBLE: {
                        sqlExpr = new OracleBinaryDoubleExpr(Double.parseDouble(this.lexer.numberString()) * -1.0);
                        this.lexer.nextToken();
                        break;
                    }
                    case IDENTIFIER:
                    case VARIANT:
                    case QUES:
                    case LITERAL_ALIAS: {
                        sqlExpr = this.expr();
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, sqlExpr);
                        break;
                    }
                    case LPAREN: {
                        this.lexer.nextToken();
                        sqlExpr = this.expr();
                        this.accept(Token.RPAREN);
                        sqlExpr = new SQLUnaryExpr(SQLUnaryOperator.Negative, sqlExpr);
                        break;
                    }
                    default: {
                        throw new ParserException("TODO " + this.lexer.info());
                    }
                }
                return this.primaryRest(sqlExpr);
            }
            case CURSOR: {
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                final SQLSelect select = this.createSelectParser().select();
                final OracleCursorExpr cursorExpr = new OracleCursorExpr(select);
                this.accept(Token.RPAREN);
                sqlExpr = cursorExpr;
                return this.primaryRest(sqlExpr);
            }
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
            case OUTER: {
                sqlExpr = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
                return this.primaryRest(sqlExpr);
            }
            default: {
                return super.primary();
            }
        }
    }
    
    @Override
    protected SQLExpr methodRest(final SQLExpr expr, final boolean acceptLPAREN) {
        if (acceptLPAREN) {
            this.accept(Token.LPAREN);
        }
        if (this.lexer.token() == Token.PLUS) {
            this.lexer.nextToken();
            this.accept(Token.RPAREN);
            return new OracleOuterExpr(expr);
        }
        if (expr instanceof SQLIdentifierExpr) {
            final String methodName = ((SQLIdentifierExpr)expr).getName();
            final SQLMethodInvokeExpr methodExpr = new SQLMethodInvokeExpr(methodName);
            if ("treat".equalsIgnoreCase(methodName)) {
                final OracleTreatExpr treatExpr = new OracleTreatExpr();
                treatExpr.setExpr(this.expr());
                this.accept(Token.AS);
                if (this.lexer.identifierEquals("REF")) {
                    treatExpr.setRef(true);
                    this.lexer.nextToken();
                }
                treatExpr.setType(this.expr());
                this.accept(Token.RPAREN);
                return this.primaryRest(treatExpr);
            }
        }
        return super.methodRest(expr, false);
    }
    
    @Override
    public SQLExpr primaryRest(SQLExpr expr) {
        if (expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
            final String ident = ((SQLIdentifierExpr)expr).getName();
            final long hashCode64 = identExpr.hashCode64();
            if (hashCode64 == FnvHash.Constants.TIMESTAMP) {
                if (this.lexer.token() != Token.LITERAL_ALIAS && this.lexer.token() != Token.LITERAL_CHARS) {
                    return new SQLIdentifierExpr("TIMESTAMP");
                }
                final SQLTimestampExpr timestamp = new SQLTimestampExpr();
                final String literal = this.lexer.stringVal();
                timestamp.setLiteral(literal);
                this.accept(Token.LITERAL_CHARS);
                if (this.lexer.identifierEquals("AT")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("TIME");
                    this.acceptIdentifier("ZONE");
                    final String timezone = this.lexer.stringVal();
                    timestamp.setTimeZone(timezone);
                    this.accept(Token.LITERAL_CHARS);
                }
                return this.primaryRest(timestamp);
            }
        }
        if (this.lexer.token() == Token.IDENTIFIER && expr instanceof SQLNumericLiteralExpr) {
            final String ident2 = this.lexer.stringVal();
            if (ident2.length() == 1) {
                final char unit = ident2.charAt(0);
                switch (unit) {
                    case 'E':
                    case 'G':
                    case 'K':
                    case 'M':
                    case 'P':
                    case 'T':
                    case 'e':
                    case 'g':
                    case 'k':
                    case 'm':
                    case 'p':
                    case 't': {
                        expr = new SQLSizeExpr(expr, SQLSizeExpr.Unit.valueOf(ident2.toUpperCase()));
                        this.lexer.nextToken();
                        break;
                    }
                }
            }
        }
        if (this.lexer.token() == Token.DOTDOT) {
            this.lexer.nextToken();
            final SQLExpr upBound = this.expr();
            return new OracleRangeExpr(expr, upBound);
        }
        if (this.lexer.token() == Token.MONKEYS_AT) {
            this.lexer.nextToken();
            final SQLDbLinkExpr dblink = new SQLDbLinkExpr();
            dblink.setExpr(expr);
            if (this.lexer.token() == Token.BANG) {
                dblink.setDbLink("!");
                this.lexer.nextToken();
            }
            else {
                final String link = this.lexer.stringVal();
                if (this.lexer.token() == Token.LITERAL_ALIAS) {
                    this.accept(Token.LITERAL_ALIAS);
                }
                else {
                    this.accept(Token.IDENTIFIER);
                }
                dblink.setDbLink(link);
                if (this.lexer.token() == Token.LPAREN) {
                    final SQLExpr dblinkExpr = dblink.getExpr();
                    if (dblinkExpr.getClass() == SQLPropertyExpr.class) {
                        final SQLExpr methodInvokeExpr = new SQLMethodInvokeExpr();
                        final SQLMethodInvokeExpr expr2 = (SQLMethodInvokeExpr)this.methodRest(methodInvokeExpr, true);
                        expr2.setOwner(((SQLPropertyExpr)expr).getOwner());
                        expr2.setMethodName(((SQLPropertyExpr)expr).getName());
                        dblink.setExpr(expr2);
                    }
                }
            }
            expr = dblink;
        }
        if (this.lexer.token() == Token.LBRACKET) {
            final SQLArrayExpr arrayExpr = new SQLArrayExpr();
            arrayExpr.setExpr(expr);
            this.lexer.nextToken();
            this.exprList(arrayExpr.getValues(), arrayExpr);
            this.accept(Token.RBRACKET);
            expr = arrayExpr;
            expr = this.primaryRest(expr);
        }
        if (this.lexer.identifierEquals("DAY") || this.lexer.identifierEquals("YEAR")) {
            final Lexer.SavePoint savePoint = this.lexer.mark();
            final String name = this.lexer.stringVal();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.reset(savePoint);
                return expr;
            }
            final OracleIntervalExpr interval = new OracleIntervalExpr();
            interval.setValue(expr);
            final OracleIntervalType type = OracleIntervalType.valueOf(name.toUpperCase());
            interval.setType(type);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.LITERAL_INT) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                interval.setPrecision(this.lexer.integerValue().intValue());
                this.lexer.nextToken();
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token() != Token.TO) {
                this.lexer.reset(savePoint);
                return expr;
            }
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("SECOND")) {
                this.lexer.nextToken();
                interval.setToType(OracleIntervalType.SECOND);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.LITERAL_INT) {
                        throw new ParserException("syntax error. " + this.lexer.info());
                    }
                    interval.setFactionalSecondsPrecision(this.lexer.integerValue().intValue());
                    this.lexer.nextToken();
                    this.accept(Token.RPAREN);
                }
            }
            else {
                interval.setToType(OracleIntervalType.MONTH);
                this.lexer.nextToken();
            }
            expr = interval;
        }
        if (this.lexer.identifierEquals("AT")) {
            final Lexer.SavePoint mark = this.lexer.mark();
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("LOCAL")) {
                this.lexer.nextToken();
                expr = new OracleDatetimeExpr(expr, new SQLIdentifierExpr("LOCAL"));
            }
            else {
                if (!this.lexer.identifierEquals("TIME")) {
                    this.lexer.reset(mark);
                    return expr;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("ZONE");
                final SQLExpr timeZone = this.primary();
                expr = new OracleDatetimeExpr(expr, timeZone);
            }
        }
        final SQLExpr restExpr = super.primaryRest(expr);
        if (restExpr != expr && restExpr instanceof SQLMethodInvokeExpr) {
            final SQLMethodInvokeExpr methodInvoke = (SQLMethodInvokeExpr)restExpr;
            if (methodInvoke.getArguments().size() == 1) {
                final SQLExpr paramExpr = methodInvoke.getArguments().get(0);
                if (paramExpr instanceof SQLIdentifierExpr && "+".equals(((SQLIdentifierExpr)paramExpr).getName())) {
                    final OracleOuterExpr outerExpr = new OracleOuterExpr();
                    if (methodInvoke.getOwner() == null) {
                        outerExpr.setExpr(new SQLIdentifierExpr(methodInvoke.getMethodName()));
                    }
                    else {
                        outerExpr.setExpr(new SQLPropertyExpr(methodInvoke.getOwner(), methodInvoke.getMethodName()));
                    }
                    return outerExpr;
                }
            }
        }
        return restExpr;
    }
    
    @Override
    protected SQLExpr dotRest(SQLExpr expr) {
        if (this.lexer.token() == Token.LITERAL_ALIAS) {
            final String name = this.lexer.stringVal();
            this.lexer.nextToken();
            expr = new SQLPropertyExpr(expr, name);
            if (this.lexer.token() == Token.DOT) {
                this.lexer.nextToken();
                expr = this.dotRest(expr);
            }
            return expr;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.NEXTVAL)) {
            if (expr instanceof SQLIdentifierExpr) {
                final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
                final SQLSequenceExpr seqExpr = new SQLSequenceExpr(identExpr, SQLSequenceExpr.Function.NextVal);
                this.lexer.nextToken();
                return seqExpr;
            }
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.CURRVAL) && expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
            final SQLSequenceExpr seqExpr = new SQLSequenceExpr(identExpr, SQLSequenceExpr.Function.CurrVal);
            this.lexer.nextToken();
            return seqExpr;
        }
        return super.dotRest(expr);
    }
    
    @Override
    protected SQLAggregateExpr parseAggregateExpr(final String methodName) {
        SQLAggregateExpr aggregateExpr;
        if (this.lexer.token() == Token.UNIQUE) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.UNIQUE);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.ALL) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.ALL);
            this.lexer.nextToken();
        }
        else if (this.lexer.token() == Token.DISTINCT) {
            aggregateExpr = new SQLAggregateExpr(methodName, SQLAggregateOption.DISTINCT);
            this.lexer.nextToken();
        }
        else {
            aggregateExpr = new SQLAggregateExpr(methodName);
        }
        this.exprList(aggregateExpr.getArguments(), aggregateExpr);
        if (this.lexer.stringVal().equalsIgnoreCase("IGNORE")) {
            this.lexer.nextToken();
            this.acceptIdentifier("NULLS");
            aggregateExpr.setIgnoreNulls(true);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.RESPECT)) {
            this.lexer.nextToken();
            this.acceptIdentifier("NULLS");
            aggregateExpr.setIgnoreNulls(false);
        }
        this.accept(Token.RPAREN);
        if (this.lexer.stringVal().equalsIgnoreCase("IGNORE")) {
            this.lexer.nextToken();
            this.acceptIdentifier("NULLS");
            aggregateExpr.setIgnoreNulls(true);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.RESPECT)) {
            this.lexer.nextToken();
            this.acceptIdentifier("NULLS");
            aggregateExpr.setIgnoreNulls(false);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.WITHIN)) {
            this.lexer.nextToken();
            this.accept(Token.GROUP);
            this.accept(Token.LPAREN);
            final SQLOrderBy orderBy = this.parseOrderBy();
            aggregateExpr.setWithinGroup(true);
            aggregateExpr.setOrderBy(orderBy);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals("KEEP")) {
            this.lexer.nextToken();
            final SQLKeep keep = new SQLKeep();
            this.accept(Token.LPAREN);
            this.acceptIdentifier("DENSE_RANK");
            if (this.lexer.identifierEquals("FIRST")) {
                this.lexer.nextToken();
                keep.setDenseRank(SQLKeep.DenseRank.FIRST);
            }
            else {
                this.acceptIdentifier("LAST");
                keep.setDenseRank(SQLKeep.DenseRank.LAST);
            }
            final SQLOrderBy orderBy2 = this.parseOrderBy();
            keep.setOrderBy(orderBy2);
            aggregateExpr.setKeep(keep);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.OVER) {
            final OracleAnalytic over = new OracleAnalytic();
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            if (this.lexer.token() == Token.PARTITION) {
                this.lexer.nextToken();
                this.accept(Token.BY);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprList(over.getPartitionBy(), over);
                    this.accept(Token.RPAREN);
                }
                else {
                    this.exprList(over.getPartitionBy(), over);
                }
            }
            final SQLOrderBy orderBy2 = this.parseOrderBy();
            if (orderBy2 != null) {
                over.setOrderBy(orderBy2);
                OracleAnalyticWindowing windowing = null;
                if (this.lexer.identifierEquals(FnvHash.Constants.ROWS)) {
                    this.lexer.nextToken();
                    windowing = new OracleAnalyticWindowing();
                    windowing.setType(OracleAnalyticWindowing.Type.ROWS);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.RANGE)) {
                    this.lexer.nextToken();
                    windowing = new OracleAnalyticWindowing();
                    windowing.setType(OracleAnalyticWindowing.Type.RANGE);
                }
                if (windowing != null) {
                    if (this.lexer.identifierEquals(FnvHash.Constants.CURRENT)) {
                        this.lexer.nextToken();
                        this.accept(Token.ROW);
                        windowing.setExpr(new SQLIdentifierExpr("CURRENT ROW"));
                        over.setWindowing(windowing);
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.UNBOUNDED)) {
                        this.lexer.nextToken();
                        if (!this.lexer.stringVal().equalsIgnoreCase("PRECEDING")) {
                            throw new ParserException("syntax error. " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        windowing.setExpr(new SQLIdentifierExpr("UNBOUNDED PRECEDING"));
                    }
                    else if (this.lexer.token() == Token.BETWEEN) {
                        this.lexer.nextToken();
                        SQLExpr beginExpr;
                        if (this.lexer.identifierEquals(FnvHash.Constants.CURRENT)) {
                            this.lexer.nextToken();
                            this.accept(Token.ROW);
                            beginExpr = new SQLIdentifierExpr("CURRENT ROW");
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.UNBOUNDED)) {
                            this.lexer.nextToken();
                            if (!this.lexer.stringVal().equalsIgnoreCase("PRECEDING")) {
                                throw new ParserException("syntax error. " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                            beginExpr = new SQLIdentifierExpr("UNBOUNDED PRECEDING");
                        }
                        else {
                            beginExpr = this.relational();
                        }
                        final SQLOver.WindowingBound beginBound = this.parseWindowingBound();
                        if (beginBound != null) {
                            over.setWindowingBetweenBeginBound(beginBound);
                        }
                        this.accept(Token.AND);
                        SQLExpr endExpr;
                        if (this.lexer.identifierEquals(FnvHash.Constants.CURRENT)) {
                            this.lexer.nextToken();
                            this.accept(Token.ROW);
                            endExpr = new SQLIdentifierExpr("CURRENT ROW");
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.UNBOUNDED)) {
                            this.lexer.nextToken();
                            if (!this.lexer.stringVal().equalsIgnoreCase("PRECEDING")) {
                                throw new ParserException("syntax error. " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                            endExpr = new SQLIdentifierExpr("UNBOUNDED PRECEDING");
                        }
                        else {
                            endExpr = this.relational();
                        }
                        final SQLExpr expr = new SQLBetweenExpr(null, beginExpr, endExpr);
                        windowing.setExpr(expr);
                    }
                    else {
                        final SQLExpr expr2 = this.expr();
                        windowing.setExpr(expr2);
                        this.acceptIdentifier("PRECEDING");
                        over.setWindowingPreceding(true);
                    }
                    over.setWindowing(windowing);
                }
            }
            this.accept(Token.RPAREN);
            aggregateExpr.setOver(over);
        }
        return aggregateExpr;
    }
    
    private OracleIntervalType parseIntervalType() {
        final String currentTokenUpperValue = this.lexer.stringVal();
        this.lexer.nextToken();
        if (currentTokenUpperValue.equals("YEAR")) {
            return OracleIntervalType.YEAR;
        }
        if (currentTokenUpperValue.equals("MONTH")) {
            return OracleIntervalType.MONTH;
        }
        if (currentTokenUpperValue.equals("HOUR")) {
            return OracleIntervalType.HOUR;
        }
        if (currentTokenUpperValue.equals("MINUTE")) {
            return OracleIntervalType.MINUTE;
        }
        if (currentTokenUpperValue.equals("SECOND")) {
            return OracleIntervalType.SECOND;
        }
        throw new ParserException("syntax error. " + this.lexer.info());
    }
    
    @Override
    public OracleSelectParser createSelectParser() {
        return new OracleSelectParser(this);
    }
    
    @Override
    protected SQLExpr parseInterval() {
        this.accept(Token.INTERVAL);
        final OracleIntervalExpr interval = new OracleIntervalExpr();
        if (this.lexer.token() == Token.LITERAL_CHARS) {
            interval.setValue(new SQLCharExpr(this.lexer.stringVal()));
        }
        else if (this.lexer.token() == Token.VARIANT) {
            interval.setValue(new SQLVariantRefExpr(this.lexer.stringVal()));
        }
        else {
            if (this.lexer.token() != Token.QUES) {
                return new SQLIdentifierExpr("INTERVAL");
            }
            interval.setValue(new SQLVariantRefExpr("?"));
        }
        this.lexer.nextToken();
        OracleIntervalType type;
        if (this.lexer.identifierEquals(FnvHash.Constants.YEAR)) {
            this.lexer.nextToken();
            type = OracleIntervalType.YEAR;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MONTH)) {
            this.lexer.nextToken();
            type = OracleIntervalType.MONTH;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.DAY)) {
            this.lexer.nextToken();
            type = OracleIntervalType.DAY;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.HOUR)) {
            this.lexer.nextToken();
            type = OracleIntervalType.HOUR;
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MINUTE)) {
            this.lexer.nextToken();
            type = OracleIntervalType.MINUTE;
        }
        else {
            if (!this.lexer.identifierEquals(FnvHash.Constants.SECOND)) {
                throw new ParserException("illegal interval type. " + this.lexer.info());
            }
            this.lexer.nextToken();
            type = OracleIntervalType.SECOND;
        }
        interval.setType(type);
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            if (this.lexer.token() != Token.LITERAL_INT && this.lexer.token() != Token.VARIANT) {
                throw new ParserException("syntax error. " + this.lexer.info());
            }
            interval.setPrecision(this.primary());
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.LITERAL_INT) {
                    throw new ParserException("syntax error. " + this.lexer.info());
                }
                interval.setFactionalSecondsPrecision(this.lexer.integerValue().intValue());
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.TO) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("SECOND")) {
                this.lexer.nextToken();
                interval.setToType(OracleIntervalType.SECOND);
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.LITERAL_INT && this.lexer.token() != Token.VARIANT) {
                        throw new ParserException("syntax error. " + this.lexer.info());
                    }
                    interval.setToFactionalSecondsPrecision(this.primary());
                    this.accept(Token.RPAREN);
                }
            }
            else {
                interval.setToType(OracleIntervalType.MONTH);
                this.lexer.nextToken();
            }
        }
        return interval;
    }
    
    @Override
    public SQLExpr relationalRest(SQLExpr expr) {
        if (this.lexer.token() == Token.IS) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                final SQLExpr rightExpr = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.IsNot, rightExpr, this.getDbType());
            }
            else if (this.lexer.identifierEquals("A")) {
                this.lexer.nextToken();
                this.accept(Token.SET);
                expr = new OracleIsSetExpr(expr);
            }
            else if (this.lexer.token() == Token.OF) {
                this.lexer.nextToken();
                if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                    this.lexer.nextToken();
                }
                final OracleIsOfTypeExpr isOf = new OracleIsOfTypeExpr();
                isOf.setExpr(expr);
                this.accept(Token.LPAREN);
                while (true) {
                    final boolean only = this.lexer.identifierEquals(FnvHash.Constants.ONLY);
                    if (only) {
                        this.lexer.nextToken();
                    }
                    final SQLExpr type = this.name();
                    if (only) {
                        type.putAttribute("ONLY", true);
                    }
                    type.setParent(isOf);
                    isOf.getTypes().add(type);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                expr = isOf;
            }
            else {
                final SQLExpr rightExpr = this.primary();
                expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Is, rightExpr, this.getDbType());
            }
            return expr;
        }
        return super.relationalRest(expr);
    }
    
    @Override
    public SQLName name() {
        final SQLName name = super.name();
        if (this.lexer.token() != Token.MONKEYS_AT) {
            return name;
        }
        this.lexer.nextToken();
        if (this.lexer.token() != Token.IDENTIFIER) {
            throw new ParserException("syntax error, expect identifier, but " + this.lexer.token() + ", " + this.lexer.info());
        }
        final SQLDbLinkExpr dbLink = new SQLDbLinkExpr();
        dbLink.setExpr(name);
        String link = this.lexer.stringVal();
        this.lexer.nextToken();
        while (this.lexer.token() == Token.DOT) {
            this.lexer.nextToken();
            final String stringVal = this.lexer.stringVal();
            this.accept(Token.IDENTIFIER);
            link = link + "." + stringVal;
        }
        dbLink.setDbLink(link);
        return dbLink;
    }
    
    @Override
    public OraclePrimaryKey parsePrimaryKey() {
        this.lexer.nextToken();
        this.accept(Token.KEY);
        final OraclePrimaryKey primaryKey = new OraclePrimaryKey();
        this.accept(Token.LPAREN);
        this.orderBy(primaryKey.getColumns(), primaryKey);
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.USING) {
            final OracleUsingIndexClause using = this.parseUsingIndex();
            primaryKey.setUsing(using);
        }
        while (true) {
            if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                primaryKey.setEnable(Boolean.TRUE);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                primaryKey.setEnable(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("VALIDATE")) {
                this.lexer.nextToken();
                primaryKey.setValidate(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NOVALIDATE")) {
                this.lexer.nextToken();
                primaryKey.setValidate(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("RELY")) {
                this.lexer.nextToken();
                primaryKey.setRely(Boolean.TRUE);
            }
            else {
                if (!this.lexer.identifierEquals("NORELY")) {
                    break;
                }
                this.lexer.nextToken();
                primaryKey.setRely(Boolean.FALSE);
            }
        }
        return primaryKey;
    }
    
    private OracleUsingIndexClause parseUsingIndex() {
        this.accept(Token.USING);
        this.accept(Token.INDEX);
        final OracleUsingIndexClause using = new OracleUsingIndexClause();
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            final OracleCreateIndexStatement createIndex = new OracleStatementParser(this.lexer).parseCreateIndex(true);
            using.setIndex(createIndex);
            this.accept(Token.RPAREN);
        }
        while (true) {
            this.parseSegmentAttributes(using);
            if (this.lexer.token() == Token.COMPUTE) {
                this.lexer.nextToken();
                this.acceptIdentifier("STATISTICS");
                using.setComputeStatistics(true);
            }
            else if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                using.setEnable(true);
            }
            else if (this.lexer.identifierEquals("REVERSE")) {
                this.lexer.nextToken();
                using.setReverse(true);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                using.setEnable(false);
            }
            else {
                if (!this.lexer.identifierEquals("LOCAL")) {
                    if (this.lexer.token() == Token.IDENTIFIER) {
                        using.setTablespace(this.name());
                    }
                    return using;
                }
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                while (true) {
                    final SQLPartition partition = this.parsePartition();
                    partition.setParent(using);
                    using.getLocalPartitionIndex().add(partition);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                if (this.lexer.token() != Token.RPAREN) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.accept(Token.RPAREN);
            }
        }
    }
    
    @Override
    public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
        column = super.parseColumnRest(column);
        if (this.lexer.identifierEquals(FnvHash.Constants.GENERATED)) {
            this.lexer.nextToken();
            if (!this.lexer.identifierEquals(FnvHash.Constants.ALWAYS)) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            this.accept(Token.AS);
            final SQLExpr expr = this.expr();
            column.setGeneratedAlawsAs(expr);
        }
        while (true) {
            if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                column.setEnable(Boolean.TRUE);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                column.setEnable(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("VALIDATE")) {
                this.lexer.nextToken();
                column.setValidate(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NOVALIDATE")) {
                this.lexer.nextToken();
                column.setValidate(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals("RELY")) {
                this.lexer.nextToken();
                column.setRely(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NORELY")) {
                this.lexer.nextToken();
                column.setRely(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.VISIBLE)) {
                this.lexer.nextToken();
                column.setVisible(true);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.VIRTUAL)) {
                    break;
                }
                this.lexer.nextToken();
                column.setVirtual(true);
            }
        }
        return column;
    }
    
    @Override
    public SQLExpr exprRest(SQLExpr expr) {
        expr = super.exprRest(expr);
        if (this.lexer.token() == Token.COLONEQ) {
            this.lexer.nextToken();
            final SQLExpr right = this.expr();
            expr = new SQLBinaryOpExpr(expr, SQLBinaryOperator.Assignment, right, this.getDbType());
        }
        return expr;
    }
    
    public SQLObject parseOpaque() {
        this.acceptIdentifier("OPAQUE");
        this.acceptIdentifier("TYPE");
        final SQLExpr expr = this.primary();
        final OracleLobStorageClause clause = new OracleLobStorageClause();
        this.accept(Token.STORE);
        this.accept(Token.AS);
        if (this.lexer.identifierEquals("SECUREFILE")) {
            this.lexer.nextToken();
            clause.setSecureFile(true);
        }
        if (this.lexer.identifierEquals("BASICFILE")) {
            this.lexer.nextToken();
            clause.setBasicFile(true);
        }
        this.accept(Token.LOB);
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                this.parseSegmentAttributes(clause);
                if (this.lexer.token() == Token.ENABLE) {
                    this.lexer.nextToken();
                    this.accept(Token.STORAGE);
                    this.accept(Token.IN);
                    this.accept(Token.ROW);
                    clause.setEnable(true);
                }
                else if (this.lexer.token() == Token.DISABLE) {
                    this.lexer.nextToken();
                    this.accept(Token.STORAGE);
                    this.accept(Token.IN);
                    this.accept(Token.ROW);
                    clause.setEnable(false);
                }
                else if (this.lexer.token() == Token.CHUNK) {
                    this.lexer.nextToken();
                    clause.setChunk(this.primary());
                }
                else if (this.lexer.token() == Token.NOCACHE) {
                    this.lexer.nextToken();
                    clause.setCache(false);
                    if (this.lexer.token() != Token.LOGGING) {
                        continue;
                    }
                    this.lexer.nextToken();
                    clause.setLogging(true);
                }
                else if (this.lexer.token() == Token.CACHE) {
                    this.lexer.nextToken();
                    clause.setCache(true);
                }
                else if (this.lexer.token() == Token.KEEP_DUPLICATES) {
                    this.lexer.nextToken();
                    clause.setKeepDuplicate(true);
                }
                else if (this.lexer.identifierEquals("PCTVERSION")) {
                    this.lexer.nextToken();
                    clause.setPctversion(this.expr());
                }
                else if (this.lexer.identifierEquals("RETENTION")) {
                    this.lexer.nextToken();
                    clause.setRetention(true);
                }
                else {
                    if (this.lexer.token() != Token.STORAGE) {
                        break;
                    }
                    final OracleStorageClause storageClause = this.parseStorage();
                    clause.setStorageClause(storageClause);
                }
            }
            this.accept(Token.RPAREN);
        }
        return clause;
    }
    
    public OracleLobStorageClause parseLobStorage() {
        this.lexer.nextToken();
        final OracleLobStorageClause clause = new OracleLobStorageClause();
        this.accept(Token.LPAREN);
        this.names(clause.getItems());
        this.accept(Token.RPAREN);
        this.accept(Token.STORE);
        this.accept(Token.AS);
        if (this.lexer.identifierEquals("SECUREFILE")) {
            this.lexer.nextToken();
            clause.setSecureFile(true);
        }
        if (this.lexer.identifierEquals("BASICFILE")) {
            this.lexer.nextToken();
            clause.setBasicFile(true);
        }
        if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.LITERAL_ALIAS) {
            final SQLName segmentName = this.name();
            clause.setSegementName(segmentName);
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                this.parseSegmentAttributes(clause);
                if (this.lexer.token() == Token.ENABLE) {
                    this.lexer.nextToken();
                    this.accept(Token.STORAGE);
                    this.accept(Token.IN);
                    this.accept(Token.ROW);
                    clause.setEnable(true);
                }
                else if (this.lexer.token() == Token.DISABLE) {
                    this.lexer.nextToken();
                    this.accept(Token.STORAGE);
                    this.accept(Token.IN);
                    this.accept(Token.ROW);
                    clause.setEnable(false);
                }
                else if (this.lexer.token() == Token.CHUNK) {
                    this.lexer.nextToken();
                    clause.setChunk(this.primary());
                }
                else if (this.lexer.token() == Token.NOCACHE) {
                    this.lexer.nextToken();
                    clause.setCache(false);
                    if (this.lexer.token() != Token.LOGGING) {
                        continue;
                    }
                    this.lexer.nextToken();
                    clause.setLogging(true);
                }
                else if (this.lexer.token() == Token.CACHE) {
                    this.lexer.nextToken();
                    clause.setCache(true);
                }
                else if (this.lexer.token() == Token.KEEP_DUPLICATES) {
                    this.lexer.nextToken();
                    clause.setKeepDuplicate(true);
                }
                else if (this.lexer.identifierEquals("PCTVERSION")) {
                    this.lexer.nextToken();
                    clause.setPctversion(this.expr());
                }
                else if (this.lexer.identifierEquals("RETENTION")) {
                    this.lexer.nextToken();
                    clause.setRetention(true);
                }
                else {
                    if (this.lexer.token() != Token.STORAGE) {
                        break;
                    }
                    final OracleStorageClause storageClause = this.parseStorage();
                    clause.setStorageClause(storageClause);
                }
            }
            this.accept(Token.RPAREN);
        }
        return clause;
    }
    
    public OracleStorageClause parseStorage() {
        this.lexer.nextToken();
        this.accept(Token.LPAREN);
        final OracleStorageClause storage = new OracleStorageClause();
        while (true) {
            if (this.lexer.identifierEquals("INITIAL")) {
                this.lexer.nextToken();
                storage.setInitial(this.expr());
            }
            else if (this.lexer.token() == Token.NEXT) {
                this.lexer.nextToken();
                storage.setNext(this.expr());
            }
            else if (this.lexer.token() == Token.MINEXTENTS) {
                this.lexer.nextToken();
                storage.setMinExtents(this.expr());
            }
            else if (this.lexer.token() == Token.MAXEXTENTS) {
                this.lexer.nextToken();
                storage.setMaxExtents(this.expr());
            }
            else if (this.lexer.token() == Token.MAXSIZE) {
                this.lexer.nextToken();
                storage.setMaxSize(this.expr());
            }
            else if (this.lexer.token() == Token.PCTINCREASE) {
                this.lexer.nextToken();
                storage.setPctIncrease(this.expr());
            }
            else if (this.lexer.identifierEquals("FREELISTS")) {
                this.lexer.nextToken();
                storage.setFreeLists(this.expr());
            }
            else if (this.lexer.identifierEquals("FREELIST")) {
                this.lexer.nextToken();
                this.acceptIdentifier("GROUPS");
                storage.setFreeListGroups(this.expr());
            }
            else if (this.lexer.identifierEquals("BUFFER_POOL")) {
                this.lexer.nextToken();
                storage.setBufferPool(this.expr());
            }
            else if (this.lexer.identifierEquals("OBJNO")) {
                this.lexer.nextToken();
                storage.setObjno(this.expr());
            }
            else if (this.lexer.token() == Token.FLASH_CACHE) {
                this.lexer.nextToken();
                OracleStorageClause.FlashCacheType flashCacheType;
                if (this.lexer.identifierEquals("KEEP")) {
                    flashCacheType = OracleStorageClause.FlashCacheType.KEEP;
                    this.lexer.nextToken();
                }
                else if (this.lexer.token() == Token.NONE) {
                    flashCacheType = OracleStorageClause.FlashCacheType.NONE;
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.DEFAULT);
                    flashCacheType = OracleStorageClause.FlashCacheType.DEFAULT;
                }
                storage.setFlashCache(flashCacheType);
            }
            else {
                if (this.lexer.token() != Token.CELL_FLASH_CACHE) {
                    break;
                }
                this.lexer.nextToken();
                OracleStorageClause.FlashCacheType flashCacheType;
                if (this.lexer.identifierEquals("KEEP")) {
                    flashCacheType = OracleStorageClause.FlashCacheType.KEEP;
                    this.lexer.nextToken();
                }
                else if (this.lexer.token() == Token.NONE) {
                    flashCacheType = OracleStorageClause.FlashCacheType.NONE;
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.DEFAULT);
                    flashCacheType = OracleStorageClause.FlashCacheType.DEFAULT;
                }
                storage.setCellFlashCache(flashCacheType);
            }
        }
        this.accept(Token.RPAREN);
        return storage;
    }
    
    @Override
    public SQLUnique parseUnique() {
        this.accept(Token.UNIQUE);
        final OracleUnique unique = new OracleUnique();
        this.accept(Token.LPAREN);
        this.orderBy(unique.getColumns(), unique);
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.USING) {
            final OracleUsingIndexClause using = this.parseUsingIndex();
            unique.setUsing(using);
        }
        return unique;
    }
    
    @Override
    public OracleConstraint parseConstaint() {
        final OracleConstraint constraint = (OracleConstraint)super.parseConstaint();
        while (true) {
            if (this.lexer.token() == Token.EXCEPTIONS) {
                this.lexer.nextToken();
                this.accept(Token.INTO);
                final SQLName exceptionsInto = this.name();
                constraint.setExceptionsInto(exceptionsInto);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                constraint.setEnable(false);
            }
            else if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                constraint.setEnable(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.VALIDATE)) {
                this.lexer.nextToken();
                constraint.setValidate(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.NOVALIDATE)) {
                this.lexer.nextToken();
                constraint.setValidate(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.INITIALLY) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.IMMEDIATE) {
                    this.lexer.nextToken();
                    constraint.setInitially(OracleConstraint.Initially.IMMEDIATE);
                }
                else {
                    this.accept(Token.DEFERRED);
                    constraint.setInitially(OracleConstraint.Initially.DEFERRED);
                }
            }
            else if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                if (!this.lexer.identifierEquals(FnvHash.Constants.DEFERRABLE)) {
                    throw new ParserException("TODO " + this.lexer.info());
                }
                this.lexer.nextToken();
                constraint.setDeferrable(false);
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.DEFERRABLE)) {
                    if (this.lexer.token() == Token.USING) {
                        final OracleUsingIndexClause using = this.parseUsingIndex();
                        constraint.setUsing(using);
                    }
                    return constraint;
                }
                this.lexer.nextToken();
                constraint.setDeferrable(true);
            }
        }
    }
    
    @Override
    protected OracleForeignKey createForeignKey() {
        return new OracleForeignKey();
    }
    
    @Override
    protected SQLCheck createCheck() {
        return new OracleCheck();
    }
    
    @Override
    protected SQLPartition parsePartition() {
        this.accept(Token.PARTITION);
        final SQLPartition partition = new SQLPartition();
        partition.setName(this.name());
        final SQLPartitionValue values = this.parsePartitionValues();
        if (values != null) {
            this.parseSegmentAttributes(values);
        }
        if (values != null) {
            partition.setValues(values);
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                final SQLSubPartition subPartition = this.parseSubPartition();
                this.parseSegmentAttributes(subPartition);
                partition.addSubPartition(subPartition);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITIONS)) {
            this.lexer.nextToken();
            final SQLExpr subPartitionsCount = this.primary();
            partition.setSubPartitionsCount(subPartitionsCount);
        }
        while (true) {
            this.parseSegmentAttributes(partition);
            if (this.lexer.token() == Token.LOB) {
                final OracleLobStorageClause lobStorage = this.parseLobStorage();
                partition.setLobStorage(lobStorage);
            }
            else {
                if (this.lexer.token() != Token.SEGMENT && !this.lexer.identifierEquals("SEGMENT")) {
                    break;
                }
                this.lexer.nextToken();
                this.accept(Token.CREATION);
                if (this.lexer.token() == Token.IMMEDIATE) {
                    this.lexer.nextToken();
                    partition.setSegmentCreationImmediate(true);
                }
                else {
                    if (this.lexer.token() != Token.DEFERRED) {
                        continue;
                    }
                    this.lexer.nextToken();
                    partition.setSegmentCreationDeferred(true);
                }
            }
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                final SQLSubPartition subPartition = this.parseSubPartition();
                this.parseSegmentAttributes(subPartition);
                partition.addSubPartition(subPartition);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        return partition;
    }
    
    @Override
    protected SQLPartitionBy parsePartitionBy() {
        this.lexer.nextToken();
        this.accept(Token.BY);
        if (this.lexer.identifierEquals("RANGE")) {
            return this.partitionByRange();
        }
        if (this.lexer.identifierEquals("HASH")) {
            final SQLPartitionByHash partitionByHash = this.partitionByHash();
            this.partitionClauseRest(partitionByHash);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                while (true) {
                    final SQLPartition partition = this.parsePartition();
                    partitionByHash.addPartition(partition);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                if (this.lexer.token() != Token.RPAREN) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                this.lexer.nextToken();
            }
            return partitionByHash;
        }
        if (this.lexer.identifierEquals("LIST")) {
            final SQLPartitionByList partitionByList = this.partitionByList();
            this.partitionClauseRest(partitionByList);
            return partitionByList;
        }
        throw new ParserException("TODO : " + this.lexer.info());
    }
    
    protected SQLPartitionByList partitionByList() {
        this.acceptIdentifier("LIST");
        final SQLPartitionByList partitionByList = new SQLPartitionByList();
        this.accept(Token.LPAREN);
        partitionByList.addColumn(this.expr());
        this.accept(Token.RPAREN);
        this.parsePartitionByRest(partitionByList);
        return partitionByList;
    }
    
    protected SQLSubPartition parseSubPartition() {
        this.acceptIdentifier("SUBPARTITION");
        final SQLSubPartition subPartition = new SQLSubPartition();
        final SQLName name = this.name();
        subPartition.setName(name);
        final SQLPartitionValue values = this.parsePartitionValues();
        if (values != null) {
            subPartition.setValues(values);
        }
        if (this.lexer.token() == Token.TABLESPACE) {
            this.lexer.nextToken();
            subPartition.setTableSpace(this.name());
        }
        return subPartition;
    }
    
    public void parseSegmentAttributes(final OracleSegmentAttributes attributes) {
        while (true) {
            if (this.lexer.token() == Token.TABLESPACE) {
                this.lexer.nextToken();
                attributes.setTablespace(this.name());
            }
            else if (this.lexer.token() == Token.NOCOMPRESS || this.lexer.identifierEquals("NOCOMPRESS")) {
                this.lexer.nextToken();
                attributes.setCompress(Boolean.FALSE);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.COMPRESS)) {
                this.lexer.nextToken();
                attributes.setCompress(Boolean.TRUE);
                if (this.lexer.token() == Token.LITERAL_INT) {
                    final int compressLevel = this.parseIntValue();
                    attributes.setCompressLevel(compressLevel);
                }
                else if (this.lexer.identifierEquals("BASIC")) {
                    this.lexer.nextToken();
                }
                else {
                    if (this.lexer.token() != Token.FOR) {
                        continue;
                    }
                    this.lexer.nextToken();
                    if (!this.lexer.identifierEquals("OLTP")) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    attributes.setCompressForOltp(true);
                }
            }
            else if (this.lexer.identifierEquals("NOCOMPRESS")) {
                this.lexer.nextToken();
                attributes.setCompress(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.LOGGING || this.lexer.identifierEquals("LOGGING")) {
                this.lexer.nextToken();
                attributes.setLogging(Boolean.TRUE);
            }
            else if (this.lexer.identifierEquals("NOLOGGING")) {
                this.lexer.nextToken();
                attributes.setLogging(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.INITRANS) {
                this.lexer.nextToken();
                attributes.setInitrans(this.parseIntValue());
            }
            else if (this.lexer.token() == Token.MAXTRANS) {
                this.lexer.nextToken();
                attributes.setMaxtrans(this.parseIntValue());
            }
            else if (this.lexer.token() == Token.PCTINCREASE) {
                this.lexer.nextToken();
                attributes.setPctincrease(this.parseIntValue());
            }
            else if (this.lexer.token() == Token.PCTFREE) {
                this.lexer.nextToken();
                attributes.setPctfree(this.parseIntValue());
            }
            else if (this.lexer.token() == Token.STORAGE || this.lexer.identifierEquals("STORAGE")) {
                final OracleStorageClause storage = this.parseStorage();
                attributes.setStorage(storage);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PCTUSED)) {
                this.lexer.nextToken();
                attributes.setPctused(this.parseIntValue());
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.USAGE)) {
                this.lexer.nextToken();
                this.acceptIdentifier("QUEUE");
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.OPAQUE)) {
                    return;
                }
                this.parseOpaque();
            }
        }
    }
    
    protected SQLPartitionByRange partitionByRange() {
        this.acceptIdentifier("RANGE");
        this.accept(Token.LPAREN);
        final SQLPartitionByRange clause = new SQLPartitionByRange();
        while (true) {
            final SQLName column = this.name();
            clause.addColumn(column);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
        if (this.lexer.token() == Token.INTERVAL) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            clause.setInterval(this.expr());
            this.accept(Token.RPAREN);
        }
        this.parsePartitionByRest(clause);
        return clause;
    }
    
    protected void parsePartitionByRest(final SQLPartitionBy clause) {
        if (this.lexer.token() == Token.STORE) {
            this.lexer.nextToken();
            this.accept(Token.IN);
            this.accept(Token.LPAREN);
            while (true) {
                final SQLName tablespace = this.name();
                clause.getStoreIn().add(tablespace);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
            final SQLSubPartitionBy subPartitionBy = this.subPartitionBy();
            clause.setSubPartitionBy(subPartitionBy);
            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITIONS)) {
                this.lexer.nextToken();
                final Number intValue = this.lexer.integerValue();
                final SQLNumberExpr numExpr = new SQLNumberExpr(intValue);
                subPartitionBy.setSubPartitionsCount(numExpr);
                this.lexer.nextToken();
            }
        }
        this.accept(Token.LPAREN);
        while (true) {
            final SQLPartition partition = this.parsePartition();
            clause.addPartition(partition);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        this.accept(Token.RPAREN);
    }
    
    protected SQLSubPartitionBy subPartitionBy() {
        this.lexer.nextToken();
        this.accept(Token.BY);
        if (this.lexer.identifierEquals(FnvHash.Constants.HASH)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            final SQLSubPartitionByHash byHash = new SQLSubPartitionByHash();
            final SQLExpr expr = this.expr();
            byHash.setExpr(expr);
            this.accept(Token.RPAREN);
            return byHash;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LIST)) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            final SQLSubPartitionByList byList = new SQLSubPartitionByList();
            final SQLName column = this.name();
            byList.setColumn(column);
            this.accept(Token.RPAREN);
            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
                this.lexer.nextToken();
                this.acceptIdentifier("TEMPLATE");
                this.accept(Token.LPAREN);
                while (true) {
                    final SQLSubPartition subPartition = this.parseSubPartition();
                    subPartition.setParent(byList);
                    byList.getSubPartitionTemplate().add(subPartition);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITIONS)) {
                this.lexer.nextToken();
                final Number intValue = this.lexer.integerValue();
                final SQLNumberExpr numExpr = new SQLNumberExpr(intValue);
                byList.setSubPartitionsCount(numExpr);
                this.lexer.nextToken();
            }
            return byList;
        }
        throw new ParserException("TODO : " + this.lexer.info());
    }
    
    protected void partitionClauseRest(final SQLPartitionBy clause) {
        if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
            this.lexer.nextToken();
            final SQLIntegerExpr countExpr = this.integerExpr();
            clause.setPartitionsCount(countExpr);
        }
        if (this.lexer.token() == Token.STORE) {
            this.lexer.nextToken();
            this.accept(Token.IN);
            this.accept(Token.LPAREN);
            this.names(clause.getStoreIn(), clause);
            this.accept(Token.RPAREN);
        }
    }
    
    protected SQLPartitionByHash partitionByHash() {
        this.acceptIdentifier("HASH");
        final SQLPartitionByHash partitionByHash = new SQLPartitionByHash();
        if (this.lexer.token() == Token.KEY) {
            this.lexer.nextToken();
            partitionByHash.setKey(true);
        }
        this.accept(Token.LPAREN);
        this.exprList(partitionByHash.getColumns(), partitionByHash);
        this.accept(Token.RPAREN);
        return partitionByHash;
    }
    
    static {
        final String[] strings = { "AVG", "CORR", "COVAR_POP", "COVAR_SAMP", "COUNT", "CUME_DIST", "DENSE_RANK", "FIRST", "FIRST_VALUE", "LAG", "LAST", "LAST_VALUE", "LISTAGG", "LEAD", "MAX", "MIN", "NTILE", "PERCENT_RANK", "PERCENTILE_CONT", "PERCENTILE_DISC", "RANK", "RATIO_TO_REPORT", "REGR_SLOPE", "REGR_INTERCEPT", "REGR_COUNT", "REGR_R2", "REGR_AVGX", "REGR_AVGY", "REGR_SXX", "REGR_SYY", "REGR_SXY", "ROW_NUMBER", "STDDEV", "STDDEV_POP", "STDDEV_SAMP", "SUM", "VAR_POP", "VAR_SAMP", "VARIANCE", "WM_CONCAT" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[OracleExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(OracleExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            OracleExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
