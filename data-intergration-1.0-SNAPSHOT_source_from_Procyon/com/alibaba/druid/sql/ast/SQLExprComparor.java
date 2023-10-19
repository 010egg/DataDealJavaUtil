// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast;

import java.util.Comparator;

public class SQLExprComparor implements Comparator<SQLExpr>
{
    public static final SQLExprComparor instance;
    
    @Override
    public int compare(final SQLExpr a, final SQLExpr b) {
        return compareTo(a, b);
    }
    
    public static int compareTo(final SQLExpr a, final SQLExpr b) {
        if (a == null && b == null) {
            return 0;
        }
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        if (a.getClass() == b.getClass() && a instanceof Comparable) {
            return ((Comparable)a).compareTo(b);
        }
        return a.getClass().getName().compareTo(b.getClass().getName());
    }
    
    static {
        instance = new SQLExprComparor();
    }
}
