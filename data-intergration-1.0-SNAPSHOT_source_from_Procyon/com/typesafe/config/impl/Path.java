// 
// Decompiled by Procyon v0.5.36
// 

package com.typesafe.config.impl;

import java.util.Iterator;
import java.util.List;
import com.typesafe.config.ConfigException;

final class Path
{
    private final String first;
    private final Path remainder;
    
    Path(final String first, final Path remainder) {
        this.first = first;
        this.remainder = remainder;
    }
    
    Path(final String... elements) {
        if (elements.length == 0) {
            throw new ConfigException.BugOrBroken("empty path");
        }
        this.first = elements[0];
        if (elements.length > 1) {
            final PathBuilder pb = new PathBuilder();
            for (int i = 1; i < elements.length; ++i) {
                pb.appendKey(elements[i]);
            }
            this.remainder = pb.result();
        }
        else {
            this.remainder = null;
        }
    }
    
    Path(final List<Path> pathsToConcat) {
        this(pathsToConcat.iterator());
    }
    
    Path(final Iterator<Path> i) {
        if (!i.hasNext()) {
            throw new ConfigException.BugOrBroken("empty path");
        }
        final Path firstPath = i.next();
        this.first = firstPath.first;
        final PathBuilder pb = new PathBuilder();
        if (firstPath.remainder != null) {
            pb.appendPath(firstPath.remainder);
        }
        while (i.hasNext()) {
            pb.appendPath(i.next());
        }
        this.remainder = pb.result();
    }
    
    String first() {
        return this.first;
    }
    
    Path remainder() {
        return this.remainder;
    }
    
    Path parent() {
        if (this.remainder == null) {
            return null;
        }
        final PathBuilder pb = new PathBuilder();
        for (Path p = this; p.remainder != null; p = p.remainder) {
            pb.appendKey(p.first);
        }
        return pb.result();
    }
    
    String last() {
        Path p;
        for (p = this; p.remainder != null; p = p.remainder) {}
        return p.first;
    }
    
    Path prepend(final Path toPrepend) {
        final PathBuilder pb = new PathBuilder();
        pb.appendPath(toPrepend);
        pb.appendPath(this);
        return pb.result();
    }
    
    int length() {
        int count = 1;
        for (Path p = this.remainder; p != null; p = p.remainder) {
            ++count;
        }
        return count;
    }
    
    Path subPath(final int removeFromFront) {
        int count;
        Path p;
        for (count = removeFromFront, p = this; p != null && count > 0; --count, p = p.remainder) {}
        return p;
    }
    
    Path subPath(final int firstIndex, final int lastIndex) {
        if (lastIndex < firstIndex) {
            throw new ConfigException.BugOrBroken("bad call to subPath");
        }
        Path from = this.subPath(firstIndex);
        final PathBuilder pb = new PathBuilder();
        int count = lastIndex - firstIndex;
        while (count > 0) {
            --count;
            pb.appendKey(from.first());
            from = from.remainder();
            if (from == null) {
                throw new ConfigException.BugOrBroken("subPath lastIndex out of range " + lastIndex);
            }
        }
        return pb.result();
    }
    
    boolean startsWith(final Path other) {
        Path myRemainder = this;
        Path otherRemainder = other;
        if (otherRemainder.length() <= myRemainder.length()) {
            while (otherRemainder != null) {
                if (!otherRemainder.first().equals(myRemainder.first())) {
                    return false;
                }
                myRemainder = myRemainder.remainder();
                otherRemainder = otherRemainder.remainder();
            }
            return true;
        }
        return false;
    }
    
    @Override
    public boolean equals(final Object other) {
        if (other instanceof Path) {
            final Path that = (Path)other;
            return this.first.equals(that.first) && ConfigImplUtil.equalsHandlingNull(this.remainder, that.remainder);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        return 41 * (41 + this.first.hashCode()) + ((this.remainder == null) ? 0 : this.remainder.hashCode());
    }
    
    static boolean hasFunkyChars(final String s) {
        final int length = s.length();
        if (length == 0) {
            return false;
        }
        for (int i = 0; i < length; ++i) {
            final char c = s.charAt(i);
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_') {
                return true;
            }
        }
        return false;
    }
    
    private void appendToStringBuilder(final StringBuilder sb) {
        if (hasFunkyChars(this.first) || this.first.isEmpty()) {
            sb.append(ConfigImplUtil.renderJsonString(this.first));
        }
        else {
            sb.append(this.first);
        }
        if (this.remainder != null) {
            sb.append(".");
            this.remainder.appendToStringBuilder(sb);
        }
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Path(");
        this.appendToStringBuilder(sb);
        sb.append(")");
        return sb.toString();
    }
    
    String render() {
        final StringBuilder sb = new StringBuilder();
        this.appendToStringBuilder(sb);
        return sb.toString();
    }
    
    static Path newKey(final String key) {
        return new Path(key, null);
    }
    
    static Path newPath(final String path) {
        return PathParser.parsePath(path);
    }
}
