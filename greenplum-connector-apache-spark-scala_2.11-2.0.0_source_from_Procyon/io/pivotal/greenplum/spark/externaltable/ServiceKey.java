// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005-b\u0001B\u0001\u0003\t6\u0011!bU3sm&\u001cWmS3z\u0015\t\u0019A!A\u0007fqR,'O\\1mi\u0006\u0014G.\u001a\u0006\u0003\u000b\u0019\tQa\u001d9be.T!a\u0002\u0005\u0002\u0013\u001d\u0014X-\u001a8qYVl'BA\u0005\u000b\u0003\u001d\u0001\u0018N^8uC2T\u0011aC\u0001\u0003S>\u001c\u0001a\u0005\u0003\u0001\u001dQ9\u0002CA\b\u0013\u001b\u0005\u0001\"\"A\t\u0002\u000bM\u001c\u0017\r\\1\n\u0005M\u0001\"AB!osJ+g\r\u0005\u0002\u0010+%\u0011a\u0003\u0005\u0002\b!J|G-^2u!\ty\u0001$\u0003\u0002\u001a!\ta1+\u001a:jC2L'0\u00192mK\"A1\u0004\u0001BK\u0002\u0013\u0005A$\u0001\u0003q_J$X#A\u000f\u0011\u0007y1\u0013F\u0004\u0002 I9\u0011\u0001eI\u0007\u0002C)\u0011!\u0005D\u0001\u0007yI|w\u000e\u001e \n\u0003EI!!\n\t\u0002\u000fA\f7m[1hK&\u0011q\u0005\u000b\u0002\u0005\u0019&\u001cHO\u0003\u0002&!A\u0011qBK\u0005\u0003WA\u00111!\u00138u\u0011!i\u0003A!E!\u0002\u0013i\u0012!\u00029peR\u0004\u0003\"B\u0018\u0001\t\u0003\u0001\u0014A\u0002\u001fj]&$h\b\u0006\u00022gA\u0011!\u0007A\u0007\u0002\u0005!)1D\fa\u0001;!9Q\u0007AA\u0001\n\u00031\u0014\u0001B2paf$\"!M\u001c\t\u000fm!\u0004\u0013!a\u0001;!9\u0011\bAI\u0001\n\u0003Q\u0014AD2paf$C-\u001a4bk2$H%M\u000b\u0002w)\u0012Q\u0004P\u0016\u0002{A\u0011ahQ\u0007\u0002\u007f)\u0011\u0001)Q\u0001\nk:\u001c\u0007.Z2lK\u0012T!A\u0011\t\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002E\u007f\t\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000f\u0019\u0003\u0011\u0011!C!\u000f\u0006i\u0001O]8ek\u000e$\bK]3gSb,\u0012\u0001\u0013\t\u0003\u0013:k\u0011A\u0013\u0006\u0003\u00172\u000bA\u0001\\1oO*\tQ*\u0001\u0003kCZ\f\u0017BA(K\u0005\u0019\u0019FO]5oO\"9\u0011\u000bAA\u0001\n\u0003\u0011\u0016\u0001\u00049s_\u0012,8\r^!sSRLX#A\u0015\t\u000fQ\u0003\u0011\u0011!C\u0001+\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u0001,Z!\tyq+\u0003\u0002Y!\t\u0019\u0011I\\=\t\u000fi\u001b\u0016\u0011!a\u0001S\u0005\u0019\u0001\u0010J\u0019\t\u000fq\u0003\u0011\u0011!C!;\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/F\u0001_!\ry&MV\u0007\u0002A*\u0011\u0011\rE\u0001\u000bG>dG.Z2uS>t\u0017BA2a\u0005!IE/\u001a:bi>\u0014\bbB3\u0001\u0003\u0003%\tAZ\u0001\tG\u0006tW)];bYR\u0011qM\u001b\t\u0003\u001f!L!!\u001b\t\u0003\u000f\t{w\u000e\\3b]\"9!\fZA\u0001\u0002\u00041\u0006b\u00027\u0001\u0003\u0003%\t%\\\u0001\tQ\u0006\u001c\bnQ8eKR\t\u0011\u0006C\u0004p\u0001\u0005\u0005I\u0011\t9\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012\u0001\u0013\u0005\be\u0002\t\t\u0011\"\u0011t\u0003\u0019)\u0017/^1mgR\u0011q\r\u001e\u0005\b5F\f\t\u00111\u0001W\u000f\u001d1(!!A\t\n]\f!bU3sm&\u001cWmS3z!\t\u0011\u0004PB\u0004\u0002\u0005\u0005\u0005\t\u0012B=\u0014\u0007aTx\u0003\u0005\u0003|}v\tT\"\u0001?\u000b\u0005u\u0004\u0012a\u0002:v]RLW.Z\u0005\u0003\u007fr\u0014\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c82\u0011\u0019y\u0003\u0010\"\u0001\u0002\u0004Q\tq\u000fC\u0004pq\u0006\u0005IQ\t9\t\u0013\u0005%\u00010!A\u0005\u0002\u0006-\u0011!B1qa2LHcA\u0019\u0002\u000e!11$a\u0002A\u0002uA\u0011\"!\u0005y\u0003\u0003%\t)a\u0005\u0002\u000fUt\u0017\r\u001d9msR!\u0011QCA\u000e!\u0011y\u0011qC\u000f\n\u0007\u0005e\u0001C\u0001\u0004PaRLwN\u001c\u0005\n\u0003;\ty!!AA\u0002E\n1\u0001\u001f\u00131\u0011%\t\t\u0003_A\u0001\n\u0013\t\u0019#A\u0006sK\u0006$'+Z:pYZ,GCAA\u0013!\rI\u0015qE\u0005\u0004\u0003SQ%AB(cU\u0016\u001cG\u000f")
public class ServiceKey implements Product, Serializable
{
    private final List<Object> port;
    
    public static Option<List<Object>> unapply(final ServiceKey x$0) {
        return ServiceKey$.MODULE$.unapply(x$0);
    }
    
    public static ServiceKey apply(final List<Object> port) {
        return ServiceKey$.MODULE$.apply(port);
    }
    
    public static <A> Function1<List<Object>, A> andThen(final Function1<ServiceKey, A> function1) {
        return (Function1<List<Object>, A>)ServiceKey$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ServiceKey> compose(final Function1<A, List<Object>> function1) {
        return (Function1<A, ServiceKey>)ServiceKey$.MODULE$.compose((Function1)function1);
    }
    
    public List<Object> port() {
        return this.port;
    }
    
    public ServiceKey copy(final List<Object> port) {
        return new ServiceKey(port);
    }
    
    public List<Object> copy$default$1() {
        return this.port();
    }
    
    public String productPrefix() {
        return "ServiceKey";
    }
    
    public int productArity() {
        return 1;
    }
    
    public Object productElement(final int x$1) {
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 0: {
                return this.port();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ServiceKey;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ServiceKey) {
                final ServiceKey serviceKey = (ServiceKey)x$1;
                final List<Object> port = this.port();
                final List<Object> port2 = serviceKey.port();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (port == null) {
                            if (port2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!port.equals(port2)) {
                            break Label_0076;
                        }
                        if (serviceKey.canEqual(this)) {
                            b = true;
                            break Label_0077;
                        }
                    }
                    b = false;
                }
                if (b) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public ServiceKey(final List<Object> port) {
        this.port = port;
        Product$class.$init$((Product)this);
    }
}
