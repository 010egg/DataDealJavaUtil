// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;
import java.util.Locale;

class ObjectReaderImplLocale extends ObjectReaderPrimitive
{
    static final ObjectReaderImplLocale INSTANCE;
    
    ObjectReaderImplLocale() {
        super(Locale.class);
    }
    
    @Override
    public Object readJSONBObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final String strVal = jsonReader.readString();
        if (strVal == null || strVal.isEmpty()) {
            return null;
        }
        final String[] items = strVal.split("_");
        if (items.length == 1) {
            return new Locale(items[0]);
        }
        if (items.length == 2) {
            return new Locale(items[0], items[1]);
        }
        return new Locale(items[0], items[1], items[2]);
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        final String strVal = jsonReader.readString();
        if (strVal == null || strVal.isEmpty()) {
            return null;
        }
        final String[] items = strVal.split("_");
        if (items.length == 1) {
            return new Locale(items[0]);
        }
        if (items.length == 2) {
            return new Locale(items[0], items[1]);
        }
        return new Locale(items[0], items[1], items[2]);
    }
    
    static {
        INSTANCE = new ObjectReaderImplLocale();
    }
}
