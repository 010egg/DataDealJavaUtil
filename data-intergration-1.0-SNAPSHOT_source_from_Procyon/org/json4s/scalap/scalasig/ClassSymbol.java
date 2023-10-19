// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.runtime.BoxedUnit;
import scala.Function1;
import scala.Tuple2;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005\u001dc\u0001B\u0001\u0003\u0001.\u00111b\u00117bgN\u001c\u00160\u001c2pY*\u00111\u0001B\u0001\tg\u000e\fG.Y:jO*\u0011QAB\u0001\u0007g\u000e\fG.\u00199\u000b\u0005\u001dA\u0011A\u00026t_:$4OC\u0001\n\u0003\ry'oZ\u0002\u0001'\u0011\u0001A\u0002\u0005\f\u0011\u00055qQ\"\u0001\u0002\n\u0005=\u0011!\u0001E*z[\n|G.\u00138g_NKXNY8m!\t\tB#D\u0001\u0013\u0015\u0005\u0019\u0012!B:dC2\f\u0017BA\u000b\u0013\u0005\u001d\u0001&o\u001c3vGR\u0004\"!E\f\n\u0005a\u0011\"\u0001D*fe&\fG.\u001b>bE2,\u0007\u0002\u0003\u000e\u0001\u0005+\u0007I\u0011A\u000e\u0002\u0015MLXNY8m\u0013:4w.F\u0001\u001d!\tiQ$\u0003\u0002\u001f\u0005\tQ1+_7c_2LeNZ8\t\u0011\u0001\u0002!\u0011#Q\u0001\nq\t1b]=nE>d\u0017J\u001c4pA!A!\u0005\u0001BK\u0002\u0013\u00051%A\u0006uQ&\u001cH+\u001f9f%\u00164W#\u0001\u0013\u0011\u0007E)s%\u0003\u0002'%\t1q\n\u001d;j_:\u0004\"!\u0005\u0015\n\u0005%\u0012\"aA%oi\"A1\u0006\u0001B\tB\u0003%A%\u0001\u0007uQ&\u001cH+\u001f9f%\u00164\u0007\u0005C\u0003.\u0001\u0011\u0005a&\u0001\u0004=S:LGO\u0010\u000b\u0004_A\n\u0004CA\u0007\u0001\u0011\u0015QB\u00061\u0001\u001d\u0011\u0015\u0011C\u00061\u0001%\u0011!\u0019\u0004\u0001#b\u0001\n\u0003!\u0014\u0001C:fY\u001a$\u0016\u0010]3\u0016\u0003U\u00022!E\u00137!\tiq'\u0003\u00029\u0005\t!A+\u001f9f\u0011!Q\u0004\u0001#A!B\u0013)\u0014!C:fY\u001a$\u0016\u0010]3!\u0011\u001da\u0004!!A\u0005\u0002u\nAaY8qsR\u0019qFP \t\u000fiY\u0004\u0013!a\u00019!9!e\u000fI\u0001\u0002\u0004!\u0003bB!\u0001#\u0003%\tAQ\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00132+\u0005\u0019%F\u0001\u000fEW\u0005)\u0005C\u0001$L\u001b\u00059%B\u0001%J\u0003%)hn\u00195fG.,GM\u0003\u0002K%\u0005Q\u0011M\u001c8pi\u0006$\u0018n\u001c8\n\u00051;%!E;oG\",7m[3e-\u0006\u0014\u0018.\u00198dK\"9a\nAI\u0001\n\u0003y\u0015AD2paf$C-\u001a4bk2$HEM\u000b\u0002!*\u0012A\u0005\u0012\u0005\b%\u0002\t\t\u0011\"\u0011T\u00035\u0001(o\u001c3vGR\u0004&/\u001a4jqV\tA\u000b\u0005\u0002V56\taK\u0003\u0002X1\u0006!A.\u00198h\u0015\u0005I\u0016\u0001\u00026bm\u0006L!a\u0017,\u0003\rM#(/\u001b8h\u0011\u001di\u0006!!A\u0005\u0002y\u000bA\u0002\u001d:pIV\u001cG/\u0011:jif,\u0012a\n\u0005\bA\u0002\t\t\u0011\"\u0001b\u00039\u0001(o\u001c3vGR,E.Z7f]R$\"AY3\u0011\u0005E\u0019\u0017B\u00013\u0013\u0005\r\te.\u001f\u0005\bM~\u000b\t\u00111\u0001(\u0003\rAH%\r\u0005\bQ\u0002\t\t\u0011\"\u0011j\u0003=\u0001(o\u001c3vGRLE/\u001a:bi>\u0014X#\u00016\u0011\u0007-t'-D\u0001m\u0015\ti'#\u0001\u0006d_2dWm\u0019;j_:L!a\u001c7\u0003\u0011%#XM]1u_JDq!\u001d\u0001\u0002\u0002\u0013\u0005!/\u0001\u0005dC:,\u0015/^1m)\t\u0019h\u000f\u0005\u0002\u0012i&\u0011QO\u0005\u0002\b\u0005>|G.Z1o\u0011\u001d1\u0007/!AA\u0002\tDq\u0001\u001f\u0001\u0002\u0002\u0013\u0005\u00130\u0001\u0005iCND7i\u001c3f)\u00059\u0003bB>\u0001\u0003\u0003%\t\u0005`\u0001\ti>\u001cFO]5oOR\tA\u000bC\u0004\u007f\u0001\u0005\u0005I\u0011I@\u0002\r\u0015\fX/\u00197t)\r\u0019\u0018\u0011\u0001\u0005\bMv\f\t\u00111\u0001c\u000f%\t)AAA\u0001\u0012\u0003\t9!A\u0006DY\u0006\u001c8oU=nE>d\u0007cA\u0007\u0002\n\u0019A\u0011AAA\u0001\u0012\u0003\tYaE\u0003\u0002\n\u00055a\u0003E\u0004\u0002\u0010\u0005UA\u0004J\u0018\u000e\u0005\u0005E!bAA\n%\u00059!/\u001e8uS6,\u0017\u0002BA\f\u0003#\u0011\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c83\u0011\u001di\u0013\u0011\u0002C\u0001\u00037!\"!a\u0002\t\u0011m\fI!!A\u0005FqD!\"!\t\u0002\n\u0005\u0005I\u0011QA\u0012\u0003\u0015\t\u0007\u000f\u001d7z)\u0015y\u0013QEA\u0014\u0011\u0019Q\u0012q\u0004a\u00019!1!%a\bA\u0002\u0011B!\"a\u000b\u0002\n\u0005\u0005I\u0011QA\u0017\u0003\u001d)h.\u00199qYf$B!a\f\u00028A!\u0011#JA\u0019!\u0015\t\u00121\u0007\u000f%\u0013\r\t)D\u0005\u0002\u0007)V\u0004H.\u001a\u001a\t\u0013\u0005e\u0012\u0011FA\u0001\u0002\u0004y\u0013a\u0001=%a!Q\u0011QHA\u0005\u0003\u0003%I!a\u0010\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003\u0003\u00022!VA\"\u0013\r\t)E\u0016\u0002\u0007\u001f\nTWm\u0019;")
public class ClassSymbol extends SymbolInfoSymbol implements Product, Serializable
{
    private final SymbolInfo symbolInfo;
    private final Option<Object> thisTypeRef;
    private Option<Type> selfType;
    private volatile boolean bitmap$0;
    
    public static Option<Tuple2<SymbolInfo, Option<Object>>> unapply(final ClassSymbol x$0) {
        return ClassSymbol$.MODULE$.unapply(x$0);
    }
    
    public static ClassSymbol apply(final SymbolInfo symbolInfo, final Option<Object> thisTypeRef) {
        return ClassSymbol$.MODULE$.apply(symbolInfo, thisTypeRef);
    }
    
    public static Function1<Tuple2<SymbolInfo, Option<Object>>, ClassSymbol> tupled() {
        return (Function1<Tuple2<SymbolInfo, Option<Object>>, ClassSymbol>)ClassSymbol$.MODULE$.tupled();
    }
    
    public static Function1<SymbolInfo, Function1<Option<Object>, ClassSymbol>> curried() {
        return (Function1<SymbolInfo, Function1<Option<Object>, ClassSymbol>>)ClassSymbol$.MODULE$.curried();
    }
    
    private Option selfType$lzycompute() {
        synchronized (this) {
            if (!this.bitmap$0) {
                this.selfType = (Option<Type>)this.thisTypeRef().map((Function1)new ClassSymbol$$anonfun$selfType.ClassSymbol$$anonfun$selfType$1(this));
                this.bitmap$0 = true;
            }
            final BoxedUnit unit = BoxedUnit.UNIT;
            return this.selfType;
        }
    }
    
    @Override
    public SymbolInfo symbolInfo() {
        return this.symbolInfo;
    }
    
    public Option<Object> thisTypeRef() {
        return this.thisTypeRef;
    }
    
    public Option<Type> selfType() {
        return (Option<Type>)(this.bitmap$0 ? this.selfType : this.selfType$lzycompute());
    }
    
    public ClassSymbol copy(final SymbolInfo symbolInfo, final Option<Object> thisTypeRef) {
        return new ClassSymbol(symbolInfo, thisTypeRef);
    }
    
    public SymbolInfo copy$default$1() {
        return this.symbolInfo();
    }
    
    public Option<Object> copy$default$2() {
        return this.thisTypeRef();
    }
    
    public String productPrefix() {
        return "ClassSymbol";
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
                o = this.thisTypeRef();
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
        return x$1 instanceof ClassSymbol;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ClassSymbol) {
                final ClassSymbol classSymbol = (ClassSymbol)x$1;
                final SymbolInfo symbolInfo = this.symbolInfo();
                final SymbolInfo symbolInfo2 = classSymbol.symbolInfo();
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
                        final Option<Object> thisTypeRef = this.thisTypeRef();
                        final Option<Object> thisTypeRef2 = classSymbol.thisTypeRef();
                        if (thisTypeRef == null) {
                            if (thisTypeRef2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!thisTypeRef.equals(thisTypeRef2)) {
                            break Label_0108;
                        }
                        if (classSymbol.canEqual(this)) {
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
    
    public ClassSymbol(final SymbolInfo symbolInfo, final Option<Object> thisTypeRef) {
        this.symbolInfo = symbolInfo;
        this.thisTypeRef = thisTypeRef;
        Product$class.$init$((Product)this);
    }
}
