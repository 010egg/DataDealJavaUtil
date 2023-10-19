// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ActionException;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;

public class ContextPropertyAction extends Action
{
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) throws ActionException {
        this.addError("The [contextProperty] element has been removed. Please use [substitutionProperty] element instead");
    }
    
    @Override
    public void end(final InterpretationContext ec, final String name) throws ActionException {
    }
}
