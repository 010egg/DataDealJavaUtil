// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import java.util.Iterator;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Collection;
import java.util.Collections;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.LinkedHashSet;
import java.util.Set;

public class BooleanConversion extends ObjectConversion<Boolean>
{
    private String defaultForTrue;
    private String defaultForFalse;
    private final Set<String> falseValues;
    private final Set<String> trueValues;
    
    public BooleanConversion(final String[] valuesForTrue, final String[] valuesForFalse) {
        this(null, null, valuesForTrue, valuesForFalse);
    }
    
    public BooleanConversion(final Boolean valueIfStringIsNull, final String valueIfObjectIsNull, final String[] valuesForTrue, final String[] valuesForFalse) {
        super(valueIfStringIsNull, valueIfObjectIsNull);
        this.falseValues = new LinkedHashSet<String>();
        this.trueValues = new LinkedHashSet<String>();
        ArgumentUtils.notEmpty("Values for true", valuesForTrue);
        ArgumentUtils.notEmpty("Values for false", valuesForFalse);
        Collections.addAll(this.falseValues, valuesForFalse);
        Collections.addAll(this.trueValues, valuesForTrue);
        ArgumentUtils.normalize(this.falseValues);
        ArgumentUtils.normalize(this.trueValues);
        for (final String falseValue : this.falseValues) {
            if (this.trueValues.contains(falseValue)) {
                throw new DataProcessingException("Ambiguous string representation for both false and true values: '" + falseValue + '\'');
            }
        }
        this.defaultForTrue = valuesForTrue[0];
        this.defaultForFalse = valuesForFalse[0];
    }
    
    @Override
    public String revert(final Boolean input) {
        if (input != null) {
            if (Boolean.FALSE.equals(input)) {
                return this.defaultForFalse;
            }
            if (Boolean.TRUE.equals(input)) {
                return this.defaultForTrue;
            }
        }
        return this.getValueIfObjectIsNull();
    }
    
    @Override
    protected Boolean fromString(final String input) {
        if (input != null) {
            return getBoolean(input, this.trueValues, this.falseValues);
        }
        return super.getValueIfStringIsNull();
    }
    
    public static Boolean getBoolean(final String booleanString, String[] trueValues, String[] falseValues) {
        trueValues = ((trueValues == null || trueValues.length == 0) ? new String[] { "true" } : trueValues);
        falseValues = ((falseValues == null || falseValues.length == 0) ? new String[] { "false" } : falseValues);
        final BooleanConversion tmp = new BooleanConversion(trueValues, falseValues);
        return getBoolean(booleanString, tmp.trueValues, tmp.falseValues);
    }
    
    private static Boolean getBoolean(final String defaultString, final Set<String> trueValues, final Set<String> falseValues) {
        final String normalized = ArgumentUtils.normalize(defaultString);
        if (falseValues.contains(normalized)) {
            return Boolean.FALSE;
        }
        if (trueValues.contains(normalized)) {
            return Boolean.TRUE;
        }
        final DataProcessingException exception = new DataProcessingException("Unable to convert '{value}' to Boolean. Allowed Strings are: " + trueValues + " for true; and " + falseValues + " for false.");
        exception.setValue(defaultString);
        throw exception;
    }
}
