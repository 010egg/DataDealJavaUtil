// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLHint;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public abstract class SQLTableSourceImpl extends SQLObjectImpl implements SQLTableSource
{
    protected String alias;
    protected List<SQLHint> hints;
    protected SQLExpr flashback;
    protected long aliasHashCode64;
    
    public SQLTableSourceImpl() {
    }
    
    public SQLTableSourceImpl(final String alias) {
        this.alias = alias;
    }
    
    @Override
    public String getAlias() {
        return this.alias;
    }
    
    public String getAlias2() {
        if (this.alias == null || this.alias.length() == 0) {
            return this.alias;
        }
        final char first = this.alias.charAt(0);
        if (first == '\"' || first == '\'') {
            final char[] chars = new char[this.alias.length() - 2];
            int len = 0;
            for (int i = 1; i < this.alias.length() - 1; ++i) {
                char ch = this.alias.charAt(i);
                if (ch == '\\') {
                    ++i;
                    ch = this.alias.charAt(i);
                }
                chars[len++] = ch;
            }
            return new String(chars, 0, len);
        }
        return this.alias;
    }
    
    @Override
    public void setAlias(final String alias) {
        this.alias = alias;
        this.aliasHashCode64 = 0L;
    }
    
    public int getHintsSize() {
        if (this.hints == null) {
            return 0;
        }
        return this.hints.size();
    }
    
    @Override
    public List<SQLHint> getHints() {
        if (this.hints == null) {
            this.hints = new ArrayList<SQLHint>(2);
        }
        return this.hints;
    }
    
    public void setHints(final List<SQLHint> hints) {
        this.hints = hints;
    }
    
    @Override
    public SQLTableSource clone() {
        throw new UnsupportedOperationException(this.getClass().getName());
    }
    
    @Override
    public String computeAlias() {
        return this.alias;
    }
    
    @Override
    public SQLExpr getFlashback() {
        return this.flashback;
    }
    
    @Override
    public void setFlashback(final SQLExpr flashback) {
        if (flashback != null) {
            flashback.setParent(this);
        }
        this.flashback = flashback;
    }
    
    @Override
    public boolean containsAlias(final String alias) {
        return SQLUtils.nameEquals(this.alias, alias);
    }
    
    @Override
    public long aliasHashCode64() {
        if (this.aliasHashCode64 == 0L && this.alias != null) {
            this.aliasHashCode64 = FnvHash.hashCode64(this.alias);
        }
        return this.aliasHashCode64;
    }
    
    @Override
    public SQLColumnDefinition findColumn(final String columnName) {
        if (columnName == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(columnName);
        return this.findColumn(hash);
    }
    
    @Override
    public SQLColumnDefinition findColumn(final long columnNameHash) {
        return null;
    }
    
    @Override
    public SQLObject resolveColum(final long columnNameHash) {
        return this.findColumn(columnNameHash);
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final String columnName) {
        if (columnName == null) {
            return null;
        }
        final long hash = FnvHash.hashCode64(columnName);
        return this.findTableSourceWithColumn(hash, columnName, 0);
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final SQLName columnName) {
        if (columnName instanceof SQLIdentifierExpr) {
            return this.findTableSourceWithColumn(columnName.nameHashCode64(), columnName.getSimpleName(), 0);
        }
        if (columnName instanceof SQLPropertyExpr) {
            final SQLExpr owner = ((SQLPropertyExpr)columnName).getOwner();
            if (owner instanceof SQLIdentifierExpr) {
                return this.findTableSource(((SQLIdentifierExpr)owner).nameHashCode64());
            }
        }
        return null;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnNameHash) {
        return this.findTableSourceWithColumn(columnNameHash, null, 0);
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnNameHash, final String columnName, final int option) {
        return null;
    }
    
    @Override
    public SQLTableSource findTableSource(final String alias) {
        final long hash = FnvHash.hashCode64(alias);
        return this.findTableSource(hash);
    }
    
    @Override
    public SQLTableSource findTableSource(final long alias_hash) {
        final long hash = this.aliasHashCode64();
        if (hash != 0L && hash == alias_hash) {
            return this;
        }
        return null;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLTableSourceImpl that = (SQLTableSourceImpl)o;
        if (this.aliasHashCode64() != that.aliasHashCode64()) {
            return false;
        }
        if (this.hints != null) {
            if (this.hints.equals(that.hints)) {
                return (this.flashback != null) ? this.flashback.equals(that.flashback) : (that.flashback == null);
            }
        }
        else if (that.hints == null) {
            return (this.flashback != null) ? this.flashback.equals(that.flashback) : (that.flashback == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.hints != null) ? this.hints.hashCode() : 0;
        result = 31 * result + ((this.flashback != null) ? this.flashback.hashCode() : 0);
        result = 31 * result + (int)(this.aliasHashCode64() ^ this.aliasHashCode64() >>> 32);
        return result;
    }
}
