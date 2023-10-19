// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Currency;

final class ObjectReaderImplCurrency extends ObjectReaderPrimitive
{
    static final ObjectReaderImplCurrency INSTANCE;
    static final long TYPE_HASH;
    
    ObjectReaderImplCurrency() {
        super(Currency.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.getType() == -110) {
            jsonReader.next();
            final long typeHash = jsonReader.readTypeHashCode();
            final long TYPE_HASH_FULL = -7860540621745740270L;
            if (typeHash != ObjectReaderImplCurrency.TYPE_HASH && typeHash != -7860540621745740270L) {
                throw new JSONException(jsonReader.info("currency not support input autoTypeClass " + jsonReader.getString()));
            }
        }
        final String strVal = jsonReader.readString();
        if (strVal == null || strVal.isEmpty()) {
            return null;
        }
        return Currency.getInstance(strVal);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        String strVal;
        if (jsonReader.isObject()) {
            final JSONObject jsonObject = new JSONObject();
            jsonReader.readObject(jsonObject, new JSONReader.Feature[0]);
            strVal = jsonObject.getString("currency");
            if (strVal == null) {
                strVal = jsonObject.getString("currencyCode");
            }
        }
        else {
            strVal = jsonReader.readString();
        }
        if (strVal == null || strVal.isEmpty()) {
            return null;
        }
        return Currency.getInstance(strVal);
    }
    
    static {
        INSTANCE = new ObjectReaderImplCurrency();
        TYPE_HASH = Fnv.hashCode64("Currency");
    }
}
