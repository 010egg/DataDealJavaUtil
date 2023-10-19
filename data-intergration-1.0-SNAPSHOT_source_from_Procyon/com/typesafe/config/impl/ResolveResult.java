// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigException;

final class ResolveResult<V extends AbstractConfigValue>
{
    public final ResolveContext context;
    public final V value;
    
    private ResolveResult(final ResolveContext context, final V value) {
        this.context = context;
        this.value = value;
    }
    
    static <V extends AbstractConfigValue> ResolveResult<V> make(final ResolveContext context, final V value) {
        return new ResolveResult<V>(context, value);
    }
    
    ResolveResult<AbstractConfigObject> asObjectResult() {
        if (!(this.value instanceof AbstractConfigObject)) {
            throw new ConfigException.BugOrBroken("Expecting a resolve result to be an object, but it was " + this.value);
        }
        final Object o = this;
        return (ResolveResult<AbstractConfigObject>)o;
    }
    
    ResolveResult<AbstractConfigValue> asValueResult() {
        final Object o = this;
        return (ResolveResult<AbstractConfigValue>)o;
    }
    
    ResolveResult<V> popTrace() {
        return make(this.context.popTrace(), this.value);
    }
    
    @Override
    public String toString() {
        return "ResolveResult(" + this.value + ")";
    }
}
