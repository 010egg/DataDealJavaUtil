// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import com.typesafe.config.ConfigException;
import java.util.Stack;

final class PathBuilder
{
    private final Stack<String> keys;
    private Path result;
    
    PathBuilder() {
        this.keys = new Stack<String>();
    }
    
    private void checkCanAppend() {
        if (this.result != null) {
            throw new ConfigException.BugOrBroken("Adding to PathBuilder after getting result");
        }
    }
    
    void appendKey(final String key) {
        this.checkCanAppend();
        this.keys.push(key);
    }
    
    void appendPath(final Path path) {
        this.checkCanAppend();
        String first = path.first();
        Path remainder = path.remainder();
        while (true) {
            this.keys.push(first);
            if (remainder == null) {
                break;
            }
            first = remainder.first();
            remainder = remainder.remainder();
        }
    }
    
    Path result() {
        if (this.result == null) {
            Path remainder = null;
            while (!this.keys.isEmpty()) {
                final String key = this.keys.pop();
                remainder = new Path(key, remainder);
            }
            this.result = remainder;
        }
        return this.result;
    }
}
