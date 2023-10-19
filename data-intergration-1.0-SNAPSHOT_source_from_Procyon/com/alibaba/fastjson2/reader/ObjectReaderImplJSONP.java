// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.reader;

import com.alibaba.fastjson2.JSONPObject;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONException;
import java.lang.reflect.Type;
import com.alibaba.fastjson2.JSONReader;

public class ObjectReaderImplJSONP implements ObjectReader
{
    private final Class objectClass;
    
    public ObjectReaderImplJSONP(final Class objectClass) {
        this.objectClass = objectClass;
    }
    
    @Override
    public Object readObject(final JSONReader jsonReader, final Type fieldType, final Object fieldName, final long features) {
        String funcName = jsonReader.readFieldNameUnquote();
        if (jsonReader.nextIfMatch('.')) {
            final String name2 = jsonReader.readFieldNameUnquote();
            funcName = funcName + '.' + name2;
        }
        char ch = jsonReader.current();
        if (ch == '/' && jsonReader.nextIfMatchIdent('/', '*', '*', '/')) {
            ch = jsonReader.current();
        }
        if (ch != '(') {
            throw new JSONException(jsonReader.info("illegal jsonp input"));
        }
        jsonReader.next();
        JSONPObject jsonp;
        if (this.objectClass == JSONObject.class) {
            jsonp = new JSONPObject(funcName);
        }
        else {
            try {
                jsonp = this.objectClass.newInstance();
            }
            catch (InstantiationException | IllegalAccessException ex2) {
                final ReflectiveOperationException ex;
                final ReflectiveOperationException e = ex;
                throw new JSONException("create jsonp instance error", e);
            }
            jsonp.setFunction(funcName);
        }
        while (!jsonReader.isEnd()) {
            if (jsonReader.nextIfMatch(')')) {
                jsonReader.nextIfMatch(';');
                jsonReader.nextIfMatchIdent('/', '*', '*', '/');
                return jsonp;
            }
            final Object param = jsonReader.readAny();
            jsonp.addParameter(param);
        }
        throw new JSONException(jsonReader.info("illegal jsonp input"));
    }
}
