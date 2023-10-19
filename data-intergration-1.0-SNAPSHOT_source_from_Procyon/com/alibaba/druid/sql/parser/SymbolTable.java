// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.sql.parser;

import java.nio.charset.Charset;

public class SymbolTable
{
    private static final Charset UTF8;
    private static final boolean JVM_16;
    public static SymbolTable global;
    private final Entry[] entries;
    private final int indexMask;
    
    public SymbolTable(final int tableSize) {
        this.indexMask = tableSize - 1;
        this.entries = new Entry[tableSize];
    }
    
    public String addSymbol(final String buffer, final int offset, final int len, final long hash) {
        final int bucket = (int)hash & this.indexMask;
        Entry entry = this.entries[bucket];
        if (entry == null) {
            final String str = SymbolTable.JVM_16 ? subString(buffer, offset, len) : buffer.substring(offset, offset + len);
            entry = new Entry(hash, len, str);
            this.entries[bucket] = entry;
            return str;
        }
        if (hash == entry.hash) {
            return entry.value;
        }
        final String str = SymbolTable.JVM_16 ? subString(buffer, offset, len) : buffer.substring(offset, offset + len);
        return str;
    }
    
    public String addSymbol(final byte[] buffer, final int offset, final int len, final long hash) {
        final int bucket = (int)hash & this.indexMask;
        Entry entry = this.entries[bucket];
        if (entry == null) {
            final String str = subString(buffer, offset, len);
            entry = new Entry(hash, len, str);
            this.entries[bucket] = entry;
            return str;
        }
        if (hash == entry.hash) {
            return entry.value;
        }
        final String str = subString(buffer, offset, len);
        return str;
    }
    
    public String addSymbol(final String symbol, final long hash) {
        final int bucket = (int)hash & this.indexMask;
        Entry entry = this.entries[bucket];
        if (entry == null) {
            entry = new Entry(hash, symbol.length(), symbol);
            this.entries[bucket] = entry;
            return symbol;
        }
        if (hash == entry.hash) {
            return entry.value;
        }
        return symbol;
    }
    
    public String findSymbol(final long hash) {
        final int bucket = (int)hash & this.indexMask;
        final Entry entry = this.entries[bucket];
        if (entry != null && entry.hash == hash) {
            return entry.value;
        }
        return null;
    }
    
    private static String subString(final String src, final int offset, final int len) {
        final char[] chars = new char[len];
        src.getChars(offset, offset + len, chars, 0);
        return new String(chars);
    }
    
    private static String subString(final byte[] bytes, final int from, final int len) {
        final byte[] strBytes = new byte[len];
        System.arraycopy(bytes, from, strBytes, 0, len);
        return new String(strBytes, SymbolTable.UTF8);
    }
    
    static {
        UTF8 = Charset.forName("UTF-8");
        String version = null;
        try {
            version = System.getProperty("java.specification.version");
        }
        catch (IllegalArgumentException ex) {}
        catch (SecurityException ex2) {}
        JVM_16 = "1.6".equals(version);
        SymbolTable.global = new SymbolTable(32768);
    }
    
    private static class Entry
    {
        public final long hash;
        public final int len;
        public final String value;
        
        public Entry(final long hash, final int len, final String value) {
            this.hash = hash;
            this.len = len;
            this.value = value;
        }
    }
}
