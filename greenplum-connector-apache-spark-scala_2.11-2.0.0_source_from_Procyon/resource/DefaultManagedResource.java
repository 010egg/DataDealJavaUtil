// 
// Decompiled by Procyon v0.5.36
// 

package resource;

import scala.Predef$;
import scala.collection.mutable.StringBuilder;
import scala.MatchError;
import scala.Some;
import scala.runtime.BoxedUnit;
import scala.None$;
import scala.Option;
import scala.reflect.OptManifest;
import scala.Function0;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001E4Q!\u0001\u0002\u0003\u000bA\u0012a\u0003R3gCVdG/T1oC\u001e,GMU3t_V\u00148-\u001a\u0006\u0002\u0007\u0005A!/Z:pkJ\u001cWm\u0001\u0001\u0016\u0005\u0019i1C\u0001\u0001\b!\rA\u0011bC\u0007\u0002\u0005%\u0011!B\u0001\u0002\u0018\u0003\n\u001cHO]1di6\u000bg.Y4fIJ+7o\\;sG\u0016\u0004\"\u0001D\u0007\r\u0001\u0011)a\u0002\u0001b\u0001\u001f\t\t!+\u0005\u0002\u0011-A\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9aj\u001c;iS:<\u0007CA\t\u0018\u0013\tA\"CA\u0002B]fD\u0001B\u0007\u0001\u0003\u0002\u0013\u0006IaG\u0001\u0002eB\u0019\u0011\u0003H\u0006\n\u0005u\u0011\"\u0001\u0003\u001fcs:\fW.\u001a \t\u0011}\u0001!1!Q\u0001\f\u0001\n!\"\u001a<jI\u0016t7-\u001a\u00132!\rA\u0011eC\u0005\u0003E\t\u0011\u0001BU3t_V\u00148-\u001a\u0005\tI\u0001\u0011\u0019\u0011)A\u0006K\u0005QQM^5eK:\u001cW\r\n\u001a\u0011\u0007\u0019J3B\u0004\u0002\u0012O%\u0011\u0001FE\u0001\u0007!J,G-\u001a4\n\u0005)Z#aC(qi6\u000bg.\u001b4fgRT!\u0001\u000b\n\t\u000b5\u0002A\u0011\u0001\u0018\u0002\rqJg.\u001b;?)\ty3\u0007F\u00021cI\u00022\u0001\u0003\u0001\f\u0011\u0015yB\u0006q\u0001!\u0011\u0015!C\u0006q\u0001&\u0011\u0019QB\u0006\"a\u00017!9Q\u0007\u0001b\u0001\n#1\u0014!\u0003;za\u0016$&/Y5u+\u0005\u0001\u0003B\u0002\u001d\u0001A\u0003%\u0001%\u0001\u0006usB,GK]1ji\u0002BQA\u000f\u0001\u0005Rm\nAa\u001c9f]V\t1\u0002C\u0003>\u0001\u0011Ec(A\u0006v]N\fg-Z\"m_N,GcA C\u0007B\u0011\u0011\u0003Q\u0005\u0003\u0003J\u0011A!\u00168ji\")!\u0004\u0010a\u0001\u0017!)A\t\u0010a\u0001\u000b\u0006)QM\u001d:peB\u0019\u0011C\u0012%\n\u0005\u001d\u0013\"AB(qi&|g\u000e\u0005\u0002J#:\u0011!j\u0014\b\u0003\u0017:k\u0011\u0001\u0014\u0006\u0003\u001b\u0012\ta\u0001\u0010:p_Rt\u0014\"A\n\n\u0005A\u0013\u0012a\u00029bG.\fw-Z\u0005\u0003%N\u0013\u0011\u0002\u00165s_^\f'\r\\3\u000b\u0005A\u0013\u0002\"B+\u0001\t#2\u0016aB5t\r\u0006$\u0018\r\u001c\u000b\u0003/j\u0003\"!\u0005-\n\u0005e\u0013\"a\u0002\"p_2,\u0017M\u001c\u0005\u00067R\u0003\r\u0001S\u0001\u0002i\")Q\f\u0001C)=\u0006Q\u0011n\u001d*fi\"\u0014xn\u001e8\u0015\u0005]{\u0006\"B.]\u0001\u0004A\u0005\"B1\u0001\t\u0003\u0012\u0017\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003\r\u0004\"!\u00053\n\u0005\u0015\u0014\"aA%oi\")q\r\u0001C!Q\u0006AAo\\*ue&tw\rF\u0001j!\tQw.D\u0001l\u0015\taW.\u0001\u0003mC:<'\"\u00018\u0002\t)\fg/Y\u0005\u0003a.\u0014aa\u0015;sS:<\u0007")
public final class DefaultManagedResource<R> extends AbstractManagedResource<R>
{
    private final Function0<R> r;
    private final OptManifest<R> evidence$2;
    private final Resource<R> typeTrait;
    
    public Resource<R> typeTrait() {
        return this.typeTrait;
    }
    
    @Override
    public R open() {
        final Object resource = this.r.apply();
        this.typeTrait().open((R)resource);
        return (R)resource;
    }
    
    @Override
    public void unsafeClose(final R r, final Option<Throwable> error) {
        if (None$.MODULE$.equals(error)) {
            this.typeTrait().close(r);
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        else {
            if (!(error instanceof Some)) {
                throw new MatchError((Object)error);
            }
            final Throwable t = (Throwable)((Some)error).x();
            this.typeTrait().closeAfterException(r, t);
            final BoxedUnit unit2 = BoxedUnit.UNIT;
        }
    }
    
    @Override
    public boolean isFatal(final Throwable t) {
        return this.typeTrait().isFatalException(t);
    }
    
    @Override
    public boolean isRethrown(final Throwable t) {
        return this.typeTrait().isRethrownException(t);
    }
    
    @Override
    public int hashCode() {
        return (this.typeTrait().hashCode() << 7) + super.hashCode() + 13;
    }
    
    @Override
    public String toString() {
        return new StringBuilder().append((Object)"Default[").append(Predef$.MODULE$.implicitly((Object)this.evidence$2)).append((Object)" : ").append((Object)this.typeTrait()).append((Object)"](...)").toString();
    }
    
    public DefaultManagedResource(final Function0<R> r, final Resource<R> evidence$1, final OptManifest<R> evidence$2) {
        this.r = r;
        this.evidence$2 = evidence$2;
        this.typeTrait = (Resource<R>)Predef$.MODULE$.implicitly((Object)evidence$1);
    }
}
