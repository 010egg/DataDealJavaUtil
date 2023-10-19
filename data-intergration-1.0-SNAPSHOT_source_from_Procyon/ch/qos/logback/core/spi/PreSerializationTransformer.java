// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.spi;

import java.io.Serializable;

public interface PreSerializationTransformer<E>
{
    Serializable transform(final E p0);
}
