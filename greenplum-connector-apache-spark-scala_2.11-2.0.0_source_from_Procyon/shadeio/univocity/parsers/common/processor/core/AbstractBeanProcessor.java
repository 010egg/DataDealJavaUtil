// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractBeanProcessor<T, C extends Context> extends BeanConversionProcessor<T> implements Processor<C>
{
    public AbstractBeanProcessor(final Class<T> beanType, final MethodFilter methodFilter) {
        super(beanType, methodFilter);
    }
    
    @Override
    public final void rowProcessed(final String[] row, final C context) {
        final T instance = this.createBean(row, context);
        if (instance != null) {
            this.beanProcessed(instance, context);
        }
    }
    
    public abstract void beanProcessed(final T p0, final C p1);
    
    @Override
    public void processStarted(final C context) {
        super.initialize(context.headers());
    }
    
    @Override
    public void processEnded(final C context) {
    }
}
