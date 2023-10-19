// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.collection.immutable.List;
import scala.runtime.AbstractFunction2;

public final class AnnotatedType$ extends AbstractFunction2<Type, List<Object>, AnnotatedType> implements Serializable
{
    public static final AnnotatedType$ MODULE$;
    
    static {
        new AnnotatedType$();
    }
    
    public final String toString() {
        return "AnnotatedType";
    }
    
    public AnnotatedType apply(final Type typeRef, final List<Object> attribTreeRefs) {
        return new AnnotatedType(typeRef, attribTreeRefs);
    }
    
    public Option<Tuple2<Type, List<Object>>> unapply(final AnnotatedType x$0) {
        return (Option<Tuple2<Type, List<Object>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.typeRef(), (Object)x$0.attribTreeRefs())));
    }
    
    private Object readResolve() {
        return AnnotatedType$.MODULE$;
    }
    
    private AnnotatedType$() {
        MODULE$ = this;
    }
}
