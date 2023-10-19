// 
// Decompiled by Procyon v0.5.36
// 

package shadeio.univocity.parsers.annotations.helpers;

import shadeio.univocity.parsers.common.DataProcessingException;
import java.lang.annotation.Annotation;
import shadeio.univocity.parsers.annotations.Parsed;
import java.lang.reflect.Field;
import shadeio.univocity.parsers.annotations.HeaderTransformer;
import shadeio.univocity.parsers.common.beans.PropertyWrapper;
import java.lang.reflect.Method;
import java.lang.reflect.AnnotatedElement;

public class FieldMapping
{
    private final Class parentClass;
    private final AnnotatedElement target;
    private int index;
    private String fieldName;
    private final Class<?> beanClass;
    private final Method readMethod;
    private final Method writeMethod;
    private boolean accessible;
    private final boolean primitive;
    private final Object defaultPrimitiveValue;
    private Boolean applyDefault;
    private Class fieldType;
    private boolean primitiveNumber;
    
    public FieldMapping(final Class<?> beanClass, final AnnotatedElement target, final PropertyWrapper property, final HeaderTransformer transformer, final String[] headers) {
        this.applyDefault = null;
        this.beanClass = beanClass;
        this.target = target;
        if (target instanceof Field) {
            this.readMethod = ((property != null) ? property.getReadMethod() : null);
            this.writeMethod = ((property != null) ? property.getWriteMethod() : null);
        }
        else {
            final Method method = (Method)target;
            this.readMethod = ((method.getReturnType() != Void.class) ? method : null);
            this.writeMethod = ((method.getParameterTypes().length != 0) ? method : null);
        }
        Class typeToSet;
        if (target != null) {
            typeToSet = AnnotationHelper.getType(target);
            this.parentClass = AnnotationHelper.getDeclaringClass(target);
        }
        else if (this.writeMethod != null && this.writeMethod.getParameterTypes().length == 1) {
            typeToSet = this.writeMethod.getParameterTypes()[0];
            this.parentClass = this.writeMethod.getDeclaringClass();
        }
        else {
            typeToSet = Object.class;
            if (this.readMethod != null) {
                this.parentClass = this.readMethod.getDeclaringClass();
            }
            else {
                this.parentClass = beanClass;
            }
        }
        this.primitive = typeToSet.isPrimitive();
        this.defaultPrimitiveValue = AnnotationHelper.getDefaultPrimitiveValue(typeToSet);
        this.primitiveNumber = (this.defaultPrimitiveValue instanceof Number);
        this.determineFieldMapping(transformer, headers);
        this.fieldType = typeToSet;
    }
    
    private void determineFieldMapping(final HeaderTransformer transformer, final String[] headers) {
        final Parsed parsed = AnnotationHelper.findAnnotation(this.target, Parsed.class);
        String name = "";
        if (parsed != null) {
            this.index = AnnotationRegistry.getValue(this.target, parsed, "index", parsed.index());
            if (this.index >= 0) {
                this.fieldName = null;
                if (transformer != null) {
                    this.index = transformer.transformIndex(this.target, this.index);
                }
                return;
            }
            final String[] fields = AnnotationRegistry.getValue(this.target, parsed, "field", parsed.field());
            if (fields.length > 1 && headers != null) {
                for (int i = 0; i < headers.length; ++i) {
                    final String header = headers[i];
                    if (header != null) {
                        for (int j = 0; j < fields.length; ++j) {
                            final String field = fields[j];
                            if (field.equalsIgnoreCase(header)) {
                                name = field;
                                break;
                            }
                        }
                    }
                }
            }
            if (name.isEmpty()) {
                name = ((fields.length == 0) ? "" : fields[0]);
            }
        }
        if (name.isEmpty()) {
            name = AnnotationHelper.getName(this.target);
        }
        this.fieldName = name;
        if (parsed != null && transformer != null) {
            if (this.index >= 0) {
                this.index = transformer.transformIndex(this.target, this.index);
            }
            else if (this.fieldName != null) {
                this.fieldName = transformer.transformName(this.target, this.fieldName);
            }
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FieldMapping that = (FieldMapping)o;
        if (this.index != that.index) {
            return false;
        }
        if (!this.target.equals(that.target)) {
            return false;
        }
        if (this.fieldName != null) {
            if (this.fieldName.equals(that.fieldName)) {
                return this.beanClass.equals(that.beanClass);
            }
        }
        else if (that.fieldName == null) {
            return this.beanClass.equals(that.beanClass);
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = this.target.hashCode();
        result = 31 * result + this.index;
        result = 31 * result + ((this.fieldName != null) ? this.fieldName.hashCode() : 0);
        result = 31 * result + this.beanClass.hashCode();
        return result;
    }
    
    public boolean isMappedToIndex() {
        return this.index >= 0;
    }
    
    public boolean isMappedToField() {
        return this.index < 0;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setFieldName(final String fieldName) {
        this.fieldName = fieldName;
    }
    
    public String getFieldName() {
        return this.fieldName;
    }
    
    public AnnotatedElement getTarget() {
        return this.target;
    }
    
    private void setAccessible() {
        if (!this.accessible) {
            if (this.target instanceof Field) {
                ((Field)this.target).setAccessible(true);
            }
            else {
                ((Method)this.target).setAccessible(true);
            }
            this.accessible = true;
        }
    }
    
    public Class<?> getFieldParent() {
        return (Class<?>)this.parentClass;
    }
    
    public Class<?> getFieldType() {
        return (Class<?>)this.fieldType;
    }
    
    public boolean canWrite(final Object instance) {
        if (!this.primitive) {
            return instance == null || this.fieldType.isAssignableFrom(instance.getClass());
        }
        if (instance instanceof Number) {
            return this.primitiveNumber;
        }
        if (instance instanceof Boolean) {
            return this.fieldType == Boolean.TYPE;
        }
        return instance instanceof Character && this.fieldType == Character.TYPE;
    }
    
    public Object read(final Object instance) {
        return this.read(instance, false);
    }
    
    private Object read(final Object instance, final boolean ignoreErrors) {
        this.setAccessible();
        try {
            if (this.readMethod != null) {
                return this.readMethod.invoke(instance, new Object[0]);
            }
            return ((Field)this.target).get(instance);
        }
        catch (Throwable e) {
            if (!ignoreErrors) {
                throw new DataProcessingException("Unable to get value from field " + this.toString(), e);
            }
            return null;
        }
    }
    
    public void write(final Object instance, Object value) {
        this.setAccessible();
        try {
            if (this.primitive) {
                if (value == null) {
                    if (this.applyDefault == null) {
                        final Object currentValue = this.read(instance, true);
                        this.applyDefault = this.defaultPrimitiveValue.equals(currentValue);
                    }
                    if (this.applyDefault != Boolean.TRUE) {
                        return;
                    }
                    value = this.defaultPrimitiveValue;
                }
                else if (this.defaultPrimitiveValue.getClass() != value.getClass() && value instanceof Number) {
                    final Number number = (Number)value;
                    if (this.fieldType == Integer.TYPE) {
                        value = number.intValue();
                    }
                    else if (this.fieldType == Long.TYPE) {
                        value = number.longValue();
                    }
                    else if (this.fieldType == Double.TYPE) {
                        value = number.doubleValue();
                    }
                    else if (this.fieldType == Float.TYPE) {
                        value = number.floatValue();
                    }
                    else if (this.fieldType == Byte.TYPE) {
                        value = number.byteValue();
                    }
                    else if (this.fieldType == Short.TYPE) {
                        value = number.shortValue();
                    }
                }
            }
            if (this.writeMethod != null) {
                this.writeMethod.invoke(instance, value);
            }
            else {
                ((Field)this.target).set(instance, value);
            }
        }
        catch (Throwable e) {
            if (e instanceof DataProcessingException) {
                throw (DataProcessingException)e;
            }
            final String valueTypeName = (value == null) ? null : value.getClass().getName();
            String msg;
            if (valueTypeName != null) {
                msg = "Unable to set value '{value}' of type '" + valueTypeName + "' to field " + this.toString();
            }
            else {
                msg = "Unable to set value 'null' to field " + this.toString();
            }
            final DataProcessingException ex = new DataProcessingException(msg, e);
            ex.markAsNonFatal();
            ex.setValue(value);
            throw ex;
        }
    }
    
    @Override
    public String toString() {
        return AnnotationHelper.describeElement(this.target);
    }
}
