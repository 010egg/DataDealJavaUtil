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

public final class Children$ extends AbstractFunction1<Seq<Object>, Children> implements Serializable
{
    public static final Children$ MODULE$;
    
    static {
        new Children$();
    }
    
    public final String toString() {
        return "Children";
    }
    
    public Children apply(final Seq<Object> symbolRefs) {
        return new Children(symbolRefs);
    }
    
    public Option<Seq<Object>> unapply(final Children x$0) {
        return (Option<Seq<Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.symbolRefs()));
    }
    
    private Object readResolve() {
        return Children$.MODULE$;
    }
    
    private Children$() {
        MODULE$ = this;
    }
}
