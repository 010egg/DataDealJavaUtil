// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.spi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.logging.log4j.util.SortedArrayStringMap;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.PropertiesUtil;
import org.apache.logging.log4j.util.StringMap;

class GarbageFreeSortedArrayThreadContextMap implements ThreadContextMap2
{
    public static final String INHERITABLE_MAP = "isThreadContextMapInheritable";
    protected static final int DEFAULT_INITIAL_CAPACITY = 16;
    protected static final String PROPERTY_NAME_INITIAL_CAPACITY = "log4j2.ThreadContext.initial.capacity";
    protected final ThreadLocal<StringMap> localMap;
    
    public GarbageFreeSortedArrayThreadContextMap() {
        this.localMap = this.createThreadLocalMap();
    }
    
    private ThreadLocal<StringMap> createThreadLocalMap() {
        final PropertiesUtil managerProps = PropertiesUtil.getProperties();
        final boolean inheritable = managerProps.getBooleanProperty("isThreadContextMapInheritable");
        if (inheritable) {
            return new InheritableThreadLocal<StringMap>() {
                @Override
                protected StringMap childValue(final StringMap parentValue) {
                    return (parentValue != null) ? GarbageFreeSortedArrayThreadContextMap.this.createStringMap(parentValue) : null;
                }
            };
        }
        return new ThreadLocal<StringMap>();
    }
    
    protected StringMap createStringMap() {
        return new SortedArrayStringMap(PropertiesUtil.getProperties().getIntegerProperty("log4j2.ThreadContext.initial.capacity", 16));
    }
    
    protected StringMap createStringMap(final ReadOnlyStringMap original) {
        return new SortedArrayStringMap(original);
    }
    
    private StringMap getThreadLocalMap() {
        StringMap map = this.localMap.get();
        if (map == null) {
            map = this.createStringMap();
            this.localMap.set(map);
        }
        return map;
    }
    
    @Override
    public void put(final String key, final String value) {
        this.getThreadLocalMap().putValue(key, value);
    }
    
    @Override
    public void putAll(final Map<String, String> values) {
        if (values == null || values.isEmpty()) {
            return;
        }
        final StringMap map = this.getThreadLocalMap();
        for (final Map.Entry<String, String> entry : values.entrySet()) {
            map.putValue(entry.getKey(), entry.getValue());
        }
    }
    
    @Override
    public String get(final String key) {
        final StringMap map = this.localMap.get();
        return (map == null) ? null : map.getValue(key);
    }
    
    @Override
    public void remove(final String key) {
        final StringMap map = this.localMap.get();
        if (map != null) {
            map.remove(key);
        }
    }
    
    @Override
    public void clear() {
        final StringMap map = this.localMap.get();
        if (map != null) {
            map.clear();
        }
    }
    
    @Override
    public boolean containsKey(final String key) {
        final StringMap map = this.localMap.get();
        return map != null && map.containsKey(key);
    }
    
    @Override
    public Map<String, String> getCopy() {
        final StringMap map = this.localMap.get();
        return (map == null) ? new HashMap<String, String>() : map.toMap();
    }
    
    @Override
    public StringMap getReadOnlyContextData() {
        StringMap map = this.localMap.get();
        if (map == null) {
            map = this.createStringMap();
            this.localMap.set(map);
        }
        return map;
    }
    
    @Override
    public Map<String, String> getImmutableMapOrNull() {
        final StringMap map = this.localMap.get();
        return (map == null) ? null : Collections.unmodifiableMap((Map<? extends String, ? extends String>)map.toMap());
    }
    
    @Override
    public boolean isEmpty() {
        final StringMap map = this.localMap.get();
        return map == null || map.size() == 0;
    }
    
    @Override
    public String toString() {
        final StringMap map = this.localMap.get();
        return (map == null) ? "{}" : map.toString();
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final StringMap map = this.localMap.get();
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other = (ThreadContextMap)obj;
        final Map<String, String> map = this.getImmutableMapOrNull();
        final Map<String, String> otherMap = other.getImmutableMapOrNull();
        if (map == null) {
            if (otherMap != null) {
                return false;
            }
        }
        else if (!map.equals(otherMap)) {
            return false;
        }
        return true;
    }
}
