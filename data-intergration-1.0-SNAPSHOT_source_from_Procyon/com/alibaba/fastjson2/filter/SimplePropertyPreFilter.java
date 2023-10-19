// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.filter;

import com.alibaba.fastjson2.JSONWriter;
import java.util.HashSet;
import java.util.Set;

public class SimplePropertyPreFilter implements PropertyPreFilter
{
    private final Class<?> clazz;
    private final Set<String> includes;
    private final Set<String> excludes;
    private int maxLevel;
    
    public SimplePropertyPreFilter(final String... properties) {
        this((Class<?>)null, properties);
    }
    
    public SimplePropertyPreFilter(final Class<?> clazz, final String... properties) {
        this.includes = new HashSet<String>();
        this.excludes = new HashSet<String>();
        this.clazz = clazz;
        for (final String item : properties) {
            if (item != null) {
                this.includes.add(item);
            }
        }
    }
    
    public int getMaxLevel() {
        return this.maxLevel;
    }
    
    public void setMaxLevel(final int maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    public Class<?> getClazz() {
        return this.clazz;
    }
    
    public Set<String> getIncludes() {
        return this.includes;
    }
    
    public Set<String> getExcludes() {
        return this.excludes;
    }
    
    @Override
    public boolean process(final JSONWriter writer, final Object source, final String name) {
        if (source == null) {
            return true;
        }
        if (this.clazz != null && !this.clazz.isInstance(source)) {
            return this.excludes.size() != 0 && this.includes.isEmpty() && this.maxLevel == 0;
        }
        return !this.excludes.contains(name) && (this.maxLevel <= 0 || writer.level() <= this.maxLevel) && (this.includes.size() == 0 || this.includes.contains(name));
    }
}
