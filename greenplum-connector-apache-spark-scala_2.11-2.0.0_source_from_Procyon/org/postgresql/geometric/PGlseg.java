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

public class PGlseg extends PGobject implements Serializable, Cloneable
{
    public PGpoint[] point;
    
    public PGlseg(final double x1, final double y1, final double x2, final double y2) {
        this(new PGpoint(x1, y1), new PGpoint(x2, y2));
    }
    
    public PGlseg(final PGpoint p1, final PGpoint p2) {
        this();
        this.point[0] = p1;
        this.point[1] = p2;
    }
    
    public PGlseg(final String s) throws SQLException {
        this();
        this.setValue(s);
    }
    
    public PGlseg() {
        this.point = new PGpoint[2];
        this.setType("lseg");
    }
    
    @Override
    public void setValue(final String s) throws SQLException {
        final PGtokenizer t = new PGtokenizer(PGtokenizer.removeBox(s), ',');
        if (t.getSize() != 2) {
            throw new PSQLException(GT.tr("Conversion to type {0} failed: {1}.", this.type, s), PSQLState.DATA_TYPE_MISMATCH);
        }
        this.point[0] = new PGpoint(t.getToken(0));
        this.point[1] = new PGpoint(t.getToken(1));
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PGlseg) {
            final PGlseg p = (PGlseg)obj;
            return (p.point[0].equals(this.point[0]) && p.point[1].equals(this.point[1])) || (p.point[0].equals(this.point[1]) && p.point[1].equals(this.point[0]));
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return this.point[0].hashCode() ^ this.point[1].hashCode();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        final PGlseg newPGlseg = (PGlseg)super.clone();
        if (newPGlseg.point != null) {
            newPGlseg.point = newPGlseg.point.clone();
            for (int i = 0; i < newPGlseg.point.length; ++i) {
                if (newPGlseg.point[i] != null) {
                    newPGlseg.point[i] = (PGpoint)newPGlseg.point[i].clone();
                }
            }
        }
        return newPGlseg;
    }
    
    @Override
    public String getValue() {
        return "[" + this.point[0] + "," + this.point[1] + "]";
    }
}
