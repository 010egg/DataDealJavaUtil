// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.expr.SQLBinaryExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGLineSegmentsExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGCircleExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGPolygonExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGCidrExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGInetExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGMacAddrExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGBoxExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGPointExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGDateField;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGExtractExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.dialect.postgresql.ast.expr.PGTypeCastExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalUnit;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntervalExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuesExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryExpr;
import com.alibaba.druid.sql.ast.expr.SQLUnaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLVariantRefExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLArrayDataType;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class PGExprParser extends SQLExprParser
{
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public PGExprParser(final String sql) {
        this(new PGLexer(sql, new SQLParserFeature[0]));
        this.lexer.nextToken();
        this.dbType = DbType.postgresql;
    }
    
    public PGExprParser(final String sql, final SQLParserFeature... features) {
        this(new PGLexer(sql, new SQLParserFeature[0]));
        this.lexer.nextToken();
        this.dbType = DbType.postgresql;
    }
    
    public PGExprParser(final Lexer lexer) {
        super(lexer);
        this.aggregateFunctions = PGExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = PGExprParser.AGGREGATE_FUNCTIONS_CODES;
        this.dbType = DbType.postgresql;
    }
    
    @Override
    public SQLDataType parseDataType() {
        if (this.lexer.token() == Token.TYPE) {
            this.lexer.nextToken();
        }
        return super.parseDataType();
    }
    
    @Override
    protected SQLDataType parseDataTypeRest(SQLDataType dataType) {
        dataType = super.parseDataTypeRest(dataType);
        if (this.lexer.token() == Token.LBRACKET) {
            this.lexer.nextToken();
            this.accept(Token.RBRACKET);
            dataType = new SQLArrayDataType(dataType);
        }
        return dataType;
    }
    
    @Override
    public PGSelectParser createSelectParser() {
        return new PGSelectParser(this);
    }
    
    @Override
    public SQLExpr primary() {
        if (this.lexer.token() == Token.ARRAY) {
            final String ident = this.lexer.stringVal();
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LPAREN) {
                final SQLIdentifierExpr array = new SQLIdentifierExpr(ident);
                return this.methodRest(array, true);
            }
            final SQLArrayExpr array2 = new SQLArrayExpr();
            array2.setExpr(new SQLIdentifierExpr(ident));
            this.accept(Token.LBRACKET);
            this.exprList(array2.getValues(), array2);
            this.accept(Token.RBRACKET);
            return this.primaryRest(array2);
        }
        else if (this.lexer.token() == Token.POUND) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LBRACE) {
                this.lexer.nextToken();
                final String varName = this.lexer.stringVal();
                this.lexer.nextToken();
                this.accept(Token.RBRACE);
                final SQLVariantRefExpr expr = new SQLVariantRefExpr("#{" + varName + "}");
                return this.primaryRest(expr);
            }
            final SQLExpr value = this.primary();
            final SQLUnaryExpr expr2 = new SQLUnaryExpr(SQLUnaryOperator.Pound, value);
            return this.primaryRest(expr2);
        }
        else {
            if (this.lexer.token() == Token.VALUES) {
                this.lexer.nextToken();
                final SQLValuesExpr values = new SQLValuesExpr();
                while (true) {
                    this.accept(Token.LPAREN);
                    final SQLListExpr listExpr = new SQLListExpr();
                    this.exprList(listExpr.getItems(), listExpr);
                    this.accept(Token.RPAREN);
                    listExpr.setParent(values);
                    values.getValues().add(listExpr);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                return values;
            }
            if (this.lexer.token() == Token.WITH) {
                final SQLQueryExpr queryExpr = new SQLQueryExpr(this.createSelectParser().select());
                return queryExpr;
            }
            return super.primary();
        }
    }
    
    @Override
    protected SQLExpr parseInterval() {
        this.accept(Token.INTERVAL);
        final SQLIntervalExpr intervalExpr = new SQLIntervalExpr();
        if (this.lexer.token() != Token.LITERAL_CHARS) {
            return new SQLIdentifierExpr("INTERVAL");
        }
        intervalExpr.setValue(new SQLCharExpr(this.lexer.stringVal()));
        this.lexer.nextToken();
        if (this.lexer.identifierEquals(FnvHash.Constants.DAY)) {
            this.lexer.nextToken();
            intervalExpr.setUnit(SQLIntervalUnit.DAY);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MONTH)) {
            this.lexer.nextToken();
            intervalExpr.setUnit(SQLIntervalUnit.MONTH);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.YEAR)) {
            this.lexer.nextToken();
            intervalExpr.setUnit(SQLIntervalUnit.YEAR);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.HOUR)) {
            this.lexer.nextToken();
            intervalExpr.setUnit(SQLIntervalUnit.HOUR);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.MINUTE)) {
            this.lexer.nextToken();
            intervalExpr.setUnit(SQLIntervalUnit.MINUTE);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.SECOND)) {
            this.lexer.nextToken();
            intervalExpr.setUnit(SQLIntervalUnit.SECOND);
        }
        return intervalExpr;
    }
    
    @Override
    public SQLExpr primaryRest(SQLExpr expr) {
        if (this.lexer.token() == Token.COLONCOLON) {
            this.lexer.nextToken();
            final SQLDataType dataType = this.parseDataType();
            final PGTypeCastExpr castExpr = new PGTypeCastExpr();
            castExpr.setExpr(expr);
            castExpr.setDataType(dataType);
            return this.primaryRest(castExpr);
        }
        if (this.lexer.token() == Token.LBRACKET) {
            final SQLArrayExpr array = new SQLArrayExpr();
            array.setExpr(expr);
            this.lexer.nextToken();
            this.exprList(array.getValues(), array);
            this.accept(Token.RBRACKET);
            return this.primaryRest(array);
        }
        if (expr.getClass() == SQLIdentifierExpr.class) {
            final SQLIdentifierExpr identifierExpr = (SQLIdentifierExpr)expr;
            final String ident = identifierExpr.getName();
            final long hash = identifierExpr.nameHashCode64();
            if (this.lexer.token() == Token.COMMA || this.lexer.token() == Token.RPAREN) {
                return super.primaryRest(expr);
            }
            if (FnvHash.Constants.TIMESTAMP == hash) {
                if (this.lexer.token() != Token.LITERAL_ALIAS && this.lexer.token() != Token.LITERAL_CHARS && this.lexer.token() != Token.WITH) {
                    return super.primaryRest(new SQLIdentifierExpr(ident));
                }
                final SQLTimestampExpr timestamp = new SQLTimestampExpr();
                if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("TIME");
                    this.acceptIdentifier("ZONE");
                    timestamp.setWithTimeZone(true);
                }
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
            else if (FnvHash.Constants.TIMESTAMPTZ == hash) {
                if (this.lexer.token() != Token.LITERAL_ALIAS && this.lexer.token() != Token.LITERAL_CHARS && this.lexer.token() != Token.WITH) {
                    return super.primaryRest(new SQLIdentifierExpr(ident));
                }
                final SQLTimestampExpr timestamp = new SQLTimestampExpr();
                timestamp.setWithTimeZone(true);
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
            else {
                if (FnvHash.Constants.EXTRACT == hash) {
                    this.accept(Token.LPAREN);
                    final PGExtractExpr extract = new PGExtractExpr();
                    final String fieldName = this.lexer.stringVal();
                    final PGDateField field = PGDateField.valueOf(fieldName.toUpperCase());
                    this.lexer.nextToken();
                    extract.setField(field);
                    this.accept(Token.FROM);
                    final SQLExpr source = this.expr();
                    extract.setSource(source);
                    this.accept(Token.RPAREN);
                    return this.primaryRest(extract);
                }
                if (FnvHash.Constants.POINT == hash) {
                    switch (this.lexer.token()) {
                        case DOT:
                        case EQ:
                        case LTGT:
                        case GT:
                        case GTEQ:
                        case LT:
                        case LTEQ:
                        case SUB:
                        case PLUS:
                        case SUBGT: {
                            break;
                        }
                        default: {
                            final SQLExpr value = this.primary();
                            final PGPointExpr point = new PGPointExpr();
                            point.setValue(value);
                            return this.primaryRest(point);
                        }
                    }
                }
                else {
                    if (FnvHash.Constants.BOX == hash) {
                        final SQLExpr value = this.primary();
                        final PGBoxExpr box = new PGBoxExpr();
                        box.setValue(value);
                        return this.primaryRest(box);
                    }
                    if (FnvHash.Constants.MACADDR == hash) {
                        final SQLExpr value = this.primary();
                        final PGMacAddrExpr macaddr = new PGMacAddrExpr();
                        macaddr.setValue(value);
                        return this.primaryRest(macaddr);
                    }
                    if (FnvHash.Constants.INET == hash) {
                        final SQLExpr value = this.primary();
                        final PGInetExpr inet = new PGInetExpr();
                        inet.setValue(value);
                        return this.primaryRest(inet);
                    }
                    if (FnvHash.Constants.CIDR == hash) {
                        final SQLExpr value = this.primary();
                        final PGCidrExpr cidr = new PGCidrExpr();
                        cidr.setValue(value);
                        return this.primaryRest(cidr);
                    }
                    if (FnvHash.Constants.POLYGON == hash) {
                        final SQLExpr value = this.primary();
                        final PGPolygonExpr polygon = new PGPolygonExpr();
                        polygon.setValue(value);
                        return this.primaryRest(polygon);
                    }
                    if (FnvHash.Constants.CIRCLE == hash) {
                        final SQLExpr value = this.primary();
                        final PGCircleExpr circle = new PGCircleExpr();
                        circle.setValue(value);
                        return this.primaryRest(circle);
                    }
                    if (FnvHash.Constants.LSEG == hash) {
                        final SQLExpr value = this.primary();
                        final PGLineSegmentsExpr lseg = new PGLineSegmentsExpr();
                        lseg.setValue(value);
                        return this.primaryRest(lseg);
                    }
                    if (ident.equalsIgnoreCase("b") && this.lexer.token() == Token.LITERAL_CHARS) {
                        final String charValue = this.lexer.stringVal();
                        this.lexer.nextToken();
                        expr = new SQLBinaryExpr(charValue);
                        return this.primaryRest(expr);
                    }
                }
            }
        }
        return super.primaryRest(expr);
    }
    
    @Override
    protected String alias() {
        String alias = super.alias();
        if (alias != null) {
            return alias;
        }
        switch (this.lexer.token()) {
            case INTERSECT: {
                alias = this.lexer.stringVal();
                this.lexer.nextToken();
                return alias;
            }
            default: {
                return alias;
            }
        }
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "PERCENTILE_CONT", "PERCENTILE_DISC", "RANK", "DENSE_RANK", "PERCENT_RANK", "CUME_DIST" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[PGExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(PGExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            PGExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
