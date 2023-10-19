// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigException;
import java.util.Collection;
import java.util.ArrayList;

final class ConfigNodePath extends AbstractConfigNode
{
    private final Path path;
    final ArrayList<Token> tokens;
    
    ConfigNodePath(final Path path, final Collection<Token> tokens) {
        this.path = path;
        this.tokens = new ArrayList<Token>(tokens);
    }
    
    protected Collection<Token> tokens() {
        return this.tokens;
    }
    
    protected Path value() {
        return this.path;
    }
    
    protected ConfigNodePath subPath(final int toRemove) {
        int periodCount = 0;
        final ArrayList<Token> tokensCopy = new ArrayList<Token>(this.tokens);
        for (int i = 0; i < tokensCopy.size(); ++i) {
            if (Tokens.isUnquotedText(tokensCopy.get(i)) && tokensCopy.get(i).tokenText().equals(".")) {
                ++periodCount;
            }
            if (periodCount == toRemove) {
                return new ConfigNodePath(this.path.subPath(toRemove), tokensCopy.subList(i + 1, tokensCopy.size()));
            }
        }
        throw new ConfigException.BugOrBroken("Tried to remove too many elements from a Path node");
    }
    
    protected ConfigNodePath first() {
        final ArrayList<Token> tokensCopy = new ArrayList<Token>(this.tokens);
        for (int i = 0; i < tokensCopy.size(); ++i) {
            if (Tokens.isUnquotedText(tokensCopy.get(i)) && tokensCopy.get(i).tokenText().equals(".")) {
                return new ConfigNodePath(this.path.subPath(0, 1), tokensCopy.subList(0, i));
            }
        }
        return this;
    }
}
