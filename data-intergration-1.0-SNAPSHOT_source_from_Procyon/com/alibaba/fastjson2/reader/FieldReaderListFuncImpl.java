// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Collection;
import com.alibaba.fastjson2.JSONReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import com.alibaba.fastjson2.schema.JSONSchema;
import java.util.Locale;
import com.alibaba.fastjson2.util.TypeUtils;
import java.lang.reflect.Type;
import java.util.function.BiConsumer;
import java.util.List;
import java.util.function.Supplier;

class FieldReaderListFuncImpl<T, V> extends FieldReaderList<T, V>
{
    final Supplier<List<V>> listCreator;
    final ObjectReader<V> itemObjectReader;
    
    public FieldReaderListFuncImpl(final Supplier<List<V>> listCreator, final ObjectReader<V> itemObjectReader, final BiConsumer<T, List<V>> function, final Type itemType, final String fieldName) {
        super(fieldName, List.class, List.class, itemType, TypeUtils.getClass(itemType), 0, 0L, null, null, null, null, null, null, function);
        this.listCreator = listCreator;
        this.itemObjectReader = itemObjectReader;
    }
    
    @Override
    public Collection<V> createList(final JSONReader.Context context) {
        return this.listCreator.get();
    }
    
    @Override
    public void accept(final T object, final Object list) {
        this.function.accept(object, list);
    }
}
