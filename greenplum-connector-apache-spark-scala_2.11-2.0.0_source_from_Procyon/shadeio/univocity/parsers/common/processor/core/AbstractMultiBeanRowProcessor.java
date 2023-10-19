// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Map;
import java.util.HashMap;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractMultiBeanRowProcessor<C extends Context> extends AbstractMultiBeanProcessor<C>
{
    private final HashMap<Class<?>, Object> row;
    private long record;
    
    public AbstractMultiBeanRowProcessor(final Class... beanTypes) {
        super((Class[])beanTypes);
        this.row = new HashMap<Class<?>, Object>();
        this.record = -1L;
    }
    
    @Override
    public void processStarted(final C context) {
        this.record = -1L;
        this.row.clear();
        super.processStarted(context);
    }
    
    @Override
    public final void beanProcessed(final Class<?> beanType, final Object beanInstance, final C context) {
        if (this.record != context.currentRecord() && this.record != -1L) {
            this.submitRow(context);
        }
        this.record = context.currentRecord();
        this.row.put(beanType, beanInstance);
    }
    
    private void submitRow(final C context) {
        if (!this.row.isEmpty()) {
            this.rowProcessed(this.row, context);
            this.row.clear();
        }
    }
    
    @Override
    public void processEnded(final C context) {
        this.submitRow(context);
        super.processEnded(context);
    }
    
    protected abstract void rowProcessed(final Map<Class<?>, Object> p0, final C p1);
}
