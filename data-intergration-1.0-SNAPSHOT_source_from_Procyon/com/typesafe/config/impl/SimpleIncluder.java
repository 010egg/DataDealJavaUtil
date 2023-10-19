// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.List;
import com.typesafe.config.ConfigParseable;
import com.typesafe.config.ConfigOrigin;
import java.util.ArrayList;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigIncluderClasspath;
import com.typesafe.config.ConfigIncluderFile;
import java.io.File;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigIncluderURL;
import java.net.MalformedURLException;
import java.net.URL;
import com.typesafe.config.ConfigMergeable;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigIncludeContext;
import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigIncluder;

class SimpleIncluder implements FullIncluder
{
    private ConfigIncluder fallback;
    
    SimpleIncluder(final ConfigIncluder fallback) {
        this.fallback = fallback;
    }
    
    static ConfigParseOptions clearForInclude(final ConfigParseOptions options) {
        return options.setSyntax(null).setOriginDescription(null).setAllowMissing(true);
    }
    
    @Override
    public ConfigObject include(final ConfigIncludeContext context, final String name) {
        final ConfigObject obj = includeWithoutFallback(context, name);
        if (this.fallback != null) {
            return obj.withFallback((ConfigMergeable)this.fallback.include(context, name));
        }
        return obj;
    }
    
    static ConfigObject includeWithoutFallback(final ConfigIncludeContext context, final String name) {
        URL url;
        try {
            url = new URL(name);
        }
        catch (MalformedURLException e) {
            url = null;
        }
        if (url != null) {
            return includeURLWithoutFallback(context, url);
        }
        final NameSource source = new RelativeNameSource(context);
        return fromBasename(source, name, context.parseOptions());
    }
    
    @Override
    public ConfigObject includeURL(final ConfigIncludeContext context, final URL url) {
        final ConfigObject obj = includeURLWithoutFallback(context, url);
        if (this.fallback != null && this.fallback instanceof ConfigIncluderURL) {
            return obj.withFallback((ConfigMergeable)((ConfigIncluderURL)this.fallback).includeURL(context, url));
        }
        return obj;
    }
    
    static ConfigObject includeURLWithoutFallback(final ConfigIncludeContext context, final URL url) {
        return ConfigFactory.parseURL(url, context.parseOptions()).root();
    }
    
    @Override
    public ConfigObject includeFile(final ConfigIncludeContext context, final File file) {
        final ConfigObject obj = includeFileWithoutFallback(context, file);
        if (this.fallback != null && this.fallback instanceof ConfigIncluderFile) {
            return obj.withFallback((ConfigMergeable)((ConfigIncluderFile)this.fallback).includeFile(context, file));
        }
        return obj;
    }
    
    static ConfigObject includeFileWithoutFallback(final ConfigIncludeContext context, final File file) {
        return ConfigFactory.parseFileAnySyntax(file, context.parseOptions()).root();
    }
    
    @Override
    public ConfigObject includeResources(final ConfigIncludeContext context, final String resource) {
        final ConfigObject obj = includeResourceWithoutFallback(context, resource);
        if (this.fallback != null && this.fallback instanceof ConfigIncluderClasspath) {
            return obj.withFallback((ConfigMergeable)((ConfigIncluderClasspath)this.fallback).includeResources(context, resource));
        }
        return obj;
    }
    
    static ConfigObject includeResourceWithoutFallback(final ConfigIncludeContext context, final String resource) {
        return ConfigFactory.parseResourcesAnySyntax(resource, context.parseOptions()).root();
    }
    
    @Override
    public ConfigIncluder withFallback(final ConfigIncluder fallback) {
        if (this == fallback) {
            throw new ConfigException.BugOrBroken("trying to create includer cycle");
        }
        if (this.fallback == fallback) {
            return this;
        }
        if (this.fallback != null) {
            return new SimpleIncluder(this.fallback.withFallback(fallback));
        }
        return new SimpleIncluder(fallback);
    }
    
    static ConfigObject fromBasename(final NameSource source, final String name, final ConfigParseOptions options) {
        ConfigObject obj;
        if (name.endsWith(".conf") || name.endsWith(".json") || name.endsWith(".properties")) {
            final ConfigParseable p = source.nameToParseable(name, options);
            obj = p.parse(p.options().setAllowMissing(options.getAllowMissing()));
        }
        else {
            final ConfigParseable confHandle = source.nameToParseable(name + ".conf", options);
            final ConfigParseable jsonHandle = source.nameToParseable(name + ".json", options);
            final ConfigParseable propsHandle = source.nameToParseable(name + ".properties", options);
            boolean gotSomething = false;
            final List<ConfigException.IO> fails = new ArrayList<ConfigException.IO>();
            final ConfigSyntax syntax = options.getSyntax();
            obj = SimpleConfigObject.empty(SimpleConfigOrigin.newSimple(name));
            Label_0228: {
                if (syntax != null) {
                    if (syntax != ConfigSyntax.CONF) {
                        break Label_0228;
                    }
                }
                try {
                    obj = confHandle.parse(confHandle.options().setAllowMissing(false).setSyntax(ConfigSyntax.CONF));
                    gotSomething = true;
                }
                catch (ConfigException.IO e) {
                    fails.add(e);
                }
            }
            Label_0294: {
                if (syntax != null) {
                    if (syntax != ConfigSyntax.JSON) {
                        break Label_0294;
                    }
                }
                try {
                    final ConfigObject parsed = jsonHandle.parse(jsonHandle.options().setAllowMissing(false).setSyntax(ConfigSyntax.JSON));
                    obj = obj.withFallback((ConfigMergeable)parsed);
                    gotSomething = true;
                }
                catch (ConfigException.IO e) {
                    fails.add(e);
                }
            }
            Label_0360: {
                if (syntax != null) {
                    if (syntax != ConfigSyntax.PROPERTIES) {
                        break Label_0360;
                    }
                }
                try {
                    final ConfigObject parsed = propsHandle.parse(propsHandle.options().setAllowMissing(false).setSyntax(ConfigSyntax.PROPERTIES));
                    obj = obj.withFallback((ConfigMergeable)parsed);
                    gotSomething = true;
                }
                catch (ConfigException.IO e) {
                    fails.add(e);
                }
            }
            if (!options.getAllowMissing() && !gotSomething) {
                if (ConfigImpl.traceLoadsEnabled()) {
                    ConfigImpl.trace("Did not find '" + name + "' with any extension (.conf, .json, .properties); exceptions should have been logged above.");
                }
                if (fails.isEmpty()) {
                    throw new ConfigException.BugOrBroken("should not be reached: nothing found but no exceptions thrown");
                }
                final StringBuilder sb = new StringBuilder();
                for (final Throwable t : fails) {
                    sb.append(t.getMessage());
                    sb.append(", ");
                }
                sb.setLength(sb.length() - 2);
                throw new ConfigException.IO(SimpleConfigOrigin.newSimple(name), sb.toString(), fails.get(0));
            }
            else if (!gotSomething && ConfigImpl.traceLoadsEnabled()) {
                ConfigImpl.trace("Did not find '" + name + "' with any extension (.conf, .json, .properties); but '" + name + "' is allowed to be missing. Exceptions from load attempts should have been logged above.");
            }
        }
        return obj;
    }
    
    static FullIncluder makeFull(final ConfigIncluder includer) {
        if (includer instanceof FullIncluder) {
            return (FullIncluder)includer;
        }
        return new Proxy(includer);
    }
    
    private static class RelativeNameSource implements NameSource
    {
        private final ConfigIncludeContext context;
        
        RelativeNameSource(final ConfigIncludeContext context) {
            this.context = context;
        }
        
        @Override
        public ConfigParseable nameToParseable(final String name, final ConfigParseOptions options) {
            final ConfigParseable p = this.context.relativeTo(name);
            if (p == null) {
                return Parseable.newNotFound(name, "include was not found: '" + name + "'", options);
            }
            return p;
        }
    }
    
    private static class Proxy implements FullIncluder
    {
        final ConfigIncluder delegate;
        
        Proxy(final ConfigIncluder delegate) {
            this.delegate = delegate;
        }
        
        @Override
        public ConfigIncluder withFallback(final ConfigIncluder fallback) {
            return this;
        }
        
        @Override
        public ConfigObject include(final ConfigIncludeContext context, final String what) {
            return this.delegate.include(context, what);
        }
        
        @Override
        public ConfigObject includeResources(final ConfigIncludeContext context, final String what) {
            if (this.delegate instanceof ConfigIncluderClasspath) {
                return ((ConfigIncluderClasspath)this.delegate).includeResources(context, what);
            }
            return SimpleIncluder.includeResourceWithoutFallback(context, what);
        }
        
        @Override
        public ConfigObject includeURL(final ConfigIncludeContext context, final URL what) {
            if (this.delegate instanceof ConfigIncluderURL) {
                return ((ConfigIncluderURL)this.delegate).includeURL(context, what);
            }
            return SimpleIncluder.includeURLWithoutFallback(context, what);
        }
        
        @Override
        public ConfigObject includeFile(final ConfigIncludeContext context, final File what) {
            if (this.delegate instanceof ConfigIncluderFile) {
                return ((ConfigIncluderFile)this.delegate).includeFile(context, what);
            }
            return SimpleIncluder.includeFileWithoutFallback(context, what);
        }
    }
    
    interface NameSource
    {
        ConfigParseable nameToParseable(final String p0, final ConfigParseOptions p1);
    }
}
