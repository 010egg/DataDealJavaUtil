// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Conversions
{
    private static final UpperCaseConversion upperCase;
    private static final LowerCaseConversion lowerCase;
    private static final TrimConversion trim;
    private static final ToStringConversion toString;
    
    private Conversions() {
    }
    
    public static ToStringConversion string() {
        return Conversions.toString;
    }
    
    public static UpperCaseConversion toUpperCase() {
        return Conversions.upperCase;
    }
    
    public static LowerCaseConversion toLowerCase() {
        return Conversions.lowerCase;
    }
    
    public static TrimConversion trim() {
        return Conversions.trim;
    }
    
    public static TrimConversion trim(final int length) {
        return new TrimConversion(length);
    }
    
    public static RegexConversion replace(final String replaceRegex, final String replacement) {
        return new RegexConversion(replaceRegex, replacement);
    }
    
    public static NullStringConversion toNull(final String... nullRepresentations) {
        return new NullStringConversion(nullRepresentations);
    }
    
    public static DateConversion toDate(final Locale locale, final String... dateFormats) {
        return new DateConversion(locale, dateFormats);
    }
    
    public static DateConversion toDate(final String... dateFormats) {
        return new DateConversion(Locale.getDefault(), dateFormats);
    }
    
    public static DateConversion toDate(final Locale locale, final Date dateIfNull, final String... dateFormats) {
        return new DateConversion(locale, dateIfNull, null, dateFormats);
    }
    
    public static DateConversion toDate(final Date dateIfNull, final String... dateFormats) {
        return new DateConversion(Locale.getDefault(), dateIfNull, null, dateFormats);
    }
    
    public static DateConversion toDate(final Locale locale, final Date dateIfNull, final String stringIfNull, final String... dateFormats) {
        return new DateConversion(locale, dateIfNull, stringIfNull, dateFormats);
    }
    
    public static DateConversion toDate(final Date dateIfNull, final String stringIfNull, final String... dateFormats) {
        return new DateConversion(Locale.getDefault(), dateIfNull, stringIfNull, dateFormats);
    }
    
    public static CalendarConversion toCalendar(final Locale locale, final String... dateFormats) {
        return new CalendarConversion(locale, dateFormats);
    }
    
    public static CalendarConversion toCalendar(final String... dateFormats) {
        return new CalendarConversion(Locale.getDefault(), dateFormats);
    }
    
    public static CalendarConversion toCalendar(final Locale locale, final Calendar dateIfNull, final String... dateFormats) {
        return new CalendarConversion(locale, dateIfNull, null, dateFormats);
    }
    
    public static CalendarConversion toCalendar(final Calendar dateIfNull, final String... dateFormats) {
        return new CalendarConversion(Locale.getDefault(), dateIfNull, null, dateFormats);
    }
    
    public static CalendarConversion toCalendar(final Locale locale, final Calendar dateIfNull, final String stringIfNull, final String... dateFormats) {
        return new CalendarConversion(locale, dateIfNull, stringIfNull, dateFormats);
    }
    
    public static CalendarConversion toCalendar(final Calendar dateIfNull, final String stringIfNull, final String... dateFormats) {
        return new CalendarConversion(Locale.getDefault(), dateIfNull, stringIfNull, dateFormats);
    }
    
    public static ByteConversion toByte() {
        return new ByteConversion();
    }
    
    public static ShortConversion toShort() {
        return new ShortConversion();
    }
    
    public static IntegerConversion toInteger() {
        return new IntegerConversion();
    }
    
    public static LongConversion toLong() {
        return new LongConversion();
    }
    
    public static BigIntegerConversion toBigInteger() {
        return new BigIntegerConversion();
    }
    
    public static FloatConversion toFloat() {
        return new FloatConversion();
    }
    
    public static DoubleConversion toDouble() {
        return new DoubleConversion();
    }
    
    public static BigDecimalConversion toBigDecimal() {
        return new BigDecimalConversion();
    }
    
    public static NumericConversion<Number> formatToNumber(final String... numberFormats) {
        return new NumericConversion<Number>(numberFormats) {
            @Override
            protected void configureFormatter(final DecimalFormat formatter) {
            }
        };
    }
    
    public static <T extends Number> NumericConversion<T> formatToNumber(final Class<T> numberType, final String... numberFormats) {
        return new NumericConversion<T>(numberFormats) {
            @Override
            protected void configureFormatter(final DecimalFormat formatter) {
            }
        };
    }
    
    public static FormattedBigDecimalConversion formatToBigDecimal(final String... numberFormats) {
        return new FormattedBigDecimalConversion(numberFormats);
    }
    
    public static FormattedBigDecimalConversion formatToBigDecimal(final BigDecimal defaultValueForNullString, final String... numberFormats) {
        return new FormattedBigDecimalConversion(defaultValueForNullString, null, numberFormats);
    }
    
    public static FormattedBigDecimalConversion formatToBigDecimal(final BigDecimal defaultValueForNullString, final String stringIfNull, final String... numberFormats) {
        return new FormattedBigDecimalConversion(defaultValueForNullString, stringIfNull, numberFormats);
    }
    
    public static BooleanConversion toBoolean(final Boolean defaultValueForNullString, final String defaultValueForNullBoolean, final String[] valuesForTrue, final String[] valuesForFalse) {
        return new BooleanConversion(defaultValueForNullString, defaultValueForNullBoolean, valuesForTrue, valuesForFalse);
    }
    
    public static BooleanConversion toBoolean(final Boolean defaultValueForNullString, final String defaultValueForNullBoolean, final String valueForTrue, final String valueForFalse) {
        return new BooleanConversion(defaultValueForNullString, defaultValueForNullBoolean, new String[] { valueForTrue }, new String[] { valueForFalse });
    }
    
    public static BooleanConversion toBoolean(final String[] valuesForTrue, final String[] valuesForFalse) {
        return new BooleanConversion(valuesForTrue, valuesForFalse);
    }
    
    public static BooleanConversion toBoolean() {
        return toBoolean("true", "false");
    }
    
    public static BooleanConversion toBoolean(final String valueForTrue, final String valueForFalse) {
        return new BooleanConversion(new String[] { valueForTrue }, new String[] { valueForFalse });
    }
    
    public static CharacterConversion toChar() {
        return new CharacterConversion();
    }
    
    public static CharacterConversion toChar(final Character defaultValueForNullString, final String defaultValueForNullChar) {
        return new CharacterConversion(defaultValueForNullString, defaultValueForNullChar);
    }
    
    public static CharacterConversion toChar(final Character defaultValueForNullString) {
        return new CharacterConversion(defaultValueForNullString, null);
    }
    
    public static <T extends Enum<T>> EnumConversion<T> toEnum(final Class<T> enumType) {
        return new EnumConversion<T>(enumType);
    }
    
    public static <T extends Enum<T>> EnumConversion<T> toEnum(final Class<T> enumType, final EnumSelector... selectors) {
        return toEnum(enumType, (T)null, null, null, selectors);
    }
    
    public static <T extends Enum<T>> EnumConversion<T> toEnum(final Class<T> enumType, final String customEnumElement, final EnumSelector... selectors) {
        return toEnum(enumType, (T)null, null, customEnumElement, new EnumSelector[0]);
    }
    
    public static <T extends Enum<T>> EnumConversion<T> toEnum(final Class<T> enumType, final T valueIfStringIsNull, final String valueIfEnumIsNull, final String customEnumElement, final EnumSelector... selectors) {
        return new EnumConversion<T>(enumType, valueIfStringIsNull, valueIfEnumIsNull, customEnumElement, selectors);
    }
    
    public static FormattedDateConversion toFormattedDate(final String pattern) {
        return toFormattedDate(pattern, null, null);
    }
    
    public static FormattedDateConversion toFormattedDate(final String pattern, final String valueIfObjectIsNull) {
        return toFormattedDate(pattern, null, valueIfObjectIsNull);
    }
    
    public static FormattedDateConversion toFormattedDate(final String pattern, final Locale locale) {
        return toFormattedDate(pattern, locale, null);
    }
    
    public static FormattedDateConversion toFormattedDate(final String pattern, final Locale locale, final String valueIfObjectIsNull) {
        return new FormattedDateConversion(pattern, locale, valueIfObjectIsNull);
    }
    
    public static ValidatedConversion notNull() {
        return validate(false, true, null, null);
    }
    
    public static ValidatedConversion notBlank() {
        return validate(false, false, null, null);
    }
    
    public static ValidatedConversion notBlank(final String regexToMatch) {
        return validate(false, false, null, null, regexToMatch);
    }
    
    public static ValidatedConversion validate(final boolean nullable, final boolean allowBlanks) {
        return new ValidatedConversion(nullable, allowBlanks, null, null, null);
    }
    
    public static ValidatedConversion validate(final boolean nullable, final boolean allowBlanks, final String[] oneOf, final String[] noneOf) {
        return new ValidatedConversion(nullable, allowBlanks, oneOf, noneOf, null);
    }
    
    public static ValidatedConversion validate(final boolean nullable, final boolean allowBlanks, final String regexToMatch) {
        return new ValidatedConversion(nullable, allowBlanks, null, null, regexToMatch);
    }
    
    public static ValidatedConversion validate(final boolean nullable, final boolean allowBlanks, final String[] oneOf, final String[] noneOf, final String regexToMatch) {
        return new ValidatedConversion(nullable, allowBlanks, oneOf, noneOf, regexToMatch);
    }
    
    public static ValidatedConversion oneOf(final String... oneOf) {
        return new ValidatedConversion(false, false, oneOf, null, null);
    }
    
    public static ValidatedConversion noneOf(final String... noneOf) {
        return new ValidatedConversion(false, false, null, noneOf, null);
    }
    
    static {
        upperCase = new UpperCaseConversion();
        lowerCase = new LowerCaseConversion();
        trim = new TrimConversion();
        toString = new ToStringConversion();
    }
}
