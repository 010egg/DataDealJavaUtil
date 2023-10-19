// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import java.util.Collection;
import ch.qos.logback.core.Appender;
import java.util.HashMap;
import ch.qos.logback.classic.util.DefaultNestedComponentRules;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.AppenderAction;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.joran.spi.ElementPath;
import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.SiftingJoranConfiguratorBase;

public class SiftingJoranConfigurator extends SiftingJoranConfiguratorBase<ILoggingEvent>
{
    SiftingJoranConfigurator(final String key, final String value, final Map<String, String> parentPropertyMap) {
        super(key, value, parentPropertyMap);
    }
    
    @Override
    protected ElementPath initialElementPath() {
        return new ElementPath("configuration");
    }
    
    @Override
    protected void addInstanceRules(final RuleStore rs) {
        super.addInstanceRules(rs);
        rs.addRule(new ElementSelector("configuration/appender"), new AppenderAction<Object>());
    }
    
    @Override
    protected void addDefaultNestedComponentRegistryRules(final DefaultNestedComponentRegistry registry) {
        DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(registry);
    }
    
    @Override
    protected void buildInterpreter() {
        super.buildInterpreter();
        final Map<String, Object> omap = this.interpreter.getInterpretationContext().getObjectMap();
        omap.put("APPENDER_BAG", new HashMap());
        omap.put("FILTER_CHAIN_BAG", new HashMap());
        final Map<String, String> propertiesMap = new HashMap<String, String>();
        propertiesMap.putAll(this.parentPropertyMap);
        propertiesMap.put(this.key, this.value);
        this.interpreter.setInterpretationContextPropertiesMap(propertiesMap);
    }
    
    @Override
    public Appender<ILoggingEvent> getAppender() {
        final Map<String, Object> omap = this.interpreter.getInterpretationContext().getObjectMap();
        final HashMap appenderMap = omap.get("APPENDER_BAG");
        this.oneAndOnlyOneCheck(appenderMap);
        final Collection values = appenderMap.values();
        if (values.size() == 0) {
            return null;
        }
        return values.iterator().next();
    }
}
