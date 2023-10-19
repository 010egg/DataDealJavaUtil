// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.xml.sax.Attributes;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.action.Action;

public class LevelAction extends Action
{
    boolean inError;
    
    public LevelAction() {
        this.inError = false;
    }
    
    @Override
    public void begin(final InterpretationContext ec, final String name, final Attributes attributes) {
        final Object o = ec.peekObject();
        if (!(o instanceof Logger)) {
            this.inError = true;
            this.addError("For element <level>, could not find a logger at the top of execution stack.");
            return;
        }
        final Logger l = (Logger)o;
        final String loggerName = l.getName();
        final String levelStr = ec.subst(attributes.getValue("value"));
        if ("INHERITED".equalsIgnoreCase(levelStr) || "NULL".equalsIgnoreCase(levelStr)) {
            l.setLevel(null);
        }
        else {
            l.setLevel(Level.toLevel(levelStr, Level.DEBUG));
        }
        this.addInfo(String.valueOf(loggerName) + " level set to " + l.getLevel());
    }
    
    public void finish(final InterpretationContext ec) {
    }
    
    @Override
    public void end(final InterpretationContext ec, final String e) {
    }
}
