// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.classic.util;

import java.util.Set;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.spi.MDCAdapter;

public class LogbackMDCAdapter implements MDCAdapter
{
    final ThreadLocal<Map<String, String>> copyOnThreadLocal;
    private static final int WRITE_OPERATION = 1;
    private static final int MAP_COPY_OPERATION = 2;
    final ThreadLocal<Integer> lastOperation;
    
    public LogbackMDCAdapter() {
        this.copyOnThreadLocal = new ThreadLocal<Map<String, String>>();
        this.lastOperation = new ThreadLocal<Integer>();
    }
    
    private Integer getAndSetLastOperation(final int op) {
        final Integer lastOp = this.lastOperation.get();
        this.lastOperation.set(op);
        return lastOp;
    }
    
    private boolean wasLastOpReadOrNull(final Integer lastOp) {
        return lastOp == null || lastOp == 2;
    }
    
    private Map<String, String> duplicateAndInsertNewMap(final Map<String, String> oldMap) {
        final Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        if (oldMap != null) {
            synchronized (oldMap) {
                newMap.putAll(oldMap);
            }
        }
        this.copyOnThreadLocal.set(newMap);
        return newMap;
    }
    
    @Override
    public void put(final String key, final String val) throws IllegalArgumentException {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }
        final Map<String, String> oldMap = this.copyOnThreadLocal.get();
        final Integer lastOp = this.getAndSetLastOperation(1);
        if (this.wasLastOpReadOrNull(lastOp) || oldMap == null) {
            final Map<String, String> newMap = this.duplicateAndInsertNewMap(oldMap);
            newMap.put(key, val);
        }
        else {
            oldMap.put(key, val);
        }
    }
    
    @Override
    public void remove(final String key) {
        if (key == null) {
            return;
        }
        final Map<String, String> oldMap = this.copyOnThreadLocal.get();
        if (oldMap == null) {
            return;
        }
        final Integer lastOp = this.getAndSetLastOperation(1);
        if (this.wasLastOpReadOrNull(lastOp)) {
            final Map<String, String> newMap = this.duplicateAndInsertNewMap(oldMap);
            newMap.remove(key);
        }
        else {
            oldMap.remove(key);
        }
    }
    
    @Override
    public void clear() {
        this.lastOperation.set(1);
        this.copyOnThreadLocal.remove();
    }
    
    @Override
    public String get(final String key) {
        final Map<String, String> map = this.copyOnThreadLocal.get();
        if (map != null && key != null) {
            return map.get(key);
        }
        return null;
    }
    
    public Map<String, String> getPropertyMap() {
        this.lastOperation.set(2);
        return this.copyOnThreadLocal.get();
    }
    
    public Set<String> getKeys() {
        final Map<String, String> map = this.getPropertyMap();
        if (map != null) {
            return map.keySet();
        }
        return null;
    }
    
    @Override
    public Map<String, String> getCopyOfContextMap() {
        final Map<String, String> hashMap = this.copyOnThreadLocal.get();
        if (hashMap == null) {
            return null;
        }
        return new HashMap<String, String>(hashMap);
    }
    
    @Override
    public void setContextMap(final Map<String, String> contextMap) {
        this.lastOperation.set(1);
        final Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        newMap.putAll(contextMap);
        this.copyOnThreadLocal.set(newMap);
    }
}
