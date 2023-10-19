// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

public class HostSpec
{
    protected final String host;
    protected final int port;
    
    public HostSpec(final String host, final int port) {
        this.host = host;
        this.port = port;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    @Override
    public String toString() {
        return this.host + ":" + this.port;
    }
    
    @Override
    public boolean equals(final Object obj) {
        return obj instanceof HostSpec && this.port == ((HostSpec)obj).port && this.host.equals(((HostSpec)obj).host);
    }
    
    @Override
    public int hashCode() {
        return this.port ^ this.host.hashCode();
    }
}
