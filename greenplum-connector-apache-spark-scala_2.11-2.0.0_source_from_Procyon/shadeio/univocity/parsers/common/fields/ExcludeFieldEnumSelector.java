// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.List;
import shadeio.univocity.parsers.common.ArgumentUtils;

public class ExcludeFieldEnumSelector extends FieldSet<Enum> implements FieldSelector
{
    private final ExcludeFieldNameSelector names;
    
    public ExcludeFieldEnumSelector() {
        this.names = new ExcludeFieldNameSelector();
    }
    
    @Override
    public int[] getFieldIndexes(final String[] headers) {
        if (headers == null) {
            return null;
        }
        this.names.set(ArgumentUtils.toArray((List<Enum>)this.get()));
        return this.names.getFieldIndexes(headers);
    }
    
    @Override
    public String describe() {
        return "undesired " + super.describe();
    }
}
