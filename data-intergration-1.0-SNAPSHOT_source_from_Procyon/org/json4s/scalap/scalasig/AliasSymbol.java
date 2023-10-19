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

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001db\u0001B\u0001\u0003\u0001.\u00111\"\u00117jCN\u001c\u00160\u001c2pY*\u00111\u0001B\u0001\tg\u000e\fG.Y:jO*\u0011QAB\u0001\u0007g\u000e\fG.\u00199\u000b\u0005\u001dA\u0011A\u00026t_:$4OC\u0001\n\u0003\ry'oZ\u0002\u0001'\u0011\u0001A\u0002\u0005\f\u0011\u00055qQ\"\u0001\u0002\n\u0005=\u0011!\u0001E*z[\n|G.\u00138g_NKXNY8m!\t\tB#D\u0001\u0013\u0015\u0005\u0019\u0012!B:dC2\f\u0017BA\u000b\u0013\u0005\u001d\u0001&o\u001c3vGR\u0004\"!E\f\n\u0005a\u0011\"\u0001D*fe&\fG.\u001b>bE2,\u0007\u0002\u0003\u000e\u0001\u0005+\u0007I\u0011A\u000e\u0002\u0015MLXNY8m\u0013:4w.F\u0001\u001d!\tiQ$\u0003\u0002\u001f\u0005\tQ1+_7c_2LeNZ8\t\u0011\u0001\u0002!\u0011#Q\u0001\nq\t1b]=nE>d\u0017J\u001c4pA!)!\u0005\u0001C\u0001G\u00051A(\u001b8jiz\"\"\u0001J\u0013\u0011\u00055\u0001\u0001\"\u0002\u000e\"\u0001\u0004a\u0002\"B\u0014\u0001\t\u0003B\u0013\u0001\u00029bi\",\u0012!\u000b\t\u0003U5r!!E\u0016\n\u00051\u0012\u0012A\u0002)sK\u0012,g-\u0003\u0002/_\t11\u000b\u001e:j]\u001eT!\u0001\f\n\t\u000fE\u0002\u0011\u0011!C\u0001e\u0005!1m\u001c9z)\t!3\u0007C\u0004\u001baA\u0005\t\u0019\u0001\u000f\t\u000fU\u0002\u0011\u0013!C\u0001m\u0005q1m\u001c9zI\u0011,g-Y;mi\u0012\nT#A\u001c+\u0005qA4&A\u001d\u0011\u0005izT\"A\u001e\u000b\u0005qj\u0014!C;oG\",7m[3e\u0015\tq$#\u0001\u0006b]:|G/\u0019;j_:L!\u0001Q\u001e\u0003#Ut7\r[3dW\u0016$g+\u0019:jC:\u001cW\rC\u0004C\u0001\u0005\u0005I\u0011I\"\u0002\u001bA\u0014x\u000eZ;diB\u0013XMZ5y+\u0005!\u0005CA#K\u001b\u00051%BA$I\u0003\u0011a\u0017M\\4\u000b\u0003%\u000bAA[1wC&\u0011aF\u0012\u0005\b\u0019\u0002\t\t\u0011\"\u0001N\u00031\u0001(o\u001c3vGR\f%/\u001b;z+\u0005q\u0005CA\tP\u0013\t\u0001&CA\u0002J]RDqA\u0015\u0001\u0002\u0002\u0013\u00051+\u0001\bqe>$Wo\u0019;FY\u0016lWM\u001c;\u0015\u0005Q;\u0006CA\tV\u0013\t1&CA\u0002B]fDq\u0001W)\u0002\u0002\u0003\u0007a*A\u0002yIEBqA\u0017\u0001\u0002\u0002\u0013\u00053,A\bqe>$Wo\u0019;Ji\u0016\u0014\u0018\r^8s+\u0005a\u0006cA/a)6\taL\u0003\u0002`%\u0005Q1m\u001c7mK\u000e$\u0018n\u001c8\n\u0005\u0005t&\u0001C%uKJ\fGo\u001c:\t\u000f\r\u0004\u0011\u0011!C\u0001I\u0006A1-\u00198FcV\fG\u000e\u0006\u0002fQB\u0011\u0011CZ\u0005\u0003OJ\u0011qAQ8pY\u0016\fg\u000eC\u0004YE\u0006\u0005\t\u0019\u0001+\t\u000f)\u0004\u0011\u0011!C!W\u0006A\u0001.Y:i\u0007>$W\rF\u0001O\u0011\u001di\u0007!!A\u0005B9\f\u0001\u0002^8TiJLgn\u001a\u000b\u0002\t\"9\u0001\u000fAA\u0001\n\u0003\n\u0018AB3rk\u0006d7\u000f\u0006\u0002fe\"9\u0001l\\A\u0001\u0002\u0004!va\u0002;\u0003\u0003\u0003E\t!^\u0001\f\u00032L\u0017m]*z[\n|G\u000e\u0005\u0002\u000em\u001a9\u0011AAA\u0001\u0012\u000398c\u0001<y-A!\u0011\u0010 \u000f%\u001b\u0005Q(BA>\u0013\u0003\u001d\u0011XO\u001c;j[\u0016L!! >\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t\u0017\u0007C\u0003#m\u0012\u0005q\u0010F\u0001v\u0011\u001dig/!A\u0005F9D\u0011\"!\u0002w\u0003\u0003%\t)a\u0002\u0002\u000b\u0005\u0004\b\u000f\\=\u0015\u0007\u0011\nI\u0001\u0003\u0004\u001b\u0003\u0007\u0001\r\u0001\b\u0005\n\u0003\u001b1\u0018\u0011!CA\u0003\u001f\tq!\u001e8baBd\u0017\u0010\u0006\u0003\u0002\u0012\u0005]\u0001\u0003B\t\u0002\u0014qI1!!\u0006\u0013\u0005\u0019y\u0005\u000f^5p]\"I\u0011\u0011DA\u0006\u0003\u0003\u0005\r\u0001J\u0001\u0004q\u0012\u0002\u0004\"CA\u000fm\u0006\u0005I\u0011BA\u0010\u0003-\u0011X-\u00193SKN|GN^3\u0015\u0005\u0005\u0005\u0002cA#\u0002$%\u0019\u0011Q\u0005$\u0003\r=\u0013'.Z2u\u0001")
public class AliasSymbol extends SymbolInfoSymbol implements Product, Serializable
{
    private final SymbolInfo symbolInfo;
    
    public static Option<SymbolInfo> unapply(final AliasSymbol x$0) {
        return AliasSymbol$.MODULE$.unapply(x$0);
    }
    
    public static AliasSymbol apply(final SymbolInfo symbolInfo) {
        return AliasSymbol$.MODULE$.apply(symbolInfo);
    }
    
    public static <A> Function1<SymbolInfo, A> andThen(final Function1<AliasSymbol, A> function1) {
        return (Function1<SymbolInfo, A>)AliasSymbol$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, AliasSymbol> compose(final Function1<A, SymbolInfo> function1) {
        return (Function1<A, AliasSymbol>)AliasSymbol$.MODULE$.compose((Function1)function1);
    }
    
    @Override
    public SymbolInfo symbolInfo() {
        return this.symbolInfo;
    }
    
    public String path() {
        return this.name();
    }
    
    public AliasSymbol copy(final SymbolInfo symbolInfo) {
        return new AliasSymbol(symbolInfo);
    }
    
    public SymbolInfo copy$default$1() {
        return this.symbolInfo();
    }
    
    public String productPrefix() {
        return "AliasSymbol";
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
        return x$1 instanceof AliasSymbol;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof AliasSymbol) {
                final AliasSymbol aliasSymbol = (AliasSymbol)x$1;
                final SymbolInfo symbolInfo = this.symbolInfo();
                final SymbolInfo symbolInfo2 = aliasSymbol.symbolInfo();
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
                        if (aliasSymbol.canEqual(this)) {
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
    
    public AliasSymbol(final SymbolInfo symbolInfo) {
        this.symbolInfo = symbolInfo;
        Product$class.$init$((Product)this);
    }
}
