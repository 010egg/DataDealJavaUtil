// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.Context;
import shadeio.univocity.parsers.common.ParsingContextWrapper;
import shadeio.univocity.parsers.common.ParsingContextSnapshot;
import shadeio.univocity.parsers.common.processor.core.Processor;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractConcurrentProcessor;

public class ConcurrentRowProcessor extends AbstractConcurrentProcessor<ParsingContext> implements RowProcessor
{
    public ConcurrentRowProcessor(final RowProcessor rowProcessor) {
        super(rowProcessor);
    }
    
    public ConcurrentRowProcessor(final RowProcessor rowProcessor, final int limit) {
        super(rowProcessor, limit);
    }
    
    @Override
    protected ParsingContext copyContext(final ParsingContext context) {
        return new ParsingContextSnapshot(context);
    }
    
    @Override
    protected ParsingContext wrapContext(final ParsingContext context) {
        return new ParsingContextWrapper(context) {
            @Override
            public long currentRecord() {
                return AbstractConcurrentProcessor.this.getRowCount();
            }
        };
    }
}
