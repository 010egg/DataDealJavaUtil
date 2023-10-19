// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.math.IntMath;
import javax.annotation.Nullable;
import com.google.common.primitives.Ints;
import com.google.common.collect.UnmodifiableIterator;
import java.util.AbstractSet;
import java.util.Set;
import java.util.Iterator;
import com.google.common.base.Preconditions;

abstract class AbstractBaseGraph<N> implements BaseGraph<N>
{
    protected long edgeCount() {
        long degreeSum = 0L;
        for (final N node : this.nodes()) {
            degreeSum += this.degree(node);
        }
        Preconditions.checkState((degreeSum & 0x1L) == 0x0L);
        return degreeSum >>> 1;
    }
    
    @Override
    public Set<EndpointPair<N>> edges() {
        return new AbstractSet<EndpointPair<N>>() {
            @Override
            public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return (UnmodifiableIterator<EndpointPair<N>>)EndpointPairIterator.of((BaseGraph<Object>)AbstractBaseGraph.this);
            }
            
            @Override
            public int size() {
                return Ints.saturatedCast(AbstractBaseGraph.this.edgeCount());
            }
            
            @Override
            public boolean contains(@Nullable final Object obj) {
                if (!(obj instanceof EndpointPair)) {
                    return false;
                }
                final EndpointPair<?> endpointPair = (EndpointPair<?>)obj;
                return AbstractBaseGraph.this.isDirected() == endpointPair.isOrdered() && AbstractBaseGraph.this.nodes().contains(endpointPair.nodeU()) && AbstractBaseGraph.this.successors(endpointPair.nodeU()).contains(endpointPair.nodeV());
            }
        };
    }
    
    @Override
    public int degree(final N node) {
        if (this.isDirected()) {
            return IntMath.saturatedAdd(this.predecessors(node).size(), this.successors(node).size());
        }
        final Set<N> neighbors = this.adjacentNodes(node);
        final int selfLoopCount = (this.allowsSelfLoops() && neighbors.contains(node)) ? 1 : 0;
        return IntMath.saturatedAdd(neighbors.size(), selfLoopCount);
    }
    
    @Override
    public int inDegree(final N node) {
        return this.isDirected() ? this.predecessors(node).size() : this.degree(node);
    }
    
    @Override
    public int outDegree(final N node) {
        return this.isDirected() ? this.successors(node).size() : this.degree(node);
    }
    
    @Override
    public boolean hasEdgeConnecting(final N nodeU, final N nodeV) {
        Preconditions.checkNotNull(nodeU);
        Preconditions.checkNotNull(nodeV);
        return this.nodes().contains(nodeU) && this.successors(nodeU).contains(nodeV);
    }
}
