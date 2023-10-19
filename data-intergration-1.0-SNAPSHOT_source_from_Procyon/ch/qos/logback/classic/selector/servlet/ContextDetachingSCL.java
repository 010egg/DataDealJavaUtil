// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.selector.servlet;

import org.slf4j.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import javax.naming.Context;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import javax.naming.NamingException;
import ch.qos.logback.classic.util.JNDIUtil;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextDetachingSCL implements ServletContextListener
{
    public void contextDestroyed(final ServletContextEvent servletContextEvent) {
        String loggerContextName = null;
        try {
            final Context ctx = JNDIUtil.getInitialContext();
            loggerContextName = JNDIUtil.lookup(ctx, "java:comp/env/logback/context-name");
        }
        catch (NamingException ex) {}
        if (loggerContextName != null) {
            System.out.println("About to detach context named " + loggerContextName);
            final ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
            if (selector == null) {
                System.out.println("Selector is null, cannot detach context. Skipping.");
                return;
            }
            final LoggerContext context = selector.getLoggerContext(loggerContextName);
            if (context != null) {
                final Logger logger = context.getLogger("ROOT");
                logger.warn("Stopping logger context " + loggerContextName);
                selector.detachLoggerContext(loggerContextName);
                context.stop();
            }
            else {
                System.out.println("No context named " + loggerContextName + " was found.");
            }
        }
    }
    
    public void contextInitialized(final ServletContextEvent arg0) {
    }
}
