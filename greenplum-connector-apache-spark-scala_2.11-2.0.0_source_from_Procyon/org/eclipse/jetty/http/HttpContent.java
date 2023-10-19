// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.http;

import org.eclipse.jetty.util.BufferUtil;
import java.nio.channels.ReadableByteChannel;
import java.io.IOException;
import java.io.InputStream;
import org.eclipse.jetty.util.resource.Resource;
import java.nio.ByteBuffer;

public interface HttpContent
{
    String getContentType();
    
    String getLastModified();
    
    ByteBuffer getIndirectBuffer();
    
    ByteBuffer getDirectBuffer();
    
    String getETag();
    
    Resource getResource();
    
    long getContentLength();
    
    InputStream getInputStream() throws IOException;
    
    ReadableByteChannel getReadableByteChannel() throws IOException;
    
    void release();
    
    public static class ResourceAsHttpContent implements HttpContent
    {
        final Resource _resource;
        final String _mimeType;
        final int _maxBuffer;
        final String _etag;
        
        public ResourceAsHttpContent(final Resource resource, final String mimeType) {
            this(resource, mimeType, -1, false);
        }
        
        public ResourceAsHttpContent(final Resource resource, final String mimeType, final int maxBuffer) {
            this(resource, mimeType, maxBuffer, false);
        }
        
        public ResourceAsHttpContent(final Resource resource, final String mimeType, final boolean etag) {
            this(resource, mimeType, -1, etag);
        }
        
        public ResourceAsHttpContent(final Resource resource, final String mimeType, final int maxBuffer, final boolean etag) {
            this._resource = resource;
            this._mimeType = mimeType;
            this._maxBuffer = maxBuffer;
            this._etag = (etag ? resource.getWeakETag() : null);
        }
        
        @Override
        public String getContentType() {
            return this._mimeType;
        }
        
        @Override
        public String getLastModified() {
            return null;
        }
        
        @Override
        public ByteBuffer getDirectBuffer() {
            if (this._resource.length() <= 0L || this._maxBuffer < this._resource.length()) {
                return null;
            }
            try {
                return BufferUtil.toBuffer(this._resource, true);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public String getETag() {
            return this._etag;
        }
        
        @Override
        public ByteBuffer getIndirectBuffer() {
            if (this._resource.length() <= 0L || this._maxBuffer < this._resource.length()) {
                return null;
            }
            try {
                return BufferUtil.toBuffer(this._resource, false);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        
        @Override
        public long getContentLength() {
            return this._resource.length();
        }
        
        @Override
        public InputStream getInputStream() throws IOException {
            return this._resource.getInputStream();
        }
        
        @Override
        public ReadableByteChannel getReadableByteChannel() throws IOException {
            return this._resource.getReadableByteChannel();
        }
        
        @Override
        public Resource getResource() {
            return this._resource;
        }
        
        @Override
        public void release() {
            this._resource.close();
        }
        
        @Override
        public String toString() {
            return String.format("%s@%x{r=%s}", this.getClass().getSimpleName(), this.hashCode(), this._resource);
        }
    }
}
