// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.prefs;

public final class EmptyValueStrategy$
{
    public static final EmptyValueStrategy$ MODULE$;
    private final EmptyValueStrategy skip;
    private final EmptyValueStrategy preserve;
    
    static {
        new EmptyValueStrategy$();
    }
    
    public EmptyValueStrategy default() {
        return this.skip();
    }
    
    public EmptyValueStrategy skip() {
        return this.skip;
    }
    
    public EmptyValueStrategy preserve() {
        return this.preserve;
    }
    
    private EmptyValueStrategy$() {
        MODULE$ = this;
        this.skip = (EmptyValueStrategy)new EmptyValueStrategy$$anon.EmptyValueStrategy$$anon$1();
        this.preserve = (EmptyValueStrategy)new EmptyValueStrategy$$anon.EmptyValueStrategy$$anon$2();
    }
}
