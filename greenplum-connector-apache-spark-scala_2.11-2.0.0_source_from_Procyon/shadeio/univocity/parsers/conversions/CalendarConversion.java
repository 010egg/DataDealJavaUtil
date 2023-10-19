// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.util.Date;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalendarConversion extends ObjectConversion<Calendar> implements FormattedConversion<SimpleDateFormat>
{
    private final DateConversion dateConversion;
    
    public CalendarConversion(final Locale locale, final Calendar valueIfStringIsNull, final String valueIfObjectIsNull, final String... dateFormats) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        ArgumentUtils.noNulls("Date formats", dateFormats);
        this.dateConversion = new DateConversion(locale, dateFormats);
    }
    
    public CalendarConversion(final Calendar valueIfStringIsNull, final String valueIfObjectIsNull, final String... dateFormats) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        ArgumentUtils.noNulls("Date formats", dateFormats);
        this.dateConversion = new DateConversion(Locale.getDefault(), dateFormats);
    }
    
    public CalendarConversion(final Locale locale, final String... dateFormats) {
        this(locale, (Calendar)null, null, dateFormats);
    }
    
    public CalendarConversion(final String... dateFormats) {
        this(Locale.getDefault(), (Calendar)null, null, dateFormats);
    }
    
    @Override
    public String revert(final Calendar input) {
        if (input == null) {
            return super.revert((Calendar)null);
        }
        return this.dateConversion.revert(input.getTime());
    }
    
    @Override
    protected Calendar fromString(final String input) {
        final Date date = this.dateConversion.execute(input);
        final Calendar out = Calendar.getInstance();
        out.setTime(date);
        return out;
    }
    
    @Override
    public SimpleDateFormat[] getFormatterObjects() {
        return this.dateConversion.getFormatterObjects();
    }
}
