// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.annotations.Beta;

@Beta
public interface PredecessorsFunction<N>
{
    Iterable<? extends N> predecessors(final N p0);
}
