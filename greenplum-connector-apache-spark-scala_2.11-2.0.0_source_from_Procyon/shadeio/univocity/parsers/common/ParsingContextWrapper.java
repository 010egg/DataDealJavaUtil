// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common;

import shadeio.univocity.parsers.common.record.Record;
import java.util.Map;

public class ParsingContextWrapper extends ContextWrapper<ParsingContext> implements ParsingContext
{
    public ParsingContextWrapper(final ParsingContext context) {
        super(context);
    }
    
    @Override
    public long currentLine() {
        return ((ParsingContext)this.context).currentLine();
    }
    
    @Override
    public long currentChar() {
        return ((ParsingContext)this.context).currentChar();
    }
    
    @Override
    public void skipLines(final long lines) {
        ((ParsingContext)this.context).skipLines(lines);
    }
    
    @Override
    public String currentParsedContent() {
        return ((ParsingContext)this.context).currentParsedContent();
    }
    
    @Override
    public int currentParsedContentLength() {
        return ((ParsingContext)this.context).currentParsedContentLength();
    }
    
    @Override
    public Map<Long, String> comments() {
        return ((ParsingContext)this.context).comments();
    }
    
    @Override
    public String lastComment() {
        return ((ParsingContext)this.context).lastComment();
    }
    
    @Override
    public String[] parsedHeaders() {
        return ((ParsingContext)this.context).parsedHeaders();
    }
    
    @Override
    public char[] lineSeparator() {
        return ((ParsingContext)this.context).lineSeparator();
    }
    
    @Override
    public String fieldContentOnError() {
        return ((ParsingContext)this.context).fieldContentOnError();
    }
    
    @Override
    public String[] selectedHeaders() {
        return ((ParsingContext)this.context).selectedHeaders();
    }
    
    @Override
    public Record toRecord(final String[] row) {
        return ((ParsingContext)this.context).toRecord(row);
    }
}
