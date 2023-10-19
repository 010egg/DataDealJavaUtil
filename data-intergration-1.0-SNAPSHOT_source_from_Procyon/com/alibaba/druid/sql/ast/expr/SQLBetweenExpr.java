// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataType;
import java.util.Arrays;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.io.Serializable;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLBetweenExpr extends SQLExprImpl implements SQLReplaceable, Serializable
{
    private static final long serialVersionUID = 1L;
    public SQLExpr testExpr;
    private boolean not;
    public SQLExpr beginExpr;
    public SQLExpr endExpr;
    
    public SQLBetweenExpr() {
    }
    
    @Override
    public SQLBetweenExpr clone() {
        final SQLBetweenExpr x = new SQLBetweenExpr();
        if (this.testExpr != null) {
            x.setTestExpr(this.testExpr.clone());
        }
        x.not = this.not;
        if (this.beginExpr != null) {
            x.setBeginExpr(this.beginExpr.clone());
        }
        if (this.endExpr != null) {
            x.setEndExpr(this.endExpr.clone());
        }
        return x;
    }
    
    public SQLBetweenExpr(final SQLExpr testExpr, final SQLExpr beginExpr, final SQLExpr endExpr) {
        this.setTestExpr(testExpr);
        this.setBeginExpr(beginExpr);
        this.setEndExpr(endExpr);
    }
    
    public SQLBetweenExpr(final SQLExpr testExpr, final boolean not, final SQLExpr beginExpr, final SQLExpr endExpr) {
        this(testExpr, beginExpr, endExpr);
        this.not = not;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.testExpr != null) {
                this.testExpr.accept(visitor);
            }
            if (this.beginExpr != null) {
                this.beginExpr.accept(visitor);
            }
            if (this.endExpr != null) {
                this.endExpr.accept(visitor);
            }
        }
        visitor.endVisit(this);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Arrays.asList(this.testExpr, this.beginExpr, this.endExpr);
    }
    
    public SQLExpr getTestExpr() {
        return this.testExpr;
    }
    
    public void setTestExpr(final SQLExpr testExpr) {
        if (testExpr != null) {
            testExpr.setParent(this);
        }
        this.testExpr = testExpr;
    }
    
    public boolean isNot() {
        return this.not;
    }
    
    public void setNot(final boolean not) {
        this.not = not;
    }
    
    public SQLExpr getBeginExpr() {
        return this.beginExpr;
    }
    
    public void setBeginExpr(final SQLExpr beginExpr) {
        if (beginExpr != null) {
            beginExpr.setParent(this);
        }
        this.beginExpr = beginExpr;
    }
    
    public SQLExpr getEndExpr() {
        return this.endExpr;
    }
    
    public void setEndExpr(final SQLExpr endExpr) {
        if (endExpr != null) {
            endExpr.setParent(this);
        }
        this.endExpr = endExpr;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.beginExpr == null) ? 0 : this.beginExpr.hashCode());
        result = 31 * result + ((this.endExpr == null) ? 0 : this.endExpr.hashCode());
        result = 31 * result + (this.not ? 1231 : 1237);
        result = 31 * result + ((this.testExpr == null) ? 0 : this.testExpr.hashCode());
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
        final SQLBetweenExpr other = (SQLBetweenExpr)obj;
        if (this.beginExpr == null) {
            if (other.beginExpr != null) {
                return false;
            }
        }
        else if (!this.beginExpr.equals(other.beginExpr)) {
            return false;
        }
        if (this.endExpr == null) {
            if (other.endExpr != null) {
                return false;
            }
        }
        else if (!this.endExpr.equals(other.endExpr)) {
            return false;
        }
        if (this.not != other.not) {
            return false;
        }
        if (this.testExpr == null) {
            if (other.testExpr != null) {
                return false;
            }
        }
        else if (!this.testExpr.equals(other.testExpr)) {
            return false;
        }
        return true;
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLBooleanExpr.DATA_TYPE;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (expr == this.testExpr) {
            this.setTestExpr(target);
            return true;
        }
        if (expr == this.beginExpr) {
            this.setBeginExpr(target);
            return true;
        }
        if (expr == this.endExpr) {
            this.setEndExpr(target);
            return true;
        }
        return false;
    }
}
