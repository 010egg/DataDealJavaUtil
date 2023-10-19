// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

final class MemoKey
{
    private final AbstractConfigValue value;
    private final Path restrictToChildOrNull;
    
    MemoKey(final AbstractConfigValue value, final Path restrictToChildOrNull) {
        this.value = value;
        this.restrictToChildOrNull = restrictToChildOrNull;
    }
    
    @Override
    public final int hashCode() {
        final int h = System.identityHashCode(this.value);
        if (this.restrictToChildOrNull != null) {
            return h + 41 * (41 + this.restrictToChildOrNull.hashCode());
        }
        return h;
    }
    
    @Override
    public final boolean equals(final Object other) {
        if (other instanceof MemoKey) {
            final MemoKey o = (MemoKey)other;
            return o.value == this.value && (o.restrictToChildOrNull == this.restrictToChildOrNull || (o.restrictToChildOrNull != null && this.restrictToChildOrNull != null && o.restrictToChildOrNull.equals(this.restrictToChildOrNull)));
        }
        return false;
    }
    
    @Override
    public final String toString() {
        return "MemoKey(" + this.value + "@" + System.identityHashCode(this.value) + "," + this.restrictToChildOrNull + ")";
    }
}
