// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Arrays;
import java.math.BigInteger;
import java.math.BigDecimal;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.text.ParsePosition;
import java.text.DecimalFormat;

public abstract class NumericConversion<T extends Number> extends ObjectConversion<T> implements FormattedConversion<DecimalFormat>
{
    private DecimalFormat[] formatters;
    private String[] formats;
    private final ParsePosition position;
    private Class<? extends Number> numberType;
    
    public NumericConversion(final T valueIfStringIsNull, final String valueIfObjectIsNull, final String... numericFormats) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        this.formatters = new DecimalFormat[0];
        this.formats = new String[0];
        this.position = new ParsePosition(0);
        this.numberType = Number.class;
        ArgumentUtils.noNulls("Numeric formats", numericFormats);
        this.formats = numericFormats.clone();
        this.formatters = new DecimalFormat[numericFormats.length];
        for (int i = 0; i < numericFormats.length; ++i) {
            final String numericFormat = numericFormats[i];
            this.configureFormatter(this.formatters[i] = new DecimalFormat(numericFormat));
        }
    }
    
    public NumericConversion(final T valueIfStringIsNull, final String valueIfObjectIsNull, final DecimalFormat... numericFormatters) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        this.formatters = new DecimalFormat[0];
        this.formats = new String[0];
        this.position = new ParsePosition(0);
        this.numberType = Number.class;
        ArgumentUtils.noNulls("Numeric formatters", numericFormatters);
        this.formatters = numericFormatters.clone();
        this.formats = new String[numericFormatters.length];
        for (int i = 0; i < numericFormatters.length; ++i) {
            this.formats[i] = numericFormatters[i].toPattern();
        }
    }
    
    public NumericConversion(final T valueIfStringIsNull, final String valueIfObjectIsNull) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        this.formatters = new DecimalFormat[0];
        this.formats = new String[0];
        this.position = new ParsePosition(0);
        this.numberType = Number.class;
    }
    
    public NumericConversion(final String... numericFormats) {
        this((Number)null, null, numericFormats);
    }
    
    public NumericConversion(final DecimalFormat... numericFormatters) {
        this((Number)null, null, numericFormatters);
    }
    
    public NumericConversion() {
        this.formatters = new DecimalFormat[0];
        this.formats = new String[0];
        this.position = new ParsePosition(0);
        this.numberType = Number.class;
    }
    
    public Class<? extends Number> getNumberType() {
        return this.numberType;
    }
    
    public void setNumberType(final Class<? extends Number> numberType) {
        this.numberType = numberType;
    }
    
    @Override
    public DecimalFormat[] getFormatterObjects() {
        return this.formatters;
    }
    
    protected abstract void configureFormatter(final DecimalFormat p0);
    
    @Override
    protected T fromString(final String input) {
        int i = 0;
        while (i < this.formatters.length) {
            this.position.setIndex(0);
            final T out = (T)this.formatters[i].parse(input, this.position);
            if (this.formatters.length == 1 || this.position.getIndex() == input.length()) {
                if (out == null || this.numberType == Number.class) {
                    return out;
                }
                if (this.numberType == Double.class) {
                    return (T)Double.valueOf(out.doubleValue());
                }
                if (this.numberType == Float.class) {
                    return (T)Float.valueOf(out.floatValue());
                }
                if (this.numberType == BigDecimal.class) {
                    if (out instanceof BigDecimal) {
                        return out;
                    }
                    return (T)new BigDecimal(String.valueOf(out));
                }
                else if (this.numberType == BigInteger.class) {
                    if (out instanceof BigInteger) {
                        return out;
                    }
                    return (T)BigInteger.valueOf(out.longValue());
                }
                else {
                    if (this.numberType == Long.class) {
                        return (T)Long.valueOf(out.longValue());
                    }
                    if (this.numberType == Integer.class) {
                        return (T)Integer.valueOf(out.intValue());
                    }
                    if (this.numberType == Short.class) {
                        return (T)Short.valueOf(out.shortValue());
                    }
                    if (this.numberType == Byte.class) {
                        return (T)Byte.valueOf(out.byteValue());
                    }
                    return out;
                }
            }
            else {
                ++i;
            }
        }
        final DataProcessingException exception = new DataProcessingException("Cannot parse '{value}' as a valid number. Supported formats are: " + Arrays.toString(this.formats));
        exception.setValue(input);
        throw exception;
    }
    
    @Override
    public String revert(final T input) {
        if (input == null) {
            return super.revert((T)null);
        }
        final DecimalFormat[] arr$ = this.formatters;
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final DecimalFormat formatter = arr$[i$];
            try {
                return formatter.format(input);
            }
            catch (Throwable ex) {
                ++i$;
                continue;
            }
            break;
        }
        final DataProcessingException exception = new DataProcessingException("Cannot format '{value}'. No valid formatters were defined.");
        exception.setValue(input);
        throw exception;
    }
    
    public void addFormat(final String format, final String... formatOptions) {
        final DecimalFormat formatter = new DecimalFormat(format);
        this.configureFormatter(formatter);
        AnnotationHelper.applyFormatSettings(formatter, formatOptions);
        this.formats = Arrays.copyOf(this.formats, this.formats.length + 1);
        this.formatters = Arrays.copyOf(this.formatters, this.formatters.length + 1);
        this.formats[this.formats.length - 1] = format;
        this.formatters[this.formatters.length - 1] = formatter;
    }
}
