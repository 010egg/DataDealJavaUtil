// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.joran.event.SaxEvent;
import java.util.List;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.action.DefinePropertyAction;
import ch.qos.logback.core.joran.action.TimestampAction;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.action.PropertyAction;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.joran.action.NestedBasicPropertyIA;
import ch.qos.logback.core.joran.action.ImplicitAction;
import ch.qos.logback.core.joran.action.NestedComplexPropertyIA;
import ch.qos.logback.core.joran.spi.Interpreter;
import java.util.Map;
import ch.qos.logback.core.joran.GenericConfigurator;

public abstract class SiftingJoranConfiguratorBase<E> extends GenericConfigurator
{
    protected final String key;
    protected final String value;
    protected final Map<String, String> parentPropertyMap;
    static final String ONE_AND_ONLY_ONE_URL = "http://logback.qos.ch/codes.html#1andOnly1";
    int errorEmmissionCount;
    
    protected SiftingJoranConfiguratorBase(final String key, final String value, final Map<String, String> parentPropertyMap) {
        this.errorEmmissionCount = 0;
        this.key = key;
        this.value = value;
        this.parentPropertyMap = parentPropertyMap;
    }
    
    @Override
    protected void addImplicitRules(final Interpreter interpreter) {
        final NestedComplexPropertyIA nestedComplexIA = new NestedComplexPropertyIA(this.getBeanDescriptionCache());
        nestedComplexIA.setContext(this.context);
        interpreter.addImplicitAction(nestedComplexIA);
        final NestedBasicPropertyIA nestedSimpleIA = new NestedBasicPropertyIA(this.getBeanDescriptionCache());
        nestedSimpleIA.setContext(this.context);
        interpreter.addImplicitAction(nestedSimpleIA);
    }
    
    @Override
    protected void addInstanceRules(final RuleStore rs) {
        rs.addRule(new ElementSelector("configuration/property"), new PropertyAction());
        rs.addRule(new ElementSelector("configuration/timestamp"), new TimestampAction());
        rs.addRule(new ElementSelector("configuration/define"), new DefinePropertyAction());
    }
    
    public abstract Appender<E> getAppender();
    
    protected void oneAndOnlyOneCheck(final Map<?, ?> appenderMap) {
        String errMsg = null;
        if (appenderMap.size() == 0) {
            ++this.errorEmmissionCount;
            errMsg = "No nested appenders found within the <sift> element in SiftingAppender.";
        }
        else if (appenderMap.size() > 1) {
            ++this.errorEmmissionCount;
            errMsg = "Only and only one appender can be nested the <sift> element in SiftingAppender. See also http://logback.qos.ch/codes.html#1andOnly1";
        }
        if (errMsg != null && this.errorEmmissionCount < 4) {
            this.addError(errMsg);
        }
    }
    
    @Override
    public void doConfigure(final List<SaxEvent> eventList) throws JoranException {
        super.doConfigure(eventList);
    }
    
    @Override
    public String toString() {
        return this.getClass().getName() + "{" + this.key + "=" + this.value + '}';
    }
}
