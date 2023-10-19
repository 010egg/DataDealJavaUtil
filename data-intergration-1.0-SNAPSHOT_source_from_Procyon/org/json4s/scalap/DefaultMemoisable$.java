// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

public final class DefaultMemoisable$
{
    public static final DefaultMemoisable$ MODULE$;
    private boolean debug;
    
    static {
        new DefaultMemoisable$();
    }
    
    public boolean debug() {
        return this.debug;
    }
    
    public void debug_$eq(final boolean x$1) {
        this.debug = x$1;
    }
    
    private DefaultMemoisable$() {
        MODULE$ = this;
        this.debug = false;
    }
}
