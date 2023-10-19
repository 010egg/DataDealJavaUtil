// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.sift;

import java.util.Map;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AppenderFactory;
import ch.qos.logback.core.joran.spi.ActionException;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;
import ch.qos.logback.core.joran.event.InPlayListener;
import ch.qos.logback.core.joran.action.Action;

public class SiftAction extends Action implements InPlayListener
{
    List<SaxEvent> seList;
    
    @Override
    public void begin(final InterpretationContext ic, final String name, final Attributes attributes) throws ActionException {
        this.seList = new ArrayList<SaxEvent>();
        ic.addInPlayListener(this);
    }
    
    @Override
    public void end(final InterpretationContext ic, final String name) throws ActionException {
        ic.removeInPlayListener(this);
        final Object o = ic.peekObject();
        if (o instanceof SiftingAppender) {
            final SiftingAppender sa = (SiftingAppender)o;
            final Map<String, String> propertyMap = ic.getCopyOfPropertyMap();
            final AppenderFactoryUsingJoran appenderFactory = new AppenderFactoryUsingJoran(this.seList, sa.getDiscriminatorKey(), propertyMap);
            sa.setAppenderFactory(appenderFactory);
        }
    }
    
    @Override
    public void inPlay(final SaxEvent event) {
        this.seList.add(event);
    }
    
    public List<SaxEvent> getSeList() {
        return this.seList;
    }
}
