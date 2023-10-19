// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple4;
import scala.Serializable;
import org.json4s.scalap.$tilde;
import scala.collection.Seq;
import scala.Option;
import scala.runtime.AbstractFunction4;

public final class AttributeInfo$ extends AbstractFunction4<Symbol, Type, Option<Object>, Seq<$tilde<String, Object>>, AttributeInfo> implements Serializable
{
    public static final AttributeInfo$ MODULE$;
    
    static {
        new AttributeInfo$();
    }
    
    public final String toString() {
        return "AttributeInfo";
    }
    
    public AttributeInfo apply(final Symbol symbol, final Type typeRef, final Option<Object> value, final Seq<$tilde<String, Object>> values) {
        return new AttributeInfo(symbol, typeRef, value, values);
    }
    
    public Option<Tuple4<Symbol, Type, Option<Object>, Seq<$tilde<String, Object>>>> unapply(final AttributeInfo x$0) {
        return (Option<Tuple4<Symbol, Type, Option<Object>, Seq<$tilde<String, Object>>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple4((Object)x$0.symbol(), (Object)x$0.typeRef(), (Object)x$0.value(), (Object)x$0.values())));
    }
    
    private Object readResolve() {
        return AttributeInfo$.MODULE$;
    }
    
    private AttributeInfo$() {
        MODULE$ = this;
    }
}
