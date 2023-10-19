// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Collection;

final class ConfigNodeConcatenation extends ConfigNodeComplexValue
{
    ConfigNodeConcatenation(final Collection<AbstractConfigNode> children) {
        super(children);
    }
    
    protected ConfigNodeConcatenation newNode(final Collection<AbstractConfigNode> nodes) {
        return new ConfigNodeConcatenation(nodes);
    }
}
