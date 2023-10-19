// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.conditional;

import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;

public class ElseAction extends ThenOrElseActionBase
{
    @Override
    void registerEventList(final IfAction ifAction, final List<SaxEvent> eventList) {
        ifAction.setElseSaxEventList(eventList);
    }
}
