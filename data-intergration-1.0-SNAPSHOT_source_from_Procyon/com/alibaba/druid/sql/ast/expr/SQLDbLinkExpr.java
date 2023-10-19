// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.statement.SQLColumnDefinition;
import com.alibaba.druid.util.FnvHash;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLDbLinkExpr extends SQLExprImpl implements SQLName, SQLExpr, SQLReplaceable
{
    private SQLExpr expr;
    private String dbLink;
    private long dbLinkHashCode64;
    private long hashCode64;
    
    @Override
    public String getSimpleName() {
        return this.dbLink;
    }
    
    public SQLExpr getExpr() {
        return this.expr;
    }
    
    public void setExpr(final SQLExpr expr) {
        this.expr = expr;
    }
    
    public String getDbLink() {
        return this.dbLink;
    }
    
    public void setDbLink(final String dbLink) {
        this.dbLink = dbLink;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.expr);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.expr == expr) {
            this.setExpr(target);
            return true;
        }
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.expr);
    }
    
    @Override
    public int hashCode() {
        final long value = this.hashCode64();
        return (int)(value ^ value >>> 32);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SQLDbLinkExpr other = (SQLDbLinkExpr)obj;
        return this.hashCode64() == other.hashCode64();
    }
    
    @Override
    public SQLDbLinkExpr clone() {
        final SQLDbLinkExpr x = new SQLDbLinkExpr();
        if (this.expr != null) {
            x.setExpr(this.expr.clone());
        }
        x.dbLink = this.dbLink;
        return x;
    }
    
    @Override
    public long nameHashCode64() {
        if (this.dbLinkHashCode64 == 0L && this.dbLink != null) {
            this.dbLinkHashCode64 = FnvHash.hashCode64(this.dbLink);
        }
        return this.dbLinkHashCode64;
    }
    
    @Override
    public long hashCode64() {
        if (this.hashCode64 == 0L) {
            long hash;
            if (this.expr instanceof SQLName) {
                hash = ((SQLName)this.expr).hashCode64();
                hash ^= 0x40L;
                hash *= 1099511628211L;
            }
            else if (this.expr == null) {
                hash = -3750763034362895579L;
            }
            else {
                hash = FnvHash.fnv1a_64_lower(this.expr.toString());
                hash ^= 0x40L;
                hash *= 1099511628211L;
            }
            hash = FnvHash.hashCode64(hash, this.dbLink);
            this.hashCode64 = hash;
        }
        return this.hashCode64;
    }
    
    @Override
    public SQLColumnDefinition getResolvedColumn() {
        return null;
    }
}
