// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import java.util.Iterator;
import java.util.Set;
import ch.qos.logback.core.status.WarnStatus;
import java.io.IOException;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.classic.BasicConfigurator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.util.StatusListenerConfigHelper;
import java.net.MalformedURLException;
import java.io.File;
import ch.qos.logback.core.util.Loader;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.Context;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.classic.gaffer.GafferUtil;
import java.net.URL;
import ch.qos.logback.classic.LoggerContext;

public class ContextInitializer
{
    public static final String GROOVY_AUTOCONFIG_FILE = "logback.groovy";
    public static final String AUTOCONFIG_FILE = "logback.xml";
    public static final String TEST_AUTOCONFIG_FILE = "logback-test.xml";
    public static final String CONFIG_FILE_PROPERTY = "logback.configurationFile";
    final LoggerContext loggerContext;
    
    public ContextInitializer(final LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
    }
    
    public void configureByResource(final URL url) throws JoranException {
        if (url == null) {
            throw new IllegalArgumentException("URL argument cannot be null");
        }
        final String urlString = url.toString();
        if (urlString.endsWith("groovy")) {
            if (EnvUtil.isGroovyAvailable()) {
                GafferUtil.runGafferConfiguratorOn(this.loggerContext, this, url);
            }
            else {
                final StatusManager sm = this.loggerContext.getStatusManager();
                sm.add(new ErrorStatus("Groovy classes are not available on the class path. ABORTING INITIALIZATION.", this.loggerContext));
            }
        }
        else {
            if (!urlString.endsWith("xml")) {
                throw new LogbackException("Unexpected filename extension of file [" + url.toString() + "]. Should be either .groovy or .xml");
            }
            final JoranConfigurator configurator = new JoranConfigurator();
            configurator.setContext(this.loggerContext);
            configurator.doConfigure(url);
        }
    }
    
    void joranConfigureByResource(final URL url) throws JoranException {
        final JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(this.loggerContext);
        configurator.doConfigure(url);
    }
    
    private URL findConfigFileURLFromSystemProperties(final ClassLoader classLoader, final boolean updateStatus) {
        final String logbackConfigFile = OptionHelper.getSystemProperty("logback.configurationFile");
        if (logbackConfigFile != null) {
            URL result = null;
            try {
                result = new URL(logbackConfigFile);
                return result;
            }
            catch (MalformedURLException ex) {
                result = Loader.getResource(logbackConfigFile, classLoader);
                if (result != null) {
                    return result;
                }
                final File f = new File(logbackConfigFile);
                if (f.exists() && f.isFile()) {
                    try {
                        result = f.toURI().toURL();
                        return result;
                    }
                    catch (MalformedURLException ex2) {}
                }
            }
            finally {
                if (updateStatus) {
                    this.statusOnResourceSearch(logbackConfigFile, classLoader, result);
                }
            }
        }
        return null;
    }
    
    public URL findURLOfDefaultConfigurationFile(final boolean updateStatus) {
        final ClassLoader myClassLoader = Loader.getClassLoaderOfObject(this);
        URL url = this.findConfigFileURLFromSystemProperties(myClassLoader, updateStatus);
        if (url != null) {
            return url;
        }
        url = this.getResource("logback.groovy", myClassLoader, updateStatus);
        if (url != null) {
            return url;
        }
        url = this.getResource("logback-test.xml", myClassLoader, updateStatus);
        if (url != null) {
            return url;
        }
        return this.getResource("logback.xml", myClassLoader, updateStatus);
    }
    
    private URL getResource(final String filename, final ClassLoader myClassLoader, final boolean updateStatus) {
        final URL url = Loader.getResource(filename, myClassLoader);
        if (updateStatus) {
            this.statusOnResourceSearch(filename, myClassLoader, url);
        }
        return url;
    }
    
    public void autoConfig() throws JoranException {
        StatusListenerConfigHelper.installIfAsked(this.loggerContext);
        final URL url = this.findURLOfDefaultConfigurationFile(true);
        if (url != null) {
            this.configureByResource(url);
        }
        else {
            final Configurator c = EnvUtil.loadFromServiceLoader(Configurator.class);
            if (c != null) {
                try {
                    c.setContext(this.loggerContext);
                    c.configure(this.loggerContext);
                    return;
                }
                catch (Exception e) {
                    throw new LogbackException(String.format("Failed to initialize Configurator: %s using ServiceLoader", (c != null) ? c.getClass().getCanonicalName() : "null"), e);
                }
            }
            final BasicConfigurator basicConfigurator = new BasicConfigurator();
            basicConfigurator.setContext(this.loggerContext);
            basicConfigurator.configure(this.loggerContext);
        }
    }
    
    private void statusOnResourceSearch(final String resourceName, final ClassLoader classLoader, final URL url) {
        final StatusManager sm = this.loggerContext.getStatusManager();
        if (url == null) {
            sm.add(new InfoStatus("Could NOT find resource [" + resourceName + "]", this.loggerContext));
        }
        else {
            sm.add(new InfoStatus("Found resource [" + resourceName + "] at [" + url.toString() + "]", this.loggerContext));
            this.multiplicityWarning(resourceName, classLoader);
        }
    }
    
    private void multiplicityWarning(final String resourceName, final ClassLoader classLoader) {
        Set<URL> urlSet = null;
        final StatusManager sm = this.loggerContext.getStatusManager();
        try {
            urlSet = Loader.getResources(resourceName, classLoader);
        }
        catch (IOException e) {
            sm.add(new ErrorStatus("Failed to get url list for resource [" + resourceName + "]", this.loggerContext, e));
        }
        if (urlSet != null && urlSet.size() > 1) {
            sm.add(new WarnStatus("Resource [" + resourceName + "] occurs multiple times on the classpath.", this.loggerContext));
            for (final URL url : urlSet) {
                sm.add(new WarnStatus("Resource [" + resourceName + "] occurs at [" + url.toString() + "]", this.loggerContext));
            }
        }
    }
}
