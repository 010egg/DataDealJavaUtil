// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function;

import java.util.function.BiConsumer;

public final class FieldBiConsumer<T> implements BiConsumer<T, Object>
{
    public final int fieldIndex;
    public final FieldConsumer<T> consumer;
    
    public FieldBiConsumer(final int fieldIndex, final FieldConsumer<T> consumer) {
        this.fieldIndex = fieldIndex;
        this.consumer = consumer;
    }
    
    @Override
    public void accept(final T object, final Object fieldValue) {
        this.consumer.accept(object, this.fieldIndex, fieldValue);
    }
}
