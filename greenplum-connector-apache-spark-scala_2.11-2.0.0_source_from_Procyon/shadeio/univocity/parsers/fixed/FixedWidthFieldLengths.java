// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.fixed;

import java.util.LinkedHashMap;

@Deprecated
public class FixedWidthFieldLengths extends FixedWidthFields
{
    public FixedWidthFieldLengths(final LinkedHashMap<String, Integer> fields) {
        super(fields);
    }
    
    public FixedWidthFieldLengths(final String[] headers, final int[] lengths) {
        super(headers, lengths);
    }
    
    public FixedWidthFieldLengths(final int... fieldLengths) {
        super(fieldLengths);
    }
}
