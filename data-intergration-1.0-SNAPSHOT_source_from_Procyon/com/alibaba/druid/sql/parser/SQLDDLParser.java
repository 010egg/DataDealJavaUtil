// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.statement.SQLPrimaryKeyImpl;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;

public class SQLDDLParser extends SQLStatementParser
{
    public SQLDDLParser(final String sql) {
        super(sql);
    }
    
    public SQLDDLParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    protected SQLTableConstraint parseConstraint() {
        if (this.lexer.token == Token.CONSTRAINT) {
            this.lexer.nextToken();
        }
        if (this.lexer.token == Token.IDENTIFIER) {
            this.exprParser.name();
            throw new ParserException("TODO. " + this.lexer.info());
        }
        if (this.lexer.token == Token.PRIMARY) {
            this.lexer.nextToken();
            this.accept(Token.KEY);
            final SQLPrimaryKeyImpl pk = new SQLPrimaryKeyImpl();
            this.accept(Token.LPAREN);
            this.exprParser.orderBy(pk.getColumns(), pk);
            this.accept(Token.RPAREN);
            return pk;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
}
