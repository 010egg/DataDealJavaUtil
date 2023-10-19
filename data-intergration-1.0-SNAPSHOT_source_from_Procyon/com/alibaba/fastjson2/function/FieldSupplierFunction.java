// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.function;

import java.util.function.Function;

public final class FieldSupplierFunction<T> implements Function<T, Object>
{
    public final FieldSupplier<T> supplier;
    public final int fieldIndex;
    
    public FieldSupplierFunction(final FieldSupplier<T> supplier, final int fieldIndex) {
        this.supplier = supplier;
        this.fieldIndex = fieldIndex;
    }
    
    @Override
    public Object apply(final T object) {
        return this.supplier.get(object, this.fieldIndex);
    }
}
