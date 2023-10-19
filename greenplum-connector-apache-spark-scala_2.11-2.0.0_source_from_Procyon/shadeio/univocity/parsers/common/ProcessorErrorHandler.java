// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public interface ProcessorErrorHandler<T extends Context>
{
    void handleError(final DataProcessingException p0, final Object[] p1, final T p2);
}
