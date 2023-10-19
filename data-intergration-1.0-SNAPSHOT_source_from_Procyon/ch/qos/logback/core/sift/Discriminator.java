// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.sift;

import ch.qos.logback.core.spi.LifeCycle;

public interface Discriminator<E> extends LifeCycle
{
    String getDiscriminatingValue(final E p0);
    
    String getKey();
}
