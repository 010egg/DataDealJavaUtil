// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

public final class NoopProcessor extends AbstractProcessor
{
    public static final Processor instance;
    
    private NoopProcessor() {
    }
    
    static {
        instance = new NoopProcessor();
    }
}
