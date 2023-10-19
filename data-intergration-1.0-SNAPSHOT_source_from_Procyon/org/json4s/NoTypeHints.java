// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.runtime.Nothing$;
import scala.None$;
import scala.collection.Iterator;
import scala.Tuple2;
import scala.PartialFunction;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001\u00055r!B\u0001\u0003\u0011\u0003;\u0011a\u0003(p)f\u0004X\rS5oiNT!a\u0001\u0003\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005)\u0011aA8sO\u000e\u0001\u0001C\u0001\u0005\n\u001b\u0005\u0011a!\u0002\u0006\u0003\u0011\u0003[!a\u0003(p)f\u0004X\rS5oiN\u001cR!\u0003\u0007\u0013+a\u0001\"!\u0004\t\u000e\u00039Q\u0011aD\u0001\u0006g\u000e\fG.Y\u0005\u0003#9\u0011a!\u00118z%\u00164\u0007C\u0001\u0005\u0014\u0013\t!\"AA\u0005UsB,\u0007*\u001b8ugB\u0011QBF\u0005\u0003/9\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u000e3%\u0011!D\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\u00069%!\t!H\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0003\u001dAqaH\u0005C\u0002\u0013\u0005\u0001%A\u0003iS:$8/F\u0001\"!\r\u0011#&\f\b\u0003G!r!\u0001J\u0014\u000e\u0003\u0015R!A\n\u0004\u0002\rq\u0012xn\u001c;?\u0013\u0005y\u0011BA\u0015\u000f\u0003\u001d\u0001\u0018mY6bO\u0016L!a\u000b\u0017\u0003\t1K7\u000f\u001e\u0006\u0003S9\u0001$AL\u001c\u0011\u0007=\u0012TG\u0004\u0002\u000ea%\u0011\u0011GD\u0001\u0007!J,G-\u001a4\n\u0005M\"$!B\"mCN\u001c(BA\u0019\u000f!\t1t\u0007\u0004\u0001\u0005\u0013aJ\u0014\u0011!A\u0001\u0006\u0003\u0001%\u0001B0%geBaAO\u0005!\u0002\u0013Y\u0014A\u00025j]R\u001c\b\u0005E\u0002#Uq\u0002$!P \u0011\u0007=\u0012d\b\u0005\u00027\u007f\u0011I\u0001(OA\u0001\u0002\u0003\u0015\t\u0001Q\t\u0003\u0003\u0012\u0003\"!\u0004\"\n\u0005\rs!a\u0002(pi\"Lgn\u001a\t\u0003\u001b\u0015K!A\u0012\b\u0003\u0007\u0005s\u0017\u0010C\u0003I\u0013\u0011\u0005\u0011*A\u0004iS:$hi\u001c:\u0015\u0005\u0005S\u0005\"B&H\u0001\u0004a\u0015!B2mCjT\bGA'P!\ry#G\u0014\t\u0003m=#\u0011\u0002\u0015&\u0002\u0002\u0003\u0005)\u0011\u0001!\u0003\t}#C\u0007\r\u0005\u0006%&!\taU\u0001\tG2\f7o\u001d$peR\u0011Ak\u0016\b\u0003\u001bUK!A\u0016\b\u0002\t9{g.\u001a\u0005\u00061F\u0003\r!W\u0001\u0005Q&tG\u000f\u0005\u000205&\u00111\f\u000e\u0002\u0007'R\u0014\u0018N\\4\t\u000buKA\u0011\t0\u0002%MDw.\u001e7e\u000bb$(/Y2u\u0011&tGo\u001d\u000b\u0003?\n\u0004\"!\u00041\n\u0005\u0005t!a\u0002\"p_2,\u0017M\u001c\u0005\u0006\u0017r\u0003\ra\u0019\u0019\u0003I\u001a\u00042a\f\u001af!\t1d\rB\u0005hE\u0006\u0005\t\u0011!B\u0001\u0001\n!q\f\n\u001b2\u0011\u001dI\u0017\"!A\u0005B)\fQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#A6\u0011\u00051\fX\"A7\u000b\u00059|\u0017\u0001\u00027b]\u001eT\u0011\u0001]\u0001\u0005U\u00064\u0018-\u0003\u0002\\[\"91/CA\u0001\n\u0003!\u0018\u0001\u00049s_\u0012,8\r^!sSRLX#A;\u0011\u000551\u0018BA<\u000f\u0005\rIe\u000e\u001e\u0005\bs&\t\t\u0011\"\u0001{\u00039\u0001(o\u001c3vGR,E.Z7f]R$\"\u0001R>\t\u000fqD\u0018\u0011!a\u0001k\u0006\u0019\u0001\u0010J\u0019\t\u000fyL\u0011\u0011!C!\u007f\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/\u0006\u0002\u0002\u0002A)\u00111AA\u0005\t6\u0011\u0011Q\u0001\u0006\u0004\u0003\u000fq\u0011AC2pY2,7\r^5p]&!\u00111BA\u0003\u0005!IE/\u001a:bi>\u0014\b\"CA\b\u0013\u0005\u0005I\u0011AA\t\u0003!\u0019\u0017M\\#rk\u0006dGcA0\u0002\u0014!AA0!\u0004\u0002\u0002\u0003\u0007A\tC\u0005\u0002\u0018%\t\t\u0011\"\u0011\u0002\u001a\u0005A\u0001.Y:i\u0007>$W\rF\u0001v\u0011%\ti\"CA\u0001\n\u0003\ny\"\u0001\u0005u_N#(/\u001b8h)\u0005Y\u0007\"CA\u0012\u0013\u0005\u0005I\u0011BA\u0013\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u001d\u0002c\u00017\u0002*%\u0019\u00111F7\u0003\r=\u0013'.Z2u\u0001")
public final class NoTypeHints
{
    public static TypeHints $plus(final TypeHints hints) {
        return NoTypeHints$.MODULE$.$plus(hints);
    }
    
    public static List<TypeHints> components() {
        return NoTypeHints$.MODULE$.components();
    }
    
    public static PartialFunction<Object, JsonAST.JObject> serialize() {
        return NoTypeHints$.MODULE$.serialize();
    }
    
    public static PartialFunction<Tuple2<String, JsonAST.JObject>, Object> deserialize() {
        return NoTypeHints$.MODULE$.deserialize();
    }
    
    public static boolean containsHint(final Class<?> clazz) {
        return NoTypeHints$.MODULE$.containsHint(clazz);
    }
    
    public static String toString() {
        return NoTypeHints$.MODULE$.toString();
    }
    
    public static int hashCode() {
        return NoTypeHints$.MODULE$.hashCode();
    }
    
    public static boolean canEqual(final Object x$1) {
        return NoTypeHints$.MODULE$.canEqual(x$1);
    }
    
    public static Iterator<Object> productIterator() {
        return NoTypeHints$.MODULE$.productIterator();
    }
    
    public static Object productElement(final int x$1) {
        return NoTypeHints$.MODULE$.productElement(x$1);
    }
    
    public static int productArity() {
        return NoTypeHints$.MODULE$.productArity();
    }
    
    public static String productPrefix() {
        return NoTypeHints$.MODULE$.productPrefix();
    }
    
    public static boolean shouldExtractHints(final Class<?> clazz) {
        return NoTypeHints$.MODULE$.shouldExtractHints(clazz);
    }
    
    public static None$ classFor(final String hint) {
        return NoTypeHints$.MODULE$.classFor(hint);
    }
    
    public static Nothing$ hintFor(final Class<?> clazz) {
        return NoTypeHints$.MODULE$.hintFor(clazz);
    }
    
    public static List<Class<?>> hints() {
        return NoTypeHints$.MODULE$.hints();
    }
}
