// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.fields;

import java.util.Iterator;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.List;
import shadeio.univocity.parsers.conversions.Conversion;
import java.util.Map;

abstract class AbstractConversionMapping<T>
{
    private Map<FieldSelector, Conversion<String, ?>[]> conversionsMap;
    private final List<FieldSelector> conversionSequence;
    
    AbstractConversionMapping(final List<FieldSelector> conversionSequence) {
        this.conversionSequence = conversionSequence;
    }
    
    public FieldSet<T> registerConversions(final Conversion<String, ?>... conversions) {
        ArgumentUtils.noNulls("Conversions", conversions);
        final FieldSelector selector = this.newFieldSelector();
        if (this.conversionsMap == null) {
            this.conversionsMap = new LinkedHashMap<FieldSelector, Conversion<String, ?>[]>();
        }
        this.conversionsMap.put(selector, conversions);
        this.conversionSequence.add(selector);
        if (selector instanceof FieldSet) {
            return (FieldSet<T>)selector;
        }
        return null;
    }
    
    protected abstract FieldSelector newFieldSelector();
    
    public void prepareExecution(final boolean writing, final FieldSelector selector, final Map<Integer, List<Conversion<?, ?>>> conversionsByIndex, final String[] values) {
        if (this.conversionsMap == null) {
            return;
        }
        final Conversion<String, ?>[] conversions = this.conversionsMap.get(selector);
        if (conversions == null) {
            return;
        }
        int[] fieldIndexes = selector.getFieldIndexes(values);
        if (fieldIndexes == null) {
            fieldIndexes = ArgumentUtils.toIntArray(conversionsByIndex.keySet());
        }
        for (final int fieldIndex : fieldIndexes) {
            List<Conversion<?, ?>> conversionsAtIndex = conversionsByIndex.get(fieldIndex);
            if (conversionsAtIndex == null) {
                conversionsAtIndex = new ArrayList<Conversion<?, ?>>();
                conversionsByIndex.put(fieldIndex, conversionsAtIndex);
            }
            validateDuplicates(selector, conversionsAtIndex, conversions);
            conversionsAtIndex.addAll(Arrays.asList(conversions));
        }
    }
    
    private static void validateDuplicates(final FieldSelector selector, final List<Conversion<?, ?>> conversionsAtIndex, final Conversion<?, ?>[] conversionsToAdd) {
        for (final Conversion<?, ?> toAdd : conversionsToAdd) {
            for (final Conversion<?, ?> existing : conversionsAtIndex) {
                if (toAdd == existing) {
                    throw new DataProcessingException("Duplicate conversion " + toAdd.getClass().getName() + " being applied to " + selector.describe());
                }
            }
        }
    }
    
    public boolean isEmpty() {
        return this.conversionsMap == null || this.conversionsMap.isEmpty();
    }
}
