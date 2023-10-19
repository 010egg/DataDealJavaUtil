// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import shadeio.univocity.parsers.common.Context;

public abstract class AbstractProcessor<T extends Context> implements Processor<T>
{
    @Override
    public void processStarted(final T context) {
    }
    
    @Override
    public void rowProcessed(final String[] row, final T context) {
    }
    
    @Override
    public void processEnded(final T context) {
    }
}
