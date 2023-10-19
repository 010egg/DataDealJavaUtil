// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.DefaultConversionProcessor;

public class ObjectRowWriterProcessor extends DefaultConversionProcessor implements RowWriterProcessor<Object[]>
{
    @Override
    public Object[] write(final Object[] input, final String[] headers, final int[] indexesToWrite) {
        if (input == null) {
            return null;
        }
        final Object[] output = new Object[input.length];
        System.arraycopy(input, 0, output, 0, input.length);
        if (this.reverseConversions(false, output, headers, indexesToWrite)) {
            return output;
        }
        return null;
    }
}
