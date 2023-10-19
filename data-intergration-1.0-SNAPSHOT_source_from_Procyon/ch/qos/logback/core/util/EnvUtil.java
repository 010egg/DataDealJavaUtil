// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

public class EnvUtil
{
    private static boolean isJDK_N_OrHigher(final int n) {
        final List<String> versionList = new ArrayList<String>();
        for (int i = 0; i < 5; ++i) {
            versionList.add("1." + (n + i));
        }
        final String javaVersion = System.getProperty("java.version");
        if (javaVersion == null) {
            return false;
        }
        for (final String v : versionList) {
            if (javaVersion.startsWith(v)) {
                return true;
            }
        }
        return false;
    }
    
    public static boolean isJDK5() {
        return isJDK_N_OrHigher(5);
    }
    
    public static boolean isJDK6OrHigher() {
        return isJDK_N_OrHigher(6);
    }
    
    public static boolean isJDK7OrHigher() {
        return isJDK_N_OrHigher(7);
    }
    
    public static boolean isJaninoAvailable() {
        final ClassLoader classLoader = EnvUtil.class.getClassLoader();
        try {
            final Class<?> bindingClass = classLoader.loadClass("org.codehaus.janino.ScriptEvaluator");
            return bindingClass != null;
        }
        catch (ClassNotFoundException e) {
            return false;
        }
    }
    
    public static boolean isWindows() {
        final String os = System.getProperty("os.name");
        return os.startsWith("Windows");
    }
}
