// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLSequenceExpr extends SQLExprImpl implements SQLReplaceable
{
    private SQLName sequence;
    private Function function;
    
    public SQLSequenceExpr() {
    }
    
    public SQLSequenceExpr(final SQLName sequence, final Function function) {
        this.sequence = sequence;
        this.function = function;
    }
    
    @Override
    public SQLSequenceExpr clone() {
        final SQLSequenceExpr x = new SQLSequenceExpr();
        if (this.sequence != null) {
            x.setSequence(this.sequence.clone());
        }
        x.function = this.function;
        return x;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this) && this.sequence != null) {
            this.sequence.accept(visitor);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.sequence == expr) {
            this.setSequence((SQLName)target);
            return true;
        }
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return (List<SQLObject>)Collections.singletonList(this.sequence);
    }
    
    public SQLName getSequence() {
        return this.sequence;
    }
    
    public void setSequence(final SQLName sequence) {
        this.sequence = sequence;
    }
    
    public Function getFunction() {
        return this.function;
    }
    
    public void setFunction(final Function function) {
        this.function = function;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.function == null) ? 0 : this.function.hashCode());
        result = 31 * result + ((this.sequence == null) ? 0 : this.sequence.hashCode());
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
        final SQLSequenceExpr other = (SQLSequenceExpr)obj;
        if (this.function != other.function) {
            return false;
        }
        if (this.sequence == null) {
            if (other.sequence != null) {
                return false;
            }
        }
        else if (!this.sequence.equals(other.sequence)) {
            return false;
        }
        return true;
    }
    
    public enum Function
    {
        NextVal("NEXTVAL"), 
        CurrVal("CURRVAL"), 
        PrevVal("PREVVAL");
        
        public final String name;
        public final String name_lcase;
        
        private Function(final String name) {
            this.name = name;
            this.name_lcase = name.toLowerCase();
        }
    }
}
