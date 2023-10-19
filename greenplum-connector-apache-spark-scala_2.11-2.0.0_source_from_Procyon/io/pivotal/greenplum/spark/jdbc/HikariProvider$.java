// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.jdbc;

import scala.runtime.BoxesRunTime;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import scala.collection.mutable.StringBuilder;
import scala.MatchError;
import scala.None$;
import scala.runtime.BoxedUnit;
import scala.Some;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.spark.sql.execution.datasources.jdbc.DriverRegistry$;
import javax.sql.DataSource;
import io.pivotal.greenplum.spark.conf.ConnectionPoolOptions;
import scala.Option;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.Logging;

public final class HikariProvider$ implements DataSourceProvider, Logging
{
    public static final HikariProvider$ MODULE$;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    static {
        new HikariProvider$();
    }
    
    @Override
    public Logger io$pivotal$greenplum$spark$Logging$$log_() {
        return this.io$pivotal$greenplum$spark$Logging$$log_;
    }
    
    @TraitSetter
    @Override
    public void io$pivotal$greenplum$spark$Logging$$log__$eq(final Logger x$1) {
        this.io$pivotal$greenplum$spark$Logging$$log_ = x$1;
    }
    
    @Override
    public Logger log() {
        return Logging$class.log(this);
    }
    
    @Override
    public void logWarning(final Function0<String> msg) {
        Logging$class.logWarning(this, msg);
    }
    
    @Override
    public void logDebug(final Function0<String> msg) {
        Logging$class.logDebug(this, msg);
    }
    
    @Override
    public DataSource createDataSource(final ConnectionKey key, final Option<String> password, final String driver, final ConnectionPoolOptions options) {
        DriverRegistry$.MODULE$.register(driver);
        final HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(key.jdbcUrl());
        dataSource.setUsername(key.userName());
        if (password instanceof Some) {
            final String pass = (String)((Some)password).x();
            dataSource.setPassword(pass);
            final BoxedUnit unit = BoxedUnit.UNIT;
        }
        else {
            if (!None$.MODULE$.equals(password)) {
                throw new MatchError((Object)password);
            }
            this.log().debug("No password is used");
            final BoxedUnit unit2 = BoxedUnit.UNIT;
        }
        dataSource.setMaximumPoolSize(options.maximumPoolSize());
        dataSource.setIdleTimeout(options.idleTimeoutMs());
        dataSource.setMinimumIdle(options.minimumIdle());
        if (this.log().isDebugEnabled()) {
            this.log().debug(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Created connection pool with ", " as a max number of " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToInteger(options.maximumPoolSize()) }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "connections for a jdbc url: ", " and user: ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key.jdbcUrl(), key.userName() }))).toString());
        }
        return dataSource;
    }
    
    private HikariProvider$() {
        Logging$class.$init$(MODULE$ = this);
    }
}
