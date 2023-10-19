// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import java.io.InputStream;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ma\u0001B\u0001\u0003\u0001\u001e\u00111b\u0015;sK\u0006l\u0017J\u001c9vi*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0005\r%A\u0011\u0011BC\u0007\u0002\u0005%\u00111B\u0001\u0002\n\u0015N|g.\u00138qkR\u0004\"!\u0004\t\u000e\u00039Q\u0011aD\u0001\u0006g\u000e\fG.Y\u0005\u0003#9\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u000e'%\u0011AC\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t-\u0001\u0011)\u001a!C\u0001/\u000511\u000f\u001e:fC6,\u0012\u0001\u0007\t\u00033yi\u0011A\u0007\u0006\u00037q\t!![8\u000b\u0003u\tAA[1wC&\u0011qD\u0007\u0002\f\u0013:\u0004X\u000f^*ue\u0016\fW\u000e\u0003\u0005\"\u0001\tE\t\u0015!\u0003\u0019\u0003\u001d\u0019HO]3b[\u0002BQa\t\u0001\u0005\u0002\u0011\na\u0001P5oSRtDCA\u0013'!\tI\u0001\u0001C\u0003\u0017E\u0001\u0007\u0001\u0004C\u0004)\u0001\u0005\u0005I\u0011A\u0015\u0002\t\r|\u0007/\u001f\u000b\u0003K)BqAF\u0014\u0011\u0002\u0003\u0007\u0001\u0004C\u0004-\u0001E\u0005I\u0011A\u0017\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\taF\u000b\u0002\u0019_-\n\u0001\u0007\u0005\u00022m5\t!G\u0003\u00024i\u0005IQO\\2iK\u000e\\W\r\u001a\u0006\u0003k9\t!\"\u00198o_R\fG/[8o\u0013\t9$GA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq!\u000f\u0001\u0002\u0002\u0013\u0005#(A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002wA\u0011AhP\u0007\u0002{)\u0011a\bH\u0001\u0005Y\u0006tw-\u0003\u0002A{\t11\u000b\u001e:j]\u001eDqA\u0011\u0001\u0002\u0002\u0013\u00051)\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001E!\tiQ)\u0003\u0002G\u001d\t\u0019\u0011J\u001c;\t\u000f!\u0003\u0011\u0011!C\u0001\u0013\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u0001&N!\ti1*\u0003\u0002M\u001d\t\u0019\u0011I\\=\t\u000f9;\u0015\u0011!a\u0001\t\u0006\u0019\u0001\u0010J\u0019\t\u000fA\u0003\u0011\u0011!C!#\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/F\u0001S!\r\u0019fKS\u0007\u0002)*\u0011QKD\u0001\u000bG>dG.Z2uS>t\u0017BA,U\u0005!IE/\u001a:bi>\u0014\bbB-\u0001\u0003\u0003%\tAW\u0001\tG\u0006tW)];bYR\u00111L\u0018\t\u0003\u001bqK!!\u0018\b\u0003\u000f\t{w\u000e\\3b]\"9a\nWA\u0001\u0002\u0004Q\u0005b\u00021\u0001\u0003\u0003%\t%Y\u0001\tQ\u0006\u001c\bnQ8eKR\tA\tC\u0004d\u0001\u0005\u0005I\u0011\t3\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012a\u000f\u0005\bM\u0002\t\t\u0011\"\u0011h\u0003\u0019)\u0017/^1mgR\u00111\f\u001b\u0005\b\u001d\u0016\f\t\u00111\u0001K\u000f\u001dQ'!!A\t\u0002-\f1b\u0015;sK\u0006l\u0017J\u001c9viB\u0011\u0011\u0002\u001c\u0004\b\u0003\t\t\t\u0011#\u0001n'\ragN\u0005\t\u0005_JDR%D\u0001q\u0015\t\th\"A\u0004sk:$\u0018.\\3\n\u0005M\u0004(!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8oc!)1\u0005\u001cC\u0001kR\t1\u000eC\u0004dY\u0006\u0005IQ\t3\t\u000fad\u0017\u0011!CAs\u0006)\u0011\r\u001d9msR\u0011QE\u001f\u0005\u0006-]\u0004\r\u0001\u0007\u0005\by2\f\t\u0011\"!~\u0003\u001d)h.\u00199qYf$2A`A\u0002!\riq\u0010G\u0005\u0004\u0003\u0003q!AB(qi&|g\u000e\u0003\u0005\u0002\u0006m\f\t\u00111\u0001&\u0003\rAH\u0005\r\u0005\n\u0003\u0013a\u0017\u0011!C\u0005\u0003\u0017\t1B]3bIJ+7o\u001c7wKR\u0011\u0011Q\u0002\t\u0004y\u0005=\u0011bAA\t{\t1qJ\u00196fGR\u0004")
public class StreamInput extends JsonInput
{
    private final InputStream stream;
    
    public static Option<InputStream> unapply(final StreamInput x$0) {
        return StreamInput$.MODULE$.unapply(x$0);
    }
    
    public static StreamInput apply(final InputStream stream) {
        return StreamInput$.MODULE$.apply(stream);
    }
    
    public static <A> Function1<InputStream, A> andThen(final Function1<StreamInput, A> function1) {
        return (Function1<InputStream, A>)StreamInput$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, StreamInput> compose(final Function1<A, InputStream> function1) {
        return (Function1<A, StreamInput>)StreamInput$.MODULE$.compose((Function1)function1);
    }
    
    public InputStream stream() {
        return this.stream;
    }
    
    public StreamInput copy(final InputStream stream) {
        return new StreamInput(stream);
    }
    
    public InputStream copy$default$1() {
        return this.stream();
    }
    
    @Override
    public String productPrefix() {
        return "StreamInput";
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
                return this.stream();
            }
        }
    }
    
    @Override
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof StreamInput;
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
            if (x$1 instanceof StreamInput) {
                final StreamInput streamInput = (StreamInput)x$1;
                final InputStream stream = this.stream();
                final InputStream stream2 = streamInput.stream();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (stream == null) {
                            if (stream2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!stream.equals(stream2)) {
                            break Label_0076;
                        }
                        if (streamInput.canEqual(this)) {
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
    
    public StreamInput(final InputStream stream) {
        this.stream = stream;
    }
}
