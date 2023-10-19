// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction2;

public final class StringBytesPair$ extends AbstractFunction2<String, byte[], StringBytesPair> implements Serializable
{
    public static final StringBytesPair$ MODULE$;
    
    static {
        new StringBytesPair$();
    }
    
    public final String toString() {
        return "StringBytesPair";
    }
    
    public StringBytesPair apply(final String string, final byte[] bytes) {
        return new StringBytesPair(string, bytes);
    }
    
    public Option<Tuple2<String, byte[]>> unapply(final StringBytesPair x$0) {
        return (Option<Tuple2<String, byte[]>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.string(), (Object)x$0.bytes())));
    }
    
    private Object readResolve() {
        return StringBytesPair$.MODULE$;
    }
    
    private StringBytesPair$() {
        MODULE$ = this;
    }
}
