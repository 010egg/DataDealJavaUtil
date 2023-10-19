// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigMergeable;

interface MergeableValue extends ConfigMergeable
{
    ConfigValue toFallbackValue();
}
