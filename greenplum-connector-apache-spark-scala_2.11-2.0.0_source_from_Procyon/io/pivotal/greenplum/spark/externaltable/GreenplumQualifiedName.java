// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.runtime.BoxedUnit;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005mb\u0001B\u0001\u0003\u00016\u0011ac\u0012:fK:\u0004H.^7Rk\u0006d\u0017NZ5fI:\u000bW.\u001a\u0006\u0003\u0007\u0011\tQ\"\u001a=uKJt\u0017\r\u001c;bE2,'BA\u0003\u0007\u0003\u0015\u0019\b/\u0019:l\u0015\t9\u0001\"A\u0005he\u0016,g\u000e\u001d7v[*\u0011\u0011BC\u0001\ba&4x\u000e^1m\u0015\u0005Y\u0011AA5p\u0007\u0001\u0019B\u0001\u0001\b\u0015/A\u0011qBE\u0007\u0002!)\t\u0011#A\u0003tG\u0006d\u0017-\u0003\u0002\u0014!\t1\u0011I\\=SK\u001a\u0004\"aD\u000b\n\u0005Y\u0001\"a\u0002)s_\u0012,8\r\u001e\t\u0003\u001faI!!\u0007\t\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011m\u0001!Q3A\u0005\u0002q\taa]2iK6\fW#A\u000f\u0011\u0005y\tcBA\b \u0013\t\u0001\u0003#\u0001\u0004Qe\u0016$WMZ\u0005\u0003E\r\u0012aa\u0015;sS:<'B\u0001\u0011\u0011\u0011!)\u0003A!E!\u0002\u0013i\u0012aB:dQ\u0016l\u0017\r\t\u0005\tO\u0001\u0011)\u001a!C\u00019\u0005!a.Y7f\u0011!I\u0003A!E!\u0002\u0013i\u0012!\u00028b[\u0016\u0004\u0003\"B\u0016\u0001\t\u0003a\u0013A\u0002\u001fj]&$h\bF\u0002._A\u0002\"A\f\u0001\u000e\u0003\tAQa\u0007\u0016A\u0002uAQa\n\u0016A\u0002uA\u0001B\r\u0001\t\u0006\u0004%\t\u0005H\u0001\ti>\u001cFO]5oO\"AA\u0007\u0001E\u0001B\u0003&Q$A\u0005u_N#(/\u001b8hA!9a\u0007AA\u0001\n\u00039\u0014\u0001B2paf$2!\f\u001d:\u0011\u001dYR\u0007%AA\u0002uAqaJ\u001b\u0011\u0002\u0003\u0007Q\u0004C\u0004<\u0001E\u0005I\u0011\u0001\u001f\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tQH\u000b\u0002\u001e}-\nq\b\u0005\u0002A\u000b6\t\u0011I\u0003\u0002C\u0007\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003\tB\t!\"\u00198o_R\fG/[8o\u0013\t1\u0015IA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq\u0001\u0013\u0001\u0012\u0002\u0013\u0005A(\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\t\u000f)\u0003\u0011\u0011!C!\u0017\u0006i\u0001O]8ek\u000e$\bK]3gSb,\u0012\u0001\u0014\t\u0003\u001bJk\u0011A\u0014\u0006\u0003\u001fB\u000bA\u0001\\1oO*\t\u0011+\u0001\u0003kCZ\f\u0017B\u0001\u0012O\u0011\u001d!\u0006!!A\u0005\u0002U\u000bA\u0002\u001d:pIV\u001cG/\u0011:jif,\u0012A\u0016\t\u0003\u001f]K!\u0001\u0017\t\u0003\u0007%sG\u000fC\u0004[\u0001\u0005\u0005I\u0011A.\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011Al\u0018\t\u0003\u001fuK!A\u0018\t\u0003\u0007\u0005s\u0017\u0010C\u0004a3\u0006\u0005\t\u0019\u0001,\u0002\u0007a$\u0013\u0007C\u0004c\u0001\u0005\u0005I\u0011I2\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012\u0001\u001a\t\u0004K\"dV\"\u00014\u000b\u0005\u001d\u0004\u0012AC2pY2,7\r^5p]&\u0011\u0011N\u001a\u0002\t\u0013R,'/\u0019;pe\"91\u000eAA\u0001\n\u0003a\u0017\u0001C2b]\u0016\u000bX/\u00197\u0015\u00055\u0004\bCA\bo\u0013\ty\u0007CA\u0004C_>dW-\u00198\t\u000f\u0001T\u0017\u0011!a\u00019\"9!\u000fAA\u0001\n\u0003\u001a\u0018\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003YCq!\u001e\u0001\u0002\u0002\u0013\u0005c/\u0001\u0004fcV\fGn\u001d\u000b\u0003[^Dq\u0001\u0019;\u0002\u0002\u0003\u0007AlB\u0004z\u0005\u0005\u0005\t\u0012\u0001>\u0002-\u001d\u0013X-\u001a8qYVl\u0017+^1mS\u001aLW\r\u001a(b[\u0016\u0004\"AL>\u0007\u000f\u0005\u0011\u0011\u0011!E\u0001yN\u001910`\f\u0011\ry\f\u0019!H\u000f.\u001b\u0005y(bAA\u0001!\u00059!/\u001e8uS6,\u0017bAA\u0003\u007f\n\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001a\t\r-ZH\u0011AA\u0005)\u0005Q\b\u0002\u0003\u001a|\u0003\u0003%)%!\u0004\u0015\u00031C\u0011\"!\u0005|\u0003\u0003%\t)a\u0005\u0002\u000b\u0005\u0004\b\u000f\\=\u0015\u000b5\n)\"a\u0006\t\rm\ty\u00011\u0001\u001e\u0011\u00199\u0013q\u0002a\u0001;!I\u00111D>\u0002\u0002\u0013\u0005\u0015QD\u0001\bk:\f\u0007\u000f\u001d7z)\u0011\ty\"a\u000b\u0011\u000b=\t\t#!\n\n\u0007\u0005\r\u0002C\u0001\u0004PaRLwN\u001c\t\u0006\u001f\u0005\u001dR$H\u0005\u0004\u0003S\u0001\"A\u0002+va2,'\u0007C\u0005\u0002.\u0005e\u0011\u0011!a\u0001[\u0005\u0019\u0001\u0010\n\u0019\t\u0013\u0005E20!A\u0005\n\u0005M\u0012a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!!\u000e\u0011\u00075\u000b9$C\u0002\u0002:9\u0013aa\u00142kK\u000e$\b")
public class GreenplumQualifiedName implements Product, Serializable
{
    private final String schema;
    private final String name;
    private String toString;
    private volatile boolean bitmap$0;
    
    public static Option<Tuple2<String, String>> unapply(final GreenplumQualifiedName x$0) {
        return GreenplumQualifiedName$.MODULE$.unapply(x$0);
    }
    
    public static GreenplumQualifiedName apply(final String schema, final String name) {
        return GreenplumQualifiedName$.MODULE$.apply(schema, name);
    }
    
    public static Function1<Tuple2<String, String>, GreenplumQualifiedName> tupled() {
        return (Function1<Tuple2<String, String>, GreenplumQualifiedName>)GreenplumQualifiedName$.MODULE$.tupled();
    }
    
    public static Function1<String, Function1<String, GreenplumQualifiedName>> curried() {
        return (Function1<String, Function1<String, GreenplumQualifiedName>>)GreenplumQualifiedName$.MODULE$.curried();
    }
    
    private String toString$lzycompute() {
        synchronized (this) {
            if (!this.bitmap$0) {
                this.toString = new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "", ".", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { SqlObjectNameUtils$.MODULE$.escape(this.schema()), SqlObjectNameUtils$.MODULE$.escape(this.name()) }));
                this.bitmap$0 = true;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.toString;
        }
    }
    
    public String schema() {
        return this.schema;
    }
    
    public String name() {
        return this.name;
    }
    
    @Override
    public String toString() {
        return this.bitmap$0 ? this.toString : this.toString$lzycompute();
    }
    
    public GreenplumQualifiedName copy(final String schema, final String name) {
        return new GreenplumQualifiedName(schema, name);
    }
    
    public String copy$default$1() {
        return this.schema();
    }
    
    public String copy$default$2() {
        return this.name();
    }
    
    public String productPrefix() {
        return "GreenplumQualifiedName";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        String s = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                s = this.name();
                break;
            }
            case 0: {
                s = this.schema();
                break;
            }
        }
        return s;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof GreenplumQualifiedName;
    }
    
    @Override
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof GreenplumQualifiedName) {
                final GreenplumQualifiedName greenplumQualifiedName = (GreenplumQualifiedName)x$1;
                final String schema = this.schema();
                final String schema2 = greenplumQualifiedName.schema();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (schema == null) {
                            if (schema2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!schema.equals(schema2)) {
                            break Label_0108;
                        }
                        final String name = this.name();
                        final String name2 = greenplumQualifiedName.name();
                        if (name == null) {
                            if (name2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!name.equals(name2)) {
                            break Label_0108;
                        }
                        if (greenplumQualifiedName.canEqual(this)) {
                            b = true;
                            break Label_0109;
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
    
    public GreenplumQualifiedName(final String schema, final String name) {
        this.schema = schema;
        this.name = name;
        Product$class.$init$((Product)this);
        if (schema == null) {
            throw new NullPointerException("schema passed to GreenplumQualifiedName was null");
        }
        if (schema.isEmpty()) {
            throw new IllegalArgumentException("schema passed to GreenplumQualifiedName was empty");
        }
        if (name == null) {
            throw new NullPointerException("name passed to GreenplumQualifiedName was null");
        }
        if (name.isEmpty()) {
            throw new IllegalArgumentException("name passed to GreenplumQualifiedName was empty");
        }
    }
}
