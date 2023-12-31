// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran.action;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.classic.net.ReceiverBase;
import ch.qos.logback.core.joran.action.Action;

public class ReceiverAction extends Action
{
    private ReceiverBase receiver;
    private boolean inError;
    
    @Override
    public void begin(final InterpretationContext ic, final String name, final Attributes attributes) throws ActionException {
        final String className = attributes.getValue("class");
        if (OptionHelper.isEmpty(className)) {
            this.addError("Missing class name for receiver. Near [" + name + "] line " + this.getLineNumber(ic));
            this.inError = true;
            return;
        }
        try {
            this.addInfo("About to instantiate receiver of type [" + className + "]");
            (this.receiver = (ReceiverBase)OptionHelper.instantiateByClassName(className, ReceiverBase.class, this.context)).setContext(this.context);
            ic.pushObject(this.receiver);
        }
        catch (Exception ex) {
            this.inError = true;
            this.addError("Could not create a receiver of type [" + className + "].", ex);
            throw new ActionException(ex);
        }
    }
    
    @Override
    public void end(final InterpretationContext ic, final String name) throws ActionException {
        if (this.inError) {
            return;
        }
        ic.getContext().register(this.receiver);
        this.receiver.start();
        final Object o = ic.peekObject();
        if (o != this.receiver) {
            this.addWarn("The object at the of the stack is not the remote pushed earlier.");
        }
        else {
            ic.popObject();
        }
    }
}
