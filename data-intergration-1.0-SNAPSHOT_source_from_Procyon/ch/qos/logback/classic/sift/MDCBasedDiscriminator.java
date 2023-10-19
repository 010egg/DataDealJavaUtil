// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import ch.qos.logback.core.util.OptionHelper;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class MDCBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent>
{
    private String key;
    private String defaultValue;
    
    @Override
    public String getDiscriminatingValue(final ILoggingEvent event) {
        final Map<String, String> mdcMap = event.getMDCPropertyMap();
        if (mdcMap == null) {
            return this.defaultValue;
        }
        final String mdcValue = mdcMap.get(this.key);
        if (mdcValue == null) {
            return this.defaultValue;
        }
        return mdcValue;
    }
    
    @Override
    public void start() {
        int errors = 0;
        if (OptionHelper.isEmpty(this.key)) {
            ++errors;
            this.addError("The \"Key\" property must be set");
        }
        if (OptionHelper.isEmpty(this.defaultValue)) {
            ++errors;
            this.addError("The \"DefaultValue\" property must be set");
        }
        if (errors == 0) {
            this.started = true;
        }
    }
    
    @Override
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public String getDefaultValue() {
        return this.defaultValue;
    }
    
    public void setDefaultValue(final String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
