// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input.concurrent;

class Entry<T>
{
    final T entry;
    final int index;
    
    Entry(final T entry, final int index) {
        this.entry = entry;
        this.index = index;
    }
    
    public T get() {
        return this.entry;
    }
}
