// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigMergeable;
import com.typesafe.config.ConfigObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Collection;
import com.typesafe.config.ConfigValueType;
import java.util.Iterator;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;
import java.util.List;

final class ConfigConcatenation extends AbstractConfigValue implements Unmergeable, Container
{
    private final List<AbstractConfigValue> pieces;
    
    ConfigConcatenation(final ConfigOrigin origin, final List<AbstractConfigValue> pieces) {
        super(origin);
        this.pieces = pieces;
        if (pieces.size() < 2) {
            throw new ConfigException.BugOrBroken("Created concatenation with less than 2 items: " + this);
        }
        boolean hadUnmergeable = false;
        for (final AbstractConfigValue p : pieces) {
            if (p instanceof ConfigConcatenation) {
                throw new ConfigException.BugOrBroken("ConfigConcatenation should never be nested: " + this);
            }
            if (!(p instanceof Unmergeable)) {
                continue;
            }
            hadUnmergeable = true;
        }
        if (!hadUnmergeable) {
            throw new ConfigException.BugOrBroken("Created concatenation without an unmergeable in it: " + this);
        }
    }
    
    private ConfigException.NotResolved notResolved() {
        return new ConfigException.NotResolved("need to Config#resolve(), see the API docs for Config#resolve(); substitution not resolved: " + this);
    }
    
    @Override
    public ConfigValueType valueType() {
        throw this.notResolved();
    }
    
    @Override
    public Object unwrapped() {
        throw this.notResolved();
    }
    
    @Override
    protected ConfigConcatenation newCopy(final ConfigOrigin newOrigin) {
        return new ConfigConcatenation(newOrigin, this.pieces);
    }
    
    @Override
    protected boolean ignoresFallbacks() {
        return false;
    }
    
    @Override
    public Collection<ConfigConcatenation> unmergedValues() {
        return Collections.singleton(this);
    }
    
    private static boolean isIgnoredWhitespace(final AbstractConfigValue value) {
        return value instanceof ConfigString && !((ConfigString)value).wasQuoted();
    }
    
    private static void join(final ArrayList<AbstractConfigValue> builder, final AbstractConfigValue origRight) {
        AbstractConfigValue left = builder.get(builder.size() - 1);
        AbstractConfigValue right = origRight;
        if (left instanceof ConfigObject && right instanceof SimpleConfigList) {
            left = DefaultTransformer.transform(left, ConfigValueType.LIST);
        }
        else if (left instanceof SimpleConfigList && right instanceof ConfigObject) {
            right = DefaultTransformer.transform(right, ConfigValueType.LIST);
        }
        AbstractConfigValue joined = null;
        if (left instanceof ConfigObject && right instanceof ConfigObject) {
            joined = right.withFallback((ConfigMergeable)left);
        }
        else if (left instanceof SimpleConfigList && right instanceof SimpleConfigList) {
            joined = ((SimpleConfigList)left).concatenate((SimpleConfigList)right);
        }
        else if ((left instanceof SimpleConfigList || left instanceof ConfigObject) && isIgnoredWhitespace(right)) {
            joined = left;
        }
        else {
            if (left instanceof ConfigConcatenation || right instanceof ConfigConcatenation) {
                throw new ConfigException.BugOrBroken("unflattened ConfigConcatenation");
            }
            if (!(left instanceof Unmergeable)) {
                if (!(right instanceof Unmergeable)) {
                    final String s1 = left.transformToString();
                    final String s2 = right.transformToString();
                    if (s1 == null || s2 == null) {
                        throw new ConfigException.WrongType(left.origin(), "Cannot concatenate object or list with a non-object-or-list, " + left + " and " + right + " are not compatible");
                    }
                    final ConfigOrigin joinedOrigin = SimpleConfigOrigin.mergeOrigins(left.origin(), right.origin());
                    joined = new ConfigString.Quoted(joinedOrigin, s1 + s2);
                }
            }
        }
        if (joined == null) {
            builder.add(right);
        }
        else {
            builder.remove(builder.size() - 1);
            builder.add(joined);
        }
    }
    
    static List<AbstractConfigValue> consolidate(final List<AbstractConfigValue> pieces) {
        if (pieces.size() < 2) {
            return pieces;
        }
        final List<AbstractConfigValue> flattened = new ArrayList<AbstractConfigValue>(pieces.size());
        for (final AbstractConfigValue v : pieces) {
            if (v instanceof ConfigConcatenation) {
                flattened.addAll(((ConfigConcatenation)v).pieces);
            }
            else {
                flattened.add(v);
            }
        }
        final ArrayList<AbstractConfigValue> consolidated = new ArrayList<AbstractConfigValue>(flattened.size());
        for (final AbstractConfigValue v2 : flattened) {
            if (consolidated.isEmpty()) {
                consolidated.add(v2);
            }
            else {
                join(consolidated, v2);
            }
        }
        return consolidated;
    }
    
    static AbstractConfigValue concatenate(final List<AbstractConfigValue> pieces) {
        final List<AbstractConfigValue> consolidated = consolidate(pieces);
        if (consolidated.isEmpty()) {
            return null;
        }
        if (consolidated.size() == 1) {
            return consolidated.get(0);
        }
        final ConfigOrigin mergedOrigin = SimpleConfigOrigin.mergeOrigins(consolidated);
        return new ConfigConcatenation(mergedOrigin, consolidated);
    }
    
    @Override
    ResolveResult<? extends AbstractConfigValue> resolveSubstitutions(final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        if (ConfigImpl.traceSubstitutionsEnabled()) {
            final int indent = context.depth() + 2;
            ConfigImpl.trace(indent - 1, "concatenation has " + this.pieces.size() + " pieces:");
            int count = 0;
            for (final AbstractConfigValue v : this.pieces) {
                ConfigImpl.trace(indent, count + ": " + v);
                ++count;
            }
        }
        final ResolveSource sourceWithParent = source;
        ResolveContext newContext = context;
        final List<AbstractConfigValue> resolved = new ArrayList<AbstractConfigValue>(this.pieces.size());
        for (final AbstractConfigValue p : this.pieces) {
            final Path restriction = newContext.restrictToChild();
            final ResolveResult<? extends AbstractConfigValue> result = newContext.unrestricted().resolve(p, sourceWithParent);
            final AbstractConfigValue r = (AbstractConfigValue)result.value;
            newContext = result.context.restrict(restriction);
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(context.depth(), "resolved concat piece to " + r);
            }
            if (r == null) {
                continue;
            }
            resolved.add(r);
        }
        final List<AbstractConfigValue> joined = consolidate(resolved);
        if (joined.size() > 1 && context.options().getAllowUnresolved()) {
            return ResolveResult.make(newContext, (AbstractConfigValue)new ConfigConcatenation(this.origin(), joined));
        }
        if (joined.isEmpty()) {
            return ResolveResult.make(newContext, (AbstractConfigValue)null);
        }
        if (joined.size() == 1) {
            return ResolveResult.make(newContext, joined.get(0));
        }
        throw new ConfigException.BugOrBroken("Bug in the library; resolved list was joined to too many values: " + joined);
    }
    
    @Override
    ResolveStatus resolveStatus() {
        return ResolveStatus.UNRESOLVED;
    }
    
    @Override
    public ConfigConcatenation replaceChild(final AbstractConfigValue child, final AbstractConfigValue replacement) {
        final List<AbstractConfigValue> newPieces = AbstractConfigValue.replaceChildInList(this.pieces, child, replacement);
        if (newPieces == null) {
            return null;
        }
        return new ConfigConcatenation(this.origin(), newPieces);
    }
    
    @Override
    public boolean hasDescendant(final AbstractConfigValue descendant) {
        return AbstractConfigValue.hasDescendantInList(this.pieces, descendant);
    }
    
    @Override
    ConfigConcatenation relativized(final Path prefix) {
        final List<AbstractConfigValue> newPieces = new ArrayList<AbstractConfigValue>();
        for (final AbstractConfigValue p : this.pieces) {
            newPieces.add(p.relativized(prefix));
        }
        return new ConfigConcatenation(this.origin(), newPieces);
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigConcatenation;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigConcatenation && this.canEqual(other) && this.pieces.equals(((ConfigConcatenation)other).pieces);
    }
    
    @Override
    public int hashCode() {
        return this.pieces.hashCode();
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        for (final AbstractConfigValue p : this.pieces) {
            p.render(sb, indent, atRoot, options);
        }
    }
}
