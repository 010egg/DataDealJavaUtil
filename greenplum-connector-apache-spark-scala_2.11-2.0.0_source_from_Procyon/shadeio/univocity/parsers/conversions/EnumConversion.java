// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.conversions;

import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.Iterator;
import java.util.HashMap;
import java.util.Set;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class EnumConversion<T extends Enum<T>> extends ObjectConversion<T>
{
    private final Class<T> enumType;
    private final Field customEnumField;
    private final Method customEnumMethod;
    private final EnumSelector[] selectors;
    private final Map<String, T>[] conversions;
    
    public EnumConversion(final Class<T> enumType) {
        this(enumType, new EnumSelector[] { EnumSelector.NAME, EnumSelector.ORDINAL, EnumSelector.STRING });
    }
    
    public EnumConversion(final Class<T> enumType, final EnumSelector... selectors) {
        this((Class<Enum>)enumType, (Enum)null, null, null, selectors);
    }
    
    public EnumConversion(final Class<T> enumType, final String customEnumElement, final EnumSelector... selectors) {
        this((Class<Enum>)enumType, (Enum)null, null, customEnumElement, new EnumSelector[0]);
    }
    
    public EnumConversion(final Class<T> enumType, final T valueIfStringIsNull, final String valueIfEnumIsNull, String customEnumElement, final EnumSelector... selectors) {
        super(valueIfStringIsNull, valueIfEnumIsNull);
        this.enumType = enumType;
        if (customEnumElement != null) {
            customEnumElement = customEnumElement.trim();
            if (customEnumElement.isEmpty()) {
                customEnumElement = null;
            }
        }
        final LinkedHashSet<EnumSelector> selectorSet = new LinkedHashSet<EnumSelector>();
        Collections.addAll(selectorSet, selectors);
        if ((selectorSet.contains(EnumSelector.CUSTOM_FIELD) || selectorSet.contains(EnumSelector.CUSTOM_METHOD)) && customEnumElement == null) {
            throw new IllegalArgumentException("Cannot create custom enum conversion without a field name to use");
        }
        Field field = null;
        Method method = null;
        if (customEnumElement != null) {
            IllegalStateException fieldError = null;
            IllegalStateException methodError = null;
            try {
                field = enumType.getDeclaredField(customEnumElement);
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
            }
            catch (Throwable e) {
                fieldError = new IllegalStateException("Unable to access custom field '" + customEnumElement + "' in enumeration type " + enumType.getName(), e);
            }
            if (field == null) {
                try {
                    try {
                        method = enumType.getDeclaredMethod(customEnumElement, (Class<?>[])new Class[0]);
                    }
                    catch (NoSuchMethodException e2) {
                        method = enumType.getDeclaredMethod(customEnumElement, String.class);
                        if (!Modifier.isStatic(method.getModifiers())) {
                            throw new IllegalArgumentException("Custom method '" + customEnumElement + "' in enumeration type " + enumType.getName() + " must be static");
                        }
                        if (method.getReturnType() != enumType) {
                            throw new IllegalArgumentException("Custom method '" + customEnumElement + "' in enumeration type " + enumType.getName() + " must return " + enumType.getName());
                        }
                    }
                    if (!method.isAccessible()) {
                        method.setAccessible(true);
                    }
                }
                catch (Throwable e) {
                    methodError = new IllegalStateException("Unable to access custom method '" + customEnumElement + "' in enumeration type " + enumType.getName(), e);
                }
                if (method != null) {
                    if (method.getReturnType() == Void.TYPE) {
                        throw new IllegalArgumentException("Custom method '" + customEnumElement + "' in enumeration type " + enumType.getName() + " must return a value");
                    }
                    if (!selectorSet.contains(EnumSelector.CUSTOM_METHOD)) {
                        selectorSet.add(EnumSelector.CUSTOM_METHOD);
                    }
                }
            }
            else if (!selectorSet.contains(EnumSelector.CUSTOM_FIELD)) {
                selectorSet.add(EnumSelector.CUSTOM_FIELD);
            }
            if (selectorSet.contains(EnumSelector.CUSTOM_FIELD) && fieldError != null) {
                throw fieldError;
            }
            if (selectorSet.contains(EnumSelector.CUSTOM_METHOD) && methodError != null) {
                throw methodError;
            }
            if (field == null && method == null) {
                throw new IllegalStateException("No method/field named '" + customEnumElement + "' found in enumeration type " + enumType.getName());
            }
        }
        if (selectorSet.contains(EnumSelector.CUSTOM_FIELD) && selectorSet.contains(EnumSelector.CUSTOM_METHOD)) {
            throw new IllegalArgumentException("Cannot create custom enum conversion using both method and field values");
        }
        if (selectorSet.isEmpty()) {
            throw new IllegalArgumentException("Selection of enum conversion types cannot be empty.");
        }
        this.customEnumField = field;
        this.customEnumMethod = method;
        this.selectors = selectorSet.toArray(new EnumSelector[selectorSet.size()]);
        this.conversions = (Map<String, T>[])new Map[selectorSet.size()];
        this.initializeMappings(selectorSet);
    }
    
    private void initializeMappings(final Set<EnumSelector> conversionTypes) {
        final T[] constants = this.enumType.getEnumConstants();
        int i = 0;
        for (final EnumSelector conversionType : conversionTypes) {
            final Map<String, T> map = new HashMap<String, T>(constants.length);
            this.conversions[i++] = map;
            for (final T constant : constants) {
                final String key = this.getKey(constant, conversionType);
                if (key != null) {
                    if (map.containsKey(key)) {
                        throw new IllegalArgumentException("Enumeration element type " + conversionType + " does not uniquely identify elements of " + this.enumType.getName() + ". Got duplicate value '" + key + "' from constants '" + constant + "' and '" + map.get(key) + "'.");
                    }
                    map.put(key, constant);
                }
            }
        }
    }
    
    private String getKey(final T constant, final EnumSelector conversionType) {
        switch (conversionType) {
            case NAME: {
                return constant.name();
            }
            case ORDINAL: {
                return String.valueOf(constant.ordinal());
            }
            case STRING: {
                return constant.toString();
            }
            case CUSTOM_FIELD: {
                try {
                    return String.valueOf(this.customEnumField.get(constant));
                }
                catch (Throwable e) {
                    throw new IllegalStateException("Error reading custom field '" + this.customEnumField.getName() + "' from enumeration constant '" + constant + "' of type " + this.enumType.getName(), e);
                }
            }
            case CUSTOM_METHOD: {
                try {
                    if (this.customEnumMethod.getParameterTypes().length == 0) {
                        return String.valueOf(this.customEnumMethod.invoke(constant, new Object[0]));
                    }
                    return null;
                }
                catch (Throwable e) {
                    throw new IllegalStateException("Error reading custom method '" + this.customEnumMethod.getName() + "' from enumeration constant '" + constant + "' of type " + this.enumType.getName(), e);
                }
                break;
            }
        }
        throw new IllegalStateException("Unsupported enumeration selector type " + conversionType);
    }
    
    @Override
    public String revert(final T input) {
        if (input == null) {
            return super.revert((T)null);
        }
        return this.getKey(input, this.selectors[0]);
    }
    
    @Override
    protected T fromString(final String input) {
        for (final Map<String, T> conversion : this.conversions) {
            final T value = conversion.get(input);
            if (value != null) {
                return value;
            }
        }
        DataProcessingException exception = null;
        if (this.customEnumMethod.getParameterTypes().length == 1) {
            try {
                final T out = (T)this.customEnumMethod.invoke(null, input);
                return out;
            }
            catch (Exception e) {
                exception = new DataProcessingException("Cannot convert '{value}' to enumeration of type " + this.enumType.getName() + " using method " + this.customEnumMethod.getName(), e);
            }
        }
        if (exception == null) {
            exception = new DataProcessingException("Cannot convert '{value}' to enumeration of type " + this.enumType.getName());
        }
        exception.setValue(input);
        exception.markAsNonFatal();
        throw exception;
    }
}
