// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.dialect.oracle.ast.stmt;

import com.alibaba.druid.sql.dialect.oracle.ast.OracleSQLObject;
import java.util.Iterator;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleASTVisitor;
import java.util.ArrayList;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;

public class OracleSelectUnPivot extends OracleSelectPivotBase
{
    private NullsIncludeType nullsIncludeType;
    private final List<SQLExpr> items;
    private final List<OracleSelectPivot.Item> pivotIn;
    
    public OracleSelectUnPivot() {
        this.items = new ArrayList<SQLExpr>();
        this.pivotIn = new ArrayList<OracleSelectPivot.Item>();
    }
    
    @Override
    public void accept0(final OracleASTVisitor visitor) {
        if (visitor.visit(this)) {
            this.acceptChild(visitor, this.items);
            this.acceptChild(visitor, this.pivotIn);
        }
        visitor.endVisit(this);
    }
    
    public List<OracleSelectPivot.Item> getPivotIn() {
        return this.pivotIn;
    }
    
    public List<SQLExpr> getItems() {
        return this.items;
    }
    
    public void addItem(final SQLExpr item) {
        if (item != null) {
            item.setParent(this);
        }
        this.items.add(item);
    }
    
    public NullsIncludeType getNullsIncludeType() {
        return this.nullsIncludeType;
    }
    
    public void setNullsIncludeType(final NullsIncludeType nullsIncludeType) {
        this.nullsIncludeType = nullsIncludeType;
    }
    
    @Override
    public OracleSelectUnPivot clone() {
        final OracleSelectUnPivot x = new OracleSelectUnPivot();
        x.setNullsIncludeType(this.nullsIncludeType);
        for (final SQLExpr e : this.items) {
            final SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.getItems().add(e2);
        }
        for (final SQLExpr e : this.pivotFor) {
            final SQLExpr e2 = e.clone();
            e2.setParent(x);
            x.getPivotFor().add(e2);
        }
        for (final OracleSelectPivot.Item e3 : this.pivotIn) {
            final OracleSelectPivot.Item e4 = e3.clone();
            e4.setParent(x);
            x.getPivotIn().add(e4);
        }
        return x;
    }
    
    public enum NullsIncludeType
    {
        INCLUDE_NULLS, 
        EXCLUDE_NULLS;
        
        public static String toString(final NullsIncludeType type, final boolean ucase) {
            if (NullsIncludeType.INCLUDE_NULLS.equals(type)) {
                return ucase ? "INCLUDE NULLS" : "include nulls";
            }
            if (NullsIncludeType.EXCLUDE_NULLS.equals(type)) {
                return ucase ? "EXCLUDE NULLS" : "exclude nulls";
            }
            throw new IllegalArgumentException();
        }
    }
}
