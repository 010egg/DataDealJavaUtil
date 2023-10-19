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

public final class Field$ extends AbstractFunction4<Object, Object, Object, Seq<Attribute>, Field> implements Serializable
{
    public static final Field$ MODULE$;
    
    static {
        new Field$();
    }
    
    public final String toString() {
        return "Field";
    }
    
    public Field apply(final int flags, final int nameIndex, final int descriptorIndex, final Seq<Attribute> attributes) {
        return new Field(flags, nameIndex, descriptorIndex, attributes);
    }
    
    public Option<Tuple4<Object, Object, Object, Seq<Attribute>>> unapply(final Field x$0) {
        return (Option<Tuple4<Object, Object, Object, Seq<Attribute>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple4((Object)BoxesRunTime.boxToInteger(x$0.flags()), (Object)BoxesRunTime.boxToInteger(x$0.nameIndex()), (Object)BoxesRunTime.boxToInteger(x$0.descriptorIndex()), (Object)x$0.attributes())));
    }
    
    private Object readResolve() {
        return Field$.MODULE$;
    }
    
    private Field$() {
        MODULE$ = this;
    }
}
