// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractBatchedObjectColumnProcessor;

public abstract class BatchedObjectColumnProcessor extends AbstractBatchedObjectColumnProcessor<ParsingContext> implements RowProcessor
{
    public BatchedObjectColumnProcessor(final int rowsPerBatch) {
        super(rowsPerBatch);
    }
}
