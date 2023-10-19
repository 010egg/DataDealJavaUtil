// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.util;

import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

public class ListDG
{
    private List<VNode> mVexs;
    
    public ListDG(final List vexs, final List<Edge> edges) {
        final int vlen = vexs.size();
        final int elen = edges.size();
        this.mVexs = new ArrayList<VNode>();
        for (int i = 0; i < vlen; ++i) {
            final VNode vnode = new VNode();
            vnode.data = vexs.get(i);
            vnode.firstEdge = null;
            this.mVexs.add(vnode);
        }
        for (int i = 0; i < elen; ++i) {
            final Object c1 = edges.get(i).from;
            final Object c2 = edges.get(i).to;
            final int p1 = this.getPosition(edges.get(i).from);
            final int p2 = this.getPosition(edges.get(i).to);
            final ENode node1 = new ENode();
            node1.ivex = p2;
            if (this.mVexs.get(p1).firstEdge == null) {
                this.mVexs.get(p1).firstEdge = node1;
            }
            else {
                this.linkLast(this.mVexs.get(p1).firstEdge, node1);
            }
        }
    }
    
    private void linkLast(final ENode list, final ENode node) {
        ENode p;
        for (p = list; p.nextEdge != null; p = p.nextEdge) {}
        p.nextEdge = node;
    }
    
    private int getPosition(final Object ch) {
        for (int i = 0; i < this.mVexs.size(); ++i) {
            if (this.mVexs.get(i).data == ch) {
                return i;
            }
        }
        return -1;
    }
    
    private void DFS(final int i, final boolean[] visited) {
        visited[i] = true;
        for (ENode node = this.mVexs.get(i).firstEdge; node != null; node = node.nextEdge) {
            if (!visited[node.ivex]) {
                this.DFS(node.ivex, visited);
            }
        }
    }
    
    public void DFS() {
        final boolean[] visited = new boolean[this.mVexs.size()];
        for (int i = 0; i < this.mVexs.size(); ++i) {
            visited[i] = false;
        }
        for (int i = 0; i < this.mVexs.size(); ++i) {
            if (!visited[i]) {
                this.DFS(i, visited);
            }
        }
    }
    
    public void BFS() {
        int head = 0;
        int rear = 0;
        final int[] queue = new int[this.mVexs.size()];
        final boolean[] visited = new boolean[this.mVexs.size()];
        for (int i = 0; i < this.mVexs.size(); ++i) {
            visited[i] = false;
        }
        for (int i = 0; i < this.mVexs.size(); ++i) {
            if (!visited[i]) {
                visited[i] = true;
                System.out.printf("%c ", this.mVexs.get(i).data);
                queue[rear++] = i;
            }
            while (head != rear) {
                final int j = queue[head++];
                for (ENode node = this.mVexs.get(j).firstEdge; node != null; node = node.nextEdge) {
                    final int k = node.ivex;
                    if (!visited[k]) {
                        visited[k] = true;
                        System.out.printf("%c ", this.mVexs.get(k).data);
                        queue[rear++] = k;
                    }
                }
            }
        }
    }
    
    public void print() {
        System.out.printf("== List Graph:\n", new Object[0]);
        for (int i = 0; i < this.mVexs.size(); ++i) {
            System.out.printf("%d(%c): ", i, this.mVexs.get(i).data);
            for (ENode node = this.mVexs.get(i).firstEdge; node != null; node = node.nextEdge) {
                System.out.printf("%d(%c) ", node.ivex, this.mVexs.get(node.ivex).data);
            }
        }
    }
    
    public boolean topologicalSort() {
        return this.topologicalSort(new Object[this.mVexs.size()]);
    }
    
    public boolean topologicalSort(final Object[] tops) {
        int index = 0;
        final int num = this.mVexs.size();
        final int[] ins = new int[num];
        final Queue<Integer> queue = new LinkedList<Integer>();
        for (int i = 0; i < num; ++i) {
            for (ENode node = this.mVexs.get(i).firstEdge; node != null; node = node.nextEdge) {
                final int[] array = ins;
                final int ivex = node.ivex;
                ++array[ivex];
            }
        }
        for (int i = 0; i < num; ++i) {
            if (ins[i] == 0) {
                queue.offer(i);
            }
        }
        while (!queue.isEmpty()) {
            final int j = queue.poll();
            tops[index++] = this.mVexs.get(j).data;
            for (ENode node = this.mVexs.get(j).firstEdge; node != null; node = node.nextEdge) {
                final int[] array2 = ins;
                final int ivex2 = node.ivex;
                --array2[ivex2];
                if (ins[node.ivex] == 0) {
                    queue.offer(node.ivex);
                }
            }
        }
        return index == num;
    }
    
    public static class Edge
    {
        public Object from;
        public Object to;
        
        public Edge(final Object from, final Object to) {
            this.from = from;
            this.to = to;
        }
    }
    
    private class ENode
    {
        int ivex;
        ENode nextEdge;
    }
    
    private class VNode
    {
        Object data;
        ENode firstEdge;
    }
}
