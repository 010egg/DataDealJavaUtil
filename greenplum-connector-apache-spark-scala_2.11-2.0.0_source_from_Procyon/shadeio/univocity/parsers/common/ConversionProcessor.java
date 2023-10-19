// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.fields.FieldSet;
import shadeio.univocity.parsers.conversions.Conversion;

public interface ConversionProcessor
{
    FieldSet<Integer> convertIndexes(final Conversion... p0);
    
    void convertAll(final Conversion... p0);
    
    FieldSet<String> convertFields(final Conversion... p0);
    
    void convertType(final Class<?> p0, final Conversion... p1);
}
