// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.util;

public final class Constants
{
    public static final boolean IS_WEB_APP;
    public static final boolean ENABLE_THREADLOCALS;
    
    private static boolean isClassAvailable(final String className) {
        try {
            return LoaderUtil.loadClass(className) != null;
        }
        catch (Throwable e) {
            return false;
        }
    }
    
    private Constants() {
    }
    
    static {
        IS_WEB_APP = PropertiesUtil.getProperties().getBooleanProperty("log4j2.is.webapp", isClassAvailable("javax.servlet.Servlet"));
        ENABLE_THREADLOCALS = (!Constants.IS_WEB_APP && PropertiesUtil.getProperties().getBooleanProperty("log4j2.enable.threadlocals", true));
    }
}
