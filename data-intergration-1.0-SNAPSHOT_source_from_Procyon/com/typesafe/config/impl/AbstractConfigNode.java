// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.Collection;
import com.typesafe.config.parser.ConfigNode;

abstract class AbstractConfigNode implements ConfigNode
{
    abstract Collection<Token> tokens();
    
    @Override
    public final String render() {
        final StringBuilder origText = new StringBuilder();
        final Iterable<Token> tokens = this.tokens();
        for (final Token t : tokens) {
            origText.append(t.tokenText());
        }
        return origText.toString();
    }
    
    @Override
    public final boolean equals(final Object other) {
        return other instanceof AbstractConfigNode && this.render().equals(((AbstractConfigNode)other).render());
    }
    
    @Override
    public final int hashCode() {
        return this.render().hashCode();
    }
}
