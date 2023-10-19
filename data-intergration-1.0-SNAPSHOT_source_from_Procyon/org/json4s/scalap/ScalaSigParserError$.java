// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction1;

public final class ScalaSigParserError$ extends AbstractFunction1<String, ScalaSigParserError> implements Serializable
{
    public static final ScalaSigParserError$ MODULE$;
    
    static {
        new ScalaSigParserError$();
    }
    
    public final String toString() {
        return "ScalaSigParserError";
    }
    
    public ScalaSigParserError apply(final String msg) {
        return new ScalaSigParserError(msg);
    }
    
    public Option<String> unapply(final ScalaSigParserError x$0) {
        return (Option<String>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.msg()));
    }
    
    private Object readResolve() {
        return ScalaSigParserError$.MODULE$;
    }
    
    private ScalaSigParserError$() {
        MODULE$ = this;
    }
}
