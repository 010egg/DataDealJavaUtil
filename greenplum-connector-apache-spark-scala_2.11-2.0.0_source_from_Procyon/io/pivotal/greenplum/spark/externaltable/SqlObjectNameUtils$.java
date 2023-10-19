// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import scala.collection.immutable.StringOps;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;

public final class SqlObjectNameUtils$
{
    public static final SqlObjectNameUtils$ MODULE$;
    
    static {
        new SqlObjectNameUtils$();
    }
    
    public String escape(final String s) {
        return new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "\"", "\"" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { new StringOps(Predef$.MODULE$.augmentString(s)).replaceAllLiterally("\"", "\"\"") }));
    }
    
    private SqlObjectNameUtils$() {
        MODULE$ = this;
    }
}
