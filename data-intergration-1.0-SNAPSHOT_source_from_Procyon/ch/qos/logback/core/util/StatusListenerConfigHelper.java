// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.Context;

public class StatusListenerConfigHelper
{
    public static void installIfAsked(final Context context) {
        final String slClass = OptionHelper.getSystemProperty("logback.statusListenerClass");
        if (!OptionHelper.isEmpty(slClass)) {
            addStatusListener(context, slClass);
        }
    }
    
    private static void addStatusListener(final Context context, final String listenerClassName) {
        StatusListener listener = null;
        if ("SYSOUT".equalsIgnoreCase(listenerClassName)) {
            listener = new OnConsoleStatusListener();
        }
        else {
            listener = createListenerPerClassName(context, listenerClassName);
        }
        initAndAddListener(context, listener);
    }
    
    private static void initAndAddListener(final Context context, final StatusListener listener) {
        if (listener != null) {
            if (listener instanceof ContextAware) {
                ((ContextAware)listener).setContext(context);
            }
            boolean effectivelyAdded = context.getStatusManager().add(listener);
            effectivelyAdded = true;
            if (effectivelyAdded && listener instanceof LifeCycle) {
                ((LifeCycle)listener).start();
            }
        }
    }
    
    private static StatusListener createListenerPerClassName(final Context context, final String listenerClass) {
        try {
            return (StatusListener)OptionHelper.instantiateByClassName(listenerClass, StatusListener.class, context);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static void addOnConsoleListenerInstance(final Context context, final OnConsoleStatusListener onConsoleStatusListener) {
        onConsoleStatusListener.setContext(context);
        final boolean effectivelyAdded = context.getStatusManager().add(onConsoleStatusListener);
        if (effectivelyAdded) {
            onConsoleStatusListener.start();
        }
    }
}
