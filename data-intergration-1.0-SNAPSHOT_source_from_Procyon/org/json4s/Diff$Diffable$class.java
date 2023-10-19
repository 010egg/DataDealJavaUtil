// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

public abstract class Diff$Diffable$class
{
    public static Diff diff(final JsonAST.JValue $this, final JsonAST.JValue other) {
        return Diff$.MODULE$.diff($this, other);
    }
    
    public static void $init$(final JsonAST.JValue $this) {
    }
}
