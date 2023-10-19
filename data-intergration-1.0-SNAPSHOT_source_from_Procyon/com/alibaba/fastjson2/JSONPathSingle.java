// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;

class JSONPathSingle extends JSONPath
{
    final JSONPathSegment segment;
    final boolean ref;
    final boolean extractSupport;
    
    JSONPathSingle(final JSONPathSegment segment, final String path, final Feature... features) {
        super(path, features);
        this.segment = segment;
        this.ref = (segment instanceof JSONPathSegmentIndex || segment instanceof JSONPathSegmentName);
        boolean extractSupport = true;
        if (segment instanceof JSONPathSegment.EvalSegment) {
            extractSupport = false;
        }
        else if (segment instanceof JSONPathSegmentIndex && ((JSONPathSegmentIndex)segment).index < 0) {
            extractSupport = false;
        }
        this.extractSupport = extractSupport;
    }
    
    @Override
    public boolean remove(final Object root) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        return this.segment.remove(context);
    }
    
    @Override
    public boolean contains(final Object root) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        return this.segment.contains(context);
    }
    
    @Override
    public boolean isRef() {
        return this.ref;
    }
    
    @Override
    public Object eval(final Object root) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        this.segment.eval(context);
        return context.value;
    }
    
    @Override
    public void set(final Object root, final Object value) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        this.segment.set(context, value);
    }
    
    @Override
    public void set(final Object root, final Object value, final JSONReader.Feature... readerFeatures) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        this.segment.set(context, value);
    }
    
    @Override
    public void setCallback(final Object root, final BiFunction callback) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        this.segment.setCallback(context, callback);
    }
    
    @Override
    public void setInt(final Object root, final int value) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        this.segment.setInt(context, value);
    }
    
    @Override
    public void setLong(final Object root, final long value) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        context.root = root;
        this.segment.setLong(context, value);
    }
    
    @Override
    public Object extract(final JSONReader jsonReader) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        if (!this.extractSupport) {
            context.root = jsonReader.readAny();
            this.segment.eval(context);
        }
        else {
            this.segment.accept(jsonReader, context);
        }
        return context.value;
    }
    
    @Override
    public String extractScalar(final JSONReader jsonReader) {
        final Context context = new Context(this, null, this.segment, null, 0L);
        this.segment.accept(jsonReader, context);
        return JSON.toJSONString(context.value);
    }
    
    @Override
    public final JSONPath getParent() {
        return RootPath.INSTANCE;
    }
}
