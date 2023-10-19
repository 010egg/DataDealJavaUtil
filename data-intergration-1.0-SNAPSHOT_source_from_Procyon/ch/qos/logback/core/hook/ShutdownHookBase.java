// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.hook;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class ShutdownHookBase extends ContextAwareBase implements ShutdownHook
{
    protected void stop() {
        this.addInfo("Logback context being closed via shutdown hook");
        final Context hookContext = this.getContext();
        if (hookContext instanceof ContextBase) {
            final ContextBase context = (ContextBase)hookContext;
            context.stop();
        }
    }
}
