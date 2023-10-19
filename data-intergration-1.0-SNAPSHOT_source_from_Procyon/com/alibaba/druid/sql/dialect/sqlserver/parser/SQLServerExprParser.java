// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.sqlserver.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.expr.SQLNullExpr;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerOutput;
import com.alibaba.druid.sql.dialect.sqlserver.ast.SQLServerTop;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.dialect.sqlserver.ast.expr.SQLServerObjectReferenceExpr;
import com.alibaba.druid.sql.ast.expr.SQLSequenceExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class SQLServerExprParser extends SQLExprParser
{
    public static final String[] AGGREGATE_FUNCTIONS;
    public static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public SQLServerExprParser(final Lexer lexer) {
        super(lexer);
        this.dbType = DbType.sqlserver;
        this.aggregateFunctions = SQLServerExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = SQLServerExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    public SQLServerExprParser(final String sql) {
        this(new SQLServerLexer(sql));
        this.lexer.nextToken();
        this.dbType = DbType.sqlserver;
    }
    
    public SQLServerExprParser(final String sql, final SQLParserFeature... features) {
        this(new SQLServerLexer(sql, features));
        this.lexer.nextToken();
        this.dbType = DbType.sqlserver;
    }
    
    @Override
    public SQLExpr primary() {
        if (this.lexer.token() == Token.LBRACKET) {
            this.lexer.nextToken();
            final SQLExpr name = this.name();
            this.accept(Token.RBRACKET);
            return this.primaryRest(name);
        }
        return super.primary();
    }
    
    @Override
    public SQLServerSelectParser createSelectParser() {
        return new SQLServerSelectParser(this);
    }
    
    @Override
    public SQLExpr primaryRest(SQLExpr expr) {
        final Token token = this.lexer.token();
        if (token == Token.DOTDOT) {
            expr = this.nameRest((SQLName)expr);
        }
        else if (this.lexer.identifierEquals(FnvHash.Constants.VALUE) && expr instanceof SQLIdentifierExpr) {
            final SQLIdentifierExpr identExpr = (SQLIdentifierExpr)expr;
            if (identExpr.nameHashCode64() == FnvHash.Constants.NEXT) {
                this.lexer.nextToken();
                this.accept(Token.FOR);
                final SQLName name = this.name();
                final SQLSequenceExpr seq = new SQLSequenceExpr();
                seq.setSequence(name);
                seq.setFunction(SQLSequenceExpr.Function.NextVal);
                expr = seq;
            }
        }
        return super.primaryRest(expr);
    }
    
    @Override
    protected SQLExpr dotRest(SQLExpr expr) {
        boolean backet = false;
        if (this.lexer.token() == Token.LBRACKET) {
            this.lexer.nextToken();
            backet = true;
        }
        expr = super.dotRest(expr);
        if (backet) {
            this.accept(Token.RBRACKET);
        }
        return expr;
    }
    
    @Override
    public SQLName nameRest(SQLName expr) {
        if (this.lexer.token() == Token.DOTDOT) {
            this.lexer.nextToken();
            boolean backet = false;
            if (this.lexer.token() == Token.LBRACKET) {
                this.lexer.nextToken();
                backet = true;
            }
            final String text = this.lexer.stringVal();
            this.lexer.nextToken();
            if (backet) {
                this.accept(Token.RBRACKET);
            }
            final SQLServerObjectReferenceExpr owner = new SQLServerObjectReferenceExpr(expr);
            expr = new SQLPropertyExpr(owner, text);
        }
        return super.nameRest(expr);
    }
    
    public SQLServerTop parseTop() {
        if (this.lexer.token() == Token.TOP) {
            final SQLServerTop top = new SQLServerTop();
            this.lexer.nextToken();
            boolean paren = false;
            if (this.lexer.token() == Token.LPAREN) {
                paren = true;
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.LITERAL_INT) {
                top.setExpr(this.lexer.integerValue().intValue());
                this.lexer.nextToken();
            }
            else {
                top.setExpr(this.primary());
            }
            if (paren) {
                this.accept(Token.RPAREN);
            }
            if (this.lexer.token() == Token.PERCENT) {
                this.lexer.nextToken();
                top.setPercent(true);
            }
            return top;
        }
        return null;
    }
    
    protected SQLServerOutput parserOutput() {
        if (this.lexer.identifierEquals("OUTPUT")) {
            this.lexer.nextToken();
            final SQLServerOutput output = new SQLServerOutput();
            final List<SQLSelectItem> selectList = output.getSelectList();
            while (true) {
                final SQLSelectItem selectItem = this.parseSelectItem();
                selectList.add(selectItem);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.INTO) {
                this.lexer.nextToken();
                output.setInto(new SQLExprTableSource(this.name()));
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    this.exprList(output.getColumns(), output);
                    this.accept(Token.RPAREN);
                }
            }
            return output;
        }
        return null;
    }
    
    @Override
    public SQLSelectItem parseSelectItem() {
        SQLExpr expr;
        if (this.lexer.token() == Token.IDENTIFIER) {
            expr = new SQLIdentifierExpr(this.lexer.stringVal());
            this.lexer.nextTokenComma();
            if (this.lexer.token() != Token.COMMA) {
                expr = this.primaryRest(expr);
                expr = this.exprRest(expr);
            }
        }
        else {
            expr = this.expr();
        }
        final String alias = this.as();
        return new SQLSelectItem(expr, alias);
    }
    
    @Override
    public SQLColumnDefinition createColumnDefinition() {
        final SQLColumnDefinition column = new SQLColumnDefinition();
        column.setDbType(this.dbType);
        return column;
    }
    
    @Override
    public SQLColumnDefinition parseColumnRest(final SQLColumnDefinition column) {
        if (this.lexer.token() == Token.IDENTITY) {
            this.lexer.nextToken();
            final SQLColumnDefinition.Identity identity = new SQLColumnDefinition.Identity();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                final SQLIntegerExpr seed = (SQLIntegerExpr)this.primary();
                this.accept(Token.COMMA);
                final SQLIntegerExpr increment = (SQLIntegerExpr)this.primary();
                this.accept(Token.RPAREN);
                identity.setSeed((Integer)seed.getNumber());
                identity.setIncrement((Integer)increment.getNumber());
            }
            if (this.lexer.token() == Token.NOT) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NULL) {
                    this.lexer.nextToken();
                    column.setDefaultExpr(new SQLNullExpr());
                }
                else {
                    this.accept(Token.FOR);
                    this.acceptIdentifier("REPLICATION ");
                    identity.setNotForReplication(true);
                }
            }
            column.setIdentity(identity);
        }
        return super.parseColumnRest(column);
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "FIRST_VALUE", "MAX", "MIN", "ROW_NUMBER", "STDDEV", "SUM" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[SQLServerExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(SQLServerExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            SQLServerExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
