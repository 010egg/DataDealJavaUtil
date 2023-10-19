// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import shadeio.univocity.parsers.common.AbstractParser;
import shadeio.univocity.parsers.common.AbstractWriter;
import shadeio.univocity.parsers.common.CommonParserSettings;
import shadeio.univocity.parsers.common.CommonWriterSettings;
import java.io.Writer;
import shadeio.univocity.parsers.common.routine.AbstractRoutines;

public class FixedWidthRoutines extends AbstractRoutines<FixedWidthParserSettings, FixedWidthWriterSettings>
{
    public FixedWidthRoutines() {
        this((FixedWidthParserSettings)null, null);
    }
    
    public FixedWidthRoutines(final FixedWidthParserSettings parserSettings) {
        this(parserSettings, null);
    }
    
    public FixedWidthRoutines(final FixedWidthWriterSettings writerSettings) {
        this((FixedWidthParserSettings)null, writerSettings);
    }
    
    public FixedWidthRoutines(final FixedWidthParserSettings parserSettings, final FixedWidthWriterSettings writerSettings) {
        super("Fixed-width parsing/writing routine", parserSettings, writerSettings);
    }
    
    @Override
    protected void adjustColumnLengths(final String[] headers, final int[] lengths) {
        if (((AbstractRoutines<P, FixedWidthWriterSettings>)this).getWriterSettings().getFieldLengths() == null) {
            ((AbstractRoutines<P, FixedWidthWriterSettings>)this).getWriterSettings().setFieldLengths(new FixedWidthFields(headers, lengths));
        }
    }
    
    @Override
    protected FixedWidthParser createParser(final FixedWidthParserSettings parserSettings) {
        return new FixedWidthParser(parserSettings);
    }
    
    @Override
    protected FixedWidthWriter createWriter(final Writer output, final FixedWidthWriterSettings writerSettings) {
        return new FixedWidthWriter(output, writerSettings);
    }
    
    @Override
    protected FixedWidthParserSettings createDefaultParserSettings() {
        return new FixedWidthParserSettings();
    }
    
    @Override
    protected FixedWidthWriterSettings createDefaultWriterSettings() {
        return new FixedWidthWriterSettings();
    }
}
