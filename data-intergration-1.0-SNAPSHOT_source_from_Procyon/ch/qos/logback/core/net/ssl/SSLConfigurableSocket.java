// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

import javax.net.ssl.SSLSocket;

public class SSLConfigurableSocket implements SSLConfigurable
{
    private final SSLSocket delegate;
    
    public SSLConfigurableSocket(final SSLSocket delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public String[] getDefaultProtocols() {
        return this.delegate.getEnabledProtocols();
    }
    
    @Override
    public String[] getSupportedProtocols() {
        return this.delegate.getSupportedProtocols();
    }
    
    @Override
    public void setEnabledProtocols(final String[] protocols) {
        this.delegate.setEnabledProtocols(protocols);
    }
    
    @Override
    public String[] getDefaultCipherSuites() {
        return this.delegate.getEnabledCipherSuites();
    }
    
    @Override
    public String[] getSupportedCipherSuites() {
        return this.delegate.getSupportedCipherSuites();
    }
    
    @Override
    public void setEnabledCipherSuites(final String[] suites) {
        this.delegate.setEnabledCipherSuites(suites);
    }
    
    @Override
    public void setNeedClientAuth(final boolean state) {
        this.delegate.setNeedClientAuth(state);
    }
    
    @Override
    public void setWantClientAuth(final boolean state) {
        this.delegate.setWantClientAuth(state);
    }
}
