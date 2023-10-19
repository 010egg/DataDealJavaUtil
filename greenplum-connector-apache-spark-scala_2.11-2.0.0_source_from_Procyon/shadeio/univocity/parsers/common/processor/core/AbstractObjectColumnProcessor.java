// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Map;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractObjectColumnProcessor<T extends Context> extends AbstractObjectProcessor<T> implements ColumnReader<Object>
{
    private final ColumnSplitter<Object> splitter;
    
    public AbstractObjectColumnProcessor() {
        this(1000);
    }
    
    public AbstractObjectColumnProcessor(final int expectedRowCount) {
        this.splitter = new ColumnSplitter<Object>(expectedRowCount);
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
    public void rowProcessed(final Object[] row, final T context) {
        this.splitter.addValuesToColumns(row, context);
    }
    
    @Override
    public void processStarted(final T context) {
        super.processStarted(context);
        this.splitter.reset();
    }
    
    public <V> List<V> getColumn(final String columnName, final Class<V> columnType) {
        return this.splitter.getColumnValues(columnName, columnType);
    }
    
    public <V> List<V> getColumn(final int columnIndex, final Class<V> columnType) {
        return this.splitter.getColumnValues(columnIndex, columnType);
    }
    
    @Override
    public List<Object> getColumn(final String columnName) {
        return this.splitter.getColumnValues(columnName, Object.class);
    }
    
    @Override
    public List<Object> getColumn(final int columnIndex) {
        return this.splitter.getColumnValues(columnIndex, Object.class);
    }
}
