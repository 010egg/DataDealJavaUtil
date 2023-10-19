// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.iterators;

import shadeio.univocity.parsers.common.AbstractParser;
import shadeio.univocity.parsers.common.record.Record;

public abstract class RecordIterator extends ParserIterator<Record>
{
    public RecordIterator(final AbstractParser parser) {
        super(parser);
    }
    
    @Override
    protected final Record nextResult() {
        return this.parser.parseNextRecord();
    }
}
