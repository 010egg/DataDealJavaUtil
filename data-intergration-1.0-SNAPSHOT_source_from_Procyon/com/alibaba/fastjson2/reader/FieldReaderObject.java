// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.function.Function;
import com.alibaba.fastjson2.util.JDKUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Member;
import com.alibaba.fastjson2.util.BeanUtils;
import com.alibaba.fastjson2.JSONSchemaValidException;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.OptionalInt;
import com.alibaba.fastjson2.JSONException;
import java.util.Collection;
import java.util.Map;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;

public class FieldReaderObject<T> extends FieldReader<T>
{
    protected ObjectReader initReader;
    protected final BiConsumer function;
    
    public FieldReaderObject(final String fieldName, final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final Field field, final BiConsumer function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field);
        this.function = function;
    }
    
    @Override
    public ObjectReader getInitReader() {
        return this.initReader;
    }
    
    @Override
    public ObjectReader getObjectReader(final JSONReader jsonReader) {
        if (this.initReader != null) {
            return this.initReader;
        }
        if (this.reader != null) {
            return this.reader;
        }
        final ObjectReader formattedObjectReader = FieldReader.createFormattedObjectReader(this.fieldType, this.fieldClass, this.format, this.locale);
        if (formattedObjectReader != null) {
            return this.reader = formattedObjectReader;
        }
        if (this.fieldClass != null && Map.class.isAssignableFrom(this.fieldClass)) {
            return this.reader = ObjectReaderImplMap.of(this.fieldType, this.fieldClass, this.features);
        }
        if (this.fieldClass != null && Collection.class.isAssignableFrom(this.fieldClass)) {
            return this.reader = ObjectReaderImplList.of(this.fieldType, this.fieldClass, this.features);
        }
        return this.reader = jsonReader.getObjectReader(this.fieldType);
    }
    
    @Override
    public ObjectReader getObjectReader(final JSONReader.Context context) {
        if (this.reader != null) {
            return this.reader;
        }
        final ObjectReader formattedObjectReader = FieldReader.createFormattedObjectReader(this.fieldType, this.fieldClass, this.format, this.locale);
        if (formattedObjectReader != null) {
            return this.reader = formattedObjectReader;
        }
        if (Map.class.isAssignableFrom(this.fieldClass)) {
            return this.reader = ObjectReaderImplMap.of(this.fieldType, this.fieldClass, this.features);
        }
        if (Collection.class.isAssignableFrom(this.fieldClass)) {
            return this.reader = ObjectReaderImplList.of(this.fieldType, this.fieldClass, this.features);
        }
        return this.reader = context.getObjectReader(this.fieldType);
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        if (!this.fieldClassSerializable) {
            final long contextFeatures = jsonReader.getContext().getFeatures();
            if ((contextFeatures & JSONReader.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
                jsonReader.skipValue();
                return;
            }
            if ((contextFeatures & JSONReader.Feature.ErrorOnNoneSerializable.mask) != 0x0L) {
                throw new JSONException("not support none-Serializable");
            }
        }
        ObjectReader objectReader;
        if (this.initReader != null) {
            objectReader = this.initReader;
        }
        else {
            final ObjectReader formattedObjectReader = FieldReader.createFormattedObjectReader(this.fieldType, this.fieldClass, this.format, this.locale);
            if (formattedObjectReader != null) {
                final ObjectReader initReader = formattedObjectReader;
                this.initReader = initReader;
                objectReader = initReader;
            }
            else {
                final ObjectReader objectReader2 = jsonReader.getContext().getObjectReader(this.fieldType);
                this.initReader = objectReader2;
                objectReader = objectReader2;
            }
        }
        if (jsonReader.isReference()) {
            final String reference = jsonReader.readReference();
            if ("..".equals(reference)) {
                this.accept(object, object);
            }
            else {
                this.addResolveTask(jsonReader, object, reference);
            }
            return;
        }
        Object value;
        try {
            if (jsonReader.nextIfNull()) {
                if (this.fieldClass == OptionalInt.class) {
                    value = OptionalInt.empty();
                }
                else if (this.fieldClass == OptionalLong.class) {
                    value = OptionalLong.empty();
                }
                else if (this.fieldClass == OptionalDouble.class) {
                    value = OptionalDouble.empty();
                }
                else if (this.fieldClass == Optional.class) {
                    value = Optional.empty();
                }
                else {
                    value = null;
                }
            }
            else if (jsonReader.jsonb) {
                if (this.fieldClass == Object.class) {
                    final ObjectReader autoTypeObjectReader = jsonReader.checkAutoType(Object.class, 0L, this.features);
                    if (autoTypeObjectReader != null) {
                        value = autoTypeObjectReader.readJSONBObject(jsonReader, this.fieldType, this.fieldName, this.features);
                    }
                    else {
                        value = jsonReader.readAny();
                    }
                }
                else {
                    value = objectReader.readJSONBObject(jsonReader, this.fieldType, this.fieldName, this.features);
                }
            }
            else {
                value = objectReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
            }
        }
        catch (JSONSchemaValidException ex) {
            throw ex;
        }
        catch (Exception | IllegalAccessError ex3) {
            final Throwable t;
            final Throwable ex2 = t;
            if ((this.features & JSONReader.Feature.NullOnError.mask) == 0x0L) {
                final Member member = (this.field != null) ? this.field : this.method;
                String message;
                if (member != null) {
                    message = "read field '" + member.getDeclaringClass().getName() + "." + member.getName();
                }
                else {
                    message = "read field " + this.fieldName + " error";
                }
                throw new JSONException(jsonReader.info(message), ex2);
            }
            value = null;
        }
        this.accept(object, value);
        if (this.noneStaticMemberClass) {
            BeanUtils.setNoneStaticMemberClassParent(value, object);
        }
    }
    
    @Override
    public void readFieldValueJSONB(final JSONReader jsonReader, final T object) {
        if (!this.fieldClassSerializable && jsonReader.getType() != -110) {
            final long contextFeatures = jsonReader.getContext().getFeatures();
            if ((contextFeatures & JSONReader.Feature.IgnoreNoneSerializable.mask) != 0x0L) {
                jsonReader.skipValue();
                return;
            }
            if ((contextFeatures & JSONReader.Feature.ErrorOnNoneSerializable.mask) != 0x0L) {
                throw new JSONException("not support none-Serializable");
            }
        }
        if (this.initReader == null) {
            this.initReader = jsonReader.getContext().getObjectReader(this.fieldType);
        }
        if (jsonReader.isReference()) {
            final String reference = jsonReader.readReference();
            if ("..".equals(reference)) {
                this.accept(object, object);
            }
            else {
                this.addResolveTask(jsonReader, object, reference);
            }
            return;
        }
        final Object value = this.initReader.readJSONBObject(jsonReader, this.fieldType, this.fieldName, this.features);
        this.accept(object, value);
    }
    
    @Override
    public void accept(final T object, final boolean value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final byte value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final short value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final int value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final long value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final float value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final double value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, final char value) {
        this.accept(object, (Object)value);
    }
    
    @Override
    public void accept(final T object, Object value) {
        if (this.schema != null) {
            this.schema.assertValidate(value);
        }
        if (value == null && (this.features & JSONReader.Feature.IgnoreSetNullValue.mask) != 0x0L) {
            return;
        }
        if (this.fieldClass == Character.TYPE && value instanceof String) {
            final String str = (String)value;
            if (str.length() > 0) {
                value = str.charAt(0);
            }
            else {
                value = '\0';
            }
        }
        if (value != null && !this.fieldClass.isInstance(value)) {
            value = TypeUtils.cast(value, this.fieldType);
        }
        try {
            if (this.function != null) {
                this.function.accept(object, value);
            }
            else if (this.method != null) {
                this.method.invoke(object, value);
            }
            else {
                JDKUtils.UNSAFE.putObject(object, this.fieldOffset, value);
            }
        }
        catch (Exception e) {
            throw new JSONException("set " + ((this.function != null) ? super.toString() : this.fieldName) + " error", e);
        }
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        if (this.initReader == null) {
            this.initReader = this.getObjectReader(jsonReader);
        }
        Object object = jsonReader.jsonb ? this.initReader.readJSONBObject(jsonReader, this.fieldType, this.fieldName, this.features) : this.initReader.readObject(jsonReader, this.fieldType, this.fieldName, this.features);
        final Function builder = this.initReader.getBuildFunction();
        if (builder != null) {
            object = builder.apply(object);
        }
        return object;
    }
    
    @Override
    public void processExtra(final JSONReader jsonReader, final Object object) {
        if (this.initReader == null) {
            this.initReader = this.getObjectReader(jsonReader);
        }
        if (this.initReader instanceof ObjectReaderBean && this.field != null) {
            final String name = jsonReader.getFieldName();
            final FieldReader extraField = this.initReader.getFieldReader(name);
            if (extraField != null) {
                try {
                    Object unwrappedFieldValue = this.field.get(object);
                    if (unwrappedFieldValue == null) {
                        unwrappedFieldValue = this.initReader.createInstance(this.features);
                        this.accept(object, unwrappedFieldValue);
                    }
                    extraField.readFieldValue(jsonReader, unwrappedFieldValue);
                    return;
                }
                catch (Exception e) {
                    throw new JSONException("read unwrapped field error", e);
                }
            }
        }
        jsonReader.skipValue();
    }
    
    @Override
    public BiConsumer getFunction() {
        return this.function;
    }
}
