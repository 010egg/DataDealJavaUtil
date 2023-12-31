// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.util;

import java.net.UnknownHostException;
import java.net.InetAddress;
import com.beust.jcommander.IStringConverter;

public class InetAddressConverter implements IStringConverter<InetAddress>
{
    @Override
    public InetAddress convert(final String host) {
        try {
            return InetAddress.getByName(host);
        }
        catch (UnknownHostException e) {
            throw new IllegalArgumentException(host, e);
        }
    }
}
