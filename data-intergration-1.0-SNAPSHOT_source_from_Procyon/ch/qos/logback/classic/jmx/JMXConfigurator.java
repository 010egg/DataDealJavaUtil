// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.jmx;

import javax.management.MBeanRegistrationException;
import javax.management.InstanceNotFoundException;
import ch.qos.logback.core.status.Status;
import java.util.ArrayList;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
import ch.qos.logback.core.util.StatusPrinter;
import ch.qos.logback.core.Context;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.status.StatusListenerAsList;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.StatusListener;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.io.File;
import ch.qos.logback.core.joran.spi.JoranException;
import java.net.URL;
import ch.qos.logback.classic.util.ContextInitializer;
import java.util.Iterator;
import java.util.List;
import javax.management.ObjectName;
import javax.management.MBeanServer;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;

public class JMXConfigurator extends ContextAwareBase implements JMXConfiguratorMBean, LoggerContextListener
{
    private static String EMPTY;
    LoggerContext loggerContext;
    MBeanServer mbs;
    ObjectName objectName;
    String objectNameAsString;
    boolean debug;
    boolean started;
    
    static {
        JMXConfigurator.EMPTY = "";
    }
    
    public JMXConfigurator(final LoggerContext loggerContext, final MBeanServer mbs, final ObjectName objectName) {
        this.debug = true;
        this.started = true;
        this.context = loggerContext;
        this.loggerContext = loggerContext;
        this.mbs = mbs;
        this.objectName = objectName;
        this.objectNameAsString = objectName.toString();
        if (this.previouslyRegisteredListenerWithSameObjectName()) {
            this.addError("Previously registered JMXConfigurator named [" + this.objectNameAsString + "] in the logger context named [" + loggerContext.getName() + "]");
        }
        else {
            loggerContext.addListener(this);
        }
    }
    
    private boolean previouslyRegisteredListenerWithSameObjectName() {
        final List<LoggerContextListener> lcll = this.loggerContext.getCopyOfListenerList();
        for (final LoggerContextListener lcl : lcll) {
            if (lcl instanceof JMXConfigurator) {
                final JMXConfigurator jmxConfigurator = (JMXConfigurator)lcl;
                if (this.objectName.equals(jmxConfigurator.objectName)) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    @Override
    public void reloadDefaultConfiguration() throws JoranException {
        final ContextInitializer ci = new ContextInitializer(this.loggerContext);
        final URL url = ci.findURLOfDefaultConfigurationFile(true);
        this.reloadByURL(url);
    }
    
    @Override
    public void reloadByFileName(final String fileName) throws JoranException, FileNotFoundException {
        final File f = new File(fileName);
        if (f.exists() && f.isFile()) {
            try {
                final URL url = f.toURI().toURL();
                this.reloadByURL(url);
                return;
            }
            catch (MalformedURLException e) {
                throw new RuntimeException("Unexpected MalformedURLException occured. See nexted cause.", e);
            }
        }
        final String errMsg = "Could not find [" + fileName + "]";
        this.addInfo(errMsg);
        throw new FileNotFoundException(errMsg);
    }
    
    void addStatusListener(final StatusListener statusListener) {
        final StatusManager sm = this.loggerContext.getStatusManager();
        sm.add(statusListener);
    }
    
    void removeStatusListener(final StatusListener statusListener) {
        final StatusManager sm = this.loggerContext.getStatusManager();
        sm.remove(statusListener);
    }
    
    @Override
    public void reloadByURL(final URL url) throws JoranException {
        final StatusListenerAsList statusListenerAsList = new StatusListenerAsList();
        this.addStatusListener(statusListenerAsList);
        this.addInfo("Resetting context: " + this.loggerContext.getName());
        this.loggerContext.reset();
        this.addStatusListener(statusListenerAsList);
        try {
            if (url != null) {
                final JoranConfigurator configurator = new JoranConfigurator();
                configurator.setContext(this.loggerContext);
                configurator.doConfigure(url);
                this.addInfo("Context: " + this.loggerContext.getName() + " reloaded.");
            }
        }
        finally {
            this.removeStatusListener(statusListenerAsList);
            if (this.debug) {
                StatusPrinter.print(statusListenerAsList.getStatusList());
            }
        }
        this.removeStatusListener(statusListenerAsList);
        if (this.debug) {
            StatusPrinter.print(statusListenerAsList.getStatusList());
        }
    }
    
    @Override
    public void setLoggerLevel(String loggerName, String levelStr) {
        if (loggerName == null) {
            return;
        }
        if (levelStr == null) {
            return;
        }
        loggerName = loggerName.trim();
        levelStr = levelStr.trim();
        this.addInfo("Trying to set level " + levelStr + " to logger " + loggerName);
        final LoggerContext lc = (LoggerContext)this.context;
        final Logger logger = lc.getLogger(loggerName);
        if ("null".equalsIgnoreCase(levelStr)) {
            logger.setLevel(null);
        }
        else {
            final Level level = Level.toLevel(levelStr, null);
            if (level != null) {
                logger.setLevel(level);
            }
        }
    }
    
    @Override
    public String getLoggerLevel(String loggerName) {
        if (loggerName == null) {
            return JMXConfigurator.EMPTY;
        }
        loggerName = loggerName.trim();
        final LoggerContext lc = (LoggerContext)this.context;
        final Logger logger = lc.exists(loggerName);
        if (logger != null && logger.getLevel() != null) {
            return logger.getLevel().toString();
        }
        return JMXConfigurator.EMPTY;
    }
    
    @Override
    public String getLoggerEffectiveLevel(String loggerName) {
        if (loggerName == null) {
            return JMXConfigurator.EMPTY;
        }
        loggerName = loggerName.trim();
        final LoggerContext lc = (LoggerContext)this.context;
        final Logger logger = lc.exists(loggerName);
        if (logger != null) {
            return logger.getEffectiveLevel().toString();
        }
        return JMXConfigurator.EMPTY;
    }
    
    @Override
    public List<String> getLoggerList() {
        final LoggerContext lc = (LoggerContext)this.context;
        final List<String> strList = new ArrayList<String>();
        for (final Logger log : lc.getLoggerList()) {
            strList.add(log.getName());
        }
        return strList;
    }
    
    @Override
    public List<String> getStatuses() {
        final List<String> list = new ArrayList<String>();
        final Iterator<Status> it = this.context.getStatusManager().getCopyOfStatusList().iterator();
        while (it.hasNext()) {
            list.add(it.next().toString());
        }
        return list;
    }
    
    @Override
    public void onStop(final LoggerContext context) {
        if (!this.started) {
            this.addInfo("onStop() method called on a stopped JMXActivator [" + this.objectNameAsString + "]");
            return;
        }
        if (this.mbs.isRegistered(this.objectName)) {
            try {
                this.addInfo("Unregistering mbean [" + this.objectNameAsString + "]");
                this.mbs.unregisterMBean(this.objectName);
            }
            catch (InstanceNotFoundException e) {
                this.addError("Unable to find a verifiably registered mbean [" + this.objectNameAsString + "]", e);
            }
            catch (MBeanRegistrationException e2) {
                this.addError("Failed to unregister [" + this.objectNameAsString + "]", e2);
            }
        }
        else {
            this.addInfo("mbean [" + this.objectNameAsString + "] was not in the mbean registry. This is OK.");
        }
        this.stop();
    }
    
    @Override
    public void onLevelChange(final Logger logger, final Level level) {
    }
    
    @Override
    public void onReset(final LoggerContext context) {
        this.addInfo("onReset() method called JMXActivator [" + this.objectNameAsString + "]");
    }
    
    @Override
    public boolean isResetResistant() {
        return true;
    }
    
    private void clearFields() {
        this.mbs = null;
        this.objectName = null;
        this.loggerContext = null;
    }
    
    private void stop() {
        this.started = false;
        this.clearFields();
    }
    
    @Override
    public void onStart(final LoggerContext context) {
    }
    
    @Override
    public String toString() {
        return String.valueOf(this.getClass().getName()) + "(" + this.context.getName() + ")";
    }
}
