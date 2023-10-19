// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.List;
import com.typesafe.config.ConfigException;
import java.util.Collections;
import java.util.Collection;

final class ConfigNodeSimpleValue extends AbstractConfigNodeValue
{
    final Token token;
    
    ConfigNodeSimpleValue(final Token value) {
        this.token = value;
    }
    
    protected Collection<Token> tokens() {
        return Collections.singletonList(this.token);
    }
    
    protected Token token() {
        return this.token;
    }
    
    protected AbstractConfigValue value() {
        if (Tokens.isValue(this.token)) {
            return Tokens.getValue(this.token);
        }
        if (Tokens.isUnquotedText(this.token)) {
            return new ConfigString.Unquoted(this.token.origin(), Tokens.getUnquotedText(this.token));
        }
        if (Tokens.isSubstitution(this.token)) {
            final List<Token> expression = Tokens.getSubstitutionPathExpression(this.token);
            final Path path = PathParser.parsePathExpression(expression.iterator(), this.token.origin());
            final boolean optional = Tokens.getSubstitutionOptional(this.token);
            return new ConfigReference(this.token.origin(), new SubstitutionExpression(path, optional));
        }
        throw new ConfigException.BugOrBroken("ConfigNodeSimpleValue did not contain a valid value token");
    }
}
