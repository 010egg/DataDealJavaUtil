// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;

public class FormattedDateConversion implements Conversion<Object, String>
{
    private final SimpleDateFormat dateFormat;
    private final String valueIfObjectIsNull;
    
    public FormattedDateConversion(final String format, Locale locale, final String valueIfObjectIsNull) {
        this.valueIfObjectIsNull = valueIfObjectIsNull;
        locale = ((locale == null) ? Locale.getDefault() : locale);
        this.dateFormat = new SimpleDateFormat(format, locale);
    }
    
    @Override
    public String execute(final Object input) {
        if (input == null) {
            return this.valueIfObjectIsNull;
        }
        Date date = null;
        if (input instanceof Date) {
            date = (Date)input;
        }
        else if (input instanceof Calendar) {
            date = ((Calendar)input).getTime();
        }
        if (date != null) {
            return this.dateFormat.format(date);
        }
        final DataProcessingException exception = new DataProcessingException("Cannot format '{value}' to a date. Not an instance of java.util.Date or java.util.Calendar");
        exception.setValue(input);
        throw exception;
    }
    
    @Override
    public Object revert(final String input) {
        throw new UnsupportedOperationException("Can't convert an input string into date type");
    }
}
