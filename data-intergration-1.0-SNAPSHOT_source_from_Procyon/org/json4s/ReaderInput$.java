// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import java.io.Reader;
import scala.runtime.AbstractFunction1;

public final class ReaderInput$ extends AbstractFunction1<Reader, ReaderInput> implements Serializable
{
    public static final ReaderInput$ MODULE$;
    
    static {
        new ReaderInput$();
    }
    
    public final String toString() {
        return "ReaderInput";
    }
    
    public ReaderInput apply(final Reader reader) {
        return new ReaderInput(reader);
    }
    
    public Option<Reader> unapply(final ReaderInput x$0) {
        return (Option<Reader>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.reader()));
    }
    
    private Object readResolve() {
        return ReaderInput$.MODULE$;
    }
    
    private ReaderInput$() {
        MODULE$ = this;
    }
}
