// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import java.util.TreeMap;
import shadeio.univocity.parsers.common.Format;

public class FixedWidthFormat extends Format
{
    private char padding;
    private char lookupWildcard;
    
    public FixedWidthFormat() {
        this.padding = ' ';
        this.lookupWildcard = '?';
    }
    
    public char getPadding() {
        return this.padding;
    }
    
    public void setPadding(final char padding) {
        this.padding = padding;
    }
    
    public boolean isPadding(final char padding) {
        return this.padding == padding;
    }
    
    @Override
    protected TreeMap<String, Object> getConfiguration() {
        final TreeMap<String, Object> out = new TreeMap<String, Object>();
        out.put("Padding", this.padding);
        return out;
    }
    
    public final FixedWidthFormat clone() {
        return (FixedWidthFormat)super.clone();
    }
    
    public char getLookupWildcard() {
        return this.lookupWildcard;
    }
    
    public void setLookupWildcard(final char lookupWildcard) {
        this.lookupWildcard = lookupWildcard;
    }
}
