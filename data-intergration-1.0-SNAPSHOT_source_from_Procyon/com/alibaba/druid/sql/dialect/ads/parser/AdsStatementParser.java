// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.ads.parser;

import com.alibaba.druid.sql.parser.SQLSelectParser;
import com.alibaba.druid.sql.ast.statement.SQLShowTableGroupsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowColumnsStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowTablesStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowDatabasesStatement;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.statement.SQLShowCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLShowProcessListStatement;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;

public class AdsStatementParser extends SQLStatementParser
{
    public AdsStatementParser(final String sql) {
        super(new AdsExprParser(sql));
    }
    
    public AdsStatementParser(final String sql, final SQLParserFeature... features) {
        super(new AdsExprParser(sql, features));
    }
    
    public AdsStatementParser(final Lexer lexer) {
        super(new AdsExprParser(lexer));
    }
    
    @Override
    public AdsSelectParser createSQLSelectParser() {
        return new AdsSelectParser(this.exprParser, this.selectListCache);
    }
    
    @Override
    public SQLCreateTableParser getSQLCreateTableParser() {
        return new AdsCreateTableParser(this.exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable() {
        final AdsCreateTableParser parser = new AdsCreateTableParser(this.exprParser);
        return parser.parseCreateTable(true);
    }
    
    @Override
    public SQLStatement parseShow() {
        this.accept(Token.SHOW);
        if (this.lexer.identifierEquals(FnvHash.Constants.DATABASES)) {
            this.lexer.nextToken();
            final SQLShowDatabasesStatement stmt = this.parseShowDatabases(false);
            return stmt;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLES)) {
            this.lexer.nextToken();
            final SQLShowTablesStatement stmt2 = this.parseShowTables();
            return stmt2;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.COLUMNS)) {
            this.lexer.nextToken();
            final SQLShowColumnsStatement stmt3 = this.parseShowColumns();
            return stmt3;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUPS)) {
            this.lexer.nextToken();
            final SQLShowTableGroupsStatement stmt4 = this.parseShowTableGroups();
            return stmt4;
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.PROCESSLIST)) {
            this.lexer.nextToken();
            final SQLShowProcessListStatement stmt5 = new SQLShowProcessListStatement();
            if (this.lexer.identifierEquals(FnvHash.Constants.MPP)) {
                this.lexer.nextToken();
                stmt5.setMpp(true);
            }
            return stmt5;
        }
        if (this.lexer.token() == Token.CREATE) {
            this.lexer.nextToken();
            this.accept(Token.TABLE);
            final SQLShowCreateTableStatement stmt6 = new SQLShowCreateTableStatement();
            stmt6.setName(this.exprParser.name());
            return stmt6;
        }
        if (this.lexer.token() == Token.ALL) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.CREATE) {
                this.lexer.nextToken();
                this.accept(Token.TABLE);
                final SQLShowCreateTableStatement stmt6 = new SQLShowCreateTableStatement();
                stmt6.setAll(true);
                stmt6.setName(this.exprParser.name());
                return stmt6;
            }
        }
        throw new ParserException("TODO " + this.lexer.info());
    }
}
