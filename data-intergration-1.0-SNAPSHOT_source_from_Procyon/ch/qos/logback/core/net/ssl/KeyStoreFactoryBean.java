// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.net.ssl;

import java.net.URL;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import ch.qos.logback.core.util.LocationUtil;
import java.security.KeyStore;

public class KeyStoreFactoryBean
{
    private String location;
    private String provider;
    private String type;
    private String password;
    
    public KeyStore createKeyStore() throws NoSuchProviderException, NoSuchAlgorithmException, KeyStoreException {
        if (this.getLocation() == null) {
            throw new IllegalArgumentException("location is required");
        }
        InputStream inputStream = null;
        try {
            final URL url = LocationUtil.urlForResource(this.getLocation());
            inputStream = url.openStream();
            final KeyStore keyStore = this.newKeyStore();
            keyStore.load(inputStream, this.getPassword().toCharArray());
            return keyStore;
        }
        catch (NoSuchProviderException ex3) {
            throw new NoSuchProviderException("no such keystore provider: " + this.getProvider());
        }
        catch (NoSuchAlgorithmException ex4) {
            throw new NoSuchAlgorithmException("no such keystore type: " + this.getType());
        }
        catch (FileNotFoundException ex5) {
            throw new KeyStoreException(this.getLocation() + ": file not found");
        }
        catch (Exception ex) {
            throw new KeyStoreException(this.getLocation() + ": " + ex.getMessage(), ex);
        }
        finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            catch (IOException ex2) {
                ex2.printStackTrace(System.err);
            }
        }
    }
    
    private KeyStore newKeyStore() throws NoSuchAlgorithmException, NoSuchProviderException, KeyStoreException {
        return (this.getProvider() != null) ? KeyStore.getInstance(this.getType(), this.getProvider()) : KeyStore.getInstance(this.getType());
    }
    
    public String getLocation() {
        return this.location;
    }
    
    public void setLocation(final String location) {
        this.location = location;
    }
    
    public String getType() {
        if (this.type == null) {
            return "JKS";
        }
        return this.type;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public String getProvider() {
        return this.provider;
    }
    
    public void setProvider(final String provider) {
        this.provider = provider;
    }
    
    public String getPassword() {
        if (this.password == null) {
            return "changeit";
        }
        return this.password;
    }
    
    public void setPassword(final String password) {
        this.password = password;
    }
}
