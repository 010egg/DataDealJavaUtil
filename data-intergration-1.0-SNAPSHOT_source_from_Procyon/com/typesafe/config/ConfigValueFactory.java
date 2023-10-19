// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.Map;
import com.typesafe.config.impl.ConfigImpl;

public final class ConfigValueFactory
{
    private ConfigValueFactory() {
    }
    
    public static ConfigValue fromAnyRef(final Object object, final String originDescription) {
        return ConfigImpl.fromAnyRef(object, originDescription);
    }
    
    public static ConfigObject fromMap(final Map<String, ?> values, final String originDescription) {
        return (ConfigObject)fromAnyRef(values, originDescription);
    }
    
    public static ConfigList fromIterable(final Iterable<?> values, final String originDescription) {
        return (ConfigList)fromAnyRef(values, originDescription);
    }
    
    public static ConfigValue fromAnyRef(final Object object) {
        return fromAnyRef(object, null);
    }
    
    public static ConfigObject fromMap(final Map<String, ?> values) {
        return fromMap(values, null);
    }
    
    public static ConfigList fromIterable(final Iterable<?> values) {
        return fromIterable(values, null);
    }
}
