// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.reflect.Manifest;

public abstract class DefaultReaders$class
{
    public static Reader mapReader(final DefaultReaders $this, final Reader valueReader) {
        return (Reader)new DefaultReaders$$anon.DefaultReaders$$anon$2($this, valueReader);
    }
    
    public static Reader arrayReader(final DefaultReaders $this, final Manifest evidence$1, final Reader evidence$2) {
        return (Reader)new DefaultReaders$$anon.DefaultReaders$$anon$3($this, evidence$1, evidence$2);
    }
    
    public static Reader OptionReader(final DefaultReaders $this, final Reader valueReader) {
        return (Reader)new DefaultReaders$$anon.DefaultReaders$$anon$4($this, valueReader);
    }
    
    public static void $init$(final DefaultReaders $this) {
    }
}
