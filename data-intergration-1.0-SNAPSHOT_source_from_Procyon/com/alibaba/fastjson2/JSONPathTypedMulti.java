// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import com.alibaba.fastjson2.util.DateUtils;
import com.alibaba.fastjson2.util.TypeUtils;
import java.time.ZoneId;
import java.lang.reflect.Type;

class JSONPathTypedMulti extends JSONPath
{
    final JSONPath[] paths;
    final Type[] types;
    final String[] formats;
    final long[] pathFeatures;
    final ZoneId zoneId;
    
    protected JSONPathTypedMulti(final JSONPath[] paths, final Type[] types, final String[] formats, final long[] pathFeatures, final ZoneId zoneId, final long features) {
        super(JSON.toJSONString(paths), features);
        this.types = types;
        this.paths = paths;
        this.formats = formats;
        this.pathFeatures = pathFeatures;
        this.zoneId = zoneId;
    }
    
    @Override
    public JSONPath getParent() {
        return null;
    }
    
    @Override
    public boolean isRef() {
        for (final JSONPath jsonPath : this.paths) {
            if (!jsonPath.isRef()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public boolean contains(final Object object) {
        for (final JSONPath jsonPath : this.paths) {
            if (jsonPath.contains(object)) {
                return true;
            }
        }
        return false;
    }
    
    protected final boolean ignoreError(final int index) {
        return this.pathFeatures != null && index < this.pathFeatures.length && (this.pathFeatures[index] & Feature.NullOnError.mask) != 0x0L;
    }
    
    @Override
    public Object eval(final Object object) {
        final Object[] array = new Object[this.paths.length];
        for (int i = 0; i < this.paths.length; ++i) {
            final JSONPath jsonPath = this.paths[i];
            final Object result = jsonPath.eval(object);
            try {
                array[i] = TypeUtils.cast(result, this.types[i]);
            }
            catch (Exception e) {
                if (!this.ignoreError(i)) {
                    throw new JSONException("jsonpath eval path, path : " + jsonPath + ", msg : " + e.getMessage(), e);
                }
            }
        }
        return array;
    }
    
    @Override
    protected JSONReader.Context createContext() {
        final JSONReader.Context context = JSONFactory.createReadContext(this.features);
        if (this.zoneId != null && this.zoneId != DateUtils.DEFAULT_ZONE_ID) {
            context.zoneId = this.zoneId;
        }
        return context;
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        final Object object = jsonReader.readAny();
        return this.eval(object);
    }
    
    @Override
    public String extractScalar(final JSONReader jsonReader) {
        final Object object = this.extract(jsonReader);
        return JSON.toJSONString(object);
    }
    
    @Override
    public void set(final Object object, final Object value) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void set(final Object object, final Object value, final JSONReader.Feature... readerFeatures) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void setCallback(final Object object, final BiFunction callback) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void setInt(final Object object, final int value) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public void setLong(final Object object, final long value) {
        throw new JSONException("unsupported operation");
    }
    
    @Override
    public boolean remove(final Object object) {
        throw new JSONException("unsupported operation");
    }
}
