// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;

class Token
{
    private final TokenType tokenType;
    private final String debugString;
    private final ConfigOrigin origin;
    private final String tokenText;
    
    Token(final TokenType tokenType, final ConfigOrigin origin) {
        this(tokenType, origin, null);
    }
    
    Token(final TokenType tokenType, final ConfigOrigin origin, final String tokenText) {
        this(tokenType, origin, tokenText, null);
    }
    
    Token(final TokenType tokenType, final ConfigOrigin origin, final String tokenText, final String debugString) {
        this.tokenType = tokenType;
        this.origin = origin;
        this.debugString = debugString;
        this.tokenText = tokenText;
    }
    
    static Token newWithoutOrigin(final TokenType tokenType, final String debugString, final String tokenText) {
        return new Token(tokenType, null, tokenText, debugString);
    }
    
    final TokenType tokenType() {
        return this.tokenType;
    }
    
    public String tokenText() {
        return this.tokenText;
    }
    
    final ConfigOrigin origin() {
        if (this.origin == null) {
            throw new ConfigException.BugOrBroken("tried to get origin from token that doesn't have one: " + this);
        }
        return this.origin;
    }
    
    final int lineNumber() {
        if (this.origin != null) {
            return this.origin.lineNumber();
        }
        return -1;
    }
    
    @Override
    public String toString() {
        if (this.debugString != null) {
            return this.debugString;
        }
        return this.tokenType.name();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Token;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof Token && this.canEqual(other) && this.tokenType == ((Token)other).tokenType;
    }
    
    @Override
    public int hashCode() {
        return this.tokenType.hashCode();
    }
}
