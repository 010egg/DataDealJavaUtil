// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.MatchError;
import scala.runtime.BoxesRunTime;
import scala.Some;
import scala.Option;
import scala.util.Either;
import scala.Predef$;
import scala.runtime.VolatileObjectRef;
import scala.None$;
import scala.collection.Iterator;
import scala.collection.SeqLike;
import scala.collection.Seq;
import scala.Predef;
import scala.Tuple2;
import scala.collection.immutable.List;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.reflect.OptManifest;
import scala.Function0;

public final class package$
{
    public static final package$ MODULE$;
    
    static {
        new package$();
    }
    
    public <A> ManagedResource<A> managed(final Function0<A> opener, final Resource<A> evidence$1, final OptManifest<A> evidence$2) {
        return new DefaultManagedResource<A>(opener, evidence$1, evidence$2);
    }
    
    public <V> ManagedResource<V> constant(final V value) {
        return new ConstantManagedResource<V>(value);
    }
    
    public <R> DefaultManagedResource<R> makeManagedResource(final Function0<R> opener, final Function1<R, BoxedUnit> closer, final List<Class<? extends Throwable>> exceptions, final OptManifest<R> evidence$3) {
        final Resource typeTrait = (Resource)new package$$anon.package$$anon$2((Function1)closer, (List)exceptions);
        return new DefaultManagedResource<R>(opener, typeTrait, evidence$3);
    }
    
    public <A, B> ManagedResource<Tuple2<A, B>> and(final ManagedResource<A> r1, final ManagedResource<B> r2) {
        return r1.flatMap((scala.Function1<A, ManagedResource<Tuple2<A, B>>>)new package$$anonfun$and.package$$anonfun$and$1((ManagedResource)r2));
    }
    
    public <A, MR, CC> ManagedResource<Seq<A>> join(final CC resources, final Predef.$less$colon$less<CC, Seq<MR>> ev0, final Predef.$less$colon$less<MR, ManagedResource<A>> ev1) {
        final Iterator itr = ((SeqLike)ev0.apply((Object)resources)).reverseIterator();
        final ManagedResource first = (ManagedResource)ev1.apply(itr.next());
        ManagedResource toReturn = first.map((Function1)new package$$anonfun.package$$anonfun$1());
        while (itr.hasNext()) {
            final ManagedResource r1 = toReturn;
            final ManagedResource r2 = (ManagedResource)ev1.apply(itr.next());
            toReturn = (ManagedResource)new package$$anon.package$$anon$1(r1, r2);
        }
        return (ManagedResource<Seq<A>>)toReturn;
    }
    
    public <A> ManagedResource<A> shared(final Function0<A> opener, final Resource<A> evidence$4, final OptManifest<A> evidence$5) {
        final VolatileObjectRef sharedReference = VolatileObjectRef.create((Object)None$.MODULE$);
        final Object lock = new Object();
        final Resource resource = (Resource)new package$$anon.package$$anon$3((Resource)evidence$4, sharedReference, lock);
        return new DefaultManagedResource<A>((scala.Function0<A>)new package$$anonfun$shared.package$$anonfun$shared$1((Function0)opener, (Resource)evidence$4, sharedReference, lock), resource, (scala.reflect.OptManifest<A>)Predef$.MODULE$.implicitly((Object)evidence$5));
    }
    
    public <A, B> Either<A, B> extractedEitherToEither(final ExtractedEither<A, B> extracted) {
        return extracted.either();
    }
    
    public final Object resource$package$$acquire$1(final Function0 opener$1, final Resource evidence$4$1, final VolatileObjectRef sharedReference$1, final Object lock$1) {
        synchronized (lock$1) {
            final Option obj = (Option)sharedReference$1.elem;
            Tuple2 tuple3 = null;
            Label_0131: {
                if (!None$.MODULE$.equals(obj)) {
                    if (obj instanceof Some) {
                        final Tuple2 tuple2 = (Tuple2)((Some)obj).x();
                        if (tuple2 != null) {
                            final int oldReferenceCount = tuple2._1$mcI$sp();
                            final Object sc = tuple2._2();
                            tuple3 = new Tuple2((Object)BoxesRunTime.boxToInteger(oldReferenceCount + 1), sc);
                            break Label_0131;
                        }
                    }
                    throw new MatchError((Object)obj);
                }
                final Object r = opener$1.apply();
                ((Resource)Predef$.MODULE$.implicitly((Object)evidence$4$1)).open(r);
                tuple3 = new Tuple2((Object)BoxesRunTime.boxToInteger(1), r);
            }
            final Tuple2 tuple4 = tuple3;
            if (tuple4 != null) {
                final int referenceCount = tuple4._1$mcI$sp();
                final Object sc2 = tuple4._2();
                final Tuple2 tuple5 = new Tuple2((Object)BoxesRunTime.boxToInteger(referenceCount), sc2);
                final int referenceCount2 = tuple5._1$mcI$sp();
                final Object sc3 = tuple5._2();
                sharedReference$1.elem = new Some((Object)new Tuple2((Object)BoxesRunTime.boxToInteger(referenceCount2), sc3));
                // monitorexit(lock$1)
                return sc3;
            }
            throw new MatchError((Object)tuple4);
        }
    }
    
    private package$() {
        MODULE$ = this;
    }
}
