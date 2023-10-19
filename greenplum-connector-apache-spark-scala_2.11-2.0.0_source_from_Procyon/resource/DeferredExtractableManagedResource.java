// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.collection.mutable.StringBuilder;
import scala.Function0;
import scala.util.Try$;
import scala.util.Try;
import scala.Option;
import scala.collection.immutable.List;
import scala.Tuple2;
import scala.runtime.BoxedUnit;
import scala.concurrent.Future;
import scala.concurrent.ExecutionContext;
import scala.collection.Traversable;
import scala.collection.TraversableOnce;
import scala.Predef;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001}4a!\u0001\u0002\u0001\u0005\u0011!$A\t#fM\u0016\u0014(/\u001a3FqR\u0014\u0018m\u0019;bE2,W*\u00198bO\u0016$'+Z:pkJ\u001cWMC\u0001\u0004\u0003!\u0011Xm]8ve\u000e,WcA\u0003\u0013KM!\u0001A\u0002\u0007\u001d!\t9!\"D\u0001\t\u0015\u0005I\u0011!B:dC2\f\u0017BA\u0006\t\u0005\u0019\te.\u001f*fMB\u0019QB\u0004\t\u000e\u0003\tI!a\u0004\u0002\u00035\u0015CHO]1di\u0006\u0014G.Z'b]\u0006<W\r\u001a*fg>,(oY3\u0011\u0005E\u0011B\u0002\u0001\u0003\u0007'\u0001!)\u0019A\u000b\u0003\u0003\u0005\u001b\u0001!\u0005\u0002\u00173A\u0011qaF\u0005\u00031!\u0011qAT8uQ&tw\r\u0005\u0002\b5%\u00111\u0004\u0003\u0002\u0004\u0003:L\bcA\u0007\u001e!%\u0011aD\u0001\u0002\u001a\u001b\u0006t\u0017mZ3e%\u0016\u001cx.\u001e:dK>\u0003XM]1uS>t7\u000f\u0003\u0005\u0004\u0001\t\u0015\r\u0011\"\u0001!+\u0005\t\u0003cA\u0007#I%\u00111E\u0001\u0002\u0010\u001b\u0006t\u0017mZ3e%\u0016\u001cx.\u001e:dKB\u0011\u0011#\n\u0003\u0006M\u0001\u0011\r!\u0006\u0002\u0002%\"A\u0001\u0006\u0001B\u0001B\u0003%\u0011%A\u0005sKN|WO]2fA!A!\u0006\u0001BC\u0002\u0013\u00051&A\u0005ue\u0006t7\u000f\\1uKV\tA\u0006\u0005\u0003\b[\u0011\u0002\u0012B\u0001\u0018\t\u0005%1UO\\2uS>t\u0017\u0007\u0003\u00051\u0001\t\u0005\t\u0015!\u0003-\u0003)!(/\u00198tY\u0006$X\r\t\u0005\u0006e\u0001!\taM\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0007Q*d\u0007\u0005\u0003\u000e\u0001A!\u0003\"B\u00022\u0001\u0004\t\u0003\"\u0002\u00162\u0001\u0004a\u0003\"\u0002\u001d\u0001\t\u0003J\u0014AC1dcVL'/\u001a$peV\u0011!H\u0014\u000b\u0003wA\u0003B!\u0004\u001f?\u001b&\u0011QH\u0001\u0002\u0010\u000bb$(/Y2uK\u0012,\u0015\u000e\u001e5feB\u0019qh\u0012&\u000f\u0005\u0001+eBA!E\u001b\u0005\u0011%BA\"\u0015\u0003\u0019a$o\\8u}%\t\u0011\"\u0003\u0002G\u0011\u00059\u0001/Y2lC\u001e,\u0017B\u0001%J\u0005\u0011a\u0015n\u001d;\u000b\u0005\u0019C\u0001CA L\u0013\ta\u0015JA\u0005UQJ|w/\u00192mKB\u0011\u0011C\u0014\u0003\u0006\u001f^\u0012\r!\u0006\u0002\u0002\u0005\")\u0011k\u000ea\u0001%\u0006\ta\r\u0005\u0003\b[Ai\u0005\"\u0002+\u0001\t\u0003*\u0016AB3ji\",'/F\u0001W!\u0011iAH\u0010\t\t\u000ba\u0003A\u0011I-\u0002\u0007=\u0004H/F\u0001[!\r91\fE\u0005\u00039\"\u0011aa\u00149uS>t\u0007\"\u00020\u0001\t\u0003z\u0016!\u0002;sS\u0016$W#\u00011\u0011\u0007\u0005$\u0007#D\u0001c\u0015\t\u0019\u0007\"\u0001\u0003vi&d\u0017BA3c\u0005\r!&/\u001f\u0005\u0006O\u0002!\t\u0005[\u0001\u0007KF,\u0018\r\\:\u0015\u0005%d\u0007CA\u0004k\u0013\tY\u0007BA\u0004C_>dW-\u00198\t\u000b54\u0007\u0019A\r\u0002\tQD\u0017\r\u001e\u0005\u0006_\u0002!\t\u0005]\u0001\tQ\u0006\u001c\bnQ8eKR\t\u0011\u000f\u0005\u0002\be&\u00111\u000f\u0003\u0002\u0004\u0013:$\b\"B;\u0001\t\u00032\u0018\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003]\u0004\"\u0001_?\u000e\u0003eT!A_>\u0002\t1\fgn\u001a\u0006\u0002y\u0006!!.\u0019<b\u0013\tq\u0018P\u0001\u0004TiJLgn\u001a")
public class DeferredExtractableManagedResource<A, R> implements ExtractableManagedResource<A>, ManagedResourceOperations<A>
{
    private final ManagedResource<R> resource;
    private final Function1<R, A> translate;
    
    @Override
    public <B> B acquireAndGet(final Function1<A, B> f) {
        return (B)ManagedResourceOperations$class.acquireAndGet(this, f);
    }
    
    @Override
    public <B> B apply(final Function1<A, B> f) {
        return (B)ManagedResourceOperations$class.apply(this, f);
    }
    
    @Override
    public <B> Traversable<B> toTraversable(final Predef.$less$colon$less<A, TraversableOnce<B>> ev) {
        return (Traversable<B>)ManagedResourceOperations$class.toTraversable(this, ev);
    }
    
    @Override
    public Future<A> toFuture(final ExecutionContext context) {
        return (Future<A>)ManagedResourceOperations$class.toFuture(this, context);
    }
    
    @Override
    public <B> ExtractableManagedResource<B> map(final Function1<A, B> f) {
        return (ExtractableManagedResource<B>)ManagedResourceOperations$class.map(this, f);
    }
    
    @Override
    public <B> ManagedResource<B> flatMap(final Function1<A, ManagedResource<B>> f) {
        return (ManagedResource<B>)ManagedResourceOperations$class.flatMap(this, f);
    }
    
    @Override
    public void foreach(final Function1<A, BoxedUnit> f) {
        ManagedResourceOperations$class.foreach(this, f);
    }
    
    @Override
    public <B> ManagedResource<Tuple2<A, B>> and(final ManagedResource<B> that) {
        return (ManagedResource<Tuple2<A, B>>)ManagedResourceOperations$class.and(this, that);
    }
    
    public ManagedResource<R> resource() {
        return this.resource;
    }
    
    public Function1<R, A> translate() {
        return this.translate;
    }
    
    @Override
    public <B> ExtractedEither<List<Throwable>, B> acquireFor(final Function1<A, B> f) {
        return this.resource().acquireFor((scala.Function1<R, B>)this.translate().andThen((Function1)f));
    }
    
    @Override
    public ExtractedEither<List<Throwable>, A> either() {
        return new ExtractedEither<List<Throwable>, A>(package$.MODULE$.extractedEitherToEither((ExtractedEither<List<Throwable>, A>)this.resource().acquireFor(this.translate())));
    }
    
    @Override
    public Option<A> opt() {
        return (Option<A>)this.either().either().right().toOption();
    }
    
    @Override
    public Try<A> tried() {
        return (Try<A>)Try$.MODULE$.apply((Function0)new DeferredExtractableManagedResource$$anonfun$tried.DeferredExtractableManagedResource$$anonfun$tried$1(this));
    }
    
    @Override
    public boolean equals(final Object that) {
        boolean b2;
        if (that instanceof DeferredExtractableManagedResource) {
            final DeferredExtractableManagedResource deferredExtractableManagedResource = (DeferredExtractableManagedResource)that;
            final ManagedResource resource = deferredExtractableManagedResource.resource();
            final ManagedResource<R> resource2 = this.resource();
            boolean b = false;
            Label_0081: {
                Label_0080: {
                    if (resource == null) {
                        if (resource2 != null) {
                            break Label_0080;
                        }
                    }
                    else if (!resource.equals(resource2)) {
                        break Label_0080;
                    }
                    final Function1 translate = deferredExtractableManagedResource.translate();
                    final Function1<R, A> translate2 = this.translate();
                    if (translate == null) {
                        if (translate2 != null) {
                            break Label_0080;
                        }
                    }
                    else if (!translate.equals(translate2)) {
                        break Label_0080;
                    }
                    b = true;
                    break Label_0081;
                }
                b = false;
            }
            b2 = b;
        }
        else {
            b2 = false;
        }
        return b2;
    }
    
    @Override
    public int hashCode() {
        return (this.resource().hashCode() << 7) + this.translate().hashCode() + 13;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append((Object)"DeferredExtractableManagedResource(").append((Object)this.resource()).append((Object)", ").append((Object)this.translate()).append((Object)")").toString();
    }
    
    public DeferredExtractableManagedResource(final ManagedResource<R> resource, final Function1<R, A> translate) {
        this.resource = resource;
        this.translate = translate;
        ManagedResourceOperations$class.$init$(this);
    }
}
