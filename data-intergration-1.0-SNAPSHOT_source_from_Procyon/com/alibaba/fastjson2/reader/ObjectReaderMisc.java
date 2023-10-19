// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

public class ObjectReaderMisc implements ObjectReader
{
    static final long HASH_ADDRESS;
    static final long HASH_PORT;
    private final Class objectClass;
    
    public ObjectReaderMisc(final Class objectClass) {
        this.objectClass = objectClass;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        if (jsonReader.nextIfNull()) {
            return null;
        }
        if (this.objectClass == InetSocketAddress.class) {
            InetAddress inetAddress = null;
            int port = 0;
            jsonReader.nextIfObjectStart();
            while (!jsonReader.nextIfObjectEnd()) {
                final long nameHashCode = jsonReader.readFieldNameHashCode();
                if (nameHashCode == ObjectReaderMisc.HASH_ADDRESS) {
                    inetAddress = jsonReader.read(InetAddress.class);
                }
                else if (nameHashCode == ObjectReaderMisc.HASH_PORT) {
                    port = jsonReader.readInt32();
                }
                else {
                    jsonReader.skipValue();
                }
            }
            jsonReader.nextIfComma();
            return new InetSocketAddress(inetAddress, port);
        }
        throw new JSONException(jsonReader.info("not support : " + this.objectClass.getName()));
    }
    
    static {
        HASH_ADDRESS = Fnv.hashCode64("address");
        HASH_PORT = Fnv.hashCode64("port");
    }
}
