// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigSyntax;
import com.typesafe.config.ConfigParseable;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigIncludeContext;

class SimpleIncludeContext implements ConfigIncludeContext
{
    private final Parseable parseable;
    private final ConfigParseOptions options;
    
    SimpleIncludeContext(final Parseable parseable) {
        this.parseable = parseable;
        this.options = SimpleIncluder.clearForInclude(parseable.options());
    }
    
    private SimpleIncludeContext(final Parseable parseable, final ConfigParseOptions options) {
        this.parseable = parseable;
        this.options = options;
    }
    
    SimpleIncludeContext withParseable(final Parseable parseable) {
        if (parseable == this.parseable) {
            return this;
        }
        return new SimpleIncludeContext(parseable);
    }
    
    @Override
    public ConfigParseable relativeTo(final String filename) {
        if (ConfigImpl.traceLoadsEnabled()) {
            ConfigImpl.trace("Looking for '" + filename + "' relative to " + this.parseable);
        }
        if (this.parseable != null) {
            return this.parseable.relativeTo(filename);
        }
        return null;
    }
    
    @Override
    public ConfigParseOptions parseOptions() {
        return this.options;
    }
    
    @Override
    public ConfigIncludeContext setParseOptions(final ConfigParseOptions options) {
        return new SimpleIncludeContext(this.parseable, options.setSyntax(null).setOriginDescription(null));
    }
}
