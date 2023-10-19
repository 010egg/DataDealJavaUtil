// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigException;

final class ConfigNodeComment extends ConfigNodeSingleToken
{
    ConfigNodeComment(final Token comment) {
        super(comment);
        if (!Tokens.isComment(super.token)) {
            throw new ConfigException.BugOrBroken("Tried to create a ConfigNodeComment from a non-comment token");
        }
    }
    
    protected String commentText() {
        return Tokens.getCommentText(super.token);
    }
}
