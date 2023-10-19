// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import java.util.Collections;
import java.util.Map;

public class ParsingContextSnapshot extends ParsingContextWrapper
{
    private final long currentLine;
    private final long currentChar;
    private final Map<Long, String> comments;
    private final String lastComment;
    private final int currentColumn;
    private final String currentParsedContent;
    private final long currentRecord;
    
    public ParsingContextSnapshot(final ParsingContext context) {
        super(context);
        this.currentLine = context.currentLine();
        this.currentChar = context.currentChar();
        this.comments = ((context.comments() == Collections.EMPTY_MAP) ? Collections.emptyMap() : Collections.unmodifiableMap((Map<? extends Long, ? extends String>)context.comments()));
        this.lastComment = context.lastComment();
        this.currentColumn = context.currentColumn();
        this.currentParsedContent = context.currentParsedContent();
        this.currentRecord = context.currentRecord();
    }
    
    @Override
    public long currentLine() {
        return this.currentLine;
    }
    
    @Override
    public long currentChar() {
        return this.currentChar;
    }
    
    @Override
    public Map<Long, String> comments() {
        return this.comments;
    }
    
    @Override
    public String lastComment() {
        return this.lastComment;
    }
    
    @Override
    public int currentColumn() {
        return this.currentColumn;
    }
    
    @Override
    public String currentParsedContent() {
        return this.currentParsedContent;
    }
    
    @Override
    public long currentRecord() {
        return this.currentRecord;
    }
}
