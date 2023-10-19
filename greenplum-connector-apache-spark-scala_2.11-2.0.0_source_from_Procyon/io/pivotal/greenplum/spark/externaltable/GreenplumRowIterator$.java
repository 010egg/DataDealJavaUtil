// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.Function1;
import scala.reflect.ClassTag$;
import scala.Array$;
import scala.Predef$;
import org.apache.spark.sql.sources.Filter;

public final class GreenplumRowIterator$
{
    public static final GreenplumRowIterator$ MODULE$;
    
    static {
        new GreenplumRowIterator$();
    }
    
    public String filterWherePredicate(final Filter[] filters) {
        return Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])filters).flatMap((Function1)new GreenplumRowIterator$$anonfun$filterWherePredicate.GreenplumRowIterator$$anonfun$filterWherePredicate$1(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)String.class)))).map((Function1)new GreenplumRowIterator$$anonfun$filterWherePredicate.GreenplumRowIterator$$anonfun$filterWherePredicate$2(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)String.class)))).mkString(" AND ");
    }
    
    private GreenplumRowIterator$() {
        MODULE$ = this;
    }
}
