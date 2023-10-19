// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import com.alibaba.druid.sql.dialect.mysql.ast.MysqlForeignKey;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlPrimaryKey;
import com.alibaba.druid.sql.ast.SQLSubPartitionBy;
import com.alibaba.druid.sql.ast.expr.SQLNumberExpr;
import com.alibaba.druid.sql.ast.SQLSubPartitionByRange;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByList;
import com.alibaba.druid.sql.ast.SQLSubPartitionByHash;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByValue;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSubPartitionByKey;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.SQLPartitionByValue;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlPartitionByKey;
import com.alibaba.druid.sql.ast.SQLCommentHint;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLCheck;
import com.alibaba.druid.sql.ast.statement.SQLForeignKeyConstraint;
import com.alibaba.druid.sql.ast.statement.SQLTableConstraint;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlExtPartition;
import com.alibaba.druid.sql.ast.expr.SQLBetweenExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.sql.ast.expr.SQLCharExpr;
import java.util.Collection;
import com.alibaba.druid.sql.ast.expr.SQLListExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableLike;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlUnique;
import com.alibaba.druid.util.MySqlUtils;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlTableIndex;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.dialect.mysql.ast.MySqlKey;
import java.util.List;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class MySqlCreateTableParser extends SQLCreateTableParser
{
    public MySqlCreateTableParser(final String sql) {
        super(new MySqlExprParser(sql));
    }
    
    public MySqlCreateTableParser(final SQLExprParser exprParser) {
        super(exprParser);
    }
    
    @Override
    public SQLCreateTableStatement parseCreateTable() {
        return this.parseCreateTable(true);
    }
    
    @Override
    public MySqlExprParser getExprParser() {
        return (MySqlExprParser)this.exprParser;
    }
    
    @Override
    public MySqlCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final MySqlCreateTableStatement stmt = new MySqlCreateTableStatement();
        if (acceptCreate) {
            if (this.lexer.hasComment() && this.lexer.isKeepComments()) {
                stmt.addBeforeComment(this.lexer.readAndResetComments());
            }
            this.accept(Token.CREATE);
        }
        if (this.lexer.identifierEquals("TEMPORARY")) {
            this.lexer.nextToken();
            stmt.setType(SQLCreateTableStatement.Type.GLOBAL_TEMPORARY);
        }
        else if (this.lexer.identifierEquals("SHADOW")) {
            this.lexer.nextToken();
            stmt.setType(SQLCreateTableStatement.Type.SHADOW);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.DIMENSION)) {
            this.lexer.nextToken();
            stmt.setDimension(true);
        }
        if (this.lexer.token() == Token.HINT) {
            this.exprParser.parseHints(stmt.getHints());
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
        if (this.lexer.token() == Token.LIKE) {
            this.lexer.nextToken();
            final SQLName name = this.exprParser.name();
            stmt.setLike(name);
        }
        if (this.lexer.token() == Token.WITH) {
            final SQLSelect query = new MySqlSelectParser(this.exprParser).select();
            stmt.setSelect(query);
        }
        else if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.SELECT) {
                final SQLSelect query = new MySqlSelectParser(this.exprParser).select();
                stmt.setSelect(query);
            }
            else {
                while (true) {
                    SQLColumnDefinition column = null;
                    boolean global = false;
                    if (this.lexer.identifierEquals(FnvHash.Constants.GLOBAL)) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.INDEX || this.lexer.token() == Token.UNIQUE) {
                            global = true;
                        }
                        else {
                            this.lexer.reset(mark);
                        }
                    }
                    if (this.lexer.token() == Token.FULLTEXT) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.KEY) {
                            final MySqlKey fulltextKey = new MySqlKey();
                            this.exprParser.parseIndex(fulltextKey.getIndexDefinition());
                            fulltextKey.setIndexType("FULLTEXT");
                            fulltextKey.setParent(stmt);
                            stmt.getTableElementList().add(fulltextKey);
                            while (this.lexer.token() == Token.HINT) {
                                this.lexer.nextToken();
                            }
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else if (this.lexer.token() == Token.INDEX) {
                            final MySqlTableIndex idx = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx.getIndexDefinition());
                            idx.setIndexType("FULLTEXT");
                            idx.setParent(stmt);
                            stmt.getTableElementList().add(idx);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else if (this.lexer.token() == Token.IDENTIFIER && MySqlUtils.isBuiltinDataType(this.lexer.stringVal())) {
                            this.lexer.reset(mark);
                        }
                        else {
                            final MySqlTableIndex idx = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx.getIndexDefinition());
                            idx.setIndexType("FULLTEXT");
                            idx.setParent(stmt);
                            stmt.getTableElementList().add(idx);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.SPATIAL)) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.INDEX || this.lexer.token() == Token.KEY || this.lexer.token() != Token.IDENTIFIER || !MySqlUtils.isBuiltinDataType(this.lexer.stringVal())) {
                            final MySqlTableIndex idx = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx.getIndexDefinition());
                            idx.setIndexType("SPATIAL");
                            idx.setParent(stmt);
                            stmt.getTableElementList().add(idx);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else {
                            this.lexer.reset(mark);
                        }
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.ANN)) {
                        final Lexer.SavePoint mark = this.lexer.mark();
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.INDEX || this.lexer.token() == Token.KEY) {
                            final MySqlTableIndex idx = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx.getIndexDefinition());
                            idx.setIndexType("ANN");
                            idx.setParent(stmt);
                            stmt.getTableElementList().add(idx);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else {
                            this.lexer.reset(mark);
                        }
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERED)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.KEY) {
                            final MySqlKey clsKey = new MySqlKey();
                            this.exprParser.parseIndex(clsKey.getIndexDefinition());
                            clsKey.setIndexType("CLUSTERED");
                            clsKey.setParent(stmt);
                            stmt.getTableElementList().add(clsKey);
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else if (this.lexer.token() == Token.INDEX) {
                            final MySqlTableIndex idx2 = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx2.getIndexDefinition());
                            idx2.setIndexType("CLUSTERED");
                            idx2.setParent(stmt);
                            stmt.getTableElementList().add(idx2);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTERING)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.KEY) {
                            final MySqlKey clsKey = new MySqlKey();
                            this.exprParser.parseIndex(clsKey.getIndexDefinition());
                            clsKey.setIndexType("CLUSTERING");
                            clsKey.setParent(stmt);
                            stmt.getTableElementList().add(clsKey);
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                        else if (this.lexer.token() == Token.INDEX) {
                            final MySqlTableIndex idx2 = new MySqlTableIndex();
                            this.exprParser.parseIndex(idx2.getIndexDefinition());
                            idx2.setIndexType("CLUSTERING");
                            idx2.setParent(stmt);
                            stmt.getTableElementList().add(idx2);
                            if (this.lexer.token() == Token.RPAREN) {
                                break;
                            }
                            if (this.lexer.token() == Token.COMMA) {
                                this.lexer.nextToken();
                                continue;
                            }
                        }
                    }
                    else if (this.lexer.token() == Token.IDENTIFIER || this.lexer.token() == Token.LITERAL_CHARS) {
                        column = this.exprParser.parseColumn();
                        column.setParent(stmt);
                        stmt.getTableElementList().add(column);
                        if (this.lexer.isKeepComments() && this.lexer.hasComment()) {
                            column.addAfterComment(this.lexer.readAndResetComments());
                        }
                    }
                    else if (this.lexer.token() == Token.CONSTRAINT || this.lexer.token() == Token.PRIMARY || this.lexer.token() == Token.UNIQUE) {
                        final SQLTableConstraint constraint = this.parseConstraint();
                        constraint.setParent(stmt);
                        if (constraint instanceof MySqlUnique) {
                            final MySqlUnique unique = (MySqlUnique)constraint;
                            if (global) {
                                unique.setGlobal(true);
                            }
                        }
                        stmt.getTableElementList().add(constraint);
                    }
                    else if (this.lexer.token() == Token.INDEX) {
                        final MySqlTableIndex idx2 = new MySqlTableIndex();
                        this.exprParser.parseIndex(idx2.getIndexDefinition());
                        if (global) {
                            idx2.getIndexDefinition().setGlobal(true);
                        }
                        idx2.setParent(stmt);
                        stmt.getTableElementList().add(idx2);
                    }
                    else if (this.lexer.token() == Token.KEY) {
                        final Lexer.SavePoint savePoint = this.lexer.mark();
                        this.lexer.nextToken();
                        boolean isColumn = false;
                        if (this.lexer.identifierEquals(FnvHash.Constants.VARCHAR)) {
                            isColumn = true;
                        }
                        this.lexer.reset(savePoint);
                        if (isColumn) {
                            column = this.exprParser.parseColumn();
                            stmt.getTableElementList().add(column);
                        }
                        else {
                            stmt.getTableElementList().add(this.parseConstraint());
                        }
                    }
                    else if (this.lexer.token() == Token.PRIMARY) {
                        final SQLTableConstraint pk = this.parseConstraint();
                        pk.setParent(stmt);
                        stmt.getTableElementList().add(pk);
                    }
                    else if (this.lexer.token() == Token.FOREIGN) {
                        final SQLForeignKeyConstraint fk = this.getExprParser().parseForeignKey();
                        fk.setParent(stmt);
                        stmt.getTableElementList().add(fk);
                    }
                    else if (this.lexer.token() == Token.CHECK) {
                        final SQLCheck check = this.exprParser.parseCheck();
                        stmt.getTableElementList().add(check);
                    }
                    else if (this.lexer.token() == Token.LIKE) {
                        this.lexer.nextToken();
                        final SQLTableLike tableLike = new SQLTableLike();
                        tableLike.setTable(new SQLExprTableSource(this.exprParser.name()));
                        tableLike.setParent(stmt);
                        stmt.getTableElementList().add(tableLike);
                        if (this.lexer.identifierEquals(FnvHash.Constants.INCLUDING)) {
                            this.lexer.nextToken();
                            this.acceptIdentifier("PROPERTIES");
                            tableLike.setIncludeProperties(true);
                        }
                        else if (this.lexer.identifierEquals(FnvHash.Constants.EXCLUDING)) {
                            this.lexer.nextToken();
                            this.acceptIdentifier("PROPERTIES");
                            tableLike.setExcludeProperties(true);
                        }
                    }
                    else {
                        column = this.exprParser.parseColumn();
                        stmt.getTableElementList().add(column);
                    }
                    if (this.lexer.token() == Token.HINT) {
                        this.lexer.nextToken();
                    }
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                    if (!this.lexer.isKeepComments() || !this.lexer.hasComment() || column == null) {
                        continue;
                    }
                    column.addAfterComment(this.lexer.readAndResetComments());
                }
            }
            if (this.lexer.token() == Token.HINT) {
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
            if (this.lexer.token() == Token.HINT && this.lexer.stringVal().charAt(0) == '!') {
                this.lexer.nextToken();
            }
        }
        while (true) {
            if (this.lexer.token() == Token.COMMA) {
                this.lexer.nextToken();
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.ENGINE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                SQLExpr expr = null;
                if (this.lexer.token() == Token.MERGE) {
                    expr = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
                else {
                    expr = this.exprParser.expr();
                }
                stmt.setEngine(expr);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.BLOCK_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                SQLExpr expr = null;
                if (this.lexer.token() == Token.MERGE) {
                    expr = new SQLIdentifierExpr(this.lexer.stringVal());
                    this.lexer.nextToken();
                }
                else {
                    expr = this.exprParser.integerExpr();
                }
                stmt.addOption("BLOCK_SIZE", expr);
            }
            else if (this.lexer.identifierEquals("BLOCK_FORMAT")) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr expr = this.exprParser.primary();
                stmt.addOption("BLOCK_FORMAT", expr);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.REPLICA_NUM)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr expr = this.exprParser.integerExpr();
                stmt.addOption("REPLICA_NUM", expr);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.TABLET_SIZE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr expr = this.exprParser.integerExpr();
                stmt.addOption("TABLET_SIZE", expr);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PCTFREE)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr expr = this.exprParser.integerExpr();
                stmt.addOption("PCTFREE", expr);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.USE_BLOOM_FILTER)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                final SQLExpr expr = this.exprParser.primary();
                stmt.addOption("USE_BLOOM_FILTER", expr);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.AUTO_INCREMENT)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                stmt.addOption("AUTO_INCREMENT", this.exprParser.expr());
            }
            else if (this.lexer.identifierEquals("AVG_ROW_LENGTH")) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.EQ) {
                    this.lexer.nextToken();
                }
                stmt.addOption("AVG_ROW_LENGTH", this.exprParser.expr());
            }
            else if (this.lexer.token() == Token.DEFAULT) {
                this.lexer.nextToken();
                this.parseTableOptionCharsetOrCollate(stmt);
            }
            else {
                if (this.parseTableOptionCharsetOrCollate(stmt)) {
                    continue;
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.CHECKSUM)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("CHECKSUM", this.exprParser.expr());
                }
                else if (this.lexer.token() == Token.COMMENT) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.setComment(this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.CONNECTION)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("CONNECTION", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("DIRECTORY");
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("DATA DIRECTORY", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("DELAY_KEY_WRITE")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("DELAY_KEY_WRITE", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("FULLTEXT_DICT")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("FULLTEXT_DICT", this.exprParser.charExpr());
                }
                else if (this.lexer.token() == Token.INDEX) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("DIRECTORY");
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("INDEX DIRECTORY", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("INSERT_METHOD")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("INSERT_METHOD", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("KEY_BLOCK_SIZE")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("KEY_BLOCK_SIZE", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.MAX_ROWS)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("MAX_ROWS", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.MIN_ROWS)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("MIN_ROWS", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.PACK_KEYS)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("PACK_KEYS", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.PASSWORD)) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("PASSWORD", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("ROW_FORMAT")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("ROW_FORMAT", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("STATS_AUTO_RECALC")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("STATS_AUTO_RECALC", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("STATS_PERSISTENT")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("STATS_PERSISTENT", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("STATS_SAMPLE_PAGES")) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    stmt.addOption("STATS_SAMPLE_PAGES", this.exprParser.expr());
                }
                else if (this.lexer.token() == Token.UNION) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.EQ) {
                        this.lexer.nextToken();
                    }
                    this.accept(Token.LPAREN);
                    final SQLListExpr list = new SQLListExpr();
                    this.exprParser.exprList(list.getItems(), list);
                    stmt.addOption("UNION", list);
                    this.accept(Token.RPAREN);
                }
                else if (this.lexer.token() == Token.TABLESPACE) {
                    this.lexer.nextToken();
                    final MySqlCreateTableStatement.TableSpaceOption option = new MySqlCreateTableStatement.TableSpaceOption();
                    option.setName(this.exprParser.name());
                    if (this.lexer.identifierEquals("STORAGE")) {
                        this.lexer.nextToken();
                        option.setStorage(this.exprParser.name());
                    }
                    stmt.addOption("TABLESPACE", option);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.TABLEGROUP)) {
                    this.lexer.nextToken();
                    final SQLName tableGroup = this.exprParser.name();
                    stmt.setTableGroup(tableGroup);
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.TYPE)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    stmt.addOption("TYPE", this.exprParser.expr());
                }
                else if (this.lexer.identifierEquals("INDEX_ALL")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    if (this.lexer.token() != Token.LITERAL_CHARS) {
                        continue;
                    }
                    if ("Y".equalsIgnoreCase(this.lexer.stringVal())) {
                        this.lexer.nextToken();
                        stmt.addOption("INDEX_ALL", new SQLCharExpr("Y"));
                    }
                    else {
                        if (!"N".equalsIgnoreCase(this.lexer.stringVal())) {
                            throw new ParserException("INDEX_ALL accept parameter ['Y' or 'N'] only.");
                        }
                        this.lexer.nextToken();
                        stmt.addOption("INDEX_ALL", new SQLCharExpr("N"));
                    }
                }
                else if (this.lexer.identifierEquals("RT_INDEX_ALL")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    if (this.lexer.token() != Token.LITERAL_CHARS) {
                        continue;
                    }
                    if ("Y".equalsIgnoreCase(this.lexer.stringVal())) {
                        this.lexer.nextToken();
                        stmt.addOption("RT_INDEX_ALL", new SQLCharExpr("Y"));
                    }
                    else {
                        if (!"N".equalsIgnoreCase(this.lexer.stringVal())) {
                            throw new ParserException("RT_INDEX_ALL accepts parameter ['Y' or 'N'] only.");
                        }
                        this.lexer.nextToken();
                        stmt.addOption("RT_INDEX_ALL", new SQLCharExpr("N"));
                    }
                }
                else if (this.lexer.identifierEquals(FnvHash.Constants.ARCHIVE)) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    this.acceptIdentifier("OSS");
                    stmt.setArchiveBy(new SQLIdentifierExpr("OSS"));
                }
                else if (this.lexer.identifierEquals("STORAGE_TYPE")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    stmt.addOption("STORAGE_TYPE", this.exprParser.charExpr());
                }
                else if (this.lexer.identifierEquals("STORAGE_POLICY")) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    stmt.addOption("STORAGE_POLICY", this.exprParser.charExpr());
                }
                else {
                    if (this.lexer.identifierEquals("HOT_PARTITION_COUNT")) {
                        this.lexer.nextToken();
                        this.accept(Token.EQ);
                        try {
                            stmt.addOption("HOT_PARTITION_COUNT", this.exprParser.integerExpr());
                            continue;
                        }
                        catch (Exception e) {
                            throw new ParserException("only integer number is supported for hot_partition_count");
                        }
                    }
                    if (this.lexer.identifierEquals("TABLE_PROPERTIES")) {
                        this.lexer.nextToken();
                        this.accept(Token.EQ);
                        stmt.addOption("TABLE_PROPERTIES", this.exprParser.charExpr());
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.ENCRYPTION)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        stmt.addOption("ENCRYPTION", this.exprParser.expr());
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.COMPRESSION)) {
                        this.lexer.nextToken();
                        if (this.lexer.token() == Token.EQ) {
                            this.lexer.nextToken();
                        }
                        stmt.addOption("COMPRESSION", this.exprParser.expr());
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
                    else if (this.lexer.token() == Token.PARTITION) {
                        final SQLPartitionBy partitionClause = this.parsePartitionBy();
                        stmt.setPartitioning(partitionClause);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.BROADCAST)) {
                        this.lexer.nextToken();
                        stmt.setBroadCast(true);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.DISTRIBUTE) || this.lexer.identifierEquals(FnvHash.Constants.DISTRIBUTED)) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        if (this.lexer.identifierEquals(FnvHash.Constants.HASH)) {
                            this.lexer.nextToken();
                            this.accept(Token.LPAREN);
                            while (true) {
                                final SQLName name = this.exprParser.name();
                                stmt.getDistributeBy().add(name);
                                if (this.lexer.token() != Token.COMMA) {
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            this.accept(Token.RPAREN);
                            stmt.setDistributeByType(new SQLIdentifierExpr("HASH"));
                        }
                        else {
                            if (!this.lexer.identifierEquals(FnvHash.Constants.BROADCAST)) {
                                continue;
                            }
                            this.lexer.nextToken();
                            stmt.setDistributeByType(new SQLIdentifierExpr("BROADCAST"));
                        }
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITION)) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        final SQLExpr dbPartitoinBy = this.exprParser.primary();
                        stmt.setDbPartitionBy(dbPartitoinBy);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITIONS)) {
                        this.lexer.nextToken();
                        final SQLExpr dbPartitions = this.exprParser.primary();
                        stmt.setDbPartitions(dbPartitions);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.TBPARTITION)) {
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        SQLExpr expr = this.exprParser.expr();
                        if (this.lexer.identifierEquals(FnvHash.Constants.STARTWITH)) {
                            this.lexer.nextToken();
                            final SQLExpr start = this.exprParser.primary();
                            this.acceptIdentifier("ENDWITH");
                            final SQLExpr end = this.exprParser.primary();
                            expr = new SQLBetweenExpr(expr, start, end);
                        }
                        stmt.setTablePartitionBy(expr);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.TBPARTITIONS)) {
                        this.lexer.nextToken();
                        final SQLExpr tbPartitions = this.exprParser.primary();
                        stmt.setTablePartitions(tbPartitions);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.EXTPARTITION)) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        final MySqlExtPartition partitionDef = new MySqlExtPartition();
                        while (true) {
                            final MySqlExtPartition.Item item2 = new MySqlExtPartition.Item();
                            if (this.lexer.identifierEquals(FnvHash.Constants.DBPARTITION)) {
                                this.lexer.nextToken();
                                final SQLName name2 = this.exprParser.name();
                                item2.setDbPartition(name2);
                                this.accept(Token.BY);
                                final SQLExpr value = this.exprParser.primary();
                                item2.setDbPartitionBy(value);
                            }
                            if (this.lexer.identifierEquals(FnvHash.Constants.TBPARTITION)) {
                                this.lexer.nextToken();
                                final SQLName name2 = this.exprParser.name();
                                item2.setTbPartition(name2);
                                this.accept(Token.BY);
                                final SQLExpr value = this.exprParser.primary();
                                item2.setTbPartitionBy(value);
                            }
                            item2.setParent(partitionDef);
                            partitionDef.getItems().add(item2);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                        stmt.setExPartition(partitionDef);
                    }
                    else if (this.lexer.identifierEquals(FnvHash.Constants.OPTIONS)) {
                        this.lexer.nextToken();
                        this.accept(Token.LPAREN);
                        stmt.putAttribute("ads.options", Boolean.TRUE);
                        while (true) {
                            final String name3 = this.lexer.stringVal();
                            this.lexer.nextToken();
                            this.accept(Token.EQ);
                            final SQLExpr value2 = this.exprParser.primary();
                            stmt.addOption(name3, value2);
                            if (this.lexer.token() != Token.COMMA) {
                                break;
                            }
                            this.lexer.nextToken();
                        }
                        this.accept(Token.RPAREN);
                    }
                    else {
                        if (this.lexer.identifierEquals(FnvHash.Constants.STORED)) {
                            this.lexer.nextToken();
                            this.accept(Token.BY);
                            final SQLName name = this.exprParser.name();
                            stmt.setStoredBy(name);
                        }
                        if (this.lexer.token() == Token.WITH) {
                            this.lexer.nextToken();
                            this.accept(Token.LPAREN);
                            while (true) {
                                final String name3 = this.lexer.stringVal();
                                this.lexer.nextToken();
                                this.accept(Token.EQ);
                                final SQLName value3 = this.exprParser.name();
                                stmt.getWith().put(name3, value3);
                                if (this.lexer.token() != Token.COMMA) {
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            this.accept(Token.RPAREN);
                        }
                        else if (this.lexer.token() == Token.HINT) {
                            this.exprParser.parseHints(stmt.getOptionHints());
                        }
                        else {
                            if (this.lexer.token() == Token.ON) {
                                throw new ParserException("TODO. " + this.lexer.info());
                            }
                            if (this.lexer.token() == Token.REPLACE) {
                                this.lexer.nextToken();
                                stmt.setReplace(true);
                            }
                            else if (this.lexer.identifierEquals("IGNORE")) {
                                this.lexer.nextToken();
                                stmt.setIgnore(true);
                            }
                            if (this.lexer.token() == Token.AS) {
                                this.lexer.nextToken();
                                if (this.lexer.token() == Token.LPAREN) {
                                    this.lexer.nextToken();
                                    final SQLSelect query = new MySqlSelectParser(this.exprParser).select();
                                    stmt.setSelect(query);
                                    this.accept(Token.RPAREN);
                                }
                            }
                            SQLCommentHint hint = null;
                            if (this.lexer.token() == Token.HINT) {
                                hint = this.exprParser.parseHint();
                            }
                            if (this.lexer.token() == Token.SELECT) {
                                final SQLSelect query2 = new MySqlSelectParser(this.exprParser).select();
                                if (hint != null) {
                                    query2.setHeadHint(hint);
                                }
                                stmt.setSelect(query2);
                                if (this.lexer.token() == Token.WITH) {
                                    this.lexer.nextToken();
                                    if (this.lexer.identifierEquals(FnvHash.Constants.NO)) {
                                        this.lexer.nextToken();
                                        this.acceptIdentifier("DATA");
                                        stmt.setWithData(false);
                                    }
                                    else {
                                        this.acceptIdentifier("DATA");
                                        stmt.setWithData(true);
                                    }
                                }
                            }
                            while (this.lexer.token() == Token.HINT) {
                                this.exprParser.parseHints(stmt.getOptionHints());
                            }
                            return stmt;
                        }
                    }
                }
            }
        }
    }
    
    @Override
    public SQLPartitionBy parsePartitionBy() {
        this.lexer.nextToken();
        this.accept(Token.BY);
        boolean linera = false;
        if (this.lexer.identifierEquals(FnvHash.Constants.LINEAR)) {
            this.lexer.nextToken();
            linera = true;
        }
        SQLPartitionBy partitionClause;
        if (this.lexer.token() == Token.KEY) {
            final MySqlPartitionByKey clause = new MySqlPartitionByKey();
            this.lexer.nextToken();
            if (linera) {
                clause.setLinear(true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
                this.lexer.nextToken();
                this.accept(Token.EQ);
                clause.setAlgorithm(this.lexer.integerValue().shortValue());
                this.lexer.nextToken();
            }
            this.accept(Token.LPAREN);
            if (this.lexer.token() != Token.RPAREN) {
                while (true) {
                    clause.addColumn(this.exprParser.name());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
            }
            this.accept(Token.RPAREN);
            partitionClause = clause;
            this.partitionClauseRest(clause);
        }
        else if (this.lexer.identifierEquals("HASH") || this.lexer.identifierEquals("UNI_HASH")) {
            final SQLPartitionByHash clause2 = new SQLPartitionByHash();
            if (this.lexer.identifierEquals("UNI_HASH")) {
                clause2.setUnique(true);
            }
            this.lexer.nextToken();
            if (linera) {
                clause2.setLinear(true);
            }
            if (this.lexer.token() == Token.KEY) {
                this.lexer.nextToken();
                clause2.setKey(true);
            }
            this.accept(Token.LPAREN);
            this.exprParser.exprList(clause2.getColumns(), clause2);
            this.accept(Token.RPAREN);
            partitionClause = clause2;
            this.partitionClauseRest(clause2);
        }
        else if (this.lexer.identifierEquals("RANGE")) {
            final SQLPartitionByRange clause3 = (SQLPartitionByRange)(partitionClause = this.partitionByRange());
            this.partitionClauseRest(clause3);
        }
        else if (this.lexer.identifierEquals("VALUE")) {
            final SQLPartitionByValue clause4 = (SQLPartitionByValue)(partitionClause = this.partitionByValue());
            this.partitionClauseRest(clause4);
        }
        else if (this.lexer.identifierEquals("LIST")) {
            this.lexer.nextToken();
            final SQLPartitionByList clause5 = new SQLPartitionByList();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                clause5.addColumn(this.exprParser.expr());
                this.accept(Token.RPAREN);
            }
            else {
                this.acceptIdentifier("COLUMNS");
                this.accept(Token.LPAREN);
                while (true) {
                    clause5.addColumn(this.exprParser.name());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
            partitionClause = clause5;
            this.partitionClauseRest(clause5);
        }
        else {
            if (this.lexer.token() != Token.IDENTIFIER) {
                throw new ParserException("TODO. " + this.lexer.info());
            }
            final SQLPartitionByRange clause3 = (SQLPartitionByRange)(partitionClause = this.partitionByRange());
            this.partitionClauseRest(clause3);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
            this.lexer.nextToken();
            partitionClause.setLifecycle((SQLIntegerExpr)this.exprParser.expr());
        }
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            while (true) {
                final SQLPartition partitionDef = this.getExprParser().parsePartition();
                partitionClause.addPartition(partitionDef);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        return partitionClause;
    }
    
    protected SQLPartitionByRange partitionByRange1() {
        this.acceptIdentifier("RANGE");
        final SQLPartitionByRange clause = new SQLPartitionByRange();
        if (this.lexer.token() == Token.LPAREN) {
            this.lexer.nextToken();
            clause.addColumn(this.exprParser.expr());
            this.accept(Token.RPAREN);
        }
        else {
            this.acceptIdentifier("COLUMNS");
            this.accept(Token.LPAREN);
            while (true) {
                clause.addColumn(this.exprParser.name());
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            this.accept(Token.RPAREN);
        }
        return clause;
    }
    
    protected SQLPartitionByValue partitionByValue() {
        final SQLPartitionByValue clause = new SQLPartitionByValue();
        if (this.lexer.identifierEquals(FnvHash.Constants.VALUE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                clause.addColumn(this.exprParser.expr());
                this.accept(Token.RPAREN);
            }
        }
        return clause;
    }
    
    protected SQLPartitionByRange partitionByRange() {
        final SQLPartitionByRange clause = new SQLPartitionByRange();
        if (this.lexer.identifierEquals(FnvHash.Constants.RANGE)) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.LPAREN) {
                this.lexer.nextToken();
                clause.addColumn(this.exprParser.expr());
                this.accept(Token.RPAREN);
            }
            else {
                this.acceptIdentifier("COLUMNS");
                this.accept(Token.LPAREN);
                while (true) {
                    clause.addColumn(this.exprParser.name());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
            }
        }
        else {
            SQLExpr expr = this.exprParser.expr();
            if (this.lexer.identifierEquals(FnvHash.Constants.STARTWITH)) {
                this.lexer.nextToken();
                final SQLExpr start = this.exprParser.primary();
                this.acceptIdentifier("ENDWITH");
                final SQLExpr end = this.exprParser.primary();
                expr = new SQLBetweenExpr(expr, start, end);
            }
            clause.setInterval(expr);
        }
        return clause;
    }
    
    protected void partitionClauseRest(final SQLPartitionBy clause) {
        if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS) || this.lexer.identifierEquals(FnvHash.Constants.TBPARTITIONS) || this.lexer.identifierEquals(FnvHash.Constants.DBPARTITIONS)) {
            this.lexer.nextToken();
            final SQLIntegerExpr countExpr = this.exprParser.integerExpr();
            clause.setPartitionsCount(countExpr);
        }
        if (this.lexer.token() == Token.PARTITION) {
            this.lexer.nextToken();
            if (this.lexer.identifierEquals("NUM")) {
                this.lexer.nextToken();
            }
            clause.setPartitionsCount(this.exprParser.expr());
            clause.putAttribute("ads.partition", Boolean.TRUE);
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
            this.lexer.nextToken();
            clause.setLifecycle((SQLIntegerExpr)this.exprParser.expr());
        }
        if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
            this.lexer.nextToken();
            this.accept(Token.BY);
            SQLSubPartitionBy subPartitionByClause = null;
            boolean linear = false;
            if (this.lexer.identifierEquals("LINEAR")) {
                this.lexer.nextToken();
                linear = true;
            }
            if (this.lexer.token() == Token.KEY) {
                final MySqlSubPartitionByKey subPartitionKey = new MySqlSubPartitionByKey();
                this.lexer.nextToken();
                if (linear) {
                    clause.setLinear(true);
                }
                if (this.lexer.identifierEquals(FnvHash.Constants.ALGORITHM)) {
                    this.lexer.nextToken();
                    this.accept(Token.EQ);
                    subPartitionKey.setAlgorithm(this.lexer.integerValue().shortValue());
                    this.lexer.nextToken();
                }
                this.accept(Token.LPAREN);
                while (true) {
                    subPartitionKey.addColumn(this.exprParser.name());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                subPartitionByClause = subPartitionKey;
            }
            else if (this.lexer.identifierEquals("VALUE")) {
                final MySqlSubPartitionByValue subPartitionByValue = new MySqlSubPartitionByValue();
                this.lexer.nextToken();
                this.accept(Token.LPAREN);
                while (true) {
                    subPartitionByValue.addColumn(this.exprParser.expr());
                    if (this.lexer.token() != Token.COMMA) {
                        break;
                    }
                    this.lexer.nextToken();
                }
                this.accept(Token.RPAREN);
                subPartitionByClause = subPartitionByValue;
            }
            else if (this.lexer.identifierEquals("HASH")) {
                this.lexer.nextToken();
                final SQLSubPartitionByHash subPartitionHash = new SQLSubPartitionByHash();
                if (linear) {
                    clause.setLinear(true);
                }
                if (this.lexer.token() == Token.KEY) {
                    this.lexer.nextToken();
                    subPartitionHash.setKey(true);
                }
                this.accept(Token.LPAREN);
                subPartitionHash.setExpr(this.exprParser.expr());
                this.accept(Token.RPAREN);
                subPartitionByClause = subPartitionHash;
            }
            else if (this.lexer.identifierEquals("LIST")) {
                this.lexer.nextToken();
                final MySqlSubPartitionByList subPartitionList = new MySqlSubPartitionByList();
                if (this.lexer.token() == Token.KEY) {
                    this.lexer.nextToken();
                    this.accept(Token.LPAREN);
                    while (true) {
                        final SQLExpr expr = this.exprParser.expr();
                        if (expr instanceof SQLIdentifierExpr && (this.lexer.identifierEquals("bigint") || this.lexer.identifierEquals("long"))) {
                            final String dataType = this.lexer.stringVal();
                            this.lexer.nextToken();
                            final SQLColumnDefinition column = this.exprParser.createColumnDefinition();
                            column.setName((SQLName)expr);
                            column.setDataType(new SQLDataTypeImpl(dataType));
                            subPartitionList.addColumn(column);
                            subPartitionList.putAttribute("ads.subPartitionList", Boolean.TRUE);
                        }
                        subPartitionList.addKey(expr);
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    subPartitionList.putAttribute("ads.subPartitionList", Boolean.TRUE);
                    this.accept(Token.RPAREN);
                }
                else if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    SQLExpr expr;
                    if (this.lexer.token() == Token.LITERAL_ALIAS) {
                        expr = new SQLIdentifierExpr(this.lexer.stringVal());
                        this.lexer.nextToken();
                    }
                    else {
                        expr = this.exprParser.expr();
                    }
                    if (expr instanceof SQLIdentifierExpr && (this.lexer.identifierEquals("bigint") || this.lexer.identifierEquals("long"))) {
                        final String dataType = this.lexer.stringVal();
                        this.lexer.nextToken();
                        final SQLColumnDefinition column = this.exprParser.createColumnDefinition();
                        column.setName((SQLName)expr);
                        column.setDataType(new SQLDataTypeImpl(dataType));
                        subPartitionList.addColumn(column);
                        subPartitionList.putAttribute("ads.subPartitionList", Boolean.TRUE);
                    }
                    else {
                        subPartitionList.addKey(expr);
                    }
                    this.accept(Token.RPAREN);
                }
                else {
                    this.acceptIdentifier("COLUMNS");
                    this.accept(Token.LPAREN);
                    while (true) {
                        subPartitionList.addColumn(this.exprParser.parseColumn());
                        if (this.lexer.token() != Token.COMMA) {
                            break;
                        }
                        this.lexer.nextToken();
                    }
                    this.accept(Token.RPAREN);
                }
                subPartitionByClause = subPartitionList;
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.RANGE)) {
                this.lexer.nextToken();
                final SQLSubPartitionByRange range = new SQLSubPartitionByRange();
                this.accept(Token.LPAREN);
                this.exprParser.exprList(range.getColumns(), range);
                this.accept(Token.RPAREN);
                subPartitionByClause = range;
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITION)) {
                this.lexer.nextToken();
                this.acceptIdentifier("OPTIONS");
                this.exprParser.parseAssignItem(subPartitionByClause.getOptions(), subPartitionByClause);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.SUBPARTITIONS)) {
                this.lexer.nextToken();
                final Number intValue = this.lexer.integerValue();
                final SQLNumberExpr numExpr = new SQLNumberExpr(intValue);
                subPartitionByClause.setSubPartitionsCount(numExpr);
                this.lexer.nextToken();
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PARTITIONS)) {
                this.lexer.nextToken();
                subPartitionByClause.setSubPartitionsCount(this.exprParser.expr());
                subPartitionByClause.getAttributes().put("adb.partitons", true);
            }
            if (this.lexer.identifierEquals(FnvHash.Constants.LIFECYCLE)) {
                this.lexer.nextToken();
                subPartitionByClause.setLifecycle((SQLIntegerExpr)this.exprParser.expr());
            }
            if (subPartitionByClause != null) {
                subPartitionByClause.setLinear(linear);
                clause.setSubPartitionBy(subPartitionByClause);
            }
        }
    }
    
    private boolean parseTableOptionCharsetOrCollate(final MySqlCreateTableStatement stmt) {
        if (this.lexer.identifierEquals("CHARACTER")) {
            this.lexer.nextToken();
            this.accept(Token.SET);
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            SQLExpr charset;
            if (this.lexer.token() == Token.IDENTIFIER) {
                charset = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.LITERAL_CHARS) {
                charset = new SQLCharExpr(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else {
                charset = this.exprParser.primary();
            }
            stmt.addOption("CHARACTER SET", charset);
            return true;
        }
        if (this.lexer.identifierEquals("CHARSET")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            SQLExpr charset;
            if (this.lexer.token() == Token.IDENTIFIER) {
                charset = new SQLIdentifierExpr(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else if (this.lexer.token() == Token.LITERAL_CHARS) {
                charset = new SQLCharExpr(this.lexer.stringVal());
                this.lexer.nextToken();
            }
            else {
                charset = this.exprParser.primary();
            }
            stmt.addOption("CHARSET", charset);
            return true;
        }
        if (this.lexer.identifierEquals("COLLATE")) {
            this.lexer.nextToken();
            if (this.lexer.token() == Token.EQ) {
                this.lexer.nextToken();
            }
            stmt.addOption("COLLATE", this.exprParser.expr());
            return true;
        }
        return false;
    }
    
    @Override
    protected SQLTableConstraint parseConstraint() {
        SQLName name = null;
        boolean hasConstaint = false;
        if (this.lexer.token() == Token.CONSTRAINT) {
            hasConstaint = true;
            this.lexer.nextToken();
        }
        if (this.lexer.token() == Token.IDENTIFIER) {
            name = this.exprParser.name();
        }
        SQLTableConstraint constraint = null;
        if (this.lexer.token() == Token.KEY) {
            final MySqlKey key = new MySqlKey();
            this.exprParser.parseIndex(key.getIndexDefinition());
            key.setHasConstraint(hasConstaint);
            if (name != null) {
                key.setName(name);
            }
            constraint = key;
        }
        else if (this.lexer.token() == Token.PRIMARY) {
            final MySqlPrimaryKey pk = this.getExprParser().parsePrimaryKey();
            if (name != null) {
                pk.setName(name);
            }
            pk.setHasConstraint(hasConstaint);
            constraint = pk;
        }
        else if (this.lexer.token() == Token.UNIQUE) {
            final MySqlUnique uk = this.getExprParser().parseUnique();
            if (name != null && uk.getName() == null) {
                uk.setName(name);
            }
            uk.setHasConstraint(hasConstaint);
            constraint = uk;
        }
        else if (this.lexer.token() == Token.FOREIGN) {
            final MysqlForeignKey fk = this.getExprParser().parseForeignKey();
            fk.setName(name);
            fk.setHasConstraint(hasConstaint);
            constraint = fk;
        }
        else if (this.lexer.token() == Token.CHECK) {
            this.lexer.nextToken();
            final SQLCheck check = new SQLCheck();
            check.setName(name);
            final SQLExpr expr = this.exprParser.primary();
            check.setExpr(expr);
            constraint = check;
            boolean enforce = true;
            if (Token.NOT.equals(this.lexer.token())) {
                enforce = false;
                this.lexer.nextToken();
            }
            if (this.lexer.stringVal().equalsIgnoreCase("ENFORCED")) {
                check.setEnforced(enforce);
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.HINT) {
                String hintText = this.lexer.stringVal();
                if (hintText != null) {
                    hintText = hintText.trim();
                }
                if (hintText.startsWith("!")) {
                    if (hintText.endsWith("NOT ENFORCED")) {
                        check.setEnforced(false);
                    }
                    else if (hintText.endsWith(" ENFORCED")) {
                        check.setEnforced(true);
                    }
                    this.lexer.nextToken();
                }
            }
        }
        if (constraint != null) {
            if (this.lexer.token() == Token.COMMENT) {
                this.lexer.nextToken();
                final SQLExpr comment = this.exprParser.primary();
                constraint.setComment(comment);
            }
            return constraint;
        }
        throw new ParserException("TODO. " + this.lexer.info());
    }
}
