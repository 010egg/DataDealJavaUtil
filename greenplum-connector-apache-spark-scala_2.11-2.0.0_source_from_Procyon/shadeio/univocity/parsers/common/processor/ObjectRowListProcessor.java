// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractObjectListProcessor;

public class ObjectRowListProcessor extends AbstractObjectListProcessor<ParsingContext> implements RowProcessor
{
    public ObjectRowListProcessor() {
    }
    
    public ObjectRowListProcessor(final int expectedRowCount) {
        super(expectedRowCount);
    }
}
