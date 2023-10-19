// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import com.alibaba.fastjson2.util.Fnv;
import com.alibaba.fastjson2.JSONB;
import java.io.Closeable;
import com.alibaba.fastjson2.util.IOUtils;
import java.io.IOException;
import com.alibaba.fastjson2.JSONException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import java.util.function.Function;

final class ObjectWriterImplInt8ValueArray extends ObjectWriterPrimitiveImpl
{
    static final ObjectWriterImplInt8ValueArray INSTANCE;
    static final byte[] JSONB_TYPE_NAME_BYTES;
    static final long JSONB_TYPE_HASH;
    private final Function<Object, byte[]> function;
    
    public ObjectWriterImplInt8ValueArray(final Function<Object, byte[]> function) {
        this.function = function;
    }
    
    @Override
    public void writeJSONB(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (jsonWriter.isWriteTypeInfo(object, fieldType)) {
            if (object == byte[].class) {
                jsonWriter.writeTypeName(ObjectWriterImplInt8ValueArray.JSONB_TYPE_NAME_BYTES, ObjectWriterImplInt8ValueArray.JSONB_TYPE_HASH);
            }
            else {
                jsonWriter.writeTypeName(object.getClass().getName());
            }
        }
        byte[] bytes;
        if (this.function != null && object != null) {
            bytes = this.function.apply(object);
        }
        else {
            bytes = (byte[])object;
        }
        jsonWriter.writeBinary(bytes);
    }
    
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        if (object == null) {
            jsonWriter.writeArrayNull();
            return;
        }
        byte[] bytes;
        if (this.function != null) {
            bytes = this.function.apply(object);
        }
        else {
            bytes = (byte[])object;
        }
        String format = jsonWriter.context.getDateFormat();
        if ("millis".equals(format)) {
            format = null;
        }
        if ("gzip".equals(format) || "gzip,base64".equals(format)) {
            GZIPOutputStream gzipOut = null;
            try {
                final ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
                if (bytes.length < 512) {
                    gzipOut = new GZIPOutputStream(byteOut, bytes.length);
                }
                else {
                    gzipOut = new GZIPOutputStream(byteOut);
                }
                gzipOut.write(bytes);
                gzipOut.finish();
                bytes = byteOut.toByteArray();
            }
            catch (IOException ex) {
                throw new JSONException("write gzipBytes error", ex);
            }
            finally {
                IOUtils.close(gzipOut);
            }
        }
        if ("base64".equals(format) || "gzip,base64".equals(format) || (jsonWriter.getFeatures(features) & JSONWriter.Feature.WriteByteArrayAsBase64.mask) != 0x0L) {
            jsonWriter.writeBase64(bytes);
            return;
        }
        jsonWriter.startArray();
        for (int i = 0; i < bytes.length; ++i) {
            if (i != 0) {
                jsonWriter.writeComma();
            }
            jsonWriter.writeInt32(bytes[i]);
        }
        jsonWriter.endArray();
    }
    
    static {
        INSTANCE = new ObjectWriterImplInt8ValueArray(null);
        JSONB_TYPE_NAME_BYTES = JSONB.toBytes("[B");
        JSONB_TYPE_HASH = Fnv.hashCode64("[B");
    }
}
