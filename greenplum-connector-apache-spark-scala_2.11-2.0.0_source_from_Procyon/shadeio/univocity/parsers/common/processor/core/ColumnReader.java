// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Map;
import java.util.List;

interface ColumnReader<T>
{
    String[] getHeaders();
    
    List<List<T>> getColumnValuesAsList();
    
    void putColumnValuesInMapOfNames(final Map<String, List<T>> p0);
    
    void putColumnValuesInMapOfIndexes(final Map<Integer, List<T>> p0);
    
    Map<String, List<T>> getColumnValuesAsMapOfNames();
    
    Map<Integer, List<T>> getColumnValuesAsMapOfIndexes();
    
    List<T> getColumn(final String p0);
    
    List<T> getColumn(final int p0);
}
