// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Function0;
import scala.Function1;
import scala.collection.mutable.StringBuilder;

public abstract class Symbol$class
{
    public static String path(final Symbol $this) {
        return new StringBuilder().append((Object)$this.parent().map((Function1)new Symbol$$anonfun$path.Symbol$$anonfun$path$1($this)).getOrElse((Function0)new Symbol$$anonfun$path.Symbol$$anonfun$path$2($this))).append((Object)$this.name()).toString();
    }
    
    public static void $init$(final Symbol $this) {
    }
}
