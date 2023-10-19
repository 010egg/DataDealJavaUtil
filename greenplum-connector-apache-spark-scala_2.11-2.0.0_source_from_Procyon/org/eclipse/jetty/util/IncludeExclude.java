// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util;

import java.util.HashSet;
import java.util.Set;

public class IncludeExclude<ITEM>
{
    private final Set<ITEM> _includes;
    private final Predicate<ITEM> _includePredicate;
    private final Set<ITEM> _excludes;
    private final Predicate<ITEM> _excludePredicate;
    
    public IncludeExclude() {
        this(HashSet.class);
    }
    
    public <SET extends Set<ITEM>> IncludeExclude(final Class<SET> setClass) {
        try {
            this._includes = setClass.newInstance();
            this._excludes = setClass.newInstance();
            if (this._includes instanceof Predicate) {
                this._includePredicate = (Predicate<ITEM>)(Predicate)this._includes;
            }
            else {
                this._includePredicate = new SetContainsPredicate<ITEM>(this._includes);
            }
            if (this._excludes instanceof Predicate) {
                this._excludePredicate = (Predicate<ITEM>)(Predicate)this._excludes;
            }
            else {
                this._excludePredicate = new SetContainsPredicate<ITEM>(this._excludes);
            }
        }
        catch (InstantiationException | IllegalAccessException ex2) {
            final ReflectiveOperationException ex;
            final ReflectiveOperationException e = ex;
            throw new RuntimeException(e);
        }
    }
    
    public <SET extends Set<ITEM>> IncludeExclude(final Set<ITEM> includeSet, final Predicate<ITEM> includePredicate, final Set<ITEM> excludeSet, final Predicate<ITEM> excludePredicate) {
        this._includes = includeSet;
        this._includePredicate = includePredicate;
        this._excludes = excludeSet;
        this._excludePredicate = excludePredicate;
    }
    
    public void include(final ITEM element) {
        this._includes.add(element);
    }
    
    public void include(final ITEM... element) {
        for (final ITEM e : element) {
            this._includes.add(e);
        }
    }
    
    public void exclude(final ITEM element) {
        this._excludes.add(element);
    }
    
    public void exclude(final ITEM... element) {
        for (final ITEM e : element) {
            this._excludes.add(e);
        }
    }
    
    public boolean matches(final ITEM e) {
        return (this._includes.isEmpty() || this._includePredicate.test(e)) && !this._excludePredicate.test(e);
    }
    
    public int size() {
        return this._includes.size() + this._excludes.size();
    }
    
    public Set<ITEM> getIncluded() {
        return this._includes;
    }
    
    public Set<ITEM> getExcluded() {
        return this._excludes;
    }
    
    public void clear() {
        this._includes.clear();
        this._excludes.clear();
    }
    
    @Override
    public String toString() {
        return String.format("%s@%x{i=%s,ip=%s,e=%s,ep=%s}", this.getClass().getSimpleName(), this.hashCode(), this._includes, this._includePredicate, this._excludes, this._excludePredicate);
    }
    
    private static class SetContainsPredicate<ITEM> implements Predicate<ITEM>
    {
        private final Set<ITEM> set;
        
        public SetContainsPredicate(final Set<ITEM> set) {
            this.set = set;
        }
        
        @Override
        public boolean test(final ITEM item) {
            return this.set.contains(item);
        }
    }
}
