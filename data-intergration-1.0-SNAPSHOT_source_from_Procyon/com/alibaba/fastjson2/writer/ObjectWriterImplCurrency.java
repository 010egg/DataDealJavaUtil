// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import com.alibaba.fastjson2.util.TypeUtils;
import java.util.Currency;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterImplCurrency extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplCurrency INSTANCE;
    static final ObjectWriterImplCurrency INSTANCE_FOR_FIELD;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    final Class defineClass;
    
    ObjectWriterImplCurrency(final Class defineClass) {
        this.defineClass = defineClass;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Currency currency = (Currency)object;
        if (jsonWriter.isWriteTypeInfo(currency) && this.defineClass == null) {
            jsonWriter.writeTypeName(ObjectWriterImplCurrency.JSONB_TYPE_NAME_BYTES, ObjectWriterImplCurrency.JSONB_TYPE_HASH);
        }
        jsonWriter.writeString(currency.getCurrencyCode());
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.jsonb) {
            this.writeJSONB(jsonWriter, object, fieldName, fieldType, features);
            return;
        }
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Currency currency = (Currency)object;
        jsonWriter.writeString(currency.getCurrencyCode());
    }
    
    static {
        INSTANCE = new ObjectWriterImplCurrency(null);
        INSTANCE_FOR_FIELD = new ObjectWriterImplCurrency(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes(TypeUtils.getTypeName(Currency.class));
        JSONB_TYPE_HASH = Fnv.hashCode64(TypeUtils.getTypeName(Currency.class));
    }
}
