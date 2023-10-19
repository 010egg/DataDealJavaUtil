// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.List;

class JSONPathTwoSegment extends JSONPath
{
    final JSONPathSegment first;
    final JSONPathSegment second;
    final boolean ref;
    final boolean extractSupport;
    
    JSONPathTwoSegment(final String path, final JSONPathSegment first, final JSONPathSegment second, final Feature... features) {
        super(path, features);
        this.first = first;
        this.second = second;
        this.ref = ((first instanceof JSONPathSegmentIndex || first instanceof JSONPathSegmentName) && (second instanceof JSONPathSegmentIndex || second instanceof JSONPathSegmentName));
        boolean extractSupport = true;
        if (first instanceof JSONPathSegment.EvalSegment) {
            extractSupport = false;
        }
        else if (first instanceof JSONPathSegmentIndex && ((JSONPathSegmentIndex)first).index < 0) {
            extractSupport = false;
        }
        else if (second instanceof JSONPathSegment.EvalSegment) {
            extractSupport = false;
        }
        else if (second instanceof JSONPathSegmentIndex && ((JSONPathSegmentIndex)second).index < 0) {
            extractSupport = false;
        }
        this.extractSupport = extractSupport;
    }
    
    @Override
    public boolean endsWithFilter() {
        return this.second instanceof JSONPathFilter;
    }
    
    @Override
    public JSONPath getParent() {
        return JSONPath.of(this.first);
    }
    
    @Override
    public boolean remove(final Object root) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return false;
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        return this.second.remove(context2);
    }
    
    @Override
    public boolean contains(final Object root) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return false;
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        return this.second.contains(context2);
    }
    
    @Override
    public boolean isRef() {
        return this.ref;
    }
    
    @Override
    public Object eval(final Object root) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return null;
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        this.second.eval(context2);
        Object contextValue = context2.value;
        if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
            if (contextValue == null) {
                contextValue = new JSONArray();
            }
            else if (!(contextValue instanceof List)) {
                contextValue = JSONArray.of(contextValue);
            }
        }
        return contextValue;
    }
    
    @Override
    public void set(final Object root, final Object value) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            Object emptyValue;
            if (this.second instanceof JSONPathSegmentIndex) {
                emptyValue = new JSONArray();
            }
            else {
                if (!(this.second instanceof JSONPathSegmentName)) {
                    return;
                }
                emptyValue = new JSONObject();
            }
            context0.value = emptyValue;
            if (root instanceof Map && this.first instanceof JSONPathSegmentName) {
                ((Map)root).put(((JSONPathSegmentName)this.first).name, emptyValue);
            }
            else if (root instanceof List && this.first instanceof JSONPathSegmentIndex) {
                ((List)root).set(((JSONPathSegmentIndex)this.first).index, emptyValue);
            }
            else if (root != null) {
                final Class<?> parentObjectClass = root.getClass();
                final JSONReader.Context readerContext = this.getReaderContext();
                final ObjectReader<?> objectReader = (ObjectReader<?>)readerContext.getObjectReader(parentObjectClass);
                if (this.first instanceof JSONPathSegmentName) {
                    final FieldReader fieldReader = objectReader.getFieldReader(((JSONPathSegmentName)this.first).nameHashCode);
                    if (fieldReader != null) {
                        final ObjectReader fieldObjectReader = fieldReader.getObjectReader(readerContext);
                        final Object fieldValue = fieldObjectReader.createInstance();
                        fieldReader.accept(root, fieldValue);
                        context0.value = fieldValue;
                    }
                }
            }
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        this.second.set(context2, value);
    }
    
    @Override
    public void set(final Object root, final Object value, final JSONReader.Feature... readerFeatures) {
        long features = 0L;
        for (final JSONReader.Feature feature : readerFeatures) {
            features |= feature.mask;
        }
        final Context context0 = new Context(this, null, this.first, this.second, features);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return;
        }
        final Context context2 = new Context(this, context0, this.second, null, features);
        this.second.set(context2, value);
    }
    
    @Override
    public void setCallback(final Object root, final BiFunction callback) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return;
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        this.second.setCallback(context2, callback);
    }
    
    @Override
    public void setInt(final Object root, final int value) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return;
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        this.second.setInt(context2, value);
    }
    
    @Override
    public void setLong(final Object root, final long value) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        context0.root = root;
        this.first.eval(context0);
        if (context0.value == null) {
            return;
        }
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        this.second.setLong(context2, value);
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader == null) {
            return null;
        }
        if (!this.extractSupport) {
            final Object root = jsonReader.readAny();
            return this.eval(root);
        }
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        this.first.accept(jsonReader, context0);
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        if (context0.eval) {
            this.second.eval(context2);
        }
        else {
            this.second.accept(jsonReader, context2);
        }
        Object contextValue = context2.value;
        if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
            if (contextValue == null) {
                contextValue = new JSONArray();
            }
            else if (!(contextValue instanceof List)) {
                contextValue = JSONArray.of(contextValue);
            }
        }
        if (contextValue instanceof Sequence) {
            contextValue = ((Sequence)contextValue).values;
        }
        return contextValue;
    }
    
    @Override
    public String extractScalar(final JSONReader jsonReader) {
        final Context context0 = new Context(this, null, this.first, this.second, 0L);
        this.first.accept(jsonReader, context0);
        final Context context2 = new Context(this, context0, this.second, null, 0L);
        this.second.accept(jsonReader, context2);
        return JSON.toJSONString(context2.value);
    }
}
