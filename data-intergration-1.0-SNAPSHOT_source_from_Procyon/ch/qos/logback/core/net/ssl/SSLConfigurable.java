// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

public interface SSLConfigurable
{
    String[] getDefaultProtocols();
    
    String[] getSupportedProtocols();
    
    void setEnabledProtocols(final String[] p0);
    
    String[] getDefaultCipherSuites();
    
    String[] getSupportedCipherSuites();
    
    void setEnabledCipherSuites(final String[] p0);
    
    void setNeedClientAuth(final boolean p0);
    
    void setWantClientAuth(final boolean p0);
}
