// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.io.ObjectStreamException;
import java.util.ListIterator;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigRenderOptions;
import java.util.Iterator;
import java.util.ArrayList;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import java.util.Collection;
import com.typesafe.config.ConfigOrigin;
import java.util.List;
import java.io.Serializable;
import com.typesafe.config.ConfigList;

final class SimpleConfigList extends AbstractConfigValue implements ConfigList, Container, Serializable
{
    private static final long serialVersionUID = 2L;
    private final List<AbstractConfigValue> value;
    private final boolean resolved;
    
    SimpleConfigList(final ConfigOrigin origin, final List<AbstractConfigValue> value) {
        this(origin, value, ResolveStatus.fromValues(value));
    }
    
    SimpleConfigList(final ConfigOrigin origin, final List<AbstractConfigValue> value, final ResolveStatus status) {
        super(origin);
        this.value = value;
        this.resolved = (status == ResolveStatus.RESOLVED);
        if (status != ResolveStatus.fromValues(value)) {
            throw new ConfigException.BugOrBroken("SimpleConfigList created with wrong resolve status: " + this);
        }
    }
    
    @Override
    public ConfigValueType valueType() {
        return ConfigValueType.LIST;
    }
    
    @Override
    public List<Object> unwrapped() {
        final List<Object> list = new ArrayList<Object>();
        for (final AbstractConfigValue v : this.value) {
            list.add(v.unwrapped());
        }
        return list;
    }
    
    @Override
    ResolveStatus resolveStatus() {
        return ResolveStatus.fromBoolean(this.resolved);
    }
    
    @Override
    public SimpleConfigList replaceChild(final AbstractConfigValue child, final AbstractConfigValue replacement) {
        final List<AbstractConfigValue> newList = AbstractConfigValue.replaceChildInList(this.value, child, replacement);
        if (newList == null) {
            return null;
        }
        return new SimpleConfigList(this.origin(), newList);
    }
    
    @Override
    public boolean hasDescendant(final AbstractConfigValue descendant) {
        return AbstractConfigValue.hasDescendantInList(this.value, descendant);
    }
    
    private SimpleConfigList modify(final NoExceptionsModifier modifier, final ResolveStatus newResolveStatus) {
        try {
            return this.modifyMayThrow(modifier, newResolveStatus);
        }
        catch (RuntimeException e) {
            throw e;
        }
        catch (Exception e2) {
            throw new ConfigException.BugOrBroken("unexpected checked exception", e2);
        }
    }
    
    private SimpleConfigList modifyMayThrow(final Modifier modifier, final ResolveStatus newResolveStatus) throws Exception {
        List<AbstractConfigValue> changed = null;
        int i = 0;
        for (final AbstractConfigValue v : this.value) {
            final AbstractConfigValue modified = modifier.modifyChildMayThrow(null, v);
            if (changed == null && modified != v) {
                changed = new ArrayList<AbstractConfigValue>();
                for (int j = 0; j < i; ++j) {
                    changed.add(this.value.get(j));
                }
            }
            if (changed != null && modified != null) {
                changed.add(modified);
            }
            ++i;
        }
        if (changed == null) {
            return this;
        }
        if (newResolveStatus != null) {
            return new SimpleConfigList(this.origin(), changed, newResolveStatus);
        }
        return new SimpleConfigList(this.origin(), changed);
    }
    
    @Override
    ResolveResult<? extends SimpleConfigList> resolveSubstitutions(final ResolveContext context, final ResolveSource source) throws NotPossibleToResolve {
        if (this.resolved) {
            return ResolveResult.make(context, this);
        }
        if (context.isRestrictedToChild()) {
            return ResolveResult.make(context, this);
        }
        try {
            final ResolveModifier modifier = new ResolveModifier(context, source.pushParent(this));
            final SimpleConfigList value = this.modifyMayThrow(modifier, context.options().getAllowUnresolved() ? null : ResolveStatus.RESOLVED);
            return ResolveResult.make(modifier.context, value);
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
    SimpleConfigList relativized(final Path prefix) {
        return this.modify(new NoExceptionsModifier() {
            public AbstractConfigValue modifyChild(final String key, final AbstractConfigValue v) {
                return v.relativized(prefix);
            }
        }, this.resolveStatus());
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof SimpleConfigList;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof SimpleConfigList && this.canEqual(other) && (this.value == ((SimpleConfigList)other).value || this.value.equals(((SimpleConfigList)other).value));
    }
    
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        if (this.value.isEmpty()) {
            sb.append("[]");
        }
        else {
            sb.append("[");
            if (options.getFormatted()) {
                sb.append('\n');
            }
            for (final AbstractConfigValue v : this.value) {
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
                        AbstractConfigValue.indent(sb, indent + 1, options);
                        sb.append("# ");
                        sb.append(comment);
                        sb.append("\n");
                    }
                }
                AbstractConfigValue.indent(sb, indent + 1, options);
                v.render(sb, indent + 1, atRoot, options);
                sb.append(",");
                if (options.getFormatted()) {
                    sb.append('\n');
                }
            }
            sb.setLength(sb.length() - 1);
            if (options.getFormatted()) {
                sb.setLength(sb.length() - 1);
                sb.append('\n');
                AbstractConfigValue.indent(sb, indent, options);
            }
            sb.append("]");
        }
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.value.contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.value.containsAll(c);
    }
    
    @Override
    public AbstractConfigValue get(final int index) {
        return this.value.get(index);
    }
    
    @Override
    public int indexOf(final Object o) {
        return this.value.indexOf(o);
    }
    
    @Override
    public boolean isEmpty() {
        return this.value.isEmpty();
    }
    
    @Override
    public Iterator<ConfigValue> iterator() {
        final Iterator<AbstractConfigValue> i = this.value.iterator();
        return new Iterator<ConfigValue>() {
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }
            
            @Override
            public ConfigValue next() {
                return i.next();
            }
            
            @Override
            public void remove() {
                throw weAreImmutable("iterator().remove");
            }
        };
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        return this.value.lastIndexOf(o);
    }
    
    private static ListIterator<ConfigValue> wrapListIterator(final ListIterator<AbstractConfigValue> i) {
        return new ListIterator<ConfigValue>() {
            @Override
            public boolean hasNext() {
                return i.hasNext();
            }
            
            @Override
            public ConfigValue next() {
                return i.next();
            }
            
            @Override
            public void remove() {
                throw weAreImmutable("listIterator().remove");
            }
            
            @Override
            public void add(final ConfigValue arg0) {
                throw weAreImmutable("listIterator().add");
            }
            
            @Override
            public boolean hasPrevious() {
                return i.hasPrevious();
            }
            
            @Override
            public int nextIndex() {
                return i.nextIndex();
            }
            
            @Override
            public ConfigValue previous() {
                return i.previous();
            }
            
            @Override
            public int previousIndex() {
                return i.previousIndex();
            }
            
            @Override
            public void set(final ConfigValue arg0) {
                throw weAreImmutable("listIterator().set");
            }
        };
    }
    
    @Override
    public ListIterator<ConfigValue> listIterator() {
        return wrapListIterator(this.value.listIterator());
    }
    
    @Override
    public ListIterator<ConfigValue> listIterator(final int index) {
        return wrapListIterator(this.value.listIterator(index));
    }
    
    @Override
    public int size() {
        return this.value.size();
    }
    
    @Override
    public List<ConfigValue> subList(final int fromIndex, final int toIndex) {
        final List<ConfigValue> list = new ArrayList<ConfigValue>();
        for (final AbstractConfigValue v : this.value.subList(fromIndex, toIndex)) {
            list.add(v);
        }
        return list;
    }
    
    @Override
    public Object[] toArray() {
        return this.value.toArray();
    }
    
    @Override
    public <T> T[] toArray(final T[] a) {
        return this.value.toArray(a);
    }
    
    private static UnsupportedOperationException weAreImmutable(final String method) {
        return new UnsupportedOperationException("ConfigList is immutable, you can't call List.'" + method + "'");
    }
    
    @Override
    public boolean add(final ConfigValue e) {
        throw weAreImmutable("add");
    }
    
    @Override
    public void add(final int index, final ConfigValue element) {
        throw weAreImmutable("add");
    }
    
    @Override
    public boolean addAll(final Collection<? extends ConfigValue> c) {
        throw weAreImmutable("addAll");
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends ConfigValue> c) {
        throw weAreImmutable("addAll");
    }
    
    @Override
    public void clear() {
        throw weAreImmutable("clear");
    }
    
    @Override
    public boolean remove(final Object o) {
        throw weAreImmutable("remove");
    }
    
    @Override
    public ConfigValue remove(final int index) {
        throw weAreImmutable("remove");
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw weAreImmutable("removeAll");
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw weAreImmutable("retainAll");
    }
    
    @Override
    public ConfigValue set(final int index, final ConfigValue element) {
        throw weAreImmutable("set");
    }
    
    @Override
    protected SimpleConfigList newCopy(final ConfigOrigin newOrigin) {
        return new SimpleConfigList(newOrigin, this.value);
    }
    
    final SimpleConfigList concatenate(final SimpleConfigList other) {
        final ConfigOrigin combinedOrigin = SimpleConfigOrigin.mergeOrigins(this.origin(), other.origin());
        final List<AbstractConfigValue> combined = new ArrayList<AbstractConfigValue>(this.value.size() + other.value.size());
        combined.addAll(this.value);
        combined.addAll(other.value);
        return new SimpleConfigList(combinedOrigin, combined);
    }
    
    private Object writeReplace() throws ObjectStreamException {
        return new SerializedConfigValue(this);
    }
    
    @Override
    public SimpleConfigList withOrigin(final ConfigOrigin origin) {
        return (SimpleConfigList)super.withOrigin(origin);
    }
    
    private static class ResolveModifier implements Modifier
    {
        ResolveContext context;
        final ResolveSource source;
        
        ResolveModifier(final ResolveContext context, final ResolveSource source) {
            this.context = context;
            this.source = source;
        }
        
        @Override
        public AbstractConfigValue modifyChildMayThrow(final String key, final AbstractConfigValue v) throws NotPossibleToResolve {
            final ResolveResult<? extends AbstractConfigValue> result = this.context.resolve(v, this.source);
            this.context = result.context;
            return (AbstractConfigValue)result.value;
        }
    }
}
