// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran.action;

import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.action.Action;

public class ContextNameAction extends Action
{
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) {
    }
    
    @Override
    public void body(final InterpretationContext ec, final String body) {
        final String finalBody = ec.subst(body);
        this.addInfo("Setting logger context name as [" + finalBody + "]");
        try {
            this.context.setName(finalBody);
        }
        catch (IllegalStateException e) {
            this.addError("Failed to rename context [" + this.context.getName() + "] as [" + finalBody + "]", e);
        }
    }
    
    @Override
    public void end(final InterpretationContext ec, final String name) {
    }
}
