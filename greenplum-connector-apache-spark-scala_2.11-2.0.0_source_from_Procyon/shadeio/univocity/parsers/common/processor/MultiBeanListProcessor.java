// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractMultiBeanListProcessor;

public class MultiBeanListProcessor extends AbstractMultiBeanListProcessor<ParsingContext> implements RowProcessor
{
    public MultiBeanListProcessor(final int expectedBeanCount, final Class... beanTypes) {
        super(expectedBeanCount, (Class[])beanTypes);
    }
    
    public MultiBeanListProcessor(final Class... beanTypes) {
        super((Class[])beanTypes);
    }
}
