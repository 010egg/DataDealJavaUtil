// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

public class SystemInfo
{
    public static String getJavaVendor() {
        return OptionHelper.getSystemProperty("java.vendor", null);
    }
}
