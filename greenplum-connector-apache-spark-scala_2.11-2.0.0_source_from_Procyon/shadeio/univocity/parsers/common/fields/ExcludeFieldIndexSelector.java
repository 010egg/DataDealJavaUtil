// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;

public class ExcludeFieldIndexSelector extends FieldSet<Integer> implements FieldSelector
{
    @Override
    public int[] getFieldIndexes(final String[] columns) {
        if (columns == null) {
            return null;
        }
        final Set<Integer> chosenFields = new HashSet<Integer>(this.get());
        for (final Integer chosenIndex : chosenFields) {
            if (chosenIndex >= columns.length || chosenIndex < 0) {
                throw new IndexOutOfBoundsException("Exclusion index '" + chosenIndex + "' is out of bounds. It must be between '0' and '" + (columns.length - 1) + '\'');
            }
        }
        final int[] out = new int[columns.length - chosenFields.size()];
        int j = 0;
        for (int i = 0; i < columns.length; ++i) {
            if (!chosenFields.contains(i)) {
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
