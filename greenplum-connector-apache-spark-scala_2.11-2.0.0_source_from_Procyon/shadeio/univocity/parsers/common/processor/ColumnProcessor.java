// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractColumnProcessor;

public class ColumnProcessor extends AbstractColumnProcessor<ParsingContext> implements RowProcessor
{
    public ColumnProcessor() {
        super(1000);
    }
    
    public ColumnProcessor(final int expectedRowCount) {
        super(expectedRowCount);
    }
}
