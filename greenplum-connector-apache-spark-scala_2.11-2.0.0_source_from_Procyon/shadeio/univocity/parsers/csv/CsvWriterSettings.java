// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import shadeio.univocity.parsers.common.Format;
import shadeio.univocity.parsers.common.CommonSettings;
import shadeio.univocity.parsers.common.fields.FieldIndexSelector;
import shadeio.univocity.parsers.common.fields.FieldNameSelector;
import shadeio.univocity.parsers.common.fields.FieldEnumSelector;
import shadeio.univocity.parsers.common.fields.FieldSet;
import java.util.Arrays;
import java.util.Map;
import shadeio.univocity.parsers.common.fields.FieldSelector;
import shadeio.univocity.parsers.common.CommonWriterSettings;

public class CsvWriterSettings extends CommonWriterSettings<CsvFormat>
{
    private boolean escapeUnquotedValues;
    private boolean quoteAllFields;
    private boolean isInputEscaped;
    private boolean normalizeLineEndingsWithinQuotes;
    private char[] quotationTriggers;
    private boolean quoteEscapingEnabled;
    private FieldSelector quotedFieldSelector;
    
    public CsvWriterSettings() {
        this.escapeUnquotedValues = false;
        this.quoteAllFields = false;
        this.isInputEscaped = false;
        this.normalizeLineEndingsWithinQuotes = true;
        this.quotationTriggers = new char[0];
        this.quoteEscapingEnabled = false;
        this.quotedFieldSelector = null;
    }
    
    public boolean getQuoteAllFields() {
        return this.quoteAllFields;
    }
    
    public void setQuoteAllFields(final boolean quoteAllFields) {
        this.quoteAllFields = quoteAllFields;
    }
    
    public boolean isEscapeUnquotedValues() {
        return this.escapeUnquotedValues;
    }
    
    public void setEscapeUnquotedValues(final boolean escapeUnquotedValues) {
        this.escapeUnquotedValues = escapeUnquotedValues;
    }
    
    public final boolean isInputEscaped() {
        return this.isInputEscaped;
    }
    
    public final void setInputEscaped(final boolean isInputEscaped) {
        this.isInputEscaped = isInputEscaped;
    }
    
    public boolean isNormalizeLineEndingsWithinQuotes() {
        return this.normalizeLineEndingsWithinQuotes;
    }
    
    public void setNormalizeLineEndingsWithinQuotes(final boolean normalizeLineEndingsWithinQuotes) {
        this.normalizeLineEndingsWithinQuotes = normalizeLineEndingsWithinQuotes;
    }
    
    @Override
    protected CsvFormat createDefaultFormat() {
        return new CsvFormat();
    }
    
    public char[] getQuotationTriggers() {
        return this.quotationTriggers;
    }
    
    public void setQuotationTriggers(final char... quotationTriggers) {
        this.quotationTriggers = ((quotationTriggers == null) ? new char[0] : quotationTriggers);
    }
    
    public boolean isQuotationTrigger(final char ch) {
        for (int i = 0; i < this.quotationTriggers.length; ++i) {
            if (this.quotationTriggers[i] == ch) {
                return true;
            }
        }
        return false;
    }
    
    public boolean isQuoteEscapingEnabled() {
        return this.quoteEscapingEnabled;
    }
    
    public void setQuoteEscapingEnabled(final boolean quoteEscapingEnabled) {
        this.quoteEscapingEnabled = quoteEscapingEnabled;
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
        out.put("Quote all fields", this.quoteAllFields);
        out.put("Escape unquoted values", this.escapeUnquotedValues);
        out.put("Normalize escaped line separators", this.normalizeLineEndingsWithinQuotes);
        out.put("Input escaped", this.isInputEscaped);
        out.put("Quote escaping enabled", this.quoteEscapingEnabled);
        out.put("Quotation triggers", Arrays.toString(this.quotationTriggers));
    }
    
    public final CsvWriterSettings clone() {
        return (CsvWriterSettings)super.clone();
    }
    
    public final CsvWriterSettings clone(final boolean clearInputSpecificSettings) {
        return (CsvWriterSettings)super.clone(clearInputSpecificSettings);
    }
    
    final FieldSelector getQuotedFieldSelector() {
        return this.quotedFieldSelector;
    }
    
    private <T> FieldSet<T> setFieldSet(final FieldSet<T> fieldSet, final T... values) {
        this.quotedFieldSelector = (FieldSelector)fieldSet;
        fieldSet.add(values);
        return fieldSet;
    }
    
    public final FieldSet<Enum> quoteFields(final Enum... columns) {
        return (FieldSet<Enum>)this.setFieldSet(new FieldEnumSelector(), (Enum[])columns);
    }
    
    public final FieldSet<String> quoteFields(final String... columns) {
        return this.setFieldSet(new FieldNameSelector(), columns);
    }
    
    public final FieldSet<Integer> quoteIndexes(final Integer... columns) {
        return this.setFieldSet(new FieldIndexSelector(), columns);
    }
}
