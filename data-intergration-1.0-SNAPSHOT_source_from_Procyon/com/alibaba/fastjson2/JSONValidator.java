// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

public class JSONValidator
{
    private final JSONReader jsonReader;
    private Boolean validateResult;
    private Type type;
    
    protected JSONValidator(final JSONReader jsonReader) {
        this.jsonReader = jsonReader;
    }
    
    public static JSONValidator fromUtf8(final byte[] jsonBytes) {
        return new JSONValidator(JSONReader.of(jsonBytes));
    }
    
    public static JSONValidator from(final String jsonStr) {
        return new JSONValidator(JSONReader.of(jsonStr));
    }
    
    public static JSONValidator from(final JSONReader jsonReader) {
        return new JSONValidator(jsonReader);
    }
    
    public boolean validate() {
        if (this.validateResult != null) {
            return this.validateResult;
        }
        char firstChar;
        try {
            firstChar = this.jsonReader.current();
            this.jsonReader.skipValue();
        }
        catch (JSONException error) {
            final Boolean value = false;
            this.validateResult = value;
            return value;
        }
        finally {
            this.jsonReader.close();
        }
        if (firstChar == '{') {
            this.type = Type.Object;
        }
        else if (firstChar == '[') {
            this.type = Type.Array;
        }
        else {
            this.type = Type.Value;
        }
        final Boolean value2 = this.jsonReader.isEnd();
        this.validateResult = value2;
        return value2;
    }
    
    public Type getType() {
        if (this.type == null) {
            this.validate();
        }
        return this.type;
    }
    
    public enum Type
    {
        Object, 
        Array, 
        Value;
        
        private static /* synthetic */ Type[] $values() {
            return new Type[] { Type.Object, Type.Array, Type.Value };
        }
        
        static {
            $VALUES = $values();
        }
    }
}
