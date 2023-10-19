// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class NullaryMethodType$ extends AbstractFunction1<Type, NullaryMethodType> implements Serializable
{
    public static final NullaryMethodType$ MODULE$;
    
    static {
        new NullaryMethodType$();
    }
    
    public final String toString() {
        return "NullaryMethodType";
    }
    
    public NullaryMethodType apply(final Type resultType) {
        return new NullaryMethodType(resultType);
    }
    
    public Option<Type> unapply(final NullaryMethodType x$0) {
        return (Option<Type>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.resultType()));
    }
    
    private Object readResolve() {
        return NullaryMethodType$.MODULE$;
    }
    
    private NullaryMethodType$() {
        MODULE$ = this;
    }
}
