// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.transform;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.SQLUtils;
import java.util.HashMap;
import java.util.Map;

public class TableMapping
{
    private final String srcTable;
    private final String destTable;
    private final long srcTableHash;
    private final Map<Long, ColumnMapping> columnMappings;
    
    public TableMapping(final String srcTable, final String destTable) {
        this.columnMappings = new HashMap<Long, ColumnMapping>();
        this.srcTable = SQLUtils.normalize(srcTable);
        this.destTable = SQLUtils.normalize(destTable);
        this.srcTableHash = FnvHash.hashCode64(srcTable);
    }
    
    public String getSrcTable() {
        return this.srcTable;
    }
    
    public String getDestTable() {
        return this.destTable;
    }
    
    public long getSrcTableHash() {
        return this.srcTableHash;
    }
    
    public String getMappingColumn(final String srcColumn) {
        final long hash = FnvHash.hashCode64(srcColumn);
        final ColumnMapping columnMapping = this.columnMappings.get(hash);
        if (columnMapping == null) {
            return null;
        }
        return columnMapping.destDestColumn;
    }
    
    public void addColumnMapping(final String srcColumn, final String destColumn) {
        final ColumnMapping columnMapping = new ColumnMapping(srcColumn, destColumn);
        this.columnMappings.put(columnMapping.srcColumnHash, columnMapping);
    }
    
    private static class ColumnMapping
    {
        public final String srcColumn;
        public final String destDestColumn;
        public final long srcColumnHash;
        
        public ColumnMapping(final String srcColumn, final String destDestColumn) {
            this.srcColumn = SQLUtils.normalize(srcColumn);
            this.destDestColumn = SQLUtils.normalize(destDestColumn);
            this.srcColumnHash = FnvHash.hashCode64(srcColumn);
        }
    }
}
