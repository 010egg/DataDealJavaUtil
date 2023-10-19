// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.impl;

import org.apache.logging.log4j.spi.ThreadContextMap;
import org.apache.logging.log4j.ThreadContextAccess;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import java.util.Map;
import java.util.HashMap;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.ContextDataInjector;
import org.apache.logging.log4j.util.StringMap;
import org.apache.logging.log4j.core.config.Property;
import java.util.List;

public class ThreadContextDataInjector
{
    public static void copyProperties(final List<Property> properties, final StringMap result) {
        if (properties != null) {
            for (int i = 0; i < properties.size(); ++i) {
                final Property prop = properties.get(i);
                result.putValue(prop.getName(), prop.getValue());
            }
        }
    }
    
    public static class ForDefaultThreadContextMap implements ContextDataInjector
    {
        private static final StringMap EMPTY_STRING_MAP;
        
        @Override
        public StringMap injectContextData(final List<Property> props, final StringMap ignore) {
            final Map<String, String> copy = ThreadContext.getImmutableContext();
            if (props == null || props.isEmpty()) {
                return copy.isEmpty() ? ForDefaultThreadContextMap.EMPTY_STRING_MAP : frozenStringMap(copy);
            }
            final StringMap result = new JdkMapAdapterStringMap(new HashMap<String, String>(copy));
            for (int i = 0; i < props.size(); ++i) {
                final Property prop = props.get(i);
                if (!copy.containsKey(prop.getName())) {
                    result.putValue(prop.getName(), prop.getValue());
                }
            }
            result.freeze();
            return result;
        }
        
        private static JdkMapAdapterStringMap frozenStringMap(final Map<String, String> copy) {
            final JdkMapAdapterStringMap result = new JdkMapAdapterStringMap(copy);
            result.freeze();
            return result;
        }
        
        @Override
        public ReadOnlyStringMap rawContextData() {
            final ThreadContextMap map = ThreadContextAccess.getThreadContextMap();
            if (map instanceof ReadOnlyStringMap) {
                return (ReadOnlyStringMap)map;
            }
            return map.isEmpty() ? ForDefaultThreadContextMap.EMPTY_STRING_MAP : new JdkMapAdapterStringMap(map.getImmutableMapOrNull());
        }
        
        static {
            (EMPTY_STRING_MAP = ContextDataFactory.createContextData()).freeze();
        }
    }
    
    public static class ForGarbageFreeThreadContextMap implements ContextDataInjector
    {
        @Override
        public StringMap injectContextData(final List<Property> props, final StringMap reusable) {
            ThreadContextDataInjector.copyProperties(props, reusable);
            final ReadOnlyStringMap immutableCopy = ThreadContextAccess.getThreadContextMap2().getReadOnlyContextData();
            reusable.putAll(immutableCopy);
            return reusable;
        }
        
        @Override
        public ReadOnlyStringMap rawContextData() {
            return ThreadContextAccess.getThreadContextMap2().getReadOnlyContextData();
        }
    }
    
    public static class ForCopyOnWriteThreadContextMap implements ContextDataInjector
    {
        @Override
        public StringMap injectContextData(final List<Property> props, final StringMap ignore) {
            final StringMap immutableCopy = ThreadContextAccess.getThreadContextMap2().getReadOnlyContextData();
            if (props == null || props.isEmpty()) {
                return immutableCopy;
            }
            final StringMap result = ContextDataFactory.createContextData(props.size() + immutableCopy.size());
            ThreadContextDataInjector.copyProperties(props, result);
            result.putAll(immutableCopy);
            return result;
        }
        
        @Override
        public ReadOnlyStringMap rawContextData() {
            return ThreadContextAccess.getThreadContextMap2().getReadOnlyContextData();
        }
    }
}
