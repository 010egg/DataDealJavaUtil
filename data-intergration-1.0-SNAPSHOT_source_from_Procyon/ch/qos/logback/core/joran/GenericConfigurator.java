// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.joran.event.SaxEventRecorder;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.SimpleRuleStore;
import ch.qos.logback.core.joran.spi.ElementPath;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import ch.qos.logback.core.joran.spi.RuleStore;
import org.xml.sax.InputSource;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.Context;
import java.io.FileInputStream;
import java.io.File;
import java.net.URLConnection;
import java.io.InputStream;
import java.io.IOException;
import ch.qos.logback.core.joran.spi.JoranException;
import java.net.URL;
import ch.qos.logback.core.joran.spi.Interpreter;
import ch.qos.logback.core.joran.util.beans.BeanDescriptionCache;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class GenericConfigurator extends ContextAwareBase
{
    private final BeanDescriptionCache beanDescriptionCache;
    protected Interpreter interpreter;
    
    public GenericConfigurator() {
        this.beanDescriptionCache = new BeanDescriptionCache();
    }
    
    public final void doConfigure(final URL url) throws JoranException {
        InputStream in = null;
        try {
            informContextOfURLUsedForConfiguration(this.getContext(), url);
            final URLConnection urlConnection = url.openConnection();
            urlConnection.setUseCaches(false);
            in = urlConnection.getInputStream();
            this.doConfigure(in);
        }
        catch (IOException ioe) {
            final String errMsg = "Could not open URL [" + url + "].";
            this.addError(errMsg, ioe);
            throw new JoranException(errMsg, ioe);
        }
        finally {
            if (in != null) {
                try {
                    in.close();
                }
                catch (IOException ioe2) {
                    final String errMsg2 = "Could not close input stream";
                    this.addError(errMsg2, ioe2);
                    throw new JoranException(errMsg2, ioe2);
                }
            }
        }
    }
    
    public final void doConfigure(final String filename) throws JoranException {
        this.doConfigure(new File(filename));
    }
    
    public final void doConfigure(final File file) throws JoranException {
        FileInputStream fis = null;
        try {
            informContextOfURLUsedForConfiguration(this.getContext(), file.toURI().toURL());
            fis = new FileInputStream(file);
            this.doConfigure(fis);
        }
        catch (IOException ioe) {
            final String errMsg = "Could not open [" + file.getPath() + "].";
            this.addError(errMsg, ioe);
            throw new JoranException(errMsg, ioe);
        }
        finally {
            if (fis != null) {
                try {
                    fis.close();
                }
                catch (IOException ioe2) {
                    final String errMsg2 = "Could not close [" + file.getName() + "].";
                    this.addError(errMsg2, ioe2);
                    throw new JoranException(errMsg2, ioe2);
                }
            }
        }
    }
    
    public static void informContextOfURLUsedForConfiguration(final Context context, final URL url) {
        ConfigurationWatchListUtil.setMainWatchURL(context, url);
    }
    
    public final void doConfigure(final InputStream inputStream) throws JoranException {
        this.doConfigure(new InputSource(inputStream));
    }
    
    protected BeanDescriptionCache getBeanDescriptionCache() {
        return this.beanDescriptionCache;
    }
    
    protected abstract void addInstanceRules(final RuleStore p0);
    
    protected abstract void addImplicitRules(final Interpreter p0);
    
    protected void addDefaultNestedComponentRegistryRules(final DefaultNestedComponentRegistry registry) {
    }
    
    protected ElementPath initialElementPath() {
        return new ElementPath();
    }
    
    protected void buildInterpreter() {
        final RuleStore rs = new SimpleRuleStore(this.context);
        this.addInstanceRules(rs);
        this.interpreter = new Interpreter(this.context, rs, this.initialElementPath());
        final InterpretationContext interpretationContext = this.interpreter.getInterpretationContext();
        interpretationContext.setContext(this.context);
        this.addImplicitRules(this.interpreter);
        this.addDefaultNestedComponentRegistryRules(interpretationContext.getDefaultNestedComponentRegistry());
    }
    
    public final void doConfigure(final InputSource inputSource) throws JoranException {
        final long threshold = System.currentTimeMillis();
        final SaxEventRecorder recorder = new SaxEventRecorder(this.context);
        recorder.recordEvents(inputSource);
        this.doConfigure(recorder.saxEventList);
        final StatusUtil statusUtil = new StatusUtil(this.context);
        if (statusUtil.noXMLParsingErrorsOccurred(threshold)) {
            this.addInfo("Registering current configuration as safe fallback point");
            this.registerSafeConfiguration(recorder.saxEventList);
        }
    }
    
    public void doConfigure(final List<SaxEvent> eventList) throws JoranException {
        this.buildInterpreter();
        synchronized (this.context.getConfigurationLock()) {
            this.interpreter.getEventPlayer().play(eventList);
        }
    }
    
    public void registerSafeConfiguration(final List<SaxEvent> eventList) {
        this.context.putObject("SAFE_JORAN_CONFIGURATION", eventList);
    }
    
    public List<SaxEvent> recallSafeConfiguration() {
        return (List<SaxEvent>)this.context.getObject("SAFE_JORAN_CONFIGURATION");
    }
}
