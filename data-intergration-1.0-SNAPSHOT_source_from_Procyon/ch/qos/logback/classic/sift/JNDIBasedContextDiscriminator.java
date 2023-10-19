// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class JNDIBasedContextDiscriminator extends AbstractDiscriminator<ILoggingEvent>
{
    private static final String KEY = "contextName";
    private String defaultValue;
    
    @Override
    public String getDiscriminatingValue(final ILoggingEvent event) {
        final ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
        if (selector == null) {
            return this.defaultValue;
        }
        final LoggerContext lc = selector.getLoggerContext();
        if (lc == null) {
            return this.defaultValue;
        }
        return lc.getName();
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
