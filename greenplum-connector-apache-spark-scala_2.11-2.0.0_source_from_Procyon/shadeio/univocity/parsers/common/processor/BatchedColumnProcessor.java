// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractBatchedColumnProcessor;

public abstract class BatchedColumnProcessor extends AbstractBatchedColumnProcessor<ParsingContext> implements RowProcessor
{
    public BatchedColumnProcessor(final int rowsPerBatch) {
        super(rowsPerBatch);
    }
}
