// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.List;
import shadeio.univocity.parsers.common.ArgumentUtils;

public class FieldEnumSelector extends FieldSet<Enum> implements FieldSelector
{
    private final FieldNameSelector names;
    
    public FieldEnumSelector() {
        this.names = new FieldNameSelector();
    }
    
    public int getFieldIndex(final Enum column) {
        return this.names.getFieldIndex(column.toString());
    }
    
    @Override
    public int[] getFieldIndexes(final String[] headers) {
        if (headers == null) {
            return null;
        }
        this.names.set(ArgumentUtils.toArray((List<Enum>)this.get()));
        return this.names.getFieldIndexes(headers);
    }
}
