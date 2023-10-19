// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.Config;
import java.util.Map;
import com.typesafe.config.ConfigObject;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigMergeable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import com.typesafe.config.ConfigException;
import java.util.List;
import com.typesafe.config.ConfigOrigin;
import com.typesafe.config.ConfigValue;

abstract class AbstractConfigValue implements ConfigValue, MergeableValue
{
    private final SimpleConfigOrigin origin;
    
    AbstractConfigValue(final ConfigOrigin origin) {
        this.origin = (SimpleConfigOrigin)origin;
    }
    
    @Override
    public SimpleConfigOrigin origin() {
        return this.origin;
    }
    
    ResolveResult<? extends AbstractConfigValue> resolveSubstitutions(final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        return ResolveResult.make(context, this);
    }
    
    ResolveStatus resolveStatus() {
        return ResolveStatus.RESOLVED;
    }
    
    protected static List<AbstractConfigValue> replaceChildInList(final List<AbstractConfigValue> list, final AbstractConfigValue child, final AbstractConfigValue replacement) {
        int i;
        for (i = 0; i < list.size() && list.get(i) != child; ++i) {}
        if (i == list.size()) {
            throw new ConfigException.BugOrBroken("tried to replace " + child + " which is not in " + list);
        }
        final List<AbstractConfigValue> newStack = new ArrayList<AbstractConfigValue>(list);
        if (replacement != null) {
            newStack.set(i, replacement);
        }
        else {
            newStack.remove(i);
        }
        if (newStack.isEmpty()) {
            return null;
        }
        return newStack;
    }
    
    protected static boolean hasDescendantInList(final List<AbstractConfigValue> list, final AbstractConfigValue descendant) {
        for (final AbstractConfigValue v : list) {
            if (v == descendant) {
                return true;
            }
        }
        for (final AbstractConfigValue v : list) {
            if (v instanceof Container && ((Container)v).hasDescendant(descendant)) {
                return true;
            }
        }
        return false;
    }
    
    AbstractConfigValue relativized(final Path prefix) {
        return this;
    }
    
    @Override
    public AbstractConfigValue toFallbackValue() {
        return this;
    }
    
    protected abstract AbstractConfigValue newCopy(final ConfigOrigin p0);
    
    protected boolean ignoresFallbacks() {
        return this.resolveStatus() == ResolveStatus.RESOLVED;
    }
    
    protected AbstractConfigValue withFallbacksIgnored() {
        if (this.ignoresFallbacks()) {
            return this;
        }
        throw new ConfigException.BugOrBroken("value class doesn't implement forced fallback-ignoring " + this);
    }
    
    protected final void requireNotIgnoringFallbacks() {
        if (this.ignoresFallbacks()) {
            throw new ConfigException.BugOrBroken("method should not have been called with ignoresFallbacks=true " + this.getClass().getSimpleName());
        }
    }
    
    protected AbstractConfigValue constructDelayedMerge(final ConfigOrigin origin, final List<AbstractConfigValue> stack) {
        return new ConfigDelayedMerge(origin, stack);
    }
    
    protected final AbstractConfigValue mergedWithTheUnmergeable(final Collection<AbstractConfigValue> stack, final Unmergeable fallback) {
        this.requireNotIgnoringFallbacks();
        final List<AbstractConfigValue> newStack = new ArrayList<AbstractConfigValue>();
        newStack.addAll(stack);
        newStack.addAll(fallback.unmergedValues());
        return this.constructDelayedMerge(AbstractConfigObject.mergeOrigins(newStack), newStack);
    }
    
    private final AbstractConfigValue delayMerge(final Collection<AbstractConfigValue> stack, final AbstractConfigValue fallback) {
        final List<AbstractConfigValue> newStack = new ArrayList<AbstractConfigValue>();
        newStack.addAll(stack);
        newStack.add(fallback);
        return this.constructDelayedMerge(AbstractConfigObject.mergeOrigins(newStack), newStack);
    }
    
    protected final AbstractConfigValue mergedWithObject(final Collection<AbstractConfigValue> stack, final AbstractConfigObject fallback) {
        this.requireNotIgnoringFallbacks();
        if (this instanceof AbstractConfigObject) {
            throw new ConfigException.BugOrBroken("Objects must reimplement mergedWithObject");
        }
        return this.mergedWithNonObject(stack, fallback);
    }
    
    protected final AbstractConfigValue mergedWithNonObject(final Collection<AbstractConfigValue> stack, final AbstractConfigValue fallback) {
        this.requireNotIgnoringFallbacks();
        if (this.resolveStatus() == ResolveStatus.RESOLVED) {
            return this.withFallbacksIgnored();
        }
        return this.delayMerge(stack, fallback);
    }
    
    protected AbstractConfigValue mergedWithTheUnmergeable(final Unmergeable fallback) {
        this.requireNotIgnoringFallbacks();
        return this.mergedWithTheUnmergeable(Collections.singletonList(this), fallback);
    }
    
    protected AbstractConfigValue mergedWithObject(final AbstractConfigObject fallback) {
        this.requireNotIgnoringFallbacks();
        return this.mergedWithObject(Collections.singletonList(this), fallback);
    }
    
    protected AbstractConfigValue mergedWithNonObject(final AbstractConfigValue fallback) {
        this.requireNotIgnoringFallbacks();
        return this.mergedWithNonObject(Collections.singletonList(this), fallback);
    }
    
    @Override
    public AbstractConfigValue withOrigin(final ConfigOrigin origin) {
        if (this.origin == origin) {
            return this;
        }
        return this.newCopy(origin);
    }
    
    @Override
    public AbstractConfigValue withFallback(final ConfigMergeable mergeable) {
        if (this.ignoresFallbacks()) {
            return this;
        }
        final ConfigValue other = ((MergeableValue)mergeable).toFallbackValue();
        if (other instanceof Unmergeable) {
            return this.mergedWithTheUnmergeable((Unmergeable)other);
        }
        if (other instanceof AbstractConfigObject) {
            return this.mergedWithObject((AbstractConfigObject)other);
        }
        return this.mergedWithNonObject((AbstractConfigValue)other);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigValue;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigValue && this.canEqual(other) && this.valueType() == ((ConfigValue)other).valueType() && ConfigImplUtil.equalsHandlingNull(this.unwrapped(), ((ConfigValue)other).unwrapped());
    }
    
    @Override
    public int hashCode() {
        final Object o = this.unwrapped();
        if (o == null) {
            return 0;
        }
        return o.hashCode();
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        this.render(sb, 0, true, null, ConfigRenderOptions.concise());
        return this.getClass().getSimpleName() + "(" + sb.toString() + ")";
    }
    
    protected static void indent(final StringBuilder sb, final int indent, final ConfigRenderOptions options) {
        if (options.getFormatted()) {
            for (int remaining = indent; remaining > 0; --remaining) {
                sb.append("    ");
            }
        }
    }
    
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final String atKey, final ConfigRenderOptions options) {
        if (atKey != null) {
            String renderedKey;
            if (options.getJson()) {
                renderedKey = ConfigImplUtil.renderJsonString(atKey);
            }
            else {
                renderedKey = ConfigImplUtil.renderStringUnquotedIfPossible(atKey);
            }
            sb.append(renderedKey);
            if (options.getJson()) {
                if (options.getFormatted()) {
                    sb.append(" : ");
                }
                else {
                    sb.append(":");
                }
            }
            else if (this instanceof ConfigObject) {
                if (options.getFormatted()) {
                    sb.append(' ');
                }
            }
            else {
                sb.append("=");
            }
        }
        this.render(sb, indent, atRoot, options);
    }
    
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        final Object u = this.unwrapped();
        sb.append(u.toString());
    }
    
    @Override
    public final String render() {
        return this.render(ConfigRenderOptions.defaults());
    }
    
    @Override
    public final String render(final ConfigRenderOptions options) {
        final StringBuilder sb = new StringBuilder();
        this.render(sb, 0, true, null, options);
        return sb.toString();
    }
    
    String transformToString() {
        return null;
    }
    
    SimpleConfig atKey(final ConfigOrigin origin, final String key) {
        final Map<String, AbstractConfigValue> m = Collections.singletonMap(key, this);
        return new SimpleConfigObject(origin, m).toConfig();
    }
    
    @Override
    public SimpleConfig atKey(final String key) {
        return this.atKey(SimpleConfigOrigin.newSimple("atKey(" + key + ")"), key);
    }
    
    SimpleConfig atPath(final ConfigOrigin origin, final Path path) {
        Path parent = path.parent();
        SimpleConfig result = this.atKey(origin, path.last());
        while (parent != null) {
            final String key = parent.last();
            result = result.atKey(origin, key);
            parent = parent.parent();
        }
        return result;
    }
    
    @Override
    public SimpleConfig atPath(final String pathExpression) {
        final SimpleConfigOrigin origin = SimpleConfigOrigin.newSimple("atPath(" + pathExpression + ")");
        return this.atPath(origin, Path.newPath(pathExpression));
    }
    
    static class NotPossibleToResolve extends Exception
    {
        private static final long serialVersionUID = 1L;
        private final String traceString;
        
        NotPossibleToResolve(final ResolveContext context) {
            super("was not possible to resolve");
            this.traceString = context.traceString();
        }
        
        String traceString() {
            return this.traceString;
        }
    }
    
    protected abstract class NoExceptionsModifier implements Modifier
    {
        @Override
        public final AbstractConfigValue modifyChildMayThrow(final String keyOrNull, final AbstractConfigValue v) throws Exception {
            try {
                return this.modifyChild(keyOrNull, v);
            }
            catch (RuntimeException e) {
                throw e;
            }
            catch (Exception e2) {
                throw new ConfigException.BugOrBroken("Unexpected exception", e2);
            }
        }
        
        abstract AbstractConfigValue modifyChild(final String p0, final AbstractConfigValue p1);
    }
    
    protected interface Modifier
    {
        AbstractConfigValue modifyChildMayThrow(final String p0, final AbstractConfigValue p1) throws Exception;
    }
}
