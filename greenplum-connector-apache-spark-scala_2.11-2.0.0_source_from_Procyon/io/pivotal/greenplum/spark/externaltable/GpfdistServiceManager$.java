// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.externaltable;

import java.net.InetAddress;
import io.pivotal.greenplum.spark.conf.ConnectorOptions$;
import io.pivotal.greenplum.spark.NetworkWrapper$;
import org.apache.commons.lang.StringUtils;
import org.apache.spark.SparkContext;
import scala.runtime.BoxesRunTime;
import scala.collection.immutable.Nil$;
import scala.collection.mutable.StringBuilder;
import org.apache.spark.scheduler.SparkListenerInterface;
import org.apache.spark.sql.SparkSession$;
import org.apache.spark.SparkEnv$;
import scala.collection.Seq;
import scala.StringContext;
import scala.Predef$;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import java.util.Map;
import scala.runtime.BoxedUnit;
import scala.Function1;
import java.util.Set;
import scala.collection.JavaConverters$;
import scala.collection.IterableLike;
import scala.collection.immutable.List;
import io.pivotal.greenplum.spark.conf.ConnectorOptions;
import scala.Function0;
import io.pivotal.greenplum.spark.Logging$class;
import scala.runtime.TraitSetter;
import org.slf4j.Logger;
import io.pivotal.greenplum.spark.util.TransactionData;
import scala.util.Try;
import java.util.concurrent.ConcurrentHashMap;
import io.pivotal.greenplum.spark.Logging;

public final class GpfdistServiceManager$ implements Logging
{
    public static final GpfdistServiceManager$ MODULE$;
    private final ConcurrentHashMap<String, Try<TransactionData>> bufferMap;
    private final ConcurrentHashMap<String, PartitionData> sendBufferMap;
    private final ConcurrentHashMap<ServiceKey, GpfdistService> servicesMap;
    private transient Logger io$pivotal$greenplum$spark$Logging$$log_;
    
    static {
        new GpfdistServiceManager$();
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
    
    private ConcurrentHashMap<String, Try<TransactionData>> bufferMap() {
        return this.bufferMap;
    }
    
    private ConcurrentHashMap<String, PartitionData> sendBufferMap() {
        return this.sendBufferMap;
    }
    
    private ConcurrentHashMap<ServiceKey, GpfdistService> servicesMap() {
        return this.servicesMap;
    }
    
    public GpfdistService getService(final ConnectorOptions connectorOptions) {
        final List ports = connectorOptions.port();
        final ServiceKey key = new ServiceKey((List<Object>)ports);
        if (this.log().isTraceEnabled()) {
            ((IterableLike)JavaConverters$.MODULE$.asScalaSetConverter((Set)this.servicesMap().entrySet()).asScala()).foreach((Function1)new GpfdistServiceManager$$anonfun$getService.GpfdistServiceManager$$anonfun$getService$1());
        }
        Label_0311: {
            if (this.servicesMap().containsKey(key)) {
                final BoxedUnit unit = BoxedUnit.UNIT;
                break Label_0311;
            }
            synchronized (this.servicesMap()) {
                if (this.servicesMap().containsKey(key)) {
                    final BoxedUnit unit2 = BoxedUnit.UNIT;
                }
                else {
                    final GpfdistHandler handler = new GpfdistHandler(this.bufferMap(), this.sendBufferMap());
                    final Server server = new Server();
                    server.setHandler(handler);
                    final String serverHost = this.getServerHost(connectorOptions);
                    final GpfdistService gpfdistService = new GpfdistService(serverHost, (List<Object>)ports, this.bufferMap(), this.sendBufferMap(), new ServerWrapper(server), connectorOptions.timeoutInMillis());
                    this.log().debug(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Service for ", " is being started...." })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { ports })));
                    gpfdistService.start();
                    final String executorId = SparkEnv$.MODULE$.get().executorId();
                    final String obj = "driver";
                    Label_0294: {
                        if (executorId == null) {
                            if (obj != null) {
                                break Label_0294;
                            }
                        }
                        else if (!executorId.equals(obj)) {
                            break Label_0294;
                        }
                        final SparkContext sc = SparkSession$.MODULE$.builder().getOrCreate().sparkContext();
                        sc.addSparkListener((SparkListenerInterface)new StopGpfdistServiceSparkListener(key));
                    }
                    this.servicesMap().put(key, gpfdistService);
                }
                // monitorexit(this.servicesMap())
                final GpfdistService service = this.servicesMap().get(key);
                final long serviceTimeout = service.getTimeout();
                if (serviceTimeout < connectorOptions.timeoutInMillis()) {
                    this.log().warn(new StringBuilder().append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Unable to change the GpfdistService timeout after the service " })).s((Seq)Nil$.MODULE$)).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "has started. GpfdistService started with timeout ", " but " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToLong(serviceTimeout) }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "a higher timeout (", ") is configured. " })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { BoxesRunTime.boxToLong(connectorOptions.timeoutInMillis()) }))).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Ensure that the desired GpfdistService timeout is set for the first " })).s((Seq)Nil$.MODULE$)).append((Object)new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "operation that accesses Greenplum." })).s((Seq)Nil$.MODULE$)).toString());
                }
                return service;
            }
        }
    }
    
    public void stopAndRemove(final ServiceKey key) {
        final GpfdistService service = this.servicesMap().remove(key);
        if (service == null) {
            this.log().warn(new StringContext((Seq)Predef$.MODULE$.wrapRefArray((Object[])new String[] { "Unable to find service for ", "" })).s((Seq)Predef$.MODULE$.genericWrapArray((Object)new Object[] { key })));
        }
        else {
            service.stop();
        }
    }
    
    private String getServerHost(final ConnectorOptions connectorOptions) {
        if (StringUtils.isNotBlank(connectorOptions.networkInterface())) {
            final InetAddress address = NetworkWrapper$.MODULE$.getInet4AddressByNetworkInterfaceName(connectorOptions.networkInterface());
            if (address != null) {
                return address.getHostAddress();
            }
        }
        return ConnectorOptions$.MODULE$.GPDB_DEFAULT_SERVER_HOST();
    }
    
    private GpfdistServiceManager$() {
        Logging$class.$init$(MODULE$ = this);
        this.bufferMap = new ConcurrentHashMap<String, Try<TransactionData>>();
        this.sendBufferMap = new ConcurrentHashMap<String, PartitionData>();
        this.servicesMap = new ConcurrentHashMap<ServiceKey, GpfdistService>();
    }
}
