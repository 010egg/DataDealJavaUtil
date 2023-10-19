// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class ContextBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent>
{
    private static final String KEY = "contextName";
    private String defaultValue;
    
    @Override
    public String getDiscriminatingValue(final ILoggingEvent event) {
        final String contextName = event.getLoggerContextVO().getName();
        if (contextName == null) {
            return this.defaultValue;
        }
        return contextName;
    }
    
    @Override
    public String getKey() {
        return "contextName";
    }
    
    public void setKey(final String key) {
        throw new UnsupportedOperationException("Key cannot be set. Using fixed key contextName");
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
