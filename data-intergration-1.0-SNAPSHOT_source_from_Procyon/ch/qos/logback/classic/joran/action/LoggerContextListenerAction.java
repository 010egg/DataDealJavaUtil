// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.spi.ContextAware;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.joran.action.Action;

public class LoggerContextListenerAction extends Action
{
    boolean inError;
    LoggerContextListener lcl;
    
    public LoggerContextListenerAction() {
        this.inError = false;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) throws ActionException {
        this.inError = false;
        final String className = attributes.getValue("class");
        if (OptionHelper.isEmpty(className)) {
            this.addError("Mandatory \"class\" attribute not set for <loggerContextListener> element");
            this.inError = true;
            return;
        }
        try {
            this.lcl = (LoggerContextListener)OptionHelper.instantiateByClassName(className, LoggerContextListener.class, this.context);
            if (this.lcl instanceof ContextAware) {
                ((ContextAware)this.lcl).setContext(this.context);
            }
            ec.pushObject(this.lcl);
            this.addInfo("Adding LoggerContextListener of type [" + className + "] to the object stack");
        }
        catch (Exception oops) {
            this.inError = true;
            this.addError("Could not create LoggerContextListener of type " + className + "].", oops);
        }
    }
    
    @Override
    public void end(final InterpretationContext ec, final String name) throws ActionException {
        if (this.inError) {
            return;
        }
        final Object o = ec.peekObject();
        if (o != this.lcl) {
            this.addWarn("The object on the top the of the stack is not the LoggerContextListener pushed earlier.");
        }
        else {
            if (this.lcl instanceof LifeCycle) {
                ((LifeCycle)this.lcl).start();
                this.addInfo("Starting LoggerContextListener");
            }
            ((LoggerContext)this.context).addListener(this.lcl);
            ec.popObject();
        }
    }
}
