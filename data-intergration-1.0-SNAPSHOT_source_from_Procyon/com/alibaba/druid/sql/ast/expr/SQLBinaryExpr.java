// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.Collections;
import com.alibaba.druid.sql.ast.SQLObject;
import java.util.List;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.math.BigInteger;
import com.alibaba.druid.util.Utils;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLBinaryExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr
{
    private String text;
    private transient Number val;
    
    public SQLBinaryExpr() {
    }
    
    public SQLBinaryExpr(final String value) {
        this.text = value;
    }
    
    public String getText() {
        return this.text;
    }
    
    @Override
    public Number getValue() {
        if (this.text == null) {
            return null;
        }
        if (this.val == null) {
            final long[] words = new long[this.text.length() / 64 + 1];
            for (int i = this.text.length() - 1; i >= 0; --i) {
                final char ch = this.text.charAt(i);
                if (ch == '1') {
                    final int wordIndex = i >> 6;
                    final long[] array = words;
                    final int n = wordIndex;
                    array[n] |= 1L << this.text.length() - 1 - i;
                }
            }
            if (words.length == 1) {
                this.val = words[0];
            }
            else {
                final byte[] bytes = new byte[words.length * 8];
                for (int j = 0; j < words.length; ++j) {
                    Utils.putLong(bytes, (words.length - 1 - j) * 8, words[j]);
                }
                this.val = new BigInteger(bytes);
            }
        }
        return this.val;
    }
    
    public void setValue(final String value) {
        this.text = value;
    }
    
    public void accept0(final SQLASTVisitor visitor) {
        visitor.visit(this);
        visitor.endVisit(this);
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("b'");
            buf.append(this.text);
            buf.append('\'');
        }
        catch (IOException ex) {
            throw new FastsqlException("output error", ex);
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.text == null) ? 0 : this.text.hashCode());
        return result;
    }
    
    @Override
    public SQLBinaryExpr clone() {
        return new SQLBinaryExpr(this.text);
    }
    
    @Override
    public List<SQLObject> getChildren() {
        return Collections.emptyList();
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
        final SQLBinaryExpr other = (SQLBinaryExpr)obj;
        if (this.text == null) {
            if (other.text != null) {
                return false;
            }
        }
        else if (!this.text.equals(other.text)) {
            return false;
        }
        return true;
    }
}
