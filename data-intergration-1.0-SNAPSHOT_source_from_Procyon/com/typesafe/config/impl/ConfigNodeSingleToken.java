// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Collections;
import java.util.Collection;

class ConfigNodeSingleToken extends AbstractConfigNode
{
    final Token token;
    
    ConfigNodeSingleToken(final Token t) {
        this.token = t;
    }
    
    protected Collection<Token> tokens() {
        return Collections.singletonList(this.token);
    }
    
    protected Token token() {
        return this.token;
    }
}
