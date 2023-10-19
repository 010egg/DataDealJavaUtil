// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction3;

public final class ClassInfoTypeWithCons$ extends AbstractFunction3<Symbol, Seq<Type>, String, ClassInfoTypeWithCons> implements Serializable
{
    public static final ClassInfoTypeWithCons$ MODULE$;
    
    static {
        new ClassInfoTypeWithCons$();
    }
    
    public final String toString() {
        return "ClassInfoTypeWithCons";
    }
    
    public ClassInfoTypeWithCons apply(final Symbol symbol, final Seq<Type> typeRefs, final String cons) {
        return new ClassInfoTypeWithCons(symbol, typeRefs, cons);
    }
    
    public Option<Tuple3<Symbol, Seq<Type>, String>> unapply(final ClassInfoTypeWithCons x$0) {
        return (Option<Tuple3<Symbol, Seq<Type>, String>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)x$0.symbol(), (Object)x$0.typeRefs(), (Object)x$0.cons())));
    }
    
    private Object readResolve() {
        return ClassInfoTypeWithCons$.MODULE$;
    }
    
    private ClassInfoTypeWithCons$() {
        MODULE$ = this;
    }
}
