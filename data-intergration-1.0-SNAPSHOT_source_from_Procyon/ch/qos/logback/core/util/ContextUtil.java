// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.util.List;
import java.util.Iterator;
import java.util.Properties;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.InetAddress;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;

public class ContextUtil extends ContextAwareBase
{
    public ContextUtil(final Context context) {
        this.setContext(context);
    }
    
    public static String getLocalHostName() throws UnknownHostException, SocketException {
        try {
            final InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostName();
        }
        catch (UnknownHostException e) {
            return getLocalAddressAsString();
        }
    }
    
    private static String getLocalAddressAsString() throws UnknownHostException, SocketException {
        final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while (interfaces != null && interfaces.hasMoreElements()) {
            final Enumeration<InetAddress> addresses = interfaces.nextElement().getInetAddresses();
            while (addresses != null && addresses.hasMoreElements()) {
                final InetAddress address = addresses.nextElement();
                if (acceptableAddress(address)) {
                    return address.getHostAddress();
                }
            }
        }
        throw new UnknownHostException();
    }
    
    private static boolean acceptableAddress(final InetAddress address) {
        return address != null && !address.isLoopbackAddress() && !address.isAnyLocalAddress() && !address.isLinkLocalAddress();
    }
    
    public void addHostNameAsProperty() {
        try {
            final String localhostName = getLocalHostName();
            this.context.putProperty("HOSTNAME", localhostName);
        }
        catch (UnknownHostException e) {
            this.addError("Failed to get local hostname", e);
        }
        catch (SocketException e2) {
            this.addError("Failed to get local hostname", e2);
        }
        catch (SecurityException e3) {
            this.addError("Failed to get local hostname", e3);
        }
    }
    
    public void addProperties(final Properties props) {
        if (props == null) {
            return;
        }
        for (final String key : props.keySet()) {
            this.context.putProperty(key, props.getProperty(key));
        }
    }
    
    public void addGroovyPackages(final List<String> frameworkPackages) {
        this.addFrameworkPackage(frameworkPackages, "org.codehaus.groovy.runtime");
    }
    
    public void addFrameworkPackage(final List<String> frameworkPackages, final String packageName) {
        if (!frameworkPackages.contains(packageName)) {
            frameworkPackages.add(packageName);
        }
    }
}
