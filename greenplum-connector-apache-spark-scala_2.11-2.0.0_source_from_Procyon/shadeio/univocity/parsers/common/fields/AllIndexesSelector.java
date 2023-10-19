// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

public class AllIndexesSelector implements FieldSelector
{
    @Override
    public int[] getFieldIndexes(final String[] headers) {
        if (headers == null) {
            return null;
        }
        final int[] out = new int[headers.length];
        for (int i = 0; i < out.length; ++i) {
            out[i] = i;
        }
        return out;
    }
    
    @Override
    public String describe() {
        return "all fields";
    }
}
