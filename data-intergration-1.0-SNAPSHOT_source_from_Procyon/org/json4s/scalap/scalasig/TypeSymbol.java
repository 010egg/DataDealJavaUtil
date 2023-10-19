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

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001.\u0011!\u0002V=qKNKXNY8m\u0015\t\u0019A!\u0001\u0005tG\u0006d\u0017m]5h\u0015\t)a!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000f!\taA[:p]R\u001a(\"A\u0005\u0002\u0007=\u0014xm\u0001\u0001\u0014\t\u0001a\u0001C\u0006\t\u0003\u001b9i\u0011AA\u0005\u0003\u001f\t\u0011\u0001cU=nE>d\u0017J\u001c4p'fl'm\u001c7\u0011\u0005E!R\"\u0001\n\u000b\u0003M\tQa]2bY\u0006L!!\u0006\n\u0003\u000fA\u0013x\u000eZ;diB\u0011\u0011cF\u0005\u00031I\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001B\u0007\u0001\u0003\u0016\u0004%\taG\u0001\u000bgfl'm\u001c7J]\u001a|W#\u0001\u000f\u0011\u00055i\u0012B\u0001\u0010\u0003\u0005)\u0019\u00160\u001c2pY&sgm\u001c\u0005\tA\u0001\u0011\t\u0012)A\u00059\u0005Y1/_7c_2LeNZ8!\u0011\u0015\u0011\u0003\u0001\"\u0001$\u0003\u0019a\u0014N\\5u}Q\u0011A%\n\t\u0003\u001b\u0001AQAG\u0011A\u0002qAQa\n\u0001\u0005B!\nA\u0001]1uQV\t\u0011\u0006\u0005\u0002+[9\u0011\u0011cK\u0005\u0003YI\ta\u0001\u0015:fI\u00164\u0017B\u0001\u00180\u0005\u0019\u0019FO]5oO*\u0011AF\u0005\u0005\bc\u0001\t\t\u0011\"\u00013\u0003\u0011\u0019w\u000e]=\u0015\u0005\u0011\u001a\u0004b\u0002\u000e1!\u0003\u0005\r\u0001\b\u0005\bk\u0001\t\n\u0011\"\u00017\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012a\u000e\u0016\u00039aZ\u0013!\u000f\t\u0003u}j\u0011a\u000f\u0006\u0003yu\n\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005y\u0012\u0012AC1o]>$\u0018\r^5p]&\u0011\u0001i\u000f\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007b\u0002\"\u0001\u0003\u0003%\teQ\u0001\u000eaJ|G-^2u!J,g-\u001b=\u0016\u0003\u0011\u0003\"!\u0012&\u000e\u0003\u0019S!a\u0012%\u0002\t1\fgn\u001a\u0006\u0002\u0013\u0006!!.\u0019<b\u0013\tqc\tC\u0004M\u0001\u0005\u0005I\u0011A'\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u00039\u0003\"!E(\n\u0005A\u0013\"aA%oi\"9!\u000bAA\u0001\n\u0003\u0019\u0016A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003)^\u0003\"!E+\n\u0005Y\u0013\"aA!os\"9\u0001,UA\u0001\u0002\u0004q\u0015a\u0001=%c!9!\fAA\u0001\n\u0003Z\u0016a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003q\u00032!\u00181U\u001b\u0005q&BA0\u0013\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u0003Cz\u0013\u0001\"\u0013;fe\u0006$xN\u001d\u0005\bG\u0002\t\t\u0011\"\u0001e\u0003!\u0019\u0017M\\#rk\u0006dGCA3i!\t\tb-\u0003\u0002h%\t9!i\\8mK\u0006t\u0007b\u0002-c\u0003\u0003\u0005\r\u0001\u0016\u0005\bU\u0002\t\t\u0011\"\u0011l\u0003!A\u0017m\u001d5D_\u0012,G#\u0001(\t\u000f5\u0004\u0011\u0011!C!]\u0006AAo\\*ue&tw\rF\u0001E\u0011\u001d\u0001\b!!A\u0005BE\fa!Z9vC2\u001cHCA3s\u0011\u001dAv.!AA\u0002Q;q\u0001\u001e\u0002\u0002\u0002#\u0005Q/\u0001\u0006UsB,7+_7c_2\u0004\"!\u0004<\u0007\u000f\u0005\u0011\u0011\u0011!E\u0001oN\u0019a\u000f\u001f\f\u0011\tedH\u0004J\u0007\u0002u*\u00111PE\u0001\beVtG/[7f\u0013\ti(PA\tBEN$(/Y2u\rVt7\r^5p]FBQA\t<\u0005\u0002}$\u0012!\u001e\u0005\b[Z\f\t\u0011\"\u0012o\u0011%\t)A^A\u0001\n\u0003\u000b9!A\u0003baBd\u0017\u0010F\u0002%\u0003\u0013AaAGA\u0002\u0001\u0004a\u0002\"CA\u0007m\u0006\u0005I\u0011QA\b\u0003\u001d)h.\u00199qYf$B!!\u0005\u0002\u0018A!\u0011#a\u0005\u001d\u0013\r\t)B\u0005\u0002\u0007\u001fB$\u0018n\u001c8\t\u0013\u0005e\u00111BA\u0001\u0002\u0004!\u0013a\u0001=%a!I\u0011Q\u0004<\u0002\u0002\u0013%\u0011qD\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002\"A\u0019Q)a\t\n\u0007\u0005\u0015bI\u0001\u0004PE*,7\r\u001e")
public class TypeSymbol extends SymbolInfoSymbol implements Product, Serializable
{
    private final SymbolInfo symbolInfo;
    
    public static Option<SymbolInfo> unapply(final TypeSymbol x$0) {
        return TypeSymbol$.MODULE$.unapply(x$0);
    }
    
    public static TypeSymbol apply(final SymbolInfo symbolInfo) {
        return TypeSymbol$.MODULE$.apply(symbolInfo);
    }
    
    public static <A> Function1<SymbolInfo, A> andThen(final Function1<TypeSymbol, A> function1) {
        return (Function1<SymbolInfo, A>)TypeSymbol$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, TypeSymbol> compose(final Function1<A, SymbolInfo> function1) {
        return (Function1<A, TypeSymbol>)TypeSymbol$.MODULE$.compose((Function1)function1);
    }
    
    @Override
    public SymbolInfo symbolInfo() {
        return this.symbolInfo;
    }
    
    public String path() {
        return this.name();
    }
    
    public TypeSymbol copy(final SymbolInfo symbolInfo) {
        return new TypeSymbol(symbolInfo);
    }
    
    public SymbolInfo copy$default$1() {
        return this.symbolInfo();
    }
    
    public String productPrefix() {
        return "TypeSymbol";
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
        return x$1 instanceof TypeSymbol;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof TypeSymbol) {
                final TypeSymbol typeSymbol = (TypeSymbol)x$1;
                final SymbolInfo symbolInfo = this.symbolInfo();
                final SymbolInfo symbolInfo2 = typeSymbol.symbolInfo();
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
                        if (typeSymbol.canEqual(this)) {
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
    
    public TypeSymbol(final SymbolInfo symbolInfo) {
        this.symbolInfo = symbolInfo;
        Product$class.$init$((Product)this);
    }
}
