// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Map;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractBatchedObjectColumnProcessor<T extends Context> extends AbstractObjectProcessor<T> implements Processor<T>, BatchedColumnReader<Object>
{
    private final ColumnSplitter<Object> splitter;
    private final int rowsPerBatch;
    private int batchCount;
    private int batchesProcessed;
    
    public AbstractBatchedObjectColumnProcessor(final int rowsPerBatch) {
        this.splitter = new ColumnSplitter<Object>(rowsPerBatch);
        this.rowsPerBatch = rowsPerBatch;
    }
    
    @Override
    public void processStarted(final T context) {
        super.processStarted(context);
        this.splitter.reset();
        this.batchCount = 0;
        this.batchesProcessed = 0;
    }
    
    @Override
    public void rowProcessed(final Object[] row, final T context) {
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
        super.processEnded(context);
        if (this.batchCount > 0) {
            this.batchProcessed(this.batchCount);
        }
    }
    
    @Override
    public final String[] getHeaders() {
        return this.splitter.getHeaders();
    }
    
    @Override
    public final List<List<Object>> getColumnValuesAsList() {
        return this.splitter.getColumnValues();
    }
    
    @Override
    public final void putColumnValuesInMapOfNames(final Map<String, List<Object>> map) {
        this.splitter.putColumnValuesInMapOfNames(map);
    }
    
    @Override
    public final void putColumnValuesInMapOfIndexes(final Map<Integer, List<Object>> map) {
        this.splitter.putColumnValuesInMapOfIndexes(map);
    }
    
    @Override
    public final Map<String, List<Object>> getColumnValuesAsMapOfNames() {
        return this.splitter.getColumnValuesAsMapOfNames();
    }
    
    @Override
    public final Map<Integer, List<Object>> getColumnValuesAsMapOfIndexes() {
        return this.splitter.getColumnValuesAsMapOfIndexes();
    }
    
    @Override
    public List<Object> getColumn(final String columnName) {
        return this.splitter.getColumnValues(columnName, Object.class);
    }
    
    @Override
    public List<Object> getColumn(final int columnIndex) {
        return this.splitter.getColumnValues(columnIndex, Object.class);
    }
    
    public <V> List<V> getColumn(final String columnName, final Class<V> columnType) {
        return this.splitter.getColumnValues(columnName, columnType);
    }
    
    public <V> List<V> getColumn(final int columnIndex, final Class<V> columnType) {
        return this.splitter.getColumnValues(columnIndex, columnType);
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
