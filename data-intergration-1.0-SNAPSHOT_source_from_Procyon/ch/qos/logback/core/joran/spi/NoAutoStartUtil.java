// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

public class NoAutoStartUtil
{
    public static boolean notMarkedWithNoAutoStart(final Object o) {
        if (o == null) {
            return false;
        }
        final Class<?> clazz = o.getClass();
        final NoAutoStart a = clazz.getAnnotation(NoAutoStart.class);
        return a == null;
    }
}
