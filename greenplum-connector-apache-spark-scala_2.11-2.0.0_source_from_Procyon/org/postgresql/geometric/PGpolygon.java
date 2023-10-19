// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.geometric;

import org.postgresql.util.PGtokenizer;
import java.sql.SQLException;
import java.io.Serializable;
import org.postgresql.util.PGobject;

public class PGpolygon extends PGobject implements Serializable, Cloneable
{
    public PGpoint[] points;
    
    public PGpolygon(final PGpoint[] points) {
        this();
        this.points = points;
    }
    
    public PGpolygon(final String s) throws SQLException {
        this();
        this.setValue(s);
    }
    
    public PGpolygon() {
        this.setType("polygon");
    }
    
    @Override
    public void setValue(final String s) throws SQLException {
        final PGtokenizer t = new PGtokenizer(PGtokenizer.removePara(s), ',');
        final int npoints = t.getSize();
        this.points = new PGpoint[npoints];
        for (int p = 0; p < npoints; ++p) {
            this.points[p] = new PGpoint(t.getToken(p));
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof PGpolygon)) {
            return false;
        }
        final PGpolygon p = (PGpolygon)obj;
        if (p.points.length != this.points.length) {
            return false;
        }
        for (int i = 0; i < this.points.length; ++i) {
            if (!this.points[i].equals(p.points[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        for (int i = 0; i < this.points.length && i < 5; ++i) {
            hash ^= this.points[i].hashCode();
        }
        return hash;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        final PGpolygon newPGpolygon = (PGpolygon)super.clone();
        if (newPGpolygon.points != null) {
            newPGpolygon.points = newPGpolygon.points.clone();
            for (int i = 0; i < newPGpolygon.points.length; ++i) {
                if (newPGpolygon.points[i] != null) {
                    newPGpolygon.points[i] = (PGpoint)newPGpolygon.points[i].clone();
                }
            }
        }
        return newPGpolygon;
    }
    
    @Override
    public String getValue() {
        final StringBuilder b = new StringBuilder();
        b.append("(");
        for (int p = 0; p < this.points.length; ++p) {
            if (p > 0) {
                b.append(",");
            }
            b.append(this.points[p].toString());
        }
        b.append(")");
        return b.toString();
    }
}
