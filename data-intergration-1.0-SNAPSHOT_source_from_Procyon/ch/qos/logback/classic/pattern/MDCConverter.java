// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.pattern;

import java.util.Iterator;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.util.OptionHelper;

public class MDCConverter extends ClassicConverter
{
    private String key;
    private String defaultValue;
    
    public MDCConverter() {
        this.defaultValue = "";
    }
    
    @Override
    public void start() {
        final String[] keyInfo = OptionHelper.extractDefaultReplacement(this.getFirstOption());
        this.key = keyInfo[0];
        if (keyInfo[1] != null) {
            this.defaultValue = keyInfo[1];
        }
        super.start();
    }
    
    @Override
    public void stop() {
        this.key = null;
        super.stop();
    }
    
    @Override
    public String convert(final ILoggingEvent event) {
        final Map<String, String> mdcPropertyMap = event.getMDCPropertyMap();
        if (mdcPropertyMap == null) {
            return this.defaultValue;
        }
        if (this.key == null) {
            return this.outputMDCForAllKeys(mdcPropertyMap);
        }
        final String value = event.getMDCPropertyMap().get(this.key);
        if (value != null) {
            return value;
        }
        return this.defaultValue;
    }
    
    private String outputMDCForAllKeys(final Map<String, String> mdcPropertyMap) {
        final StringBuilder buf = new StringBuilder();
        boolean first = true;
        for (final Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
            if (first) {
                first = false;
            }
            else {
                buf.append(", ");
            }
            buf.append(entry.getKey()).append('=').append(entry.getValue());
        }
        return buf.toString();
    }
}
