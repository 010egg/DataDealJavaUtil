// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import java.io.InputStream;
import scala.runtime.AbstractFunction1;

public final class StreamInput$ extends AbstractFunction1<InputStream, StreamInput> implements Serializable
{
    public static final StreamInput$ MODULE$;
    
    static {
        new StreamInput$();
    }
    
    public final String toString() {
        return "StreamInput";
    }
    
    public StreamInput apply(final InputStream stream) {
        return new StreamInput(stream);
    }
    
    public Option<InputStream> unapply(final StreamInput x$0) {
        return (Option<InputStream>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.stream()));
    }
    
    private Object readResolve() {
        return StreamInput$.MODULE$;
    }
    
    private StreamInput$() {
        MODULE$ = this;
    }
}
