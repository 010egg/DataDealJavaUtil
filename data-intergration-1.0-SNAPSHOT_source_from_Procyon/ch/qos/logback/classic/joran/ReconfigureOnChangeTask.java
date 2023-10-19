// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran;

import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.StatusUtil;
import java.util.Iterator;
import java.net.URL;
import java.io.File;
import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
import ch.qos.logback.classic.gaffer.GafferUtil;
import ch.qos.logback.classic.util.EnvUtil;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import java.util.ArrayList;
import java.util.List;
import ch.qos.logback.core.spi.ContextAwareBase;

public class ReconfigureOnChangeTask extends ContextAwareBase implements Runnable
{
    public static final String DETECTED_CHANGE_IN_CONFIGURATION_FILES = "Detected change in configuration files.";
    static final String RE_REGISTERING_PREVIOUS_SAFE_CONFIGURATION = "Re-registering previous fallback configuration once more as a fallback configuration point";
    static final String FALLING_BACK_TO_SAFE_CONFIGURATION = "Given previous errors, falling back to previously registered safe configuration.";
    long birthdate;
    List<ReconfigureOnChangeTaskListener> listeners;
    
    public ReconfigureOnChangeTask() {
        this.birthdate = System.currentTimeMillis();
    }
    
    void addListener(final ReconfigureOnChangeTaskListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList<ReconfigureOnChangeTaskListener>();
        }
        this.listeners.add(listener);
    }
    
    @Override
    public void run() {
        this.fireEnteredRunMethod();
        final ConfigurationWatchList configurationWatchList = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
        if (configurationWatchList == null) {
            this.addWarn("Empty ConfigurationWatchList in context");
            return;
        }
        final List<File> filesToWatch = configurationWatchList.getCopyOfFileWatchList();
        if (filesToWatch == null || filesToWatch.isEmpty()) {
            this.addInfo("Empty watch file list. Disabling ");
            return;
        }
        if (!configurationWatchList.changeDetected()) {
            return;
        }
        this.fireChangeDetected();
        final URL mainConfigurationURL = configurationWatchList.getMainURL();
        this.addInfo("Detected change in configuration files.");
        this.addInfo("Will reset and reconfigure context named [" + this.context.getName() + "]");
        final LoggerContext lc = (LoggerContext)this.context;
        if (mainConfigurationURL.toString().endsWith("xml")) {
            this.performXMLConfiguration(lc, mainConfigurationURL);
        }
        else if (mainConfigurationURL.toString().endsWith("groovy")) {
            if (EnvUtil.isGroovyAvailable()) {
                lc.reset();
                GafferUtil.runGafferConfiguratorOn(lc, this, mainConfigurationURL);
            }
            else {
                this.addError("Groovy classes are not available on the class path. ABORTING INITIALIZATION.");
            }
        }
        this.fireDoneReconfiguring();
    }
    
    private void fireEnteredRunMethod() {
        if (this.listeners == null) {
            return;
        }
        for (final ReconfigureOnChangeTaskListener listener : this.listeners) {
            listener.enteredRunMethod();
        }
    }
    
    private void fireChangeDetected() {
        if (this.listeners == null) {
            return;
        }
        for (final ReconfigureOnChangeTaskListener listener : this.listeners) {
            listener.changeDetected();
        }
    }
    
    private void fireDoneReconfiguring() {
        if (this.listeners == null) {
            return;
        }
        for (final ReconfigureOnChangeTaskListener listener : this.listeners) {
            listener.doneReconfiguring();
        }
    }
    
    private void performXMLConfiguration(final LoggerContext lc, final URL mainConfigurationURL) {
        final JoranConfigurator jc = new JoranConfigurator();
        jc.setContext(this.context);
        final StatusUtil statusUtil = new StatusUtil(this.context);
        final List<SaxEvent> eventList = jc.recallSafeConfiguration();
        final URL mainURL = ConfigurationWatchListUtil.getMainWatchURL(this.context);
        lc.reset();
        final long threshold = System.currentTimeMillis();
        try {
            jc.doConfigure(mainConfigurationURL);
            if (statusUtil.hasXMLParsingErrors(threshold)) {
                this.fallbackConfiguration(lc, eventList, mainURL);
            }
        }
        catch (JoranException ex) {
            this.fallbackConfiguration(lc, eventList, mainURL);
        }
    }
    
    private List<SaxEvent> removeIncludeEvents(final List<SaxEvent> unsanitizedEventList) {
        final List<SaxEvent> sanitizedEvents = new ArrayList<SaxEvent>();
        if (unsanitizedEventList == null) {
            return sanitizedEvents;
        }
        for (final SaxEvent e : unsanitizedEventList) {
            if (!"include".equalsIgnoreCase(e.getLocalName())) {
                sanitizedEvents.add(e);
            }
        }
        return sanitizedEvents;
    }
    
    private void fallbackConfiguration(final LoggerContext lc, final List<SaxEvent> eventList, final URL mainURL) {
        final List<SaxEvent> failsafeEvents = this.removeIncludeEvents(eventList);
        final JoranConfigurator joranConfigurator = new JoranConfigurator();
        joranConfigurator.setContext(this.context);
        final ConfigurationWatchList oldCWL = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
        final ConfigurationWatchList newCWL = oldCWL.buildClone();
        if (failsafeEvents == null || failsafeEvents.isEmpty()) {
            this.addWarn("No previous configuration to fall back on.");
        }
        else {
            this.addWarn("Given previous errors, falling back to previously registered safe configuration.");
            try {
                lc.reset();
                ConfigurationWatchListUtil.registerConfigurationWatchList(this.context, newCWL);
                joranConfigurator.doConfigure(failsafeEvents);
                this.addInfo("Re-registering previous fallback configuration once more as a fallback configuration point");
                joranConfigurator.registerSafeConfiguration(eventList);
                this.addInfo("after registerSafeConfiguration: " + eventList);
            }
            catch (JoranException e) {
                this.addError("Unexpected exception thrown by a configuration considered safe.", e);
            }
        }
    }
    
    @Override
    public String toString() {
        return "ReconfigureOnChangeTask(born:" + this.birthdate + ")";
    }
}
