// 
// Decompiled by Procyon v0.5.36
// 

package org.eclipse.jetty.util.ssl;

import javax.net.ssl.SSLEngine;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.net.Socket;
import java.security.Principal;
import javax.net.ssl.X509KeyManager;
import javax.net.ssl.X509ExtendedKeyManager;

public class AliasedX509ExtendedKeyManager extends X509ExtendedKeyManager
{
    private String _keyAlias;
    private X509KeyManager _keyManager;
    
    public AliasedX509ExtendedKeyManager(final String keyAlias, final X509KeyManager keyManager) throws Exception {
        this._keyAlias = keyAlias;
        this._keyManager = keyManager;
    }
    
    @Override
    public String chooseClientAlias(final String[] keyType, final Principal[] issuers, final Socket socket) {
        return (this._keyAlias == null) ? this._keyManager.chooseClientAlias(keyType, issuers, socket) : this._keyAlias;
    }
    
    @Override
    public String chooseServerAlias(final String keyType, final Principal[] issuers, final Socket socket) {
        return (this._keyAlias == null) ? this._keyManager.chooseServerAlias(keyType, issuers, socket) : this._keyAlias;
    }
    
    @Override
    public String[] getClientAliases(final String keyType, final Principal[] issuers) {
        return this._keyManager.getClientAliases(keyType, issuers);
    }
    
    @Override
    public String[] getServerAliases(final String keyType, final Principal[] issuers) {
        return this._keyManager.getServerAliases(keyType, issuers);
    }
    
    @Override
    public X509Certificate[] getCertificateChain(final String alias) {
        return this._keyManager.getCertificateChain(alias);
    }
    
    @Override
    public PrivateKey getPrivateKey(final String alias) {
        return this._keyManager.getPrivateKey(alias);
    }
    
    @Override
    public String chooseEngineServerAlias(final String keyType, final Principal[] issuers, final SSLEngine engine) {
        return (this._keyAlias == null) ? super.chooseEngineServerAlias(keyType, issuers, engine) : this._keyAlias;
    }
    
    @Override
    public String chooseEngineClientAlias(final String[] keyType, final Principal[] issuers, final SSLEngine engine) {
        return (this._keyAlias == null) ? super.chooseEngineClientAlias(keyType, issuers, engine) : this._keyAlias;
    }
}
