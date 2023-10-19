// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Function1;
import scala.collection.immutable.List;

public abstract class ManagedTraversable$class
{
    public static boolean ignoreError(final ManagedTraversable $this, final Exception error) {
        return false;
    }
    
    public static void handleErrorsDuringTraversal(final ManagedTraversable $this, final List ex) {
        ex.headOption().foreach((Function1)new ManagedTraversable$$anonfun$handleErrorsDuringTraversal.ManagedTraversable$$anonfun$handleErrorsDuringTraversal$1($this));
    }
    
    public static void foreach(final ManagedTraversable $this, final Function1 f) {
        final ExtractedEither result = $this.resource().acquireFor((Function1)new ManagedTraversable$$anonfun.ManagedTraversable$$anonfun$1($this, f));
        package$.MODULE$.extractedEitherToEither((ExtractedEither<Object, Object>)result).left().foreach((Function1)new ManagedTraversable$$anonfun$foreach.ManagedTraversable$$anonfun$foreach$1($this));
    }
    
    public static String toString(final ManagedTraversable $this) {
        return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "ManagedTraversable(", ")" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { $this.resource() }));
    }
    
    public static void $init$(final ManagedTraversable $this) {
    }
}
