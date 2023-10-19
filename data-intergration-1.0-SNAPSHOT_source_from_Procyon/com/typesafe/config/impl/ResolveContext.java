// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.Collection;
import com.typesafe.config.ConfigException;
import java.util.ArrayList;
import java.util.Map;
import java.util.IdentityHashMap;
import java.util.Collections;
import java.util.Set;
import java.util.List;
import com.typesafe.config.ConfigResolveOptions;

final class ResolveContext
{
    private final ResolveMemos memos;
    private final ConfigResolveOptions options;
    private final Path restrictToChild;
    private final List<AbstractConfigValue> resolveStack;
    private final Set<AbstractConfigValue> cycleMarkers;
    
    ResolveContext(final ResolveMemos memos, final ConfigResolveOptions options, final Path restrictToChild, final List<AbstractConfigValue> resolveStack, final Set<AbstractConfigValue> cycleMarkers) {
        this.memos = memos;
        this.options = options;
        this.restrictToChild = restrictToChild;
        this.resolveStack = Collections.unmodifiableList((List<? extends AbstractConfigValue>)resolveStack);
        this.cycleMarkers = Collections.unmodifiableSet((Set<? extends AbstractConfigValue>)cycleMarkers);
    }
    
    private static Set<AbstractConfigValue> newCycleMarkers() {
        return Collections.newSetFromMap(new IdentityHashMap<AbstractConfigValue, Boolean>());
    }
    
    ResolveContext(final ConfigResolveOptions options, final Path restrictToChild) {
        this(new ResolveMemos(), options, restrictToChild, new ArrayList<AbstractConfigValue>(), newCycleMarkers());
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "ResolveContext restrict to child " + restrictToChild);
        }
    }
    
    ResolveContext addCycleMarker(final AbstractConfigValue value) {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "++ Cycle marker " + value + "@" + System.identityHashCode(value));
        }
        if (this.cycleMarkers.contains(value)) {
            throw new ConfigException.BugOrBroken("Added cycle marker twice " + value);
        }
        final Set<AbstractConfigValue> copy = newCycleMarkers();
        copy.addAll(this.cycleMarkers);
        copy.add(value);
        return new ResolveContext(this.memos, this.options, this.restrictToChild, this.resolveStack, copy);
    }
    
    ResolveContext removeCycleMarker(final AbstractConfigValue value) {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "-- Cycle marker " + value + "@" + System.identityHashCode(value));
        }
        final Set<AbstractConfigValue> copy = newCycleMarkers();
        copy.addAll(this.cycleMarkers);
        copy.remove(value);
        return new ResolveContext(this.memos, this.options, this.restrictToChild, this.resolveStack, copy);
    }
    
    private ResolveContext memoize(final MemoKey key, final AbstractConfigValue value) {
        final ResolveMemos changed = this.memos.put(key, value);
        return new ResolveContext(changed, this.options, this.restrictToChild, this.resolveStack, this.cycleMarkers);
    }
    
    ConfigResolveOptions options() {
        return this.options;
    }
    
    boolean isRestrictedToChild() {
        return this.restrictToChild != null;
    }
    
    Path restrictToChild() {
        return this.restrictToChild;
    }
    
    ResolveContext restrict(final Path restrictTo) {
        if (restrictTo == this.restrictToChild) {
            return this;
        }
        return new ResolveContext(this.memos, this.options, restrictTo, this.resolveStack, this.cycleMarkers);
    }
    
    ResolveContext unrestricted() {
        return this.restrict(null);
    }
    
    String traceString() {
        final String separator = ", ";
        final StringBuilder sb = new StringBuilder();
        for (final AbstractConfigValue value : this.resolveStack) {
            if (value instanceof ConfigReference) {
                sb.append(((ConfigReference)value).expression().toString());
                sb.append(separator);
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - separator.length());
        }
        return sb.toString();
    }
    
    private ResolveContext pushTrace(final AbstractConfigValue value) {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "pushing trace " + value);
        }
        final List<AbstractConfigValue> copy = new ArrayList<AbstractConfigValue>(this.resolveStack);
        copy.add(value);
        return new ResolveContext(this.memos, this.options, this.restrictToChild, copy, this.cycleMarkers);
    }
    
    ResolveContext popTrace() {
        final List<AbstractConfigValue> copy = new ArrayList<AbstractConfigValue>(this.resolveStack);
        final AbstractConfigValue old = copy.remove(this.resolveStack.size() - 1);
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth() - 1, "popped trace " + old);
        }
        return new ResolveContext(this.memos, this.options, this.restrictToChild, copy, this.cycleMarkers);
    }
    
    int depth() {
        if (this.resolveStack.size() > 30) {
            throw new ConfigException.BugOrBroken("resolve getting too deep");
        }
        return this.resolveStack.size();
    }
    
    ResolveResult<? extends AbstractConfigValue> resolve(final AbstractConfigValue original, final ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "resolving " + original + " restrictToChild=" + this.restrictToChild + " in " + source);
        }
        return this.pushTrace(original).realResolve(original, source).popTrace();
    }
    
    private ResolveResult<? extends AbstractConfigValue> realResolve(final AbstractConfigValue original, final ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
        final MemoKey fullKey = new MemoKey(original, null);
        MemoKey restrictedKey = null;
        AbstractConfigValue cached = this.memos.get(fullKey);
        if (cached == null && this.isRestrictedToChild()) {
            restrictedKey = new MemoKey(original, this.restrictToChild());
            cached = this.memos.get(restrictedKey);
        }
        if (cached != null) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(this.depth(), "using cached resolution " + cached + " for " + original + " restrictToChild " + this.restrictToChild());
            }
            return ResolveResult.make(this, cached);
        }
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "not found in cache, resolving " + original + "@" + System.identityHashCode(original));
        }
        if (this.cycleMarkers.contains(original)) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(this.depth(), "Cycle detected, can't resolve; " + original + "@" + System.identityHashCode(original));
            }
            throw new AbstractConfigValue.NotPossibleToResolve(this);
        }
        final ResolveResult<? extends AbstractConfigValue> result = original.resolveSubstitutions(this, source);
        final AbstractConfigValue resolved = (AbstractConfigValue)result.value;
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(this.depth(), "resolved to " + resolved + "@" + System.identityHashCode(resolved) + " from " + original + "@" + System.identityHashCode(resolved));
        }
        ResolveContext withMemo = result.context;
        if (resolved == null || resolved.resolveStatus() == ResolveStatus.RESOLVED) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(this.depth(), "caching " + fullKey + " result " + resolved);
            }
            withMemo = withMemo.memoize(fullKey, resolved);
        }
        else if (this.isRestrictedToChild()) {
            if (restrictedKey == null) {
                throw new ConfigException.BugOrBroken("restrictedKey should not be null here");
            }
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(this.depth(), "caching " + restrictedKey + " result " + resolved);
            }
            withMemo = withMemo.memoize(restrictedKey, resolved);
        }
        else {
            if (!this.options().getAllowUnresolved()) {
                throw new ConfigException.BugOrBroken("resolveSubstitutions() did not give us a resolved object");
            }
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(this.depth(), "caching " + fullKey + " result " + resolved);
            }
            withMemo = withMemo.memoize(fullKey, resolved);
        }
        return ResolveResult.make(withMemo, resolved);
    }
    
    static AbstractConfigValue resolve(final AbstractConfigValue value, final AbstractConfigObject root, final ConfigResolveOptions options) {
        final ResolveSource source = new ResolveSource(root);
        final ResolveContext context = new ResolveContext(options, null);
        try {
            return (AbstractConfigValue)context.resolve(value, source).value;
        }
        catch (AbstractConfigValue.NotPossibleToResolve e) {
            throw new ConfigException.BugOrBroken("NotPossibleToResolve was thrown from an outermost resolve", e);
        }
    }
}
