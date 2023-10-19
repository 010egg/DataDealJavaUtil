// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.Iterator;
import java.util.List;

public class FieldIndexSelector extends FieldSet<Integer> implements FieldSelector
{
    @Override
    public int[] getFieldIndexes(final String[] columns) {
        final List<Integer> chosenIndexes = this.get();
        final int[] out = new int[chosenIndexes.size()];
        int i = 0;
        for (final Integer index : chosenIndexes) {
            out[i++] = index;
        }
        return out;
    }
}
