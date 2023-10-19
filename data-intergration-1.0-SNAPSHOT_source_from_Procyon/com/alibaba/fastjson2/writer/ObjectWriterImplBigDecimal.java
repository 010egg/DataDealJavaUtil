// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.math.BigDecimal;
import java.util.function.Function;
import java.text.DecimalFormat;

final class ObjectWriterImplBigDecimal extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplBigDecimal INSTANCE;
    private final DecimalFormat format;
    final Function<Object, BigDecimal> function;
    
    public ObjectWriterImplBigDecimal(final DecimalFormat format, final Function<Object, BigDecimal> function) {
        this.format = format;
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        BigDecimal decimal;
        if (this.function != null && object != null) {
            decimal = this.function.apply(object);
        }
        else {
            decimal = (BigDecimal)object;
        }
        jsonWriter.writeDecimal(decimal, features, this.format);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        BigDecimal decimal;
        if (this.function != null && object != null) {
            decimal = this.function.apply(object);
        }
        else {
            decimal = (BigDecimal)object;
        }
        jsonWriter.writeDecimal(decimal, features, this.format);
    }
    
    @Override
    public Function getFunction() {
        return this.function;
    }
    
    static {
        INSTANCE = new ObjectWriterImplBigDecimal(null, null);
    }
}
