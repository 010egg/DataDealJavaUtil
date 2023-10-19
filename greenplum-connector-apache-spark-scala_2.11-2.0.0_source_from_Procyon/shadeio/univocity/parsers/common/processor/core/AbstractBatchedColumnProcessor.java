// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Map;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractBatchedColumnProcessor<T extends Context> implements Processor<T>, BatchedColumnReader<String>
{
    private final ColumnSplitter<String> splitter;
    private final int rowsPerBatch;
    private int batchCount;
    private int batchesProcessed;
    
    public AbstractBatchedColumnProcessor(final int rowsPerBatch) {
        this.splitter = new ColumnSplitter<String>(rowsPerBatch);
        this.rowsPerBatch = rowsPerBatch;
    }
    
    @Override
    public void processStarted(final T context) {
        this.splitter.reset();
        this.batchCount = 0;
        this.batchesProcessed = 0;
    }
    
    @Override
    public void rowProcessed(final String[] row, final T context) {
        this.splitter.addValuesToColumns(row, context);
        ++this.batchCount;
        if (this.batchCount >= this.rowsPerBatch) {
            this.batchProcessed(this.batchCount);
            this.batchCount = 0;
            this.splitter.clearValues();
            ++this.batchesProcessed;
        }
    }
    
    @Override
    public void processEnded(final T context) {
        if (this.batchCount > 0) {
            this.batchProcessed(this.batchCount);
        }
    }
    
    @Override
    public final String[] getHeaders() {
        return this.splitter.getHeaders();
    }
    
    @Override
    public final List<List<String>> getColumnValuesAsList() {
        return this.splitter.getColumnValues();
    }
    
    @Override
    public final void putColumnValuesInMapOfNames(final Map<String, List<String>> map) {
        this.splitter.putColumnValuesInMapOfNames(map);
    }
    
    @Override
    public final void putColumnValuesInMapOfIndexes(final Map<Integer, List<String>> map) {
        this.splitter.putColumnValuesInMapOfIndexes(map);
    }
    
    @Override
    public final Map<String, List<String>> getColumnValuesAsMapOfNames() {
        return this.splitter.getColumnValuesAsMapOfNames();
    }
    
    @Override
    public final Map<Integer, List<String>> getColumnValuesAsMapOfIndexes() {
        return this.splitter.getColumnValuesAsMapOfIndexes();
    }
    
    @Override
    public List<String> getColumn(final String columnName) {
        return this.splitter.getColumnValues(columnName, String.class);
    }
    
    @Override
    public List<String> getColumn(final int columnIndex) {
        return this.splitter.getColumnValues(columnIndex, String.class);
    }
    
    @Override
    public int getRowsPerBatch() {
        return this.rowsPerBatch;
    }
    
    @Override
    public int getBatchesProcessed() {
        return this.batchesProcessed;
    }
    
    @Override
    public abstract void batchProcessed(final int p0);
}
