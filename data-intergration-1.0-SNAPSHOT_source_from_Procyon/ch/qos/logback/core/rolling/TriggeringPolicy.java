// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.rolling;

import java.io.File;
import ch.qos.logback.core.spi.LifeCycle;

public interface TriggeringPolicy<E> extends LifeCycle
{
    boolean isTriggeringEvent(final File p0, final E p1);
}
