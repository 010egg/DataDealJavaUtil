// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Function0$class;
import scala.reflect.ScalaSignature;
import scala.Function0;

@ScalaSignature(bytes = "\u0006\u0001E2Q!\u0001\u0002\u0001\u0005\u0019\u00111\u0002\u00165sK\u0006$Gj\\2bY*\u00111\u0001B\u0001\u0007UN|g\u000eN:\u000b\u0003\u0015\t1a\u001c:h+\t9\u0011cE\u0002\u0001\u0011y\u00012!\u0003\b\u0010\u001b\u0005Q!BA\u0006\r\u0003\u0011a\u0017M\\4\u000b\u00035\tAA[1wC&\u0011\u0011A\u0003\t\u0003!Ea\u0001\u0001B\u0003\u0013\u0001\t\u0007ACA\u0001B\u0007\u0001\t\"!F\u000e\u0011\u0005YIR\"A\f\u000b\u0003a\tQa]2bY\u0006L!AG\f\u0003\u000f9{G\u000f[5oOB\u0011a\u0003H\u0005\u0003;]\u00111!\u00118z!\r1rdD\u0005\u0003A]\u0011\u0011BR;oGRLwN\u001c\u0019\t\u0011\t\u0002!\u0011!S\u0001\n\r\nA!\u001b8jiB\u0019a\u0003J\b\n\u0005\u0015:\"\u0001\u0003\u001fcs:\fW.\u001a \t\u000b\u001d\u0002A\u0011\u0001\u0015\u0002\rqJg.\u001b;?)\tI3\u0006E\u0002+\u0001=i\u0011A\u0001\u0005\u0007E\u0019\"\t\u0019A\u0012\t\u000b5\u0002A\u0011\t\u0018\u0002\u0019%t\u0017\u000e^5bYZ\u000bG.^3\u0015\u0003=AQ\u0001\r\u0001\u0005\u00029\nQ!\u00199qYf\u0004")
public class ThreadLocal<A> extends java.lang.ThreadLocal<A> implements Function0<A>
{
    private final Function0<A> init;
    
    public boolean apply$mcZ$sp() {
        return Function0$class.apply$mcZ$sp((Function0)this);
    }
    
    public byte apply$mcB$sp() {
        return Function0$class.apply$mcB$sp((Function0)this);
    }
    
    public char apply$mcC$sp() {
        return Function0$class.apply$mcC$sp((Function0)this);
    }
    
    public double apply$mcD$sp() {
        return Function0$class.apply$mcD$sp((Function0)this);
    }
    
    public float apply$mcF$sp() {
        return Function0$class.apply$mcF$sp((Function0)this);
    }
    
    public int apply$mcI$sp() {
        return Function0$class.apply$mcI$sp((Function0)this);
    }
    
    public long apply$mcJ$sp() {
        return Function0$class.apply$mcJ$sp((Function0)this);
    }
    
    public short apply$mcS$sp() {
        return Function0$class.apply$mcS$sp((Function0)this);
    }
    
    public void apply$mcV$sp() {
        Function0$class.apply$mcV$sp((Function0)this);
    }
    
    public String toString() {
        return Function0$class.toString((Function0)this);
    }
    
    public A initialValue() {
        return (A)this.init.apply();
    }
    
    public A apply() {
        return this.get();
    }
    
    public ThreadLocal(final Function0<A> init) {
        this.init = init;
        Function0$class.$init$((Function0)this);
    }
}
