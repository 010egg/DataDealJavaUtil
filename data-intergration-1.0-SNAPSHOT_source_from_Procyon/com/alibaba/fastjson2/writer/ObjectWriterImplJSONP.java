// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.writer;

import java.util.List;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.JSONPObject;

public class ObjectWriterImplJSONP extends ObjectWriterPrimitiveImpl<JSONPObject>
{
    @Override
    public void write(final JSONWriter jsonWriter, final Object object, final Object fieldName, final Type fieldType, final long features) {
        final long features2 = jsonWriter.getFeatures(features);
        if ((features2 & JSONWriter.Feature.BrowserSecure.mask) != 0x0L) {
            final String SECURITY_PREFIX = "/**/";
            jsonWriter.writeRaw("/**/");
        }
        final JSONPObject jsonp = (JSONPObject)object;
        jsonWriter.writeRaw(jsonp.getFunction());
        jsonWriter.writeRaw('(');
        final List<Object> parameters = jsonp.getParameters();
        for (int i = 0; i < parameters.size(); ++i) {
            if (i != 0) {
                jsonWriter.writeRaw(',');
            }
            jsonWriter.writeAny(parameters.get(i));
        }
        jsonWriter.writeRaw(')');
    }
}
