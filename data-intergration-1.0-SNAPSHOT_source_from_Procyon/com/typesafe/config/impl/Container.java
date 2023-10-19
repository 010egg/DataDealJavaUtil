// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigValue;

interface Container extends ConfigValue
{
    AbstractConfigValue replaceChild(final AbstractConfigValue p0, final AbstractConfigValue p1);
    
    boolean hasDescendant(final AbstractConfigValue p0);
}
