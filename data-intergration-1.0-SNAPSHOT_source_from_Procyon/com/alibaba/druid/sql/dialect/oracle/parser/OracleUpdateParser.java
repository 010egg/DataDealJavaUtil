// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleUpdateStatement;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class OracleUpdateParser extends SQLStatementParser
{
    public OracleUpdateParser(final String sql) {
        super(new OracleExprParser(sql));
    }
    
    public OracleUpdateParser(final Lexer lexer) {
        super(new OracleExprParser(lexer));
    }
    
    @Override
    public OracleUpdateStatement parseUpdateStatement() {
        final OracleUpdateStatement update = new OracleUpdateStatement();
        if (this.lexer.token() == Token.UPDATE) {
            this.lexer.nextToken();
            this.parseHints(update);
            if (this.lexer.identifierEquals("ONLY")) {
                update.setOnly(true);
            }
            final SQLTableSource tableSource = this.exprParser.createSelectParser().parseTableSource();
            update.setTableSource(tableSource);
            if (update.getAlias() == null || update.getAlias().length() == 0) {
                update.setAlias(this.tableAlias());
            }
        }
        this.parseUpdateSet(update);
        this.parseWhere(update);
        this.parseReturn(update);
        this.parseErrorLoging(update);
        return update;
    }
    
    private void parseErrorLoging(final OracleUpdateStatement update) {
        if (this.lexer.identifierEquals("LOG")) {
            throw new ParserException("TODO. " + this.lexer.info());
        }
    }
    
    private void parseReturn(final OracleUpdateStatement update) {
        if (this.lexer.token() == Token.RETURN || this.lexer.token() == Token.RETURNING) {
            this.lexer.nextToken();
            while (true) {
                final SQLExpr item = this.exprParser.expr();
                update.getReturning().add(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.INTO);
            while (true) {
                final SQLExpr item = this.exprParser.expr();
                update.addReturningInto(item);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
    }
    
    private void parseHints(final OracleUpdateStatement update) {
        this.exprParser.parseHints(update.getHints());
    }
    
    private void parseWhere(final OracleUpdateStatement update) {
        if (this.lexer.token() == Token.WHERE) {
            this.lexer.nextToken();
            update.setWhere(this.exprParser.expr());
        }
    }
}
