// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

final class NoopRowProcessorErrorHandler implements RowProcessorErrorHandler
{
    public static final RowProcessorErrorHandler instance;
    
    private NoopRowProcessorErrorHandler() {
    }
    
    @Override
    public void handleError(final DataProcessingException error, final Object[] inputRow, final ParsingContext context) {
        throw error;
    }
    
    static {
        instance = new NoopRowProcessorErrorHandler();
    }
}
