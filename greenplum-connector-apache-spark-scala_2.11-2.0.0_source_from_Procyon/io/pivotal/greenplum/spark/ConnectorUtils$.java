// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import java.net.InetAddress;
import org.apache.commons.lang.StringUtils;
import io.pivotal.greenplum.spark.conf.ConnectorOptions;
import scala.util.Failure;
import scala.Function0;
import scala.util.Try$;
import scala.util.Try;
import scala.math.Ordering;
import scala.Function1;
import scala.reflect.ClassTag$;
import scala.Array$;
import scala.collection.immutable.StringOps;
import scala.Predef$;
import scala.collection.immutable.List;

public final class ConnectorUtils$
{
    public static final ConnectorUtils$ MODULE$;
    
    static {
        new ConnectorUtils$();
    }
    
    public List<Object> parsePortString(final String portStr) {
        return (List<Object>)Predef$.MODULE$.intArrayOps((int[])Predef$.MODULE$.intArrayOps((int[])Predef$.MODULE$.intArrayOps((int[])Predef$.MODULE$.refArrayOps((Object[])new StringOps(Predef$.MODULE$.augmentString(portStr)).split(',')).flatMap((Function1)new ConnectorUtils$$anonfun$parsePortString.ConnectorUtils$$anonfun$parsePortString$1(portStr), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.Int()))).distinct()).sorted((Ordering)Ordering.Int$.MODULE$)).toList();
    }
    
    public Try<List<Object>> io$pivotal$greenplum$spark$ConnectorUtils$$parsePortOrRange(final String str) {
        final Try[] array = (Try[])Predef$.MODULE$.refArrayOps((Object[])Predef$.MODULE$.refArrayOps((Object[])new StringOps(Predef$.MODULE$.augmentString(str)).split('-')).map((Function1)new ConnectorUtils$$anonfun.ConnectorUtils$$anonfun$1(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Try.class)))).map((Function1)new ConnectorUtils$$anonfun.ConnectorUtils$$anonfun$2(), Array$.MODULE$.canBuildFrom(ClassTag$.MODULE$.apply((Class)Try.class)));
        Object o;
        if (array.length == 1) {
            o = Try$.MODULE$.apply((Function0)new ConnectorUtils$$anonfun$io$pivotal$greenplum$spark$ConnectorUtils$$parsePortOrRange.ConnectorUtils$$anonfun$io$pivotal$greenplum$spark$ConnectorUtils$$parsePortOrRange$1(array));
        }
        else if (array.length == 2) {
            o = Try$.MODULE$.apply((Function0)new ConnectorUtils$$anonfun$io$pivotal$greenplum$spark$ConnectorUtils$$parsePortOrRange.ConnectorUtils$$anonfun$io$pivotal$greenplum$spark$ConnectorUtils$$parsePortOrRange$2(array));
        }
        else {
            o = new Failure((Throwable)new IllegalArgumentException("invalid port range"));
        }
        return (Try<List<Object>>)o;
    }
    
    public String getServerAddress(final ConnectorOptions connectorOptions, final NetworkWrapper networkWrapper) {
        String s;
        if (connectorOptions.useHostname()) {
            s = networkWrapper.getLocalHostname();
        }
        else if (StringUtils.isNotBlank(connectorOptions.address())) {
            s = networkWrapper.getInetAddressByName(connectorOptions.address()).getHostAddress();
        }
        else {
            final InetAddress inetAddress = StringUtils.isNotBlank(connectorOptions.networkInterface()) ? networkWrapper.getInet4AddressNetworkInterfaceByName(connectorOptions.networkInterface()) : null;
            s = ((inetAddress == null) ? networkWrapper.getLocalHostAddress() : inetAddress.getHostAddress());
        }
        return s;
    }
    
    public NetworkWrapper getServerAddress$default$2() {
        return new NetworkWrapper();
    }
    
    private ConnectorUtils$() {
        MODULE$ = this;
    }
}
