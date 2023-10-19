// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.collection.Seq;
import scala.runtime.AbstractFunction1;

public final class AnnotInfo$ extends AbstractFunction1<Seq<Object>, AnnotInfo> implements Serializable
{
    public static final AnnotInfo$ MODULE$;
    
    static {
        new AnnotInfo$();
    }
    
    public final String toString() {
        return "AnnotInfo";
    }
    
    public AnnotInfo apply(final Seq<Object> refs) {
        return new AnnotInfo(refs);
    }
    
    public Option<Seq<Object>> unapply(final AnnotInfo x$0) {
        return (Option<Seq<Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.refs()));
    }
    
    private Object readResolve() {
        return AnnotInfo$.MODULE$;
    }
    
    private AnnotInfo$() {
        MODULE$ = this;
    }
}
