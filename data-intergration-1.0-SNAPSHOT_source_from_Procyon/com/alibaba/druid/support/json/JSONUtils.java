// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.druid.support.json;

public class JSONUtils
{
    public static String toJSONString(final Object o) {
        final JSONWriter writer = new JSONWriter();
        writer.writeObject(o);
        return writer.toString();
    }
    
    public static Object parse(final String text) {
        final JSONParser parser = new JSONParser(text);
        return parser.parse();
    }
}
