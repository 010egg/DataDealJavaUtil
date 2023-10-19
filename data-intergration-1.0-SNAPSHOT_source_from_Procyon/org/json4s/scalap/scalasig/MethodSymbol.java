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
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005Ub\u0001B\u0001\u0003\u0001.\u0011A\"T3uQ>$7+_7c_2T!a\u0001\u0003\u0002\u0011M\u001c\u0017\r\\1tS\u001eT!!\u0002\u0004\u0002\rM\u001c\u0017\r\\1q\u0015\t9\u0001\"\u0001\u0004kg>tGg\u001d\u0006\u0002\u0013\u0005\u0019qN]4\u0004\u0001M!\u0001\u0001\u0004\t\u0017!\tia\"D\u0001\u0003\u0013\ty!A\u0001\tTs6\u0014w\u000e\\%oM>\u001c\u00160\u001c2pYB\u0011\u0011\u0003F\u0007\u0002%)\t1#A\u0003tG\u0006d\u0017-\u0003\u0002\u0016%\t9\u0001K]8ek\u000e$\bCA\t\u0018\u0013\tA\"C\u0001\u0007TKJL\u0017\r\\5{C\ndW\r\u0003\u0005\u001b\u0001\tU\r\u0011\"\u0001\u001c\u0003)\u0019\u00180\u001c2pY&sgm\\\u000b\u00029A\u0011Q\"H\u0005\u0003=\t\u0011!bU=nE>d\u0017J\u001c4p\u0011!\u0001\u0003A!E!\u0002\u0013a\u0012aC:z[\n|G.\u00138g_\u0002B\u0001B\t\u0001\u0003\u0016\u0004%\taI\u0001\tC2L\u0017m\u001d*fMV\tA\u0005E\u0002\u0012K\u001dJ!A\n\n\u0003\r=\u0003H/[8o!\t\t\u0002&\u0003\u0002*%\t\u0019\u0011J\u001c;\t\u0011-\u0002!\u0011#Q\u0001\n\u0011\n\u0011\"\u00197jCN\u0014VM\u001a\u0011\t\u000b5\u0002A\u0011\u0001\u0018\u0002\rqJg.\u001b;?)\ry\u0003'\r\t\u0003\u001b\u0001AQA\u0007\u0017A\u0002qAQA\t\u0017A\u0002\u0011Bqa\r\u0001\u0002\u0002\u0013\u0005A'\u0001\u0003d_BLHcA\u00186m!9!D\rI\u0001\u0002\u0004a\u0002b\u0002\u00123!\u0003\u0005\r\u0001\n\u0005\bq\u0001\t\n\u0011\"\u0001:\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012A\u000f\u0016\u00039mZ\u0013\u0001\u0010\t\u0003{\tk\u0011A\u0010\u0006\u0003\u007f\u0001\u000b\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005\u0005\u0013\u0012AC1o]>$\u0018\r^5p]&\u00111I\u0010\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007bB#\u0001#\u0003%\tAR\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00133+\u00059%F\u0001\u0013<\u0011\u001dI\u0005!!A\u0005B)\u000bQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#A&\u0011\u00051\u000bV\"A'\u000b\u00059{\u0015\u0001\u00027b]\u001eT\u0011\u0001U\u0001\u0005U\u00064\u0018-\u0003\u0002S\u001b\n11\u000b\u001e:j]\u001eDq\u0001\u0016\u0001\u0002\u0002\u0013\u0005Q+\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001(\u0011\u001d9\u0006!!A\u0005\u0002a\u000ba\u0002\u001d:pIV\u001cG/\u00127f[\u0016tG\u000f\u0006\u0002Z9B\u0011\u0011CW\u0005\u00037J\u00111!\u00118z\u0011\u001dif+!AA\u0002\u001d\n1\u0001\u001f\u00132\u0011\u001dy\u0006!!A\u0005B\u0001\fq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002CB\u0019!-Z-\u000e\u0003\rT!\u0001\u001a\n\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002gG\nA\u0011\n^3sCR|'\u000fC\u0004i\u0001\u0005\u0005I\u0011A5\u0002\u0011\r\fg.R9vC2$\"A[7\u0011\u0005EY\u0017B\u00017\u0013\u0005\u001d\u0011un\u001c7fC:Dq!X4\u0002\u0002\u0003\u0007\u0011\fC\u0004p\u0001\u0005\u0005I\u0011\t9\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012a\n\u0005\be\u0002\t\t\u0011\"\u0011t\u0003!!xn\u0015;sS:<G#A&\t\u000fU\u0004\u0011\u0011!C!m\u00061Q-];bYN$\"A[<\t\u000fu#\u0018\u0011!a\u00013\u001e9\u0011PAA\u0001\u0012\u0003Q\u0018\u0001D'fi\"|GmU=nE>d\u0007CA\u0007|\r\u001d\t!!!A\t\u0002q\u001c2a_?\u0017!\u0019q\u00181\u0001\u000f%_5\tqPC\u0002\u0002\u0002I\tqA];oi&lW-C\u0002\u0002\u0006}\u0014\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c83\u0011\u0019i3\u0010\"\u0001\u0002\nQ\t!\u0010C\u0004sw\u0006\u0005IQI:\t\u0013\u0005=10!A\u0005\u0002\u0006E\u0011!B1qa2LH#B\u0018\u0002\u0014\u0005U\u0001B\u0002\u000e\u0002\u000e\u0001\u0007A\u0004\u0003\u0004#\u0003\u001b\u0001\r\u0001\n\u0005\n\u00033Y\u0018\u0011!CA\u00037\tq!\u001e8baBd\u0017\u0010\u0006\u0003\u0002\u001e\u0005\u0015\u0002\u0003B\t&\u0003?\u0001R!EA\u00119\u0011J1!a\t\u0013\u0005\u0019!V\u000f\u001d7fe!I\u0011qEA\f\u0003\u0003\u0005\raL\u0001\u0004q\u0012\u0002\u0004\"CA\u0016w\u0006\u0005I\u0011BA\u0017\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005=\u0002c\u0001'\u00022%\u0019\u00111G'\u0003\r=\u0013'.Z2u\u0001")
public class MethodSymbol extends SymbolInfoSymbol implements Product, Serializable
{
    private final SymbolInfo symbolInfo;
    private final Option<Object> aliasRef;
    
    public static Option<Tuple2<SymbolInfo, Option<Object>>> unapply(final MethodSymbol x$0) {
        return MethodSymbol$.MODULE$.unapply(x$0);
    }
    
    public static MethodSymbol apply(final SymbolInfo symbolInfo, final Option<Object> aliasRef) {
        return MethodSymbol$.MODULE$.apply(symbolInfo, aliasRef);
    }
    
    public static Function1<Tuple2<SymbolInfo, Option<Object>>, MethodSymbol> tupled() {
        return (Function1<Tuple2<SymbolInfo, Option<Object>>, MethodSymbol>)MethodSymbol$.MODULE$.tupled();
    }
    
    public static Function1<SymbolInfo, Function1<Option<Object>, MethodSymbol>> curried() {
        return (Function1<SymbolInfo, Function1<Option<Object>, MethodSymbol>>)MethodSymbol$.MODULE$.curried();
    }
    
    @Override
    public SymbolInfo symbolInfo() {
        return this.symbolInfo;
    }
    
    public Option<Object> aliasRef() {
        return this.aliasRef;
    }
    
    public MethodSymbol copy(final SymbolInfo symbolInfo, final Option<Object> aliasRef) {
        return new MethodSymbol(symbolInfo, aliasRef);
    }
    
    public SymbolInfo copy$default$1() {
        return this.symbolInfo();
    }
    
    public Option<Object> copy$default$2() {
        return this.aliasRef();
    }
    
    public String productPrefix() {
        return "MethodSymbol";
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
                o = this.aliasRef();
                break;
            }
            case 0: {
                o = this.symbolInfo();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof MethodSymbol;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof MethodSymbol) {
                final MethodSymbol methodSymbol = (MethodSymbol)x$1;
                final SymbolInfo symbolInfo = this.symbolInfo();
                final SymbolInfo symbolInfo2 = methodSymbol.symbolInfo();
                boolean b = false;
                Label_0109: {
                    Label_0108: {
                        if (symbolInfo == null) {
                            if (symbolInfo2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!symbolInfo.equals(symbolInfo2)) {
                            break Label_0108;
                        }
                        final Option<Object> aliasRef = this.aliasRef();
                        final Option<Object> aliasRef2 = methodSymbol.aliasRef();
                        if (aliasRef == null) {
                            if (aliasRef2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!aliasRef.equals(aliasRef2)) {
                            break Label_0108;
                        }
                        if (methodSymbol.canEqual(this)) {
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
    
    public MethodSymbol(final SymbolInfo symbolInfo, final Option<Object> aliasRef) {
        this.symbolInfo = symbolInfo;
        this.aliasRef = aliasRef;
        Product$class.$init$((Product)this);
    }
}
