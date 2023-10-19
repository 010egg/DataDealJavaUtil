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
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u0015c\u0001B\u0001\u0003\u0001.\u0011Q\"\u00118o_R\fG/\u001a3UsB,'BA\u0002\u0005\u0003!\u00198-\u00197bg&<'BA\u0003\u0007\u0003\u0019\u00198-\u00197ba*\u0011q\u0001C\u0001\u0007UN|g\u000eN:\u000b\u0003%\t1a\u001c:h\u0007\u0001\u0019B\u0001\u0001\u0007\u0011-A\u0011QBD\u0007\u0002\u0005%\u0011qB\u0001\u0002\u0005)f\u0004X\r\u0005\u0002\u0012)5\t!CC\u0001\u0014\u0003\u0015\u00198-\u00197b\u0013\t)\"CA\u0004Qe>$Wo\u0019;\u0011\u0005E9\u0012B\u0001\r\u0013\u00051\u0019VM]5bY&T\u0018M\u00197f\u0011!Q\u0002A!f\u0001\n\u0003Y\u0012a\u0002;za\u0016\u0014VMZ\u000b\u0002\u0019!AQ\u0004\u0001B\tB\u0003%A\"\u0001\u0005usB,'+\u001a4!\u0011!y\u0002A!f\u0001\n\u0003\u0001\u0013AD1uiJL'\r\u0016:fKJ+gm]\u000b\u0002CA\u0019!EK\u0017\u000f\u0005\rBcB\u0001\u0013(\u001b\u0005)#B\u0001\u0014\u000b\u0003\u0019a$o\\8u}%\t1#\u0003\u0002*%\u00059\u0001/Y2lC\u001e,\u0017BA\u0016-\u0005\u0011a\u0015n\u001d;\u000b\u0005%\u0012\u0002CA\t/\u0013\ty#CA\u0002J]RD\u0001\"\r\u0001\u0003\u0012\u0003\u0006I!I\u0001\u0010CR$(/\u001b2Ue\u0016,'+\u001a4tA!)1\u0007\u0001C\u0001i\u00051A(\u001b8jiz\"2!\u000e\u001c8!\ti\u0001\u0001C\u0003\u001be\u0001\u0007A\u0002C\u0003 e\u0001\u0007\u0011\u0005C\u0004:\u0001\u0005\u0005I\u0011\u0001\u001e\u0002\t\r|\u0007/\u001f\u000b\u0004kmb\u0004b\u0002\u000e9!\u0003\u0005\r\u0001\u0004\u0005\b?a\u0002\n\u00111\u0001\"\u0011\u001dq\u0004!%A\u0005\u0002}\nabY8qs\u0012\"WMZ1vYR$\u0013'F\u0001AU\ta\u0011iK\u0001C!\t\u0019\u0005*D\u0001E\u0015\t)e)A\u0005v]\u000eDWmY6fI*\u0011qIE\u0001\u000bC:tw\u000e^1uS>t\u0017BA%E\u0005E)hn\u00195fG.,GMV1sS\u0006t7-\u001a\u0005\b\u0017\u0002\t\n\u0011\"\u0001M\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uII*\u0012!\u0014\u0016\u0003C\u0005Cqa\u0014\u0001\u0002\u0002\u0013\u0005\u0003+A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002#B\u0011!kV\u0007\u0002'*\u0011A+V\u0001\u0005Y\u0006twMC\u0001W\u0003\u0011Q\u0017M^1\n\u0005a\u001b&AB*ue&tw\rC\u0004[\u0001\u0005\u0005I\u0011A.\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u00035Bq!\u0018\u0001\u0002\u0002\u0013\u0005a,\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005}\u0013\u0007CA\ta\u0013\t\t'CA\u0002B]fDqa\u0019/\u0002\u0002\u0003\u0007Q&A\u0002yIEBq!\u001a\u0001\u0002\u0002\u0013\u0005c-A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u00059\u0007c\u00015l?6\t\u0011N\u0003\u0002k%\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u00051L'\u0001C%uKJ\fGo\u001c:\t\u000f9\u0004\u0011\u0011!C\u0001_\u0006A1-\u00198FcV\fG\u000e\u0006\u0002qgB\u0011\u0011#]\u0005\u0003eJ\u0011qAQ8pY\u0016\fg\u000eC\u0004d[\u0006\u0005\t\u0019A0\t\u000fU\u0004\u0011\u0011!C!m\u0006A\u0001.Y:i\u0007>$W\rF\u0001.\u0011\u001dA\b!!A\u0005Be\f\u0001\u0002^8TiJLgn\u001a\u000b\u0002#\"91\u0010AA\u0001\n\u0003b\u0018AB3rk\u0006d7\u000f\u0006\u0002q{\"91M_A\u0001\u0002\u0004yv\u0001C@\u0003\u0003\u0003E\t!!\u0001\u0002\u001b\u0005sgn\u001c;bi\u0016$G+\u001f9f!\ri\u00111\u0001\u0004\t\u0003\t\t\t\u0011#\u0001\u0002\u0006M)\u00111AA\u0004-A9\u0011\u0011BA\b\u0019\u0005*TBAA\u0006\u0015\r\tiAE\u0001\beVtG/[7f\u0013\u0011\t\t\"a\u0003\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t'\u0007C\u00044\u0003\u0007!\t!!\u0006\u0015\u0005\u0005\u0005\u0001\u0002\u0003=\u0002\u0004\u0005\u0005IQI=\t\u0015\u0005m\u00111AA\u0001\n\u0003\u000bi\"A\u0003baBd\u0017\u0010F\u00036\u0003?\t\t\u0003\u0003\u0004\u001b\u00033\u0001\r\u0001\u0004\u0005\u0007?\u0005e\u0001\u0019A\u0011\t\u0015\u0005\u0015\u00121AA\u0001\n\u0003\u000b9#A\u0004v]\u0006\u0004\b\u000f\\=\u0015\t\u0005%\u0012Q\u0007\t\u0006#\u0005-\u0012qF\u0005\u0004\u0003[\u0011\"AB(qi&|g\u000eE\u0003\u0012\u0003ca\u0011%C\u0002\u00024I\u0011a\u0001V;qY\u0016\u0014\u0004\"CA\u001c\u0003G\t\t\u00111\u00016\u0003\rAH\u0005\r\u0005\u000b\u0003w\t\u0019!!A\u0005\n\u0005u\u0012a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!a\u0010\u0011\u0007I\u000b\t%C\u0002\u0002DM\u0013aa\u00142kK\u000e$\b")
public class AnnotatedType extends Type implements Product, Serializable
{
    private final Type typeRef;
    private final List<Object> attribTreeRefs;
    
    public static Option<Tuple2<Type, List<Object>>> unapply(final AnnotatedType x$0) {
        return AnnotatedType$.MODULE$.unapply(x$0);
    }
    
    public static AnnotatedType apply(final Type typeRef, final List<Object> attribTreeRefs) {
        return AnnotatedType$.MODULE$.apply(typeRef, attribTreeRefs);
    }
    
    public static Function1<Tuple2<Type, List<Object>>, AnnotatedType> tupled() {
        return (Function1<Tuple2<Type, List<Object>>, AnnotatedType>)AnnotatedType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<List<Object>, AnnotatedType>> curried() {
        return (Function1<Type, Function1<List<Object>, AnnotatedType>>)AnnotatedType$.MODULE$.curried();
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public List<Object> attribTreeRefs() {
        return this.attribTreeRefs;
    }
    
    public AnnotatedType copy(final Type typeRef, final List<Object> attribTreeRefs) {
        return new AnnotatedType(typeRef, attribTreeRefs);
    }
    
    public Type copy$default$1() {
        return this.typeRef();
    }
    
    public List<Object> copy$default$2() {
        return this.attribTreeRefs();
    }
    
    public String productPrefix() {
        return "AnnotatedType";
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
                o = this.attribTreeRefs();
                break;
            }
            case 0: {
                o = this.typeRef();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof AnnotatedType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof AnnotatedType) {
                final AnnotatedType annotatedType = (AnnotatedType)x$1;
                final Type typeRef = this.typeRef();
                final Type typeRef2 = annotatedType.typeRef();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (typeRef == null) {
                            if (typeRef2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!typeRef.equals(typeRef2)) {
                            break Label_0108;
                        }
                        final List<Object> attribTreeRefs = this.attribTreeRefs();
                        final List<Object> attribTreeRefs2 = annotatedType.attribTreeRefs();
                        if (attribTreeRefs == null) {
                            if (attribTreeRefs2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!attribTreeRefs.equals(attribTreeRefs2)) {
                            break Label_0108;
                        }
                        if (annotatedType.canEqual(this)) {
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
    
    public AnnotatedType(final Type typeRef, final List<Object> attribTreeRefs) {
        this.typeRef = typeRef;
        this.attribTreeRefs = attribTreeRefs;
        Product$class.$init$((Product)this);
    }
}
