// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.Config;
import java.util.Map;
import com.typesafe.config.ConfigRenderOptions;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import com.typesafe.config.ConfigMergeable;
import java.util.List;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigObject;

abstract class AbstractConfigObject extends AbstractConfigValue implements ConfigObject, Container
{
    private final SimpleConfig config;
    
    protected AbstractConfigObject(final ConfigOrigin origin) {
        super(origin);
        this.config = new SimpleConfig(this);
    }
    
    @Override
    public SimpleConfig toConfig() {
        return this.config;
    }
    
    @Override
    public AbstractConfigObject toFallbackValue() {
        return this;
    }
    
    @Override
    public abstract AbstractConfigObject withOnlyKey(final String p0);
    
    @Override
    public abstract AbstractConfigObject withoutKey(final String p0);
    
    @Override
    public abstract AbstractConfigObject withValue(final String p0, final ConfigValue p1);
    
    protected abstract AbstractConfigObject withOnlyPathOrNull(final Path p0);
    
    abstract AbstractConfigObject withOnlyPath(final Path p0);
    
    abstract AbstractConfigObject withoutPath(final Path p0);
    
    abstract AbstractConfigObject withValue(final Path p0, final ConfigValue p1);
    
    protected final AbstractConfigValue peekAssumingResolved(final String key, final Path originalPath) {
        try {
            return this.attemptPeekWithPartialResolve(key);
        }
        catch (ConfigException.NotResolved e) {
            throw ConfigImpl.improveNotResolved(originalPath, e);
        }
    }
    
    abstract AbstractConfigValue attemptPeekWithPartialResolve(final String p0);
    
    protected AbstractConfigValue peekPath(final Path path) {
        return peekPath(this, path);
    }
    
    private static AbstractConfigValue peekPath(final AbstractConfigObject self, final Path path) {
        try {
            final Path next = path.remainder();
            final AbstractConfigValue v = self.attemptPeekWithPartialResolve(path.first());
            if (next == null) {
                return v;
            }
            if (v instanceof AbstractConfigObject) {
                return peekPath((AbstractConfigObject)v, next);
            }
            return null;
        }
        catch (ConfigException.NotResolved e) {
            throw ConfigImpl.improveNotResolved(path, e);
        }
    }
    
    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.OBJECT;
    }
    
    protected abstract AbstractConfigObject newCopy(final ResolveStatus p0, final ConfigOrigin p1);
    
    @Override
    protected AbstractConfigObject newCopy(final ConfigOrigin origin) {
        return this.newCopy(this.resolveStatus(), origin);
    }
    
    @Override
    protected AbstractConfigObject constructDelayedMerge(final ConfigOrigin origin, final List<AbstractConfigValue> stack) {
        return new ConfigDelayedMergeObject(origin, stack);
    }
    
    @Override
    protected abstract AbstractConfigObject mergedWithObject(final AbstractConfigObject p0);
    
    @Override
    public AbstractConfigObject withFallback(final ConfigMergeable mergeable) {
        return (AbstractConfigObject)super.withFallback(mergeable);
    }
    
    static ConfigOrigin mergeOrigins(final Collection<? extends AbstractConfigValue> stack) {
        if (stack.isEmpty()) {
            throw new ConfigException.BugOrBroken("can't merge origins on empty list");
        }
        final List<ConfigOrigin> origins = new ArrayList<ConfigOrigin>();
        ConfigOrigin firstOrigin = null;
        int numMerged = 0;
        for (final AbstractConfigValue v : stack) {
            if (firstOrigin == null) {
                firstOrigin = v.origin();
            }
            if (v instanceof AbstractConfigObject && ((AbstractConfigObject)v).resolveStatus() == ResolveStatus.RESOLVED && ((ConfigObject)v).isEmpty()) {
                continue;
            }
            origins.add(v.origin());
            ++numMerged;
        }
        if (numMerged == 0) {
            origins.add(firstOrigin);
        }
        return SimpleConfigOrigin.mergeOrigins(origins);
    }
    
    static ConfigOrigin mergeOrigins(final AbstractConfigObject... stack) {
        return mergeOrigins(Arrays.asList(stack));
    }
    
    @Override
    abstract ResolveResult<? extends AbstractConfigObject> resolveSubstitutions(final ResolveContext p0, final ResolveSource p1) throws NotPossibleToResolve;
    
    @Override
    abstract AbstractConfigObject relativized(final Path p0);
    
    @Override
    public abstract AbstractConfigValue get(final Object p0);
    
    @Override
    protected abstract void render(final StringBuilder p0, final int p1, final boolean p2, final ConfigRenderOptions p3);
    
    private static UnsupportedOperationException weAreImmutable(final String method) {
        return new UnsupportedOperationException("ConfigObject is immutable, you can't call Map." + method);
    }
    
    @Override
    public void clear() {
        throw weAreImmutable("clear");
    }
    
    @Override
    public ConfigValue put(final String arg0, final ConfigValue arg1) {
        throw weAreImmutable("put");
    }
    
    @Override
    public void putAll(final Map<? extends String, ? extends ConfigValue> arg0) {
        throw weAreImmutable("putAll");
    }
    
    @Override
    public ConfigValue remove(final Object arg0) {
        throw weAreImmutable("remove");
    }
    
    @Override
    public AbstractConfigObject withOrigin(final ConfigOrigin origin) {
        return (AbstractConfigObject)super.withOrigin(origin);
    }
}
