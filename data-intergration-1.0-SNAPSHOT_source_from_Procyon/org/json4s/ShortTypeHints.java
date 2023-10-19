// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Tuple2;
import scala.PartialFunction;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.Option;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ee\u0001B\u0001\u0003\u0001\u001e\u0011ab\u00155peR$\u0016\u0010]3IS:$8O\u0003\u0002\u0004\t\u00051!n]8oiMT\u0011!B\u0001\u0004_J<7\u0001A\n\u0006\u0001!q!#\u0006\t\u0003\u00131i\u0011A\u0003\u0006\u0002\u0017\u0005)1oY1mC&\u0011QB\u0003\u0002\u0007\u0003:L(+\u001a4\u0011\u0005=\u0001R\"\u0001\u0002\n\u0005E\u0011!!\u0003+za\u0016D\u0015N\u001c;t!\tI1#\u0003\u0002\u0015\u0015\t9\u0001K]8ek\u000e$\bCA\u0005\u0017\u0013\t9\"B\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001a\u0001\tU\r\u0011\"\u0001\u001b\u0003\u0015A\u0017N\u001c;t+\u0005Y\u0002c\u0001\u000f%O9\u0011QD\t\b\u0003=\u0005j\u0011a\b\u0006\u0003A\u0019\ta\u0001\u0010:p_Rt\u0014\"A\u0006\n\u0005\rR\u0011a\u00029bG.\fw-Z\u0005\u0003K\u0019\u0012A\u0001T5ti*\u00111E\u0003\u0019\u0003QE\u00022!\u000b\u00170\u001d\tI!&\u0003\u0002,\u0015\u00051\u0001K]3eK\u001aL!!\f\u0018\u0003\u000b\rc\u0017m]:\u000b\u0005-R\u0001C\u0001\u00192\u0019\u0001!\u0011BM\u001a\u0002\u0002\u0003\u0005)\u0011\u0001\u001e\u0003\t}#CG\r\u0005\ti\u0001\u0011\t\u0012)A\u0005k\u00051\u0001.\u001b8ug\u0002\u00022\u0001\b\u00137a\t9\u0014\bE\u0002*Ya\u0002\"\u0001M\u001d\u0005\u0013I\u001a\u0014\u0011!A\u0001\u0006\u0003Q\u0014CA\u001e?!\tIA(\u0003\u0002>\u0015\t9aj\u001c;iS:<\u0007CA\u0005@\u0013\t\u0001%BA\u0002B]fDQA\u0011\u0001\u0005\u0002\r\u000ba\u0001P5oSRtDC\u0001#F!\ty\u0001\u0001C\u0003\u001a\u0003\u0002\u0007a\tE\u0002\u001dI\u001d\u0003$\u0001\u0013&\u0011\u0007%b\u0013\n\u0005\u00021\u0015\u0012I!'RA\u0001\u0002\u0003\u0015\tA\u000f\u0005\u0006\u0019\u0002!\t!T\u0001\bQ&tGOR8s)\tqe\u000b\u0005\u0002P)6\t\u0001K\u0003\u0002R%\u0006!A.\u00198h\u0015\u0005\u0019\u0016\u0001\u00026bm\u0006L!!\u0016)\u0003\rM#(/\u001b8h\u0011\u001596\n1\u0001Y\u0003\u0015\u0019G.\u0019>{a\tI6\fE\u0002*Yi\u0003\"\u0001M.\u0005\u0013q3\u0016\u0011!A\u0001\u0006\u0003Q$\u0001B0%iMBQA\u0018\u0001\u0005\u0002}\u000b\u0001b\u00197bgN4uN\u001d\u000b\u0003A\u000e\u00042!C1(\u0013\t\u0011'B\u0001\u0004PaRLwN\u001c\u0005\u0006Iv\u0003\r!Z\u0001\u0005Q&tG\u000f\u0005\u0002*M&\u0011QK\f\u0005\bQ\u0002\t\t\u0011\"\u0001j\u0003\u0011\u0019w\u000e]=\u0015\u0005\u0011S\u0007bB\rh!\u0003\u0005\rA\u0012\u0005\bY\u0002\t\n\u0011\"\u0001n\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012A\u001c\u0016\u00037=\\\u0013\u0001\u001d\t\u0003cZl\u0011A\u001d\u0006\u0003gR\f\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005UT\u0011AC1o]>$\u0018\r^5p]&\u0011qO\u001d\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB=\u0001\u0003\u0003%\tE_\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u00039Cq\u0001 \u0001\u0002\u0002\u0013\u0005Q0\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001\u007f!\tIq0C\u0002\u0002\u0002)\u00111!\u00138u\u0011%\t)\u0001AA\u0001\n\u0003\t9!\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0007y\nI\u0001C\u0005\u0002\f\u0005\r\u0011\u0011!a\u0001}\u0006\u0019\u0001\u0010J\u0019\t\u0013\u0005=\u0001!!A\u0005B\u0005E\u0011a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0005\u0005M\u0001#BA\u000b\u00037qTBAA\f\u0015\r\tIBC\u0001\u000bG>dG.Z2uS>t\u0017\u0002BA\u000f\u0003/\u0011\u0001\"\u0013;fe\u0006$xN\u001d\u0005\n\u0003C\u0001\u0011\u0011!C\u0001\u0003G\t\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0005\u0003K\tY\u0003E\u0002\n\u0003OI1!!\u000b\u000b\u0005\u001d\u0011un\u001c7fC:D\u0011\"a\u0003\u0002 \u0005\u0005\t\u0019\u0001 \t\u0013\u0005=\u0002!!A\u0005B\u0005E\u0012\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003yD\u0011\"!\u000e\u0001\u0003\u0003%\t%a\u000e\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012A\u0014\u0005\n\u0003w\u0001\u0011\u0011!C!\u0003{\ta!Z9vC2\u001cH\u0003BA\u0013\u0003\u007fA\u0011\"a\u0003\u0002:\u0005\u0005\t\u0019\u0001 \b\u0013\u0005\r#!!A\t\u0002\u0005\u0015\u0013AD*i_J$H+\u001f9f\u0011&tGo\u001d\t\u0004\u001f\u0005\u001dc\u0001C\u0001\u0003\u0003\u0003E\t!!\u0013\u0014\u000b\u0005\u001d\u00131J\u000b\u0011\u000f\u00055\u00131KA,\t6\u0011\u0011q\n\u0006\u0004\u0003#R\u0011a\u0002:v]RLW.Z\u0005\u0005\u0003+\nyEA\tBEN$(/Y2u\rVt7\r^5p]F\u0002B\u0001\b\u0013\u0002ZA\"\u00111LA0!\u0011IC&!\u0018\u0011\u0007A\ny\u0006\u0002\u00063\u0003\u000f\n\t\u0011!A\u0003\u0002iBqAQA$\t\u0003\t\u0019\u0007\u0006\u0002\u0002F!Q\u0011QGA$\u0003\u0003%)%a\u000e\t\u0015\u0005%\u0014qIA\u0001\n\u0003\u000bY'A\u0003baBd\u0017\u0010F\u0002E\u0003[Bq!GA4\u0001\u0004\ty\u0007\u0005\u0003\u001dI\u0005E\u0004\u0007BA:\u0003o\u0002B!\u000b\u0017\u0002vA\u0019\u0001'a\u001e\u0005\u0015I\ni'!A\u0001\u0002\u000b\u0005!\b\u0003\u0006\u0002|\u0005\u001d\u0013\u0011!CA\u0003{\nq!\u001e8baBd\u0017\u0010\u0006\u0003\u0002\u0000\u0005\u0005\u0005cA\u0005b7!I\u00111QA=\u0003\u0003\u0005\r\u0001R\u0001\u0004q\u0012\u0002\u0004BCAD\u0003\u000f\n\t\u0011\"\u0003\u0002\n\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\tY\tE\u0002P\u0003\u001bK1!a$Q\u0005\u0019y%M[3di\u0002")
public class ShortTypeHints implements TypeHints, Product, Serializable
{
    private final List<Class<?>> hints;
    private volatile CompositeTypeHints$ org$json4s$TypeHints$$CompositeTypeHints$module;
    
    public static Option<List<Class<?>>> unapply(final ShortTypeHints x$0) {
        return ShortTypeHints$.MODULE$.unapply(x$0);
    }
    
    public static ShortTypeHints apply(final List<Class<?>> hints) {
        return ShortTypeHints$.MODULE$.apply(hints);
    }
    
    public static <A> Function1<List<Class<?>>, A> andThen(final Function1<ShortTypeHints, A> function1) {
        return (Function1<List<Class<?>>, A>)ShortTypeHints$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ShortTypeHints> compose(final Function1<A, List<Class<?>>> function1) {
        return (Function1<A, ShortTypeHints>)ShortTypeHints$.MODULE$.compose((Function1)function1);
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
        return clazz.getName().substring(clazz.getName().lastIndexOf(".") + 1);
    }
    
    @Override
    public Option<Class<?>> classFor(final String hint) {
        return (Option<Class<?>>)this.hints().find((Function1)new ShortTypeHints$$anonfun$classFor.ShortTypeHints$$anonfun$classFor$5(this, hint));
    }
    
    public ShortTypeHints copy(final List<Class<?>> hints) {
        return new ShortTypeHints(hints);
    }
    
    public List<Class<?>> copy$default$1() {
        return this.hints();
    }
    
    public String productPrefix() {
        return "ShortTypeHints";
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
        return x$1 instanceof ShortTypeHints;
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
            if (x$1 instanceof ShortTypeHints) {
                final ShortTypeHints shortTypeHints = (ShortTypeHints)x$1;
                final List<Class<?>> hints = this.hints();
                final List<Class<?>> hints2 = shortTypeHints.hints();
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
                        if (shortTypeHints.canEqual(this)) {
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
    
    public ShortTypeHints(final List<Class<?>> hints) {
        this.hints = hints;
        TypeHints$class.$init$(this);
        Product$class.$init$((Product)this);
    }
}
