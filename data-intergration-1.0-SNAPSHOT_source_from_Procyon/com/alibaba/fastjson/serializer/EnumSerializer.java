// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.serializer;

import java.io.IOException;
import com.alibaba.fastjson.JSONException;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.lang.reflect.Member;

public class EnumSerializer implements ObjectSerializer
{
    private final Member member;
    public static final EnumSerializer instance;
    
    public EnumSerializer() {
        this.member = null;
    }
    
    public EnumSerializer(final Member member) {
        this.member = member;
    }
    
    public void write(final JSONSerializer serializer, final Object object, final Object fieldName, final Type fieldType, final int features) throws IOException {
        if (this.member == null) {
            final SerializeWriter out = serializer.out;
            out.writeEnum((Enum<?>)object);
            return;
        }
        Object fieldValue = null;
        try {
            if (this.member instanceof Field) {
                fieldValue = ((Field)this.member).get(object);
            }
            else {
                fieldValue = ((Method)this.member).invoke(object, new Object[0]);
            }
        }
        catch (Exception e) {
            throw new JSONException("getEnumValue error", e);
        }
        serializer.write(fieldValue);
    }
    
    static {
        instance = new EnumSerializer();
    }
}
