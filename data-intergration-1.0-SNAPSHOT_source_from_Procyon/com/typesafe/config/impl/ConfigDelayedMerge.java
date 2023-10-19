// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Collections;
import com.typesafe.config.ConfigRenderOptions;
import java.util.Collection;
import java.util.ArrayList;
import com.typesafe.config.ConfigMergeable;
import com.typesafe.config.ConfigValueType;
import java.util.Iterator;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;
import java.util.List;

final class ConfigDelayedMerge extends AbstractConfigValue implements Unmergeable, ReplaceableMergeStack
{
    private final List<AbstractConfigValue> stack;
    
    ConfigDelayedMerge(final ConfigOrigin origin, final List<AbstractConfigValue> stack) {
        super(origin);
        this.stack = stack;
        if (stack.isEmpty()) {
            throw new ConfigException.BugOrBroken("creating empty delayed merge value");
        }
        for (final AbstractConfigValue v : stack) {
            if (v instanceof ConfigDelayedMerge || v instanceof ConfigDelayedMergeObject) {
                throw new ConfigException.BugOrBroken("placed nested DelayedMerge in a ConfigDelayedMerge, should have consolidated stack");
            }
        }
    }
    
    @Override
    public ConfigValueType valueType() {
        throw new ConfigException.NotResolved("called valueType() on value with unresolved substitutions, need to Config#resolve() first, see API docs");
    }
    
    @Override
    public Object unwrapped() {
        throw new ConfigException.NotResolved("called unwrapped() on value with unresolved substitutions, need to Config#resolve() first, see API docs");
    }
    
    @Override
    ResolveResult<? extends AbstractConfigValue> resolveSubstitutions(final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        return resolveSubstitutions(this, this.stack, context, source);
    }
    
    static ResolveResult<? extends AbstractConfigValue> resolveSubstitutions(final ReplaceableMergeStack replaceable, final List<AbstractConfigValue> stack, final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace(context.depth(), "delayed merge stack has " + stack.size() + " items:");
            int count = 0;
            for (final AbstractConfigValue v : stack) {
                ConfigImpl.trace(context.depth() + 1, count + ": " + v);
                ++count;
            }
        }
        ResolveContext newContext = context;
        int count2 = 0;
        AbstractConfigValue merged = null;
        for (final AbstractConfigValue end : stack) {
            if (end instanceof ReplaceableMergeStack) {
                throw new ConfigException.BugOrBroken("A delayed merge should not contain another one: " + replaceable);
            }
            ResolveSource sourceForEnd;
            if (end instanceof Unmergeable) {
                final AbstractConfigValue remainder = replaceable.makeReplacement(context, count2 + 1);
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(newContext.depth(), "remainder portion: " + remainder);
                }
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(newContext.depth(), "building sourceForEnd");
                }
                sourceForEnd = source.replaceWithinCurrentParent((AbstractConfigValue)replaceable, remainder);
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(newContext.depth(), "  sourceForEnd before reset parents but after replace: " + sourceForEnd);
                }
                sourceForEnd = sourceForEnd.resetParents();
            }
            else {
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(newContext.depth(), "will resolve end against the original source with parent pushed");
                }
                sourceForEnd = source.pushParent(replaceable);
            }
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(newContext.depth(), "sourceForEnd      =" + sourceForEnd);
            }
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(newContext.depth(), "Resolving highest-priority item in delayed merge " + end + " against " + sourceForEnd + " endWasRemoved=" + (source != sourceForEnd));
            }
            final ResolveResult<? extends AbstractConfigValue> result = newContext.resolve(end, sourceForEnd);
            final AbstractConfigValue resolvedEnd = (AbstractConfigValue)result.value;
            newContext = result.context;
            if (resolvedEnd != null) {
                if (merged == null) {
                    merged = resolvedEnd;
                }
                else {
                    if (ConfigImpl.traceSubstitutionsEnabled()) {
                        ConfigImpl.trace(newContext.depth() + 1, "merging " + merged + " with fallback " + resolvedEnd);
                    }
                    merged = merged.withFallback((ConfigMergeable)resolvedEnd);
                }
            }
            ++count2;
            if (!ConfigImpl.traceSubstitutionsEnabled()) {
                continue;
            }
            ConfigImpl.trace(newContext.depth(), "stack merged, yielding: " + merged);
        }
        return ResolveResult.make(newContext, merged);
    }
    
    @Override
    public AbstractConfigValue makeReplacement(final ResolveContext context, final int skipping) {
        return makeReplacement(context, this.stack, skipping);
    }
    
    static AbstractConfigValue makeReplacement(final ResolveContext context, final List<AbstractConfigValue> stack, final int skipping) {
        final List<AbstractConfigValue> subStack = stack.subList(skipping, stack.size());
        if (subStack.isEmpty()) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(context.depth(), "Nothing else in the merge stack, replacing with null");
            }
            return null;
        }
        AbstractConfigValue merged = null;
        for (final AbstractConfigValue v : subStack) {
            if (merged == null) {
                merged = v;
            }
            else {
                merged = merged.withFallback((ConfigMergeable)v);
            }
        }
        return merged;
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
        return new ConfigDelayedMerge(this.origin(), newStack);
    }
    
    @Override
    public boolean hasDescendant(final AbstractConfigValue descendant) {
        return AbstractConfigValue.hasDescendantInList(this.stack, descendant);
    }
    
    @Override
    ConfigDelayedMerge relativized(final Path prefix) {
        final List<AbstractConfigValue> newStack = new ArrayList<AbstractConfigValue>();
        for (final AbstractConfigValue o : this.stack) {
            newStack.add(o.relativized(prefix));
        }
        return new ConfigDelayedMerge(this.origin(), newStack);
    }
    
    static boolean stackIgnoresFallbacks(final List<AbstractConfigValue> stack) {
        final AbstractConfigValue last = stack.get(stack.size() - 1);
        return last.ignoresFallbacks();
    }
    
    @Override
    protected boolean ignoresFallbacks() {
        return stackIgnoresFallbacks(this.stack);
    }
    
    @Override
    protected AbstractConfigValue newCopy(final ConfigOrigin newOrigin) {
        return new ConfigDelayedMerge(newOrigin, this.stack);
    }
    
    @Override
    protected final ConfigDelayedMerge mergedWithTheUnmergeable(final Unmergeable fallback) {
        return (ConfigDelayedMerge)this.mergedWithTheUnmergeable(this.stack, fallback);
    }
    
    @Override
    protected final ConfigDelayedMerge mergedWithObject(final AbstractConfigObject fallback) {
        return (ConfigDelayedMerge)this.mergedWithObject(this.stack, fallback);
    }
    
    @Override
    protected ConfigDelayedMerge mergedWithNonObject(final AbstractConfigValue fallback) {
        return (ConfigDelayedMerge)this.mergedWithNonObject(this.stack, fallback);
    }
    
    @Override
    public Collection<AbstractConfigValue> unmergedValues() {
        return this.stack;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigDelayedMerge;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigDelayedMerge && this.canEqual(other) && (this.stack == ((ConfigDelayedMerge)other).stack || this.stack.equals(((ConfigDelayedMerge)other).stack));
    }
    
    @Override
    public int hashCode() {
        return this.stack.hashCode();
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final String atKey, final ConfigRenderOptions options) {
        render(this.stack, sb, indent, atRoot, atKey, options);
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        this.render(sb, indent, atRoot, null, options);
    }
    
    static void render(final List<AbstractConfigValue> stack, final StringBuilder sb, final int indent, final boolean atRoot, final String atKey, final ConfigRenderOptions options) {
        final boolean commentMerge = options.getComments();
        if (commentMerge) {
            sb.append("# unresolved merge of " + stack.size() + " values follows (\n");
            if (atKey == null) {
                AbstractConfigValue.indent(sb, indent, options);
                sb.append("# this unresolved merge will not be parseable because it's at the root of the object\n");
                AbstractConfigValue.indent(sb, indent, options);
                sb.append("# the HOCON format has no way to list multiple root objects in a single file\n");
            }
        }
        final List<AbstractConfigValue> reversed = new ArrayList<AbstractConfigValue>();
        reversed.addAll(stack);
        Collections.reverse(reversed);
        int i = 0;
        for (final AbstractConfigValue v : reversed) {
            if (commentMerge) {
                AbstractConfigValue.indent(sb, indent, options);
                if (atKey != null) {
                    sb.append("#     unmerged value " + i + " for key " + ConfigImplUtil.renderJsonString(atKey) + " from ");
                }
                else {
                    sb.append("#     unmerged value " + i + " from ");
                }
                ++i;
                sb.append(v.origin().description());
                sb.append("\n");
                for (final String comment : v.origin().comments()) {
                    AbstractConfigValue.indent(sb, indent, options);
                    sb.append("# ");
                    sb.append(comment);
                    sb.append("\n");
                }
            }
            AbstractConfigValue.indent(sb, indent, options);
            if (atKey != null) {
                sb.append(ConfigImplUtil.renderJsonString(atKey));
                if (options.getFormatted()) {
                    sb.append(" : ");
                }
                else {
                    sb.append(":");
                }
            }
            v.render(sb, indent, atRoot, options);
            sb.append(",");
            if (options.getFormatted()) {
                sb.append('\n');
            }
        }
        sb.setLength(sb.length() - 1);
        if (options.getFormatted()) {
            sb.setLength(sb.length() - 1);
            sb.append("\n");
        }
        if (commentMerge) {
            AbstractConfigValue.indent(sb, indent, options);
            sb.append("# ) end of unresolved merge\n");
        }
    }
}
