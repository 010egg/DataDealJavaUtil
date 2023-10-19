// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.Level;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.classic.LoggerContext;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.joran.action.Action;

public class LoggerAction extends Action
{
    public static final String LEVEL_ATTRIBUTE = "level";
    boolean inError;
    Logger logger;
    
    public LoggerAction() {
        this.inError = false;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) {
        this.inError = false;
        this.logger = null;
        final LoggerContext loggerContext = (LoggerContext)this.context;
        final String loggerName = ec.subst(attributes.getValue("name"));
        if (OptionHelper.isEmpty(loggerName)) {
            this.inError = true;
            final String aroundLine = this.getLineColStr(ec);
            final String errorMsg = "No 'name' attribute in element " + name + ", around " + aroundLine;
            this.addError(errorMsg);
            return;
        }
        this.logger = loggerContext.getLogger(loggerName);
        final String levelStr = ec.subst(attributes.getValue("level"));
        if (!OptionHelper.isEmpty(levelStr)) {
            if ("INHERITED".equalsIgnoreCase(levelStr) || "NULL".equalsIgnoreCase(levelStr)) {
                this.addInfo("Setting level of logger [" + loggerName + "] to null, i.e. INHERITED");
                this.logger.setLevel(null);
            }
            else {
                final Level level = Level.toLevel(levelStr);
                this.addInfo("Setting level of logger [" + loggerName + "] to " + level);
                this.logger.setLevel(level);
            }
        }
        final String additivityStr = ec.subst(attributes.getValue("additivity"));
        if (!OptionHelper.isEmpty(additivityStr)) {
            final boolean additive = OptionHelper.toBoolean(additivityStr, true);
            this.addInfo("Setting additivity of logger [" + loggerName + "] to " + additive);
            this.logger.setAdditive(additive);
        }
        ec.pushObject(this.logger);
    }
    
    @Override
    public void end(final InterpretationContext ec, final String e) {
        if (this.inError) {
            return;
        }
        final Object o = ec.peekObject();
        if (o != this.logger) {
            this.addWarn("The object on the top the of the stack is not " + this.logger + " pushed earlier");
            this.addWarn("It is: " + o);
        }
        else {
            ec.popObject();
        }
    }
    
    public void finish(final InterpretationContext ec) {
    }
}
