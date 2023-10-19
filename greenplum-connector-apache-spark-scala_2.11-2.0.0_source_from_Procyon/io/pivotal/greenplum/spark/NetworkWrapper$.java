// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import java.util.Enumeration;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.InetAddress;

public final class NetworkWrapper$
{
    public static final NetworkWrapper$ MODULE$;
    
    static {
        new NetworkWrapper$();
    }
    
    public InetAddress getInet4AddressByNetworkInterfaceName(final String name) {
        final NetworkInterface nic = NetworkInterface.getByName(name);
        if (nic != null && nic.getInetAddresses().hasMoreElements()) {
            final Enumeration addresses = nic.getInetAddresses();
            while (addresses.hasMoreElements()) {
                final InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address) {
                    return addr;
                }
            }
        }
        return null;
    }
    
    private NetworkWrapper$() {
        MODULE$ = this;
    }
}
