// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import shadeio.univocity.parsers.common.processor.core.BeanConversionProcessor;

public class BeanWriterProcessor<T> extends BeanConversionProcessor<T> implements RowWriterProcessor<T>
{
    public BeanWriterProcessor(final Class<T> beanType) {
        super(beanType, MethodFilter.ONLY_GETTERS);
    }
    
    @Override
    public Object[] write(final T input, final String[] headers, final int[] indexesToWrite) {
        if (!this.initialized) {
            super.initialize(headers);
        }
        return this.reverseConversions(input, headers, indexesToWrite);
    }
}
