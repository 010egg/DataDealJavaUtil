// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.geometric;

import java.awt.Point;
import org.postgresql.util.ByteConverter;
import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.util.PGtokenizer;
import java.sql.SQLException;
import java.io.Serializable;
import org.postgresql.util.PGBinaryObject;
import org.postgresql.util.PGobject;

public class PGpoint extends PGobject implements PGBinaryObject, Serializable, Cloneable
{
    public double x;
    public double y;
    
    public PGpoint(final double x, final double y) {
        this();
        this.x = x;
        this.y = y;
    }
    
    public PGpoint(final String value) throws SQLException {
        this();
        this.setValue(value);
    }
    
    public PGpoint() {
        this.setType("point");
    }
    
    @Override
    public void setValue(final String s) throws SQLException {
        final PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(s), ',');
        try {
            this.x = Double.parseDouble(t.getToken(0));
            this.y = Double.parseDouble(t.getToken(1));
        }
        catch (NumberFormatException e) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, s), PSQLState.DATA_TYPE_MISMATCH, e);
        }
    }
    
    @Override
    public void setByteValue(final byte[] b, final int offset) {
        this.x = ByteConverter.float8(b, offset);
        this.y = ByteConverter.float8(b, offset + 8);
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PGpoint) {
            final PGpoint p = (PGpoint)obj;
            return this.x == p.x && this.y == p.y;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long v1 = Double.doubleToLongBits(this.x);
        final long v2 = Double.doubleToLongBits(this.y);
        return (int)(v1 ^ v2 ^ v1 >>> 32 ^ v2 >>> 32);
    }
    
    @Override
    public String getValue() {
        return "(" + this.x + "," + this.y + ")";
    }
    
    @Override
    public int lengthInBytes() {
        return 16;
    }
    
    @Override
    public void toBytes(final byte[] b, final int offset) {
        ByteConverter.float8(b, offset, this.x);
        ByteConverter.float8(b, offset + 8, this.y);
    }
    
    public void translate(final int x, final int y) {
        this.translate(x, (double)y);
    }
    
    public void translate(final double x, final double y) {
        this.x += x;
        this.y += y;
    }
    
    public void move(final int x, final int y) {
        this.setLocation(x, y);
    }
    
    public void move(final double x, final double y) {
        this.x = x;
        this.y = y;
    }
    
    public void setLocation(final int x, final int y) {
        this.move(x, (double)y);
    }
    
    public void setLocation(final Point p) {
        this.setLocation(p.x, p.y);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
