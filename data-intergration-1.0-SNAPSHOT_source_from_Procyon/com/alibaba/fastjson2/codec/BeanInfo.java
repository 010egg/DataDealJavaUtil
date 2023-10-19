// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2.codec;

import java.util.ArrayList;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONReader;
import java.util.Locale;
import com.alibaba.fastjson2.filter.Filter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class BeanInfo
{
    public String typeKey;
    public String typeName;
    public Class builder;
    public Method buildMethod;
    public String builderWithPrefix;
    public Class[] seeAlso;
    public String[] seeAlsoNames;
    public Class seeAlsoDefault;
    public Constructor creatorConstructor;
    public Constructor markerConstructor;
    public Method createMethod;
    public String[] createParameterNames;
    public long readerFeatures;
    public long writerFeatures;
    public boolean writeEnumAsJavaBean;
    public String namingStrategy;
    public String[] ignores;
    public String[] orders;
    public String[] includes;
    public boolean mixIn;
    public boolean kotlin;
    public Class serializer;
    public Class deserializer;
    public Class<? extends Filter>[] serializeFilters;
    public String schema;
    public String format;
    public Locale locale;
    public boolean alphabetic;
    public String objectWriterFieldName;
    public String objectReaderFieldName;
    public Class<? extends JSONReader.AutoTypeBeforeHandler> autoTypeBeforeHandler;
    
    public BeanInfo() {
        this.alphabetic = true;
    }
    
    public void required(final String fieldName) {
        if (this.schema == null) {
            this.schema = JSONObject.of("required", (Object)JSONArray.of((Object)fieldName)).toString();
        }
        else {
            final JSONObject object = JSONObject.parseObject(this.schema);
            final JSONArray array = object.getJSONArray("required");
            ((ArrayList<String>)array).add(fieldName);
            this.schema = object.toString();
        }
    }
}
