// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple4;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction4;

public final class Method$ extends AbstractFunction4<Object, Object, Object, Seq<Attribute>, Method> implements Serializable
{
    public static final Method$ MODULE$;
    
    static {
        new Method$();
    }
    
    public final String toString() {
        return "Method";
    }
    
    public Method apply(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        return new Method(flags, nameIndex, descriptorIndex, attributes);
    }
    
    public Option<Tuple4<Object, Object, Object, Seq<Attribute>>> unapply(final Method x$0) {
        return (Option<Tuple4<Object, Object, Object, Seq<Attribute>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple4((Object)BoxesRunTime.boxToInteger(x$0.flags()), (Object)BoxesRunTime.boxToInteger(x$0.nameIndex()), (Object)BoxesRunTime.boxToInteger(x$0.descriptorIndex()), (Object)x$0.attributes())));
    }
    
    private Object readResolve() {
        return Method$.MODULE$;
    }
    
    private Method$() {
        MODULE$ = this;
    }
}
