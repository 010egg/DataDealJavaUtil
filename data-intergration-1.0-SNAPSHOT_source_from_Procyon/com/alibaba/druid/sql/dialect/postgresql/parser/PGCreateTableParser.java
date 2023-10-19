// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.parser;

import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class PGCreateTableParser extends SQLCreateTableParser
{
    public PGCreateTableParser(final Lexer lexer) {
        super(new PGExprParser(lexer));
    }
    
    public PGCreateTableParser(final String sql) {
        super(new PGExprParser(sql));
    }
    
    public PGCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLPartitionBy parsePartitionBy() {
        this.lexer.nextToken();
        this.accept(Token.BY);
        if (this.lexer.identifierEquals("LIST")) {
            this.lexer.nextToken();
            final SQLPartitionByList list = new SQLPartitionByList();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                list.addColumn(this.exprParser.expr());
                this.accept(Token.RPAREN);
            }
            else {
                this.acceptIdentifier("COLUMNS");
                this.accept(Token.LPAREN);
                while (true) {
                    list.addColumn(this.exprParser.name());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            return list;
        }
        if (this.lexer.identifierEquals("HASH") || this.lexer.identifierEquals("UNI_HASH")) {
            final SQLPartitionByHash hash = new SQLPartitionByHash();
            if (this.lexer.identifierEquals("UNI_HASH")) {
                hash.setUnique(true);
            }
            this.lexer.nextToken();
            if (this.lexer.token() == Token.KEY) {
                this.lexer.nextToken();
                hash.setKey(true);
            }
            this.accept(Token.LPAREN);
            this.exprParser.exprList(hash.getColumns(), hash);
            this.accept(Token.RPAREN);
            return hash;
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
}
