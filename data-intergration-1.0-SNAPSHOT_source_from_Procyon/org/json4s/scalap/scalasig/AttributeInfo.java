// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple4;
import org.json4s.scalap.$tilde;
import scala.collection.Seq;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005]e\u0001B\u0001\u0003\u0001.\u0011Q\"\u0011;ue&\u0014W\u000f^3J]\u001a|'BA\u0002\u0005\u0003!\u00198-\u00197bg&<'BA\u0003\u0007\u0003\u0019\u00198-\u00197ba*\u0011q\u0001C\u0001\u0007UN|g\u000eN:\u000b\u0003%\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0007\u0013+A\u0011Q\u0002E\u0007\u0002\u001d)\tq\"A\u0003tG\u0006d\u0017-\u0003\u0002\u0012\u001d\t1\u0011I\\=SK\u001a\u0004\"!D\n\n\u0005Qq!a\u0002)s_\u0012,8\r\u001e\t\u0003\u001bYI!a\u0006\b\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011e\u0001!Q3A\u0005\u0002i\taa]=nE>dW#A\u000e\u0011\u0005qiR\"\u0001\u0002\n\u0005y\u0011!AB*z[\n|G\u000e\u0003\u0005!\u0001\tE\t\u0015!\u0003\u001c\u0003\u001d\u0019\u00180\u001c2pY\u0002B\u0001B\t\u0001\u0003\u0016\u0004%\taI\u0001\bif\u0004XMU3g+\u0005!\u0003C\u0001\u000f&\u0013\t1#A\u0001\u0003UsB,\u0007\u0002\u0003\u0015\u0001\u0005#\u0005\u000b\u0011\u0002\u0013\u0002\u0011QL\b/\u001a*fM\u0002B\u0001B\u000b\u0001\u0003\u0016\u0004%\taK\u0001\u0006m\u0006dW/Z\u000b\u0002YA\u0019Q\"L\u0018\n\u00059r!AB(qi&|g\u000e\u0005\u0002\u000ea%\u0011\u0011G\u0004\u0002\u0004\u0003:L\b\u0002C\u001a\u0001\u0005#\u0005\u000b\u0011\u0002\u0017\u0002\rY\fG.^3!\u0011!)\u0004A!f\u0001\n\u00031\u0014A\u0002<bYV,7/F\u00018!\rA\u0004i\u0011\b\u0003syr!AO\u001f\u000e\u0003mR!\u0001\u0010\u0006\u0002\rq\u0012xn\u001c;?\u0013\u0005y\u0011BA \u000f\u0003\u001d\u0001\u0018mY6bO\u0016L!!\u0011\"\u0003\u0007M+\u0017O\u0003\u0002@\u001dA!A)R$0\u001b\u0005!\u0011B\u0001$\u0005\u0005\u0019!C/\u001b7eKB\u0011\u0001j\u0013\b\u0003\u001b%K!A\u0013\b\u0002\rA\u0013X\rZ3g\u0013\taUJ\u0001\u0004TiJLgn\u001a\u0006\u0003\u0015:A\u0001b\u0014\u0001\u0003\u0012\u0003\u0006IaN\u0001\bm\u0006dW/Z:!\u0011\u0015\t\u0006\u0001\"\u0001S\u0003\u0019a\u0014N\\5u}Q)1\u000bV+W/B\u0011A\u0004\u0001\u0005\u00063A\u0003\ra\u0007\u0005\u0006EA\u0003\r\u0001\n\u0005\u0006UA\u0003\r\u0001\f\u0005\u0006kA\u0003\ra\u000e\u0005\b3\u0002\t\t\u0011\"\u0001[\u0003\u0011\u0019w\u000e]=\u0015\u000bM[F,\u00180\t\u000feA\u0006\u0013!a\u00017!9!\u0005\u0017I\u0001\u0002\u0004!\u0003b\u0002\u0016Y!\u0003\u0005\r\u0001\f\u0005\bka\u0003\n\u00111\u00018\u0011\u001d\u0001\u0007!%A\u0005\u0002\u0005\fabY8qs\u0012\"WMZ1vYR$\u0013'F\u0001cU\tY2mK\u0001e!\t)'.D\u0001g\u0015\t9\u0007.A\u0005v]\u000eDWmY6fI*\u0011\u0011ND\u0001\u000bC:tw\u000e^1uS>t\u0017BA6g\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\b[\u0002\t\n\u0011\"\u0001o\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uII*\u0012a\u001c\u0016\u0003I\rDq!\u001d\u0001\u0012\u0002\u0013\u0005!/\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u001a\u0016\u0003MT#\u0001L2\t\u000fU\u0004\u0011\u0013!C\u0001m\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\"T#A<+\u0005]\u001a\u0007bB=\u0001\u0003\u0003%\tE_\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003m\u00042\u0001`A\u0002\u001b\u0005i(B\u0001@\u0000\u0003\u0011a\u0017M\\4\u000b\u0005\u0005\u0005\u0011\u0001\u00026bm\u0006L!\u0001T?\t\u0013\u0005\u001d\u0001!!A\u0005\u0002\u0005%\u0011\u0001\u00049s_\u0012,8\r^!sSRLXCAA\u0006!\ri\u0011QB\u0005\u0004\u0003\u001fq!aA%oi\"I\u00111\u0003\u0001\u0002\u0002\u0013\u0005\u0011QC\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\ry\u0013q\u0003\u0005\u000b\u00033\t\t\"!AA\u0002\u0005-\u0011a\u0001=%c!I\u0011Q\u0004\u0001\u0002\u0002\u0013\u0005\u0013qD\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\u0011\u0011\u0011\u0005\t\u0006\u0003G\tIcL\u0007\u0003\u0003KQ1!a\n\u000f\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0005\u0003W\t)C\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011%\ty\u0003AA\u0001\n\u0003\t\t$\u0001\u0005dC:,\u0015/^1m)\u0011\t\u0019$!\u000f\u0011\u00075\t)$C\u0002\u000289\u0011qAQ8pY\u0016\fg\u000eC\u0005\u0002\u001a\u00055\u0012\u0011!a\u0001_!I\u0011Q\b\u0001\u0002\u0002\u0013\u0005\u0013qH\u0001\tQ\u0006\u001c\bnQ8eKR\u0011\u00111\u0002\u0005\n\u0003\u0007\u0002\u0011\u0011!C!\u0003\u000b\n\u0001\u0002^8TiJLgn\u001a\u000b\u0002w\"I\u0011\u0011\n\u0001\u0002\u0002\u0013\u0005\u00131J\u0001\u0007KF,\u0018\r\\:\u0015\t\u0005M\u0012Q\n\u0005\n\u00033\t9%!AA\u0002=:\u0011\"!\u0015\u0003\u0003\u0003E\t!a\u0015\u0002\u001b\u0005#HO]5ckR,\u0017J\u001c4p!\ra\u0012Q\u000b\u0004\t\u0003\t\t\t\u0011#\u0001\u0002XM)\u0011QKA-+AI\u00111LA17\u0011bsgU\u0007\u0003\u0003;R1!a\u0018\u000f\u0003\u001d\u0011XO\u001c;j[\u0016LA!a\u0019\u0002^\t\t\u0012IY:ue\u0006\u001cGOR;oGRLwN\u001c\u001b\t\u000fE\u000b)\u0006\"\u0001\u0002hQ\u0011\u00111\u000b\u0005\u000b\u0003\u0007\n)&!A\u0005F\u0005\u0015\u0003BCA7\u0003+\n\t\u0011\"!\u0002p\u0005)\u0011\r\u001d9msRI1+!\u001d\u0002t\u0005U\u0014q\u000f\u0005\u00073\u0005-\u0004\u0019A\u000e\t\r\t\nY\u00071\u0001%\u0011\u0019Q\u00131\u000ea\u0001Y!1Q'a\u001bA\u0002]B!\"a\u001f\u0002V\u0005\u0005I\u0011QA?\u0003\u001d)h.\u00199qYf$B!a \u0002\bB!Q\"LAA!\u001di\u00111Q\u000e%Y]J1!!\"\u000f\u0005\u0019!V\u000f\u001d7fi!I\u0011\u0011RA=\u0003\u0003\u0005\raU\u0001\u0004q\u0012\u0002\u0004BCAG\u0003+\n\t\u0011\"\u0003\u0002\u0010\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\t\t\nE\u0002}\u0003'K1!!&~\u0005\u0019y%M[3di\u0002")
public class AttributeInfo implements Product, Serializable
{
    private final Symbol symbol;
    private final Type typeRef;
    private final Option<Object> value;
    private final Seq<$tilde<String, Object>> values;
    
    public static Option<Tuple4<Symbol, Type, Option<Object>, Seq<$tilde<String, Object>>>> unapply(final AttributeInfo x$0) {
        return AttributeInfo$.MODULE$.unapply(x$0);
    }
    
    public static AttributeInfo apply(final Symbol symbol, final Type typeRef, final Option<Object> value, final Seq<$tilde<String, Object>> values) {
        return AttributeInfo$.MODULE$.apply(symbol, typeRef, value, values);
    }
    
    public static Function1<Tuple4<Symbol, Type, Option<Object>, Seq<$tilde<String, Object>>>, AttributeInfo> tupled() {
        return (Function1<Tuple4<Symbol, Type, Option<Object>, Seq<$tilde<String, Object>>>, AttributeInfo>)AttributeInfo$.MODULE$.tupled();
    }
    
    public static Function1<Symbol, Function1<Type, Function1<Option<Object>, Function1<Seq<$tilde<String, Object>>, AttributeInfo>>>> curried() {
        return (Function1<Symbol, Function1<Type, Function1<Option<Object>, Function1<Seq<$tilde<String, Object>>, AttributeInfo>>>>)AttributeInfo$.MODULE$.curried();
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public Option<Object> value() {
        return this.value;
    }
    
    public Seq<$tilde<String, Object>> values() {
        return this.values;
    }
    
    public AttributeInfo copy(final Symbol symbol, final Type typeRef, final Option<Object> value, final Seq<$tilde<String, Object>> values) {
        return new AttributeInfo(symbol, typeRef, value, values);
    }
    
    public Symbol copy$default$1() {
        return this.symbol();
    }
    
    public Type copy$default$2() {
        return this.typeRef();
    }
    
    public Option<Object> copy$default$3() {
        return this.value();
    }
    
    public Seq<$tilde<String, Object>> copy$default$4() {
        return this.values();
    }
    
    public String productPrefix() {
        return "AttributeInfo";
    }
    
    public int productArity() {
        return 4;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 3: {
                o = this.values();
                break;
            }
            case 2: {
                o = this.value();
                break;
            }
            case 1: {
                o = this.typeRef();
                break;
            }
            case 0: {
                o = this.symbol();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof AttributeInfo;
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
            if (x$1 instanceof AttributeInfo) {
                final AttributeInfo attributeInfo = (AttributeInfo)x$1;
                final Symbol symbol = this.symbol();
                final Symbol symbol2 = attributeInfo.symbol();
                boolean b = false;
                Label_0173: {
                    Label_0172: {
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0172;
                        }
                        final Type typeRef = this.typeRef();
                        final Type typeRef2 = attributeInfo.typeRef();
                        if (typeRef == null) {
                            if (typeRef2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!typeRef.equals(typeRef2)) {
                            break Label_0172;
                        }
                        final Option<Object> value = this.value();
                        final Option<Object> value2 = attributeInfo.value();
                        if (value == null) {
                            if (value2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!value.equals(value2)) {
                            break Label_0172;
                        }
                        final Seq<$tilde<String, Object>> values = this.values();
                        final Seq<$tilde<String, Object>> values2 = attributeInfo.values();
                        if (values == null) {
                            if (values2 != null) {
                                break Label_0172;
                            }
                        }
                        else if (!values.equals(values2)) {
                            break Label_0172;
                        }
                        if (attributeInfo.canEqual(this)) {
                            b = true;
                            break Label_0173;
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
    
    public AttributeInfo(final Symbol symbol, final Type typeRef, final Option<Object> value, final Seq<$tilde<String, Object>> values) {
        this.symbol = symbol;
        this.typeRef = typeRef;
        this.value = value;
        this.values = values;
        Product$class.$init$((Product)this);
    }
}
