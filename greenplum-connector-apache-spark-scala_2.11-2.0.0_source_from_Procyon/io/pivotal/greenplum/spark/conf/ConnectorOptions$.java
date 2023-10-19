// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark.conf;

import scala.sys.package$;
import scala.collection.immutable.Map;
import scala.Serializable;

public final class ConnectorOptions$ implements Serializable
{
    public static final ConnectorOptions$ MODULE$;
    private final String GPDB_NETWORK_PORT;
    private final String GPDB_NETWORK_ADDRESS;
    private final String GPDB_NETWORK_INTERFACE;
    private final String GPDB_NETWORK_HOSTNAME;
    private final String GPDB_NETWORK_TIMEOUT;
    private final String GPDB_ENV_VAR_PREFIX;
    private final String GPDB_DEFAULT_NETWORK_INTERFACE;
    private final String GPDB_DEFAULT_SERVER_HOST;
    
    static {
        new ConnectorOptions$();
    }
    
    public String GPDB_NETWORK_PORT() {
        return this.GPDB_NETWORK_PORT;
    }
    
    public String GPDB_NETWORK_ADDRESS() {
        return this.GPDB_NETWORK_ADDRESS;
    }
    
    public String GPDB_NETWORK_INTERFACE() {
        return this.GPDB_NETWORK_INTERFACE;
    }
    
    public String GPDB_NETWORK_HOSTNAME() {
        return this.GPDB_NETWORK_HOSTNAME;
    }
    
    public String GPDB_NETWORK_TIMEOUT() {
        return this.GPDB_NETWORK_TIMEOUT;
    }
    
    public String GPDB_ENV_VAR_PREFIX() {
        return this.GPDB_ENV_VAR_PREFIX;
    }
    
    public String GPDB_DEFAULT_NETWORK_INTERFACE() {
        return this.GPDB_DEFAULT_NETWORK_INTERFACE;
    }
    
    public String GPDB_DEFAULT_SERVER_HOST() {
        return this.GPDB_DEFAULT_SERVER_HOST;
    }
    
    public Map<String, String> $lessinit$greater$default$2() {
        return (Map<String, String>)package$.MODULE$.env();
    }
    
    private Object readResolve() {
        return ConnectorOptions$.MODULE$;
    }
    
    private ConnectorOptions$() {
        MODULE$ = this;
        this.GPDB_NETWORK_PORT = "server.port";
        this.GPDB_NETWORK_ADDRESS = "server.hostEnv";
        this.GPDB_NETWORK_INTERFACE = "server.nic";
        this.GPDB_NETWORK_HOSTNAME = "server.useHostname";
        this.GPDB_NETWORK_TIMEOUT = "server.timeout";
        this.GPDB_ENV_VAR_PREFIX = "env.";
        this.GPDB_DEFAULT_NETWORK_INTERFACE = "eth0";
        this.GPDB_DEFAULT_SERVER_HOST = "0.0.0.0";
    }
}
