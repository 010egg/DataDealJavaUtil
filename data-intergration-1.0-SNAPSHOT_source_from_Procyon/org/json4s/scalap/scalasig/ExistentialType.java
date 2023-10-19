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

@ScalaSignature(bytes = "\u0006\u0001\u0005-c\u0001B\u0001\u0003\u0001.\u0011q\"\u0012=jgR,g\u000e^5bYRK\b/\u001a\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u0019A1\u0002CA\u0007\u000f\u001b\u0005\u0011\u0011BA\b\u0003\u0005\u0011!\u0016\u0010]3\u0011\u0005E!R\"\u0001\n\u000b\u0003M\tQa]2bY\u0006L!!\u0006\n\u0003\u000fA\u0013x\u000eZ;diB\u0011\u0011cF\u0005\u00031I\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001B\u0007\u0001\u0003\u0016\u0004%\taG\u0001\bif\u0004XMU3g+\u0005a\u0001\u0002C\u000f\u0001\u0005#\u0005\u000b\u0011\u0002\u0007\u0002\u0011QL\b/\u001a*fM\u0002B\u0001b\b\u0001\u0003\u0016\u0004%\t\u0001I\u0001\bgfl'm\u001c7t+\u0005\t\u0003c\u0001\u0012+[9\u00111\u0005\u000b\b\u0003I\u001dj\u0011!\n\u0006\u0003M)\ta\u0001\u0010:p_Rt\u0014\"A\n\n\u0005%\u0012\u0012a\u00029bG.\fw-Z\u0005\u0003W1\u00121aU3r\u0015\tI#\u0003\u0005\u0002\u000e]%\u0011qF\u0001\u0002\u0007'fl'm\u001c7\t\u0011E\u0002!\u0011#Q\u0001\n\u0005\n\u0001b]=nE>d7\u000f\t\u0005\u0006g\u0001!\t\u0001N\u0001\u0007y%t\u0017\u000e\u001e \u0015\u0007U2t\u0007\u0005\u0002\u000e\u0001!)!D\ra\u0001\u0019!)qD\ra\u0001C!9\u0011\bAA\u0001\n\u0003Q\u0014\u0001B2paf$2!N\u001e=\u0011\u001dQ\u0002\b%AA\u00021Aqa\b\u001d\u0011\u0002\u0003\u0007\u0011\u0005C\u0004?\u0001E\u0005I\u0011A \u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\t\u0001I\u000b\u0002\r\u0003.\n!\t\u0005\u0002D\u00116\tAI\u0003\u0002F\r\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003\u000fJ\t!\"\u00198o_R\fG/[8o\u0013\tIEIA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dqa\u0013\u0001\u0012\u0002\u0013\u0005A*\u0001\bd_BLH\u0005Z3gCVdG\u000f\n\u001a\u0016\u00035S#!I!\t\u000f=\u0003\u0011\u0011!C!!\u0006i\u0001O]8ek\u000e$\bK]3gSb,\u0012!\u0015\t\u0003%^k\u0011a\u0015\u0006\u0003)V\u000bA\u0001\\1oO*\ta+\u0001\u0003kCZ\f\u0017B\u0001-T\u0005\u0019\u0019FO]5oO\"9!\fAA\u0001\n\u0003Y\u0016\u0001\u00049s_\u0012,8\r^!sSRLX#\u0001/\u0011\u0005Ei\u0016B\u00010\u0013\u0005\rIe\u000e\u001e\u0005\bA\u0002\t\t\u0011\"\u0001b\u00039\u0001(o\u001c3vGR,E.Z7f]R$\"AY3\u0011\u0005E\u0019\u0017B\u00013\u0013\u0005\r\te.\u001f\u0005\bM~\u000b\t\u00111\u0001]\u0003\rAH%\r\u0005\bQ\u0002\t\t\u0011\"\u0011j\u0003=\u0001(o\u001c3vGRLE/\u001a:bi>\u0014X#\u00016\u0011\u0007-t'-D\u0001m\u0015\ti'#\u0001\u0006d_2dWm\u0019;j_:L!a\u001c7\u0003\u0011%#XM]1u_JDq!\u001d\u0001\u0002\u0002\u0013\u0005!/\u0001\u0005dC:,\u0015/^1m)\t\u0019h\u000f\u0005\u0002\u0012i&\u0011QO\u0005\u0002\b\u0005>|G.Z1o\u0011\u001d1\u0007/!AA\u0002\tDq\u0001\u001f\u0001\u0002\u0002\u0013\u0005\u00130\u0001\u0005iCND7i\u001c3f)\u0005a\u0006bB>\u0001\u0003\u0003%\t\u0005`\u0001\ti>\u001cFO]5oOR\t\u0011\u000bC\u0004\u007f\u0001\u0005\u0005I\u0011I@\u0002\r\u0015\fX/\u00197t)\r\u0019\u0018\u0011\u0001\u0005\bMv\f\t\u00111\u0001c\u000f%\t)AAA\u0001\u0012\u0003\t9!A\bFq&\u001cH/\u001a8uS\u0006dG+\u001f9f!\ri\u0011\u0011\u0002\u0004\t\u0003\t\t\t\u0011#\u0001\u0002\fM)\u0011\u0011BA\u0007-A9\u0011qBA\u000b\u0019\u0005*TBAA\t\u0015\r\t\u0019BE\u0001\beVtG/[7f\u0013\u0011\t9\"!\u0005\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t'\u0007C\u00044\u0003\u0013!\t!a\u0007\u0015\u0005\u0005\u001d\u0001\u0002C>\u0002\n\u0005\u0005IQ\t?\t\u0015\u0005\u0005\u0012\u0011BA\u0001\n\u0003\u000b\u0019#A\u0003baBd\u0017\u0010F\u00036\u0003K\t9\u0003\u0003\u0004\u001b\u0003?\u0001\r\u0001\u0004\u0005\u0007?\u0005}\u0001\u0019A\u0011\t\u0015\u0005-\u0012\u0011BA\u0001\n\u0003\u000bi#A\u0004v]\u0006\u0004\b\u000f\\=\u0015\t\u0005=\u00121\b\t\u0006#\u0005E\u0012QG\u0005\u0004\u0003g\u0011\"AB(qi&|g\u000eE\u0003\u0012\u0003oa\u0011%C\u0002\u0002:I\u0011a\u0001V;qY\u0016\u0014\u0004\"CA\u001f\u0003S\t\t\u00111\u00016\u0003\rAH\u0005\r\u0005\u000b\u0003\u0003\nI!!A\u0005\n\u0005\r\u0013a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!!\u0012\u0011\u0007I\u000b9%C\u0002\u0002JM\u0013aa\u00142kK\u000e$\b")
public class ExistentialType extends Type implements Product, Serializable
{
    private final Type typeRef;
    private final Seq<Symbol> symbols;
    
    public static Option<Tuple2<Type, Seq<Symbol>>> unapply(final ExistentialType x$0) {
        return ExistentialType$.MODULE$.unapply(x$0);
    }
    
    public static ExistentialType apply(final Type typeRef, final Seq<Symbol> symbols) {
        return ExistentialType$.MODULE$.apply(typeRef, symbols);
    }
    
    public static Function1<Tuple2<Type, Seq<Symbol>>, ExistentialType> tupled() {
        return (Function1<Tuple2<Type, Seq<Symbol>>, ExistentialType>)ExistentialType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Seq<Symbol>, ExistentialType>> curried() {
        return (Function1<Type, Function1<Seq<Symbol>, ExistentialType>>)ExistentialType$.MODULE$.curried();
    }
    
    public Type typeRef() {
        return this.typeRef;
    }
    
    public Seq<Symbol> symbols() {
        return this.symbols;
    }
    
    public ExistentialType copy(final Type typeRef, final Seq<Symbol> symbols) {
        return new ExistentialType(typeRef, symbols);
    }
    
    public Type copy$default$1() {
        return this.typeRef();
    }
    
    public Seq<Symbol> copy$default$2() {
        return this.symbols();
    }
    
    public String productPrefix() {
        return "ExistentialType";
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
        return x$1 instanceof ExistentialType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ExistentialType) {
                final ExistentialType existentialType = (ExistentialType)x$1;
                final Type typeRef = this.typeRef();
                final Type typeRef2 = existentialType.typeRef();
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
                        final Seq<Symbol> symbols = this.symbols();
                        final Seq<Symbol> symbols2 = existentialType.symbols();
                        if (symbols == null) {
                            if (symbols2 != null) {
                                break Label_0108;
                            }
                        }
                        else if (!symbols.equals(symbols2)) {
                            break Label_0108;
                        }
                        if (existentialType.canEqual(this)) {
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
    
    public ExistentialType(final Type typeRef, final Seq<Symbol> symbols) {
        this.typeRef = typeRef;
        this.symbols = symbols;
        Product$class.$init$((Product)this);
    }
}
