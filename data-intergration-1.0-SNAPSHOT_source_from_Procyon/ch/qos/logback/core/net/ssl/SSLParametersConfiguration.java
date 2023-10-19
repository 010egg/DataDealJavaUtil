// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

import java.util.List;
import ch.qos.logback.core.util.StringCollectionUtil;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;
import ch.qos.logback.core.util.OptionHelper;
import ch.qos.logback.core.spi.ContextAwareBase;

public class SSLParametersConfiguration extends ContextAwareBase
{
    private String includedProtocols;
    private String excludedProtocols;
    private String includedCipherSuites;
    private String excludedCipherSuites;
    private Boolean needClientAuth;
    private Boolean wantClientAuth;
    private String[] enabledProtocols;
    private String[] enabledCipherSuites;
    
    public void configure(final SSLConfigurable socket) {
        socket.setEnabledProtocols(this.enabledProtocols(socket.getSupportedProtocols(), socket.getDefaultProtocols()));
        socket.setEnabledCipherSuites(this.enabledCipherSuites(socket.getSupportedCipherSuites(), socket.getDefaultCipherSuites()));
        if (this.isNeedClientAuth() != null) {
            socket.setNeedClientAuth(this.isNeedClientAuth());
        }
        if (this.isWantClientAuth() != null) {
            socket.setWantClientAuth(this.isWantClientAuth());
        }
    }
    
    private String[] enabledProtocols(final String[] supportedProtocols, final String[] defaultProtocols) {
        if (this.enabledProtocols == null) {
            if (OptionHelper.isEmpty(this.getIncludedProtocols()) && OptionHelper.isEmpty(this.getExcludedProtocols())) {
                this.enabledProtocols = Arrays.copyOf(defaultProtocols, defaultProtocols.length);
            }
            else {
                this.enabledProtocols = this.includedStrings(supportedProtocols, this.getIncludedProtocols(), this.getExcludedProtocols());
            }
            for (final String protocol : this.enabledProtocols) {
                this.addInfo("enabled protocol: " + protocol);
            }
        }
        return this.enabledProtocols;
    }
    
    private String[] enabledCipherSuites(final String[] supportedCipherSuites, final String[] defaultCipherSuites) {
        if (this.enabledCipherSuites == null) {
            if (OptionHelper.isEmpty(this.getIncludedCipherSuites()) && OptionHelper.isEmpty(this.getExcludedCipherSuites())) {
                this.enabledCipherSuites = Arrays.copyOf(defaultCipherSuites, defaultCipherSuites.length);
            }
            else {
                this.enabledCipherSuites = this.includedStrings(supportedCipherSuites, this.getIncludedCipherSuites(), this.getExcludedCipherSuites());
            }
            for (final String cipherSuite : this.enabledCipherSuites) {
                this.addInfo("enabled cipher suite: " + cipherSuite);
            }
        }
        return this.enabledCipherSuites;
    }
    
    private String[] includedStrings(final String[] defaults, final String included, final String excluded) {
        final List<String> values = new ArrayList<String>(defaults.length);
        values.addAll(Arrays.asList(defaults));
        if (included != null) {
            StringCollectionUtil.retainMatching(values, this.stringToArray(included));
        }
        if (excluded != null) {
            StringCollectionUtil.removeMatching(values, this.stringToArray(excluded));
        }
        return values.toArray(new String[values.size()]);
    }
    
    private String[] stringToArray(final String s) {
        return s.split("\\s*,\\s*");
    }
    
    public String getIncludedProtocols() {
        return this.includedProtocols;
    }
    
    public void setIncludedProtocols(final String protocols) {
        this.includedProtocols = protocols;
    }
    
    public String getExcludedProtocols() {
        return this.excludedProtocols;
    }
    
    public void setExcludedProtocols(final String protocols) {
        this.excludedProtocols = protocols;
    }
    
    public String getIncludedCipherSuites() {
        return this.includedCipherSuites;
    }
    
    public void setIncludedCipherSuites(final String cipherSuites) {
        this.includedCipherSuites = cipherSuites;
    }
    
    public String getExcludedCipherSuites() {
        return this.excludedCipherSuites;
    }
    
    public void setExcludedCipherSuites(final String cipherSuites) {
        this.excludedCipherSuites = cipherSuites;
    }
    
    public Boolean isNeedClientAuth() {
        return this.needClientAuth;
    }
    
    public void setNeedClientAuth(final Boolean needClientAuth) {
        this.needClientAuth = needClientAuth;
    }
    
    public Boolean isWantClientAuth() {
        return this.wantClientAuth;
    }
    
    public void setWantClientAuth(final Boolean wantClientAuth) {
        this.wantClientAuth = wantClientAuth;
    }
}
