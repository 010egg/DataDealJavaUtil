// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLObjectImpl;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLStatement;

public class SQLInsertStatement extends SQLInsertInto implements SQLStatement
{
    protected SQLWithSubqueryClause with;
    protected boolean upsert;
    
    public SQLInsertStatement() {
        this.upsert = false;
    }
    
    public void cloneTo(final SQLInsertStatement x) {
        super.cloneTo(x);
        x.dbType = this.dbType;
        x.upsert = this.upsert;
        x.afterSemi = this.afterSemi;
        if (this.with != null) {
            x.setWith(this.with.clone());
        }
    }
    
    @Override
    public SQLInsertStatement clone() {
        final SQLInsertStatement x = new SQLInsertStatement();
        this.cloneTo(x);
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.tableSource);
            this.acceptChild(visitor, this.columns);
            this.acceptChild(visitor, this.valuesList);
            this.acceptChild(visitor, this.query);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        final List<SQLObject> children = new ArrayList<SQLObject>();
        children.add(this.tableSource);
        children.addAll(this.columns);
        children.addAll(this.valuesList);
        if (this.query != null) {
            children.add(this.query);
        }
        return children;
    }
    
    public boolean isUpsert() {
        return this.upsert;
    }
    
    public void setUpsert(final boolean upsert) {
        this.upsert = upsert;
    }
    
    @Override
    public DbType getDbType() {
        return this.dbType;
    }
    
    @Override
    public void setDbType(final DbType dbType) {
        this.dbType = dbType;
    }
    
    @Override
    public boolean isAfterSemi() {
        return this.afterSemi;
    }
    
    @Override
    public void setAfterSemi(final boolean afterSemi) {
        this.afterSemi = afterSemi;
    }
    
    public SQLWithSubqueryClause getWith() {
        return this.with;
    }
    
    public void setWith(final SQLWithSubqueryClause with) {
        if (with != null) {
            with.setParent(this);
        }
        this.with = with;
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, this.dbType);
    }
    
    public static class ValuesClause extends SQLObjectImpl implements SQLReplaceable
    {
        private final List values;
        private transient String originalString;
        private transient int replaceCount;
        
        public ValuesClause() {
            this(new ArrayList<SQLExpr>());
        }
        
        @Override
        public ValuesClause clone() {
            final ValuesClause x = new ValuesClause(new ArrayList<SQLExpr>(this.values.size()));
            for (final Object v : this.values) {
                x.addValue(v);
            }
            return x;
        }
        
        @Override
        public boolean replace(final SQLExpr expr, final SQLExpr target) {
            for (int i = 0; i < this.values.size(); ++i) {
                if (this.values.get(i) == expr) {
                    target.setParent(this);
                    this.values.set(i, target);
                    return true;
                }
            }
            return false;
        }
        
        public ValuesClause(final List<SQLExpr> values) {
            this.values = values;
            for (int i = 0; i < values.size(); ++i) {
                values.get(i).setParent(this);
            }
        }
        
        public ValuesClause(final List values, final SQLObject parent) {
            this.values = values;
            for (int i = 0; i < values.size(); ++i) {
                final Object val = values.get(i);
                if (val instanceof SQLObject) {
                    ((SQLObject)val).setParent(this);
                }
            }
            this.parent = parent;
        }
        
        public void addValue(final Object value) {
            if (value instanceof SQLObject) {
                ((SQLObject)value).setParent(this);
            }
            this.values.add(value);
        }
        
        public void addValue(final SQLExpr value) {
            value.setParent(this);
            this.values.add(value);
        }
        
        public List<SQLExpr> getValues() {
            return (List<SQLExpr>)this.values;
        }
        
        @Override
        protected void accept0(final SQLASTVisitor visitor) {
            if (visitor.visit(this)) {
                for (int i = 0; i < this.values.size(); ++i) {
                    final Object item = this.values.get(i);
                    if (item instanceof SQLObject) {
                        final SQLObject value = (SQLObject)item;
                        value.accept(visitor);
                    }
                }
            }
            visitor.endVisit(this);
        }
        
        public String getOriginalString() {
            return this.originalString;
        }
        
        public void setOriginalString(final String originalString) {
            this.originalString = originalString;
        }
        
        public int getReplaceCount() {
            return this.replaceCount;
        }
        
        public void incrementReplaceCount() {
            ++this.replaceCount;
        }
    }
}
