// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.ObjectStreamException;
import java.util.AbstractMap;
import com.typesafe.config.ConfigObject;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Arrays;
import com.typesafe.config.ConfigRenderOptions;
import java.util.Set;
import com.typesafe.config.ConfigMergeable;
import java.util.HashSet;
import com.typesafe.config.ConfigValue;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Collections;
import java.util.Collection;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;
import java.util.Map;
import java.io.Serializable;

final class SimpleConfigObject extends AbstractConfigObject implements Serializable
{
    private static final long serialVersionUID = 2L;
    private final Map<String, AbstractConfigValue> value;
    private final boolean resolved;
    private final boolean ignoresFallbacks;
    private static final String EMPTY_NAME = "empty config";
    private static final SimpleConfigObject emptyInstance;
    
    SimpleConfigObject(final ConfigOrigin origin, final Map<String, AbstractConfigValue> value, final ResolveStatus status, final boolean ignoresFallbacks) {
        super(origin);
        if (value == null) {
            throw new ConfigException.BugOrBroken("creating config object with null map");
        }
        this.value = value;
        this.resolved = (status == ResolveStatus.RESOLVED);
        this.ignoresFallbacks = ignoresFallbacks;
        if (status != ResolveStatus.fromValues(value.values())) {
            throw new ConfigException.BugOrBroken("Wrong resolved status on " + this);
        }
    }
    
    SimpleConfigObject(final ConfigOrigin origin, final Map<String, AbstractConfigValue> value) {
        this(origin, value, ResolveStatus.fromValues(value.values()), false);
    }
    
    @Override
    public SimpleConfigObject withOnlyKey(final String key) {
        return this.withOnlyPath(Path.newKey(key));
    }
    
    @Override
    public SimpleConfigObject withoutKey(final String key) {
        return this.withoutPath(Path.newKey(key));
    }
    
    @Override
    protected SimpleConfigObject withOnlyPathOrNull(final Path path) {
        final String key = path.first();
        final Path next = path.remainder();
        AbstractConfigValue v = this.value.get(key);
        if (next != null) {
            if (v != null && v instanceof AbstractConfigObject) {
                v = ((AbstractConfigObject)v).withOnlyPathOrNull(next);
            }
            else {
                v = null;
            }
        }
        if (v == null) {
            return null;
        }
        return new SimpleConfigObject(this.origin(), Collections.singletonMap(key, v), v.resolveStatus(), this.ignoresFallbacks);
    }
    
    @Override
    SimpleConfigObject withOnlyPath(final Path path) {
        final SimpleConfigObject o = this.withOnlyPathOrNull(path);
        if (o == null) {
            return new SimpleConfigObject(this.origin(), Collections.emptyMap(), ResolveStatus.RESOLVED, this.ignoresFallbacks);
        }
        return o;
    }
    
    @Override
    SimpleConfigObject withoutPath(final Path path) {
        final String key = path.first();
        final Path next = path.remainder();
        AbstractConfigValue v = this.value.get(key);
        if (v != null && next != null && v instanceof AbstractConfigObject) {
            v = ((AbstractConfigObject)v).withoutPath(next);
            final Map<String, AbstractConfigValue> updated = new HashMap<String, AbstractConfigValue>(this.value);
            updated.put(key, v);
            return new SimpleConfigObject(this.origin(), updated, ResolveStatus.fromValues(updated.values()), this.ignoresFallbacks);
        }
        if (next != null || v == null) {
            return this;
        }
        final Map<String, AbstractConfigValue> smaller = new HashMap<String, AbstractConfigValue>(this.value.size() - 1);
        for (final Map.Entry<String, AbstractConfigValue> old : this.value.entrySet()) {
            if (!old.getKey().equals(key)) {
                smaller.put(old.getKey(), old.getValue());
            }
        }
        return new SimpleConfigObject(this.origin(), smaller, ResolveStatus.fromValues(smaller.values()), this.ignoresFallbacks);
    }
    
    @Override
    public SimpleConfigObject withValue(final String key, final ConfigValue v) {
        if (v == null) {
            throw new ConfigException.BugOrBroken("Trying to store null ConfigValue in a ConfigObject");
        }
        Map<String, AbstractConfigValue> newMap;
        if (this.value.isEmpty()) {
            newMap = Collections.singletonMap(key, v);
        }
        else {
            newMap = new HashMap<String, AbstractConfigValue>(this.value);
            newMap.put(key, (AbstractConfigValue)v);
        }
        return new SimpleConfigObject(this.origin(), newMap, ResolveStatus.fromValues(newMap.values()), this.ignoresFallbacks);
    }
    
    @Override
    SimpleConfigObject withValue(final Path path, final ConfigValue v) {
        final String key = path.first();
        final Path next = path.remainder();
        if (next == null) {
            return this.withValue(key, v);
        }
        final AbstractConfigValue child = this.value.get(key);
        if (child != null && child instanceof AbstractConfigObject) {
            return this.withValue(key, ((AbstractConfigObject)child).withValue(next, v));
        }
        final SimpleConfig subtree = ((AbstractConfigValue)v).atPath(SimpleConfigOrigin.newSimple("withValue(" + next.render() + ")"), next);
        return this.withValue(key, subtree.root());
    }
    
    protected AbstractConfigValue attemptPeekWithPartialResolve(final String key) {
        return this.value.get(key);
    }
    
    private SimpleConfigObject newCopy(final ResolveStatus newStatus, final ConfigOrigin newOrigin, final boolean newIgnoresFallbacks) {
        return new SimpleConfigObject(newOrigin, this.value, newStatus, newIgnoresFallbacks);
    }
    
    @Override
    protected SimpleConfigObject newCopy(final ResolveStatus newStatus, final ConfigOrigin newOrigin) {
        return this.newCopy(newStatus, newOrigin, this.ignoresFallbacks);
    }
    
    @Override
    protected SimpleConfigObject withFallbacksIgnored() {
        if (this.ignoresFallbacks) {
            return this;
        }
        return this.newCopy(this.resolveStatus(), this.origin(), true);
    }
    
    @Override
    ResolveStatus resolveStatus() {
        return ResolveStatus.fromBoolean(this.resolved);
    }
    
    @Override
    public SimpleConfigObject replaceChild(final AbstractConfigValue child, final AbstractConfigValue replacement) {
        final HashMap<String, AbstractConfigValue> newChildren = new HashMap<String, AbstractConfigValue>(this.value);
        for (final Map.Entry<String, AbstractConfigValue> old : newChildren.entrySet()) {
            if (old.getValue() == child) {
                if (replacement != null) {
                    old.setValue(replacement);
                }
                else {
                    newChildren.remove(old.getKey());
                }
                return new SimpleConfigObject(this.origin(), newChildren, ResolveStatus.fromValues(newChildren.values()), this.ignoresFallbacks);
            }
        }
        throw new ConfigException.BugOrBroken("SimpleConfigObject.replaceChild did not find " + child + " in " + this);
    }
    
    @Override
    public boolean hasDescendant(final AbstractConfigValue descendant) {
        for (final AbstractConfigValue child : this.value.values()) {
            if (child == descendant) {
                return true;
            }
        }
        for (final AbstractConfigValue child : this.value.values()) {
            if (child instanceof Container && ((Container)child).hasDescendant(descendant)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    protected boolean ignoresFallbacks() {
        return this.ignoresFallbacks;
    }
    
    @Override
    public Map<String, Object> unwrapped() {
        final Map<String, Object> m = new HashMap<String, Object>();
        for (final Map.Entry<String, AbstractConfigValue> e : this.value.entrySet()) {
            m.put(e.getKey(), e.getValue().unwrapped());
        }
        return m;
    }
    
    @Override
    protected SimpleConfigObject mergedWithObject(final AbstractConfigObject abstractFallback) {
        this.requireNotIgnoringFallbacks();
        if (!(abstractFallback instanceof SimpleConfigObject)) {
            throw new ConfigException.BugOrBroken("should not be reached (merging non-SimpleConfigObject)");
        }
        final SimpleConfigObject fallback = (SimpleConfigObject)abstractFallback;
        boolean changed = false;
        boolean allResolved = true;
        final Map<String, AbstractConfigValue> merged = new HashMap<String, AbstractConfigValue>();
        final Set<String> allKeys = new HashSet<String>();
        allKeys.addAll(this.keySet());
        allKeys.addAll(fallback.keySet());
        for (final String key : allKeys) {
            final AbstractConfigValue first = this.value.get(key);
            final AbstractConfigValue second = fallback.value.get(key);
            AbstractConfigValue kept;
            if (first == null) {
                kept = second;
            }
            else if (second == null) {
                kept = first;
            }
            else {
                kept = first.withFallback((ConfigMergeable)second);
            }
            merged.put(key, kept);
            if (first != kept) {
                changed = true;
            }
            if (kept.resolveStatus() == ResolveStatus.UNRESOLVED) {
                allResolved = false;
            }
        }
        final ResolveStatus newResolveStatus = ResolveStatus.fromBoolean(allResolved);
        final boolean newIgnoresFallbacks = fallback.ignoresFallbacks();
        if (changed) {
            return new SimpleConfigObject(AbstractConfigObject.mergeOrigins(this, fallback), merged, newResolveStatus, newIgnoresFallbacks);
        }
        if (newResolveStatus != this.resolveStatus() || newIgnoresFallbacks != this.ignoresFallbacks()) {
            return this.newCopy(newResolveStatus, this.origin(), newIgnoresFallbacks);
        }
        return this;
    }
    
    private SimpleConfigObject modify(final NoExceptionsModifier modifier) {
        try {
            return this.modifyMayThrow(modifier);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ConfigException.BugOrBroken("unexpected checked exception", e2);
        }
    }
    
    private SimpleConfigObject modifyMayThrow(final Modifier modifier) throws Exception {
        Map<String, AbstractConfigValue> changes = null;
        for (final String k : this.keySet()) {
            final AbstractConfigValue v = this.value.get(k);
            final AbstractConfigValue modified = modifier.modifyChildMayThrow(k, v);
            if (modified != v) {
                if (changes == null) {
                    changes = new HashMap<String, AbstractConfigValue>();
                }
                changes.put(k, modified);
            }
        }
        if (changes == null) {
            return this;
        }
        final Map<String, AbstractConfigValue> modified2 = new HashMap<String, AbstractConfigValue>();
        boolean sawUnresolved = false;
        for (final String i : this.keySet()) {
            if (changes.containsKey(i)) {
                final AbstractConfigValue newValue = changes.get(i);
                if (newValue == null) {
                    continue;
                }
                modified2.put(i, newValue);
                if (newValue.resolveStatus() != ResolveStatus.UNRESOLVED) {
                    continue;
                }
                sawUnresolved = true;
            }
            else {
                final AbstractConfigValue newValue = this.value.get(i);
                modified2.put(i, newValue);
                if (newValue.resolveStatus() != ResolveStatus.UNRESOLVED) {
                    continue;
                }
                sawUnresolved = true;
            }
        }
        return new SimpleConfigObject(this.origin(), modified2, sawUnresolved ? ResolveStatus.UNRESOLVED : ResolveStatus.RESOLVED, this.ignoresFallbacks());
    }
    
    @Override
    ResolveResult<? extends AbstractConfigObject> resolveSubstitutions(final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        if (this.resolveStatus() == ResolveStatus.RESOLVED) {
            return ResolveResult.make(context, (AbstractConfigObject)this);
        }
        final ResolveSource sourceWithParent = source.pushParent(this);
        try {
            final ResolveModifier modifier = new ResolveModifier(context, sourceWithParent);
            final AbstractConfigValue value = this.modifyMayThrow(modifier);
            return ResolveResult.make(modifier.context, value).asObjectResult();
        }
        catch (NotPossibleToResolve e) {
            throw e;
        }
        catch (RuntimeException e2) {
            throw e2;
        }
        catch (Exception e3) {
            throw new ConfigException.BugOrBroken("unexpected checked exception", e3);
        }
    }
    
    @Override
    SimpleConfigObject relativized(final Path prefix) {
        return this.modify(new NoExceptionsModifier() {
            public AbstractConfigValue modifyChild(final String key, final AbstractConfigValue v) {
                return v.relativized(prefix);
            }
        });
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        if (this.isEmpty()) {
            sb.append("{}");
        }
        else {
            final boolean outerBraces = options.getJson() || !atRoot;
            int innerIndent;
            if (outerBraces) {
                innerIndent = indent + 1;
                sb.append("{");
                if (options.getFormatted()) {
                    sb.append('\n');
                }
            }
            else {
                innerIndent = indent;
            }
            int separatorCount = 0;
            final String[] keys = this.keySet().toArray(new String[this.size()]);
            Arrays.sort(keys, new RenderComparator());
            for (final String k : keys) {
                final AbstractConfigValue v = this.value.get(k);
                if (options.getOriginComments()) {
                    final String[] split;
                    final String[] lines = split = v.origin().description().split("\n");
                    for (final String l : split) {
                        AbstractConfigValue.indent(sb, indent + 1, options);
                        sb.append('#');
                        if (!l.isEmpty()) {
                            sb.append(' ');
                        }
                        sb.append(l);
                        sb.append("\n");
                    }
                }
                if (options.getComments()) {
                    for (final String comment : v.origin().comments()) {
                        AbstractConfigValue.indent(sb, innerIndent, options);
                        sb.append("#");
                        if (!comment.startsWith(" ")) {
                            sb.append(' ');
                        }
                        sb.append(comment);
                        sb.append("\n");
                    }
                }
                AbstractConfigValue.indent(sb, innerIndent, options);
                v.render(sb, innerIndent, false, k, options);
                if (options.getFormatted()) {
                    if (options.getJson()) {
                        sb.append(",");
                        separatorCount = 2;
                    }
                    else {
                        separatorCount = 1;
                    }
                    sb.append('\n');
                }
                else {
                    sb.append(",");
                    separatorCount = 1;
                }
            }
            sb.setLength(sb.length() - separatorCount);
            if (outerBraces) {
                if (options.getFormatted()) {
                    sb.append('\n');
                    if (outerBraces) {
                        AbstractConfigValue.indent(sb, indent, options);
                    }
                }
                sb.append("}");
            }
        }
        if (atRoot && options.getFormatted()) {
            sb.append('\n');
        }
    }
    
    @Override
    public AbstractConfigValue get(final Object key) {
        return this.value.get(key);
    }
    
    private static boolean mapEquals(final Map<String, ConfigValue> a, final Map<String, ConfigValue> b) {
        if (a == b) {
            return true;
        }
        final Set<String> aKeys = a.keySet();
        final Set<String> bKeys = b.keySet();
        if (!aKeys.equals(bKeys)) {
            return false;
        }
        for (final String key : aKeys) {
            if (!a.get(key).equals(b.get(key))) {
                return false;
            }
        }
        return true;
    }
    
    private static int mapHash(final Map<String, ConfigValue> m) {
        final List<String> keys = new ArrayList<String>();
        keys.addAll(m.keySet());
        Collections.sort(keys);
        int valuesHash = 0;
        for (final String k : keys) {
            valuesHash += m.get(k).hashCode();
        }
        return 41 * (41 + keys.hashCode()) + valuesHash;
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigObject;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigObject && this.canEqual(other) && mapEquals(this, (Map<String, ConfigValue>)other);
    }
    
    @Override
    public int hashCode() {
        return mapHash(this);
    }
    
    @Override
    public boolean containsKey(final Object key) {
        return this.value.containsKey(key);
    }
    
    @Override
    public Set<String> keySet() {
        return this.value.keySet();
    }
    
    @Override
    public boolean containsValue(final Object v) {
        return this.value.containsValue(v);
    }
    
    @Override
    public Set<Map.Entry<String, ConfigValue>> entrySet() {
        final HashSet<Map.Entry<String, ConfigValue>> entries = new HashSet<Map.Entry<String, ConfigValue>>();
        for (final Map.Entry<String, AbstractConfigValue> e : this.value.entrySet()) {
            entries.add((Map.Entry<String, AbstractConfigValue>)new AbstractMap.SimpleImmutableEntry<String, AbstractConfigValue>(e.getKey(), e.getValue()));
        }
        return entries;
    }
    
    @Override
    public boolean isEmpty() {
        return this.value.isEmpty();
    }
    
    @Override
    public int size() {
        return this.value.size();
    }
    
    @Override
    public Collection<ConfigValue> values() {
        return new HashSet<ConfigValue>(this.value.values());
    }
    
    static final SimpleConfigObject empty() {
        return SimpleConfigObject.emptyInstance;
    }
    
    static final SimpleConfigObject empty(final ConfigOrigin origin) {
        if (origin == null) {
            return empty();
        }
        return new SimpleConfigObject(origin, Collections.emptyMap());
    }
    
    static final SimpleConfigObject emptyMissing(final ConfigOrigin baseOrigin) {
        return new SimpleConfigObject(SimpleConfigOrigin.newSimple(baseOrigin.description() + " (not found)"), Collections.emptyMap());
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
    
    static {
        emptyInstance = empty(SimpleConfigOrigin.newSimple("empty config"));
    }
    
    private static final class ResolveModifier implements Modifier
    {
        final Path originalRestrict;
        ResolveContext context;
        final ResolveSource source;
        
        ResolveModifier(final ResolveContext context, final ResolveSource source) {
            this.context = context;
            this.source = source;
            this.originalRestrict = context.restrictToChild();
        }
        
        @Override
        public AbstractConfigValue modifyChildMayThrow(final String key, final AbstractConfigValue v) throws NotPossibleToResolve {
            if (!this.context.isRestrictedToChild()) {
                final ResolveResult<? extends AbstractConfigValue> result = this.context.unrestricted().resolve(v, this.source);
                this.context = result.context.unrestricted().restrict(this.originalRestrict);
                return (AbstractConfigValue)result.value;
            }
            if (!key.equals(this.context.restrictToChild().first())) {
                return v;
            }
            final Path remainder = this.context.restrictToChild().remainder();
            if (remainder != null) {
                final ResolveResult<? extends AbstractConfigValue> result2 = this.context.restrict(remainder).resolve(v, this.source);
                this.context = result2.context.unrestricted().restrict(this.originalRestrict);
                return (AbstractConfigValue)result2.value;
            }
            return v;
        }
    }
    
    private static final class RenderComparator implements Comparator<String>, Serializable
    {
        private static final long serialVersionUID = 1L;
        
        private static boolean isAllDigits(final String s) {
            final int length = s.length();
            if (length == 0) {
                return false;
            }
            for (int i = 0; i < length; ++i) {
                final char c = s.charAt(i);
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public int compare(final String a, final String b) {
            final boolean aDigits = isAllDigits(a);
            final boolean bDigits = isAllDigits(b);
            if (aDigits && bDigits) {
                return Integer.compare(Integer.parseInt(a), Integer.parseInt(b));
            }
            if (aDigits) {
                return -1;
            }
            if (bDigits) {
                return 1;
            }
            return a.compareTo(b);
        }
    }
}
