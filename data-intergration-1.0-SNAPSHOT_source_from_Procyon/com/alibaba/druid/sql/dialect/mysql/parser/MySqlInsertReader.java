// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.mysql.parser;

import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import java.io.IOException;
import com.alibaba.druid.sql.parser.SQLParserFeature;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import java.io.Reader;
import java.io.Closeable;

public class MySqlInsertReader implements Closeable
{
    private final Reader in;
    private char[] buf;
    private int pos;
    private char ch;
    MySqlStatementParser parser;
    private MySqlInsertStatement statement;
    
    public MySqlInsertReader(final Reader in) {
        this.buf = new char[1024];
        this.in = in;
    }
    
    public MySqlInsertStatement parseStatement() throws IOException {
        this.in.read(this.buf);
        final String text = new String(this.buf);
        this.parser = new MySqlStatementParser(text, new SQLParserFeature[] { SQLParserFeature.InsertReader });
        this.statement = (MySqlInsertStatement)this.parser.parseStatement();
        this.pos = this.parser.getLexer().pos() - 1;
        this.ch = this.buf[this.pos];
        return this.statement;
    }
    
    public MySqlInsertStatement getStatement() {
        return this.statement;
    }
    
    public SQLInsertStatement.ValuesClause readCaluse() {
        return null;
    }
    
    public boolean isEOF() {
        return false;
    }
    
    @Override
    public void close() {
        JdbcUtils.close(this.in);
    }
}
