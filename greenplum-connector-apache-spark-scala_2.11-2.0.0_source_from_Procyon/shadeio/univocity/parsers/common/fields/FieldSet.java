// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class FieldSet<T>
{
    private final List<T> fields;
    private final List<FieldSet<T>> wrappedFieldSets;
    
    public FieldSet() {
        this.fields = new ArrayList<T>();
        this.wrappedFieldSets = Collections.emptyList();
    }
    
    public FieldSet(final List<FieldSet<T>> wrappedFieldSets) {
        this.fields = new ArrayList<T>();
        this.wrappedFieldSets = wrappedFieldSets;
        if (this.wrappedFieldSets.contains(this)) {
            this.wrappedFieldSets.remove(this);
        }
    }
    
    public List<T> get() {
        return new ArrayList<T>((Collection<? extends T>)this.fields);
    }
    
    public FieldSet<T> set(final T... fields) {
        this.fields.clear();
        this.add(fields);
        for (final FieldSet<T> wrapped : this.wrappedFieldSets) {
            wrapped.set(fields);
        }
        return this;
    }
    
    public FieldSet<T> add(final T... fields) {
        for (final T field : fields) {
            this.addElement(field);
        }
        for (final FieldSet<T> wrapped : this.wrappedFieldSets) {
            wrapped.add(fields);
        }
        return this;
    }
    
    private void addElement(final T field) {
        this.fields.add(field);
    }
    
    public FieldSet<T> set(final Collection<T> fields) {
        this.fields.clear();
        this.add(fields);
        for (final FieldSet<T> wrapped : this.wrappedFieldSets) {
            wrapped.set(fields);
        }
        return this;
    }
    
    public FieldSet<T> add(final Collection<T> fields) {
        for (final T field : fields) {
            this.addElement(field);
        }
        for (final FieldSet<T> wrapped : this.wrappedFieldSets) {
            wrapped.add(fields);
        }
        return this;
    }
    
    public FieldSet<T> remove(final T... fields) {
        for (final T field : fields) {
            this.fields.remove(field);
        }
        for (final FieldSet<T> wrapped : this.wrappedFieldSets) {
            wrapped.remove(fields);
        }
        return this;
    }
    
    public FieldSet<T> remove(final Collection<T> fields) {
        this.fields.removeAll(fields);
        for (final FieldSet<T> wrapped : this.wrappedFieldSets) {
            wrapped.remove(fields);
        }
        return this;
    }
    
    public String describe() {
        return "field selection: " + this.fields.toString();
    }
    
    @Override
    public String toString() {
        return this.fields.toString();
    }
}
