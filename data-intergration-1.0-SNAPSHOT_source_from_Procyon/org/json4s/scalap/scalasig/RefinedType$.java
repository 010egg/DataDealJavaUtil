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

public final class RefinedType$ extends AbstractFunction2<Symbol, List<Type>, RefinedType> implements Serializable
{
    public static final RefinedType$ MODULE$;
    
    static {
        new RefinedType$();
    }
    
    public final String toString() {
        return "RefinedType";
    }
    
    public RefinedType apply(final Symbol classSym, final List<Type> typeRefs) {
        return new RefinedType(classSym, typeRefs);
    }
    
    public Option<Tuple2<Symbol, List<Type>>> unapply(final RefinedType x$0) {
        return (Option<Tuple2<Symbol, List<Type>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.classSym(), (Object)x$0.typeRefs())));
    }
    
    private Object readResolve() {
        return RefinedType$.MODULE$;
    }
    
    private RefinedType$() {
        MODULE$ = this;
    }
}
