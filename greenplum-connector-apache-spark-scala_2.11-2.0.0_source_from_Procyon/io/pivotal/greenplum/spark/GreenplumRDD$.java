// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import org.apache.spark.sql.jdbc.JdbcDialects$;
import org.apache.spark.sql.execution.datasources.jdbc.JDBCRDD$;
import scala.Option;
import org.apache.spark.sql.jdbc.JdbcDialect;
import org.apache.spark.sql.sources.Filter;
import scala.Serializable;

public final class GreenplumRDD$ implements Serializable
{
    public static final GreenplumRDD$ MODULE$;
    
    static {
        new GreenplumRDD$();
    }
    
    public Option<String> compileFilter(final Filter f, final JdbcDialect dialect) {
        return (Option<String>)JDBCRDD$.MODULE$.compileFilter(f, dialect);
    }
    
    public Option<String> compileFilter(final Filter f) {
        return this.compileFilter(f, JdbcDialects$.MODULE$.get("jdbc:postgresql"));
    }
    
    private Object readResolve() {
        return GreenplumRDD$.MODULE$;
    }
    
    private GreenplumRDD$() {
        MODULE$ = this;
    }
}
