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
import scala.collection.Seq;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005=d\u0001B\u0001\u0003\u0001.\u0011\u0001\u0003U8msRK\b/Z,ji\"\u001cuN\\:\u000b\u0005\r!\u0011\u0001C:dC2\f7/[4\u000b\u0005\u00151\u0011AB:dC2\f\u0007O\u0003\u0002\b\u0011\u00051!n]8oiMT\u0011!C\u0001\u0004_J<7\u0001A\n\u0005\u00011\u0001b\u0003\u0005\u0002\u000e\u001d5\t!!\u0003\u0002\u0010\u0005\t!A+\u001f9f!\t\tB#D\u0001\u0013\u0015\u0005\u0019\u0012!B:dC2\f\u0017BA\u000b\u0013\u0005\u001d\u0001&o\u001c3vGR\u0004\"!E\f\n\u0005a\u0011\"\u0001D*fe&\fG.\u001b>bE2,\u0007\u0002\u0003\u000e\u0001\u0005+\u0007I\u0011A\u000e\u0002\u000fQL\b/\u001a*fMV\tA\u0002\u0003\u0005\u001e\u0001\tE\t\u0015!\u0003\r\u0003!!\u0018\u0010]3SK\u001a\u0004\u0003\u0002C\u0010\u0001\u0005+\u0007I\u0011\u0001\u0011\u0002\u000fMLXNY8mgV\t\u0011\u0005E\u0002#U5r!a\t\u0015\u000f\u0005\u0011:S\"A\u0013\u000b\u0005\u0019R\u0011A\u0002\u001fs_>$h(C\u0001\u0014\u0013\tI##A\u0004qC\u000e\\\u0017mZ3\n\u0005-b#aA*fc*\u0011\u0011F\u0005\t\u0003\u001b9J!a\f\u0002\u0003\u0015QK\b/Z*z[\n|G\u000e\u0003\u00052\u0001\tE\t\u0015!\u0003\"\u0003!\u0019\u00180\u001c2pYN\u0004\u0003\u0002C\u001a\u0001\u0005+\u0007I\u0011\u0001\u001b\u0002\t\r|gn]\u000b\u0002kA\u0011a'\u000f\b\u0003#]J!\u0001\u000f\n\u0002\rA\u0013X\rZ3g\u0013\tQ4H\u0001\u0004TiJLgn\u001a\u0006\u0003qIA\u0001\"\u0010\u0001\u0003\u0012\u0003\u0006I!N\u0001\u0006G>t7\u000f\t\u0005\u0006\u007f\u0001!\t\u0001Q\u0001\u0007y%t\u0017\u000e\u001e \u0015\t\u0005\u00135\t\u0012\t\u0003\u001b\u0001AQA\u0007 A\u00021AQa\b A\u0002\u0005BQa\r A\u0002UBqA\u0012\u0001\u0002\u0002\u0013\u0005q)\u0001\u0003d_BLH\u0003B!I\u0013*CqAG#\u0011\u0002\u0003\u0007A\u0002C\u0004 \u000bB\u0005\t\u0019A\u0011\t\u000fM*\u0005\u0013!a\u0001k!9A\nAI\u0001\n\u0003i\u0015AD2paf$C-\u001a4bk2$H%M\u000b\u0002\u001d*\u0012AbT\u0016\u0002!B\u0011\u0011KV\u0007\u0002%*\u00111\u000bV\u0001\nk:\u001c\u0007.Z2lK\u0012T!!\u0016\n\u0002\u0015\u0005tgn\u001c;bi&|g.\u0003\u0002X%\n\tRO\\2iK\u000e\\W\r\u001a,be&\fgnY3\t\u000fe\u0003\u0011\u0013!C\u00015\u0006q1m\u001c9zI\u0011,g-Y;mi\u0012\u0012T#A.+\u0005\u0005z\u0005bB/\u0001#\u0003%\tAX\u0001\u000fG>\u0004\u0018\u0010\n3fM\u0006,H\u000e\u001e\u00134+\u0005y&FA\u001bP\u0011\u001d\t\u0007!!A\u0005B\t\fQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#A2\u0011\u0005\u0011LW\"A3\u000b\u0005\u0019<\u0017\u0001\u00027b]\u001eT\u0011\u0001[\u0001\u0005U\u00064\u0018-\u0003\u0002;K\"91\u000eAA\u0001\n\u0003a\u0017\u0001\u00049s_\u0012,8\r^!sSRLX#A7\u0011\u0005Eq\u0017BA8\u0013\u0005\rIe\u000e\u001e\u0005\bc\u0002\t\t\u0011\"\u0001s\u00039\u0001(o\u001c3vGR,E.Z7f]R$\"a\u001d<\u0011\u0005E!\u0018BA;\u0013\u0005\r\te.\u001f\u0005\boB\f\t\u00111\u0001n\u0003\rAH%\r\u0005\bs\u0002\t\t\u0011\"\u0011{\u0003=\u0001(o\u001c3vGRLE/\u001a:bi>\u0014X#A>\u0011\u0007q|8/D\u0001~\u0015\tq(#\u0001\u0006d_2dWm\u0019;j_:L1!!\u0001~\u0005!IE/\u001a:bi>\u0014\b\"CA\u0003\u0001\u0005\u0005I\u0011AA\u0004\u0003!\u0019\u0017M\\#rk\u0006dG\u0003BA\u0005\u0003\u001f\u00012!EA\u0006\u0013\r\tiA\u0005\u0002\b\u0005>|G.Z1o\u0011!9\u00181AA\u0001\u0002\u0004\u0019\b\"CA\n\u0001\u0005\u0005I\u0011IA\u000b\u0003!A\u0017m\u001d5D_\u0012,G#A7\t\u0013\u0005e\u0001!!A\u0005B\u0005m\u0011\u0001\u0003;p'R\u0014\u0018N\\4\u0015\u0003\rD\u0011\"a\b\u0001\u0003\u0003%\t%!\t\u0002\r\u0015\fX/\u00197t)\u0011\tI!a\t\t\u0011]\fi\"!AA\u0002M<\u0011\"a\n\u0003\u0003\u0003E\t!!\u000b\u0002!A{G.\u001f+za\u0016<\u0016\u000e\u001e5D_:\u001c\bcA\u0007\u0002,\u0019A\u0011AAA\u0001\u0012\u0003\ticE\u0003\u0002,\u0005=b\u0003\u0005\u0005\u00022\u0005]B\"I\u001bB\u001b\t\t\u0019DC\u0002\u00026I\tqA];oi&lW-\u0003\u0003\u0002:\u0005M\"!E!cgR\u0014\u0018m\u0019;Gk:\u001cG/[8og!9q(a\u000b\u0005\u0002\u0005uBCAA\u0015\u0011)\tI\"a\u000b\u0002\u0002\u0013\u0015\u00131\u0004\u0005\u000b\u0003\u0007\nY#!A\u0005\u0002\u0006\u0015\u0013!B1qa2LHcB!\u0002H\u0005%\u00131\n\u0005\u00075\u0005\u0005\u0003\u0019\u0001\u0007\t\r}\t\t\u00051\u0001\"\u0011\u0019\u0019\u0014\u0011\ta\u0001k!Q\u0011qJA\u0016\u0003\u0003%\t)!\u0015\u0002\u000fUt\u0017\r\u001d9msR!\u00111KA0!\u0015\t\u0012QKA-\u0013\r\t9F\u0005\u0002\u0007\u001fB$\u0018n\u001c8\u0011\rE\tY\u0006D\u00116\u0013\r\tiF\u0005\u0002\u0007)V\u0004H.Z\u001a\t\u0013\u0005\u0005\u0014QJA\u0001\u0002\u0004\t\u0015a\u0001=%a!Q\u0011QMA\u0016\u0003\u0003%I!a\u001a\u0002\u0017I,\u0017\r\u001a*fg>dg/\u001a\u000b\u0003\u0003S\u00022\u0001ZA6\u0013\r\ti'\u001a\u0002\u0007\u001f\nTWm\u0019;")
public class PolyTypeWithCons extends Type implements Product, Serializable
{
    private final Type typeRef;
    private final Seq<TypeSymbol> symbols;
    private final String cons;
    
    public static Option<Tuple3<Type, Seq<TypeSymbol>, String>> unapply(final PolyTypeWithCons x$0) {
        return PolyTypeWithCons$.MODULE$.unapply(x$0);
    }
    
    public static PolyTypeWithCons apply(final Type typeRef, final Seq<TypeSymbol> symbols, final String cons) {
        return PolyTypeWithCons$.MODULE$.apply(typeRef, symbols, cons);
    }
    
    public static Function1<Tuple3<Type, Seq<TypeSymbol>, String>, PolyTypeWithCons> tupled() {
        return (Function1<Tuple3<Type, Seq<TypeSymbol>, String>, PolyTypeWithCons>)PolyTypeWithCons$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Seq<TypeSymbol>, Function1<String, PolyTypeWithCons>>> curried() {
        return (Function1<Type, Function1<Seq<TypeSymbol>, Function1<String, PolyTypeWithCons>>>)PolyTypeWithCons$.MODULE$.curried();
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public Seq<TypeSymbol> symbols() {
        return this.symbols;
    }
    
    public String cons() {
        return this.cons;
    }
    
    public PolyTypeWithCons copy(final Type typeRef, final Seq<TypeSymbol> symbols, final String cons) {
        return new PolyTypeWithCons(typeRef, symbols, cons);
    }
    
    public Type copy$default$1() {
        return this.typeRef();
    }
    
    public Seq<TypeSymbol> copy$default$2() {
        return this.symbols();
    }
    
    public String copy$default$3() {
        return this.cons();
    }
    
    public String productPrefix() {
        return "PolyTypeWithCons";
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
                o = this.cons();
                break;
            }
            case 1: {
                o = this.symbols();
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
        return x$1 instanceof PolyTypeWithCons;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof PolyTypeWithCons) {
                final PolyTypeWithCons polyTypeWithCons = (PolyTypeWithCons)x$1;
                final Type typeRef = this.typeRef();
                final Type typeRef2 = polyTypeWithCons.typeRef();
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
                        final Seq<TypeSymbol> symbols = this.symbols();
                        final Seq<TypeSymbol> symbols2 = polyTypeWithCons.symbols();
                        if (symbols == null) {
                            if (symbols2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!symbols.equals(symbols2)) {
                            break Label_0140;
                        }
                        final String cons = this.cons();
                        final String cons2 = polyTypeWithCons.cons();
                        if (cons == null) {
                            if (cons2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!cons.equals(cons2)) {
                            break Label_0140;
                        }
                        if (polyTypeWithCons.canEqual(this)) {
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
    
    public PolyTypeWithCons(final Type typeRef, final Seq<TypeSymbol> symbols, final String cons) {
        this.typeRef = typeRef;
        this.symbols = symbols;
        this.cons = cons;
        Product$class.$init$((Product)this);
    }
}
