// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.record.AbstractRecordFactory;
import shadeio.univocity.parsers.common.record.RecordFactory;
import shadeio.univocity.parsers.common.record.Record;
import java.util.Collections;
import java.util.Map;
import shadeio.univocity.parsers.common.record.RecordMetaData;

class NoopParsingContext implements ParsingContext
{
    static final NoopParsingContext instance;
    private RecordMetaData recordMetaData;
    
    private NoopParsingContext() {
    }
    
    @Override
    public void stop() {
    }
    
    @Override
    public boolean isStopped() {
        return false;
    }
    
    @Override
    public long currentLine() {
        return 0L;
    }
    
    @Override
    public long currentChar() {
        return 0L;
    }
    
    @Override
    public int currentColumn() {
        return 0;
    }
    
    @Override
    public long currentRecord() {
        return 0L;
    }
    
    @Override
    public void skipLines(final long lines) {
    }
    
    @Override
    public String[] parsedHeaders() {
        return null;
    }
    
    @Override
    public String currentParsedContent() {
        return null;
    }
    
    @Override
    public int currentParsedContentLength() {
        return 0;
    }
    
    @Override
    public Map<Long, String> comments() {
        return Collections.emptyMap();
    }
    
    @Override
    public String lastComment() {
        return null;
    }
    
    @Override
    public char[] lineSeparator() {
        return Format.getSystemLineSeparator();
    }
    
    @Override
    public String[] headers() {
        return null;
    }
    
    @Override
    public String[] selectedHeaders() {
        return null;
    }
    
    @Override
    public int[] extractedFieldIndexes() {
        return null;
    }
    
    @Override
    public boolean columnsReordered() {
        return true;
    }
    
    @Override
    public int indexOf(final String header) {
        return -1;
    }
    
    @Override
    public int indexOf(final Enum<?> header) {
        return -1;
    }
    
    @Override
    public String fieldContentOnError() {
        return null;
    }
    
    @Override
    public int errorContentLength() {
        return -1;
    }
    
    @Override
    public Record toRecord(final String[] row) {
        return null;
    }
    
    @Override
    public RecordMetaData recordMetaData() {
        if (this.recordMetaData == null) {
            this.recordMetaData = ((AbstractRecordFactory<R, RecordMetaData>)new RecordFactory(this)).getRecordMetaData();
        }
        return this.recordMetaData;
    }
    
    static {
        instance = new NoopParsingContext();
    }
}
