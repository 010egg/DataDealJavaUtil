// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.util.AggregationType;
import ch.qos.logback.core.joran.util.PropertySetter;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.ElementPath;
import ch.qos.logback.core.joran.util.beans.BeanDescriptionCache;
import java.util.Stack;

public class NestedBasicPropertyIA extends ImplicitAction
{
    Stack<IADataForBasicProperty> actionDataStack;
    private final BeanDescriptionCache beanDescriptionCache;
    
    public NestedBasicPropertyIA(final BeanDescriptionCache beanDescriptionCache) {
        this.actionDataStack = new Stack<IADataForBasicProperty>();
        this.beanDescriptionCache = beanDescriptionCache;
    }
    
    @Override
    public boolean isApplicable(final ElementPath elementPath, final Attributes attributes, final InterpretationContext ec) {
        final String nestedElementTagName = elementPath.peekLast();
        if (ec.isEmpty()) {
            return false;
        }
        final Object o = ec.peekObject();
        final PropertySetter parentBean = new PropertySetter(this.beanDescriptionCache, o);
        parentBean.setContext(this.context);
        final AggregationType aggregationType = parentBean.computeAggregationType(nestedElementTagName);
        switch (aggregationType) {
            case NOT_FOUND:
            case AS_COMPLEX_PROPERTY:
            case AS_COMPLEX_PROPERTY_COLLECTION: {
                return false;
            }
            case AS_BASIC_PROPERTY:
            case AS_BASIC_PROPERTY_COLLECTION: {
                final IADataForBasicProperty ad = new IADataForBasicProperty(parentBean, aggregationType, nestedElementTagName);
                this.actionDataStack.push(ad);
                return true;
            }
            default: {
                this.addError("PropertySetter.canContainComponent returned " + aggregationType);
                return false;
            }
        }
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String localName, final Attributes attributes) {
    }
    
    @Override
    public void body(final InterpretationContext ec, final String body) {
        final String finalBody = ec.subst(body);
        final IADataForBasicProperty actionData = this.actionDataStack.peek();
        switch (actionData.aggregationType) {
            case AS_BASIC_PROPERTY: {
                actionData.parentBean.setProperty(actionData.propertyName, finalBody);
                return;
            }
            case AS_BASIC_PROPERTY_COLLECTION: {
                actionData.parentBean.addBasicProperty(actionData.propertyName, finalBody);
                break;
            }
        }
        this.addError("Unexpected aggregationType " + actionData.aggregationType);
    }
    
    @Override
    public void end(final InterpretationContext ec, final String tagName) {
        this.actionDataStack.pop();
    }
}
