// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson.parser.deserializer;

import java.lang.reflect.InvocationTargetException;
import com.alibaba.fastjson.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import java.lang.reflect.Method;

public class EnumCreatorDeserializer implements ObjectDeserializer
{
    private final Method creator;
    private final Class paramType;
    
    public EnumCreatorDeserializer(final Method creator) {
        this.creator = creator;
        this.paramType = creator.getParameterTypes()[0];
    }
    
    public <T> T deserialze(final DefaultJSONParser parser, final Type type, final Object fieldName) {
        final Object arg = parser.parseObject((Class<Object>)this.paramType);
        try {
            return (T)this.creator.invoke(null, arg);
        }
        catch (IllegalAccessException e) {
            throw new JSONException("parse enum error", e);
        }
        catch (InvocationTargetException e2) {
            throw new JSONException("parse enum error", e2);
        }
    }
    
    public int getFastMatchToken() {
        return 0;
    }
}
