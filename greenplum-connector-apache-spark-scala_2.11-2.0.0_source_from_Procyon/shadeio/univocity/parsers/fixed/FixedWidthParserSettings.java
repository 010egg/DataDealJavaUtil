// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import shadeio.univocity.parsers.common.Format;
import shadeio.univocity.parsers.annotations.Headers;
import shadeio.univocity.parsers.common.CommonSettings;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.input.DefaultCharAppender;
import shadeio.univocity.parsers.common.input.CharAppender;
import java.util.HashMap;
import java.util.Map;
import shadeio.univocity.parsers.common.CommonParserSettings;

public class FixedWidthParserSettings extends CommonParserSettings<FixedWidthFormat>
{
    protected boolean skipTrailingCharsUntilNewline;
    protected boolean recordEndsOnNewline;
    private boolean useDefaultPaddingForHeaders;
    private FixedWidthFields fieldLengths;
    private Map<String, FixedWidthFields> lookaheadFormats;
    private Map<String, FixedWidthFields> lookbehindFormats;
    
    public FixedWidthParserSettings(final FixedWidthFields fieldLengths) {
        this.skipTrailingCharsUntilNewline = false;
        this.recordEndsOnNewline = false;
        this.useDefaultPaddingForHeaders = true;
        this.lookaheadFormats = new HashMap<String, FixedWidthFields>();
        this.lookbehindFormats = new HashMap<String, FixedWidthFields>();
        if (fieldLengths == null) {
            throw new IllegalArgumentException("Field lengths cannot be null");
        }
        this.fieldLengths = fieldLengths;
        final String[] names = fieldLengths.getFieldNames();
        if (names != null) {
            this.setHeaders(names);
        }
    }
    
    public FixedWidthParserSettings() {
        this.skipTrailingCharsUntilNewline = false;
        this.recordEndsOnNewline = false;
        this.useDefaultPaddingForHeaders = true;
        this.lookaheadFormats = new HashMap<String, FixedWidthFields>();
        this.lookbehindFormats = new HashMap<String, FixedWidthFields>();
        this.fieldLengths = null;
    }
    
    int[] getFieldLengths() {
        if (this.fieldLengths == null) {
            return null;
        }
        return this.fieldLengths.getFieldLengths();
    }
    
    int[] getAllLengths() {
        if (this.fieldLengths == null) {
            return null;
        }
        return this.fieldLengths.getAllLengths();
    }
    
    char[] getFieldPaddings() {
        if (this.fieldLengths == null) {
            return null;
        }
        return this.fieldLengths.getFieldPaddings(this.getFormat());
    }
    
    boolean[] getFieldsToIgnore() {
        if (this.fieldLengths == null) {
            return null;
        }
        return this.fieldLengths.getFieldsToIgnore();
    }
    
    FieldAlignment[] getFieldAlignments() {
        if (this.fieldLengths == null) {
            return null;
        }
        return this.fieldLengths.getFieldAlignments();
    }
    
    public boolean getSkipTrailingCharsUntilNewline() {
        return this.skipTrailingCharsUntilNewline;
    }
    
    public void setSkipTrailingCharsUntilNewline(final boolean skipTrailingCharsUntilNewline) {
        this.skipTrailingCharsUntilNewline = skipTrailingCharsUntilNewline;
    }
    
    public boolean getRecordEndsOnNewline() {
        return this.recordEndsOnNewline;
    }
    
    public void setRecordEndsOnNewline(final boolean recordEndsOnNewline) {
        this.recordEndsOnNewline = recordEndsOnNewline;
    }
    
    @Override
    protected FixedWidthFormat createDefaultFormat() {
        return new FixedWidthFormat();
    }
    
    @Override
    protected CharAppender newCharAppender() {
        return new DefaultCharAppender(this.getMaxCharsPerColumn(), this.getNullValue(), this.getWhitespaceRangeStart());
    }
    
    @Override
    public int getMaxCharsPerColumn() {
        final int max = super.getMaxCharsPerColumn();
        int minimum = 0;
        for (final int length : this.calculateMaxFieldLengths()) {
            minimum += length + 2;
        }
        return (max > minimum) ? max : minimum;
    }
    
    @Override
    public int getMaxColumns() {
        final int max = super.getMaxColumns();
        final int minimum = this.calculateMaxFieldLengths().length;
        return (max > minimum) ? max : minimum;
    }
    
    private int[] calculateMaxFieldLengths() {
        return Lookup.calculateMaxFieldLengths(this.fieldLengths, this.lookaheadFormats, this.lookbehindFormats);
    }
    
    Lookup[] getLookaheadFormats() {
        return Lookup.getLookupFormats(this.lookaheadFormats, this.getFormat());
    }
    
    Lookup[] getLookbehindFormats() {
        return Lookup.getLookupFormats(this.lookbehindFormats, this.getFormat());
    }
    
    public void addFormatForLookahead(final String lookahead, final FixedWidthFields lengths) {
        Lookup.registerLookahead(lookahead, lengths, this.lookaheadFormats);
    }
    
    public void addFormatForLookbehind(final String lookbehind, final FixedWidthFields lengths) {
        Lookup.registerLookbehind(lookbehind, lengths, this.lookbehindFormats);
    }
    
    public boolean getUseDefaultPaddingForHeaders() {
        return this.useDefaultPaddingForHeaders;
    }
    
    public void setUseDefaultPaddingForHeaders(final boolean useDefaultPaddingForHeaders) {
        this.useDefaultPaddingForHeaders = useDefaultPaddingForHeaders;
    }
    
    @Override
    protected void configureFromAnnotations(final Class<?> beanClass) {
        if (this.fieldLengths == null) {
            try {
                this.fieldLengths = FixedWidthFields.forParsing(beanClass);
                final Headers headerAnnotation = AnnotationHelper.findHeadersAnnotation(beanClass);
                if (this.headerExtractionEnabled == null && headerAnnotation != null) {
                    this.setHeaderExtractionEnabled(headerAnnotation.extract());
                }
            }
            catch (IllegalArgumentException e) {
                throw e;
            }
            catch (Exception ex) {}
        }
        if (this.headerExtractionEnabled == null) {
            this.setHeaderExtractionEnabled(false);
        }
        super.configureFromAnnotations(beanClass);
        if (!this.isHeaderExtractionEnabled()) {
            FixedWidthFields.setHeadersIfPossible(this.fieldLengths, this);
        }
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
        out.put("Skip trailing characters until new line", this.skipTrailingCharsUntilNewline);
        out.put("Record ends on new line", this.recordEndsOnNewline);
        out.put("Field lengths", (this.fieldLengths == null) ? "<null>" : this.fieldLengths.toString());
        out.put("Lookahead formats", this.lookaheadFormats);
        out.put("Lookbehind formats", this.lookbehindFormats);
    }
    
    public final FixedWidthParserSettings clone() {
        return (FixedWidthParserSettings)super.clone();
    }
    
    @Deprecated
    @Override
    protected final FixedWidthParserSettings clone(final boolean clearInputSpecificSettings) {
        return this.clone(clearInputSpecificSettings, (this.fieldLengths == null) ? null : this.fieldLengths.clone());
    }
    
    public final FixedWidthParserSettings clone(final FixedWidthFields fields) {
        return this.clone(true, fields);
    }
    
    private FixedWidthParserSettings clone(final boolean clearInputSpecificSettings, final FixedWidthFields fields) {
        final FixedWidthParserSettings out = (FixedWidthParserSettings)super.clone(clearInputSpecificSettings);
        out.fieldLengths = fields;
        if (clearInputSpecificSettings) {
            out.lookaheadFormats = new HashMap<String, FixedWidthFields>();
            out.lookbehindFormats = new HashMap<String, FixedWidthFields>();
        }
        else {
            out.lookaheadFormats = new HashMap<String, FixedWidthFields>(this.lookaheadFormats);
            out.lookbehindFormats = new HashMap<String, FixedWidthFields>(this.lookbehindFormats);
        }
        return out;
    }
}
