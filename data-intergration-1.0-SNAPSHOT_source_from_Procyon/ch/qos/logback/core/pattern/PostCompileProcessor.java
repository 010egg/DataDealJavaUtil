// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;

public interface PostCompileProcessor<E>
{
    void process(final Context p0, final Converter<E> p1);
}
