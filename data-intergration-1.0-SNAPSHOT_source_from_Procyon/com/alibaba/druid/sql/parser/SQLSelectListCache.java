// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.util.FnvHash;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;
import com.alibaba.druid.DbType;
import com.alibaba.druid.support.logging.Log;

public class SQLSelectListCache
{
    private static final Log LOG;
    private final DbType dbType;
    private final List<Entry> entries;
    
    public SQLSelectListCache(final DbType dbType) {
        this.entries = new CopyOnWriteArrayList<Entry>();
        this.dbType = dbType;
    }
    
    public void add(final String select) {
        if (select == null || select.length() == 0) {
            return;
        }
        final SQLSelectParser selectParser = SQLParserUtils.createSQLStatementParser(select, this.dbType).createSQLSelectParser();
        final SQLSelectQueryBlock queryBlock = SQLParserUtils.createSelectQueryBlock(this.dbType);
        selectParser.accept(Token.SELECT);
        selectParser.parseSelectList(queryBlock);
        selectParser.accept(Token.FROM);
        selectParser.accept(Token.EOF);
        final String printSql = queryBlock.toString();
        final long printSqlHash = FnvHash.fnv1a_64_lower(printSql);
        this.entries.add(new Entry(select.substring(6), queryBlock, printSql, printSqlHash));
        if (this.entries.size() > 5) {
            SQLSelectListCache.LOG.warn("SelectListCache is too large.");
        }
    }
    
    public int getSize() {
        return this.entries.size();
    }
    
    public void clear() {
        this.entries.clear();
    }
    
    public boolean match(final Lexer lexer, final SQLSelectQueryBlock queryBlock) {
        if (lexer.token != Token.SELECT) {
            return false;
        }
        final int pos = lexer.pos;
        final String text = lexer.text;
        for (int i = 0; i < this.entries.size(); ++i) {
            final Entry entry = this.entries.get(i);
            final String block = entry.sql;
            if (text.startsWith(block, pos)) {
                queryBlock.setCachedSelectList(entry.printSql, entry.printSqlHash);
                final int len = pos + block.length();
                lexer.reset(len, text.charAt(len), Token.FROM);
                return true;
            }
        }
        return false;
    }
    
    static {
        LOG = LogFactory.getLog(SQLSelectListCache.class);
    }
    
    private static class Entry
    {
        public final String sql;
        public final SQLSelectQueryBlock queryBlock;
        public final String printSql;
        public final long printSqlHash;
        
        public Entry(final String sql, final SQLSelectQueryBlock queryBlock, final String printSql, final long printSqlHash) {
            this.sql = sql;
            this.queryBlock = queryBlock;
            this.printSql = printSql;
            this.printSqlHash = printSqlHash;
        }
    }
}
