// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.Appender;
import java.util.HashMap;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.spi.AppenderAttachable;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;

public class AppenderRefAction<E> extends Action
{
    boolean inError;
    
    public AppenderRefAction() {
        this.inError = false;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String tagName, final Attributes attributes) {
        this.inError = false;
        final Object o = ec.peekObject();
        if (!(o instanceof AppenderAttachable)) {
            final String errMsg = "Could not find an AppenderAttachable at the top of execution stack. Near [" + tagName + "] line " + this.getLineNumber(ec);
            this.inError = true;
            this.addError(errMsg);
            return;
        }
        final AppenderAttachable<E> appenderAttachable = (AppenderAttachable<E>)o;
        final String appenderName = ec.subst(attributes.getValue("ref"));
        if (OptionHelper.isEmpty(appenderName)) {
            final String errMsg2 = "Missing appender ref attribute in <appender-ref> tag.";
            this.inError = true;
            this.addError(errMsg2);
            return;
        }
        final HashMap<String, Appender<E>> appenderBag = ec.getObjectMap().get("APPENDER_BAG");
        final Appender<E> appender = appenderBag.get(appenderName);
        if (appender == null) {
            final String msg = "Could not find an appender named [" + appenderName + "]. Did you define it below instead of above in the configuration file?";
            this.inError = true;
            this.addError(msg);
            this.addError("See http://logback.qos.ch/codes.html#appender_order for more details.");
            return;
        }
        this.addInfo("Attaching appender named [" + appenderName + "] to " + appenderAttachable);
        appenderAttachable.addAppender(appender);
    }
    
    @Override
    public void end(final InterpretationContext ec, final String n) {
    }
}
