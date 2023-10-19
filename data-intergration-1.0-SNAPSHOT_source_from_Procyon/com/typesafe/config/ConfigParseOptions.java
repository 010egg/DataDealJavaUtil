// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

public final class ConfigParseOptions
{
    final ConfigSyntax syntax;
    final String originDescription;
    final boolean allowMissing;
    final ConfigIncluder includer;
    final ClassLoader classLoader;
    
    private ConfigParseOptions(final ConfigSyntax syntax, final String originDescription, final boolean allowMissing, final ConfigIncluder includer, final ClassLoader classLoader) {
        this.syntax = syntax;
        this.originDescription = originDescription;
        this.allowMissing = allowMissing;
        this.includer = includer;
        this.classLoader = classLoader;
    }
    
    public static ConfigParseOptions defaults() {
        return new ConfigParseOptions(null, null, true, null, null);
    }
    
    public ConfigParseOptions setSyntax(final ConfigSyntax syntax) {
        if (this.syntax == syntax) {
            return this;
        }
        return new ConfigParseOptions(syntax, this.originDescription, this.allowMissing, this.includer, this.classLoader);
    }
    
    public ConfigSyntax getSyntax() {
        return this.syntax;
    }
    
    public ConfigParseOptions setOriginDescription(final String originDescription) {
        if (this.originDescription == originDescription) {
            return this;
        }
        if (this.originDescription != null && originDescription != null && this.originDescription.equals(originDescription)) {
            return this;
        }
        return new ConfigParseOptions(this.syntax, originDescription, this.allowMissing, this.includer, this.classLoader);
    }
    
    public String getOriginDescription() {
        return this.originDescription;
    }
    
    ConfigParseOptions withFallbackOriginDescription(final String originDescription) {
        if (this.originDescription == null) {
            return this.setOriginDescription(originDescription);
        }
        return this;
    }
    
    public ConfigParseOptions setAllowMissing(final boolean allowMissing) {
        if (this.allowMissing == allowMissing) {
            return this;
        }
        return new ConfigParseOptions(this.syntax, this.originDescription, allowMissing, this.includer, this.classLoader);
    }
    
    public boolean getAllowMissing() {
        return this.allowMissing;
    }
    
    public ConfigParseOptions setIncluder(final ConfigIncluder includer) {
        if (this.includer == includer) {
            return this;
        }
        return new ConfigParseOptions(this.syntax, this.originDescription, this.allowMissing, includer, this.classLoader);
    }
    
    public ConfigParseOptions prependIncluder(final ConfigIncluder includer) {
        if (includer == null) {
            throw new NullPointerException("null includer passed to prependIncluder");
        }
        if (this.includer == includer) {
            return this;
        }
        if (this.includer != null) {
            return this.setIncluder(includer.withFallback(this.includer));
        }
        return this.setIncluder(includer);
    }
    
    public ConfigParseOptions appendIncluder(final ConfigIncluder includer) {
        if (includer == null) {
            throw new NullPointerException("null includer passed to appendIncluder");
        }
        if (this.includer == includer) {
            return this;
        }
        if (this.includer != null) {
            return this.setIncluder(this.includer.withFallback(includer));
        }
        return this.setIncluder(includer);
    }
    
    public ConfigIncluder getIncluder() {
        return this.includer;
    }
    
    public ConfigParseOptions setClassLoader(final ClassLoader loader) {
        if (this.classLoader == loader) {
            return this;
        }
        return new ConfigParseOptions(this.syntax, this.originDescription, this.allowMissing, this.includer, loader);
    }
    
    public ClassLoader getClassLoader() {
        if (this.classLoader == null) {
            return Thread.currentThread().getContextClassLoader();
        }
        return this.classLoader;
    }
}
