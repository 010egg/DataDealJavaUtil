// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class JSONPathMulti extends JSONPath
{
    final List<JSONPathSegment> segments;
    final boolean ref;
    final boolean extractSupport;
    
    JSONPathMulti(final String path, final List<JSONPathSegment> segments, final Feature... features) {
        super(path, features);
        this.segments = segments;
        boolean extractSupport = true;
        boolean ref = true;
        for (final JSONPathSegment segment : segments) {
            if (segment instanceof JSONPathSegmentIndex) {
                if (((JSONPathSegmentIndex)segment).index >= 0) {
                    continue;
                }
                extractSupport = false;
            }
            else {
                if (segment instanceof JSONPathSegmentName) {
                    continue;
                }
                ref = false;
                break;
            }
        }
        this.extractSupport = extractSupport;
        this.ref = ref;
    }
    
    @Override
    public boolean remove(final Object root) {
        Context context = null;
        final int size = this.segments.size();
        if (size == 0) {
            return false;
        }
        for (int i = 0; i < size; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (i == 0) {
                context.root = root;
            }
            if (i == size - 1) {
                return segment.remove(context);
            }
            segment.eval(context);
            if (context.value == null) {
                return false;
            }
        }
        return false;
    }
    
    @Override
    public boolean contains(final Object root) {
        Context context = null;
        final int size = this.segments.size();
        if (size == 0) {
            return root != null;
        }
        for (int i = 0; i < size; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (i == 0) {
                context.root = root;
            }
            if (i == size - 1) {
                return segment.contains(context);
            }
            segment.eval(context);
        }
        return false;
    }
    
    @Override
    public boolean endsWithFilter() {
        final int size = this.segments.size();
        final JSONPathSegment last = this.segments.get(size - 1);
        return last instanceof JSONPathFilter;
    }
    
    @Override
    public JSONPath getParent() {
        final int size = this.segments.size();
        if (size == 0) {
            return null;
        }
        if (size == 1) {
            return RootPath.INSTANCE;
        }
        if (size == 2) {
            return JSONPath.of(this.segments.get(0));
        }
        final StringBuilder buf = new StringBuilder();
        buf.append('$');
        final List<JSONPathSegment> parentSegments = new ArrayList<JSONPathSegment>(size - 1);
        for (int i = 0, end = size - 1; i < end; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            parentSegments.add(segment);
            final boolean array = segment instanceof JSONPathSegmentIndex || segment instanceof JSONPathSegment.MultiIndexSegment || segment instanceof JSONPathFilter;
            if (!array) {
                buf.append('.');
            }
            buf.append(segment);
        }
        final String parentPath = buf.toString();
        if (size == 3) {
            new JSONPathTwoSegment(parentPath, this.segments.get(0), this.segments.get(1), new Feature[0]);
        }
        return new JSONPathMulti(parentPath, parentSegments, new Feature[0]);
    }
    
    @Override
    public boolean isRef() {
        return this.ref;
    }
    
    @Override
    public Object eval(final Object root) {
        Context context = null;
        final int size = this.segments.size();
        if (size == 0) {
            return root;
        }
        for (int i = 0; i < size; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (i == 0) {
                context.root = root;
            }
            segment.eval(context);
        }
        Object contextValue = context.value;
        if ((context.path.features & Feature.AlwaysReturnList.mask) != 0x0L) {
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
        Context context = null;
        final int size = this.segments.size();
        for (int i = 0; i < size - 1; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (i == 0) {
                context.root = root;
            }
            segment.eval(context);
            if (context.value == null && nextSegment != null) {
                if (value == null) {
                    return;
                }
                Object parentObject;
                if (i == 0) {
                    parentObject = root;
                }
                else {
                    parentObject = context.parent.value;
                }
                Object emptyValue;
                if (nextSegment instanceof JSONPathSegmentIndex) {
                    emptyValue = new JSONArray();
                }
                else {
                    if (!(nextSegment instanceof JSONPathSegmentName)) {
                        return;
                    }
                    emptyValue = new JSONObject();
                }
                context.value = emptyValue;
                if (parentObject instanceof Map && segment instanceof JSONPathSegmentName) {
                    ((Map)parentObject).put(((JSONPathSegmentName)segment).name, emptyValue);
                }
                else if (parentObject instanceof List && segment instanceof JSONPathSegmentIndex) {
                    final List list = (List)parentObject;
                    final int index = ((JSONPathSegmentIndex)segment).index;
                    if (index == list.size()) {
                        list.add(emptyValue);
                    }
                    else {
                        list.set(index, emptyValue);
                    }
                }
                else if (parentObject != null) {
                    final Class<?> parentObjectClass = parentObject.getClass();
                    final JSONReader.Context readerContext = this.getReaderContext();
                    final ObjectReader<?> objectReader = (ObjectReader<?>)readerContext.getObjectReader(parentObjectClass);
                    if (segment instanceof JSONPathSegmentName) {
                        final FieldReader fieldReader = objectReader.getFieldReader(((JSONPathSegmentName)segment).nameHashCode);
                        if (fieldReader != null) {
                            final ObjectReader fieldObjectReader = fieldReader.getObjectReader(readerContext);
                            final Object fieldValue = fieldObjectReader.createInstance();
                            fieldReader.accept(parentObject, fieldValue);
                            context.value = fieldValue;
                        }
                    }
                }
            }
        }
        context = new Context(this, context, this.segments.get(0), null, 0L);
        context.root = root;
        final JSONPathSegment segment2 = this.segments.get(size - 1);
        segment2.set(context, value);
    }
    
    @Override
    public void set(final Object root, final Object value, final JSONReader.Feature... readerFeatures) {
        long features = 0L;
        for (final JSONReader.Feature feature : readerFeatures) {
            features |= feature.mask;
        }
        Context context = null;
        final int size = this.segments.size();
        for (int i = 0; i < size - 1; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, features);
            if (i == 0) {
                context.root = root;
            }
            segment.eval(context);
        }
        context = new Context(this, context, this.segments.get(0), null, features);
        context.root = root;
        final JSONPathSegment segment2 = this.segments.get(size - 1);
        segment2.set(context, value);
    }
    
    @Override
    public void setCallback(final Object root, final BiFunction callback) {
        Context context = null;
        final int size = this.segments.size();
        for (int i = 0; i < size - 1; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (i == 0) {
                context.root = root;
            }
            segment.eval(context);
        }
        context = new Context(this, context, this.segments.get(0), null, 0L);
        context.root = root;
        final JSONPathSegment segment2 = this.segments.get(size - 1);
        segment2.setCallback(context, callback);
    }
    
    @Override
    public void setInt(final Object rootObject, final int value) {
        this.set(rootObject, value);
    }
    
    @Override
    public void setLong(final Object rootObject, final long value) {
        this.set(rootObject, value);
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        if (jsonReader == null) {
            return null;
        }
        final int size = this.segments.size();
        if (size == 0) {
            return null;
        }
        if (!this.extractSupport) {
            final Object root = jsonReader.readAny();
            return this.eval(root);
        }
        boolean eval = false;
        Context context = null;
        for (int i = 0; i < size; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (eval) {
                segment.eval(context);
            }
            else {
                segment.accept(jsonReader, context);
            }
            if (context.eval) {
                eval = true;
                if (context.value == null) {
                    break;
                }
            }
        }
        Object value = context.value;
        if (value instanceof Sequence) {
            value = ((Sequence)value).values;
        }
        if ((this.features & Feature.AlwaysReturnList.mask) != 0x0L) {
            if (value == null) {
                value = new JSONArray();
            }
            else if (!(value instanceof List)) {
                value = JSONArray.of(value);
            }
        }
        return value;
    }
    
    @Override
    public String extractScalar(final JSONReader jsonReader) {
        final int size = this.segments.size();
        if (size == 0) {
            return null;
        }
        boolean eval = false;
        Context context = null;
        for (int i = 0; i < size; ++i) {
            final JSONPathSegment segment = this.segments.get(i);
            JSONPathSegment nextSegment = null;
            final int nextIndex = i + 1;
            if (nextIndex < size) {
                nextSegment = this.segments.get(nextIndex);
            }
            context = new Context(this, context, segment, nextSegment, 0L);
            if (eval) {
                segment.eval(context);
            }
            else {
                segment.accept(jsonReader, context);
            }
            if (context.eval) {
                eval = true;
                if (context.value == null) {
                    break;
                }
            }
        }
        return JSON.toJSONString(context.value);
    }
}
