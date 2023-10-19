// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.input;

import java.io.Reader;

public interface CharInputReader extends CharInput
{
    void start(final Reader p0);
    
    void stop();
    
    char nextChar();
    
    char getChar();
    
    long charCount();
    
    long lineCount();
    
    void skipLines(final long p0);
    
    String readComment();
    
    void enableNormalizeLineEndings(final boolean p0);
    
    char[] getLineSeparator();
    
    char skipWhitespace(final char p0, final char p1, final char p2);
    
    int currentParsedContentLength();
    
    String currentParsedContent();
    
    void markRecordStart();
    
    String getString(final char p0, final char p1, final boolean p2, final String p3, final int p4);
    
    boolean skipString(final char p0, final char p1);
    
    String getQuotedString(final char p0, final char p1, final char p2, final int p3, final char p4, final char p5, final boolean p6, final boolean p7, final boolean p8, final boolean p9);
    
    boolean skipQuotedString(final char p0, final char p1, final char p2, final char p3);
}
