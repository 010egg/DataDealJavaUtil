// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.processor.core.Processor;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.CompositeProcessor;

public class CompositeRowProcessor extends CompositeProcessor<ParsingContext> implements RowProcessor
{
    public CompositeRowProcessor(final Processor... processors) {
        super((Processor[])processors);
    }
}
