// 
// Decompiled by Procyon v0.5.36
// 

package io.pivotal.greenplum.spark;

import java.net.InetAddress;
import scala.reflect.ScalaSignature;

@ScalaSignature(bytes = "\u0006\u0001}2A!\u0001\u0002\u0001\u0017\tqa*\u001a;x_J\\wK]1qa\u0016\u0014(BA\u0002\u0005\u0003\u0015\u0019\b/\u0019:l\u0015\t)a!A\u0005he\u0016,g\u000e\u001d7v[*\u0011q\u0001C\u0001\ba&4x\u000e^1m\u0015\u0005I\u0011AA5p\u0007\u0001\u0019\"\u0001\u0001\u0007\u0011\u00055\u0001R\"\u0001\b\u000b\u0003=\tQa]2bY\u0006L!!\u0005\b\u0003\r\u0005s\u0017PU3g\u0011\u0015\u0019\u0002\u0001\"\u0001\u0015\u0003\u0019a\u0014N\\5u}Q\tQ\u0003\u0005\u0002\u0017\u00015\t!\u0001C\u0003\u0019\u0001\u0011\u0005\u0011$A\nhKRdunY1m\u0011>\u001cH/\u00113ee\u0016\u001c8/F\u0001\u001b!\tYbD\u0004\u0002\u000e9%\u0011QDD\u0001\u0007!J,G-\u001a4\n\u0005}\u0001#AB*ue&twM\u0003\u0002\u001e\u001d!)!\u0005\u0001C\u0001G\u0005!r-\u001a;J]\u0016$\u0018\t\u001a3sKN\u001c()\u001f(b[\u0016$\"\u0001\n\u0017\u0011\u0005\u0015RS\"\u0001\u0014\u000b\u0005\u001dB\u0013a\u00018fi*\t\u0011&\u0001\u0003kCZ\f\u0017BA\u0016'\u0005-Ie.\u001a;BI\u0012\u0014Xm]:\t\u000b5\n\u0003\u0019\u0001\u000e\u0002\t9\fW.\u001a\u0005\u0006_\u0001!\t!G\u0001\u0011O\u0016$Hj\\2bY\"{7\u000f\u001e8b[\u0016DQ!\r\u0001\u0005\u0002I\nQeZ3u\u0013:,G\u000fN!eIJ,7o\u001d(fi^|'o[%oi\u0016\u0014h-Y2f\u0005ft\u0015-\\3\u0015\u0005\u0011\u001a\u0004\"B\u00171\u0001\u0004Qr!B\u001b\u0003\u0011\u00031\u0014A\u0004(fi^|'o[,sCB\u0004XM\u001d\t\u0003-]2Q!\u0001\u0002\t\u0002a\u001a\"a\u000e\u0007\t\u000bM9D\u0011\u0001\u001e\u0015\u0003YBQ\u0001P\u001c\u0005\u0002u\nQeZ3u\u0013:,G\u000fN!eIJ,7o\u001d\"z\u001d\u0016$xo\u001c:l\u0013:$XM\u001d4bG\u0016t\u0015-\\3\u0015\u0005\u0011r\u0004\"B\u0017<\u0001\u0004Q\u0002")
public class NetworkWrapper
{
    public static InetAddress getInet4AddressByNetworkInterfaceName(final String name) {
        return NetworkWrapper$.MODULE$.getInet4AddressByNetworkInterfaceName(name);
    }
    
    public String getLocalHostAddress() {
        return InetAddress.getLocalHost().getHostAddress();
    }
    
    public InetAddress getInetAddressByName(final String name) {
        return InetAddress.getByName(name);
    }
    
    public String getLocalHostname() {
        return InetAddress.getLocalHost().getHostName();
    }
    
    public InetAddress getInet4AddressNetworkInterfaceByName(final String name) {
        return NetworkWrapper$.MODULE$.getInet4AddressByNetworkInterfaceName(name);
    }
}
