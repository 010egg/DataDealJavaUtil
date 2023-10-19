// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

import org.xml.sax.Locator;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareImpl;

class CAI_WithLocatorSupport extends ContextAwareImpl
{
    CAI_WithLocatorSupport(final Context context, final Interpreter interpreter) {
        super(context, interpreter);
    }
    
    @Override
    protected Object getOrigin() {
        final Interpreter i = (Interpreter)super.getOrigin();
        final Locator locator = i.locator;
        if (locator != null) {
            return Interpreter.class.getName() + "@" + locator.getLineNumber() + ":" + locator.getColumnNumber();
        }
        return Interpreter.class.getName() + "@NA:NA";
    }
}
