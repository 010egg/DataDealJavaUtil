// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import ch.qos.logback.core.sift.SiftingJoranConfiguratorBase;
import java.util.Map;
import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractAppenderFactoryUsingJoran;

public class AppenderFactoryUsingJoran extends AbstractAppenderFactoryUsingJoran<ILoggingEvent>
{
    AppenderFactoryUsingJoran(final List<SaxEvent> eventList, final String key, final Map<String, String> parentPropertyMap) {
        super(eventList, key, parentPropertyMap);
    }
    
    @Override
    public SiftingJoranConfiguratorBase<ILoggingEvent> getSiftingJoranConfigurator(final String discriminatingValue) {
        return new SiftingJoranConfigurator(this.key, discriminatingValue, this.parentPropertyMap);
    }
}
