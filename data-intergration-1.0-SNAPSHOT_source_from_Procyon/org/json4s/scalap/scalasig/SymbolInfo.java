// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.MatchError;
import scala.None$;
import scala.Some;
import scala.Predef$;
import scala.runtime.RichInt$;
import scala.collection.mutable.StringBuilder;
import scala.runtime.BoxesRunTime;
import scala.Function1;
import scala.Tuple6;
import scala.Option;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005=f\u0001B\u0001\u0003\u0001.\u0011!bU=nE>d\u0017J\u001c4p\u0015\t\u0019A!\u0001\u0005tG\u0006d\u0017m]5h\u0015\t)a!\u0001\u0004tG\u0006d\u0017\r\u001d\u0006\u0003\u000f!\taA[:p]R\u001a(\"A\u0005\u0002\u0007=\u0014xm\u0001\u0001\u0014\t\u0001a!#\u0006\t\u0003\u001bAi\u0011A\u0004\u0006\u0002\u001f\u0005)1oY1mC&\u0011\u0011C\u0004\u0002\u0007\u0003:L(+\u001a4\u0011\u00055\u0019\u0012B\u0001\u000b\u000f\u0005\u001d\u0001&o\u001c3vGR\u0004\"!\u0004\f\n\u0005]q!\u0001D*fe&\fG.\u001b>bE2,\u0007\u0002C\r\u0001\u0005+\u0007I\u0011\u0001\u000e\u0002\t9\fW.Z\u000b\u00027A\u0011Ad\b\b\u0003\u001buI!A\b\b\u0002\rA\u0013X\rZ3g\u0013\t\u0001\u0013E\u0001\u0004TiJLgn\u001a\u0006\u0003=9A\u0001b\t\u0001\u0003\u0012\u0003\u0006IaG\u0001\u0006]\u0006lW\r\t\u0005\tK\u0001\u0011)\u001a!C\u0001M\u0005)qn\u001e8feV\tq\u0005\u0005\u0002)S5\t!!\u0003\u0002+\u0005\t11+_7c_2D\u0001\u0002\f\u0001\u0003\u0012\u0003\u0006IaJ\u0001\u0007_^tWM\u001d\u0011\t\u00119\u0002!Q3A\u0005\u0002=\nQA\u001a7bON,\u0012\u0001\r\t\u0003\u001bEJ!A\r\b\u0003\u0007%sG\u000f\u0003\u00055\u0001\tE\t\u0015!\u00031\u0003\u00191G.Y4tA!Aa\u0007\u0001BK\u0002\u0013\u0005q'A\u0007qe&4\u0018\r^3XSRD\u0017N\\\u000b\u0002qA\u0019Q\"\u000f\u0007\n\u0005ir!AB(qi&|g\u000e\u0003\u0005=\u0001\tE\t\u0015!\u00039\u00039\u0001(/\u001b<bi\u0016<\u0016\u000e\u001e5j]\u0002B\u0001B\u0010\u0001\u0003\u0016\u0004%\taL\u0001\u0005S:4w\u000e\u0003\u0005A\u0001\tE\t\u0015!\u00031\u0003\u0015IgNZ8!\u0011!\u0011\u0005A!f\u0001\n\u0003\u0019\u0015!B3oiJLX#\u0001#\u0011\u0005\u0015C\u0005C\u0001\u0015G\u0013\t9%A\u0001\u0005TG\u0006d\u0017mU5h\u0013\tIeIA\u0003F]R\u0014\u0018\u0010\u0003\u0005L\u0001\tE\t\u0015!\u0003E\u0003\u0019)g\u000e\u001e:zA!)Q\n\u0001C\u0001\u001d\u00061A(\u001b8jiz\"ra\u0014)R%N#V\u000b\u0005\u0002)\u0001!)\u0011\u0004\u0014a\u00017!)Q\u0005\u0014a\u0001O!)a\u0006\u0014a\u0001a!)a\u0007\u0014a\u0001q!)a\b\u0014a\u0001a!)!\t\u0014a\u0001\t\")q\u000b\u0001C\u00011\u0006a1/_7c_2\u001cFO]5oOR\u0011\u0011\f\u0019\t\u00035~k\u0011a\u0017\u0006\u00039v\u000bA\u0001\\1oO*\ta,\u0001\u0003kCZ\f\u0017B\u0001\u0011\\\u0011\u0015\tg\u000b1\u0001\r\u0003\r\tg.\u001f\u0005\u0006G\u0002!\t\u0005Z\u0001\ti>\u001cFO]5oOR\t\u0011\fC\u0004g\u0001\u0005\u0005I\u0011A4\u0002\t\r|\u0007/\u001f\u000b\b\u001f\"L'n\u001b7n\u0011\u001dIR\r%AA\u0002mAq!J3\u0011\u0002\u0003\u0007q\u0005C\u0004/KB\u0005\t\u0019\u0001\u0019\t\u000fY*\u0007\u0013!a\u0001q!9a(\u001aI\u0001\u0002\u0004\u0001\u0004b\u0002\"f!\u0003\u0005\r\u0001\u0012\u0005\b_\u0002\t\n\u0011\"\u0001q\u00039\u0019w\u000e]=%I\u00164\u0017-\u001e7uIE*\u0012!\u001d\u0016\u00037I\\\u0013a\u001d\t\u0003ifl\u0011!\u001e\u0006\u0003m^\f\u0011\"\u001e8dQ\u0016\u001c7.\u001a3\u000b\u0005at\u0011AC1o]>$\u0018\r^5p]&\u0011!0\u001e\u0002\u0012k:\u001c\u0007.Z2lK\u00124\u0016M]5b]\u000e,\u0007b\u0002?\u0001#\u0003%\t!`\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00133+\u0005q(FA\u0014s\u0011%\t\t\u0001AI\u0001\n\u0003\t\u0019!\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u001a\u0016\u0005\u0005\u0015!F\u0001\u0019s\u0011%\tI\u0001AI\u0001\n\u0003\tY!\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001b\u0016\u0005\u00055!F\u0001\u001ds\u0011%\t\t\u0002AI\u0001\n\u0003\t\u0019!\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u001b\t\u0013\u0005U\u0001!%A\u0005\u0002\u0005]\u0011AD2paf$C-\u001a4bk2$HEN\u000b\u0003\u00033Q#\u0001\u0012:\t\u0013\u0005u\u0001!!A\u0005B\u0005}\u0011!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001Z\u0011!\t\u0019\u0003AA\u0001\n\u0003y\u0013\u0001\u00049s_\u0012,8\r^!sSRL\b\"CA\u0014\u0001\u0005\u0005I\u0011AA\u0015\u00039\u0001(o\u001c3vGR,E.Z7f]R$B!a\u000b\u00022A\u0019Q\"!\f\n\u0007\u0005=bBA\u0002B]fD\u0011\"a\r\u0002&\u0005\u0005\t\u0019\u0001\u0019\u0002\u0007a$\u0013\u0007C\u0005\u00028\u0001\t\t\u0011\"\u0011\u0002:\u0005y\u0001O]8ek\u000e$\u0018\n^3sCR|'/\u0006\u0002\u0002<A1\u0011QHA\"\u0003Wi!!a\u0010\u000b\u0007\u0005\u0005c\"\u0001\u0006d_2dWm\u0019;j_:LA!!\u0012\u0002@\tA\u0011\n^3sCR|'\u000fC\u0005\u0002J\u0001\t\t\u0011\"\u0001\u0002L\u0005A1-\u00198FcV\fG\u000e\u0006\u0003\u0002N\u0005M\u0003cA\u0007\u0002P%\u0019\u0011\u0011\u000b\b\u0003\u000f\t{w\u000e\\3b]\"Q\u00111GA$\u0003\u0003\u0005\r!a\u000b\t\u0013\u0005]\u0003!!A\u0005B\u0005e\u0013\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003AB\u0011\"!\u0018\u0001\u0003\u0003%\t%a\u0018\u0002\r\u0015\fX/\u00197t)\u0011\ti%!\u0019\t\u0015\u0005M\u00121LA\u0001\u0002\u0004\tYcB\u0005\u0002f\t\t\t\u0011#\u0001\u0002h\u0005Q1+_7c_2LeNZ8\u0011\u0007!\nIG\u0002\u0005\u0002\u0005\u0005\u0005\t\u0012AA6'\u0015\tI'!\u001c\u0016!-\ty'!\u001e\u001cOAB\u0004\u0007R(\u000e\u0005\u0005E$bAA:\u001d\u00059!/\u001e8uS6,\u0017\u0002BA<\u0003c\u0012\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c87\u0011\u001di\u0015\u0011\u000eC\u0001\u0003w\"\"!a\u001a\t\u0011\r\fI'!A\u0005F\u0011D!\"!!\u0002j\u0005\u0005I\u0011QAB\u0003\u0015\t\u0007\u000f\u001d7z)5y\u0015QQAD\u0003\u0013\u000bY)!$\u0002\u0010\"1\u0011$a A\u0002mAa!JA@\u0001\u00049\u0003B\u0002\u0018\u0002\u0000\u0001\u0007\u0001\u0007\u0003\u00047\u0003\u007f\u0002\r\u0001\u000f\u0005\u0007}\u0005}\u0004\u0019\u0001\u0019\t\r\t\u000by\b1\u0001E\u0011)\t\u0019*!\u001b\u0002\u0002\u0013\u0005\u0015QS\u0001\bk:\f\u0007\u000f\u001d7z)\u0011\t9*a(\u0011\t5I\u0014\u0011\u0014\t\n\u001b\u0005m5d\n\u00199a\u0011K1!!(\u000f\u0005\u0019!V\u000f\u001d7fm!I\u0011\u0011UAI\u0003\u0003\u0005\raT\u0001\u0004q\u0012\u0002\u0004BCAS\u0003S\n\t\u0011\"\u0003\u0002(\u0006Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\tI\u000bE\u0002[\u0003WK1!!,\\\u0005\u0019y%M[3di\u0002")
public class SymbolInfo implements Product, Serializable
{
    private final String name;
    private final Symbol owner;
    private final int flags;
    private final Option<Object> privateWithin;
    private final int info;
    private final ScalaSig.Entry entry;
    
    public static Option<Tuple6<String, Symbol, Object, Option<Object>, Object, ScalaSig.Entry>> unapply(final SymbolInfo x$0) {
        return SymbolInfo$.MODULE$.unapply(x$0);
    }
    
    public static SymbolInfo apply(final String name, final Symbol owner, final int flags, final Option<Object> privateWithin, final int info, final ScalaSig.Entry entry) {
        return SymbolInfo$.MODULE$.apply(name, owner, flags, privateWithin, info, entry);
    }
    
    public static Function1<Tuple6<String, Symbol, Object, Option<Object>, Object, ScalaSig.Entry>, SymbolInfo> tupled() {
        return (Function1<Tuple6<String, Symbol, Object, Option<Object>, Object, ScalaSig.Entry>, SymbolInfo>)SymbolInfo$.MODULE$.tupled();
    }
    
    public static Function1 curried() {
        return SymbolInfo$.MODULE$.curried();
    }
    
    public String name() {
        return this.name;
    }
    
    public Symbol owner() {
        return this.owner;
    }
    
    public int flags() {
        return this.flags;
    }
    
    public Option<Object> privateWithin() {
        return this.privateWithin;
    }
    
    public int info() {
        return this.info;
    }
    
    public ScalaSig.Entry entry() {
        return this.entry;
    }
    
    public String symbolString(final Object any) {
        String s;
        if (any instanceof SymbolInfoSymbol) {
            s = BoxesRunTime.boxToInteger(((SymbolInfoSymbol)any).index()).toString();
        }
        else {
            s = any.toString();
        }
        return s;
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append((Object)this.name()).append((Object)", owner=").append((Object)this.symbolString(this.owner())).append((Object)", flags=").append((Object)RichInt$.MODULE$.toHexString$extension(Predef$.MODULE$.intWrapper(this.flags()))).append((Object)", info=").append((Object)BoxesRunTime.boxToInteger(this.info()));
        final Option<Object> privateWithin = this.privateWithin();
        String string;
        if (privateWithin instanceof Some) {
            final Object any = ((Some)privateWithin).x();
            string = new StringBuilder().append((Object)", privateWithin=").append((Object)this.symbolString(any)).toString();
        }
        else {
            if (!None$.MODULE$.equals(privateWithin)) {
                throw new MatchError((Object)privateWithin);
            }
            string = " ";
        }
        return append.append((Object)string).toString();
    }
    
    public SymbolInfo copy(final String name, final Symbol owner, final int flags, final Option<Object> privateWithin, final int info, final ScalaSig.Entry entry) {
        return new SymbolInfo(name, owner, flags, privateWithin, info, entry);
    }
    
    public String copy$default$1() {
        return this.name();
    }
    
    public Symbol copy$default$2() {
        return this.owner();
    }
    
    public int copy$default$3() {
        return this.flags();
    }
    
    public Option<Object> copy$default$4() {
        return this.privateWithin();
    }
    
    public int copy$default$5() {
        return this.info();
    }
    
    public ScalaSig.Entry copy$default$6() {
        return this.entry();
    }
    
    public String productPrefix() {
        return "SymbolInfo";
    }
    
    public int productArity() {
        return 6;
    }
    
    public Object productElement(final int x$1) {
        Object o = null;
        switch (x$1) {
            default: {
                throw new IndexOutOfBoundsException(BoxesRunTime.boxToInteger(x$1).toString());
            }
            case 5: {
                o = this.entry();
                break;
            }
            case 4: {
                o = BoxesRunTime.boxToInteger(this.info());
                break;
            }
            case 3: {
                o = this.privateWithin();
                break;
            }
            case 2: {
                o = BoxesRunTime.boxToInteger(this.flags());
                break;
            }
            case 1: {
                o = this.owner();
                break;
            }
            case 0: {
                o = this.name();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof SymbolInfo;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(Statics.mix(Statics.mix(Statics.mix(Statics.mix(Statics.mix(-889275714, Statics.anyHash((Object)this.name())), Statics.anyHash((Object)this.owner())), this.flags()), Statics.anyHash((Object)this.privateWithin())), this.info()), Statics.anyHash((Object)this.entry())), 6);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof SymbolInfo) {
                final SymbolInfo symbolInfo = (SymbolInfo)x$1;
                final String name = this.name();
                final String name2 = symbolInfo.name();
                boolean b = false;
                Label_0197: {
                    Label_0196: {
                        if (name == null) {
                            if (name2 != null) {
                                break Label_0196;
                            }
                        }
                        else if (!name.equals(name2)) {
                            break Label_0196;
                        }
                        final Symbol owner = this.owner();
                        final Symbol owner2 = symbolInfo.owner();
                        if (owner == null) {
                            if (owner2 != null) {
                                break Label_0196;
                            }
                        }
                        else if (!owner.equals(owner2)) {
                            break Label_0196;
                        }
                        if (this.flags() == symbolInfo.flags()) {
                            final Option<Object> privateWithin = this.privateWithin();
                            final Option<Object> privateWithin2 = symbolInfo.privateWithin();
                            if (privateWithin == null) {
                                if (privateWithin2 != null) {
                                    break Label_0196;
                                }
                            }
                            else if (!privateWithin.equals(privateWithin2)) {
                                break Label_0196;
                            }
                            if (this.info() == symbolInfo.info()) {
                                final ScalaSig.Entry entry = this.entry();
                                final ScalaSig.Entry entry2 = symbolInfo.entry();
                                if (entry == null) {
                                    if (entry2 != null) {
                                        break Label_0196;
                                    }
                                }
                                else if (!entry.equals(entry2)) {
                                    break Label_0196;
                                }
                                if (symbolInfo.canEqual(this)) {
                                    b = true;
                                    break Label_0197;
                                }
                            }
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
    
    public SymbolInfo(final String name, final Symbol owner, final int flags, final Option<Object> privateWithin, final int info, final ScalaSig.Entry entry) {
        this.name = name;
        this.owner = owner;
        this.flags = flags;
        this.privateWithin = privateWithin;
        this.info = info;
        this.entry = entry;
        Product$class.$init$((Product)this);
    }
}
