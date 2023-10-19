// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.Manifest;

public abstract class Serialization$class
{
    public static Object read(final Serialization $this, final String json, final Formats formats, final Manifest mf) {
        return $this.read(new StringInput(json), formats, (scala.reflect.Manifest<Object>)mf);
    }
    
    public static Formats formats(final Serialization $this, final TypeHints hints) {
        return (Formats)new Serialization$$anon.Serialization$$anon$1($this, hints);
    }
    
    public static void $init$(final Serialization $this) {
    }
}
