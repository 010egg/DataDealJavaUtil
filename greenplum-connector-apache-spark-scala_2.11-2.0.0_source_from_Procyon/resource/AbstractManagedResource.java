// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.None$;
import scala.util.control.Exception$;
import scala.MatchError;
import scala.collection.immutable.List$;
import scala.util.Right;
import scala.collection.immutable.Nil$;
import scala.package$;
import scala.util.Left;
import scala.util.Either;
import scala.Function0;
import scala.collection.immutable.List;
import scala.util.control.ControlThrowable;
import scala.Option;
import scala.Tuple2;
import scala.runtime.BoxedUnit;
import scala.concurrent.Future;
import scala.concurrent.ExecutionContext;
import scala.collection.Traversable;
import scala.collection.TraversableOnce;
import scala.Predef;
import scala.Function1;
import scala.runtime.Nothing$;
import scala.util.control.Exception;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u000194Q!\u0001\u0002\u0002\u0002\u0015\u0011q#\u00112tiJ\f7\r^'b]\u0006<W\r\u001a*fg>,(oY3\u000b\u0003\r\t\u0001B]3t_V\u00148-Z\u0002\u0001+\t11c\u0005\u0003\u0001\u000f5a\u0002C\u0001\u0005\f\u001b\u0005I!\"\u0001\u0006\u0002\u000bM\u001c\u0017\r\\1\n\u00051I!AB!osJ+g\rE\u0002\u000f\u001fEi\u0011AA\u0005\u0003!\t\u0011q\"T1oC\u001e,GMU3t_V\u00148-\u001a\t\u0003%Ma\u0001\u0001B\u0003\u0015\u0001\t\u0007QCA\u0001S#\t1\u0012\u0004\u0005\u0002\t/%\u0011\u0001$\u0003\u0002\b\u001d>$\b.\u001b8h!\tA!$\u0003\u0002\u001c\u0013\t\u0019\u0011I\\=\u0011\u00079i\u0012#\u0003\u0002\u001f\u0005\tIR*\u00198bO\u0016$'+Z:pkJ\u001cWm\u00149fe\u0006$\u0018n\u001c8t\u0011\u0015\u0001\u0003\u0001\"\u0001\"\u0003\u0019a\u0014N\\5u}Q\t!\u0005E\u0002\u000f\u0001EAQ\u0001\n\u0001\u0007\u0012\u0015\nAa\u001c9f]V\t\u0011\u0003C\u0003(\u0001\u0019E\u0001&A\u0006v]N\fg-Z\"m_N,GcA\u0015-]A\u0011\u0001BK\u0005\u0003W%\u0011A!\u00168ji\")QF\na\u0001#\u00051\u0001.\u00198eY\u0016DQa\f\u0014A\u0002A\na!\u001a:s_J\u001c\bc\u0001\u00052g%\u0011!'\u0003\u0002\u0007\u001fB$\u0018n\u001c8\u0011\u0005QbdBA\u001b;\u001d\t1\u0014(D\u00018\u0015\tAD!\u0001\u0004=e>|GOP\u0005\u0002\u0015%\u00111(C\u0001\ba\u0006\u001c7.Y4f\u0013\tidHA\u0005UQJ|w/\u00192mK*\u00111(\u0003\u0005\u0006\u0001\u0002!\t\"Q\u0001\u000bSN\u0014V\r\u001e5s_^tGC\u0001\"F!\tA1)\u0003\u0002E\u0013\t9!i\\8mK\u0006t\u0007\"\u0002$@\u0001\u0004\u0019\u0014!\u0001;\t\u000b!\u0003A\u0011C%\u0002\u000f%\u001ch)\u0019;bYR\u0011!I\u0013\u0005\u0006\r\u001e\u0003\ra\r\u0005\b\u0019\u0002\u0011\r\u0011\"\u0004N\u0003A\u0019\u0017\r^2iS:<gj\u001c8GCR\fG.F\u0001O!\ryuK\u0006\b\u0003!Vk\u0011!\u0015\u0006\u0003%N\u000bqaY8oiJ|GN\u0003\u0002U\u0013\u0005!Q\u000f^5m\u0013\t1\u0016+A\u0005Fq\u000e,\u0007\u000f^5p]&\u0011\u0001,\u0017\u0002\u0006\u0007\u0006$8\r\u001b\u0006\u0003-FCaa\u0017\u0001!\u0002\u001bq\u0015!E2bi\u000eD\u0017N\\4O_:4\u0015\r^1mA!)Q\f\u0001C!=\u0006Q\u0011mY9vSJ,gi\u001c:\u0016\u0005};GC\u00011j!\u0011q\u0011m\u00194\n\u0005\t\u0014!aD#yiJ\f7\r^3e\u000b&$\b.\u001a:\u0011\u0007Q\"7'\u0003\u0002f}\t!A*[:u!\t\u0011r\rB\u0003i9\n\u0007QCA\u0001C\u0011\u0015QG\f1\u0001l\u0003\u00051\u0007\u0003\u0002\u0005m#\u0019L!!\\\u0005\u0003\u0013\u0019+hn\u0019;j_:\f\u0004")
public abstract class AbstractManagedResource<R> implements ManagedResourceOperations<R>
{
    private final Exception.Catch<Nothing$> catchingNonFatal;
    
    @Override
    public <B> B acquireAndGet(final Function1<R, B> f) {
        return (B)ManagedResourceOperations$class.acquireAndGet(this, f);
    }
    
    @Override
    public <B> B apply(final Function1<R, B> f) {
        return (B)ManagedResourceOperations$class.apply(this, f);
    }
    
    @Override
    public <B> Traversable<B> toTraversable(final Predef.$less$colon$less<R, TraversableOnce<B>> ev) {
        return (Traversable<B>)ManagedResourceOperations$class.toTraversable(this, ev);
    }
    
    @Override
    public Future<R> toFuture(final ExecutionContext context) {
        return (Future<R>)ManagedResourceOperations$class.toFuture(this, context);
    }
    
    @Override
    public <B> ExtractableManagedResource<B> map(final Function1<R, B> f) {
        return (ExtractableManagedResource<B>)ManagedResourceOperations$class.map(this, f);
    }
    
    @Override
    public <B> ManagedResource<B> flatMap(final Function1<R, ManagedResource<B>> f) {
        return (ManagedResource<B>)ManagedResourceOperations$class.flatMap(this, f);
    }
    
    @Override
    public void foreach(final Function1<R, BoxedUnit> f) {
        ManagedResourceOperations$class.foreach(this, f);
    }
    
    @Override
    public <B> ManagedResource<Tuple2<R, B>> and(final ManagedResource<B> that) {
        return (ManagedResource<Tuple2<R, B>>)ManagedResourceOperations$class.and(this, that);
    }
    
    public abstract R open();
    
    public abstract void unsafeClose(final R p0, final Option<Throwable> p1);
    
    public boolean isRethrown(final Throwable t) {
        return t instanceof ControlThrowable || t instanceof InterruptedException;
    }
    
    public boolean isFatal(final Throwable t) {
        return t instanceof VirtualMachineError;
    }
    
    private final Exception.Catch<Nothing$> catchingNonFatal() {
        return this.catchingNonFatal;
    }
    
    @Override
    public <B> ExtractedEither<List<Throwable>, B> acquireFor(final Function1<R, B> f) {
        final Object handle = this.open();
        final Either result = this.catchingNonFatal().either((Function0)new AbstractManagedResource$$anonfun.AbstractManagedResource$$anonfun$5(this, (Function1)f, handle));
        final Either close = this.catchingNonFatal().either((Function0)new AbstractManagedResource$$anonfun.AbstractManagedResource$$anonfun$1(this, handle, result));
        final Tuple2 tuple2 = new Tuple2((Object)result, (Object)close);
        if (tuple2 != null) {
            final Either either2 = (Either)tuple2._1();
            if (either2 instanceof Left) {
                final Throwable t1 = (Throwable)((Left)either2).a();
                if (this.isRethrown(t1)) {
                    throw t1;
                }
            }
        }
        Object o = null;
        Label_0544: {
            if (tuple2 != null) {
                final Either either3 = (Either)tuple2._1();
                final Either either4 = (Either)tuple2._2();
                if (either3 instanceof Left) {
                    final Throwable t2 = (Throwable)((Left)either3).a();
                    if (either4 instanceof Left) {
                        final Throwable t3 = (Throwable)((Left)either4).a();
                        o = package$.MODULE$.Left().apply((Object)Nil$.MODULE$.$colon$colon((Object)t3).$colon$colon((Object)t2));
                        break Label_0544;
                    }
                }
            }
            if (tuple2 != null) {
                final Either either5 = (Either)tuple2._1();
                if (either5 instanceof Left) {
                    final Throwable t4 = (Throwable)((Left)either5).a();
                    o = package$.MODULE$.Left().apply((Object)Nil$.MODULE$.$colon$colon((Object)t4));
                    break Label_0544;
                }
            }
            if (tuple2 != null) {
                final Either either6 = (Either)tuple2._1();
                final Either either7 = (Either)tuple2._2();
                if (either6 instanceof Right) {
                    final Object b = ((Right)either6).b();
                    if (b instanceof ExtractedEither) {
                        final Either either8 = ((ExtractedEither)b).either();
                        if (either8 instanceof Left) {
                            final Object ts = ((Left)either8).a();
                            if (either7 instanceof Left) {
                                final Throwable t5 = (Throwable)((Left)either7).a();
                                o = package$.MODULE$.Left().apply(((List)ts).$colon$plus((Object)t5, List$.MODULE$.canBuildFrom()));
                                break Label_0544;
                            }
                        }
                    }
                }
            }
            if (tuple2 != null) {
                final Either either9 = (Either)tuple2._2();
                if (either9 instanceof Left) {
                    final Throwable t6 = (Throwable)((Left)either9).a();
                    o = package$.MODULE$.Left().apply((Object)Nil$.MODULE$.$colon$colon((Object)t6));
                    break Label_0544;
                }
            }
            if (tuple2 != null) {
                final Either either10 = (Either)tuple2._1();
                if (either10 instanceof Right) {
                    final Object r = ((Right)either10).b();
                    o = package$.MODULE$.Right().apply(r);
                    break Label_0544;
                }
            }
            throw new MatchError((Object)tuple2);
        }
        final Either either = (Either)o;
        return new ExtractedEither<List<Throwable>, B>((scala.util.Either<List<Throwable>, B>)either);
    }
    
    public AbstractManagedResource() {
        ManagedResourceOperations$class.$init$(this);
        this.catchingNonFatal = (Exception.Catch<Nothing$>)new Exception.Catch(Exception$.MODULE$.mkThrowableCatcher((Function1)new AbstractManagedResource$$anonfun.AbstractManagedResource$$anonfun$2(this), (Function1)new AbstractManagedResource$$anonfun.AbstractManagedResource$$anonfun$3(this)), (Option)None$.MODULE$, (Function1)new AbstractManagedResource$$anonfun.AbstractManagedResource$$anonfun$4(this)).withDesc("<non-fatal>");
    }
}
