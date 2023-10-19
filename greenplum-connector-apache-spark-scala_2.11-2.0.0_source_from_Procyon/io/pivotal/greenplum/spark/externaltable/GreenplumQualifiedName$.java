// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Some;
import scala.None$;
import scala.Tuple2;
import scala.Option;
import scala.Serializable;
import scala.runtime.AbstractFunction2;

public final class GreenplumQualifiedName$ extends AbstractFunction2<String, String, GreenplumQualifiedName> implements Serializable
{
    public static final GreenplumQualifiedName$ MODULE$;
    
    static {
        new GreenplumQualifiedName$();
    }
    
    public final String toString() {
        return "GreenplumQualifiedName";
    }
    
    public GreenplumQualifiedName apply(final String schema, final String name) {
        return new GreenplumQualifiedName(schema, name);
    }
    
    public Option<Tuple2<String, String>> unapply(final GreenplumQualifiedName x$0) {
        return (Option<Tuple2<String, String>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)new Tuple2((Object)x$0.schema(), (Object)x$0.name())));
    }
    
    private Object readResolve() {
        return GreenplumQualifiedName$.MODULE$;
    }
    
    private GreenplumQualifiedName$() {
        MODULE$ = this;
    }
}
