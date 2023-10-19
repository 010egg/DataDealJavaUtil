// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;

public class NewRuleAction extends Action
{
    boolean inError;
    
    public NewRuleAction() {
        this.inError = false;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String localName, final Attributes attributes) {
        this.inError = false;
        final String pattern = attributes.getValue("pattern");
        final String actionClass = attributes.getValue("actionClass");
        if (OptionHelper.isEmpty(pattern)) {
            this.inError = true;
            final String errorMsg = "No 'pattern' attribute in <newRule>";
            this.addError(errorMsg);
            return;
        }
        if (OptionHelper.isEmpty(actionClass)) {
            this.inError = true;
            final String errorMsg = "No 'actionClass' attribute in <newRule>";
            this.addError(errorMsg);
            return;
        }
        try {
            this.addInfo("About to add new Joran parsing rule [" + pattern + "," + actionClass + "].");
            ec.getJoranInterpreter().getRuleStore().addRule(new ElementSelector(pattern), actionClass);
        }
        catch (Exception oops) {
            this.inError = true;
            final String errorMsg = "Could not add new Joran parsing rule [" + pattern + "," + actionClass + "]";
            this.addError(errorMsg);
        }
    }
    
    @Override
    public void end(final InterpretationContext ec, final String n) {
    }
    
    public void finish(final InterpretationContext ec) {
    }
}
