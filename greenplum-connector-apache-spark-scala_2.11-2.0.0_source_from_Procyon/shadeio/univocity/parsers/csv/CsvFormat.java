// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import java.util.TreeMap;
import shadeio.univocity.parsers.common.Format;

public class CsvFormat extends Format
{
    private char quote;
    private char quoteEscape;
    private char delimiter;
    private Character charToEscapeQuoteEscaping;
    
    public CsvFormat() {
        this.quote = '\"';
        this.quoteEscape = '\"';
        this.delimiter = ',';
        this.charToEscapeQuoteEscaping = null;
    }
    
    public char getQuote() {
        return this.quote;
    }
    
    public void setQuote(final char quote) {
        this.quote = quote;
    }
    
    public boolean isQuote(final char ch) {
        return this.quote == ch;
    }
    
    public char getQuoteEscape() {
        return this.quoteEscape;
    }
    
    public void setQuoteEscape(final char quoteEscape) {
        this.quoteEscape = quoteEscape;
    }
    
    public boolean isQuoteEscape(final char ch) {
        return this.quoteEscape == ch;
    }
    
    public char getDelimiter() {
        return this.delimiter;
    }
    
    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }
    
    public boolean isDelimiter(final char ch) {
        return this.delimiter == ch;
    }
    
    public final char getCharToEscapeQuoteEscaping() {
        if (this.charToEscapeQuoteEscaping != null) {
            return this.charToEscapeQuoteEscaping;
        }
        if (this.quote == this.quoteEscape) {
            return '\0';
        }
        return this.quoteEscape;
    }
    
    public final void setCharToEscapeQuoteEscaping(final char charToEscapeQuoteEscaping) {
        this.charToEscapeQuoteEscaping = charToEscapeQuoteEscaping;
    }
    
    public final boolean isCharToEscapeQuoteEscaping(final char ch) {
        final char current = this.getCharToEscapeQuoteEscaping();
        return current != '\0' && current == ch;
    }
    
    @Override
    protected TreeMap<String, Object> getConfiguration() {
        final TreeMap<String, Object> out = new TreeMap<String, Object>();
        out.put("Quote character", this.quote);
        out.put("Quote escape character", this.quoteEscape);
        out.put("Quote escape escape character", this.charToEscapeQuoteEscaping);
        out.put("Field delimiter", this.delimiter);
        return out;
    }
    
    public final CsvFormat clone() {
        return (CsvFormat)super.clone();
    }
}
