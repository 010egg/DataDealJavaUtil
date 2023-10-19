// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config;

import com.typesafe.config.impl.ConfigBeanImpl;

public class ConfigBeanFactory
{
    public static <T> T create(final Config config, final Class<T> clazz) {
        return ConfigBeanImpl.createInternal(config, clazz);
    }
}
