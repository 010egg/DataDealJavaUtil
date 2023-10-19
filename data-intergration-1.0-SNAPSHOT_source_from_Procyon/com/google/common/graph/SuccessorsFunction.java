// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.graph;

import com.google.common.annotations.Beta;

@Beta
public interface SuccessorsFunction<N>
{
    Iterable<? extends N> successors(final N p0);
}
