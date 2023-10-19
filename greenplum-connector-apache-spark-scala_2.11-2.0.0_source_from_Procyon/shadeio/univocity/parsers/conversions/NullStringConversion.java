// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.util.Collection;
import java.util.Collections;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.HashSet;
import java.util.Set;

public class NullStringConversion implements Conversion<Object, Object>
{
    private final Set<String> nullStrings;
    private final String defaultNullString;
    
    public NullStringConversion(final String... nullRepresentations) {
        this.nullStrings = new HashSet<String>();
        ArgumentUtils.noNulls("Null representation strings", nullRepresentations);
        Collections.addAll(this.nullStrings, nullRepresentations);
        this.defaultNullString = nullRepresentations[0];
    }
    
    @Override
    public Object execute(final Object input) {
        if (input == null) {
            return null;
        }
        if (this.nullStrings.contains(String.valueOf(input))) {
            return null;
        }
        return input;
    }
    
    @Override
    public Object revert(final Object input) {
        if (input == null) {
            return this.defaultNullString;
        }
        return input;
    }
}
