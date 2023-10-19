// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

interface BatchedColumnReader<T> extends ColumnReader<T>
{
    int getRowsPerBatch();
    
    int getBatchesProcessed();
    
    void batchProcessed(final int p0);
}
