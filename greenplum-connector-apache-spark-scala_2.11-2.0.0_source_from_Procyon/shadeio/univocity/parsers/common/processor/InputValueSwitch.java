// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor;

import shadeio.univocity.parsers.common.Context;
import shadeio.univocity.parsers.common.ParsingContextWrapper;
import shadeio.univocity.parsers.common.ParsingContext;
import shadeio.univocity.parsers.common.processor.core.AbstractInputValueSwitch;

public class InputValueSwitch extends AbstractInputValueSwitch<ParsingContext> implements RowProcessor
{
    public InputValueSwitch() {
        this(0);
    }
    
    public InputValueSwitch(final int columnIndex) {
        super(columnIndex);
    }
    
    public InputValueSwitch(final String columnName) {
        super(columnName);
    }
    
    @Override
    protected final ParsingContext wrapContext(final ParsingContext context) {
        return new ParsingContextWrapper(context) {
            private final String[] fieldNames = InputValueSwitch.this.getHeaders();
            private final int[] indexes = InputValueSwitch.this.getIndexes();
            
            @Override
            public String[] headers() {
                return (this.fieldNames == null || this.fieldNames.length == 0) ? ((ParsingContext)this.context).headers() : this.fieldNames;
            }
            
            @Override
            public int[] extractedFieldIndexes() {
                return (this.indexes == null || this.indexes.length == 0) ? ((ParsingContext)this.context).extractedFieldIndexes() : this.indexes;
            }
        };
    }
}
