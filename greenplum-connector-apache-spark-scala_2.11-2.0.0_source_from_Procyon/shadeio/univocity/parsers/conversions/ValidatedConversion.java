// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import shadeio.univocity.parsers.common.DataValidationException;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.Collection;
import java.util.HashSet;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.Set;

public class ValidatedConversion implements Conversion<Object, Object>
{
    private final String regexToMatch;
    private final boolean nullable;
    private final boolean allowBlanks;
    private final Set<String> oneOf;
    private final Set<String> noneOf;
    private final Matcher matcher;
    private final Validator[] validators;
    
    public ValidatedConversion(final String regexToMatch) {
        this(false, false, null, null, regexToMatch);
    }
    
    public ValidatedConversion(final boolean nullable, final boolean allowBlanks) {
        this(nullable, allowBlanks, null, null, null);
    }
    
    public ValidatedConversion(final boolean nullable, final boolean allowBlanks, final String[] oneOf, final String[] noneOf, final String regexToMatch) {
        this(nullable, allowBlanks, oneOf, noneOf, regexToMatch, null);
    }
    
    public ValidatedConversion(final boolean nullable, final boolean allowBlanks, final String[] oneOf, final String[] noneOf, final String regexToMatch, final Class[] validators) {
        this.regexToMatch = regexToMatch;
        this.matcher = ((regexToMatch == null || regexToMatch.isEmpty()) ? null : Pattern.compile(regexToMatch).matcher(""));
        this.nullable = nullable;
        this.allowBlanks = allowBlanks;
        this.oneOf = ((oneOf == null || oneOf.length == 0) ? null : new HashSet<String>(Arrays.asList(oneOf)));
        this.noneOf = ((noneOf == null || noneOf.length == 0) ? null : new HashSet<String>(Arrays.asList(noneOf)));
        this.validators = ((validators == null || validators.length == 0) ? new Validator[0] : this.instantiateValidators(validators));
    }
    
    private Validator[] instantiateValidators(final Class[] validators) {
        final Validator[] out = new Validator[validators.length];
        for (int i = 0; i < validators.length; ++i) {
            out[i] = AnnotationHelper.newInstance(Validator.class, (Class<Validator>)validators[i], ArgumentUtils.EMPTY_STRING_ARRAY);
        }
        return out;
    }
    
    @Override
    public Object execute(final Object input) {
        this.validate(input);
        return input;
    }
    
    @Override
    public Object revert(final Object input) {
        this.validate(input);
        return input;
    }
    
    private void validate(final Object value) {
        DataValidationException e = null;
        String str = null;
        if (value == null) {
            if (this.nullable) {
                if (this.noneOf == null || !this.noneOf.contains(null)) {
                    return;
                }
                e = new DataValidationException("Value '{value}' is not allowed.");
            }
            else {
                if (this.oneOf != null && this.oneOf.contains(null)) {
                    return;
                }
                e = new DataValidationException("Null values not allowed.");
            }
        }
        else {
            str = String.valueOf(value);
            if (str.trim().isEmpty()) {
                if (this.allowBlanks) {
                    if (this.noneOf == null || !this.noneOf.contains(str)) {
                        return;
                    }
                    e = new DataValidationException("Value '{value}' is not allowed.");
                }
                else {
                    if (this.oneOf != null && this.oneOf.contains(str)) {
                        return;
                    }
                    e = new DataValidationException("Blanks are not allowed. '{value}' is blank.");
                }
            }
            if (this.matcher != null && e == null) {
                final boolean match;
                synchronized (this.matcher) {
                    match = this.matcher.reset(str).matches();
                }
                if (!match) {
                    e = new DataValidationException("Value '{value}' does not match expected pattern: '" + this.regexToMatch + "'");
                }
            }
        }
        if (this.oneOf != null && !this.oneOf.contains(str)) {
            e = new DataValidationException("Value '{value}' is not allowed. Expecting one of: " + this.oneOf);
        }
        if (e == null && this.noneOf != null && this.noneOf.contains(str)) {
            e = new DataValidationException("Value '{value}' is not allowed.");
        }
        for (int i = 0; e == null && i < this.validators.length; ++i) {
            final String error = this.validators[i].validate(value);
            if (error != null && !error.trim().isEmpty()) {
                e = new DataValidationException("Value '{value}' didn't pass validation: " + error);
            }
        }
        if (e != null) {
            e.setValue(value);
            throw e;
        }
    }
}
