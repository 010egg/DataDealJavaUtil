// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.ast.expr;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.util.HexBin;
import com.alibaba.druid.sql.visitor.SQLASTVisitor;
import java.io.IOException;
import com.alibaba.druid.FastsqlException;
import com.alibaba.druid.sql.ast.SQLExprImpl;

public class SQLHexExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr
{
    private final String hex;
    
    public SQLHexExpr(final String hex) {
        this.hex = hex;
    }
    
    public String getHex() {
        return this.hex;
    }
    
    @Override
    public void output(final Appendable buf) {
        try {
            buf.append("0x");
            buf.append(this.hex);
            final String charset = (String)this.getAttribute("USING");
            if (charset != null) {
                buf.append(" USING ");
                buf.append(charset);
            }
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + ((this.hex == null) ? 0 : this.hex.hashCode());
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
        final SQLHexExpr other = (SQLHexExpr)obj;
        if (this.hex == null) {
            if (other.hex != null) {
                return false;
            }
        }
        else if (!this.hex.equals(other.hex)) {
            return false;
        }
        return true;
    }
    
    public byte[] toBytes() {
        return HexBin.decode(this.hex);
    }
    
    @Override
    public SQLHexExpr clone() {
        return new SQLHexExpr(this.hex);
    }
    
    @Override
    public byte[] getValue() {
        return this.toBytes();
    }
    
    public SQLCharExpr toCharExpr() {
        final byte[] bytes = this.toBytes();
        if (bytes == null) {
            return null;
        }
        final String str = new String(bytes, SQLUtils.UTF8);
        return new SQLCharExpr(str);
    }
}
