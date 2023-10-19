// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.db2.parser;

import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.statement.SQLAlterTableAlterColumn;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.dialect.db2.ast.stmt.DB2ValuesStatement;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLStatement;
import java.util.List;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class DB2StatementParser extends SQLStatementParser
{
    public DB2StatementParser(final String sql) {
        super(new DB2ExprParser(sql));
    }
    
    public DB2StatementParser(final String sql, final SQLParserFeature... features) {
        super(new DB2ExprParser(sql, features));
    }
    
    public DB2StatementParser(final Lexer lexer) {
        super(new DB2ExprParser(lexer));
    }
    
    @Override
    public DB2SelectParser createSQLSelectParser() {
        return new DB2SelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public boolean parseStatementListDialect(final List<SQLStatement> statementList) {
        if (this.lexer.token() == Token.VALUES) {
            this.lexer.nextToken();
            final DB2ValuesStatement stmt = new DB2ValuesStatement();
            stmt.setExpr(this.exprParser.expr());
            statementList.add(stmt);
            return true;
        }
        return false;
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new DB2CreateTableParser(this.exprParser);
    }
    
    @Override
    protected SQLAlterTableAlterColumn parseAlterColumn() {
        if (this.lexer.token() == Token.COLUMN) {
            this.lexer.nextToken();
        }
        final SQLColumnDefinition column = this.exprParser.parseColumn();
        final SQLAlterTableAlterColumn alterColumn = new SQLAlterTableAlterColumn();
        alterColumn.setColumn(column);
        if (column.getDataType() == null && column.getConstraints().size() == 0) {
            if (this.lexer.token() == Token.SET) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    this.accept(Token.NULL);
                    alterColumn.setSetNotNull(true);
                }
                else if (this.lexer.token() == Token.DEFAULT) {
                    this.lexer.nextToken();
                    final SQLExpr defaultValue = this.exprParser.expr();
                    alterColumn.setSetDefault(defaultValue);
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
                        throw new ParserException("TODO : " + this.lexer.info());
                    }
                    this.lexer.nextToken();
                    this.acceptIdentifier("TYPE");
                    final SQLDataType dataType = this.exprParser.parseDataType();
                    alterColumn.setDataType(dataType);
                }
            }
            else if (this.lexer.token() == Token.DROP) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                    this.accept(Token.NULL);
                    alterColumn.setDropNotNull(true);
                }
                else {
                    this.accept(Token.DEFAULT);
                    alterColumn.setDropDefault(true);
                }
            }
        }
        return alterColumn;
    }
}
