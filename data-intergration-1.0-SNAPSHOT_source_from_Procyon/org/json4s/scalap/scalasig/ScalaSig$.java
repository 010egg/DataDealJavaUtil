// 
// Decompiled by Procyon v0.5.36
// 

package org.json4s.scalap.scalasig;

import scala.Some;
import scala.runtime.BoxesRunTime;
import scala.None$;
import scala.Tuple3;
import scala.Option;
import scala.Serializable;
import org.json4s.scalap.$tilde;
import scala.collection.Seq;
import scala.runtime.AbstractFunction3;

public final class ScalaSig$ extends AbstractFunction3<Object, Object, Seq<$tilde<Object, ByteCode>>, ScalaSig> implements Serializable
{
    public static final ScalaSig$ MODULE$;
    
    static {
        new ScalaSig$();
    }
    
    public final String toString() {
        return "ScalaSig";
    }
    
    public ScalaSig apply(final int majorVersion, final int minorVersion, final Seq<$tilde<Object, ByteCode>> table) {
        return new ScalaSig(majorVersion, minorVersion, table);
    }
    
    public Option<Tuple3<Object, Object, Seq<$tilde<Object, ByteCode>>>> unapply(final ScalaSig x$0) {
        return (Option<Tuple3<Object, Object, Seq<$tilde<Object, ByteCode>>>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple3((Object)BoxesRunTime.boxToInteger(x$0.majorVersion()), (Object)BoxesRunTime.boxToInteger(x$0.minorVersion()), (Object)x$0.table())));
    }
    
    private Object readResolve() {
        return ScalaSig$.MODULE$;
    }
    
    private ScalaSig$() {
        MODULE$ = this;
    }
}
