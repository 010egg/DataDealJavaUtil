// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.common.processor.core;

import java.util.Collection;
import java.util.Arrays;
import java.util.TreeSet;
import shadeio.univocity.parsers.common.ArgumentUtils;
import shadeio.univocity.parsers.common.Context;
import shadeio.univocity.parsers.conversions.Conversion;
import java.util.List;
import shadeio.univocity.parsers.common.DataProcessingException;
import java.util.HashSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.lang.annotation.Annotation;
import shadeio.univocity.parsers.annotations.helpers.AnnotationRegistry;
import shadeio.univocity.parsers.annotations.Nested;
import shadeio.univocity.parsers.annotations.Parsed;
import java.util.Iterator;
import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;
import shadeio.univocity.parsers.common.beans.PropertyWrapper;
import java.lang.reflect.Field;
import shadeio.univocity.parsers.annotations.helpers.AnnotationHelper;
import java.util.LinkedHashSet;
import shadeio.univocity.parsers.annotations.helpers.MethodFilter;
import shadeio.univocity.parsers.annotations.HeaderTransformer;
import java.util.Map;
import shadeio.univocity.parsers.annotations.helpers.FieldMapping;
import java.util.Set;
import shadeio.univocity.parsers.common.DefaultConversionProcessor;

public class BeanConversionProcessor<T> extends DefaultConversionProcessor
{
    final Class<T> beanClass;
    protected final Set<FieldMapping> parsedFields;
    private int lastFieldIndexMapped;
    private FieldMapping[] readOrder;
    private FieldMapping[] missing;
    private Object[] valuesForMissing;
    protected boolean initialized;
    boolean strictHeaderValidationEnabled;
    private String[] syntheticHeaders;
    private Object[] row;
    private Map<FieldMapping, BeanConversionProcessor<?>> nestedAttributes;
    protected final HeaderTransformer transformer;
    protected final MethodFilter methodFilter;
    
    @Deprecated
    public BeanConversionProcessor(final Class<T> beanType) {
        this(beanType, null, MethodFilter.ONLY_SETTERS);
    }
    
    public BeanConversionProcessor(final Class<T> beanType, final MethodFilter methodFilter) {
        this(beanType, null, methodFilter);
    }
    
    BeanConversionProcessor(final Class<T> beanType, final HeaderTransformer transformer, final MethodFilter methodFilter) {
        this.parsedFields = new LinkedHashSet<FieldMapping>();
        this.lastFieldIndexMapped = -1;
        this.initialized = false;
        this.strictHeaderValidationEnabled = false;
        this.syntheticHeaders = null;
        this.nestedAttributes = null;
        this.beanClass = beanType;
        this.transformer = transformer;
        this.methodFilter = methodFilter;
    }
    
    public boolean isStrictHeaderValidationEnabled() {
        return this.strictHeaderValidationEnabled;
    }
    
    public final void initialize() {
        this.initialize(null);
    }
    
    protected final void initialize(final String[] headers) {
        if (!this.initialized) {
            this.initialized = true;
            final Map<Field, PropertyWrapper> allFields = AnnotationHelper.getAllFields(this.beanClass);
            for (final Map.Entry<Field, PropertyWrapper> e : allFields.entrySet()) {
                final Field field = e.getKey();
                final PropertyWrapper property = e.getValue();
                this.processField(field, property, headers);
            }
            for (final Method method : AnnotationHelper.getAnnotatedMethods(this.beanClass, this.methodFilter)) {
                this.processField(method, null, headers);
            }
            this.readOrder = null;
            this.lastFieldIndexMapped = -1;
            this.validateMappings();
        }
    }
    
    public void setStrictHeaderValidationEnabled(final boolean strictHeaderValidationEnabled) {
        this.strictHeaderValidationEnabled = strictHeaderValidationEnabled;
    }
    
    void processField(final AnnotatedElement element, final PropertyWrapper propertyDescriptor, final String[] headers) {
        final Parsed annotation = AnnotationHelper.findAnnotation(element, Parsed.class);
        if (annotation != null) {
            final FieldMapping mapping = new FieldMapping(this.beanClass, element, propertyDescriptor, this.transformer, headers);
            if (this.processField(mapping)) {
                this.parsedFields.add(mapping);
                this.setupConversions(element, mapping);
            }
        }
        final Nested nested = AnnotationHelper.findAnnotation(element, Nested.class);
        if (nested != null) {
            Class nestedType = AnnotationRegistry.getValue(element, nested, "type", nested.type());
            if (nestedType == Object.class) {
                nestedType = AnnotationHelper.getType(element);
            }
            final Class<? extends HeaderTransformer> transformerType = AnnotationRegistry.getValue(element, nested, "headerTransformer", nested.headerTransformer());
            HeaderTransformer transformer;
            if (transformerType != HeaderTransformer.class) {
                final String[] args = AnnotationRegistry.getValue(element, nested, "args", nested.args());
                transformer = AnnotationHelper.newInstance(HeaderTransformer.class, transformerType, args);
            }
            else {
                transformer = null;
            }
            final FieldMapping mapping2 = new FieldMapping(nestedType, element, propertyDescriptor, null, headers);
            final BeanConversionProcessor<?> processor = this.createNestedProcessor(nested, nestedType, mapping2, transformer);
            processor.initialize(headers);
            this.getNestedAttributes().put(mapping2, processor);
        }
    }
    
    Map<FieldMapping, BeanConversionProcessor<?>> getNestedAttributes() {
        if (this.nestedAttributes == null) {
            this.nestedAttributes = new LinkedHashMap<FieldMapping, BeanConversionProcessor<?>>();
        }
        return this.nestedAttributes;
    }
    
    BeanConversionProcessor<?> createNestedProcessor(final Annotation annotation, final Class nestedType, final FieldMapping fieldMapping, final HeaderTransformer transformer) {
        return new BeanConversionProcessor<Object>(nestedType, transformer, this.methodFilter);
    }
    
    protected boolean processField(final FieldMapping field) {
        return true;
    }
    
    void validateMappings() {
        final Map<String, FieldMapping> mappedNames = new HashMap<String, FieldMapping>();
        final Map<Integer, FieldMapping> mappedIndexes = new HashMap<Integer, FieldMapping>();
        final Set<FieldMapping> duplicateNames = new HashSet<FieldMapping>();
        final Set<FieldMapping> duplicateIndexes = new HashSet<FieldMapping>();
        for (final FieldMapping mapping : this.parsedFields) {
            final String name = mapping.getFieldName();
            final int index = mapping.getIndex();
            if (index != -1) {
                if (mappedIndexes.containsKey(index)) {
                    duplicateIndexes.add(mapping);
                    duplicateIndexes.add(mappedIndexes.get(index));
                }
                else {
                    mappedIndexes.put(index, mapping);
                }
            }
            else if (mappedNames.containsKey(name)) {
                duplicateNames.add(mapping);
                duplicateNames.add(mappedNames.get(name));
            }
            else {
                mappedNames.put(name, mapping);
            }
        }
        if (duplicateIndexes.size() > 0 || duplicateNames.size() > 0) {
            final StringBuilder msg = new StringBuilder("Conflicting field mappings defined in annotated class: " + this.getBeanClass().getName());
            for (final FieldMapping mapping2 : duplicateIndexes) {
                msg.append("\n\tIndex: '").append(mapping2.getIndex()).append("' of  ").append(describeField(mapping2.getTarget()));
            }
            for (final FieldMapping mapping2 : duplicateNames) {
                msg.append("\n\tName: '").append(mapping2.getFieldName()).append("' of ").append(describeField(mapping2.getTarget()));
            }
            throw new DataProcessingException(msg.toString());
        }
    }
    
    static String describeField(final AnnotatedElement target) {
        if (target instanceof Method) {
            return "method: " + target;
        }
        return "field '" + AnnotationHelper.getName(target) + "' (" + AnnotationHelper.getType(target).getName() + ')';
    }
    
    private void setupConversions(final AnnotatedElement target, final FieldMapping mapping) {
        final List<Annotation> annotations = AnnotationHelper.findAllAnnotationsInPackage(target, Parsed.class.getPackage());
        Conversion lastConversion = null;
        for (final Annotation annotation : annotations) {
            try {
                final Conversion conversion = AnnotationHelper.getConversion(target, annotation);
                if (conversion == null) {
                    continue;
                }
                this.addConversion(conversion, mapping);
                lastConversion = conversion;
            }
            catch (Throwable ex) {
                final String path = annotation.annotationType().getSimpleName() + "' of field " + mapping;
                throw new DataProcessingException("Error processing annotation '" + path + ". " + ex.getMessage(), ex);
            }
        }
        final Parsed parsed = AnnotationHelper.findAnnotation(target, Parsed.class);
        final boolean applyDefaultConversion = AnnotationRegistry.getValue(target, parsed, "applyDefaultConversion", parsed.applyDefaultConversion());
        if (applyDefaultConversion) {
            final Conversion defaultConversion = AnnotationHelper.getDefaultConversion(target);
            if (this.applyDefaultConversion(lastConversion, defaultConversion)) {
                this.addConversion(defaultConversion, mapping);
            }
        }
    }
    
    private boolean applyDefaultConversion(final Conversion lastConversionApplied, final Conversion defaultConversion) {
        if (defaultConversion == null) {
            return false;
        }
        if (lastConversionApplied == null) {
            return true;
        }
        if (lastConversionApplied.getClass() == defaultConversion.getClass()) {
            return false;
        }
        final Method execute = this.getConversionMethod(lastConversionApplied, "execute");
        final Method revert = this.getConversionMethod(lastConversionApplied, "revert");
        final Method defaultExecute = this.getConversionMethod(defaultConversion, "execute");
        final Method defaultRevert = this.getConversionMethod(defaultConversion, "revert");
        return execute.getReturnType() != defaultExecute.getReturnType() || revert.getReturnType() != defaultRevert.getReturnType();
    }
    
    private Method getConversionMethod(final Conversion conversion, final String methodName) {
        Method targetMethod = null;
        for (final Method method : conversion.getClass().getMethods()) {
            if (method.getName().equals(methodName) && !method.isSynthetic() && !method.isBridge() && (method.getModifiers() & 0x1) == 0x1 && method.getParameterTypes().length == 1 && method.getReturnType() != Void.TYPE) {
                if (targetMethod != null) {
                    throw new DataProcessingException("Unable to convert values for class '" + this.beanClass + "'. Multiple '" + methodName + "' methods defined in conversion " + conversion.getClass() + '.');
                }
                targetMethod = method;
            }
        }
        if (targetMethod != null) {
            return targetMethod;
        }
        throw new DataProcessingException("Unable to convert values for class '" + this.beanClass + "'. Cannot find method '" + methodName + "' in conversion " + conversion.getClass() + '.');
    }
    
    protected void addConversion(final Conversion conversion, final FieldMapping mapping) {
        if (conversion == null) {
            return;
        }
        if (mapping.isMappedToIndex()) {
            this.convertIndexes(conversion).add(mapping.getIndex());
        }
        else {
            this.convertFields(conversion).add(mapping.getFieldName());
        }
    }
    
    void mapValuesToFields(final T instance, final Object[] row, final Context context) {
        if (row.length > this.lastFieldIndexMapped) {
            this.lastFieldIndexMapped = row.length;
            this.mapFieldIndexes(context, row, context.headers(), context.extractedFieldIndexes(), context.columnsReordered());
        }
        final int last = (row.length < this.readOrder.length) ? row.length : this.readOrder.length;
        for (int i = 0; i < last; ++i) {
            final FieldMapping field = this.readOrder[i];
            if (field != null) {
                final Object value = row[i];
                field.write(instance, value);
            }
        }
        if (row.length < this.readOrder.length) {
            for (int i = last; i < this.readOrder.length; ++i) {
                final FieldMapping field = this.readOrder[i];
                if (field != null) {
                    final Object value = this.conversions.applyConversions(i, null, null);
                    field.write(instance, value);
                }
            }
        }
        if (this.missing != null) {
            for (int i = 0; i < this.missing.length; ++i) {
                final Object value2 = this.valuesForMissing[i];
                if (value2 != null) {
                    final FieldMapping field2 = this.missing[i];
                    field2.write(instance, value2);
                }
            }
        }
    }
    
    private void mapFieldIndexes(final Context context, final Object[] row, String[] headers, final int[] indexes, final boolean columnsReordered) {
        if (headers == null) {
            headers = ArgumentUtils.EMPTY_STRING_ARRAY;
        }
        boolean boundToIndex = false;
        int last = (headers.length > row.length) ? headers.length : row.length;
        for (final FieldMapping mapping : this.parsedFields) {
            final int index = mapping.getIndex();
            if (last <= index) {
                last = index;
                boundToIndex = true;
            }
        }
        if (boundToIndex) {
            ++last;
        }
        FieldMapping[] fieldOrder = new FieldMapping[last];
        final TreeSet<String> fieldsNotFound = new TreeSet<String>();
        for (final FieldMapping mapping2 : this.parsedFields) {
            if (mapping2.isMappedToField()) {
                final int[] positions = ArgumentUtils.indexesOf(headers, mapping2.getFieldName());
                if (positions.length == 0) {
                    fieldsNotFound.add(mapping2.getFieldName());
                }
                else {
                    for (int i = 0; i < positions.length; ++i) {
                        fieldOrder[positions[i]] = mapping2;
                    }
                }
            }
            else {
                if (mapping2.getIndex() >= fieldOrder.length) {
                    continue;
                }
                fieldOrder[mapping2.getIndex()] = mapping2;
            }
        }
        if (context != null && !fieldsNotFound.isEmpty()) {
            if (headers.length == 0) {
                throw new DataProcessingException("Could not find fields " + fieldsNotFound.toString() + " in input. Please enable header extraction in the parser settings in order to match field names.");
            }
            if (this.strictHeaderValidationEnabled) {
                final DataProcessingException exception = new DataProcessingException("Could not find fields " + fieldsNotFound.toString() + "' in input. Names found: {headers}");
                exception.setValue("headers", Arrays.toString(headers));
                throw exception;
            }
        }
        if (indexes != null) {
            for (int j = 0; j < fieldOrder.length; ++j) {
                boolean isIndexUsed = false;
                for (int k = 0; k < indexes.length; ++k) {
                    if (indexes[k] == j) {
                        isIndexUsed = true;
                        break;
                    }
                }
                if (!isIndexUsed) {
                    fieldOrder[j] = null;
                }
            }
            if (columnsReordered) {
                final FieldMapping[] newFieldOrder = new FieldMapping[indexes.length];
                for (int l = 0; l < indexes.length; ++l) {
                    for (int k = 0; k < fieldOrder.length; ++k) {
                        final int index2 = indexes[l];
                        if (index2 != -1) {
                            final FieldMapping field = fieldOrder[index2];
                            newFieldOrder[l] = field;
                        }
                    }
                }
                fieldOrder = newFieldOrder;
            }
        }
        this.readOrder = fieldOrder;
        this.initializeValuesForMissing();
    }
    
    private void initializeValuesForMissing() {
        if (this.readOrder.length < this.parsedFields.size()) {
            final Set<FieldMapping> unmapped = new LinkedHashSet<FieldMapping>(this.parsedFields);
            unmapped.removeAll(Arrays.asList(this.readOrder));
            this.missing = unmapped.toArray(new FieldMapping[0]);
            final String[] headers = new String[this.missing.length];
            final BeanConversionProcessor tmp = new BeanConversionProcessor(this.getBeanClass(), this.methodFilter) {
                @Override
                protected void addConversion(final Conversion conversion, final FieldMapping mapping) {
                    if (conversion == null) {
                        return;
                    }
                    this.convertFields(conversion).add(mapping.getFieldName());
                }
            };
            for (int i = 0; i < this.missing.length; ++i) {
                final FieldMapping mapping = this.missing[i];
                if (this.processField(mapping)) {
                    tmp.setupConversions(mapping.getTarget(), mapping);
                }
                headers[i] = mapping.getFieldName();
            }
            tmp.initializeConversions(headers, null);
            this.valuesForMissing = tmp.applyConversions(new String[this.missing.length], null);
        }
        else {
            this.missing = null;
            this.valuesForMissing = null;
        }
    }
    
    public T createBean(final String[] row, final Context context) {
        final Object[] convertedRow = super.applyConversions(row, context);
        if (convertedRow == null) {
            return null;
        }
        T instance;
        try {
            instance = this.beanClass.newInstance();
        }
        catch (Throwable e) {
            throw new DataProcessingException("Unable to instantiate class '" + this.beanClass.getName() + '\'', row, e);
        }
        this.mapValuesToFields(instance, convertedRow, context);
        if (this.nestedAttributes != null) {
            this.processNestedAttributes(row, instance, context);
        }
        return instance;
    }
    
    void processNestedAttributes(final String[] row, final Object instance, final Context context) {
        for (final Map.Entry<FieldMapping, BeanConversionProcessor<?>> e : this.nestedAttributes.entrySet()) {
            final Object nested = e.getValue().createBean(row, context);
            if (nested != null) {
                e.getKey().write(instance, nested);
            }
        }
    }
    
    private void mapFieldsToValues(final T instance, final Object[] row, final String[] headers, final int[] indexes, final boolean columnsReordered) {
        if (row.length > this.lastFieldIndexMapped) {
            this.mapFieldIndexes(null, row, headers, indexes, columnsReordered);
        }
        for (int last = (row.length < this.readOrder.length) ? row.length : this.readOrder.length, i = 0; i < last; ++i) {
            final FieldMapping field = this.readOrder[i];
            if (field != null) {
                try {
                    row[i] = field.read(instance);
                }
                catch (Throwable e) {
                    if (!this.beanClass.isAssignableFrom(instance.getClass())) {
                        this.handleConversionError(e, new Object[] { instance }, -1);
                        throw this.toDataProcessingException(e, row, i);
                    }
                    if (!this.handleConversionError(e, row, i)) {
                        throw this.toDataProcessingException(e, row, i);
                    }
                }
            }
        }
    }
    
    public final Object[] reverseConversions(final T bean, String[] headers, final int[] indexesToWrite) {
        if (bean == null) {
            return null;
        }
        if (this.row == null) {
            if (headers != null) {
                this.row = new Object[headers.length];
            }
            else if (indexesToWrite != null) {
                int minimumRowLength = 0;
                for (final int index : indexesToWrite) {
                    if (index + 1 > minimumRowLength) {
                        minimumRowLength = index + 1;
                    }
                }
                if (minimumRowLength < indexesToWrite.length) {
                    minimumRowLength = indexesToWrite.length;
                }
                this.row = new Object[minimumRowLength];
            }
            else {
                final Set<Integer> assignedIndexes = new HashSet<Integer>();
                int lastIndex = -1;
                for (final FieldMapping f : this.parsedFields) {
                    if (lastIndex < f.getIndex() + 1) {
                        lastIndex = f.getIndex() + 1;
                    }
                    assignedIndexes.add(f.getIndex());
                }
                if (lastIndex < this.parsedFields.size()) {
                    lastIndex = this.parsedFields.size();
                }
                this.row = new Object[lastIndex];
                if (this.syntheticHeaders == null) {
                    this.syntheticHeaders = new String[lastIndex];
                    final Iterator<FieldMapping> it = this.parsedFields.iterator();
                    for (int i = 0; i < lastIndex; ++i) {
                        if (!assignedIndexes.contains(i)) {
                            String fieldName = null;
                            while (it.hasNext() && (fieldName = it.next().getFieldName()) == null) {}
                            this.syntheticHeaders[i] = fieldName;
                        }
                    }
                }
            }
        }
        if (this.nestedAttributes != null) {
            for (final Map.Entry<FieldMapping, BeanConversionProcessor<?>> e : this.nestedAttributes.entrySet()) {
                final Object nested = e.getKey().read(bean);
                if (nested != null) {
                    final BeanConversionProcessor<Object> nestedProcessor = e.getValue();
                    nestedProcessor.row = this.row;
                    nestedProcessor.reverseConversions(nested, headers, indexesToWrite);
                }
            }
        }
        if (this.syntheticHeaders != null) {
            headers = this.syntheticHeaders;
        }
        try {
            this.mapFieldsToValues(bean, this.row, headers, indexesToWrite, false);
        }
        catch (Throwable ex) {
            if (ex instanceof DataProcessingException) {
                final DataProcessingException error = (DataProcessingException)ex;
                if (error.isHandled()) {
                    return null;
                }
                throw error;
            }
            else {
                if (!this.handleConversionError(ex, this.row, -1)) {
                    throw this.toDataProcessingException(ex, this.row, -1);
                }
                return null;
            }
        }
        if (super.reverseConversions(true, this.row, headers, indexesToWrite)) {
            return this.row;
        }
        return null;
    }
    
    public Class<T> getBeanClass() {
        return this.beanClass;
    }
}
