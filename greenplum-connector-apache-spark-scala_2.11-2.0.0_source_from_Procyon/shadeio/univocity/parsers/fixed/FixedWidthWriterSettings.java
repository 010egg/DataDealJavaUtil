// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import shadeio.univocity.parsers.common.Format;
import shadeio.univocity.parsers.annotations.Headers;
import shadeio.univocity.parsers.common.CommonSettings;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import java.util.HashMap;
import java.util.Map;
import shadeio.univocity.parsers.common.CommonWriterSettings;

public class FixedWidthWriterSettings extends CommonWriterSettings<FixedWidthFormat>
{
    private FixedWidthFields fieldLengths;
    private Map<String, FixedWidthFields> lookaheadFormats;
    private Map<String, FixedWidthFields> lookbehindFormats;
    private boolean useDefaultPaddingForHeaders;
    private FieldAlignment defaultAlignmentForHeaders;
    private boolean writeLineSeparatorAfterRecord;
    
    public FixedWidthWriterSettings(final FixedWidthFields fieldLengths) {
        this.lookaheadFormats = new HashMap<String, FixedWidthFields>();
        this.lookbehindFormats = new HashMap<String, FixedWidthFields>();
        this.useDefaultPaddingForHeaders = true;
        this.defaultAlignmentForHeaders = null;
        this.writeLineSeparatorAfterRecord = true;
        this.setFieldLengths(fieldLengths);
        final String[] names = fieldLengths.getFieldNames();
        if (names != null) {
            this.setHeaders(names);
        }
    }
    
    public FixedWidthWriterSettings() {
        this.lookaheadFormats = new HashMap<String, FixedWidthFields>();
        this.lookbehindFormats = new HashMap<String, FixedWidthFields>();
        this.useDefaultPaddingForHeaders = true;
        this.defaultAlignmentForHeaders = null;
        this.writeLineSeparatorAfterRecord = true;
        this.fieldLengths = null;
    }
    
    final void setFieldLengths(final FixedWidthFields fieldLengths) {
        if (fieldLengths == null) {
            throw new IllegalArgumentException("Field lengths cannot be null");
        }
        this.fieldLengths = fieldLengths;
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
    
    FieldAlignment[] getFieldAlignments() {
        if (this.fieldLengths == null) {
            return null;
        }
        return this.fieldLengths.getFieldAlignments();
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
    
    @Override
    protected FixedWidthFormat createDefaultFormat() {
        return new FixedWidthFormat();
    }
    
    @Override
    public int getMaxColumns() {
        final int max = super.getMaxColumns();
        final int minimum = Lookup.calculateMaxFieldLengths(this.fieldLengths, this.lookaheadFormats, this.lookbehindFormats).length;
        return (max > minimum) ? max : minimum;
    }
    
    public void addFormatForLookahead(final String lookahead, final FixedWidthFields lengths) {
        Lookup.registerLookahead(lookahead, lengths, this.lookaheadFormats);
    }
    
    public void addFormatForLookbehind(final String lookbehind, final FixedWidthFields lengths) {
        Lookup.registerLookbehind(lookbehind, lengths, this.lookbehindFormats);
    }
    
    Lookup[] getLookaheadFormats() {
        return Lookup.getLookupFormats(this.lookaheadFormats, this.getFormat());
    }
    
    Lookup[] getLookbehindFormats() {
        return Lookup.getLookupFormats(this.lookbehindFormats, this.getFormat());
    }
    
    public boolean getUseDefaultPaddingForHeaders() {
        return this.useDefaultPaddingForHeaders;
    }
    
    public void setUseDefaultPaddingForHeaders(final boolean useDefaultPaddingForHeaders) {
        this.useDefaultPaddingForHeaders = useDefaultPaddingForHeaders;
    }
    
    public FieldAlignment getDefaultAlignmentForHeaders() {
        return this.defaultAlignmentForHeaders;
    }
    
    public void setDefaultAlignmentForHeaders(final FieldAlignment defaultAlignmentForHeaders) {
        this.defaultAlignmentForHeaders = defaultAlignmentForHeaders;
    }
    
    public boolean getWriteLineSeparatorAfterRecord() {
        return this.writeLineSeparatorAfterRecord;
    }
    
    public void setWriteLineSeparatorAfterRecord(final boolean writeLineSeparatorAfterRecord) {
        this.writeLineSeparatorAfterRecord = writeLineSeparatorAfterRecord;
    }
    
    @Override
    protected void configureFromAnnotations(final Class<?> beanClass) {
        if (this.fieldLengths != null) {
            return;
        }
        try {
            this.fieldLengths = FixedWidthFields.forWriting(beanClass);
            final Headers headerAnnotation = AnnotationHelper.findHeadersAnnotation(beanClass);
            this.setHeaderWritingEnabled(headerAnnotation != null && headerAnnotation.write());
        }
        catch (Exception ex) {}
        super.configureFromAnnotations(beanClass);
        FixedWidthFields.setHeadersIfPossible(this.fieldLengths, this);
    }
    
    @Override
    protected void addConfiguration(final Map<String, Object> out) {
        super.addConfiguration(out);
        out.put("Write line separator after record", this.writeLineSeparatorAfterRecord);
        out.put("Field lengths", this.fieldLengths);
        out.put("Lookahead formats", this.lookaheadFormats);
        out.put("Lookbehind formats", this.lookbehindFormats);
        out.put("Use default padding for headers", this.useDefaultPaddingForHeaders);
        out.put("Default alignment for headers", this.defaultAlignmentForHeaders);
    }
    
    public final FixedWidthWriterSettings clone() {
        return (FixedWidthWriterSettings)super.clone(false);
    }
    
    @Deprecated
    @Override
    protected final FixedWidthWriterSettings clone(final boolean clearInputSpecificSettings) {
        return this.clone(clearInputSpecificSettings, (this.fieldLengths == null) ? null : this.fieldLengths.clone());
    }
    
    public final FixedWidthWriterSettings clone(final FixedWidthFields fields) {
        return this.clone(true, fields);
    }
    
    private FixedWidthWriterSettings clone(final boolean clearInputSpecificSettings, final FixedWidthFields fields) {
        final FixedWidthWriterSettings out = (FixedWidthWriterSettings)super.clone(clearInputSpecificSettings);
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
