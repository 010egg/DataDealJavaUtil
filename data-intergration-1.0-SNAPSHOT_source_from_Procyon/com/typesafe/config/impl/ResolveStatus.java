// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.Collection;

enum ResolveStatus
{
    UNRESOLVED, 
    RESOLVED;
    
    static final ResolveStatus fromValues(final Collection<? extends AbstractConfigValue> values) {
        for (final AbstractConfigValue v : values) {
            if (v.resolveStatus() == ResolveStatus.UNRESOLVED) {
                return ResolveStatus.UNRESOLVED;
            }
        }
        return ResolveStatus.RESOLVED;
    }
    
    static final ResolveStatus fromBoolean(final boolean resolved) {
        return resolved ? ResolveStatus.RESOLVED : ResolveStatus.UNRESOLVED;
    }
}
