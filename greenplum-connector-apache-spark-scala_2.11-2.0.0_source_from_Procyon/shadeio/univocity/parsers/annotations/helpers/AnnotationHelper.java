// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations.helpers;

import java.lang.reflect.Array;
import shadeio.univocity.parsers.annotations.Copy;
import java.util.Stack;
import java.util.HashSet;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Collections;
import java.util.Comparator;
import shadeio.univocity.parsers.annotations.Nested;
import java.lang.reflect.Field;
import shadeio.univocity.parsers.annotations.Headers;
import shadeio.univocity.parsers.common.ArgumentUtils;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import shadeio.univocity.parsers.annotations.HeaderTransformer;
import java.text.DateFormatSymbols;
import java.util.TimeZone;
import java.util.Currency;
import java.lang.reflect.Method;
import shadeio.univocity.parsers.common.beans.PropertyWrapper;
import java.util.Map;
import java.text.DecimalFormatSymbols;
import shadeio.univocity.parsers.common.beans.BeanHelper;
import java.util.HashMap;
import shadeio.univocity.parsers.conversions.ObjectConversion;
import java.math.BigInteger;
import java.lang.reflect.Constructor;
import shadeio.univocity.parsers.common.input.CharAppender;
import shadeio.univocity.parsers.common.input.DefaultCharAppender;
import java.util.Locale;
import shadeio.univocity.parsers.conversions.ToStringConversion;
import shadeio.univocity.parsers.annotations.Convert;
import java.util.Arrays;
import shadeio.univocity.parsers.conversions.FormattedConversion;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.Date;
import shadeio.univocity.parsers.conversions.NumericConversion;
import java.math.BigDecimal;
import shadeio.univocity.parsers.annotations.Format;
import shadeio.univocity.parsers.common.DataProcessingException;
import shadeio.univocity.parsers.conversions.BooleanConversion;
import shadeio.univocity.parsers.annotations.BooleanString;
import shadeio.univocity.parsers.annotations.Replace;
import shadeio.univocity.parsers.annotations.UpperCase;
import shadeio.univocity.parsers.annotations.LowerCase;
import shadeio.univocity.parsers.annotations.Trim;
import shadeio.univocity.parsers.conversions.EnumConversion;
import shadeio.univocity.parsers.conversions.EnumSelector;
import shadeio.univocity.parsers.annotations.EnumOptions;
import shadeio.univocity.parsers.conversions.ValidatedConversion;
import shadeio.univocity.parsers.annotations.Validate;
import shadeio.univocity.parsers.conversions.Conversions;
import shadeio.univocity.parsers.annotations.NullString;
import shadeio.univocity.parsers.conversions.Conversion;
import shadeio.univocity.parsers.annotations.Parsed;
import java.util.Set;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

public class AnnotationHelper
{
    private static AnnotatedElement lastProcessedElement;
    private static Class<? extends Annotation> lastProcessedAnnotationType;
    private static Annotation lastAnnotationFound;
    private static final Set<Class> javaLangAnnotationTypes;
    private static final Set<Class> customAnnotationTypes;
    
    private AnnotationHelper() {
    }
    
    private static String getNullValue(final String defaultValue) {
        if ("null".equals(defaultValue)) {
            return null;
        }
        if ("'null'".equals(defaultValue)) {
            return "null";
        }
        return defaultValue;
    }
    
    private static String getNullWriteValue(final AnnotatedElement target, final Parsed parsed) {
        if (parsed == null) {
            return null;
        }
        return getNullValue(AnnotationRegistry.getValue(target, parsed, "defaultNullWrite", parsed.defaultNullWrite()));
    }
    
    private static String getNullReadValue(final AnnotatedElement target, final Parsed parsed) {
        if (parsed == null) {
            return null;
        }
        return getNullValue(AnnotationRegistry.getValue(target, parsed, "defaultNullRead", parsed.defaultNullRead()));
    }
    
    public static Conversion getConversion(final AnnotatedElement target, final Annotation annotation) {
        return getConversion(getType(target), target, annotation);
    }
    
    public static Conversion getConversion(final Class classType, final Annotation annotation) {
        return getConversion(classType, null, annotation);
    }
    
    private static Conversion getConversion(final Class fieldType, final AnnotatedElement target, final Annotation annotation) {
        try {
            final Parsed parsed = (target == null) ? null : findAnnotation(target, Parsed.class);
            final Class annType = annotation.annotationType();
            final String nullRead = getNullReadValue(target, parsed);
            final String nullWrite = getNullWriteValue(target, parsed);
            if (annType == NullString.class) {
                final NullString nullString = (NullString)annotation;
                final String[] nulls = AnnotationRegistry.getValue(target, nullString, "nulls", nullString.nulls());
                return Conversions.toNull(nulls);
            }
            if (annType == Validate.class) {
                final Validate validate = (Validate)annotation;
                final boolean nullable = AnnotationRegistry.getValue(target, validate, "nullable", validate.nullable());
                final boolean allowBlanks = AnnotationRegistry.getValue(target, validate, "allowBlanks", validate.allowBlanks());
                final String[] oneOf = AnnotationRegistry.getValue(target, validate, "oneOf", validate.oneOf());
                final String[] noneOf = AnnotationRegistry.getValue(target, validate, "noneOf", validate.noneOf());
                final String matches = AnnotationRegistry.getValue(target, validate, "matches", validate.matches());
                final Class[] validators = AnnotationRegistry.getValue(target, validate, "validators", validate.validators());
                return new ValidatedConversion(nullable, allowBlanks, oneOf, noneOf, matches, validators);
            }
            if (annType == EnumOptions.class) {
                if (fieldType.isEnum()) {
                    final EnumOptions enumOptions = (EnumOptions)annotation;
                    final String customElement = AnnotationRegistry.getValue(target, enumOptions, "customElement", enumOptions.customElement());
                    String element = customElement.trim();
                    if (element.isEmpty()) {
                        element = null;
                    }
                    final Enum nullReadValue = (nullRead == null) ? null : Enum.valueOf((Class<Enum>)fieldType, nullRead);
                    final EnumSelector[] selectors = AnnotationRegistry.getValue(target, enumOptions, "selectors", enumOptions.selectors());
                    return new EnumConversion(fieldType, nullReadValue, nullWrite, element, selectors);
                }
                if (target == null) {
                    throw new IllegalStateException("Invalid " + EnumOptions.class.getName() + " instance for converting class " + fieldType.getName() + ". Not an enum type.");
                }
                throw new IllegalStateException("Invalid " + EnumOptions.class.getName() + " annotation on " + describeElement(target) + ". Attribute must be an enum type.");
            }
            else if (annType == Trim.class) {
                final Trim trim = (Trim)annotation;
                final int length = AnnotationRegistry.getValue(target, trim, "length", trim.length());
                if (length == -1) {
                    return Conversions.trim();
                }
                return Conversions.trim(length);
            }
            else {
                if (annType == LowerCase.class) {
                    return Conversions.toLowerCase();
                }
                if (annType == UpperCase.class) {
                    return Conversions.toUpperCase();
                }
                if (annType == Replace.class) {
                    final Replace replace = (Replace)annotation;
                    final String expression = AnnotationRegistry.getValue(target, replace, "expression", replace.expression());
                    final String replacement = AnnotationRegistry.getValue(target, replace, "replacement", replace.replacement());
                    return Conversions.replace(expression, replacement);
                }
                if (annType == BooleanString.class) {
                    if (fieldType == Boolean.TYPE || fieldType == Boolean.class) {
                        final BooleanString boolString = (BooleanString)annotation;
                        final String[] falseStrings = AnnotationRegistry.getValue(target, boolString, "falseStrings", boolString.falseStrings());
                        final String[] trueStrings = AnnotationRegistry.getValue(target, boolString, "trueStrings", boolString.trueStrings());
                        Boolean valueForNull = (nullRead == null) ? null : BooleanConversion.getBoolean(nullRead, trueStrings, falseStrings);
                        if (valueForNull == null && fieldType == Boolean.TYPE) {
                            valueForNull = Boolean.FALSE;
                        }
                        return Conversions.toBoolean(valueForNull, nullWrite, trueStrings, falseStrings);
                    }
                    if (target == null) {
                        throw new DataProcessingException("Invalid  usage of " + BooleanString.class.getName() + ". Got type " + fieldType.getName() + " instead of boolean.");
                    }
                    throw new DataProcessingException("Invalid annotation: " + describeElement(target) + " has type " + fieldType.getName() + " instead of boolean.");
                }
                else {
                    if (annType == Format.class) {
                        final Format format = (Format)annotation;
                        final String[] formats = AnnotationRegistry.getValue(target, format, "formats", format.formats());
                        final String[] options = AnnotationRegistry.getValue(target, format, "options", format.options());
                        final Locale locale = extractLocale(options);
                        Conversion conversion = null;
                        if (fieldType == BigDecimal.class) {
                            final BigDecimal defaultForNull = (nullRead == null) ? null : new BigDecimal(nullRead);
                            conversion = Conversions.formatToBigDecimal(defaultForNull, nullWrite, formats);
                        }
                        else if (Number.class.isAssignableFrom(fieldType) || (fieldType.isPrimitive() && fieldType != Boolean.TYPE && fieldType != Character.TYPE)) {
                            conversion = Conversions.formatToNumber(formats);
                            ((NumericConversion)conversion).setNumberType(fieldType);
                        }
                        else {
                            Date dateIfNull = null;
                            if (nullRead != null) {
                                if ("now".equalsIgnoreCase(nullRead)) {
                                    dateIfNull = new Date();
                                }
                                else {
                                    if (formats.length == 0) {
                                        throw new DataProcessingException("No format defined");
                                    }
                                    final SimpleDateFormat sdf = new SimpleDateFormat(formats[0], locale);
                                    dateIfNull = sdf.parse(nullRead);
                                }
                            }
                            if (Date.class == fieldType) {
                                conversion = Conversions.toDate(locale, dateIfNull, nullWrite, formats);
                            }
                            else if (Calendar.class == fieldType) {
                                Calendar calendarIfNull = null;
                                if (dateIfNull != null) {
                                    calendarIfNull = Calendar.getInstance();
                                    calendarIfNull.setTime(dateIfNull);
                                }
                                conversion = Conversions.toCalendar(locale, calendarIfNull, nullWrite, formats);
                            }
                        }
                        if (conversion != null) {
                            if (options.length > 0) {
                                if (!(conversion instanceof FormattedConversion)) {
                                    throw new DataProcessingException("Options '" + Arrays.toString(options) + "' not supported by conversion of type '" + conversion.getClass() + "'. It must implement " + FormattedConversion.class);
                                }
                                final Object[] arr$;
                                final Object[] formatters = arr$ = ((FormattedConversion)conversion).getFormatterObjects();
                                for (final Object formatter : arr$) {
                                    applyFormatSettings(formatter, options);
                                }
                            }
                            return conversion;
                        }
                    }
                    else if (annType == Convert.class) {
                        final Convert convert = (Convert)annotation;
                        final String[] args = AnnotationRegistry.getValue(target, convert, "args", convert.args());
                        final Class conversionClass = AnnotationRegistry.getValue(target, convert, "conversionClass", convert.conversionClass());
                        return newInstance(Conversion.class, (Class<Conversion>)conversionClass, args);
                    }
                    if (fieldType == String.class && (nullRead != null || nullWrite != null)) {
                        return new ToStringConversion(nullRead, nullWrite);
                    }
                    return null;
                }
            }
        }
        catch (DataProcessingException ex) {
            throw ex;
        }
        catch (Throwable ex2) {
            if (target == null) {
                throw new DataProcessingException("Unexpected error identifying conversions to apply over type " + fieldType, ex2);
            }
            throw new DataProcessingException("Unexpected error identifying conversions to apply over " + describeElement(target), ex2);
        }
    }
    
    private static Locale extractLocale(final String[] options) {
        for (int i = 0; i < options.length; ++i) {
            if (options[i] != null && options[i].startsWith("locale=")) {
                final String locale = options[i].substring("locale=".length());
                final CharAppender appender = new DefaultCharAppender(100, "", 0);
                int j = 0;
                char ch;
                while (j < locale.length() && Character.isLetterOrDigit(ch = locale.charAt(j))) {
                    ++j;
                    appender.append(ch);
                }
                final String languageCode = appender.getAndReset();
                ++j;
                while (j < locale.length() && Character.isLetterOrDigit(ch = locale.charAt(j))) {
                    ++j;
                    appender.append(ch);
                }
                final String countryCode = appender.getAndReset();
                ++j;
                while (j < locale.length() && Character.isLetterOrDigit(ch = locale.charAt(j))) {
                    ++j;
                    appender.append(ch);
                }
                final String variant = appender.getAndReset();
                options[i] = null;
                return new Locale(languageCode, countryCode, variant);
            }
        }
        return Locale.getDefault();
    }
    
    public static <T> T newInstance(final Class parent, final Class<T> type, final String[] args) {
        if (!parent.isAssignableFrom(type)) {
            throw new DataProcessingException("Not a valid " + parent.getSimpleName() + " class: '" + type.getSimpleName() + "' (" + type.getName() + ')');
        }
        try {
            final Constructor constructor = type.getConstructor(String[].class);
            return constructor.newInstance(args);
        }
        catch (NoSuchMethodException e) {
            if (args.length == 0) {
                try {
                    return type.newInstance();
                }
                catch (Exception ex) {
                    throw new DataProcessingException("Unexpected error instantiating custom " + parent.getSimpleName() + " class '" + type.getSimpleName() + "' (" + type.getName() + ')', e);
                }
            }
            throw new DataProcessingException("Could not find a public constructor with a String[] parameter in custom " + parent.getSimpleName() + " class '" + type.getSimpleName() + "' (" + type.getName() + ')', e);
        }
        catch (Exception e2) {
            throw new DataProcessingException("Unexpected error instantiating custom " + parent.getSimpleName() + " class '" + type.getSimpleName() + "' (" + type.getName() + ')', e2);
        }
    }
    
    public static Conversion getDefaultConversion(final Class fieldType, final AnnotatedElement target, final Parsed parsed) {
        final String nullRead = getNullReadValue(target, parsed);
        Object valueIfStringIsNull = null;
        ObjectConversion conversion = null;
        if (fieldType == Boolean.class || fieldType == Boolean.TYPE) {
            conversion = Conversions.toBoolean();
            valueIfStringIsNull = ((nullRead == null) ? null : Boolean.valueOf(nullRead));
        }
        else if (fieldType == Character.class || fieldType == Character.TYPE) {
            conversion = Conversions.toChar();
            if (nullRead != null && nullRead.length() > 1) {
                throw new DataProcessingException("Invalid default value for character '" + nullRead + "'. It should contain one character only.");
            }
            valueIfStringIsNull = ((nullRead == null) ? null : Character.valueOf(nullRead.charAt(0)));
        }
        else if (fieldType == Byte.class || fieldType == Byte.TYPE) {
            conversion = Conversions.toByte();
            valueIfStringIsNull = ((nullRead == null) ? null : Byte.valueOf(nullRead));
        }
        else if (fieldType == Short.class || fieldType == Short.TYPE) {
            conversion = Conversions.toShort();
            valueIfStringIsNull = ((nullRead == null) ? null : Short.valueOf(nullRead));
        }
        else if (fieldType == Integer.class || fieldType == Integer.TYPE) {
            conversion = Conversions.toInteger();
            valueIfStringIsNull = ((nullRead == null) ? null : Integer.valueOf(nullRead));
        }
        else if (fieldType == Long.class || fieldType == Long.TYPE) {
            conversion = Conversions.toLong();
            valueIfStringIsNull = ((nullRead == null) ? null : Long.valueOf(nullRead));
        }
        else if (fieldType == Float.class || fieldType == Float.TYPE) {
            conversion = Conversions.toFloat();
            valueIfStringIsNull = ((nullRead == null) ? null : Float.valueOf(nullRead));
        }
        else if (fieldType == Double.class || fieldType == Double.TYPE) {
            conversion = Conversions.toDouble();
            valueIfStringIsNull = ((nullRead == null) ? null : Double.valueOf(nullRead));
        }
        else if (fieldType == BigInteger.class) {
            conversion = Conversions.toBigInteger();
            valueIfStringIsNull = ((nullRead == null) ? null : new BigInteger(nullRead));
        }
        else if (fieldType == BigDecimal.class) {
            conversion = Conversions.toBigDecimal();
            valueIfStringIsNull = ((nullRead == null) ? null : new BigDecimal(nullRead));
        }
        else if (Enum.class.isAssignableFrom(fieldType)) {
            conversion = Conversions.toEnum((Class<Enum>)fieldType);
        }
        if (conversion != null) {
            conversion.setValueIfStringIsNull(valueIfStringIsNull);
            conversion.setValueIfObjectIsNull(getNullWriteValue(target, parsed));
        }
        return conversion;
    }
    
    public static Conversion getDefaultConversion(final AnnotatedElement target) {
        final Parsed parsed = findAnnotation(target, Parsed.class);
        return getDefaultConversion(getType(target), target, parsed);
    }
    
    public static void applyFormatSettings(final Object formatter, final String[] propertiesAndValues) {
        if (propertiesAndValues.length == 0) {
            return;
        }
        final Map<String, String> values = new HashMap<String, String>();
        for (final String setting : propertiesAndValues) {
            if (setting != null) {
                final String[] pair = setting.split("=");
                if (pair.length != 2) {
                    throw new DataProcessingException("Illegal format setting '" + setting + "' among: " + Arrays.toString(propertiesAndValues));
                }
                values.put(pair[0], pair[1]);
            }
        }
        try {
            for (final PropertyWrapper property : BeanHelper.getPropertyDescriptors(formatter.getClass())) {
                final String name = property.getName();
                String value = values.remove(name);
                if (value != null) {
                    invokeSetter(formatter, property, value);
                }
                if ("decimalFormatSymbols".equals(property.getName())) {
                    final DecimalFormatSymbols modifiedDecimalSymbols = new DecimalFormatSymbols();
                    boolean modified = false;
                    try {
                        for (final PropertyWrapper prop : BeanHelper.getPropertyDescriptors(modifiedDecimalSymbols.getClass())) {
                            value = values.remove(prop.getName());
                            if (value != null) {
                                invokeSetter(modifiedDecimalSymbols, prop, value);
                                modified = true;
                            }
                        }
                        if (modified) {
                            final Method writeMethod = property.getWriteMethod();
                            if (writeMethod == null) {
                                throw new IllegalStateException("No write method defined for property " + property.getName());
                            }
                            writeMethod.invoke(formatter, modifiedDecimalSymbols);
                        }
                    }
                    catch (Throwable ex) {
                        throw new DataProcessingException("Error trying to configure decimal symbols of formatter '" + formatter.getClass() + '.', ex);
                    }
                }
            }
        }
        catch (Exception ex2) {}
        if (!values.isEmpty()) {
            throw new DataProcessingException("Cannot find properties in formatter of type '" + formatter.getClass() + "': " + values);
        }
    }
    
    private static void invokeSetter(final Object formatter, final PropertyWrapper property, final String value) {
        final Method writeMethod = property.getWriteMethod();
        if (writeMethod == null) {
            final DataProcessingException exception = new DataProcessingException("Cannot set property '" + property.getName() + "' of formatter '" + formatter.getClass() + "' to '{value}'. No setter defined");
            exception.setValue(value);
            throw exception;
        }
        final Class<?> parameterType = writeMethod.getParameterTypes()[0];
        Object parameterValue = null;
        if (parameterType == String.class) {
            parameterValue = value;
        }
        else if (parameterType == Integer.class || parameterType == Integer.TYPE) {
            parameterValue = Integer.parseInt(value);
        }
        else if (parameterType == Character.class || parameterType == Character.TYPE) {
            parameterValue = value.charAt(0);
        }
        else if (parameterType == Currency.class) {
            parameterValue = Currency.getInstance(value);
        }
        else if (parameterType == Boolean.class || parameterType == Boolean.TYPE) {
            parameterValue = Boolean.valueOf(value);
        }
        else if (parameterType == TimeZone.class) {
            parameterValue = TimeZone.getTimeZone(value);
        }
        else if (parameterType == DateFormatSymbols.class) {
            parameterValue = DateFormatSymbols.getInstance(new Locale(value));
        }
        if (parameterValue == null) {
            final DataProcessingException exception2 = new DataProcessingException("Cannot set property '" + property.getName() + "' of formatter '" + formatter.getClass() + ". Cannot convert '{value}' to instance of " + parameterType);
            exception2.setValue(value);
            throw exception2;
        }
        try {
            writeMethod.invoke(formatter, parameterValue);
        }
        catch (Throwable e) {
            final DataProcessingException exception3 = new DataProcessingException("Error setting property '" + property.getName() + "' of formatter '" + formatter.getClass() + ", with '{parameterValue}' (converted from '{value}')", e);
            exception3.setValue("parameterValue", parameterValue);
            exception3.setValue(value);
            throw exception3;
        }
    }
    
    private static boolean allFieldsIndexOrNameBased(final boolean searchName, final Class<?> beanClass, final MethodFilter filter) {
        boolean hasAnnotation = false;
        for (final TransformedHeader header : getFieldSequence(beanClass, true, null, filter)) {
            if (header != null) {
                if (header.getTarget() == null) {
                    continue;
                }
                final AnnotatedElement element = header.getTarget();
                if (element instanceof Method && filter.reject((Method)element)) {
                    continue;
                }
                final Parsed annotation = findAnnotation(element, Parsed.class);
                if (annotation == null) {
                    continue;
                }
                hasAnnotation = true;
                final int index = AnnotationRegistry.getValue(element, annotation, "index", annotation.index());
                if ((index != -1 && searchName) || (index == -1 && !searchName)) {
                    return false;
                }
                continue;
            }
        }
        return hasAnnotation;
    }
    
    public static boolean allFieldsIndexBasedForParsing(final Class<?> beanClass) {
        return allFieldsIndexOrNameBased(false, beanClass, MethodFilter.ONLY_SETTERS);
    }
    
    public static boolean allFieldsNameBasedForParsing(final Class<?> beanClass) {
        return allFieldsIndexOrNameBased(true, beanClass, MethodFilter.ONLY_SETTERS);
    }
    
    public static boolean allFieldsIndexBasedForWriting(final Class<?> beanClass) {
        return allFieldsIndexOrNameBased(false, beanClass, MethodFilter.ONLY_GETTERS);
    }
    
    public static boolean allFieldsNameBasedForWriting(final Class<?> beanClass) {
        return allFieldsIndexOrNameBased(true, beanClass, MethodFilter.ONLY_GETTERS);
    }
    
    public static Integer[] getSelectedIndexes(final Class<?> beanClass, final MethodFilter filter) {
        final List<Integer> indexes = new ArrayList<Integer>();
        for (final TransformedHeader header : getFieldSequence(beanClass, true, null, filter)) {
            if (header == null) {
                continue;
            }
            final int index = header.getHeaderIndex();
            if (index == -1) {
                continue;
            }
            if (filter == MethodFilter.ONLY_GETTERS && indexes.contains(index)) {
                throw new IllegalArgumentException("Duplicate field index '" + index + "' found in attribute '" + header.getTargetName() + "' of class " + beanClass.getName());
            }
            indexes.add(index);
        }
        return indexes.toArray(new Integer[indexes.size()]);
    }
    
    public static String[] deriveHeaderNamesFromFields(final Class<?> beanClass, final MethodFilter filter) {
        final List<TransformedHeader> sequence = getFieldSequence(beanClass, true, null, filter);
        final List<String> out = new ArrayList<String>(sequence.size());
        for (final TransformedHeader field : sequence) {
            if (field == null) {
                return ArgumentUtils.EMPTY_STRING_ARRAY;
            }
            out.add(field.getHeaderName());
        }
        return out.toArray(new String[out.size()]);
    }
    
    public static <T extends Annotation> T findAnnotationInClass(final Class<?> beanClass, final Class<T> annotation) {
        Class<?> parent = beanClass;
        do {
            T out = parent.getAnnotation(annotation);
            if (out != null) {
                return out;
            }
            for (final Class<?> iface : parent.getInterfaces()) {
                out = (T)findAnnotationInClass(iface, (Class<Annotation>)annotation);
                if (out != null) {
                    return out;
                }
            }
            parent = parent.getSuperclass();
        } while (parent != null);
        return null;
    }
    
    public static Headers findHeadersAnnotation(final Class<?> beanClass) {
        return findAnnotationInClass(beanClass, Headers.class);
    }
    
    public static Class<?> getType(final AnnotatedElement element) {
        if (element instanceof Field) {
            return ((Field)element).getType();
        }
        final Method method = (Method)element;
        final Class<?>[] params = method.getParameterTypes();
        if (params.length == 1) {
            return params[0];
        }
        if (params.length > 1) {
            throw new IllegalArgumentException("Method " + describeElement(element) + " cannot have multiple parameters");
        }
        final Class<?> returnType = method.getReturnType();
        if (returnType != Void.TYPE) {
            return returnType;
        }
        throw new IllegalArgumentException("Method " + describeElement(element) + " must return a value if it has no input parameter");
    }
    
    public static Class<?> getDeclaringClass(final AnnotatedElement element) {
        if (element instanceof Field) {
            return ((Field)element).getDeclaringClass();
        }
        return ((Method)element).getDeclaringClass();
    }
    
    public static String getName(final AnnotatedElement element) {
        if (element instanceof Field) {
            return ((Field)element).getName();
        }
        return ((Method)element).getName();
    }
    
    static String describeElement(final AnnotatedElement element) {
        String description;
        if (element instanceof Field) {
            description = "attribute '" + ((Field)element).getName() + "'";
        }
        else {
            description = "method '" + ((Method)element).getName() + "'";
        }
        return description + " of class " + getDeclaringClass(element).getName();
    }
    
    private static void processAnnotations(final AnnotatedElement element, final boolean processNested, final List<Integer> indexes, final List<TransformedHeader> tmp, final Map<AnnotatedElement, List<TransformedHeader>> nestedReplacements, final HeaderTransformer transformer, final MethodFilter filter) {
        final Parsed annotation = findAnnotation(element, Parsed.class);
        if (annotation != null) {
            final TransformedHeader header = new TransformedHeader(element, transformer);
            if (filter == MethodFilter.ONLY_GETTERS && header.getHeaderIndex() >= 0 && indexes.contains(header.getHeaderIndex())) {
                throw new IllegalArgumentException("Duplicate field index '" + header.getHeaderIndex() + "' found in " + describeElement(element));
            }
            tmp.add(header);
            indexes.add(header.getHeaderIndex());
        }
        if (processNested) {
            final Nested nested = findAnnotation(element, Nested.class);
            if (nested != null) {
                tmp.add(new TransformedHeader(element, null));
                Class nestedBeanType = AnnotationRegistry.getValue(element, nested, "type", nested.type());
                if (nestedBeanType == Object.class) {
                    nestedBeanType = getType(element);
                }
                final Class<? extends HeaderTransformer> transformerType = AnnotationRegistry.getValue(element, nested, "headerTransformer", nested.headerTransformer());
                if (transformerType != HeaderTransformer.class) {
                    final String[] args = AnnotationRegistry.getValue(element, nested, "args", nested.args());
                    final HeaderTransformer innerTransformer = newInstance(HeaderTransformer.class, transformerType, args);
                    nestedReplacements.put(element, getFieldSequence(nestedBeanType, true, indexes, innerTransformer, filter));
                }
                else {
                    nestedReplacements.put(element, getFieldSequence(nestedBeanType, true, indexes, transformer, filter));
                }
            }
        }
    }
    
    public static List<TransformedHeader> getFieldSequence(final Class beanClass, final boolean processNested, final HeaderTransformer transformer, final MethodFilter filter) {
        final List<Integer> indexes = new ArrayList<Integer>();
        final List<TransformedHeader> tmp = getFieldSequence(beanClass, processNested, indexes, transformer, filter);
        Collections.sort(tmp, new Comparator<TransformedHeader>() {
            @Override
            public int compare(final TransformedHeader t1, final TransformedHeader t2) {
                final int i1 = t1.getHeaderIndex();
                final int i2 = t2.getHeaderIndex();
                return (i1 < i2) ? -1 : ((i1 == i2) ? 0 : 1);
            }
        });
        Collections.sort(indexes);
        int col = -1;
        for (final int i : indexes) {
            ++col;
            if (i < 0) {
                continue;
            }
            if (i == col) {
                continue;
            }
            while (i >= tmp.size()) {
                tmp.add(null);
            }
            Collections.swap(tmp, i, col);
        }
        return tmp;
    }
    
    private static List<TransformedHeader> getFieldSequence(final Class beanClass, final boolean processNested, final List<Integer> indexes, final HeaderTransformer transformer, final MethodFilter filter) {
        final List<TransformedHeader> tmp = new ArrayList<TransformedHeader>();
        final Map<AnnotatedElement, List<TransformedHeader>> nestedReplacements = new LinkedHashMap<AnnotatedElement, List<TransformedHeader>>();
        for (final Field field : getAllFields(beanClass).keySet()) {
            processAnnotations(field, processNested, indexes, tmp, nestedReplacements, transformer, filter);
        }
        for (final Method method : getAnnotatedMethods(beanClass, filter)) {
            processAnnotations(method, processNested, indexes, tmp, nestedReplacements, transformer, filter);
        }
        if (!nestedReplacements.isEmpty()) {
            final int size = tmp.size();
            for (int i = size - 1; i >= 0; --i) {
                final TransformedHeader field2 = tmp.get(i);
                final List<TransformedHeader> nestedFields = nestedReplacements.remove(field2.getTarget());
                if (nestedFields != null) {
                    tmp.remove(i);
                    tmp.addAll(i, nestedFields);
                    if (nestedReplacements.isEmpty()) {
                        break;
                    }
                }
            }
        }
        return tmp;
    }
    
    public static Map<Field, PropertyWrapper> getAllFields(final Class<?> beanClass) {
        final Map<String, PropertyWrapper> properties = new LinkedHashMap<String, PropertyWrapper>();
        try {
            for (final PropertyWrapper property : BeanHelper.getPropertyDescriptors(beanClass)) {
                final String name = property.getName();
                if (name != null) {
                    properties.put(name, property);
                }
            }
        }
        catch (Exception ex) {}
        final Set<String> used = new HashSet<String>();
        Class<?> clazz = beanClass;
        final Map<Field, PropertyWrapper> out = new LinkedHashMap<Field, PropertyWrapper>();
        do {
            final Field[] arr$2;
            final Field[] declared = arr$2 = clazz.getDeclaredFields();
            for (final Field field : arr$2) {
                if (!used.contains(field.getName())) {
                    used.add(field.getName());
                    out.put(field, properties.get(field.getName()));
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null && clazz != Object.class);
        return out;
    }
    
    public static List<Method> getAnnotatedMethods(final Class<?> beanClass, final MethodFilter filter) {
        final List<Method> out = new ArrayList<Method>();
        Class clazz = beanClass;
        do {
            final Method[] arr$;
            final Method[] declared = arr$ = clazz.getDeclaredMethods();
            for (final Method method : arr$) {
                final Annotation[] arr$2;
                final Annotation[] annotations = arr$2 = method.getDeclaredAnnotations();
                final int len$2 = arr$2.length;
                int i$2 = 0;
                while (i$2 < len$2) {
                    final Annotation annotation = arr$2[i$2];
                    if (isCustomAnnotation(annotation)) {
                        if (filter.reject(method)) {
                            break;
                        }
                        out.add(method);
                        break;
                    }
                    else {
                        ++i$2;
                    }
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null && clazz != Object.class);
        return out;
    }
    
    public static synchronized <A extends Annotation> A findAnnotation(final AnnotatedElement annotatedElement, final Class<A> annotationType) {
        if (annotatedElement == null || annotationType == null) {
            return null;
        }
        if (annotatedElement.equals(AnnotationHelper.lastProcessedElement) && annotationType == AnnotationHelper.lastProcessedAnnotationType) {
            return (A)AnnotationHelper.lastAnnotationFound;
        }
        AnnotationHelper.lastProcessedElement = annotatedElement;
        AnnotationHelper.lastProcessedAnnotationType = annotationType;
        final Stack<Annotation> path = new Stack<Annotation>();
        final A annotation = findAnnotation(annotatedElement, annotationType, new HashSet<Annotation>(), path);
        if (annotation == null || path.isEmpty()) {
            return (A)(AnnotationHelper.lastAnnotationFound = annotation);
        }
        while (!path.isEmpty()) {
            final Annotation parent = path.pop();
            final Annotation target = path.isEmpty() ? annotation : path.peek();
            for (final Method method : parent.annotationType().getDeclaredMethods()) {
                final Copy copy = method.getAnnotation(Copy.class);
                if (copy != null) {
                    final Class targetClass = copy.to();
                    String targetProperty = copy.property();
                    if (targetProperty.trim().isEmpty()) {
                        targetProperty = method.getName();
                    }
                    final Object existingValue = AnnotationRegistry.getValue(annotatedElement, target, method.getName());
                    Object value;
                    if (existingValue != null) {
                        value = existingValue;
                    }
                    else {
                        value = invoke(parent, method);
                    }
                    final Class sourceValueType = method.getReturnType();
                    final Class<?> targetPropertyType = findAnnotationMethodType(targetClass, targetProperty);
                    if (targetPropertyType != null && targetPropertyType.isArray() && !value.getClass().isArray()) {
                        final Object array = Array.newInstance(sourceValueType, 1);
                        Array.set(array, 0, value);
                        value = array;
                    }
                    if (targetClass == target.annotationType()) {
                        AnnotationRegistry.setValue(annotatedElement, annotation, targetProperty, value);
                    }
                    else {
                        final A ann = findAnnotation(annotatedElement, (Class<A>)targetClass, new HashSet<Annotation>(), new Stack<Annotation>());
                        if (ann == null) {
                            throw new IllegalStateException("Can't process @Copy annotation on '" + method + "'. " + "Annotation '" + targetClass.getName() + "' not used in " + parent.annotationType().getName() + ". Unable to process field " + annotatedElement + "'");
                        }
                        AnnotationRegistry.setValue(annotatedElement, ann, targetProperty, value);
                    }
                }
            }
        }
        return (A)(AnnotationHelper.lastAnnotationFound = annotation);
    }
    
    private static Class<?> findAnnotationMethodType(final Class<? extends Annotation> type, final String methodName) {
        for (final Method method : type.getDeclaredMethods()) {
            if (method.getName().equals(methodName)) {
                return method.getReturnType();
            }
        }
        return null;
    }
    
    private static Object invoke(final Annotation annotation, final Method method) {
        try {
            return method.invoke(annotation, (Object[])null);
        }
        catch (Exception e) {
            throw new IllegalStateException("Can't read value from annotation " + annotation, e);
        }
    }
    
    private static <A> A findAnnotation(final AnnotatedElement annotatedElement, final Class<A> annotationType, final Set<Annotation> visited, final Stack<Annotation> path) {
        final Annotation[] declaredAnnotations = annotatedElement.getDeclaredAnnotations();
        for (int i = 0; i < declaredAnnotations.length; ++i) {
            final Annotation ann = declaredAnnotations[i];
            if (ann.annotationType() == annotationType) {
                return (A)ann;
            }
        }
        for (int i = 0; i < declaredAnnotations.length; ++i) {
            final Annotation ann = declaredAnnotations[i];
            if (isCustomAnnotation(ann) && visited.add(ann)) {
                final A annotation = (A)findAnnotation(ann.annotationType(), (Class<Object>)annotationType, visited, path);
                path.push(ann);
                if (annotation != null) {
                    return annotation;
                }
            }
        }
        return null;
    }
    
    private static boolean isCustomAnnotation(final Annotation annotation) {
        final Class annotationType = annotation.annotationType();
        if (AnnotationHelper.customAnnotationTypes.contains(annotationType)) {
            return true;
        }
        if (AnnotationHelper.javaLangAnnotationTypes.contains(annotationType)) {
            return false;
        }
        if (annotationType.getName().startsWith("java.lang.annotation")) {
            AnnotationHelper.javaLangAnnotationTypes.add(annotationType);
            return false;
        }
        AnnotationHelper.customAnnotationTypes.add(annotationType);
        return true;
    }
    
    public static List<Annotation> findAllAnnotationsInPackage(final AnnotatedElement annotatedElement, final Package aPackage) {
        final ArrayList<Annotation> found = new ArrayList<Annotation>();
        findAllAnnotationsInPackage(annotatedElement, aPackage, found, new HashSet<Annotation>());
        return found;
    }
    
    private static void findAllAnnotationsInPackage(final AnnotatedElement annotatedElement, final Package aPackage, final ArrayList<? super Annotation> found, final Set<Annotation> visited) {
        final Annotation[] declaredAnnotations = annotatedElement.getDeclaredAnnotations();
        for (int i = 0; i < declaredAnnotations.length; ++i) {
            final Annotation ann = declaredAnnotations[i];
            if (aPackage.equals(ann.annotationType().getPackage())) {
                found.add(ann);
            }
            if (isCustomAnnotation(ann) && visited.add(ann)) {
                findAllAnnotationsInPackage(ann.annotationType(), aPackage, found, visited);
            }
        }
    }
    
    public static final Object getDefaultPrimitiveValue(final Class type) {
        if (type == Integer.TYPE) {
            return 0;
        }
        if (type == Double.TYPE) {
            return 0.0;
        }
        if (type == Boolean.TYPE) {
            return Boolean.FALSE;
        }
        if (type == Long.TYPE) {
            return 0L;
        }
        if (type == Float.TYPE) {
            return 0.0f;
        }
        if (type == Byte.TYPE) {
            return 0;
        }
        if (type == Character.TYPE) {
            return '\0';
        }
        if (type == Short.TYPE) {
            return 0;
        }
        return null;
    }
    
    static {
        javaLangAnnotationTypes = new HashSet<Class>();
        customAnnotationTypes = new HashSet<Class>();
    }
}
