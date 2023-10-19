// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.List;
import com.typesafe.config.ConfigException;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;

final class ConfigNodeField extends AbstractConfigNode
{
    private final ArrayList<AbstractConfigNode> children;
    
    public ConfigNodeField(final Collection<AbstractConfigNode> children) {
        this.children = new ArrayList<AbstractConfigNode>(children);
    }
    
    protected Collection<Token> tokens() {
        final ArrayList<Token> tokens = new ArrayList<Token>();
        for (final AbstractConfigNode child : this.children) {
            tokens.addAll(child.tokens());
        }
        return tokens;
    }
    
    public ConfigNodeField replaceValue(final AbstractConfigNodeValue newValue) {
        final ArrayList<AbstractConfigNode> childrenCopy = new ArrayList<AbstractConfigNode>(this.children);
        for (int i = 0; i < childrenCopy.size(); ++i) {
            if (childrenCopy.get(i) instanceof AbstractConfigNodeValue) {
                childrenCopy.set(i, newValue);
                return new ConfigNodeField(childrenCopy);
            }
        }
        throw new ConfigException.BugOrBroken("Field node doesn't have a value");
    }
    
    public AbstractConfigNodeValue value() {
        for (int i = 0; i < this.children.size(); ++i) {
            if (this.children.get(i) instanceof AbstractConfigNodeValue) {
                return this.children.get(i);
            }
        }
        throw new ConfigException.BugOrBroken("Field node doesn't have a value");
    }
    
    public ConfigNodePath path() {
        for (int i = 0; i < this.children.size(); ++i) {
            if (this.children.get(i) instanceof ConfigNodePath) {
                return this.children.get(i);
            }
        }
        throw new ConfigException.BugOrBroken("Field node doesn't have a path");
    }
    
    protected Token separator() {
        for (final AbstractConfigNode child : this.children) {
            if (child instanceof ConfigNodeSingleToken) {
                final Token t = ((ConfigNodeSingleToken)child).token();
                if (t == Tokens.PLUS_EQUALS || t == Tokens.COLON || t == Tokens.EQUALS) {
                    return t;
                }
                continue;
            }
        }
        return null;
    }
    
    protected List<String> comments() {
        final List<String> comments = new ArrayList<String>();
        for (final AbstractConfigNode child : this.children) {
            if (child instanceof ConfigNodeComment) {
                comments.add(((ConfigNodeComment)child).commentText());
            }
        }
        return comments;
    }
}
