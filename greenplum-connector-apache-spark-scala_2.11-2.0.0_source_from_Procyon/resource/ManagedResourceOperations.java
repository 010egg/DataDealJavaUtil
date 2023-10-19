// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.Tuple2;
import scala.runtime.BoxedUnit;
import scala.concurrent.Future;
import scala.concurrent.ExecutionContext;
import scala.collection.Traversable;
import scala.collection.TraversableOnce;
import scala.Predef;
import scala.Function1;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001da!C\u0001\u0003!\u0003\r\t!BA\u0003\u0005ei\u0015M\\1hK\u0012\u0014Vm]8ve\u000e,w\n]3sCRLwN\\:\u000b\u0003\r\t\u0001B]3t_V\u00148-Z\u0002\u0001+\t11cE\u0002\u0001\u000f5\u0001\"\u0001C\u0006\u000e\u0003%Q\u0011AC\u0001\u0006g\u000e\fG.Y\u0005\u0003\u0019%\u0011a!\u00118z%\u00164\u0007c\u0001\b\u0010#5\t!!\u0003\u0002\u0011\u0005\tyQ*\u00198bO\u0016$'+Z:pkJ\u001cW\r\u0005\u0002\u0013'1\u0001AA\u0002\u000b\u0001\t\u000b\u0007QCA\u0001S#\t1\u0012\u0004\u0005\u0002\t/%\u0011\u0001$\u0003\u0002\b\u001d>$\b.\u001b8h!\tA!$\u0003\u0002\u001c\u0013\t\u0019\u0011I\\=\t\u000bu\u0001A\u0011\u0001\u0010\u0002\r\u0011Jg.\u001b;%)\u0005y\u0002C\u0001\u0005!\u0013\t\t\u0013B\u0001\u0003V]&$\b\"B\u0012\u0001\t\u0003\"\u0013!D1dcVL'/Z!oI\u001e+G/\u0006\u0002&OQ\u0011a%\u000b\t\u0003%\u001d\"Q\u0001\u000b\u0012C\u0002U\u0011\u0011A\u0011\u0005\u0006U\t\u0002\raK\u0001\u0002MB!\u0001\u0002L\t'\u0013\ti\u0013BA\u0005Gk:\u001cG/[8oc!)q\u0006\u0001C!a\u0005)\u0011\r\u001d9msV\u0011\u0011g\r\u000b\u0003eQ\u0002\"AE\u001a\u0005\u000b!r#\u0019A\u000b\t\u000b)r\u0003\u0019A\u001b\u0011\t!a\u0013C\r\u0005\u0006o\u0001!\t\u0005O\u0001\u000ei>$&/\u0019<feN\f'\r\\3\u0016\u0005e\nEC\u0001\u001eC!\rYd\bQ\u0007\u0002y)\u0011Q(C\u0001\u000bG>dG.Z2uS>t\u0017BA =\u0005-!&/\u0019<feN\f'\r\\3\u0011\u0005I\tE!\u0002\u00157\u0005\u0004)\u0002\"B\"7\u0001\b!\u0015AA3w!\u0011)\u0005*E&\u000f\u0005!1\u0015BA$\n\u0003\u0019\u0001&/\u001a3fM&\u0011\u0011J\u0013\u0002\u0011I1,7o\u001d\u0013d_2|g\u000e\n7fgNT!aR\u0005\u0011\u0007mb\u0005)\u0003\u0002Ny\tyAK]1wKJ\u001c\u0018M\u00197f\u001f:\u001cW\rC\u0003P\u0001\u0011\u0005\u0003+\u0001\u0005u_\u001a+H/\u001e:f)\t\tv\u000bE\u0002S+Fi\u0011a\u0015\u0006\u0003)&\t!bY8oGV\u0014(/\u001a8u\u0013\t16K\u0001\u0004GkR,(/\u001a\u0005\u00061:\u0003\u001d!W\u0001\bG>tG/\u001a=u!\t\u0011&,\u0003\u0002\\'\n\u0001R\t_3dkRLwN\\\"p]R,\u0007\u0010\u001e\u0005\u0006;\u0002!\tEX\u0001\u0004[\u0006\u0004XCA0e)\t\u0001W\rE\u0002\u000fC\u000eL!A\u0019\u0002\u00035\u0015CHO]1di\u0006\u0014G.Z'b]\u0006<W\r\u001a*fg>,(oY3\u0011\u0005I!G!\u0002\u0015]\u0005\u0004)\u0002\"\u0002\u0016]\u0001\u00041\u0007\u0003\u0002\u0005-#\rDQ\u0001\u001b\u0001\u0005B%\fqA\u001a7bi6\u000b\u0007/\u0006\u0002k[R\u00111N\u001c\t\u0004\u001d=a\u0007C\u0001\nn\t\u0015AsM1\u0001\u0016\u0011\u0015Qs\r1\u0001p!\u0011AA&E6\t\u000bE\u0004A\u0011\t:\u0002\u000f\u0019|'/Z1dQR\u0011qd\u001d\u0005\u0006UA\u0004\r\u0001\u001e\t\u0005\u00111\nr\u0004C\u0003w\u0001\u0011\u0005s/A\u0002b]\u0012,\"\u0001\u001f@\u0015\u0005e|\bc\u0001\b\u0010uB!\u0001b_\t~\u0013\ta\u0018B\u0001\u0004UkBdWM\r\t\u0003%y$Q\u0001K;C\u0002UAq!!\u0001v\u0001\u0004\t\u0019!\u0001\u0003uQ\u0006$\bc\u0001\b\u0010{B\u0019a\u0002A\t")
public interface ManagedResourceOperations<R> extends ManagedResource<R>
{
     <B> B acquireAndGet(final Function1<R, B> p0);
    
     <B> B apply(final Function1<R, B> p0);
    
     <B> Traversable<B> toTraversable(final Predef.$less$colon$less<R, TraversableOnce<B>> p0);
    
    Future<R> toFuture(final ExecutionContext p0);
    
     <B> ExtractableManagedResource<B> map(final Function1<R, B> p0);
    
     <B> ManagedResource<B> flatMap(final Function1<R, ManagedResource<B>> p0);
    
    void foreach(final Function1<R, BoxedUnit> p0);
    
     <B> ManagedResource<Tuple2<R, B>> and(final ManagedResource<B> p0);
}
