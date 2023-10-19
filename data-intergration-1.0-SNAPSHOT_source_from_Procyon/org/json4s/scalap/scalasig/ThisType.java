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

@ScalaSignature(bytes = "\u0006\u0001\u0005Ua\u0001B\u0001\u0003\u0001.\u0011\u0001\u0002\u00165jgRK\b/\u001a\u0006\u0003\u0007\u0011\t\u0001b]2bY\u0006\u001c\u0018n\u001a\u0006\u0003\u000b\u0019\taa]2bY\u0006\u0004(BA\u0004\t\u0003\u0019Q7o\u001c85g*\t\u0011\"A\u0002pe\u001e\u001c\u0001a\u0005\u0003\u0001\u0019A1\u0002CA\u0007\u000f\u001b\u0005\u0011\u0011BA\b\u0003\u0005\u0011!\u0016\u0010]3\u0011\u0005E!R\"\u0001\n\u000b\u0003M\tQa]2bY\u0006L!!\u0006\n\u0003\u000fA\u0013x\u000eZ;diB\u0011\u0011cF\u0005\u00031I\u0011AbU3sS\u0006d\u0017N_1cY\u0016D\u0001B\u0007\u0001\u0003\u0016\u0004%\taG\u0001\u0007gfl'm\u001c7\u0016\u0003q\u0001\"!D\u000f\n\u0005y\u0011!AB*z[\n|G\u000e\u0003\u0005!\u0001\tE\t\u0015!\u0003\u001d\u0003\u001d\u0019\u00180\u001c2pY\u0002BQA\t\u0001\u0005\u0002\r\na\u0001P5oSRtDC\u0001\u0013&!\ti\u0001\u0001C\u0003\u001bC\u0001\u0007A\u0004C\u0004(\u0001\u0005\u0005I\u0011\u0001\u0015\u0002\t\r|\u0007/\u001f\u000b\u0003I%BqA\u0007\u0014\u0011\u0002\u0003\u0007A\u0004C\u0004,\u0001E\u0005I\u0011\u0001\u0017\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\tQF\u000b\u0002\u001d]-\nq\u0006\u0005\u00021k5\t\u0011G\u0003\u00023g\u0005IQO\\2iK\u000e\\W\r\u001a\u0006\u0003iI\t!\"\u00198o_R\fG/[8o\u0013\t1\u0014GA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq\u0001\u000f\u0001\u0002\u0002\u0013\u0005\u0013(A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002uA\u00111\bQ\u0007\u0002y)\u0011QHP\u0001\u0005Y\u0006twMC\u0001@\u0003\u0011Q\u0017M^1\n\u0005\u0005c$AB*ue&tw\rC\u0004D\u0001\u0005\u0005I\u0011\u0001#\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\u0016\u0003\u0015\u0003\"!\u0005$\n\u0005\u001d\u0013\"aA%oi\"9\u0011\nAA\u0001\n\u0003Q\u0015A\u00049s_\u0012,8\r^#mK6,g\u000e\u001e\u000b\u0003\u0017:\u0003\"!\u0005'\n\u00055\u0013\"aA!os\"9q\nSA\u0001\u0002\u0004)\u0015a\u0001=%c!9\u0011\u000bAA\u0001\n\u0003\u0012\u0016a\u00049s_\u0012,8\r^%uKJ\fGo\u001c:\u0016\u0003M\u00032\u0001V,L\u001b\u0005)&B\u0001,\u0013\u0003)\u0019w\u000e\u001c7fGRLwN\\\u0005\u00031V\u0013\u0001\"\u0013;fe\u0006$xN\u001d\u0005\b5\u0002\t\t\u0011\"\u0001\\\u0003!\u0019\u0017M\\#rk\u0006dGC\u0001/`!\t\tR,\u0003\u0002_%\t9!i\\8mK\u0006t\u0007bB(Z\u0003\u0003\u0005\ra\u0013\u0005\bC\u0002\t\t\u0011\"\u0011c\u0003!A\u0017m\u001d5D_\u0012,G#A#\t\u000f\u0011\u0004\u0011\u0011!C!K\u0006AAo\\*ue&tw\rF\u0001;\u0011\u001d9\u0007!!A\u0005B!\fa!Z9vC2\u001cHC\u0001/j\u0011\u001dye-!AA\u0002-;qa\u001b\u0002\u0002\u0002#\u0005A.\u0001\u0005UQ&\u001cH+\u001f9f!\tiQNB\u0004\u0002\u0005\u0005\u0005\t\u0012\u00018\u0014\u00075|g\u0003\u0005\u0003qgr!S\"A9\u000b\u0005I\u0014\u0012a\u0002:v]RLW.Z\u0005\u0003iF\u0014\u0011#\u00112tiJ\f7\r\u001e$v]\u000e$\u0018n\u001c82\u0011\u0015\u0011S\u000e\"\u0001w)\u0005a\u0007b\u00023n\u0003\u0003%)%\u001a\u0005\bs6\f\t\u0011\"!{\u0003\u0015\t\u0007\u000f\u001d7z)\t!3\u0010C\u0003\u001bq\u0002\u0007A\u0004C\u0004~[\u0006\u0005I\u0011\u0011@\u0002\u000fUt\u0017\r\u001d9msR\u0019q0!\u0002\u0011\tE\t\t\u0001H\u0005\u0004\u0003\u0007\u0011\"AB(qi&|g\u000e\u0003\u0005\u0002\bq\f\t\u00111\u0001%\u0003\rAH\u0005\r\u0005\n\u0003\u0017i\u0017\u0011!C\u0005\u0003\u001b\t1B]3bIJ+7o\u001c7wKR\u0011\u0011q\u0002\t\u0004w\u0005E\u0011bAA\ny\t1qJ\u00196fGR\u0004")
public class ThisType extends Type implements Product, Serializable
{
    private final Symbol symbol;
    
    public static Option<Symbol> unapply(final ThisType x$0) {
        return ThisType$.MODULE$.unapply(x$0);
    }
    
    public static ThisType apply(final Symbol symbol) {
        return ThisType$.MODULE$.apply(symbol);
    }
    
    public static <A> Function1<Symbol, A> andThen(final Function1<ThisType, A> function1) {
        return (Function1<Symbol, A>)ThisType$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ThisType> compose(final Function1<A, Symbol> function1) {
        return (Function1<A, ThisType>)ThisType$.MODULE$.compose((Function1)function1);
    }
    
    public Symbol symbol() {
        return this.symbol;
    }
    
    public ThisType copy(final Symbol symbol) {
        return new ThisType(symbol);
    }
    
    public Symbol copy$default$1() {
        return this.symbol();
    }
    
    public String productPrefix() {
        return "ThisType";
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
                return this.symbol();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ThisType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ThisType) {
                final ThisType thisType = (ThisType)x$1;
                final Symbol symbol = this.symbol();
                final Symbol symbol2 = thisType.symbol();
                boolean b = false;
                Label_0077: {
                    Label_0076: {
                        if (symbol == null) {
                            if (symbol2 != null) {
                                break Label_0076;
                            }
                        }
                        else if (!symbol.equals(symbol2)) {
                            break Label_0076;
                        }
                        if (thisType.canEqual(this)) {
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
    
    public ThisType(final Symbol symbol) {
        this.symbol = symbol;
        Product$class.$init$((Product)this);
    }
}
