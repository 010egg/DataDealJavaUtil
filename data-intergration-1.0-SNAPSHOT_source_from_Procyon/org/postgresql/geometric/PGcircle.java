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
import org.postgresql.util.PGobject;

public class PGcircle extends PGobject implements Serializable, Cloneable
{
    public PGpoint center;
    public double radius;
    
    public PGcircle(final double x, final double y, final double r) {
        this(new PGpoint(x, y), r);
    }
    
    public PGcircle(final PGpoint c, final double r) {
        this();
        this.center = c;
        this.radius = r;
    }
    
    public PGcircle(final String s) throws SQLException {
        this();
        this.setValue(s);
    }
    
    public PGcircle() {
        this.setType("circle");
    }
    
    @Override
    public void setValue(final String s) throws SQLException {
        final PGtokenizer t = new PGtokenizer(PGtokenizer.removeAngle(s), ',');
        if (t.getSize() != 2) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[] { this.type, s }), PSQLState.DATA_TYPE_MISMATCH);
        }
        try {
            this.center = new PGpoint(t.getToken(0));
            this.radius = Double.parseDouble(t.getToken(1));
        }
        catch (NumberFormatException e) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", new Object[] { this.type, s }), PSQLState.DATA_TYPE_MISMATCH, e);
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PGcircle) {
            final PGcircle p = (PGcircle)obj;
            return p.center.equals(this.center) && p.radius == this.radius;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        final long v = Double.doubleToLongBits(this.radius);
        return (int)((long)this.center.hashCode() ^ v ^ v >>> 32);
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        final PGcircle newPGcircle = (PGcircle)super.clone();
        if (newPGcircle.center != null) {
            newPGcircle.center = (PGpoint)newPGcircle.center.clone();
        }
        return newPGcircle;
    }
    
    @Override
    public String getValue() {
        return "<" + this.center + "," + this.radius + ">";
    }
}
