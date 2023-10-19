// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Arrays;
import java.text.ParseException;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion extends ObjectConversion<Date> implements FormattedConversion<SimpleDateFormat>
{
    private final Locale locale;
    private final SimpleDateFormat[] parsers;
    private final String[] formats;
    
    public DateConversion(final Locale locale, final Date valueIfStringIsNull, final String valueIfObjectIsNull, final String... dateFormats) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        ArgumentUtils.noNulls("Date formats", dateFormats);
        this.locale = ((locale == null) ? Locale.getDefault() : locale);
        this.formats = dateFormats.clone();
        this.parsers = new SimpleDateFormat[dateFormats.length];
        for (int i = 0; i < dateFormats.length; ++i) {
            final String dateFormat = dateFormats[i];
            this.parsers[i] = new SimpleDateFormat(dateFormat, this.locale);
        }
    }
    
    public DateConversion(final Date valueIfStringIsNull, final String valueIfObjectIsNull, final String... dateFormats) {
        this(Locale.getDefault(), valueIfStringIsNull, valueIfObjectIsNull, dateFormats);
    }
    
    public DateConversion(final Locale locale, final String... dateFormats) {
        this(locale, (Date)null, null, dateFormats);
    }
    
    public DateConversion(final String... dateFormats) {
        this(Locale.getDefault(), (Date)null, null, dateFormats);
    }
    
    @Override
    public String revert(final Date input) {
        if (input == null) {
            return super.revert((Date)null);
        }
        return this.parsers[0].format(input);
    }
    
    @Override
    protected Date fromString(final String input) {
        final SimpleDateFormat[] arr$ = this.parsers;
        final int len$ = arr$.length;
        int i$ = 0;
        while (i$ < len$) {
            final SimpleDateFormat formatter = arr$[i$];
            try {
                synchronized (formatter) {
                    return formatter.parse(input);
                }
            }
            catch (ParseException ex) {
                ++i$;
                continue;
            }
            break;
        }
        final DataProcessingException exception = new DataProcessingException("Cannot parse '{value}' as a valid date of locale '" + this.locale + "'. Supported formats are: " + Arrays.toString(this.formats));
        exception.setValue(input);
        throw exception;
    }
    
    @Override
    public SimpleDateFormat[] getFormatterObjects() {
        return this.parsers;
    }
}
