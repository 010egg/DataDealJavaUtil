// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.presto.parser;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class PrestoStatementParser extends SQLStatementParser
{
    public PrestoStatementParser(final String sql) {
        super(new PrestoExprParser(sql, new SQLParserFeature[0]));
    }
    
    public PrestoStatementParser(final Lexer lexer) {
        super(new PrestoExprParser(lexer));
    }
    
    @Override
    protected void parseInsertColumns(final SQLInsertInto insert) {
        if (this.lexer.token() == Token.RPAREN) {
            return;
        }
        while (true) {
            final SQLName expr = this.exprParser.name();
            expr.setParent(insert);
            insert.getColumns().add(expr);
            if (this.lexer.token() == Token.IDENTIFIER) {
                final String text = this.lexer.stringVal();
                if (text.equalsIgnoreCase("TINYINT") || text.equalsIgnoreCase("BIGINT") || text.equalsIgnoreCase("INTEGER") || text.equalsIgnoreCase("DOUBLE") || text.equalsIgnoreCase("DATE") || text.equalsIgnoreCase("VARCHAR")) {
                    expr.getAttributes().put("dataType", text);
                    this.lexer.nextToken();
                }
                else if (text.equalsIgnoreCase("CHAR")) {
                    String dataType = text;
                    this.lexer.nextToken();
                    this.accept(Token.LPAREN);
                    final SQLExpr char_len = this.exprParser.primary();
                    this.accept(Token.RPAREN);
                    dataType = dataType + "(" + char_len.toString() + ")";
                    expr.getAttributes().put("dataType", dataType);
                }
            }
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
    }
}
