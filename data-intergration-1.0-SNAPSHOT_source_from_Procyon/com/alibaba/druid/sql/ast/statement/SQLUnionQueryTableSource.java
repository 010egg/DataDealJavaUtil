// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.statement;

import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.SQLObject;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLName;
import java.util.List;

public class SQLUnionQueryTableSource extends SQLTableSourceImpl
{
    private SQLUnionQuery union;
    protected List<SQLName> columns;
    
    public SQLUnionQueryTableSource() {
        this.columns = new ArrayList<SQLName>();
    }
    
    public SQLUnionQueryTableSource(final String alias) {
        super(alias);
        this.columns = new ArrayList<SQLName>();
    }
    
    public SQLUnionQueryTableSource(final SQLUnionQuery union, final String alias) {
        super(alias);
        this.columns = new ArrayList<SQLName>();
        this.setUnion(union);
    }
    
    public SQLUnionQueryTableSource(final SQLUnionQuery union) {
        this.columns = new ArrayList<SQLName>();
        this.setUnion(union);
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.union != null) {
            this.union.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("(");
            this.union.output(buf);
            buf.append(")");
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    public SQLUnionQuery getUnion() {
        return this.union;
    }
    
    public void setUnion(final SQLUnionQuery union) {
        if (union != null) {
            union.setParent(this);
        }
        this.union = union;
    }
    
    @Override
    public SQLUnionQueryTableSource clone() {
        final SQLUnionQueryTableSource x = new SQLUnionQueryTableSource(this.union.clone(), this.alias);
        if (this.flashback != null) {
            x.setFlashback(this.flashback.clone());
        }
        if (this.hints != null) {
            for (final SQLHint e : this.hints) {
                final SQLHint e2 = e.clone();
                e2.setParent(x);
                x.getHints().add(e2);
            }
        }
        return x;
    }
    
    public List<SQLName> getColumns() {
        return this.columns;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        final SQLUnionQueryTableSource that = (SQLUnionQueryTableSource)o;
        if (this.union != null) {
            if (this.union.equals(that.union)) {
                return (this.columns != null) ? this.columns.equals(that.columns) : (that.columns == null);
            }
        }
        else if (that.union == null) {
            return (this.columns != null) ? this.columns.equals(that.columns) : (that.columns == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + ((this.union != null) ? this.union.hashCode() : 0);
        result = 31 * result + ((this.columns != null) ? this.columns.hashCode() : 0);
        return result;
    }
    
    @Override
    public SQLTableSource findTableSourceWithColumn(final long columnNameHash, final String columnName, final int option) {
        if (this.union == null) {
            return null;
        }
        final SQLSelectQueryBlock firstQueryBlock = this.union.getFirstQueryBlock();
        if (firstQueryBlock != null) {
            final SQLSelectItem selectItem = firstQueryBlock.findSelectItem(columnNameHash);
            if (selectItem != null) {
                return this;
            }
        }
        return null;
    }
}
