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

@ScalaSignature(bytes = "\u0006\u0001\u0005\rd\u0001B\u0001\u0003\u0001.\u00111\u0002V=qKJ+g\rV=qK*\u00111\u0001B\u0001\tg\u000e\fG.Y:jO*\u0011QAB\u0001\u0007g\u000e\fG.\u00199\u000b\u0005\u001dA\u0011A\u00026t_:$4OC\u0001\n\u0003\ry'oZ\u0002\u0001'\u0011\u0001A\u0002\u0005\f\u0011\u00055qQ\"\u0001\u0002\n\u0005=\u0011!\u0001\u0002+za\u0016\u0004\"!\u0005\u000b\u000e\u0003IQ\u0011aE\u0001\u0006g\u000e\fG.Y\u0005\u0003+I\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u0012/%\u0011\u0001D\u0005\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t5\u0001\u0011)\u001a!C\u00017\u00051\u0001O]3gSb,\u0012\u0001\u0004\u0005\t;\u0001\u0011\t\u0012)A\u0005\u0019\u00059\u0001O]3gSb\u0004\u0003\u0002C\u0010\u0001\u0005+\u0007I\u0011\u0001\u0011\u0002\rMLXNY8m+\u0005\t\u0003CA\u0007#\u0013\t\u0019#A\u0001\u0004Ts6\u0014w\u000e\u001c\u0005\tK\u0001\u0011\t\u0012)A\u0005C\u000591/_7c_2\u0004\u0003\u0002C\u0014\u0001\u0005+\u0007I\u0011\u0001\u0015\u0002\u0011QL\b/Z!sON,\u0012!\u000b\t\u0004UIbaBA\u00161\u001d\tas&D\u0001.\u0015\tq#\"\u0001\u0004=e>|GOP\u0005\u0002'%\u0011\u0011GE\u0001\ba\u0006\u001c7.Y4f\u0013\t\u0019DGA\u0002TKFT!!\r\n\t\u0011Y\u0002!\u0011#Q\u0001\n%\n\u0011\u0002^=qK\u0006\u0013xm\u001d\u0011\t\u000ba\u0002A\u0011A\u001d\u0002\rqJg.\u001b;?)\u0011Q4\bP\u001f\u0011\u00055\u0001\u0001\"\u0002\u000e8\u0001\u0004a\u0001\"B\u00108\u0001\u0004\t\u0003\"B\u00148\u0001\u0004I\u0003bB \u0001\u0003\u0003%\t\u0001Q\u0001\u0005G>\u0004\u0018\u0010\u0006\u0003;\u0003\n\u001b\u0005b\u0002\u000e?!\u0003\u0005\r\u0001\u0004\u0005\b?y\u0002\n\u00111\u0001\"\u0011\u001d9c\b%AA\u0002%Bq!\u0012\u0001\u0012\u0002\u0013\u0005a)\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u0003\u001dS#\u0001\u0004%,\u0003%\u0003\"AS(\u000e\u0003-S!\u0001T'\u0002\u0013Ut7\r[3dW\u0016$'B\u0001(\u0013\u0003)\tgN\\8uCRLwN\\\u0005\u0003!.\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001d\u0011\u0006!%A\u0005\u0002M\u000babY8qs\u0012\"WMZ1vYR$#'F\u0001UU\t\t\u0003\nC\u0004W\u0001E\u0005I\u0011A,\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%gU\t\u0001L\u000b\u0002*\u0011\"9!\fAA\u0001\n\u0003Z\u0016!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001]!\ti&-D\u0001_\u0015\ty\u0006-\u0001\u0003mC:<'\"A1\u0002\t)\fg/Y\u0005\u0003Gz\u0013aa\u0015;sS:<\u0007bB3\u0001\u0003\u0003%\tAZ\u0001\raJ|G-^2u\u0003JLG/_\u000b\u0002OB\u0011\u0011\u0003[\u0005\u0003SJ\u00111!\u00138u\u0011\u001dY\u0007!!A\u0005\u00021\fa\u0002\u001d:pIV\u001cG/\u00127f[\u0016tG\u000f\u0006\u0002naB\u0011\u0011C\\\u0005\u0003_J\u00111!\u00118z\u0011\u001d\t(.!AA\u0002\u001d\f1\u0001\u001f\u00132\u0011\u001d\u0019\b!!A\u0005BQ\fq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002kB\u0019a/_7\u000e\u0003]T!\u0001\u001f\n\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002{o\nA\u0011\n^3sCR|'\u000fC\u0004}\u0001\u0005\u0005I\u0011A?\u0002\u0011\r\fg.R9vC2$2A`A\u0002!\t\tr0C\u0002\u0002\u0002I\u0011qAQ8pY\u0016\fg\u000eC\u0004rw\u0006\u0005\t\u0019A7\t\u0013\u0005\u001d\u0001!!A\u0005B\u0005%\u0011\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003\u001dD\u0011\"!\u0004\u0001\u0003\u0003%\t%a\u0004\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012\u0001\u0018\u0005\n\u0003'\u0001\u0011\u0011!C!\u0003+\ta!Z9vC2\u001cHc\u0001@\u0002\u0018!A\u0011/!\u0005\u0002\u0002\u0003\u0007QnB\u0005\u0002\u001c\t\t\t\u0011#\u0001\u0002\u001e\u0005YA+\u001f9f%\u00164G+\u001f9f!\ri\u0011q\u0004\u0004\t\u0003\t\t\t\u0011#\u0001\u0002\"M)\u0011qDA\u0012-AA\u0011QEA\u0016\u0019\u0005J#(\u0004\u0002\u0002()\u0019\u0011\u0011\u0006\n\u0002\u000fI,h\u000e^5nK&!\u0011QFA\u0014\u0005E\t%m\u001d;sC\u000e$h)\u001e8di&|gn\r\u0005\bq\u0005}A\u0011AA\u0019)\t\ti\u0002\u0003\u0006\u0002\u000e\u0005}\u0011\u0011!C#\u0003\u001fA!\"a\u000e\u0002 \u0005\u0005I\u0011QA\u001d\u0003\u0015\t\u0007\u000f\u001d7z)\u001dQ\u00141HA\u001f\u0003\u007fAaAGA\u001b\u0001\u0004a\u0001BB\u0010\u00026\u0001\u0007\u0011\u0005\u0003\u0004(\u0003k\u0001\r!\u000b\u0005\u000b\u0003\u0007\ny\"!A\u0005\u0002\u0006\u0015\u0013aB;oCB\u0004H.\u001f\u000b\u0005\u0003\u000f\n\u0019\u0006E\u0003\u0012\u0003\u0013\ni%C\u0002\u0002LI\u0011aa\u00149uS>t\u0007CB\t\u0002P1\t\u0013&C\u0002\u0002RI\u0011a\u0001V;qY\u0016\u001c\u0004\"CA+\u0003\u0003\n\t\u00111\u0001;\u0003\rAH\u0005\r\u0005\u000b\u00033\ny\"!A\u0005\n\u0005m\u0013a\u0003:fC\u0012\u0014Vm]8mm\u0016$\"!!\u0018\u0011\u0007u\u000by&C\u0002\u0002by\u0013aa\u00142kK\u000e$\b")
public class TypeRefType extends Type implements Product, Serializable
{
    private final Type prefix;
    private final Symbol symbol;
    private final Seq<Type> typeArgs;
    
    public static Option<Tuple3<Type, Symbol, Seq<Type>>> unapply(final TypeRefType x$0) {
        return TypeRefType$.MODULE$.unapply(x$0);
    }
    
    public static TypeRefType apply(final Type prefix, final Symbol symbol, final Seq<Type> typeArgs) {
        return TypeRefType$.MODULE$.apply(prefix, symbol, typeArgs);
    }
    
    public static Function1<Tuple3<Type, Symbol, Seq<Type>>, TypeRefType> tupled() {
        return (Function1<Tuple3<Type, Symbol, Seq<Type>>, TypeRefType>)TypeRefType$.MODULE$.tupled();
    }
    
    public static Function1<Type, Function1<Symbol, Function1<Seq<Type>, TypeRefType>>> curried() {
        return (Function1<Type, Function1<Symbol, Function1<Seq<Type>, TypeRefType>>>)TypeRefType$.MODULE$.curried();
    }
    
    public Type prefix() {
        return this.prefix;
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public Seq<Type> typeArgs() {
        return this.typeArgs;
    }
    
    public TypeRefType copy(final Type prefix, final Symbol symbol, final Seq<Type> typeArgs) {
        return new TypeRefType(prefix, symbol, typeArgs);
    }
    
    public Type copy$default$1() {
        return this.prefix();
    }
    
    public Symbol copy$default$2() {
        return this.symbol();
    }
    
    public Seq<Type> copy$default$3() {
        return this.typeArgs();
    }
    
    public String productPrefix() {
        return "TypeRefType";
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
                o = this.typeArgs();
                break;
            }
            case 1: {
                o = this.symbol();
                break;
            }
            case 0: {
                o = this.prefix();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof TypeRefType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof TypeRefType) {
                final TypeRefType typeRefType = (TypeRefType)x$1;
                final Type prefix = this.prefix();
                final Type prefix2 = typeRefType.prefix();
                boolean b = false;
                Label_0141: {
                    Label_0140: {
                        if (prefix == null) {
                            if (prefix2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!prefix.equals(prefix2)) {
                            break Label_0140;
                        }
                        final Symbol symbol = this.symbol();
                        final Symbol symbol2 = typeRefType.symbol();
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0140;
                        }
                        final Seq<Type> typeArgs = this.typeArgs();
                        final Seq<Type> typeArgs2 = typeRefType.typeArgs();
                        if (typeArgs == null) {
                            if (typeArgs2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!typeArgs.equals(typeArgs2)) {
                            break Label_0140;
                        }
                        if (typeRefType.canEqual(this)) {
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
    
    public TypeRefType(final Type prefix, final Symbol symbol, final Seq<Type> typeArgs) {
        this.prefix = prefix;
        this.symbol = symbol;
        this.typeArgs = typeArgs;
        Product$class.$init$((Product)this);
    }
}
