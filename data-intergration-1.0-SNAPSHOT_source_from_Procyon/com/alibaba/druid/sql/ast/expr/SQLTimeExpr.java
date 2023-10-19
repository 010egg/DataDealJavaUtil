// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.DbType;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import com.alibaba.druid.sql.ast.SQLObject;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLTimeExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr, SQLReplaceable
{
    public static final SQLDataType DATA_TYPE;
    private SQLExpr literal;
    public static long supportDbTypes;
    
    public SQLTimeExpr() {
    }
    
    public SQLTimeExpr(final Date now, final TimeZone timeZone) {
        this.setLiteral(now, timeZone);
    }
    
    public void setLiteral(final Date x, final TimeZone timeZone) {
        if (x == null) {
            this.literal = null;
            return;
        }
        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        final String text = format.format(x);
        this.setLiteral(text);
    }
    
    public SQLTimeExpr(final String literal) {
        this.setLiteral(literal);
    }
    
    public SQLExpr getLiteral() {
        return this.literal;
    }
    
    public void setLiteral(final String literal) {
        this.setLiteral(new SQLCharExpr(literal));
    }
    
    public void setLiteral(final SQLExpr x) {
        if (x != null) {
            x.setParent(this);
        }
        this.literal = x;
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLTimeExpr.DATA_TYPE;
    }
    
    @Override
    public String getValue() {
        if (this.literal instanceof SQLCharExpr) {
            return ((SQLCharExpr)this.literal).getText();
        }
        return null;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        if (this.literal == expr) {
            this.setLiteral(target);
            return true;
        }
        return false;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.literal == null) ? 0 : this.literal.hashCode());
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
        final SQLTimeExpr other = (SQLTimeExpr)obj;
        if (this.literal == null) {
            if (other.literal != null) {
                return false;
            }
        }
        else if (!this.literal.equals(other.literal)) {
            return false;
        }
        return true;
    }
    
    @Override
    public SQLTimeExpr clone() {
        final SQLTimeExpr x = new SQLTimeExpr();
        if (this.literal != null) {
            x.setLiteral(this.literal.clone());
        }
        return x;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    public static boolean isSupport(final DbType dbType) {
        return (dbType.mask & SQLTimeExpr.supportDbTypes) != 0x0L;
    }
    
    public static boolean check(final String str) {
        if (str == null || str.length() != 8) {
            return false;
        }
        if (str.charAt(2) != ':' && str.charAt(5) != ':') {
            return false;
        }
        final char c0 = str.charAt(0);
        final char c2 = str.charAt(1);
        final char c3 = str.charAt(3);
        final char c4 = str.charAt(4);
        final char c5 = str.charAt(6);
        final char c6 = str.charAt(7);
        if (c0 < '0' || c0 > '9') {
            return false;
        }
        if (c2 < '0' || c2 > '9') {
            return false;
        }
        if (c3 < '0' || c3 > '9') {
            return false;
        }
        if (c4 < '0' || c4 > '9') {
            return false;
        }
        if (c5 < '0' || c5 > '9') {
            return false;
        }
        if (c6 < '0' || c6 > '9') {
            return false;
        }
        final int HH = (c0 - '0') * 10 + (c2 - '0');
        final int mm = (c3 - '0') * 10 + (c4 - '0');
        final int ss = (c5 - '0') * 10 + (c6 - '0');
        return HH <= 24 && mm <= 60 && ss <= 60;
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("time");
        SQLTimeExpr.supportDbTypes = DbType.of(DbType.mysql, DbType.oracle, DbType.presto, DbType.trino, DbType.postgresql);
    }
}
