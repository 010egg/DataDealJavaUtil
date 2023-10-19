// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.turbo;

import ch.qos.logback.core.joran.GenericConfigurator;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.gaffer.GafferUtil;
import ch.qos.logback.classic.util.EnvUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.Marker;
import java.io.File;
import java.util.List;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
import java.net.URL;

public class ReconfigureOnChangeFilter extends TurboFilter
{
    public static final long DEFAULT_REFRESH_PERIOD = 60000L;
    long refreshPeriod;
    URL mainConfigurationURL;
    protected volatile long nextCheck;
    ConfigurationWatchList configurationWatchList;
    private long invocationCounter;
    private volatile long mask;
    private volatile long lastMaskCheck;
    private static final int MAX_MASK = 65535;
    private static final long MASK_INCREASE_THRESHOLD = 100L;
    private static final long MASK_DECREASE_THRESHOLD = 800L;
    
    public ReconfigureOnChangeFilter() {
        this.refreshPeriod = 60000L;
        this.invocationCounter = 0L;
        this.mask = 15L;
        this.lastMaskCheck = System.currentTimeMillis();
    }
    
    @Override
    public void start() {
        this.configurationWatchList = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
        if (this.configurationWatchList != null) {
            this.mainConfigurationURL = this.configurationWatchList.getMainURL();
            if (this.mainConfigurationURL == null) {
                this.addWarn("Due to missing top level configuration file, automatic reconfiguration is impossible.");
                return;
            }
            final List<File> watchList = this.configurationWatchList.getCopyOfFileWatchList();
            final long inSeconds = this.refreshPeriod / 1000L;
            this.addInfo("Will scan for changes in [" + watchList + "] every " + inSeconds + " seconds. ");
            synchronized (this.configurationWatchList) {
                this.updateNextCheck(System.currentTimeMillis());
            }
            // monitorexit(this.configurationWatchList)
            super.start();
        }
        else {
            this.addWarn("Empty ConfigurationWatchList in context");
        }
    }
    
    @Override
    public String toString() {
        return "ReconfigureOnChangeFilter{invocationCounter=" + this.invocationCounter + '}';
    }
    
    @Override
    public FilterReply decide(final Marker marker, final Logger logger, final Level level, final String format, final Object[] params, final Throwable t) {
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if ((this.invocationCounter++ & this.mask) != this.mask) {
            return FilterReply.NEUTRAL;
        }
        final long now = System.currentTimeMillis();
        synchronized (this.configurationWatchList) {
            this.updateMaskIfNecessary(now);
            if (this.changeDetected(now)) {
                this.disableSubsequentReconfiguration();
                this.detachReconfigurationToNewThread();
            }
        }
        // monitorexit(this.configurationWatchList)
        return FilterReply.NEUTRAL;
    }
    
    private void updateMaskIfNecessary(final long now) {
        final long timeElapsedSinceLastMaskUpdateCheck = now - this.lastMaskCheck;
        this.lastMaskCheck = now;
        if (timeElapsedSinceLastMaskUpdateCheck < 100L && this.mask < 65535L) {
            this.mask = (this.mask << 1 | 0x1L);
        }
        else if (timeElapsedSinceLastMaskUpdateCheck > 800L) {
            this.mask >>>= 2;
        }
    }
    
    void detachReconfigurationToNewThread() {
        this.addInfo("Detected change in [" + this.configurationWatchList.getCopyOfFileWatchList() + "]");
        this.context.getExecutorService().submit(new ReconfiguringThread());
    }
    
    void updateNextCheck(final long now) {
        this.nextCheck = now + this.refreshPeriod;
    }
    
    protected boolean changeDetected(final long now) {
        if (now >= this.nextCheck) {
            this.updateNextCheck(now);
            return this.configurationWatchList.changeDetected();
        }
        return false;
    }
    
    void disableSubsequentReconfiguration() {
        this.nextCheck = Long.MAX_VALUE;
    }
    
    public long getRefreshPeriod() {
        return this.refreshPeriod;
    }
    
    public void setRefreshPeriod(final long refreshPeriod) {
        this.refreshPeriod = refreshPeriod;
    }
    
    class ReconfiguringThread implements Runnable
    {
        @Override
        public void run() {
            if (ReconfigureOnChangeFilter.this.mainConfigurationURL == null) {
                ReconfigureOnChangeFilter.this.addInfo("Due to missing top level configuration file, skipping reconfiguration");
                return;
            }
            final LoggerContext lc = (LoggerContext)ReconfigureOnChangeFilter.this.context;
            ReconfigureOnChangeFilter.this.addInfo("Will reset and reconfigure context named [" + ReconfigureOnChangeFilter.this.context.getName() + "]");
            if (ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("xml")) {
                this.performXMLConfiguration(lc);
            }
            else if (ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("groovy")) {
                if (EnvUtil.isGroovyAvailable()) {
                    lc.reset();
                    GafferUtil.runGafferConfiguratorOn(lc, this, ReconfigureOnChangeFilter.this.mainConfigurationURL);
                }
                else {
                    ReconfigureOnChangeFilter.this.addError("Groovy classes are not available on the class path. ABORTING INITIALIZATION.");
                }
            }
        }
        
        private void performXMLConfiguration(final LoggerContext lc) {
            final JoranConfigurator jc = new JoranConfigurator();
            jc.setContext(ReconfigureOnChangeFilter.this.context);
            final StatusUtil statusUtil = new StatusUtil(ReconfigureOnChangeFilter.this.context);
            final List<SaxEvent> eventList = jc.recallSafeConfiguration();
            final URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(ReconfigureOnChangeFilter.this.context);
            lc.reset();
            final long threshold = System.currentTimeMillis();
            try {
                jc.doConfigure(ReconfigureOnChangeFilter.this.mainConfigurationURL);
                if (statusUtil.hasXMLParsingErrors(threshold)) {
                    this.fallbackConfiguration(lc, eventList, mainURL);
                }
            }
            catch (JoranException ex) {
                this.fallbackConfiguration(lc, eventList, mainURL);
            }
        }
        
        private void fallbackConfiguration(final LoggerContext lc, final List<SaxEvent> eventList, final URL mainURL) {
            final JoranConfigurator joranConfigurator = new JoranConfigurator();
            joranConfigurator.setContext(ReconfigureOnChangeFilter.this.context);
            if (eventList != null) {
                ReconfigureOnChangeFilter.this.addWarn("Falling back to previously registered safe configuration.");
                try {
                    lc.reset();
                    GenericConfigurator.informContextOfURLUsedForConfiguration(ReconfigureOnChangeFilter.this.context, mainURL);
                    joranConfigurator.doConfigure(eventList);
                    ReconfigureOnChangeFilter.this.addInfo("Re-registering previous fallback configuration once more as a fallback configuration point");
                    joranConfigurator.registerSafeConfiguration(eventList);
                }
                catch (JoranException e) {
                    ReconfigureOnChangeFilter.this.addError("Unexpected exception thrown by a configuration considered safe.", e);
                }
            }
            else {
                ReconfigureOnChangeFilter.this.addWarn("No previous configuration to fall back on.");
            }
        }
    }
}
