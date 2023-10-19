// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractMultiBeanProcessor;

public abstract class MultiBeanProcessor extends AbstractMultiBeanProcessor<ParsingContext> implements RowProcessor
{
    public MultiBeanProcessor(final Class... beanTypes) {
        super((Class[])beanTypes);
    }
}
