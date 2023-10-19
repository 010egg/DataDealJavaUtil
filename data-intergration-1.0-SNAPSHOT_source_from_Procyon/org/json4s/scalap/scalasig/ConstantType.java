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

@ScalaSignature(bytes = "\u0006\u0001\u0005=a\u0001B\u0001\u0003\u0001.\u0011AbQ8ogR\fg\u000e\u001e+za\u0016T!a\u0001\u0003\u0002\u0011M\u001c\u0017\r\\1tS\u001eT!!\u0002\u0004\u0002\rM\u001c\u0017\r\\1q\u0015\t9\u0001\"\u0001\u0004kg>tGg\u001d\u0006\u0002\u0013\u0005\u0019qN]4\u0004\u0001M!\u0001\u0001\u0004\t\u0017!\tia\"D\u0001\u0003\u0013\ty!A\u0001\u0003UsB,\u0007CA\t\u0015\u001b\u0005\u0011\"\"A\n\u0002\u000bM\u001c\u0017\r\\1\n\u0005U\u0011\"a\u0002)s_\u0012,8\r\u001e\t\u0003#]I!\u0001\u0007\n\u0003\u0019M+'/[1mSj\f'\r\\3\t\u0011i\u0001!Q3A\u0005\u0002m\t\u0001bY8ogR\fg\u000e^\u000b\u00029A\u0011\u0011#H\u0005\u0003=I\u00111!\u00118z\u0011!\u0001\u0003A!E!\u0002\u0013a\u0012!C2p]N$\u0018M\u001c;!\u0011\u0015\u0011\u0003\u0001\"\u0001$\u0003\u0019a\u0014N\\5u}Q\u0011A%\n\t\u0003\u001b\u0001AQAG\u0011A\u0002qAqa\n\u0001\u0002\u0002\u0013\u0005\u0001&\u0001\u0003d_BLHC\u0001\u0013*\u0011\u001dQb\u0005%AA\u0002qAqa\u000b\u0001\u0012\u0002\u0013\u0005A&\u0001\bd_BLH\u0005Z3gCVdG\u000fJ\u0019\u0016\u00035R#\u0001\b\u0018,\u0003=\u0002\"\u0001M\u001b\u000e\u0003ER!AM\u001a\u0002\u0013Ut7\r[3dW\u0016$'B\u0001\u001b\u0013\u0003)\tgN\\8uCRLwN\\\u0005\u0003mE\u0012\u0011#\u001e8dQ\u0016\u001c7.\u001a3WCJL\u0017M\\2f\u0011\u001dA\u0004!!A\u0005Be\nQ\u0002\u001d:pIV\u001cG\u000f\u0015:fM&DX#\u0001\u001e\u0011\u0005m\u0002U\"\u0001\u001f\u000b\u0005ur\u0014\u0001\u00027b]\u001eT\u0011aP\u0001\u0005U\u00064\u0018-\u0003\u0002By\t11\u000b\u001e:j]\u001eDqa\u0011\u0001\u0002\u0002\u0013\u0005A)\u0001\u0007qe>$Wo\u0019;Be&$\u00180F\u0001F!\t\tb)\u0003\u0002H%\t\u0019\u0011J\u001c;\t\u000f%\u0003\u0011\u0011!C\u0001\u0015\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HC\u0001\u000fL\u0011\u001da\u0005*!AA\u0002\u0015\u000b1\u0001\u001f\u00132\u0011\u001dq\u0005!!A\u0005B=\u000bq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002!B\u0019\u0011\u000b\u0016\u000f\u000e\u0003IS!a\u0015\n\u0002\u0015\r|G\u000e\\3di&|g.\u0003\u0002V%\nA\u0011\n^3sCR|'\u000fC\u0004X\u0001\u0005\u0005I\u0011\u0001-\u0002\u0011\r\fg.R9vC2$\"!\u0017/\u0011\u0005EQ\u0016BA.\u0013\u0005\u001d\u0011un\u001c7fC:Dq\u0001\u0014,\u0002\u0002\u0003\u0007A\u0004C\u0004_\u0001\u0005\u0005I\u0011I0\u0002\u0011!\f7\u000f[\"pI\u0016$\u0012!\u0012\u0005\bC\u0002\t\t\u0011\"\u0011c\u0003!!xn\u0015;sS:<G#\u0001\u001e\t\u000f\u0011\u0004\u0011\u0011!C!K\u00061Q-];bYN$\"!\u00174\t\u000f1\u001b\u0017\u0011!a\u00019\u001d9\u0001NAA\u0001\u0012\u0003I\u0017\u0001D\"p]N$\u0018M\u001c;UsB,\u0007CA\u0007k\r\u001d\t!!!A\t\u0002-\u001c2A\u001b7\u0017!\u0011i\u0007\u000f\b\u0013\u000e\u00039T!a\u001c\n\u0002\u000fI,h\u000e^5nK&\u0011\u0011O\u001c\u0002\u0012\u0003\n\u001cHO]1di\u001a+hn\u0019;j_:\f\u0004\"\u0002\u0012k\t\u0003\u0019H#A5\t\u000f\u0005T\u0017\u0011!C#E\"9aO[A\u0001\n\u0003;\u0018!B1qa2LHC\u0001\u0013y\u0011\u0015QR\u000f1\u0001\u001d\u0011\u001dQ(.!A\u0005\u0002n\fq!\u001e8baBd\u0017\u0010\u0006\u0002}\u007fB\u0019\u0011# \u000f\n\u0005y\u0014\"AB(qi&|g\u000e\u0003\u0005\u0002\u0002e\f\t\u00111\u0001%\u0003\rAH\u0005\r\u0005\n\u0003\u000bQ\u0017\u0011!C\u0005\u0003\u000f\t1B]3bIJ+7o\u001c7wKR\u0011\u0011\u0011\u0002\t\u0004w\u0005-\u0011bAA\u0007y\t1qJ\u00196fGR\u0004")
public class ConstantType extends Type implements Product, Serializable
{
    private final Object constant;
    
    public static Option<Object> unapply(final ConstantType x$0) {
        return ConstantType$.MODULE$.unapply(x$0);
    }
    
    public static ConstantType apply(final Object constant) {
        return ConstantType$.MODULE$.apply(constant);
    }
    
    public static <A> Function1<Object, A> andThen(final Function1<ConstantType, A> function1) {
        return (Function1<Object, A>)ConstantType$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ConstantType> compose(final Function1<A, Object> function1) {
        return (Function1<A, ConstantType>)ConstantType$.MODULE$.compose((Function1)function1);
    }
    
    public Object constant() {
        return this.constant;
    }
    
    public ConstantType copy(final Object constant) {
        return new ConstantType(constant);
    }
    
    public Object copy$default$1() {
        return this.constant();
    }
    
    public String productPrefix() {
        return "ConstantType";
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
                return this.constant();
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ConstantType;
    }
    
    public int hashCode() {
        return ScalaRunTime$.MODULE$._hashCode((Product)this);
    }
    
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ConstantType) {
                final ConstantType constantType = (ConstantType)x$1;
                if (BoxesRunTime.equals(this.constant(), constantType.constant()) && constantType.canEqual(this)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public ConstantType(final Object constant) {
        this.constant = constant;
        Product$class.$init$((Product)this);
    }
}
