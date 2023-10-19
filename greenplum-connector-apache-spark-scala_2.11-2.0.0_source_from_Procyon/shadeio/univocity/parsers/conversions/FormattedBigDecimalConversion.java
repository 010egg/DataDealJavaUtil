// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.text.DecimalFormat;
import java.math.BigDecimal;

public class FormattedBigDecimalConversion extends NumericConversion<BigDecimal>
{
    public FormattedBigDecimalConversion(final BigDecimal valueIfStringIsNull, final String valueIfObjectIsNull, final String... numericFormats) {
        super(valueIfStringIsNull, valueIfObjectIsNull, numericFormats);
    }
    
    public FormattedBigDecimalConversion(final BigDecimal valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
    }
    
    public FormattedBigDecimalConversion(final String... numericFormats) {
        super((Number)null, null, numericFormats);
    }
    
    public FormattedBigDecimalConversion(final DecimalFormat... numericFormatters) {
        super(numericFormatters);
    }
    
    public FormattedBigDecimalConversion() {
    }
    
    @Override
    protected void configureFormatter(final DecimalFormat formatter) {
        formatter.setParseBigDecimal(true);
    }
}
