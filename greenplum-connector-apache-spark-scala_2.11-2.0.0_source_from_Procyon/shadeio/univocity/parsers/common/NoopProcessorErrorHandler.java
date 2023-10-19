// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

public final class NoopProcessorErrorHandler<T extends Context> implements ProcessorErrorHandler<T>
{
    public static final ProcessorErrorHandler instance;
    
    private NoopProcessorErrorHandler() {
    }
    
    @Override
    public void handleError(final DataProcessingException error, final Object[] inputRow, final T context) {
        throw error;
    }
    
    static {
        instance = new NoopProcessorErrorHandler();
    }
}
