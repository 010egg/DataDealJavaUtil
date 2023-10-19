// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

public abstract class DefaultJsonFormats$class
{
    public static JsonFormat GenericFormat(final DefaultJsonFormats $this, final Reader reader, final Writer writer) {
        return (JsonFormat)new DefaultJsonFormats$$anon.DefaultJsonFormats$$anon$1($this, reader, writer);
    }
    
    public static void $init$(final DefaultJsonFormats $this) {
    }
}
