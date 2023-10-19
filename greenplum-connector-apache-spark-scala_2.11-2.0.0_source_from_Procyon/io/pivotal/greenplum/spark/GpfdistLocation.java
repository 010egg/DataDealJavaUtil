// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import java.lang.constant.Constable;
import scala.runtime.BoxesRunTime;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Function0;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import org.slf4j.Logger;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001dc\u0001B\u0001\u0003\u0001.\u0011qb\u00129gI&\u001cH\u000fT8dCRLwN\u001c\u0006\u0003\u0007\u0011\tQa\u001d9be.T!!\u0002\u0004\u0002\u0013\u001d\u0014X-\u001a8qYVl'BA\u0004\t\u0003\u001d\u0001\u0018N^8uC2T\u0011!C\u0001\u0003S>\u001c\u0001aE\u0003\u0001\u0019I1\u0012\u0004\u0005\u0002\u000e!5\taBC\u0001\u0010\u0003\u0015\u00198-\u00197b\u0013\t\tbB\u0001\u0004B]f\u0014VM\u001a\t\u0003'Qi\u0011AA\u0005\u0003+\t\u0011q\u0001T8hO&tw\r\u0005\u0002\u000e/%\u0011\u0001D\u0004\u0002\b!J|G-^2u!\ti!$\u0003\u0002\u001c\u001d\ta1+\u001a:jC2L'0\u00192mK\"AQ\u0004\u0001BK\u0002\u0013\u0005a$\u0001\u0004tKJ4XM]\u000b\u0002?A\u0011\u0001e\t\b\u0003\u001b\u0005J!A\t\b\u0002\rA\u0013X\rZ3g\u0013\t!SE\u0001\u0004TiJLgn\u001a\u0006\u0003E9A\u0001b\n\u0001\u0003\u0012\u0003\u0006IaH\u0001\bg\u0016\u0014h/\u001a:!\u0011!I\u0003A!f\u0001\n\u0003Q\u0013\u0001\u00029peR,\u0012a\u000b\t\u0003\u001b1J!!\f\b\u0003\u0007%sG\u000f\u0003\u00050\u0001\tE\t\u0015!\u0003,\u0003\u0015\u0001xN\u001d;!\u0011\u0015\t\u0004\u0001\"\u00013\u0003\u0019a\u0014N\\5u}Q\u00191\u0007N\u001b\u0011\u0005M\u0001\u0001\"B\u000f1\u0001\u0004y\u0002\"B\u00151\u0001\u0004Y\u0003\"B\u001c\u0001\t\u0003A\u0014\u0001C4f]\u0016\u0014\u0018\r^3\u0015\u0005}I\u0004\"\u0002\u001e7\u0001\u0004y\u0012\u0001\u00029bi\"Dq\u0001\u0010\u0001\u0002\u0002\u0013\u0005Q(\u0001\u0003d_BLHcA\u001a?\u007f!9Qd\u000fI\u0001\u0002\u0004y\u0002bB\u0015<!\u0003\u0005\ra\u000b\u0005\b\u0003\u0002\t\n\u0011\"\u0001C\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012a\u0011\u0016\u0003?\u0011[\u0013!\u0012\t\u0003\r.k\u0011a\u0012\u0006\u0003\u0011&\u000b\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005)s\u0011AC1o]>$\u0018\r^5p]&\u0011Aj\u0012\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007b\u0002(\u0001#\u0003%\taT\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00133+\u0005\u0001&FA\u0016E\u0011\u001d\u0011\u0006!!A\u0005BM\u000bQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#\u0001+\u0011\u0005USV\"\u0001,\u000b\u0005]C\u0016\u0001\u00027b]\u001eT\u0011!W\u0001\u0005U\u00064\u0018-\u0003\u0002%-\"9A\fAA\u0001\n\u0003Q\u0013\u0001\u00049s_\u0012,8\r^!sSRL\bb\u00020\u0001\u0003\u0003%\taX\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\t\u00017\r\u0005\u0002\u000eC&\u0011!M\u0004\u0002\u0004\u0003:L\bb\u00023^\u0003\u0003\u0005\raK\u0001\u0004q\u0012\n\u0004b\u00024\u0001\u0003\u0003%\teZ\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\t\u0001\u000eE\u0002jY\u0002l\u0011A\u001b\u0006\u0003W:\t!bY8mY\u0016\u001cG/[8o\u0013\ti'N\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011\u001dy\u0007!!A\u0005\u0002A\f\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0003cR\u0004\"!\u0004:\n\u0005Mt!a\u0002\"p_2,\u0017M\u001c\u0005\bI:\f\t\u00111\u0001a\u0011\u001d1\b!!A\u0005B]\f\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u0002W!9\u0011\u0010AA\u0001\n\u0003R\u0018\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003QCq\u0001 \u0001\u0002\u0002\u0013\u0005S0\u0001\u0004fcV\fGn\u001d\u000b\u0003czDq\u0001Z>\u0002\u0002\u0003\u0007\u0001mB\u0005\u0002\u0002\t\t\t\u0011#\u0001\u0002\u0004\u0005yq\t\u001d4eSN$Hj\\2bi&|g\u000eE\u0002\u0014\u0003\u000b1\u0001\"\u0001\u0002\u0002\u0002#\u0005\u0011qA\n\u0006\u0003\u000b\tI!\u0007\t\b\u0003\u0017\t\tbH\u00164\u001b\t\tiAC\u0002\u0002\u00109\tqA];oi&lW-\u0003\u0003\u0002\u0014\u00055!!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oe!9\u0011'!\u0002\u0005\u0002\u0005]ACAA\u0002\u0011!I\u0018QAA\u0001\n\u000bR\bBCA\u000f\u0003\u000b\t\t\u0011\"!\u0002 \u0005)\u0011\r\u001d9msR)1'!\t\u0002$!1Q$a\u0007A\u0002}Aa!KA\u000e\u0001\u0004Y\u0003BCA\u0014\u0003\u000b\t\t\u0011\"!\u0002*\u00059QO\\1qa2LH\u0003BA\u0016\u0003o\u0001R!DA\u0017\u0003cI1!a\f\u000f\u0005\u0019y\u0005\u000f^5p]B)Q\"a\r W%\u0019\u0011Q\u0007\b\u0003\rQ+\b\u000f\\33\u0011%\tI$!\n\u0002\u0002\u0003\u00071'A\u0002yIAB!\"!\u0010\u0002\u0006\u0005\u0005I\u0011BA \u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u0005\u0003cA+\u0002D%\u0019\u0011Q\t,\u0003\r=\u0013'.Z2u\u0001")
public class GpfdistLocation implements Logging, Product, Serializable
{
    private final String server;
    private final int port;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    public static Option<Tuple2<String, Object>> unapply(final GpfdistLocation x$0) {
        return GpfdistLocation$.MODULE$.unapply(x$0);
    }
    
    public static GpfdistLocation apply(final String server, final int port) {
        return GpfdistLocation$.MODULE$.apply(server, port);
    }
    
    public static Function1<Tuple2<String, Object>, GpfdistLocation> tupled() {
        return (Function1<Tuple2<String, Object>, GpfdistLocation>)GpfdistLocation$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<Object, GpfdistLocation>> curried() {
        return (Function1<String, Function1<Object, GpfdistLocation>>)GpfdistLocation$.MODULE$.curried();
    }
    
    @Override
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @Override
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    @Override
    public Logger log() {
        return Logging$class.log(this);
    }
    
    @Override
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    @Override
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    public String server() {
        return this.server;
    }
    
    public int port() {
        return this.port;
    }
    
    public String generate(final String path) {
        final String uriString = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "gpfdist://", ":", "/", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { this.server(), BoxesRunTime.boxToInteger(this.port()), path }));
        if (this.log().isDebugEnabled()) {
            this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Creating URI string at ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { uriString })));
        }
        return uriString;
    }
    
    public GpfdistLocation copy(final String server, final int port) {
        return new GpfdistLocation(server, port);
    }
    
    public String copy$default$1() {
        return this.server();
    }
    
    public int copy$default$2() {
        return this.port();
    }
    
    public String productPrefix() {
        return "GpfdistLocation";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        Constable constable = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                constable = BoxesRunTime.boxToInteger(this.port());
                break;
            }
            case 0: {
                constable = this.server();
                break;
            }
        }
        return constable;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof GpfdistLocation;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.server())), this.port()), 2);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof GpfdistLocation) {
                final GpfdistLocation gpfdistLocation = (GpfdistLocation)x$1;
                final String server = this.server();
                final String server2 = gpfdistLocation.server();
                boolean b = false;
                Label_0089: {
                    Label_0088: {
                        if (server == null) {
                            if (server2 != null) {
                                break Label_0088;
                            }
                        }
                        else if (!server.equals(server2)) {
                            break Label_0088;
                        }
                        if (this.port() == gpfdistLocation.port() && gpfdistLocation.canEqual(this)) {
                            b = true;
                            break Label_0089;
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
    
    public GpfdistLocation(final String server, final int port) {
        this.server = server;
        this.port = port;
        Logging$class.$init$(this);
        Product$class.$init$((Product)this);
    }
}
