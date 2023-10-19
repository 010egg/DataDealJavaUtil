// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.turbo;

import org.slf4j.MDC;
import ch.qos.logback.classic.Logger;
import org.slf4j.Marker;
import java.util.HashMap;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.classic.Level;
import java.util.Map;

public class DynamicThresholdFilter extends TurboFilter
{
    private Map<String, Level> valueLevelMap;
    private Level defaultThreshold;
    private String key;
    private FilterReply onHigherOrEqual;
    private FilterReply onLower;
    
    public DynamicThresholdFilter() {
        this.valueLevelMap = new HashMap<String, Level>();
        this.defaultThreshold = Level.ERROR;
        this.onHigherOrEqual = FilterReply.NEUTRAL;
        this.onLower = FilterReply.DENY;
    }
    
    public String getKey() {
        return this.key;
    }
    
    public void setKey(final String key) {
        this.key = key;
    }
    
    public Level getDefaultThreshold() {
        return this.defaultThreshold;
    }
    
    public void setDefaultThreshold(final Level defaultThreshold) {
        this.defaultThreshold = defaultThreshold;
    }
    
    public FilterReply getOnHigherOrEqual() {
        return this.onHigherOrEqual;
    }
    
    public void setOnHigherOrEqual(final FilterReply onHigherOrEqual) {
        this.onHigherOrEqual = onHigherOrEqual;
    }
    
    public FilterReply getOnLower() {
        return this.onLower;
    }
    
    public void setOnLower(final FilterReply onLower) {
        this.onLower = onLower;
    }
    
    public void addMDCValueLevelPair(final MDCValueLevelPair mdcValueLevelPair) {
        if (this.valueLevelMap.containsKey(mdcValueLevelPair.getValue())) {
            this.addError(String.valueOf(mdcValueLevelPair.getValue()) + " has been already set");
        }
        else {
            this.valueLevelMap.put(mdcValueLevelPair.getValue(), mdcValueLevelPair.getLevel());
        }
    }
    
    @Override
    public void start() {
        if (this.key == null) {
            this.addError("No key name was specified");
        }
        super.start();
    }
    
    @Override
    public FilterReply decide(final Marker marker, final Logger logger, final Level level, final String s, final Object[] objects, final Throwable throwable) {
        final String mdcValue = MDC.get(this.key);
        if (!this.isStarted()) {
            return FilterReply.NEUTRAL;
        }
        Level levelAssociatedWithMDCValue = null;
        if (mdcValue != null) {
            levelAssociatedWithMDCValue = this.valueLevelMap.get(mdcValue);
        }
        if (levelAssociatedWithMDCValue == null) {
            levelAssociatedWithMDCValue = this.defaultThreshold;
        }
        if (level.isGreaterOrEqual(levelAssociatedWithMDCValue)) {
            return this.onHigherOrEqual;
        }
        return this.onLower;
    }
}
