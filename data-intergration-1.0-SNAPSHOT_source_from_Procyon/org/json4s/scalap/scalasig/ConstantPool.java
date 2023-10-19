// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.reflect.ClassTag$;
import scala.Array$;
import scala.Product$class;
import scala.runtime.Statics;
import scala.runtime.ScalaRunTime$;
import scala.collection.Iterator;
import scala.runtime.BoxesRunTime;
import scala.Function0;
import scala.Option;
import scala.Function1;
import scala.collection.mutable.ArrayBuffer;
import scala.reflect.ScalaSignature;
import scala.Serializable;
import scala.Product;

@ScalaSignature(bytes = "\u0006\u0001\u0005=c\u0001B\u0001\u0003\u0001.\u0011AbQ8ogR\fg\u000e\u001e)p_2T!a\u0001\u0003\u0002\u0011M\u001c\u0017\r\\1tS\u001eT!!\u0002\u0004\u0002\rM\u001c\u0017\r\\1q\u0015\t9\u0001\"\u0001\u0004kg>tGg\u001d\u0006\u0002\u0013\u0005\u0019qN]4\u0004\u0001M!\u0001\u0001\u0004\n\u0016!\ti\u0001#D\u0001\u000f\u0015\u0005y\u0011!B:dC2\f\u0017BA\t\u000f\u0005\u0019\te.\u001f*fMB\u0011QbE\u0005\u0003)9\u0011q\u0001\u0015:pIV\u001cG\u000f\u0005\u0002\u000e-%\u0011qC\u0004\u0002\r'\u0016\u0014\u0018.\u00197ju\u0006\u0014G.\u001a\u0005\t3\u0001\u0011)\u001a!C\u00015\u0005\u0019A.\u001a8\u0016\u0003m\u0001\"!\u0004\u000f\n\u0005uq!aA%oi\"Aq\u0004\u0001B\tB\u0003%1$\u0001\u0003mK:\u0004\u0003\"B\u0011\u0001\t\u0003\u0011\u0013A\u0002\u001fj]&$h\b\u0006\u0002$KA\u0011A\u0005A\u0007\u0002\u0005!)\u0011\u0004\ta\u00017!9q\u0005\u0001b\u0001\n\u0003Q\u0012\u0001B:ju\u0016Da!\u000b\u0001!\u0002\u0013Y\u0012!B:ju\u0016\u0004\u0003BB\u0016\u0001A\u0003%A&\u0001\u0004ck\u001a4WM\u001d\t\u0004[I\"T\"\u0001\u0018\u000b\u0005=\u0002\u0014aB7vi\u0006\u0014G.\u001a\u0006\u0003c9\t!bY8mY\u0016\u001cG/[8o\u0013\t\u0019dFA\u0006BeJ\f\u0017PQ;gM\u0016\u0014\b\u0003B\u00076G]J!A\u000e\b\u0003\u0013\u0019+hn\u0019;j_:\f\u0004CA\u00079\u0013\tIdBA\u0002B]fDaa\u000f\u0001!\u0002\u0013a\u0014A\u0002<bYV,7\u000fE\u0002\u000e{}J!A\u0010\b\u0003\u000b\u0005\u0013(/Y=\u0011\u00075\u0001u'\u0003\u0002B\u001d\t1q\n\u001d;j_:DQa\u0011\u0001\u0005\u0002\u0011\u000ba![:Gk2dW#A#\u0011\u000551\u0015BA$\u000f\u0005\u001d\u0011un\u001c7fC:DQ!\u0013\u0001\u0005\u0002)\u000bQ!\u00199qYf$\"aN&\t\u000b1C\u0005\u0019A\u000e\u0002\u000b%tG-\u001a=\t\u000b9\u0003A\u0011A(\u0002\u0007\u0005$G\r\u0006\u0002$!\")\u0011+\u0014a\u0001i\u0005\ta\rC\u0004T\u0001\u0005\u0005I\u0011\u0001+\u0002\t\r|\u0007/\u001f\u000b\u0003GUCq!\u0007*\u0011\u0002\u0003\u00071\u0004C\u0004X\u0001E\u0005I\u0011\u0001-\u0002\u001d\r|\u0007/\u001f\u0013eK\u001a\fW\u000f\u001c;%cU\t\u0011L\u000b\u0002\u001c5.\n1\f\u0005\u0002]C6\tQL\u0003\u0002_?\u0006IQO\\2iK\u000e\\W\r\u001a\u0006\u0003A:\t!\"\u00198o_R\fG/[8o\u0013\t\u0011WLA\tv]\u000eDWmY6fIZ\u000b'/[1oG\u0016Dq\u0001\u001a\u0001\u0002\u0002\u0013\u0005S-A\u0007qe>$Wo\u0019;Qe\u00164\u0017\u000e_\u000b\u0002MB\u0011q\r\\\u0007\u0002Q*\u0011\u0011N[\u0001\u0005Y\u0006twMC\u0001l\u0003\u0011Q\u0017M^1\n\u00055D'AB*ue&tw\rC\u0004p\u0001\u0005\u0005I\u0011\u0001\u000e\u0002\u0019A\u0014x\u000eZ;di\u0006\u0013\u0018\u000e^=\t\u000fE\u0004\u0011\u0011!C\u0001e\u0006q\u0001O]8ek\u000e$X\t\\3nK:$HCA\u001ct\u0011\u001d!\b/!AA\u0002m\t1\u0001\u001f\u00132\u0011\u001d1\b!!A\u0005B]\fq\u0002\u001d:pIV\u001cG/\u0013;fe\u0006$xN]\u000b\u0002qB\u0019\u0011P_\u001c\u000e\u0003AJ!a\u001f\u0019\u0003\u0011%#XM]1u_JDq! \u0001\u0002\u0002\u0013\u0005a0\u0001\u0005dC:,\u0015/^1m)\t)u\u0010C\u0004uy\u0006\u0005\t\u0019A\u001c\t\u0013\u0005\r\u0001!!A\u0005B\u0005\u0015\u0011\u0001\u00035bg\"\u001cu\u000eZ3\u0015\u0003mA\u0011\"!\u0003\u0001\u0003\u0003%\t%a\u0003\u0002\u0011Q|7\u000b\u001e:j]\u001e$\u0012A\u001a\u0005\n\u0003\u001f\u0001\u0011\u0011!C!\u0003#\ta!Z9vC2\u001cHcA#\u0002\u0014!AA/!\u0004\u0002\u0002\u0003\u0007qgB\u0005\u0002\u0018\t\t\t\u0011#\u0001\u0002\u001a\u0005a1i\u001c8ti\u0006tG\u000fU8pYB\u0019A%a\u0007\u0007\u0011\u0005\u0011\u0011\u0011!E\u0001\u0003;\u0019R!a\u0007\u0002 U\u0001b!!\t\u0002(m\u0019SBAA\u0012\u0015\r\t)CD\u0001\beVtG/[7f\u0013\u0011\tI#a\t\u0003#\u0005\u00137\u000f\u001e:bGR4UO\\2uS>t\u0017\u0007C\u0004\"\u00037!\t!!\f\u0015\u0005\u0005e\u0001BCA\u0005\u00037\t\t\u0011\"\u0012\u0002\f!I\u0011*a\u0007\u0002\u0002\u0013\u0005\u00151\u0007\u000b\u0004G\u0005U\u0002BB\r\u00022\u0001\u00071\u0004\u0003\u0006\u0002:\u0005m\u0011\u0011!CA\u0003w\tq!\u001e8baBd\u0017\u0010\u0006\u0003\u0002>\u0005}\u0002cA\u0007A7!I\u0011\u0011IA\u001c\u0003\u0003\u0005\raI\u0001\u0004q\u0012\u0002\u0004BCA#\u00037\t\t\u0011\"\u0003\u0002H\u0005Y!/Z1e%\u0016\u001cx\u000e\u001c<f)\t\tI\u0005E\u0002h\u0003\u0017J1!!\u0014i\u0005\u0019y%M[3di\u0002")
public class ConstantPool implements Product, Serializable
{
    private final int len;
    private final int size;
    public final ArrayBuffer<Function1<ConstantPool, Object>> org$json4s$scalap$scalasig$ConstantPool$$buffer;
    public final Option<Object>[] org$json4s$scalap$scalasig$ConstantPool$$values;
    
    public static Option<Object> unapply(final ConstantPool x$0) {
        return ConstantPool$.MODULE$.unapply(x$0);
    }
    
    public static <A> Function1<Object, A> andThen(final Function1<ConstantPool, A> function1) {
        return (Function1<Object, A>)ConstantPool$.MODULE$.andThen((Function1)function1);
    }
    
    public static <A> Function1<A, ConstantPool> compose(final Function1<A, Object> function1) {
        return (Function1<A, ConstantPool>)ConstantPool$.MODULE$.compose((Function1)function1);
    }
    
    public int len() {
        return this.len;
    }
    
    public int size() {
        return this.size;
    }
    
    public boolean isFull() {
        return this.org$json4s$scalap$scalasig$ConstantPool$$buffer.length() >= this.size();
    }
    
    public Object apply(final int index) {
        final int i = index - 1;
        return this.org$json4s$scalap$scalasig$ConstantPool$$values[i].getOrElse((Function0)new ConstantPool$$anonfun$apply.ConstantPool$$anonfun$apply$40(this, i));
    }
    
    public ConstantPool add(final Function1<ConstantPool, Object> f) {
        this.org$json4s$scalap$scalasig$ConstantPool$$buffer.$plus$eq((Object)f);
        return this;
    }
    
    public ConstantPool copy(final int len) {
        return new ConstantPool(len);
    }
    
    public int copy$default$1() {
        return this.len();
    }
    
    public String productPrefix() {
        return "ConstantPool";
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
                return BoxesRunTime.boxToInteger(this.len());
            }
        }
    }
    
    public Iterator<Object> productIterator() {
        return (Iterator<Object>)ScalaRunTime$.MODULE$.typedProductIterator((Product)this);
    }
    
    public boolean canEqual(final Object x$1) {
        return x$1 instanceof ConstantPool;
    }
    
    @Override
    public int hashCode() {
        return Statics.finalizeHash(Statics.mix(-889275714, this.len()), 1);
    }
    
    @Override
    public String toString() {
        return ScalaRunTime$.MODULE$._toString((Product)this);
    }
    
    @Override
    public boolean equals(final Object x$1) {
        if (this != x$1) {
            if (x$1 instanceof ConstantPool) {
                final ConstantPool constantPool = (ConstantPool)x$1;
                if (this.len() == constantPool.len() && constantPool.canEqual(this)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public ConstantPool(final int len) {
        this.len = len;
        Product$class.$init$((Product)this);
        this.size = len - 1;
        this.org$json4s$scalap$scalasig$ConstantPool$$buffer = (ArrayBuffer<Function1<ConstantPool, Object>>)new ArrayBuffer();
        this.org$json4s$scalap$scalasig$ConstantPool$$values = (Option<Object>[])Array$.MODULE$.fill(this.size(), (Function0)new ConstantPool$$anonfun.ConstantPool$$anonfun$61(this), ClassTag$.MODULE$.apply((Class)Option.class));
    }
}
