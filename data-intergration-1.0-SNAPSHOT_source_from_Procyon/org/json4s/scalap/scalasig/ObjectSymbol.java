// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ua\u0001B\u0001\u0003\u0001.\u0011Ab\u00142kK\u000e$8+_7c_2T!a\u0001\u0003\u0002\u0011M\u001c\u0017\r\\1tS\u001eT!!\u0002\u0004\u0002\rM\u001c\u0017\r\\1q\u0015\t9\u0001\"\u0001\u0004kg>tGg\u001d\u0006\u0002\u0013\u0005\u0019qN]4\u0004\u0001M!\u0001\u0001\u0004\t\u0017!\tia\"D\u0001\u0003\u0013\ty!A\u0001\tTs6\u0014w\u000e\\%oM>\u001c\u00160\u001c2pYB\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9\u0001K]8ek\u000e$\bCA\t\u0018\u0013\tA\"C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001b\u0001\tU\r\u0011\"\u0001\u001c\u0003)\u0019\u00180\u001c2pY&sgm\\\u000b\u00029A\u0011Q\"H\u0005\u0003=\t\u0011!bU=nE>d\u0017J\u001c4p\u0011!\u0001\u0003A!E!\u0002\u0013a\u0012aC:z[\n|G.\u00138g_\u0002BQA\t\u0001\u0005\u0002\r\na\u0001P5oSRtDC\u0001\u0013&!\ti\u0001\u0001C\u0003\u001bC\u0001\u0007A\u0004C\u0004(\u0001\u0005\u0005I\u0011\u0001\u0015\u0002\t\r|\u0007/\u001f\u000b\u0003I%BqA\u0007\u0014\u0011\u0002\u0003\u0007A\u0004C\u0004,\u0001E\u0005I\u0011\u0001\u0017\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tQF\u000b\u0002\u001d]-\nq\u0006\u0005\u00021k5\t\u0011G\u0003\u00023g\u0005IQO\\2iK\u000e\\W\r\u001a\u0006\u0003iI\t!\"\u00198o_R\fG/[8o\u0013\t1\u0014GA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq\u0001\u000f\u0001\u0002\u0002\u0013\u0005\u0013(A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002uA\u00111\bQ\u0007\u0002y)\u0011QHP\u0001\u0005Y\u0006twMC\u0001@\u0003\u0011Q\u0017M^1\n\u0005\u0005c$AB*ue&tw\rC\u0004D\u0001\u0005\u0005I\u0011\u0001#\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u0003\u0015\u0003\"!\u0005$\n\u0005\u001d\u0013\"aA%oi\"9\u0011\nAA\u0001\n\u0003Q\u0015A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003\u0017:\u0003\"!\u0005'\n\u00055\u0013\"aA!os\"9q\nSA\u0001\u0002\u0004)\u0015a\u0001=%c!9\u0011\u000bAA\u0001\n\u0003\u0012\u0016a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003M\u00032\u0001V,L\u001b\u0005)&B\u0001,\u0013\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u00031V\u0013\u0001\"\u0013;fe\u0006$xN\u001d\u0005\b5\u0002\t\t\u0011\"\u0001\\\u0003!\u0019\u0017M\\#rk\u0006dGC\u0001/`!\t\tR,\u0003\u0002_%\t9!i\\8mK\u0006t\u0007bB(Z\u0003\u0003\u0005\ra\u0013\u0005\bC\u0002\t\t\u0011\"\u0011c\u0003!A\u0017m\u001d5D_\u0012,G#A#\t\u000f\u0011\u0004\u0011\u0011!C!K\u0006AAo\\*ue&tw\rF\u0001;\u0011\u001d9\u0007!!A\u0005B!\fa!Z9vC2\u001cHC\u0001/j\u0011\u001dye-!AA\u0002-;qa\u001b\u0002\u0002\u0002#\u0005A.\u0001\u0007PE*,7\r^*z[\n|G\u000e\u0005\u0002\u000e[\u001a9\u0011AAA\u0001\u0012\u0003q7cA7p-A!\u0001o\u001d\u000f%\u001b\u0005\t(B\u0001:\u0013\u0003\u001d\u0011XO\u001c;j[\u0016L!\u0001^9\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t\u0017\u0007C\u0003#[\u0012\u0005a\u000fF\u0001m\u0011\u001d!W.!A\u0005F\u0015Dq!_7\u0002\u0002\u0013\u0005%0A\u0003baBd\u0017\u0010\u0006\u0002%w\")!\u0004\u001fa\u00019!9Q0\\A\u0001\n\u0003s\u0018aB;oCB\u0004H.\u001f\u000b\u0004\u007f\u0006\u0015\u0001\u0003B\t\u0002\u0002qI1!a\u0001\u0013\u0005\u0019y\u0005\u000f^5p]\"A\u0011q\u0001?\u0002\u0002\u0003\u0007A%A\u0002yIAB\u0011\"a\u0003n\u0003\u0003%I!!\u0004\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003\u001f\u00012aOA\t\u0013\r\t\u0019\u0002\u0010\u0002\u0007\u001f\nTWm\u0019;")
public class ObjectSymbol extends SymbolInfoSymbol implements Product, Serializable
{
    private final SymbolInfo symbolInfo;
    
    public static Option<SymbolInfo> unapply(final ObjectSymbol x$0) {
        return ObjectSymbol$.MODULE$.unapply(x$0);
    }
    
    public static ObjectSymbol apply(final SymbolInfo symbolInfo) {
        return ObjectSymbol$.MODULE$.apply(symbolInfo);
    }
    
    public static <A> Function1<SymbolInfo, A> andThen(final Function1<ObjectSymbol, A> function1) {
        return (Function1<SymbolInfo, A>)ObjectSymbol$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ObjectSymbol> compose(final Function1<A, SymbolInfo> function1) {
        return (Function1<A, ObjectSymbol>)ObjectSymbol$.MODULE$.compose((Function1)function1);
    }
    
    @Override
    public SymbolInfo symbolInfo() {
        return this.symbolInfo;
    }
    
    public ObjectSymbol copy(final SymbolInfo symbolInfo) {
        return new ObjectSymbol(symbolInfo);
    }
    
    public SymbolInfo copy$default$1() {
        return this.symbolInfo();
    }
    
    public String productPrefix() {
        return "ObjectSymbol";
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
                return this.symbolInfo();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ObjectSymbol;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ObjectSymbol) {
                final ObjectSymbol objectSymbol = (ObjectSymbol)x$1;
                final SymbolInfo symbolInfo = this.symbolInfo();
                final SymbolInfo symbolInfo2 = objectSymbol.symbolInfo();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (symbolInfo == null) {
                            if (symbolInfo2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!symbolInfo.equals(symbolInfo2)) {
                            break Label_0076;
                        }
                        if (objectSymbol.canEqual(this)) {
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
    
    public ObjectSymbol(final SymbolInfo symbolInfo) {
        this.symbolInfo = symbolInfo;
        Product$class.$init$((Product)this);
    }
}
