// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.HashMap;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;
import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.common.Context;
import java.util.List;

class ColumnSplitter<T>
{
    private List<List<T>> columnValues;
    private String[] headers;
    private int expectedRowCount;
    private long rowCount;
    private long addNullsFrom;
    
    ColumnSplitter(final int expectedRowCount) {
        this.headers = null;
        this.expectedRowCount = 1000;
        if (expectedRowCount <= 0) {
            throw new IllegalArgumentException("Expected row count must be positive");
        }
        this.expectedRowCount = expectedRowCount;
    }
    
    void clearValues() {
        this.addNullsFrom = this.rowCount;
        this.columnValues = null;
    }
    
    void reset() {
        this.columnValues = null;
        this.headers = null;
        this.addNullsFrom = 0L;
        this.rowCount = 0L;
    }
    
    List<List<T>> getColumnValues() {
        return this.columnValues;
    }
    
    String[] getHeaders() {
        return this.headers;
    }
    
    private void initialize(final Context context) {
        if (this.headers == null) {
            final String[] allHeaders = context.headers();
            if (allHeaders == null) {
                this.headers = ArgumentUtils.EMPTY_STRING_ARRAY;
            }
            else if (!context.columnsReordered()) {
                this.headers = allHeaders;
            }
            else {
                final int[] selectedIndexes = context.extractedFieldIndexes();
                final int last = Math.min(allHeaders.length, selectedIndexes.length);
                this.headers = new String[selectedIndexes.length];
                for (int i = 0; i < last; ++i) {
                    final int idx = selectedIndexes[i];
                    if (idx < allHeaders.length) {
                        this.headers[i] = allHeaders[selectedIndexes[i]];
                    }
                }
            }
        }
        this.columnValues = new ArrayList<List<T>>((this.headers.length > 0) ? this.headers.length : 10);
    }
    
    String getHeader(final int columnIndex) {
        if (columnIndex < this.headers.length) {
            return this.headers[columnIndex];
        }
        return null;
    }
    
    void addValuesToColumns(final T[] row, final Context context) {
        if (this.columnValues == null) {
            this.initialize(context);
        }
        if (this.columnValues.size() < row.length) {
            int columnsToAdd = row.length - this.columnValues.size();
            while (columnsToAdd-- > 0) {
                long records = context.currentRecord() - this.addNullsFrom;
                final ArrayList<T> values = new ArrayList<T>((this.expectedRowCount < records) ? ((int)records) : this.expectedRowCount);
                while (--records > 0L) {
                    values.add(null);
                }
                this.columnValues.add(values);
            }
        }
        for (int i = 0; i < row.length; ++i) {
            this.columnValues.get(i).add(row[i]);
        }
        if (row.length < this.columnValues.size()) {
            for (int i = row.length; i < this.columnValues.size(); ++i) {
                this.columnValues.get(i).add(null);
            }
        }
        ++this.rowCount;
    }
    
    void putColumnValuesInMapOfNames(final Map<String, List<T>> map) {
        if (this.columnValues == null) {
            return;
        }
        for (int i = 0; i < this.columnValues.size(); ++i) {
            final String header = this.getHeader(i);
            if (header == null) {
                throw new DataProcessingException("Parsed input does not have header for column at index '" + i + "'. Parsed header names: " + Arrays.toString(this.getHeaders()), i);
            }
            map.put(header, this.columnValues.get(i));
        }
    }
    
     <V> List<V> getColumnValues(final int columnIndex, final Class<V> columnType) {
        if (columnIndex < 0) {
            throw new IllegalArgumentException("Column index must be positive");
        }
        if (columnIndex >= this.columnValues.size()) {
            throw new IllegalArgumentException("Column index must be less than " + this.columnValues.size() + ". Got " + columnIndex);
        }
        return (List<V>)this.columnValues.get(columnIndex);
    }
    
     <V> List<V> getColumnValues(final String columnName, final Class<V> columnType) {
        final int index = ArgumentUtils.indexOf(this.headers, columnName);
        if (index == -1) {
            throw new IllegalArgumentException("No column named '" + columnName + "' has been found. Available column headers: " + Arrays.toString(this.headers));
        }
        return this.getColumnValues(index, columnType);
    }
    
    void putColumnValuesInMapOfIndexes(final Map<Integer, List<T>> map) {
        if (this.columnValues == null) {
            return;
        }
        for (int i = 0; i < this.columnValues.size(); ++i) {
            map.put(i, this.columnValues.get(i));
        }
    }
    
    Map<String, List<T>> getColumnValuesAsMapOfNames() {
        final Map<String, List<T>> map = new HashMap<String, List<T>>();
        this.putColumnValuesInMapOfNames(map);
        return map;
    }
    
    Map<Integer, List<T>> getColumnValuesAsMapOfIndexes() {
        final Map<Integer, List<T>> map = new HashMap<Integer, List<T>>();
        this.putColumnValuesInMapOfIndexes(map);
        return map;
    }
}
