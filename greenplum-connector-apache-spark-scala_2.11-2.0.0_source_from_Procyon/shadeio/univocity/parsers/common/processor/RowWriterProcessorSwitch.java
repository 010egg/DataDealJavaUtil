// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Arrays;
import java.util.Map;

public abstract class RowWriterProcessorSwitch implements RowWriterProcessor<Object>
{
    private RowWriterProcessor selectedRowWriterProcessor;
    private int minimumRowLength;
    
    public RowWriterProcessorSwitch() {
        this.selectedRowWriterProcessor = null;
        this.minimumRowLength = Integer.MIN_VALUE;
    }
    
    protected abstract RowWriterProcessor<?> switchRowProcessor(final Object p0);
    
    protected String[] getHeaders() {
        return null;
    }
    
    protected int[] getIndexes() {
        return null;
    }
    
    public void rowProcessorSwitched(final RowWriterProcessor<?> from, final RowWriterProcessor<?> to) {
    }
    
    public abstract String[] getHeaders(final Map p0, final Map p1);
    
    public abstract String[] getHeaders(final Object p0);
    
    protected abstract String describeSwitch();
    
    public final int getMinimumRowLength() {
        if (this.minimumRowLength == Integer.MIN_VALUE) {
            this.minimumRowLength = 0;
            if (this.getHeaders() != null) {
                this.minimumRowLength = this.getHeaders().length;
            }
            if (this.getIndexes() != null) {
                for (final int index : this.getIndexes()) {
                    if (index + 1 > this.minimumRowLength) {
                        this.minimumRowLength = index + 1;
                    }
                }
            }
        }
        return this.minimumRowLength;
    }
    
    @Override
    public Object[] write(final Object input, final String[] headers, final int[] indicesToWrite) {
        final RowWriterProcessor<?> processor = this.switchRowProcessor(input);
        if (processor == null) {
            final DataProcessingException ex = new DataProcessingException("Cannot find switch for input. Headers: {headers}, indices to write: " + Arrays.toString(indicesToWrite) + ". " + this.describeSwitch());
            ex.setValue("headers", Arrays.toString(headers));
            ex.setValue(input);
            throw ex;
        }
        if (processor != this.selectedRowWriterProcessor) {
            this.rowProcessorSwitched(this.selectedRowWriterProcessor, processor);
            this.selectedRowWriterProcessor = processor;
        }
        String[] headersToUse = this.getHeaders();
        int[] indexesToUse = this.getIndexes();
        headersToUse = ((headersToUse == null) ? headers : headersToUse);
        indexesToUse = ((indexesToUse == null) ? indicesToWrite : indexesToUse);
        return this.selectedRowWriterProcessor.write(input, headersToUse, indexesToUse);
    }
}
