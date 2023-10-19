// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.parser;

import com.alibaba.druid.sql.ast.statement.SQLCreateTableStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalIdKey;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSupplementalLogGrp;
import com.alibaba.druid.sql.ast.statement.SQLTableElement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.statement.SQLExternalRecordFormat;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.SQLPartitionByList;
import com.alibaba.druid.sql.ast.SQLPartition;
import com.alibaba.druid.sql.ast.SQLPartitionByHash;
import com.alibaba.druid.sql.ast.SQLPartitionByRange;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleLobStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.clause.OracleStorageClause;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleLobParameters;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleXmlColumnProperties;
import com.alibaba.druid.sql.ast.SQLPartitionBy;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.Collection;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.parser.ParserException;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSegmentAttributes;
import com.alibaba.druid.sql.parser.Token;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import com.alibaba.druid.sql.parser.SQLExprParser;
import com.alibaba.druid.sql.parser.Lexer;
import com.alibaba.druid.sql.parser.SQLCreateTableParser;

public class OracleCreateTableParser extends SQLCreateTableParser
{
    public OracleCreateTableParser(final Lexer lexer) {
        super(new OracleExprParser(lexer));
    }
    
    public OracleCreateTableParser(final String sql) {
        super(new OracleExprParser(sql));
    }
    
    @Override
    protected OracleCreateTableStatement newCreateStatement() {
        return new OracleCreateTableStatement();
    }
    
    @Override
    public OracleCreateTableStatement parseCreateTable(final boolean acceptCreate) {
        final OracleCreateTableStatement stmt = (OracleCreateTableStatement)super.parseCreateTable(acceptCreate);
        if (this.lexer.token() == Token.OF) {
            this.lexer.nextToken();
            stmt.setOf(this.exprParser.name());
            if (this.lexer.identifierEquals("OIDINDEX")) {
                this.lexer.nextToken();
                final OracleCreateTableStatement.OIDIndex oidIndex = new OracleCreateTableStatement.OIDIndex();
                if (this.lexer.token() != Token.LPAREN) {
                    oidIndex.setName(this.exprParser.name());
                }
                this.accept(Token.LPAREN);
                this.getExprParser().parseSegmentAttributes(oidIndex);
                this.accept(Token.RPAREN);
                stmt.setOidIndex(oidIndex);
            }
        }
        while (true) {
            this.getExprParser().parseSegmentAttributes(stmt);
            if (this.lexer.identifierEquals(FnvHash.Constants.IN_MEMORY_METADATA)) {
                this.lexer.nextToken();
                stmt.setInMemoryMetadata(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.CURSOR_SPECIFIC_SEGMENT)) {
                this.lexer.nextToken();
                stmt.setCursorSpecificSegment(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.NOPARALLEL)) {
                this.lexer.nextToken();
                stmt.setParallel(false);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.PARALLEL)) {
                this.lexer.nextToken();
                stmt.setParallel(true);
                if (this.lexer.token() != Token.LITERAL_INT) {
                    continue;
                }
                stmt.setParallelValue(this.exprParser.primary());
            }
            else if (this.lexer.token() == Token.CACHE) {
                this.lexer.nextToken();
                stmt.setCache(Boolean.TRUE);
            }
            else if (this.lexer.token() == Token.NOCACHE) {
                this.lexer.nextToken();
                stmt.setCache(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.ENABLE) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.ROW) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                this.lexer.nextToken();
                this.acceptIdentifier("MOVEMENT");
                stmt.setEnableRowMovement(Boolean.TRUE);
            }
            else if (this.lexer.token() == Token.DISABLE) {
                this.lexer.nextToken();
                if (this.lexer.token() != Token.ROW) {
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                this.lexer.nextToken();
                this.acceptIdentifier("MOVEMENT");
                stmt.setEnableRowMovement(Boolean.FALSE);
            }
            else if (this.lexer.token() == Token.ON) {
                this.lexer.nextToken();
                this.accept(Token.COMMIT);
                if (this.lexer.identifierEquals("PRESERVE")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("ROWS");
                    stmt.setOnCommitPreserveRows(true);
                }
                else {
                    this.accept(Token.DELETE);
                    this.acceptIdentifier("ROWS");
                    stmt.setOnCommitDeleteRows(true);
                }
            }
            else if (this.lexer.identifierEquals("STORAGE")) {
                final OracleStorageClause storage = ((OracleExprParser)this.exprParser).parseStorage();
                stmt.setStorage(storage);
            }
            else if (this.lexer.identifierEquals("ORGANIZATION")) {
                this.parseOrganization(stmt);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.CLUSTER)) {
                this.lexer.nextToken();
                final SQLName cluster = this.exprParser.name();
                stmt.setCluster(cluster);
                this.accept(Token.LPAREN);
                this.exprParser.names(stmt.getClusterColumns(), cluster);
                this.accept(Token.RPAREN);
            }
            else if (this.lexer.identifierEquals("MONITORING")) {
                this.lexer.nextToken();
                stmt.setMonitoring(true);
            }
            else if (this.lexer.identifierEquals(FnvHash.Constants.INCLUDING)) {
                this.lexer.nextToken();
                this.exprParser.names(stmt.getIncluding(), stmt);
                this.acceptIdentifier("OVERFLOW");
            }
            else if (this.lexer.token() == Token.LOB) {
                final OracleLobStorageClause lobStorage = ((OracleExprParser)this.exprParser).parseLobStorage();
                stmt.setLobStorage(lobStorage);
            }
            else if (this.lexer.token() == Token.SEGMENT) {
                this.lexer.nextToken();
                this.accept(Token.CREATION);
                if (this.lexer.token() == Token.IMMEDIATE) {
                    this.lexer.nextToken();
                    stmt.setDeferredSegmentCreation(OracleCreateTableStatement.DeferredSegmentCreation.IMMEDIATE);
                }
                else {
                    this.accept(Token.DEFERRED);
                    stmt.setDeferredSegmentCreation(OracleCreateTableStatement.DeferredSegmentCreation.DEFERRED);
                }
            }
            else if (this.lexer.token() == Token.COLUMN) {
                this.lexer.nextToken();
                final SQLName name = this.exprParser.name();
                if (this.lexer.token() == Token.NOT) {
                    this.lexer.nextToken();
                }
                if (!this.lexer.identifierEquals(FnvHash.Constants.SUBSTITUTABLE)) {
                    continue;
                }
                this.lexer.nextToken();
                this.acceptIdentifier("AT");
                this.accept(Token.ALL);
                this.acceptIdentifier("LEVELS");
            }
            else {
                if (this.lexer.identifierEquals(FnvHash.Constants.VARRAY)) {
                    this.lexer.nextToken();
                    final SQLName name = this.exprParser.name();
                    this.accept(Token.STORE);
                    this.accept(Token.AS);
                    if (this.lexer.identifierEquals(FnvHash.Constants.BASICFILE)) {
                        this.lexer.nextToken();
                    }
                    this.getExprParser().parseLobStorage();
                    throw new ParserException("TODO : " + this.lexer.info());
                }
                if (this.lexer.token() == Token.PARTITION) {
                    this.lexer.nextToken();
                    this.accept(Token.BY);
                    if (this.lexer.identifierEquals("RANGE")) {
                        final SQLPartitionByRange partitionByRange = this.getExprParser().partitionByRange();
                        this.getExprParser().partitionClauseRest(partitionByRange);
                        stmt.setPartitioning(partitionByRange);
                    }
                    else if (this.lexer.identifierEquals("HASH")) {
                        final SQLPartitionByHash partitionByHash = this.getExprParser().partitionByHash();
                        this.getExprParser().partitionClauseRest(partitionByHash);
                        if (this.lexer.token() == Token.LPAREN) {
                            this.lexer.nextToken();
                            while (true) {
                                final SQLPartition partition = this.getExprParser().parsePartition();
                                partitionByHash.addPartition(partition);
                                if (this.lexer.token() != Token.COMMA) {
                                    break;
                                }
                                this.lexer.nextToken();
                            }
                            if (this.lexer.token() != Token.RPAREN) {
                                throw new ParserException("TODO : " + this.lexer.info());
                            }
                            this.lexer.nextToken();
                        }
                        stmt.setPartitioning(partitionByHash);
                    }
                    else {
                        if (!this.lexer.identifierEquals("LIST")) {
                            throw new ParserException("TODO : " + this.lexer.info());
                        }
                        final SQLPartitionByList partitionByList = this.partitionByList();
                        this.getExprParser().partitionClauseRest(partitionByList);
                        stmt.setPartitioning(partitionByList);
                    }
                }
                else {
                    if (!this.lexer.identifierEquals(FnvHash.Constants.XMLTYPE)) {
                        if (this.lexer.token() == Token.AS) {
                            this.lexer.nextToken();
                            final SQLSelect select = new OracleSelectParser(this.exprParser).select();
                            stmt.setSelect(select);
                        }
                        return stmt;
                    }
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.COLUMN) {
                        this.lexer.nextToken();
                    }
                    final OracleXmlColumnProperties xmlColumnProperties = new OracleXmlColumnProperties();
                    xmlColumnProperties.setColumn(this.exprParser.name());
                    if (this.lexer.token() == Token.STORE) {
                        this.lexer.nextToken();
                        this.accept(Token.AS);
                        final OracleXmlColumnProperties.OracleXMLTypeStorage storage2 = new OracleXmlColumnProperties.OracleXMLTypeStorage();
                        if (this.lexer.identifierEquals("SECUREFILE")) {
                            storage2.setSecureFile(true);
                            this.lexer.nextToken();
                        }
                        else if (this.lexer.identifierEquals("BASICFILE")) {
                            storage2.setBasicFile(true);
                            this.lexer.nextToken();
                        }
                        if (this.lexer.identifierEquals("BINARY")) {
                            this.lexer.nextToken();
                            this.acceptIdentifier("XML");
                            storage2.setBinaryXml(true);
                        }
                        else if (this.lexer.identifierEquals("CLOB")) {
                            this.lexer.nextToken();
                            storage2.setClob(true);
                        }
                        if (this.lexer.token() == Token.LPAREN) {
                            this.lexer.nextToken();
                            final OracleLobParameters lobParameters = new OracleLobParameters();
                        Label_1903:
                            while (true) {
                                switch (this.lexer.token()) {
                                    case TABLESPACE: {
                                        this.lexer.nextToken();
                                        final SQLName tableSpace = this.exprParser.name();
                                        lobParameters.setTableSpace(tableSpace);
                                        continue;
                                    }
                                    case ENABLE:
                                    case DISABLE: {
                                        final Boolean enable = this.lexer.token() == Token.ENABLE;
                                        this.lexer.nextToken();
                                        this.accept(Token.STORAGE);
                                        this.accept(Token.IN);
                                        this.accept(Token.ROW);
                                        lobParameters.setEnableStorageInRow(enable);
                                        continue;
                                    }
                                    case CHUNK: {
                                        this.lexer.nextToken();
                                        final SQLExpr chunk = this.exprParser.expr();
                                        lobParameters.setChunk(chunk);
                                        continue;
                                    }
                                    case NOCACHE: {
                                        this.lexer.nextToken();
                                        lobParameters.setCache(false);
                                        continue;
                                    }
                                    case LOGGING: {
                                        this.lexer.nextToken();
                                        lobParameters.setLogging(true);
                                        continue;
                                    }
                                    case NOCOMPRESS: {
                                        this.lexer.nextToken();
                                        lobParameters.setCompress(false);
                                        continue;
                                    }
                                    case KEEP_DUPLICATES: {
                                        this.lexer.nextToken();
                                        lobParameters.setKeepDuplicates(true);
                                        continue;
                                    }
                                    case STORAGE: {
                                        final OracleStorageClause storageClause = this.getExprParser().parseStorage();
                                        lobParameters.setStorage(storageClause);
                                        continue;
                                    }
                                    case IDENTIFIER: {
                                        final long hash = this.lexer.hash_lower();
                                        if (hash == FnvHash.Constants.PCTVERSION) {
                                            lobParameters.setPctVersion(this.exprParser.primary());
                                            this.lexer.nextToken();
                                            continue;
                                        }
                                        break Label_1903;
                                    }
                                    default: {
                                        break Label_1903;
                                    }
                                }
                            }
                            this.accept(Token.RPAREN);
                            storage2.setLobParameters(lobParameters);
                        }
                    }
                    while (true) {
                        if (this.lexer.identifierEquals(FnvHash.Constants.ALLOW)) {
                            this.lexer.nextToken();
                            if (this.lexer.identifierEquals("NONSCHEMA")) {
                                this.lexer.nextToken();
                                xmlColumnProperties.setAllowNonSchema(true);
                            }
                            else {
                                if (!this.lexer.identifierEquals("ANYSCHEMA")) {
                                    throw new ParserException("TODO : " + this.lexer.info());
                                }
                                this.lexer.nextToken();
                                xmlColumnProperties.setAllowAnySchema(true);
                            }
                        }
                        else {
                            if (!this.lexer.identifierEquals(FnvHash.Constants.DISALLOW)) {
                                stmt.setXmlTypeColumnProperties(xmlColumnProperties);
                                break;
                            }
                            this.lexer.nextToken();
                            if (this.lexer.identifierEquals("NONSCHEMA")) {
                                this.lexer.nextToken();
                                xmlColumnProperties.setAllowNonSchema(false);
                            }
                            else {
                                if (!this.lexer.identifierEquals("ANYSCHEMA")) {
                                    throw new ParserException("TODO : " + this.lexer.info());
                                }
                                this.lexer.nextToken();
                                xmlColumnProperties.setAllowAnySchema(false);
                            }
                        }
                    }
                }
            }
        }
    }
    
    private void parseOrganization(final OracleCreateTableStatement stmt) {
        final OracleCreateTableStatement.Organization organization = new OracleCreateTableStatement.Organization();
        this.acceptIdentifier("ORGANIZATION");
        if (this.lexer.token() == Token.INDEX) {
            this.lexer.nextToken();
            organization.setType("INDEX");
            this.getExprParser().parseSegmentAttributes(organization);
            if (this.lexer.identifierEquals(FnvHash.Constants.PCTTHRESHOLD)) {
                this.lexer.nextToken();
                if (this.lexer.token() == Token.LITERAL_INT) {
                    final int pctthreshold = ((SQLNumericLiteralExpr)this.exprParser.primary()).getNumber().intValue();
                    organization.setPctthreshold(pctthreshold);
                }
            }
        }
        else if (this.lexer.identifierEquals("HEAP")) {
            this.lexer.nextToken();
            organization.setType("HEAP");
            this.getExprParser().parseSegmentAttributes(organization);
        }
        else {
            if (!this.lexer.identifierEquals("EXTERNAL")) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            organization.setType("EXTERNAL");
            this.accept(Token.LPAREN);
            if (this.lexer.identifierEquals("TYPE")) {
                this.lexer.nextToken();
                organization.setExternalType(this.exprParser.name());
            }
            this.accept(Token.DEFAULT);
            this.acceptIdentifier("DIRECTORY");
            organization.setExternalDirectory(this.exprParser.expr());
            if (this.lexer.identifierEquals("ACCESS")) {
                this.lexer.nextToken();
                this.acceptIdentifier("PARAMETERS");
                if (this.lexer.token() == Token.LPAREN) {
                    this.lexer.nextToken();
                    final SQLExternalRecordFormat recordFormat = new SQLExternalRecordFormat();
                    if (this.lexer.identifierEquals("RECORDS")) {
                        this.lexer.nextToken();
                        if (!this.lexer.identifierEquals("DELIMITED")) {
                            throw new ParserException("TODO " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        if (!this.lexer.identifierEquals("NEWLINE")) {
                            throw new ParserException("TODO " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        recordFormat.setDelimitedBy(new SQLIdentifierExpr("NEWLINE"));
                        if (this.lexer.identifierEquals(FnvHash.Constants.NOLOGFILE)) {
                            this.lexer.nextToken();
                            recordFormat.setLogfile(false);
                        }
                        if (this.lexer.identifierEquals(FnvHash.Constants.NOBADFILE)) {
                            this.lexer.nextToken();
                            recordFormat.setBadfile(false);
                        }
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.FIELDS)) {
                        this.lexer.nextToken();
                        if (!this.lexer.identifierEquals(FnvHash.Constants.TERMINATED)) {
                            throw new ParserException("TODO " + this.lexer.info());
                        }
                        this.lexer.nextToken();
                        this.accept(Token.BY);
                        recordFormat.setTerminatedBy(this.exprParser.primary());
                        if (this.lexer.identifierEquals(FnvHash.Constants.LTRIM)) {
                            this.lexer.nextToken();
                            recordFormat.setLtrim(true);
                        }
                    }
                    if (this.lexer.identifierEquals(FnvHash.Constants.MISSING)) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("FIELD");
                        this.accept(Token.VALUES);
                        this.acceptIdentifier("ARE");
                        this.accept(Token.NULL);
                        recordFormat.setMissingFieldValuesAreNull(true);
                    }
                    if (this.lexer.token() == Token.REJECT) {
                        this.lexer.nextToken();
                        this.acceptIdentifier("ROWS");
                        this.accept(Token.WITH);
                        this.accept(Token.ALL);
                        this.accept(Token.NULL);
                        this.acceptIdentifier("FIELDS");
                        recordFormat.setRejectRowsWithAllNullFields(true);
                    }
                    organization.setExternalDirectoryRecordFormat(recordFormat);
                    this.accept(Token.RPAREN);
                }
                else if (this.lexer.token() == Token.USING) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("CLOB");
                    throw new ParserException("TODO " + this.lexer.info());
                }
            }
            this.acceptIdentifier("LOCATION");
            this.accept(Token.LPAREN);
            this.exprParser.exprList(organization.getExternalDirectoryLocation(), organization);
            this.accept(Token.RPAREN);
            this.accept(Token.RPAREN);
            if (this.lexer.token() == Token.REJECT) {
                this.lexer.nextToken();
                this.accept(Token.LIMIT);
                organization.setExternalRejectLimit(this.exprParser.primary());
            }
        }
        stmt.setOrganization(organization);
    }
    
    protected SQLPartitionByList partitionByList() {
        this.acceptIdentifier("LIST");
        final SQLPartitionByList partitionByList = new SQLPartitionByList();
        this.accept(Token.LPAREN);
        partitionByList.addColumn(this.exprParser.expr());
        this.accept(Token.RPAREN);
        this.getExprParser().parsePartitionByRest(partitionByList);
        return partitionByList;
    }
    
    @Override
    protected SQLTableElement parseCreateTableSupplementalLogingProps() {
        this.acceptIdentifier("SUPPLEMENTAL");
        this.acceptIdentifier("LOG");
        if (this.lexer.token() == Token.GROUP) {
            this.lexer.nextToken();
            final OracleSupplementalLogGrp logGrp = new OracleSupplementalLogGrp();
            logGrp.setGroup(this.exprParser.name());
            this.accept(Token.LPAREN);
            while (true) {
                final SQLName column = this.exprParser.name();
                if (this.lexer.identifierEquals("NO")) {
                    this.lexer.nextToken();
                    this.acceptIdentifier("LOG");
                    column.putAttribute("NO LOG", Boolean.TRUE);
                }
                logGrp.addColumn(column);
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.RPAREN) {
                this.accept(Token.RPAREN);
                if (this.lexer.identifierEquals("ALWAYS")) {
                    this.lexer.nextToken();
                    logGrp.setAlways(true);
                }
                return logGrp;
            }
            throw new ParserException("TODO " + this.lexer.info());
        }
        else {
            if (!this.lexer.identifierEquals(FnvHash.Constants.DATA)) {
                throw new ParserException("TODO " + this.lexer.info());
            }
            this.lexer.nextToken();
            final OracleSupplementalIdKey idKey = new OracleSupplementalIdKey();
            this.accept(Token.LPAREN);
            while (true) {
                if (this.lexer.token() == Token.ALL) {
                    this.lexer.nextToken();
                    idKey.setAll(true);
                }
                else if (this.lexer.token() == Token.PRIMARY) {
                    this.lexer.nextToken();
                    this.accept(Token.KEY);
                    idKey.setPrimaryKey(true);
                }
                else if (this.lexer.token() == Token.UNIQUE) {
                    this.lexer.nextToken();
                    if (this.lexer.token() == Token.INDEX) {
                        this.lexer.nextToken();
                        idKey.setUniqueIndex(true);
                    }
                    else {
                        idKey.setUnique(true);
                    }
                }
                else if (this.lexer.token() == Token.FOREIGN) {
                    this.lexer.nextToken();
                    this.accept(Token.KEY);
                    idKey.setForeignKey(true);
                }
                if (this.lexer.token() != Token.COMMA) {
                    break;
                }
                this.lexer.nextToken();
            }
            if (this.lexer.token() == Token.RPAREN) {
                this.accept(Token.RPAREN);
                this.acceptIdentifier("COLUMNS");
                return idKey;
            }
            throw new ParserException("TODO " + this.lexer.info());
        }
    }
    
    @Override
    public OracleExprParser getExprParser() {
        return (OracleExprParser)this.exprParser;
    }
}
