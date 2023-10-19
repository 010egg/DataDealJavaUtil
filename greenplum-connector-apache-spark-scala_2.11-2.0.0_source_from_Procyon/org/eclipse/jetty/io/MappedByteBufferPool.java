// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.io;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.eclipse.jetty.util.BufferUtil;
import java.util.concurrent.ConcurrentHashMap;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.ConcurrentMap;

public class MappedByteBufferPool implements ByteBufferPool
{
    private final ConcurrentMap<Integer, Queue<ByteBuffer>> directBuffers;
    private final ConcurrentMap<Integer, Queue<ByteBuffer>> heapBuffers;
    private final int factor;
    
    public MappedByteBufferPool() {
        this(1024);
    }
    
    public MappedByteBufferPool(final int factor) {
        this.directBuffers = new ConcurrentHashMap<Integer, Queue<ByteBuffer>>();
        this.heapBuffers = new ConcurrentHashMap<Integer, Queue<ByteBuffer>>();
        this.factor = factor;
    }
    
    @Override
    public ByteBuffer acquire(final int size, final boolean direct) {
        final int bucket = this.bucketFor(size);
        final ConcurrentMap<Integer, Queue<ByteBuffer>> buffers = this.buffersFor(direct);
        ByteBuffer result = null;
        final Queue<ByteBuffer> byteBuffers = buffers.get(bucket);
        if (byteBuffers != null) {
            result = byteBuffers.poll();
        }
        if (result == null) {
            final int capacity = bucket * this.factor;
            result = this.newByteBuffer(capacity, direct);
        }
        BufferUtil.clear(result);
        return result;
    }
    
    protected ByteBuffer newByteBuffer(final int capacity, final boolean direct) {
        return direct ? BufferUtil.allocateDirect(capacity) : BufferUtil.allocate(capacity);
    }
    
    @Override
    public void release(final ByteBuffer buffer) {
        if (buffer == null) {
            return;
        }
        assert buffer.capacity() % this.factor == 0;
        final int bucket = this.bucketFor(buffer.capacity());
        final ConcurrentMap<Integer, Queue<ByteBuffer>> buffers = this.buffersFor(buffer.isDirect());
        Queue<ByteBuffer> byteBuffers = buffers.get(bucket);
        if (byteBuffers == null) {
            byteBuffers = new ConcurrentLinkedQueue<ByteBuffer>();
            final Queue<ByteBuffer> existing = buffers.putIfAbsent(bucket, byteBuffers);
            if (existing != null) {
                byteBuffers = existing;
            }
        }
        BufferUtil.clear(buffer);
        byteBuffers.offer(buffer);
    }
    
    public void clear() {
        this.directBuffers.clear();
        this.heapBuffers.clear();
    }
    
    private int bucketFor(final int size) {
        int bucket = size / this.factor;
        if (size % this.factor > 0) {
            ++bucket;
        }
        return bucket;
    }
    
    ConcurrentMap<Integer, Queue<ByteBuffer>> buffersFor(final boolean direct) {
        return direct ? this.directBuffers : this.heapBuffers;
    }
    
    public static class Tagged extends MappedByteBufferPool
    {
        private final AtomicInteger tag;
        
        public Tagged() {
            this.tag = new AtomicInteger();
        }
        
        @Override
        protected ByteBuffer newByteBuffer(final int capacity, final boolean direct) {
            final ByteBuffer buffer = super.newByteBuffer(capacity + 4, direct);
            buffer.limit(buffer.capacity());
            buffer.putInt(this.tag.incrementAndGet());
            final ByteBuffer slice = buffer.slice();
            BufferUtil.clear(slice);
            return slice;
        }
    }
}
