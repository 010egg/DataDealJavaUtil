// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractObjectColumnProcessor;

public class ObjectColumnProcessor extends AbstractObjectColumnProcessor<ParsingContext> implements RowProcessor
{
    public ObjectColumnProcessor() {
        this(1000);
    }
    
    public ObjectColumnProcessor(final int expectedRowCount) {
        super(expectedRowCount);
    }
}
