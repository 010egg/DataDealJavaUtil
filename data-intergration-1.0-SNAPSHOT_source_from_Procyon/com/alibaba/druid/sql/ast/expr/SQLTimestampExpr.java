// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLDataTypeImpl;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import java.util.List;
import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.text.ParseException;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import com.alibaba.druid.util.MySqlUtils;
import java.util.TimeZone;
import java.util.Date;
import com.alibaba.druid.sql.ast.SQLDataType;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLTimestampExpr extends SQLExprImpl implements SQLValuableExpr, SQLLiteralExpr
{
    public static final SQLDataType DATA_TYPE;
    protected String literal;
    protected String timeZone;
    protected boolean withTimeZone;
    
    public SQLTimestampExpr() {
        this.withTimeZone = false;
    }
    
    public SQLTimestampExpr(final String literal) {
        this.withTimeZone = false;
        this.literal = literal;
    }
    
    public SQLTimestampExpr(final Date date) {
        this.withTimeZone = false;
        this.setLiteral(date);
    }
    
    public SQLTimestampExpr(final Date date, final TimeZone timeZone) {
        this.withTimeZone = false;
        this.setLiteral(date, timeZone);
    }
    
    @Override
    public SQLTimestampExpr clone() {
        final SQLTimestampExpr x = new SQLTimestampExpr();
        x.literal = this.literal;
        x.timeZone = this.timeZone;
        x.withTimeZone = this.withTimeZone;
        return x;
    }
    
    public Date getDate(final TimeZone timeZone) {
        if (this.literal == null || this.literal.length() == 0) {
            return null;
        }
        return MySqlUtils.parseDate(this.literal, timeZone);
    }
    
    public boolean addDay(final int delta) {
        if (this.literal == null) {
            return false;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    
    public boolean addHour(final int delta) {
        if (this.literal == null) {
            return false;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final Date date = format.parse(this.literal);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(11, delta);
            final String result_chars = format.format(calendar.getTime());
            this.setLiteral(result_chars);
            return true;
        }
        catch (ParseException ex) {
            return false;
        }
    }
    
    public boolean addMiniute(final int delta) {
        if (this.literal == null) {
            return false;
        }
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            final Date date = format.parse(this.literal);
            final Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(12, delta);
            final String result_chars = format.format(calendar.getTime());
            this.setLiteral(result_chars);
            return true;
        }
        catch (ParseException ex) {
            return false;
        }
    }
    
    @Override
    public String getValue() {
        return this.literal;
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
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (timeZone != null) {
            format.setTimeZone(timeZone);
        }
        this.literal = format.format(x);
    }
    
    public String getTimeZone() {
        return this.timeZone;
    }
    
    public void setTimeZone(final String timeZone) {
        this.timeZone = timeZone;
    }
    
    public boolean isWithTimeZone() {
        return this.withTimeZone;
    }
    
    public void setWithTimeZone(final boolean withTimeZone) {
        this.withTimeZone = withTimeZone;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.literal == null) ? 0 : this.literal.hashCode());
        result = 31 * result + ((this.timeZone == null) ? 0 : this.timeZone.hashCode());
        result = 31 * result + (this.withTimeZone ? 1231 : 1237);
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
        final SQLTimestampExpr other = (SQLTimestampExpr)obj;
        if (this.literal == null) {
            if (other.literal != null) {
                return false;
            }
        }
        else if (!this.literal.equals(other.literal)) {
            return false;
        }
        if (this.timeZone == null) {
            if (other.timeZone != null) {
                return false;
            }
        }
        else if (!this.timeZone.equals(other.timeZone)) {
            return false;
        }
        return this.withTimeZone == other.withTimeZone;
    }
    
    @Override
    protected void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public String toString() {
        return SQLUtils.toSQLString(this, (DbType)null);
    }
    
    @Override
    public SQLDataType computeDataType() {
        return SQLTimestampExpr.DATA_TYPE;
    }
    
    @Override
    public List getChildren() {
        return Collections.emptyList();
    }
    
    public static boolean check(final String str) {
        final int len;
        if (str == null || (len = str.length()) < 14 || len > 23) {
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
        final char c9 = str.charAt(8);
        final char c10 = str.charAt(9);
        final char c11 = str.charAt(10);
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
        char M0 = '\0';
        char M2 = '\0';
        char d0 = '\0';
        char d2 = '\0';
        if (c9 == ' ') {
            if (c7 != '-') {
                return false;
            }
            M2 = c6;
            d2 = c8;
        }
        else if (c10 == ' ') {
            if (c7 == '-') {
                M2 = c6;
                d0 = c8;
                d2 = c9;
            }
            else {
                if (c8 != '-') {
                    return false;
                }
                M0 = c6;
                M2 = c7;
                d2 = c9;
            }
        }
        else {
            if (c11 != ' ') {
                return false;
            }
            if (c8 != '-') {
                return false;
            }
            M0 = c6;
            M2 = c7;
            d0 = c9;
            d2 = c10;
        }
        int month;
        if (M0 == '\0') {
            if (M2 < '0' || M2 > '9') {
                return false;
            }
            month = M2 - '0';
        }
        else {
            if (M0 != '0' && M0 != '1') {
                return false;
            }
            if (M2 < '0' || M2 > '9') {
                return false;
            }
            month = (M0 - '0') * 10 + M2 - 48;
        }
        int day;
        if (d0 == '\0') {
            if (d2 < '0' || d2 > '9') {
                return false;
            }
            day = d2 - '0';
        }
        else {
            if (d0 < '0' || d0 > '9') {
                return false;
            }
            if (d2 < '0' || d2 > '9') {
                return false;
            }
            day = (d0 - '0') * 10 + d2 - 48;
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
                if (day > 31) {
                    return false;
                }
                break;
            }
            case 2: {
                if (day > 29) {
                    return false;
                }
                break;
            }
            case 4:
            case 6:
            case 9:
            case 11: {
                if (day > 30) {
                    return false;
                }
                break;
            }
        }
        int index = len - 1;
        if (str.charAt(index) == '0' && str.charAt(index - 1) == '.') {
            index -= 2;
        }
        final char x0 = str.charAt(index--);
        final char x2 = str.charAt(index--);
        final char x3 = str.charAt(index--);
        final char x4 = str.charAt(index--);
        final char x5 = str.charAt(index--);
        final char x6 = str.charAt(index--);
        final char x7 = str.charAt(index--);
        final char x8 = str.charAt(index--);
        final char x9 = str.charAt(index--);
        final char x10 = str.charAt(index--);
        final char x11 = str.charAt(index--);
        final char x12 = str.charAt(index--);
        final char x13 = str.charAt(index--);
        char s0;
        char s2;
        char m0;
        char m2;
        char h0;
        char h2;
        if (x6 == ' ') {
            if (x2 != ':' || x4 != ':') {
                return false;
            }
            s0 = '\0';
            s2 = x0;
            m0 = '\0';
            m2 = x3;
            h0 = '\0';
            h2 = x5;
        }
        else if (x7 == ' ') {
            if (x2 == ':') {
                s0 = '\0';
                s2 = x0;
                if (x4 == ':') {
                    m0 = '\0';
                    m2 = x3;
                    h2 = x5;
                    h0 = x6;
                }
                else {
                    if (x5 != ':') {
                        return false;
                    }
                    m0 = x3;
                    m2 = x4;
                    h0 = '\0';
                    h2 = x6;
                }
            }
            else {
                if (x3 != ':') {
                    return false;
                }
                s0 = x0;
                s2 = x2;
                if (x5 != ':') {
                    return false;
                }
                m0 = '\0';
                m2 = x4;
                h0 = '\0';
                h2 = x6;
            }
        }
        else if (x8 == ' ') {
            if (x2 == ':') {
                s0 = '\0';
                s2 = x0;
                if (x5 != ':') {
                    return false;
                }
                m2 = x3;
                m0 = x4;
                h2 = x6;
                h0 = x7;
            }
            else {
                if (x3 != ':') {
                    return false;
                }
                s0 = x0;
                s2 = x2;
                if (x5 == ':') {
                    m0 = '\0';
                    m2 = x4;
                    h2 = x6;
                    h0 = x7;
                }
                else {
                    if (x6 != ':') {
                        return false;
                    }
                    m2 = x4;
                    m0 = x5;
                    h0 = '\0';
                    h2 = x7;
                }
            }
        }
        else if (x9 == ' ') {
            if (x3 != ':' || x6 != ':') {
                return false;
            }
            s2 = x0;
            s0 = x2;
            m2 = x4;
            m0 = x5;
            h2 = x7;
            h0 = x8;
        }
        else {
            if (x13 != ' ') {
                return false;
            }
            if (x4 != '.' || x7 != ':' || x10 != ':') {
                return false;
            }
            s2 = x5;
            s0 = x6;
            m2 = x8;
            m0 = x9;
            h2 = x11;
            h0 = x12;
            final int S2 = x0;
            final int S3 = x2;
            final int S4 = x3;
            if (S4 < 48 || S4 > 57) {
                return false;
            }
            if (S3 < 48 || S3 > 57) {
                return false;
            }
            if (S2 < 48 || S2 > 57) {
                return false;
            }
        }
        if (h0 == '\0') {
            if (h2 < '0' || h2 > '9') {
                return false;
            }
        }
        else {
            if (h0 < '0' || h0 > '2') {
                return false;
            }
            if (h2 < '0' || h2 > '9') {
                return false;
            }
            final int hour = (h0 - '0') * 10 + (h2 - '0');
            if (hour > 24) {
                return false;
            }
        }
        if (m0 == '\0') {
            if (m2 < '0' || m2 > '9') {
                return false;
            }
        }
        else {
            if (m0 < '0' || m0 > '6') {
                return false;
            }
            if (m2 < '0' || m2 > '9') {
                return false;
            }
            final int minute = (m0 - '0') * 10 + (m2 - '0');
            if (minute > 60) {
                return false;
            }
        }
        if (s0 == '\0') {
            if (s2 < '0' || s2 > '9') {
                return false;
            }
        }
        else {
            if (s0 < '0' || s0 > '6') {
                return false;
            }
            if (s2 < '0' || s2 > '9') {
                return false;
            }
            final int second = (s0 - '0') * 10 + (s2 - '0');
            if (second > 60) {
                return false;
            }
        }
        return true;
    }
    
    public static SQLTimestampExpr of(final String str) {
        return new SQLTimestampExpr(str);
    }
    
    static {
        DATA_TYPE = new SQLDataTypeImpl("timestamp");
    }
}
