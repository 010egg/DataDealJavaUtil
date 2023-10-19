// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.Function0;
import scala.concurrent.Future$;
import scala.concurrent.Future;
import scala.concurrent.ExecutionContext;
import scala.collection.Traversable;
import scala.Predef;
import scala.collection.immutable.List;
import scala.Function1;

public abstract class ManagedResourceOperations$class
{
    public static Object acquireAndGet(final ManagedResourceOperations $this, final Function1 f) {
        return $this.apply(f);
    }
    
    public static Object apply(final ManagedResourceOperations $this, final Function1 f) {
        return package$.MODULE$.extractedEitherToEither((ExtractedEither<List<Throwable>, Object>)$this.acquireFor(f)).fold((Function1)new ManagedResourceOperations$$anonfun$apply.ManagedResourceOperations$$anonfun$apply$1($this), (Function1)new ManagedResourceOperations$$anonfun$apply.ManagedResourceOperations$$anonfun$apply$3($this));
    }
    
    public static Traversable toTraversable(final ManagedResourceOperations $this, final Predef.$less$colon$less ev) {
        return (Traversable)new ManagedResourceOperations$$anon.ManagedResourceOperations$$anon$1($this, ev);
    }
    
    public static Future toFuture(final ManagedResourceOperations $this, final ExecutionContext context) {
        return Future$.MODULE$.apply((Function0)new ManagedResourceOperations$$anonfun$toFuture.ManagedResourceOperations$$anonfun$toFuture$1($this), context);
    }
    
    public static ExtractableManagedResource map(final ManagedResourceOperations $this, final Function1 f) {
        return new DeferredExtractableManagedResource($this, f);
    }
    
    public static ManagedResource flatMap(final ManagedResourceOperations $this, final Function1 f) {
        return (ManagedResource)new ManagedResourceOperations$$anon.ManagedResourceOperations$$anon$2($this, f);
    }
    
    public static void foreach(final ManagedResourceOperations $this, final Function1 f) {
        $this.acquireAndGet(f);
    }
    
    public static ManagedResource and(final ManagedResourceOperations $this, final ManagedResource that) {
        return package$.MODULE$.and((ManagedResource<Object>)$this, (ManagedResource<Object>)that);
    }
    
    public static void $init$(final ManagedResourceOperations $this) {
    }
}
