// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.iterators;

import java.util.Iterator;
import shadeio.univocity.parsers.common.Context;
import shadeio.univocity.parsers.common.ResultIterator;
import shadeio.univocity.parsers.common.AbstractParser;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.IterableResult;

abstract class ParserIterator<T> implements IterableResult<T, ParsingContext>
{
    protected final AbstractParser parser;
    
    protected ParserIterator(final AbstractParser parser) {
        this.parser = parser;
    }
    
    @Override
    public final ParsingContext getContext() {
        return this.parser.getContext();
    }
    
    protected abstract void beginParsing();
    
    @Override
    public final ResultIterator<T, ParsingContext> iterator() {
        return new ResultIterator<T, ParsingContext>() {
            T next;
            boolean started;
            
            @Override
            public ParsingContext getContext() {
                return ParserIterator.this.parser.getContext();
            }
            
            @Override
            public boolean hasNext() {
                if (this.started) {
                    return this.next != null;
                }
                this.started = true;
                ParserIterator.this.beginParsing();
                this.next = ParserIterator.this.nextResult();
                return this.next != null;
            }
            
            @Override
            public T next() {
                final T out = this.next;
                this.next = ParserIterator.this.nextResult();
                return out;
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException("Can't remove row");
            }
        };
    }
    
    protected abstract T nextResult();
}
