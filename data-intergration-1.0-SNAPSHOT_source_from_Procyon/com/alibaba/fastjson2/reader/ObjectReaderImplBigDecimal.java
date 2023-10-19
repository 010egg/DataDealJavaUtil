// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.util.Map;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.function.impl.ToBigDecimal;
import java.math.BigDecimal;
import java.util.function.Function;

final class ObjectReaderImplBigDecimal extends ObjectReaderPrimitive
{
    private final Function converter;
    static final ObjectReaderImplBigDecimal INSTANCE;
    final Function<BigDecimal, Object> function;
    
    public ObjectReaderImplBigDecimal(final Function<BigDecimal, Object> function) {
        super(BigDecimal.class);
        this.converter = new ToBigDecimal();
        this.function = function;
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final BigDecimal decimal = jsonReader.readBigDecimal();
        if (this.function != null) {
            return this.function.apply(decimal);
        }
        return decimal;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final BigDecimal decimal = jsonReader.readBigDecimal();
        if (this.function != null) {
            return this.function.apply(decimal);
        }
        return decimal;
    }
    
    @Override
    public Object createInstance(final Map map, final long features) {
        Object value = map.get("value");
        if (value == null) {
            value = map.get("$numberDecimal");
        }
        if (!(value instanceof BigDecimal)) {
            value = this.converter.apply(value);
        }
        final BigDecimal decimal = (BigDecimal)value;
        if (this.function != null) {
            return this.function.apply(decimal);
        }
        return decimal;
    }
    
    static {
        INSTANCE = new ObjectReaderImplBigDecimal((Function<BigDecimal, Object>)null);
    }
}
