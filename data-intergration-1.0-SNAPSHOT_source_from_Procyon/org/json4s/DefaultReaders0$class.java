// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.collection.generic.CanBuildFrom;

public abstract class DefaultReaders0$class
{
    public static Reader iterableReader(final DefaultReaders0 $this, final CanBuildFrom cbf, final Reader valueReader) {
        return (Reader)new DefaultReaders0$$anon.DefaultReaders0$$anon$1($this, cbf, valueReader);
    }
    
    public static void $init$(final DefaultReaders0 $this) {
    }
}
