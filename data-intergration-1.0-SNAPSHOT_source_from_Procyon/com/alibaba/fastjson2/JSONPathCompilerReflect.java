// 
// Decompiled by Procyon v0.5.36
// 

package com.alibaba.fastjson2;

import java.util.function.BiFunction;
import com.alibaba.fastjson2.writer.FieldWriter;
import com.alibaba.fastjson2.writer.ObjectWriter;
import com.alibaba.fastjson2.reader.FieldReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import java.lang.reflect.Type;

public class JSONPathCompilerReflect implements JSONFactory.JSONPathCompiler
{
    static final JSONPathCompilerReflect INSTANCE;
    
    @Override
    public JSONPath compile(final Class objectClass, final JSONPath path) {
        if (path instanceof JSONPathSingleName) {
            return this.compileSingleNamePath(objectClass, (JSONPathSingleName)path);
        }
        if (path instanceof JSONPathTwoSegment) {
            final JSONPathTwoSegment twoSegmentPath = (JSONPathTwoSegment)path;
            final JSONPathSegment first = this.compile(objectClass, path, twoSegmentPath.first, null);
            final JSONPathSegment segment = this.compile(objectClass, path, twoSegmentPath.second, first);
            if (first != twoSegmentPath.first || segment != twoSegmentPath.second) {
                if (first instanceof NameSegmentTyped && segment instanceof NameSegmentTyped) {
                    return new TwoNameSegmentTypedPath(twoSegmentPath.path, (NameSegmentTyped)first, (NameSegmentTyped)segment);
                }
                return new JSONPathTwoSegment(twoSegmentPath.path, first, segment, new JSONPath.Feature[0]);
            }
        }
        return path;
    }
    
    protected JSONPath compileSingleNamePath(final Class objectClass, final JSONPathSingleName path) {
        final String fieldName = path.name;
        final ObjectReader objectReader = path.getReaderContext().getObjectReader(objectClass);
        final FieldReader fieldReader = objectReader.getFieldReader(fieldName);
        final ObjectWriter objectWriter = path.getWriterContext().getObjectWriter((Class<Object>)objectClass);
        final FieldWriter fieldWriter = objectWriter.getFieldWriter(fieldName);
        return new SingleNamePathTyped(path.path, objectClass, objectReader, fieldReader, objectWriter, fieldWriter);
    }
    
    protected JSONPathSegment compile(final Class objectClass, final JSONPath path, final JSONPathSegment segment, final JSONPathSegment parent) {
        if (segment instanceof JSONPathSegmentName) {
            final JSONPathSegmentName nameSegment = (JSONPathSegmentName)segment;
            final String fieldName = nameSegment.name;
            final JSONReader.Context readerContext = path.getReaderContext();
            final JSONWriter.Context writerContext = path.getWriterContext();
            ObjectReader objectReader = null;
            FieldReader fieldReader = null;
            if (parent == null) {
                objectReader = readerContext.getObjectReader(objectClass);
            }
            else if (parent instanceof NameSegmentTyped) {
                final NameSegmentTyped nameSegmentTyped = (NameSegmentTyped)parent;
                if (nameSegmentTyped.fieldReader != null) {
                    objectReader = readerContext.getObjectReader(nameSegmentTyped.fieldReader.fieldType);
                }
            }
            if (objectReader != null) {
                fieldReader = objectReader.getFieldReader(fieldName);
            }
            ObjectWriter objectWriter = null;
            FieldWriter fieldWriter = null;
            if (parent == null) {
                objectWriter = writerContext.getObjectWriter((Class<Object>)objectClass);
            }
            else if (parent instanceof NameSegmentTyped) {
                final NameSegmentTyped nameSegmentTyped2 = (NameSegmentTyped)parent;
                if (nameSegmentTyped2.fieldWriter != null) {
                    objectWriter = writerContext.getObjectWriter((Class<Object>)nameSegmentTyped2.fieldWriter.fieldClass);
                }
            }
            if (objectWriter != null) {
                fieldWriter = objectWriter.getFieldWriter(fieldName);
            }
            return new NameSegmentTyped(objectClass, objectReader, fieldReader, objectWriter, fieldWriter, fieldName, nameSegment.nameHashCode);
        }
        return segment;
    }
    
    static {
        INSTANCE = new JSONPathCompilerReflect();
    }
    
    public static class TwoNameSegmentTypedPath extends JSONPathTwoSegment
    {
        final NameSegmentTyped first;
        final NameSegmentTyped second;
        
        public TwoNameSegmentTypedPath(final String path, final NameSegmentTyped first, final NameSegmentTyped second) {
            super(path, first, second, new Feature[0]);
            this.first = first;
            this.second = second;
        }
        
        @Override
        public Object eval(final Object root) {
            final Object firstValue = this.first.fieldWriter.getFieldValue(root);
            if (firstValue == null) {
                return null;
            }
            return this.second.fieldWriter.getFieldValue(firstValue);
        }
        
        @Override
        public void set(final Object root, final Object value) {
            final Object firstValue = this.first.fieldWriter.getFieldValue(root);
            if (firstValue == null) {
                return;
            }
            this.second.fieldReader.accept(firstValue, value);
        }
        
        @Override
        public void setInt(final Object root, final int value) {
            final Object firstValue = this.first.fieldWriter.getFieldValue(root);
            if (firstValue == null) {
                return;
            }
            this.second.fieldReader.accept(firstValue, value);
        }
        
        @Override
        public void setLong(final Object root, final long value) {
            final Object firstValue = this.first.fieldWriter.getFieldValue(root);
            if (firstValue == null) {
                return;
            }
            this.second.fieldReader.accept(firstValue, value);
        }
        
        @Override
        public void setCallback(final Object root, final BiFunction callback) {
            final Object firstValue = this.first.fieldWriter.getFieldValue(root);
            if (firstValue == null) {
                return;
            }
            final Object secondValue = this.second.fieldWriter.getFieldValue(firstValue);
            final Object secondValueApply = callback.apply(firstValue, secondValue);
            if (secondValueApply == secondValue) {
                return;
            }
            if (this.second.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.second.fieldReader.accept(firstValue, secondValueApply);
        }
    }
    
    public static class NameSegmentTyped extends JSONPathSegmentName
    {
        final Class objectClass;
        final FieldReader fieldReader;
        final FieldWriter fieldWriter;
        
        public NameSegmentTyped(final Class objectClass, final ObjectReader objectReader, final FieldReader fieldReader, final ObjectWriter objectWriter, final FieldWriter fieldWriter, final String name, final long nameHashCode) {
            super(name, nameHashCode);
            this.objectClass = objectClass;
            this.fieldReader = fieldReader;
            this.fieldWriter = fieldWriter;
        }
        
        @Override
        public void eval(final JSONPath.Context context) {
            if (this.fieldWriter == null) {
                throw new UnsupportedOperationException();
            }
            final Object object = (context.parent == null) ? context.root : context.parent.value;
            if (object == null) {
                return;
            }
            context.value = this.fieldWriter.getFieldValue(object);
        }
    }
    
    public static class SingleNamePathTyped extends JSONPath
    {
        final Class objectClass;
        final ObjectReader objectReader;
        final FieldReader fieldReader;
        final ObjectWriter objectWriter;
        final FieldWriter fieldWriter;
        
        public SingleNamePathTyped(final String path, final Class objectClass, final ObjectReader objectReader, final FieldReader fieldReader, final ObjectWriter objectWriter, final FieldWriter fieldWriter) {
            super(path, new Feature[0]);
            this.objectClass = objectClass;
            this.objectReader = objectReader;
            this.fieldReader = fieldReader;
            this.objectWriter = objectWriter;
            this.fieldWriter = fieldWriter;
        }
        
        @Override
        public JSONPath getParent() {
            return null;
        }
        
        @Override
        public boolean isRef() {
            return true;
        }
        
        @Override
        public boolean contains(final Object rootObject) {
            return this.fieldWriter != null && this.fieldWriter.getFieldValue(rootObject) != null;
        }
        
        @Override
        public Object eval(final Object object) {
            if (this.fieldWriter == null) {
                throw new UnsupportedOperationException();
            }
            return this.fieldWriter.getFieldValue(object);
        }
        
        @Override
        public Object extract(final JSONReader jsonReader) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public String extractScalar(final JSONReader jsonReader) {
            throw new UnsupportedOperationException();
        }
        
        @Override
        public void set(final Object rootObject, final Object value) {
            if (this.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.fieldReader.accept(rootObject, value);
        }
        
        @Override
        public void set(final Object rootObject, final Object value, final JSONReader.Feature... readerFeatures) {
            if (this.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.fieldReader.accept(rootObject, value);
        }
        
        @Override
        public void setCallback(final Object rootObject, final BiFunction callback) {
            if (this.fieldWriter == null) {
                throw new UnsupportedOperationException();
            }
            final Object fieldValue = this.fieldWriter.getFieldValue(rootObject);
            final Object fieldValueApply = callback.apply(rootObject, fieldValue);
            if (fieldValueApply == fieldValue) {
                return;
            }
            if (this.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.fieldReader.accept(rootObject, fieldValueApply);
        }
        
        @Override
        public void setInt(final Object rootObject, final int value) {
            if (this.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.fieldReader.accept(rootObject, value);
        }
        
        @Override
        public void setLong(final Object rootObject, final long value) {
            if (this.fieldReader == null) {
                throw new UnsupportedOperationException();
            }
            this.fieldReader.accept(rootObject, value);
        }
        
        @Override
        public boolean remove(final Object rootObject) {
            throw new UnsupportedOperationException();
        }
    }
}
