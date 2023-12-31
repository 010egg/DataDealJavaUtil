// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Set;

abstract class ForwardingValueGraph<N, V> extends AbstractValueGraph<N, V>
{
    protected abstract ValueGraph<N, V> delegate();
    
    @Override
    public Set<N> nodes() {
        return this.delegate().nodes();
    }
    
    @Override
    protected long edgeCount() {
        return this.delegate().edges().size();
    }
    
    @Override
    public boolean isDirected() {
        return this.delegate().isDirected();
    }
    
    @Override
    public boolean allowsSelfLoops() {
        return this.delegate().allowsSelfLoops();
    }
    
    @Override
    public ElementOrder<N> nodeOrder() {
        return this.delegate().nodeOrder();
    }
    
    @Override
    public Set<N> adjacentNodes(final N node) {
        return this.delegate().adjacentNodes(node);
    }
    
    @Override
    public Set<N> predecessors(final N node) {
        return this.delegate().predecessors(node);
    }
    
    @Override
    public Set<N> successors(final N node) {
        return this.delegate().successors(node);
    }
    
    @Override
    public int degree(final N node) {
        return this.delegate().degree(node);
    }
    
    @Override
    public int inDegree(final N node) {
        return this.delegate().inDegree(node);
    }
    
    @Override
    public int outDegree(final N node) {
        return this.delegate().outDegree(node);
    }
    
    @Override
    public boolean hasEdgeConnecting(final N nodeU, final N nodeV) {
        return this.delegate().hasEdgeConnecting(nodeU, nodeV);
    }
    
    @Override
    public Optional<V> edgeValue(final N nodeU, final N nodeV) {
        return this.delegate().edgeValue(nodeU, nodeV);
    }
    
    @Nullable
    @Override
    public V edgeValueOrDefault(final N nodeU, final N nodeV, @Nullable final V defaultValue) {
        return this.delegate().edgeValueOrDefault(nodeU, nodeV, defaultValue);
    }
}
