// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.DbType;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.text.ParseException;
import java.util.Calendar;
import com.alibaba.druid.util.MySqlUtils;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.Date;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLReplaceable;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLDateExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr, SQLReplaceable
{
    public static final SQLDataType DATA_TYPE;
    private String literal;
    public static long supportDbTypes;
    
    public SQLDateExpr() {
    }
    
    public SQLDateExpr(final String literal) {
        this.setLiteral(literal);
    }
    
    public SQLDateExpr(final Date literal) {
        this.setLiteral(literal);
    }
    
    public SQLDateExpr(final Date literal, final TimeZone timeZone) {
        this.setLiteral(literal, timeZone);
    }
    
    public String getLiteral() {
        return this.literal;
    }
    
    public void setLiteral(final String literal) {
        this.literal = literal;
    }
    
    public void setLiteral(final Date x) {
        this.setLiteral(x, null);
    }
    
    public void setLiteral(final Date x, final TimeZone timeZone) {
        if (x == null) {
            this.literal = null;
            return;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        final String text = format.format(x);
        this.setLiteral(text);
    }
    
    @Override
    public String getValue() {
        return this.literal;
    }
    
    public Date getDate() {
        return this.getDate(null);
    }
    
    public Date getDate(final TimeZone timeZone) {
        return MySqlUtils.parseDate(this.literal, timeZone);
    }
    
    public boolean addDay(final int delta) {
        if (this.literal == null) {
            return false;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            final Date date = format.parse(this.literal);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(5, delta);
            final String result_chars = format.format(calendar.getTime());
            this.setLiteral(result_chars);
            return true;
        }
        catch (ParseException ex) {
            return false;
        }
    }
    
    public boolean addMonth(final int delta) {
        if (this.literal == null) {
            return false;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            final Date date = format.parse(this.literal);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(2, delta);
            final String result_chars = format.format(calendar.getTime());
            this.setLiteral(result_chars);
            return true;
        }
        catch (ParseException ex) {
            return false;
        }
    }
    
    public boolean addYear(final int delta) {
        if (this.literal == null) {
            return false;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            final Date date = format.parse(this.literal);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(1, delta);
            final String result_chars = format.format(calendar.getTime());
            this.setLiteral(result_chars);
            return true;
        }
        catch (ParseException ex) {
            return false;
        }
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
        final SQLDateExpr other = (SQLDateExpr)obj;
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
    public SQLDateExpr clone() {
        final SQLDateExpr x = new SQLDateExpr();
        if (this.literal != null) {
            x.setLiteral(this.literal);
        }
        return x;
    }
    
    @Override
    public boolean replace(final SQLExpr expr, final SQLExpr target) {
        return false;
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
    }
    
    public static boolean isSupport(final DbType dbType) {
        return (dbType.mask & SQLDateExpr.supportDbTypes) != 0x0L;
    }
    
    public static boolean check(final String str) {
        final int len;
        if (str == null || (len = str.length()) < 8) {
            return false;
        }
        final char c0 = str.charAt(0);
        final char c2 = str.charAt(1);
        final char c3 = str.charAt(2);
        final char c4 = str.charAt(3);
        final char c5 = str.charAt(4);
        final char c6 = str.charAt(5);
        final char c7 = str.charAt(6);
        final char c8 = str.charAt(7);
        if (c0 < '1' | c0 > '9') {
            return false;
        }
        if (c2 < '0' | c2 > '9') {
            return false;
        }
        if (c3 < '0' | c3 > '9') {
            return false;
        }
        if (c4 < '0' | c4 > '9') {
            return false;
        }
        final int year = (c0 - '0') * 1000 + (c2 - '0') * 100 + (c3 - '0') * 10 + (c4 - '0');
        if (year < 1000) {
            return false;
        }
        if (c5 != '-') {
            return false;
        }
        if (c6 < '0' | c6 > '9') {
            return false;
        }
        int month;
        int day;
        if (c7 == '-') {
            month = c6 - '0';
            if (c8 < '0' | c8 > '9') {
                return false;
            }
            if (len == 8) {
                day = c8 - '0';
            }
            else {
                if (len != 9) {
                    return false;
                }
                final char c9 = str.charAt(8);
                if (c9 < '0' | c9 > '9') {
                    return false;
                }
                day = (c8 - '0') * 10 + (c9 - '0');
            }
        }
        else {
            if (c7 < '0' | c7 > '9') {
                return false;
            }
            month = (c6 - '0') * 10 + (c7 - '0');
            if (c8 != '-') {
                return false;
            }
            if (len == 9) {
                final char c9 = str.charAt(8);
                if (c9 < '0' | c9 > '9') {
                    return false;
                }
                day = c9 - '0';
            }
            else {
                if (len != 10) {
                    return false;
                }
                final char c9 = str.charAt(8);
                final char c10 = str.charAt(9);
                if (c9 < '0' | c9 > '9') {
                    return false;
                }
                if (c10 < '0' | c10 > '9') {
                    return false;
                }
                day = (c9 - '0') * 10 + (c10 - '0');
            }
        }
        if (month < 1) {
            return false;
        }
        if (day < 1) {
            return false;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12: {
                return day <= 31;
            }
            case 2: {
                return day <= 29;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                return day <= 30;
            }
            default: {
                return false;
            }
        }
    }
    
    public static String format(final Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        final int year = calendar.get(1);
        final int month = calendar.get(2) + 1;
        final int dayOfMonth = calendar.get(5);
        final char[] chars = { (char)(year / 1000 + 48), (char)(year / 100 % 10 + 48), (char)(year / 10 % 10 + 48), (char)(year % 10 + 48), '-', (char)(month / 10 + 48), (char)(month % 10 + 48), '-', (char)(dayOfMonth / 10 + 48), (char)(dayOfMonth % 10 + 48) };
        return new String(chars);
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLDateExpr.DATA_TYPE;
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("date");
        SQLDateExpr.supportDbTypes = DbType.of(DbType.mysql, DbType.oracle, DbType.presto, DbType.trino, DbType.postgresql, DbType.ads, DbType.hive, DbType.odps);
    }
}
