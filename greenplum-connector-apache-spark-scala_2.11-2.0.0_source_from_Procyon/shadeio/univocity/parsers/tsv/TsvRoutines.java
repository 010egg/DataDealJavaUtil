// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.tsv;

import shadeio.univocity.parsers.common.AbstractParser;
import shadeio.univocity.parsers.common.AbstractWriter;
import shadeio.univocity.parsers.common.CommonParserSettings;
import shadeio.univocity.parsers.common.CommonWriterSettings;
import java.io.Writer;
import shadeio.univocity.parsers.common.routine.AbstractRoutines;

public class TsvRoutines extends AbstractRoutines<TsvParserSettings, TsvWriterSettings>
{
    public TsvRoutines() {
        this((TsvParserSettings)null, null);
    }
    
    public TsvRoutines(final TsvParserSettings parserSettings) {
        this(parserSettings, null);
    }
    
    public TsvRoutines(final TsvWriterSettings writerSettings) {
        this((TsvParserSettings)null, writerSettings);
    }
    
    public TsvRoutines(final TsvParserSettings parserSettings, final TsvWriterSettings writerSettings) {
        super("TSV parsing/writing routine", parserSettings, writerSettings);
    }
    
    @Override
    protected TsvParser createParser(final TsvParserSettings parserSettings) {
        return new TsvParser(parserSettings);
    }
    
    @Override
    protected TsvWriter createWriter(final Writer output, final TsvWriterSettings writerSettings) {
        return new TsvWriter(output, writerSettings);
    }
    
    @Override
    protected TsvParserSettings createDefaultParserSettings() {
        return new TsvParserSettings();
    }
    
    @Override
    protected TsvWriterSettings createDefaultWriterSettings() {
        return new TsvWriterSettings();
    }
}
