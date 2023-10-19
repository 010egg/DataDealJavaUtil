// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran.action;

import java.util.concurrent.ScheduledFuture;
import ch.qos.logback.core.util.Duration;
import java.net.URL;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.classic.util.EnvUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.ContextUtil;
import ch.qos.logback.core.util.StatusListenerConfigHelper;
import ch.qos.logback.core.status.OnConsoleStatusListener;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.action.Action;

public class ConfigurationAction extends Action
{
    static final String INTERNAL_DEBUG_ATTR = "debug";
    static final String PACKAGING_DATA_ATTR = "packagingData";
    static final String SCAN_ATTR = "scan";
    static final String SCAN_PERIOD_ATTR = "scanPeriod";
    static final String DEBUG_SYSTEM_PROPERTY_KEY = "logback.debug";
    long threshold;
    
    public ConfigurationAction() {
        this.threshold = 0L;
    }
    
    @Override
    public void begin(final InterpretationContext ic, final String name, final Attributes attributes) {
        this.threshold = System.currentTimeMillis();
        String debugAttrib = this.getSystemProperty("logback.debug");
        if (debugAttrib == null) {
            debugAttrib = ic.subst(attributes.getValue("debug"));
        }
        if (OptionHelper.isEmpty(debugAttrib) || debugAttrib.equalsIgnoreCase("false") || debugAttrib.equalsIgnoreCase("null")) {
            this.addInfo("debug attribute not set");
        }
        else {
            StatusListenerConfigHelper.addOnConsoleListenerInstance(this.context, new OnConsoleStatusListener());
        }
        this.processScanAttrib(ic, attributes);
        final ContextUtil contextUtil = new ContextUtil(this.context);
        contextUtil.addHostNameAsProperty();
        final LoggerContext lc = (LoggerContext)this.context;
        final boolean packagingData = OptionHelper.toBoolean(ic.subst(attributes.getValue("packagingData")), false);
        lc.setPackagingDataEnabled(packagingData);
        if (EnvUtil.isGroovyAvailable()) {
            contextUtil.addGroovyPackages(lc.getFrameworkPackages());
        }
        ic.pushObject(this.getContext());
    }
    
    String getSystemProperty(final String name) {
        try {
            return System.getProperty(name);
        }
        catch (SecurityException ex) {
            return null;
        }
    }
    
    void processScanAttrib(final InterpretationContext ic, final Attributes attributes) {
        final String scanAttrib = ic.subst(attributes.getValue("scan"));
        if (!OptionHelper.isEmpty(scanAttrib) && !"false".equalsIgnoreCase(scanAttrib)) {
            final ScheduledExecutorService scheduledExecutorService = this.context.getScheduledExecutorService();
            final URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(this.context);
            if (mainURL == null) {
                this.addWarn("Due to missing top level configuration file, reconfiguration on change (configuration file scanning) cannot be done.");
                return;
            }
            final ReconfigureOnChangeTask rocTask = new ReconfigureOnChangeTask();
            rocTask.setContext(this.context);
            this.context.putObject("RECONFIGURE_ON_CHANGE_TASK", rocTask);
            final String scanPeriodAttrib = ic.subst(attributes.getValue("scanPeriod"));
            final Duration duration = this.getDuration(scanAttrib, scanPeriodAttrib);
            if (duration == null) {
                return;
            }
            this.addInfo("Will scan for changes in [" + mainURL + "] ");
            this.addInfo("Setting ReconfigureOnChangeTask scanning period to " + duration);
            final ScheduledFuture<?> scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(rocTask, duration.getMilliseconds(), duration.getMilliseconds(), TimeUnit.MILLISECONDS);
            this.context.addScheduledFuture(scheduledFuture);
        }
    }
    
    private Duration getDuration(final String scanAttrib, final String scanPeriodAttrib) {
        Duration duration = null;
        if (!OptionHelper.isEmpty(scanPeriodAttrib)) {
            try {
                duration = Duration.valueOf(scanPeriodAttrib);
            }
            catch (NumberFormatException nfe) {
                this.addError("Error while converting [" + scanAttrib + "] to long", nfe);
            }
        }
        return duration;
    }
    
    @Override
    public void end(final InterpretationContext ec, final String name) {
        this.addInfo("End of configuration.");
        ec.popObject();
    }
}
