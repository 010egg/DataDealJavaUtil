// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import shadeio.univocity.parsers.common.Context;

public class CompositeProcessor<C extends Context> implements Processor<C>
{
    private final Processor[] processors;
    
    public CompositeProcessor(final Processor... processors) {
        this.processors = processors;
    }
    
    @Override
    public void processStarted(final C context) {
        for (int i = 0; i < this.processors.length; ++i) {
            this.processors[i].processStarted(context);
        }
    }
    
    @Override
    public void rowProcessed(final String[] row, final C context) {
        for (int i = 0; i < this.processors.length; ++i) {
            this.processors[i].rowProcessed(row, context);
        }
    }
    
    @Override
    public void processEnded(final C context) {
        for (int i = 0; i < this.processors.length; ++i) {
            this.processors[i].processEnded(context);
        }
    }
}
