// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.JSONException;
import java.nio.charset.StandardCharsets;
import java.net.InetSocketAddress;
import java.util.regex.Pattern;
import java.text.SimpleDateFormat;
import java.net.InetAddress;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;

final class ObjectWriterMisc implements ObjectWriter
{
    static final ObjectWriterMisc INSTANCE;
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeNull();
            return;
        }
        final Class<?> objectClass = object.getClass();
        final String name;
        final String objectClassName = name = objectClass.getName();
        String str = null;
        switch (name) {
            case "net.sf.json.JSONNull": {
                jsonWriter.writeNull();
                return;
            }
            case "java.net.Inet4Address":
            case "java.net.Inet6Address": {
                str = ((InetAddress)object).getHostName();
                break;
            }
            case "java.text.SimpleDateFormat": {
                str = ((SimpleDateFormat)object).toPattern();
                break;
            }
            case "java.util.regex.Pattern": {
                str = ((Pattern)object).pattern();
                break;
            }
            case "java.net.InetSocketAddress": {
                final InetSocketAddress address = (InetSocketAddress)object;
                jsonWriter.startObject();
                jsonWriter.writeName("address");
                jsonWriter.writeColon();
                jsonWriter.writeAny(address.getAddress());
                jsonWriter.writeName("port");
                jsonWriter.writeColon();
                jsonWriter.writeInt32(address.getPort());
                jsonWriter.endObject();
                return;
            }
            case "com.fasterxml.jackson.databind.node.ArrayNode": {
                str = object.toString();
                if (jsonWriter.isUTF8()) {
                    jsonWriter.writeRaw(str.getBytes(StandardCharsets.UTF_8));
                }
                else {
                    jsonWriter.writeRaw(str);
                }
                return;
            }
            default: {
                throw new JSONException("not support class : " + objectClassName);
            }
        }
        jsonWriter.writeString(str);
    }
    
    static {
        INSTANCE = new ObjectWriterMisc();
    }
}
