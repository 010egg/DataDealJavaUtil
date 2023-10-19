// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.List;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsNewExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsTransformExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLArrayExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsUDTFSQLSelectItem;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLTimestampExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateExpr;
import com.alibaba.druid.sql.ast.expr.SQLDateTimeExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class OdpsExprParser extends SQLExprParser
{
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    static final long GSONBUILDER;
    
    public OdpsExprParser(final Lexer lexer) {
        super(lexer, DbType.odps);
        this.aggregateFunctions = OdpsExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = OdpsExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    public OdpsExprParser(final String sql, final SQLParserFeature... features) {
        this(new OdpsLexer(sql, features));
        this.lexer.nextToken();
    }
    
    public OdpsExprParser(final String sql, final boolean skipComments, final boolean keepComments) {
        this(new OdpsLexer(sql, skipComments, keepComments));
        this.lexer.nextToken();
    }
    
    @Override
    protected SQLExpr parseAliasExpr(final String alias) {
        final String chars = alias.substring(1, alias.length() - 1);
        return new SQLCharExpr(chars);
    }
    
    @Override
    public SQLSelectItem parseSelectItem() {
        SQLExpr expr;
        if (this.lexer.token() == Token.IDENTIFIER) {
            final String stringVal = this.lexer.stringVal();
            final long hash_lower = this.lexer.hash_lower();
            this.lexer.nextTokenComma();
            if (FnvHash.Constants.DATETIME == hash_lower && this.lexer.stringVal().charAt(0) != '`' && (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS)) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLDateTimeExpr ts = (SQLDateTimeExpr)(expr = new SQLDateTimeExpr(literal));
            }
            else if (FnvHash.Constants.DATE == hash_lower && this.lexer.stringVal().charAt(0) != '`' && (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS)) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLDateExpr d = (SQLDateExpr)(expr = new SQLDateExpr(literal));
            }
            else if (FnvHash.Constants.TIMESTAMP == hash_lower && this.lexer.stringVal().charAt(0) != '`' && (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS)) {
                final String literal = this.lexer.stringVal();
                this.lexer.nextToken();
                final SQLTimestampExpr ts2 = (SQLTimestampExpr)(expr = new SQLTimestampExpr(literal));
            }
            else {
                expr = new SQLIdentifierExpr(stringVal);
                if (this.lexer.token() != Token.COMMA) {
                    expr = this.primaryRest(expr);
                    expr = this.exprRest(expr);
                }
            }
        }
        else {
            expr = this.expr();
        }
        String alias = null;
        if (this.lexer.token() == Token.AS) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                final OdpsUDTFSQLSelectItem selectItem = new OdpsUDTFSQLSelectItem();
                selectItem.setExpr(expr);
                while (true) {
                    alias = this.lexer.stringVal();
                    this.lexer.nextToken();
                    selectItem.getAliasList().add(alias);
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                return selectItem;
            }
            alias = this.alias();
        }
        else {
            alias = this.as();
        }
        final SQLSelectItem item = new SQLSelectItem(expr, alias);
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            item.addAfterComment(this.lexer.readAndResetComments());
        }
        return item;
    }
    
    @Override
    public SQLExpr primaryRest(SQLExpr expr) {
        if (this.lexer.token() == Token.COLON) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LITERAL_INT && expr instanceof SQLPropertyExpr) {
                final SQLPropertyExpr propertyExpr = (SQLPropertyExpr)expr;
                final Number integerValue = this.lexer.integerValue();
                this.lexer.nextToken();
                propertyExpr.setName(propertyExpr.getName() + ':' + integerValue.intValue());
                return propertyExpr;
            }
            expr = this.dotRest(expr);
            return expr;
        }
        else {
            if (this.lexer.token() == Token.LBRACKET) {
                final SQLArrayExpr array = new SQLArrayExpr();
                array.setExpr(expr);
                this.lexer.nextToken();
                this.exprList(array.getValues(), array);
                this.accept(Token.RBRACKET);
                return this.primaryRest(array);
            }
            if ((this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS) && expr instanceof SQLCharExpr) {
                SQLCharExpr charExpr = new SQLCharExpr(this.lexer.stringVal());
                this.lexer.nextTokenValue();
                final SQLMethodInvokeExpr concat = new SQLMethodInvokeExpr("concat", null, new SQLExpr[] { expr, charExpr });
                while (this.lexer.token() == Token.LITERAL_CHARS || this.lexer.token() == Token.LITERAL_ALIAS) {
                    charExpr = new SQLCharExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                    concat.addArgument(charExpr);
                }
                expr = concat;
            }
            if (this.lexer.token() == Token.LPAREN && expr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)expr).nameHashCode64() == FnvHash.Constants.TRANSFORM) {
                final OdpsTransformExpr transformExpr = new OdpsTransformExpr();
                this.lexer.nextToken();
                this.exprList(transformExpr.getInputColumns(), transformExpr);
                this.accept(Token.RPAREN);
                if (this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
                    final SQLExternalRecordFormat recordFormat = this.parseRowFormat();
                    transformExpr.setInputRowFormat(recordFormat);
                }
                if (this.lexer.token() == Token.USING || this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                    this.lexer.nextToken();
                    transformExpr.setUsing(this.expr());
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.RESOURCES)) {
                    this.lexer.nextToken();
                    this.exprList(transformExpr.getResources(), transformExpr);
                }
                if (this.lexer.token() == Token.AS) {
                    this.lexer.nextToken();
                    final List<SQLColumnDefinition> outputColumns = transformExpr.getOutputColumns();
                    if (this.lexer.token() == Token.LPAREN) {
                        this.lexer.nextToken();
                        while (true) {
                            final SQLColumnDefinition column = this.parseColumn();
                            outputColumns.add(column);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                    }
                    else {
                        final SQLColumnDefinition column = new SQLColumnDefinition();
                        column.setName(this.name());
                        outputColumns.add(column);
                    }
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
                    final SQLExternalRecordFormat recordFormat = this.parseRowFormat();
                    transformExpr.setOutputRowFormat(recordFormat);
                }
                return transformExpr;
            }
            if (expr instanceof SQLIdentifierExpr && ((SQLIdentifierExpr)expr).nameHashCode64() == FnvHash.Constants.NEW) {
                final SQLIdentifierExpr ident = (SQLIdentifierExpr)expr;
                final OdpsNewExpr newExpr = new OdpsNewExpr();
                if (this.lexer.token() == Token.IDENTIFIER) {
                    final Lexer.SavePoint mark = this.lexer.mark();
                    String methodName = this.lexer.stringVal();
                    this.lexer.nextToken();
                    switch (this.lexer.token()) {
                        case ON:
                        case WHERE:
                        case GROUP:
                        case ORDER:
                        case INNER:
                        case JOIN:
                        case FULL:
                        case OUTER:
                        case LEFT:
                        case RIGHT:
                        case LATERAL:
                        case FROM:
                        case COMMA:
                        case RPAREN: {
                            return ident;
                        }
                        default: {
                            while (this.lexer.token() == Token.DOT) {
                                this.lexer.nextToken();
                                methodName = methodName + '.' + this.lexer.stringVal();
                                this.lexer.nextToken();
                            }
                            newExpr.setMethodName(methodName);
                            Label_0915: {
                                if (this.lexer.token() == Token.LT) {
                                    this.lexer.nextToken();
                                    while (true) {
                                        while (this.lexer.token() != Token.GT) {
                                            final SQLDataType paramType = this.parseDataType(false);
                                            paramType.setParent(newExpr);
                                            newExpr.getTypeParameters().add(paramType);
                                            if (this.lexer.token() != Token.COMMA) {
                                                this.accept(Token.GT);
                                                break Label_0915;
                                            }
                                            this.lexer.nextToken();
                                        }
                                        continue;
                                    }
                                }
                            }
                            if (this.lexer.token() == Token.LBRACKET) {
                                this.lexer.nextToken();
                                this.exprList(newExpr.getArguments(), newExpr);
                                this.accept(Token.RBRACKET);
                                if (this.lexer.token() == Token.LBRACKET) {
                                    this.lexer.nextToken();
                                    this.accept(Token.RBRACKET);
                                }
                                newExpr.setArray(true);
                                Label_1075: {
                                    if (this.lexer.token() == Token.LBRACE) {
                                        this.lexer.nextToken();
                                        while (true) {
                                            while (this.lexer.token() != Token.RPAREN) {
                                                final SQLExpr item = this.expr();
                                                newExpr.getInitValues().add(item);
                                                item.setParent(newExpr);
                                                if (this.lexer.token() != Token.COMMA) {
                                                    this.accept(Token.RBRACE);
                                                    break Label_1075;
                                                }
                                                this.lexer.nextToken();
                                            }
                                            continue;
                                        }
                                    }
                                }
                                if (this.lexer.token() == Token.LBRACKET) {
                                    expr = this.primaryRest(newExpr);
                                }
                                else {
                                    expr = newExpr;
                                }
                            }
                            else {
                                this.accept(Token.LPAREN);
                                this.exprList(newExpr.getArguments(), newExpr);
                                this.accept(Token.RPAREN);
                                expr = newExpr;
                            }
                            break;
                        }
                    }
                }
                else if (this.lexer.identifierEquals("java") || this.lexer.identifierEquals("com")) {
                    final SQLName name = this.name();
                    String strName = ident.getName() + ' ' + name.toString();
                    if (this.lexer.token() == Token.LT) {
                        this.lexer.nextToken();
                        int i = 0;
                        while (this.lexer.token() != Token.GT) {
                            if (i != 0) {
                                strName += ", ";
                            }
                            final SQLName arg = this.name();
                            strName += arg.toString();
                            ++i;
                        }
                        this.lexer.nextToken();
                    }
                    ident.setName(strName);
                }
            }
            if (expr == null) {
                return null;
            }
            return super.primaryRest(expr);
        }
    }
    
    @Override
    public SQLExpr relationalRest(final SQLExpr expr) {
        if (this.lexer.identifierEquals("REGEXP")) {
            this.lexer.nextToken();
            SQLExpr rightExp = this.bitOr();
            rightExp = this.relationalRest(rightExp);
            return new SQLBinaryOpExpr(expr, SQLBinaryOperator.RegExp, rightExp, this.dbType);
        }
        return super.relationalRest(expr);
    }
    
    @Override
    public OdpsSelectParser createSelectParser() {
        return new OdpsSelectParser(this);
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "LAG", "LEAD", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "WM_CONCAT", "STRAGG", "COLLECT_LIST", "COLLECT_SET" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[OdpsExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(OdpsExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            OdpsExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
        GSONBUILDER = FnvHash.fnv1a_64_lower("GSONBUILDER");
    }
}
