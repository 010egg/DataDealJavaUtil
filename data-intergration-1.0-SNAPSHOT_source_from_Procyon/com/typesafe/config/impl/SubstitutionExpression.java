// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

final class SubstitutionExpression
{
    private final Path path;
    private final boolean optional;
    
    SubstitutionExpression(final Path path, final boolean optional) {
        this.path = path;
        this.optional = optional;
    }
    
    Path path() {
        return this.path;
    }
    
    boolean optional() {
        return this.optional;
    }
    
    SubstitutionExpression changePath(final Path newPath) {
        if (newPath == this.path) {
            return this;
        }
        return new SubstitutionExpression(newPath, this.optional);
    }
    
    @Override
    public String toString() {
        return "${" + (this.optional ? "?" : "") + this.path.render() + "}";
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof SubstitutionExpression) {
            final SubstitutionExpression otherExp = (SubstitutionExpression)other;
            return otherExp.path.equals(this.path) && otherExp.optional == this.optional;
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int h = 41 * (41 + this.path.hashCode());
        h = 41 * (h + (this.optional ? 1 : 0));
        return h;
    }
}
