// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

public class Csv
{
    public static CsvParserSettings parseExcel() {
        final CsvParserSettings settings = new CsvParserSettings();
        settings.getFormat().setLineSeparator("\r\n");
        settings.getFormat().setComment('\0');
        settings.setParseUnescapedQuotes(false);
        settings.setSkipEmptyLines(false);
        settings.trimValues(false);
        return settings;
    }
    
    public static CsvParserSettings parseRfc4180() {
        final CsvParserSettings settings = parseExcel();
        settings.setNormalizeLineEndingsWithinQuotes(false);
        return settings;
    }
    
    public static CsvWriterSettings writeExcel() {
        final CsvWriterSettings settings = new CsvWriterSettings();
        settings.getFormat().setLineSeparator("\r\n");
        settings.getFormat().setComment('\0');
        settings.setEmptyValue(null);
        settings.setSkipEmptyLines(false);
        settings.trimValues(false);
        return settings;
    }
    
    public static CsvWriterSettings writeRfc4180() {
        final CsvWriterSettings settings = writeExcel();
        settings.setNormalizeLineEndingsWithinQuotes(false);
        settings.setQuoteEscapingEnabled(true);
        return settings;
    }
}
