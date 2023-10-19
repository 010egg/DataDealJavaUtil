// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class StringInput$ extends AbstractFunction1<String, StringInput> implements Serializable
{
    public static final StringInput$ MODULE$;
    
    static {
        new StringInput$();
    }
    
    public final String toString() {
        return "StringInput";
    }
    
    public StringInput apply(final String string) {
        return new StringInput(string);
    }
    
    public Option<String> unapply(final StringInput x$0) {
        return (Option<String>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.string()));
    }
    
    private Object readResolve() {
        return StringInput$.MODULE$;
    }
    
    private StringInput$() {
        MODULE$ = this;
    }
}
