// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.lang.reflect.Array;
import java.util.Date;
import java.text.Collator;
import java.util.Map;
import java.util.Comparator;

public class MapComparator<K, V> implements Comparator<Map<K, V>>
{
    private boolean isDesc;
    private K orderByKey;
    
    public MapComparator(final K orderByKey, final boolean isDesc) {
        this.orderByKey = orderByKey;
        this.isDesc = isDesc;
    }
    
    private int compare(final Number o1, final Number o2) {
        return (int)(o1.doubleValue() - o2.doubleValue());
    }
    
    private int compare(final String o1, final String o2) {
        return Collator.getInstance().compare(o1, o2);
    }
    
    private int compare(final Date o1, final Date o2) {
        return (int)(o1.getTime() - o2.getTime());
    }
    
    @Override
    public int compare(final Map<K, V> o1, final Map<K, V> o2) {
        int result = this.compare_0(o1, o2);
        if (this.isDesc) {
            result = -result;
        }
        return result;
    }
    
    private Object getValueByKey(final Map<K, V> map, final K key) {
        if (key instanceof String) {
            final String keyStr = (String)key;
            final int bracketIndex = keyStr.indexOf(91);
            if (bracketIndex > 0) {
                final Object value = map.get(keyStr.substring(0, bracketIndex));
                if (value == null) {
                    return null;
                }
                final int p2 = keyStr.indexOf(93, bracketIndex);
                if (p2 == -1) {
                    return null;
                }
                final String indexText = keyStr.substring(bracketIndex + 1, p2);
                final int index = Integer.parseInt(indexText);
                if (value.getClass().isArray() && Array.getLength(value) >= index) {
                    return Array.get(value, index);
                }
                return null;
            }
        }
        return map.get(key);
    }
    
    private int compare_0(final Map<K, V> o1, final Map<K, V> o2) {
        final Object v1 = this.getValueByKey(o1, this.orderByKey);
        final Object v2 = this.getValueByKey(o2, this.orderByKey);
        if (v1 == null && v2 == null) {
            return 0;
        }
        if (v1 == null) {
            return -1;
        }
        if (v2 == null) {
            return 1;
        }
        if (v1 instanceof Long) {
            return (int)((long)v1 - ((Number)v2).longValue());
        }
        if (v1 instanceof Number) {
            return this.compare((Number)v1, (Number)v2);
        }
        if (v1 instanceof String) {
            return this.compare((String)v1, (String)v2);
        }
        if (v1 instanceof Date) {
            return this.compare((Date)v1, (Date)v2);
        }
        return 0;
    }
}
