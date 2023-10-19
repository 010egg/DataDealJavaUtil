// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import org.json4s.reflect.Reflector$;
import scala.Tuple2;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.Option;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\rf\u0001B\u0001\u0003\u0001\u001e\u0011QBR;mYRK\b/\u001a%j]R\u001c(BA\u0002\u0005\u0003\u0019Q7o\u001c85g*\tQ!A\u0002pe\u001e\u001c\u0001aE\u0003\u0001\u00119\u0011R\u0003\u0005\u0002\n\u00195\t!BC\u0001\f\u0003\u0015\u00198-\u00197b\u0013\ti!B\u0001\u0004B]f\u0014VM\u001a\t\u0003\u001fAi\u0011AA\u0005\u0003#\t\u0011\u0011\u0002V=qK\"Kg\u000e^:\u0011\u0005%\u0019\u0012B\u0001\u000b\u000b\u0005\u001d\u0001&o\u001c3vGR\u0004\"!\u0003\f\n\u0005]Q!\u0001D*fe&\fG.\u001b>bE2,\u0007\u0002C\r\u0001\u0005+\u0007I\u0011\u0001\u000e\u0002\u000b!Lg\u000e^:\u0016\u0003m\u00012\u0001\b\u0013(\u001d\ti\"E\u0004\u0002\u001fC5\tqD\u0003\u0002!\r\u00051AH]8pizJ\u0011aC\u0005\u0003G)\tq\u0001]1dW\u0006<W-\u0003\u0002&M\t!A*[:u\u0015\t\u0019#\u0002\r\u0002)cA\u0019\u0011\u0006L\u0018\u000f\u0005%Q\u0013BA\u0016\u000b\u0003\u0019\u0001&/\u001a3fM&\u0011QF\f\u0002\u0006\u00072\f7o\u001d\u0006\u0003W)\u0001\"\u0001M\u0019\r\u0001\u0011I!gMA\u0001\u0002\u0003\u0015\tA\u000f\u0002\u0005?\u0012\"D\u0007\u0003\u00055\u0001\tE\t\u0015!\u00036\u0003\u0019A\u0017N\u001c;tAA\u0019A\u0004\n\u001c1\u0005]J\u0004cA\u0015-qA\u0011\u0001'\u000f\u0003\neM\n\t\u0011!A\u0003\u0002i\n\"a\u000f \u0011\u0005%a\u0014BA\u001f\u000b\u0005\u001dqu\u000e\u001e5j]\u001e\u0004\"!C \n\u0005\u0001S!aA!os\")!\t\u0001C\u0001\u0007\u00061A(\u001b8jiz\"\"\u0001R#\u0011\u0005=\u0001\u0001\"B\rB\u0001\u00041\u0005c\u0001\u000f%\u000fB\u0012\u0001J\u0013\t\u0004S1J\u0005C\u0001\u0019K\t%\u0011T)!A\u0001\u0002\u000b\u0005!\bC\u0003M\u0001\u0011\u0005Q*A\u0004iS:$hi\u001c:\u0015\u000593\u0006CA(U\u001b\u0005\u0001&BA)S\u0003\u0011a\u0017M\\4\u000b\u0003M\u000bAA[1wC&\u0011Q\u000b\u0015\u0002\u0007'R\u0014\u0018N\\4\t\u000b][\u0005\u0019\u0001-\u0002\u000b\rd\u0017M\u001f>1\u0005e[\u0006cA\u0015-5B\u0011\u0001g\u0017\u0003\n9Z\u000b\t\u0011!A\u0003\u0002i\u0012Aa\u0018\u00135k!)a\f\u0001C\u0001?\u0006A1\r\\1tg\u001a{'\u000f\u0006\u0002aYB\u0019\u0011\"Y2\n\u0005\tT!AB(qi&|g\u000e\r\u0002eOB\u0019q*\u001a4\n\u00055\u0002\u0006C\u0001\u0019h\t%A\u0017.!A\u0001\u0002\u000b\u0005!HA\u0002`I]BqA[/\u0002\u0002\u0003\u00051.\u0001\u0005%C:|gNZ;o\u0017\u0001AQ!\\/A\u00029\fA\u0001[5oiB\u0011\u0011f\\\u0005\u0003+:Bq!\u001d\u0001\u0002\u0002\u0013\u0005!/\u0001\u0003d_BLHC\u0001#t\u0011\u001dI\u0002\u000f%AA\u0002\u0019Cq!\u001e\u0001\u0012\u0002\u0013\u0005a/\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003]T#a\u0007=,\u0003e\u0004\"A_@\u000e\u0003mT!\u0001`?\u0002\u0013Ut7\r[3dW\u0016$'B\u0001@\u000b\u0003)\tgN\\8uCRLwN\\\u0005\u0004\u0003\u0003Y(!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"I\u0011Q\u0001\u0001\u0002\u0002\u0013\u0005\u0013qA\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u00039C\u0011\"a\u0003\u0001\u0003\u0003%\t!!\u0004\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u0005\u0005=\u0001cA\u0005\u0002\u0012%\u0019\u00111\u0003\u0006\u0003\u0007%sG\u000fC\u0005\u0002\u0018\u0001\t\t\u0011\"\u0001\u0002\u001a\u0005q\u0001O]8ek\u000e$X\t\\3nK:$Hc\u0001 \u0002\u001c!Q\u0011QDA\u000b\u0003\u0003\u0005\r!a\u0004\u0002\u0007a$\u0013\u0007C\u0005\u0002\"\u0001\t\t\u0011\"\u0011\u0002$\u0005y\u0001O]8ek\u000e$\u0018\n^3sCR|'/\u0006\u0002\u0002&A)\u0011qEA\u0017}5\u0011\u0011\u0011\u0006\u0006\u0004\u0003WQ\u0011AC2pY2,7\r^5p]&!\u0011qFA\u0015\u0005!IE/\u001a:bi>\u0014\b\"CA\u001a\u0001\u0005\u0005I\u0011AA\u001b\u0003!\u0019\u0017M\\#rk\u0006dG\u0003BA\u001c\u0003{\u00012!CA\u001d\u0013\r\tYD\u0003\u0002\b\u0005>|G.Z1o\u0011%\ti\"!\r\u0002\u0002\u0003\u0007a\bC\u0005\u0002B\u0001\t\t\u0011\"\u0011\u0002D\u0005A\u0001.Y:i\u0007>$W\r\u0006\u0002\u0002\u0010!I\u0011q\t\u0001\u0002\u0002\u0013\u0005\u0013\u0011J\u0001\ti>\u001cFO]5oOR\ta\nC\u0005\u0002N\u0001\t\t\u0011\"\u0011\u0002P\u00051Q-];bYN$B!a\u000e\u0002R!I\u0011QDA&\u0003\u0003\u0005\rAP\u0004\n\u0003+\u0012\u0011\u0011!E\u0001\u0003/\nQBR;mYRK\b/\u001a%j]R\u001c\bcA\b\u0002Z\u0019A\u0011AAA\u0001\u0012\u0003\tYfE\u0003\u0002Z\u0005uS\u0003E\u0004\u0002`\u0005\u0015\u0014\u0011\u000e#\u000e\u0005\u0005\u0005$bAA2\u0015\u00059!/\u001e8uS6,\u0017\u0002BA4\u0003C\u0012\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c82!\u0011aB%a\u001b1\t\u00055\u0014\u0011\u000f\t\u0005S1\ny\u0007E\u00021\u0003c\"!BMA-\u0003\u0003\u0005\tQ!\u0001;\u0011\u001d\u0011\u0015\u0011\fC\u0001\u0003k\"\"!a\u0016\t\u0015\u0005\u001d\u0013\u0011LA\u0001\n\u000b\nI\u0005\u0003\u0006\u0002|\u0005e\u0013\u0011!CA\u0003{\nQ!\u00199qYf$2\u0001RA@\u0011\u001dI\u0012\u0011\u0010a\u0001\u0003\u0003\u0003B\u0001\b\u0013\u0002\u0004B\"\u0011QQAE!\u0011IC&a\"\u0011\u0007A\nI\t\u0002\u00063\u0003\u007f\n\t\u0011!A\u0003\u0002iB!\"!$\u0002Z\u0005\u0005I\u0011QAH\u0003\u001d)h.\u00199qYf$B!!%\u0002\u0014B\u0019\u0011\"Y\u000e\t\u0013\u0005U\u00151RA\u0001\u0002\u0004!\u0015a\u0001=%a!Q\u0011\u0011TA-\u0003\u0003%I!a'\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003;\u00032aTAP\u0013\r\t\t\u000b\u0015\u0002\u0007\u001f\nTWm\u0019;")
public class FullTypeHints implements TypeHints, Product, Serializable
{
    private final List<Class<?>> hints;
    private volatile CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$module;
    
    public static Option<List<Class<?>>> unapply(final FullTypeHints x$0) {
        return FullTypeHints$.MODULE$.unapply(x$0);
    }
    
    public static FullTypeHints apply(final List<Class<?>> hints) {
        return FullTypeHints$.MODULE$.apply(hints);
    }
    
    public static <A> Function1<List<Class<?>>, A> andThen(final Function1<FullTypeHints, A> function1) {
        return (Function1<List<Class<?>>, A>)FullTypeHints$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, FullTypeHints> compose(final Function1<A, List<Class<?>>> function1) {
        return (Function1<A, FullTypeHints>)FullTypeHints$.MODULE$.compose((Function1)function1);
    }
    
    private CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$lzycompute() {
        synchronized (this) {
            if (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) {
                this.org$json4s$TypeHints$$CompositeTypeHints$module = new CompositeTypeHints$(this);
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.org$json4s$TypeHints$$CompositeTypeHints$module;
        }
    }
    
    @Override
    public CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints() {
        return (this.org$json4s$TypeHints$$CompositeTypeHints$module == null) ? this.org$json4s$TypeHints$$CompositeTypeHints$lzycompute() : this.org$json4s$TypeHints$$CompositeTypeHints$module;
    }
    
    @Override
    public boolean containsHint(final Class<?> clazz) {
        return TypeHints$class.containsHint(this, clazz);
    }
    
    @Override
    public boolean shouldExtractHints(final Class<?> clazz) {
        return TypeHints$class.shouldExtractHints(this, clazz);
    }
    
    @Override
    public PartialFunction<Tuple2<String, JsonAST.JObject>, Object> deserialize() {
        return (PartialFunction<Tuple2<String, JsonAST.JObject>, Object>)TypeHints$class.deserialize(this);
    }
    
    @Override
    public PartialFunction<Object, JsonAST.JObject> serialize() {
        return (PartialFunction<Object, JsonAST.JObject>)TypeHints$class.serialize(this);
    }
    
    @Override
    public List<TypeHints> components() {
        return (List<TypeHints>)TypeHints$class.components(this);
    }
    
    @Override
    public TypeHints $plus(final TypeHints hints) {
        return TypeHints$class.$plus(this, hints);
    }
    
    @Override
    public List<Class<?>> hints() {
        return this.hints;
    }
    
    @Override
    public String hintFor(final Class<?> clazz) {
        return clazz.getName();
    }
    
    @Override
    public Option<Class<?>> classFor(final String hint) {
        return (Option<Class<?>>)Reflector$.MODULE$.scalaTypeOf(hint).map((Function1)new FullTypeHints$$anonfun$classFor.FullTypeHints$$anonfun$classFor$6(this));
    }
    
    public FullTypeHints copy(final List<Class<?>> hints) {
        return new FullTypeHints(hints);
    }
    
    public List<Class<?>> copy$default$1() {
        return this.hints();
    }
    
    public String productPrefix() {
        return "FullTypeHints";
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
                return this.hints();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof FullTypeHints;
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
            if (x$1 instanceof FullTypeHints) {
                final FullTypeHints fullTypeHints = (FullTypeHints)x$1;
                final List<Class<?>> hints = this.hints();
                final List<Class<?>> hints2 = fullTypeHints.hints();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (hints == null) {
                            if (hints2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!hints.equals(hints2)) {
                            break Label_0076;
                        }
                        if (fullTypeHints.canEqual(this)) {
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
    
    public FullTypeHints(final List<Class<?>> hints) {
        this.hints = hints;
        TypeHints$class.$init$(this);
        Product$class.$init$((Product)this);
    }
}
