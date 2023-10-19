// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;

public class NOPAction extends Action
{
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) {
    }
    
    @Override
    public void end(final InterpretationContext ec, final String name) {
    }
}
