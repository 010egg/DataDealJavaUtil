// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.parser;

import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.ClusteringType;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.hive.ast.HiveInputOutputFormat;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.odps.ast.OdpsCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class OdpsCreateTableParser extends SQLCreateTableParser
{
    public OdpsCreateTableParser(final String sql) {
        super(new OdpsExprParser(sql, new SQLParserFeature[0]));
    }
    
    public OdpsCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final OdpsCreateTableStatement stmt = new OdpsCreateTableStatement();
        if (acceptCreate) {
            this.accept(Token.CREATE);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.EXTERNAL)) {
            this.lexer.nextToken();
            stmt.setExternal(true);
        }
        this.accept(Token.TABLE);
        if (this.lexer.token() == Token.IF || this.lexer.identifierEquals("IF")) {
            this.lexer.nextToken();
            this.accept(Token.NOT);
            this.accept(Token.EXISTS);
            stmt.setIfNotExiists(true);
        }
        stmt.setName(this.exprParser.name());
        if (this.lexer.token() == Token.COMMENT) {
            this.lexer.nextToken();
            stmt.setComment(this.exprParser.primary());
        }
        if (this.lexer.token() == Token.SEMI || this.lexer.token() == Token.EOF) {
            return stmt;
        }
        while (true) {
            if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
                this.parseTblProperties(stmt);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                this.lexer.nextToken();
                stmt.setLifecycle(this.exprParser.expr());
            }
            else {
                if (!this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
                    break;
                }
                this.lexer.nextToken();
                this.accept(Token.AS);
                if (this.lexer.identifierEquals(FnvHash.Constants.INPUTFORMAT)) {
                    final HiveInputOutputFormat format = new HiveInputOutputFormat();
                    this.lexer.nextToken();
                    format.setInput(this.exprParser.primary());
                    if (this.lexer.identifierEquals(FnvHash.Constants.OUTPUTFORMAT)) {
                        this.lexer.nextToken();
                        format.setOutput(this.exprParser.primary());
                    }
                    stmt.setStoredAs(format);
                }
                else {
                    final SQLName name = this.exprParser.name();
                    stmt.setStoredAs(name);
                }
            }
        }
    Label_0959:
        while (true) {
            if (this.lexer.token() == Token.LIKE) {
                this.lexer.nextToken();
                final SQLName name = this.exprParser.name();
                stmt.setLike(name);
            }
            else if (this.lexer.token() == Token.AS) {
                this.lexer.nextToken();
                final OdpsSelectParser selectParser = new OdpsSelectParser(this.exprParser);
                final SQLSelect select = selectParser.select();
                stmt.setSelect(select);
            }
            else if (this.lexer.token() == Token.LPAREN || !stmt.isExternal()) {
                this.accept(Token.LPAREN);
                if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                    stmt.addBodyBeforeComment(this.lexer.readAndResetComments());
                }
                while (true) {
                    switch (this.lexer.token()) {
                        case IDENTIFIER:
                        case KEY:
                        case SEQUENCE:
                        case USER:
                        case GROUP:
                        case INDEX:
                        case ENABLE:
                        case DISABLE:
                        case DESC:
                        case ALL:
                        case INTERVAL:
                        case OPEN:
                        case PARTITION:
                        case SCHEMA:
                        case CONSTRAINT:
                        case COMMENT:
                        case VIEW:
                        case SHOW:
                        case ORDER:
                        case LEAVE:
                        case UNIQUE:
                        case DEFAULT:
                        case EXPLAIN:
                        case CHECK:
                        case CLOSE:
                        case IN:
                        case OUT:
                        case INOUT:
                        case LIMIT:
                        case FULL:
                        case MINUS:
                        case VALUES:
                        case TRIGGER:
                        case USE:
                        case LIKE:
                        case DISTRIBUTE:
                        case DELETE:
                        case UPDATE:
                        case IS:
                        case LEFT:
                        case RIGHT:
                        case REPEAT:
                        case COMPUTE:
                        case LOCK:
                        case TABLE:
                        case DO:
                        case WHILE:
                        case LOOP:
                        case FOR:
                        case RLIKE:
                        case PROCEDURE:
                        case GRANT:
                        case EXCEPT:
                        case CREATE:
                        case PARTITIONED:
                        case UNION:
                        case PRIMARY:
                        case INNER:
                        case TO:
                        case DECLARE:
                        case REFERENCES:
                        case FOREIGN:
                        case ESCAPE:
                        case BY:
                        case ALTER:
                        case SOME:
                        case ASC:
                        case NULL:
                        case CURSOR:
                        case FETCH:
                        case OVER:
                        case DATABASE: {
                            final SQLColumnDefinition column = this.exprParser.parseColumn(stmt);
                            stmt.getTableElementList().add(column);
                            if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                                column.addAfterComment(this.lexer.readAndResetComments());
                            }
                            if (this.lexer.token() != Token.COMMA) {
                                this.accept(Token.RPAREN);
                                break Label_0959;
                            }
                            this.lexer.nextToken();
                            if (!this.lexer.isKeepComments() || !this.lexer.hasComment()) {
                                continue;
                            }
                            column.addAfterComment(this.lexer.readAndResetComments());
                            continue;
                        }
                        default: {
                            throw new ParserException("expect identifier. " + this.lexer.info());
                        }
                    }
                }
            }
            while (true) {
                if (this.lexer.token() == Token.COMMENT) {
                    this.lexer.nextToken();
                    stmt.setComment(this.exprParser.primary());
                }
                else if (this.lexer.token() == Token.PARTITIONED) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    this.accept(Token.LPAREN);
                    while (true) {
                        switch (this.lexer.token()) {
                            case IDENTIFIER:
                            case KEY:
                            case SEQUENCE:
                            case USER:
                            case GROUP:
                            case INDEX:
                            case INTERVAL:
                            case PARTITION:
                            case CHECK:
                            case TABLE:
                            case LOOP: {
                                final SQLColumnDefinition column = this.exprParser.parseColumn();
                                stmt.addPartitionColumn(column);
                                if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                                    column.addAfterComment(this.lexer.readAndResetComments());
                                }
                                if (this.lexer.token() != Token.COMMA) {
                                    this.accept(Token.RPAREN);
                                    continue Label_0959;
                                }
                                this.lexer.nextToken();
                                if (!this.lexer.isKeepComments() || !this.lexer.hasComment()) {
                                    continue;
                                }
                                column.addAfterComment(this.lexer.readAndResetComments());
                                continue;
                            }
                            default: {
                                throw new ParserException("expect identifier. " + this.lexer.info());
                            }
                        }
                    }
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.RANGE)) {
                    this.lexer.nextToken();
                    if (!this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
                        continue;
                    }
                    stmt.setClusteringType(ClusteringType.Range);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    this.accept(Token.LPAREN);
                    while (true) {
                        final SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
                        stmt.addClusteredByItem(item);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ROW)) {
                    final SQLExternalRecordFormat recordFormat = this.exprParser.parseRowFormat();
                    stmt.setRowFormat(recordFormat);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.SORTED)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    this.accept(Token.LPAREN);
                    while (true) {
                        final SQLSelectOrderByItem item = this.exprParser.parseSelectOrderByItem();
                        stmt.addSortedByItem(item);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                }
                else if (stmt.getClusteringType() != ClusteringType.Range && (stmt.getClusteredBy().size() > 0 || stmt.getSortedBy().size() > 0) && this.lexer.token() == Token.INTO) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.LITERAL_INT) {
                        throw new ParserException("into buckets must be integer. " + this.lexer.info());
                    }
                    stmt.setBuckets(this.lexer.integerValue().intValue());
                    this.lexer.nextToken();
                    this.acceptIdentifier("BUCKETS");
                    if (this.lexer.token() != Token.INTO) {
                        continue;
                    }
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.LITERAL_INT) {
                        throw new ParserException("into shards must be integer. " + this.lexer.info());
                    }
                    stmt.setShards(this.lexer.integerValue().intValue());
                    this.lexer.nextToken();
                    this.acceptIdentifier("SHARDS");
                }
                else if (this.lexer.token() == Token.INTO) {
                    this.lexer.nextToken();
                    if (this.lexer.token() != Token.LITERAL_INT) {
                        throw new ParserException("into shards must be integer. " + this.lexer.info());
                    }
                    stmt.setIntoBuckets(new SQLIntegerExpr(this.lexer.integerValue().intValue()));
                    this.lexer.nextToken();
                    this.acceptIdentifier("BUCKETS");
                }
                else if (this.lexer.token() == Token.AS && stmt.getSelect() == null) {
                    this.lexer.nextToken();
                    final OdpsSelectParser selectParser = new OdpsSelectParser(this.exprParser);
                    final SQLSelect select = selectParser.select();
                    stmt.setSelect(select);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                    this.lexer.nextToken();
                    stmt.setLifecycle(this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.AS) {
                        this.lexer.nextToken();
                        if (this.lexer.identifierEquals(FnvHash.Constants.INPUTFORMAT)) {
                            final HiveInputOutputFormat format = new HiveInputOutputFormat();
                            this.lexer.nextToken();
                            format.setInput(this.exprParser.primary());
                            if (this.lexer.identifierEquals(FnvHash.Constants.OUTPUTFORMAT)) {
                                this.lexer.nextToken();
                                format.setOutput(this.exprParser.primary());
                            }
                            stmt.setStoredAs(format);
                        }
                        else {
                            final SQLName storedAs = this.exprParser.name();
                            stmt.setStoredAs(storedAs);
                        }
                    }
                    else {
                        this.accept(Token.BY);
                        final SQLExpr storedBy = this.exprParser.expr();
                        stmt.setStoredBy(storedBy);
                    }
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                    this.lexer.nextToken();
                    stmt.setLifecycle(this.exprParser.expr());
                }
                else if (this.lexer.token() == Token.WITH) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("SERDEPROPERTIES");
                    this.accept(Token.LPAREN);
                    this.exprParser.exprList(stmt.getWithSerdeproperties(), stmt);
                    this.accept(Token.RPAREN);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
                    this.parseTblProperties(stmt);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.LOCATION)) {
                    this.lexer.nextToken();
                    final SQLExpr location = this.exprParser.expr();
                    stmt.setLocation(location);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.TBLPROPERTIES)) {
                    this.parseTblProperties(stmt);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.USING)) {
                    this.lexer.nextToken();
                    final SQLExpr using = this.exprParser.expr();
                    stmt.setUsing(using);
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                        return stmt;
                    }
                    this.lexer.nextToken();
                    stmt.setLifecycle(this.exprParser.expr());
                }
            }
            break;
        }
    }
    
    private void parseTblProperties(final OdpsCreateTableStatement stmt) {
        this.acceptIdentifier("TBLPROPERTIES");
        this.accept(Token.LPAREN);
        do {
            final String name = this.lexer.stringVal();
            this.lexer.nextToken();
            this.accept(Token.EQ);
            final SQLExpr value = this.exprParser.primary();
            stmt.addTblProperty(name, value);
            if (this.lexer.token() != Token.COMMA) {
                break;
            }
            this.lexer.nextToken();
        } while (this.lexer.token() != Token.RPAREN);
        this.accept(Token.RPAREN);
    }
}
