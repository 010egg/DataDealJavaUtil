// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input.concurrent;

import java.util.Arrays;

abstract class FixedInstancePool<T>
{
    final Entry<T>[] instancePool;
    private final int[] instanceIndexes;
    private int head;
    private int tail;
    int count;
    private int lastInstanceIndex;
    
    FixedInstancePool(final int size) {
        this.head = 0;
        this.tail = 0;
        this.count = 0;
        this.lastInstanceIndex = 0;
        this.instancePool = (Entry<T>[])new Entry[size];
        Arrays.fill(this.instanceIndexes = new int[size], -1);
        this.instancePool[0] = new Entry<T>(this.newInstance(), 0);
        this.instanceIndexes[0] = 0;
    }
    
    protected abstract T newInstance();
    
    public synchronized Entry<T> allocate() {
        while (this.count == this.instancePool.length) {
            try {
                this.wait(50L);
                continue;
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return new Entry<T>(this.newInstance(), -1);
            }
            break;
        }
        int index = this.instanceIndexes[this.head];
        if (index == -1) {
            index = ++this.lastInstanceIndex;
            this.instanceIndexes[index] = index;
            this.instancePool[index] = new Entry<T>(this.newInstance(), index);
        }
        final Entry<T> out = this.instancePool[index];
        ++this.head;
        if (this.head == this.instancePool.length) {
            this.head = 0;
        }
        ++this.count;
        return out;
    }
    
    public synchronized void release(final Entry<T> e) {
        if (e.index != -1) {
            this.instanceIndexes[this.tail++] = e.index;
            if (this.tail == this.instancePool.length) {
                this.tail = 0;
            }
            --this.count;
        }
        this.notify();
    }
}
