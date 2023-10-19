// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Map;
import java.util.List;
import shadeio.univocity.parsers.common.Context;

public abstract class AbstractColumnProcessor<T extends Context> implements Processor<T>, ColumnReader<String>
{
    private final ColumnSplitter<String> splitter;
    
    public AbstractColumnProcessor() {
        this(1000);
    }
    
    public AbstractColumnProcessor(final int expectedRowCount) {
        this.splitter = new ColumnSplitter<String>(expectedRowCount);
    }
    
    @Override
    public void processStarted(final T context) {
        this.splitter.reset();
    }
    
    @Override
    public void rowProcessed(final String[] row, final T context) {
        this.splitter.addValuesToColumns(row, context);
    }
    
    @Override
    public void processEnded(final T context) {
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
}
