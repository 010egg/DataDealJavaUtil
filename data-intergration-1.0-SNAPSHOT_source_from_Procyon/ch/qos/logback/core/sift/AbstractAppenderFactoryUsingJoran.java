// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import java.util.Map;
import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;

public abstract class AbstractAppenderFactoryUsingJoran<E> implements AppenderFactory<E>
{
    final List<SaxEvent> eventList;
    protected String key;
    protected Map<String, String> parentPropertyMap;
    
    protected AbstractAppenderFactoryUsingJoran(final List<SaxEvent> eventList, final String key, final Map<String, String> parentPropertyMap) {
        this.eventList = this.removeSiftElement(eventList);
        this.key = key;
        this.parentPropertyMap = parentPropertyMap;
    }
    
    List<SaxEvent> removeSiftElement(final List<SaxEvent> eventList) {
        return eventList.subList(1, eventList.size() - 1);
    }
    
    public abstract SiftingJoranConfiguratorBase<E> getSiftingJoranConfigurator(final String p0);
    
    @Override
    public Appender<E> buildAppender(final Context context, final String discriminatingValue) throws JoranException {
        final SiftingJoranConfiguratorBase<E> sjc = this.getSiftingJoranConfigurator(discriminatingValue);
        sjc.setContext(context);
        sjc.doConfigure(this.eventList);
        return sjc.getAppender();
    }
    
    public List<SaxEvent> getEventList() {
        return this.eventList;
    }
}
