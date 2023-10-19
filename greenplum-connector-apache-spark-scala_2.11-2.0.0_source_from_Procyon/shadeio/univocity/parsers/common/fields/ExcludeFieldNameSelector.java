// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.Set;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import shadeio.univocity.parsers.common.ArgumentUtils;

public class ExcludeFieldNameSelector extends FieldSet<String> implements FieldSelector
{
    @Override
    public int[] getFieldIndexes(String[] headers) {
        if (headers == null) {
            return null;
        }
        headers = ArgumentUtils.normalize(headers);
        final Set<String> chosenFields = new HashSet<String>(this.get());
        ArgumentUtils.normalize(chosenFields);
        final Object[] unknownFields = ArgumentUtils.findMissingElements(headers, chosenFields);
        if (unknownFields.length > 0) {
            throw new IllegalStateException("Unknown field names: " + Arrays.toString(unknownFields));
        }
        final int[] out = new int[headers.length - chosenFields.size()];
        int j = 0;
        for (int i = 0; i < headers.length; ++i) {
            if (!chosenFields.contains(headers[i])) {
                out[j++] = i;
            }
        }
        return out;
    }
    
    @Override
    public String describe() {
        return "undesired " + super.describe();
    }
}
