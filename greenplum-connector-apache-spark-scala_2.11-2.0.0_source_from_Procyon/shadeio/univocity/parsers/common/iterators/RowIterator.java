// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.iterators;

import shadeio.univocity.parsers.common.AbstractParser;

public abstract class RowIterator extends ParserIterator<String[]>
{
    public RowIterator(final AbstractParser parser) {
        super(parser);
    }
    
    @Override
    protected final String[] nextResult() {
        return this.parser.parseNext();
    }
}
