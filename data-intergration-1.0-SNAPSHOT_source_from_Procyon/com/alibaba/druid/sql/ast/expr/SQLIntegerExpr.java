// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import java.math.BigDecimal;
import java.math.BigInteger;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLDataType;

public class SQLIntegerExpr extends SQLNumericLiteralExpr implements SQLValuableExpr, Comparable<SQLIntegerExpr>
{
    public static final SQLDataType DATA_TYPE;
    private Number number;
    private String type;
    
    public SQLIntegerExpr(final Number number) {
        this.number = number;
    }
    
    public SQLIntegerExpr(final Number number, final SQLObject parent) {
        this.number = number;
        this.parent = parent;
    }
    
    public SQLIntegerExpr() {
    }
    
    @Override
    public Number getNumber() {
        return this.number;
    }
    
    @Override
    public void setNumber(final Number number) {
        this.number = number;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append(this.number.toString());
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
    
    public String getType() {
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.number == null) ? 0 : this.number.hashCode());
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
        final SQLIntegerExpr other = (SQLIntegerExpr)obj;
        if (this.number == null) {
            if (other.number != null) {
                return false;
            }
        }
        else if (!this.number.equals(other.number)) {
            return false;
        }
        return true;
    }
    
    @Override
    public Object getValue() {
        return this.number;
    }
    
    @Override
    public SQLIntegerExpr clone() {
        return new SQLIntegerExpr(this.number);
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLIntegerExpr.DATA_TYPE;
    }
    
    public void decrement() {
        if (this.number instanceof Integer) {
            this.number = this.number.intValue() - 1;
        }
        else {
            if (!(this.number instanceof Long)) {
                throw new FastsqlException("decrement not support.");
            }
            this.number = this.number.longValue() - 1L;
        }
    }
    
    public static boolean isZero(final SQLExpr expr) {
        if (expr instanceof SQLIntegerExpr) {
            final Number number = ((SQLIntegerExpr)expr).getNumber();
            return number != null && number.intValue() == 0;
        }
        return false;
    }
    
    public static SQLIntegerExpr substract(final SQLIntegerExpr a, final SQLIntegerExpr b) {
        final int val = a.number.intValue() - b.number.intValue();
        return new SQLIntegerExpr(val);
    }
    
    public static SQLIntegerExpr least(final SQLIntegerExpr a, final SQLIntegerExpr b) {
        if (a == null) {
            return b;
        }
        if (a.number.intValue() <= b.number.intValue()) {
            return a;
        }
        return b;
    }
    
    public static SQLIntegerExpr greatst(final SQLIntegerExpr a, final SQLIntegerExpr b) {
        if (a.number.intValue() >= b.number.intValue()) {
            return a;
        }
        return b;
    }
    
    public static SQLIntegerExpr ofIntOrLong(final long value) {
        if (value >= -2147483648L && value <= 2147483647L) {
            return new SQLIntegerExpr((int)value);
        }
        return new SQLIntegerExpr(value);
    }
    
    public static SQLIntegerExpr add(final long a, final long b) {
        final long r = a + b;
        if (a > 0L && b > 0L && r <= 0L) {
            return new SQLIntegerExpr(BigInteger.valueOf(a).add(BigInteger.valueOf(b)));
        }
        return new SQLIntegerExpr(r);
    }
    
    @Override
    public int compareTo(final SQLIntegerExpr o) {
        if (this.number instanceof Integer && o.number instanceof Integer) {
            return ((Integer)this.number).compareTo((Integer)o.number);
        }
        if (this.number instanceof Long && o.number instanceof Long) {
            return ((Long)this.number).compareTo((Long)o.number);
        }
        if (this.number instanceof BigDecimal && o.number instanceof BigDecimal) {
            return ((BigDecimal)this.number).compareTo((BigDecimal)o.number);
        }
        if (this.number instanceof Float && o.number instanceof Float) {
            return ((Float)this.number).compareTo((Float)o.number);
        }
        if (this.number instanceof Double && o.number instanceof Double) {
            return ((Float)this.number).compareTo((Float)o.number);
        }
        return -1;
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("bigint");
    }
}
