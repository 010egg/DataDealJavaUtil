// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.core.types;

public class PGUnknown implements PGType
{
    Object val;
    
    public PGUnknown(final Object x) {
        this.val = x;
    }
    
    public static PGType castToServerType(final Object val, final int targetType) {
        return new PGUnknown(val);
    }
    
    @Override
    public String toString() {
        return this.val.toString();
    }
}
