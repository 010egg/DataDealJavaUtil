// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractListProcessor;

public class RowListProcessor extends AbstractListProcessor<ParsingContext> implements RowProcessor
{
    public RowListProcessor() {
    }
    
    public RowListProcessor(final int expectedRowCount) {
        super(expectedRowCount);
    }
}
