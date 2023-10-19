// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.math.BigDecimal;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLDataType;

public class SQLNumberExpr extends SQLNumericLiteralExpr implements SQLValuableExpr
{
    public static final SQLDataType DATA_TYPE_NUMBER;
    public static final SQLDataType DATA_TYPE_DOUBLE;
    public static final SQLDataType DATA_TYPE_BIGINT;
    private Number number;
    private char[] chars;
    
    public SQLNumberExpr() {
    }
    
    public SQLNumberExpr(final Number number) {
        this.number = number;
    }
    
    public SQLNumberExpr(final char[] chars, final SQLObject parent) {
        this.chars = chars;
        this.parent = parent;
    }
    
    public SQLNumberExpr(final char[] chars) {
        this.chars = chars;
    }
    
    @Override
    public Number getNumber() {
        if (this.chars != null && this.number == null) {
            boolean exp = false;
            for (int i = 0; i < this.chars.length; ++i) {
                final char ch = this.chars[i];
                if (ch == 'e' || ch == 'E') {
                    exp = true;
                }
            }
            if (exp) {
                this.number = Double.parseDouble(new String(this.chars));
            }
            else {
                this.number = new BigDecimal(this.chars);
            }
        }
        return this.number;
    }
    
    public String getLiteral() {
        if (this.chars == null) {
            return null;
        }
        return new String(this.chars);
    }
    
    @Override
    public Number getValue() {
        return this.getNumber();
    }
    
    @Override
    public void setNumber(final Number number) {
        this.number = number;
        this.chars = null;
    }
    
    public void output(final StringBuilder buf) {
        if (this.chars != null) {
            buf.append(this.chars);
        }
        else {
            buf.append(this.number.toString());
        }
    }
    
    @Override
    public void output(final StringBuffer buf) {
        if (this.chars != null) {
            buf.append(this.chars);
        }
        else {
            buf.append(this.number.toString());
        }
    }
    
    @Override
    public void output(final Appendable buf) {
        if (buf instanceof StringBuilder) {
            this.output((StringBuilder)buf);
        }
        else if (buf instanceof StringBuffer) {
            this.output((StringBuffer)buf);
        }
        else {
            super.output(buf);
        }
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final Number number = this.getNumber();
        if (number == null) {
            return 0;
        }
        return number.hashCode();
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this.chars != null && this.number == null) {
            this.number = new BigDecimal(this.chars);
        }
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final SQLNumberExpr other = (SQLNumberExpr)obj;
        return Utils.equals(this.getNumber(), other.getNumber());
    }
    
    @Override
    public SQLNumberExpr clone() {
        final SQLNumberExpr x = new SQLNumberExpr();
        x.chars = this.chars;
        x.number = this.number;
        return x;
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLNumberExpr.DATA_TYPE_NUMBER;
    }
    
    public static boolean isZero(final SQLExpr x) {
        if (x instanceof SQLNumberExpr) {
            final Number number = ((SQLNumberExpr)x).getNumber();
            if (number instanceof Integer) {
                return number.intValue() == 0;
            }
            if (number instanceof Long) {
                return number.longValue() == 0L;
            }
        }
        return false;
    }
    
    public static boolean isOne(final SQLExpr x) {
        if (x instanceof SQLNumberExpr) {
            final Number number = ((SQLNumberExpr)x).getNumber();
            if (number instanceof Integer) {
                return number.intValue() == 1;
            }
            if (number instanceof Long) {
                return number.longValue() == 1L;
            }
        }
        return false;
    }
    
    static {
        DATA_TYPE_NUMBER = new SQLDataTypeImpl("number");
        DATA_TYPE_DOUBLE = new SQLDataTypeImpl("double");
        DATA_TYPE_BIGINT = SQLIntegerExpr.DATA_TYPE;
    }
}
