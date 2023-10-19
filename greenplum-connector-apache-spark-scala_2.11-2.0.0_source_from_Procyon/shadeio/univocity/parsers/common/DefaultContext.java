// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.record.AbstractRecordFactory;
import shadeio.univocity.parsers.common.record.RecordMetaData;
import shadeio.univocity.parsers.common.record.Record;
import shadeio.univocity.parsers.common.record.RecordFactory;

public class DefaultContext implements Context
{
    protected boolean stopped;
    final ParserOutput output;
    final ColumnMap columnMap;
    final int errorContentLength;
    protected RecordFactory recordFactory;
    
    public DefaultContext(final int errorContentLength) {
        this(null, errorContentLength);
    }
    
    public DefaultContext(final ParserOutput output, final int errorContentLength) {
        this.stopped = false;
        this.output = output;
        this.errorContentLength = errorContentLength;
        this.columnMap = new ColumnMap(this, output);
    }
    
    @Override
    public String[] headers() {
        if (this.output == null) {
            return ArgumentUtils.EMPTY_STRING_ARRAY;
        }
        return this.output.getHeaders();
    }
    
    @Override
    public String[] selectedHeaders() {
        final int[] extractedFieldIndexes = this.extractedFieldIndexes();
        if (extractedFieldIndexes != null) {
            final String[] extractedFields = new String[extractedFieldIndexes.length];
            final String[] headers = this.headers();
            for (int i = 0; i < extractedFieldIndexes.length; ++i) {
                extractedFields[i] = headers[extractedFieldIndexes[i]];
            }
            return extractedFields;
        }
        return this.headers();
    }
    
    @Override
    public int[] extractedFieldIndexes() {
        if (this.output == null) {
            return null;
        }
        return this.output.getSelectedIndexes();
    }
    
    @Override
    public boolean columnsReordered() {
        return this.output != null && this.output.isColumnReorderingEnabled();
    }
    
    @Override
    public int indexOf(final String header) {
        return this.columnMap.indexOf(header);
    }
    
    @Override
    public int indexOf(final Enum<?> header) {
        return this.columnMap.indexOf(header);
    }
    
    void reset() {
        if (this.output != null) {
            this.output.reset();
        }
        this.recordFactory = null;
        this.columnMap.reset();
    }
    
    @Override
    public int currentColumn() {
        if (this.output == null) {
            return -1;
        }
        return this.output.getCurrentColumn();
    }
    
    @Override
    public long currentRecord() {
        if (this.output == null) {
            return -1L;
        }
        return this.output.getCurrentRecord();
    }
    
    @Override
    public void stop() {
        this.stopped = true;
    }
    
    @Override
    public boolean isStopped() {
        return this.stopped;
    }
    
    @Override
    public int errorContentLength() {
        return this.errorContentLength;
    }
    
    @Override
    public Record toRecord(final String[] row) {
        if (this.recordFactory == null) {
            this.recordFactory = new RecordFactory(this);
        }
        return this.recordFactory.newRecord(row);
    }
    
    @Override
    public RecordMetaData recordMetaData() {
        if (this.recordFactory == null) {
            this.recordFactory = new RecordFactory(this);
        }
        return ((AbstractRecordFactory<R, RecordMetaData>)this.recordFactory).getRecordMetaData();
    }
}
