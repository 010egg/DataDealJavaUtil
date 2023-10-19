// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.h2.parser;

import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertInto;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collection;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.ast.statement.SQLReplaceStatement;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class H2StatementParser extends SQLStatementParser
{
    public H2StatementParser(final String sql) {
        super(new H2ExprParser(sql));
    }
    
    public H2StatementParser(final String sql, final SQLParserFeature... features) {
        super(new H2ExprParser(sql, features));
    }
    
    public H2StatementParser(final Lexer lexer) {
        super(new H2ExprParser(lexer));
    }
    
    @Override
    public H2SelectParser createSQLSelectParser() {
        return new H2SelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public SQLStatement parseMerge() {
        this.accept(Token.MERGE);
        this.accept(Token.INTO);
        final SQLReplaceStatement stmt = new SQLReplaceStatement();
        stmt.setDbType(DbType.h2);
        final SQLName tableName = this.exprParser.name();
        stmt.setTableName(tableName);
        if (this.lexer.token() == Token.KEY) {
            this.lexer.nextToken();
            this.accept(Token.LPAREN);
            this.exprParser.exprList(stmt.getColumns(), stmt);
            this.accept(Token.RPAREN);
        }
        if (this.lexer.token() == Token.VALUES || this.lexer.identifierEquals("VALUE")) {
            this.lexer.nextToken();
            this.parseValueClause(stmt.getValuesList(), null, 0, stmt);
        }
        else if (this.lexer.token() == Token.SELECT) {
            final SQLQueryExpr queryExpr = (SQLQueryExpr)this.exprParser.expr();
            stmt.setQuery(queryExpr);
        }
        else if (this.lexer.token() == Token.LPAREN) {
            final SQLSelect select = this.createSQLSelectParser().select();
            final SQLQueryExpr queryExpr2 = new SQLQueryExpr(select);
            stmt.setQuery(queryExpr2);
        }
        return stmt;
    }
    
    @Override
    protected void parseInsert0(final SQLInsertInto insertStatement, final boolean acceptSubQuery) {
        super.parseInsert0(insertStatement, acceptSubQuery);
        this.parseSetStatement(insertStatement);
    }
    
    private void parseSetStatement(final SQLInsertInto insertStatement) {
        if (this.lexer.token() == Token.SET) {
            this.lexer.nextToken();
            final SQLInsertStatement.ValuesClause values = new SQLInsertStatement.ValuesClause();
            insertStatement.addValueCause(values);
            while (true) {
                final SQLName name = this.exprParser.name();
                insertStatement.addColumn(name);
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                else {
                    this.accept(Token.COLONEQ);
                }
                values.addValue(this.exprParser.expr());
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
        }
    }
}
