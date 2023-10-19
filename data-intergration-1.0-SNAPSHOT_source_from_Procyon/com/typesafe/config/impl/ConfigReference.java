// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigRenderOptions;
import java.util.Collections;
import java.util.Collection;
import com.typesafe.config.ConfigValueType;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigOrigin;

final class ConfigReference extends AbstractConfigValue implements Unmergeable
{
    private final SubstitutionExpression expr;
    private final int prefixLength;
    
    ConfigReference(final ConfigOrigin origin, final SubstitutionExpression expr) {
        this(origin, expr, 0);
    }
    
    private ConfigReference(final ConfigOrigin origin, final SubstitutionExpression expr, final int prefixLength) {
        super(origin);
        this.expr = expr;
        this.prefixLength = prefixLength;
    }
    
    private ConfigException.NotResolved notResolved() {
        return new ConfigException.NotResolved("need to Config#resolve(), see the API docs for Config#resolve(); substitution not resolved: " + this);
    }
    
    @Override
    public ConfigValueType valueType() {
        throw this.notResolved();
    }
    
    @Override
    public Object unwrapped() {
        throw this.notResolved();
    }
    
    @Override
    protected ConfigReference newCopy(final ConfigOrigin newOrigin) {
        return new ConfigReference(newOrigin, this.expr, this.prefixLength);
    }
    
    @Override
    protected boolean ignoresFallbacks() {
        return false;
    }
    
    @Override
    public Collection<ConfigReference> unmergedValues() {
        return Collections.singleton(this);
    }
    
    @Override
    ResolveResult<? extends AbstractConfigValue> resolveSubstitutions(final ResolveContext context, final ResolveSource source) {
        ResolveContext newContext = context.addCycleMarker(this);
        AbstractConfigValue v;
        try {
            final ResolveSource.ResultWithPath resultWithPath = source.lookupSubst(newContext, this.expr, this.prefixLength);
            newContext = resultWithPath.result.context;
            if (resultWithPath.result.value != null) {
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(newContext.depth(), "recursively resolving " + resultWithPath + " which was the resolution of " + this.expr + " against " + source);
                }
                final ResolveSource recursiveResolveSource = new ResolveSource(resultWithPath.pathFromRoot.last(), resultWithPath.pathFromRoot);
                if (ConfigImpl.traceSubstitutionsEnabled()) {
                    ConfigImpl.trace(newContext.depth(), "will recursively resolve against " + recursiveResolveSource);
                }
                final ResolveResult<? extends AbstractConfigValue> result = newContext.resolve((AbstractConfigValue)resultWithPath.result.value, recursiveResolveSource);
                v = (AbstractConfigValue)result.value;
                newContext = result.context;
            }
            else {
                v = null;
            }
        }
        catch (NotPossibleToResolve e) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
                ConfigImpl.trace(newContext.depth(), "not possible to resolve " + this.expr + ", cycle involved: " + e.traceString());
            }
            if (!this.expr.optional()) {
                throw new ConfigException.UnresolvedSubstitution(this.origin(), this.expr + " was part of a cycle of substitutions involving " + e.traceString(), e);
            }
            v = null;
        }
        if (v != null || this.expr.optional()) {
            return ResolveResult.make(newContext.removeCycleMarker(this), v);
        }
        if (newContext.options().getAllowUnresolved()) {
            return ResolveResult.make(newContext.removeCycleMarker(this), (AbstractConfigValue)this);
        }
        throw new ConfigException.UnresolvedSubstitution(this.origin(), this.expr.toString());
    }
    
    @Override
    ResolveStatus resolveStatus() {
        return ResolveStatus.UNRESOLVED;
    }
    
    @Override
    ConfigReference relativized(final Path prefix) {
        final SubstitutionExpression newExpr = this.expr.changePath(this.expr.path().prepend(prefix));
        return new ConfigReference(this.origin(), newExpr, this.prefixLength + prefix.length());
    }
    
    @Override
    protected boolean canEqual(final Object other) {
        return other instanceof ConfigReference;
    }
    
    @Override
    public boolean equals(final Object other) {
        return other instanceof ConfigReference && this.canEqual(other) && this.expr.equals(((ConfigReference)other).expr);
    }
    
    @Override
    public int hashCode() {
        return this.expr.hashCode();
    }
    
    @Override
    protected void render(final StringBuilder sb, final int indent, final boolean atRoot, final ConfigRenderOptions options) {
        sb.append(this.expr.toString());
    }
    
    SubstitutionExpression expression() {
        return this.expr;
    }
}
