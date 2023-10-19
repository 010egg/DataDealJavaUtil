// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005-c\u0001B\u0001\u0003\u0001.\u0011Qb\u00117bgNLeNZ8UsB,'BA\u0002\u0005\u0003!\u00198-\u00197bg&<'BA\u0003\u0007\u0003\u0019\u00198-\u00197ba*\u0011q\u0001C\u0001\u0007UN|g\u000eN:\u000b\u0003%\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0007\u0011-A\u0011QBD\u0007\u0002\u0005%\u0011qB\u0001\u0002\u0005)f\u0004X\r\u0005\u0002\u0012)5\t!CC\u0001\u0014\u0003\u0015\u00198-\u00197b\u0013\t)\"CA\u0004Qe>$Wo\u0019;\u0011\u0005E9\u0012B\u0001\r\u0013\u00051\u0019VM]5bY&T\u0018M\u00197f\u0011!Q\u0002A!f\u0001\n\u0003Y\u0012AB:z[\n|G.F\u0001\u001d!\tiQ$\u0003\u0002\u001f\u0005\t11+_7c_2D\u0001\u0002\t\u0001\u0003\u0012\u0003\u0006I\u0001H\u0001\bgfl'm\u001c7!\u0011!\u0011\u0003A!f\u0001\n\u0003\u0019\u0013\u0001\u0003;za\u0016\u0014VMZ:\u0016\u0003\u0011\u00022!J\u0017\r\u001d\t13F\u0004\u0002(U5\t\u0001F\u0003\u0002*\u0015\u00051AH]8pizJ\u0011aE\u0005\u0003YI\tq\u0001]1dW\u0006<W-\u0003\u0002/_\t\u00191+Z9\u000b\u00051\u0012\u0002\u0002C\u0019\u0001\u0005#\u0005\u000b\u0011\u0002\u0013\u0002\u0013QL\b/\u001a*fMN\u0004\u0003\"B\u001a\u0001\t\u0003!\u0014A\u0002\u001fj]&$h\bF\u00026m]\u0002\"!\u0004\u0001\t\u000bi\u0011\u0004\u0019\u0001\u000f\t\u000b\t\u0012\u0004\u0019\u0001\u0013\t\u000fe\u0002\u0011\u0011!C\u0001u\u0005!1m\u001c9z)\r)4\b\u0010\u0005\b5a\u0002\n\u00111\u0001\u001d\u0011\u001d\u0011\u0003\b%AA\u0002\u0011BqA\u0010\u0001\u0012\u0002\u0013\u0005q(\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003\u0001S#\u0001H!,\u0003\t\u0003\"a\u0011%\u000e\u0003\u0011S!!\u0012$\u0002\u0013Ut7\r[3dW\u0016$'BA$\u0013\u0003)\tgN\\8uCRLwN\\\u0005\u0003\u0013\u0012\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dY\u0005!%A\u0005\u00021\u000babY8qs\u0012\"WMZ1vYR$#'F\u0001NU\t!\u0013\tC\u0004P\u0001\u0005\u0005I\u0011\t)\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005\t\u0006C\u0001*X\u001b\u0005\u0019&B\u0001+V\u0003\u0011a\u0017M\\4\u000b\u0003Y\u000bAA[1wC&\u0011\u0001l\u0015\u0002\u0007'R\u0014\u0018N\\4\t\u000fi\u0003\u0011\u0011!C\u00017\u0006a\u0001O]8ek\u000e$\u0018I]5usV\tA\f\u0005\u0002\u0012;&\u0011aL\u0005\u0002\u0004\u0013:$\bb\u00021\u0001\u0003\u0003%\t!Y\u0001\u000faJ|G-^2u\u000b2,W.\u001a8u)\t\u0011W\r\u0005\u0002\u0012G&\u0011AM\u0005\u0002\u0004\u0003:L\bb\u00024`\u0003\u0003\u0005\r\u0001X\u0001\u0004q\u0012\n\u0004b\u00025\u0001\u0003\u0003%\t%[\u0001\u0010aJ|G-^2u\u0013R,'/\u0019;peV\t!\u000eE\u0002l]\nl\u0011\u0001\u001c\u0006\u0003[J\t!bY8mY\u0016\u001cG/[8o\u0013\tyGN\u0001\u0005Ji\u0016\u0014\u0018\r^8s\u0011\u001d\t\b!!A\u0005\u0002I\f\u0001bY1o\u000bF,\u0018\r\u001c\u000b\u0003gZ\u0004\"!\u0005;\n\u0005U\u0014\"a\u0002\"p_2,\u0017M\u001c\u0005\bMB\f\t\u00111\u0001c\u0011\u001dA\b!!A\u0005Be\f\u0001\u0002[1tQ\u000e{G-\u001a\u000b\u00029\"91\u0010AA\u0001\n\u0003b\u0018\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003ECqA \u0001\u0002\u0002\u0013\u0005s0\u0001\u0004fcV\fGn\u001d\u000b\u0004g\u0006\u0005\u0001b\u00024~\u0003\u0003\u0005\rAY\u0004\n\u0003\u000b\u0011\u0011\u0011!E\u0001\u0003\u000f\tQb\u00117bgNLeNZ8UsB,\u0007cA\u0007\u0002\n\u0019A\u0011AAA\u0001\u0012\u0003\tYaE\u0003\u0002\n\u00055a\u0003E\u0004\u0002\u0010\u0005UA\u0004J\u001b\u000e\u0005\u0005E!bAA\n%\u00059!/\u001e8uS6,\u0017\u0002BA\f\u0003#\u0011\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c83\u0011\u001d\u0019\u0014\u0011\u0002C\u0001\u00037!\"!a\u0002\t\u0011m\fI!!A\u0005FqD!\"!\t\u0002\n\u0005\u0005I\u0011QA\u0012\u0003\u0015\t\u0007\u000f\u001d7z)\u0015)\u0014QEA\u0014\u0011\u0019Q\u0012q\u0004a\u00019!1!%a\bA\u0002\u0011B!\"a\u000b\u0002\n\u0005\u0005I\u0011QA\u0017\u0003\u001d)h.\u00199qYf$B!a\f\u0002<A)\u0011#!\r\u00026%\u0019\u00111\u0007\n\u0003\r=\u0003H/[8o!\u0015\t\u0012q\u0007\u000f%\u0013\r\tID\u0005\u0002\u0007)V\u0004H.\u001a\u001a\t\u0013\u0005u\u0012\u0011FA\u0001\u0002\u0004)\u0014a\u0001=%a!Q\u0011\u0011IA\u0005\u0003\u0003%I!a\u0011\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003\u000b\u00022AUA$\u0013\r\tIe\u0015\u0002\u0007\u001f\nTWm\u0019;")
public class ClassInfoType extends Type implements Product, Serializable
{
    private final Symbol symbol;
    private final Seq<Type> typeRefs;
    
    public static Option<Tuple2<Symbol, Seq<Type>>> unapply(final ClassInfoType x$0) {
        return ClassInfoType$.MODULE$.unapply(x$0);
    }
    
    public static ClassInfoType apply(final Symbol symbol, final Seq<Type> typeRefs) {
        return ClassInfoType$.MODULE$.apply(symbol, typeRefs);
    }
    
    public static Function1<Tuple2<Symbol, Seq<Type>>, ClassInfoType> tupled() {
        return (Function1<Tuple2<Symbol, Seq<Type>>, ClassInfoType>)ClassInfoType$.MODULE$.tupled();
    }
    
    public static Function1<Symbol, Function1<Seq<Type>, ClassInfoType>> curried() {
        return (Function1<Symbol, Function1<Seq<Type>, ClassInfoType>>)ClassInfoType$.MODULE$.curried();
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public Seq<Type> typeRefs() {
        return this.typeRefs;
    }
    
    public ClassInfoType copy(final Symbol symbol, final Seq<Type> typeRefs) {
        return new ClassInfoType(symbol, typeRefs);
    }
    
    public Symbol copy$default$1() {
        return this.symbol();
    }
    
    public Seq<Type> copy$default$2() {
        return this.typeRefs();
    }
    
    public String productPrefix() {
        return "ClassInfoType";
    }
    
    public int productArity() {
        return 2;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 1: {
                o = this.typeRefs();
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
        return x$1 instanceof ClassInfoType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ClassInfoType) {
                final ClassInfoType classInfoType = (ClassInfoType)x$1;
                final Symbol symbol = this.symbol();
                final Symbol symbol2 = classInfoType.symbol();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0108;
                        }
                        final Seq<Type> typeRefs = this.typeRefs();
                        final Seq<Type> typeRefs2 = classInfoType.typeRefs();
                        if (typeRefs == null) {
                            if (typeRefs2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!typeRefs.equals(typeRefs2)) {
                            break Label_0108;
                        }
                        if (classInfoType.canEqual(this)) {
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
    
    public ClassInfoType(final Symbol symbol, final Seq<Type> typeRefs) {
        this.symbol = symbol;
        this.typeRefs = typeRefs;
        Product$class.$init$((Product)this);
    }
}
