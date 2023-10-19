// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.joran.spi;

import java.util.List;

public class ElementSelector extends ElementPath
{
    public ElementSelector() {
    }
    
    public ElementSelector(final List<String> list) {
        super(list);
    }
    
    public ElementSelector(final String p) {
        super(p);
    }
    
    public boolean fullPathMatch(final ElementPath path) {
        if (path.size() != this.size()) {
            return false;
        }
        for (int len = this.size(), i = 0; i < len; ++i) {
            if (!this.equalityCheck(this.get(i), path.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    public int getTailMatchLength(final ElementPath p) {
        if (p == null) {
            return 0;
        }
        final int lSize = this.partList.size();
        final int rSize = p.partList.size();
        if (lSize == 0 || rSize == 0) {
            return 0;
        }
        final int minLen = (lSize <= rSize) ? lSize : rSize;
        int match = 0;
        for (int i = 1; i <= minLen; ++i) {
            final String l = this.partList.get(lSize - i);
            final String r = p.partList.get(rSize - i);
            if (!this.equalityCheck(l, r)) {
                break;
            }
            ++match;
        }
        return match;
    }
    
    public boolean isContainedIn(final ElementPath p) {
        return p != null && p.toStableString().contains(this.toStableString());
    }
    
    public int getPrefixMatchLength(final ElementPath p) {
        if (p == null) {
            return 0;
        }
        final int lSize = this.partList.size();
        final int rSize = p.partList.size();
        if (lSize == 0 || rSize == 0) {
            return 0;
        }
        final int minLen = (lSize <= rSize) ? lSize : rSize;
        int match = 0;
        for (int i = 0; i < minLen; ++i) {
            final String l = this.partList.get(i);
            final String r = p.partList.get(i);
            if (!this.equalityCheck(l, r)) {
                break;
            }
            ++match;
        }
        return match;
    }
    
    private boolean equalityCheck(final String x, final String y) {
        return x.equalsIgnoreCase(y);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null || !(o instanceof ElementSelector)) {
            return false;
        }
        final ElementSelector r = (ElementSelector)o;
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
    
    @Override
    public int hashCode() {
        int hc = 0;
        for (int len = this.size(), i = 0; i < len; ++i) {
            hc ^= this.get(i).toLowerCase().hashCode();
        }
        return hc;
    }
}
