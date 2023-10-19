// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONException;
import java.util.Arrays;
import java.util.function.Function;
import java.util.ArrayList;
import java.util.List;
import java.util.Collection;
import com.alibaba.fastjson2.JSONReader;
import java.util.Date;
import com.alibaba.fastjson2.util.TypeUtils;
import com.alibaba.fastjson2.util.Fnv;
import java.util.function.BiConsumer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import java.lang.reflect.Type;

public class FieldReaderList<T, V> extends FieldReaderObject<T>
{
    final long fieldClassHash;
    final long itemClassHash;
    
    public FieldReaderList(final String fieldName, final Type fieldType, final Class fieldClass, final Type itemType, final Class itemClass, final int ordinal, final long features, final String format, final Locale locale, final Object defaultValue, final JSONSchema schema, final Method method, final Field field, final BiConsumer function) {
        super(fieldName, fieldType, fieldClass, ordinal, features, format, locale, defaultValue, schema, method, field, function);
        this.itemType = itemType;
        this.itemClass = itemClass;
        this.itemClassHash = ((this.itemClass == null) ? 0L : Fnv.hashCode64(itemClass.getName()));
        this.fieldClassHash = ((fieldClass == null) ? 0L : Fnv.hashCode64(TypeUtils.getTypeName(fieldClass)));
        if (format != null && itemType == Date.class) {
            this.itemReader = new ObjectReaderImplDate(format, locale);
        }
    }
    
    @Override
    public long getItemClassHash() {
        return this.itemClassHash;
    }
    
    public Collection<V> createList(final JSONReader.Context context) {
        if (this.fieldClass == List.class || this.fieldClass == Collection.class || this.fieldClass == ArrayList.class) {
            return new ArrayList<V>();
        }
        return this.getObjectReader(context).createInstance();
    }
    
    @Override
    public void readFieldValue(final JSONReader jsonReader, final T object) {
        if (jsonReader.jsonb) {
            this.readFieldValueJSONB(jsonReader, object);
            return;
        }
        if (jsonReader.nextIfNull()) {
            this.accept(object, null);
            return;
        }
        final JSONReader.Context context = jsonReader.getContext();
        final ObjectReader objectReader = this.getObjectReader(context);
        Function builder = null;
        if (this.initReader != null) {
            builder = this.initReader.getBuildFunction();
        }
        else if (objectReader instanceof ObjectReaderImplList) {
            builder = objectReader.getBuildFunction();
        }
        final char current = jsonReader.current();
        if (current == '[') {
            final ObjectReader itemObjectReader = this.getItemObjectReader(context);
            Collection list = this.createList(context);
            jsonReader.next();
            int i = 0;
            while (!jsonReader.nextIfArrayEnd()) {
                Label_0203: {
                    Object item;
                    if (jsonReader.isReference()) {
                        final String path = jsonReader.readReference();
                        if (!"..".equals(path)) {
                            this.addResolveTask(jsonReader, (List)list, i, path);
                            break Label_0203;
                        }
                        item = list;
                    }
                    else {
                        item = itemObjectReader.readObject(jsonReader, null, null, 0L);
                    }
                    list.add(item);
                    jsonReader.nextIfComma();
                }
                ++i;
            }
            if (builder != null) {
                list = builder.apply(list);
            }
            this.accept(object, list);
            jsonReader.nextIfComma();
            return;
        }
        if (current == '{' && this.getItemObjectReader(context) instanceof ObjectReaderBean) {
            final Object itemValue = jsonReader.jsonb ? this.itemReader.readJSONBObject(jsonReader, null, null, this.features) : this.itemReader.readObject(jsonReader, null, null, this.features);
            Collection list = objectReader.createInstance(this.features);
            list.add(itemValue);
            if (builder != null) {
                list = builder.apply(list);
            }
            this.accept(object, list);
            jsonReader.nextIfComma();
            return;
        }
        final Object value = jsonReader.jsonb ? objectReader.readJSONBObject(jsonReader, null, null, this.features) : objectReader.readObject(jsonReader, null, null, this.features);
        this.accept(object, value);
    }
    
    @Override
    public Object readFieldValue(final JSONReader jsonReader) {
        if (jsonReader.jsonb) {
            final int entryCnt = jsonReader.startArray();
            final Object[] array = new Object[entryCnt];
            final ObjectReader itemObjectReader = this.getItemObjectReader(jsonReader.getContext());
            for (int i = 0; i < entryCnt; ++i) {
                array[i] = itemObjectReader.readObject(jsonReader, null, null, 0L);
            }
            return Arrays.asList(array);
        }
        if (jsonReader.current() == '[') {
            final JSONReader.Context ctx = jsonReader.getContext();
            final ObjectReader itemObjectReader2 = this.getItemObjectReader(ctx);
            final Collection list = this.createList(ctx);
            jsonReader.next();
            while (!jsonReader.nextIfArrayEnd()) {
                list.add(itemObjectReader2.readObject(jsonReader, null, null, 0L));
                jsonReader.nextIfComma();
            }
            jsonReader.nextIfComma();
            return list;
        }
        if (jsonReader.isString()) {
            final String str = jsonReader.readString();
            if (this.itemType instanceof Class && Number.class.isAssignableFrom((Class<?>)this.itemType)) {
                final Function typeConvert = jsonReader.getContext().getProvider().getTypeConvert(String.class, this.itemType);
                if (typeConvert != null) {
                    final Collection list = this.createList(jsonReader.getContext());
                    if (str.indexOf(44) != -1) {
                        final String[] split;
                        final String[] items = split = str.split(",");
                        for (final String item : split) {
                            final Object converted = typeConvert.apply(item);
                            list.add(converted);
                        }
                    }
                    return list;
                }
            }
        }
        throw new JSONException(jsonReader.info("TODO : " + this.getClass()));
    }
    
    @Override
    public ObjectReader checkObjectAutoType(final JSONReader jsonReader) {
        if (!jsonReader.nextIfMatch((byte)(-110))) {
            return null;
        }
        final long typeHash = jsonReader.readTypeHashCode();
        final long features = jsonReader.features(this.features);
        final JSONReader.Context context = jsonReader.getContext();
        final JSONReader.AutoTypeBeforeHandler autoTypeFilter = context.getContextAutoTypeBeforeHandler();
        if (autoTypeFilter != null) {
            Class<?> filterClass = autoTypeFilter.apply(typeHash, this.fieldClass, features);
            if (filterClass == null) {
                final String typeName = jsonReader.getString();
                filterClass = autoTypeFilter.apply(typeName, this.fieldClass, features);
            }
            if (filterClass != null) {
                return context.getObjectReader(this.fieldClass);
            }
        }
        final boolean isSupportAutoType = jsonReader.isSupportAutoType(features);
        if (!isSupportAutoType) {
            if (jsonReader.isArray() && !jsonReader.isEnabled(JSONReader.Feature.ErrorOnNotSupportAutoType)) {
                return this.getObjectReader(jsonReader);
            }
            throw new JSONException(jsonReader.info("autoType not support input " + jsonReader.getString()));
        }
        else {
            ObjectReader autoTypeObjectReader = jsonReader.getObjectReaderAutoType(typeHash, this.fieldClass, features);
            if (autoTypeObjectReader instanceof ObjectReaderImplList) {
                final ObjectReaderImplList listReader = (ObjectReaderImplList)autoTypeObjectReader;
                autoTypeObjectReader = new ObjectReaderImplList(this.fieldType, this.fieldClass, listReader.instanceType, this.itemType, listReader.builder);
            }
            if (autoTypeObjectReader == null) {
                throw new JSONException(jsonReader.info("auotype not support : " + jsonReader.getString()));
            }
            return autoTypeObjectReader;
        }
    }
}
