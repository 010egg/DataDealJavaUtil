// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Iterator;
import java.util.HashMap;
import shadeio.univocity.parsers.common.processor.RowProcessor;
import java.util.Map;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractProcessorSwitch<T extends Context> implements Processor<T>, ColumnOrderDependent
{
    private Map<Processor, T> processors;
    private Processor selectedProcessor;
    private T contextForProcessor;
    
    protected abstract Processor<T> switchRowProcessor(final String[] p0, final T p1);
    
    public String[] getHeaders() {
        return null;
    }
    
    public int[] getIndexes() {
        return null;
    }
    
    public void processorSwitched(final Processor<T> from, final Processor<T> to) {
        if (from != null) {
            if (from instanceof RowProcessor && (to == null || to instanceof RowProcessor)) {
                this.rowProcessorSwitched((RowProcessor)from, (RowProcessor)to);
            }
        }
        else if (to != null && to instanceof RowProcessor) {
            this.rowProcessorSwitched((RowProcessor)from, (RowProcessor)to);
        }
    }
    
    public void rowProcessorSwitched(final RowProcessor from, final RowProcessor to) {
    }
    
    @Override
    public void processStarted(final T context) {
        this.processors = new HashMap<Processor, T>();
        this.selectedProcessor = NoopProcessor.instance;
    }
    
    protected abstract T wrapContext(final T p0);
    
    @Override
    public final void rowProcessed(String[] row, final T context) {
        Processor processor = this.switchRowProcessor(row, context);
        if (processor == null) {
            processor = NoopProcessor.instance;
        }
        if (processor != this.selectedProcessor) {
            this.contextForProcessor = this.processors.get(processor);
            if (processor != NoopProcessor.instance) {
                if (this.contextForProcessor == null) {
                    processor.processStarted(this.contextForProcessor = this.wrapContext(context));
                    this.processors.put(processor, this.contextForProcessor);
                }
                this.processorSwitched(this.selectedProcessor, processor);
                this.selectedProcessor = processor;
                if (this.getIndexes() != null) {
                    final int[] indexes = this.getIndexes();
                    final String[] tmp = new String[indexes.length];
                    for (int i = 0; i < indexes.length; ++i) {
                        final int index = indexes[i];
                        if (index < row.length) {
                            tmp[i] = row[index];
                        }
                    }
                    row = tmp;
                }
                this.selectedProcessor.rowProcessed(row, this.contextForProcessor);
            }
        }
        else {
            this.selectedProcessor.rowProcessed(row, this.contextForProcessor);
        }
    }
    
    @Override
    public void processEnded(final T context) {
        this.processorSwitched(this.selectedProcessor, null);
        this.selectedProcessor = NoopProcessor.instance;
        for (final Map.Entry<Processor, T> e : this.processors.entrySet()) {
            e.getKey().processEnded(e.getValue());
        }
    }
    
    @Override
    public boolean preventColumnReordering() {
        return true;
    }
}
