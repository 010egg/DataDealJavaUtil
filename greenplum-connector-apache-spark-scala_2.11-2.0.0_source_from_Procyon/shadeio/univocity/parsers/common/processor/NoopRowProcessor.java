// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

public final class NoopRowProcessor extends AbstractRowProcessor
{
    public static final RowProcessor instance;
    
    private NoopRowProcessor() {
    }
    
    static {
        instance = new NoopRowProcessor();
    }
}
