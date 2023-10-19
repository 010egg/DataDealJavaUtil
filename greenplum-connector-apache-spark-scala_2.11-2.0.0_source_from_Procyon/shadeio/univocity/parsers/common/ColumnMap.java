// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.fields.FieldSelector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;
import java.util.Map;

public class ColumnMap
{
    private Map<String, Integer> columnMap;
    private Map<String, Integer> normalizedColumnMap;
    private int[] enumMap;
    private int[] extractedIndexes;
    private final Context context;
    private final ParserOutput output;
    
    public ColumnMap(final Context context, final ParserOutput output) {
        this.extractedIndexes = null;
        this.context = context;
        this.output = output;
    }
    
    public int indexOf(final String header) {
        if (this.columnMap != null && this.columnMap.isEmpty()) {
            return -1;
        }
        this.validateHeader(header);
        if (this.columnMap == null) {
            final String[] headers = this.context.headers();
            if (headers == null) {
                this.columnMap = Collections.emptyMap();
                this.normalizedColumnMap = Collections.emptyMap();
                return -1;
            }
            this.columnMap = new HashMap<String, Integer>(headers.length);
            this.extractedIndexes = this.context.extractedFieldIndexes();
            if (this.extractedIndexes != null) {
                if (this.context.columnsReordered()) {
                    for (int i = 0; i < this.extractedIndexes.length; ++i) {
                        final int originalIndex = this.extractedIndexes[i];
                        final String h = headers[originalIndex];
                        this.columnMap.put(h, i);
                    }
                }
                else {
                    for (int i = 0; i < this.extractedIndexes.length; ++i) {
                        this.columnMap.put(headers[i], i);
                    }
                }
            }
            else {
                for (int i = 0; i < headers.length; ++i) {
                    this.columnMap.put(headers[i], i);
                }
            }
            this.normalizedColumnMap = new HashMap<String, Integer>(headers.length);
            for (final Map.Entry<String, Integer> e : this.columnMap.entrySet()) {
                if (e.getKey() != null) {
                    this.normalizedColumnMap.put(e.getKey().trim().toLowerCase(), e.getValue());
                }
            }
        }
        Integer index = this.columnMap.get(header);
        if (index == null) {
            index = this.normalizedColumnMap.get(header.trim().toLowerCase());
            if (index == null) {
                return -1;
            }
        }
        return index;
    }
    
    private void validateHeader(final Object header) {
        if (header != null) {
            return;
        }
        if (this.context.headers() == null) {
            throw new IllegalArgumentException("Header name cannot be null.");
        }
        throw new IllegalArgumentException("Header name cannot be null. Use one of the available column names: " + Arrays.asList(this.context.headers()));
    }
    
    public int indexOf(final Enum<?> header) {
        if (this.enumMap != null && this.enumMap.length == 0) {
            return -1;
        }
        this.validateHeader(header);
        if (this.enumMap == null) {
            final String[] headers = this.context.headers();
            if (headers == null) {
                this.enumMap = new int[0];
                return -1;
            }
            final Enum<?>[] constants = (Enum<?>[])header.getClass().getEnumConstants();
            int lastOrdinal = Integer.MIN_VALUE;
            for (int i = 0; i < constants.length; ++i) {
                if (lastOrdinal < constants[i].ordinal()) {
                    lastOrdinal = constants[i].ordinal();
                }
            }
            this.enumMap = new int[lastOrdinal + 1];
            FieldSelector selector = (this.output == null) ? null : this.output.getFieldSelector();
            if (!this.context.columnsReordered()) {
                selector = null;
            }
            for (int j = 0; j < constants.length; ++j) {
                final Enum<?> constant = constants[j];
                final String name = constant.toString();
                final int index = ArgumentUtils.indexOf(headers, name, selector);
                this.enumMap[constant.ordinal()] = index;
            }
        }
        return this.enumMap[header.ordinal()];
    }
    
    void reset() {
        this.columnMap = null;
        this.normalizedColumnMap = null;
        this.enumMap = null;
        this.extractedIndexes = null;
    }
}
