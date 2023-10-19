// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.sql.SQLException;
import java.io.Serializable;

public class PGobject implements Serializable, Cloneable
{
    protected String type;
    protected String value;
    
    public final void setType(final String type) {
        this.type = type;
    }
    
    public void setValue(final String value) throws SQLException {
        this.value = value;
    }
    
    public final String getType() {
        return this.type;
    }
    
    public String getValue() {
        return this.value;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (!(obj instanceof PGobject)) {
            return false;
        }
        final Object otherValue = ((PGobject)obj).getValue();
        if (otherValue == null) {
            return this.getValue() == null;
        }
        return otherValue.equals(this.getValue());
    }
    
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    
    @Override
    public String toString() {
        return this.getValue();
    }
    
    @Override
    public int hashCode() {
        return (this.getValue() != null) ? this.getValue().hashCode() : 0;
    }
}
