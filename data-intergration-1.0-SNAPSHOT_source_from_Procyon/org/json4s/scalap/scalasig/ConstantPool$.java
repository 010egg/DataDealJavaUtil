// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class ConstantPool$ extends AbstractFunction1<Object, ConstantPool> implements Serializable
{
    public static final ConstantPool$ MODULE$;
    
    static {
        new ConstantPool$();
    }
    
    public final String toString() {
        return "ConstantPool";
    }
    
    public ConstantPool apply(final int len) {
        return new ConstantPool(len);
    }
    
    public Option<Object> unapply(final ConstantPool x$0) {
        return (Option<Object>)((x$0 == null) ? None$.MODULE$ : new Some((Object)BoxesRunTime.boxToInteger(x$0.len())));
    }
    
    private Object readResolve() {
        return ConstantPool$.MODULE$;
    }
    
    private ConstantPool$() {
        MODULE$ = this;
    }
}
