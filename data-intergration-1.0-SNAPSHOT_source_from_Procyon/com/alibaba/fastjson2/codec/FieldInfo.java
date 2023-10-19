// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.codec;

import java.lang.reflect.Constructor;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.util.Locale;

public class FieldInfo
{
    public static final long VALUE_MASK = 281474976710656L;
    public static final long UNWRAPPED_MASK = 562949953421312L;
    public static final long RAW_VALUE_MASK = 1125899906842624L;
    public static final long READ_USING_MASK = 2251799813685248L;
    public static final long FIELD_MASK = 4503599627370496L;
    public static final long JIT = 18014398509481984L;
    public static final long DISABLE_UNSAFE = 36028797018963968L;
    public String fieldName;
    public String format;
    public String label;
    public int ordinal;
    public long features;
    public boolean ignore;
    public String[] alternateNames;
    public Class<?> writeUsing;
    public Class<?> keyUsing;
    public Class<?> valueUsing;
    public Class<?> readUsing;
    public boolean fieldClassMixIn;
    public boolean isTransient;
    public String defaultValue;
    public Locale locale;
    public String schema;
    public boolean required;
    
    public ObjectReader getInitReader() {
        if (this.readUsing != null && ObjectReader.class.isAssignableFrom(this.readUsing)) {
            try {
                final Constructor<?> constructor = this.readUsing.getDeclaredConstructor((Class<?>[])new Class[0]);
                constructor.setAccessible(true);
                return (ObjectReader)constructor.newInstance(new Object[0]);
            }
            catch (Exception ex) {
                return null;
            }
        }
        return null;
    }
    
    public void init() {
        this.fieldName = null;
        this.format = null;
        this.label = null;
        this.ordinal = 0;
        this.features = 0L;
        this.ignore = false;
        this.required = false;
        this.alternateNames = null;
        this.writeUsing = null;
        this.keyUsing = null;
        this.valueUsing = null;
        this.readUsing = null;
        this.fieldClassMixIn = false;
        this.isTransient = false;
        this.defaultValue = null;
        this.locale = null;
        this.schema = null;
    }
}
