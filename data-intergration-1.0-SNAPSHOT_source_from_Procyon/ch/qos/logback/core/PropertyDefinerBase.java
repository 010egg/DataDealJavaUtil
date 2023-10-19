// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core;

import ch.qos.logback.core.spi.PropertyDefiner;
import ch.qos.logback.core.spi.ContextAwareBase;

public abstract class PropertyDefinerBase extends ContextAwareBase implements PropertyDefiner
{
    protected static String booleanAsStr(final boolean bool) {
        return bool ? Boolean.TRUE.toString() : Boolean.FALSE.toString();
    }
}
