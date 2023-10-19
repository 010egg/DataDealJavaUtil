// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.Serializable;
import scala.collection.immutable.List;
import scala.runtime.AbstractFunction3;

public final class AnnotatedWithSelfType$ extends AbstractFunction3<Type, Symbol, List<Object>, AnnotatedWithSelfType> implements Serializable
{
    public static final AnnotatedWithSelfType$ MODULE$;
    
    static {
        new AnnotatedWithSelfType$();
    }
    
    public final String toString() {
        return "AnnotatedWithSelfType";
    }
    
    public AnnotatedWithSelfType apply(final Type typeRef, final Symbol symbol, final List<Object> attribTreeRefs) {
        return new AnnotatedWithSelfType(typeRef, symbol, attribTreeRefs);
    }
    
    public Option<Tuple3<Type, Symbol, List<Object>>> unapply(final AnnotatedWithSelfType x$0) {
        return (Option<Tuple3<Type, Symbol, List<Object>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.typeRef(), (Object)x$0.symbol(), (Object)x$0.attribTreeRefs())));
    }
    
    private Object readResolve() {
        return AnnotatedWithSelfType$.MODULE$;
    }
    
    private AnnotatedWithSelfType$() {
        MODULE$ = this;
    }
}
