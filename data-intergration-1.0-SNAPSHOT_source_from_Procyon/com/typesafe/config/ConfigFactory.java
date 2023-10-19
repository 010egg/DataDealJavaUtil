// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.Map;
import java.io.File;
import java.net.URL;
import java.io.Reader;
import com.typesafe.config.impl.Parseable;
import java.util.Properties;
import com.typesafe.config.impl.ConfigImpl;
import java.util.concurrent.Callable;

public final class ConfigFactory
{
    private static final String STRATEGY_PROPERTY_NAME = "config.strategy";
    
    private ConfigFactory() {
    }
    
    public static Config load(final String resourceBasename) {
        return load(resourceBasename, ConfigParseOptions.defaults(), ConfigResolveOptions.defaults());
    }
    
    public static Config load(final ClassLoader loader, final String resourceBasename) {
        return load(resourceBasename, ConfigParseOptions.defaults().setClassLoader(loader), ConfigResolveOptions.defaults());
    }
    
    public static Config load(final String resourceBasename, final ConfigParseOptions parseOptions, final ConfigResolveOptions resolveOptions) {
        final ConfigParseOptions withLoader = ensureClassLoader(parseOptions, "load");
        final Config appConfig = parseResourcesAnySyntax(resourceBasename, withLoader);
        return load(withLoader.getClassLoader(), appConfig, resolveOptions);
    }
    
    public static Config load(final ClassLoader loader, final String resourceBasename, final ConfigParseOptions parseOptions, final ConfigResolveOptions resolveOptions) {
        return load(resourceBasename, parseOptions.setClassLoader(loader), resolveOptions);
    }
    
    private static ClassLoader checkedContextClassLoader(final String methodName) {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            throw new ConfigException.BugOrBroken("Context class loader is not set for the current thread; if Thread.currentThread().getContextClassLoader() returns null, you must pass a ClassLoader explicitly to ConfigFactory." + methodName);
        }
        return loader;
    }
    
    private static ConfigParseOptions ensureClassLoader(final ConfigParseOptions options, final String methodName) {
        if (options.getClassLoader() == null) {
            return options.setClassLoader(checkedContextClassLoader(methodName));
        }
        return options;
    }
    
    public static Config load(final Config config) {
        return load(checkedContextClassLoader("load"), config);
    }
    
    public static Config load(final ClassLoader loader, final Config config) {
        return load(loader, config, ConfigResolveOptions.defaults());
    }
    
    public static Config load(final Config config, final ConfigResolveOptions resolveOptions) {
        return load(checkedContextClassLoader("load"), config, resolveOptions);
    }
    
    public static Config load(final ClassLoader loader, final Config config, final ConfigResolveOptions resolveOptions) {
        return defaultOverrides(loader).withFallback((ConfigMergeable)config).withFallback((ConfigMergeable)defaultReference(loader)).resolve(resolveOptions);
    }
    
    public static Config load() {
        final ClassLoader loader = checkedContextClassLoader("load");
        return load(loader);
    }
    
    public static Config load(final ConfigParseOptions parseOptions) {
        return load(parseOptions, ConfigResolveOptions.defaults());
    }
    
    public static Config load(final ClassLoader loader) {
        final ConfigParseOptions withLoader = ConfigParseOptions.defaults().setClassLoader(loader);
        return ConfigImpl.computeCachedConfig(loader, "load", new Callable<Config>() {
            @Override
            public Config call() {
                return ConfigFactory.load(loader, ConfigFactory.defaultApplication(withLoader));
            }
        });
    }
    
    public static Config load(final ClassLoader loader, final ConfigParseOptions parseOptions) {
        return load(parseOptions.setClassLoader(loader));
    }
    
    public static Config load(final ClassLoader loader, final ConfigResolveOptions resolveOptions) {
        return load(loader, ConfigParseOptions.defaults(), resolveOptions);
    }
    
    public static Config load(final ClassLoader loader, final ConfigParseOptions parseOptions, final ConfigResolveOptions resolveOptions) {
        final ConfigParseOptions withLoader = ensureClassLoader(parseOptions, "load");
        return load(loader, defaultApplication(withLoader), resolveOptions);
    }
    
    public static Config load(final ConfigParseOptions parseOptions, final ConfigResolveOptions resolveOptions) {
        final ConfigParseOptions withLoader = ensureClassLoader(parseOptions, "load");
        return load(defaultApplication(withLoader), resolveOptions);
    }
    
    public static Config defaultReference() {
        return defaultReference(checkedContextClassLoader("defaultReference"));
    }
    
    public static Config defaultReference(final ClassLoader loader) {
        return ConfigImpl.defaultReference(loader);
    }
    
    public static Config defaultOverrides() {
        return systemProperties();
    }
    
    public static Config defaultOverrides(final ClassLoader loader) {
        return systemProperties();
    }
    
    public static Config defaultApplication() {
        return defaultApplication(ConfigParseOptions.defaults());
    }
    
    public static Config defaultApplication(final ClassLoader loader) {
        return defaultApplication(ConfigParseOptions.defaults().setClassLoader(loader));
    }
    
    public static Config defaultApplication(final ConfigParseOptions options) {
        return getConfigLoadingStrategy().parseApplicationConfig(ensureClassLoader(options, "defaultApplication"));
    }
    
    public static void invalidateCaches() {
        ConfigImpl.reloadSystemPropertiesConfig();
    }
    
    public static Config empty() {
        return empty(null);
    }
    
    public static Config empty(final String originDescription) {
        return ConfigImpl.emptyConfig(originDescription);
    }
    
    public static Config systemProperties() {
        return ConfigImpl.systemPropertiesAsConfig();
    }
    
    public static Config systemEnvironment() {
        return ConfigImpl.envVariablesAsConfig();
    }
    
    public static Config parseProperties(final Properties properties, final ConfigParseOptions options) {
        return Parseable.newProperties(properties, options).parse().toConfig();
    }
    
    public static Config parseProperties(final Properties properties) {
        return parseProperties(properties, ConfigParseOptions.defaults());
    }
    
    public static Config parseReader(final Reader reader, final ConfigParseOptions options) {
        return Parseable.newReader(reader, options).parse().toConfig();
    }
    
    public static Config parseReader(final Reader reader) {
        return parseReader(reader, ConfigParseOptions.defaults());
    }
    
    public static Config parseURL(final URL url, final ConfigParseOptions options) {
        return Parseable.newURL(url, options).parse().toConfig();
    }
    
    public static Config parseURL(final URL url) {
        return parseURL(url, ConfigParseOptions.defaults());
    }
    
    public static Config parseFile(final File file, final ConfigParseOptions options) {
        return Parseable.newFile(file, options).parse().toConfig();
    }
    
    public static Config parseFile(final File file) {
        return parseFile(file, ConfigParseOptions.defaults());
    }
    
    public static Config parseFileAnySyntax(final File fileBasename, final ConfigParseOptions options) {
        return ConfigImpl.parseFileAnySyntax(fileBasename, options).toConfig();
    }
    
    public static Config parseFileAnySyntax(final File fileBasename) {
        return parseFileAnySyntax(fileBasename, ConfigParseOptions.defaults());
    }
    
    public static Config parseResources(final Class<?> klass, final String resource, final ConfigParseOptions options) {
        return Parseable.newResources(klass, resource, options).parse().toConfig();
    }
    
    public static Config parseResources(final Class<?> klass, final String resource) {
        return parseResources(klass, resource, ConfigParseOptions.defaults());
    }
    
    public static Config parseResourcesAnySyntax(final Class<?> klass, final String resourceBasename, final ConfigParseOptions options) {
        return ConfigImpl.parseResourcesAnySyntax(klass, resourceBasename, options).toConfig();
    }
    
    public static Config parseResourcesAnySyntax(final Class<?> klass, final String resourceBasename) {
        return parseResourcesAnySyntax(klass, resourceBasename, ConfigParseOptions.defaults());
    }
    
    public static Config parseResources(final ClassLoader loader, final String resource, final ConfigParseOptions options) {
        return parseResources(resource, options.setClassLoader(loader));
    }
    
    public static Config parseResources(final ClassLoader loader, final String resource) {
        return parseResources(loader, resource, ConfigParseOptions.defaults());
    }
    
    public static Config parseResourcesAnySyntax(final ClassLoader loader, final String resourceBasename, final ConfigParseOptions options) {
        return ConfigImpl.parseResourcesAnySyntax(resourceBasename, options.setClassLoader(loader)).toConfig();
    }
    
    public static Config parseResourcesAnySyntax(final ClassLoader loader, final String resourceBasename) {
        return parseResourcesAnySyntax(loader, resourceBasename, ConfigParseOptions.defaults());
    }
    
    public static Config parseResources(final String resource, final ConfigParseOptions options) {
        final ConfigParseOptions withLoader = ensureClassLoader(options, "parseResources");
        return Parseable.newResources(resource, withLoader).parse().toConfig();
    }
    
    public static Config parseResources(final String resource) {
        return parseResources(resource, ConfigParseOptions.defaults());
    }
    
    public static Config parseResourcesAnySyntax(final String resourceBasename, final ConfigParseOptions options) {
        return ConfigImpl.parseResourcesAnySyntax(resourceBasename, options).toConfig();
    }
    
    public static Config parseResourcesAnySyntax(final String resourceBasename) {
        return parseResourcesAnySyntax(resourceBasename, ConfigParseOptions.defaults());
    }
    
    public static Config parseString(final String s, final ConfigParseOptions options) {
        return Parseable.newString(s, options).parse().toConfig();
    }
    
    public static Config parseString(final String s) {
        return parseString(s, ConfigParseOptions.defaults());
    }
    
    public static Config parseMap(final Map<String, ?> values, final String originDescription) {
        return ConfigImpl.fromPathMap(values, originDescription).toConfig();
    }
    
    public static Config parseMap(final Map<String, ?> values) {
        return parseMap(values, null);
    }
    
    private static ConfigLoadingStrategy getConfigLoadingStrategy() {
        final String className = System.getProperties().getProperty("config.strategy");
        if (className != null) {
            try {
                return ConfigLoadingStrategy.class.cast(Class.forName(className).newInstance());
            }
            catch (Throwable e) {
                throw new ConfigException.BugOrBroken("Failed to load strategy: " + className, e);
            }
        }
        return new DefaultConfigLoadingStrategy();
    }
}
