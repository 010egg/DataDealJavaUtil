// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigList;
import java.util.Set;
import java.util.Map;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigMergeable;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;
import java.util.List;

final class ConfigDelayedMergeObject extends AbstractConfigObject implements Unmergeable, ReplaceableMergeStack
{
    private final List<AbstractConfigValue> stack;
    
    ConfigDelayedMergeObject(final ConfigOrigin origin, final List<AbstractConfigValue> stack) {
        super(origin);
        this.stack = stack;
        if (stack.isEmpty()) {
            throw new ConfigException.BugOrBroken("creating empty delayed merge object");
        }
        if (!(stack.get(0) instanceof AbstractConfigObject)) {
            throw new ConfigException.BugOrBroken("created a delayed merge object not guaranteed to be an object");
        }
        for (final AbstractConfigValue v : stack) {
            if (v instanceof ConfigDelayedMerge || v instanceof ConfigDelayedMergeObject) {
                throw new ConfigException.BugOrBroken("placed nested DelayedMerge in a ConfigDelayedMergeObject, should have consolidated stack");
            }
        }
    }
    
    @Override
    protected ConfigDelayedMergeObject newCopy(final ResolveStatus status, final ConfigOrigin origin) {
        if (status != this.resolveStatus()) {
            throw new ConfigException.BugOrBroken("attempt to create resolved ConfigDelayedMergeObject");
        }
        return new ConfigDelayedMergeObject(origin, this.stack);
    }
    
    @Override
    ResolveResult<? extends AbstractConfigObject> resolveSubstitutions(final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        final ResolveResult<? extends AbstractConfigValue> merged = ConfigDelayedMerge.resolveSubstitutions(this, this.stack, context, source);
        return merged.asObjectResult();
    }
    
    @Override
    public AbstractConfigValue makeReplacement(final ResolveContext context, final int skipping) {
        return ConfigDelayedMerge.makeReplacement(context, this.stack, skipping);
    }
    
    @Override
    ResolveStatus resolveStatus() {
        return ResolveStatus.UNRESOLVED;
    }
    
    @Override
    public AbstractConfigValue replaceChild(final AbstractConfigValue child, final AbstractConfigValue replacement) {
        final List<AbstractConfigValue> newStack = AbstractConfigValue.replaceChildInList(this.stack, child, replacement);
        if (newStack == null) {
            return null;
        }
        return new ConfigDelayedMergeObject(this.origin(), newStack);
    }
    
    @Override
    public boolean hasDescendant(final AbstractConfigValue descendant) {
        return AbstractConfigValue.hasDescendantInList(this.stack, descendant);
    }
    
    @Override
    ConfigDelayedMergeObject relativized(final Path prefix) {
        final List<AbstractConfigValue> newStack = new ArrayList<AbstractConfigValue>();
        for (final AbstractConfigValue o : this.stack) {
            newStack.add(o.relativized(prefix));
        }
        return new ConfigDelayedMergeObject(this.origin(), newStack);
    }
    
    @Override
    protected boolean ignoresFallbacks() {
        return ConfigDelayedMerge.stackIgnoresFallbacks(this.stack);
    }
    
    @Override
    protected final ConfigDelayedMergeObject mergedWithTheUnmergeable(final Unmergeable fallback) {
        this.requireNotIgnoringFallbacks();
        return (ConfigDelayedMergeObject)this.mergedWithTheUnmergeable(this.stack, fallback);
    }
    
    @Override
    protected final ConfigDelayedMergeObject mergedWithObject(final AbstractConfigObject fallback) {
        return this.mergedWithNonObject(fallback);
    }
    
    @Override
    protected final ConfigDelayedMergeObject mergedWithNonObject(final AbstractConfigValue fallback) {
        this.requireNotIgnoringFallbacks();
        return (ConfigDelayedMergeObject)this.mergedWithNonObject(this.stack, fallback);
    }
    
    @Override
    public ConfigDelayedMergeObject withFallback(final ConfigMergeable mergeable) {
        return (ConfigDelayedMergeObject)super.withFallback(mergeable);
    }
    
    @Override
    public ConfigDelayedMergeObject withOnlyKey(final String key) {
        throw notResolved();
    }
    
    @Override
    public ConfigDelayedMergeObject withoutKey(final String key) {
        throw notResolved();
    }
    
    @Override
    protected AbstractConfigObject withOnlyPathOrNull(final Path path) {
        throw notResolved();
    }
    
    @Override
    AbstractConfigObject withOnlyPath(final Path path) {
        throw notResolved();
    }
    
    @Override
    AbstractConfigObject withoutPath(final Path path) {
        throw notResolved();
    }
    
    @Override
    public ConfigDelayedMergeObject withValue(final String key, final ConfigValue value) {
        throw notResolved();
    }
    
    @Override
    ConfigDelayedMergeObject withValue(final Path path, final ConfigValue value) {
        throw notResolved();
    }
    
    @Override
    public Collection<AbstractConfigValue> unmergedValues() {
        return this.stack;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigDelayedMergeObject;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigDelayedMergeObject && this.canEqual(other) && (this.stack == ((ConfigDelayedMergeObject)other).stack || this.stack.equals(((ConfigDelayedMergeObject)other).stack));
    }
    
    @Override
    public int hashCode() {
        return this.stack.hashCode();
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final String atKey, final ConfigRenderOptions options) {
        ConfigDelayedMerge.render(this.stack, sb, indent, atRoot, atKey, options);
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        this.render(sb, indent, atRoot, null, options);
    }
    
    private static ConfigException notResolved() {
        return new ConfigException.NotResolved("need to Config#resolve() before using this object, see the API docs for Config#resolve()");
    }
    
    @Override
    public Map<String, Object> unwrapped() {
        throw notResolved();
    }
    
    @Override
    public AbstractConfigValue get(final Object key) {
        throw notResolved();
    }
    
    @Override
    public boolean containsKey(final Object key) {
        throw notResolved();
    }
    
    @Override
    public boolean containsValue(final Object value) {
        throw notResolved();
    }
    
    @Override
    public Set<Map.Entry<String, ConfigValue>> entrySet() {
        throw notResolved();
    }
    
    @Override
    public boolean isEmpty() {
        throw notResolved();
    }
    
    @Override
    public Set<String> keySet() {
        throw notResolved();
    }
    
    @Override
    public int size() {
        throw notResolved();
    }
    
    @Override
    public Collection<ConfigValue> values() {
        throw notResolved();
    }
    
    protected AbstractConfigValue attemptPeekWithPartialResolve(final String key) {
        for (final AbstractConfigValue layer : this.stack) {
            if (layer instanceof AbstractConfigObject) {
                final AbstractConfigObject objectLayer = (AbstractConfigObject)layer;
                final AbstractConfigValue v = objectLayer.attemptPeekWithPartialResolve(key);
                if (v != null) {
                    if (v.ignoresFallbacks()) {
                        return v;
                    }
                    continue;
                }
                else {
                    if (layer instanceof Unmergeable) {
                        throw new ConfigException.BugOrBroken("should not be reached: unmergeable object returned null value");
                    }
                    continue;
                }
            }
            else {
                if (layer instanceof Unmergeable) {
                    throw new ConfigException.NotResolved("Key '" + key + "' is not available at '" + this.origin().description() + "' because value at '" + layer.origin().description() + "' has not been resolved and may turn out to contain or hide '" + key + "'. Be sure to Config#resolve() before using a config object.");
                }
                if (layer.resolveStatus() == ResolveStatus.UNRESOLVED) {
                    if (!(layer instanceof ConfigList)) {
                        throw new ConfigException.BugOrBroken("Expecting a list here, not " + layer);
                    }
                    return null;
                }
                else {
                    if (!layer.ignoresFallbacks()) {
                        throw new ConfigException.BugOrBroken("resolved non-object should ignore fallbacks");
                    }
                    return null;
                }
            }
        }
        throw new ConfigException.BugOrBroken("Delayed merge stack does not contain any unmergeable values");
    }
}
