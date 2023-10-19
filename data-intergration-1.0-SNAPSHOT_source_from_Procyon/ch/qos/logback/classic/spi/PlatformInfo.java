// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.spi;

public class PlatformInfo
{
    private static final int UNINITIALIZED = -1;
    private static int hasJMXObjectName;
    
    static {
        PlatformInfo.hasJMXObjectName = -1;
    }
    
    public static boolean hasJMXObjectName() {
        if (PlatformInfo.hasJMXObjectName == -1) {
            try {
                Class.forName("javax.management.ObjectName");
                PlatformInfo.hasJMXObjectName = 1;
            }
            catch (Throwable t) {
                PlatformInfo.hasJMXObjectName = 0;
            }
        }
        return PlatformInfo.hasJMXObjectName == 1;
    }
}
