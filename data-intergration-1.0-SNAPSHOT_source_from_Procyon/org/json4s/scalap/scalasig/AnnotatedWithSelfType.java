// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple3;
import scala.Option;
import scala.collection.immutable.List;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\rd\u0001B\u0001\u0003\u0001.\u0011Q#\u00118o_R\fG/\u001a3XSRD7+\u001a7g)f\u0004XM\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u00011\u0003\u0002\u0001\r!Y\u0001\"!\u0004\b\u000e\u0003\tI!a\u0004\u0002\u0003\tQK\b/\u001a\t\u0003#Qi\u0011A\u0005\u0006\u0002'\u0005)1oY1mC&\u0011QC\u0005\u0002\b!J|G-^2u!\t\tr#\u0003\u0002\u0019%\ta1+\u001a:jC2L'0\u00192mK\"A!\u0004\u0001BK\u0002\u0013\u00051$A\u0004usB,'+\u001a4\u0016\u00031A\u0001\"\b\u0001\u0003\u0012\u0003\u0006I\u0001D\u0001\tif\u0004XMU3gA!Aq\u0004\u0001BK\u0002\u0013\u0005\u0001%\u0001\u0004ts6\u0014w\u000e\\\u000b\u0002CA\u0011QBI\u0005\u0003G\t\u0011aaU=nE>d\u0007\u0002C\u0013\u0001\u0005#\u0005\u000b\u0011B\u0011\u0002\u000fMLXNY8mA!Aq\u0005\u0001BK\u0002\u0013\u0005\u0001&\u0001\bbiR\u0014\u0018N\u0019+sK\u0016\u0014VMZ:\u0016\u0003%\u00022A\u000b\u001a6\u001d\tY\u0003G\u0004\u0002-_5\tQF\u0003\u0002/\u0015\u00051AH]8pizJ\u0011aE\u0005\u0003cI\tq\u0001]1dW\u0006<W-\u0003\u00024i\t!A*[:u\u0015\t\t$\u0003\u0005\u0002\u0012m%\u0011qG\u0005\u0002\u0004\u0013:$\b\u0002C\u001d\u0001\u0005#\u0005\u000b\u0011B\u0015\u0002\u001f\u0005$HO]5c)J,WMU3gg\u0002BQa\u000f\u0001\u0005\u0002q\na\u0001P5oSRtD\u0003B\u001f?\u007f\u0001\u0003\"!\u0004\u0001\t\u000biQ\u0004\u0019\u0001\u0007\t\u000b}Q\u0004\u0019A\u0011\t\u000b\u001dR\u0004\u0019A\u0015\t\u000f\t\u0003\u0011\u0011!C\u0001\u0007\u0006!1m\u001c9z)\u0011iD)\u0012$\t\u000fi\t\u0005\u0013!a\u0001\u0019!9q$\u0011I\u0001\u0002\u0004\t\u0003bB\u0014B!\u0003\u0005\r!\u000b\u0005\b\u0011\u0002\t\n\u0011\"\u0001J\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012A\u0013\u0016\u0003\u0019-[\u0013\u0001\u0014\t\u0003\u001bJk\u0011A\u0014\u0006\u0003\u001fB\u000b\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005E\u0013\u0012AC1o]>$\u0018\r^5p]&\u00111K\u0014\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB+\u0001#\u0003%\tAV\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00133+\u00059&FA\u0011L\u0011\u001dI\u0006!%A\u0005\u0002i\u000babY8qs\u0012\"WMZ1vYR$3'F\u0001\\U\tI3\nC\u0004^\u0001\u0005\u0005I\u0011\t0\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005y\u0006C\u00011f\u001b\u0005\t'B\u00012d\u0003\u0011a\u0017M\\4\u000b\u0003\u0011\fAA[1wC&\u0011a-\u0019\u0002\u0007'R\u0014\u0018N\\4\t\u000f!\u0004\u0011\u0011!C\u0001S\u0006a\u0001O]8ek\u000e$\u0018I]5usV\tQ\u0007C\u0004l\u0001\u0005\u0005I\u0011\u00017\u0002\u001dA\u0014x\u000eZ;di\u0016cW-\\3oiR\u0011Q\u000e\u001d\t\u0003#9L!a\u001c\n\u0003\u0007\u0005s\u0017\u0010C\u0004rU\u0006\u0005\t\u0019A\u001b\u0002\u0007a$\u0013\u0007C\u0004t\u0001\u0005\u0005I\u0011\t;\u0002\u001fA\u0014x\u000eZ;di&#XM]1u_J,\u0012!\u001e\t\u0004mflW\"A<\u000b\u0005a\u0014\u0012AC2pY2,7\r^5p]&\u0011!p\u001e\u0002\t\u0013R,'/\u0019;pe\"9A\u0010AA\u0001\n\u0003i\u0018\u0001C2b]\u0016\u000bX/\u00197\u0015\u0007y\f\u0019\u0001\u0005\u0002\u0012\u007f&\u0019\u0011\u0011\u0001\n\u0003\u000f\t{w\u000e\\3b]\"9\u0011o_A\u0001\u0002\u0004i\u0007\"CA\u0004\u0001\u0005\u0005I\u0011IA\u0005\u0003!A\u0017m\u001d5D_\u0012,G#A\u001b\t\u0013\u00055\u0001!!A\u0005B\u0005=\u0011\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003}C\u0011\"a\u0005\u0001\u0003\u0003%\t%!\u0006\u0002\r\u0015\fX/\u00197t)\rq\u0018q\u0003\u0005\tc\u0006E\u0011\u0011!a\u0001[\u001eI\u00111\u0004\u0002\u0002\u0002#\u0005\u0011QD\u0001\u0016\u0003:tw\u000e^1uK\u0012<\u0016\u000e\u001e5TK24G+\u001f9f!\ri\u0011q\u0004\u0004\t\u0003\t\t\t\u0011#\u0001\u0002\"M)\u0011qDA\u0012-AA\u0011QEA\u0016\u0019\u0005JS(\u0004\u0002\u0002()\u0019\u0011\u0011\u0006\n\u0002\u000fI,h\u000e^5nK&!\u0011QFA\u0014\u0005E\t%m\u001d;sC\u000e$h)\u001e8di&|gn\r\u0005\bw\u0005}A\u0011AA\u0019)\t\ti\u0002\u0003\u0006\u0002\u000e\u0005}\u0011\u0011!C#\u0003\u001fA!\"a\u000e\u0002 \u0005\u0005I\u0011QA\u001d\u0003\u0015\t\u0007\u000f\u001d7z)\u001di\u00141HA\u001f\u0003\u007fAaAGA\u001b\u0001\u0004a\u0001BB\u0010\u00026\u0001\u0007\u0011\u0005\u0003\u0004(\u0003k\u0001\r!\u000b\u0005\u000b\u0003\u0007\ny\"!A\u0005\u0002\u0006\u0015\u0013aB;oCB\u0004H.\u001f\u000b\u0005\u0003\u000f\n\u0019\u0006E\u0003\u0012\u0003\u0013\ni%C\u0002\u0002LI\u0011aa\u00149uS>t\u0007CB\t\u0002P1\t\u0013&C\u0002\u0002RI\u0011a\u0001V;qY\u0016\u001c\u0004\"CA+\u0003\u0003\n\t\u00111\u0001>\u0003\rAH\u0005\r\u0005\u000b\u00033\ny\"!A\u0005\n\u0005m\u0013a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!!\u0018\u0011\u0007\u0001\fy&C\u0002\u0002b\u0005\u0014aa\u00142kK\u000e$\b")
public class AnnotatedWithSelfType extends Type implements Product, Serializable
{
    private final Type typeRef;
    private final Symbol symbol;
    private final List<Object> attribTreeRefs;
    
    public static Option<Tuple3<Type, Symbol, List<Object>>> unapply(final AnnotatedWithSelfType x$0) {
        return AnnotatedWithSelfType$.MODULE$.unapply(x$0);
    }
    
    public static AnnotatedWithSelfType apply(final Type typeRef, final Symbol symbol, final List<Object> attribTreeRefs) {
        return AnnotatedWithSelfType$.MODULE$.apply(typeRef, symbol, attribTreeRefs);
    }
    
    public static Function1<Tuple3<Type, Symbol, List<Object>>, AnnotatedWithSelfType> tupled() {
        return (Function1<Tuple3<Type, Symbol, List<Object>>, AnnotatedWithSelfType>)AnnotatedWithSelfType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Symbol, Function1<List<Object>, AnnotatedWithSelfType>>> curried() {
        return (Function1<Type, Function1<Symbol, Function1<List<Object>, AnnotatedWithSelfType>>>)AnnotatedWithSelfType$.MODULE$.curried();
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public List<Object> attribTreeRefs() {
        return this.attribTreeRefs;
    }
    
    public AnnotatedWithSelfType copy(final Type typeRef, final Symbol symbol, final List<Object> attribTreeRefs) {
        return new AnnotatedWithSelfType(typeRef, symbol, attribTreeRefs);
    }
    
    public Type copy$default$1() {
        return this.typeRef();
    }
    
    public Symbol copy$default$2() {
        return this.symbol();
    }
    
    public List<Object> copy$default$3() {
        return this.attribTreeRefs();
    }
    
    public String productPrefix() {
        return "AnnotatedWithSelfType";
    }
    
    public int productArity() {
        return 3;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 2: {
                o = this.attribTreeRefs();
                break;
            }
            case 1: {
                o = this.symbol();
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
        return x$1 instanceof AnnotatedWithSelfType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof AnnotatedWithSelfType) {
                final AnnotatedWithSelfType annotatedWithSelfType = (AnnotatedWithSelfType)x$1;
                final Type typeRef = this.typeRef();
                final Type typeRef2 = annotatedWithSelfType.typeRef();
                boolean b = false;
                Label_0141: {
                    Label_0140: {
                        if (typeRef == null) {
                            if (typeRef2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!typeRef.equals(typeRef2)) {
                            break Label_0140;
                        }
                        final Symbol symbol = this.symbol();
                        final Symbol symbol2 = annotatedWithSelfType.symbol();
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0140;
                        }
                        final List<Object> attribTreeRefs = this.attribTreeRefs();
                        final List<Object> attribTreeRefs2 = annotatedWithSelfType.attribTreeRefs();
                        if (attribTreeRefs == null) {
                            if (attribTreeRefs2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!attribTreeRefs.equals(attribTreeRefs2)) {
                            break Label_0140;
                        }
                        if (annotatedWithSelfType.canEqual(this)) {
                            b = true;
                            break Label_0141;
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
    
    public AnnotatedWithSelfType(final Type typeRef, final Symbol symbol, final List<Object> attribTreeRefs) {
        this.typeRef = typeRef;
        this.symbol = symbol;
        this.attribTreeRefs = attribTreeRefs;
        Product$class.$init$((Product)this);
    }
}
