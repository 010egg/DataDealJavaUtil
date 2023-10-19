// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigParseable;
import java.lang.ref.WeakReference;
import java.net.URL;
import com.typesafe.config.ConfigMergeable;
import java.util.Properties;
import com.typesafe.config.ConfigIncluder;
import java.util.List;
import java.util.Iterator;
import com.typesafe.config.ConfigMemorySize;
import java.util.ArrayList;
import java.util.HashMap;
import java.time.Duration;
import com.typesafe.config.ConfigException;
import java.util.Map;
import com.typesafe.config.ConfigValue;
import java.util.Collections;
import java.io.File;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.Config;
import java.util.concurrent.Callable;
import com.typesafe.config.ConfigOrigin;

public class ConfigImpl
{
    private static final ConfigOrigin defaultValueOrigin;
    private static final ConfigBoolean defaultTrueValue;
    private static final ConfigBoolean defaultFalseValue;
    private static final ConfigNull defaultNullValue;
    private static final SimpleConfigList defaultEmptyList;
    private static final SimpleConfigObject defaultEmptyObject;
    
    public static Config computeCachedConfig(final ClassLoader loader, final String key, final Callable<Config> updater) {
        LoaderCache cache;
        try {
            cache = LoaderCacheHolder.cache;
        }
        catch (ExceptionInInitializerError e) {
            throw ConfigImplUtil.extractInitializerError(e);
        }
        return cache.getOrElseUpdate(loader, key, updater);
    }
    
    public static ConfigObject parseResourcesAnySyntax(final Class<?> klass, final String resourceBasename, final ConfigParseOptions baseOptions) {
        final SimpleIncluder.NameSource source = new ClasspathNameSourceWithClass(klass);
        return SimpleIncluder.fromBasename(source, resourceBasename, baseOptions);
    }
    
    public static ConfigObject parseResourcesAnySyntax(final String resourceBasename, final ConfigParseOptions baseOptions) {
        final SimpleIncluder.NameSource source = new ClasspathNameSource();
        return SimpleIncluder.fromBasename(source, resourceBasename, baseOptions);
    }
    
    public static ConfigObject parseFileAnySyntax(final File basename, final ConfigParseOptions baseOptions) {
        final SimpleIncluder.NameSource source = new FileNameSource();
        return SimpleIncluder.fromBasename(source, basename.getPath(), baseOptions);
    }
    
    static AbstractConfigObject emptyObject(final String originDescription) {
        final ConfigOrigin origin = (originDescription != null) ? SimpleConfigOrigin.newSimple(originDescription) : null;
        return emptyObject(origin);
    }
    
    public static Config emptyConfig(final String originDescription) {
        return emptyObject(originDescription).toConfig();
    }
    
    static AbstractConfigObject empty(final ConfigOrigin origin) {
        return emptyObject(origin);
    }
    
    private static SimpleConfigList emptyList(final ConfigOrigin origin) {
        if (origin == null || origin == ConfigImpl.defaultValueOrigin) {
            return ConfigImpl.defaultEmptyList;
        }
        return new SimpleConfigList(origin, Collections.emptyList());
    }
    
    private static AbstractConfigObject emptyObject(final ConfigOrigin origin) {
        if (origin == ConfigImpl.defaultValueOrigin) {
            return ConfigImpl.defaultEmptyObject;
        }
        return SimpleConfigObject.empty(origin);
    }
    
    private static ConfigOrigin valueOrigin(final String originDescription) {
        if (originDescription == null) {
            return ConfigImpl.defaultValueOrigin;
        }
        return SimpleConfigOrigin.newSimple(originDescription);
    }
    
    public static ConfigValue fromAnyRef(final Object object, final String originDescription) {
        final ConfigOrigin origin = valueOrigin(originDescription);
        return fromAnyRef(object, origin, FromMapMode.KEYS_ARE_KEYS);
    }
    
    public static ConfigObject fromPathMap(final Map<String, ?> pathMap, final String originDescription) {
        final ConfigOrigin origin = valueOrigin(originDescription);
        return (ConfigObject)fromAnyRef(pathMap, origin, FromMapMode.KEYS_ARE_PATHS);
    }
    
    static AbstractConfigValue fromAnyRef(final Object object, final ConfigOrigin origin, final FromMapMode mapMode) {
        if (origin == null) {
            throw new ConfigException.BugOrBroken("origin not supposed to be null");
        }
        if (object == null) {
            if (origin != ConfigImpl.defaultValueOrigin) {
                return new ConfigNull(origin);
            }
            return ConfigImpl.defaultNullValue;
        }
        else {
            if (object instanceof AbstractConfigValue) {
                return (AbstractConfigValue)object;
            }
            if (object instanceof Boolean) {
                if (origin != ConfigImpl.defaultValueOrigin) {
                    return new ConfigBoolean(origin, (boolean)object);
                }
                if (object) {
                    return ConfigImpl.defaultTrueValue;
                }
                return ConfigImpl.defaultFalseValue;
            }
            else {
                if (object instanceof String) {
                    return new ConfigString.Quoted(origin, (String)object);
                }
                if (object instanceof Number) {
                    if (object instanceof Double) {
                        return new ConfigDouble(origin, (double)object, null);
                    }
                    if (object instanceof Integer) {
                        return new ConfigInt(origin, (int)object, null);
                    }
                    if (object instanceof Long) {
                        return new ConfigLong(origin, (long)object, null);
                    }
                    return ConfigNumber.newNumber(origin, ((Number)object).doubleValue(), null);
                }
                else {
                    if (object instanceof Duration) {
                        return new ConfigLong(origin, ((Duration)object).toMillis(), null);
                    }
                    if (object instanceof Map) {
                        if (((Map)object).isEmpty()) {
                            return emptyObject(origin);
                        }
                        if (mapMode == FromMapMode.KEYS_ARE_KEYS) {
                            final Map<String, AbstractConfigValue> values = new HashMap<String, AbstractConfigValue>();
                            for (final Map.Entry<?, ?> entry : ((Map)object).entrySet()) {
                                final Object key = entry.getKey();
                                if (!(key instanceof String)) {
                                    throw new ConfigException.BugOrBroken("bug in method caller: not valid to create ConfigObject from map with non-String key: " + key);
                                }
                                final AbstractConfigValue value = fromAnyRef(entry.getValue(), origin, mapMode);
                                values.put((String)key, value);
                            }
                            return new SimpleConfigObject(origin, values);
                        }
                        return PropertiesParser.fromPathMap(origin, (Map<?, ?>)object);
                    }
                    else if (object instanceof Iterable) {
                        final Iterator<?> i = ((Iterable)object).iterator();
                        if (!i.hasNext()) {
                            return emptyList(origin);
                        }
                        final List<AbstractConfigValue> values2 = new ArrayList<AbstractConfigValue>();
                        while (i.hasNext()) {
                            final AbstractConfigValue v = fromAnyRef(i.next(), origin, mapMode);
                            values2.add(v);
                        }
                        return new SimpleConfigList(origin, values2);
                    }
                    else {
                        if (object instanceof ConfigMemorySize) {
                            return new ConfigLong(origin, ((ConfigMemorySize)object).toBytes(), null);
                        }
                        throw new ConfigException.BugOrBroken("bug in method caller: not valid to create ConfigValue from: " + object);
                    }
                }
            }
        }
    }
    
    static ConfigIncluder defaultIncluder() {
        try {
            return DefaultIncluderHolder.defaultIncluder;
        }
        catch (ExceptionInInitializerError e) {
            throw ConfigImplUtil.extractInitializerError(e);
        }
    }
    
    private static Properties getSystemProperties() {
        final Properties systemProperties = System.getProperties();
        final Properties systemPropertiesCopy = new Properties();
        synchronized (systemProperties) {
            systemPropertiesCopy.putAll(systemProperties);
        }
        return systemPropertiesCopy;
    }
    
    private static AbstractConfigObject loadSystemProperties() {
        return (AbstractConfigObject)Parseable.newProperties(getSystemProperties(), ConfigParseOptions.defaults().setOriginDescription("system properties")).parse();
    }
    
    static AbstractConfigObject systemPropertiesAsConfigObject() {
        try {
            return SystemPropertiesHolder.systemProperties;
        }
        catch (ExceptionInInitializerError e) {
            throw ConfigImplUtil.extractInitializerError(e);
        }
    }
    
    public static Config systemPropertiesAsConfig() {
        return systemPropertiesAsConfigObject().toConfig();
    }
    
    public static void reloadSystemPropertiesConfig() {
        SystemPropertiesHolder.systemProperties = loadSystemProperties();
    }
    
    private static AbstractConfigObject loadEnvVariables() {
        final Map<String, String> env = System.getenv();
        final Map<String, AbstractConfigValue> m = new HashMap<String, AbstractConfigValue>();
        for (final Map.Entry<String, String> entry : env.entrySet()) {
            final String key = entry.getKey();
            m.put(key, new ConfigString.Quoted(SimpleConfigOrigin.newSimple("env var " + key), entry.getValue()));
        }
        return new SimpleConfigObject(SimpleConfigOrigin.newSimple("env variables"), m, ResolveStatus.RESOLVED, false);
    }
    
    static AbstractConfigObject envVariablesAsConfigObject() {
        try {
            return EnvVariablesHolder.envVariables;
        }
        catch (ExceptionInInitializerError e) {
            throw ConfigImplUtil.extractInitializerError(e);
        }
    }
    
    public static Config envVariablesAsConfig() {
        return envVariablesAsConfigObject().toConfig();
    }
    
    public static Config defaultReference(final ClassLoader loader) {
        return computeCachedConfig(loader, "defaultReference", new Callable<Config>() {
            @Override
            public Config call() {
                final Config unresolvedResources = Parseable.newResources("reference.conf", ConfigParseOptions.defaults().setClassLoader(loader)).parse().toConfig();
                return ConfigImpl.systemPropertiesAsConfig().withFallback((ConfigMergeable)unresolvedResources).resolve();
            }
        });
    }
    
    public static boolean traceLoadsEnabled() {
        try {
            return DebugHolder.traceLoadsEnabled();
        }
        catch (ExceptionInInitializerError e) {
            throw ConfigImplUtil.extractInitializerError(e);
        }
    }
    
    public static boolean traceSubstitutionsEnabled() {
        try {
            return DebugHolder.traceSubstitutionsEnabled();
        }
        catch (ExceptionInInitializerError e) {
            throw ConfigImplUtil.extractInitializerError(e);
        }
    }
    
    public static void trace(final String message) {
        System.err.println(message);
    }
    
    public static void trace(int indentLevel, final String message) {
        while (indentLevel > 0) {
            System.err.print("  ");
            --indentLevel;
        }
        System.err.println(message);
    }
    
    static ConfigException.NotResolved improveNotResolved(final Path what, final ConfigException.NotResolved original) {
        final String newMessage = what.render() + " has not been resolved, you need to call Config#resolve(), see API docs for Config#resolve()";
        if (newMessage.equals(original.getMessage())) {
            return original;
        }
        return new ConfigException.NotResolved(newMessage, original);
    }
    
    public static ConfigOrigin newSimpleOrigin(final String description) {
        if (description == null) {
            return ConfigImpl.defaultValueOrigin;
        }
        return SimpleConfigOrigin.newSimple(description);
    }
    
    public static ConfigOrigin newFileOrigin(final String filename) {
        return SimpleConfigOrigin.newFile(filename);
    }
    
    public static ConfigOrigin newURLOrigin(final URL url) {
        return SimpleConfigOrigin.newURL(url);
    }
    
    static {
        defaultValueOrigin = SimpleConfigOrigin.newSimple("hardcoded value");
        defaultTrueValue = new ConfigBoolean(ConfigImpl.defaultValueOrigin, true);
        defaultFalseValue = new ConfigBoolean(ConfigImpl.defaultValueOrigin, false);
        defaultNullValue = new ConfigNull(ConfigImpl.defaultValueOrigin);
        defaultEmptyList = new SimpleConfigList(ConfigImpl.defaultValueOrigin, Collections.emptyList());
        defaultEmptyObject = SimpleConfigObject.empty(ConfigImpl.defaultValueOrigin);
    }
    
    private static class LoaderCache
    {
        private Config currentSystemProperties;
        private WeakReference<ClassLoader> currentLoader;
        private Map<String, Config> cache;
        
        LoaderCache() {
            this.currentSystemProperties = null;
            this.currentLoader = new WeakReference<ClassLoader>(null);
            this.cache = new HashMap<String, Config>();
        }
        
        synchronized Config getOrElseUpdate(final ClassLoader loader, final String key, final Callable<Config> updater) {
            if (loader != this.currentLoader.get()) {
                this.cache.clear();
                this.currentLoader = new WeakReference<ClassLoader>(loader);
            }
            final Config systemProperties = ConfigImpl.systemPropertiesAsConfig();
            if (systemProperties != this.currentSystemProperties) {
                this.cache.clear();
                this.currentSystemProperties = systemProperties;
            }
            Config config = this.cache.get(key);
            if (config == null) {
                try {
                    config = updater.call();
                }
                catch (RuntimeException e) {
                    throw e;
                }
                catch (Exception e2) {
                    throw new ConfigException.Generic(e2.getMessage(), e2);
                }
                if (config == null) {
                    throw new ConfigException.BugOrBroken("null config from cache updater");
                }
                this.cache.put(key, config);
            }
            return config;
        }
    }
    
    private static class LoaderCacheHolder
    {
        static final LoaderCache cache;
        
        static {
            cache = new LoaderCache();
        }
    }
    
    static class FileNameSource implements SimpleIncluder.NameSource
    {
        @Override
        public ConfigParseable nameToParseable(final String name, final ConfigParseOptions parseOptions) {
            return Parseable.newFile(new File(name), parseOptions);
        }
    }
    
    static class ClasspathNameSource implements SimpleIncluder.NameSource
    {
        @Override
        public ConfigParseable nameToParseable(final String name, final ConfigParseOptions parseOptions) {
            return Parseable.newResources(name, parseOptions);
        }
    }
    
    static class ClasspathNameSourceWithClass implements SimpleIncluder.NameSource
    {
        private final Class<?> klass;
        
        public ClasspathNameSourceWithClass(final Class<?> klass) {
            this.klass = klass;
        }
        
        @Override
        public ConfigParseable nameToParseable(final String name, final ConfigParseOptions parseOptions) {
            return Parseable.newResources(this.klass, name, parseOptions);
        }
    }
    
    private static class DefaultIncluderHolder
    {
        static final ConfigIncluder defaultIncluder;
        
        static {
            defaultIncluder = new SimpleIncluder(null);
        }
    }
    
    private static class SystemPropertiesHolder
    {
        static volatile AbstractConfigObject systemProperties;
        
        static {
            SystemPropertiesHolder.systemProperties = loadSystemProperties();
        }
    }
    
    private static class EnvVariablesHolder
    {
        static final AbstractConfigObject envVariables;
        
        static {
            envVariables = loadEnvVariables();
        }
    }
    
    private static class DebugHolder
    {
        private static String LOADS;
        private static String SUBSTITUTIONS;
        private static final Map<String, Boolean> diagnostics;
        private static final boolean traceLoadsEnabled;
        private static final boolean traceSubstitutionsEnabled;
        
        private static Map<String, Boolean> loadDiagnostics() {
            final Map<String, Boolean> result = new HashMap<String, Boolean>();
            result.put(DebugHolder.LOADS, false);
            result.put(DebugHolder.SUBSTITUTIONS, false);
            final String s = System.getProperty("config.trace");
            if (s == null) {
                return result;
            }
            final String[] split;
            final String[] keys = split = s.split(",");
            for (final String k : split) {
                if (k.equals(DebugHolder.LOADS)) {
                    result.put(DebugHolder.LOADS, true);
                }
                else if (k.equals(DebugHolder.SUBSTITUTIONS)) {
                    result.put(DebugHolder.SUBSTITUTIONS, true);
                }
                else {
                    System.err.println("config.trace property contains unknown trace topic '" + k + "'");
                }
            }
            return result;
        }
        
        static boolean traceLoadsEnabled() {
            return DebugHolder.traceLoadsEnabled;
        }
        
        static boolean traceSubstitutionsEnabled() {
            return DebugHolder.traceSubstitutionsEnabled;
        }
        
        static {
            DebugHolder.LOADS = "loads";
            DebugHolder.SUBSTITUTIONS = "substitutions";
            diagnostics = loadDiagnostics();
            traceLoadsEnabled = DebugHolder.diagnostics.get(DebugHolder.LOADS);
            traceSubstitutionsEnabled = DebugHolder.diagnostics.get(DebugHolder.SUBSTITUTIONS);
        }
    }
}
