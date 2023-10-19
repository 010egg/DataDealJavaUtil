// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.h2.parser;

import java.util.Arrays;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLDefaultExpr;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;

public class H2ExprParser extends SQLExprParser
{
    private static final String[] AGGREGATE_FUNCTIONS;
    private static final long[] AGGREGATE_FUNCTIONS_CODES;
    
    public H2ExprParser(final String sql) {
        this(new H2Lexer(sql));
        this.lexer.nextToken();
    }
    
    public H2ExprParser(final String sql, final SQLParserFeature... features) {
        this(new H2Lexer(sql, features));
        this.lexer.nextToken();
    }
    
    public H2ExprParser(final Lexer lexer) {
        super(lexer);
        this.dbType = lexer.getDbType();
        this.aggregateFunctions = H2ExprParser.AGGREGATE_FUNCTIONS;
        this.aggregateFunctionHashCodes = H2ExprParser.AGGREGATE_FUNCTIONS_CODES;
    }
    
    @Override
    public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
        column = super.parseColumnRest(column);
        if (this.lexer.identifierEquals(FnvHash.Constants.GENERATED)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.BY) {
                this.lexer.nextToken();
                this.accept(Token.DEFAULT);
                column.setGeneratedAlawsAs(new SQLDefaultExpr());
            }
            else {
                this.acceptIdentifier("ALWAYS");
                column.setGeneratedAlawsAs(new SQLIdentifierExpr("ALWAYS"));
            }
            this.accept(Token.AS);
            this.acceptIdentifier("IDENTITY");
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
            column.setIdentity(identity);
        }
        return column;
    }
    
    static {
        final String[] strings = { "AVG", "COUNT", "MAX", "MIN", "STDDEV", "SUM", "ROW_NUMBER", "ROWNUMBER" };
        AGGREGATE_FUNCTIONS_CODES = FnvHash.fnv1a_64_lower(strings, true);
        AGGREGATE_FUNCTIONS = new String[H2ExprParser.AGGREGATE_FUNCTIONS_CODES.length];
        for (final String str : strings) {
            final long hash = FnvHash.fnv1a_64_lower(str);
            final int index = Arrays.binarySearch(H2ExprParser.AGGREGATE_FUNCTIONS_CODES, hash);
            H2ExprParser.AGGREGATE_FUNCTIONS[index] = str;
        }
    }
}
