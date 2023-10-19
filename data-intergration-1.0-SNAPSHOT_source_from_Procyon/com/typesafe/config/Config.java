// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.List;
import java.time.Duration;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.Set;

public interface Config extends ConfigMergeable
{
    ConfigObject root();
    
    ConfigOrigin origin();
    
    Config withFallback(final ConfigMergeable p0);
    
    Config resolve();
    
    Config resolve(final ConfigResolveOptions p0);
    
    boolean isResolved();
    
    Config resolveWith(final Config p0);
    
    Config resolveWith(final Config p0, final ConfigResolveOptions p1);
    
    void checkValid(final Config p0, final String... p1);
    
    boolean hasPath(final String p0);
    
    boolean hasPathOrNull(final String p0);
    
    boolean isEmpty();
    
    Set<Map.Entry<String, ConfigValue>> entrySet();
    
    boolean getIsNull(final String p0);
    
    boolean getBoolean(final String p0);
    
    Number getNumber(final String p0);
    
    int getInt(final String p0);
    
    long getLong(final String p0);
    
    double getDouble(final String p0);
    
    String getString(final String p0);
    
     <T extends Enum<T>> T getEnum(final Class<T> p0, final String p1);
    
    ConfigObject getObject(final String p0);
    
    Config getConfig(final String p0);
    
    Object getAnyRef(final String p0);
    
    ConfigValue getValue(final String p0);
    
    Long getBytes(final String p0);
    
    ConfigMemorySize getMemorySize(final String p0);
    
    @Deprecated
    Long getMilliseconds(final String p0);
    
    @Deprecated
    Long getNanoseconds(final String p0);
    
    long getDuration(final String p0, final TimeUnit p1);
    
    Duration getDuration(final String p0);
    
    ConfigList getList(final String p0);
    
    List<Boolean> getBooleanList(final String p0);
    
    List<Number> getNumberList(final String p0);
    
    List<Integer> getIntList(final String p0);
    
    List<Long> getLongList(final String p0);
    
    List<Double> getDoubleList(final String p0);
    
    List<String> getStringList(final String p0);
    
     <T extends Enum<T>> List<T> getEnumList(final Class<T> p0, final String p1);
    
    List<? extends ConfigObject> getObjectList(final String p0);
    
    List<? extends Config> getConfigList(final String p0);
    
    List<?> getAnyRefList(final String p0);
    
    List<Long> getBytesList(final String p0);
    
    List<ConfigMemorySize> getMemorySizeList(final String p0);
    
    @Deprecated
    List<Long> getMillisecondsList(final String p0);
    
    @Deprecated
    List<Long> getNanosecondsList(final String p0);
    
    List<Long> getDurationList(final String p0, final TimeUnit p1);
    
    List<Duration> getDurationList(final String p0);
    
    Config withOnlyPath(final String p0);
    
    Config withoutPath(final String p0);
    
    Config atPath(final String p0);
    
    Config atKey(final String p0);
    
    Config withValue(final String p0, final ConfigValue p1);
}
