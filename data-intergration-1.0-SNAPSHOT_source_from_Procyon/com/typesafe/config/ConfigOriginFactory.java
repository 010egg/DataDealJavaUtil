// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.net.URL;
import com.typesafe.config.impl.ConfigImpl;

public final class ConfigOriginFactory
{
    private ConfigOriginFactory() {
    }
    
    public static ConfigOrigin newSimple() {
        return newSimple(null);
    }
    
    public static ConfigOrigin newSimple(final String description) {
        return ConfigImpl.newSimpleOrigin(description);
    }
    
    public static ConfigOrigin newFile(final String filename) {
        return ConfigImpl.newFileOrigin(filename);
    }
    
    public static ConfigOrigin newURL(final URL url) {
        return ConfigImpl.newURLOrigin(url);
    }
}
