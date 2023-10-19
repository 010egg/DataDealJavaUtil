// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Collection;

final class ConfigNodeArray extends ConfigNodeComplexValue
{
    ConfigNodeArray(final Collection<AbstractConfigNode> children) {
        super(children);
    }
    
    protected ConfigNodeArray newNode(final Collection<AbstractConfigNode> nodes) {
        return new ConfigNodeArray(nodes);
    }
}
