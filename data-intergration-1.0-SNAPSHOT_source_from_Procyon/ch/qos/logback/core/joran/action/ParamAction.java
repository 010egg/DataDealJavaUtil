// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.util.PropertySetter;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.util.beans.BeanDescriptionCache;

public class ParamAction extends Action
{
    static String NO_NAME;
    static String NO_VALUE;
    boolean inError;
    private final BeanDescriptionCache beanDescriptionCache;
    
    public ParamAction(final BeanDescriptionCache beanDescriptionCache) {
        this.inError = false;
        this.beanDescriptionCache = beanDescriptionCache;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String localName, final Attributes attributes) {
        String name = attributes.getValue("name");
        String value = attributes.getValue("value");
        if (name == null) {
            this.inError = true;
            this.addError(ParamAction.NO_NAME);
            return;
        }
        if (value == null) {
            this.inError = true;
            this.addError(ParamAction.NO_VALUE);
            return;
        }
        value = value.trim();
        final Object o = ec.peekObject();
        final PropertySetter propSetter = new PropertySetter(this.beanDescriptionCache, o);
        propSetter.setContext(this.context);
        value = ec.subst(value);
        name = ec.subst(name);
        propSetter.setProperty(name, value);
    }
    
    @Override
    public void end(final InterpretationContext ec, final String localName) {
    }
    
    public void finish(final InterpretationContext ec) {
    }
    
    static {
        ParamAction.NO_NAME = "No name attribute in <param> element";
        ParamAction.NO_VALUE = "No name attribute in <param> element";
    }
}
