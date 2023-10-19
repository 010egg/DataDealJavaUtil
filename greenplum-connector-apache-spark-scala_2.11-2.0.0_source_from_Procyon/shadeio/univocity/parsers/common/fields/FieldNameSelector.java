// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.List;
import java.util.Arrays;
import java.util.Collection;
import shadeio.univocity.parsers.common.ArgumentUtils;

public class FieldNameSelector extends FieldSet<String> implements FieldSelector
{
    public int getFieldIndex(final String header) {
        return this.getFieldIndexes(new String[] { header })[0];
    }
    
    @Override
    public int[] getFieldIndexes(String[] headers) {
        if (headers == null) {
            return null;
        }
        headers = ArgumentUtils.normalize(headers);
        final List<String> selection = this.get();
        ArgumentUtils.normalize(selection);
        final String[] chosenFields = selection.toArray(new String[selection.size()]);
        final Object[] unknownFields = ArgumentUtils.findMissingElements(headers, chosenFields);
        if (unknownFields.length > 0 && !selection.containsAll(Arrays.asList(headers)) && unknownFields.length == chosenFields.length) {
            return new int[0];
        }
        int[] out = new int[chosenFields.length];
        int i = 0;
        for (final String chosenField : chosenFields) {
            final int[] indexes = ArgumentUtils.indexesOf(headers, chosenField);
            if (indexes.length > 1) {
                out = Arrays.copyOf(out, out.length + indexes.length - 1);
            }
            if (indexes.length == 0) {
                out[i++] = -1;
            }
            else {
                for (int j = 0; j < indexes.length; ++j) {
                    out[i++] = indexes[j];
                }
            }
        }
        return out;
    }
}
