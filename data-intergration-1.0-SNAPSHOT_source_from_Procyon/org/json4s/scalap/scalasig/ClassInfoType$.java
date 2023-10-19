// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction2;

public final class ClassInfoType$ extends AbstractFunction2<Symbol, Seq<Type>, ClassInfoType> implements Serializable
{
    public static final ClassInfoType$ MODULE$;
    
    static {
        new ClassInfoType$();
    }
    
    public final String toString() {
        return "ClassInfoType";
    }
    
    public ClassInfoType apply(final Symbol symbol, final Seq<Type> typeRefs) {
        return new ClassInfoType(symbol, typeRefs);
    }
    
    public Option<Tuple2<Symbol, Seq<Type>>> unapply(final ClassInfoType x$0) {
        return (Option<Tuple2<Symbol, Seq<Type>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.symbol(), (Object)x$0.typeRefs())));
    }
    
    private Object readResolve() {
        return ClassInfoType$.MODULE$;
    }
    
    private ClassInfoType$() {
        MODULE$ = this;
    }
}
