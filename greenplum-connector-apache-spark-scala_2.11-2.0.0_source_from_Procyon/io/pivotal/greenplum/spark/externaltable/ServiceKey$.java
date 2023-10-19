// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Some;
import scala.None$;
import scala.Option;
import scala.Serializable;
import scala.collection.immutable.List;
import scala.runtime.AbstractFunction1;

public final class ServiceKey$ extends AbstractFunction1<List<Object>, ServiceKey> implements Serializable
{
    public static final ServiceKey$ MODULE$;
    
    static {
        new ServiceKey$();
    }
    
    public final String toString() {
        return "ServiceKey";
    }
    
    public ServiceKey apply(final List<Object> port) {
        return new ServiceKey(port);
    }
    
    public Option<List<Object>> unapply(final ServiceKey x$0) {
        return (Option<List<Object>>)((x$0 == null) ? None$.MODULE$ : new Some((Object)x$0.port()));
    }
    
    private Object readResolve() {
        return ServiceKey$.MODULE$;
    }
    
    private ServiceKey$() {
        MODULE$ = this;
    }
}
