// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.jmx;

import java.util.List;
import java.net.URL;
import java.io.FileNotFoundException;
import ch.qos.logback.core.joran.spi.JoranException;

public interface JMXConfiguratorMBean
{
    void reloadDefaultConfiguration() throws JoranException;
    
    void reloadByFileName(final String p0) throws JoranException, FileNotFoundException;
    
    void reloadByURL(final URL p0) throws JoranException;
    
    void setLoggerLevel(final String p0, final String p1);
    
    String getLoggerLevel(final String p0);
    
    String getLoggerEffectiveLevel(final String p0);
    
    List<String> getLoggerList();
    
    List<String> getStatuses();
}
