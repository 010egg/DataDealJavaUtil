// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.tsv;

import java.util.TreeMap;
import shadeio.univocity.parsers.common.Format;

public class TsvFormat extends Format
{
    private char escapeChar;
    private char escapedTabChar;
    
    public TsvFormat() {
        this.escapeChar = '\\';
        this.escapedTabChar = 't';
    }
    
    public void setEscapeChar(final char escapeChar) {
        this.escapeChar = escapeChar;
    }
    
    public char getEscapeChar() {
        return this.escapeChar;
    }
    
    public char getEscapedTabChar() {
        return this.escapedTabChar;
    }
    
    public void setEscapedTabChar(final char escapedTabChar) {
        this.escapedTabChar = escapedTabChar;
    }
    
    public boolean isEscapeChar(final char ch) {
        return this.escapeChar == ch;
    }
    
    @Override
    protected TreeMap<String, Object> getConfiguration() {
        final TreeMap<String, Object> out = new TreeMap<String, Object>();
        out.put("Escape character", this.escapeChar);
        return out;
    }
    
    public final TsvFormat clone() {
        return (TsvFormat)super.clone();
    }
}
