// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

public abstract class DefaultWriters$class
{
    public static Writer arrayWriter(final DefaultWriters $this, final Writer valueWriter) {
        return (Writer)new DefaultWriters$$anon.DefaultWriters$$anon$5($this, valueWriter);
    }
    
    public static Writer mapWriter(final DefaultWriters $this, final Writer valueWriter) {
        return (Writer)new DefaultWriters$$anon.DefaultWriters$$anon$6($this, valueWriter);
    }
    
    public static Writer OptionWriter(final DefaultWriters $this, final Writer valueWriter) {
        return (Writer)new DefaultWriters$$anon.DefaultWriters$$anon$7($this, valueWriter);
    }
    
    public static void $init$(final DefaultWriters $this) {
    }
}
