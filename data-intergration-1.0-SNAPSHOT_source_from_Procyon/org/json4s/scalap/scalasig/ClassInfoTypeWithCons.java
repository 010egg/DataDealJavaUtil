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

@ScalaSignature(bytes = "\u0006\u0001\u0005=d\u0001B\u0001\u0003\u0001.\u0011Qc\u00117bgNLeNZ8UsB,w+\u001b;i\u0007>t7O\u0003\u0002\u0004\t\u0005A1oY1mCNLwM\u0003\u0002\u0006\r\u000511oY1mCBT!a\u0002\u0005\u0002\r)\u001cxN\u001c\u001bt\u0015\u0005I\u0011aA8sO\u000e\u00011\u0003\u0002\u0001\r!Y\u0001\"!\u0004\b\u000e\u0003\tI!a\u0004\u0002\u0003\tQK\b/\u001a\t\u0003#Qi\u0011A\u0005\u0006\u0002'\u0005)1oY1mC&\u0011QC\u0005\u0002\b!J|G-^2u!\t\tr#\u0003\u0002\u0019%\ta1+\u001a:jC2L'0\u00192mK\"A!\u0004\u0001BK\u0002\u0013\u00051$\u0001\u0004ts6\u0014w\u000e\\\u000b\u00029A\u0011Q\"H\u0005\u0003=\t\u0011aaU=nE>d\u0007\u0002\u0003\u0011\u0001\u0005#\u0005\u000b\u0011\u0002\u000f\u0002\u000fMLXNY8mA!A!\u0005\u0001BK\u0002\u0013\u00051%\u0001\u0005usB,'+\u001a4t+\u0005!\u0003cA\u0013.\u00199\u0011ae\u000b\b\u0003O)j\u0011\u0001\u000b\u0006\u0003S)\ta\u0001\u0010:p_Rt\u0014\"A\n\n\u00051\u0012\u0012a\u00029bG.\fw-Z\u0005\u0003]=\u00121aU3r\u0015\ta#\u0003\u0003\u00052\u0001\tE\t\u0015!\u0003%\u0003%!\u0018\u0010]3SK\u001a\u001c\b\u0005\u0003\u00054\u0001\tU\r\u0011\"\u00015\u0003\u0011\u0019wN\\:\u0016\u0003U\u0002\"AN\u001d\u000f\u0005E9\u0014B\u0001\u001d\u0013\u0003\u0019\u0001&/\u001a3fM&\u0011!h\u000f\u0002\u0007'R\u0014\u0018N\\4\u000b\u0005a\u0012\u0002\u0002C\u001f\u0001\u0005#\u0005\u000b\u0011B\u001b\u0002\u000b\r|gn\u001d\u0011\t\u000b}\u0002A\u0011\u0001!\u0002\rqJg.\u001b;?)\u0011\t%i\u0011#\u0011\u00055\u0001\u0001\"\u0002\u000e?\u0001\u0004a\u0002\"\u0002\u0012?\u0001\u0004!\u0003\"B\u001a?\u0001\u0004)\u0004b\u0002$\u0001\u0003\u0003%\taR\u0001\u0005G>\u0004\u0018\u0010\u0006\u0003B\u0011&S\u0005b\u0002\u000eF!\u0003\u0005\r\u0001\b\u0005\bE\u0015\u0003\n\u00111\u0001%\u0011\u001d\u0019T\t%AA\u0002UBq\u0001\u0014\u0001\u0012\u0002\u0013\u0005Q*\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u00039S#\u0001H(,\u0003A\u0003\"!\u0015,\u000e\u0003IS!a\u0015+\u0002\u0013Ut7\r[3dW\u0016$'BA+\u0013\u0003)\tgN\\8uCRLwN\\\u0005\u0003/J\u0013\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dI\u0006!%A\u0005\u0002i\u000babY8qs\u0012\"WMZ1vYR$#'F\u0001\\U\t!s\nC\u0004^\u0001E\u0005I\u0011\u00010\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%gU\tqL\u000b\u00026\u001f\"9\u0011\rAA\u0001\n\u0003\u0012\u0017!\u00049s_\u0012,8\r\u001e)sK\u001aL\u00070F\u0001d!\t!\u0017.D\u0001f\u0015\t1w-\u0001\u0003mC:<'\"\u00015\u0002\t)\fg/Y\u0005\u0003u\u0015Dqa\u001b\u0001\u0002\u0002\u0013\u0005A.\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001n!\t\tb.\u0003\u0002p%\t\u0019\u0011J\u001c;\t\u000fE\u0004\u0011\u0011!C\u0001e\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HCA:w!\t\tB/\u0003\u0002v%\t\u0019\u0011I\\=\t\u000f]\u0004\u0018\u0011!a\u0001[\u0006\u0019\u0001\u0010J\u0019\t\u000fe\u0004\u0011\u0011!C!u\u0006y\u0001O]8ek\u000e$\u0018\n^3sCR|'/F\u0001|!\raxp]\u0007\u0002{*\u0011aPE\u0001\u000bG>dG.Z2uS>t\u0017bAA\u0001{\nA\u0011\n^3sCR|'\u000fC\u0005\u0002\u0006\u0001\t\t\u0011\"\u0001\u0002\b\u0005A1-\u00198FcV\fG\u000e\u0006\u0003\u0002\n\u0005=\u0001cA\t\u0002\f%\u0019\u0011Q\u0002\n\u0003\u000f\t{w\u000e\\3b]\"Aq/a\u0001\u0002\u0002\u0003\u00071\u000fC\u0005\u0002\u0014\u0001\t\t\u0011\"\u0011\u0002\u0016\u0005A\u0001.Y:i\u0007>$W\rF\u0001n\u0011%\tI\u0002AA\u0001\n\u0003\nY\"\u0001\u0005u_N#(/\u001b8h)\u0005\u0019\u0007\"CA\u0010\u0001\u0005\u0005I\u0011IA\u0011\u0003\u0019)\u0017/^1mgR!\u0011\u0011BA\u0012\u0011!9\u0018QDA\u0001\u0002\u0004\u0019x!CA\u0014\u0005\u0005\u0005\t\u0012AA\u0015\u0003U\u0019E.Y:t\u0013:4w\u000eV=qK^KG\u000f[\"p]N\u00042!DA\u0016\r!\t!!!A\t\u0002\u000552#BA\u0016\u0003_1\u0002\u0003CA\u0019\u0003oaB%N!\u000e\u0005\u0005M\"bAA\u001b%\u00059!/\u001e8uS6,\u0017\u0002BA\u001d\u0003g\u0011\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c84\u0011\u001dy\u00141\u0006C\u0001\u0003{!\"!!\u000b\t\u0015\u0005e\u00111FA\u0001\n\u000b\nY\u0002\u0003\u0006\u0002D\u0005-\u0012\u0011!CA\u0003\u000b\nQ!\u00199qYf$r!QA$\u0003\u0013\nY\u0005\u0003\u0004\u001b\u0003\u0003\u0002\r\u0001\b\u0005\u0007E\u0005\u0005\u0003\u0019\u0001\u0013\t\rM\n\t\u00051\u00016\u0011)\ty%a\u000b\u0002\u0002\u0013\u0005\u0015\u0011K\u0001\bk:\f\u0007\u000f\u001d7z)\u0011\t\u0019&a\u0018\u0011\u000bE\t)&!\u0017\n\u0007\u0005]#C\u0001\u0004PaRLwN\u001c\t\u0007#\u0005mC\u0004J\u001b\n\u0007\u0005u#C\u0001\u0004UkBdWm\r\u0005\n\u0003C\ni%!AA\u0002\u0005\u000b1\u0001\u001f\u00131\u0011)\t)'a\u000b\u0002\u0002\u0013%\u0011qM\u0001\fe\u0016\fGMU3t_24X\r\u0006\u0002\u0002jA\u0019A-a\u001b\n\u0007\u00055TM\u0001\u0004PE*,7\r\u001e")
public class ClassInfoTypeWithCons extends Type implements Product, Serializable
{
    private final Symbol symbol;
    private final Seq<Type> typeRefs;
    private final String cons;
    
    public static Option<Tuple3<Symbol, Seq<Type>, String>> unapply(final ClassInfoTypeWithCons x$0) {
        return ClassInfoTypeWithCons$.MODULE$.unapply(x$0);
    }
    
    public static ClassInfoTypeWithCons apply(final Symbol symbol, final Seq<Type> typeRefs, final String cons) {
        return ClassInfoTypeWithCons$.MODULE$.apply(symbol, typeRefs, cons);
    }
    
    public static Function1<Tuple3<Symbol, Seq<Type>, String>, ClassInfoTypeWithCons> tupled() {
        return (Function1<Tuple3<Symbol, Seq<Type>, String>, ClassInfoTypeWithCons>)ClassInfoTypeWithCons$.MODULE$.tupled();
    }
    
    public static Function1<Symbol, Function1<Seq<Type>, Function1<String, ClassInfoTypeWithCons>>> curried() {
        return (Function1<Symbol, Function1<Seq<Type>, Function1<String, ClassInfoTypeWithCons>>>)ClassInfoTypeWithCons$.MODULE$.curried();
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public Seq<Type> typeRefs() {
        return this.typeRefs;
    }
    
    public String cons() {
        return this.cons;
    }
    
    public ClassInfoTypeWithCons copy(final Symbol symbol, final Seq<Type> typeRefs, final String cons) {
        return new ClassInfoTypeWithCons(symbol, typeRefs, cons);
    }
    
    public Symbol copy$default$1() {
        return this.symbol();
    }
    
    public Seq<Type> copy$default$2() {
        return this.typeRefs();
    }
    
    public String copy$default$3() {
        return this.cons();
    }
    
    public String productPrefix() {
        return "ClassInfoTypeWithCons";
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
                o = this.typeRefs();
                break;
            }
            case 0: {
                o = this.symbol();
                break;
            }
        }
        return o;
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ClassInfoTypeWithCons;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ClassInfoTypeWithCons) {
                final ClassInfoTypeWithCons classInfoTypeWithCons = (ClassInfoTypeWithCons)x$1;
                final Symbol symbol = this.symbol();
                final Symbol symbol2 = classInfoTypeWithCons.symbol();
                boolean b = false;
                Label_0141: {
                    Label_0140: {
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0140;
                        }
                        final Seq<Type> typeRefs = this.typeRefs();
                        final Seq<Type> typeRefs2 = classInfoTypeWithCons.typeRefs();
                        if (typeRefs == null) {
                            if (typeRefs2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!typeRefs.equals(typeRefs2)) {
                            break Label_0140;
                        }
                        final String cons = this.cons();
                        final String cons2 = classInfoTypeWithCons.cons();
                        if (cons == null) {
                            if (cons2 != null) {
                                break Label_0140;
                            }
                        }
                        else if (!cons.equals(cons2)) {
                            break Label_0140;
                        }
                        if (classInfoTypeWithCons.canEqual(this)) {
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
    
    public ClassInfoTypeWithCons(final Symbol symbol, final Seq<Type> typeRefs, final String cons) {
        this.symbol = symbol;
        this.typeRefs = typeRefs;
        this.cons = cons;
        Product$class.$init$((Product)this);
    }
}
