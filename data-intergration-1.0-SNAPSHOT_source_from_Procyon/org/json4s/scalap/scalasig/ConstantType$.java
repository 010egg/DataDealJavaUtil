// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class ConstantType$ extends AbstractFunction1<Object, ConstantType> implements Serializable
{
    public static final ConstantType$ MODULE$;
    
    static {
        new ConstantType$();
    }
    
    public final String toString() {
        return "ConstantType";
    }
    
    public ConstantType apply(final Object constant) {
        return new ConstantType(constant);
    }
    
    public Option<Object> unapply(final ConstantType x$0) {
        return (Option<Object>)((x$0 == null) ? None$.MODULE$ : new Some(x$0.constant()));
    }
    
    private Object readResolve() {
        return ConstantType$.MODULE$;
    }
    
    private ConstantType$() {
        MODULE$ = this;
    }
}
