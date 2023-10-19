// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

import java.util.Iterator;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class ElementPath
{
    ArrayList<String> partList;
    
    public ElementPath() {
        this.partList = new ArrayList<String>();
    }
    
    public ElementPath(final List<String> list) {
        (this.partList = new ArrayList<String>()).addAll(list);
    }
    
    public ElementPath(final String pathStr) {
        this.partList = new ArrayList<String>();
        if (pathStr == null) {
            return;
        }
        final String[] partArray = pathStr.split("/");
        if (partArray == null) {
            return;
        }
        for (final String part : partArray) {
            if (part.length() > 0) {
                this.partList.add(part);
            }
        }
    }
    
    public ElementPath duplicate() {
        final ElementPath p = new ElementPath();
        p.partList.addAll(this.partList);
        return p;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof ElementPath)) {
            return false;
        }
        final ElementPath r = (ElementPath)o;
        if (r.size() != this.size()) {
            return false;
        }
        for (int len = this.size(), i = 0; i < len; ++i) {
            if (!this.equalityCheck(this.get(i), r.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    private boolean equalityCheck(final String x, final String y) {
        return x.equalsIgnoreCase(y);
    }
    
    public List<String> getCopyOfPartList() {
        return new ArrayList<String>(this.partList);
    }
    
    public void push(final String s) {
        this.partList.add(s);
    }
    
    public String get(final int i) {
        return this.partList.get(i);
    }
    
    public void pop() {
        if (!this.partList.isEmpty()) {
            this.partList.remove(this.partList.size() - 1);
        }
    }
    
    public String peekLast() {
        if (!this.partList.isEmpty()) {
            final int size = this.partList.size();
            return this.partList.get(size - 1);
        }
        return null;
    }
    
    public int size() {
        return this.partList.size();
    }
    
    protected String toStableString() {
        final StringBuilder result = new StringBuilder();
        for (final String current : this.partList) {
            result.append("[").append(current).append("]");
        }
        return result.toString();
    }
    
    @Override
    public String toString() {
        return this.toStableString();
    }
}
