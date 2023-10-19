// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.gaffer;

import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.ErrorStatus;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.io.File;
import ch.qos.logback.classic.LoggerContext;

public class GafferUtil
{
    private static String ERROR_MSG;
    
    static {
        GafferUtil.ERROR_MSG = "Failed to instantiate ch.qos.logback.classic.gaffer.GafferConfigurator";
    }
    
    public static void runGafferConfiguratorOn(final LoggerContext loggerContext, final Object origin, final File configFile) {
        final GafferConfigurator gafferConfigurator = newGafferConfiguratorInstance(loggerContext, origin);
        if (gafferConfigurator != null) {
            gafferConfigurator.run(configFile);
        }
    }
    
    public static void runGafferConfiguratorOn(final LoggerContext loggerContext, final Object origin, final URL configFile) {
        final GafferConfigurator gafferConfigurator = newGafferConfiguratorInstance(loggerContext, origin);
        if (gafferConfigurator != null) {
            gafferConfigurator.run(configFile);
        }
    }
    
    private static GafferConfigurator newGafferConfiguratorInstance(final LoggerContext loggerContext, final Object origin) {
        try {
            final Class gcClass = Class.forName("ch.qos.logback.classic.gaffer.GafferConfigurator");
            final Constructor c = gcClass.getConstructor(LoggerContext.class);
            return c.newInstance(loggerContext);
        }
        catch (ClassNotFoundException e) {
            addError(loggerContext, origin, GafferUtil.ERROR_MSG, e);
        }
        catch (NoSuchMethodException e2) {
            addError(loggerContext, origin, GafferUtil.ERROR_MSG, e2);
        }
        catch (InvocationTargetException e3) {
            addError(loggerContext, origin, GafferUtil.ERROR_MSG, e3);
        }
        catch (InstantiationException e4) {
            addError(loggerContext, origin, GafferUtil.ERROR_MSG, e4);
        }
        catch (IllegalAccessException e5) {
            addError(loggerContext, origin, GafferUtil.ERROR_MSG, e5);
        }
        return null;
    }
    
    private static void addError(final LoggerContext context, final Object origin, final String msg) {
        addError(context, origin, msg, null);
    }
    
    private static void addError(final LoggerContext context, final Object origin, final String msg, final Throwable t) {
        final StatusManager sm = context.getStatusManager();
        if (sm == null) {
            return;
        }
        sm.add(new ErrorStatus(msg, origin, t));
    }
}
