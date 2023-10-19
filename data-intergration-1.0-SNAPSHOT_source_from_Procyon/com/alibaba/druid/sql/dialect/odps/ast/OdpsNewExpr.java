// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.odps.ast;

import com.alibaba.druid.sql.ast.SQLObject;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import java.util.Iterator;
import com.alibaba.druid.sql.dialect.odps.visitor.OdpsASTVisitor;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;

public class OdpsNewExpr extends SQLMethodInvokeExpr implements OdpsObject
{
    private boolean array;
    private List<SQLExpr> initValues;
    private List<SQLDataType> typeParameters;
    
    public OdpsNewExpr() {
        this.array = false;
        this.initValues = new ArrayList<SQLExpr>();
        this.typeParameters = new ArrayList<SQLDataType>();
    }
    
    @Override
    public OdpsNewExpr clone() {
        final OdpsNewExpr x = new OdpsNewExpr();
        this.cloneTo(x);
        return x;
    }
    
    public void accept0(final SQLASTVisitor v) {
        this.accept0((OdpsASTVisitor)v);
    }
    
    @Override
    public void accept0(final OdpsASTVisitor visitor) {
        if (visitor.visit(this)) {
            if (this.owner != null) {
                this.owner.accept(visitor);
            }
            for (final SQLExpr arg : this.arguments) {
                if (arg != null) {
                    arg.accept(visitor);
                }
            }
            if (this.from != null) {
                this.from.accept(visitor);
            }
            if (this.using != null) {
                this.using.accept(visitor);
            }
            if (this._for != null) {
                this._for.accept(visitor);
            }
            visitor.endVisit(this);
        }
        visitor.endVisit(this);
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("new ");
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
        super.output(buf);
    }
    
    public boolean isArray() {
        return this.array;
    }
    
    public void setArray(final boolean array) {
        this.array = array;
    }
    
    public List<SQLExpr> getInitValues() {
        return this.initValues;
    }
    
    public List<SQLDataType> getTypeParameters() {
        return this.typeParameters;
    }
}
