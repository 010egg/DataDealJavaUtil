// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.util.FnvHash;
import java.util.Collection;
import java.util.ArrayList;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLIntegerExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectImpl;

public class SQLSelectItem extends SQLObjectImpl implements SQLReplaceable
{
    protected SQLExpr expr;
    protected String alias;
    protected boolean connectByRoot;
    protected transient long aliasHashCode64;
    protected List<String> aliasList;
    
    public SQLSelectItem() {
        this.connectByRoot = false;
    }
    
    public SQLSelectItem(final SQLExpr expr) {
        this(expr, null);
    }
    
    public SQLSelectItem(final int value) {
        this(new SQLIntegerExpr(value), null);
    }
    
    public SQLSelectItem(final SQLExpr expr, final String alias) {
        this.connectByRoot = false;
        this.expr = expr;
        this.alias = alias;
        if (expr != null) {
            expr.setParent(this);
        }
    }
    
    public SQLSelectItem(final SQLExpr expr, final String alias, final boolean connectByRoot) {
        this.connectByRoot = false;
        this.connectByRoot = connectByRoot;
        this.expr = expr;
        this.alias = alias;
        if (expr != null) {
            expr.setParent(this);
        }
    }
    
    public SQLSelectItem(final SQLExpr expr, final List<String> aliasList, final boolean connectByRoot) {
        this.connectByRoot = false;
        this.connectByRoot = connectByRoot;
        this.expr = expr;
        this.aliasList = aliasList;
        if (expr != null) {
            expr.setParent(this);
        }
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        if (expr != null) {
            expr.setParent(this);
        }
        this.expr = expr;
    }
    
    public String computeAlias() {
        String alias = this.getAlias();
        if (alias == null) {
            if (this.expr instanceof SQLIdentifierExpr) {
                alias = ((SQLIdentifierExpr)this.expr).getName();
            }
            else if (this.expr instanceof SQLPropertyExpr) {
                alias = ((SQLPropertyExpr)this.expr).getName();
            }
        }
        return SQLUtils.normalize(alias);
    }
    
    @Override
    public SQLDataType computeDataType() {
        if (this.expr == null) {
            return null;
        }
        return this.expr.computeDataType();
    }
    
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
    
    public void setAlias(final String alias) {
        this.alias = alias;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            if (this.connectByRoot) {
                buf.append(" CONNECT_BY_ROOT ");
            }
            this.expr.output(buf);
            if (this.alias != null && this.alias.length() != 0) {
                buf.append(" AS ");
                buf.append(this.alias);
            }
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this) && this.expr != null) {
            this.expr.accept(v);
        }
        v.endVisit(this);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final SQLSelectItem that = (SQLSelectItem)o;
        if (this.connectByRoot != that.connectByRoot) {
            return false;
        }
        if (this.alias_hash() != that.alias_hash()) {
            return false;
        }
        if (this.expr != null) {
            if (this.expr.equals(that.expr)) {
                return (this.aliasList != null) ? this.aliasList.equals(that.aliasList) : (that.aliasList == null);
            }
        }
        else if (that.expr == null) {
            return (this.aliasList != null) ? this.aliasList.equals(that.aliasList) : (that.aliasList == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.expr != null) ? this.expr.hashCode() : 0;
        result = 31 * result + ((this.alias != null) ? this.alias.hashCode() : 0);
        result = 31 * result + (this.connectByRoot ? 1 : 0);
        result = 31 * result + (int)(this.alias_hash() ^ this.alias_hash() >>> 32);
        result = 31 * result + ((this.aliasList != null) ? this.aliasList.hashCode() : 0);
        return result;
    }
    
    public boolean isConnectByRoot() {
        return this.connectByRoot;
    }
    
    public void setConnectByRoot(final boolean connectByRoot) {
        this.connectByRoot = connectByRoot;
    }
    
    @Override
    public SQLSelectItem clone() {
        final SQLSelectItem x = new SQLSelectItem();
        x.alias = this.alias;
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.connectByRoot = this.connectByRoot;
        if (this.aliasList != null) {
            x.aliasList = new ArrayList<String>(this.aliasList);
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    public boolean match(final String alias) {
        if (alias == null) {
            return false;
        }
        final long hash = FnvHash.hashCode64(alias);
        return this.match(hash);
    }
    
    public long alias_hash() {
        if (this.aliasHashCode64 == 0L) {
            this.aliasHashCode64 = FnvHash.hashCode64(this.alias);
        }
        return this.aliasHashCode64;
    }
    
    public boolean match(final long alias_hash) {
        final long hash = this.alias_hash();
        if (hash == alias_hash) {
            return true;
        }
        if (this.expr instanceof SQLAllColumnExpr) {
            final SQLTableSource resolvedTableSource = ((SQLAllColumnExpr)this.expr).getResolvedTableSource();
            return resolvedTableSource != null && resolvedTableSource.findColumn(alias_hash) != null;
        }
        if (this.expr instanceof SQLIdentifierExpr) {
            return ((SQLIdentifierExpr)this.expr).nameHashCode64() == alias_hash;
        }
        if (!(this.expr instanceof SQLPropertyExpr)) {
            return false;
        }
        final String ident = ((SQLPropertyExpr)this.expr).getName();
        if ("*".equals(ident)) {
            final SQLTableSource resolvedTableSource2 = ((SQLPropertyExpr)this.expr).getResolvedTableSource();
            return resolvedTableSource2 != null && resolvedTableSource2.findColumn(alias_hash) != null;
        }
        return this.alias == null && ((SQLPropertyExpr)this.expr).nameHashCode64() == alias_hash;
    }
    
    public List<String> getAliasList() {
        return this.aliasList;
    }
    
    @Override
    public String toString() {
        DbType dbType = null;
        if (this.parent instanceof OracleSQLObject) {
            dbType = DbType.oracle;
        }
        return SQLUtils.toSQLString(this, dbType);
    }
    
    public boolean isUDTFSelectItem() {
        return this.aliasList != null && this.aliasList.size() > 0;
    }
}
