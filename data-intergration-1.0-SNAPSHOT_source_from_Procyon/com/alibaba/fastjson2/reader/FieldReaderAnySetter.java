// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONReader;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.util.Locale;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.lang.reflect.Type;

class FieldReaderAnySetter<T> extends FieldReaderObject<T>
{
    FieldReaderAnySetter(final Type fieldType, final Class fieldClass, final int ordinal, final long features, final String format, final JSONSchema schema, final Method method) {
        super("$$any$$", fieldType, fieldClass, ordinal, features, format, null, null, schema, method, null, null);
    }
    
    @Override
    public ObjectReader getItemObjectReader(final JSONReader jsonReader) {
        if (this.itemReader != null) {
            return this.itemReader;
        }
        return this.itemReader = jsonReader.getObjectReader(this.fieldType);
    }
    
    @Override
    public void accept(final T object, final Object value) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public void processExtra(final JSONReader jsonReader, final Object object) {
        final String name = jsonReader.getFieldName();
        final ObjectReader itemObjectReader = this.getItemObjectReader(jsonReader);
        final Object value = itemObjectReader.readObject(jsonReader, this.fieldType, this.fieldName, 0L);
        try {
            this.method.invoke(object, name, value);
        }
        catch (Exception e) {
            throw new JSONException(jsonReader.info("any set error"), e);
        }
    }
    
    @Override
    public void acceptExtra(final Object object, final String name, final Object value) {
        try {
            this.method.invoke(object, name, value);
        }
        catch (Exception e) {
            throw new JSONException("any set error");
        }
    }
    
    @Override
    public boolean isReadOnly() {
        return true;
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        throw new UnsupportedOperationException();
    }
}
