// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.clickhouse.parser;

import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.statement.SQLWithSubqueryClause;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class ClickhouseStatementParser extends SQLStatementParser
{
    public ClickhouseStatementParser(final String sql) {
        super(new ClickhouseExprParser(sql));
    }
    
    public ClickhouseStatementParser(final String sql, final SQLParserFeature... features) {
        super(new ClickhouseExprParser(sql, features));
    }
    
    public ClickhouseStatementParser(final Lexer lexer) {
        super(new ClickhouseExprParser(lexer));
    }
    
    @Override
    public SQLWithSubqueryClause parseWithQuery() {
        final SQLWithSubqueryClause withQueryClause = new SQLWithSubqueryClause();
        if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
            withQueryClause.addBeforeComment(this.lexer.readAndResetComments());
        }
        this.accept(Token.WITH);
        while (true) {
            final SQLWithSubqueryClause.Entry entry = new SQLWithSubqueryClause.Entry();
            entry.setParent(withQueryClause);
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                switch (this.lexer.token()) {
                    case VALUES:
                    case WITH:
                    case SELECT: {
                        entry.setSubQuery(this.createSQLSelectParser().select());
                        break;
                    }
                }
                this.accept(Token.RPAREN);
            }
            else {
                entry.setExpr(this.exprParser.expr());
            }
            this.accept(Token.AS);
            final String alias = this.lexer.stringVal();
            this.lexer.nextToken();
            entry.setAlias(alias);
            withQueryClause.addEntry(entry);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        }
        return withQueryClause;
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new ClickhouseCreateTableParser(this.exprParser);
    }
}
