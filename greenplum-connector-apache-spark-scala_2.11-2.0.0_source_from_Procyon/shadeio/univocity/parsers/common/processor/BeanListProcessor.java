// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractBeanListProcessor;

public class BeanListProcessor<T> extends AbstractBeanListProcessor<T, ParsingContext> implements RowProcessor
{
    public BeanListProcessor(final Class<T> beanType) {
        super(beanType);
    }
    
    public BeanListProcessor(final Class<T> beanType, final int expectedBeanCount) {
        super(beanType, expectedBeanCount);
    }
}
