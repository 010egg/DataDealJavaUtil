// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import java.util.List;
import com.typesafe.config.impl.ConfigImplUtil;

public final class ConfigUtil
{
    private ConfigUtil() {
    }
    
    public static String quoteString(final String s) {
        return ConfigImplUtil.renderJsonString(s);
    }
    
    public static String joinPath(final String... elements) {
        return ConfigImplUtil.joinPath(elements);
    }
    
    public static String joinPath(final List<String> elements) {
        return ConfigImplUtil.joinPath(elements);
    }
    
    public static List<String> splitPath(final String path) {
        return ConfigImplUtil.splitPath(path);
    }
}
