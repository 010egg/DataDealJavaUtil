// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLVariantRefExpr extends SQLExprImpl
{
    private String name;
    private boolean global;
    private boolean session;
    private int index;
    
    public SQLVariantRefExpr(final String name) {
        this.global = false;
        this.session = false;
        this.index = -1;
        this.name = name;
    }
    
    public SQLVariantRefExpr(final String name, final SQLObject parent) {
        this.global = false;
        this.session = false;
        this.index = -1;
        this.name = name;
        this.parent = parent;
    }
    
    public SQLVariantRefExpr(final String name, final boolean global) {
        this(name, global, false);
    }
    
    public SQLVariantRefExpr(final String name, final boolean global, final boolean session) {
        this.global = false;
        this.session = false;
        this.index = -1;
        this.name = name;
        this.global = global;
        this.session = session;
    }
    
    public SQLVariantRefExpr() {
        this.global = false;
        this.session = false;
        this.index = -1;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append(this.name);
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SQLVariantRefExpr)) {
            return false;
        }
        final SQLVariantRefExpr other = (SQLVariantRefExpr)obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        }
        else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }
    
    public boolean isGlobal() {
        return this.global;
    }
    
    public void setGlobal(final boolean global) {
        this.global = global;
    }
    
    public boolean isSession() {
        return this.session;
    }
    
    public void setSession(final boolean session) {
        this.session = session;
    }
    
    @Override
    public SQLVariantRefExpr clone() {
        final SQLVariantRefExpr var = new SQLVariantRefExpr(this.name, this.global, this.session);
        if (this.attributes != null) {
            var.attributes = new HashMap<String, Object>(this.attributes.size());
            for (final Map.Entry<String, Object> entry : this.attributes.entrySet()) {
                final String k = entry.getKey();
                final Object v = entry.getValue();
                if (v instanceof SQLObject) {
                    var.attributes.put(k, ((SQLObject)v).clone());
                }
                else {
                    var.attributes.put(k, v);
                }
            }
        }
        var.index = this.index;
        return var;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
}
