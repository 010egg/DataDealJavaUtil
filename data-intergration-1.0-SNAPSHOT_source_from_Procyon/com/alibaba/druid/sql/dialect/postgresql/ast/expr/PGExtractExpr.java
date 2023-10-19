// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.postgresql.ast.expr;

import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLReplaceable;

public class PGExtractExpr extends PGExprImpl implements SQLReplaceable
{
    private PGDateField field;
    private SQLExpr source;
    
    @Override
    public PGExtractExpr clone() {
        final PGExtractExpr x = new PGExtractExpr();
        x.field = this.field;
        if (this.source != null) {
            x.setSource(this.source.clone());
        }
        return x;
    }
    
    public PGDateField getField() {
        return this.field;
    }
    
    public void setField(final PGDateField field) {
        this.field = field;
    }
    
    public SQLExpr getSource() {
        return this.source;
    }
    
    public void setSource(final SQLExpr source) {
        if (source != null) {
            source.setParent(this);
        }
        this.source = source;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.source == expr) {
            this.setSource(target);
            return true;
        }
        return false;
    }
    
    @Override
    public void accept0(final PGASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.source);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.source);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.field == null) ? 0 : this.field.hashCode());
        result = 31 * result + ((this.source == null) ? 0 : this.source.hashCode());
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
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final PGExtractExpr other = (PGExtractExpr)obj;
        if (this.field != other.field) {
            return false;
        }
        if (this.source == null) {
            if (other.source != null) {
                return false;
            }
        }
        else if (!this.source.equals(other.source)) {
            return false;
        }
        return true;
    }
}
