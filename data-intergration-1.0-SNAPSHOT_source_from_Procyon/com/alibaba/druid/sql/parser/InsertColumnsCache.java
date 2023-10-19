// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import com.alibaba.druid.util.FnvHash;
import com.alibaba.druid.sql.ast.SQLExpr;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InsertColumnsCache
{
    public static InsertColumnsCache global;
    public ConcurrentMap<Long, Entry> cache;
    private final Entry[] buckets;
    private final int indexMask;
    
    public InsertColumnsCache(final int tableSize) {
        this.cache = new ConcurrentHashMap<Long, Entry>();
        this.indexMask = tableSize - 1;
        this.buckets = new Entry[tableSize];
    }
    
    public final Entry get(final long hashCode64) {
        final int bucket = (int)hashCode64 & this.indexMask;
        for (Entry entry = this.buckets[bucket]; entry != null; entry = entry.next) {
            if (hashCode64 == entry.hashCode64) {
                return entry;
            }
        }
        return null;
    }
    
    public boolean put(final long hashCode64, final String columnsString, final String columnsFormattedString, final List<SQLExpr> columns) {
        final int bucket = (int)hashCode64 & this.indexMask;
        for (Entry entry = this.buckets[bucket]; entry != null; entry = entry.next) {
            if (hashCode64 == entry.hashCode64) {
                return true;
            }
        }
        Entry entry = new Entry(hashCode64, columnsString, columnsFormattedString, columns, this.buckets[bucket]);
        this.buckets[bucket] = entry;
        return false;
    }
    
    static {
        InsertColumnsCache.global = new InsertColumnsCache(8192);
    }
    
    public static final class Entry
    {
        public final long hashCode64;
        public final String columnsString;
        public final String columnsFormattedString;
        public final long columnsFormattedStringHash;
        public final List<SQLExpr> columns;
        public final Entry next;
        
        public Entry(final long hashCode64, final String columnsString, final String columnsFormattedString, final List<SQLExpr> columns, final Entry next) {
            this.hashCode64 = hashCode64;
            this.columnsString = columnsString;
            this.columnsFormattedString = columnsFormattedString;
            this.columnsFormattedStringHash = FnvHash.fnv1a_64_lower(columnsFormattedString);
            this.columns = columns;
            this.next = next;
        }
    }
}
