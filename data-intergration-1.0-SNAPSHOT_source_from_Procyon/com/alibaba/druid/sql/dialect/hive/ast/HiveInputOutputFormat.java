// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.hive.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class HiveInputOutputFormat extends SQLExprImpl
{
    private SQLExpr input;
    private SQLExpr output;
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final HiveInputOutputFormat that = (HiveInputOutputFormat)o;
        if (this.input != null) {
            if (this.input.equals(that.input)) {
                return (this.output != null) ? this.output.equals(that.output) : (that.output == null);
            }
        }
        else if (that.input == null) {
            return (this.output != null) ? this.output.equals(that.output) : (that.output == null);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = (this.input != null) ? this.input.hashCode() : 0;
        result = 31 * result + ((this.output != null) ? this.output.hashCode() : 0);
        return result;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor v) {
        if (v.visit(this)) {
            this.acceptChild(v, this.input);
            this.acceptChild(v, this.output);
        }
        v.endVisit(this);
    }
    
    @Override
    public HiveInputOutputFormat clone() {
        final HiveInputOutputFormat x = new HiveInputOutputFormat();
        if (this.input != null) {
            x.setInput(this.input.clone());
        }
        if (this.output != null) {
            x.setOutput(this.output.clone());
        }
        return x;
    }
    
    public SQLExpr getInput() {
        return this.input;
    }
    
    public void setInput(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.input = x;
    }
    
    public SQLExpr getOutput() {
        return this.output;
    }
    
    public void setOutput(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.output = x;
    }
}
