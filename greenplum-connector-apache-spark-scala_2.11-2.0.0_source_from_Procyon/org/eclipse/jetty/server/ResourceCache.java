// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.server;

import java.nio.channels.ReadableByteChannel;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.eclipse.jetty.http.DateGenerator;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.BufferUtil;
import java.nio.ByteBuffer;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;
import java.io.IOException;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.http.HttpContent;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import org.eclipse.jetty.http.MimeTypes;
import org.eclipse.jetty.util.resource.ResourceFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentMap;
import org.eclipse.jetty.util.log.Logger;

public class ResourceCache
{
    private static final Logger LOG;
    private final ConcurrentMap<String, Content> _cache;
    private final AtomicInteger _cachedSize;
    private final AtomicInteger _cachedFiles;
    private final ResourceFactory _factory;
    private final ResourceCache _parent;
    private final MimeTypes _mimeTypes;
    private final boolean _etagSupported;
    private final boolean _useFileMappedBuffer;
    private int _maxCachedFileSize;
    private int _maxCachedFiles;
    private int _maxCacheSize;
    
    public ResourceCache(final ResourceCache parent, final ResourceFactory factory, final MimeTypes mimeTypes, final boolean useFileMappedBuffer, final boolean etags) {
        this._maxCachedFileSize = 134217728;
        this._maxCachedFiles = 2048;
        this._maxCacheSize = 268435456;
        this._factory = factory;
        this._cache = new ConcurrentHashMap<String, Content>();
        this._cachedSize = new AtomicInteger();
        this._cachedFiles = new AtomicInteger();
        this._mimeTypes = mimeTypes;
        this._parent = parent;
        this._useFileMappedBuffer = useFileMappedBuffer;
        this._etagSupported = etags;
    }
    
    public int getCachedSize() {
        return this._cachedSize.get();
    }
    
    public int getCachedFiles() {
        return this._cachedFiles.get();
    }
    
    public int getMaxCachedFileSize() {
        return this._maxCachedFileSize;
    }
    
    public void setMaxCachedFileSize(final int maxCachedFileSize) {
        this._maxCachedFileSize = maxCachedFileSize;
        this.shrinkCache();
    }
    
    public int getMaxCacheSize() {
        return this._maxCacheSize;
    }
    
    public void setMaxCacheSize(final int maxCacheSize) {
        this._maxCacheSize = maxCacheSize;
        this.shrinkCache();
    }
    
    public int getMaxCachedFiles() {
        return this._maxCachedFiles;
    }
    
    public void setMaxCachedFiles(final int maxCachedFiles) {
        this._maxCachedFiles = maxCachedFiles;
        this.shrinkCache();
    }
    
    public boolean isUseFileMappedBuffer() {
        return this._useFileMappedBuffer;
    }
    
    public void flushCache() {
        if (this._cache != null) {
            while (this._cache.size() > 0) {
                for (final String path : this._cache.keySet()) {
                    final Content content = this._cache.remove(path);
                    if (content != null) {
                        content.invalidate();
                    }
                }
            }
        }
    }
    
    public HttpContent lookup(final String pathInContext) throws IOException {
        final Content content = this._cache.get(pathInContext);
        if (content != null && content.isValid()) {
            return content;
        }
        final Resource resource = this._factory.getResource(pathInContext);
        final HttpContent loaded = this.load(pathInContext, resource);
        if (loaded != null) {
            return loaded;
        }
        if (this._parent != null) {
            final HttpContent httpContent = this._parent.lookup(pathInContext);
            if (httpContent != null) {
                return httpContent;
            }
        }
        return null;
    }
    
    protected boolean isCacheable(final Resource resource) {
        final long len = resource.length();
        return len > 0L && len < this._maxCachedFileSize && len < this._maxCacheSize;
    }
    
    private HttpContent load(final String pathInContext, final Resource resource) throws IOException {
        Content content = null;
        if (resource == null || !resource.exists()) {
            return null;
        }
        if (!resource.isDirectory() && this.isCacheable(resource)) {
            content = new Content(pathInContext, resource);
            this.shrinkCache();
            final Content added = this._cache.putIfAbsent(pathInContext, content);
            if (added != null) {
                content.invalidate();
                content = added;
            }
            return content;
        }
        return new HttpContent.ResourceAsHttpContent(resource, this._mimeTypes.getMimeByExtension(resource.toString()), this.getMaxCachedFileSize(), this._etagSupported);
    }
    
    private void shrinkCache() {
        while (this._cache.size() > 0 && (this._cachedFiles.get() > this._maxCachedFiles || this._cachedSize.get() > this._maxCacheSize)) {
            final SortedSet<Content> sorted = new TreeSet<Content>(new Comparator<Content>() {
                @Override
                public int compare(final Content c1, final Content c2) {
                    if (c1._lastAccessed < c2._lastAccessed) {
                        return -1;
                    }
                    if (c1._lastAccessed > c2._lastAccessed) {
                        return 1;
                    }
                    if (c1._length < c2._length) {
                        return -1;
                    }
                    return c1._key.compareTo(c2._key);
                }
            });
            for (final Content content : this._cache.values()) {
                sorted.add(content);
            }
            for (final Content content : sorted) {
                if (this._cachedFiles.get() <= this._maxCachedFiles && this._cachedSize.get() <= this._maxCacheSize) {
                    break;
                }
                if (content != this._cache.remove(content.getKey())) {
                    continue;
                }
                content.invalidate();
            }
        }
    }
    
    protected ByteBuffer getIndirectBuffer(final Resource resource) {
        try {
            return BufferUtil.toBuffer(resource, true);
        }
        catch (IOException | IllegalArgumentException ex2) {
            final Exception ex;
            final Exception e = ex;
            ResourceCache.LOG.warn(e);
            return null;
        }
    }
    
    protected ByteBuffer getDirectBuffer(final Resource resource) {
        try {
            if (this._useFileMappedBuffer && resource.getFile() != null && resource.length() < 2147483647L) {
                return BufferUtil.toMappedBuffer(resource.getFile());
            }
            return BufferUtil.toBuffer(resource, true);
        }
        catch (IOException | IllegalArgumentException ex2) {
            final Exception ex;
            final Exception e = ex;
            ResourceCache.LOG.warn(e);
            return null;
        }
    }
    
    @Override
    public String toString() {
        return "ResourceCache[" + this._parent + "," + this._factory + "]@" + this.hashCode();
    }
    
    static {
        LOG = Log.getLogger(ResourceCache.class);
    }
    
    public class Content implements HttpContent
    {
        final Resource _resource;
        final int _length;
        final String _key;
        final long _lastModified;
        final ByteBuffer _lastModifiedBytes;
        final ByteBuffer _contentType;
        final String _etag;
        volatile long _lastAccessed;
        AtomicReference<ByteBuffer> _indirectBuffer;
        AtomicReference<ByteBuffer> _directBuffer;
        
        Content(final String pathInContext, final Resource resource) {
            this._indirectBuffer = new AtomicReference<ByteBuffer>();
            this._directBuffer = new AtomicReference<ByteBuffer>();
            this._key = pathInContext;
            this._resource = resource;
            final String mimeType = ResourceCache.this._mimeTypes.getMimeByExtension(this._resource.toString());
            this._contentType = ((mimeType == null) ? null : BufferUtil.toBuffer(mimeType));
            final boolean exists = resource.exists();
            this._lastModified = (exists ? resource.lastModified() : -1L);
            this._lastModifiedBytes = ((this._lastModified < 0L) ? null : BufferUtil.toBuffer(DateGenerator.formatDate(this._lastModified)));
            this._length = (exists ? ((int)resource.length()) : 0);
            ResourceCache.this._cachedSize.addAndGet(this._length);
            ResourceCache.this._cachedFiles.incrementAndGet();
            this._lastAccessed = System.currentTimeMillis();
            this._etag = (ResourceCache.this._etagSupported ? resource.getWeakETag() : null);
        }
        
        public String getKey() {
            return this._key;
        }
        
        public boolean isCached() {
            return this._key != null;
        }
        
        public boolean isMiss() {
            return false;
        }
        
        @Override
        public Resource getResource() {
            return this._resource;
        }
        
        @Override
        public String getETag() {
            return this._etag;
        }
        
        boolean isValid() {
            if (this._lastModified == this._resource.lastModified() && this._length == this._resource.length()) {
                this._lastAccessed = System.currentTimeMillis();
                return true;
            }
            if (this == ResourceCache.this._cache.remove(this._key)) {
                this.invalidate();
            }
            return false;
        }
        
        protected void invalidate() {
            ResourceCache.this._cachedSize.addAndGet(-this._length);
            ResourceCache.this._cachedFiles.decrementAndGet();
            this._resource.close();
        }
        
        @Override
        public String getLastModified() {
            return BufferUtil.toString(this._lastModifiedBytes);
        }
        
        @Override
        public String getContentType() {
            return BufferUtil.toString(this._contentType);
        }
        
        @Override
        public void release() {
        }
        
        @Override
        public ByteBuffer getIndirectBuffer() {
            ByteBuffer buffer = this._indirectBuffer.get();
            if (buffer == null) {
                final ByteBuffer buffer2 = ResourceCache.this.getIndirectBuffer(this._resource);
                if (buffer2 == null) {
                    ResourceCache.LOG.warn("Could not load " + this, new Object[0]);
                }
                else if (this._indirectBuffer.compareAndSet(null, buffer2)) {
                    buffer = buffer2;
                }
                else {
                    buffer = this._indirectBuffer.get();
                }
            }
            if (buffer == null) {
                return null;
            }
            return buffer.slice();
        }
        
        @Override
        public ByteBuffer getDirectBuffer() {
            ByteBuffer buffer = this._directBuffer.get();
            if (buffer == null) {
                final ByteBuffer buffer2 = ResourceCache.this.getDirectBuffer(this._resource);
                if (buffer2 == null) {
                    ResourceCache.LOG.warn("Could not load " + this, new Object[0]);
                }
                else if (this._directBuffer.compareAndSet(null, buffer2)) {
                    buffer = buffer2;
                }
                else {
                    buffer = this._directBuffer.get();
                }
            }
            if (buffer == null) {
                return null;
            }
            return buffer.asReadOnlyBuffer();
        }
        
        @Override
        public long getContentLength() {
            return this._length;
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            final ByteBuffer indirect = this.getIndirectBuffer();
            if (indirect != null && indirect.hasArray()) {
                return new ByteArrayInputStream(indirect.array(), indirect.arrayOffset() + indirect.position(), indirect.remaining());
            }
            return this._resource.getInputStream();
        }
        
        @Override
        public ReadableByteChannel getReadableByteChannel() throws IOException {
            return this._resource.getReadableByteChannel();
        }
        
        @Override
        public String toString() {
            return String.format("CachedContent@%x{r=%s,e=%b,lm=%s,ct=%s}", this.hashCode(), this._resource, this._resource.exists(), BufferUtil.toString(this._lastModifiedBytes), this._contentType);
        }
    }
}
