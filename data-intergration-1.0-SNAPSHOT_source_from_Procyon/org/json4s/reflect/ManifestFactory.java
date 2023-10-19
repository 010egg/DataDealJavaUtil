// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.reflect;

import java.lang.reflect.Type;
import scala.collection.Seq;
import scala.reflect.Manifest;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001a<Q!\u0001\u0002\t\u0002%\tq\"T1oS\u001a,7\u000f\u001e$bGR|'/\u001f\u0006\u0003\u0007\u0011\tqA]3gY\u0016\u001cGO\u0003\u0002\u0006\r\u00051!n]8oiMT\u0011aB\u0001\u0004_J<7\u0001\u0001\t\u0003\u0015-i\u0011A\u0001\u0004\u0006\u0019\tA\t!\u0004\u0002\u0010\u001b\u0006t\u0017NZ3ti\u001a\u000b7\r^8ssN\u00111B\u0004\t\u0003\u001fIi\u0011\u0001\u0005\u0006\u0002#\u0005)1oY1mC&\u00111\u0003\u0005\u0002\u0007\u0003:L(+\u001a4\t\u000bUYA\u0011\u0001\f\u0002\rqJg.\u001b;?)\u0005I\u0001\"\u0002\r\f\t\u0003I\u0012AC7b]&4Wm\u001d;PMR\u0011!d\u000b\u0019\u00037\t\u00022\u0001\b\u0010!\u001b\u0005i\"BA\u0002\u0011\u0013\tyRD\u0001\u0005NC:Lg-Z:u!\t\t#\u0005\u0004\u0001\u0005\u0013\r:\u0012\u0011!A\u0001\u0006\u0003!#aA0%cE\u0011Q\u0005\u000b\t\u0003\u001f\u0019J!a\n\t\u0003\u000f9{G\u000f[5oOB\u0011q\"K\u0005\u0003UA\u00111!\u00118z\u0011\u0015as\u00031\u0001.\u0003\u0005!\bC\u0001\u00185\u001b\u0005y#BA\u00021\u0015\t\t$'\u0001\u0003mC:<'\"A\u001a\u0002\t)\fg/Y\u0005\u0003k=\u0012A\u0001V=qK\")\u0001d\u0003C\u0001oQ\u0019\u0001(\u0010&1\u0005eZ\u0004c\u0001\u000f\u001fuA\u0011\u0011e\u000f\u0003\nyY\n\t\u0011!A\u0003\u0002\u0011\u00121a\u0018\u00135\u0011\u0015qd\u00071\u0001@\u0003\u001d)'/Y:ve\u0016\u0004$\u0001\u0011%\u0011\u0007\u0005#uI\u0004\u0002\u0010\u0005&\u00111\tE\u0001\u0007!J,G-\u001a4\n\u0005\u00153%!B\"mCN\u001c(BA\"\u0011!\t\t\u0003\nB\u0005J{\u0005\u0005\t\u0011!B\u0001I\t\u0019q\f\n\u001a\t\u000b-3\u0004\u0019\u0001'\u0002\u0011QL\b/Z!sON\u00042!T+Y\u001d\tq5K\u0004\u0002P%6\t\u0001K\u0003\u0002R\u0011\u00051AH]8pizJ\u0011!E\u0005\u0003)B\tq\u0001]1dW\u0006<W-\u0003\u0002W/\n\u00191+Z9\u000b\u0005Q\u0003\u0002GA-\\!\rabD\u0017\t\u0003Cm#\u0011\u0002\u0018&\u0002\u0002\u0003\u0005)\u0011\u0001\u0013\u0003\u0007}#3\u0007C\u0003\u0019\u0017\u0011\u0005a\f\u0006\u0002`IB\u0012\u0001M\u0019\t\u00049y\t\u0007CA\u0011c\t%\u0019W,!A\u0001\u0002\u000b\u0005AEA\u0002`IUBQ!Z/A\u0002\u0019\f!a\u001d;\u0011\u0005)9\u0017B\u00015\u0003\u0005%\u00196-\u00197b)f\u0004X\rC\u0003k\u0017\u0011%1.A\u0005ge>l7\t\\1tgR\u0011A.\u001d\u0019\u0003[>\u00042\u0001\b\u0010o!\t\ts\u000eB\u0005qS\u0006\u0005\t\u0011!B\u0001I\t\u0019q\fJ\u001c\t\u000bIL\u0007\u0019A:\u0002\u000b\rd\u0017M\u001f>1\u0005Q4\bcA!EkB\u0011\u0011E\u001e\u0003\noF\f\t\u0011!A\u0003\u0002\u0011\u00121a\u0018\u00137\u0001")
public final class ManifestFactory
{
    public static Manifest<?> manifestOf(final ScalaType st) {
        return ManifestFactory$.MODULE$.manifestOf(st);
    }
    
    public static Manifest<?> manifestOf(final Class<?> erasure, final Seq<Manifest<?>> typeArgs) {
        return ManifestFactory$.MODULE$.manifestOf(erasure, typeArgs);
    }
    
    public static Manifest<?> manifestOf(final Type t) {
        return ManifestFactory$.MODULE$.manifestOf(t);
    }
}
