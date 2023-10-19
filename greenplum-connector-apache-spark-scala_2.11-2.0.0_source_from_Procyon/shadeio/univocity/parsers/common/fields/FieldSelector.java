// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

public interface FieldSelector
{
    int[] getFieldIndexes(final String[] p0);
    
    String describe();
}
