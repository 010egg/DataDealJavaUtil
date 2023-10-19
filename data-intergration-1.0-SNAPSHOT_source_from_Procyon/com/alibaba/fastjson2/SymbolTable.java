// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.Iterator;
import java.util.Set;
import com.alibaba.fastjson2.util.Fnv;
import java.util.Collection;
import java.util.TreeSet;
import java.util.Arrays;

public final class SymbolTable
{
    private final String[] names;
    private final long hashCode64;
    private final short[] mapping;
    private final long[] hashCodes;
    private final long[] hashCodesOrigin;
    
    public SymbolTable(final String... input) {
        final Set<String> set = new TreeSet<String>(Arrays.asList(input));
        this.names = new String[set.size()];
        final Iterator<String> it = set.iterator();
        for (int i = 0; i < this.names.length; ++i) {
            if (it.hasNext()) {
                this.names[i] = it.next();
            }
        }
        final long[] hashCodes = new long[this.names.length];
        for (int j = 0; j < this.names.length; ++j) {
            final long hashCode = Fnv.hashCode64(this.names[j]);
            hashCodes[j] = hashCode;
        }
        this.hashCodesOrigin = hashCodes;
        Arrays.sort(this.hashCodes = Arrays.copyOf(hashCodes, hashCodes.length));
        this.mapping = new short[this.hashCodes.length];
        for (int j = 0; j < hashCodes.length; ++j) {
            final long hashCode = hashCodes[j];
            final int index = Arrays.binarySearch(this.hashCodes, hashCode);
            this.mapping[index] = (short)j;
        }
        long hashCode2 = -3750763034362895579L;
        for (final long hashCode3 : hashCodes) {
            hashCode2 ^= hashCode3;
            hashCode2 *= 1099511628211L;
        }
        this.hashCode64 = hashCode2;
    }
    
    public int size() {
        return this.names.length;
    }
    
    public long hashCode64() {
        return this.hashCode64;
    }
    
    public String getNameByHashCode(final long hashCode) {
        final int m = Arrays.binarySearch(this.hashCodes, hashCode);
        if (m < 0) {
            return null;
        }
        final int index = this.mapping[m];
        return this.names[index];
    }
    
    public int getOrdinalByHashCode(final long hashCode) {
        final int m = Arrays.binarySearch(this.hashCodes, hashCode);
        if (m < 0) {
            return -1;
        }
        return this.mapping[m] + 1;
    }
    
    public int getOrdinal(final String name) {
        final int m = Arrays.binarySearch(this.hashCodes, Fnv.hashCode64(name));
        if (m < 0) {
            return -1;
        }
        return this.mapping[m] + 1;
    }
    
    public String getName(final int ordinal) {
        return this.names[ordinal - 1];
    }
    
    public long getHashCode(final int ordinal) {
        return this.hashCodesOrigin[ordinal - 1];
    }
}
