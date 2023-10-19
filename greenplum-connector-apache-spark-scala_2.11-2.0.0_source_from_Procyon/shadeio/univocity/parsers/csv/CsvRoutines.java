// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.csv;

import shadeio.univocity.parsers.common.AbstractParser;
import shadeio.univocity.parsers.common.AbstractWriter;
import shadeio.univocity.parsers.common.CommonParserSettings;
import shadeio.univocity.parsers.common.CommonWriterSettings;
import java.io.Writer;
import shadeio.univocity.parsers.common.routine.AbstractRoutines;

public class CsvRoutines extends AbstractRoutines<CsvParserSettings, CsvWriterSettings>
{
    public CsvRoutines() {
        this((CsvParserSettings)null, null);
    }
    
    public CsvRoutines(final CsvParserSettings parserSettings) {
        this(parserSettings, null);
    }
    
    public CsvRoutines(final CsvWriterSettings writerSettings) {
        this((CsvParserSettings)null, writerSettings);
    }
    
    public CsvRoutines(final CsvParserSettings parserSettings, final CsvWriterSettings writerSettings) {
        super("CSV parsing/writing routine", parserSettings, writerSettings);
    }
    
    @Override
    protected CsvParser createParser(final CsvParserSettings parserSettings) {
        return new CsvParser(parserSettings);
    }
    
    @Override
    protected CsvWriter createWriter(final Writer output, final CsvWriterSettings writerSettings) {
        return new CsvWriter(output, writerSettings);
    }
    
    @Override
    protected CsvParserSettings createDefaultParserSettings() {
        return new CsvParserSettings();
    }
    
    @Override
    protected CsvWriterSettings createDefaultWriterSettings() {
        return new CsvWriterSettings();
    }
}
