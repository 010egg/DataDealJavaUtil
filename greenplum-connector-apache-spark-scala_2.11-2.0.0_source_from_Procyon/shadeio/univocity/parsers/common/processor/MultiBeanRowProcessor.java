// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractMultiBeanRowProcessor;

public abstract class MultiBeanRowProcessor extends AbstractMultiBeanRowProcessor<ParsingContext> implements RowProcessor
{
    public MultiBeanRowProcessor(final Class... beanTypes) {
        super((Class[])beanTypes);
    }
}
