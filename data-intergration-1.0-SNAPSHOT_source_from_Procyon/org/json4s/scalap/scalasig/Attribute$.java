// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction2;

public final class Attribute$ extends AbstractFunction2<Object, ByteCode, Attribute> implements Serializable
{
    public static final Attribute$ MODULE$;
    
    static {
        new Attribute$();
    }
    
    public final String toString() {
        return "Attribute";
    }
    
    public Attribute apply(final int nameIndex, final ByteCode byteCode) {
        return new Attribute(nameIndex, byteCode);
    }
    
    public Option<Tuple2<Object, ByteCode>> unapply(final Attribute x$0) {
        return (Option<Tuple2<Object, ByteCode>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)BoxesRunTime.boxToInteger(x$0.nameIndex()), (Object)x$0.byteCode())));
    }
    
    private Object readResolve() {
        return Attribute$.MODULE$;
    }
    
    private Attribute$() {
        MODULE$ = this;
    }
}
