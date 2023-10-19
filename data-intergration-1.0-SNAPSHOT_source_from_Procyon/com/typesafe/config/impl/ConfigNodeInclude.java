// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

final class ConfigNodeInclude extends AbstractConfigNode
{
    private final ArrayList<AbstractConfigNode> children;
    private final ConfigIncludeKind kind;
    private final boolean isRequired;
    
    ConfigNodeInclude(final Collection<AbstractConfigNode> children, final ConfigIncludeKind kind, final boolean isRequired) {
        this.children = new ArrayList<AbstractConfigNode>(children);
        this.kind = kind;
        this.isRequired = isRequired;
    }
    
    public final Collection<AbstractConfigNode> children() {
        return this.children;
    }
    
    protected Collection<Token> tokens() {
        final ArrayList<Token> tokens = new ArrayList<Token>();
        for (final AbstractConfigNode child : this.children) {
            tokens.addAll(child.tokens());
        }
        return tokens;
    }
    
    protected ConfigIncludeKind kind() {
        return this.kind;
    }
    
    protected boolean isRequired() {
        return this.isRequired;
    }
    
    protected String name() {
        for (final AbstractConfigNode n : this.children) {
            if (n instanceof ConfigNodeSimpleValue) {
                return (String)Tokens.getValue(((ConfigNodeSimpleValue)n).token()).unwrapped();
            }
        }
        return null;
    }
}
