// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.internal.asm;

final class Edge
{
    final Label successor;
    final Edge nextEdge;
    
    Edge(final Label successor, final Edge nextEdge) {
        this.successor = successor;
        this.nextEdge = nextEdge;
    }
}
