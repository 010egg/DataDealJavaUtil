// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import org.eclipse.jetty.util.BufferUtil;
import java.nio.ByteBuffer;

public class ArrayByteBufferPool implements ByteBufferPool
{
    private final int _min;
    private final Bucket[] _direct;
    private final Bucket[] _indirect;
    private final int _inc;
    
    public ArrayByteBufferPool() {
        this(0, 1024, 65536);
    }
    
    public ArrayByteBufferPool(final int minSize, final int increment, final int maxSize) {
        if (minSize >= increment) {
            throw new IllegalArgumentException("minSize >= increment");
        }
        if (maxSize % increment != 0 || increment >= maxSize) {
            throw new IllegalArgumentException("increment must be a divisor of maxSize");
        }
        this._min = minSize;
        this._inc = increment;
        this._direct = new Bucket[maxSize / increment];
        this._indirect = new Bucket[maxSize / increment];
        int size = 0;
        for (int i = 0; i < this._direct.length; ++i) {
            size += this._inc;
            this._direct[i] = new Bucket(size);
            this._indirect[i] = new Bucket(size);
        }
    }
    
    @Override
    public ByteBuffer acquire(final int size, final boolean direct) {
        final Bucket bucket = this.bucketFor(size, direct);
        ByteBuffer buffer = (bucket == null) ? null : bucket._queue.poll();
        if (buffer == null) {
            final int capacity = (bucket == null) ? size : bucket._size;
            buffer = (direct ? BufferUtil.allocateDirect(capacity) : BufferUtil.allocate(capacity));
        }
        return buffer;
    }
    
    @Override
    public void release(final ByteBuffer buffer) {
        if (buffer != null) {
            final Bucket bucket = this.bucketFor(buffer.capacity(), buffer.isDirect());
            if (bucket != null) {
                BufferUtil.clear(buffer);
                bucket._queue.offer(buffer);
            }
        }
    }
    
    public void clear() {
        for (int i = 0; i < this._direct.length; ++i) {
            this._direct[i]._queue.clear();
            this._indirect[i]._queue.clear();
        }
    }
    
    private Bucket bucketFor(final int size, final boolean direct) {
        if (size <= this._min) {
            return null;
        }
        final int b = (size - 1) / this._inc;
        if (b >= this._direct.length) {
            return null;
        }
        final Bucket bucket = direct ? this._direct[b] : this._indirect[b];
        return bucket;
    }
    
    Bucket[] bucketsFor(final boolean direct) {
        return direct ? this._direct : this._indirect;
    }
    
    public static class Bucket
    {
        public final int _size;
        public final Queue<ByteBuffer> _queue;
        
        Bucket(final int size) {
            this._queue = new ConcurrentLinkedQueue<ByteBuffer>();
            this._size = size;
        }
        
        @Override
        public String toString() {
            return String.format("Bucket@%x{%d,%d}", this.hashCode(), this._size, this._queue.size());
        }
    }
}
