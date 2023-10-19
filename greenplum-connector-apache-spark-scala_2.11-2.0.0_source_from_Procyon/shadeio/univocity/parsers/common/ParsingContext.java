// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Map;

public interface ParsingContext extends Context
{
    String[] headers();
    
    int[] extractedFieldIndexes();
    
    boolean columnsReordered();
    
    long currentLine();
    
    long currentChar();
    
    void skipLines(final long p0);
    
    String[] parsedHeaders();
    
    String currentParsedContent();
    
    int currentParsedContentLength();
    
    String fieldContentOnError();
    
    Map<Long, String> comments();
    
    String lastComment();
    
    char[] lineSeparator();
}
