// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import shadeio.univocity.parsers.common.DefaultConversionProcessor;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractObjectProcessor<T extends Context> extends DefaultConversionProcessor implements Processor<T>
{
    @Override
    public void rowProcessed(final String[] row, final T context) {
        final Object[] objectRow = this.applyConversions(row, context);
        if (objectRow != null) {
            this.rowProcessed(objectRow, context);
        }
    }
    
    public abstract void rowProcessed(final Object[] p0, final T p1);
    
    @Override
    public void processStarted(final T context) {
    }
    
    @Override
    public void processEnded(final T context) {
    }
}
