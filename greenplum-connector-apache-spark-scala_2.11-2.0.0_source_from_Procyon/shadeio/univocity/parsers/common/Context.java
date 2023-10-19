// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.record.RecordMetaData;
import shadeio.univocity.parsers.common.record.Record;

public interface Context
{
    String[] headers();
    
    String[] selectedHeaders();
    
    int[] extractedFieldIndexes();
    
    boolean columnsReordered();
    
    int indexOf(final String p0);
    
    int indexOf(final Enum<?> p0);
    
    int currentColumn();
    
    long currentRecord();
    
    void stop();
    
    boolean isStopped();
    
    int errorContentLength();
    
    Record toRecord(final String[] p0);
    
    RecordMetaData recordMetaData();
}
