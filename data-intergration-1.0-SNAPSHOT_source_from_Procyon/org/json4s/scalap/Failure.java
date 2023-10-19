// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.None$;
import scala.Function1;
import scala.Tuple2;
import scala.Function2;
import scala.Function0;
import scala.runtime.Nothing$;
import scala.collection.Iterator;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001a;Q!\u0001\u0002\t\u0002&\tqAR1jYV\u0014XM\u0003\u0002\u0004\t\u000511oY1mCBT!!\u0002\u0004\u0002\r)\u001cxN\u001c\u001bt\u0015\u00059\u0011aA8sO\u000e\u0001\u0001C\u0001\u0006\f\u001b\u0005\u0011a!\u0002\u0007\u0003\u0011\u0003k!a\u0002$bS2,(/Z\n\u0005\u001799\"\u0004E\u0002\u000b\u001fEI!\u0001\u0005\u0002\u0003\u00139{7+^2dKN\u001c\bC\u0001\n\u0016\u001b\u0005\u0019\"\"\u0001\u000b\u0002\u000bM\u001c\u0017\r\\1\n\u0005Y\u0019\"a\u0002(pi\"Lgn\u001a\t\u0003%aI!!G\n\u0003\u000fA\u0013x\u000eZ;diB\u0011!cG\u0005\u00039M\u0011AbU3sS\u0006d\u0017N_1cY\u0016DQAH\u0006\u0005\u0002}\ta\u0001P5oSRtD#A\u0005\t\u000b\u0005ZA\u0011\u0001\u0012\u0002\u000b\u0015\u0014(o\u001c:\u0016\u0003EAq\u0001J\u0006\u0002\u0002\u0013\u0005S%A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002MA\u0011q\u0005L\u0007\u0002Q)\u0011\u0011FK\u0001\u0005Y\u0006twMC\u0001,\u0003\u0011Q\u0017M^1\n\u00055B#AB*ue&tw\rC\u00040\u0017\u0005\u0005I\u0011\u0001\u0019\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u0003E\u0002\"A\u0005\u001a\n\u0005M\u001a\"aA%oi\"9QgCA\u0001\n\u00031\u0014A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003oi\u0002\"A\u0005\u001d\n\u0005e\u001a\"aA!os\"91\bNA\u0001\u0002\u0004\t\u0014a\u0001=%c!9QhCA\u0001\n\u0003r\u0014a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003}\u00022\u0001Q\"8\u001b\u0005\t%B\u0001\"\u0014\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003\t\u0006\u0013\u0001\"\u0013;fe\u0006$xN\u001d\u0005\b\r.\t\t\u0011\"\u0001H\u0003!\u0019\u0017M\\#rk\u0006dGC\u0001%L!\t\u0011\u0012*\u0003\u0002K'\t9!i\\8mK\u0006t\u0007bB\u001eF\u0003\u0003\u0005\ra\u000e\u0005\b\u001b.\t\t\u0011\"\u0011O\u0003!A\u0017m\u001d5D_\u0012,G#A\u0019\t\u000fA[\u0011\u0011!C!#\u0006AAo\\*ue&tw\rF\u0001'\u0011\u001d\u00196\"!A\u0005\nQ\u000b1B]3bIJ+7o\u001c7wKR\tQ\u000b\u0005\u0002(-&\u0011q\u000b\u000b\u0002\u0007\u001f\nTWm\u0019;")
public final class Failure
{
    public static String toString() {
        return Failure$.MODULE$.toString();
    }
    
    public static int hashCode() {
        return Failure$.MODULE$.hashCode();
    }
    
    public static boolean canEqual(final Object x$1) {
        return Failure$.MODULE$.canEqual(x$1);
    }
    
    public static Iterator<Object> productIterator() {
        return Failure$.MODULE$.productIterator();
    }
    
    public static Object productElement(final int x$1) {
        return Failure$.MODULE$.productElement(x$1);
    }
    
    public static int productArity() {
        return Failure$.MODULE$.productArity();
    }
    
    public static String productPrefix() {
        return Failure$.MODULE$.productPrefix();
    }
    
    public static Nothing$ error() {
        return Failure$.MODULE$.error();
    }
    
    public static <Out2, B> Result<Out2, B, Nothing$> orElse(final Function0<Result<Out2, B, Nothing$>> other) {
        return Failure$.MODULE$.orElse(other);
    }
    
    public static <Out2, B> NoSuccess<Nothing$> flatMap(final Function2<Nothing$, Nothing$, Result<Out2, B, Nothing$>> f) {
        return Failure$.MODULE$.flatMap((scala.Function2<Nothing$, Nothing$, Result<Object, Object, Nothing$>>)f);
    }
    
    public static <Out2, B> NoSuccess<Nothing$> map(final Function2<Nothing$, Nothing$, Tuple2<Out2, B>> f) {
        return Failure$.MODULE$.map((scala.Function2<Nothing$, Nothing$, scala.Tuple2<Object, Object>>)f);
    }
    
    public static <Out2> NoSuccess<Nothing$> mapOut(final Function1<Nothing$, Out2> f) {
        return Failure$.MODULE$.mapOut((scala.Function1<Nothing$, Object>)f);
    }
    
    public static <B> NoSuccess<Nothing$> map(final Function1<Nothing$, B> f) {
        return Failure$.MODULE$.map((scala.Function1<Nothing$, Object>)f);
    }
    
    public static None$ toOption() {
        return Failure$.MODULE$.toOption();
    }
    
    public static Nothing$ value() {
        return Failure$.MODULE$.value();
    }
    
    public static Nothing$ out() {
        return Failure$.MODULE$.out();
    }
}
