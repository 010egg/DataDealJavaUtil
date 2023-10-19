// 
// Decompiled by Procyon v0.5.36
// 

package org.postgresql.util;

import java.util.ArrayList;
import java.util.List;

public class PGtokenizer
{
    protected List tokens;
    
    public PGtokenizer(final String string, final char delim) {
        this.tokenize(string, delim);
    }
    
    public int tokenize(final String string, final char delim) {
        this.tokens = new ArrayList();
        int nest = 0;
        boolean skipChar = false;
        boolean nestedDoubleQuote = false;
        int p = 0;
        int s = 0;
        while (p < string.length()) {
            final char c = string.charAt(p);
            if (c == '(' || c == '[' || c == '<' || (!nestedDoubleQuote && !skipChar && c == '\"')) {
                ++nest;
                if (c == '\"') {
                    nestedDoubleQuote = true;
                    skipChar = true;
                }
            }
            if (c == ')' || c == ']' || c == '>' || (nestedDoubleQuote && !skipChar && c == '\"')) {
                --nest;
                if (c == '\"') {
                    nestedDoubleQuote = false;
                }
            }
            skipChar = (c == '\\');
            if (nest == 0 && c == delim) {
                this.tokens.add(string.substring(s, p));
                s = p + 1;
            }
            ++p;
        }
        if (s < string.length()) {
            this.tokens.add(string.substring(s));
        }
        return this.tokens.size();
    }
    
    public int getSize() {
        return this.tokens.size();
    }
    
    public String getToken(final int n) {
        return this.tokens.get(n);
    }
    
    public PGtokenizer tokenizeToken(final int n, final char delim) {
        return new PGtokenizer(this.getToken(n), delim);
    }
    
    public static String remove(String s, final String l, final String t) {
        if (s.startsWith(l)) {
            s = s.substring(l.length());
        }
        if (s.endsWith(t)) {
            s = s.substring(0, s.length() - t.length());
        }
        return s;
    }
    
    public void remove(final String l, final String t) {
        for (int i = 0; i < this.tokens.size(); ++i) {
            this.tokens.set(i, remove(this.tokens.get(i), l, t));
        }
    }
    
    public static String removePara(final String s) {
        return remove(s, "(", ")");
    }
    
    public void removePara() {
        this.remove("(", ")");
    }
    
    public static String removeBox(final String s) {
        return remove(s, "[", "]");
    }
    
    public void removeBox() {
        this.remove("[", "]");
    }
    
    public static String removeAngle(final String s) {
        return remove(s, "<", ">");
    }
    
    public void removeAngle() {
        this.remove("<", ">");
    }
}
