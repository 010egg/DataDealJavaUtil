// 
// Decompiled by Procyon v0.5.36
// 

package ch.qos.logback.core.util;

class CharSequenceState
{
    final char c;
    int occurrences;
    
    public CharSequenceState(final char c) {
        this.c = c;
        this.occurrences = 1;
    }
    
    void incrementOccurrences() {
        ++this.occurrences;
    }
}
