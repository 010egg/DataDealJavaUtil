// 
// Decompiled by Procyon v0.5.36
// 

package com.zaxxer.hikari.util;

import java.util.Comparator;
import java.util.function.UnaryOperator;
import java.util.function.Predicate;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.ListIterator;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Iterator;
import java.lang.reflect.Array;
import java.io.Serializable;
import java.util.RandomAccess;
import java.util.List;

public final class FastList<T> implements List<T>, RandomAccess, Serializable
{
    private static final long serialVersionUID = -4598088075242913858L;
    private final Class<?> clazz;
    private T[] elementData;
    private int size;
    
    public FastList(final Class<?> clazz) {
        this.elementData = (T[])Array.newInstance(clazz, 32);
        this.clazz = clazz;
    }
    
    public FastList(final Class<?> clazz, final int capacity) {
        this.elementData = (T[])Array.newInstance(clazz, capacity);
        this.clazz = clazz;
    }
    
    @Override
    public boolean add(final T element) {
        if (this.size < this.elementData.length) {
            this.elementData[this.size++] = element;
        }
        else {
            final int oldCapacity = this.elementData.length;
            final int newCapacity = oldCapacity << 1;
            final T[] newElementData = (T[])Array.newInstance(this.clazz, newCapacity);
            System.arraycopy(this.elementData, 0, newElementData, 0, oldCapacity);
            newElementData[this.size++] = element;
            this.elementData = newElementData;
        }
        return true;
    }
    
    @Override
    public T get(final int index) {
        return this.elementData[index];
    }
    
    public T removeLast() {
        final T[] elementData = this.elementData;
        final int size = this.size - 1;
        this.size = size;
        final T element = elementData[size];
        this.elementData[this.size] = null;
        return element;
    }
    
    @Override
    public boolean remove(final Object element) {
        for (int index = this.size - 1; index >= 0; --index) {
            if (element == this.elementData[index]) {
                final int numMoved = this.size - index - 1;
                if (numMoved > 0) {
                    System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
                }
                this.elementData[--this.size] = null;
                return true;
            }
        }
        return false;
    }
    
    @Override
    public void clear() {
        for (int i = 0; i < this.size; ++i) {
            this.elementData[i] = null;
        }
        this.size = 0;
    }
    
    @Override
    public int size() {
        return this.size;
    }
    
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
    
    @Override
    public T set(final int index, final T element) {
        final T old = this.elementData[index];
        this.elementData[index] = element;
        return old;
    }
    
    @Override
    public T remove(final int index) {
        if (this.size == 0) {
            return null;
        }
        final T old = this.elementData[index];
        final int numMoved = this.size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(this.elementData, index + 1, this.elementData, index, numMoved);
        }
        this.elementData[--this.size] = null;
        return old;
    }
    
    @Override
    public boolean contains(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int index;
            
            @Override
            public boolean hasNext() {
                return this.index < FastList.this.size;
            }
            
            @Override
            public T next() {
                if (this.index < FastList.this.size) {
                    return FastList.this.elementData[this.index++];
                }
                throw new NoSuchElementException("No more elements in FastList");
            }
        };
    }
    
    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public <E> E[] toArray(final E[] a) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean addAll(final int index, final Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean retainAll(final Collection<?> c) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void add(final int index, final T element) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int indexOf(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public int lastIndexOf(final Object o) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public ListIterator<T> listIterator(final int index) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public List<T> subList(final int fromIndex, final int toIndex) {
        throw new UnsupportedOperationException();
    }
    
    public Object clone() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void forEach(final Consumer<? super T> action) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public Spliterator<T> spliterator() {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public boolean removeIf(final Predicate<? super T> filter) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void replaceAll(final UnaryOperator<T> operator) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void sort(final Comparator<? super T> c) {
        throw new UnsupportedOperationException();
    }
}
