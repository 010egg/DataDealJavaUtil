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

public class PGpath extends PGobject implements Serializable, Cloneable
{
    public boolean open;
    public PGpoint[] points;
    
    public PGpath(final PGpoint[] points, final boolean open) {
        this();
        this.points = points;
        this.open = open;
    }
    
    public PGpath() {
        this.setType("path");
    }
    
    public PGpath(final String s) throws SQLException {
        this();
        this.setValue(s);
    }
    
    @Override
    public void setValue(String s) throws SQLException {
        if (s.startsWith("[") && s.endsWith("]")) {
            this.open = true;
            s = PGtokenizer.removeBox(s);
        }
        else {
            if (!s.startsWith("(") || !s.endsWith(")")) {
                throw new PSQLException(GT.tr("Cannot tell if path is open or closed: {0}.", s), PSQLState.DATA_TYPE_MISMATCH);
            }
            this.open = false;
            s = PGtokenizer.removePara(s);
        }
        final PGtokenizer t = new PGtokenizer(s, ',');
        final int npoints = t.getSize();
        this.points = new PGpoint[npoints];
        for (int p = 0; p < npoints; ++p) {
            this.points[p] = new PGpoint(t.getToken(p));
        }
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof PGpath)) {
            return false;
        }
        final PGpath p = (PGpath)obj;
        if (p.points.length != this.points.length) {
            return false;
        }
        if (p.open != this.open) {
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
        final PGpath newPGpath = (PGpath)super.clone();
        if (newPGpath.points != null) {
            newPGpath.points = newPGpath.points.clone();
            for (int i = 0; i < newPGpath.points.length; ++i) {
                newPGpath.points[i] = (PGpoint)newPGpath.points[i].clone();
            }
        }
        return newPGpath;
    }
    
    @Override
    public String getValue() {
        final StringBuilder b = new StringBuilder(this.open ? "[" : "(");
        for (int p = 0; p < this.points.length; ++p) {
            if (p > 0) {
                b.append(",");
            }
            b.append(this.points[p].toString());
        }
        b.append(this.open ? "]" : ")");
        return b.toString();
    }
    
    public boolean isOpen() {
        return this.open;
    }
    
    public boolean isClosed() {
        return !this.open;
    }
    
    public void closePath() {
        this.open = false;
    }
    
    public void openPath() {
        this.open = true;
    }
}
