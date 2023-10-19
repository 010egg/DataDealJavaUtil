// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.geometric;

import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLState;
import org.postgresql.util.GT;
import org.postgresql.util.PGtokenizer;
import java.sql.SQLException;
import java.io.Serializable;
import org.postgresql.util.PGBinaryObject;
import org.postgresql.util.PGobject;

public class PGbox extends PGobject implements PGBinaryObject, Serializable, Cloneable
{
    public PGpoint[] point;
    
    public PGbox(final double x1, final double y1, final double x2, final double y2) {
        this();
        this.point[0] = new PGpoint(x1, y1);
        this.point[1] = new PGpoint(x2, y2);
    }
    
    public PGbox(final PGpoint p1, final PGpoint p2) {
        this();
        this.point[0] = p1;
        this.point[1] = p2;
    }
    
    public PGbox(final String s) throws SQLException {
        this();
        this.setValue(s);
    }
    
    public PGbox() {
        this.point = new PGpoint[2];
        this.setType("box");
    }
    
    @Override
    public void setValue(final String value) throws SQLException {
        final PGtokenizer t = new PGtokenizer(value, ',');
        if (t.getSize() != 2) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, value), PSQLState.DATA_TYPE_MISMATCH);
        }
        this.point[0] = new PGpoint(t.getToken(0));
        this.point[1] = new PGpoint(t.getToken(1));
    }
    
    @Override
    public void setByteValue(final byte[] b, final int offset) {
        (this.point[0] = new PGpoint()).setByteValue(b, offset);
        (this.point[1] = new PGpoint()).setByteValue(b, offset + this.point[0].lengthInBytes());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PGbox) {
            final PGbox p = (PGbox)obj;
            if (p.point[0].equals(this.point[0]) && p.point[1].equals(this.point[1])) {
                return true;
            }
            if (p.point[0].equals(this.point[1]) && p.point[1].equals(this.point[0])) {
                return true;
            }
            if (p.point[0].x == this.point[0].x && p.point[0].y == this.point[1].y && p.point[1].x == this.point[1].x && p.point[1].y == this.point[0].y) {
                return true;
            }
            if (p.point[0].x == this.point[1].x && p.point[0].y == this.point[0].y && p.point[1].x == this.point[0].x && p.point[1].y == this.point[1].y) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.point[0].hashCode() ^ this.point[1].hashCode();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        final PGbox newPGbox = (PGbox)super.clone();
        if (newPGbox.point != null) {
            newPGbox.point = newPGbox.point.clone();
            for (int i = 0; i < newPGbox.point.length; ++i) {
                if (newPGbox.point[i] != null) {
                    newPGbox.point[i] = (PGpoint)newPGbox.point[i].clone();
                }
            }
        }
        return newPGbox;
    }
    
    @Override
    public String getValue() {
        return this.point[0].toString() + "," + this.point[1].toString();
    }
    
    @Override
    public int lengthInBytes() {
        return this.point[0].lengthInBytes() + this.point[1].lengthInBytes();
    }
    
    @Override
    public void toBytes(final byte[] bytes, final int offset) {
        this.point[0].toBytes(bytes, offset);
        this.point[1].toBytes(bytes, offset + this.point[0].lengthInBytes());
    }
}
