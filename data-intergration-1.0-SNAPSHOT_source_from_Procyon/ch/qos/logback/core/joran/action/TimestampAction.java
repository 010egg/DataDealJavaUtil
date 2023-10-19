// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.util.CachingDateFormatter;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;

public class TimestampAction extends Action
{
    static String DATE_PATTERN_ATTRIBUTE;
    static String TIME_REFERENCE_ATTRIBUTE;
    static String CONTEXT_BIRTH;
    boolean inError;
    
    public TimestampAction() {
        this.inError = false;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) throws ActionException {
        final String keyStr = attributes.getValue("key");
        if (OptionHelper.isEmpty(keyStr)) {
            this.addError("Attribute named [key] cannot be empty");
            this.inError = true;
        }
        final String datePatternStr = attributes.getValue(TimestampAction.DATE_PATTERN_ATTRIBUTE);
        if (OptionHelper.isEmpty(datePatternStr)) {
            this.addError("Attribute named [" + TimestampAction.DATE_PATTERN_ATTRIBUTE + "] cannot be empty");
            this.inError = true;
        }
        final String timeReferenceStr = attributes.getValue(TimestampAction.TIME_REFERENCE_ATTRIBUTE);
        long timeReference;
        if (TimestampAction.CONTEXT_BIRTH.equalsIgnoreCase(timeReferenceStr)) {
            this.addInfo("Using context birth as time reference.");
            timeReference = this.context.getBirthTime();
        }
        else {
            timeReference = System.currentTimeMillis();
            this.addInfo("Using current interpretation time, i.e. now, as time reference.");
        }
        if (this.inError) {
            return;
        }
        final String scopeStr = attributes.getValue("scope");
        final ActionUtil.Scope scope = ActionUtil.stringToScope(scopeStr);
        final CachingDateFormatter sdf = new CachingDateFormatter(datePatternStr);
        final String val = sdf.format(timeReference);
        this.addInfo("Adding property to the context with key=\"" + keyStr + "\" and value=\"" + val + "\" to the " + scope + " scope");
        ActionUtil.setProperty(ec, keyStr, val, scope);
    }
    
    @Override
    public void end(final InterpretationContext ec, final String name) throws ActionException {
    }
    
    static {
        TimestampAction.DATE_PATTERN_ATTRIBUTE = "datePattern";
        TimestampAction.TIME_REFERENCE_ATTRIBUTE = "timeReference";
        TimestampAction.CONTEXT_BIRTH = "contextBirth";
    }
}
